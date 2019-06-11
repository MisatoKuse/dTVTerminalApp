/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_soap.h
 *  @brief The dupnp_soap interface provides some methods for creating and
 *  parsing messages of SOAP protocol
 *  ( such as creating a content-type and a SOAPAction header, making and parsing
 *  a request/response envelope element defined by SOAP).
 *  UPnP uses SOAP to deliver control messages to devices and return results or errors
 *  back to control points.
 */

#ifndef DUPNP_SOAP_H
#define DUPNP_SOAP_H

#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Appends a content-type HTTP header string to <em>header</em> array.
 * If <em>header</em> had a content-type HTTP header, replaces it .
 * @param[out] header pointer to the destination du_str_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>header</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 * <b>dupnp_soap_header_set_content_type</b> appends <br>
 * 'content-type: text/xml; charset="utf-8"' to <em>header</em>.
 */
extern du_bool dupnp_soap_header_set_content_type(du_str_array* header);

/**
 * Appends a SOAPAction HTTP header string to <em>header</em> array.
 * If <em>header</em> had a SOAPAction HTTP header, replaces it with new value.
 * @param[out] header pointer to the destination du_str_array structure.
 * @param[in] service_type_v UPnP service type and version.
 * @param[in] action_name name of action.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>header</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 * <b>dupnp_soap_header_set_soapaction</b> appends <br>
 * 'soapaction: "urn:schemas-upnp-org:service:service_type_v#action_name"'
 * to <em>header</em>.
 */
extern du_bool dupnp_soap_header_set_soapaction(du_str_array* header, const du_uchar* service_type_v, const du_uchar* action_name);

/**
 * Appends a User-Agent HTTP header string to <em>header</em> array.
 * If <em>header</em> had a User-Agent HTTP header, replaces it with new value.
 * @param[out] header pointer to the destination du_str_array structure.
 * @param[in] version UPnP service type and version.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>header</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 * <b>dupnp_soap_header_set_user_agent</b> appends <br>
 * user-agent: <em>version</em> <br>
 * to <em>header</em>.
 */
extern du_bool dupnp_soap_header_set_user_agent(du_str_array* header, const du_uchar* version);

/**
 * Makes a SOAP envelope element to query for the value of a state variable
 * and stores it in <em>xml</em>.
 * @param[out] xml pointer to the destination du_uchar_array structure.
 * @param[in] variable_name the name of state variable to be queried.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function stores the elememt in <em>xml</em> in the following format. <br><br>
 * \<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" <br>
 *     s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"\> <br>
 *   \<s:Body\> <br>
 *     \<u:QueryStateVariable xmlns:u="urn:schemas-upnp-org:control-1-0"\> <br>
 *       \<u:varName\><em>variable_name</em>\</u:varName\> <br>
 *     \</u:QueryStateVariable\> <br>
 *   \</s:Body\> <br>
 * \</s:Envelope\> <br>
 */
extern du_bool dupnp_soap_make_query_request(du_uchar_array* xml, const du_uchar* variable_name);

/**
 * Parses response envelope element defined by SOAP to answer a query for the value of
 *  a state variable, stores each name and value of QueryStateVariable element of
 *  the envelope element in <em>param_array</em>, and gets the value of the state variable
 *  specified in query.
 * @param[in] xml a string containing the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure.
 * @param[out] value pointer to the storage location to receive the value of state
 *   variable specified in query.
 * @return  false if a parse error occurred, otherwise true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dupnp_soap_parse_query_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** value);

/**
 * Makes a response envelope element to return a error information and stores it in
 * <em>xml</em>.
 * @param[out] xml pointer to the destination du_uchar_array structure.
 * @param[in] error_code value code identifying what error was encountered.
 * @param[in] error_description short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function stores the elememt in <em>xml</em> in the following format. <br><br>
 * \<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" <br>
 *   s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"\> <br>
 *  \<s:Body\> <br>
 *   \<s:Fault\> <br>
 *    \<faultcode\>s:Client\</faultcode\> <br>
 *      \<faultstring>UPnPError</faultstring\> <br>
 *      \<detail\> <br>
 *        \<UPnPError xmlns="urn:schemas-upnp-org:control-1-0"\>  <br>
 *          \<errorCode><em>error_code</em></errorCode\>  <br>
 *          \<errorDescription\><em>error_description</em>\</errorDescription\>  <br>
 *        \</UPnPError\> <br>
 *      \</detail\> <br>
 *    \</s:Fault\> <br>
 *  \</s:Body\> <br>
 *\</s:Envelope\> <br>
 */
extern du_bool dupnp_soap_make_error_response(du_uchar_array* xml, const du_uchar* error_code, const du_uchar* error_description);

