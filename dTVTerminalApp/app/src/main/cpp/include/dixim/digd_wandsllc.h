/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wandsllc.h
 *  @brief The digd_wandsllc interface provides methods for making SOAP request/response messages of
 *   WANCableLinkConfig actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANDSLLinkConfig actions.
 *  @see  WANDSLLinkConfig:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_WANDSLLC_H
#define DIGD_WANDSLLC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetDSLLinkType.
  * @return the action name of SetDSLLinkType.
  */
extern const du_uchar* digd_wandsllc_action_name_set_dsl_link_type(void);

/**
 * Appends a SOAP request message of SetDSLLinkType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_link_type value of NewLinkType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_dsl_link_type(du_uchar_array* xml, du_uint32 v, const du_uchar* new_link_type);

/**
 * Parses a SOAP request message of SetDSLLinkType action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_link_type pointer to the storage location for NewLinkType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_set_dsl_link_type(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_link_type);

/**
 * Appends a SOAP response message of SetDSLLinkType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_dsl_link_type_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDSLLinkType action, stores each name and
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
extern du_bool digd_wandsllc_parse_set_dsl_link_type_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDSLLinkInfo.
  * @return the action name of GetDSLLinkInfo.
  */
extern const du_uchar* digd_wandsllc_action_name_get_dsl_link_info(void);

/**
 * Appends a SOAP request message of GetDSLLinkInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_dsl_link_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDSLLinkInfo action, stores each name and
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
extern du_bool digd_wandsllc_parse_get_dsl_link_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDSLLinkInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_link_type value of NewLinkType argument.
 * @param[in] new_link_status value of NewLinkStatus argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_dsl_link_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_link_type, const du_uchar* new_link_status);

/**
 * Parses a SOAP response message of GetDSLLinkInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_link_type pointer to the storage location for NewLinkType argument value.
 * @param[out] new_link_status pointer to the storage location for NewLinkStatus argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_get_dsl_link_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_link_type, const du_uchar** new_link_status);

/**
  * Returns the action name of GetAutoConfig.
  * @return the action name of GetAutoConfig.
  */
extern const du_uchar* digd_wandsllc_action_name_get_auto_config(void);

/**
 * Appends a SOAP request message of GetAutoConfig action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_auto_config(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetAutoConfig action, stores each name and
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
extern du_bool digd_wandsllc_parse_get_auto_config(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetAutoConfig action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_auto_config value of NewAutoConfig argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_auto_config_response(du_uchar_array* xml, du_uint32 v, du_bool new_auto_config);

/**
 * Parses a SOAP response message of GetAutoConfig action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_auto_config pointer to the storage location for NewAutoConfig argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_get_auto_config_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_auto_config);

/**
  * Returns the action name of GetModulationType.
  * @return the action name of GetModulationType.
  */
extern const du_uchar* digd_wandsllc_action_name_get_modulation_type(void);

/**
 * Appends a SOAP request message of GetModulationType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_modulation_type(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetModulationType action, stores each name and
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
extern du_bool digd_wandsllc_parse_get_modulation_type(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetModulationType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_modulation_type value of NewModulationType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_modulation_type_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_modulation_type);

/**
 * Parses a SOAP response message of GetModulationType action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_modulation_type pointer to the storage location for NewModulationType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_get_modulation_type_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_modulation_type);

/**
  * Returns the action name of SetDestinationAddress.
  * @return the action name of SetDestinationAddress.
  */
extern const du_uchar* digd_wandsllc_action_name_set_destination_address(void);

/**
 * Appends a SOAP request message of SetDestinationAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_destination_address value of NewDestinationAddress argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_destination_address(du_uchar_array* xml, du_uint32 v, const du_uchar* new_destination_address);

/**
 * Parses a SOAP request message of SetDestinationAddress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_destination_address pointer to the storage location for NewDestinationAddress argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_set_destination_address(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_destination_address);

/**
 * Appends a SOAP response message of SetDestinationAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_destination_address_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDestinationAddress action, stores each name and
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
extern du_bool digd_wandsllc_parse_set_destination_address_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDestinationAddress.
  * @return the action name of GetDestinationAddress.
  */
extern const du_uchar* digd_wandsllc_action_name_get_destination_address(void);

