/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wanclc.h
 *  @brief The digd_wanclc interface provides methods for making SOAP request/response messages of
 *   WANCableLinkConfig actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANCableLinkConfig actions.
 *  @see  WANCableLinkConfig:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_WANCLC_H
#define DIGD_WANCLC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of GetCableLinkConfigInfo.
  * @return the action name of GetCableLinkConfigInfo.
  */
extern const du_uchar* digd_wanclc_action_name_get_cable_link_config_info(void);

/**
 * Appends a SOAP request message of GetCableLinkConfigInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_cable_link_config_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetCableLinkConfigInfo action, stores each name and
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
extern du_bool digd_wanclc_parse_get_cable_link_config_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetCableLinkConfigInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_cable_link_config_state value of NewCableLinkConfigState argument.
 * @param[in] new_link_type value of NewLinkType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_cable_link_config_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_cable_link_config_state, const du_uchar* new_link_type);

/**
 * Parses a SOAP response message of GetCableLinkConfigInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_cable_link_config_state pointer to the storage location for NewCableLinkConfigState argument value.
 * @param[out] new_link_type pointer to the storage location for NewLinkType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_cable_link_config_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_cable_link_config_state, const du_uchar** new_link_type);

/**
  * Returns the action name of GetDownstreamFrequency.
  * @return the action name of GetDownstreamFrequency.
  */
extern const du_uchar* digd_wanclc_action_name_get_downstream_frequency(void);

/**
 * Appends a SOAP request message of GetDownstreamFrequency action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_downstream_frequency(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDownstreamFrequency action, stores each name and
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
extern du_bool digd_wanclc_parse_get_downstream_frequency(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDownstreamFrequency action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_downstream_frequency value of NewDownstreamFrequency argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_downstream_frequency_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_downstream_frequency);

/**
 * Parses a SOAP response message of GetDownstreamFrequency action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_downstream_frequency pointer to the storage location for NewDownstreamFrequency argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_downstream_frequency_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_downstream_frequency);

/**
  * Returns the action name of GetDownstreamModulation.
  * @return the action name of GetDownstreamModulation.
  */
extern const du_uchar* digd_wanclc_action_name_get_downstream_modulation(void);

/**
 * Appends a SOAP request message of GetDownstreamModulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_downstream_modulation(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDownstreamModulation action, stores each name and
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
extern du_bool digd_wanclc_parse_get_downstream_modulation(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDownstreamModulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_downstream_modulation value of NewDownstreamModulation argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_downstream_modulation_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_downstream_modulation);

/**
 * Parses a SOAP response message of GetDownstreamModulation action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_downstream_modulation pointer to the storage location for NewDownstreamModulation argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_downstream_modulation_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_downstream_modulation);

/**
  * Returns the action name of GetUpstreamFrequency.
  * @return the action name of GetUpstreamFrequency.
  */
extern const du_uchar* digd_wanclc_action_name_get_upstream_frequency(void);

/**
 * Appends a SOAP request message of GetUpstreamFrequency action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_frequency(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetUpstreamFrequency action, stores each name and
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
extern du_bool digd_wanclc_parse_get_upstream_frequency(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetUpstreamFrequency action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_upstream_frequency value of NewUpstreamFrequency argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_frequency_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_upstream_frequency);

/**
 * Parses a SOAP response message of GetUpstreamFrequency action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_upstream_frequency pointer to the storage location for NewUpstreamFrequency argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_upstream_frequency_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_upstream_frequency);

/**
  * Returns the action name of GetUpstreamModulation.
  * @return the action name of GetUpstreamModulation.
  */
extern const du_uchar* digd_wanclc_action_name_get_upstream_modulation(void);

/**
 * Appends a SOAP request message of GetUpstreamModulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_modulation(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetUpstreamModulation action, stores each name and
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
extern du_bool digd_wanclc_parse_get_upstream_modulation(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetUpstreamModulation action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_upstream_modulation value of NewUpstreamModulation argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_modulation_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_upstream_modulation);

/**
 * Parses a SOAP response message of GetUpstreamModulation action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_upstream_modulation pointer to the storage location for NewUpstreamModulation argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_upstream_modulation_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_upstream_modulation);

/**
  * Returns the action name of GetUpstreamChannelID.
  * @return the action name of GetUpstreamChannelID.
  */
extern const du_uchar* digd_wanclc_action_name_get_upstream_channel_id(void);

/**
 * Appends a SOAP request message of GetUpstreamChannelID action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_channel_id(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetUpstreamChannelID action, stores each name and
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
extern du_bool digd_wanclc_parse_get_upstream_channel_id(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetUpstreamChannelID action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_upstream_channel_id value of NewUpstreamChannelID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_channel_id_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_upstream_channel_id);

/**
 * Parses a SOAP response message of GetUpstreamChannelID action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_upstream_channel_id pointer to the storage location for NewUpstreamChannelID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_upstream_channel_id_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_upstream_channel_id);

/**
  * Returns the action name of GetUpstreamPowerLevel.
  * @return the action name of GetUpstreamPowerLevel.
  */
