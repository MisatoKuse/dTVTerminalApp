/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

/** @file
 *  @brief The ddps interface provides methods for making SOAP request/response messages of
 *   DtcpPlus Service's actions. This interface provides methods for parsing SOAP request/response
 *   messages of DtcpPlus Service's actions.
 */

#ifndef DLNA_DDPS_H
#define DLNA_DDPS_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of PrepareRegistion
  * @return the action name of PrepareRegistion
  */
extern const du_uchar* ddps_action_name_prepare_registration(void);

/**
 * Appends a SOAP request message of PrepareRegistration action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool ddps_make_prepare_registration(du_uchar_array* xml, du_uint32 ver);

/**
 * Parses a SOAP response message of PrepareRegisterDevice action, stores each name and 
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
extern du_bool ddps_parse_prepare_registration_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of RegisterDevice.
  * @return the action name of RegisterDevice.
  */
extern const du_uchar* ddps_action_name_register_device(void);

/**
 * Appends a SOAP request message of RegisterDevice action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] device_id value of DeviceID argument.
 * @param[in] device_name value of DeviceName argument.
 * @param[in] dtla_device_id_hash value of DTLADeviceIDHash argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool ddps_make_register_device(du_uchar_array* xml, const du_uchar* device_id, const du_uchar* device_name, const du_uchar* dtla_device_id_hash, du_uint32 ver);

/**
 * Parses a SOAP request message of RegisterDevice action, stores each name and 
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] device_id pointer to the storage location for DeviceID argument value.
 * @param[out] device_name pointer to the storage location for DeviceName argument value.
 * @param[out] dtla_device_id_hash pointer to the storage location for DTLADeviceIDHash argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by 
 * the <b>du_str_array_init</b> function.
 */
extern du_bool ddps_parse_register_device(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** device_id, const du_uchar** device_name, const du_uchar** dtla_device_id_hash);

/**
 * Appends a SOAP response message of RegisterDevice action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool ddps_make_register_device_response(du_uchar_array* xml, du_uint32 ver);

/**
 * Parses a SOAP response message of RegisterDevice action, stores each name and 
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
extern du_bool ddps_parse_register_device_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of UnregisterDevice.
  * @return the action name of UnregisterDevice.
  */
extern const du_uchar* ddps_action_name_unregister_device(void);

/**
 * Appends a SOAP request message of UnregisterDevice action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] device_id value of DeviceID argument.
 * @param[in] dtla_device_id_hash value of DTLADeviceIDHash argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
  extern du_bool ddps_make_unregister_device(du_uchar_array* xml, const du_uchar* device_id, const du_uchar* dtla_device_id_hash, du_uint32 ver);

/**
 * Parses a SOAP request message of UnregisterDevice action, stores each name and 
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] device_id pointer to the storage location for DeviceID argument value.
 * @param[out] dtla_device_id_hash pointer to the storage location for DTLADeviceIDHash argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by 
 * the <b>du_str_array_init</b> function.
 */
extern du_bool ddps_parse_unregister_device(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** device_id, const du_uchar** dtla_device_id_hash);

/**
 * Appends a SOAP response message of UnregisterDevice action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool ddps_make_unregister_device_response(du_uchar_array* xml, du_uint32 ver);

/**
 * Parses a SOAP response message of UnregisterDevice action, stores each name and 
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
extern du_bool ddps_parse_unregister_device_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetRegisteredDevices.
  * @return the action name of GetRegisteredDevices.
  */
extern const du_uchar* ddps_action_name_get_registered_devices(void);

/**
 * Appends a SOAP request message of GetRegisteredDevices action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool ddps_make_get_registered_devices(du_uchar_array* xml, du_uint32 ver);

/**
 * Parses a SOAP request message of GetRegisteredDevices action, stores each name and 
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
extern du_bool ddps_parse_get_registered_devices(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetRegisteredDevices action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] devices value of Devices argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool ddps_make_get_registered_devices_response(du_uchar_array* xml, const du_uchar* devices, du_uint32 ver);

/**
 * Parses a SOAP response message of GetRegisteredDevices action, stores each name and 
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] devices pointer to the storage location for Devices argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by 
 * the <b>du_str_array_init</b> function.
 */
extern du_bool ddps_parse_get_registered_devices_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** devices);

/**
 * Returns an error code of 'device already registered'.
 * This error code is "800".
 * @return  "800" string.
 */
extern const du_uchar* ddps_error_code_device_already_registered(void);

/**
 * Returns an error code of 'device not registered'.
 * This error code is "801".
 * @return  "801" string.
 */
extern const du_uchar* ddps_error_code_device_not_registered(void);

/**
 * Returns an error code of 'no space left'.
 * This error code is "802".
 * @return  "802" string.
 */
extern const du_uchar* ddps_error_code_no_space_left(void);

#ifdef __cplusplus
}
#endif

#endif
