/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wanipv6fc.h
 *  @brief The digd_wanipv6fc interface provides methods for making SOAP request/response messages of
 *   WANIPv6FirewallControl actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANIPv6FirewallControl actions.
 *  @see  WANIPv6FirewallControl:1 Service Template Version 2.00 For UPnP. Version 1.0
 */

#ifndef DIGD_WANIPV6FC_H
#define DIGD_WANIPV6FC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of GetFirewallStatus.
  * @return the action name of GetFirewallStatus.
  */
extern const du_uchar* digd_wanipv6fc_action_name_get_firewall_status(void);

/**
 * Appends a SOAP request message of GetFirewallStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_get_firewall_status(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetFirewallStatus action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_get_firewall_status(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetFirewallStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] firewall_enabled value of FirewallEnabled argument.
 * @param[in] inbound_pinhole_allowed value of InboundPinholeAllowed argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_get_firewall_status_response(du_uchar_array* xml, du_uint32 v, du_bool firewall_enabled, du_bool inbound_pinhole_allowed);

/**
 * Parses a SOAP response message of GetFirewallStatus action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] firewall_enabled pointer to the storage location for FirewallEnabled argument value.
 * @param[out] inbound_pinhole_allowed pointer to the storage location for InboundPinholeAllowed argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_get_firewall_status_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* firewall_enabled, du_bool* inbound_pinhole_allowed);

/**
  * Returns the action name of GetOutboundPinholeTimeout.
  * @return the action name of GetOutboundPinholeTimeout.
  */
extern const du_uchar* digd_wanipv6fc_action_name_get_outbound_pinhole_timeout(void);

/**
 * Appends a SOAP request message of GetOutboundPinholeTimeout action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] remote_host value of RemoteHost argument.
 * @param[in] remote_port value of RemotePort argument.
 * @param[in] internal_client value of InternalClient argument.
 * @param[in] internal_port value of InternalPort argument.
 * @param[in] protocol value of Protocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_get_outbound_pinhole_timeout(du_uchar_array* xml, du_uint32 v, const du_uchar* remote_host, du_uint16 remote_port, const du_uchar* internal_client, du_uint16 internal_port, du_uint16 protocol);

/**
 * Parses a SOAP request message of GetOutboundPinholeTimeout action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] remote_host pointer to the storage location for RemoteHost argument value.
 * @param[out] remote_port pointer to the storage location for RemotePort argument value.
 * @param[out] internal_client pointer to the storage location for InternalClient argument value.
 * @param[out] internal_port pointer to the storage location for InternalPort argument value.
 * @param[out] protocol pointer to the storage location for Protocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_get_outbound_pinhole_timeout(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** remote_host, du_uint16* remote_port, const du_uchar** internal_client, du_uint16* internal_port, du_uint16* protocol);

/**
 * Appends a SOAP response message of GetOutboundPinholeTimeout action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] outbound_pinhole_timeout value of OutboundPinholeTimeout argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_get_outbound_pinhole_timeout_response(du_uchar_array* xml, du_uint32 v, du_uint32 outbound_pinhole_timeout);

/**
 * Parses a SOAP response message of GetOutboundPinholeTimeout action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] outbound_pinhole_timeout pointer to the storage location for OutboundPinholeTimeout argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_get_outbound_pinhole_timeout_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* outbound_pinhole_timeout);

/**
  * Returns the action name of AddPinhole.
  * @return the action name of AddPinhole.
  */
extern const du_uchar* digd_wanipv6fc_action_name_add_pinhole(void);

