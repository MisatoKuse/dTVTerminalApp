/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dpd_pe.h
 *  @brief The digd_lanhcm interface provides methods for making SOAP request/response messages of
 *   PrintEnhanced actions. This interface provides methods for parsing SOAP request/response
 *   messages of PrintEnhanced actions.
 *  @see  PrintEnhanced:1 Service Template Version 1.01 For UPnP. Version 1.0
 */

#ifndef DPD_PE_H
#define DPD_PE_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of CancelJob.
  * @return the action name of CancelJob.
  */
extern const du_uchar* dpd_pe_action_name_cancel_job(void);

/**
 * Appends a SOAP request message of CancelJob action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_id value of JobId argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_cancel_job(du_uchar_array* xml, du_uint32 v, du_int32 job_id);

/**
 * Parses a SOAP request message of CancelJob action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_cancel_job(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* job_id);

/**
 * Appends a SOAP response message of CancelJob action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_cancel_job_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of CancelJob action, stores each name and
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
extern du_bool dpd_pe_parse_cancel_job_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of CreateJob.
  * @return the action name of CreateJob.
  */
extern const du_uchar* dpd_pe_action_name_create_job(void);

/**
 * Appends a SOAP request message of CreateJob action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_name value of JobName argument.
 * @param[in] job_originating_user_name value of JobOriginatingUserName argument.
 * @param[in] document_format value of DocumentFormat argument.
 * @param[in] copies value of Copies argument.
 * @param[in] sides value of Sides argument.
 * @param[in] number_up value of NumberUp argument.
 * @param[in] orientation_requested value of OrientationRequested argument.
 * @param[in] media_size value of MediaSize argument.
 * @param[in] media_type value of MediaType argument.
 * @param[in] print_quality value of PrintQuality argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_create_job(du_uchar_array* xml, du_uint32 v, const du_uchar* job_name, const du_uchar* job_originating_user_name, const du_uchar* document_format, du_int32 copies, const du_uchar* sides, const du_uchar* number_up, const du_uchar* orientation_requested, const du_uchar* media_size, const du_uchar* media_type, const du_uchar* print_quality);

/**
 * Parses a SOAP request message of CreateJob action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_name pointer to the storage location for JobName argument value.
 * @param[out] job_originating_user_name pointer to the storage location for JobOriginatingUserName argument value.
 * @param[out] document_format pointer to the storage location for DocumentFormat argument value.
 * @param[out] copies pointer to the storage location for Copies argument value.
 * @param[out] sides pointer to the storage location for Sides argument value.
 * @param[out] number_up pointer to the storage location for NumberUp argument value.
 * @param[out] orientation_requested pointer to the storage location for OrientationRequested argument value.
 * @param[out] media_size pointer to the storage location for MediaSize argument value.
 * @param[out] media_type pointer to the storage location for MediaType argument value.
 * @param[out] print_quality pointer to the storage location for PrintQuality argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_create_job(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** job_name, const du_uchar** job_originating_user_name, const du_uchar** document_format, du_int32* copies, const du_uchar** sides, const du_uchar** number_up, const du_uchar** orientation_requested, const du_uchar** media_size, const du_uchar** media_type, const du_uchar** print_quality);

/**
 * Appends a SOAP response message of CreateJob action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_id value of JobId argument.
 * @param[in] data_sink value of DataSink argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_create_job_response(du_uchar_array* xml, du_uint32 v, du_int32 job_id, const du_uchar* data_sink);

/**
 * Parses a SOAP response message of CreateJob action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @param[out] data_sink pointer to the storage location for DataSink argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_create_job_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* job_id, const du_uchar** data_sink);

/**
  * Returns the action name of CreateJobV2.
  * @return the action name of CreateJobV2.
  */
extern const du_uchar* dpd_pe_action_name_create_job_v2(void);

