/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wanelc.h
 *  @brief The digd_wanelc interface provides methods for making SOAP request/response messages of
 *   WANEthernetLinkConfig actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANEthernetLinkConfig actions.
 *  @see  WANEthernetLinkConfig:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_WANELC_H
#define DIGD_WANELC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of GetEthernetLinkStatus.
  * @return the action name of GetEthernetLinkStatus.
  */
extern const du_uchar* digd_wanelc_action_name_get_ethernet_link_status(void);

/**
 * Appends a SOAP request message of GetEthernetLinkStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanelc_make_get_ethernet_link_status(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetEthernetLinkStatus action, stores each name and
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
extern du_bool digd_wanelc_parse_get_ethernet_link_status(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetEthernetLinkStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ethernet_link_status value of NewEthernetLinkStatus argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanelc_make_get_ethernet_link_status_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ethernet_link_status);

/**
 * Parses a SOAP response message of GetEthernetLinkStatus action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ethernet_link_status pointer to the storage location for NewEthernetLinkStatus argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanelc_parse_get_ethernet_link_status_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ethernet_link_status);

/**
 * the state variable name of 'EthernetLinkStatus'.
 * @return  "EthernetLinkStatus" string.
 */
extern const du_uchar* digd_wanelc_state_variable_name_ethernet_link_status(void);

#ifdef __cplusplus
}
#endif

#endif
