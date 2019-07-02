/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_bridge.h 5746 2011-03-08 04:29:30Z gondo $ 
 */ 

#ifndef DDTCP_BRIDGE_H
#define DDTCP_BRIDGE_H

#include <ddtcp.h>
#include <ddtcp_sink.h>
#include <ddtcp_source.h>

#ifdef __cplusplus
extern "C" {
#endif

extern ddtcp_ret ddtcp_enable_bridge(ddtcp dtcp);

extern ddtcp_ret ddtcp_bridge_enable_local_independent_sink(ddtcp dtcp);

extern ddtcp_ret ddtcp_bridge_set_upstream_source_effective_key_count(ddtcp dtcp, du_uint32 count);

extern ddtcp_ret ddtcp_bridge_set_upstream_source_ake_interval_msec(ddtcp dtcp, du_uint32 interval_msec);

extern ddtcp_ret ddtcp_bridge_add_upstream_source(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port);

extern ddtcp_ret ddtcp_bridge_remove_upstream_source(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port);

extern ddtcp_ret ddtcp_bridge_add_upstream_ra_source(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port);

extern ddtcp_ret ddtcp_bridge_remove_upstream_ra_source(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port);

extern ddtcp_ret ddtcp_bridge_remove_all_upstream_source(ddtcp dtcp);

/**
 * Starts listening to messages(Ex AKE CHALLENGE message) from the remote sink devices.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] listen_ip IP address to listen to messages in dotted decimal notation("xxx.xxx.xxx.xxx")
 *            The IP address "0.0.0.0" can be used for all currently enabled network adapters. 
 * @param[in] listen_port listen port number
 * @param[in] ake_end_handler handler called at the end of AKE sessions
 * @param[in] ake_end_handler_arg optional argument passed to ake_end_handler
 * @param[in] mv_end_handler handler called at the end of this Move transaction
 * @param[in] mv_end_handler_arg optional argument passed to mv_end_handler
 * @param[out] listen handle to listen instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_bridge_source_listen_ake(ddtcp dtcp, const du_uchar* listen_ip, du_uint16 listen_port, ddtcp_source_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_source_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_source_listen* listen);

/**
 * Closes the listen.
 * @param[in] listen handle to listen instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_bridge_source_close_listen_ake(ddtcp_source_listen* listen);

extern ddtcp_ret ddtcp_bridge_sink_get_ake_handle_async(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_bridge_sink_get_ra_ake_handle_async(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

#ifdef __cplusplus
}
#endif

#endif
