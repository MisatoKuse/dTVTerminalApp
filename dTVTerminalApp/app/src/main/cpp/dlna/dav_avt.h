/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dav_avt.h
 *  @brief The dav_avt interface provides methods for making SOAP request/response messages of
 *   AVTransport actions. This interface provides methods for parsing SOAP request/response
 *   messages of AVTransport actions.
 *  @see  AVTransport:2 Service Template Version 1.01 For UPnP. Version 1.0 section 2.4
 */

#ifndef DAV_AVT_H
#define DAV_AVT_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of SetAVTransportURI.
  * @return the action name of SetAVTransportURI.
  */
extern const du_uchar* dav_avt_action_name_set_av_transport_uri(void);

/**
 * Appends a SOAP request message of SetAVTransportURI action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] current_uri value of CurrentURI argument.
 * @param[in] current_uri_meta_data value of CurrentURIMetaData argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_av_transport_uri(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* current_uri, const du_uchar* current_uri_meta_data);

/**
 * Parses a SOAP request message of SetAVTransportURI action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] current_uri pointer to the storage location for CurrentURI argument value.
 * @param[out] current_uri_meta_data pointer to the storage location for CurrentURIMetaData argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_set_av_transport_uri(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** current_uri, const du_uchar** current_uri_meta_data);

/**
 * Appends a SOAP response message of SetAVTransportURI action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_av_transport_uri_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetAVTransportURI action, stores each name and
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
extern du_bool dav_avt_parse_set_av_transport_uri_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetNextAVTransportURI.
  * @return the action name of SetNextAVTransportURI.
  */
extern const du_uchar* dav_avt_action_name_set_next_av_transport_uri(void);

/**
 * Appends a SOAP request message of SetNextAVTransportURI action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] next_uri value of NextURI argument.
 * @param[in] next_uri_meta_data value of NextURIMetaData argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_next_av_transport_uri(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* next_uri, const du_uchar* next_uri_meta_data);

/**
 * Parses a SOAP request message of SetNextAVTransportURI action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] next_uri pointer to the storage location for NextURI argument value.
 * @param[out] next_uri_meta_data pointer to the storage location for NextURIMetaData argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_set_next_av_transport_uri(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** next_uri, const du_uchar** next_uri_meta_data);

/**
 * Appends a SOAP response message of SetNextAVTransportURI action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_next_av_transport_uri_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetNextAVTransportURI action, stores each name and
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
extern du_bool dav_avt_parse_set_next_av_transport_uri_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetMediaInfo.
  * @return the action name of GetMediaInfo.
  */
extern const du_uchar* dav_avt_action_name_get_media_info(void);

/**
 * Appends a SOAP request message of GetMediaInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_media_info(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetMediaInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_media_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetMediaInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] nr_tracks value of NrTracks argument.
 * @param[in] media_duration value of MediaDuration argument.
 * @param[in] current_uri value of CurrentURI argument.
 * @param[in] current_uri_meta_data value of CurrentURIMetaData argument.
 * @param[in] next_uri value of NextURI argument.
 * @param[in] next_uri_meta_data value of NextURIMetaData argument.
 * @param[in] play_medium value of PlayMedium argument.
 * @param[in] record_medium value of RecordMedium argument.
 * @param[in] write_status value of WriteStatus argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_media_info_response(du_uchar_array* xml, du_uint32 v, du_uint32 nr_tracks, const du_uchar* media_duration, const du_uchar* current_uri, const du_uchar* current_uri_meta_data, const du_uchar* next_uri, const du_uchar* next_uri_meta_data, const du_uchar* play_medium, const du_uchar* record_medium, const du_uchar* write_status);

/**
 * Parses a SOAP response message of GetMediaInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] nr_tracks pointer to the storage location for NrTracks argument value.
 * @param[out] media_duration pointer to the storage location for MediaDuration argument value.
 * @param[out] current_uri pointer to the storage location for CurrentURI argument value.
 * @param[out] current_uri_meta_data pointer to the storage location for CurrentURIMetaData argument value.
 * @param[out] next_uri pointer to the storage location for NextURI argument value.
 * @param[out] next_uri_meta_data pointer to the storage location for NextURIMetaData argument value.
 * @param[out] play_medium pointer to the storage location for PlayMedium argument value.
 * @param[out] record_medium pointer to the storage location for RecordMedium argument value.
 * @param[out] write_status pointer to the storage location for WriteStatus argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_media_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* nr_tracks, const du_uchar** media_duration, const du_uchar** current_uri, const du_uchar** current_uri_meta_data, const du_uchar** next_uri, const du_uchar** next_uri_meta_data, const du_uchar** play_medium, const du_uchar** record_medium, const du_uchar** write_status);

/**
  * Returns the action name of GetMediaInfo_Ext.
  * @return the action name of GetMediaInfo_Ext.
  */
