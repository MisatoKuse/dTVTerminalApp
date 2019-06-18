/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dav_cds.h
 *  @brief The dav_cds interface provides methods for making SOAP request/response messages of
 *   ContentDirectory actions. This interface provides methods for parsing SOAP request/response
 *   messages of ContentDirectory actions.
 *  @see  ContentDirectory:3 Service Template Version 1.01 For UPnP. Version 1.0 section 2.5
 */

#ifndef DAV_CDS_H
#define DAV_CDS_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of GetSearchCapabilities.
  * @return the action name of GetSearchCapabilities.
  */
extern const du_uchar* dav_cds_action_name_get_search_capabilities(void);

/**
 * Appends a SOAP request message of GetSearchCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_search_capabilities(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetSearchCapabilities action, stores each name and
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
extern du_bool dav_cds_parse_get_search_capabilities(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetSearchCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] search_caps value of SearchCaps argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_search_capabilities_response(du_uchar_array* xml, du_uint32 v, const du_uchar* search_caps);

/**
 * Parses a SOAP response message of GetSearchCapabilities action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] search_caps pointer to the storage location for SearchCaps argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_search_capabilities_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** search_caps);

/**
  * Returns the action name of GetSortCapabilities.
  * @return the action name of GetSortCapabilities.
  */
extern const du_uchar* dav_cds_action_name_get_sort_capabilities(void);

/**
 * Appends a SOAP request message of GetSortCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_sort_capabilities(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetSortCapabilities action, stores each name and
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
extern du_bool dav_cds_parse_get_sort_capabilities(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetSortCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] sort_caps value of SortCaps argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_sort_capabilities_response(du_uchar_array* xml, du_uint32 v, const du_uchar* sort_caps);

/**
 * Parses a SOAP response message of GetSortCapabilities action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] sort_caps pointer to the storage location for SortCaps argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_sort_capabilities_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** sort_caps);

/**
  * Returns the action name of GetSortExtensionCapabilities.
  * @return the action name of GetSortExtensionCapabilities.
  */
extern const du_uchar* dav_cds_action_name_get_sort_extension_capabilities(void);

/**
 * Appends a SOAP request message of GetSortExtensionCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_sort_extension_capabilities(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetSortExtensionCapabilities action, stores each name and
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
extern du_bool dav_cds_parse_get_sort_extension_capabilities(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetSortExtensionCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] sort_extension_caps value of SortExtensionCaps argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_sort_extension_capabilities_response(du_uchar_array* xml, du_uint32 v, const du_uchar* sort_extension_caps);

/**
 * Parses a SOAP response message of GetSortExtensionCapabilities action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] sort_extension_caps pointer to the storage location for SortExtensionCaps argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_sort_extension_capabilities_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** sort_extension_caps);

/**
  * Returns the action name of GetFeatureList.
  * @return the action name of GetFeatureList.
  */
extern const du_uchar* dav_cds_action_name_get_feature_list(void);

/**
 * Appends a SOAP request message of GetFeatureList action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_feature_list(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetFeatureList action, stores each name and
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
extern du_bool dav_cds_parse_get_feature_list(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetFeatureList action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] feature_list value of FeatureList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_feature_list_response(du_uchar_array* xml, du_uint32 v, const du_uchar* feature_list);

/**
 * Parses a SOAP response message of GetFeatureList action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] feature_list pointer to the storage location for FeatureList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_feature_list_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** feature_list);

/**
  * Returns the action name of GetSystemUpdateID.
  * @return the action name of GetSystemUpdateID.
  */
extern const du_uchar* dav_cds_action_name_get_system_update_id(void);

/**
 * Appends a SOAP request message of GetSystemUpdateID action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_system_update_id(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetSystemUpdateID action, stores each name and
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
extern du_bool dav_cds_parse_get_system_update_id(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetSystemUpdateID action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] id value of Id argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_system_update_id_response(du_uchar_array* xml, du_uint32 v, du_uint32 id);

/**
 * Parses a SOAP response message of GetSystemUpdateID action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] id pointer to the storage location for Id argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_system_update_id_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* id);

/**
  * Returns the action name of GetServiceResetToken.
  * @return the action name of GetServiceResetToken.
  */
