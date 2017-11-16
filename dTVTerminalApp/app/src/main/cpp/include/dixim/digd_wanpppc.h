/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wanpppc.h
 *  @brief The digd_wanpppc interface provides methods for making SOAP request/response messages of
 *   WANPPPConnection actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANPPPConnection actions.
 *  @see  WANPPPConnection:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_WANPPPC_H
#define DIGD_WANPPPC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetConnectionType.
  * @return the action name of SetConnectionType.
  */
extern const du_uchar* digd_wanpppc_action_name_set_connection_type(void);

/**
 * Appends a SOAP request message of SetConnectionType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_connection_type value of NewConnectionType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_connection_type(du_uchar_array* xml, du_uint32 v, const du_uchar* new_connection_type);

/**
 * Parses a SOAP request message of SetConnectionType action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_connection_type pointer to the storage location for NewConnectionType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_set_connection_type(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_connection_type);

/**
 * Appends a SOAP response message of SetConnectionType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_connection_type_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetConnectionType action, stores each name and
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
extern du_bool digd_wanpppc_parse_set_connection_type_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetConnectionTypeInfo.
  * @return the action name of GetConnectionTypeInfo.
  */
extern const du_uchar* digd_wanpppc_action_name_get_connection_type_info(void);

/**
 * Appends a SOAP request message of GetConnectionTypeInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_connection_type_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetConnectionTypeInfo action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_connection_type_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetConnectionTypeInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_connection_type value of NewConnectionType argument.
 * @param[in] new_possible_connection_types value of NewPossibleConnectionTypes argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_connection_type_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_connection_type, const du_uchar* new_possible_connection_types);

/**
 * Parses a SOAP response message of GetConnectionTypeInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_connection_type pointer to the storage location for NewConnectionType argument value.
 * @param[out] new_possible_connection_types pointer to the storage location for NewPossibleConnectionTypes argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_connection_type_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_connection_type, const du_uchar** new_possible_connection_types);

/**
  * Returns the action name of ConfigureConnection.
  * @return the action name of ConfigureConnection.
  */
extern const du_uchar* digd_wanpppc_action_name_configure_connection(void);

/**
 * Appends a SOAP request message of ConfigureConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_user_name value of NewUserName argument.
 * @param[in] new_password value of NewPassword argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_configure_connection(du_uchar_array* xml, du_uint32 v, const du_uchar* new_user_name, const du_uchar* new_password);

/**
 * Parses a SOAP request message of ConfigureConnection action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_user_name pointer to the storage location for NewUserName argument value.
 * @param[out] new_password pointer to the storage location for NewPassword argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_configure_connection(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_user_name, const du_uchar** new_password);

/**
 * Appends a SOAP response message of ConfigureConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_configure_connection_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of ConfigureConnection action, stores each name and
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
extern du_bool digd_wanpppc_parse_configure_connection_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of RequestConnection.
  * @return the action name of RequestConnection.
  */
extern const du_uchar* digd_wanpppc_action_name_request_connection(void);

/**
 * Appends a SOAP request message of RequestConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_request_connection(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of RequestConnection action, stores each name and
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
extern du_bool digd_wanpppc_parse_request_connection(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of RequestConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_request_connection_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of RequestConnection action, stores each name and
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
extern du_bool digd_wanpppc_parse_request_connection_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of RequestTermination.
  * @return the action name of RequestTermination.
  */
extern const du_uchar* digd_wanpppc_action_name_request_termination(void);

/**
 * Appends a SOAP request message of RequestTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_request_termination(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of RequestTermination action, stores each name and
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
extern du_bool digd_wanpppc_parse_request_termination(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of RequestTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_request_termination_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of RequestTermination action, stores each name and
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
extern du_bool digd_wanpppc_parse_request_termination_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of ForceTermination.
  * @return the action name of ForceTermination.
  */
extern const du_uchar* digd_wanpppc_action_name_force_termination(void);

/**
 * Appends a SOAP request message of ForceTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_force_termination(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of ForceTermination action, stores each name and
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
extern du_bool digd_wanpppc_parse_force_termination(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of ForceTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_force_termination_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of ForceTermination action, stores each name and
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
extern du_bool digd_wanpppc_parse_force_termination_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetAutoDisconnectTime.
  * @return the action name of SetAutoDisconnectTime.
  */
extern const du_uchar* digd_wanpppc_action_name_set_auto_disconnect_time(void);

