/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_sink.h 9622 2015-10-30 10:50:57Z gondo $ 
 */ 

/** @file ddtcp_sink.h
 *  @brief The ddtcp_sink interface provides the high-level interface used by an application to integrate with DTCP-IP sink device functions.<br>
 *         - Creates the DTCP-IP instance and startup (For details, refer to <b>ddtcp.h</b>).<br>
 *         ----- In NON-Move,<br>
 *         1 <b>ddtcp_sink_do_ake</b> creates the AKE session instance and starts the AKE session with the remote source device. The AKE session is done asynchronously.<br>
 *         2 The callback function specified by <em>handler</em> param of <b>ddtcp_sink_do_ake</b> is called at the end of the AKE session.<br>
 *         3 After the AKE session is completed successfully, <b>ddtcp_sink_create_http_stream</b> will create a handle to the PCP streaming session instance.<br>
 *         4 <b>ddtcp_sink_depacketize</b> or <b>ddtcp_sink_depacketize_each_e_emi</b> depacketizes the PCP stream from the source device one by one, and aquires the clear content data.<br>
 *         5 <b>ddtcp_sink_destroy_stream</b> destroys the PCP streaming session instance.<br>
 *         6 <b>ddtcp_sink_close_ake</b> destroys the AKE session instance.<br>
 *         7 Shuts down and destroys the DTCP-IP instance(For details, refer to <b>ddtcp.h</b>).<br>
 *         ----- In Move,<br>
 *         1 <b>ddtcp_sink_mv_do_ake</b> creates the Move AKE session instance and starts the Move AKE session with the remote source device. The Move AKE session is done asynchronously.<br>
 *         2 The callback function specified by <em>ake_end_handler</em> param of <b>ddtcp_sink_mv_do_ake</b> is called at the end of the Move AKE session.<br>
 *         3 After the Move AKE session is completed successfully, <b>ddtcp_sink_mv_create_http_stream</b> will create a handle to the PCP streaming session instance.<br>
 *         4 <b>ddtcp_sink_depacketize</b> depacketizes the PCP stream from the source device one by one, and aquires the clear content data.<br>
 *         5 <b>ddtcp_sink_destroy_stream</b> destroys the PCP streaming session instance.<br>
 *         6 <b>ddtcp_sink_mv_set_make_content_usable_handler</b> set a callback function to be usable the content received.<br>
 *         7 <b>ddtcp_sink_mv_set_make_content_discard_handler</b> set a callback function to be discard the content received.<br>
 *         8 <b>ddtcp_sink_mv_do_commitment</b> starts the Move Commitment with the remote source device. The Move Commitment is done asynchronously.<br>
 *         9 If the Move Commitment succeeds, the callback function specified by <em>handler</em> param of <b>ddtcp_sink_mv_set_make_content_usable_handler</b> is called.<br>
 *         10 If the Move Commitment fails, the callback function specified by <em>handler</em> param of <b>ddtcp_sink_mv_set_make_content_discard_handler</b> is called.<br>
 *         11 The callback function specified by <em>mv_end_handler</em> param of <b>ddtcp_sink_mv_do_ake</b> is called at the end of the Move transaction.<br>
 *         12 <b>ddtcp_sink_close_ake</b> destroys the Move AKE session instance.<br>
 *         -----<br>
 *         - Sink devices must expire their Kx when they detect themselves being disconnected from network.<br>
 *           <b>ddtcp_sink_expire_kx</b> can expires Kx.<br>
 *         - <b>ddtcp_sink_is_kx_expired</b> checks Kx is expired.<br>
 *         - <b>ddtcp_sink_check_wlan_wep</b> checks whether wireless LAN(802.11) WEP is enable or not.<br>
 *         -----<br>
 *         - Shuts down and destroys the DTCP-IP instance(For details, refer to <b>ddtcp.h</b>).<br>
 *  @see ddtcp.h
 */

#ifndef DDTCP_SINK_H
#define DDTCP_SINK_H

#include <ddtcp.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Type of handle to AKE session instance
 */
typedef void* ddtcp_sink_ake;

/**
 * Type of handle to PCP streaming session instance
 */
typedef void* ddtcp_sink_stream;