extern const du_uchar* dav_cds_action_name_get_service_reset_token(void);

/**
 * Appends a SOAP request message of GetServiceResetToken action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_service_reset_token(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetServiceResetToken action, stores each name and
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
extern du_bool dav_cds_parse_get_service_reset_token(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetServiceResetToken action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] reset_token value of ResetToken argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_service_reset_token_response(du_uchar_array* xml, du_uint32 v, const du_uchar* reset_token);

/**
 * Parses a SOAP response message of GetServiceResetToken action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] reset_token pointer to the storage location for ResetToken argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_service_reset_token_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** reset_token);

/**
  * Returns the action name of Browse.
  * @return the action name of Browse.
  */
extern const du_uchar* dav_cds_action_name_browse(void);

/**
 * Appends a SOAP request message of Browse action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] object_id value of ObjectID argument.
 * @param[in] browse_flag value of BrowseFlag argument.
 * @param[in] filter value of Filter argument.
 * @param[in] starting_index value of StartingIndex argument.
 * @param[in] requested_count value of RequestedCount argument.
 * @param[in] sort_criteria value of SortCriteria argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_browse(du_uchar_array* xml, du_uint32 v, const du_uchar* object_id, const du_uchar* browse_flag, const du_uchar* filter, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria);

/**
 * Parses a SOAP request message of Browse action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] object_id pointer to the storage location for ObjectID argument value.
 * @param[out] browse_flag pointer to the storage location for BrowseFlag argument value.
 * @param[out] filter pointer to the storage location for Filter argument value.
 * @param[out] starting_index pointer to the storage location for StartingIndex argument value.
 * @param[out] requested_count pointer to the storage location for RequestedCount argument value.
 * @param[out] sort_criteria pointer to the storage location for SortCriteria argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_browse(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** object_id, const du_uchar** browse_flag, const du_uchar** filter, du_uint32* starting_index, du_uint32* requested_count, const du_uchar** sort_criteria);

/**
 * Appends a SOAP response message of Browse action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] result value of Result argument.
 * @param[in] number_returned value of NumberReturned argument.
 * @param[in] total_matches value of TotalMatches argument.
 * @param[in] update_id value of UpdateID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_browse_response(du_uchar_array* xml, du_uint32 v, const du_uchar* result, du_uint32 number_returned, du_uint32 total_matches, du_uint32 update_id);

/**
 * Parses a SOAP response message of Browse action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] result pointer to the storage location for Result argument value.
 * @param[out] number_returned pointer to the storage location for NumberReturned argument value.
 * @param[out] total_matches pointer to the storage location for TotalMatches argument value.
 * @param[out] update_id pointer to the storage location for UpdateID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_browse_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** result, du_uint32* number_returned, du_uint32* total_matches, du_uint32* update_id);

/**
  * Returns the action name of Search.
  * @return the action name of Search.
  */
extern const du_uchar* dav_cds_action_name_search(void);

