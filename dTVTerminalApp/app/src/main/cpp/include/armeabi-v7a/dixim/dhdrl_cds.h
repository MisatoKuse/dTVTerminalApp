/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dhdrl_cds.h
 *  @brief The dhdrl_cds interface provides methods for making SOAP request/response messages of
 *   ContentDirectory actions. This interface provides methods for parsing SOAP request/response
 *   messages of HD Recording Link actions for extended ContentDirectory.
 *  @see   HD Recording Link Revision 0.90
 */

#ifndef DHDRL_CDS_H
#define DHDRL_CDS_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of X_HDLnkGetRecordDestinations.
  * @return the action name of X_HDLnkGetRecordDestinations.
  */
extern const du_uchar* dhdrl_cds_action_name_x_hd_lnk_get_record_destinations(void);

/**
 * Appends a SOAP request message of X_HDLnkGetRecordDestinations action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dhdrl_cds_make_x_hd_lnk_get_record_destinations(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of X_HDLnkGetRecordDestinations action, stores each name and
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
extern du_bool dhdrl_cds_parse_x_hd_lnk_get_record_destinations(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of X_HDLnkGetRecordDestinations action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] record_destination_list value of RecordDestinationList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dhdrl_cds_make_x_hd_lnk_get_record_destinations_response(du_uchar_array* xml, du_uint32 v, const du_uchar* record_destination_list);

/**
 * Parses a SOAP response message of X_HDLnkGetRecordDestinations action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] record_destination_list pointer to the storage location for RecordDestinationList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dhdrl_cds_parse_x_hd_lnk_get_record_destinations_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** record_destination_list);

/**
  * Returns the action name of X_HDLnkGetRecordDestinationInfo.
  * @return the action name of X_HDLnkGetRecordDestinationInfo.
  */
extern const du_uchar* dhdrl_cds_action_name_x_hd_lnk_get_record_destination_info(void);

/**
 * Appends a SOAP request message of X_HDLnkGetRecordDestinationInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] record_destination_id value of RecordDestinationID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dhdrl_cds_make_x_hd_lnk_get_record_destination_info(du_uchar_array* xml, du_uint32 v, const du_uchar* record_destination_id);

/**
 * Parses a SOAP request message of X_HDLnkGetRecordDestinationInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] record_destination_id pointer to the storage location for RecordDestinationID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dhdrl_cds_parse_x_hd_lnk_get_record_destination_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** record_destination_id);

/**
 * Appends a SOAP response message of X_HDLnkGetRecordDestinationInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] record_destination_info value of RecordDestinationInfo argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dhdrl_cds_make_x_hd_lnk_get_record_destination_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* record_destination_info);

/**
 * Parses a SOAP response message of X_HDLnkGetRecordDestinationInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] record_destination_info pointer to the storage location for RecordDestinationInfo argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dhdrl_cds_parse_x_hd_lnk_get_record_destination_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** record_destination_info);

/**
  * Returns the action name of X_HDLnkGetRecordContainerID.
  * @return the action name of X_HDLnkGetRecordContainerID.
  */
extern const du_uchar* dhdrl_cds_action_name_x_hd_lnk_get_record_container_id(void);

/**
 * Appends a SOAP request message of X_HDLnkGetRecordContainerID action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] record_destination_id value of RecordDestinationID argument.
 * @param[in] elements value of Elements argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dhdrl_cds_make_x_hd_lnk_get_record_container_id(du_uchar_array* xml, du_uint32 v, const du_uchar* record_destination_id, const du_uchar* elements);

/**
 * Parses a SOAP request message of X_HDLnkGetRecordContainerID action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] record_destination_id pointer to the storage location for RecordDestinationID argument value.
 * @param[out] elements pointer to the storage location for Elements argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dhdrl_cds_parse_x_hd_lnk_get_record_container_id(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** record_destination_id, const du_uchar** elements);

/**
 * Appends a SOAP response message of X_HDLnkGetRecordContainerID action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] container_id value of ContainerID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dhdrl_cds_make_x_hd_lnk_get_record_container_id_response(du_uchar_array* xml, du_uint32 v, const du_uchar* container_id);

/**
 * Parses a SOAP response message of X_HDLnkGetRecordContainerID action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] container_id pointer to the storage location for ContainerID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dhdrl_cds_parse_x_hd_lnk_get_record_container_id_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** container_id);

/**
 * the state variable name of 'A_ARG_TYPE_ObjectID'.
 * @return  "A_ARG_TYPE_ObjectID" string.
 */
extern const du_uchar* dhdrl_cds_state_variable_name_a_arg_type_object_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_Result'.
 * @return  "A_ARG_TYPE_Result" string.
 */
extern const du_uchar* dhdrl_cds_state_variable_name_a_arg_type_result(void);

/**
 * the state variable name of 'X_RecordDestinationList'.
 * @return  "X_RecordDestinationList" string.
 */
extern const du_uchar* dhdrl_cds_state_variable_name_x_record_destination_list(void);

/**
 * the state variable name of 'X_A_ARG_TYPE_RecordDestinationID'.
 * @return  "X_A_ARG_TYPE_RecordDestinationID" string.
 */
extern const du_uchar* dhdrl_cds_state_variable_name_x_a_arg_type_record_destination_id(void);

/**
 * the state variable name of 'X_A_ARG_TYPE_RecordDestinationInfo'.
 * @return  "X_A_ARG_TYPE_RecordDestinationInfo" string.
 */
extern const du_uchar* dhdrl_cds_state_variable_name_x_a_arg_type_record_destination_info(void);

#ifdef __cplusplus
}
#endif

#endif
