/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_sink_internal.h 3426 2009-02-20 09:58:07Z gondo $ 
 */ 

#ifndef DDTCP_SINK_INTERNAL_H
#define DDTCP_SINK_INTERNAL_H

#include <ddtcp.h>
#include <ddtcp_internal.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    ddtcp_impl* dtcp;
    void* arg;
    ddtcp_sink_ake_end_handler handler;
} ddtcp_sink_ake_end_handler_wrap_arg;

typedef struct {
    du_bool is_canceled_sink_by_local;
    ddtcp_sink_ake_end_handler ake_end;
    void* ake_end_arg;
    ddtcp_sink_ake_end_handler_wrap_arg ake_end_wrap_arg_impl;
    ddtcp_sink_mv_end_handler mv_end;
    void* mv_end_arg;
    ddtcp_ake_ip_session_handle ake_ip_session_handle;
    du_uchar remote_ip[DDTCP_UTIL_SOCKET_IP_STR_SIZE];
    du_uint16 remote_port;
    ddtcp_util_thread thread;
    ddtcp dtcp;
    void* common;
    du_bool send_capability_exchange;
    du_bool send_ra_register; // send ra_register after RTT-AKE
    du_bool use_remote_access; // try RA-AKE instead of RTT-AKE
    du_bool do_expire;
    du_bool disable_kx;
    du_bool enable_capability_exchange;
    du_uint8 key_label;
    void* upstream_source_handle;
} ddtcp_sink_ake_impl;

extern ddtcp_ret ddtcp_sink_do_ake_x(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_sink_do_ra_ake_x(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_sink_do_ake_with_capability_exchange_x(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_sink_do_ra_ake_with_capability_exchange_x(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

// XXX: move to ddtcp_sink.h
//extern ddtcp_ret ddtcp_sink_get_kx_label(ddtcp_sink_ake ake, du_uint8* label);

extern ddtcp_ret ddtcp_sink_do_content_key_req_and_check_kx(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, du_uint8 label, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_sink_do_content_key_req2_and_check_kr(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, du_uint8 label, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

#ifdef __cplusplus
}
#endif

#endif