extern const du_uchar* dav_avt_action_name_get_media_info_ext(void);

/**
 * Appends a SOAP request message of GetMediaInfo_Ext action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_media_info_ext(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetMediaInfo_Ext action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_media_info_ext(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetMediaInfo_Ext action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_type value of CurrentType argument.
 * @param[in] nr_tracks value of NrTracks argument.
 * @param[in] media_duration value of MediaDuration argument.
 * @param[in] current_uri value of CurrentURI argument.
 * @param[in] current_uri_meta_data value of CurrentURIMetaData argument.
 * @param[in] next_uri value of NextURI argument.
 * @param[in] next_uri_meta_data value of NextURIMetaData argument.
 * @param[in] play_medium value of PlayMedium argument.
 * @param[in] record_medium value of RecordMedium argument.
 * @param[in] write_status value of WriteStatus argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_media_info_ext_response(du_uchar_array* xml, du_uint32 v, const du_uchar* current_type, du_uint32 nr_tracks, const du_uchar* media_duration, const du_uchar* current_uri, const du_uchar* current_uri_meta_data, const du_uchar* next_uri, const du_uchar* next_uri_meta_data, const du_uchar* play_medium, const du_uchar* record_medium, const du_uchar* write_status);

/**
 * Parses a SOAP response message of GetMediaInfo_Ext action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_type pointer to the storage location for CurrentType argument value.
 * @param[out] nr_tracks pointer to the storage location for NrTracks argument value.
 * @param[out] media_duration pointer to the storage location for MediaDuration argument value.
 * @param[out] current_uri pointer to the storage location for CurrentURI argument value.
 * @param[out] current_uri_meta_data pointer to the storage location for CurrentURIMetaData argument value.
 * @param[out] next_uri pointer to the storage location for NextURI argument value.
 * @param[out] next_uri_meta_data pointer to the storage location for NextURIMetaData argument value.
 * @param[out] play_medium pointer to the storage location for PlayMedium argument value.
 * @param[out] record_medium pointer to the storage location for RecordMedium argument value.
 * @param[out] write_status pointer to the storage location for WriteStatus argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_media_info_ext_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** current_type, du_uint32* nr_tracks, const du_uchar** media_duration, const du_uchar** current_uri, const du_uchar** current_uri_meta_data, const du_uchar** next_uri, const du_uchar** next_uri_meta_data, const du_uchar** play_medium, const du_uchar** record_medium, const du_uchar** write_status);

/**
  * Returns the action name of GetTransportInfo.
  * @return the action name of GetTransportInfo.
  */
extern const du_uchar* dav_avt_action_name_get_transport_info(void);

/**
 * Appends a SOAP request message of GetTransportInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_transport_info(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetTransportInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_transport_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetTransportInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_transport_state value of CurrentTransportState argument.
 * @param[in] current_transport_status value of CurrentTransportStatus argument.
 * @param[in] current_speed value of CurrentSpeed argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_transport_info_response(du_uchar_array* xml, du_uint32 v, const du_uchar* current_transport_state, const du_uchar* current_transport_status, const du_uchar* current_speed);

/**
 * Parses a SOAP response message of GetTransportInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_transport_state pointer to the storage location for CurrentTransportState argument value.
 * @param[out] current_transport_status pointer to the storage location for CurrentTransportStatus argument value.
 * @param[out] current_speed pointer to the storage location for CurrentSpeed argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_transport_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** current_transport_state, const du_uchar** current_transport_status, const du_uchar** current_speed);

/**
  * Returns the action name of GetPositionInfo.
  * @return the action name of GetPositionInfo.
  */
