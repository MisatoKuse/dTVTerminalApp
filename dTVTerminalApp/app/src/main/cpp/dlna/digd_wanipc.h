/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_wanipc.h
 *  @brief The digd_wanipc interface provides methods for making SOAP request/response messages of
 *   WANIPConnection actions. This interface provides methods for parsing SOAP request/response
 *   messages of WANIPConnection actions.
 *  @see  WANIPConnection:2 Service Template Version 2.00 For UPnP. Version 1.0
 */

#ifndef DIGD_WANIPC_H
#define DIGD_WANIPC_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetConnectionType.
  * @return the action name of SetConnectionType.
  */
extern const du_uchar* digd_wanipc_action_name_set_connection_type(void);

/**
 * Appends a SOAP request message of SetConnectionType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_connection_type value of NewConnectionType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_connection_type(du_uchar_array* xml, du_uint32 v, const du_uchar* new_connection_type);

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
extern du_bool digd_wanipc_parse_set_connection_type(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_connection_type);

/**
 * Appends a SOAP response message of SetConnectionType action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_connection_type_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_set_connection_type_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetConnectionTypeInfo.
  * @return the action name of GetConnectionTypeInfo.
  */
extern const du_uchar* digd_wanipc_action_name_get_connection_type_info(void);

/**
 * Appends a SOAP request message of GetConnectionTypeInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_connection_type_info(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_connection_type_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetConnectionTypeInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_connection_type value of NewConnectionType argument.
 * @param[in] new_possible_connection_types value of NewPossibleConnectionTypes argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_connection_type_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_connection_type, const du_uchar* new_possible_connection_types);

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
extern du_bool digd_wanipc_parse_get_connection_type_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_connection_type, const du_uchar** new_possible_connection_types);

/**
  * Returns the action name of RequestConnection.
  * @return the action name of RequestConnection.
  */
extern const du_uchar* digd_wanipc_action_name_request_connection(void);

/**
 * Appends a SOAP request message of RequestConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_request_connection(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_request_connection(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of RequestConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_request_connection_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_request_connection_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of RequestTermination.
  * @return the action name of RequestTermination.
  */
extern const du_uchar* digd_wanipc_action_name_request_termination(void);

/**
 * Appends a SOAP request message of RequestTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_request_termination(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_request_termination(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of RequestTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_request_termination_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_request_termination_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of ForceTermination.
  * @return the action name of ForceTermination.
  */
extern const du_uchar* digd_wanipc_action_name_force_termination(void);

/**
 * Appends a SOAP request message of ForceTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_force_termination(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_force_termination(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of ForceTermination action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_force_termination_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_force_termination_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetAutoDisconnectTime.
  * @return the action name of SetAutoDisconnectTime.
  */
extern const du_uchar* digd_wanipc_action_name_set_auto_disconnect_time(void);

/**
 * Appends a SOAP request message of SetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_auto_disconnect_time value of NewAutoDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_auto_disconnect_time(du_uchar_array* xml, du_uint32 v, du_uint32 new_auto_disconnect_time);

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
extern du_bool digd_wanipc_parse_set_auto_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_auto_disconnect_time);

/**
 * Appends a SOAP response message of SetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_auto_disconnect_time_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_set_auto_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetIdleDisconnectTime.
  * @return the action name of SetIdleDisconnectTime.
  */
extern const du_uchar* digd_wanipc_action_name_set_idle_disconnect_time(void);

/**
 * Appends a SOAP request message of SetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_idle_disconnect_time value of NewIdleDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_idle_disconnect_time(du_uchar_array* xml, du_uint32 v, du_uint32 new_idle_disconnect_time);

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
extern du_bool digd_wanipc_parse_set_idle_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_idle_disconnect_time);

/**
 * Appends a SOAP response message of SetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_idle_disconnect_time_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_set_idle_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetWarnDisconnectDelay.
  * @return the action name of SetWarnDisconnectDelay.
  */