/**
 * Appends a SOAP request message of Search action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] container_id value of ContainerID argument.
 * @param[in] search_criteria value of SearchCriteria argument.
 * @param[in] filter value of Filter argument.
 * @param[in] starting_index value of StartingIndex argument.
 * @param[in] requested_count value of RequestedCount argument.
 * @param[in] sort_criteria value of SortCriteria argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_search(du_uchar_array* xml, du_uint32 v, const du_uchar* container_id, const du_uchar* search_criteria, const du_uchar* filter, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria);

/**
 * Parses a SOAP request message of Search action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] container_id pointer to the storage location for ContainerID argument value.
 * @param[out] search_criteria pointer to the storage location for SearchCriteria argument value.
 * @param[out] filter pointer to the storage location for Filter argument value.
 * @param[out] starting_index pointer to the storage location for StartingIndex argument value.
 * @param[out] requested_count pointer to the storage location for RequestedCount argument value.
 * @param[out] sort_criteria pointer to the storage location for SortCriteria argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_search(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** container_id, const du_uchar** search_criteria, const du_uchar** filter, du_uint32* starting_index, du_uint32* requested_count, const du_uchar** sort_criteria);

/**
 * Appends a SOAP response message of Search action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] result value of Result argument.
 * @param[in] number_returned value of NumberReturned argument.
 * @param[in] total_matches value of TotalMatches argument.
 * @param[in] update_id value of UpdateID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_search_response(du_uchar_array* xml, du_uint32 v, const du_uchar* result, du_uint32 number_returned, du_uint32 total_matches, du_uint32 update_id);

/**
 * Parses a SOAP response message of Search action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] result pointer to the storage location for Result argument value.
 * @param[out] number_returned pointer to the storage location for NumberReturned argument value.
 * @param[out] total_matches pointer to the storage location for TotalMatches argument value.
 * @param[out] update_id pointer to the storage location for UpdateID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_search_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** result, du_uint32* number_returned, du_uint32* total_matches, du_uint32* update_id);

/**
  * Returns the action name of CreateObject.
  * @return the action name of CreateObject.
  */
extern const du_uchar* dav_cds_action_name_create_object(void);

/**
 * Appends a SOAP request message of CreateObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] container_id value of ContainerID argument.
 * @param[in] elements value of Elements argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_create_object(du_uchar_array* xml, du_uint32 v, const du_uchar* container_id, const du_uchar* elements);

/**
 * Parses a SOAP request message of CreateObject action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] container_id pointer to the storage location for ContainerID argument value.
 * @param[out] elements pointer to the storage location for Elements argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_create_object(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** container_id, const du_uchar** elements);

/**
 * Appends a SOAP response message of CreateObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] object_id value of ObjectID argument.
 * @param[in] result value of Result argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_create_object_response(du_uchar_array* xml, du_uint32 v, const du_uchar* object_id, const du_uchar* result);

/**
 * Parses a SOAP response message of CreateObject action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] object_id pointer to the storage location for ObjectID argument value.
 * @param[out] result pointer to the storage location for Result argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_create_object_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** object_id, const du_uchar** result);

/**
  * Returns the action name of DestroyObject.
  * @return the action name of DestroyObject.
  */
extern const du_uchar* dav_cds_action_name_destroy_object(void);

/**
 * Appends a SOAP request message of DestroyObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] object_id value of ObjectID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_destroy_object(du_uchar_array* xml, du_uint32 v, const du_uchar* object_id);

/**
 * Parses a SOAP request message of DestroyObject action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] object_id pointer to the storage location for ObjectID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_destroy_object(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** object_id);

/**
 * Appends a SOAP response message of DestroyObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_destroy_object_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DestroyObject action, stores each name and
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
extern du_bool dav_cds_parse_destroy_object_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of UpdateObject.
  * @return the action name of UpdateObject.
  */
extern const du_uchar* dav_cds_action_name_update_object(void);

/**
 * Appends a SOAP request message of UpdateObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] object_id value of ObjectID argument.
 * @param[in] current_tag_value value of CurrentTagValue argument.
 * @param[in] new_tag_value value of NewTagValue argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_update_object(du_uchar_array* xml, du_uint32 v, const du_uchar* object_id, const du_uchar* current_tag_value, const du_uchar* new_tag_value);

/**
 * Parses a SOAP request message of UpdateObject action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] object_id pointer to the storage location for ObjectID argument value.
 * @param[out] current_tag_value pointer to the storage location for CurrentTagValue argument value.
 * @param[out] new_tag_value pointer to the storage location for NewTagValue argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_update_object(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** object_id, const du_uchar** current_tag_value, const du_uchar** new_tag_value);

/**
 * Appends a SOAP response message of UpdateObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_update_object_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of UpdateObject action, stores each name and
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
extern du_bool dav_cds_parse_update_object_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of MoveObject.
  * @return the action name of MoveObject.
  */
extern const du_uchar* dav_cds_action_name_move_object(void);