extern const du_uchar* dav_avt_action_name_get_position_info(void);

/**
 * Appends a SOAP request message of GetPositionInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_position_info(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetPositionInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_position_info(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetPositionInfo action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] track value of Track argument.
 * @param[in] track_duration value of TrackDuration argument.
 * @param[in] track_meta_data value of TrackMetaData argument.
 * @param[in] track_uri value of TrackURI argument.
 * @param[in] rel_time value of RelTime argument.
 * @param[in] abs_time value of AbsTime argument.
 * @param[in] rel_count value of RelCount argument.
 * @param[in] abs_count value of AbsCount argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_position_info_response(du_uchar_array* xml, du_uint32 v, du_uint32 track, const du_uchar* track_duration, const du_uchar* track_meta_data, const du_uchar* track_uri, const du_uchar* rel_time, const du_uchar* abs_time, du_int32 rel_count, du_int32 abs_count);

/**
 * Parses a SOAP response message of GetPositionInfo action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] track pointer to the storage location for Track argument value.
 * @param[out] track_duration pointer to the storage location for TrackDuration argument value.
 * @param[out] track_meta_data pointer to the storage location for TrackMetaData argument value.
 * @param[out] track_uri pointer to the storage location for TrackURI argument value.
 * @param[out] rel_time pointer to the storage location for RelTime argument value.
 * @param[out] abs_time pointer to the storage location for AbsTime argument value.
 * @param[out] rel_count pointer to the storage location for RelCount argument value.
 * @param[out] abs_count pointer to the storage location for AbsCount argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_position_info_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* track, const du_uchar** track_duration, const du_uchar** track_meta_data, const du_uchar** track_uri, const du_uchar** rel_time, const du_uchar** abs_time, du_int32* rel_count, du_int32* abs_count);

/**
  * Returns the action name of GetDeviceCapabilities.
  * @return the action name of GetDeviceCapabilities.
  */
extern const du_uchar* dav_avt_action_name_get_device_capabilities(void);

/**
 * Appends a SOAP request message of GetDeviceCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_device_capabilities(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetDeviceCapabilities action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_device_capabilities(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetDeviceCapabilities action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] play_media value of PlayMedia argument.
 * @param[in] rec_media value of RecMedia argument.
 * @param[in] rec_quality_modes value of RecQualityModes argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_device_capabilities_response(du_uchar_array* xml, du_uint32 v, const du_uchar* play_media, const du_uchar* rec_media, const du_uchar* rec_quality_modes);

/**
 * Parses a SOAP response message of GetDeviceCapabilities action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] play_media pointer to the storage location for PlayMedia argument value.
 * @param[out] rec_media pointer to the storage location for RecMedia argument value.
 * @param[out] rec_quality_modes pointer to the storage location for RecQualityModes argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_device_capabilities_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** play_media, const du_uchar** rec_media, const du_uchar** rec_quality_modes);

/**
  * Returns the action name of GetTransportSettings.
  * @return the action name of GetTransportSettings.
  */
extern const du_uchar* dav_avt_action_name_get_transport_settings(void);

/**
 * Appends a SOAP request message of GetTransportSettings action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_transport_settings(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetTransportSettings action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_transport_settings(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetTransportSettings action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] play_mode value of PlayMode argument.
 * @param[in] rec_quality_mode value of RecQualityMode argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_transport_settings_response(du_uchar_array* xml, du_uint32 v, const du_uchar* play_mode, const du_uchar* rec_quality_mode);

/**
 * Parses a SOAP response message of GetTransportSettings action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] play_mode pointer to the storage location for PlayMode argument value.
 * @param[out] rec_quality_mode pointer to the storage location for RecQualityMode argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_transport_settings_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** play_mode, const du_uchar** rec_quality_mode);

/**
  * Returns the action name of Stop.
  * @return the action name of Stop.
  */