/**
 * Appends a SOAP request message of CreateJobV2 action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_name value of JobName argument.
 * @param[in] job_originating_user_name value of JobOriginatingUserName argument.
 * @param[in] document_format value of DocumentFormat argument.
 * @param[in] copies value of Copies argument.
 * @param[in] sides value of Sides argument.
 * @param[in] number_up value of NumberUp argument.
 * @param[in] orientation_requested value of OrientationRequested argument.
 * @param[in] media_size value of MediaSize argument.
 * @param[in] media_type value of MediaType argument.
 * @param[in] print_quality value of PrintQuality argument.
 * @param[in] critical_attributes_list value of CriticalAttributesList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_create_job_v2(du_uchar_array* xml, du_uint32 v, const du_uchar* job_name, const du_uchar* job_originating_user_name, const du_uchar* document_format, du_int32 copies, const du_uchar* sides, const du_uchar* number_up, const du_uchar* orientation_requested, const du_uchar* media_size, const du_uchar* media_type, const du_uchar* print_quality, const du_uchar* critical_attributes_list);

/**
 * Parses a SOAP request message of CreateJobV2 action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_name pointer to the storage location for JobName argument value.
 * @param[out] job_originating_user_name pointer to the storage location for JobOriginatingUserName argument value.
 * @param[out] document_format pointer to the storage location for DocumentFormat argument value.
 * @param[out] copies pointer to the storage location for Copies argument value.
 * @param[out] sides pointer to the storage location for Sides argument value.
 * @param[out] number_up pointer to the storage location for NumberUp argument value.
 * @param[out] orientation_requested pointer to the storage location for OrientationRequested argument value.
 * @param[out] media_size pointer to the storage location for MediaSize argument value.
 * @param[out] media_type pointer to the storage location for MediaType argument value.
 * @param[out] print_quality pointer to the storage location for PrintQuality argument value.
 * @param[out] critical_attributes_list pointer to the storage location for CriticalAttributesList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_create_job_v2(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** job_name, const du_uchar** job_originating_user_name, const du_uchar** document_format, du_int32* copies, const du_uchar** sides, const du_uchar** number_up, const du_uchar** orientation_requested, const du_uchar** media_size, const du_uchar** media_type, const du_uchar** print_quality, const du_uchar** critical_attributes_list);

/**
 * Appends a SOAP response message of CreateJobV2 action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_id value of JobId argument.
 * @param[in] data_sink value of DataSink argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_create_job_v2_response(du_uchar_array* xml, du_uint32 v, du_int32 job_id, const du_uchar* data_sink);

/**
 * Parses a SOAP response message of CreateJobV2 action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @param[out] data_sink pointer to the storage location for DataSink argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_create_job_v2_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* job_id, const du_uchar** data_sink);

/**
  * Returns the action name of CreateURIJob.
  * @return the action name of CreateURIJob.
  */
extern const du_uchar* dpd_pe_action_name_create_uri_job(void);

/**
 * Appends a SOAP request message of CreateURIJob action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_name value of JobName argument.
 * @param[in] job_originating_user_name value of JobOriginatingUserName argument.
 * @param[in] document_format value of DocumentFormat argument.
 * @param[in] copies value of Copies argument.
 * @param[in] sides value of Sides argument.
 * @param[in] number_up value of NumberUp argument.
 * @param[in] orientation_requested value of OrientationRequested argument.
 * @param[in] media_size value of MediaSize argument.
 * @param[in] media_type value of MediaType argument.
 * @param[in] print_quality value of PrintQuality argument.
 * @param[in] critical_attributes_list value of CriticalAttributesList argument.
 * @param[in] source_uri value of SourceURI argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_create_uri_job(du_uchar_array* xml, du_uint32 v, const du_uchar* job_name, const du_uchar* job_originating_user_name, const du_uchar* document_format, du_int32 copies, const du_uchar* sides, const du_uchar* number_up, const du_uchar* orientation_requested, const du_uchar* media_size, const du_uchar* media_type, const du_uchar* print_quality, const du_uchar* critical_attributes_list, const du_uchar* source_uri);

/**
 * Parses a SOAP request message of CreateURIJob action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_name pointer to the storage location for JobName argument value.
 * @param[out] job_originating_user_name pointer to the storage location for JobOriginatingUserName argument value.
 * @param[out] document_format pointer to the storage location for DocumentFormat argument value.
 * @param[out] copies pointer to the storage location for Copies argument value.
 * @param[out] sides pointer to the storage location for Sides argument value.
 * @param[out] number_up pointer to the storage location for NumberUp argument value.
 * @param[out] orientation_requested pointer to the storage location for OrientationRequested argument value.
 * @param[out] media_size pointer to the storage location for MediaSize argument value.
 * @param[out] media_type pointer to the storage location for MediaType argument value.
 * @param[out] print_quality pointer to the storage location for PrintQuality argument value.
 * @param[out] critical_attributes_list pointer to the storage location for CriticalAttributesList argument value.
 * @param[out] source_uri pointer to the storage location for SourceURI argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_create_uri_job(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** job_name, const du_uchar** job_originating_user_name, const du_uchar** document_format, du_int32* copies, const du_uchar** sides, const du_uchar** number_up, const du_uchar** orientation_requested, const du_uchar** media_size, const du_uchar** media_type, const du_uchar** print_quality, const du_uchar** critical_attributes_list, const du_uchar** source_uri);

/**
 * Appends a SOAP response message of CreateURIJob action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_id value of JobId argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_create_uri_job_response(du_uchar_array* xml, du_uint32 v, du_int32 job_id);

/**
 * Parses a SOAP response message of CreateURIJob action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_create_uri_job_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* job_id);

/**
  * Returns the action name of GetJobAttributes.
  * @return the action name of GetJobAttributes.
  */
