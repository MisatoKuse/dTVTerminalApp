/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

/** @file ddtcp.h
 *  @brief The ddtcp interface provides the high-level interface used by an application to integrate with DTCP-IP sink and source device functions.<br>
 *         - <b>ddtcp_create_dtcp</b> creates the DTCP-IP instance.<br>
 *         - <b>ddtcp_startup</b> loads the private data and activates the DTCP-IP instance.<br>
 *         - For the behavior of the source device, refer to <b>ddtcp_source.h</b>. For the behavior of the sink device, refer to <b>ddtcp_sink.h</b>.<br>
 *         - <b>ddtcp_shutdown</b> ends each behavior of source and sink devices.<br>
 *         - <b>ddtcp_destroy_dtcp</b> destroys the DTCP-IP instance.<br>
 *         - optional.<br>
 *         - <b>ddtcp_set_additional_param</b> sets additional parameters, refer to <b>dixim_dtcp_private_data_io</b> and <b>dixim_dtcp_constant_data_io</b>.<br>
 *         - <b>ddtcp_set_rtt_max_retries</b> sets RTT test count to retry for sink and source device.<br>
 *         - <b>ddtcp_set_sink_rtt_max_retries</b> sets RTT test count to retry for sink device.<br>
 *         - <b>ddtcp_set_source_rtt_max_retries</b> sets RTT test count to retry for source device.<br>
 *         - <b>ddtcp_set_sink_to_send_capability_exchange</b> sets sink to send capability exchange command.<br>
 *         - <b>ddtcp_set_source_listen_backlog</b> sets backlog of listen.<br>
 *         - <b>ddtcp_set_source_listen_max_connection</b> sets max count of AKE or other commands connection.<br>
 *         - <b>ddtcp_set_check_wlan_wep_handler</b> sets the handler to check whether wireless LAN(802.11) WEP is enable or not.<br>
 *         - <b>ddtcp_check_wlan_wep</b> checks whether wireless LAN(802.11) WEP is enable or not.<br>
 *         - Source devices must expire their Kx and Kr when they detect themselves being disconnected from network.<br>
 *           <b>ddtcp_source_expire_kx</b> can expires Kx and Kr.<br>
 *  @see ddtcp_source.h ddtcp_sink.h
 */

#ifndef DDTCP_H
#define DDTCP_H

#include <du_socket_os.h>
#include <du_ip.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Enumerated type of return value
 */