extern const du_uchar* dav_avt_action_name_stop(void);

/**
 * Appends a SOAP request message of Stop action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_stop(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of Stop action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_stop(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of Stop action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_stop_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Stop action, stores each name and
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
extern du_bool dav_avt_parse_stop_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of Play.
  * @return the action name of Play.
  */
extern const du_uchar* dav_avt_action_name_play(void);

/**
 * Appends a SOAP request message of Play action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] speed value of Speed argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_play(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* speed);

/**
 * Parses a SOAP request message of Play action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] speed pointer to the storage location for Speed argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_play(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** speed);

/**
 * Appends a SOAP response message of Play action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_play_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Play action, stores each name and
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
extern du_bool dav_avt_parse_play_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of Pause.
  * @return the action name of Pause.
  */
extern const du_uchar* dav_avt_action_name_pause(void);

/**
 * Appends a SOAP request message of Pause action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_pause(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of Pause action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_pause(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of Pause action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_pause_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Pause action, stores each name and
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
extern du_bool dav_avt_parse_pause_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of Record.
  * @return the action name of Record.
  */
extern const du_uchar* dav_avt_action_name_record(void);

/**
 * Appends a SOAP request message of Record action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_record(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of Record action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_record(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of Record action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_record_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Record action, stores each name and
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
extern du_bool dav_avt_parse_record_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of Seek.
  * @return the action name of Seek.
  */
extern const du_uchar* dav_avt_action_name_seek(void);

/**
 * Appends a SOAP request message of Seek action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] unit value of Unit argument.
 * @param[in] target value of Target argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_seek(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* unit, const du_uchar* target);

/**
 * Parses a SOAP request message of Seek action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] unit pointer to the storage location for Unit argument value.
 * @param[out] target pointer to the storage location for Target argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_seek(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** unit, const du_uchar** target);

/**
 * Appends a SOAP response message of Seek action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_seek_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Seek action, stores each name and
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
extern du_bool dav_avt_parse_seek_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of Next.
  * @return the action name of Next.
  */
extern const du_uchar* dav_avt_action_name_next(void);

/**
 * Appends a SOAP request message of Next action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_next(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of Next action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_next(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of Next action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_next_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Next action, stores each name and
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
extern du_bool dav_avt_parse_next_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of Previous.
  * @return the action name of Previous.
  */
extern const du_uchar* dav_avt_action_name_previous(void);

/**
 * Appends a SOAP request message of Previous action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_previous(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of Previous action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_previous(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of Previous action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_previous_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of Previous action, stores each name and
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
extern du_bool dav_avt_parse_previous_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetPlayMode.
  * @return the action name of SetPlayMode.
  */
extern const du_uchar* dav_avt_action_name_set_play_mode(void);

/**
 * Appends a SOAP request message of SetPlayMode action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] new_play_mode value of NewPlayMode argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_play_mode(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* new_play_mode);

/**
 * Parses a SOAP request message of SetPlayMode action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] new_play_mode pointer to the storage location for NewPlayMode argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_set_play_mode(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** new_play_mode);

/**
 * Appends a SOAP response message of SetPlayMode action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_play_mode_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetPlayMode action, stores each name and
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
extern du_bool dav_avt_parse_set_play_mode_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetRecordQualityMode.
  * @return the action name of SetRecordQualityMode.
  */
extern const du_uchar* dav_avt_action_name_set_record_quality_mode(void);

/**
 * Appends a SOAP request message of SetRecordQualityMode action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] new_record_quality_mode value of NewRecordQualityMode argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_record_quality_mode(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* new_record_quality_mode);

/**
 * Parses a SOAP request message of SetRecordQualityMode action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] new_record_quality_mode pointer to the storage location for NewRecordQualityMode argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_set_record_quality_mode(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** new_record_quality_mode);

/**
 * Appends a SOAP response message of SetRecordQualityMode action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_record_quality_mode_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetRecordQualityMode action, stores each name and
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
extern du_bool dav_avt_parse_set_record_quality_mode_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetCurrentTransportActions.
  * @return the action name of GetCurrentTransportActions.
  */