extern const du_uchar* digd_wanipc_action_name_set_warn_disconnect_delay(void);

/**
 * Appends a SOAP request message of SetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_warn_disconnect_delay value of NewWarnDisconnectDelay argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_warn_disconnect_delay(du_uchar_array* xml, du_uint32 v, du_uint32 new_warn_disconnect_delay);

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
extern du_bool digd_wanipc_parse_set_warn_disconnect_delay(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_warn_disconnect_delay);

/**
 * Appends a SOAP response message of SetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_set_warn_disconnect_delay_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_set_warn_disconnect_delay_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetStatusInfo.
  * @return the action name of GetStatusInfo.
  */
extern const du_uchar* digd_wanipc_action_name_get_status_info(void);

/**
 * Appends a SOAP request message of GetStatusInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_status_info(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_status_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

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
extern du_bool digd_wanipc_make_get_status_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_connection_status, const du_uchar* new_last_connection_error, du_uint32 new_uptime);

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
extern du_bool digd_wanipc_parse_get_status_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_connection_status, const du_uchar** new_last_connection_error, du_uint32* new_uptime);

/**
  * Returns the action name of GetAutoDisconnectTime.
  * @return the action name of GetAutoDisconnectTime.
  */
extern const du_uchar* digd_wanipc_action_name_get_auto_disconnect_time(void);

/**
 * Appends a SOAP request message of GetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_auto_disconnect_time(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_auto_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetAutoDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_auto_disconnect_time value of NewAutoDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_auto_disconnect_time_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_auto_disconnect_time);

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
extern du_bool digd_wanipc_parse_get_auto_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_auto_disconnect_time);

/**
  * Returns the action name of GetIdleDisconnectTime.
  * @return the action name of GetIdleDisconnectTime.
  */
extern const du_uchar* digd_wanipc_action_name_get_idle_disconnect_time(void);

/**
 * Appends a SOAP request message of GetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_idle_disconnect_time(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_idle_disconnect_time(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetIdleDisconnectTime action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_idle_disconnect_time value of NewIdleDisconnectTime argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_idle_disconnect_time_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_idle_disconnect_time);

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
extern du_bool digd_wanipc_parse_get_idle_disconnect_time_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_idle_disconnect_time);

/**
  * Returns the action name of GetWarnDisconnectDelay.
  * @return the action name of GetWarnDisconnectDelay.
  */
extern const du_uchar* digd_wanipc_action_name_get_warn_disconnect_delay(void);

/**
 * Appends a SOAP request message of GetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_warn_disconnect_delay(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_warn_disconnect_delay(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetWarnDisconnectDelay action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_warn_disconnect_delay value of NewWarnDisconnectDelay argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_warn_disconnect_delay_response(du_uchar_array* xml, du_uint32 v, du_uint32 new_warn_disconnect_delay);

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
extern du_bool digd_wanipc_parse_get_warn_disconnect_delay_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* new_warn_disconnect_delay);

/**
  * Returns the action name of GetNATRSIPStatus.
  * @return the action name of GetNATRSIPStatus.
  */
extern const du_uchar* digd_wanipc_action_name_get_natrsip_status(void);

/**
 * Appends a SOAP request message of GetNATRSIPStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_natrsip_status(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_natrsip_status(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetNATRSIPStatus action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_rsip_available value of NewRSIPAvailable argument.
 * @param[in] new_nat_enabled value of NewNATEnabled argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_natrsip_status_response(du_uchar_array* xml, du_uint32 v, du_bool new_rsip_available, du_bool new_nat_enabled);

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
extern du_bool digd_wanipc_parse_get_natrsip_status_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* new_rsip_available, du_bool* new_nat_enabled);

/**
  * Returns the action name of GetGenericPortMappingEntry.
  * @return the action name of GetGenericPortMappingEntry.
  */
extern const du_uchar* digd_wanipc_action_name_get_generic_port_mapping_entry(void);

