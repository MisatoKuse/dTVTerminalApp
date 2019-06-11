/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

/** @file ddtcp_source.h
 *  @brief The ddtcp_source interface provides the high-level interface used by an application to integrate with DTCP-IP source device functions.<br>
 *         - Creates the DTCP-IP instance and startup (For details, refer to <b>ddtcp.h</b>).<br>
 *         - <b>ddtcp_source_listen_ake</b> creates the listen instance and listens to messages(Ex AKE CHALLENGE message) from remote sink devices.<br>
 *         ----- In NON-Move,<br>
 *         1 With the demand of sink device, <b>ddtcp_source_create_http_stream</b> creates the PCP streaming session instance.<br>
 *         2 <b>ddtcp_source_packetize</b> or <b>ddtcp_source_packetize_each</b> packetizes the clear content data to PCP data one by one, and the application sends it to the sink device.<br>
 *         3 <b>ddtcp_source_destroy_stream</b> destroys the PCP streaming session instance.<br>
 *         ----- In Move,<br>
 *         1 With the demand of sink device, <b>ddtcp_source_mv_create_http_stream</b> creates the PCP streaming session instance.<br>
 *         2 <b>ddtcp_source_mv_set_make_content_unusable_handler</b> set a callback function to be UN-usable the content received.<br>
 *         3 <b>ddtcp_source_packetize</b> or <b>ddtcp_source_packetize_each</b> packetizes the clear content data to PCP data one by one, and the application sends it to the sink device.<br>
 *         4 <b>ddtcp_source_destroy_stream</b> destroys the PCP streaming session instance.<br>
 *         5 If the Move Commitment succeeds, the callback function specified by <em>handler</em> param of <b>ddtcp_source_mv_set_make_content_unusable_handler</b> is called.<br>
 *         6 If necessary, <b>ddtcp_source_mv_cancel_transaction</b> can terminate the Move transaction.<br>
 *         -----<br>
 *         - <b>ddtcp_source_set_e_emi()</b> sets E-EMI. This E-EMI value is used from the following PCP. <br>
 *         - <b>ddtcp_source_set_pcp_ur()</b> sets PCP-UR. This PCP-UR value is used from the following PCP. <br>
 *         - Source devices must expire their Kx and Kr when they detect themselves being disconnected from network.<br>
 *           <b>ddtcp_source_expire_kx</b> can expires Kx and kr.<br>
 *         - <b>ddtcp_source_check_wlan_wep</b> checks whether wireless LAN(802.11) WEP is enable or not.<br>
 *         -----<br>
 *         - <b>ddtcp_source_close_listen_ake</b> ends the listen and destroys the listen instance.<br>
 *         - Shuts down and destroys the DTCP-IP instance (For details, refer to <b>ddtcp.h</b>).<br>
 *  @see ddtcp.h
 */

#ifndef DDTCP_SOURCE_H
#define DDTCP_SOURCE_H

#include <ddtcp.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Type of handle to listen instance
 */
typedef void* ddtcp_source_listen;

/**
 * Type of handle to PCP streaming session instance
 */
typedef void* ddtcp_source_stream;

/**
 * Starts listening to messages(Ex AKE CHALLENGE message) from the remote sink devices.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] listen_ip IP address to listen to messages in dotted decimal notation("xxx.xxx.xxx.xxx")
 *            The IP address "0.0.0.0" can be used for all currently enabled network adapters. 
 * @param[in] listen_port listen port number
 * @param[out] listen handle to listen instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_source_listen_ake(ddtcp dtcp, const du_uchar* listen_ip, du_uint16 listen_port, ddtcp_source_listen* listen);

/**
 * An application-defined callback function interface to be called at the end of AKE session.
 * @param[in] status status of the AKE sessions<br>
 * @param[in] ake handle to listen instance
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_source_ake_end_handler)(ddtcp_ret status, ddtcp_source_listen listen, void* arg);

/**
 * Starts listening to messages(Ex AKE CHALLENGE message) from the remote sink devices.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] listen_ip IP address to listen to messages in dotted decimal notation("xxx.xxx.xxx.xxx")
 *            The IP address "0.0.0.0" can be used for all currently enabled network adapters. 
 * @param[in] listen_port listen port number
 * @param[in] ake_end_handler handler called at the end of AKE sessions
 * @param[in] ake_end_handler_arg optional argument passed to ake_end_handler
 * @param[out] listen handle to listen instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_source_listen_ake_2(ddtcp dtcp, const du_uchar* listen_ip, du_uint16 listen_port, ddtcp_source_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_source_listen* listen);

/**
 * An application-defined callback function interface to be called at the end of Move transaction.
 * @param[in] status status of the Move transaction
 * @param[in] kxm_label kxm_label
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_source_mv_end_handler)(ddtcp_ret status, du_uint8 kxm_label, void* arg);

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
extern ddtcp_ret ddtcp_source_listen_ake_3(ddtcp dtcp, const du_uchar* listen_ip, du_uint16 listen_port, ddtcp_source_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_source_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_source_listen* listen);

/**
 * Closes the listen.
 * @param[in] listen handle to listen instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_source_close_listen_ake(ddtcp_source_listen* listen);

/**
 * Creates the PCP streaming session instance.
 * @param[in] listen handle to listen instance
 * @param[in] e_emi E-EMI value of PCP
 * @param[in] payload_size payload size of PCP, 0x8000000(DDTCP_PCP_PAYLOAD_MAX_SIZE) or less, and should be aligned 16
 * @param[in] content_size clear content size
 * @param[out] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If <em>ake</em> is zero, CopyFree contents only packetized.
 */                                                                  