extern const du_uchar* dav_avt_action_name_get_current_transport_actions(void);

/**
 * Appends a SOAP request message of GetCurrentTransportActions action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_current_transport_actions(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetCurrentTransportActions action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_current_transport_actions(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetCurrentTransportActions action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] actions value of Actions argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_current_transport_actions_response(du_uchar_array* xml, du_uint32 v, const du_uchar* actions);

/**
 * Parses a SOAP response message of GetCurrentTransportActions action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] actions pointer to the storage location for Actions argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_current_transport_actions_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** actions);

/**
  * Returns the action name of GetDRMState.
  * @return the action name of GetDRMState.
  */
extern const du_uchar* dav_avt_action_name_get_drm_state(void);

/**
 * Appends a SOAP request message of GetDRMState action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_drm_state(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetDRMState action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_drm_state(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetDRMState action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] curent_drm_state value of CurentDRMState argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_drm_state_response(du_uchar_array* xml, du_uint32 v, const du_uchar* curent_drm_state);

/**
 * Parses a SOAP response message of GetDRMState action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] curent_drm_state pointer to the storage location for CurentDRMState argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_drm_state_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** curent_drm_state);

/**
  * Returns the action name of GetStateVariables.
  * @return the action name of GetStateVariables.
  */
extern const du_uchar* dav_avt_action_name_get_state_variables(void);

/**
 * Appends a SOAP request message of GetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] state_variable_list value of StateVariableList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_state_variables(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* state_variable_list);

/**
 * Parses a SOAP request message of GetStateVariables action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] state_variable_list pointer to the storage location for StateVariableList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_state_variables(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** state_variable_list);

/**
 * Appends a SOAP response message of GetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] state_variable_value_pairs value of StateVariableValuePairs argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_get_state_variables_response(du_uchar_array* xml, du_uint32 v, const du_uchar* state_variable_value_pairs);

/**
 * Parses a SOAP response message of GetStateVariables action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] state_variable_value_pairs pointer to the storage location for StateVariableValuePairs argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_get_state_variables_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** state_variable_value_pairs);

/**
  * Returns the action name of SetStateVariables.
  * @return the action name of SetStateVariables.
  */
extern const du_uchar* dav_avt_action_name_set_state_variables(void);

/**
 * Appends a SOAP request message of SetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] av_transport_udn value of AVTransportUDN argument.
 * @param[in] service_type value of ServiceType argument.
 * @param[in] service_id value of ServiceId argument.
 * @param[in] state_variable_value_pairs value of StateVariableValuePairs argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_state_variables(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* av_transport_udn, const du_uchar* service_type, const du_uchar* service_id, const du_uchar* state_variable_value_pairs);

/**
 * Parses a SOAP request message of SetStateVariables action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] av_transport_udn pointer to the storage location for AVTransportUDN argument value.
 * @param[out] service_type pointer to the storage location for ServiceType argument value.
 * @param[out] service_id pointer to the storage location for ServiceId argument value.
 * @param[out] state_variable_value_pairs pointer to the storage location for StateVariableValuePairs argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_set_state_variables(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** av_transport_udn, const du_uchar** service_type, const du_uchar** service_id, const du_uchar** state_variable_value_pairs);

/**
 * Appends a SOAP response message of SetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] state_variable_list value of StateVariableList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_avt_make_set_state_variables_response(du_uchar_array* xml, du_uint32 v, const du_uchar* state_variable_list);

/**
 * Parses a SOAP response message of SetStateVariables action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] state_variable_list pointer to the storage location for StateVariableList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_avt_parse_set_state_variables_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** state_variable_list);

/**
 * the state variable name of 'TransportState'.
 * @return  "TransportState" string.
 */
extern const du_uchar* dav_avt_state_variable_name_transport_state(void);

/**
 * the state variable name of 'TransportStatus'.
 * @return  "TransportStatus" string.
 */
extern const du_uchar* dav_avt_state_variable_name_transport_status(void);

