/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_HTTP_CLIENT_CP_H
#define DUPNP_HTTP_CLIENT_CP_H

#include <dupnp.h>
#include <dupnp_impl.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_http_client_cp_gena_subscribe(dupnp_impl* upnp, const du_uchar* url, const du_uchar* callback_url_path, const du_str_array* header, du_int32 sub_timeout, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

extern du_bool dupnp_http_client_cp_gena_renewal(dupnp_impl* upnp, const du_uchar* url, const du_str_array* header, const du_uchar* sid, du_int32 sub_timeout, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

extern du_bool dupnp_http_client_cp_gena_unsubscribe(dupnp_impl* upnp, const du_uchar* url, const du_str_array* header, const du_uchar* sid, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

#ifdef __cplusplus
}
#endif

#endif