/**
 * Appends a SOAP request message of AddPinhole action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] remote_host value of RemoteHost argument.
 * @param[in] remote_port value of RemotePort argument.
 * @param[in] internal_client value of InternalClient argument.
 * @param[in] internal_port value of InternalPort argument.
 * @param[in] protocol value of Protocol argument.
 * @param[in] lease_time value of LeaseTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_add_pinhole(du_uchar_array* xml, du_uint32 v, const du_uchar* remote_host, du_uint16 remote_port, const du_uchar* internal_client, du_uint16 internal_port, du_uint16 protocol, du_uint32 lease_time);

/**
 * Parses a SOAP request message of AddPinhole action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] remote_host pointer to the storage location for RemoteHost argument value.
 * @param[out] remote_port pointer to the storage location for RemotePort argument value.
 * @param[out] internal_client pointer to the storage location for InternalClient argument value.
 * @param[out] internal_port pointer to the storage location for InternalPort argument value.
 * @param[out] protocol pointer to the storage location for Protocol argument value.
 * @param[out] lease_time pointer to the storage location for LeaseTime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_add_pinhole(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** remote_host, du_uint16* remote_port, const du_uchar** internal_client, du_uint16* internal_port, du_uint16* protocol, du_uint32* lease_time);

/**
 * Appends a SOAP response message of AddPinhole action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] unique_id value of UniqueID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_add_pinhole_response(du_uchar_array* xml, du_uint32 v, du_uint16 unique_id);

/**
 * Parses a SOAP response message of AddPinhole action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] unique_id pointer to the storage location for UniqueID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_add_pinhole_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* unique_id);

/**
  * Returns the action name of UpdatePinhole.
  * @return the action name of UpdatePinhole.
  */
extern const du_uchar* digd_wanipv6fc_action_name_update_pinhole(void);

/**
 * Appends a SOAP request message of UpdatePinhole action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] unique_id value of UniqueID argument.
 * @param[in] new_lease_time value of NewLeaseTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_update_pinhole(du_uchar_array* xml, du_uint32 v, du_uint16 unique_id, du_uint32 new_lease_time);

/**
 * Parses a SOAP request message of UpdatePinhole action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] unique_id pointer to the storage location for UniqueID argument value.
 * @param[out] new_lease_time pointer to the storage location for NewLeaseTime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_update_pinhole(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* unique_id, du_uint32* new_lease_time);

/**
 * Appends a SOAP response message of UpdatePinhole action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_update_pinhole_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of UpdatePinhole action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_update_pinhole_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeletePinhole.
  * @return the action name of DeletePinhole.
  */
extern const du_uchar* digd_wanipv6fc_action_name_delete_pinhole(void);

/**
 * Appends a SOAP request message of DeletePinhole action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] unique_id value of UniqueID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_delete_pinhole(du_uchar_array* xml, du_uint32 v, du_uint16 unique_id);

/**
 * Parses a SOAP request message of DeletePinhole action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] unique_id pointer to the storage location for UniqueID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_delete_pinhole(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* unique_id);

/**
 * Appends a SOAP response message of DeletePinhole action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_delete_pinhole_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeletePinhole action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_delete_pinhole_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetPinholePackets.
  * @return the action name of GetPinholePackets.
  */
extern const du_uchar* digd_wanipv6fc_action_name_get_pinhole_packets(void);

/**
 * Appends a SOAP request message of GetPinholePackets action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] unique_id value of UniqueID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_get_pinhole_packets(du_uchar_array* xml, du_uint32 v, du_uint16 unique_id);

/**
 * Parses a SOAP request message of GetPinholePackets action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] unique_id pointer to the storage location for UniqueID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_get_pinhole_packets(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* unique_id);

/**
 * Appends a SOAP response message of GetPinholePackets action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] pinhole_packets value of PinholePackets argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_get_pinhole_packets_response(du_uchar_array* xml, du_uint32 v, du_uint32 pinhole_packets);

/**
 * Parses a SOAP response message of GetPinholePackets action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] pinhole_packets pointer to the storage location for PinholePackets argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_get_pinhole_packets_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* pinhole_packets);

/**
  * Returns the action name of CheckPinholeWorking.
  * @return the action name of CheckPinholeWorking.
  */
extern const du_uchar* digd_wanipv6fc_action_name_check_pinhole_working(void);