extern const du_uchar* dpd_pe_action_name_get_job_attributes(void);

/**
 * Appends a SOAP request message of GetJobAttributes action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_id value of JobId argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_job_attributes(du_uchar_array* xml, du_uint32 v, du_int32 job_id);

/**
 * Parses a SOAP request message of GetJobAttributes action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_job_attributes(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int32* job_id);

/**
 * Appends a SOAP response message of GetJobAttributes action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] job_name value of JobName argument.
 * @param[in] job_originating_user_name value of JobOriginatingUserName argument.
 * @param[in] job_media_sheets_completed value of JobMediaSheetsCompleted argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_job_attributes_response(du_uchar_array* xml, du_uint32 v, const du_uchar* job_name, const du_uchar* job_originating_user_name, du_int32 job_media_sheets_completed);

/**
 * Parses a SOAP response message of GetJobAttributes action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] job_name pointer to the storage location for JobName argument value.
 * @param[out] job_originating_user_name pointer to the storage location for JobOriginatingUserName argument value.
 * @param[out] job_media_sheets_completed pointer to the storage location for JobMediaSheetsCompleted argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_job_attributes_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** job_name, const du_uchar** job_originating_user_name, du_int32* job_media_sheets_completed);

/**
  * Returns the action name of GetMargins.
  * @return the action name of GetMargins.
  */
extern const du_uchar* dpd_pe_action_name_get_margins(void);

/**
 * Appends a SOAP request message of GetMargins action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] media_size value of MediaSize argument.
 * @param[in] media_type value of MediaType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_margins(du_uchar_array* xml, du_uint32 v, const du_uchar* media_size, const du_uchar* media_type);

/**
 * Parses a SOAP request message of GetMargins action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] media_size pointer to the storage location for MediaSize argument value.
 * @param[out] media_type pointer to the storage location for MediaType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_margins(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** media_size, const du_uchar** media_type);

/**
 * Appends a SOAP response message of GetMargins action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] page_margins value of PageMargins argument.
 * @param[in] full_bleed_supported value of FullBleedSupported argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_margins_response(du_uchar_array* xml, du_uint32 v, const du_uchar* page_margins, du_bool full_bleed_supported);

/**
 * Parses a SOAP response message of GetMargins action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] page_margins pointer to the storage location for PageMargins argument value.
 * @param[out] full_bleed_supported pointer to the storage location for FullBleedSupported argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_margins_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** page_margins, du_bool* full_bleed_supported);

/**
  * Returns the action name of GetMediaList.
  * @return the action name of GetMediaList.
  */
extern const du_uchar* dpd_pe_action_name_get_media_list(void);

