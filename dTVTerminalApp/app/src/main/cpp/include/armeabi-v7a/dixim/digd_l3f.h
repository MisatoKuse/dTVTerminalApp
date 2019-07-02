/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file digd_l3f.h
 *  @brief The digd_l3f interface provides methods for making SOAP request/response messages of
 *   Layer3Forwarding actions. This interface provides methods for parsing SOAP request/response
 *   messages of Layer3Forwarding actions.
 *  @see  Layer3Forwarding:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DIGD_L3F_H
#define DIGD_L3F_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetDefaultConnectionService.
  * @return the action name of SetDefaultConnectionService.
  */
extern const du_uchar* digd_l3f_action_name_set_default_connection_service(void);

/**
 * Appends a SOAP request message of SetDefaultConnectionService action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_default_connection_service value of NewDefaultConnectionService argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_l3f_make_set_default_connection_service(du_uchar_array* xml, du_uint32 v, const du_uchar* new_default_connection_service);

/**
 * Parses a SOAP request message of SetDefaultConnectionService action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_default_connection_service pointer to the storage location for NewDefaultConnectionService argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_l3f_parse_set_default_connection_service(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_default_connection_service);

/**
 * Appends a SOAP response message of SetDefaultConnectionService action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_l3f_make_set_default_connection_service_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetDefaultConnectionService action, stores each name and
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
extern du_bool digd_l3f_parse_set_default_connection_service_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetDefaultConnectionService.
  * @return the action name of GetDefaultConnectionService.
  */
extern const du_uchar* digd_l3f_action_name_get_default_connection_service(void);

/**
 * Appends a SOAP request message of GetDefaultConnectionService action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_l3f_make_get_default_connection_service(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetDefaultConnectionService action, stores each name and
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
extern du_bool digd_l3f_parse_get_default_connection_service(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetDefaultConnectionService action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_default_connection_service value of NewDefaultConnectionService argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool digd_l3f_make_get_default_connection_service_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_default_connection_service);

/**
 * Parses a SOAP response message of GetDefaultConnectionService action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_default_connection_service pointer to the storage location for NewDefaultConnectionService argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool digd_l3f_parse_get_default_connection_service_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_default_connection_service);

/**
 * the state variable name of 'DefaultConnectionService'.
 * @return  "DefaultConnectionService" string.
 */
extern const du_uchar* digd_l3f_state_variable_name_default_connection_service(void);

#ifdef __cplusplus
}
#endif

#endif
