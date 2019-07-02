/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_sink_private.h 4086 2009-08-28 14:04:49Z gondo $ 
 */ 

#ifndef DDTCP_SINK_PRIVATE_H
#define DDTCP_SINK_PRIVATE_H

#include <ddtcp_sink.h>
#include <ddtcp_private.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Does the AKE session and creates the AKE session handle.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] remote_ip IP address of the remote source device in dotted decimal notation("xxx.xxx.xxx.xxx")
 * @param[in] remote_port listen port number of the remote source device
 * @param[in] handler handler called at the end of this AKE session
 * @param[in] handler_arg optional argument passed to handler
 * @param[out] ake handle to AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the AKE session completion. 
 *         The AKE session is done asynchronously and in the end, <em>handler</em> is called with <em>handler_arg</em>.
 */
extern ddtcp_ret ddtcp_sink_do_ake_with_capability_exchange(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

/**
 * Does the Move AKE session and creates the Move AKE session handle.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] remote_ip IP address of the remote source device in dotted decimal notation("xxx.xxx.xxx.xxx")
 * @param[in] remote_port listen port number of the remote source device
 * @param[in] ake_end_handler handler called at the end of this Move AKE session
 * @param[in] ake_end_handler_arg optional argument passed to ake_end_handler
 * @param[in] mv_end_handler handler called at the end of this Move transaction
 * @param[in] mv_end_handler_arg optional argument passed to mv_end_handler
 * @param[out] ake handle to Move AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the Move AKE session completion. 
 *         The Move AKE session is done asynchronously and in the end, <em>ake_end_handler</em> is called with <em>ake_end_handler_arg</em>.
 *         Afterwards, When the Move transaction completed, <em>mv_end_handler</em> is called with <em>mv_end_handler_arg</em>.
 */
extern ddtcp_ret ddtcp_sink_mv_do_ake_with_capability_exchange(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_sink_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_sink_ake* ake);

/**
 * Does the RA-AKE session and creates the RA-AKE session handle.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] remote_ip IP address of the remote source device in dotted decimal notation("xxx.xxx.xxx.xxx")
 * @param[in] remote_port listen port number of the remote source device
 * @param[in] handler handler called at the end of this RA-AKE session
 * @param[in] handler_arg optional argument passed to handler
 * @param[out] ake handle to RA-AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the RA-AKE session completion. 
 *         The RA-AKE session is done asynchronously and in the end, <em>handler</em> is called with <em>handler_arg</em>.
 */
extern ddtcp_ret ddtcp_sink_do_ra_ake_with_capability_exchange(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

/**
 * Does the Move AKE session and creates the Move AKE session handle.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] remote_ip IP address of the remote source device in dotted decimal notation("xxx.xxx.xxx.xxx")
 * @param[in] remote_port listen port number of the remote source device
 * @param[in] ake_end_handler handler called at the end of this Move AKE session
 * @param[in] ake_end_handler_arg optional argument passed to ake_end_handler
 * @param[in] mv_end_handler handler called at the end of this Move transaction
 * @param[in] mv_end_handler_arg optional argument passed to mv_end_handler
 * @param[out] ake handle to Move AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the Move AKE session completion. 
 *         The Move AKE session is done asynchronously and in the end, <em>ake_end_handler</em> is called with <em>ake_end_handler_arg</em>.
 *         Afterwards, When the Move transaction completed, <em>mv_end_handler</em> is called with <em>mv_end_handler_arg</em>.
 */
extern ddtcp_ret ddtcp_sink_ra_mv_do_ake_with_capability_exchange(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_sink_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_sink_ake* ake);

/**
 * Checks PCP-UR is supported by remote source.
 * @param[in] ake handle to AKE session instance
 * @param[out] is_supported it shows PCP-UR is supported by remote source.
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function is used in combination	with <em>ddtcp_sink_do_ake_with_capability_exchange</em> or <em>ddtcp_sink_mv_do_ake_with_capability_exchange</em>.
 */
extern ddtcp_ret ddtcp_sink_is_pcp_ur_supported_by_remote_source(ddtcp_sink_ake ake, du_bool* is_supported);

// XXX: move to ddtcp_sink.h
//extern ddtcp_ret ddtcp_sink_do_content_key_req_and_check_kx(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, du_uint8 label, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_sink_do_content_key_req2_and_check_kr(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, du_uint8 label, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

extern ddtcp_ret ddtcp_sink_depacketize_each_pcp(ddtcp_sink_stream stream, du_uint8* packetized, du_uint32 packetized_size, du_uint8* clear, du_uint32* clear_size, du_uint32* processed_size, ddtcp_pcp* pcp);

#ifdef __cplusplus
}
#endif

#endif