/**
 * the state variable name of 'CurrentMediaCategory'.
 * @return  "CurrentMediaCategory" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_media_category(void);

/**
 * the state variable name of 'PlaybackStorageMedium'.
 * @return  "PlaybackStorageMedium" string.
 */
extern const du_uchar* dav_avt_state_variable_name_playback_storage_medium(void);

/**
 * the state variable name of 'RecordStorageMedium'.
 * @return  "RecordStorageMedium" string.
 */
extern const du_uchar* dav_avt_state_variable_name_record_storage_medium(void);

/**
 * the state variable name of 'PossiblePlaybackStorageMedia'.
 * @return  "PossiblePlaybackStorageMedia" string.
 */
extern const du_uchar* dav_avt_state_variable_name_possible_playback_storage_media(void);

/**
 * the state variable name of 'PossibleRecordStorageMedia'.
 * @return  "PossibleRecordStorageMedia" string.
 */
extern const du_uchar* dav_avt_state_variable_name_possible_record_storage_media(void);

/**
 * the state variable name of 'CurrentPlayMode'.
 * @return  "CurrentPlayMode" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_play_mode(void);

/**
 * the state variable name of 'TransportPlaySpeed'.
 * @return  "TransportPlaySpeed" string.
 */
extern const du_uchar* dav_avt_state_variable_name_transport_play_speed(void);

/**
 * the state variable name of 'RecordMediumWriteStatus'.
 * @return  "RecordMediumWriteStatus" string.
 */
extern const du_uchar* dav_avt_state_variable_name_record_medium_write_status(void);

/**
 * the state variable name of 'CurrentRecordQualityMode'.
 * @return  "CurrentRecordQualityMode" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_record_quality_mode(void);

/**
 * the state variable name of 'PossibleRecordQualityModes'.
 * @return  "PossibleRecordQualityModes" string.
 */
extern const du_uchar* dav_avt_state_variable_name_possible_record_quality_modes(void);

/**
 * the state variable name of 'NumberOfTracks'.
 * @return  "NumberOfTracks" string.
 */
extern const du_uchar* dav_avt_state_variable_name_number_of_tracks(void);

/**
 * the state variable name of 'CurrentTrack'.
 * @return  "CurrentTrack" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_track(void);

/**
 * the state variable name of 'CurrentTrackDuration'.
 * @return  "CurrentTrackDuration" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_track_duration(void);

/**
 * the state variable name of 'CurrentMediaDuration'.
 * @return  "CurrentMediaDuration" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_media_duration(void);

/**
 * the state variable name of 'CurrentTrackMetaData'.
 * @return  "CurrentTrackMetaData" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_track_meta_data(void);

/**
 * the state variable name of 'CurrentTrackURI'.
 * @return  "CurrentTrackURI" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_track_uri(void);

/**
 * the state variable name of 'AVTransportURI'.
 * @return  "AVTransportURI" string.
 */
extern const du_uchar* dav_avt_state_variable_name_av_transport_uri(void);

/**
 * the state variable name of 'AVTransportURIMetaData'.
 * @return  "AVTransportURIMetaData" string.
 */
extern const du_uchar* dav_avt_state_variable_name_av_transport_uri_meta_data(void);

/**
 * the state variable name of 'NextAVTransportURI'.
 * @return  "NextAVTransportURI" string.
 */
extern const du_uchar* dav_avt_state_variable_name_next_av_transport_uri(void);

/**
 * the state variable name of 'NextAVTransportURIMetaData'.
 * @return  "NextAVTransportURIMetaData" string.
 */
extern const du_uchar* dav_avt_state_variable_name_next_av_transport_uri_meta_data(void);

/**
 * the state variable name of 'RelativeTimePosition'.
 * @return  "RelativeTimePosition" string.
 */
extern const du_uchar* dav_avt_state_variable_name_relative_time_position(void);

/**
 * the state variable name of 'AbsoluteTimePosition'.
 * @return  "AbsoluteTimePosition" string.
 */
extern const du_uchar* dav_avt_state_variable_name_absolute_time_position(void);