/**
 * Appends a SOAP request message of GetGenericPortMappingEntry action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_port_mapping_index value of NewPortMappingIndex argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_generic_port_mapping_entry(du_uchar_array* xml, du_uint32 v, du_uint16 new_port_mapping_index);

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
extern du_bool digd_wanipc_parse_get_generic_port_mapping_entry(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_port_mapping_index);

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
extern du_bool digd_wanipc_make_get_generic_port_mapping_entry_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

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
extern du_bool digd_wanipc_parse_get_generic_port_mapping_entry_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
  * Returns the action name of GetSpecificPortMappingEntry.
  * @return the action name of GetSpecificPortMappingEntry.
  */
extern const du_uchar* digd_wanipc_action_name_get_specific_port_mapping_entry(void);

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
extern du_bool digd_wanipc_make_get_specific_port_mapping_entry(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol);

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
extern du_bool digd_wanipc_parse_get_specific_port_mapping_entry(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol);

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
extern du_bool digd_wanipc_make_get_specific_port_mapping_entry_response(du_uchar_array* xml, du_uint32 v, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

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
extern du_bool digd_wanipc_parse_get_specific_port_mapping_entry_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
  * Returns the action name of AddPortMapping.
  * @return the action name of AddPortMapping.
  */
extern const du_uchar* digd_wanipc_action_name_add_port_mapping(void);

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
extern du_bool digd_wanipc_make_add_port_mapping(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

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
extern du_bool digd_wanipc_parse_add_port_mapping(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
 * Appends a SOAP response message of AddPortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_add_port_mapping_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_add_port_mapping_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeletePortMapping.
  * @return the action name of DeletePortMapping.
  */
extern const du_uchar* digd_wanipc_action_name_delete_port_mapping(void);

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
extern du_bool digd_wanipc_make_delete_port_mapping(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol);

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
extern du_bool digd_wanipc_parse_delete_port_mapping(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol);

/**
 * Appends a SOAP response message of DeletePortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_delete_port_mapping_response(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_delete_port_mapping_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeletePortMappingRange.
  * @return the action name of DeletePortMappingRange.
  */
extern const du_uchar* digd_wanipc_action_name_delete_port_mapping_range(void);

/**
 * Appends a SOAP request message of DeletePortMappingRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_start_port value of NewStartPort argument.
 * @param[in] new_end_port value of NewEndPort argument.
 * @param[in] new_protocol value of NewProtocol argument.
 * @param[in] new_manage value of NewManage argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_delete_port_mapping_range(du_uchar_array* xml, du_uint32 v, du_uint16 new_start_port, du_uint16 new_end_port, const du_uchar* new_protocol, du_bool new_manage);

/**
 * Parses a SOAP request message of DeletePortMappingRange action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_start_port pointer to the storage location for NewStartPort argument value.
 * @param[out] new_end_port pointer to the storage location for NewEndPort argument value.
 * @param[out] new_protocol pointer to the storage location for NewProtocol argument value.
 * @param[out] new_manage pointer to the storage location for NewManage argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipc_parse_delete_port_mapping_range(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_start_port, du_uint16* new_end_port, const du_uchar** new_protocol, du_bool* new_manage);

/**
 * Appends a SOAP response message of DeletePortMappingRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_delete_port_mapping_range_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeletePortMappingRange action, stores each name and
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
extern du_bool digd_wanipc_parse_delete_port_mapping_range_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetExternalIPAddress.
  * @return the action name of GetExternalIPAddress.
  */
extern const du_uchar* digd_wanipc_action_name_get_external_ip_address(void);

/**
 * Appends a SOAP request message of GetExternalIPAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_external_ip_address(du_uchar_array* xml, du_uint32 v);

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
extern du_bool digd_wanipc_parse_get_external_ip_address(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetExternalIPAddress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_external_ip_address value of NewExternalIPAddress argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_external_ip_address_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_external_ip_address);

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
extern du_bool digd_wanipc_parse_get_external_ip_address_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_external_ip_address);

/**
  * Returns the action name of GetListOfPortMappings.
  * @return the action name of GetListOfPortMappings.
  */
extern const du_uchar* digd_wanipc_action_name_get_list_of_port_mappings(void);

/**
 * Appends a SOAP request message of GetListOfPortMappings action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_start_port value of NewStartPort argument.
 * @param[in] new_end_port value of NewEndPort argument.
 * @param[in] new_protocol value of NewProtocol argument.
 * @param[in] new_manage value of NewManage argument.
 * @param[in] new_number_of_ports value of NewNumberOfPorts argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_list_of_port_mappings(du_uchar_array* xml, du_uint32 v, du_uint16 new_start_port, du_uint16 new_end_port, const du_uchar* new_protocol, du_bool new_manage, du_uint16 new_number_of_ports);

/**
 * Parses a SOAP request message of GetListOfPortMappings action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_start_port pointer to the storage location for NewStartPort argument value.
 * @param[out] new_end_port pointer to the storage location for NewEndPort argument value.
 * @param[out] new_protocol pointer to the storage location for NewProtocol argument value.
 * @param[out] new_manage pointer to the storage location for NewManage argument value.
 * @param[out] new_number_of_ports pointer to the storage location for NewNumberOfPorts argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipc_parse_get_list_of_port_mappings(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_start_port, du_uint16* new_end_port, const du_uchar** new_protocol, du_bool* new_manage, du_uint16* new_number_of_ports);

/**
 * Appends a SOAP response message of GetListOfPortMappings action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_port_listing value of NewPortListing argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_get_list_of_port_mappings_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_port_listing);

/**
 * Parses a SOAP response message of GetListOfPortMappings action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_port_listing pointer to the storage location for NewPortListing argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipc_parse_get_list_of_port_mappings_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_port_listing);

/**
  * Returns the action name of AddAnyPortMapping.
  * @return the action name of AddAnyPortMapping.
  */
extern const du_uchar* digd_wanipc_action_name_add_any_port_mapping(void);

/**
 * Appends a SOAP request message of AddAnyPortMapping action to a <em>xml</em> array.
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
extern du_bool digd_wanipc_make_add_any_port_mapping(du_uchar_array* xml, du_uint32 v, const du_uchar* new_remote_host, du_uint16 new_external_port, const du_uchar* new_protocol, du_uint16 new_internal_port, const du_uchar* new_internal_client, du_bool new_enabled, const du_uchar* new_port_mapping_description, du_uint32 new_lease_duration);

/**
 * Parses a SOAP request message of AddAnyPortMapping action, stores each name and
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
extern du_bool digd_wanipc_parse_add_any_port_mapping(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_remote_host, du_uint16* new_external_port, const du_uchar** new_protocol, du_uint16* new_internal_port, const du_uchar** new_internal_client, du_bool* new_enabled, const du_uchar** new_port_mapping_description, du_uint32* new_lease_duration);

/**
 * Appends a SOAP response message of AddAnyPortMapping action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_reserved_port value of NewReservedPort argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_wanipc_make_add_any_port_mapping_response(du_uchar_array* xml, du_uint32 v, du_uint16 new_reserved_port);

/**
 * Parses a SOAP response message of AddAnyPortMapping action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_reserved_port pointer to the storage location for NewReservedPort argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_wanipc_parse_add_any_port_mapping_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* new_reserved_port);

/**
 * the state variable name of 'ConnectionType'.
 * @return  "ConnectionType" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_connection_type(void);

/**
 * the state variable name of 'PossibleConnectionTypes'.
 * @return  "PossibleConnectionTypes" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_possible_connection_types(void);

/**
 * the state variable name of 'ConnectionStatus'.
 * @return  "ConnectionStatus" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_connection_status(void);

/**
 * the state variable name of 'Uptime'.
 * @return  "Uptime" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_uptime(void);

/**
 * the state variable name of 'LastConnectionError'.
 * @return  "LastConnectionError" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_last_connection_error(void);

/**
 * the state variable name of 'AutoDisconnectTime'.
 * @return  "AutoDisconnectTime" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_auto_disconnect_time(void);

/**
 * the state variable name of 'IdleDisconnectTime'.
 * @return  "IdleDisconnectTime" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_idle_disconnect_time(void);

/**
 * the state variable name of 'WarnDisconnectDelay'.
 * @return  "WarnDisconnectDelay" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_warn_disconnect_delay(void);

/**
 * the state variable name of 'RSIPAvailable'.
 * @return  "RSIPAvailable" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_rsip_available(void);

/**
 * the state variable name of 'NATEnabled'.
 * @return  "NATEnabled" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_nat_enabled(void);

/**
 * the state variable name of 'ExternalIPAddress'.
 * @return  "ExternalIPAddress" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_external_ip_address(void);

/**
 * the state variable name of 'PortMappingNumberOfEntries'.
 * @return  "PortMappingNumberOfEntries" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_port_mapping_number_of_entries(void);

/**
 * the state variable name of 'PortMappingEnabled'.
 * @return  "PortMappingEnabled" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_port_mapping_enabled(void);

/**
 * the state variable name of 'PortMappingLeaseDuration'.
 * @return  "PortMappingLeaseDuration" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_port_mapping_lease_duration(void);

/**
 * the state variable name of 'RemoteHost'.
 * @return  "RemoteHost" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_remote_host(void);

/**
 * the state variable name of 'ExternalPort'.
 * @return  "ExternalPort" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_external_port(void);

/**
 * the state variable name of 'InternalPort'.
 * @return  "InternalPort" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_internal_port(void);

/**
 * the state variable name of 'PortMappingProtocol'.
 * @return  "PortMappingProtocol" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_port_mapping_protocol(void);

/**
 * the state variable name of 'InternalClient'.
 * @return  "InternalClient" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_internal_client(void);

/**
 * the state variable name of 'PortMappingDescription'.
 * @return  "PortMappingDescription" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_port_mapping_description(void);

/**
 * the state variable name of 'SystemUpdateID'.
 * @return  "SystemUpdateID" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_system_update_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_Manage'.
 * @return  "A_ARG_TYPE_Manage" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_a_arg_type_manage(void);

/**
 * the state variable name of 'A_ARG_TYPE_PortListing'.
 * @return  "A_ARG_TYPE_PortListing" string.
 */
extern const du_uchar* digd_wanipc_state_variable_name_a_arg_type_port_listing(void);

/**
 * Returns an error code of 'InactiveConnectionStateRequired'.
 * This error code is "703".
 * @return  "703" string.
 */
extern const du_uchar* digd_wanipc_error_code_inactive_connection_state_required(void);

/**
 * Returns an error code of 'ConnectionSetupFailed'.
 * This error code is "704".
 * @return  "704" string.
 */
extern const du_uchar* digd_wanipc_error_code_connection_setup_failed(void);

/**
 * Returns an error code of 'ConnectionSetupInProgress'.
 * This error code is "705".
 * @return  "705" string.
 */
extern const du_uchar* digd_wanipc_error_code_connection_setup_in_progress(void);

/**
 * Returns an error code of 'ConnectionNotConfigured'.
 * This error code is "706".
 * @return  "706" string.
 */
extern const du_uchar* digd_wanipc_error_code_connection_not_configured(void);

/**
 * Returns an error code of 'DisconnectInProgress'.
 * This error code is "707".
 * @return  "707" string.
 */
extern const du_uchar* digd_wanipc_error_code_disconnect_in_progress(void);

/**
 * Returns an error code of 'InvalidLayer2Address'.
 * This error code is "708".
 * @return  "708" string.
 */
extern const du_uchar* digd_wanipc_error_code_invalid_layer2_address(void);

/**
 * Returns an error code of 'InternetAccessDisabled'.
 * This error code is "709".
 * @return  "709" string.
 */
extern const du_uchar* digd_wanipc_error_code_internet_access_disabled(void);

/**
 * Returns an error code of 'InvalidConnectionType'.
 * This error code is "710".
 * @return  "710" string.
 */
extern const du_uchar* digd_wanipc_error_code_invalid_connection_type(void);

/**
 * Returns an error code of 'ConnectionAlreadyTerminated'.
 * This error code is "711".
 * @return  "711" string.
 */
extern const du_uchar* digd_wanipc_error_code_connection_already_terminated(void);

/**
 * Returns an error code of 'SpecifiedArrayIndexInvalid'.
 * This error code is "713".
 * @return  "713" string.
 */
extern const du_uchar* digd_wanipc_error_code_specified_array_index_invalid(void);

/**
 * Returns an error code of 'NoSuchEntryInArray'.
 * This error code is "714".
 * @return  "714" string.
 */
extern const du_uchar* digd_wanipc_error_code_no_such_entry_in_array(void);

/**
 * Returns an error code of 'WildCardNotPermittedInSrcIP'.
 * This error code is "715".
 * @return  "715" string.
 */
extern const du_uchar* digd_wanipc_error_code_wild_card_not_permitted_in_src_ip(void);

/**
 * Returns an error code of 'WildCardNotPermittedInExtPort'.
 * This error code is "716".
 * @return  "716" string.
 */
extern const du_uchar* digd_wanipc_error_code_wild_card_not_permitted_in_ext_port(void);

/**
 * Returns an error code of 'ConflictInMappingEntry'.
 * This error code is "718".
 * @return  "718" string.
 */
extern const du_uchar* digd_wanipc_error_code_conflict_in_mapping_entry(void);

/**
 * Returns an error code of 'SamePortValuesRequired'.
 * This error code is "724".
 * @return  "724" string.
 */
extern const du_uchar* digd_wanipc_error_code_same_port_values_required(void);

/**
 * Returns an error code of 'OnlyPermanentLeasesSupported'.
 * This error code is "725".
 * @return  "725" string.
 */
extern const du_uchar* digd_wanipc_error_code_only_permanent_leases_supported(void);

/**
 * Returns an error code of 'RemoteHostOnlySupportsWildcard'.
 * This error code is "726".
 * @return  "726" string.
 */
extern const du_uchar* digd_wanipc_error_code_remote_host_only_supports_wildcard(void);

/**
 * Returns an error code of 'ExternalPortOnlySupportsWildcard'.
 * This error code is "727".
 * @return  "727" string.
 */
extern const du_uchar* digd_wanipc_error_code_external_port_only_supports_wildcard(void);

/**
 * Returns an error code of 'NoPortMapsAvailable'.
 * This error code is "728".
 * @return  "728" string.
 */
extern const du_uchar* digd_wanipc_error_code_no_port_maps_available(void);

/**
 * Returns an error code of 'ConflictWithOtherMechanisms'.
 * This error code is "729".
 * @return  "729" string.
 */
extern const du_uchar* digd_wanipc_error_code_conflict_with_other_mechanisms(void);

/**
 * Returns an error code of 'PortMappingNotFound'.
 * This error code is "730".
 * @return  "730" string.
 */
extern const du_uchar* digd_wanipc_error_code_port_mapping_not_found(void);

/**
 * Returns an error code of 'ReadOnly'.
 * This error code is "731".
 * @return  "731" string.
 */
extern const du_uchar* digd_wanipc_error_code_read_only(void);

/**
 * Returns an error code of 'WildCardNotPermittedInIntPort'.
 * This error code is "732".
 * @return  "732" string.
 */
extern const du_uchar* digd_wanipc_error_code_wild_card_not_permitted_in_int_port(void);

/**
 * Returns an error code of 'InconsistentParameters'.
 * This error code is "733".
 * @return  "733" string.
 */
extern const du_uchar* digd_wanipc_error_code_inconsistent_parameters(void);

#ifdef __cplusplus
}
#endif

#endif