/**
 * Appends a SOAP request message of SetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_auto_disconnect_time value of NewAutoDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_auto_disconnect_time(du_uchar_array* xml, du_uint32 v, du_uint32 new_auto_disconnect_time);

/**
 * Parses a SOAP request message of SetAutoDisconnectTime action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_auto_disconnect_time pointer to the storage location for NewAutoDisconnectTime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_set_auto_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_auto_disconnect_time);

/**
 * Appends a SOAP response message of SetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_auto_disconnect_time_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetAutoDisconnectTime action, stores each name and
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
extern du_bool digd_wanpppc_parse_set_auto_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetIdleDisconnectTime.
  * @return the action name of SetIdleDisconnectTime.
  */
extern const du_uchar* digd_wanpppc_action_name_set_idle_disconnect_time(void);

/**
 * Appends a SOAP request message of SetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_idle_disconnect_time value of NewIdleDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_idle_disconnect_time(du_uchar_array* xml, du_uint32 v, du_uint32 new_idle_disconnect_time);

/**
 * Parses a SOAP request message of SetIdleDisconnectTime action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_idle_disconnect_time pointer to the storage location for NewIdleDisconnectTime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_set_idle_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_idle_disconnect_time);

/**
 * Appends a SOAP response message of SetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_idle_disconnect_time_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetIdleDisconnectTime action, stores each name and
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
extern du_bool digd_wanpppc_parse_set_idle_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetWarnDisconnectDelay.
  * @return the action name of SetWarnDisconnectDelay.
  */
extern const du_uchar* digd_wanpppc_action_name_set_warn_disconnect_delay(void);

/**
 * Appends a SOAP request message of SetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_warn_disconnect_delay value of NewWarnDisconnectDelay argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_warn_disconnect_delay(du_uchar_array* xml, du_uint32 v, du_uint32 new_warn_disconnect_delay);

/**
 * Parses a SOAP request message of SetWarnDisconnectDelay action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_warn_disconnect_delay pointer to the storage location for NewWarnDisconnectDelay argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_set_warn_disconnect_delay(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_warn_disconnect_delay);

/**
 * Appends a SOAP response message of SetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_set_warn_disconnect_delay_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetWarnDisconnectDelay action, stores each name and
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
extern du_bool digd_wanpppc_parse_set_warn_disconnect_delay_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetStatusInfo.
  * @return the action name of GetStatusInfo.
  */
extern const du_uchar* digd_wanpppc_action_name_get_status_info(void);

/**
 * Appends a SOAP request message of GetStatusInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_status_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetStatusInfo action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_status_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetStatusInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_connection_status value of NewConnectionStatus argument.
 * @param[in] new_last_connection_error value of NewLastConnectionError argument.
 * @param[in] new_uptime value of NewUptime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_status_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_connection_status, const du_uchar* new_last_connection_error, du_uint32 new_uptime);

/**
 * Parses a SOAP response message of GetStatusInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_connection_status pointer to the storage location for NewConnectionStatus argument value.
 * @param[out] new_last_connection_error pointer to the storage location for NewLastConnectionError argument value.
 * @param[out] new_uptime pointer to the storage location for NewUptime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_status_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_connection_status, const du_uchar** new_last_connection_error, du_uint32* new_uptime);

/**
  * Returns the action name of GetLinkLayerMaxBitRates.
  * @return the action name of GetLinkLayerMaxBitRates.
  */
extern const du_uchar* digd_wanpppc_action_name_get_link_layer_max_bit_rates(void);

/**
 * Appends a SOAP request message of GetLinkLayerMaxBitRates action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_link_layer_max_bit_rates(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetLinkLayerMaxBitRates action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_link_layer_max_bit_rates(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetLinkLayerMaxBitRates action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_upstream_max_bit_rate value of NewUpstreamMaxBitRate argument.
 * @param[in] new_downstream_max_bit_rate value of NewDownstreamMaxBitRate argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_link_layer_max_bit_rates_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_upstream_max_bit_rate, du_uint32 new_downstream_max_bit_rate);

/**
 * Parses a SOAP response message of GetLinkLayerMaxBitRates action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_upstream_max_bit_rate pointer to the storage location for NewUpstreamMaxBitRate argument value.
 * @param[out] new_downstream_max_bit_rate pointer to the storage location for NewDownstreamMaxBitRate argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_link_layer_max_bit_rates_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_upstream_max_bit_rate, du_uint32* new_downstream_max_bit_rate);

/**
  * Returns the action name of GetPPPEncryptionProtocol.
  * @return the action name of GetPPPEncryptionProtocol.
  */