/**
 * An application-defined callback function interface to be called at the end of AKE session.
 * @param[in] status status of the AKE session<br>
 * @param[in] ake handle to AKE session instance
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_sink_ake_end_handler)(ddtcp_ret status, ddtcp_sink_ake ake, void* arg);

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
extern ddtcp_ret ddtcp_sink_do_ake(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

/**
 * Closes the AKE session.
 * @param[in] ake handle to AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_close_ake(ddtcp_sink_ake* ake);

/**
 * Creates the PCP streaming session instance.
 * @param[in] ake handle to AKE session instance
 * @param[out] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function is called after the AKE session succeeds with the handle to this AKE session. 
 *         If <em>ake</em> is zero, CopyFree contents only depacketized.
 */
extern ddtcp_ret ddtcp_sink_create_http_stream(ddtcp_sink_ake ake, ddtcp_sink_stream* stream);

/**
 * Depacketizes the PCP stream.
 * @param[in] stream handle to PCP streaming session instance
 * @param[in] packetized PCP packetized data
 * @param[in] packetized_size PCP packetized data size
 * @param[out] clear buffer to write depacketized clear content data
 * @param[in, out] clear_size size of clear content data buffer and clear content data size
 * @param[out] processed_size size of depacketized PCP data size
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If all packetized data is processed, <em>packetized_size</em> will be equal to <em>processed_size</em>.
 */
extern ddtcp_ret ddtcp_sink_depacketize(ddtcp_sink_stream stream, du_uint8* packetized, du_uint32 packetized_size, du_uint8* clear, du_uint32* clear_size, du_uint32* processed_size);

/**
 * Depacketizes the PCP stream.
 * @param[in] stream handle to PCP streaming session instance
 * @param[in] packetized PCP packetized data
 * @param[in] packetized_size PCP packetized data size
 * @param[out] clear buffer to write depacketized clear content data
 * @param[in, out] clear_size size of clear content data buffer and clear content data size
 * @param[out] processed_size size of depacketized PCP data size
 * @param[out] e_emi e_emi value of passed packetized data
 * @param[out] pcp_ur PCP-UR value of passed packetized data
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If all packetized data is processed, <em>packetized_size</em> will be equal to <em>processed_size</em>.
 *         If E-EMI or PCP-UR is changed, this function is returned once.
 *         If remote source device doesn't support PCP-UR, <em>pcp_ur</em> is always zero.
 */
extern ddtcp_ret ddtcp_sink_depacketize_each_e_emi(ddtcp_sink_stream stream, du_uint8* packetized, du_uint32 packetized_size, du_uint8* clear, du_uint32* clear_size, du_uint32* processed_size, ddtcp_e_emi* e_emi, du_uint8* pcp_ur);

/**
 * Destroys the PCP streaming session instance.
 * @param[in] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_destroy_stream(ddtcp_sink_stream* stream);

/**
 * Gets the most restrictive E-EMI value of the PCPs included in the streaming session.
 * @param[in] stream handle to PCP streaming session instance
 * @param[out] most_restrictive the most restrictive E-EMI value
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_get_most_restrictive_e_emi(ddtcp_sink_stream stream, ddtcp_e_emi* most_restrictive);

/**
 * An application-defined callback function interface to be called at the end of Move transaction.
 * @param[in] status status of the Move transaction
 * @param[in] ake handle to Move AKE instance
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_sink_mv_end_handler)(ddtcp_ret status, ddtcp_sink_ake ake, void* arg);

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
extern ddtcp_ret ddtcp_sink_mv_do_ake(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, ddtcp_sink_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_sink_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_sink_ake* ake);

/**
 * Gets the Kxm_label value of the Move transaction.
 * @param[in] ake handle to Move AKE instance
 * @param[out] label the Kxm_label value of the Move transaction
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_mv_get_kxm_label(ddtcp_sink_ake ake, du_uint8* label);

/**
 * Creates the PCP streaming session instance for the Move transaction.
 * @param[in] ake handle to Move AKE instance
 * @param[out] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function is called after the Move AKE session succeeds with the handle to this Move AKE session. 
 */
extern ddtcp_ret ddtcp_sink_mv_create_http_stream(ddtcp_sink_ake ake, ddtcp_sink_stream* stream);

