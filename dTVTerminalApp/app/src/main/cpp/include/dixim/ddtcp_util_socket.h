/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_util_socket.h 9622 2015-10-30 10:50:57Z gondo $ 
 */ 

#ifndef DDTCP_UTIL_SOCKET_H
#define DDTCP_UTIL_SOCKET_H

#include <ddtcp.h>
#include <du_socket.h>
#include <du_type_os.h>
#include <du_poll.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DDTCP_UTIL_SOCKET_SCOPE_ID_STR_SIZE 16
#define DDTCP_UTIL_SOCKET_IP_STR_SIZE (DU_IP_STR_SIZE + DDTCP_UTIL_SOCKET_SCOPE_ID_STR_SIZE)

typedef struct {
    du_socket s;
    du_poll p;
    du_bool poll_created;
    du_bool opend;
} ddtcp_util_socket;

#define  ddtcp_util_socket_throw { \
    ddtcp_ret ret; \
 \
    ret = ddtcp_util_socket_get_last_error(); \
    if (ret == DDTCP_RET_SUCCESS) { \
        ret = DDTCP_RET_FAILURE_SOCKET; \
    } \
    ddtcp_func_throw(ret); \
}

typedef void* ddtcp_util_socket_ttl;

extern ddtcp_ret ddtcp_util_socket_init();
extern ddtcp_ret ddtcp_util_socket_end();
extern ddtcp_ret ddtcp_util_socket_close(ddtcp_util_socket* s);
extern void ddtcp_util_socket_free(ddtcp_util_socket* s);
extern ddtcp_ret ddtcp_util_socket_tcp(ddtcp_util_socket* s, const du_uchar addr_str[DDTCP_UTIL_SOCKET_IP_STR_SIZE]);
extern ddtcp_ret ddtcp_util_socket_ip(const du_uchar addr_str[DDTCP_UTIL_SOCKET_IP_STR_SIZE], du_uint16 remote_port, du_ip* ip);
extern ddtcp_ret ddtcp_util_socket_listen(ddtcp_util_socket* s, du_uint32 backlog);
extern ddtcp_ret ddtcp_util_socket_bind(ddtcp_util_socket* s, const du_ip* ip);
extern ddtcp_ret ddtcp_util_socket_wait_for_accept(ddtcp_util_socket* s, du_int32 timeout_msec, du_bool* acceptable);
extern ddtcp_ret ddtcp_util_socket_accept(ddtcp_util_socket* s, ddtcp_util_socket* c, du_ip* ip);
extern ddtcp_ret ddtcp_util_socket_connect(ddtcp_util_socket* s, du_int32 timeout_msec, const du_ip* ip);
extern ddtcp_ret ddtcp_util_socket_wait_read(ddtcp_util_socket* s, du_uint32 timeout_msec);
extern ddtcp_ret ddtcp_util_socket_recv(ddtcp_util_socket* s, du_uint8* buf, du_uint32 len, du_uint32* nbytes);
extern ddtcp_ret ddtcp_util_socket_send(ddtcp_util_socket* s, const du_uint8* buf, du_uint32 len, du_uint32 timeout_msec);
extern ddtcp_ret ddtcp_util_socket_get_last_error();
extern ddtcp_ret ddtcp_util_socket_set_ttl(ddtcp_util_socket* s, du_int32 ttl);
extern ddtcp_ret ddtcp_util_socket_set_hops(ddtcp_util_socket* s, du_int32 hops);
extern ddtcp_ret ddtcp_util_socket_disable_nagle(ddtcp_util_socket* s);
extern ddtcp_ret ddtcp_util_socket_set_dtcp_params(ddtcp_util_socket* s);
extern ddtcp_ret ddtcp_util_socket_set_ttl_constraint(ddtcp_util_socket* s);
extern ddtcp_ret ddtcp_util_socket_verify_dtcp_params(ddtcp_util_socket* s);
extern ddtcp_ret ddtcp_util_socket_read_buf_size(ddtcp_util_socket* s, du_uint32* size);

extern ddtcp_ret ddtcp_util_socket_ttl_init(ddtcp_util_socket* s, ddtcp_util_socket_ttl* ttl);
extern ddtcp_ret ddtcp_util_socket_ttl_source_init(du_uint16 local_port, ddtcp_util_socket_ttl* ttl);
extern ddtcp_ret ddtcp_util_socket_ttl_sink_init(du_uchar remote_addr_str[DDTCP_UTIL_SOCKET_IP_STR_SIZE], du_uint16 remote_port, ddtcp_util_socket_ttl* ttl);

extern ddtcp_ret ddtcp_util_socket_ttl_end(ddtcp_util_socket_ttl* ttl);
extern ddtcp_ret ddtcp_util_socket_ttl_check_before_recv(ddtcp_util_socket_ttl ttl, du_timel timeout_msec);
extern ddtcp_ret ddtcp_util_socket_ttl_check_after_recv(ddtcp_util_socket_ttl ttl);

extern ddtcp_ret ddtcp_util_socket_get_local_ip_str(ddtcp_util_socket* s, du_uchar ip_str[DDTCP_UTIL_SOCKET_IP_STR_SIZE]);

extern du_bool ddtcp_util_socket_is_socket_opend(ddtcp_util_socket* s);

// XXX: TEST
extern void test_ddtcp_util_socket_close_socket_only(ddtcp_util_socket* s);

#define DDTCP_SOCKET_TTL 3

#ifdef __cplusplus
}
#endif

#endif