/**
 * Appends a SOAP request message of MoveObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] object_id value of ObjectID argument.
 * @param[in] new_parent_id value of NewParentID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_move_object(du_uchar_array* xml, du_uint32 v, const du_uchar* object_id, const du_uchar* new_parent_id);

/**
 * Parses a SOAP request message of MoveObject action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] object_id pointer to the storage location for ObjectID argument value.
 * @param[out] new_parent_id pointer to the storage location for NewParentID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_move_object(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** object_id, const du_uchar** new_parent_id);

/**
 * Appends a SOAP response message of MoveObject action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_object_id value of NewObjectID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_move_object_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_object_id);

/**
 * Parses a SOAP response message of MoveObject action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_object_id pointer to the storage location for NewObjectID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_move_object_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_object_id);

/**
  * Returns the action name of ImportResource.
  * @return the action name of ImportResource.
  */
extern const du_uchar* dav_cds_action_name_import_resource(void);

/**
 * Appends a SOAP request message of ImportResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] source_uri value of SourceURI argument.
 * @param[in] destination_uri value of DestinationURI argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_import_resource(du_uchar_array* xml, du_uint32 v, const du_uchar* source_uri, const du_uchar* destination_uri);

/**
 * Parses a SOAP request message of ImportResource action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] source_uri pointer to the storage location for SourceURI argument value.
 * @param[out] destination_uri pointer to the storage location for DestinationURI argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_import_resource(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** source_uri, const du_uchar** destination_uri);

/**
 * Appends a SOAP response message of ImportResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] transfer_id value of TransferID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_import_resource_response(du_uchar_array* xml, du_uint32 v, du_uint32 transfer_id);

/**
 * Parses a SOAP response message of ImportResource action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] transfer_id pointer to the storage location for TransferID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_import_resource_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* transfer_id);

/**
  * Returns the action name of ExportResource.
  * @return the action name of ExportResource.
  */
extern const du_uchar* dav_cds_action_name_export_resource(void);

/**
 * Appends a SOAP request message of ExportResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] source_uri value of SourceURI argument.
 * @param[in] destination_uri value of DestinationURI argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_export_resource(du_uchar_array* xml, du_uint32 v, const du_uchar* source_uri, const du_uchar* destination_uri);

/**
 * Parses a SOAP request message of ExportResource action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] source_uri pointer to the storage location for SourceURI argument value.
 * @param[out] destination_uri pointer to the storage location for DestinationURI argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_export_resource(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** source_uri, const du_uchar** destination_uri);

/**
 * Appends a SOAP response message of ExportResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] transfer_id value of TransferID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_export_resource_response(du_uchar_array* xml, du_uint32 v, du_uint32 transfer_id);

/**
 * Parses a SOAP response message of ExportResource action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] transfer_id pointer to the storage location for TransferID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_export_resource_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* transfer_id);

/**
  * Returns the action name of StopTransferResource.
  * @return the action name of StopTransferResource.
  */
extern const du_uchar* dav_cds_action_name_stop_transfer_resource(void);

/**
 * Appends a SOAP request message of StopTransferResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] transfer_id value of TransferID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_stop_transfer_resource(du_uchar_array* xml, du_uint32 v, du_uint32 transfer_id);

/**
 * Parses a SOAP request message of StopTransferResource action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] transfer_id pointer to the storage location for TransferID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_stop_transfer_resource(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* transfer_id);

/**
 * Appends a SOAP response message of StopTransferResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_stop_transfer_resource_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of StopTransferResource action, stores each name and
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
extern du_bool dav_cds_parse_stop_transfer_resource_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of DeleteResource.
  * @return the action name of DeleteResource.
  */
extern const du_uchar* dav_cds_action_name_delete_resource(void);

/**
 * Appends a SOAP request message of DeleteResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] resource_uri value of ResourceURI argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_delete_resource(du_uchar_array* xml, du_uint32 v, const du_uchar* resource_uri);

/**
 * Parses a SOAP request message of DeleteResource action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] resource_uri pointer to the storage location for ResourceURI argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_delete_resource(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** resource_uri);

/**
 * Appends a SOAP response message of DeleteResource action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_delete_resource_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of DeleteResource action, stores each name and
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
extern du_bool dav_cds_parse_delete_resource_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetTransferProgress.
  * @return the action name of GetTransferProgress.
  */