extern const du_uchar* digd_wanpppc_action_name_get_ppp_encryption_protocol(void);

/**
 * Appends a SOAP request message of GetPPPEncryptionProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_ppp_encryption_protocol(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPPPEncryptionProtocol action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_ppp_encryption_protocol(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPPPEncryptionProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ppp_encryption_protocol value of NewPPPEncryptionProtocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_ppp_encryption_protocol_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ppp_encryption_protocol);

/**
 * Parses a SOAP response message of GetPPPEncryptionProtocol action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ppp_encryption_protocol pointer to the storage location for NewPPPEncryptionProtocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_ppp_encryption_protocol_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ppp_encryption_protocol);

/**
  * Returns the action name of GetPPPCompressionProtocol.
  * @return the action name of GetPPPCompressionProtocol.
  */
extern const du_uchar* digd_wanpppc_action_name_get_ppp_compression_protocol(void);

/**
 * Appends a SOAP request message of GetPPPCompressionProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_ppp_compression_protocol(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPPPCompressionProtocol action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_ppp_compression_protocol(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPPPCompressionProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ppp_compression_protocol value of NewPPPCompressionProtocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_ppp_compression_protocol_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ppp_compression_protocol);

/**
 * Parses a SOAP response message of GetPPPCompressionProtocol action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ppp_compression_protocol pointer to the storage location for NewPPPCompressionProtocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_ppp_compression_protocol_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ppp_compression_protocol);

/**
  * Returns the action name of GetPPPAuthenticationProtocol.
  * @return the action name of GetPPPAuthenticationProtocol.
  */
extern const du_uchar* digd_wanpppc_action_name_get_ppp_authentication_protocol(void);

/**
 * Appends a SOAP request message of GetPPPAuthenticationProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_ppp_authentication_protocol(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPPPAuthenticationProtocol action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_ppp_authentication_protocol(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPPPAuthenticationProtocol action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_ppp_authentication_protocol value of NewPPPAuthenticationProtocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_ppp_authentication_protocol_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_ppp_authentication_protocol);

/**
 * Parses a SOAP response message of GetPPPAuthenticationProtocol action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_ppp_authentication_protocol pointer to the storage location for NewPPPAuthenticationProtocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_ppp_authentication_protocol_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_ppp_authentication_protocol);

/**
  * Returns the action name of GetUserName.
  * @return the action name of GetUserName.
  */
extern const du_uchar* digd_wanpppc_action_name_get_user_name(void);

/**
 * Appends a SOAP request message of GetUserName action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_user_name(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetUserName action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_user_name(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetUserName action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_user_name value of NewUserName argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_user_name_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_user_name);

/**
 * Parses a SOAP response message of GetUserName action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_user_name pointer to the storage location for NewUserName argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_user_name_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_user_name);

/**
  * Returns the action name of GetPassword.
  * @return the action name of GetPassword.
  */
extern const du_uchar* digd_wanpppc_action_name_get_password(void);

/**
 * Appends a SOAP request message of GetPassword action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_password(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPassword action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_password(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPassword action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_password value of NewPassword argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_password_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_password);

/**
 * Parses a SOAP response message of GetPassword action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_password pointer to the storage location for NewPassword argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_password_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_password);

/**
  * Returns the action name of GetAutoDisconnectTime.
  * @return the action name of GetAutoDisconnectTime.
  */
extern const du_uchar* digd_wanpppc_action_name_get_auto_disconnect_time(void);

/**
 * Appends a SOAP request message of GetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_auto_disconnect_time(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetAutoDisconnectTime action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_auto_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_auto_disconnect_time value of NewAutoDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_auto_disconnect_time_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_auto_disconnect_time);

/**
 * Parses a SOAP response message of GetAutoDisconnectTime action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_auto_disconnect_time pointer to the storage location for NewAutoDisconnectTime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_auto_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_auto_disconnect_time);

/**
  * Returns the action name of GetIdleDisconnectTime.
  * @return the action name of GetIdleDisconnectTime.
  */
extern const du_uchar* digd_wanpppc_action_name_get_idle_disconnect_time(void);

