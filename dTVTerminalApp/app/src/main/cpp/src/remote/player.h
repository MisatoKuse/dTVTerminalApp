/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef PLAYER_H
#define PLAYER_H

#include <du_http_client.h>
#include <dav_didl_object_array.h>
#include <dav_didl_duration.h>
#include <dav_capability.h>
#include <du_thread.h>
#include <du_mutex.h>
#include <du_sync.h>
#include "player_media_info.h"

#include <ddtcp.h>
#include <ddtcp_private.h>

#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>
#define ACTION_TIMEOUT_MS 30000

typedef enum {
    PLAYR_ACTION_FLAG_NONE = 0,
    PLAYR_ACTION_FLAG_PLAY,
    PLAYR_ACTION_FLAG_DOWNLOAD,
    PLAYR_ACTION_FLAG_PAUSE,
    PLAYR_ACTION_FLAG_STOP,
    PLAYR_ACTION_FLAG_SEEK,
} player_action_flag;

typedef struct player_position_info {
    du_bool is_current_position_ms_available;
    du_uint32 current_position_ms;

    du_bool is_duration_ms_available;
    du_uint32 duration_ms;

    du_uchar_array uri; // Empty if the uri is unavailable.
} player_position_info;

extern void player_position_info_init(player_position_info* info);

extern void player_position_info_reset(player_position_info* info);

extern void player_position_info_free(player_position_info* info);


typedef enum player_action {
    PLAYER_ACTION_NONE,
    PLAYER_ACTION_PLAY,
    PLAYER_ACTION_DOWNLOAD,
    PLAYER_ACTION_PAUSE,
    PLAYER_ACTION_STOP,
    PLAYER_ACTION_SET_MEDIA,
    PLAYER_ACTION_RESET_MEDIA,
    PLAYER_ACTION_TERMINATE,
} player_action;

typedef enum player_state {
    PLAYER_STATE_NO_MEDIA_PRESENT,
    PLAYER_STATE_STOPPED,
    PLAYER_STATE_PLAYING,
    PLAYER_STATE_DOWNLOADING,
    PLAYER_STATE_PAUSED_PLAYBACK,
    PLAYER_STATE_TRANSITIONING,
} player_state;

typedef enum player_status {
    PLAYER_STATUS_OK,
    PLAYER_STATUS_ERROR_OCCURRED,
} player_status;

typedef void (*player_state_change_handler)(player_state state, player_status status, void* handler_arg);

typedef struct player {
    dav_capability* _cap;
    du_uchar* _download_dir;

    du_mutex _thread_mutex;
    du_sync _thread_sync;
    du_sync _action_sync;
    du_thread _thread;
    du_uint32 _stack_size;
    player_action _action;          // need lock
    player_media_info _media_info;  // need lock
    player_state _state;            // need lock
    player_status _status;          // need lock
    du_bool _disconnect;
    du_http_client _hc;
    du_uint32 _nbytes_read;
    du_ptr_array _state_change_handler_array;

//#ifdef ENABLE_DTCP
    ddtcp _dtcp;
    du_uchar* _private_data_home;
//#endif
} player;

extern du_bool player_init(player* p, du_uint32 stack_size, dav_capability* cap, const du_uchar* download_dir);

extern du_bool player_start(player* p, JavaVM *vm, jobject objTmp, jmethodID mid);

extern void player_stop(player* p);

extern void player_free(player* p);

extern du_bool player_set_private_data_home(player* p, const du_uchar* private_data_home);


extern du_bool player_set_state_change_handler(player* p, player_state_change_handler handler, void* handler_arg);

extern du_bool player_sort_resource(player* p, dav_didl_object* object);

extern du_bool player_is_supported(player* p, dav_didl_object* object);

extern du_bool player_set_media(player* p, const du_uchar* uri, dav_didl_object* object);

extern du_bool player_reset_media(player* p);

extern du_bool player_action_play(player* p);

extern du_bool player_action_download(player* p);

extern du_bool player_action_pause(player* p);

extern du_bool player_action_stop(player* p);

extern du_bool player_action_seek(player* p, du_uint32 target_time_ms);

extern du_bool player_get_current_available_actions(player* p, du_uint32* actions, du_uchar_array* play_speed);

extern du_bool player_get_position_info(player* p, player_position_info* info);

extern du_bool player_get_protocol_info(player* p, du_uchar_array* protocol_info);

extern du_bool player_get_device_id_hash(player* p, du_uchar_array* hash_base64_ecoded);

extern du_bool player_sink_ra_register(player* p, const du_uchar* dtcp1_host, du_uint16 dtcp1_port);

#ifdef __cplusplus
}
#endif

#endif