extern ddtcp_ret ddtcp_source_create_http_stream(ddtcp_source_listen listen, ddtcp_e_emi e_emi, du_uint32 payload_size, du_uint64 content_size, ddtcp_source_stream* stream);

/**
 * Struct type of Nc information
 */
typedef struct {
    du_uint8 _nc[DDTCP_NC_SIZE];
    du_uint32 _accumulated_size;
} ddtcp_source_nc;

/**
 * Creates the PCP streaming session instance.
 * @param[in] listen handle to listen instance
 * @param[in] e_emi E-EMI value of PCP
 * @param[in] payload_size payload size of PCP, 0x8000000(DDTCP_PCP_PAYLOAD_MAX_SIZE) or less, and should be aligned 16
 * @param[in] content_size clear content size
 * @param[in] nc last Nc or NULL
 * @param[out] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If <em>ake</em> is zero, CopyFree contents only packetized.
 */                                                                  
extern ddtcp_ret ddtcp_source_create_http_stream_2(ddtcp_source_listen listen, ddtcp_e_emi e_emi, du_uint32 payload_size, du_uint64 content_size, du_uint8 nc[DDTCP_NC_SIZE], ddtcp_source_stream* stream);

/**
 * Destroys the PCP streaming session instance.
 * @param[in] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_source_destroy_stream(ddtcp_source_stream* stream);

/**
 * Destroys the PCP streaming session instance.
 * @param[in] stream handle to PCP streaming session instance
 * @param[out] nc last Nc
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_source_destroy_stream_2(ddtcp_source_stream* stream, du_uint8 nc[DDTCP_NC_SIZE]);

/**
 * Gets size of the PCP from size of the clear data. 
 * @param[in] clear_size clear data size
 * @param[out] packetized_size PCP size
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark <em>packetized_size</em> is DDTCP_PCP_HEADER_SIZE(14byte) plus <em>clear_size</em> plus padding size.
 *         Padding size make <em>clear_size</em> to be aligned 16byte.
 */
extern ddtcp_ret ddtcp_source_get_packetized_size(du_uint32 clear_size, du_uint32* packetized_size);

/**
 * Creates a packet of PCP.
 * @param[in] stream handle to PCP streaming session instance
 * @param[in] clear clear data
 * @param[in] clear_size clear data size
 * @param[out] packetized buffer to write a pakcet of PCP
 * @param[in, out] packetized_size size of the buffer of a packet of PCP and size of a packet of PCP
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark Required <em>packetized_size</em> can be acquired <b>ddtcp_source_packetized_size</b> function.
 *         In case of the content size is unknown, for example when sending live content over DTCP-IP 
 *         while receiving it, the content will be processed to PCP by <b>ddtcp_source_packetize_each</b>, 
 *         and sent to Sink Device.<br>
 *         Note: For the same stream handle created by <b>ddtcp_source_create_http_stream</b>, 
 *         <b>ddtcp_source_packetize_each</b> and <b>ddtcp_source_packetize</b> cannot be used together.
 */
extern ddtcp_ret ddtcp_source_packetize_each(ddtcp_source_stream stream, du_uint8* clear, du_uint32 clear_size, du_uint8* packetized, du_uint32* packetized_size);

