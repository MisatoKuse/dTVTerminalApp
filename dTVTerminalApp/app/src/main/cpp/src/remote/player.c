/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <du_http_client.h>
#include <du_byte.h>
#include <du_sync.h>
#include <du_csv.h>
#include <du_file.h>
#include <du_alloc.h>
#include <du_str.h>
#include <du_mime_type.h>
#include <du_base64.h>
#include <du_log.h>
#include "player.h"
#include "dmp.h"

#include <ddtcp_sink.h>
#include <ddtcp_plus_sink.h>
#include <ddtcp_util_http.h>
#include <hwif_aux.h>
#include <ddtcp_plus.h>

#include <stdio.h>

#define HTTP_CLIENT_IN_BUF_SIZE 1024
#define HTTP_CLIENT_CONNECT_TIMEOUT_MS 10000
#define HTTP_CLIENT_WRITE_TIMEOUT_MS 10000
#define HTTP_CLIENT_READ_TIMEOUT_MS 30000

void player_position_info_init(player_position_info* info) {
    du_byte_zero((du_uint8*)info, sizeof(player_position_info));

    du_uchar_array_init(&info->uri);
}

void player_position_info_free(player_position_info* info) {
    du_uchar_array_free(&info->uri);
}

void player_position_info_reset(player_position_info* info) {
    player_position_info_free(info);
    player_position_info_init(info);
}

du_bool player_set_state_change_handler(player* p, player_state_change_handler handler, void* handler_arg) {
    du_uint32 len;
    du_uint32 pos;

    len = du_ptr_array_length(&p->_state_change_handler_array);
    pos = du_ptr_array_find(&p->_state_change_handler_array, handler);

    if (len == pos) {
        if (!du_ptr_array_cato(&p->_state_change_handler_array, handler)) {du_log_mark_w(0); return 0;}
        if (!du_ptr_array_cato(&p->_state_change_handler_array, handler_arg)) {du_log_mark_w(0); return 0;}
    }

    return 1;
}

static du_bool change_state(player* p, player_state state, player_status status) {
    du_uint32 i;
    du_uint32 len;
    void** handler;

    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); goto error;}

    p->_state = state;
    p->_status = status;

    du_sync_notify(&p->_action_sync);
    du_mutex_unlock(&p->_thread_mutex);

    len = du_ptr_array_length(&p->_state_change_handler_array);
    handler = du_ptr_array_get(&p->_state_change_handler_array);
    for (i = 0; i < len; i += 2) {
        ((player_state_change_handler)handler[i])(state, status, handler[i + 1]);
    }

    return 1;

error:
    du_sync_notify(&p->_action_sync);
    {du_log_mark_w(0); return 0;}
}

static du_bool get_current_action(player* p, player_action* action) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}
    *action = p->_action;
    p->_action = PLAYER_ACTION_NONE;
    du_mutex_unlock(&p->_thread_mutex);
    return 1;
}

static du_bool set_action(player* p, player_action action) {
    if (p->_action != PLAYER_ACTION_NONE) {du_log_mark_w(0); return 0;}
    p->_action = action;
    du_sync_notify(&p->_thread_sync);
    return 1;
}

static du_bool start_http_connection(player* p, player_action action, du_str_array* request_header, du_str_array* response_header) {
    du_uchar status[4];
    const du_uchar* uri;

    if (!p->_disconnect) return 1;

    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    if (action == PLAYER_ACTION_DOWNLOAD) {
        uri = player_media_info_get_original_uri(&p->_media_info);
        if (!dav_http_header_set_transfer_mode(request_header, DAV_HTTP_TRANSFER_MODE_BACKGROUND, 1)) {du_log_mark_w(0); goto error;}
    } else {
        uri = player_media_info_get_uri(&p->_media_info);
        if (!dav_http_header_set_transfer_mode(request_header, player_media_info_get_transfer_mode(&p->_media_info), 1)) {du_log_mark_w(0); goto error;}
    }
    if (!uri) {du_log_mark_w(0); goto error;}
    if (!du_http_client_set_uri(&p->_hc, uri)) {du_log_mark_w(0); goto error;}
    if (!du_http_header_set_str(request_header, du_http_header_connection(), DU_UCHAR_CONST("close"), 1)) {du_log_mark_w(0); goto error;}

    if (!du_http_client_request(&p->_hc, du_http_method_get(), request_header, 0, status, response_header, HTTP_CLIENT_CONNECT_TIMEOUT_MS, HTTP_CLIENT_WRITE_TIMEOUT_MS, HTTP_CLIENT_READ_TIMEOUT_MS)) {du_log_mark_w(0); goto error;}
    if (!du_http_status_is_successful(status)) {du_log_mark_w(0); goto error;}
    p->_disconnect = 0;

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

static void end_http_connection(player* p) {
    if (p->_disconnect) return;

    du_http_client_cancel(&p->_hc);
    du_http_client_disconnect(&p->_hc);
    p->_nbytes_read = 0;
    p->_disconnect = 1;
}

static du_bool check_action(player* p, player_action action) {
    switch (action) {
    case PLAYER_ACTION_PAUSE:
        if (!change_state(p, PLAYER_STATE_PAUSED_PLAYBACK, PLAYER_STATUS_OK)) {du_log_mark_w(0); return 0;}
        break;
    case PLAYER_ACTION_NONE:
        end_http_connection(p);
        if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) {du_log_mark_w(0); return 0;}
        break;
    case PLAYER_ACTION_STOP:
    case PLAYER_ACTION_TERMINATE:
        end_http_connection(p);
        if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) {du_log_mark_w(0); return 0;}
        break;
    default:
        {du_log_mark_w(0); return 0;}
    }
    return 1;
}

