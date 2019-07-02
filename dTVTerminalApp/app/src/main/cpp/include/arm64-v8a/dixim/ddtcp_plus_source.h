/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: $ 
 */ 

#ifndef DDTCP_PLUS_SOURCE_H
#define DDTCP_PLUS_SOURCE_H

#include <ddtcp.h>
#include <ddtcp_source.h>

#ifdef __cplusplus
extern "C" {
#endif

/** @file ddtcp_plus_source.h
 *  @brief The ddtcp_source interface provides the high-level interface used by an application to integrate with DTCP-IP source device functions.<br>
 *         For Remote Access, uses the following alternative functions.<br>
 *         <b>ddtcp_source_listen_ra_ake</b> is used instead of <b>ddtcp_source_listen_ake</b>.<br>
 *         <b>ddtcp_source_ra_create_http_stream</b> is used instead of <b>ddtcp_source_create_http_stream</b>.<br>
 *  @see ddtcp_source.h
 */

/**
 * Starts listening to messages(Ex AKE CHALLENGE message) with remote access message support
 * from the remote sink devices.
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
extern ddtcp_ret ddtcp_source_listen_ra_ake(ddtcp dtcp, const du_uchar* listen_ip, du_uint16 listen_port, ddtcp_source_ake_end_handler ake_end_handler, void* ake_end_handler_arg, ddtcp_source_mv_end_handler mv_end_handler, void* mv_end_handler_arg, ddtcp_source_listen* listen);

/**
 * Creates the PCP streaming session instance for the Remote Access streaming.
 * @param[in] listen handle to listen instance
 * @param[in] kr_label kr_label
 * @param[in] e_emi E-EMI value of PCP
 * @param[in] payload_size payload size of PCP, 0x8000000(DDTCP_PCP_PAYLOAD_MAX_SIZE) or less, and should be aligned 16
 * @param[in] content_size clear content size
 * @param[in] nc last Nc or NULL
 * @param[out] stream handle to PCP streaming session instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If <em>ake</em> is zero, CopyFree contents only packetized.
 */
extern ddtcp_ret ddtcp_source_ra_create_http_stream(ddtcp_source_listen listen, du_uint8 kr_label, ddtcp_e_emi e_emi, du_uint32 payload_size, du_uint64 content_size, du_uint8 nc[DDTCP_NC_SIZE], ddtcp_source_stream* stream);

#define DDTCP_RSR_SINK_DEVICE_MAX_COUNT 20 /**< maximum count of sink devices in Remote Sink Registry */

/**
 * Gets sink device count in Remote Sink Registory.
 * @param[in] dtcp handle to dtcp instance
 * @param[out] count sink device count(max. DDTCP_RSR_SINK_DEVICE_MAX_COUNT)
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.
 */
extern ddtcp_ret ddtcp_source_get_rsr_count(ddtcp dtcp, du_uint8* count);

/**
 * Reset Remote Sink Registory.
 * @param[in] dtcp handle to dtcp instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.
 */
extern ddtcp_ret ddtcp_source_reset_rsr(ddtcp dtcp);

#ifdef __cplusplus
}
#endif

#endif
