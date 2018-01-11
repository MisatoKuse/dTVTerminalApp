/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifdef WIN32
#include <winsock2.h>
#endif

#include "downloader.h"
#include "dmspu_cipher_file.h"
#include "../DTVTLogger.h"

#include <cipher_file.h>
#include <cipher_file_context_global.h>
#include <media_mpeg.h>
#include <ddtcp_source.h>
#include <ddtcp_sink.h>
#include <ddtcp_util_http.h>
#include <dav_urn.h>
#include <dav_http.h>
#include <dav_content_features.h>
#include <dav_flags_param.h>
#include <dav_didl_object_array.h>
#include <dav_didl_parser_packed.h>
#include <dav_didl_libxml.h>
#include <dav_didl_duration.h>
#include <dav_protocol_info.h>
#include <du_libxml.h>
#include <du_http_client.h>
#include <du_str_array.h>
#include <du_log.h>
#include <du_str.h>
#include <du_ringbuffer.h>
#include <du_byte.h>
#include <du_thread.h>
#include <du_alloc.h>
#include <du_sync.h>
#include <du_elapse.h>
#include <du_param.h>
#include <du_csv.h>
#include <du_file.h>
#include <du_file_input_buffer.h>
#include <du_uuid.h>

#include <libxml/tree.h>


#ifndef LOGW
#define LOGW du_log_mark_w(0)
#endif

    static const du_uchar *LOG_CATEGORY = DU_UCHAR_CONST("downloader");

    static du_bool CANCEL_REQUESTED;
    static ddtcp DTCP;

#define DOWNLOADER_HTTP_CLIENT_CONNECT_TIMEOUT_MS 30000
#define DOWNLOADER_HTTP_CLIENT_READ_TIMEOUT_MS 90000
#define DOWNLOADER_HTTP_CLIENT_WRITE_TIMEOUT_MS 30000
#define DOWNLOADER_HTTP_CLIENT_BUF_SIZE (512 * 1024)

#define DOWNLOADER_RINGBUFFER_SIZE (1024 * 1024)
#define DOWNLOADER_BUF_SIZE (512 * 1024)

