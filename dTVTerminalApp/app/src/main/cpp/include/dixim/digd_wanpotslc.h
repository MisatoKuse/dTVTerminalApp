/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wanpotslc.h
 *  @brief The digd_wanpotslc interface provides methods for making SOAP request/response messages of
 *   WANPOTSLinkConfig actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANPOTSLinkConfig actions.
 *  @see  WANPOTSLinkConfig:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_WANPOTSLC_H
#define DIGD_WANPOTSLC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetISPInfo.
  * @return the action name of SetISPInfo.
  */
extern const du_uchar* digd_wanpotslc_action_name_set_isp_info(void);

/**
 * Appends a SOAP request message of SetISPInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_isp_phone_number value of NewISPPhoneNumber argument.
 * @param[in] new_isp_info value of NewISPInfo argument.
 * @param[in] new_link_type value of NewLinkType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_set_isp_info(du_uchar_array* xml, du_uint32 v, const du_uchar* new_isp_phone_number, const du_uchar* new_isp_info, const du_uchar* new_link_type);

/**
 * Parses a SOAP request message of SetISPInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_isp_phone_number pointer to the storage location for NewISPPhoneNumber argument value.
 * @param[out] new_isp_info pointer to the storage location for NewISPInfo argument value.
 * @param[out] new_link_type pointer to the storage location for NewLinkType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_set_isp_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_isp_phone_number, const du_uchar** new_isp_info, const du_uchar** new_link_type);

/**
 * Appends a SOAP response message of SetISPInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_set_isp_info_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetISPInfo action, stores each name and
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
extern du_bool digd_wanpotslc_parse_set_isp_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetCallRetryInfo.
  * @return the action name of SetCallRetryInfo.
  */
extern const du_uchar* digd_wanpotslc_action_name_set_call_retry_info(void);

/**
 * Appends a SOAP request message of SetCallRetryInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_number_of_retries value of NewNumberOfRetries argument.
 * @param[in] new_delay_between_retries value of NewDelayBetweenRetries argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_set_call_retry_info(du_uchar_array* xml, du_uint32 v, du_uint32 new_number_of_retries, du_uint32 new_delay_between_retries);

/**
 * Parses a SOAP request message of SetCallRetryInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_number_of_retries pointer to the storage location for NewNumberOfRetries argument value.
 * @param[out] new_delay_between_retries pointer to the storage location for NewDelayBetweenRetries argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_set_call_retry_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_number_of_retries, du_uint32* new_delay_between_retries);

/**
 * Appends a SOAP response message of SetCallRetryInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_set_call_retry_info_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetCallRetryInfo action, stores each name and
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
extern du_bool digd_wanpotslc_parse_set_call_retry_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetISPInfo.
  * @return the action name of GetISPInfo.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_isp_info(void);

/**
 * Appends a SOAP request message of GetISPInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_isp_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetISPInfo action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_isp_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetISPInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_isp_phone_number value of NewISPPhoneNumber argument.
 * @param[in] new_isp_info value of NewISPInfo argument.
 * @param[in] new_link_type value of NewLinkType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_isp_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_isp_phone_number, const du_uchar* new_isp_info, const du_uchar* new_link_type);

/**
 * Parses a SOAP response message of GetISPInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_isp_phone_number pointer to the storage location for NewISPPhoneNumber argument value.
 * @param[out] new_isp_info pointer to the storage location for NewISPInfo argument value.
 * @param[out] new_link_type pointer to the storage location for NewLinkType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_isp_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_isp_phone_number, const du_uchar** new_isp_info, const du_uchar** new_link_type);

/**
  * Returns the action name of GetCallRetryInfo.
  * @return the action name of GetCallRetryInfo.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_call_retry_info(void);

/**
 * Appends a SOAP request message of GetCallRetryInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_call_retry_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetCallRetryInfo action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_call_retry_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetCallRetryInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_number_of_retries value of NewNumberOfRetries argument.
 * @param[in] new_delay_between_retries value of NewDelayBetweenRetries argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_call_retry_info_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_number_of_retries, du_uint32 new_delay_between_retries);

/**
 * Parses a SOAP response message of GetCallRetryInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_number_of_retries pointer to the storage location for NewNumberOfRetries argument value.
 * @param[out] new_delay_between_retries pointer to the storage location for NewDelayBetweenRetries argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_call_retry_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_number_of_retries, du_uint32* new_delay_between_retries);

/**
  * Returns the action name of GetFclass.
  * @return the action name of GetFclass.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_fclass(void);

/**
 * Appends a SOAP request message of GetFclass action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_fclass(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetFclass action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_fclass(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetFclass action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_fclass value of NewFclass argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_fclass_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_fclass);

/**
 * Parses a SOAP response message of GetFclass action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_fclass pointer to the storage location for NewFclass argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_fclass_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_fclass);

/**
  * Returns the action name of GetDataModulationSupported.
  * @return the action name of GetDataModulationSupported.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_data_modulation_supported(void);

/**
 * Appends a SOAP request message of GetDataModulationSupported action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_data_modulation_supported(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDataModulationSupported action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_data_modulation_supported(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDataModulationSupported action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_data_modulation_supported value of NewDataModulationSupported argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_data_modulation_supported_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_data_modulation_supported);

/**
 * Parses a SOAP response message of GetDataModulationSupported action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_data_modulation_supported pointer to the storage location for NewDataModulationSupported argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_data_modulation_supported_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_data_modulation_supported);

/**
  * Returns the action name of GetDataProtocol.
  * @return the action name of GetDataProtocol.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_data_protocol(void);

/**
 * Appends a SOAP request message of GetDataProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_data_protocol(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDataProtocol action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_data_protocol(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDataProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_data_protocol value of NewDataProtocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_data_protocol_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_data_protocol);

/**
 * Parses a SOAP response message of GetDataProtocol action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_data_protocol pointer to the storage location for NewDataProtocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_data_protocol_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_data_protocol);

/**
  * Returns the action name of GetDataCompression.
  * @return the action name of GetDataCompression.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_data_compression(void);

/**
 * Appends a SOAP request message of GetDataCompression action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_data_compression(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDataCompression action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_data_compression(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDataCompression action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_data_compression value of NewDataCompression argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_data_compression_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_data_compression);

/**
 * Parses a SOAP response message of GetDataCompression action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_data_compression pointer to the storage location for NewDataCompression argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_data_compression_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_data_compression);

/**
  * Returns the action name of GetPlusVTRCommandSupported.
  * @return the action name of GetPlusVTRCommandSupported.
  */
