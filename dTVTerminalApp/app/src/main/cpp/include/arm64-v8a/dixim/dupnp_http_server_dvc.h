/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_HTTP_SERVER_DVC_H
#define DUPNP_HTTP_SERVER_DVC_H

#include <dupnp_http_server.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_http_server_dvc_gena_read_subscribe_request_header(dupnp_http_server* dhs, du_bool* is_renewal_request, const du_uchar** sid, const du_uchar** callback, const du_uchar** error_status);

extern du_bool dupnp_http_server_dvc_gena_read_unsubscribe_request_header(dupnp_http_server* dhs, const du_uchar** sid, const du_uchar** error_status);

extern du_bool dupnp_http_server_dvc_gena_write_subscribe_response(dupnp_http_server* dhs, const du_uchar* sid, du_uint32 timeout, du_str_array* header);

extern du_bool dupnp_http_server_dvc_gena_write_unsubscribe_response(dupnp_http_server* dhs, du_str_array* header);

#ifdef __cplusplus
}
#endif

#endif