static du_bool play_media(player* p, player_action* action) {
    du_str_array request_header;
    du_str_array response_header;
    du_uchar buf[1024];
    du_uint32 nbytes;
    du_bool playing = 0;

    du_str_array_init(&request_header);
    du_str_array_init(&response_header);

    if (!start_http_connection(p, PLAYER_ACTION_PLAY, &request_header, &response_header)) goto temporary_error;

    for (;;) {
        if (!get_current_action(p, action)) {du_log_mark_w(0); goto error;}
        if (*action != PLAYER_ACTION_NONE && *action != PLAYER_ACTION_PLAY) break;

        if (!du_http_client_read_body(&p->_hc, buf, sizeof buf, HTTP_CLIENT_READ_TIMEOUT_MS, &nbytes)) goto temporary_error;
        if (!playing) {
            if (!change_state(p, PLAYER_STATE_PLAYING, PLAYER_STATUS_OK)) {du_log_mark_w(0); goto error;}
            playing = 1;
        }
        if (!nbytes) break;
        p->_nbytes_read += nbytes;
        printf("%d bytes read\n", p->_nbytes_read);
    }

    if (!check_action(p, *action)) {du_log_mark_w(0); goto error;}
    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    return 1;

temporary_error:
    end_http_connection(p);
    if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_ERROR_OCCURRED)) {du_log_mark_w(0); return 0;}
    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    return 1;

error:
    end_http_connection(p);
    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    {du_log_mark_w(0); return 0;}
}

typedef struct ake_handler_info {
    du_sync sync;
    du_mutex mutex;
    du_bool error_occurred;
} ake_handler_info;

static du_bool ake_handler_info_create(ake_handler_info* info) {
    if (!du_sync_create(&info->sync)) {du_log_mark_w(0); return 0;}
    if (!du_mutex_create(&info->mutex)) {du_log_mark_w(0); goto error;}
    info->error_occurred = 0;
    return 1;

error:
    du_sync_free(&info->sync);
    {du_log_mark_w(0); return 0;}
}

static void ake_handler_info_free(ake_handler_info* info) {
    du_sync_free(&info->sync);
    du_mutex_free(&info->mutex);
}

static ddtcp_ret ake_end_handler(ddtcp_ret status, ddtcp_sink_ake ake, void* arg) {
    ake_handler_info* info = (ake_handler_info*)arg;

    if (DDTCP_FAILED(status)) {du_log_mark_w(0); goto error;}
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    return status;

error:
    printf("ake error status=0x%x\n", status);
    info->error_occurred = 1;
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    return status;
}
//#ifdef ENABLE_DTCP
static du_bool do_ake(player* p, ddtcp_sink_ake* ake, ake_handler_info* hinfo, du_bool move, ddtcp_sink_mv_end_handler mvend_handler) {
    du_uint16 dtcp1raport;

    du_mutex_lock(&hinfo->mutex);
    du_mutex_lock(&p->_thread_mutex);

    dtcp1raport = player_media_info_get_dtcp1raport(&p->_media_info);

    if (move) {
        if (DDTCP_FAILED(ddtcp_sink_mv_do_ake(p->_dtcp, player_media_info_get_dtcp1host(&p->_media_info), player_media_info_get_dtcp1port(&p->_media_info), ake_end_handler, hinfo, mvend_handler, hinfo, ake))) {du_log_mark_w(0); goto error;}
    } else {
        if (dtcp1raport) {
            if (DDTCP_FAILED(ddtcp_sink_do_ra_ake(p->_dtcp, player_media_info_get_dtcp1host(&p->_media_info), dtcp1raport, ake_end_handler, hinfo, ake))) {du_log_mark_w(0); goto error;}
        } else {
            if (DDTCP_FAILED(ddtcp_sink_do_ake(p->_dtcp, player_media_info_get_dtcp1host(&p->_media_info), player_media_info_get_dtcp1port(&p->_media_info), ake_end_handler, hinfo, ake))) {du_log_mark_w(0); goto error;}
        }
    }

    du_mutex_unlock(&p->_thread_mutex);
    du_sync_wait(&hinfo->sync, &hinfo->mutex);
    du_mutex_unlock(&hinfo->mutex);
    if (hinfo->error_occurred) {du_log_mark_w(0); goto error;}
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    du_mutex_unlock(&hinfo->mutex);
    {du_log_mark_w(0); return 0;}
}

static du_bool start_http_connection_dtcp(player* p, player_action action, du_bool move, du_bool remote, ddtcp_sink_stream* stream, ddtcp_sink_ake ake) {
    du_str_array request_header;
    du_str_array response_header;

    du_str_array_init(&request_header);
    du_str_array_init(&response_header);

    if (move && !remote) {
        du_uint8 kxm_label;
        du_uchar strnum[DU_STR_FMT_SIZE];

        if (DDTCP_FAILED(ddtcp_sink_mv_get_kxm_label(ake, &kxm_label))) {du_log_mark_w(0); goto error;}
        strnum[du_str_fmt_xint16(strnum, (du_uint16)kxm_label)] = 0;
        if (!du_http_header_set_str(&request_header, ddtcp_util_http_header_blkmove_dtcp_com(), strnum, 1)) {du_log_mark_w(0); goto error;}
        if (!start_http_connection(p, action, &request_header, &response_header)) {du_log_mark_w(0); goto error;}

        if (DDTCP_FAILED(ddtcp_sink_mv_create_http_stream(ake, stream))) goto error2;
    } else if (!move && !remote) {
        if (!start_http_connection(p, action, &request_header, &response_header)) {du_log_mark_w(0); goto error;}

        if (DDTCP_FAILED(ddtcp_sink_create_http_stream(ake, stream))) goto error2;
    } else if (!move && remote) {
        du_uint8 kr_label;
        du_uchar strnum[DU_STR_FMT_SIZE];

        if (DDTCP_FAILED(ddtcp_sink_ra_get_kr_label(ake, &kr_label))) {du_log_mark_w(0); goto error;}
        strnum[du_str_fmt_xint16(strnum, (du_uint16)kr_label)] = 0;
        if (!du_http_header_set_str(&request_header, ddtcp_util_http_header_remoteaccess_dtcp_com(), strnum, 0)) {du_log_mark_w(0); goto error;}

        if (!start_http_connection(p, action, &request_header, &response_header)) {du_log_mark_w(0); goto error;}
        if (DDTCP_FAILED(ddtcp_sink_create_http_stream(ake, stream))) goto error2;
    } else {
        du_log_w(0, DU_UCHAR_CONST("not supported request"));
        {du_log_mark_w(0); goto error;}
    }

    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    return 1;

error2:
    end_http_connection(p);
error:
    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    {du_log_mark_w(0); return 0;}
}

