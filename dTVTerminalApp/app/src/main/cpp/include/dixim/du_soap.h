/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_soap interface provides methods for creating SOAP envelope element
 *  ( such as creating a SOAP Header, a SOAP body for a SOAP request/response message,
 *  parsing a document of request/response envelope element).
 *  UPnP uses SOAP to deliver control messages to devices and return results or errors
 *  back to control points.
 */

#ifndef DU_SOAP_H
#define DU_SOAP_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Gets a xmlns string for SOAP envelope.
 *  This value is "http://schemas.xmlsoap.org/soap/envelope/".
 *  @return  "http://schemas.xmlsoap.org/soap/envelope/" string.
 */
extern const du_uchar* du_soap_ns_envelope(void);

/**
 *  Gets a xmlns string for SOAP envelope, but it doesn't have trailing slash.
 *  Some implementations use this wrong namespace.
 *  This attribute value is "http://schemas.xmlsoap.org/soap/envelope".
 *  @return  "http://schemas.xmlsoap.org/soap/envelope" string.
 */
extern const du_uchar* du_soap_ns_envelope_noslash(void);

/**
 *  Gets a xmlns string for SOAP encoding.
 *  This attribute value is "http://schemas.xmlsoap.org/soap/encoding/".
 *  @return  "http://schemas.xmlsoap.org/soap/encoding/" string.
 */
extern const du_uchar* du_soap_ns_encoding(void);

/**
 *  Appends a start tag of a SOAP envelope element.
 *  @param[in] xml pointer to the destination du_uchar_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark <b>du_soap_envelope_start</b> appends
 *  "<s:Envelope>" with namespace and encoding style attribute and
 *  "<s:Body>" .
 */
extern du_bool du_soap_envelope_start(du_uchar_array* xml);

/**
 *  Appends a start tag of a SOAP request message.
 *  @param[in] xml pointer to the destination du_uchar_array structure.
 *  @param[in] service_type_v URN(Uniform Resource Name) of UPnP service type and version.
 *  @param[in] action_name name of action.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark <b>du_soap_envelope_request_start</b> appends "<u:action_name>"
 *  with xmlns namespace attribute of service_type_v to xml.
 */
extern du_bool du_soap_envelope_request_start(du_uchar_array* xml, const du_uchar* service_type_v, const du_uchar* action_name);

/**
 *  Appends a end tag of a SOAP request message.
 *  @param[in] xml pointer to the destination du_uchar_array structure.
 *  @param[in] action_name name of action.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark <b>du_soap_envelope_request_end</b> appends "</u:action_name>" to xml.
 */
extern du_bool du_soap_envelope_request_end(du_uchar_array* xml, const du_uchar* action_name);

/**
 *  Appends a start tag of a SOAP response message.
 *  @param[in] xml pointer to the destination du_uchar_array structure.
 *  @param[in] service_type_v URN(Uniform Resource Name) of UPnP service type and version.
 *  @param[in] action_name name of action.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark <b>du_soap_envelope_response_start</b> appends "<u:action_nameResponse>"
 *  with xmlns namespace attribute of service_type_v to xml.
 */
extern du_bool du_soap_envelope_response_start(du_uchar_array* xml, const du_uchar* service_type_v, const du_uchar* action_name);

/**
 *  Appends an end tag of a SOAP response message.
 *  @param[in] xml pointer to the destination du_uchar_array structure.
 *  @param[in] action_name name of action.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark <b>du_soap_envelope_response_end</b> appends "</u:action_nameResponse>"
 *    to xml.
 */
extern du_bool du_soap_envelope_response_end(du_uchar_array* xml, const du_uchar* action_name);

/**
 *  Appends an end tag of a BODY element and SOAP envelope element
 *  @param[in] xml pointer to the destination du_uchar_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark <b>du_soap_envelope_end</b> appends "</s:Body>" and "</s:Envelope>" to xml.
 */
extern du_bool du_soap_envelope_end(du_uchar_array* xml);

/**
 *  Parses a request envelope element.
 *  @param[out] param_array pointer to the destination du_str_array structure.
 *  @param[in] xml a string containing all of the document of the request envelope element.
 *  @param[in] xml_len length of xml.
 *  @return  false if a parse error occurred, otherwise true.
 *  @remark param_appay is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 */
extern du_bool du_soap_envelope_parse_request(du_str_array* param_array, const du_uchar* xml, du_uint32 xml_len);

/**
 *  Parses a response envelope element.
 *  @param[out] param_array pointer to the destination du_str_array structure.
 *  @param[in] xml a string containing all of the document of the response envelop element.
 *  @param[in] xml_len length of xml.
 *  @return  false if a parse error occurred, otherwise true.
 *  @remark param_appay is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 */
extern du_bool du_soap_envelope_parse_response(du_str_array* param_array, const du_uchar* xml, du_uint32 xml_len);

/**
 *  Gets a parameter value by name.
 *  @param[in] param_array pointer to the du_str_array structure.
 *  @param[in] name the name whose associated value is to be returned.
 *  @return  the pointer to the value to which name specified,
 *  or null if the param_array contains no data for this name.
 */
extern const du_uchar* du_soap_param_array_get_value(du_str_array* param_array, const du_uchar* name);

/**
 *  Gets the number of the parameter.
 *  @param[in] param_array pointer to the du_str_array structure.
 *  @return  the number of the parameter stored in param_array.
 */
extern du_uint32 du_soap_param_array_get_length(du_str_array* param_array);

#ifdef __cplusplus
}
#endif

#endif
