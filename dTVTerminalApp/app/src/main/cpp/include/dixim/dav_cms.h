/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dav_cms.h
 *  @brief The dav_cms interface provides methods for making SOAP request/response messages of
 *   ConnectionManager actions. This interface provides methods for parsing SOAP request/response
 *   messages of ConnectionManager actions.
 *  @see  ConnectionManager:2 Service Template Version 1.01 For UPnP. Version 1.0 section 2.4
 */

#ifndef DAV_CMS_H
#define DAV_CMS_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of GetProtocolInfo.
  * @return the action name of GetProtocolInfo.
  */
extern const du_uchar* dav_cms_action_name_get_protocol_info(void);

/**
 * Appends a SOAP request message of GetProtocolInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_get_protocol_info(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetProtocolInfo action, stores each name and
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
extern du_bool dav_cms_parse_get_protocol_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetProtocolInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] source value of Source argument.
 * @param[in] sink value of Sink argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_get_protocol_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* source, const du_uchar* sink);

/**
 * Parses a SOAP response message of GetProtocolInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] source pointer to the storage location for Source argument value.
 * @param[out] sink pointer to the storage location for Sink argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_get_protocol_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** source, const du_uchar** sink);

/**
  * Returns the action name of PrepareForConnection.
  * @return the action name of PrepareForConnection.
  */
extern const du_uchar* dav_cms_action_name_prepare_for_connection(void);

/**
 * Appends a SOAP request message of PrepareForConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] remote_protocol_info value of RemoteProtocolInfo argument.
 * @param[in] peer_connection_manager value of PeerConnectionManager argument.
 * @param[in] peer_connection_id value of PeerConnectionID argument.
 * @param[in] direction value of Direction argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_prepare_for_connection(du_uchar_array* xml, du_uint32 v, const du_uchar* remote_protocol_info, const du_uchar* peer_connection_manager, du_int32 peer_connection_id, const du_uchar* direction);

/**
 * Parses a SOAP request message of PrepareForConnection action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] remote_protocol_info pointer to the storage location for RemoteProtocolInfo argument value.
 * @param[out] peer_connection_manager pointer to the storage location for PeerConnectionManager argument value.
 * @param[out] peer_connection_id pointer to the storage location for PeerConnectionID argument value.
 * @param[out] direction pointer to the storage location for Direction argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_prepare_for_connection(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** remote_protocol_info, const du_uchar** peer_connection_manager, du_int32* peer_connection_id, const du_uchar** direction);

/**
 * Appends a SOAP response message of PrepareForConnection action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] connection_id value of ConnectionID argument.
 * @param[in] av_transport_id value of AVTransportID argument.
 * @param[in] rcs_id value of RcsID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_prepare_for_connection_response(du_uchar_array* xml, du_uint32 v, du_int32 connection_id, du_int32 av_transport_id, du_int32 rcs_id);

/**
 * Parses a SOAP response message of PrepareForConnection action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] connection_id pointer to the storage location for ConnectionID argument value.
 * @param[out] av_transport_id pointer to the storage location for AVTransportID argument value.
 * @param[out] rcs_id pointer to the storage location for RcsID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_prepare_for_connection_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* connection_id, du_int32* av_transport_id, du_int32* rcs_id);

/**
  * Returns the action name of ConnectionComplete.
  * @return the action name of ConnectionComplete.
  */
extern const du_uchar* dav_cms_action_name_connection_complete(void);

/**
 * Appends a SOAP request message of ConnectionComplete action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] connection_id value of ConnectionID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_connection_complete(du_uchar_array* xml, du_uint32 v, du_int32 connection_id);

/**
 * Parses a SOAP request message of ConnectionComplete action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] connection_id pointer to the storage location for ConnectionID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_connection_complete(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* connection_id);

/**
 * Appends a SOAP response message of ConnectionComplete action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_connection_complete_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of ConnectionComplete action, stores each name and
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
extern du_bool dav_cms_parse_connection_complete_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetCurrentConnectionIDs.
  * @return the action name of GetCurrentConnectionIDs.
  */
extern const du_uchar* dav_cms_action_name_get_current_connection_ids(void);

/**
 * Appends a SOAP request message of GetCurrentConnectionIDs action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_get_current_connection_ids(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetCurrentConnectionIDs action, stores each name and
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
extern du_bool dav_cms_parse_get_current_connection_ids(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetCurrentConnectionIDs action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] connection_ids value of ConnectionIDs argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_get_current_connection_ids_response(du_uchar_array* xml, du_uint32 v, const du_uchar* connection_ids);

/**
 * Parses a SOAP response message of GetCurrentConnectionIDs action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] connection_ids pointer to the storage location for ConnectionIDs argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_get_current_connection_ids_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** connection_ids);

/**
  * Returns the action name of GetCurrentConnectionInfo.
  * @return the action name of GetCurrentConnectionInfo.
  */
extern const du_uchar* dav_cms_action_name_get_current_connection_info(void);