extern const du_uchar* dav_cds_action_name_get_transfer_progress(void);

/**
 * Appends a SOAP request message of GetTransferProgress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] transfer_id value of TransferID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_transfer_progress(du_uchar_array* xml, du_uint32 v, du_uint32 transfer_id);

/**
 * Parses a SOAP request message of GetTransferProgress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] transfer_id pointer to the storage location for TransferID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_transfer_progress(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* transfer_id);

/**
 * Appends a SOAP response message of GetTransferProgress action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] transfer_status value of TransferStatus argument.
 * @param[in] transfer_length value of TransferLength argument.
 * @param[in] transfer_total value of TransferTotal argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_transfer_progress_response(du_uchar_array* xml, du_uint32 v, const du_uchar* transfer_status, const du_uchar* transfer_length, const du_uchar* transfer_total);

/**
 * Parses a SOAP response message of GetTransferProgress action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] transfer_status pointer to the storage location for TransferStatus argument value.
 * @param[out] transfer_length pointer to the storage location for TransferLength argument value.
 * @param[out] transfer_total pointer to the storage location for TransferTotal argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_transfer_progress_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** transfer_status, const du_uchar** transfer_length, const du_uchar** transfer_total);

/**
  * Returns the action name of CreateReference.
  * @return the action name of CreateReference.
  */
extern const du_uchar* dav_cds_action_name_create_reference(void);

/**
 * Appends a SOAP request message of CreateReference action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] container_id value of ContainerID argument.
 * @param[in] object_id value of ObjectID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_create_reference(du_uchar_array* xml, du_uint32 v, const du_uchar* container_id, const du_uchar* object_id);

/**
 * Parses a SOAP request message of CreateReference action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] container_id pointer to the storage location for ContainerID argument value.
 * @param[out] object_id pointer to the storage location for ObjectID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_create_reference(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** container_id, const du_uchar** object_id);

/**
 * Appends a SOAP response message of CreateReference action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] new_id value of NewID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_create_reference_response(du_uchar_array* xml, du_uint32 v, const du_uchar* new_id);

/**
 * Parses a SOAP response message of CreateReference action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] new_id pointer to the storage location for NewID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_create_reference_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** new_id);

/**
  * Returns the action name of FreeFormQuery.
  * @return the action name of FreeFormQuery.
  */
extern const du_uchar* dav_cds_action_name_free_form_query(void);

/**
 * Appends a SOAP request message of FreeFormQuery action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] container_id value of ContainerID argument.
 * @param[in] cds_view value of CDSView argument.
 * @param[in] query_request value of QueryRequest argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_free_form_query(du_uchar_array* xml, du_uint32 v, const du_uchar* container_id, du_uint32 cds_view, const du_uchar* query_request);

/**
 * Parses a SOAP request message of FreeFormQuery action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] container_id pointer to the storage location for ContainerID argument value.
 * @param[out] cds_view pointer to the storage location for CDSView argument value.
 * @param[out] query_request pointer to the storage location for QueryRequest argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_free_form_query(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** container_id, du_uint32* cds_view, const du_uchar** query_request);

/**
 * Appends a SOAP response message of FreeFormQuery action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] query_result value of QueryResult argument.
 * @param[in] update_id value of UpdateID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_free_form_query_response(du_uchar_array* xml, du_uint32 v, const du_uchar* query_result, du_uint32 update_id);

/**
 * Parses a SOAP response message of FreeFormQuery action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] query_result pointer to the storage location for QueryResult argument value.
 * @param[out] update_id pointer to the storage location for UpdateID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_free_form_query_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** query_result, du_uint32* update_id);

/**
  * Returns the action name of GetFreeFormQueryCapabilities.
  * @return the action name of GetFreeFormQueryCapabilities.
  */
extern const du_uchar* dav_cds_action_name_get_free_form_query_capabilities(void);