/**
 * Appends a SOAP request message of GetMediaList action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] media_size value of MediaSize argument.
 * @param[in] media_type value of MediaType argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_media_list(du_uchar_array* xml, du_uint32 v, const du_uchar* media_size, const du_uchar* media_type);

/**
 * Parses a SOAP request message of GetMediaList action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] media_size pointer to the storage location for MediaSize argument value.
 * @param[out] media_type pointer to the storage location for MediaType argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_media_list(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** media_size, const du_uchar** media_type);

/**
 * Appends a SOAP response message of GetMediaList action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] media_list value of MediaList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_media_list_response(du_uchar_array* xml, du_uint32 v, const du_uchar* media_list);

/**
 * Parses a SOAP response message of GetMediaList action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] media_list pointer to the storage location for MediaList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_media_list_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** media_list);

/**
  * Returns the action name of GetPrinterAttributes.
  * @return the action name of GetPrinterAttributes.
  */
extern const du_uchar* dpd_pe_action_name_get_printer_attributes(void);

/**
 * Appends a SOAP request message of GetPrinterAttributes action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_printer_attributes(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPrinterAttributes action, stores each name and
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
extern du_bool dpd_pe_parse_get_printer_attributes(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPrinterAttributes action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] printer_state value of PrinterState argument.
 * @param[in] printer_state_reasons value of PrinterStateReasons argument.
 * @param[in] job_id_list value of JobIdList argument.
 * @param[in] job_id value of JobId argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_printer_attributes_response(du_uchar_array* xml, du_uint32 v, const du_uchar* printer_state, const du_uchar* printer_state_reasons, const du_uchar* job_id_list, du_int32 job_id);

/**
 * Parses a SOAP response message of GetPrinterAttributes action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] printer_state pointer to the storage location for PrinterState argument value.
 * @param[out] printer_state_reasons pointer to the storage location for PrinterStateReasons argument value.
 * @param[out] job_id_list pointer to the storage location for JobIdList argument value.
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_printer_attributes_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** printer_state, const du_uchar** printer_state_reasons, const du_uchar** job_id_list, du_int32* job_id);

/**
  * Returns the action name of GetPrinterAttributesV2.
  * @return the action name of GetPrinterAttributesV2.
  */
extern const du_uchar* dpd_pe_action_name_get_printer_attributes_v2(void);

