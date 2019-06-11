/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_lanhcm.h
 *  @brief The digd_lanhcm interface provides methods for making SOAP request/response messages of
 *   LANHostConfigManagement actions. This interface provides methods for parsing SOAP request/response
 *   messages of LANHostConfigManagement actions.
 *  @see  LANHostConfigManagement:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_LANHCM_H
#define DIGD_LANHCM_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetDHCPServerConfigurable.
  * @return the action name of SetDHCPServerConfigurable.
  */
extern const du_uchar* digd_lanhcm_action_name_set_dhcp_server_configurable(void);

/**
 * Appends a SOAP request message of SetDHCPServerConfigurable action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dhcp_server_configurable value of NewDHCPServerConfigurable argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_dhcp_server_configurable(du_uchar_array* xml, du_uint32 v, du_bool new_dhcp_server_configurable);

/**
 * Parses a SOAP request message of SetDHCPServerConfigurable action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dhcp_server_configurable pointer to the storage location for NewDHCPServerConfigurable argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_dhcp_server_configurable(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_dhcp_server_configurable);

/**
 * Appends a SOAP response message of SetDHCPServerConfigurable action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_dhcp_server_configurable_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDHCPServerConfigurable action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_dhcp_server_configurable_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDHCPServerConfigurable.
  * @return the action name of GetDHCPServerConfigurable.
  */
extern const du_uchar* digd_lanhcm_action_name_get_dhcp_server_configurable(void);

/**
 * Appends a SOAP request message of GetDHCPServerConfigurable action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_dhcp_server_configurable(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDHCPServerConfigurable action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_dhcp_server_configurable(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDHCPServerConfigurable action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dhcp_server_configurable value of NewDHCPServerConfigurable argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_dhcp_server_configurable_response(du_uchar_array* xml, du_uint32 v, du_bool new_dhcp_server_configurable);

/**
 * Parses a SOAP response message of GetDHCPServerConfigurable action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dhcp_server_configurable pointer to the storage location for NewDHCPServerConfigurable argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_dhcp_server_configurable_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_dhcp_server_configurable);

/**
  * Returns the action name of SetDHCPRelay.
  * @return the action name of SetDHCPRelay.
  */
extern const du_uchar* digd_lanhcm_action_name_set_dhcp_relay(void);

/**
 * Appends a SOAP request message of SetDHCPRelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dhcp_relay value of NewDHCPRelay argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_dhcp_relay(du_uchar_array* xml, du_uint32 v, du_bool new_dhcp_relay);

/**
 * Parses a SOAP request message of SetDHCPRelay action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dhcp_relay pointer to the storage location for NewDHCPRelay argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_dhcp_relay(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_dhcp_relay);

/**
 * Appends a SOAP response message of SetDHCPRelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_dhcp_relay_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDHCPRelay action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_dhcp_relay_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDHCPRelay.
  * @return the action name of GetDHCPRelay.
  */
extern const du_uchar* digd_lanhcm_action_name_get_dhcp_relay(void);

/**
 * Appends a SOAP request message of GetDHCPRelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_dhcp_relay(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDHCPRelay action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_dhcp_relay(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDHCPRelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dhcp_relay value of NewDHCPRelay argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_dhcp_relay_response(du_uchar_array* xml, du_uint32 v, du_bool new_dhcp_relay);

/**
 * Parses a SOAP response message of GetDHCPRelay action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dhcp_relay pointer to the storage location for NewDHCPRelay argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_dhcp_relay_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_dhcp_relay);

/**
  * Returns the action name of SetSubnetMask.
  * @return the action name of SetSubnetMask.
  */
extern const du_uchar* digd_lanhcm_action_name_set_subnet_mask(void);

/**
 * Appends a SOAP request message of SetSubnetMask action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_subnet_mask value of NewSubnetMask argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_subnet_mask(du_uchar_array* xml, du_uint32 v, const du_uchar* new_subnet_mask);

/**
 * Parses a SOAP request message of SetSubnetMask action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_subnet_mask pointer to the storage location for NewSubnetMask argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_subnet_mask(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_subnet_mask);

/**
 * Appends a SOAP response message of SetSubnetMask action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_subnet_mask_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetSubnetMask action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_subnet_mask_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetSubnetMask.
  * @return the action name of GetSubnetMask.
  */
extern const du_uchar* digd_lanhcm_action_name_get_subnet_mask(void);

/**
 * Appends a SOAP request message of GetSubnetMask action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_subnet_mask(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetSubnetMask action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_subnet_mask(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetSubnetMask action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_subnet_mask value of NewSubnetMask argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_subnet_mask_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_subnet_mask);

/**
 * Parses a SOAP response message of GetSubnetMask action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_subnet_mask pointer to the storage location for NewSubnetMask argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_subnet_mask_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_subnet_mask);

/**
  * Returns the action name of SetIPRouter.
  * @return the action name of SetIPRouter.
  */
extern const du_uchar* digd_lanhcm_action_name_set_ip_router(void);