/**
 * Appends a SOAP request message of GetFreeFormQueryCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_free_form_query_capabilities(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetFreeFormQueryCapabilities action, stores each name and
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
extern du_bool dav_cds_parse_get_free_form_query_capabilities(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetFreeFormQueryCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] ffq_capabilities value of FFQCapabilities argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_get_free_form_query_capabilities_response(du_uchar_array* xml, du_uint32 v, const du_uchar* ffq_capabilities);

/**
 * Parses a SOAP response message of GetFreeFormQueryCapabilities action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] ffq_capabilities pointer to the storage location for FFQCapabilities argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_get_free_form_query_capabilities_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** ffq_capabilities);

/**
  * Returns the action name of X_GetDLNAUploadProfiles.
  * @return the action name of X_GetDLNAUploadProfiles.
  */
extern const du_uchar* dav_cds_action_name_x_get_dlna_upload_profiles(void);

/**
 * Appends a SOAP request message of X_GetDLNAUploadProfiles action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] upload_profiles value of UploadProfiles argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_x_get_dlna_upload_profiles(du_uchar_array* xml, du_uint32 v, const du_uchar* upload_profiles);

/**
 * Parses a SOAP request message of X_GetDLNAUploadProfiles action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] upload_profiles pointer to the storage location for UploadProfiles argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_x_get_dlna_upload_profiles(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** upload_profiles);

/**
 * Appends a SOAP response message of X_GetDLNAUploadProfiles action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] supported_upload_profiles value of SupportedUploadProfiles argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_cds_make_x_get_dlna_upload_profiles_response(du_uchar_array* xml, du_uint32 v, const du_uchar* supported_upload_profiles);

/**
 * Parses a SOAP response message of X_GetDLNAUploadProfiles action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] supported_upload_profiles pointer to the storage location for SupportedUploadProfiles argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_cds_parse_x_get_dlna_upload_profiles_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** supported_upload_profiles);

/**
 * the state variable name of 'SearchCapabilities'.
 * @return  "SearchCapabilities" string.
 */
extern const du_uchar* dav_cds_state_variable_name_search_capabilities(void);

/**
 * the state variable name of 'SortCapabilities'.
 * @return  "SortCapabilities" string.
 */
extern const du_uchar* dav_cds_state_variable_name_sort_capabilities(void);

/**
 * the state variable name of 'SortExtensionCapabilities'.
 * @return  "SortExtensionCapabilities" string.
 */
extern const du_uchar* dav_cds_state_variable_name_sort_extension_capabilities(void);

/**
 * the state variable name of 'SystemUpdateID'.
 * @return  "SystemUpdateID" string.
 */
extern const du_uchar* dav_cds_state_variable_name_system_update_id(void);

/**
 * the state variable name of 'ContainerUpdateIDs'.
 * @return  "ContainerUpdateIDs" string.
 */
extern const du_uchar* dav_cds_state_variable_name_container_update_ids(void);

/**
 * the state variable name of 'ServiceResetToken'.
 * @return  "ServiceResetToken" string.
 */
extern const du_uchar* dav_cds_state_variable_name_service_reset_token(void);

/**
 * the state variable name of 'LastChange'.
 * @return  "LastChange" string.
 */
extern const du_uchar* dav_cds_state_variable_name_last_change(void);

/**
 * the state variable name of 'TransferIDs'.
 * @return  "TransferIDs" string.
 */
extern const du_uchar* dav_cds_state_variable_name_transfer_ids(void);

/**
 * the state variable name of 'FeatureList'.
 * @return  "FeatureList" string.
 */
extern const du_uchar* dav_cds_state_variable_name_feature_list(void);