static du_bool end_http_connection_dtcp(player* p, du_bool move, ddtcp_sink_stream* stream) {
    if (move) {
        if (DDTCP_FAILED(ddtcp_sink_mv_destroy_stream(stream))) {du_log_mark_w(0); goto error;}

    } else {
        if (DDTCP_FAILED(ddtcp_sink_destroy_stream(stream))) {du_log_mark_w(0); goto error;}
    }
    end_http_connection(p);
    return 1;

error:
    end_http_connection(p);
    {du_log_mark_w(0); return 0;}
}

static ddtcp_ret ra_management_end_handler(ddtcp_ret status, ddtcp_sink_ake ake, void* arg) {
    ake_handler_info* info = (ake_handler_info*)arg;

    printf("ra_management end status=0x%x\n", status);
    if (DDTCP_FAILED(status)) {du_log_mark_w(0); goto error;}
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    return status;

error:
    info->error_occurred = 1;
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    return status;
}


static du_bool do_ra_management(ddtcp_sink_ake ake, ake_handler_info* hinfo) {
    ddtcp_ret ret = DDTCP_RET_SUCCESS;

    du_mutex_lock(&hinfo->mutex);

    if (DDTCP_FAILED(ret = ddtcp_sink_do_ra_management(ake, ra_management_end_handler, hinfo, 1))) {du_log_mark_w(0); {du_log_mark_w(0); goto error;}}
    du_sync_wait(&hinfo->sync, &hinfo->mutex);

    du_mutex_unlock(&hinfo->mutex);
    if (hinfo->error_occurred) {du_log_mark_w(0); {du_log_mark_w(0); return 0;}}
    return 1;

error:
    du_mutex_unlock(&hinfo->mutex);
    {du_log_mark_w(0); return 0;}
}

#define BUF_SIZE 16384

static du_bool play_media_by_dtcp(player* p, player_action* action) {
    ddtcp_sink_stream stream = 0;
    du_uint8* packetized = 0;
    du_uint8* clear = 0;
    du_uint32 clear_size = 0;
    du_uint32 processed;
    du_uint32 remain = 0;
    du_uint32 nbytes;
    ddtcp_sink_ake ake = 0;
    ake_handler_info hinfo;
    du_bool completed = 0;
    du_bool playing = 0;
    du_bool remote = 0;
#if 1   // XXX: test
    FILE* fp = fopen("play.bin", "wb");
#endif

    packetized = du_alloc(BUF_SIZE);
    if (!packetized) {du_log_mark_w(0); goto error;}
    clear = du_alloc(BUF_SIZE);
    if (!clear) {du_log_mark_w(0); goto error;}

    if (player_media_info_get_dtcp1raport(&p->_media_info)) {
        remote = 1;
    }

    if (!ake_handler_info_create(&hinfo)) {du_log_mark_w(0); goto error;}

    if (!do_ake(p, &ake, &hinfo, 0, 0)) goto temporary_error;
    if (!start_http_connection_dtcp(p, PLAYER_ACTION_PLAY, 0, remote, &stream, ake)) goto temporary_error;

    while (!completed) {
        if (!get_current_action(p, action)) goto error2;
        if (*action != PLAYER_ACTION_NONE && *action != PLAYER_ACTION_PLAY) break;

        if (!du_http_client_read_body(&p->_hc, packetized + remain, BUF_SIZE - remain, HTTP_CLIENT_READ_TIMEOUT_MS ,&nbytes)) goto temporary_error;
        if (!playing) {
            if (!change_state(p, PLAYER_STATE_PLAYING, PLAYER_STATUS_OK)) goto error2;
            playing = 1;
        }
        if (!nbytes) {
            completed = 1;
        }

        clear_size = BUF_SIZE;
        if (DDTCP_FAILED(ddtcp_sink_depacketize(stream, packetized, nbytes + remain, clear, &clear_size, &processed))) goto error2;
        remain = nbytes + remain - processed;
        du_byte_copy(packetized, remain, packetized + processed);

        p->_nbytes_read += clear_size;

#if 1   // XXX: test
        fwrite(clear, clear_size, 1, fp);
        fflush(fp);
#endif
        printf("%d bytes read\n", p->_nbytes_read);
    }

    if (remote) {
        if (!do_ra_management(ake, &hinfo)) {du_log_mark_w(0);};
    }

    if (!check_action(p, *action)) goto error2;

#if 1   // XXX: test
    fclose(fp);
#endif
    end_http_connection_dtcp(p, 0, &stream);
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
    du_alloc_free(packetized);
    du_alloc_free(clear);
    return 1;

temporary_error:
    if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_ERROR_OCCURRED)) goto error2;
    end_http_connection_dtcp(p, 0, &stream);
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
    du_alloc_free(packetized);
    du_alloc_free(clear);
#if 1   // XXX: test
    fclose(fp);
#endif
    return 1;

error2:
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
error:
    end_http_connection_dtcp(p, 0, &stream);
    ake_handler_info_free(&hinfo);
    du_alloc_free(packetized);
    du_alloc_free(clear);
#if 1   // XXX: test
    fclose(fp);
#endif
    {du_log_mark_w(0); return 0;}
}
//#endif