/**
 * Appends a SOAP request message of SetIPRouter action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ip_routers value of NewIPRouters argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_ip_router(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ip_routers);

/**
 * Parses a SOAP request message of SetIPRouter action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ip_routers pointer to the storage location for NewIPRouters argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_ip_router(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ip_routers);

/**
 * Appends a SOAP response message of SetIPRouter action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_ip_router_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetIPRouter action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_ip_router_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeleteIPRouter.
  * @return the action name of DeleteIPRouter.
  */
extern const du_uchar* digd_lanhcm_action_name_delete_ip_router(void);

/**
 * Appends a SOAP request message of DeleteIPRouter action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ip_routers value of NewIPRouters argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_delete_ip_router(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ip_routers);

/**
 * Parses a SOAP request message of DeleteIPRouter action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ip_routers pointer to the storage location for NewIPRouters argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_delete_ip_router(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ip_routers);

/**
 * Appends a SOAP response message of DeleteIPRouter action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_delete_ip_router_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeleteIPRouter action, stores each name and
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
extern du_bool digd_lanhcm_parse_delete_ip_router_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetIPRoutersList.
  * @return the action name of GetIPRoutersList.
  */
extern const du_uchar* digd_lanhcm_action_name_get_ip_routers_list(void);

/**
 * Appends a SOAP request message of GetIPRoutersList action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_ip_routers_list(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetIPRoutersList action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_ip_routers_list(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetIPRoutersList action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ip_routers value of NewIPRouters argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_ip_routers_list_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ip_routers);

/**
 * Parses a SOAP response message of GetIPRoutersList action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ip_routers pointer to the storage location for NewIPRouters argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_ip_routers_list_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ip_routers);

/**
  * Returns the action name of SetDomainName.
  * @return the action name of SetDomainName.
  */
extern const du_uchar* digd_lanhcm_action_name_set_domain_name(void);

/**
 * Appends a SOAP request message of SetDomainName action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_domain_name value of NewDomainName argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_domain_name(du_uchar_array* xml, du_uint32 v, const du_uchar* new_domain_name);

/**
 * Parses a SOAP request message of SetDomainName action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_domain_name pointer to the storage location for NewDomainName argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_domain_name(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_domain_name);

/**
 * Appends a SOAP response message of SetDomainName action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_domain_name_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDomainName action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_domain_name_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDomainName.
  * @return the action name of GetDomainName.
  */
extern const du_uchar* digd_lanhcm_action_name_get_domain_name(void);

/**
 * Appends a SOAP request message of GetDomainName action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_domain_name(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDomainName action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_domain_name(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDomainName action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_domain_name value of NewDomainName argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_domain_name_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_domain_name);

/**
 * Parses a SOAP response message of GetDomainName action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_domain_name pointer to the storage location for NewDomainName argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_domain_name_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_domain_name);

/**
  * Returns the action name of SetAddressRange.
  * @return the action name of SetAddressRange.
  */
extern const du_uchar* digd_lanhcm_action_name_set_address_range(void);

/**
 * Appends a SOAP request message of SetAddressRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_min_address value of NewMinAddress argument.
 * @param[in] new_max_address value of NewMaxAddress argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_address_range(du_uchar_array* xml, du_uint32 v, const du_uchar* new_min_address, const du_uchar* new_max_address);

/**
 * Parses a SOAP request message of SetAddressRange action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_min_address pointer to the storage location for NewMinAddress argument value.
 * @param[out] new_max_address pointer to the storage location for NewMaxAddress argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_address_range(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_min_address, const du_uchar** new_max_address);

/**
 * Appends a SOAP response message of SetAddressRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_address_range_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetAddressRange action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_address_range_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetAddressRange.
  * @return the action name of GetAddressRange.
  */
extern const du_uchar* digd_lanhcm_action_name_get_address_range(void);

/**
 * Appends a SOAP request message of GetAddressRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_address_range(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetAddressRange action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_address_range(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetAddressRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_min_address value of NewMinAddress argument.
 * @param[in] new_max_address value of NewMaxAddress argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_address_range_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_min_address, const du_uchar* new_max_address);

/**
 * Parses a SOAP response message of GetAddressRange action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_min_address pointer to the storage location for NewMinAddress argument value.
 * @param[out] new_max_address pointer to the storage location for NewMaxAddress argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_address_range_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_min_address, const du_uchar** new_max_address);

/**
  * Returns the action name of SetReservedAddress.
  * @return the action name of SetReservedAddress.
  */
extern const du_uchar* digd_lanhcm_action_name_set_reserved_address(void);

/**
 * Appends a SOAP request message of SetReservedAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_reserved_addresses value of NewReservedAddresses argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_reserved_address(du_uchar_array* xml, du_uint32 v, const du_uchar* new_reserved_addresses);

/**
 * Parses a SOAP request message of SetReservedAddress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_reserved_addresses pointer to the storage location for NewReservedAddresses argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_reserved_address(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_reserved_addresses);

/**
 * Appends a SOAP response message of SetReservedAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_reserved_address_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetReservedAddress action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_reserved_address_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeleteReservedAddress.
  * @return the action name of DeleteReservedAddress.
  */