/**
 * the state variable name of 'A_ARG_TYPE_ObjectID'.
 * @return  "A_ARG_TYPE_ObjectID" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_object_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_Result'.
 * @return  "A_ARG_TYPE_Result" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_result(void);

/**
 * the state variable name of 'A_ARG_TYPE_SearchCriteria'.
 * @return  "A_ARG_TYPE_SearchCriteria" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_search_criteria(void);

/**
 * the state variable name of 'A_ARG_TYPE_BrowseFlag'.
 * @return  "A_ARG_TYPE_BrowseFlag" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_browse_flag(void);

/**
 * the state variable name of 'A_ARG_TYPE_Filter'.
 * @return  "A_ARG_TYPE_Filter" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_filter(void);

/**
 * the state variable name of 'A_ARG_TYPE_SortCriteria'.
 * @return  "A_ARG_TYPE_SortCriteria" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_sort_criteria(void);

/**
 * the state variable name of 'A_ARG_TYPE_Index'.
 * @return  "A_ARG_TYPE_Index" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_index(void);

/**
 * the state variable name of 'A_ARG_TYPE_Count'.
 * @return  "A_ARG_TYPE_Count" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_count(void);

/**
 * the state variable name of 'A_ARG_TYPE_UpdateID'.
 * @return  "A_ARG_TYPE_UpdateID" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_update_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_TransferID'.
 * @return  "A_ARG_TYPE_TransferID" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_transfer_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_TransferStatus'.
 * @return  "A_ARG_TYPE_TransferStatus" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_transfer_status(void);

/**
 * the state variable name of 'A_ARG_TYPE_TransferLength'.
 * @return  "A_ARG_TYPE_TransferLength" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_transfer_length(void);

/**
 * the state variable name of 'A_ARG_TYPE_TransferTotal'.
 * @return  "A_ARG_TYPE_TransferTotal" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_transfer_total(void);

/**
 * the state variable name of 'A_ARG_TYPE_TagValueList'.
 * @return  "A_ARG_TYPE_TagValueList" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_tag_value_list(void);

/**
 * the state variable name of 'A_ARG_TYPE_URI'.
 * @return  "A_ARG_TYPE_URI" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_uri(void);

/**
 * the state variable name of 'A_ARG_TYPE_CDSView'.
 * @return  "A_ARG_TYPE_CDSView" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_cds_view(void);

/**
 * the state variable name of 'A_ARG_TYPE_QueryRequest'.
 * @return  "A_ARG_TYPE_QueryRequest" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_query_request(void);

/**
 * the state variable name of 'A_ARG_TYPE_QueryResult'.
 * @return  "A_ARG_TYPE_QueryResult" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_query_result(void);

/**
 * the state variable name of 'A_ARG_TYPE_FFQCapabilities'.
 * @return  "A_ARG_TYPE_FFQCapabilities" string.
 */
extern const du_uchar* dav_cds_state_variable_name_a_arg_type_ffq_capabilities(void);

/**
 * the state variable name of 'X_A_ARG_Type_UploadProfiles'.
 * @return  "X_A_ARG_Type_UploadProfiles" string.
 */
extern const du_uchar* dav_cds_state_variable_name_x_a_arg_type_upload_profiles(void);

/**
 * the state variable name of 'X_A_ARG_Type_SupportedUploadProfiles'.
 * @return  "X_A_ARG_Type_SupportedUploadProfiles" string.
 */
extern const du_uchar* dav_cds_state_variable_name_x_a_arg_type_supported_upload_profiles(void);

/**
 * Returns an error code of 'no such object'.
 * This error code is "701".
 * @return  "701" string.
 */
extern const du_uchar* dav_cds_error_code_no_such_object(void);

/**
 * Returns an error code of 'invalid currentTagValue'.
 * This error code is "702".
 * @return  "702" string.
 */
extern const du_uchar* dav_cds_error_code_invalid_current_tag_value(void);

/**
 * Returns an error code of 'invalid newTagValue'.
 * This error code is "703".
 * @return  "703" string.
 */
extern const du_uchar* dav_cds_error_code_invalid_new_tag_value(void);

/**
 * Returns an error code of 'required tag'.
 * This error code is "704".
 * @return  "704" string.
 */
extern const du_uchar* dav_cds_error_code_required_tag(void);

/**
 * Returns an error code of 'read only tag'.
 * This error code is "705".
 * @return  "705" string.
 */
extern const du_uchar* dav_cds_error_code_read_only_tag(void);