/**
 * the state variable name of 'RelativeCounterPosition'.
 * @return  "RelativeCounterPosition" string.
 */
extern const du_uchar* dav_avt_state_variable_name_relative_counter_position(void);

/**
 * the state variable name of 'AbsoluteCounterPosition'.
 * @return  "AbsoluteCounterPosition" string.
 */
extern const du_uchar* dav_avt_state_variable_name_absolute_counter_position(void);

/**
 * the state variable name of 'CurrentTransportActions'.
 * @return  "CurrentTransportActions" string.
 */
extern const du_uchar* dav_avt_state_variable_name_current_transport_actions(void);

/**
 * the state variable name of 'LastChange'.
 * @return  "LastChange" string.
 */
extern const du_uchar* dav_avt_state_variable_name_last_change(void);

/**
 * the state variable name of 'DRMState'.
 * @return  "DRMState" string.
 */
extern const du_uchar* dav_avt_state_variable_name_drm_state(void);

/**
 * the state variable name of 'A_ARG_TYPE_SeekMode'.
 * @return  "A_ARG_TYPE_SeekMode" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_seek_mode(void);

/**
 * the state variable name of 'A_ARG_TYPE_SeekTarget'.
 * @return  "A_ARG_TYPE_SeekTarget" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_seek_target(void);

/**
 * the state variable name of 'A_ARG_TYPE_InstanceID'.
 * @return  "A_ARG_TYPE_InstanceID" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_instance_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_DeviceUDN'.
 * @return  "A_ARG_TYPE_DeviceUDN" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_device_udn(void);

/**
 * the state variable name of 'A_ARG_TYPE_ServiceType'.
 * @return  "A_ARG_TYPE_ServiceType" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_service_type(void);

/**
 * the state variable name of 'A_ARG_TYPE_ServiceID'.
 * @return  "A_ARG_TYPE_ServiceID" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_service_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_StateVariableValuePairs'.
 * @return  "A_ARG_TYPE_StateVariableValuePairs" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_state_variable_value_pairs(void);

/**
 * the state variable name of 'A_ARG_TYPE_StateVariableList'.
 * @return  "A_ARG_TYPE_StateVariableList" string.
 */
extern const du_uchar* dav_avt_state_variable_name_a_arg_type_state_variable_list(void);

/**
 * Returns an error code of 'transition not available'.
 * This error code is "701".
 * @return  "701" string.
 */
extern const du_uchar* dav_avt_error_code_transition_not_available(void);

/**
 * Returns an error code of 'no contents'.
 * This error code is "702".
 * @return  "702" string.
 */
extern const du_uchar* dav_avt_error_code_no_contents(void);

/**
 * Returns an error code of 'read error'.
 * This error code is "703".
 * @return  "703" string.
 */
extern const du_uchar* dav_avt_error_code_read_error(void);

/**
 * Returns an error code of 'format not supported for playback'.
 * This error code is "704".
 * @return  "704" string.
 */
extern const du_uchar* dav_avt_error_code_format_not_supported_for_playback(void);

/**
 * Returns an error code of 'transport is locked'.
 * This error code is "705".
 * @return  "705" string.
 */
extern const du_uchar* dav_avt_error_code_transport_is_locked(void);

/**
 * Returns an error code of 'write error'.
 * This error code is "706".
 * @return  "706" string.
 */
extern const du_uchar* dav_avt_error_code_write_error(void);

/**
 * Returns an error code of 'media is protected or not writable'.
 * This error code is "707".
 * @return  "707" string.
 */
extern const du_uchar* dav_avt_error_code_media_is_protected_or_not_writable(void);

/**
 * Returns an error code of 'format not supported for recording'.
 * This error code is "708".
 * @return  "708" string.
 */
extern const du_uchar* dav_avt_error_code_format_not_supported_for_recoding(void);

/**
 * Returns an error code of 'media is full'.
 * This error code is "709".
 * @return  "709" string.
 */
extern const du_uchar* dav_avt_error_code_media_is_full(void);

/**
 * Returns an error code of 'seek mode not supported'.
 * This error code is "710".
 * @return  "710" string.
 */