/**
 * Appends a SOAP request message of GetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_idle_disconnect_time(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetIdleDisconnectTime action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_idle_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_idle_disconnect_time value of NewIdleDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_idle_disconnect_time_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_idle_disconnect_time);

/**
 * Parses a SOAP response message of GetIdleDisconnectTime action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_idle_disconnect_time pointer to the storage location for NewIdleDisconnectTime argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_idle_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_idle_disconnect_time);

/**
  * Returns the action name of GetWarnDisconnectDelay.
  * @return the action name of GetWarnDisconnectDelay.
  */
extern const du_uchar* digd_wanpppc_action_name_get_warn_disconnect_delay(void);

/**
 * Appends a SOAP request message of GetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_warn_disconnect_delay(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetWarnDisconnectDelay action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_warn_disconnect_delay(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_warn_disconnect_delay value of NewWarnDisconnectDelay argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_warn_disconnect_delay_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_warn_disconnect_delay);

/**
 * Parses a SOAP response message of GetWarnDisconnectDelay action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_warn_disconnect_delay pointer to the storage location for NewWarnDisconnectDelay argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_warn_disconnect_delay_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_warn_disconnect_delay);

/**
  * Returns the action name of GetNATRSIPStatus.
  * @return the action name of GetNATRSIPStatus.
  */
extern const du_uchar* digd_wanpppc_action_name_get_natrsip_status(void);

/**
 * Appends a SOAP request message of GetNATRSIPStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_natrsip_status(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetNATRSIPStatus action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_natrsip_status(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetNATRSIPStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_rsip_available value of NewRSIPAvailable argument.
 * @param[in] new_nat_enabled value of NewNATEnabled argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_natrsip_status_response(du_uchar_array* xml, du_uint32 v, du_bool new_rsip_available, du_bool new_nat_enabled);

/**
 * Parses a SOAP response message of GetNATRSIPStatus action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_rsip_available pointer to the storage location for NewRSIPAvailable argument value.
 * @param[out] new_nat_enabled pointer to the storage location for NewNATEnabled argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_natrsip_status_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_rsip_available, du_bool* new_nat_enabled);

/**
  * Returns the action name of GetGenericPortMappingEntry.
  * @return the action name of GetGenericPortMappingEntry.
  */
extern const du_uchar* digd_wanpppc_action_name_get_generic_port_mapping_entry(void);

/**
 * Appends a SOAP request message of GetGenericPortMappingEntry action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_port_mapping_index value of NewPortMappingIndex argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_generic_port_mapping_entry(du_uchar_array* xml, du_uint32 v, du_uint16 new_port_mapping_index);

/**
 * Parses a SOAP request message of GetGenericPortMappingEntry action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_port_mapping_index pointer to the storage location for NewPortMappingIndex argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_generic_port_mapping_entry(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_port_mapping_index);

/**
 * Appends a SOAP response message of GetGenericPortMappingEntry action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_remote_host value of NewRemoteHost argument.
 * @param[in] new_external_port value of NewExternalPort argument.
 * @param[in] new_protocol value of NewProtocol argument.
 * @param[in] new_internal_port value of NewInternalPort argument.
 * @param[in] new_internal_client value of NewInternalClient argument.
 * @param[in] new_enabled value of NewEnabled argument.
 * @param[in] new_port_mapping_description value of NewPortMappingDescription argument.
 * @param[in] new_lease_duration value of NewLeaseDuration argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_generic_port_mapping_entry_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

/**
 * Parses a SOAP response message of GetGenericPortMappingEntry action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_remote_host pointer to the storage location for NewRemoteHost argument value.
 * @param[out] new_external_port pointer to the storage location for NewExternalPort argument value.
 * @param[out] new_protocol pointer to the storage location for NewProtocol argument value.
 * @param[out] new_internal_port pointer to the storage location for NewInternalPort argument value.
 * @param[out] new_internal_client pointer to the storage location for NewInternalClient argument value.
 * @param[out] new_enabled pointer to the storage location for NewEnabled argument value.
 * @param[out] new_port_mapping_description pointer to the storage location for NewPortMappingDescription argument value.
 * @param[out] new_lease_duration pointer to the storage location for NewLeaseDuration argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_generic_port_mapping_entry_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
  * Returns the action name of GetSpecificPortMappingEntry.
  * @return the action name of GetSpecificPortMappingEntry.
  */
extern const du_uchar* digd_wanpppc_action_name_get_specific_port_mapping_entry(void);

