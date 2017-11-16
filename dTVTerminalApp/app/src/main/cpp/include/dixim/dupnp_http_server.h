/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_HTTP_SERVER_H
#define DUPNP_HTTP_SERVER_H

#include <dupnp.h>
#include <du_net_task.h>
#include <du_http_server.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_http_server dupnp_http_server;
typedef void (*dupnp_http_server_free)(dupnp_http_server* dhs);

typedef enum dupnp_http_server_state {
    DUPNP_HTTP_SERVER_STATE_ERROR,
    DUPNP_HTTP_SERVER_STATE_READ_START_LINE,
    DUPNP_HTTP_SERVER_STATE_READ_HEADER,
    DUPNP_HTTP_SERVER_STATE_READ_BODY,
    DUPNP_HTTP_SERVER_STATE_DISPATCH,
    DUPNP_HTTP_SERVER_STATE_END,
} dupnp_http_server_state;

typedef void (*dupnp_http_server_request_free)(void*);

struct dupnp_http_server {
    du_http_server hs;
    void* arg;
    dupnp_http_server_free free;
    dupnp_http_server_state state;
    du_uchar_array body;
    du_bool need_pooling;
    du_bool async_process;
    du_bool response_ongoing;
};

extern dupnp_http_server* dupnp_http_server_create(void* arg);

extern void dupnp_http_server_reset(dupnp_http_server* dhs);

extern void dupnp_http_server_accept_handler(du_net_task* ntask);

extern du_bool dupnp_http_server_write_error_response(dupnp_http_server* dhs, const du_uchar status[4], const du_uchar* reason, const du_uchar* server_name, du_bool is_persistent_connection);

extern void dupnp_http_server_set_async_mode(dupnp_http_server* dhs);

extern void dupnp_http_server_continue_response(dupnp_http_server* dhs);

#ifdef __cplusplus
}
#endif

#endif