extern const du_uchar* digd_wanpotslc_action_name_get_plus_vtr_command_supported(void);

/**
 * Appends a SOAP request message of GetPlusVTRCommandSupported action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_plus_vtr_command_supported(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPlusVTRCommandSupported action, stores each name and
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
extern du_bool digd_wanpotslc_parse_get_plus_vtr_command_supported(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPlusVTRCommandSupported action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_plus_vtr_command_supported value of NewPlusVTRCommandSupported argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpotslc_make_get_plus_vtr_command_supported_response(du_uchar_array* xml, du_uint32 v, du_bool new_plus_vtr_command_supported);

/**
 * Parses a SOAP response message of GetPlusVTRCommandSupported action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_plus_vtr_command_supported pointer to the storage location for NewPlusVTRCommandSupported argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpotslc_parse_get_plus_vtr_command_supported_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_plus_vtr_command_supported);

/**
 * the state variable name of 'ISPPhoneNumber'.
 * @return  "ISPPhoneNumber" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_isp_phone_number(void);

/**
 * the state variable name of 'ISPInfo'.
 * @return  "ISPInfo" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_isp_info(void);

/**
 * the state variable name of 'LinkType'.
 * @return  "LinkType" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_link_type(void);

/**
 * the state variable name of 'NumberOfRetries'.
 * @return  "NumberOfRetries" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_number_of_retries(void);

/**
 * the state variable name of 'DelayBetweenRetries'.
 * @return  "DelayBetweenRetries" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_delay_between_retries(void);

/**
 * the state variable name of 'Fclass'.
 * @return  "Fclass" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_fclass(void);

/**
 * the state variable name of 'DataModulationSupported'.
 * @return  "DataModulationSupported" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_data_modulation_supported(void);

/**
 * the state variable name of 'DataProtocol'.
 * @return  "DataProtocol" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_data_protocol(void);

/**
 * the state variable name of 'DataCompression'.
 * @return  "DataCompression" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_data_compression(void);

/**
 * the state variable name of 'PlusVTRCommandSupported'.
 * @return  "PlusVTRCommandSupported" string.
 */
extern const du_uchar* digd_wanpotslc_state_variable_name_plus_vtr_command_supported(void);

#ifdef __cplusplus
}
#endif

#endif
