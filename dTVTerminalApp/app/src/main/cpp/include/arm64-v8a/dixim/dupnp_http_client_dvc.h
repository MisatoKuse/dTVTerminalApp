/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_HTTP_CLIENT_DVC_H
#define DUPNP_HTTP_CLIENT_DVC_H

#include <dupnp.h>
#include <dupnp_impl.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_http_client_dvc_gena_notify_event(dupnp_impl* upnp, const du_uchar* url, const du_uchar* sid, du_uint32 seq, const du_uchar* xml, du_uint32 xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

#ifdef __cplusplus
}
#endif

#endif
