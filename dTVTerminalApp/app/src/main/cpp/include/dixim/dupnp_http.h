/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_HTTP_H
#define DUPNP_HTTP_H

#include <dupnp_impl.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_http_init(dupnp_http* http, dupnp_impl* upnp, du_uint16 port);

extern void dupnp_http_free(dupnp_http* http);

extern du_bool dupnp_http_enable_server(dupnp_http* http, du_bool flag);

extern du_bool dupnp_http_start(dupnp_http* http);

extern void dupnp_http_stop(dupnp_http* http);

extern void dupnp_http_get_port(dupnp_http* http, du_uint16* port);

extern du_bool dupnp_http_set_access_control_handler(dupnp_http* x, dupnp_access_control_handler handler, void* arg);

extern du_bool dupnp_http_remove_access_control_handler(dupnp_http* x, dupnp_access_control_handler handler, void* arg);

extern du_bool dupnp_http_set_server_request_event_listener(dupnp_http* x, dupnp_http_server_request_event_listener event_listener, void* arg);

extern du_bool dupnp_http_remove_server_request_event_listener(dupnp_http* x, dupnp_http_server_request_event_listener event_listener, void* arg);

#ifdef __cplusplus
}
#endif

#endif