/**
 * Returns an error code of 'parameter mismatch'.
 * This error code is "706".
 * @return  "706" string.
 */
extern const du_uchar* dav_cds_error_code_parameter_mismatch(void);

/**
 * Returns an error code of 'unsupported or invalid search criteria'.
 * This error code is "708".
 * @return  "708" string.
 */
extern const du_uchar* dav_cds_error_code_unsupported_or_invalid_search_criteria(void);

/**
 * Returns an error code of 'unsupported or invalid sort criteria'.
 * This error code is "709".
 * @return  "709" string.
 */
extern const du_uchar* dav_cds_error_code_unsupported_or_invalid_sort_criteria(void);

/**
 * Returns an error code of 'no such container'.
 * This error code is "710".
 * @return  "710" string.
 */
extern const du_uchar* dav_cds_error_code_no_such_container(void);

/**
 * Returns an error code of 'restricted object'.
 * This error code is "711".
 * @return  "711" string.
 */
extern const du_uchar* dav_cds_error_code_restricted_object(void);

/**
 * Returns an error code of 'bad metadata'.
 * This error code is "712".
 * @return  "712" string.
 */
extern const du_uchar* dav_cds_error_code_bad_metadata(void);

/**
 * Returns an error code of 'restricted parent object'.
 * This error code is "713".
 * @return  "713" string.
 */
extern const du_uchar* dav_cds_error_code_restricted_parent_object(void);

/**
 * Returns an error code of 'no such resource'.
 * This error code is "714".
 * @return  "714" string.
 */
extern const du_uchar* dav_cds_error_code_no_such_source_resource(void);

/**
 * Returns an error code of 'source resource access denied'.
 * This error code is "715".
 * @return  "715" string.
 */
extern const du_uchar* dav_cds_error_code_source_resource_access_denied(void);

/**
 * Returns an error code of 'transfer busy'.
 * This error code is "716".
 * @return  "716" string.
 */
extern const du_uchar* dav_cds_error_code_transfer_busy(void);

/**
 * Returns an error code of 'no such file transfer'.
 * This error code is "717".
 * @return  "717" string.
 */
extern const du_uchar* dav_cds_error_code_no_such_file_transfer(void);

/**
 * Returns an error code of 'no such destination resource'.
 * This error code is "718".
 * @return  "718" string.
 */
extern const du_uchar* dav_cds_error_code_no_such_destination_resource(void);

/**
 * Returns an error code of 'destination resource access denied'.
 * This error code is "719".
 * @return  "719" string.
 */
extern const du_uchar* dav_cds_error_code_destination_resource_access_denied(void);

/**
 * Returns an error code of 'cannot process the request'.
 * This error code is "720".
 * @return  "720" string.
 */
extern const du_uchar* dav_cds_error_code_cannot_process_the_request(void);

/**
 * Returns an error code of 'Restricted source parent object'.
 * This error code is "721".
 * @return  "721" string.
 */
extern const du_uchar* dav_cds_error_code_restricted_source_parent_object(void);

/**
 * Returns an error code of 'Incompatible parent class'.
 * This error code is "722".
 * @return  "722" string.
 */
extern const du_uchar* dav_cds_error_code_incompatible_parent_class(void);

/**
 * Returns an error code of 'Illegal destination '.
 * This error code is "723".
 * @return  "723" string.
 */
extern const du_uchar* dav_cds_error_code_illegal_destination(void);

/**
 * Returns an error code of 'Unsupported or invalid CDS View'.
 * This error code is "724".
 * @return  "724" string.
 */
extern const du_uchar* dav_cds_error_code_unsupported_or_invalid_cds_view(void);

/**
 * Returns an error code of 'Invalid Query Request'.
 * This error code is "725".
 * @return  "725" string.
 */
extern const du_uchar* dav_cds_error_code_invalid_query_request(void);

/**
 * Returns an error code of 'Unsupported Query Request instruction(s)'.
 * This error code is "726".
 * @return  "726" string.
 */
extern const du_uchar* dav_cds_error_code_unsupported_query_request_instructions(void);

#ifdef __cplusplus
}
#endif

#endif
