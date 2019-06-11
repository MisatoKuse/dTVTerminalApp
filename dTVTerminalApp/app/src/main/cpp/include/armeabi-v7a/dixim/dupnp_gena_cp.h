/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_GENA_CP_H
#define DUPNP_GENA_CP_H

#include <dupnp.h>
#include <dupnp_impl.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_gena_cp_init(dupnp_gena* gena);

extern void dupnp_gena_cp_free(dupnp_gena* gena);

extern du_bool dupnp_gena_cp_enable_gena(dupnp_gena* gena, du_bool flag);

extern du_bool dupnp_gena_cp_start(dupnp_gena* gena);

extern void dupnp_gena_cp_stop(dupnp_gena* gena);

extern du_bool dupnp_gena_cp_subscribe(dupnp_gena* gena, const du_uchar* event_sub_url, const du_uchar* callback_url_path, const du_str_array* header, du_int32 sub_timeout, du_int32 timeout_ms, dupnp_http_response_handler subscribe_response_handler, void* subscribe_response_handler_arg, dupnp_cp_gena_renewal_alarm_handler renewal_alarm_handler, void* renewal_alarm_handler_arg, dupnp_cp_gena_event_handler event_handler, void* event_handler_arg, du_uint32* id);

extern du_bool dupnp_gena_cp_unsubscribe(dupnp_gena* gena, const du_uchar* event_sub_url, const du_str_array* header, du_int32 timeout_ms, dupnp_http_response_handler unsubscribe_response_handler, void* unsubscribe_response_handler_arg, du_uint32* id);

extern du_bool dupnp_gena_cp_subscribe_renewal(dupnp_gena* gena, const du_uchar* sid, const du_str_array* header, du_int32 timeout_ms, dupnp_http_response_handler renewal_response_handler, void* renewal_response_handler_arg, du_uint32* id);

extern void dupnp_gena_cp_remove(dupnp_gena* gena, const du_uchar* event_sub_url);

#ifdef __cplusplus
}
#endif

#endif