/**
 * Parses response envelope element defined by SOAP to indicate the reason why the service did not
 * return a value for the variable, stores each name and value of UPnPError element of
 *  the envelope element in <em>param_array</em>, and gets the error code and error description
 * @param[in] xml a string containing the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure.
 * @param[out] error_code pointer to the storage location to receive the error_code
 * @param[out] error_description pointer to the storage location to receive the error description
 * @return  false if a parse error occurred, otherwise true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dupnp_soap_parse_error_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** error_code, const du_uchar** error_description);

/**
 * Returns pointer to "QeryStateVariable" string.
 * @return pointer to "QeryStateVariable" string.
 */
extern const du_uchar* dupnp_soap_soapaction_fragment_query_state_variable(void);

/**
 * Returns an error code of invalid action.
 * This error code is "401".
 * @return  "401" string.
 * @remark invalid action indicates that there is no action of the
 * specified name in the service.
 */
extern const du_uchar* dupnp_soap_error_code_invalid_action(void);

/**
 * Returns an error code of invalid args.
 * This error code is "402".
 * @return  "402" string.
 * @remark invalid args indicates one of the following,<br>
 *  not enough in arguments, too many in arguments, wrong argument(s) name,
 * or wrong data type(s).
 */
extern const du_uchar* dupnp_soap_error_code_invalid_args(void);

/**
 * Returns an error code of invalid variable.
 * This error code is "404".
 * @return  "404" string.
 * @remark invalid variable indicates that there is no state variable of the
 * specified name in the service.
 */
extern const du_uchar* dupnp_soap_error_code_invalid_var(void);

/**
 * Returns an error code of action failed.
 * This error code is "501".
 * @return  "501" string.
 * @remark "action failed" may mean that current state of service prevents
 * invoking requested action.
 */
extern const du_uchar* dupnp_soap_error_code_action_failed(void);

/**
 * Returns an error code of invalid argument value.
 * This error code is "600".
 * @return  "600" string.
 */
extern const du_uchar* dupnp_soap_error_code_argument_value_invalid(void);

/**
 * Returns an error code that indicates the status argument value is out of range.
 * This error code is "601".
 * @return  "601" string.
 */
extern const du_uchar* dupnp_soap_error_code_argument_value_out_of_range(void);

/**
 * Returns an error code that indicates the optional action required is not implemented
 *  in the service.
 * This error code is "602".
 * @return  "602" string.
 */
extern const du_uchar* dupnp_soap_error_code_optional_action_not_implemented(void);

/**
 * Returns an error code that indicates out of memory error occurred.
 * This error code is "603".
 * @return  "603" string.
 */
extern const du_uchar* dupnp_soap_error_code_out_of_memory(void);

/**
 * Returns an error code that indicates human intervention is required.
 * This error code is "604".
 * @return  "604" string.
 */
extern const du_uchar* dupnp_soap_error_code_human_intervention_required(void);

/**
 * Returns an error code that indicates the argument string is too long.
 * This error code is "605".
 * @return  "605" string.
 */
extern const du_uchar* dupnp_soap_error_code_string_argument_too_long(void);

/**
 * Returns an error code that indicates the requested action is not authorized.
 * This error code is "606".
 * @return  "606" string.
 */
extern const du_uchar* dupnp_soap_error_code_action_not_authorized(void);

/**
 * Returns an error code that indicates the signature is not correct.
 * This error code is "607".
 * @return  "607" string.
 */
extern const du_uchar* dupnp_soap_error_code_signature_failure(void);

/**
 * Returns an error code that indicates the signature is not defined.
 * This error code is "608".
 * @return  "608" string.
 */
extern const du_uchar* dupnp_soap_error_code_signature_missing(void);

/**
 * Returns an error code that indicates data is not encrypted.
 * This error code is "609".
 * @return  "609" string.
 */
extern const du_uchar* dupnp_soap_error_code_not_encrypted(void);

/**
 * Returns an error code that indicates the specified sequence is not correct.
 * This error code is "610".
 * @return  "610" string.
 */
extern const du_uchar* dupnp_soap_error_code_invalid_sequence(void);

/**
 * Returns an error code that indicates the specified control url is not correct.
 * This error code is "611".
 * @return  "611" string.
 */
extern const du_uchar* dupnp_soap_error_code_invalid_control_url(void);


/**
 * Returns an error code that indicates the specified session is not correct.
 * This error code is "612".
 * @return  "612" string.
 */
extern const du_uchar* dupnp_soap_error_code_no_such_session(void);

#ifdef __cplusplus
}
#endif

#endif