typedef enum {
    DDTCP_RET_SUCCESS = 0x000, /**< success */

    DDTCP_RET_FAILURE = 0x100, /**< failure */
    DDTCP_RET_FAILURE_CANCELED_BY_LOCAL, /**< canceled by local */
    DDTCP_RET_FAILURE_CANCELED_BY_REMOTE, /**< canceled by remote */
    DDTCP_RET_FAILURE_INVALID_ARGUMENT, /**< invalid argument */
    DDTCP_RET_FAILURE_TO_ALLOCATE_MEMORY, /**< failure to allocate memory */
    DDTCP_RET_FAILURE_BUFFER_TOO_SMALL, /**< buffer too small */
    DDTCP_RET_FAILURE_NOT_IMPLEMENTED, /**< not implemented */
    DDTCP_RET_FAILURE_INVALID_PRIVATE_DATA, /**< invalid Private Data */
    DDTCP_RET_FAILURE_INVALID_API_SEQUENCE, /**< invalid API sequence */
    DDTCP_RET_FAILURE_INVALID_EC_DSA_SIGNATURE, /**< invalid EC-DSA signature */
    DDTCP_RET_FAILURE_AKE_BUSY, /**< failure AKE busy */
    DDTCP_RET_FAILURE_WLAN_WEP_OFF, /**< failure Wireless LAN WEP off */

    DDTCP_RET_FAILURE_SOCKET = 0x200, /**< failure socket */
    DDTCP_RET_FAILURE_SOCKET_NO_RESOURCE, /**< failure socket no resource */
    DDTCP_RET_FAILURE_SOCKET_CONNECTION_REFUSED, /**< failure socket connection refused */
    DDTCP_RET_FAILURE_SOCKET_CONNECTION_TIMED_OUT, /**< failure socket connection timed out */
    DDTCP_RET_FAILURE_SOCKET_NETWORK_UNREACHABLE, /**< failure socket network unreachable */
    DDTCP_RET_FAILURE_SOCKET_ADDRESS_ALREADY_IN_USE, /**< failure socket address already in use */
    DDTCP_RET_FAILURE_SOCKET_WOULD_BLOCK, /**< failure socket would block */
    DDTCP_RET_FAILURE_SOCKET_READ_TIMED_OUT, /**< failure socket read timed out */
    DDTCP_RET_FAILURE_SOCKET_WRITE_TIMED_OUT, /**< failure socket write timed out */
    DDTCP_RET_FAILURE_SOCKET_CHECK_TTL_TIMED_OUT, /**< failure socket check ttl timed out */
    DDTCP_RET_FAILURE_SOCKET_CANCELED, /**< failure socket canceled */
    DDTCP_RET_FAILURE_SOCKET_INVALID_REMOTE_TTL, /**< failure socket invalid remote TTL */
    DDTCP_RET_FAILURE_SOCKET_NETIF_NOT_FOUND, /**< failure socket net I/F not found */
    DDTCP_RET_FAILURE_SOCKET_TTL, /**< failure socket TTL */

    DDTCP_RET_FAILURE_SRM = 0x300, /**< failure SRM */
    DDTCP_RET_FAILURE_SRM_INVALID, /**< (internal code)failure SRM invalid */
    DDTCP_RET_FAILURE_SRM_LOCAL_SRM_INVALID, /**< failure local SRM invalid */
    DDTCP_RET_FAILURE_SRM_REMOTE_SRM_INVALID, /**< failure remote SRM invalid */
    DDTCP_RET_FAILURE_SRM_DEVICE_REVOKED, /**< (internal code)failure SRM device revoked */ 
    DDTCP_RET_FAILURE_SRM_LOCAL_DEVICE_REVOKED, /**< failure SRM local device revoked */ 
    DDTCP_RET_FAILURE_SRM_REMOTE_DEVICE_REVOKED, /**< failure SRM remote device revoked */ 

    DDTCP_RET_FAILURE_CERT = 0x400, /**< failure Certificate */
    DDTCP_RET_FAILURE_CERT_NOT_IMPLEMENTED, /**< (internal code)failure Certificate not implemented */
    DDTCP_RET_FAILURE_CERT_LOCAL_CERT_NOT_IMPLEMENTED, /**< failure Certificate local Certificate not implemented */
    DDTCP_RET_FAILURE_CERT_REMOTE_CERT_NOT_IMPLEMENTED, /**< failure Certificate remote Certificate not implemented */
    DDTCP_RET_FAILURE_CERT_INVALID, /**< (internal code)failure Certificate invalid */
    DDTCP_RET_FAILURE_CERT_LOCAL_CERT_INVALID, /**< failure Certificate local Certificate invalid */
    DDTCP_RET_FAILURE_CERT_REMOTE_CERT_INVALID, /**< failure Certificate remote Certificate invalid */
    DDTCP_RET_FAILURE_CERT_LOCAL_CERT_IDU_IS_REQUIRED, /**< failure Certificate local Certificate IDu is required */

    DDTCP_RET_FAILURE_TRANSPORT = 0x500, /**< failure transport */
    DDTCP_RET_FAILURE_TRANSPORT_REMOTE_DEVICE_TIMEOUT, /**< failure transport remote device timeout */

    DDTCP_RET_FAILURE_AKE = 0x600, /**< failure AKE */
    DDTCP_RET_FAILURE_AKE_REMOTE_COMMAND_INVALID, /**< failure AKE remote command invalid */
    DDTCP_RET_FAILURE_AKE_SINK_COUNT_REACHED_LIMIT, /**< failure AKE sink count reached limit */
    DDTCP_RET_FAILURE_AKE_INVALID_COMMAND_SEQUENCE, /**< failure AKE invalid command sequence */
    DDTCP_RET_FAILURE_AKE_RTT_FAILED, /**< failure AKE RTT failed */

    DDTCP_RET_FAILURE_EXCHANGE_KEY = 0x700, /**< failure exchange key*/
    DDTCP_RET_FAILURE_EXCHANGE_KEY_SINK_NOT_AQUIRED, /**< failure exchange key not aquired */
    DDTCP_RET_FAILURE_EXCHANGE_KEY_EXPIRED, /**< failure exchange key expired */

    DDTCP_RET_FAILURE_AKE_CONT_KEY_CONF = 0x800, /**< failure AKE cont key conf */
    DDTCP_RET_FAILURE_AKE_CONT_KEY_CONF_FAILED, /**< failure AKE cont key conf failed*/

    DDTCP_RET_FAILURE_AKE_IP = 0x900, /**< failure AKE IP */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_COMMAND_NOT_IMPLEMENTED, /**< failure AKE IP remote command not implemented */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_COMMAND_AKE_LABEL_INVALID, /**< failure AKE IP remote command ake label invalid */
    DDTCP_RET_FAILURE_AKE_IP_INVALID_COMMAND_SEQUENCE, /**< failure AKE IP invalid command sequence */
    DDTCP_RET_FAILURE_AKE_IP_RTT_FAILED, /**< failure AKE IP RTT failed */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_COMMAND_TIMEOUT, /**< failure AKE IP command timeout */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE, /**< failure AKE IP remote device response */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_NO_MORE_AUTH, /**< failure AKE IP remote device response no more authentication */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_AUTH_FAILED, /**< failure AKE IP remote device response authentication failed */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_REJECTED, /**< failure AKE IP remote device response rejected */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_NOT_IMPLEMENTED, /**< failure AKE IP remote device response not implemented */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_ANY_OTHER_ERROR, /**< failure AKE IP remote device response any remote error */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_INVALID_COMMAND_SEQUENCE, /**< failure AKE IP remote device response invalid command sequence */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_DEVICE_RESPONSE_SYNTAX_ERROR, /**< failure AKE IP remote device response syntax error */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_COMMAND_INVALID, /**< failure AKE IP remote command invalid */
    DDTCP_RET_FAILURE_AKE_IP_REMOTE_CAPABILITY_EXCHANGE_COMMAND, /**< failure AKE IP remote CAPABILITY_EXCHANGE command */

    DDTCP_RET_FAILURE_PCP = 0xA00, /**< failure content */
    DDTCP_RET_FAILURE_PCP_REMOTE_PCP_NOT_IMPLEMENTED, /**< failure PCP remote PCP not implemented */
    DDTCP_RET_FAILURE_PCP_PCP_SIZE_TOO_LARGE, /**< (internal code)failure PCP PCP size too large */
    DDTCP_RET_FAILURE_PCP_LOCAL_PCP_SIZE_TOO_LARGE, /**< failure PCP local PCP size too large */
    DDTCP_RET_FAILURE_PCP_REMOTE_PCP_SIZE_TOO_LARGE, /**< failure PCP remote PCP size too large */
    DDTCP_RET_FAILURE_PCP_EXCHANGE_KEY_LABEL_NOT_MATCHED, /**< failure PCP exchange key label not matched */
    DDTCP_RET_FAILURE_PCP_INVALID_E_EMI, /**< failure PCP invalid E-EMI */
    DDTCP_RET_FAILURE_PCP_AKE_HAS_NOT_BEEN_DONE, /**< failure PCP AKE has not been done */

    DDTCP_RET_FAILURE_MOVE = 0xB00, /**< failure Move */
    DDTCP_RET_FAILURE_MOVE_BUSY, /**< failure Move transmission busy */
    DDTCP_RET_FAILURE_MOVE_TRANSACTION_DOES_NOT_EXIST, /* failure Move transaction does not exit */
    DDTCP_RET_FAILURE_MOVE_FINALIZE, /**< failure Move finalize */
    DDTCP_RET_FAILURE_MOVE_FINALIZE_BUSY, /**< failure Move finalize busy */
    DDTCP_RET_FAILURE_MOVE_FINALIZE_REMOTE_MAC_INVALID, /**< failure Move finalize remote MAC invalid*/
    
    DDTCP_RET_FAILURE_RA = 0xC00, /**< failure Remote Access */
    DDTCP_RET_FAILURE_RA_BUSY, /**< failure RA kr_label duplicate */
    DDTCP_RET_FAILURE_RA_CONNECTION_COUNT_MAX, /**< failure RA RACC is equal as or larger than MAX */
    DDTCP_RET_FAILURE_RA_SOURCE_NOT_SUPPORTED, /**< failure RA Source did not support RA */
    DDTCP_RET_FAILURE_RA_SINK_NOT_REGISTERED, /**< failure RA Sink Not Registered */
    DDTCP_RET_FAILURE_RA_NO_MORE_KR_ALLOWED, /**< failure RA No more K_R allowed */
    DDTCP_RET_FAILURE_RA_KR_UNAVAILABLE, /**< failure RA K_R unavailable */

    DDTCP_RET_FAILURE_USER_DEFINED_BASE = 0x2000,  /**< failure user defined base */
    DDTCP_RET_FAILURE_USER_DEFINED_END = 0x2fff,  /**< failure user defined end */
} ddtcp_ret;