/**
 * Appends a SOAP request message of GetSpecificPortMappingEntry action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_remote_host value of NewRemoteHost argument.
 * @param[in] new_external_port value of NewExternalPort argument.
 * @param[in] new_protocol value of NewProtocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_specific_port_mapping_entry(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol);

/**
 * Parses a SOAP request message of GetSpecificPortMappingEntry action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_remote_host pointer to the storage location for NewRemoteHost argument value.
 * @param[out] new_external_port pointer to the storage location for NewExternalPort argument value.
 * @param[out] new_protocol pointer to the storage location for NewProtocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_specific_port_mapping_entry(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol);

/**
 * Appends a SOAP response message of GetSpecificPortMappingEntry action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_internal_port value of NewInternalPort argument.
 * @param[in] new_internal_client value of NewInternalClient argument.
 * @param[in] new_enabled value of NewEnabled argument.
 * @param[in] new_port_mapping_description value of NewPortMappingDescription argument.
 * @param[in] new_lease_duration value of NewLeaseDuration argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_specific_port_mapping_entry_response(du_uchar_array* xml, du_uint32 v, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

/**
 * Parses a SOAP response message of GetSpecificPortMappingEntry action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_internal_port pointer to the storage location for NewInternalPort argument value.
 * @param[out] new_internal_client pointer to the storage location for NewInternalClient argument value.
 * @param[out] new_enabled pointer to the storage location for NewEnabled argument value.
 * @param[out] new_port_mapping_description pointer to the storage location for NewPortMappingDescription argument value.
 * @param[out] new_lease_duration pointer to the storage location for NewLeaseDuration argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_specific_port_mapping_entry_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
  * Returns the action name of AddPortMapping.
  * @return the action name of AddPortMapping.
  */
extern const du_uchar* digd_wanpppc_action_name_add_port_mapping(void);

/**
 * Appends a SOAP request message of AddPortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_remote_host value of NewRemoteHost argument.
 * @param[in] new_external_port value of NewExternalPort argument.
 * @param[in] new_protocol value of NewProtocol argument.
 * @param[in] new_internal_port value of NewInternalPort argument.
 * @param[in] new_internal_client value of NewInternalClient argument.
 * @param[in] new_enabled value of NewEnabled argument.
 * @param[in] new_port_mapping_description value of NewPortMappingDescription argument.
 * @param[in] new_lease_duration value of NewLeaseDuration argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_add_port_mapping(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

/**
 * Parses a SOAP request message of AddPortMapping action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_remote_host pointer to the storage location for NewRemoteHost argument value.
 * @param[out] new_external_port pointer to the storage location for NewExternalPort argument value.
 * @param[out] new_protocol pointer to the storage location for NewProtocol argument value.
 * @param[out] new_internal_port pointer to the storage location for NewInternalPort argument value.
 * @param[out] new_internal_client pointer to the storage location for NewInternalClient argument value.
 * @param[out] new_enabled pointer to the storage location for NewEnabled argument value.
 * @param[out] new_port_mapping_description pointer to the storage location for NewPortMappingDescription argument value.
 * @param[out] new_lease_duration pointer to the storage location for NewLeaseDuration argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_add_port_mapping(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
 * Appends a SOAP response message of AddPortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_add_port_mapping_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of AddPortMapping action, stores each name and
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
extern du_bool digd_wanpppc_parse_add_port_mapping_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeletePortMapping.
  * @return the action name of DeletePortMapping.
  */
extern const du_uchar* digd_wanpppc_action_name_delete_port_mapping(void);

/**
 * Appends a SOAP request message of DeletePortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_remote_host value of NewRemoteHost argument.
 * @param[in] new_external_port value of NewExternalPort argument.
 * @param[in] new_protocol value of NewProtocol argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_delete_port_mapping(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol);

/**
 * Parses a SOAP request message of DeletePortMapping action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_remote_host pointer to the storage location for NewRemoteHost argument value.
 * @param[out] new_external_port pointer to the storage location for NewExternalPort argument value.
 * @param[out] new_protocol pointer to the storage location for NewProtocol argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_delete_port_mapping(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol);

/**
 * Appends a SOAP response message of DeletePortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_delete_port_mapping_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeletePortMapping action, stores each name and
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
extern du_bool digd_wanpppc_parse_delete_port_mapping_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetExternalIPAddress.
  * @return the action name of GetExternalIPAddress.
  */
