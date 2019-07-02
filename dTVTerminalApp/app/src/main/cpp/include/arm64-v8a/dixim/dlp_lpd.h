/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dlp_lpd.h
 *  @brief The dlp_lpd interface provides methods for making SOAP request/response messages of
 *   LowPowerDevice actions. This interface provides methods for parsing SOAP request/response
 *   messages of LowPowerDevice actions.
 *  @see  LowPowerDevice:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DLP_LPD_H
#define DLP_LPD_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of GetPowerManagementInfo.
  * @return the action name of GetPowerManagementInfo.
  */
extern const du_uchar* dlp_lpd_action_name_get_power_management_info(void);

/**
 * Appends a SOAP request message of GetPowerManagementInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpd_make_get_power_management_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPowerManagementInfo action, stores each name and
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
extern du_bool dlp_lpd_parse_get_power_management_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPowerManagementInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] wakeup_method value of WakeupMethod argument.
 * @param[in] power_supply_status value of PowerSupplyStatus argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpd_make_get_power_management_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* wakeup_method, const du_uchar* power_supply_status);

/**
 * Parses a SOAP response message of GetPowerManagementInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] wakeup_method pointer to the storage location for WakeupMethod argument value.
 * @param[out] power_supply_status pointer to the storage location for PowerSupplyStatus argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpd_parse_get_power_management_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** wakeup_method, const du_uchar** power_supply_status);

/**
  * Returns the action name of GoToSleep.
  * @return the action name of GoToSleep.
  */
extern const du_uchar* dlp_lpd_action_name_go_to_sleep(void);

/**
 * Appends a SOAP request message of GoToSleep action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] recommended_sleep_period value of RecommendedSleepPeriod argument.
 * @param[in] recommended_power_state value of RecommendedPowerState argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpd_make_go_to_sleep(du_uchar_array* xml, du_uint32 v, du_int32 recommended_sleep_period, const du_uchar* recommended_power_state);

/**
 * Parses a SOAP request message of GoToSleep action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] recommended_sleep_period pointer to the storage location for RecommendedSleepPeriod argument value.
 * @param[out] recommended_power_state pointer to the storage location for RecommendedPowerState argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpd_parse_go_to_sleep(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* recommended_sleep_period, const du_uchar** recommended_power_state);

/**
 * Appends a SOAP response message of GoToSleep action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] sleep_period value of SleepPeriod argument.
 * @param[in] power_state value of PowerState argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpd_make_go_to_sleep_response(du_uchar_array* xml, du_uint32 v, du_int32 sleep_period, const du_uchar* power_state);

/**
 * Parses a SOAP response message of GoToSleep action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] sleep_period pointer to the storage location for SleepPeriod argument value.
 * @param[out] power_state pointer to the storage location for PowerState argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpd_parse_go_to_sleep_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* sleep_period, const du_uchar** power_state);

/**
  * Returns the action name of Wakeup.
  * @return the action name of Wakeup.
  */
extern const du_uchar* dlp_lpd_action_name_wakeup(void);

/**
 * Appends a SOAP request message of Wakeup action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpd_make_wakeup(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of Wakeup action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpd_parse_wakeup(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of Wakeup action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dlp_lpd_make_wakeup_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Wakeup action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dlp_lpd_parse_wakeup_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * the state variable name of 'ExternalPowerSupplySource'.
 * @return  "ExternalPowerSupplySource" string.
 */
extern const du_uchar* dlp_lpd_state_variable_name_external_power_supply_source(void);

/**
 * the state variable name of 'BatteryLow'.
 * @return  "BatteryLow" string.
 */
extern const du_uchar* dlp_lpd_state_variable_name_battery_low(void);

/**
 * the state variable name of 'PowerSupplyStatus'.
 * @return  "PowerSupplyStatus" string.
 */
extern const du_uchar* dlp_lpd_state_variable_name_power_supply_status(void);

/**
 * the state variable name of 'SleepPeriod'.
 * @return  "SleepPeriod" string.
 */
extern const du_uchar* dlp_lpd_state_variable_name_sleep_period(void);

/**
 * the state variable name of 'PowerState'.
 * @return  "PowerState" string.
 */
extern const du_uchar* dlp_lpd_state_variable_name_power_state(void);

/**
 * the state variable name of 'WakeupMethod'.
 * @return  "WakeupMethod" string.
 */
extern const du_uchar* dlp_lpd_state_variable_name_wakeup_method(void);

#ifdef __cplusplus
}
#endif

#endif
