/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dlp_lpp.h
 *  @brief The dlp_lpd interface provides methods for making SOAP request/response messages of
 *   LowPowerProxy actions. This interface provides methods for parsing SOAP request/response
 *   messages of LowPowerProxy actions.
 *  @see  LowPowerProxy:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DLP_LPP_H
#define DLP_LPP_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SearchSleepingDevices.
  * @return the action name of SearchSleepingDevices.
  */
extern const du_uchar* dlp_lpp_action_name_search_sleeping_devices(void);

/**
 * Appends a SOAP request message of SearchSleepingDevices action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] search_criteria value of SearchCriteria argument.
 * @param[in] power_state value of PowerState argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpp_make_search_sleeping_devices(du_uchar_array* xml, du_uint32 v, const du_uchar* search_criteria, du_int32 power_state);

/**
 * Parses a SOAP request message of SearchSleepingDevices action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] search_criteria pointer to the storage location for SearchCriteria argument value.
 * @param[out] power_state pointer to the storage location for PowerState argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpp_parse_search_sleeping_devices(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** search_criteria, du_int32* power_state);

/**
 * Appends a SOAP response message of SearchSleepingDevices action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] device_list value of DeviceList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpp_make_search_sleeping_devices_response(du_uchar_array* xml, du_uint32 v, const du_uchar* device_list);

/**
 * Parses a SOAP response message of SearchSleepingDevices action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] device_list pointer to the storage location for DeviceList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpp_parse_search_sleeping_devices_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** device_list);

/**
  * Returns the action name of WakeupDevice.
  * @return the action name of WakeupDevice.
  */
extern const du_uchar* dlp_lpp_action_name_wakeup_device(void);

/**
 * Appends a SOAP request message of WakeupDevice action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] uuid value of Uuid argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpp_make_wakeup_device(du_uchar_array* xml, du_uint32 v, const du_uchar* uuid);

/**
 * Parses a SOAP request message of WakeupDevice action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] uuid pointer to the storage location for Uuid argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpp_parse_wakeup_device(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** uuid);

/**
 * Appends a SOAP response message of WakeupDevice action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] success value of Success argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpp_make_wakeup_device_response(du_uchar_array* xml, du_uint32 v, du_bool success);

/**
 * Parses a SOAP response message of WakeupDevice action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] success pointer to the storage location for Success argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpp_parse_wakeup_device_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* success);

/**
 * the state variable name of 'DeviceListInfo'.
 * @return  "DeviceListInfo" string.
 */
extern const du_uchar* dlp_lpp_state_variable_name_device_list_info(void);

/**
 * the state variable name of 'A_ARG_TYPE_PowerState'.
 * @return  "A_ARG_TYPE_PowerState" string.
 */
extern const du_uchar* dlp_lpp_state_variable_name_a_arg_type_power_state(void);

/**
 * the state variable name of 'A_ARG_TYPE_Success'.
 * @return  "A_ARG_TYPE_Success" string.
 */
extern const du_uchar* dlp_lpp_state_variable_name_a_arg_type_success(void);

/**
 * the state variable name of 'A_ARG_TYPE_SearchCriteria'.
 * @return  "A_ARG_TYPE_SearchCriteria" string.
 */
extern const du_uchar* dlp_lpp_state_variable_name_a_arg_type_search_criteria(void);

/**
 * the state variable name of 'A_ARG_TYPE_UUID'.
 * @return  "A_ARG_TYPE_UUID" string.
 */
extern const du_uchar* dlp_lpp_state_variable_name_a_arg_type_uuid(void);

#ifdef __cplusplus
}
#endif

#endif
