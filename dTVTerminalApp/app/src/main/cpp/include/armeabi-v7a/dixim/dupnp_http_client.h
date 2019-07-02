/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_HTTP_CLIENT_H
#define DUPNP_HTTP_CLIENT_H

#include <dupnp_impl.h>
#include <du_http_client.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_http_client dupnp_http_client;

typedef void (*dupnp_http_client_free)(dupnp_http_client* dhc);

typedef enum {
    DUPNP_HTTP_CLIENT_STATE_INIT,
    DUPNP_HTTP_CLIENT_STATE_ERROR,
    DUPNP_HTTP_CLIENT_STATE_CONNECT,
    DUPNP_HTTP_CLIENT_STATE_WRITE,
    DUPNP_HTTP_CLIENT_STATE_READ_STATUS_LINE,
    DUPNP_HTTP_CLIENT_STATE_READ_HEADER,
    DUPNP_HTTP_CLIENT_STATE_READ_BODY,
    DUPNP_HTTP_CLIENT_STATE_END,
} dupnp_http_client_state;

struct dupnp_http_client {
    dupnp_impl* upnp;
    du_http_client hc;
    dupnp_http_response_handler handler;
    void* arg;
    dupnp_http_client_free free;
    dupnp_http_client_state state;
    du_uchar method[16];
    du_uchar* uri;
    du_str_array request_header;
    du_uchar status[4];
    du_str_array response_header;
    du_uchar_array body;
    du_uint32 body_size;
    du_bool need_pooling;
    du_bool responded;
    du_bool connected;
};

extern du_bool dupnp_http_client_issue(dupnp_http_client* dhc, du_int32 timeout_ms, du_bool cancelable, du_uint32* id);

extern du_bool dupnp_http_client_change_net_task_state(dupnp_nettaskmgr* nettaskmgr, du_net_task_state state, du_net_task* task);

extern du_bool dupnp_http_client_handler_write(du_net_task* task);

extern du_bool dupnp_http_client_handler_read_status_line(du_net_task* task);

extern du_bool dupnp_http_client_reconnect(du_net_task* task);

extern du_bool dupnp_http_client_handler_read_header(du_net_task* task, du_uint32 max_body_size);

extern du_bool dupnp_http_client_handler_read_body(du_net_task* task, du_uint32 max_body_size);

extern void dupnp_http_client_callback_response_handler(dupnp_http_client* dhc, du_uint32 id, du_socket_error error);

extern void dupnp_http_client_handler(du_net_task* task);

extern dupnp_http_client* dupnp_http_client_create(dupnp_impl* upnp, dupnp_http_response_handler handler, void* arg, const du_uchar* method, const du_uchar* uri, const du_str_array* header, const du_uchar* body, du_uint32 body_size);

extern du_bool dupnp_http_client_request(dupnp_impl* upnp, const du_uchar* method, const du_uchar* url, const du_uchar* proxy_host, du_uint16 proxy_port, const du_str_array* header, const du_uchar* request_xml, du_uint32 request_xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

extern du_bool dupnp_http_client_get(dupnp_impl* upnp, const du_uchar* url, const du_uchar* proxy_host, du_uint16 proxy_port, const du_str_array* header, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

extern du_bool dupnp_http_client_soap(dupnp_impl* upnp, const du_uchar* url, const du_uchar* proxy_host, du_uint16 proxy_port, const du_str_array* header, const du_uchar* request_xml, du_uint32 request_xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

#ifdef __cplusplus
}
#endif

#endif
