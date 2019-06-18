/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: $ 
 */ 

#ifndef DDTCP_PLUS_SINK_H
#define DDTCP_PLUS_SINK_H

#include <ddtcp.h>
#include <ddtcp_sink.h>

#ifdef __cplusplus
extern "C" {
#endif

/** @file ddtcp_plus_sink.h
 *  @brief The ddtcp_sink interface provides the high-level interface used by an application to integrate with DTCP-IP sink device functions.<br>
 *         For Remote Access, uses the following alternative functions.<br>
 *         <b>ddtcp_sink_do_ra_ake</b> is used instead of <b>ddtcp_sink_do_ake</b>.<br>
 *         <b>ddtcp_sink_ra_mv_do_ake</b> is used instead of <b>ddtcp_sink_do_mv_ake</b>.<br>
 *         <b>ddtcp_sink_do_ra_management</b> to tell the source that streaming is over and the Remote Exchange Key assigned in AKE session should be expired. the source received this sets vacant the session and can make a new session with any sink.<br>
 *  @see ddtcp_sink.h
 */

/**
 * An application-defined callback function interface to be called
 * at the end of ra_management subfunction command.
 * @param[in] status status of the AKE session<br>
 * @param[in] ake handle to AKE session instance
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success. the source reports that the ID of this sink is regitstered as allowed sink to connect with remote access, and the Remote Exchange Key assigned in this session to connect this server is valid. If do_expire flag was set at the calling of the original function, the key was expired successfully.<br>
 *         DDTCP_RET_FAILURE_RA_SINK_NOT_REGISTERD success. the source reports that the ID of this sink is not registered as allowed sink to connect remote access.<br>
 *         DDTCP_RET_FAILURE_RA_NO_MORE_KR_ALLOWED success. the source reports that the ID of this sink is registered as allowed sink to use remote access, but all available remote access connection in the source are in use so the sink can not get new remote access connection to this source.<br>
 *         DDTCP_RET_FAILURE_RA_KR_UNAVAILABLE success. the source reports that the ID of this sink is registered as allowed sink to use remote access, and there is an vacant access connection in the source. If the sink did not get a Remote Exchange Key via this session, the sink can get remote access to get contents. If the sink has already got a Remote Exchange Key via this session, the Remote Exchange Key is expired already, so the sink have to get another Remote Exchange Key to get contents.<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_sink_ra_management_end_handler)(ddtcp_ret status, ddtcp_sink_ake ake, void* arg);

/**
 * Does the AKE session and send Remote Access Sink Register Request to the source.
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
 *         To check the sink's id is registered already or not, use <em>ddtcp_sink_do_ra_management_alone</em> to send ra_management subfunction command.
 */
extern ddtcp_ret ddtcp_sink_ra_register(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

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
extern ddtcp_ret ddtcp_sink_do_ra_ake(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

/**
 * Sends ra_management subfunction with ake session still opened.
 * @param[in] ake handle to RA-AKE session instance which has done RA-RA-AKE.
 * @param[in] handler handler called at the end of ra_management subfunction
 * @param[in] handler_arg optional argument passed to handler
 * @param[in] do_expire set to expire the session with received kr_label
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the RA-AKE session completion. 
 *         The RA-AKE session is done asynchronously and in the end, <em>handler</em> is called with <em>handler_arg</em>. About result status, see <em>ddtcp_sink_ra_management_end_handler</em> description.
 */
extern ddtcp_ret ddtcp_sink_do_ra_management(ddtcp_sink_ake ake, ddtcp_sink_ra_management_end_handler handler, void* handler_arg, du_bool do_expire);

/**
 * Creates the RA-AKE session handle and send ra_management subfunction.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] remote_ip IP address of the remote source device in dotted decimal notation("xxx.xxx.xxx.xxx")
 * @param[in] remote_port listen port number of the remote source device
 * @param[in] handler handler called at the end of ra_management subfunction
 * @param[in] handler_arg optional argument passed to handler
 * @param[out] ake handle to RA-AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the RA-AKE session completion. 
 *         The RA-AKE session is done asynchronously and in the end, <em>handler</em> is called with <em>handler_arg</em>. About result status, see <em>ddtcp_sink_ra_management_end_handler</em> description.
 */
extern ddtcp_ret ddtcp_sink_do_ra_management_alone(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ra_management_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

/**
 * Gets the Kr_label value of the Remote Access transaction.
 * @param[in] ake handle to RA-AKE instance
 * @param[out] label the Kr_label value of the Remote Access transaction
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_ra_get_kr_label(ddtcp_sink_ake ake, du_uint8* label);

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
extern ddtcp_ret ddtcp_sink_ra_mv_do_ake(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_sink_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_sink_ake* ake);

#ifdef __cplusplus
}
#endif

#endif