extern const du_uchar* dav_avt_error_code_seek_mode_not_supported(void);

/**
 * Returns an error code of 'illegal seek target'.
 * This error code is "711".
 * @return  "711" string.
 */
extern const du_uchar* dav_avt_error_code_illegal_seek_target(void);

/**
 * Returns an error code of 'play mode not supported'.
 * This error code is "712".
 * @return  "712" string.
 */
extern const du_uchar* dav_avt_error_code_play_mode_not_supported(void);

/**
 * Returns an error code of 'record quality not supported'.
 * This error code is "713".
 * @return  "713" string.
 */
extern const du_uchar* dav_avt_error_code_record_quality_not_supported(void);

/**
 * Returns an error code of 'illegal MIME-type'.
 * This error code is "714".
 * @return  "714" string.
 */
extern const du_uchar* dav_avt_error_code_illegal_mime_type(void);

/**
 * Returns an error code of 'content BUSY'.
 * This error code is "715".
 * @return  "715" string.
 */
extern const du_uchar* dav_avt_error_code_content_busy(void);

/**
 * Returns an error code of 'resource not found'.
 * This error code is "716".
 * @return  "716" string.
 */
extern const du_uchar* dav_avt_error_code_resource_not_found(void);

/**
 * Returns an error code of 'play speed not supported'.
 * This error code is "717".
 * @return  "717" string.
 */
extern const du_uchar* dav_avt_error_code_play_speed_not_supported(void);

/**
 * Returns an error code of 'invalid InstanceID'.
 * This error code is "718".
 * @return  "718" string.
 */
extern const du_uchar* dav_avt_error_code_invalid_instance_id(void);

/**
 * Returns an error code of 'DRM error'.
 * This error code is "719".
 * @return  "719" string.
 */
extern const du_uchar* dav_avt_error_code_drm_error(void);

/**
 * Returns an error code of 'Expired content'.
 * This error code is "720".
 * @return  "720" string.
 */
extern const du_uchar* dav_avt_error_code_expired_content(void);

/**
 * Returns an error code of 'Non-allowed use'.
 * This error code is "721".
 * @return  "721" string.
 */
extern const du_uchar* dav_avt_error_code_nonallowed_use(void);

/**
 * Returns an error code of 'Can’t determine allowed uses'.
 * This error code is "722".
 * @return  "722" string.
 */
extern const du_uchar* dav_avt_error_code_cant_determine_allowed_uses(void);

/**
 * Returns an error code of 'Exhausted allowed use'.
 * This error code is "723".
 * @return  "723" string.
 */
extern const du_uchar* dav_avt_error_code_exhausted_allowed_use(void);

/**
 * Returns an error code of 'Device authentication failure'.
 * This error code is "724".
 * @return  "724" string.
 */
extern const du_uchar* dav_avt_error_code_device_authentication_failure(void);

/**
 * Returns an error code of 'Device revocation'.
 * This error code is "725".
 * @return  "725" string.
 */
extern const du_uchar* dav_avt_error_code_device_revocation(void);

/**
 * Returns an error code of 'Invalid StateVariableList'.
 * This error code is "726".
 * @return  "726" string.
 */
extern const du_uchar* dav_avt_error_code_invalid_statevariablelist(void);

/**
 * Returns an error code of 'Ill-formed CSV List'.
 * This error code is "727".
 * @return  "727" string.
 */
extern const du_uchar* dav_avt_error_code_ill_formed_csv_list(void);

/**
 * Returns an error code of 'Invalid State Variable Value'.
 * This error code is "728".
 * @return  "728" string.
 */
extern const du_uchar* dav_avt_error_code_invalid_state_variable_value(void);

/**
 * Returns an error code of 'Invalid Service Type'.
 * This error code is "729".
 * @return  "729" string.
 */
extern const du_uchar* dav_avt_error_code_invalid_service_type(void);

/**
 * Returns an error code of 'Invalid Service Id'.
 * This error code is "730".
 * @return  "730" string.
 */
extern const du_uchar* dav_avt_error_code_invalid_service_id(void);

#ifdef __cplusplus
}
#endif

#endif