/**
 * Checks if a return value is a successful value.
 * @param[in] ret ddtcp_ret type value
 * @return  true ret is a successful value<br>
 *          false ret is a failed value<br>
 */ 
#define DDTCP_SUCCESS(ret) ((ret) < DDTCP_RET_FAILURE)

/**
 * Checks if a return value is a failed value.
 * @param[in] ret ddtcp_ret type value
 * @return  true ret is a failed value<br>
 *          false ret is a successful value<br>
 */ 
#define DDTCP_FAILED(ret) ((ret) >= DDTCP_RET_FAILURE)

/**
 * Type of handle to DTCP-IP instance
 */
typedef void* ddtcp;

/**
 * Extended Encryption Mode Indicator(E-EMI)
 */
typedef enum {
    DDTCP_E_EMI_CF = 0, /**< Copy Free(N.A) */
    DDTCP_E_EMI_CF_EPN = 2, /**< Copy Free with EPN asserted(Mode D0) */
    DDTCP_E_EMI_NMC = 4, /**< No More Copies(Mode C0) */
    DDTCP_E_EMI_MOVE = 6, /**< Move(Mode C1) */
    DDTCP_E_EMI_COG_FNC = 8, /**< Copy One Generation Format Non Cognizant recording permitted (Mode B0) */
    DDTCP_E_EMI_COG_FC = 10, /**< Copy One Generation Format Cognizant recording only(Mode B1) */
    DDTCP_E_EMI_CN = 12, /**< Copy Never(Mode A0) */
    DDTCP_E_EMI_INVALID = 0x1F, /**< invalid e_emi */
} ddtcp_e_emi;