/**
 * Appends a SOAP request message of GetDestinationAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_destination_address(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDestinationAddress action, stores each name and
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
extern du_bool digd_wandsllc_parse_get_destination_address(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDestinationAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_destination_address value of NewDestinationAddress argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_destination_address_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_destination_address);

/**
 * Parses a SOAP response message of GetDestinationAddress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_destination_address pointer to the storage location for NewDestinationAddress argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_get_destination_address_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_destination_address);

/**
  * Returns the action name of SetATMEncapsulation.
  * @return the action name of SetATMEncapsulation.
  */
extern const du_uchar* digd_wandsllc_action_name_set_atm_encapsulation(void);

/**
 * Appends a SOAP request message of SetATMEncapsulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_atm_encapsulation value of NewATMEncapsulation argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_atm_encapsulation(du_uchar_array* xml, du_uint32 v, const du_uchar* new_atm_encapsulation);

/**
 * Parses a SOAP request message of SetATMEncapsulation action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_atm_encapsulation pointer to the storage location for NewATMEncapsulation argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_set_atm_encapsulation(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_atm_encapsulation);

/**
 * Appends a SOAP response message of SetATMEncapsulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_atm_encapsulation_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetATMEncapsulation action, stores each name and
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
extern du_bool digd_wandsllc_parse_set_atm_encapsulation_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetATMEncapsulation.
  * @return the action name of GetATMEncapsulation.
  */
extern const du_uchar* digd_wandsllc_action_name_get_atm_encapsulation(void);

/**
 * Appends a SOAP request message of GetATMEncapsulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_atm_encapsulation(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetATMEncapsulation action, stores each name and
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
extern du_bool digd_wandsllc_parse_get_atm_encapsulation(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetATMEncapsulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_atm_encapsulation value of NewATMEncapsulation argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_atm_encapsulation_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_atm_encapsulation);

/**
 * Parses a SOAP response message of GetATMEncapsulation action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_atm_encapsulation pointer to the storage location for NewATMEncapsulation argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_get_atm_encapsulation_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_atm_encapsulation);

/**
  * Returns the action name of SetFCSPreserved.
  * @return the action name of SetFCSPreserved.
  */
extern const du_uchar* digd_wandsllc_action_name_set_fcs_preserved(void);

/**
 * Appends a SOAP request message of SetFCSPreserved action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_fcs_preserved value of NewFCSPreserved argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_fcs_preserved(du_uchar_array* xml, du_uint32 v, du_bool new_fcs_preserved);

/**
 * Parses a SOAP request message of SetFCSPreserved action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_fcs_preserved pointer to the storage location for NewFCSPreserved argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_set_fcs_preserved(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_fcs_preserved);

/**
 * Appends a SOAP response message of SetFCSPreserved action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_set_fcs_preserved_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetFCSPreserved action, stores each name and
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
extern du_bool digd_wandsllc_parse_set_fcs_preserved_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetFCSPreserved.
  * @return the action name of GetFCSPreserved.
  */
extern const du_uchar* digd_wandsllc_action_name_get_fcs_preserved(void);

/**
 * Appends a SOAP request message of GetFCSPreserved action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_fcs_preserved(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetFCSPreserved action, stores each name and
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
extern du_bool digd_wandsllc_parse_get_fcs_preserved(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetFCSPreserved action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_fcs_preserved value of NewFCSPreserved argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wandsllc_make_get_fcs_preserved_response(du_uchar_array* xml, du_uint32 v, du_bool new_fcs_preserved);

/**
 * Parses a SOAP response message of GetFCSPreserved action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_fcs_preserved pointer to the storage location for NewFCSPreserved argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wandsllc_parse_get_fcs_preserved_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_fcs_preserved);

/**
 * the state variable name of 'LinkType'.
 * @return  "LinkType" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_link_type(void);

/**
 * the state variable name of 'LinkStatus'.
 * @return  "LinkStatus" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_link_status(void);

/**
 * the state variable name of 'ModulationType'.
 * @return  "ModulationType" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_modulation_type(void);

/**
 * the state variable name of 'DestinationAddress'.
 * @return  "DestinationAddress" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_destination_address(void);

/**
 * the state variable name of 'ATMEncapsulation'.
 * @return  "ATMEncapsulation" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_atm_encapsulation(void);

/**
 * the state variable name of 'FCSPreserved'.
 * @return  "FCSPreserved" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_fcs_preserved(void);

/**
 * the state variable name of 'AutoConfig'.
 * @return  "AutoConfig" string.
 */
extern const du_uchar* digd_wandsllc_state_variable_name_auto_config(void);

#ifdef __cplusplus
}
#endif

#endif