extern const du_uchar* digd_lanhcm_action_name_delete_reserved_address(void);

/**
 * Appends a SOAP request message of DeleteReservedAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_reserved_addresses value of NewReservedAddresses argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_delete_reserved_address(du_uchar_array* xml, du_uint32 v, const du_uchar* new_reserved_addresses);

/**
 * Parses a SOAP request message of DeleteReservedAddress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_reserved_addresses pointer to the storage location for NewReservedAddresses argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_delete_reserved_address(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_reserved_addresses);

/**
 * Appends a SOAP response message of DeleteReservedAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_delete_reserved_address_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeleteReservedAddress action, stores each name and
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
extern du_bool digd_lanhcm_parse_delete_reserved_address_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetReservedAddresses.
  * @return the action name of GetReservedAddresses.
  */
extern const du_uchar* digd_lanhcm_action_name_get_reserved_addresses(void);

/**
 * Appends a SOAP request message of GetReservedAddresses action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_reserved_addresses(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetReservedAddresses action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_reserved_addresses(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetReservedAddresses action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_reserved_addresses value of NewReservedAddresses argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_reserved_addresses_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_reserved_addresses);

/**
 * Parses a SOAP response message of GetReservedAddresses action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_reserved_addresses pointer to the storage location for NewReservedAddresses argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_reserved_addresses_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_reserved_addresses);

/**
  * Returns the action name of SetDNSServer.
  * @return the action name of SetDNSServer.
  */
extern const du_uchar* digd_lanhcm_action_name_set_dns_server(void);

/**
 * Appends a SOAP request message of SetDNSServer action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dns_servers value of NewDNSServers argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_dns_server(du_uchar_array* xml, du_uint32 v, const du_uchar* new_dns_servers);

/**
 * Parses a SOAP request message of SetDNSServer action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dns_servers pointer to the storage location for NewDNSServers argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_set_dns_server(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_dns_servers);

/**
 * Appends a SOAP response message of SetDNSServer action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_set_dns_server_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDNSServer action, stores each name and
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
extern du_bool digd_lanhcm_parse_set_dns_server_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeleteDNSServer.
  * @return the action name of DeleteDNSServer.
  */
extern const du_uchar* digd_lanhcm_action_name_delete_dns_server(void);

/**
 * Appends a SOAP request message of DeleteDNSServer action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dns_servers value of NewDNSServers argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_delete_dns_server(du_uchar_array* xml, du_uint32 v, const du_uchar* new_dns_servers);

/**
 * Parses a SOAP request message of DeleteDNSServer action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dns_servers pointer to the storage location for NewDNSServers argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_delete_dns_server(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_dns_servers);

/**
 * Appends a SOAP response message of DeleteDNSServer action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_delete_dns_server_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeleteDNSServer action, stores each name and
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
extern du_bool digd_lanhcm_parse_delete_dns_server_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDNSServers.
  * @return the action name of GetDNSServers.
  */
extern const du_uchar* digd_lanhcm_action_name_get_dns_servers(void);

/**
 * Appends a SOAP request message of GetDNSServers action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_dns_servers(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDNSServers action, stores each name and
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
extern du_bool digd_lanhcm_parse_get_dns_servers(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDNSServers action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_dns_servers value of NewDNSServers argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_lanhcm_make_get_dns_servers_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_dns_servers);

/**
 * Parses a SOAP response message of GetDNSServers action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_dns_servers pointer to the storage location for NewDNSServers argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_lanhcm_parse_get_dns_servers_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_dns_servers);

/**
 * the state variable name of 'DHCPServerConfigurable'.
 * @return  "DHCPServerConfigurable" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_dhcp_server_configurable(void);

/**
 * the state variable name of 'DHCPRelay'.
 * @return  "DHCPRelay" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_dhcp_relay(void);

/**
 * the state variable name of 'SubnetMask'.
 * @return  "SubnetMask" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_subnet_mask(void);

/**
 * the state variable name of 'IPRouters'.
 * @return  "IPRouters" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_ip_routers(void);

/**
 * the state variable name of 'DNSServers'.
 * @return  "DNSServers" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_dns_servers(void);

/**
 * the state variable name of 'DomainName'.
 * @return  "DomainName" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_domain_name(void);

/**
 * the state variable name of 'MinAddress'.
 * @return  "MinAddress" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_min_address(void);

/**
 * the state variable name of 'MaxAddress'.
 * @return  "MaxAddress" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_max_address(void);

/**
 * the state variable name of 'ReservedAddresses'.
 * @return  "ReservedAddresses" string.
 */
extern const du_uchar* digd_lanhcm_state_variable_name_reserved_addresses(void);

#ifdef __cplusplus
}
#endif

#endif