#define DDTCP_PCP_PAYLOAD_MAX_SIZE 0x8000000 /**< maximum size of a PCP payload size */
#define DDTCP_PCP_PAYLOAD_MAX_SIZE_ALIGNED_TS_PACKET_SIZE (DDTCP_PCP_PAYLOAD_MAX_SIZE / 188 * 188) /**< maximum size of a PCP payload size aligned MPEG-TS packet size*/ 
#define DDTCP_PCP_PAYLOAD_MAX_SIZE_ALIGNED_TTS_PACKET_SIZE (DDTCP_PCP_PAYLOAD_MAX_SIZE / 192 * 192) /**< maximum size of a PCP payload size aligned MPEG-TTS packet size*/
#define DDTCP_PCP_PAYLOAD_MAX_SIZE_ALIGNED_TS_AND_TTS_PACKET_SIZE (DDTCP_PCP_PAYLOAD_MAX_SIZE / 9024 * 9024) /**< maximum size of a PCP payload size aligned MPEG-TS/TTS packet size*/

/**
 * Size of Nc
 */
#define DDTCP_NC_SIZE 8

/**
 * Size of PCP-UR
 */
#define DDTCP_PCP_UR_SIZE 2

/**
 * Enumerated type of additional parameter type
 */
typedef enum {
    DDTCP_ADDITINAL_PARAM_TYPE_PRIVATE_DATA_IO = 1, /**< additional parameter type of dixim_dtcp_private_data_io library */
} ddtcp_additional_param_type;

/**
 * Creates DTCP-IP instance.
 * @param[out] dtcp handle to DTCP-IP instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */ 
extern ddtcp_ret ddtcp_create_dtcp(ddtcp *dtcp);

/**
 * Destroys DTCP-IP instance.
 * @param[in] dtcp handle to DTCP-IP instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */ 