/**
 * Appends a SOAP request message of GetPrinterAttributesV2 action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_printer_attributes_v2(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP request message of GetPrinterAttributesV2 action, stores each name and
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
extern du_bool dpd_pe_parse_get_printer_attributes_v2(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
 * Appends a SOAP response message of GetPrinterAttributesV2 action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] printer_state value of PrinterState argument.
 * @param[in] printer_state_reasons value of PrinterStateReasons argument.
 * @param[in] job_id_list value of JobIdList argument.
 * @param[in] job_id value of JobId argument.
 * @param[in] internet_connect_state value of InternetConnectState argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dpd_pe_make_get_printer_attributes_v2_response(du_uchar_array* xml, du_uint32 v, const du_uchar* printer_state, const du_uchar* printer_state_reasons, const du_uchar* job_id_list, du_int32 job_id, const du_uchar* internet_connect_state);

/**
 * Parses a SOAP response message of GetPrinterAttributesV2 action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] printer_state pointer to the storage location for PrinterState argument value.
 * @param[out] printer_state_reasons pointer to the storage location for PrinterStateReasons argument value.
 * @param[out] job_id_list pointer to the storage location for JobIdList argument value.
 * @param[out] job_id pointer to the storage location for JobId argument value.
 * @param[out] internet_connect_state pointer to the storage location for InternetConnectState argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dpd_pe_parse_get_printer_attributes_v2_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** printer_state, const du_uchar** printer_state_reasons, const du_uchar** job_id_list, du_int32* job_id, const du_uchar** internet_connect_state);

/**
 * the state variable name of 'A_ARG_TYPE_CriticalAttribList'.
 * @return  "A_ARG_TYPE_CriticalAttribList" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_a_arg_type_critical_attrib_list(void);

/**
 * the state variable name of 'A_ARG_TYPE_MediaList'.
 * @return  "A_ARG_TYPE_MediaList" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_a_arg_type_media_list(void);

/**
 * the state variable name of 'A_ARG_TYPE_PrinterAbortReason'.
 * @return  "A_ARG_TYPE_PrinterAbortReason" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_a_arg_type_printer_abort_reason(void);

/**
 * the state variable name of 'CharRepSupported'.
 * @return  "CharRepSupported" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_char_rep_supported(void);

/**
 * the state variable name of 'ColorSupported'.
 * @return  "ColorSupported" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_color_supported(void);

/**
 * the state variable name of 'ContentCompleteList'.
 * @return  "ContentCompleteList" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_content_complete_list(void);

/**
 * the state variable name of 'Copies'.
 * @return  "Copies" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_copies(void);

/**
 * the state variable name of 'CriticalAttributesSupported'.
 * @return  "CriticalAttributesSupported" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_critical_attributes_supported(void);

/**
 * the state variable name of 'DataSink'.
 * @return  "DataSink" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_data_sink(void);

/**
 * the state variable name of 'DeviceId'.
 * @return  "DeviceId" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_device_id(void);

/**
 * the state variable name of 'DocumentFormat'.
 * @return  "DocumentFormat" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_document_format(void);

/**
 * the state variable name of 'DocumentUTF16Supported'.
 * @return  "DocumentUTF16Supported" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_document_utf16supported(void);

/**
 * the state variable name of 'FullBleedSupported'.
 * @return  "FullBleedSupported" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_full_bleed_supported(void);

/**
 * the state variable name of 'InternetConnectState'.
 * @return  "InternetConnectState" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_internet_connect_state(void);

/**
 * the state variable name of 'JobAbortState'.
 * @return  "JobAbortState" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_abort_state(void);

/**
 * the state variable name of 'JobEndState'.
 * @return  "JobEndState" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_end_state(void);

/**
 * the state variable name of 'JobId'.
 * @return  "JobId" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_id(void);

/**
 * the state variable name of 'JobIdList'.
 * @return  "JobIdList" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_id_list(void);

/**
 * the state variable name of 'JobMediaSheetsCompleted'.
 * @return  "JobMediaSheetsCompleted" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_media_sheets_completed(void);

/**
 * the state variable name of 'JobName'.
 * @return  "JobName" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_name(void);

/**
 * the state variable name of 'JobOriginatingUserName'.
 * @return  "JobOriginatingUserName" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_job_originating_user_name(void);

/**
 * the state variable name of 'MediaSize'.
 * @return  "MediaSize" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_media_size(void);

/**
 * the state variable name of 'MediaType'.
 * @return  "MediaType" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_media_type(void);

/**
 * the state variable name of 'NumberUp'.
 * @return  "NumberUp" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_number_up(void);

/**
 * the state variable name of 'OrientationRequested'.
 * @return  "OrientationRequested" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_orientation_requested(void);

/**
 * the state variable name of 'PageMargins'.
 * @return  "PageMargins" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_page_margins(void);

/**
 * the state variable name of 'PrinterLocation'.
 * @return  "PrinterLocation" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_printer_location(void);

/**
 * the state variable name of 'PrinterName'.
 * @return  "PrinterName" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_printer_name(void);

/**
 * the state variable name of 'PrintQuality'.
 * @return  "PrintQuality" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_print_quality(void);

/**
 * the state variable name of 'PrinterState'.
 * @return  "PrinterState" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_printer_state(void);

/**
 * the state variable name of 'PrinterStateReasons'.
 * @return  "PrinterStateReasons" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_printer_state_reasons(void);

/**
 * the state variable name of 'Sides'.
 * @return  "Sides" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_sides(void);

/**
 * the state variable name of 'SourceURI'.
 * @return  "SourceURI" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_source_uri(void);

/**
 * the state variable name of 'XHTMLImageSupported'.
 * @return  "XHTMLImageSupported" string.
 */
extern const du_uchar* dpd_pe_state_variable_name_xhtml_image_supported(void);

#ifdef __cplusplus
}
#endif

#endif