/**
 * Appends a SOAP request message of CheckPinholeWorking action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] unique_id value of UniqueID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_check_pinhole_working(du_uchar_array* xml, du_uint32 v, du_uint16 unique_id);

/**
 * Parses a SOAP request message of CheckPinholeWorking action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] unique_id pointer to the storage location for UniqueID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_check_pinhole_working(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* unique_id);

/**
 * Appends a SOAP response message of CheckPinholeWorking action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] is_working value of IsWorking argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipv6fc_make_check_pinhole_working_response(du_uchar_array* xml, du_uint32 v, du_bool is_working);

/**
 * Parses a SOAP response message of CheckPinholeWorking action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] is_working pointer to the storage location for IsWorking argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipv6fc_parse_check_pinhole_working_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* is_working);

/**
 * the state variable name of 'FirewallEnabled'.
 * @return  "FirewallEnabled" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_firewall_enabled(void);

/**
 * the state variable name of 'InboundPinholeAllowed'.
 * @return  "InboundPinholeAllowed" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_inbound_pinhole_allowed(void);

/**
 * the state variable name of 'A_ARG_TYPE_OutboundPinholeTimeout'.
 * @return  "A_ARG_TYPE_OutboundPinholeTimeout" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_outbound_pinhole_timeout(void);

/**
 * the state variable name of 'A_ARG_TYPE_IPv6Address'.
 * @return  "A_ARG_TYPE_IPv6Address" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_ipv6address(void);

/**
 * the state variable name of 'A_ARG_TYPE_Port'.
 * @return  "A_ARG_TYPE_Port" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_port(void);

/**
 * the state variable name of 'A_ARG_TYPE_Protocol'.
 * @return  "A_ARG_TYPE_Protocol" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_protocol(void);

/**
 * the state variable name of 'A_ARG_TYPE_LeaseTime'.
 * @return  "A_ARG_TYPE_LeaseTime" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_lease_time(void);

/**
 * the state variable name of 'A_ARG_TYPE_UniqueID'.
 * @return  "A_ARG_TYPE_UniqueID" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_unique_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_PinholePackets'.
 * @return  "A_ARG_TYPE_PinholePackets" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_pinhole_packets(void);

/**
 * the state variable name of 'A_ARG_TYPE_Boolean'.
 * @return  "A_ARG_TYPE_Boolean" string.
 */
extern const du_uchar* digd_wanipv6fc_state_variable_name_a_arg_type_boolean(void);

/**
 * Returns an error code of 'Pinhole Space Exhausted'.
 * This error code is "701".
 * @return  "701" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_pinhole_space_exhausted(void);

/**
 * Returns an error code of 'Firewall Disabled'.
 * This error code is "702".
 * @return  "702" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_firewall_disabled(void);

/**
 * Returns an error code of 'Inbound Pinhole Not Allowed'.
 * This error code is "703".
 * @return  "703" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_inbound_pinhole_not_allowed(void);

/**
 * Returns an error code of 'No Such Entry'.
 * This error code is "704".
 * @return  "704" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_no_such_entry(void);

/**
 * Returns an error code of 'Protocol Not Supported'.
 * This error code is "705".
 * @return  "705" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_protocol_not_supported(void);

/**
 * Returns an error code of 'Internal Port Wildcarding Not Allowed'.
 * This error code is "706".
 * @return  "706" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_internal_port_wildcarding_not_allowed(void);

/**
 * Returns an error code of 'Protocol Wildcarding Not Allowed'.
 * This error code is "707".
 * @return  "707" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_protocol_wildcarding_not_allowed(void);

/**
 * Returns an error code of 'Wild Card Not Permitted In Src IP'.
 * This error code is "708".
 * @return  "708" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_wild_card_not_permitted_in_src_ip(void);

/**
 * Returns an error code of 'No Traffic Received'.
 * This error code is "709".
 * @return  "709" string.
 */
extern const du_uchar* digd_wanipv6fc_error_code_no_traffic_received(void);

#ifdef __cplusplus
}
#endif

#endif