extern const du_uchar* digd_wanpppc_action_name_get_external_ip_address(void);

/**
 * Appends a SOAP request message of GetExternalIPAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_external_ip_address(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetExternalIPAddress action, stores each name and
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
extern du_bool digd_wanpppc_parse_get_external_ip_address(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetExternalIPAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_external_ip_address value of NewExternalIPAddress argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanpppc_make_get_external_ip_address_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_external_ip_address);

/**
 * Parses a SOAP response message of GetExternalIPAddress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_external_ip_address pointer to the storage location for NewExternalIPAddress argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanpppc_parse_get_external_ip_address_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_external_ip_address);

/**
 * the state variable name of 'ConnectionType'.
 * @return  "ConnectionType" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_connection_type(void);

/**
 * the state variable name of 'PossibleConnectionTypes'.
 * @return  "PossibleConnectionTypes" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_possible_connection_types(void);

/**
 * the state variable name of 'ConnectionStatus'.
 * @return  "ConnectionStatus" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_connection_status(void);

/**
 * the state variable name of 'Uptime'.
 * @return  "Uptime" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_uptime(void);

/**
 * the state variable name of 'UpstreamMaxBitRate'.
 * @return  "UpstreamMaxBitRate" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_upstream_max_bit_rate(void);

/**
 * the state variable name of 'DownstreamMaxBitRate'.
 * @return  "DownstreamMaxBitRate" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_downstream_max_bit_rate(void);

/**
 * the state variable name of 'LastConnectionError'.
 * @return  "LastConnectionError" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_last_connection_error(void);

/**
 * the state variable name of 'AutoDisconnectTime'.
 * @return  "AutoDisconnectTime" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_auto_disconnect_time(void);

/**
 * the state variable name of 'IdleDisconnectTime'.
 * @return  "IdleDisconnectTime" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_idle_disconnect_time(void);

/**
 * the state variable name of 'WarnDisconnectDelay'.
 * @return  "WarnDisconnectDelay" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_warn_disconnect_delay(void);

/**
 * the state variable name of 'RSIPAvailable'.
 * @return  "RSIPAvailable" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_rsip_available(void);

/**
 * the state variable name of 'NATEnabled'.
 * @return  "NATEnabled" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_nat_enabled(void);

/**
 * the state variable name of 'UserName'.
 * @return  "UserName" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_user_name(void);

/**
 * the state variable name of 'Password'.
 * @return  "Password" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_password(void);

/**
 * the state variable name of 'PPPEncryptionProtocol'.
 * @return  "PPPEncryptionProtocol" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_ppp_encryption_protocol(void);

/**
 * the state variable name of 'PPPCompressionProtocol'.
 * @return  "PPPCompressionProtocol" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_ppp_compression_protocol(void);

/**
 * the state variable name of 'PPPAuthenticationProtocol'.
 * @return  "PPPAuthenticationProtocol" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_ppp_authentication_protocol(void);

/**
 * the state variable name of 'ExternalIPAddress'.
 * @return  "ExternalIPAddress" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_external_ip_address(void);

/**
 * the state variable name of 'PortMappingNumberOfEntries'.
 * @return  "PortMappingNumberOfEntries" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_port_mapping_number_of_entries(void);

/**
 * the state variable name of 'PortMappingEnabled'.
 * @return  "PortMappingEnabled" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_port_mapping_enabled(void);

/**
 * the state variable name of 'PortMappingLeaseDuration'.
 * @return  "PortMappingLeaseDuration" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_port_mapping_lease_duration(void);

/**
 * the state variable name of 'RemoteHost'.
 * @return  "RemoteHost" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_remote_host(void);

/**
 * the state variable name of 'ExternalPort'.
 * @return  "ExternalPort" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_external_port(void);

/**
 * the state variable name of 'InternalPort'.
 * @return  "InternalPort" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_internal_port(void);

/**
 * the state variable name of 'PortMappingProtocol'.
 * @return  "PortMappingProtocol" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_port_mapping_protocol(void);

/**
 * the state variable name of 'InternalClient'.
 * @return  "InternalClient" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_internal_client(void);

/**
 * the state variable name of 'PortMappingDescription'.
 * @return  "PortMappingDescription" string.
 */
extern const du_uchar* digd_wanpppc_state_variable_name_port_mapping_description(void);

#ifdef __cplusplus
}
#endif

#endif