static const du_uchar* get_suffix_by_mime_type(const du_uchar* mime_type) {
    if (du_mime_type_major_equal(mime_type, DU_UCHAR_CONST("video"))) {
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("mpeg"))) return DU_UCHAR_CONST("mpg");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("vnd.dlna.mpeg-tts"))) return DU_UCHAR_CONST("tts");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("x-ms-wmv"))) return DU_UCHAR_CONST("wmv");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("mp4"))) return DU_UCHAR_CONST("mp4");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("3gpp"))) return DU_UCHAR_CONST("3gp");
    } else if (du_mime_type_major_equal(mime_type, DU_UCHAR_CONST("audio"))) {
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("x-ms-wma"))) return DU_UCHAR_CONST("wma");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("mpeg"))) return DU_UCHAR_CONST("mp3");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("mp4"))) return DU_UCHAR_CONST("m4a");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("wav"))) return DU_UCHAR_CONST("wav");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("L16"))) return DU_UCHAR_CONST("lpcm");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("3gpp"))) return DU_UCHAR_CONST("3gp");
    } else if (du_mime_type_major_equal(mime_type, DU_UCHAR_CONST("image"))) {
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("jpeg"))) return DU_UCHAR_CONST("jpg");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("png"))) return DU_UCHAR_CONST("png");
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("bmp"))) return DU_UCHAR_CONST("bmp");
    } else if (du_mime_type_major_equal(mime_type, DU_UCHAR_CONST("application"))) {
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("ogg"))) return DU_UCHAR_CONST("ogg");
//#ifdef ENABLE_DTCP
        if (du_mime_type_sub_equal(mime_type, DU_UCHAR_CONST("x-dtcp1"))) return DU_UCHAR_CONST("tts");
//#endif
    }
    {du_log_mark_w(0); return 0;}
}

static du_bool get_fn(player* p, du_uchar_array* fn) {
    const du_uchar* title;
    const du_uchar* suffix;
    du_uint16 i;
    du_uint32 len;
    du_uchar strnum[DU_STR_FMT_SIZE];
    du_file_status fs;

    if (!p->_download_dir) {du_log_mark_w(0); return 0;}
    if (du_str_len(p->_download_dir)) {
        du_uchar_array_cats(fn, p->_download_dir);
        if (!du_str_end(p->_download_dir, DU_UCHAR_CONST(DU_FILE_SEPARATOR))) {
            du_uchar_array_cato(fn, du_file_separator());
        }
    }

    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    title = player_media_info_get_title(&p->_media_info);
    suffix = get_suffix_by_mime_type(player_media_info_get_original_mime_type(&p->_media_info));
    if (!suffix) {du_log_mark_w(0); goto error;}

    du_uchar_array_cats(fn, title);
    len = du_uchar_array_length(fn);

    for (i = 0; i < DU_UINT16_MAX; ++i) {
        du_uchar_array_cato(fn, '.');
        du_uchar_array_cats(fn, suffix);
        du_uchar_array_cat0(fn);
        if (du_uchar_array_failed(fn)) {du_log_mark_w(0); goto error;}

        if (du_file_get_status(du_uchar_array_get(fn), &fs)) goto temporary_error;
        break;

temporary_error:
        du_uchar_array_truncate_length(fn, len);
        strnum[du_str_fmt_uint16(strnum, i)] = 0;
        du_uchar_array_cato(fn, '(');
        du_uchar_array_cats(fn, strnum);
        du_uchar_array_cato(fn, ')');
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

static du_bool save_media(player* p, player_action* action) {
    du_str_array request_header;
    du_str_array response_header;
    du_file f;
    du_uchar_array tmp;
    const du_uchar* fn = 0;
    du_uchar buf[1024];
    du_uint32 nbytes;
    du_bool downloading = 0;
    du_bool completed = 0;

    du_uchar_array_init(&tmp);
    du_str_array_init(&request_header);
    du_str_array_init(&response_header);

    if (!get_fn(p, &tmp)) goto temporary_error;
    fn = du_uchar_array_get(&tmp);
    if (!du_file_open_create(&f, fn)) goto temporary_error;

    if (!start_http_connection(p, PLAYER_ACTION_DOWNLOAD, &request_header, &response_header)) goto temporary_error;

    for (;;) {
        if (!get_current_action(p, action)) {du_log_mark_w(0); goto error;}
        if (*action != PLAYER_ACTION_NONE && *action != PLAYER_ACTION_DOWNLOAD) break;

        if (!du_http_client_read_body(&p->_hc, buf, sizeof buf, HTTP_CLIENT_READ_TIMEOUT_MS, &nbytes)) goto temporary_error2;
        if (!downloading) {
            if (!change_state(p, PLAYER_STATE_DOWNLOADING, PLAYER_STATUS_OK)) {du_log_mark_w(0); goto error;}
            downloading = 1;
        }
        if (!nbytes) {
            puts("download completed.");
            completed = 1;
            if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) {du_log_mark_w(0); goto error;}
            break;
        }
        p->_nbytes_read += nbytes;
        if (!du_file_write_all(f, buf, nbytes)) goto temporary_error2;
        printf("%d bytes saved\n", p->_nbytes_read);
    }

    du_file_flush(f);
    du_file_close(f);
    if (completed) {
        puts("download completed.");
    } else {
        du_file_remove(fn);
    }
    if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) {du_log_mark_w(0); goto error;}

    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    du_uchar_array_free(&tmp);
    end_http_connection(p);
    return 1;

temporary_error2:
    du_file_flush(f);
    du_file_close(f);
    du_file_remove(fn);
temporary_error:
    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    du_uchar_array_free(&tmp);
    if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_ERROR_OCCURRED)) {du_log_mark_w(0); return 0;}
    end_http_connection(p);
    return 1;