#define LOGD du_log_mark_d(0)
#define LOGW du_log_mark_w(0)

    typedef struct ake_handler_info {
        du_sync sync;
        du_mutex mutex;
        du_bool error_occurred;
    } ake_handler_info;

    typedef struct downloader_write_thread_info {
        volatile du_bool completed;
        volatile du_bool error_occurred;
        du_uint32 e_emi;
        du_ringbuffer rb;
        du_mutex mutex;
        cipher_file cf;
        du_bool need_commit;
        du_uint64 total_size;
        du_uint64 wrote_size;

        downloader_progress_handler progress_handler;
        void *handler_arg;

        const du_uchar *dixim_file;
        du_uint64 reserved_space;

        downloader *d;
    } downloader_write_thread_info;

    du_bool
    cipher_file_content_usable(const du_uchar *fn, du_uint8 initial_count, cipher_file_context) {
        du_bool ret = cipher_file_content_usable(fn, initial_count);
        return ret;
    }

    du_bool cipher_file_open_truncate(cipher_file *cf, const du_uchar *fn, cipher_file_context) {
        du_bool ret = cipher_file_open_truncate(cf, fn);
        return ret;
    }

    static void ake_handler_info_free(ake_handler_info *info) {
        du_sync_free(&info->sync);
        du_mutex_free(&info->mutex);
    }

    static du_bool ake_handler_info_create(ake_handler_info *info) {
        if (!du_sync_create(&info->sync)) return 0;
        if (!du_mutex_create(&info->mutex)) goto error;
        info->error_occurred = 0;
        return 1;

        error:
        du_sync_free(&info->sync);
        return 0;
    }

    static ddtcp_ret ake_end_handler(ddtcp_ret status, ddtcp_sink_ake ake, void *arg) {
        ake_handler_info *info = (ake_handler_info *) arg;

        du_mutex_lock(&info->mutex);
        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("DTCP AKE finish. status=%u"), status);
        if (DDTCP_FAILED(status)) goto error;
        du_sync_notify(&info->sync);
        du_mutex_unlock(&info->mutex);

        return status;

        error:
        info->error_occurred = 1;
        du_sync_notify(&info->sync);
        du_mutex_unlock(&info->mutex);
        du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("DTCP AKE failed: errno=0x%x"), status);
        return status;
    }

    static ddtcp_ret mv_end_handler(ddtcp_ret status, ddtcp_sink_ake ake, void *arg) {
        ake_handler_info *info = (ake_handler_info *) arg;

        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("+dtcp_move_end: info=%p"), info);
        du_mutex_lock(&info->mutex);
        du_log_dv(LOG_CATEGORY,
                  DU_UCHAR_CONST(" dtcp_move_end: DTCP AKE move complete finish. status=%u"),
                  status);
        if (DDTCP_FAILED(status)) goto error;
        du_sync_notify(&info->sync);
        du_mutex_unlock(&info->mutex);

        du_log_d(LOG_CATEGORY, DU_UCHAR_CONST("-dtcp_move_end: DTCP MOVE succeeded."));
        return status;

        error:
        info->error_occurred = 1;
        du_sync_notify(&info->sync);
        du_mutex_unlock(&info->mutex);
        du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("-dtcp_move_end: DTCP MOVE errno=0x%x"), status);
        return status;
    }

    typedef struct make_content_handler_info {
        du_bool content_enabled;
    } make_content_handler_info;

    static ddtcp_ret make_content_usable_handler(void *arg) {
        make_content_handler_info *info = (make_content_handler_info *) arg;

        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("Make content usable"));
        info->content_enabled = 1;
        return DDTCP_RET_SUCCESS;
    }

    static ddtcp_ret make_content_discard_handler(void *arg) {
        make_content_handler_info *info = (make_content_handler_info *) arg;

        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("Make content discard"));
        info->content_enabled = 0;
        return DDTCP_RET_SUCCESS;
    }

    static du_bool do_ake(ddtcp_sink_ake *ake, ake_handler_info *hinfo, const du_uchar *dtcp1host,
                          du_uint16 dtcp1port, du_bool move) {
        ddtcp_ret ret;
        du_elapse lapper;
        ddtcp dtcp = DTCP;

        du_log_d(LOG_CATEGORY, DU_UCHAR_CONST("+++ do_ake()"));

        if (!du_mutex_lock(&hinfo->mutex)) {
            LOGW;
            goto error;
        }

        du_elapse_start(&lapper);

        if (move) {
            ret = ddtcp_sink_mv_do_ake(dtcp, dtcp1host, dtcp1port, ake_end_handler, hinfo,
                                       mv_end_handler, hinfo, ake);
            if (DDTCP_FAILED(ret)) {
                du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to do ake for move: ret=0x%x."),
                          ret);
                goto error2;
            }
        } else {
            ret = ddtcp_sink_do_ake(dtcp, dtcp1host, dtcp1port, ake_end_handler, hinfo, ake);
            if (DDTCP_FAILED(ret)) {
                du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("Failed to do ake: ret=0x%x."), ret);
                goto error2;
            }
        }
        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("do ake: start cost [%umsec]"),
                  du_elapse_delta(&lapper));
        if (!du_sync_timedwait(&hinfo->sync, &hinfo->mutex, 60000)) {
            du_log_e(LOG_CATEGORY, DU_UCHAR_CONST("AKE TIME OUT"));
            goto error2;
        }
        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("do ake: finish cost [%umsec]"),
                  du_elapse_delta(&lapper));
        if (hinfo->error_occurred) {
            LOGW;
            goto error2;
        }
        du_mutex_unlock(&hinfo->mutex);
        du_log_d(LOG_CATEGORY, DU_UCHAR_CONST("--- do_ake()"));
        return 1;

        error2:
        du_mutex_unlock(&hinfo->mutex);
        error:
        du_log_w(LOG_CATEGORY, DU_UCHAR_CONST("!!! do_ake()"));
        return 0;
    }

    static du_bool
    downloader_write_thread_info_init(downloader_write_thread_info *x, downloader *d, du_bool move,
                                      cipher_file cf, du_uint64 total_size,
                                      const du_uchar *dixim_file, du_uint64 reserved_space,
                                      downloader_progress_handler progress_handler,
                                      void *handler_arg) {
        du_byte_zero((du_uint8 *) x, sizeof(downloader_write_thread_info));

        x->d = d;
        x->e_emi = move ? DDTCP_E_EMI_NMC : DDTCP_E_EMI_CF;
        x->cf = cf;
        x->total_size = total_size;
        x->dixim_file = dixim_file;
        x->reserved_space = reserved_space;
        x->progress_handler = progress_handler;
        x->handler_arg = handler_arg;

        if (!du_ringbuffer_init(&x->rb, DOWNLOADER_RINGBUFFER_SIZE)) {
            LOGW;
            return 0;
        }
        if (!du_mutex_create(&x->mutex)) {
            LOGW;
            goto error2;
        }

        if (!du_ringbuffer_start(&x->rb)) {
            LOGW;
            goto error3;
        }

        return 1;

        error3:
        du_mutex_free(&x->mutex);
        error2:
        du_ringbuffer_free(&x->rb);
        return 0;
    }

    static void downloader_write_thread_info_free(downloader_write_thread_info *x) {
        du_ringbuffer_stop(&x->rb);
        du_mutex_free(&x->mutex);
        du_ringbuffer_free(&x->rb);
    }

    static du_bool
    check_drive_free_space(const du_uchar *dixim_file, du_uint64 size, du_uint64 reserved_space) {
        du_file_system_status fss;

        if (!du_file_get_system_status(dixim_file, &fss)) {
            du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST("!du_file_get_system_status(): %s"), dixim_file);
            return 0;
        }
        if (fss.free < size + reserved_space) {
            du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST(
                              "!!! insufficient storage: fss.free=%lld size=%llu reserved_space=%llu"),
                      fss.free, size, reserved_space);
            return 0;
        }
        return 1;
    }

    static void get_transfer_mode3(dav_http_transfer_mode *transfer_mode, dav_content_features *cf,
                                   const du_uchar *upnp_class) {
        const du_str_array *param_array;
        const du_uchar *flags_param;

        if (dav_didl_derived_from(upnp_class, dav_didl_class_audio_item())) {
            *transfer_mode = DAV_HTTP_TRANSFER_MODE_STREAMING;
        } else if (dav_didl_derived_from(upnp_class, dav_didl_class_video_item())) {
            *transfer_mode = DAV_HTTP_TRANSFER_MODE_STREAMING;
        } else {
            *transfer_mode = DAV_HTTP_TRANSFER_MODE_INTERACTIVE;
        }

        param_array = dav_content_features_get_param_array(cf);
        if (!param_array) return;
        flags_param = du_param_get_value_by_name(param_array,
                                                 dav_content_features_name_flags_param());

        if (flags_param &&
            dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_DLNA_V15)) {
            if (dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_TM_B)) {
                *transfer_mode = DAV_HTTP_TRANSFER_MODE_BACKGROUND;
            }
        }
    }

    static du_bool
    get_transfer_mode2(dav_http_transfer_mode *transfer_mode, dav_didl_object_property *res,
                       const du_uchar *upnp_class) {
        dav_protocol_info pi;
        dav_content_features cf;
        const du_uchar *s;

        dav_protocol_info_init(&pi);
        dav_content_features_init(&cf);

        if (!(s = dav_didl_object_attribute_list_get_attribute_value(res->attr_list,
                                                                     dav_didl_attribute_protocol_info()))) {
            LOGW;
            goto error;
        }
        if (!dav_protocol_info_parse(&pi, s)) {
            LOGW;
            goto error;
        }
        if (!(s = dav_protocol_info_get_additional_info(&pi))) {
            LOGW;
            goto error;
        }
        if (!dav_content_features_parse(&cf, s)) {
            LOGW;
            goto error;
        }

        get_transfer_mode3(transfer_mode, &cf, upnp_class);

        dav_content_features_free(&cf);
        dav_protocol_info_free(&pi);
        return 1;

        error:
        dav_content_features_free(&cf);
        dav_protocol_info_free(&pi);
        return 0;
    }

    static du_bool get_transfer_mode(dav_http_transfer_mode *transfer_mode, const du_uchar *url,
                                     const du_uchar *didl) {
        dav_didl_object_array doa;
        dav_didl_object *obj;
        dav_didl_object_property *prop;
        const du_uchar *upnp_class;
        du_uint32 i;
        int size=0;
        int cnt=0;  //added

        dav_didl_object_array_init(&doa);
        size=du_str_len(didl);
        if (!dav_didl_parser_packed_parse2(didl, size, 1, &doa)) {
            LOGW;
            goto error;
        }

        cnt=dav_didl_object_array_length(&doa);
        if (1 != cnt) {
            LOGW;
            goto error;
        }
        obj = static_cast<dav_didl_object *>(dav_didl_object_array_get_pos(&doa, 0));

        if (!(prop = dav_didl_object_property_list_get_property(obj->prop_list,
                                                                dav_didl_element_upnp_class(),
                                                                0))) {
            LOGW;
            goto error;
        }
        if (!(upnp_class = prop->value)) {
            LOGW;
            goto error;
        }

        for (i = 0; (prop = dav_didl_object_property_list_get_property(obj->prop_list,
                                                                       dav_didl_element_res(),
                                                                       i)); ++i) {
            if (du_str_equal(url, prop->value)) break;
        }
        if (!prop) {
            LOGW;
            goto error;
        }

        if (!get_transfer_mode2(transfer_mode, prop, upnp_class)) {
            LOGW;
            goto error;
        }

        dav_didl_object_array_free_object(&doa);
        return 1;

        error:
        dav_didl_object_array_free_object(&doa);
        return 0;
    }

    static du_bool
    start_http_connection(du_http_client *hc, const du_uchar *url, const du_uchar *didl,
                          du_str_array *request_header, du_str_array *response_header) {
        du_uchar status[4];
        dav_http_transfer_mode transfer_mode;

        if (!get_transfer_mode(&transfer_mode, url, didl)) {
            LOGW;
            goto error;
        }

        if (!dav_http_header_set_transfer_mode(request_header, transfer_mode, 1)) {
            LOGW;
            goto error;
        }

        if (!du_http_client_set_uri(hc, url)) {
            LOGW;
            goto error;
        }
        if (!du_http_header_set_str(request_header, du_http_header_connection(),
                                    DU_UCHAR_CONST("close"), 1)) {
            LOGW;
            goto error;
        }

        if (!du_http_client_request(hc, du_http_method_get(), request_header, 0, status,
                                    response_header, DOWNLOADER_HTTP_CLIENT_CONNECT_TIMEOUT_MS,
                                    DOWNLOADER_HTTP_CLIENT_WRITE_TIMEOUT_MS,
                                    DOWNLOADER_HTTP_CLIENT_READ_TIMEOUT_MS)) {
            LOGW;
            goto error;
        }
        if (!du_http_status_is_successful(status)) {
            LOGW;
            goto error;
        }

        return 1;

        error:
        return 0;
    }

    static void end_http_connection(du_http_client *hc) {
        du_http_client_cancel(hc);
        du_http_client_disconnect(hc);
    }

    static du_bool is_cancel_requested(downloader *d) {
        if (CANCEL_REQUESTED) return 1;
        if (d->cancel_requested) return 1;
        return 0;
    }

    static du_bool
    start_http_connection_dtcp(du_http_client *hc, const du_uchar *url, const du_uchar *didl,
                               du_bool move, ddtcp_sink_stream *stream, ddtcp_sink_ake ake,
                               du_str_array *request_header_from_upper) {
        du_str_array request_header;
        du_str_array response_header;

        du_str_array_init(&request_header);
        du_str_array_init(&response_header);

        if (request_header_from_upper) {
            du_str_array_cat(&request_header, request_header_from_upper);
        }

        if (move) {
            du_uint8 kxm_label;
            du_uchar strnum[DU_STR_FMT_SIZE];

            if (DDTCP_FAILED(ddtcp_sink_mv_get_kxm_label(ake, &kxm_label))) {
                LOGW;
                goto error;
            }
            strnum[du_str_fmt_0xint8(strnum, kxm_label, 2)] = 0;
            if (!du_http_header_set_str(&request_header, ddtcp_util_http_header_blkmove_dtcp_com(),
                                        strnum, 1)) {
                LOGW;
                goto error;
            }
            if (!du_http_header_set_str(&request_header, DU_UCHAR_CONST("X-ParentalLock"),
                                        DU_UCHAR_CONST("move_copy/1.0"), 1)) {
                LOGW;
                goto error;
            }
            if (!start_http_connection(hc, url, didl, &request_header, &response_header)) {
                LOGW;
                goto error;
            }
            if (DDTCP_FAILED(ddtcp_sink_mv_create_http_stream(ake, stream))) {
                LOGW;
                goto error2;
            }
        } else {
            if (!start_http_connection(hc, url, didl, &request_header, &response_header)) {
                LOGW;
                goto error;
            }
            if (DDTCP_FAILED(ddtcp_sink_create_http_stream(ake, stream))) {
                LOGW;
                goto error2;
            }
        }
        du_str_array_free(&request_header);
        du_str_array_free(&response_header);
        return 1;

        error2:
        end_http_connection(hc);
        error:
        du_str_array_free(&request_header);
        du_str_array_free(&response_header);
        return 0;
    }

    static du_bool
    end_http_connection_dtcp(du_http_client *hc, du_bool move, ddtcp_sink_stream *stream) {
        if (move) {
            if (DDTCP_FAILED(ddtcp_sink_mv_destroy_stream(stream))) {
                LOGW;
                goto error;
            }

        } else {
            if (DDTCP_FAILED(ddtcp_sink_destroy_stream(stream))) {
                LOGW;
                goto error;
            }
        }

        end_http_connection(hc);
        return 1;

        error:
        end_http_connection(hc);
        return 0;
    }

    static du_bool
    write_to_file(downloader_write_thread_info *info, du_uint8 *buf, du_uint32 buf_size) {
        du_uint32 active_buffer_size;
        du_uint32 n;
        du_uint32 write_size;
        du_elapse el;
        const du_uint64 log_timming_delta = 50000000;
        du_uint64 next_log_timming = log_timming_delta;
        du_uint32 remains;

        du_elapse_start(&el);

        while (1) {
            if (info->error_occurred) return 0;

            if (!du_ringbuffer_get_active_buffer_size(&info->rb, &active_buffer_size)) {
                LOGW;
                return 0;
            }
            if (!active_buffer_size) {
                if (info->completed) {
                    break;
                } else {
                    du_thread_sleep(10);
                    continue;
                }
            }

            write_size = active_buffer_size < buf_size ? active_buffer_size : buf_size;
            if (!check_drive_free_space(info->dixim_file, write_size, info->reserved_space)) {
                LOGW;
                return 0;
            }

            if (!du_mutex_lock(&info->mutex)) {
                LOGW;
                return 0;
            }
            if (!du_ringbuffer_read(&info->rb, buf, write_size, &n)) {
                LOGW;
                goto error;
            }
            du_mutex_unlock(&info->mutex);

            remains = n;
            while (remains) {
                if (info->error_occurred) {
                    LOGW;
                    return 0;
                }
                if (CANCEL_REQUESTED) {
                    LOGW;
                    return 0;
                }

                write_size = remains;
                remains -= write_size;
            }

            if (!cipher_file_write_with_padding(&info->cf, buf, n)) {
                du_log_ev(LOG_CATEGORY, DU_UCHAR_CONST(
                        "Failed to write data to cipher file with padding."));
                return 0;
            }

            info->wrote_size += n;

            if (info->progress_handler) {
                info->progress_handler(info->wrote_size, info->total_size, info->handler_arg);
            }

            if (next_log_timming < info->wrote_size) {
                du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("%llu KB/s [%llu B] [%u ms]"),
                          info->wrote_size / du_elapse_total(&el), info->wrote_size,
                          du_elapse_total(&el));
                next_log_timming += log_timming_delta;
            }
        }

        return 1;

        error:
        du_mutex_unlock(&info->mutex);
        return 0;
    }

    static void downloader_write_thread(void *arg) {
        downloader_write_thread_info *info = (downloader_write_thread_info *) arg;
        du_uint32 active_buffer_size;
        du_uint8 *buf = 0;
        du_uint32 buf_size = DOWNLOADER_RINGBUFFER_SIZE;

        du_log_dv(0, DU_UCHAR_CONST("+++ downloader_write_thread"));

        if (!(buf = static_cast<du_uint8 *>(du_alloc(buf_size)))) goto error;

        while (1) {
            if (info->error_occurred) {
                LOGW;
                goto error;
            }
            if (is_cancel_requested(info->d)) {
                LOGW;
                goto error;
            }

            if (!du_ringbuffer_get_active_buffer_size(&info->rb, &active_buffer_size)) {
                LOGW;
                goto error;
            }

            if (!active_buffer_size && info->completed) break;

            if (!info->completed) {
                if (active_buffer_size < buf_size) goto next;
            }

            if (!write_to_file(info, buf, buf_size)) {
                LOGW;
                goto error;
            }

            continue;
            next:
            du_thread_sleep(10);
        }

        info->need_commit = 1;

        du_alloc_free(buf);
        du_log_dv(0, DU_UCHAR_CONST("--- downloader_write_thread"));
        return;

        error:
        info->error_occurred = 1;
        du_alloc_free(buf);
        du_log_dv(0, DU_UCHAR_CONST("!!! downloader_write_thread"));
        return;
    }

    static du_bool
    move_dn_ringbuffer_write(downloader_write_thread_info *info, du_uint8 *des_buf, du_uint32 n) {
        du_uint32 inactive_buffer_size;
        du_uint32 written_size;

        while (n) {
            if (info->error_occurred) {
                LOGW;
                return 0;
            }

            if (!du_ringbuffer_get_inactive_buffer_size(&info->rb, &inactive_buffer_size)) {
                LOGW;
                return 0;
            }
            if (!inactive_buffer_size) {
                du_thread_sleep(10);
                continue;
            }

            du_mutex_lock(&info->mutex);
            if (!du_ringbuffer_write(&info->rb, des_buf, n, &written_size)) {
                LOGW;
                goto error;
            }
            du_mutex_unlock(&info->mutex);

            n -= written_size;
            des_buf += written_size;
        }

        return 1;

        error:
        du_mutex_unlock(&info->mutex);
        return 0;
    }

    static du_bool filter_res(const du_uchar *didl, const du_uchar *url, du_uchar_array *ua) {
        xmlDocPtr doc;
        xmlNodePtr root;
        xmlNodePtr node;
        xmlNodePtr res;
        du_uint32 i;

        if (!(doc = dav_didl_libxml_make_doc(didl, du_str_len(didl)))) return 0;
        if (!(root = xmlDocGetRootElement(doc))) goto error;
        if (!(node = dav_didl_libxml_get_element_by_name(root->children,
                                                         dav_didl_element_item())))
            goto error;

        i = 0;
        while ((res = dav_didl_libxml_get_element_by_name2(node->children, dav_didl_element_res(),
                                                           i))) {
            if (!du_libxml_content_equal(res, url)) {
                if (!du_libxml_remove_element(res)) goto error;
            } else {
                ++i;
            }
        }


        if (!du_libxml_make_xml(doc, ua, 0)) goto error;

        xmlFreeDoc(doc);
        return 1;

        error:
        xmlFreeDoc(doc);
        return 0;
    }

    static du_bool read_body_move_dn(downloader_write_thread_info *info, du_http_client *hc,
                                     ddtcp_sink_stream stream, du_bool move) {
        du_uint32 clear_size = 0;
        du_uint32 remain = 0;
        du_uint8 *packetized = 0;
        du_uint32 packetized_size = DOWNLOADER_RINGBUFFER_SIZE;
        du_uint8 *clear = 0;
        du_bool downloading = 0;
        du_uint64 total_read = 0;
        media_mpeg mm = 0;
        media_mpeg_cci_status mms;
        du_thread th;
        du_uint8 *des_buf = 0;

        du_thread_init(&th);

        if (!(packetized = (du_uint8 *) du_alloc(DOWNLOADER_RINGBUFFER_SIZE))) {
            LOGW;
            goto error;
        }
        if (!(clear = (du_uint8 *) du_alloc(DOWNLOADER_RINGBUFFER_SIZE))) {
            LOGW;
            goto error;
        }
        if (!(des_buf = static_cast<du_uint8 *>(du_alloc(DOWNLOADER_RINGBUFFER_SIZE)))) {
            LOGW;
            goto error;
        }
        if (!du_thread_create(&th, 0, downloader_write_thread, (void *) info)) {
            LOGW;
            goto error;
        }

        {
            du_int32 cmd;

            media_mpeg_init(&mm);

            cmd = MEDIA_MPEG_CMD_CHECK_CCI_DESC;
            if (move) {
                cmd |= MEDIA_MPEG_CMD_MOVE;
            } else {
                cmd |= MEDIA_MPEG_CMD_COPY;
                media_mpeg_cci_status_init(&mms);
            }
            media_mpeg_set_cmd(&mm, cmd);
            media_mpeg_set_src_domain(&mm, MEDIA_MPEG_DOMAIN_DTCP);
            media_mpeg_set_dst_domain(&mm, MEDIA_MPEG_DOMAIN_LOCAL);

            if (!media_mpeg_check(&mm)) {
                LOGW;
                goto error2;
            }
        }

        while (!info->completed) {
            du_uint32 nbytes = 0;
            static du_uint32 cnt;
            du_bool running;
            ddtcp_e_emi e_emi;
            du_uint32 processed = 0;
            ddtcp_ret rc;

            du_uint8 pcp_ur[DDTCP_PCP_UR_SIZE];

            if (info->error_occurred) {
                LOGW;
                goto error2;
            }
            if (is_cancel_requested(info->d)) {
                LOGW;
                goto error2;
            }

            if (!du_ringbuffer_is_running(&info->rb, &running)) {
                LOGW;
                goto error2;
            }
            if (!running) {
                LOGW;
                goto error2;
            }

            if (!du_http_client_read_body(hc, packetized + remain, packetized_size - remain,
                                          DOWNLOADER_HTTP_CLIENT_READ_TIMEOUT_MS, &nbytes)) {
                LOGW;
                goto error2;
            }

            if (!nbytes) {
                info->completed = 1;
                break;
            }

            if (!downloading) {
                downloading = 1;
            }

            clear_size = DOWNLOADER_RINGBUFFER_SIZE;
            if (DDTCP_FAILED(
                    rc = ddtcp_sink_depacketize_each_e_emi(stream, packetized, nbytes + remain,
                                                           clear, &clear_size, &processed, &e_emi,
                                                           pcp_ur))) {
                LOGW;
                goto error2;
            }
            remain = nbytes + remain - processed;
            du_byte_copy(packetized, remain, packetized + processed);

            {
                du_uint8 *src = clear;
                du_uint32 src_size = clear_size;
                du_uint8 *des;
                du_uint32 des_size;

                while (src_size) {
                    du_uint32 n;

                    des = des_buf;
                    des_size = DOWNLOADER_RINGBUFFER_SIZE;
                    if (!media_mpeg_convert(mm, &src, &src_size, &des, &des_size, &n)) {
                        LOGW;
                        goto error2;
                    }
                    if (!move) {
                        if (!media_mpeg_cci_get_status(mm, e_emi, &mms)) {
                            LOGW;
                            goto error2;
                        }
                        if (mms.discard_this_chunk) {
                            if (!media_mpeg_convert_flush_discard(mm)) {
                                LOGW;
                                goto error2;
                            }
                            n = 0;
                        }
                        switch (mms.state) {
                            case MEDIA_MPEG_STATE_KEEP_STREAMING:
                                break;
                            case MEDIA_MPEG_STATE_DISCARD_SAVED_DATA:
                                // XXX: impl. reopen
                                du_log_w(0, DU_UCHAR_CONST(
                                        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"));
                                du_log_w(0, DU_UCHAR_CONST(
                                        "XXXXXXXXXXXXXXXXXXXX REOPEN XXXXXXXXXXXXXXXXXXXX"));
                                du_log_w(0, DU_UCHAR_CONST(
                                        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"));
                                break;
                            case MEDIA_MPEG_STATE_STOP_STREAMING:
                                info->completed = 1; // XXX: TODO DEBUG
                                goto end;
                            default:
                                goto error;
                        }
                    }
                    if (n) {
                        if (!move_dn_ringbuffer_write(info, des_buf, n)) {
                            LOGW;
                            goto error2;
                        }
                        total_read += n;
                    }
                    for (;;) {
                        des = des_buf;
                        des_size = DOWNLOADER_RINGBUFFER_SIZE;
                        if (!media_mpeg_convert_flush(mm, &des, &des_size, &n)) {
                            LOGW;
                            goto error2;
                        }
                        if (!n) break;
                        if (!move_dn_ringbuffer_write(info, des_buf, n)) {
                            LOGW;
                            goto error2;
                        }
                        total_read += n;
                    }
                }
            }
            end:

            if (!(++cnt % 10000)) {
#ifdef WIN32
                du_log_dv(0, DU_UCHAR_CONST("downloader: %I64u bytes read"), total_read);
#else
                du_log_dv(0, DU_UCHAR_CONST("downloader: %llu bytes read"), total_read);
#endif
            }
        }

        du_thread_join(&th);
        media_mpeg_free(&mm);
        du_alloc_free(des_buf);
        du_alloc_free(clear);
        du_alloc_free(packetized);
        return 1;

        error2:
        info->error_occurred = 1;
        du_thread_join(&th);
        media_mpeg_free(&mm);
        error:
        du_alloc_free(des_buf);
        du_alloc_free(clear);
        du_alloc_free(packetized);
        return 0;
    }

    static du_bool commit(ddtcp_sink_ake ake, ake_handler_info *ainfo) {
        make_content_handler_info info;
        ddtcp_ret ret;

        info.content_enabled = 0;

        if (DDTCP_FAILED(
                ddtcp_sink_mv_set_make_content_usable_handler(ake, make_content_usable_handler,
                                                              (void *) &info))) {
            LOGW;
            return 0;
        }
        if (DDTCP_FAILED(
                ddtcp_sink_mv_set_make_content_discard_handler(ake, make_content_discard_handler,
                                                               (void *) &info))) {
            LOGW;
            return 0;
        }
        if (DDTCP_FAILED((ret = ddtcp_sink_mv_do_commitment(ake)))) {
            LOGW;
            du_log_wv(0, DU_UCHAR_CONST("ddtcp_sink_mv_do_commitment: ret(%x)"), ret);
            return 0;
        }

        if (!du_mutex_lock(&ainfo->mutex)) return 0;
        du_sync_timedwait(&ainfo->sync, &ainfo->mutex, 10000);
        du_mutex_unlock(&ainfo->mutex);

        if (!info.content_enabled) {
            LOGW;
            return 0;
        }

        return 1;
    }

    static du_bool
    pre_commit_file(cipher_file cf, const du_uchar *dixim_file, const du_uchar *didl_xml,
                    const du_uchar *url, du_uchar_array *tshr_xml, du_uint32 e_emi,
                    du_bool *opened) {
        du_uchar_array ua;
        du_str_array sa;

        du_uchar_array_init(&ua);
        du_str_array_init(&sa);

        if (!filter_res(didl_xml, url, &ua)) {
            LOGW;
            goto error;
        }
        if (!du_uchar_array_cat0(&ua)) {
            LOGW;
            goto error;
        }

        if (*opened) {
            //if (!cipher_file_close_truncate(cf, (ddtcp_e_emi)e_emi, 1)) {LOGW; goto error;}
            if (!cipher_file_close_truncate(&cf, (ddtcp_e_emi) e_emi, 1)) {
                LOGW;
                goto error;
            }
            *opened = 0;
        }

        if (!du_param_set(&sa, dmspu_cipher_file_param_name_version(), DU_UCHAR_CONST("2.0"))) {
            LOGW;
            goto error;
        }
        if (!du_param_set(&sa, dmspu_cipher_file_param_name_didl_lite(), du_uchar_array_get(&ua))) {
            LOGW;
            goto error;
        }
        if (du_uchar_array_length(tshr_xml)) {
            if (!du_uchar_array_cat0(tshr_xml)) {
                LOGW;
                goto error;
            }
            if (!du_param_set(&sa, dmspu_cipher_file_param_name_tshr(),
                              du_uchar_array_get(tshr_xml))) {
                LOGW;
                goto error;
            }
        }
        if (!dmspu_cipher_file_write_param(dixim_file, &sa,
                                           cipher_file_context_global_get_instance())) {
            du_log_w(LOG_CATEGORY, DU_UCHAR_CONST("Cannot to write parameters to cipher file."));
            goto error;
        }

        du_uchar_array_free(&ua);
        du_str_array_free(&sa);
        return 1;

        error:
        du_uchar_array_free(&ua);
        du_str_array_free(&sa);
        return 0;
    }

    static du_bool post_commit_file(cipher_file cf, const du_uchar *dixim_file) {
        //extern du_bool cipher_file_content_usable(const du_uchar* fn, du_uint8 initial_count);
        if (!cipher_file_content_usable(dixim_file, 1,
                                        cipher_file_context_global_get_instance()))
            return 0;
#if 0
        if (!cipher_file_backup_nmcc_db()) return 0;
#endif
        return 1;
    }

    static du_bool estimate_size(const du_uchar *didl, const du_uchar *url, du_uint64 *size) {
        dav_didl_object_array doa;
        dav_didl_object *obj;
        dav_didl_object_property *prop;
        const du_uchar *upnp_class;
        du_uint32 i;
        const du_uchar *s;
        const du_uchar *s2;

        dav_didl_object_array_init(&doa);
        if (!dav_didl_parser_packed_parse2(didl, du_str_len(didl), 1, &doa)) {
            LOGW;
            goto error;
        }

        if (1 != dav_didl_object_array_length(&doa)) {
            LOGW;
            goto error;
        }
        obj = static_cast<dav_didl_object *>(dav_didl_object_array_get_pos(&doa, 0));

        if (!(prop = dav_didl_object_property_list_get_property(obj->prop_list,
                                                                dav_didl_element_upnp_class(),
                                                                0))) {
            LOGW;
            goto error;
        }
        if (!(upnp_class = prop->value)) {
            LOGW;
            goto error;
        }

        for (i = 0; (prop = dav_didl_object_property_list_get_property(obj->prop_list,
                                                                       dav_didl_element_res(),
                                                                       i)); ++i) {
            if (du_str_equal(url, prop->value)) break;
        }
        if (!prop) {
            LOGW;
            goto error;
        }

        do {
            if ((s = dav_didl_object_attribute_list_get_attribute_value(prop->attr_list,
                                                                        dav_didl_attribute_dlna_cleartext_size()))) {
                if (!du_str_scan_uint64(s, size)) {
                    LOGW;
                    goto error;
                }
                du_log_dv(0, DU_UCHAR_CONST("downloader: estimate size using dlna:cleartextSize"));
                break;
            }

            if ((s = dav_didl_object_attribute_list_get_attribute_value(prop->attr_list,
                                                                        dav_didl_attribute_dlna_estimated_size()))) {
                if (!du_str_scan_uint64(s, size)) {
                    LOGW;
                    goto error;
                }
                du_log_dv(0, DU_UCHAR_CONST("downloader: estimate size using dlna:estimatedSize"));
                break;
            }

            s = dav_didl_object_attribute_list_get_attribute_value(prop->attr_list,
                                                                   dav_didl_attribute_bitrate());
            s2 = dav_didl_object_attribute_list_get_attribute_value(prop->attr_list,
                                                                    dav_didl_attribute_duration());
            if (s && s2) {
                du_uint32 duration_ms;
                du_uint32 byterate;

                if (!du_str_scan_uint32(s, &byterate)) {
                    LOGW;
                    goto error;
                }
                if (!dav_didl_duration_scan(s2, &duration_ms)) {
                    LOGW;
                    goto error;
                }
                *size = (duration_ms / 1000) * byterate;
                du_log_dv(0,
                          DU_UCHAR_CONST("downloader: estimate size using bitrate and duration"));
                break;
            }

            *size = 0;
        } while (0);

        du_log_dv(LOG_CATEGORY, DU_UCHAR_CONST("downloader: estimated size: %llu"), *size);

        dav_didl_object_array_free_object(&doa);
        return 1;

        error:
        dav_didl_object_array_free_object(&doa);
        return 0;
    }

#define MAX_RETRY_CREATE_TEMPORARY_FILE 10

    static du_bool create_temporary_file(const du_uchar *original_path, du_uchar_array *path) {
        du_uint8 b[DU_UUID_SIZE];
        du_uchar uuid[DU_UUID_FMT_SIZE];
        du_file f = DU_FILE_INVALID;
        du_uint32 len;
        du_uint32 pos;
        du_bool created = 0;
        du_uint32 i;

        len = du_str_len(original_path);
        pos = du_str_rchr(original_path, du_file_separator());
        if (pos != len) {
            if (!du_uchar_array_copyn(path, original_path, pos + 1)) goto error;
        }

        if (!du_uchar_array_cats(path, DU_UCHAR_CONST(".dmsp-"))) goto error;
        len = du_uchar_array_length(path);

        for (i = 0; i < MAX_RETRY_CREATE_TEMPORARY_FILE; ++i) {
            if (!du_uuid_create(b)) goto error;
            uuid[du_uuid_fmt(uuid, b)] = 0;

            if (!du_uchar_array_cats(path, uuid))
                goto error;
            if (!du_uchar_array_cats0(path, DU_UCHAR_CONST(".dtcp")))
                goto error;

            if (!du_file_open_truncate_tmp(&f, du_uchar_array_get(path))) {
                goto temporary_error;
            }
            du_file_close(f);
            created = 1;

            break;

            temporary_error:
            du_log_wv(LOG_CATEGORY, DU_UCHAR_CONST("This temporary file name already exists: %s"),
                      du_uchar_array_get(path));
            if (created) {
                du_file_remove(du_uchar_array_get(path));
                created = 0;
            }
            du_uchar_array_truncate_length(path, len);
        }
        if (i == MAX_RETRY_CREATE_TEMPORARY_FILE) goto error;

        du_log_dv(0, DU_UCHAR_CONST("downloader: created temporary path: [%s]"),
                  du_uchar_array_get(path));
        return 1;

        error:
        if (created) {
            du_file_remove(du_uchar_array_get(path));
        }
        du_uchar_array_truncate(path);
        return 0;
    }

    static du_bool download(downloader *d,
                            ddtcp dtcp,
                            const du_uchar *dtcp1host,
                            du_uint16 dtcp1port,
                            const du_uchar *url,
                            du_bool move,
                            downloader_status_handler status_handler,
                            downloader_progress_handler progress_handler,
                            void *handler_arg,
                            const du_uchar *dixim_file,
                            const du_uchar *didl_xml,
                            du_uint64 size,
                            du_str_array *request_header) {
        ddtcp_sink_ake ake = 0;
        ake_handler_info hinfo;
        ddtcp_sink_stream stream = 0;
        du_http_client hc;
        downloader_write_thread_info info;
        cipher_file cf = {0};
        du_uint64 reserved_space = 0;
        du_uchar_array temp_path;
        const du_uchar *temp_path_ = 0;
        du_bool opened = 0;
        du_uchar_array tshr_xml;
        DTCP = dtcp;
        du_bool ok = false;

        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), enter");

        du_log_dv(0, DU_UCHAR_CONST(
                          "+++ downloader_download [%s:%u] [move:%d] [url:%s] [path:%s] [size:%llu]"),
                  dtcp1host, dtcp1port, move, url, dixim_file, size);

        if (!size) {
            if (!estimate_size(didl_xml, url, &size)) { LOGW; }
        }

        CANCEL_REQUESTED = 0;

        du_uchar_array_init(&temp_path);
        du_uchar_array_init(&tshr_xml);

        if (!create_temporary_file(dixim_file, &temp_path)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), create_temporary_file");
            goto error;
        }
        temp_path_ = du_uchar_array_get(&temp_path);

        //extern du_bool cipher_file_open_truncate(cipher_file* cf, const du_uchar* fn);
        if (!cipher_file_open_truncate(&cf, temp_path_,
                                       cipher_file_context_global_get_instance())) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), cipher_file_open_truncate");
            goto error;
        }
        opened = 1;
        reserved_space = 1; // XXX:
        reserved_space *= 1024 * 1024;
        if (!check_drive_free_space(temp_path_, size, reserved_space)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), check_drive_free_space");
            goto error2;
        }

        if (!downloader_write_thread_info_init(&info, d, move, cf, size, temp_path_, reserved_space,
                                               progress_handler, handler_arg)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), downloader_write_thread_info_init");
            goto error2;
        }

        if (!du_http_client_init(&hc, DOWNLOADER_HTTP_CLIENT_BUF_SIZE,
                                 DOWNLOADER_HTTP_CLIENT_BUF_SIZE)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), du_http_client_init");
            goto error3;
        }

        if (!ake_handler_info_create(&hinfo)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), ake_handler_info_create");
            goto error6;
        }
        if (!do_ake(&ake, &hinfo, dtcp1host, dtcp1port, move)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), do_ake");
            goto error7;
        }

        if (!start_http_connection_dtcp(&hc, url, didl_xml, move, &stream, ake, request_header)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), start_http_connection_dtcp");
            goto error7;
        }

        if (!read_body_move_dn(&info, &hc, stream, move)) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), read_body_move_dn");
            goto error8;
        }

        if (!info.completed) {
            LOGW;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), !info.completed");
            goto error8;
        }

        ok = end_http_connection_dtcp(&hc, move, &stream);

        if (info.need_commit) {
            if (!pre_commit_file(cf, temp_path_, didl_xml, url, &tshr_xml, info.e_emi, &opened)) {
                LOGW;
                DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), !pre_commit_file");
                goto error7;
            }
            if (move) {
                if (!commit(ake, &hinfo)) {
                    LOGW;
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), commit");
                    goto error7;
                }
            }
        } else {
            if (opened) {
                //extern du_bool cipher_file_close_truncate(cipher_file* cf, ddtcp_e_emi, du_bool make_usable);
                //if (!cipher_file_close_truncate(cf, DDTCP_E_EMI_INVALID, 0)) {LOGW; goto error7;}
                if (!cipher_file_close_truncate(&cf, DDTCP_E_EMI_INVALID, 0)) {
                    LOGW;
                    DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), cipher_file_close_truncate");
                    goto error7;
                }
                opened = 0;
            }
        }

        if (move || info.e_emi == DDTCP_E_EMI_NMC) {
            if (!post_commit_file(cf, temp_path_)) {
                LOGW;
                DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), post_commit_file");
                goto error7;
            }
        }

        if (!du_file_rename(temp_path_, dixim_file)) {
            LOGW;
        }

        ddtcp_sink_close_ake(&ake);
        ake_handler_info_free(&hinfo);
        du_http_client_free(&hc);
        downloader_write_thread_info_free(&info);

        if (status_handler) {
            status_handler(dtvt::DOWNLOADER_STATUS_COMPLETED, 0, handler_arg);
        }

        du_uchar_array_free(&temp_path);
        du_uchar_array_free(&tshr_xml);
        du_log_dv(0, DU_UCHAR_CONST("--- downloader_download"));
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), exit true");
        return 1;

        error8:
        info.error_occurred = 1;
        end_http_connection_dtcp(&hc, move, &stream);
        error7:
        if (ake) {
            ddtcp_sink_close_ake(&ake);
        }
        ake_handler_info_free(&hinfo);
        error6:
        du_http_client_free(&hc);
        error3:
        downloader_write_thread_info_free(&info);
        error2:
        if (opened) {
            cipher_file_close_truncate(&cf, DDTCP_E_EMI_INVALID, 0);
            opened = 0;
        }
        du_file_remove(temp_path_);
        error:
        if (status_handler)
            status_handler(CANCEL_REQUESTED ? dtvt::DOWNLOADER_STATUS_CANCELLED
                                            : dtvt::DOWNLOADER_STATUS_ERROR_OCCURED, 0, handler_arg);
        du_uchar_array_free(&temp_path);
        du_uchar_array_free(&tshr_xml);
        du_log_dv(0, DU_UCHAR_CONST("--- downloader_download"));
        du_log_dv(0, DU_UCHAR_CONST("!!! downloader_download"));
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>downloader.cc download(), exit false");
        return 0;
    }

    void downloader_cancel() {
        CANCEL_REQUESTED = 1;
    }

    void downloader_cancel2(downloader *d) {
        d->cancel_requested = 1;
    }

    extern du_bool downloader_download(ddtcp dtcp, const du_uchar *dtcp1host, du_uint16 dtcp1port,
                                       const du_uchar *url, du_bool move,
                                       downloader_status_handler status_handler,
                                       downloader_progress_handler progress_handler,
                                       void *handler_arg, const du_uchar *dixim_file,
                                       const du_uchar *didl_xml, du_uint64 size,
                                       du_str_array *request_header) {
        downloader d;

        du_byte_zero((du_uint8 *) &d, sizeof(downloader));

        return download(&d, dtcp, dtcp1host, dtcp1port, url, move, status_handler, progress_handler,
                        handler_arg, dixim_file, didl_xml, size, request_header);
    }

    du_bool
    downloader_download2(downloader *d, ddtcp dtcp, const du_uchar *dtcp1host, du_uint16 dtcp1port,
                         const du_uchar *url, du_bool move,
                         downloader_status_handler status_handler,
                         downloader_progress_handler progress_handler, void *handler_arg,
                         const du_uchar *dixim_file, const du_uchar *didl_xml, du_uint64 size,
                         du_str_array *request_header) {
        return download(d, dtcp, dtcp1host, dtcp1port, url, move, status_handler, progress_handler,
                        handler_arg, dixim_file, didl_xml, size, request_header);
    }