/**
 * Appends a SOAP request message of GetCurrentConnectionInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] connection_id value of ConnectionID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_get_current_connection_info(du_uchar_array* xml, du_uint32 v, du_int32 connection_id);

/**
 * Parses a SOAP request message of GetCurrentConnectionInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] connection_id pointer to the storage location for ConnectionID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_get_current_connection_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* connection_id);

/**
 * Appends a SOAP response message of GetCurrentConnectionInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] rcs_id value of RcsID argument.
 * @param[in] av_transport_id value of AVTransportID argument.
 * @param[in] protocol_info value of ProtocolInfo argument.
 * @param[in] peer_connection_manager value of PeerConnectionManager argument.
 * @param[in] peer_connection_id value of PeerConnectionID argument.
 * @param[in] direction value of Direction argument.
 * @param[in] status value of Status argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cms_make_get_current_connection_info_response(du_uchar_array* xml, du_uint32 v, du_int32 rcs_id, du_int32 av_transport_id, const du_uchar* protocol_info, const du_uchar* peer_connection_manager, du_int32 peer_connection_id, const du_uchar* direction, const du_uchar* status);

/**
 * Parses a SOAP response message of GetCurrentConnectionInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] rcs_id pointer to the storage location for RcsID argument value.
 * @param[out] av_transport_id pointer to the storage location for AVTransportID argument value.
 * @param[out] protocol_info pointer to the storage location for ProtocolInfo argument value.
 * @param[out] peer_connection_manager pointer to the storage location for PeerConnectionManager argument value.
 * @param[out] peer_connection_id pointer to the storage location for PeerConnectionID argument value.
 * @param[out] direction pointer to the storage location for Direction argument value.
 * @param[out] status pointer to the storage location for Status argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cms_parse_get_current_connection_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* rcs_id, du_int32* av_transport_id, const du_uchar** protocol_info, const du_uchar** peer_connection_manager, du_int32* peer_connection_id, const du_uchar** direction, const du_uchar** status);

/**
 * the state variable name of 'SourceProtocolInfo'.
 * @return  "SourceProtocolInfo" string.
 */
extern const du_uchar* dav_cms_state_variable_name_source_protocol_info(void);

/**
 * the state variable name of 'SinkProtocolInfo'.
 * @return  "SinkProtocolInfo" string.
 */
extern const du_uchar* dav_cms_state_variable_name_sink_protocol_info(void);

/**
 * the state variable name of 'CurrentConnectionIDs'.
 * @return  "CurrentConnectionIDs" string.
 */
extern const du_uchar* dav_cms_state_variable_name_current_connection_ids(void);

/**
 * the state variable name of 'A_ARG_TYPE_ConnectionStatus'.
 * @return  "A_ARG_TYPE_ConnectionStatus" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_connection_status(void);

/**
 * the state variable name of 'A_ARG_TYPE_ConnectionManager'.
 * @return  "A_ARG_TYPE_ConnectionManager" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_connection_manager(void);

/**
 * the state variable name of 'A_ARG_TYPE_Direction'.
 * @return  "A_ARG_TYPE_Direction" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_direction(void);

/**
 * the state variable name of 'A_ARG_TYPE_ProtocolInfo'.
 * @return  "A_ARG_TYPE_ProtocolInfo" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_protocol_info(void);

/**
 * the state variable name of 'A_ARG_TYPE_ConnectionID'.
 * @return  "A_ARG_TYPE_ConnectionID" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_connection_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_AVTransportID'.
 * @return  "A_ARG_TYPE_AVTransportID" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_av_transport_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_RcsID'.
 * @return  "A_ARG_TYPE_RcsID" string.
 */
extern const du_uchar* dav_cms_state_variable_name_a_arg_type_rcs_id(void);

/**
 * Returns an error code of 'incompatible protocol info'.
 * This error code is "701".
 * @return  "701" string.
 */
extern const du_uchar* dav_cms_error_code_incompatible_protocol_info(void);

/**
 * Returns an error code of 'incompatible directions'.
 * This error code is "702".
 * @return  "702" string.
 */
extern const du_uchar* dav_cms_error_code_incompatible_directions(void);

/**
 * Returns an error code of 'insufficient network resources'.
 * This error code is "703".
 * @return  "703" string.
 */
extern const du_uchar* dav_cms_error_code_insufficient_network_resources(void);

/**
 * Returns an error code of 'local restrictions'.
 * This error code is "704".
 * @return  "704" string.
 */
extern const du_uchar* dav_cms_error_code_local_restrictions(void);

/**
 * Returns an error code of 'access denied'.
 * This error code is "705".
 * @return  "705" string.
 */
extern const du_uchar* dav_cms_error_code_access_denied(void);

/**
 * Returns an error code of 'invalid connection reference'.
 * This error code is "706".
 * @return  "706" string.
 */
extern const du_uchar* dav_cms_error_code_invalid_connection_reference(void);

/**
 * Returns an error code of 'not in network'.
 * This error code is "707".
 * @return  "707" string.
 */
extern const du_uchar* dav_cms_error_code_not_in_network(void);

/**
 * Returns an error code of 'Connection Table overflow'.
 * This error code is "708".
 * @return  "708" string.
 */
extern const du_uchar* dav_cms_error_code_connection_table_overflow(void);

/**
 * Returns an error code of 'Internal processing resources exceeded'.
 * This error code is "709".
 * @return  "709" string.
 */
extern const du_uchar* dav_cms_error_code_internal_processing_resources_exceeded(void);

/**
 * Returns an error code of 'Internal memory resources exceeded'.
 * This error code is "710".
 * @return  "710" string.
 */
extern const du_uchar* dav_cms_error_code_internal_memory_resources_exceeded(void);

/**
 * Returns an error code of 'Internal storage system capabilities exceeded'.
 * This error code is "711".
 * @return  "711" string.
 */
extern const du_uchar* dav_cms_error_code_internal_storage_system_capabilities_exceeded(void);

#ifdef __cplusplus
}
#endif

#endif