error:
    du_file_flush(f);
    du_file_close(f);
    du_file_remove(fn);
    du_str_array_free(&request_header);
    du_str_array_free(&response_header);
    du_uchar_array_free(&tmp);
    end_http_connection(p);
    {du_log_mark_w(0); return 0;}
}

static ddtcp_ret mv_end_handler(ddtcp_ret status, ddtcp_sink_ake ake, void* arg) {
    ake_handler_info* info = (ake_handler_info*)arg;

    printf("mv end status=0x%x\n", status);
    if (DDTCP_FAILED(status)) {du_log_mark_w(0); goto error;}
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    return status;

 error:
    info->error_occurred = 1;
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    return status;
}

static ddtcp_ret make_content_usable_handler(void* arg) {
    const du_uchar* path = (const du_uchar*)arg;

    printf("Make content USABLE\n");
    printf("path=%s\n", path);
    return DDTCP_RET_SUCCESS;
}

static ddtcp_ret make_content_discard_handler(void* arg) {
    const du_uchar* path = (const du_uchar*)arg;

    printf("Make content DISCARD\n");
    printf("path=%s\n", path);
    du_file_remove(path);
    return DDTCP_RET_SUCCESS;
}

static du_bool mv_commitment(ddtcp_sink_ake ake, const du_uchar* content_path) {
    ddtcp_ret ret = DDTCP_RET_SUCCESS;

    if (DDTCP_FAILED(ret = ddtcp_sink_mv_set_make_content_usable_handler(ake, make_content_usable_handler, (void*)content_path))) {du_log_mark_w(0); goto error;}
    if (DDTCP_FAILED(ret = ddtcp_sink_mv_set_make_content_discard_handler(ake, make_content_discard_handler, (void*)content_path))) {du_log_mark_w(0); goto error;}
    if (DDTCP_FAILED(ret = ddtcp_sink_mv_do_commitment(ake))) {du_log_mark_w(0); goto error;}
    return 1;

error:
    printf("mv_commitment error ret=0x%x\n", ret);
    {du_log_mark_w(0); return 0;}
}

static du_bool confirm_move(pmi_additional_mm_flags_param fp, du_bool* move) {
    *move = 0;

    switch (fp) {
    case PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_COPY:
        break;
    case PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_MOVE:
        *move = 1;
        break;
    default:
        puts("This content do not support DIS.");
        {du_log_mark_w(0); return 0;}
    }
    return 1;
}

static du_bool do_mv_commitment(ddtcp_sink_ake ake, ake_handler_info* hinfo, const du_uchar* fn) {
    du_mutex_lock(&hinfo->mutex);
    if (!mv_commitment(ake, fn)) {
        du_mutex_unlock(&hinfo->mutex);
        {du_log_mark_w(0); return 0;}
    }
    du_sync_wait(&hinfo->sync, &hinfo->mutex);
    du_mutex_unlock(&hinfo->mutex);
    if (hinfo->error_occurred) {du_log_mark_w(0); return 0;};
    return 1;
}

//#ifdef ENABLE_DTCP
static du_bool save_media_by_dtcp(player* p, player_action* action) {
    ddtcp_sink_stream stream = 0;
    ddtcp_sink_ake ake = 0;
    du_bool move = 0;
    du_uint8* packetized = 0;
    du_uint8* clear = 0;
    du_uint32 clear_size = 0;
    du_uint32 processed;
    du_uint32 remain = 0;
    du_uint32 nbytes;
    ake_handler_info hinfo;
    du_file f;
    du_uchar_array tmp;
    const du_uchar* fn = 0;
    du_bool downloading = 0;
    du_bool completed = 0;
    pmi_additional_mm_flags_param mm_flags;

    du_uchar_array_init(&tmp);
    packetized = du_alloc(BUF_SIZE);
    if (!packetized) {du_log_mark_w(0); goto error;}
    clear = du_alloc(BUF_SIZE);
    if (!clear) {du_log_mark_w(0); goto error;}
    if (!ake_handler_info_create(&hinfo)) {du_log_mark_w(0); goto error;}

    du_mutex_lock(&p->_thread_mutex);
    mm_flags = player_media_info_get_additional_mm_flags_param(&p->_media_info);
    du_mutex_unlock(&p->_thread_mutex);
    if (!confirm_move(mm_flags, &move)) goto temporary_error;

    if (!get_fn(p, &tmp)) goto temporary_error;
    fn = du_uchar_array_get(&tmp);
    if (!du_file_open_create(&f, fn)) goto temporary_error;

    if (!do_ake(p, &ake, &hinfo, move, mv_end_handler)) goto temporary_error2;
    if (!start_http_connection_dtcp(p, PLAYER_ACTION_DOWNLOAD, move, 0, &stream, ake)) goto temporary_error2;

    while (!completed) {
        if (!get_current_action(p, action)) goto error3;
        if (*action != PLAYER_ACTION_NONE && *action != PLAYER_ACTION_DOWNLOAD) break;

        if (!du_http_client_read_body(&p->_hc, packetized + remain, BUF_SIZE - remain, HTTP_CLIENT_READ_TIMEOUT_MS ,&nbytes)) goto temporary_error2;
        if (!downloading) {
            if (!change_state(p, PLAYER_STATE_DOWNLOADING, PLAYER_STATUS_OK)) goto error3;
            downloading = 1;
        }
        if (!nbytes) {
            completed = 1;
        }

        clear_size = BUF_SIZE;
        if (DDTCP_FAILED(ddtcp_sink_depacketize(stream, packetized, nbytes + remain, clear, &clear_size, &processed))) goto error3;
        remain = nbytes + remain - processed;
        du_byte_copy(packetized, remain, packetized + processed);

        if (!du_file_write_all(f, clear, clear_size)) goto temporary_error2;
        p->_nbytes_read += clear_size;
        printf("%d bytes read\n", p->_nbytes_read);
    }
    if (!completed) goto temporary_error2;
    du_file_flush(f);
    du_file_close(f);

    if (!end_http_connection_dtcp(p, move, &stream)) goto temporary_error2;
    if (!do_mv_commitment(ake, &hinfo, fn)) goto temporary_error;

    puts("download completed.");
    if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) {du_log_mark_w(0); goto error;}

    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
    du_uchar_array_free(&tmp);
    du_alloc_free(packetized);
    du_alloc_free(clear);
    return 1;