extern const du_uchar* digd_wanclc_action_name_get_upstream_power_level(void);

/**
 * Appends a SOAP request message of GetUpstreamPowerLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_power_level(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetUpstreamPowerLevel action, stores each name and
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
extern du_bool digd_wanclc_parse_get_upstream_power_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetUpstreamPowerLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_upstream_power_level value of NewUpstreamPowerLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_upstream_power_level_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_upstream_power_level);

/**
 * Parses a SOAP response message of GetUpstreamPowerLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_upstream_power_level pointer to the storage location for NewUpstreamPowerLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_upstream_power_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_upstream_power_level);

/**
  * Returns the action name of GetBPIEncryptionEnabled.
  * @return the action name of GetBPIEncryptionEnabled.
  */
extern const du_uchar* digd_wanclc_action_name_get_bpi_encryption_enabled(void);

/**
 * Appends a SOAP request message of GetBPIEncryptionEnabled action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_bpi_encryption_enabled(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetBPIEncryptionEnabled action, stores each name and
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
extern du_bool digd_wanclc_parse_get_bpi_encryption_enabled(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetBPIEncryptionEnabled action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_bpi_encryption_enabled value of NewBPIEncryptionEnabled argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_bpi_encryption_enabled_response(du_uchar_array* xml, du_uint32 v, du_bool new_bpi_encryption_enabled);

/**
 * Parses a SOAP response message of GetBPIEncryptionEnabled action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_bpi_encryption_enabled pointer to the storage location for NewBPIEncryptionEnabled argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_bpi_encryption_enabled_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_bpi_encryption_enabled);

/**
  * Returns the action name of GetConfigFile.
  * @return the action name of GetConfigFile.
  */
extern const du_uchar* digd_wanclc_action_name_get_config_file(void);

/**
 * Appends a SOAP request message of GetConfigFile action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_config_file(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetConfigFile action, stores each name and
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
extern du_bool digd_wanclc_parse_get_config_file(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetConfigFile action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_config_file value of NewConfigFile argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_config_file_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_config_file);

/**
 * Parses a SOAP response message of GetConfigFile action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_config_file pointer to the storage location for NewConfigFile argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_config_file_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_config_file);

/**
  * Returns the action name of GetTFTPServer.
  * @return the action name of GetTFTPServer.
  */
extern const du_uchar* digd_wanclc_action_name_get_tftp_server(void);

/**
 * Appends a SOAP request message of GetTFTPServer action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_tftp_server(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetTFTPServer action, stores each name and
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
extern du_bool digd_wanclc_parse_get_tftp_server(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetTFTPServer action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_tftp_server value of NewTFTPServer argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanclc_make_get_tftp_server_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_tftp_server);

/**
 * Parses a SOAP response message of GetTFTPServer action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_tftp_server pointer to the storage location for NewTFTPServer argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanclc_parse_get_tftp_server_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_tftp_server);

/**
 * the state variable name of 'CableLinkConfigState'.
 * @return  "CableLinkConfigState" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_cable_link_config_state(void);

/**
 * the state variable name of 'LinkType'.
 * @return  "LinkType" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_link_type(void);

/**
 * the state variable name of 'DownstreamFrequency'.
 * @return  "DownstreamFrequency" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_downstream_frequency(void);

/**
 * the state variable name of 'DownstreamModulation'.
 * @return  "DownstreamModulation" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_downstream_modulation(void);

/**
 * the state variable name of 'UpstreamFrequency'.
 * @return  "UpstreamFrequency" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_upstream_frequency(void);

/**
 * the state variable name of 'UpstreamModulation'.
 * @return  "UpstreamModulation" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_upstream_modulation(void);

/**
 * the state variable name of 'UpstreamChannelID'.
 * @return  "UpstreamChannelID" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_upstream_channel_id(void);

/**
 * the state variable name of 'UpstreamPowerLevel'.
 * @return  "UpstreamPowerLevel" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_upstream_power_level(void);

/**
 * the state variable name of 'ConfigFile'.
 * @return  "ConfigFile" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_config_file(void);

/**
 * the state variable name of 'TFTPServer'.
 * @return  "TFTPServer" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_tftp_server(void);

/**
 * the state variable name of 'BPIEncryptionEnabled'.
 * @return  "BPIEncryptionEnabled" string.
 */
extern const du_uchar* digd_wanclc_state_variable_name_bpi_encryption_enabled(void);

#ifdef __cplusplus
}
#endif

#endif