/**
 * Creates the PCP stream.
 * @param[in] stream handle to PCP streaming session instance
 * @param[in] clear clear data
 * @param[in] clear_size clear data size
 * @param[out] packetized the buffer to write packetized content data
 * @param[in, out] packetized_size size of the packetized content data buffer and packetized content data size
 * @param[out] processed_size size of packetized data size
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If all clear data is processed, <em>clear_size</em> will be equal to <em>processed_size</em>.
 *         In case of the content size is known, for example when sending content stored in PC over DTCP-IP, 
 *         use <b>ddtcp_source_packetize.</b>
 *         Note: For the same stream handle created by <b>ddtcp_source_create_http_stream</b>, 
 *         <b>ddtcp_source_packetize_each</b> and <b>ddtcp_source_packetize</b> cannot be used together.
 */
extern ddtcp_ret ddtcp_source_packetize(ddtcp_source_stream stream, du_uint8* clear, du_uint32 clear_size, du_uint8* packetized, du_uint32* packetized_size, du_uint32* processed_size);

/**
 * An application-defined callback function interface to be called to be UN-usable the content sent.<br>
 * This callback function is called in the Move Commitment.
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_source_mv_make_content_unusable_handler)(void* arg);

/**
 * Sets the handler to UN-usable content received to the Move transaction instance.
 * @param[in] listen handle to listen instance
 * @param[in] kxm_label kxm_label
 * @param[in] handler handler to UN-usable content sent
 * @param[in] handler_arg optional argument passed to handler
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This handler is required to be set.
 */
extern ddtcp_ret ddtcp_source_mv_set_make_content_unusable_handler(ddtcp_source_listen listen, du_uint8 kxm_label, ddtcp_source_mv_make_content_unusable_handler handler, void* handler_arg); 

/**
 * Creates the PCP streaming session instance for the Move transaction.
 * @param[in] listen handle to listen instance
 * @param[in] kxm_label kxm_label
 * @param[in] payload_size payload size of PCP, 0x8000000(DDTCP_PCP_PAYLOAD_MAX_SIZE) or less, and should be aligned 16
 * @param[in] content_size clear content size
 * @param[out] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */                                                                  
extern ddtcp_ret ddtcp_source_mv_create_http_stream(ddtcp_source_listen listen, du_uint8 kxm_label, du_uint32 payload_size, du_uint64 content_size, ddtcp_source_stream* stream);

/**
 * Starts listening to Move Commitment(MV_Finalize command) from the remote sink devices.
 * @param[in] listen handle to listen instance
 * @param[in] kxm_label kxm_label
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function is optional. If this is not called, when ddtcp_source_mv_destroy_stream is called, Listening to Move Commitment will be started.
 */
extern ddtcp_ret ddtcp_source_mv_listen_commitment(ddtcp_source_listen listen, du_uint8 kxm_label);

/**
 * Destroys the PCP streaming session instance for the Move transaction.
 * @param[in] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark When this function is called, SDK ends MV Transmission and begins to measure the timeout until MV Commitment for one minute.
 */
extern ddtcp_ret ddtcp_source_mv_destroy_stream(ddtcp_source_stream* stream);

/**
 * Cancels the Move transaction.
 * @param[in] listen handle to listen instance
 * @param[in] kxm_label kxm_label
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_source_mv_cancel_transaction(ddtcp_source_listen listen, du_uint8 kxm_label);

/**
 * Expires Kx and Kr.
 * @param[in] listen handle to listen instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function exists only for backwards compatibility. Please use <b>ddtcp_expire_source_kx</b>. Source devices must expire their Kx and Kr when they detect themselves being disconnected from network.
 */
extern ddtcp_ret ddtcp_source_expire_kx(ddtcp_source_listen listen);

/**
 * Sets E-EMI.
 * @param[in] stream handle to PCP streaming session instance
 * @param[in] e_emi new E-EMI value
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This E-EMI value is used from the following PCP. 
 */
extern ddtcp_ret ddtcp_source_set_e_emi(ddtcp_source_stream stream, ddtcp_e_emi e_emi);

/**
 * Sets PCP-UR.
 * @param[in] stream handle to PCP streaming session instance
 * @param[in] pcp_ur new PCP-UR value
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This PCP-UR value is used from the following PCP. 
 */
extern ddtcp_ret ddtcp_source_set_pcp_ur(ddtcp_source_stream stream, du_uint8 pcp_ur[DDTCP_PCP_UR_SIZE]);

/**
 * Checks whether wireless LAN(802.11) WEP is enable or not
 * @param[in] listen handle to listen instance
 * @param[in] local_addr_str local IP address used at AKE or PCP streaming
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function calls <em>ddtcp_check_wlan_wep_handler</em> with <em>DDTCP_WLAN_WEP_CHECK_POINT_ANYWHERE</em>.
 */
extern ddtcp_ret ddtcp_source_check_wlan_wep(ddtcp_source_listen listen, const du_uchar* local_addr_str);

#ifdef __cplusplus
}
#endif

#endif