temporary_error2:
    du_file_flush(f);
    du_file_close(f);
temporary_error:
    if (fn) du_file_remove(fn);
    if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_ERROR_OCCURRED)) goto error2;
    end_http_connection_dtcp(p, move, &stream);
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
    du_uchar_array_free(&tmp);
    du_alloc_free(packetized);
    du_alloc_free(clear);
    return 1;

error3:
    du_file_flush(f);
    du_file_close(f);
error2:
    if (fn) du_file_remove(fn);
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
error:
    du_uchar_array_free(&tmp);
    du_alloc_free(packetized);
    du_alloc_free(clear);
    end_http_connection_dtcp(p, move, &stream);
    {du_log_mark_w(0); return 0;}
}
//#endif

static du_bool wait_action(player* p, player_action* action) {
    du_bool r;

    if (*action == PLAYER_ACTION_TERMINATE) {du_log_mark_w(0); return 0;}
    if (!get_current_action(p, action)) {du_log_mark_w(0); return 0;}
    if (*action != PLAYER_ACTION_NONE) return 1;

    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}
    r = du_sync_wait(&p->_thread_sync, &p->_thread_mutex);
    du_mutex_unlock(&p->_thread_mutex);
    if (!r) {du_log_mark_w(0); return 0;}

    if (!get_current_action(p, action)) {du_log_mark_w(0); return 0;}
    return 1;
}

static void player_thread(void* arg) {
    player* p = (player*)arg;
    player_action action = PLAYER_ACTION_NONE;;

    for (;;) {
        if (!wait_action(p, &action)) break;

        switch (action) {
        case PLAYER_ACTION_SET_MEDIA:
            if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) return;
            break;

        case PLAYER_ACTION_RESET_MEDIA:
            if (!change_state(p, PLAYER_STATE_NO_MEDIA_PRESENT, PLAYER_STATUS_OK)) return;
            break;

        case PLAYER_ACTION_PLAY:
            if (!change_state(p, PLAYER_STATE_TRANSITIONING, PLAYER_STATUS_OK)) return;
#ifndef ENABLE_DTCP
            if (!play_media(p, &action)) return;
#else
            {
                du_bool dtcp;

                du_mutex_lock(&p->_thread_mutex);
                dtcp = player_media_info_is_dtcp(&p->_media_info);
                du_mutex_unlock(&p->_thread_mutex);
                if (dtcp) {
                    if (!play_media_by_dtcp(p, &action)) return;
                } else {
                    if (!play_media(p, &action)) return;
                }
            }
#endif
            break;

        case PLAYER_ACTION_DOWNLOAD:
            if (!change_state(p, PLAYER_STATE_TRANSITIONING, PLAYER_STATUS_OK)) return;
#ifndef ENABLE_DTCP
            if (!save_media(p, &action)) return;
#else
            {
                du_bool dtcp;

                du_mutex_lock(&p->_thread_mutex);
                dtcp = player_media_info_is_dtcp(&p->_media_info);
                du_mutex_unlock(&p->_thread_mutex);
                if (dtcp) {
                    if (!save_media_by_dtcp(p, &action)) return;
                } else {
                    if (!save_media(p, &action)) return;
                }
            }
#endif
            break;

        case PLAYER_ACTION_PAUSE:
            if (!change_state(p, PLAYER_STATE_PAUSED_PLAYBACK, PLAYER_STATUS_OK)) return;
            break;

        case PLAYER_ACTION_STOP:
            end_http_connection(p);
            if (!change_state(p, PLAYER_STATE_STOPPED, PLAYER_STATUS_OK)) return;
            break;

        case PLAYER_ACTION_TERMINATE:
            return;

        default:
            break;
        }
    }
}

static du_bool thread_start(player* p) {
    return du_thread_create(&p->_thread, p->_stack_size, player_thread, p);
}

static du_bool thread_stop(player* p) {
    set_action(p, PLAYER_ACTION_TERMINATE);
    du_thread_join(&p->_thread);

    return 1;
}

