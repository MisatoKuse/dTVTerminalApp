/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_GENA_CP_SUB_INFO_H
#define DUPNP_GENA_CP_SUB_INFO_H

#include <dupnp.h>
#include <dupnp_cp.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    DUPNP_GENA_CP_SUB_STATE_UNKNOWN,
    DUPNP_GENA_CP_SUB_STATE_WAIT_INITIAL_EVENT,
    DUPNP_GENA_CP_SUB_STATE_WAIT_EVENT,
    DUPNP_GENA_CP_SUB_STATE_UNSUBSCRIBE_IN_PROGRESS,
} dupnp_gena_cp_sub_state;

typedef struct dupnp_gena_cp_sub_info {
    dupnp_gena_cp_sub_state state;
    du_uchar* event_sub_url;
    du_uchar* callback_url_path;
    dupnp_http_response_handler subscribe_response_handler;
    void* subscribe_response_handler_arg;
    dupnp_http_response_handler renewal_response_handler;
    void* renewal_response_handler_arg;
    dupnp_http_response_handler unsubscribe_response_handler;
    void* unsubscribe_response_handler_arg;
    dupnp_cp_gena_renewal_alarm_handler renewal_alarm_handler;
    void* renewal_alarm_handler_arg;
    dupnp_cp_gena_event_handler event_handler;
    void* event_handler_arg;
    du_uchar* sid;
    du_uint32 seq;
    du_int32 timeout;
    du_time expiration;
    du_bool no_more_renewal;
    du_id32 task_id;
} dupnp_gena_cp_sub_info;

extern du_bool dupnp_gena_cp_sub_info_init(dupnp_gena_cp_sub_info* si, const du_uchar* event_sub_url, const du_uchar* callback_url_path, dupnp_http_response_handler subscribe_response_handler, void* subscribe_response_handler_arg, dupnp_cp_gena_renewal_alarm_handler renewal_alarm_handler, void* renewal_alarm_handler_arg, dupnp_cp_gena_event_handler event_handler, void* event_handler_arg, du_time now);

extern void dupnp_gena_cp_sub_info_free(dupnp_gena_cp_sub_info* si);

extern du_bool dupnp_gena_cp_sub_info_copy(dupnp_gena_cp_sub_info* to, dupnp_gena_cp_sub_info* from);

#ifdef __cplusplus
}
#endif

#endif