extern ddtcp_ret ddtcp_destroy_dtcp(ddtcp* dtcp);

/**
 * Loads the private data and activates the DTCP-IP instance.
 * @param[in] dtcp handle to DTCP-IP instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_startup(ddtcp dtcp);

/**
 * Unloads the Private Data and inactivate the DTCP-IP instance.
 * @param[in] dtcp handle to DTCP-IP instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_shutdown(ddtcp dtcp);

/**
 * Set additional parameters.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] type type
 * @param[in] param additional param
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_additional_param(ddtcp dtcp, ddtcp_additional_param_type type, void* param);

/**
 * Sets RTT test count to retry for sink and source device.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] retries RTT test count to retry
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_rtt_max_retries(ddtcp dtcp, du_uint16 retries);

/**
 * Sets RTT test count to retry for sink device.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] retries RTT test count to retry
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_sink_rtt_max_retries(ddtcp dtcp, du_uint16 retries);

/**
 * Sets RTT test count to retry for source device.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] retries RTT test count to retry
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_source_rtt_max_retries(ddtcp dtcp, du_uint16 retries);

/**
 * Sets sink to send capability exchange command.
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] send send or doesn't send
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_sink_to_send_capability_exchange(ddtcp dtcp, du_bool send);

/**
 * Sets backlog of listen
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] backlog backlog
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_source_listen_backlog(ddtcp dtcp, du_uint32 backlog);

/**
 * Sets max count of AKE or other commands connection
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] max max count of AKE or other commands connection
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_source_listen_max_connection(ddtcp dtcp, du_uint32 max);

/**
 * check point of wireless LAN(802.11) WEP status
 */
typedef enum {
    DDTCP_WLAN_WEP_CHECK_POINT_ANYWHERE, /**< anywhere */
    DDTCP_WLAN_WEP_CHECK_POINT_SOURCE_BEFORE_LISTEN, /**< before listen (source device) */
    DDTCP_WLAN_WEP_CHECK_POINT_SOURCE_AFTER_ACCEPT_AKE, /**< after accept AKE (source devcie) */
    DDTCP_WLAN_WEP_CHECK_POINT_SINK_BEFORE_DO_AKE, /**< before do AKE (sink device) */
} ddtcp_wlan_wep_check_point;

/**
 * An application-defined callback function interface to be called at <em>ddtcp_wlan_wep_check_point</em>.
 * @param[in] check_point the point in which this handler was called
 * @param[in] local_addr_str local IP address used at AKE or PCP streaming
 * @param[in] arg optional argument
 * @param[out] wlan_wep_off this interface is wireless LAN(802.11) and WEP or WPA or WPA2 is disabled.
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
typedef ddtcp_ret (*ddtcp_check_wlan_wep_handler)(ddtcp_wlan_wep_check_point check_point, const du_uchar* local_addr_str, void* arg, du_bool* wlan_wep_off);

/**
 * Sets the handler to check whether wireless LAN(802.11) WEP is enable or not
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] handler handler called at <em>ddtcp_wlan_wep_check_point</em>
 * @param[in] arg optional argument passed to handler
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 */
extern ddtcp_ret ddtcp_set_check_wlan_wep_handler(ddtcp dtcp, ddtcp_check_wlan_wep_handler handler, void* arg);

/**
 * Checks whether wireless LAN(802.11) WEP is enable or not
 * @param[in] dtcp handle to DTCP-IP instance
 * @param[in] local_addr_str local IP address used at AKE or PCP streaming
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark This function calls <em>ddtcp_check_wlan_wep_handler</em> with <em>DDTCP_WLAN_WEP_CHECK_POINT_ANYWHERE</em>.
 */
extern ddtcp_ret ddtcp_check_wlan_wep(ddtcp dtcp, const du_uchar* local_addr_str);

/**
 * Expires Source Device's Kx and Kr.
 * @param[in] dtcp handle to DTCP-IP instance
 * @return DDTCP_RET_SUCCESS success<br>
 *         (other value) failure<br>
 * @remark Source devices must expire their Kx and Kr when they detect themselves being disconnected from network.
 */
extern ddtcp_ret ddtcp_expire_source_kx(ddtcp dtcp);

#ifdef __cplusplus
}
#endif

#endif