du_bool player_action_play(player* p) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}
    switch (p->_state) {
    case PLAYER_STATE_PLAYING:
        break;

    case PLAYER_STATE_STOPPED:
    case PLAYER_STATE_PAUSED_PLAYBACK:
        if (!set_action(p, PLAYER_ACTION_PLAY)) {du_log_mark_w(0); goto error;}
        break;

    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_action_download(player* p) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}
    switch (p->_state) {
    case PLAYER_STATE_DOWNLOADING:
        break;

    case PLAYER_STATE_STOPPED:
    case PLAYER_STATE_PAUSED_PLAYBACK:
        if (!set_action(p, PLAYER_ACTION_DOWNLOAD)) {du_log_mark_w(0); goto error;}
        break;

    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_action_pause(player* p) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    if (!player_media_info_is_connection_stalling_supported(&p->_media_info)) {du_log_mark_w(0); goto error;}

    switch (p->_state) {
    case PLAYER_STATE_PLAYING:
        if (!set_action(p, PLAYER_ACTION_PAUSE)) {du_log_mark_w(0); goto error;}
        if (!du_sync_timedwait(&p->_action_sync, &p->_thread_mutex, ACTION_TIMEOUT_MS)) {du_log_mark_w(0); goto error;}
        if (p->_state != PLAYER_STATE_PAUSED_PLAYBACK) {du_log_mark_w(0); goto error;}
        break;

    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_action_stop(player* p) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    switch (p->_state) {
    case PLAYER_STATE_STOPPED:
        break;

    case PLAYER_STATE_PLAYING:
    case PLAYER_STATE_DOWNLOADING:
    case PLAYER_STATE_TRANSITIONING:
    case PLAYER_STATE_PAUSED_PLAYBACK:
        if (!set_action(p, PLAYER_ACTION_STOP)) {du_log_mark_w(0); goto error;}
        if (!du_sync_timedwait(&p->_action_sync, &p->_thread_mutex, ACTION_TIMEOUT_MS)) {du_log_mark_w(0); goto error;}
        if (p->_state != PLAYER_STATE_STOPPED) {du_log_mark_w(0); goto error;}
        break;

    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_action_seek(player* p, du_uint32 target_time_ms) {
    {du_log_mark_w(0); return 0;}
}

static du_bool set_media(player* p, const du_uchar* uri, dav_didl_object* object) {
    if (!player_media_info_set_property(&p->_media_info, uri, object)) {du_log_mark_w(0); return 0;}
    if (!set_action(p, PLAYER_ACTION_SET_MEDIA)) {du_log_mark_w(0); return 0;}
    if (!du_sync_timedwait(&p->_action_sync, &p->_thread_mutex, ACTION_TIMEOUT_MS)) {du_log_mark_w(0); return 0;}
    if (p->_state != PLAYER_STATE_STOPPED) {du_log_mark_w(0); return 0;}

    return 1;
}

du_bool player_set_media(player* p, const du_uchar* uri, dav_didl_object* object) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    switch (p->_state) {
    case PLAYER_STATE_NO_MEDIA_PRESENT:
    case PLAYER_STATE_STOPPED:
        if (!set_media(p, uri, object)) {du_log_mark_w(0); goto error;}
        break;

    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_reset_media(player* p) {
    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    switch (p->_state) {
    case PLAYER_STATE_STOPPED:
        player_media_info_reset(&p->_media_info);
        if (!set_action(p, PLAYER_ACTION_RESET_MEDIA)) {du_log_mark_w(0); return 0;}
        if (!du_sync_timedwait(&p->_action_sync, &p->_thread_mutex, ACTION_TIMEOUT_MS)) {du_log_mark_w(0); return 0;}
        if (p->_state != PLAYER_STATE_NO_MEDIA_PRESENT) {du_log_mark_w(0); return 0;}
    case PLAYER_STATE_NO_MEDIA_PRESENT:
        break;

    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_get_position_info(player* p, player_position_info* info) {
    const du_uchar* uri;

    player_position_info_reset(info);

    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    // info->current_position_ms is not supported in this program.

    info->is_duration_ms_available = 1;
    info->duration_ms = player_media_info_get_duration(&p->_media_info);

    uri = player_media_info_get_uri(&p->_media_info);
    if (uri) {
        if (!du_uchar_array_copys(&info->uri, uri)) {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_sort_resource(player* p, dav_didl_object* object) {
    dav_capability_class cls;

    if (!dav_capability_get_media_class_from_object(p->_cap, object, &cls)) {du_log_mark_w(0); return 0;}
    if (!dav_capability_sort(p->_cap, object, cls)) {du_log_mark_w(0); return 0;}
    return 1;
}

du_bool player_is_supported(player* p, dav_didl_object* object) {
    dav_capability_class cls;

    if (!dav_capability_get_media_class_from_object(p->_cap, object, &cls)) {du_log_mark_w(0); return 0;}
    return dav_capability_is_supported2(p->_cap, object, 0, cls);
}

static void set_available_action(du_uint32* actions, player_action_flag flag) {
    *actions |= (1 << flag);
}

du_bool player_get_current_available_actions(player* p, du_uint32* actions, du_uchar_array* play_speed) {
    du_uchar_array_truncate(play_speed);
    *actions = 0;

    if (!du_mutex_lock(&p->_thread_mutex)) {du_log_mark_w(0); return 0;}

    switch (p->_state) {
    case PLAYER_STATE_PLAYING:
        set_available_action(actions, PLAYR_ACTION_FLAG_PLAY);
        if (player_media_info_get_time_seek_supported(&p->_media_info)) {
            // This play_speed is not supported in this program. If program supported, refer to the following code.
            // if (!du_uchar_array_cats(play_speed, DU_UCHAR_CONST("2,4,8"))) {du_log_mark_w(0); goto error;}
        }
        set_available_action(actions, PLAYR_ACTION_FLAG_STOP);
        if (player_media_info_is_connection_stalling_supported(&p->_media_info)) {
            set_available_action(actions, PLAYR_ACTION_FLAG_PAUSE);
        }
        break;
    case PLAYER_STATE_STOPPED:
//#ifdef ENABLE_DTCP
        if (player_media_info_is_dtcp(&p->_media_info)) {
            pmi_additional_mm_flags_param fp;

            fp = player_media_info_get_additional_mm_flags_param(&p->_media_info);
            if (fp == PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_COPY || fp == PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_MOVE) {
                set_available_action(actions, PLAYR_ACTION_FLAG_DOWNLOAD);
            }
        } else {
            set_available_action(actions, PLAYR_ACTION_FLAG_DOWNLOAD);
        }
//#else
        //set_available_action(actions, PLAYR_ACTION_FLAG_DOWNLOAD);
//#endif
    case PLAYER_STATE_PAUSED_PLAYBACK:
        set_available_action(actions, PLAYR_ACTION_FLAG_PLAY);
        if (player_media_info_get_time_seek_supported(&p->_media_info)) {
            // This play_speed is not supported in this program. If program supported, refer to the following code.
            // if (!du_uchar_array_cats(play_speed, DU_UCHAR_CONST("2,4,8"))) {du_log_mark_w(0); goto error;}
        }
        set_available_action(actions, PLAYR_ACTION_FLAG_STOP);
        break;
    case PLAYER_STATE_DOWNLOADING:
    case PLAYER_STATE_TRANSITIONING:
        set_available_action(actions, PLAYR_ACTION_FLAG_STOP);
    case PLAYER_STATE_NO_MEDIA_PRESENT:
        break;
    default:
        {du_log_mark_w(0); goto error;}
    }

    du_mutex_unlock(&p->_thread_mutex);
    return 1;

error:
    du_mutex_unlock(&p->_thread_mutex);
    {du_log_mark_w(0); return 0;}
}

du_bool player_init(player* p, du_uint32 stack_size, dav_capability* cap, const du_uchar* download_dir) {
    du_byte_zero((du_uint8*)p, sizeof(player));

    player_media_info_init(&p->_media_info);
    du_ptr_array_init(&p->_state_change_handler_array);
    if (!du_mutex_create(&p->_thread_mutex)) {du_log_mark_w(0); goto error;}
    if (!du_sync_create(&p->_thread_sync)) goto error2;
    if (!du_sync_create(&p->_action_sync)) goto error3;
    if (!du_http_client_init(&p->_hc, HTTP_CLIENT_IN_BUF_SIZE, 0)) goto error4;
    p->_stack_size = stack_size;

    p->_cap = cap;
    p->_action = PLAYER_ACTION_NONE;
    p->_state = PLAYER_STATE_NO_MEDIA_PRESENT;
    p->_status = PLAYER_STATUS_OK;
    p->_disconnect = 1;

    if (download_dir) {
        if (!du_str_clone(download_dir, &p->_download_dir)) goto error5;
    }

    if (DDTCP_FAILED(ddtcp_create_dtcp(&p->_dtcp))) goto error5;
    return 1;

error5:
    du_http_client_free(&p->_hc);
error4:
    du_sync_free(&p->_action_sync);
error3:
    du_sync_free(&p->_thread_sync);
error2:
    du_mutex_free(&p->_thread_mutex);
error:
    player_media_info_free(&p->_media_info);
    du_ptr_array_free(&p->_state_change_handler_array);
    du_alloc_free(p->_download_dir);
    {du_log_mark_w(0); return 0;}
}

void player_free(player* p) {
//#ifdef ENABLE_DTCP
    ddtcp_destroy_dtcp(&p->_dtcp);
    du_alloc_free(p->_private_data_home);
//#endif
    du_http_client_free(&p->_hc);
    du_sync_free(&p->_action_sync);
    du_sync_free(&p->_thread_sync);
    du_mutex_free(&p->_thread_mutex);
    player_media_info_free(&p->_media_info);
    du_ptr_array_free(&p->_state_change_handler_array);
    du_alloc_free(p->_download_dir);
}

du_bool player_get_protocol_info(player* p, du_uchar_array* protocol_info) {
    return player_media_info_get_protocol_info(&p->_media_info, protocol_info);
}


du_bool player_set_private_data_home(player* p, const du_uchar* private_data_home) {
    if (!du_str_clone(private_data_home, &p->_private_data_home)) {du_log_mark_w(0); return 0;}
    return 1;
}

du_bool player_start(player* p, JavaVM *vm, jobject objTmp, jmethodID mid) {

    if (!p->_private_data_home) {du_log_mark_w(0); return 0;}
    void *private_data;
    dixim_hwif_private_data_io hwif;
    hwif.private_data_home = (void *) p->_private_data_home;
    hwif.vm = vm;
    hwif.obj = objTmp;
    hwif.mac_address_method_id = mid;
    private_data = &hwif;
    if (DDTCP_FAILED(ddtcp_set_additional_param(p->_dtcp, DDTCP_ADDITINAL_PARAM_TYPE_PRIVATE_DATA_IO, private_data))) {du_log_mark_w(0); return 0;}
    //
    if (DDTCP_FAILED(ddtcp_startup(p->_dtcp))) {du_log_mark_w(0); return 0;}
    return thread_start(p);
}

void player_stop(player* p) {
    thread_stop(p);
    ddtcp_shutdown(p->_dtcp);
}

du_bool player_get_device_id_hash(player* p, du_uchar_array* hash_base64_ecoded) {
    ddtcp_ret ret = DDTCP_RET_SUCCESS;
    du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE];

    if (DDTCP_FAILED(ret = ddtcp_get_device_id_hash(p->_dtcp, device_id_hash))) {du_log_mark_w(0); goto error;}
    if (!du_base64_encode(device_id_hash, DDTCP_CRYPTO_SHA1_DIGEST_SIZE, hash_base64_ecoded)) {du_log_mark_w(0); goto error;}

    return 1;
error:
    printf("get device id hash error ret=0x%x\n", ret);
    {du_log_mark_w(0); return 0;}
}

du_bool player_sink_ra_register(player* p, const du_uchar* dtcp1_host, du_uint16 dtcp1_port) {
    ddtcp_sink_ake ake = 0;
    ake_handler_info hinfo;
    ddtcp_ret ret = DDTCP_RET_SUCCESS;

    if (!ake_handler_info_create(&hinfo)) {du_log_mark_w(0); goto error;}

    du_mutex_lock(&hinfo.mutex);
    if (DDTCP_FAILED(ret = ddtcp_sink_ra_register(p->_dtcp, dtcp1_host, dtcp1_port, ake_end_handler, &hinfo, &ake))) {du_log_mark_w(0); goto error;}
    du_sync_wait(&hinfo.sync, &hinfo.mutex);
    du_mutex_unlock(&hinfo.mutex);
    if (hinfo.error_occurred) {du_log_mark_w(0); goto error;}

     if (DDTCP_FAILED(ret = ddtcp_sink_close_ake(&ake))) {du_log_mark_w(0); goto error;}
    ake_handler_info_free(&hinfo);

    return 1;

error:
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
    printf("ra register error ret=0x%x\n", ret);
    {du_log_mark_w(0); return 0;}

}