/**
 * Destroys the PCP streaming session instance for the Move transaction.
 * @param[in] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_mv_destroy_stream(ddtcp_sink_stream* stream);

/**
 * An application-defined callback function interface to be called to be usable the content received.<br>
 * This callback function is called in the Move Commitment.
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_sink_mv_make_content_usable_handler)(void* arg);

/**
 * Sets the handler to usable content received to the Move transaction instance.
 * @param[in] ake handle to Move AKE instance
 * @param[in] handler handler to be usable content received
 * @param[in] handler_arg optional argument passed to handler
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This handler is required to be set.
 */
extern ddtcp_ret ddtcp_sink_mv_set_make_content_usable_handler(ddtcp_sink_ake ake, ddtcp_sink_mv_make_content_usable_handler handler, void* handler_arg); 

/**
 * An application-defined callback function interface to be called to be discard the content received.<br>
 * This callback function is called in the Move Commitment.
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This handler is required to be set.
 */
typedef ddtcp_ret (*ddtcp_sink_mv_make_content_discard_handler)(void* arg);

/**
 * Sets the handler to discard content received to the Move transaction instance.
 * @param[in] ake handle to Move AKE instance
 * @param[in] handler handler to discard content received
 * @param[in] handler_arg optional argument passed to handler
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This handler is required to be set.
 */
extern ddtcp_ret ddtcp_sink_mv_set_make_content_discard_handler(ddtcp_sink_ake ake, ddtcp_sink_mv_make_content_discard_handler handler, void* handler_arg); 

/**
 * Does the Move Commitment.
 * @param[in] ake handle to Move AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the Move Commitment completion. 
 *         The Move Commitment is done asynchronously and in the end, <em>mv_end_handler</em>(param of <b>ddtcp_sink_mv_do_ake</b>) is called.
 */
extern ddtcp_ret ddtcp_sink_mv_do_commitment(ddtcp_sink_ake ake);

/**
 * Expires Kx.
 * @param[in] ake handle to (Move) AKE session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark Sink devices must expire their Kx when they detect themselves being disconnected from network.
 */
extern ddtcp_ret ddtcp_sink_expire_kx(ddtcp_sink_ake ake);

/**
 * Checks Kx is Expired.
 * @param[in] ake handle to (Move) AKE session instance
 * @param[out] is_expired it shows Kx expired
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_is_kx_expired(ddtcp_sink_ake ake, du_bool* is_expired);

/**
 * Checks whether wireless LAN(802.11) WEP is enable or not
 * @param[in] ake handle to (Move) AKE session instance
 * @param[in] local_addr_str local IP address used at AKE or PCP streaming
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function calls <em>ddtcp_check_wlan_wep_handler</em> with <em>DDTCP_WLAN_WEP_CHECK_POINT_ANYWHERE</em>.
 */
extern ddtcp_ret ddtcp_sink_check_wlan_wep(ddtcp_sink_ake ake, const du_uchar* local_addr_str);

/**
 * Gets the Kx_label value of the AKE.
 * @param[in] ake handle to AKE instance
 * @param[out] label the Kx_label value of the AKE
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_sink_get_kx_label(ddtcp_sink_ake ake, du_uint8* label);

/**
 * Sent CONTENT_KEY_REQ command and checks Kx_label.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] remote_ip IP address of the remote source device in dotted decimal notation("xxx.xxx.xxx.xxx")
 * @param[in] remote_port listen port number of the remote source device
 * @param[in] label Kx_label
 * @param[in] handler handler called at the end of this session
 * @param[in] handler_arg optional argument passed to handler
 * @param[out] ake handle to session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function returns at once not waiting for the session completion. 
 *         The session is done asynchronously and in the end, <em>handler</em> is called with <em>handler_arg</em>.
 */
extern ddtcp_ret ddtcp_sink_do_content_key_req_and_check_kx(ddtcp dtcp, const du_uchar* remote_ip, du_uint16 remote_port, du_uint8 label, ddtcp_sink_ake_end_handler handler, void* handler_arg, ddtcp_sink_ake* ake);

#ifdef __cplusplus
}
#endif

#endif
