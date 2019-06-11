/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl.h
 *  @brief The dav_didl interface provides methods for manipulating the name of
 *  elements and attributes for UPnP AV ( such as normalizing the name, getting a
 *  specific name of elements and attributes for UPnP AV).
 */

#ifndef DAV_DIDL_H
#define DAV_DIDL_H

#include <du_type.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DAV_DIDL_VALUE_MAX_SIZE 1024
#define DAV_DIDL_VALUE_LIMITED_SIZE 256

#define DAV_DIDL_INTEGER_SIZE 11
#define DAV_DIDL_UNSIGNED_INTEGER_SIZE 10
#define DAV_DIDL_LONG_SIZE 20
#define DAV_DIDL_UNSIGNED_LONG_SIZE 21
#define DAV_DIDL_BOOLEAN_SIZE 5

/**
 * The type of Normalized DIDL-Lite element name.
 * When using this type, you can compare DIDL-Lite element names
 * by the pointer address, not the string value.
 */
typedef du_uchar dav_didl_name;

/**
  * Type of element and attribute.
  * @li @c DAV_DIDL_TYPE_UNKNOWN : type is unknown.
  * @li @c DAV_DIDL_TYPE_INTEGER : type is integer.
  * @li @c DAV_DIDL_TYPE_UNSIGNED_INTEGER : type is unsigned integer.
  * @li @c DAV_DIDL_TYPE_LONG : type is long.
  * @li @c DAV_DIDL_TYPE_UNSIGNED_LONG : type is unsigned long.
  * @li @c DAV_DIDL_TYPE_BOOLEAN : type is boolean.
  * @li @c DAV_DIDL_TYPE_STRING : type is string.
  * @li @c DAV_DIDL_TYPE_URI : type is URI.
  */

typedef enum {
    DAV_DIDL_TYPE_UNKNOWN,
    DAV_DIDL_TYPE_INTEGER,
    DAV_DIDL_TYPE_UNSIGNED_INTEGER,
    DAV_DIDL_TYPE_LONG,
    DAV_DIDL_TYPE_UNSIGNED_LONG,
    DAV_DIDL_TYPE_BOOLEAN,
    DAV_DIDL_TYPE_STRING,
    DAV_DIDL_TYPE_URI,
    DAV_DIDL_TYPE_FLOAT,
    DAV_DIDL_TYPE_XML,
} dav_didl_type;

/**
 * Gets the type of specified attribute.
 * @param[in] attr_name attribute name.
 * @return type of the <em>attr_name</em> attribute.
 */
extern dav_didl_type dav_didl_get_attribute_type(const dav_didl_name* attr_name);

/**
 * Gets the type of specified element.
 * @param[in] elem_name element name.
 * @return type of the <em>elem_name</em> element.
 */
extern dav_didl_type dav_didl_get_element_type(const dav_didl_name* elem_name);

/**
 * Normalizes <em>attr_name</em> attribute name string. Checks <em>attr_name</em> to ensure that
 * <em>attr_name</em> is a consistent name of an attribute.
 * @param[in] attr_name the name of attribute to nomalize.
 * @return the pointer to the nomalized string of <em>attr_name</em> if <em>attr_name</em>
 *  is a consistent attribute name, otherwise null.
 */
extern const dav_didl_name* dav_didl_normalize_attribute(const du_uchar* attr_name);

/**
 * Normalizes <em>elem_name</em> element name string. Checks <em>elem_name</em> to ensure that
 * <em>elem_name</em> is a consistent name of element.
 * @param[in] elem_name the name of element to nomalize.
 * @return the pointer to the nomalized string of <em>elem_name</em> if <em>elem_name</em>
 *  is a consistent element name, otherwise null.
 */
extern const dav_didl_name* dav_didl_normalize_element(const du_uchar* elem_name);

/**
 * Normalizes <em>class_name</em> class name string. Checks <em>class_name</em> to ensure that
 * <em>class_name</em> is a consistent name of class.
 * @param[in] class_name the name of class to nomalize.
 * @return the pointer to the nomalized string of <em>class_name</em> if <em>class_name</em>
 *  is a consistent element name, otherwise null.
 */
extern const dav_didl_name* dav_didl_normalize_class(const du_uchar* class_name);

/**
 * Adds <em>attr_name</em> attribute name string and its type to the dav_didl.
 * @param[in] attr_name the attribute name string to be added.
 * @param[in] type type of the <em>attr_name</em> attribute.
 * @return  true if the function succeeds. false if the function fails.
 */
extern du_bool dav_didl_add_attribute(const du_uchar* attr_name, dav_didl_type type);

/**
 * Adds <em>elem_name</em> element name string and its type to the dav_didl.
 * @param[in] elem_name the element name string to be added.
 * @param[in] type type of the <em>elem_name</em> element.
 * @return  true if the function succeeds. false if the function fails.
 */
extern du_bool dav_didl_add_element(const du_uchar* elem_name, dav_didl_type type);

/**
 * Adds <em>class_name</em> class name string to the dav_didl.
 * @param[in] class_name the class name string to be added.
 * @return  true if the function succeeds. false if the function fails.
 */
extern du_bool dav_didl_add_class(const du_uchar* class_name);

/**
 * Adds prefix and uri of namespace to the dav_didl.
 * For convenience, the following pairs are registered by default.
 * <table>
 * <tr><th>prefix</th><th>namespace</th></tr>
 * <tr><td></td><td>urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/</td></tr>
 * <tr><td>upnp</td><td>urn:schemas-upnp-org:metadata-1-0/upnp/</td></tr>
 * <tr><td>dc</td><td>http://purl.org/dc/elements/1.1/</td></tr>
 * <tr><td>dlna</td><td>urn:schemas-dlna-org:metadata-1-0/</td></tr>
 * <tr><td>arib</td><td>urn:schemas-arib-or-jp:elements-1-0/</td></tr>
 * <tr><td>dtcp</td><td>urn:schemas-dtcp-com:metadata-1-0/</td></tr>
 * <tr><td>dixim</td><td>urn:schemas-digion-com:metadata-1-0/dixim/DIDL-Lite/</td></tr>
 * <tr><td>microsoft</td><td>urn:schemas-microsoft-com:WMPNSS-1-0/</td></tr>
 * <tr><td>dlpa</td><td>urn:schemas-dlpa-jp:metadata-1-0/</td></tr>
 * <tr><td>jlabs</td><td>urn:schemas-jlabs-or-jp:metadata-1-0</td></tr>
 * </table>
 * @param[in] prefix prefix string of namespace to be added.
 * @param[in] uri uri string of namespace to be added.
 * @return  true if the function succeeds. false if the function fails.
 */
extern du_bool dav_didl_add_namespace(const du_uchar* prefix, const du_uchar* uri);

/**
 * Gets a namespace URI which corresponds to the specified prefix.
 * @param[in] prefix prefix string of namespace.
 * @return namespace URI which corresponds to the specified prefix.
 *  NULL if not found.
 */
extern const du_uchar* dav_didl_get_namespace(const du_uchar* prefix);

/**
 * Gets all namespace information stored in the dav_didl.
 * @return  prefix and uri string array of namespaces stored in the dav_didl.
 *    Return value is du_param data which give name-value pair data.
 *    Here, name data is prefix string and value data is uri string.
 */
extern const du_str_array* dav_didl_get_namespace_list(void);

/**
 * Converts a character expression into a du_bool form.
 * @param[in] boolean_value_str the string specified the character expression.
 * @param[out] boolean_value true if <em>boolean_value</em> is "1" or "true" or "yes".
 *  false if <em>boolean_value</em> is "0" or "false" or "no".
 * @return true if <em>boolean_value</em> is "1" or "0" or "true" or "false" or "yes" or "no". Otherwise false.
 */
extern du_bool dav_didl_convert_boolean_value(const du_uchar* boolean_value_str, du_bool* boolean_value);

/**
 * Tests if <em>sub_class</em> is derived from <em>super_class</em>.
 * @param[in] sub_class a string.
 * @param[in] super_class a superclass ( prefix ) string.
 * @return true if <em>sub_class</em> equals <em>super_class</em> or
 *    the character sequence represented by <em>super_class</em> followed by "."
 *    character is a prefix of the character sequence represented by <em>sub_class</em>.
 *    Otherwise false.
 */
extern du_bool dav_didl_derived_from(const du_uchar* sub_class, const du_uchar* super_class);

/**
 * Returns the wildcard character.
 * This character is "*".
 * @return the wildcard character.
 */
extern const dav_didl_name* dav_didl_wildcard(void);

/**
 * Returns the allip character.
 * This character is "ALLIP".
 * @return the allip character.
 */
extern const dav_didl_name* dav_didl_allip(void);

/**
 * Returns the name of the available attribute.
 * This name is "allowedUse".
 * @return the name of the available attribute.
 */
extern const dav_didl_name* dav_didl_attribute_allowed_use(void);
/**
 * Returns the name of the available attribute.
 * This name is "available".
 * @return the name of the available attribute.
 */
extern const dav_didl_name* dav_didl_attribute_available(void);

/**
 * Returns the name of the attribute of the bitrate in bytes/second of the resource.
 * This name is "bitrate".
 * @return the name of the bitrate attribute.
 */
extern const dav_didl_name* dav_didl_attribute_bitrate(void);

/**
 * Returns the name of the attribute of the bits per sample of the resource.
 * This name is "bitsPerSample".
 * @return the name of the bits per sample attribute.
 */
extern const dav_didl_name* dav_didl_attribute_bits_per_sample(void);

/**
 * Returns the name of the child count attribute.
 * This name is "childCount".
 * @return the name of the child count attribute.
 */
extern const dav_didl_name* dav_didl_attribute_child_count(void);

/**
 * Returns the name of the attribute of the color depth in bits of the resource (image or video).
 * This name is "colorDepth".
 * @return the name of the color depth attribute.
 */
extern const dav_didl_name* dav_didl_attribute_color_depth(void);

/**
 * Returns the name of the contentInfoURI attribute.
 * This name is contentInfoURI".
 * @return the name of the contentInfoURI attribute.
 */
extern const dav_didl_name* dav_didl_attribute_content_info_uri(void);

/**
 * Returns the name of the contentInfoURI attribute.
 * This name is "contentInfoURI".
 * @return the name of the contentInfoURI attribute.
 */
extern const dav_didl_name* dav_didl_attribute_content_info_uri(void);

/**
 * Returns the name of the currency attribute.
 * This name is "currency".
 * @return the name of the currency attribute.
 */
extern const dav_didl_name* dav_didl_attribute_currency(void);

/**
 * Returns the name of the daylightSaving attribute.
 * This name is "daylightSaving".
 * @return the name of the daylightSaving attribute.
 */
extern const dav_didl_name* dav_didl_attribute_daylight_saving(void);

/**
 * Returns the name of the distriNetworkName attribute.
 * This name is "distriNetworkName".
 * @return the name of the distriNetworkName attribute.
 */
extern const dav_didl_name* dav_didl_attribute_distri_network_name(void);

/**
 * Returns the name of the distriNetworkID attribute.
 * This name is "distriNetworkID".
 * @return the name of the distriNetworkID attribute.
 */
extern const dav_didl_name* dav_didl_attribute_distri_network_id(void);

/**
 * Returns the name of the attribute of the duration of the playback of
 * the resource, at normal speed.
 * This name is "duration".
 * @return the name of the duration attribute.
 */
extern const dav_didl_name* dav_didl_attribute_duration(void);

/**
 * Returns the name of the available attribute.
 * This name is "extended".
 * @return the name of the available attribute.
 */
extern const dav_didl_name* dav_didl_attribute_extended(void);
/**
 * Returns the name of the id attribute.
 * This name is "id".
 * @return the name of the id attribute.
 */
extern const dav_didl_name* dav_didl_attribute_id(void);

/**
 * Returns the name of the attribute of the optional uri locator for
 *  resource updates.
 * This name is "importUri".
 * @return the name of the import uri attribute.
 */
extern const dav_didl_name* dav_didl_attribute_import_uri(void);

/**
 * Returns the name of the upnp includeDerived attribute.
 * This name is "upnp:includeDerived".
 * @return the name of the upnp includeDerived attribute.
 */
extern const dav_didl_name* dav_didl_attribute_include_derived(void);

/**
 * Returns the name of the mimeType attribute.
 * This name is "mimeType".
 * @return the name of the mimeType attribute.
 */
extern const dav_didl_name* dav_didl_attribute_mime_type(void);

/**
 * Returns the name of the upnp name attribute.
 * This name is "upnp:name".
 * @return the name of the upnp name attribute.
 */
extern const dav_didl_name* dav_didl_attribute_name(void);

/**
 * Returns the name of the name space attribute.
 * This name is "nameSpace".
 * @return the name of the name space attribute.
 */
extern const dav_didl_name* dav_didl_attribute_name_space(void);

/**
 * Returns the name of the neverPlayable attribute.
 * This name is "neverPlayable".
 * @return the name of the neverPlayable attribute.
 */
extern const dav_didl_name* dav_didl_attribute_never_playable(void);

/**
 * Returns the name of the attribute of the number of audio channels of the resource.
 * This name is "nrAudioChannels".
 * @return the name of the nr audio channels attribute.
 */
extern const dav_didl_name* dav_didl_attribute_nr_audio_channels(void);

/**
 * Returns the name of the parent ID attribute.
 * This name is "parentID".
 * @return the name of the parent ID attribute.
 */
extern const dav_didl_name* dav_didl_attribute_parent_id(void);

/**
 * Returns the name of the attribute of the protection type of the resource.
 * This name is "protection".
 * @return the name of the protection attribute.
 */
extern const dav_didl_name* dav_didl_attribute_protection(void);

/**
 * Returns the name of the attribute that identifies the
 * streaming or transport protocol for transmitting the resource.
 * This name is "protocolInfo".
 * @return the name of the protocol info attribute.
 */
extern const dav_didl_name* dav_didl_attribute_protocol_info(void);

/**
 * Returns the name of the quality indicator attribute.
 * This name is "qualityIndicator".
 * @return the name of the quality indicator attribute.
 */
extern const dav_didl_name* dav_didl_attribute_quality_indicator(void);

/**
 * Returns the name of the rcsInstanceType attribute.
 * This name is "rcsInstanceType".
 * @return the name of the rcsInstanceType attribute.
 */
extern const dav_didl_name* dav_didl_attribute_rcs_instance_type(void);

/**
 * Returns the name of the available attribute.
 * This name is "recordQuality".
 * @return the name of the available attribute.
 */
extern const dav_didl_name* dav_didl_attribute_record_quality(void);
/**
 * Returns the name of the reference ID attribute.
 * This name is "refID".
 * @return the name of the reference ID attribute.
 */
extern const dav_didl_name* dav_didl_attribute_ref_id(void);

/**
 * Returns the name of the reference ID attribute.
 * This name is "remainingTime".
 * @return the name of the reference ID attribute.
 */
extern const dav_didl_name* dav_didl_attribute_remaining_time(void);

/**
 * Returns the name of the attribute of the resolution of the resource (image or video).
 * This name is "resolution".
 * @return the name of the resolution attribute.
 */
extern const dav_didl_name* dav_didl_attribute_resolution(void);

/**
 * Returns the name of the restricted attribute.
 * This name is "restricted".
 * @return the name of the restricted attribute.
 */
extern const dav_didl_name* dav_didl_attribute_restricted(void);

/**
 * Returns the name of the available attribute.
 * This name is "rightsInfoURI".
 * @return the name of the available attribute.
 */
extern const dav_didl_name* dav_didl_attribute_rights_info_uri(void);
/**
 * Returns the name of the upnp role attribute.
 * This name is "upnp:role".
 * @return the name of the upnp role attribute.
 */
extern const dav_didl_name* dav_didl_attribute_role(void);

/**
 * Returns the name of the attribute of the sample frequency of the resource in Hz.
 * This name is "sampleFrequency".
 * @return the name of the sample frequency attribute.
 */
extern const dav_didl_name* dav_didl_attribute_sample_frequency(void);

/**
 * Returns the name of the searchable attribute.
 * This name is "searchable".
 * @return the name of the searchable attribute.
 */
extern const dav_didl_name* dav_didl_attribute_searchable(void);

/**
 * Returns the name of the serviceID attribute.
 * This name is "serviceID".
 * @return the name of the serviceID attribute.
 */
extern const dav_didl_name* dav_didl_attribute_service_id(void);

/**
 * Returns the name of the serviceName attribute.
 * This name is "serviceName".
 * @return the name of the serviceName attribute.
 */
extern const dav_didl_name* dav_didl_attribute_service_name(void);

/**
 * Returns the name of the serviceType attribute.
 * This name is "serviceType".
 * @return the name of the serviceType attribute.
 */
extern const dav_didl_name* dav_didl_attribute_service_type(void);

/**
 * Returns the name of the size attribute.
 * This name is "size".
 * @return the name of the size attribute.
 */
extern const dav_didl_name* dav_didl_attribute_size(void);

/**
 * Returns the name of the sync attribute.
 * This name is "sync".
 * @return the name of the sync attribute.
 */
extern const dav_didl_name* dav_didl_attribute_sync(void);

/**
 * Returns the name of the tspec attribute.
 * This name is "tspec".
 * @return the name of the tspec attribute.
 */
extern const dav_didl_name* dav_didl_attribute_tspec(void);

/**
 * Returns the name of the type attribute.
 * This name is "type".
 * @return the name of the type attribute.
 */
extern const dav_didl_name* dav_didl_attribute_type(void);

/**
 * Returns the name of the updateCount attribute.
 * This name is "updateCount".
 * @return the name of the updateCount attribute.
 */
extern const dav_didl_name* dav_didl_attribute_update_count(void);

/**
 * Returns the name of the usage attribute.
 * This name is "usage".
 * @return the name of the usage attribute.
 */
extern const dav_didl_name* dav_didl_attribute_usage(void);
/**
 * Returns the name of the usageInfo attribute.
 * This name is "usageInfo".
 * @return the name of the usageInfo attribute.
 */
extern const dav_didl_name* dav_didl_attribute_usage_info(void);

/**
 * Returns the name of the validityStart attribute.
 * This name is "validityStart".
 * @return the name of the validityStart attribute.
 */
extern const dav_didl_name* dav_didl_attribute_validity_start(void);

/**
 * Returns the name of the validityEnd attribute.
 * This name is "validityEnd".
 * @return the name of the validityEnd attribute.
 */
extern const dav_didl_name* dav_didl_attribute_validity_end(void);

/**
 * Returns the name of the xmlFlag attribute.
 * This name is "xmlFlag".
 * @return the name of the xmlFlag attribute.
 */
extern const dav_didl_name* dav_didl_attribute_xml_flag(void);

/**
 * Returns the name of the arib resolution attribute.
 * This name is "arib:resolution".
 * @return the name of the arib resolution attribute.
 */
extern const dav_didl_name* dav_didl_attribute_arib_resolution(void);

/**
 * Returns the name of the dixim file attribute.
 * This name is "dixim:file".
 * @return the name of the dixim file attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_file(void);

/**
 * Returns the name of the dixim info attribute.
 * This name is "dixim:file".
 * @return the name of the dixim info attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_info(void);

/**
 * Returns the name of the dixim timestamp attribute.
 * This name is "dixim:timestamp".
 * @return the name of the dixim timestamp attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_timestamp(void);

/**
 * Returns the name of the dixim timestamp2 attribute.
 * This name is "dixim:timestamp2".
 * @return the name of the dixim timestamp2 attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_timestamp2(void);

/**
 * Returns the name of the dixim transporter attribute.
 * This name is "dixim:transporter".
 * @return the name of the dixim transporter attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_transporter(void);

/**
 * Returns the name of the dixim populator attribute.
 * This name is "dixim:populator".
 * @return the name of the dixim populator attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_populator(void);

/**
 * Returns the name of the dixim no need to escape attribute.
 * This name is "dixim:noNeedToEscape".
 * @return the name of the dixim no need to escape attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_no_need_to_escape(void);

/**
 * Returns the name of the dixim playlist attribute.
 * This name is "dixim:playlist".
 * @return the name of the dixim playlist attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_playlist(void);

/**
 * Returns the name of the dixim persist across crawls attribute.
 * This name is "dixim:persistAcrossCrawls".
 * @return the name of the dixim persist across crawls attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_persist_across_crawls(void);

/**
 * Returns the name of the dixim persistence attribute.
 * This name is "dixim:persistence".
 * @return the name of the dixim persistence attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_persistence(void);

/**
 * Returns the name of the dixim scan method attribute.
 * This name is "dixim:scanMethod".
 * @return the name of the dixim scan method attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_scan_method(void);

/**
 * Returns the name of the dixim frame rate attribute.
 * This name is "dixim:frameRate".
 * @return the name of the dixim frame rate attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_frame_rate(void);

/**
 * Returns the name of the dixim sort order attribute.
 * This name is "dixim:sortOrder".
 * @return the name of the dixim sort order attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_sort_order(void);

/**
 * Returns the name of the dixim das order attribute.
 * This name is "dixim:das".
 * @return the name of the dixim das order attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das(void);

/**
 * Returns the name of the dixim das rule id attribute.
 * This name is "dixim:dasRuleID".
 * @return the name of the dixim das rule id attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das_rule_id(void);

/**
 * Returns the name of the dixim das available resolutions attribute.
 * This name is "dixim:dasAvailableResolutions".
 * @return the name of the dixim das available resolutions attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das_available_resolutions(void);

/**
 * Returns the name of the dixim das minimum bitrate attribute.
 * This name is "dixim:dasMinimumBitrate".
 * @return the name of the dixim das minimum bitrate attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das_minimum_bitrate(void);

/**
 * Returns the name of the dixim das maximum bitrate attribute.
 * This name is "dixim:dasMaximumBitrate".
 * @return the name of the dixim das maximum bitrate attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das_maximum_bitrate(void);

/**
 * Returns the name of the dixim das default audio bitrate attribute.
 * This name is "dixim:dasDefaultAudioBitrate".
 * @return the name of the dixim das default audio bitrate attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das_default_audio_bitrate(void);

/**
 * Returns the name of the dixim das default video bitrate attribute.
 * This name is "dixim:dasDefaultVideoBitrate".
 * @return the name of the dixim das default video bitrate attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_das_default_video_bitrate(void);

/**
 * Returns the name of the dixim original profile attribute.
 * This name is "dixim:originalProfile".
 * @return the name of the dixim original profile attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_original_profile(void);

/**
 * Returns the name of the dixim name attribute.
 * This name is "dixim:name".
 * @return the name of the dixim name attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_name(void);

/**
 * Returns the name of the dixim audio stream type attribute.
 * This name is "dixim:audioStreamType".
 * @return the name of the dixim audio stream type attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_audio_stream_type(void);

/**
 * Returns the name of the dixim original sample frequency attribute.
 * This name is "dixim:originalSampleFrequency".
 * @return the name of the dixim original sample frequency attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_original_sample_frequency(void);

/**
 * Returns the name of the dixim merge condition attribute.
 * This name is "dixim:mergeCondition".
 * @return the name of the dixim merge condition attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dixim_merge_condition(void);

/**
 * Returns the name of the dlna cleartext size attribute.
 * This name is "dlna:cleartextSize".
 * @return the name of the dlna cleartext size attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_cleartext_size(void);

/**
 * Returns the name of the dlna estimated size attribute.
 * This name is "dlna:estimatedSize".
 * @return the name of the dlna estimated size attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_estimated_size(void);

/**
 * Returns the name of the dlna dlna managed attribute.
 * This name is "dlna:dlnaManaged".
 * @return the name of the dlna dlna managed attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_dlna_managed(void);

/**
 * Returns the name of the attribute to present the URI for the IFO file.
 * This name is "dlna:ifoFileURI".
 * @return the name of the dlna ifo file URI attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_ifo_file_uri(void);

/**
 * Returns the name of the attribute of the uri locator for
 * the IFO file uploads.
 * This name is "dlna:importIfoFileURI".
 * @return the name of the dlna import ifo file URI attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_import_ifo_file_uri(void);

/**
 * Returns the name of the dlna profileID attribute.
 * This name is "dlna:profileID".
 * @return the name of the dlna profileID attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_profile_id(void);

/**
 * Returns the name of the dlna resume upload attribute.
 * This name is "dlna:resumeUpload".
 * @return the name of the dlna resume upload attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_resume_upload(void);

/**
 * Returns the name of the dlna uploaded size attribute.
 * This name is "dlna:uploadedSize".
 * @return the name of the dlna uploaded size attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlna_uploaded_size(void);

/**
 * Returns the name of the dtcp upload info attribute.
 * This name is "dtcp:uploadInfo".
 * @return the name of the dtcp upload info attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dtcp_upload_info(void);

/**
 * Returns the name of the dlpa scan method attribute.
 * This name is "dlpa:scanMethod".
 * @return the name of the dlpa scan method attribute.
 */
extern const dav_didl_name* dav_didl_attribute_dlpa_scan_method(void);

/**
 * Returns the name of the jlabs audio attribute.
 * This name is "jlabs:audio".
 * @return the name of the jlabs audio attribute.
 */
extern const dav_didl_name* dav_didl_attribute_jlabs_audio(void);

/**
 * Returns the name of the jlabs max BW attribute.
 * This name is "jlabs:maxBW".
 * @return the name of the jlabs max BW attribute.
 */
extern const dav_didl_name* dav_didl_attribute_jlabs_max_bw(void);

/**
 * Returns the name of the container element.
 * This name is "container".
 * @return the name of the container element.
 */
extern const dav_didl_name* dav_didl_element_container(void);

/**
 * Returns the name of the 'desc' element.
 * This name is "desc".
 * @return the name of the 'desc' element.
 */
extern const dav_didl_name* dav_didl_element_desc(void);

/**
 * Returns the name of the 'DIDL-Lite' element.
 * This name is "DIDL-Lite".
 * @return the name of the 'DIDL-Lite' element.
 */
extern const dav_didl_name* dav_didl_element_didl_lite(void);

/**
 * Returns the name of the 'item' element.
 * This name is "item".
 * @return the name of the 'item' element.
 */
extern const dav_didl_name* dav_didl_element_item(void);

/**
 * Returns the name of the 'res' element.
 * This name is "res".
 * @return the name of the 'res' element.
 */
extern const dav_didl_name* dav_didl_element_res(void);

/**
 * Returns the name of the arib 'audioComponentType' element.
 * This name is "arib:audioComponentType".
 * @return the name of the arib 'audioComponentType' element.
 */
extern const dav_didl_name* dav_didl_element_arib_audio_component_type(void);

/**
 * Returns the name of the arib 'caProgramInfo' element.
 * This name is "arib:caProgramInfo".
 * @return the name of the arib 'caProgramInfo' element.
 */
extern const dav_didl_name* dav_didl_element_arib_ca_program_info(void);

/**
 * Returns the name of the arib 'captionInfo' element.
 * This name is "arib:captionInfo".
 * @return the name of the arib 'captionInfo' element.
 */
extern const dav_didl_name* dav_didl_element_arib_caption_info(void);

/**
 * Returns the name of the arib 'copyControlInfo' element.
 * This name is "arib:copyControlInfo".
 * @return the name of the arib 'copyControlInfo' element.
 */
extern const dav_didl_name* dav_didl_element_arib_copy_control_info(void);

/**
 * Returns the name of the arib 'dataProgramInfo' element.
 * This name is "arib:dataProgramInfo".
 * @return the name of the arib 'dataProgramInfo' element.
 */
extern const dav_didl_name* dav_didl_element_arib_data_program_info(void);

/**
 * Returns the name of the arib 'longDescription' element.
 * This name is "arib:longDescription".
 * @return the name of the  arib 'longDescription' element.
 */
extern const dav_didl_name* dav_didl_element_arib_long_description(void);

/**
 * Returns the name of the arib 'multiESInfo' element.
 * This name is "arib:multiESInfo".
 * @return the name of the arib 'multiESInfo' element.
 */
extern const dav_didl_name* dav_didl_element_arib_multi_es_info(void);

/**
 * Returns the name of the arib 'multiViewInfo' element.
 * This name is "arib:multiViewInfo".
 * @return the name of the arib 'multiViewInfo' element.
 */
extern const dav_didl_name* dav_didl_element_arib_multi_view_info(void);

/**
 * Returns the name of the arib 'objectType' element.
 * This name is "arib:objectType".
 * @return the name of the arib 'objectType' element.
 */
extern const dav_didl_name* dav_didl_element_arib_object_type(void);

/**
 * Returns the name of the arib 'videoComponentType' element.
 * This name is "arib:videoComponentType".
 * @return the name of the arib 'videoComponentType' element.
 */
extern const dav_didl_name* dav_didl_element_arib_video_component_type(void);


/**
 * Returns the name of the Dublin Core 'contributor' element.
 * This name is "dc:contributor".
 * @return the name of the 'contributor' element.
 */
extern const dav_didl_name* dav_didl_element_dc_contributor(void);

/**
 * Returns the name of the Dublin Core 'creator' element.
 * This name is "dc:creator".
 * @return the name of the 'creator' element.
 */
extern const dav_didl_name* dav_didl_element_dc_creator(void);

/**
 * Returns the name of the Dublin Core 'date' element.
 * This name is "dc:date".
 * @return the name of the Dublin Core 'date' element.
 */
extern const dav_didl_name* dav_didl_element_dc_date(void);

/**
 * Returns the name of the Dublin Core 'description' element.
 * This name is "dc:description".
 * @return the name of the Dublin Core 'description' element.
 */
extern const dav_didl_name* dav_didl_element_dc_description(void);

/**
 * Returns the name of the Dublin Core 'language' element.
 * This name is "dc:language".
 * @return the name of the Dublin Core 'language' element.
 */
extern const dav_didl_name* dav_didl_element_dc_language(void);

/**
 * Returns the name of the Dublin Core 'publisher' element.
 * This name is "dc:publisher".
 * @return the name of the Dublin Core 'publisher' element.
 */
extern const dav_didl_name* dav_didl_element_dc_publisher(void);

/**
 * Returns the name of the Dublin Core 'relation' element.
 * This name is "dc:relation".
 * @return the name of the Dublin Core 'relation' element.
 */
extern const dav_didl_name* dav_didl_element_dc_relation(void);

/**
 * Returns the name of the Dublin Core 'rights' element.
 * This name is "dc:rights".
 * @return the name of the Dublin Core 'rights' element.
 */
extern const dav_didl_name* dav_didl_element_dc_rights(void);

/**
 * Returns the name of the Dublin Core 'title' element.
 * This name is "dc:title".
 * @return the name of the Dublin Core 'title' element.
 */
extern const dav_didl_name* dav_didl_element_dc_title(void);

/**
 * Returns the name of the DiXiM 'upperTransferableTag' element.
 * This name is "dixim:upperTransferableTag".
 * @return the name of the DiXiM 'upperTransferableTag' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_upper_transferable_tag(void);

/**
 * Returns the name of the dixim CFID attribute.
 * This name is "dixim:CFID".
 * @return the name of the dixim CFID attribute.
 */
extern const dav_didl_name* dav_didl_element_dixim_cfid(void);

/**
 * Returns the name of the dixim last play time element.
 * This name is "dixim:lastPlayTime".
 * @return the name of the dixim last play time element.
 */
extern const dav_didl_name* dav_didl_element_dixim_last_play_time(void);

/**
 * Returns the name of the dixim object create time element.
 * This name is "dixim:objectCreateTime".
 * @return the name of the dixim object create time element.
 */
extern const dav_didl_name* dav_didl_element_dixim_object_create_time(void);

/**
 * Returns the name of the dixim play count element.
 * This name is "dixim:playCount".
 * @return the name of the dixim play count element.
 */
extern const dav_didl_name* dav_didl_element_dixim_play_count(void);

/**
 * Returns the name of the DiXiM 'copyCount' element.
 * This name is "dixim:copyCount".
 * @return the name of the DiXiM 'copyCount' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_copy_count(void);

/**
 * Returns the name of the DiXiM 'info' element.
 * This name is "dixim:copyCount".
 * @return the name of the DiXiM 'info' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_info(void);

/**
 * Returns the name of the DiXiM 'ddd' element.
 * This name is "dixim:ddd".
 * @return the name of the DiXiM 'ddd' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_ddd(void);

/**
 * Returns the name of the DiXiM 'albumArtist' element.
 * This name is "dixim:albumArtist".
 * @return the name of the DiXiM 'albumArtist' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_album_artist(void);

/**
 * Returns the name of the DiXiM 'audioCodec' element.
 * This name is "dixim:audioCodec".
 * @return the name of the DiXiM 'audioCodec' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_audio_codec(void);

/**
 * Returns the name of the DiXiM 'releaseDate' element.
 * This name is "dixim:releaseDate".
 * @return the name of the DiXiM 'releaseDate' element.
 */
extern const dav_didl_name* dav_didl_element_dixim_release_date(void);

/**
 * Returns the name of the DLNA 'containerType' element.
 * This name is "dlna:containerType".
 * @return the name of the DLNA 'containerType' element.
 */
extern const dav_didl_name* dav_didl_element_dlna_container_type(void);

/**
 * Returns the name of the DLNA 'takeOut' element.
 * This name is "dlna:takeOut".
 * @return the name of the DLNA 'takeOut' element.
 */
extern const dav_didl_name* dav_didl_element_dlna_take_out(void);

/**
 * Returns the name of the Microsoft 'artistAlbumArtist' element.
 * This name is "microsoft:artistAlbumArtist".
 * @return the name of the Microsoft 'artistAlbumArtist' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_artist_album_artist(void);

/**
 * Returns the name of the Microsoft 'artistConductor' element.
 * This name is "microsoft:artistConductor".
 * @return the name of the Microsoft 'artistConductor' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_artist_conductor(void);

/**
 * Returns the name of the Microsoft 'artistPerformer' element.
 * This name is "microsoft:artistPerformer".
 * @return the name of the Microsoft 'artistPerformer' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_artist_performer(void);

/**
 * Returns the name of the Microsoft 'authorComposer' element.
 * This name is "microsoft:authorComposer".
 * @return the name of the Microsoft 'authorComposer' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_author_composer(void);

/**
 * Returns the name of the Microsoft 'authorOriginalLyricist' element.
 * This name is "microsoft:authorOriginalLyricist".
 * @return the name of the Microsoft 'authorOriginalLyricist' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_author_original_lyricist(void);

/**
 * Returns the name of the Microsoft 'authorWriter' element.
 * This name is "microsoft:authorWriter".
 * @return the name of the Microsoft 'authorWriter' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_author_writer(void);

/**
 * Returns the name of the Microsoft 'userRatingInStars' element.
 * This name is "microsoft:userRatingInStars".
 * @return the name of the Microsoft 'userRatingInStars' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_user_rating_in_stars(void);

/**
 * Returns the name of the Microsoft 'userRating' element.
 * This name is "microsoft:userRating".
 * @return the name of the Microsoft 'userRating' element.
 */
extern const dav_didl_name* dav_didl_element_microsoft_user_rating(void);

/**
 * Returns the name of the UPnP 'actor' element.
 * This name is "upnp:actor".
 * @return the name of the UPnP 'actor' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_actor(void);

/**
 * Returns the name of the UPnP 'album' element.
 * This name is "upnp:album".
 * @return the name of the UPnP 'album element.
 */
extern const dav_didl_name* dav_didl_element_upnp_album(void);

/**
 * Returns the name of the UPnP 'albumArtURI' element.
 * This name is "upnp:albumArtURI".
 * @return the name of the UPnP 'albumArtURI element.
 */
extern const dav_didl_name* dav_didl_element_upnp_album_art_uri(void);

/**
 * Returns the name of the UPnP 'artist' element.
 * This name is "upnp:artist".
 * @return the name of the UPnP 'artist' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_artist(void);

/**
 * Returns the name of the UPnP 'artistDiscographyURI' element.
 * This name is "upnp:artistDiscographyURI".
 * @return the name of the UPnP 'artistDiscographyURI' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_artist_discography_uri(void);

/**
 * Returns the name of the UPnP 'author' element.
 * This name is "upnp:author".
 * @return the name of the UPnP 'author' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_author(void);

/**
 * Returns the name of the UPnP 'bookmarkID' element.
 * This name is "upnp:bookmarkID".
 * @return the name of the UPnP 'bookmarkID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_bookmark_id(void);

/**
 * Returns the name of the UPnP 'bookmarkedObjectID' element.
 * This name is "upnp:bookmarkedObjectID".
 * @return the name of the UPnP 'bookmarkedObjectID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_bookmarked_object_id(void);

/**
 * Returns the name of the UPnP 'callSign' element.
 * This name is "upnp:callSign".
 * @return the name of the UPnP 'callSign' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_call_sign(void);

/**
 * Returns the name of the UPnP 'channelGroupName' element.
 * This name is "upnp:channelGroupName".
 * @return the name of the UPnP 'channelGroupName' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_channel_group_name(void);

/**
 * Returns the name of the UPnP 'channelName' element.
 * This name is "upnp:channelName".
 * @return the name of the UPnP 'channelName' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_channel_name(void);

/**
 * Returns the name of the UPnP 'channelNr' element.
 * This name is "upnp:channelNr".
 * @return the name of the UPnP 'channelNr' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_channel_nr(void);

/**
 * Returns the name of the UPnP 'channelID' element.
 * This name is "upnp:channelID".
 * @return the name of the UPnP 'channelID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_channel_id(void);

/**
 * Returns the name of the UPnP 'class' element.
 * This name is "upnp:class".
 * @return the name of the UPnP 'class' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_class(void);

/**
 * Returns the name of the UPnP 'containerUpdateID' element.
 * This name is "upnp:containerUpdateID".
 * @return the name of the UPnP 'containerUpdateID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_container_update_id(void);

/**
 * Returns the name of the UPnP 'createClass' element.
 * This name is "upnp:createClass".
 * @return the name of the UPnP 'createClass' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_create_class(void);

/**
 * Returns the name of the UPnP 'dateTimeRange' element.
 * This name is "upnp:dateTimeRange".
 * @return the name of the UPnP 'dateTimeRange' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_date_time_range(void);

/**
 * Returns the name of the UPnP 'deviceUDN' element.
 * This name is "upnp:deviceUDN".
 * @return the name of the UPnP 'deviceUDN' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_device_udn(void);

/**
 * Returns the name of the UPnP 'director' element.
 * This name is "upnp:director".
 * @return the name of the UPnP 'director' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_director(void);

/**
 * Returns the name of the UPnP 'DVDRegionCode' element.
 * This name is "upnp:DVDRegionCode".
 * @return the name of the UPnP 'DVDRegionCode' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_dvd_region_code(void);

/**
 * Returns the name of the UPnP 'epgProviderName' element.
 * This name is "upnp:epgProviderName".
 * @return the name of the UPnP 'epgProviderName' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_epg_provider_name(void);

/**
 * Returns the name of the UPnP 'episodeCount' element.
 * This name is "upnp:episodeCount".
 * @return the name of the UPnP 'episodeCount' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_episode_count(void);

/**
 * Returns the name of the UPnP 'episodeNumber' element.
 * This name is "upnp:episodeNumber".
 * @return the name of the UPnP 'episodeNumber' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_episode_number(void);

/**
 * Returns the name of the UPnP 'episodeType' element.
 * This name is "upnp:episodeType".
 * @return the name of the UPnP 'episodeType' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_episode_type(void);

/**
 * Returns the name of the UPnP 'fmBody' element.
 * This name is "upnp:fmBody".
 * @return the name of the UPnP 'fmBody' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_body(void);

/**
 * Returns the name of the UPnP 'fmClass' element.
 * This name is "upnp:fmClass".
 * @return the name of the UPnP 'fmClass' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_class(void);

/**
 * Returns the name of the UPnP 'fmEmbeddedXML' element.
 * This name is "upnp:fmEmbeddedXML".
 * @return the name of the UPnP 'fmEmbeddedXML' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_embedded_xml(void);

/**
 * Returns the name of the UPnP 'fmEmbeddedString' element.
 * This name is "upnp:fmEmbeddedString".
 * @return the name of the UPnP 'fmEmbeddedString' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_embedded_string(void);

/**
 * Returns the name of the UPnP 'fmId' element.
 * This name is "upnp:fmId".
 * @return the name of the UPnP 'fmId' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_id(void);

/**
 * Returns the name of the UPnP 'fmProvider' element.
 * This name is "upnp:fmProvider".
 * @return the name of the UPnP 'fmProvider' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_provider(void);

/**
 * Returns the name of the UPnP 'fmURI' element.
 * This name is "upnp:fmURI".
 * @return the name of the UPnP 'fmURI' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_fm_uri(void);

/**
 * Returns the name of the UPnP 'foreignMetadata' element.
 * This name is "upnp:foreignMetadata".
 * @return the name of the UPnP 'foreignMetadata' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_foreign_metadata(void);

/**
 * Returns the name of the UPnP 'genre' element.
 * This name is "upnp:genre".
 * @return the name of the UPnP 'genre element.
 */
extern const dav_didl_name* dav_didl_element_upnp_genre(void);

/**
 * Returns the name of the UPnP 'icon' element.
 * This name is "upnp:icon".
 * @return the name of the UPnP 'icon' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_icon(void);

/**
 * Returns the name of the UPnP 'icon' element.
 * This name is "upnp:lastPlaybackPosition".
 * @return the name of the UPnP 'icon' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_last_playback_position(void);

/**
 * Returns the name of the UPnP 'icon' element.
 * This name is "upnp:lastPlaybackTime".
 * @return the name of the UPnP 'icon' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_last_playback_time(void);

/**
 * Returns the name of the UPnP 'longDescription' element.
 * This name is "upnp:longDescription".
 * @return the name of the UPnP 'longDescription' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_long_description(void);

/**
 * Returns the name of the UPnP 'lyricsURI' element.
 * This name is "upnp:lyricsURI".
 * @return the name of the UPnP 'lyricsURI element.
 */
extern const dav_didl_name* dav_didl_element_upnp_lyrics_uri(void);

/**
 * Returns the name of the UPnP 'networkAffiliation' element.
 * This name is "upnp:networkAffiliation".
 * @return the name of the UPnP 'networkAffiliation' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_network_affiliation(void);

/**
 * Returns the name of the UPnP 'objectUpdateID' element.
 * This name is "upnp:objectUpdateID".
 * @return the name of the UPnP 'objectUpdateID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_object_update_id(void);

/**
 * Returns the name of the UPnP 'originalTrackNumber' element.
 * This name is "upnp:originalTrackNumber".
 * @return the name of the UPnP 'originalTrackNumber' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_original_track_number(void);

/**
 * Returns the name of the UPnP 'payPerView' element.
 * This name is "upnp:payPerView".
 * @return the name of the UPnP 'payPerView' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_pay_per_view(void);

/**
 * Returns the name of the UPnP 'playbackCount' element.
 * This name is "upnp:playbackCount".
 * @return the name of the UPnP 'playbackCount' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_playback_count(void);

/**
 * Returns the name of the UPnP 'playlist' element.
 * This name is "upnp:playlist".
 * @return the name of the UPnP 'playlist' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_playlist(void);

/**
 * Returns the name of the UPnP 'price' element.
 * This name is "upnp:price".
 * @return the name of the UPnP 'price' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_price(void);

/**
 * Returns the name of the UPnP 'producer' element.
 * This name is "upnp:producer".
 * @return the name of the UPnP 'producer' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_producer(void);

/**
 * Returns the name of the UPnP 'programCode' element.
 * This name is "upnp:programCode".
 * @return the name of the UPnP 'programCode' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_program_code(void);

/**
 * Returns the name of the UPnP 'programTitle' element.
 * This name is "upnp:programTitle".
 * @return the name of the UPnP 'programTitle' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_program_title(void);

/**
 * Returns the name of the UPnP 'programID' element.
 * This name is "upnp:programID".
 * @return the name of the UPnP 'programID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_program_id(void);

/**
 * Returns the name of the UPnP 'radioBand' element.
 * This name is "upnp:radioBand".
 * @return the name of the UPnP 'radioBand' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_radio_band(void);

/**
 * Returns the name of the UPnP 'radioCallSign' element.
 * This name is "upnp:radioCallSign".
 * @return the name of the UPnP 'radioCallSign' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_radio_call_sign(void);

/**
 * Returns the name of the UPnP 'radioStationID' element.
 * This name is "upnp:radioStationID".
 * @return the name of the UPnP 'radioStationID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_radio_station_id(void);

/**
 * Returns the name of the UPnP 'rating' element.
 * This name is "upnp:rating".
 * @return the name of the UPnP 'rating' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_rating(void);

/**
 * Returns the name of the UPnP 'recordable' element.
 * This name is "upnp:recordable".
 * @return the name of the UPnP 'recordable' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_recordable(void);

/**
 * Returns the name of the UPnP 'region' element.
 * This name is "upnp:region".
 * @return the name of the UPnP 'region' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_region(void);

/**
 * Returns the name of the UPnP 'recordedDayOfWeek' element.
 * This name is "upnp:recordedDayOfWeek".
 * @return the name of the UPnP 'recordedDayOfWeek' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_recorded_day_of_week(void);

/**
 * Returns the name of the UPnP 'recordedDuration' element.
 * This name is "upnp:recordedDuration".
 * @return the name of the UPnP 'recordedDuration' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_recorded_duration(void);

/**
 * Returns the name of the UPnP 'recordedStartDateTime' element.
 * This name is "upnp:recordedStartDateTime".
 * @return the name of the UPnP 'recordedStartDateTime' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_recorded_start_date_time(void);

/**
 * Returns the name of the UPnP 'scheduledEndTime' element.
 * This name is "upnp:scheduledEndTime".
 * @return the name of the UPnP 'scheduledEndTime' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_scheduled_end_time(void);

/**
 * Returns the name of the UPnP 'scheduledStartTime' element.
 * This name is "upnp:scheduledStartTime".
 * @return the name of the UPnP 'scheduledStartTime' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_scheduled_start_time(void);

/**
 * Returns the name of the UPnP 'searchClass' element.
 * This name is "upnp:searchClass".
 * @return the name of the UPnP 'searchClass' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_search_class(void);

/**
 * Returns the name of the UPnP 'seriesID' element.
 * This name is "upnp:seriesID".
 * @return the name of the UPnP 'seriesID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_series_id(void);

/**
 * Returns the name of the UPnP 'seriesTitle' element.
 * This name is "upnp:seriesTitle".
 * @return the name of the UPnP 'seriesTitle' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_series_title(void);

/**
 * Returns the name of the UPnP 'serviceProvider' element.
 * This name is "upnp:serviceProvider".
 * @return the name of the UPnP 'serviceProvider' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_service_provider(void);

/**
 * Returns the name of the UPnP 'signalLocked' element.
 * This name is "upnp:signalLocked".
 * @return the name of the UPnP 'signalLocked' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_signal_locked(void);

/**
 * Returns the name of the UPnP 'signalStrength' element.
 * This name is "upnp:signalStrength".
 * @return the name of the UPnP 'signalStrength' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_signal_strength(void);

/**
 * Returns the name of the UPnP 'srsRecordScheduleID' element.
 * This name is "upnp:srsRecordScheduleID".
 * @return the name of the UPnP 'srsRecordScheduleID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_srs_record_schedule_id(void);

/**
 * Returns the name of the UPnP 'srsRecordTaskID' element.
 * This name is "upnp:srsRecordTaskID".
 * @return the name of the UPnP 'srsRecordTaskID' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_srs_record_task_id(void);

/**
 * Returns the name of the UPnP 'stateVariableCollection' element.
 * This name is "upnp:stateVariableCollection".
 * @return the name of the UPnP 'stateVariableCollection' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_state_variable_collection(void);

/**
 * Returns the name of the UPnP 'storageFree' element.
 * This name is "upnp:storageFree".
 * @return the name of the UPnP 'storageFree' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_storage_free(void);

/**
 * Returns the name of the UPnP 'storageMaxPartition' element.
 * This name is "upnp:storageMaxPartition".
 * @return the name of the UPnP 'storageMaxPartition' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_storage_max_partition(void);

/**
 * Returns the name of the UPnP 'storageMedium' element.
 * This name is "upnp:storageMedium".
 * @return the name of the UPnP 'storageMedium' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_storage_medium(void);

/**
 * Returns the name of the UPnP 'storageTotal' element.
 * This name is "upnp:storageTotal".
 * @return the name of the UPnP 'storageTotal element.
 */
extern const dav_didl_name* dav_didl_element_upnp_storage_total(void);

/**
 * Returns the name of the UPnP 'storageUsed' element.
 * This name is "upnp:storageUsed".
 * @return the name of the UPnP 'storageUsed element.
 */
extern const dav_didl_name* dav_didl_element_upnp_storage_used(void);

/**
 * Returns the name of the UPnP 'toc' element.
 * This name is "upnp:toc".
 * @return the name of the UPnP 'toc' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_toc(void);

/**
 * Returns the name of the UPnP 'totalDeletedChildCount' element.
 * This name is "upnp:totalDeletedChildCount".
 * @return the name of the UPnP 'totalDeletedChildCount' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_total_deleted_child_count(void);

/**
 * Returns the name of the UPnP 'tuned' element.
 * This name is "upnp:tuned".
 * @return the name of the UPnP 'tuned' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_tuned(void);

/**
 * Returns the name of the UPnP 'userAnnotation' element.
 * This name is "upnp:userAnnotation".
 * @return the name of the UPnP 'userAnnotation' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_user_annotation(void);

/**
 * Returns the name of the UPnP 'writeStatus' element.
 * This name is "upnp:writeStatus".
 * @return the name of the UPnP 'writeStatus' element.
 */
extern const dav_didl_name* dav_didl_element_upnp_write_status(void);

/**
 * Returns the name of the xsrs 'reservationID' element.
 * This name is "xsrs:reservationID".
 * @return the name of the xsrs 'reservationID' element.
 */
extern const dav_didl_name* dav_didl_element_xsrs_reservation_id(void);

/**
 * Returns the name of the UPnP 'object' class.
 * This name is "object".
 * @return the name of the UPnP 'object' class.
 */
extern const dav_didl_name* dav_didl_class_object(void);

/**
 * Returns the name of the UPnP 'object.item' class.
 * This name is "object.item".
 * @return the name of the UPnP 'object.item' class.
 */
extern const dav_didl_name* dav_didl_class_item(void);

/**
 * Returns the name of the UPnP 'object.container' class.
 * This name is "object.container".
 * @return the name of the UPnP 'object.container' class.
 */
extern const dav_didl_name* dav_didl_class_container(void);

/**
 * Returns the name of the UPnP 'object.item.imageItem' class.
 * This name is "object.item.imageItem".
 * @return the name of the UPnP 'object.item.imageItem' class.
 */
extern const dav_didl_name* dav_didl_class_image_item(void);

/**
 * Returns the name of the UPnP 'object.item.audioItem' class.
 * This name is "object.item.audioItem".
 * @return the name of the UPnP 'object.item.audioItem' class.
 */
extern const dav_didl_name* dav_didl_class_audio_item(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem' class.
 * This name is "object.item.videoItem".
 * @return the name of the UPnP 'object.item.videoItem' class.
 */
extern const dav_didl_name* dav_didl_class_video_item(void);

/**
 * Returns the name of the UPnP 'object.item.playlistItem' class.
 * This name is "object.item.playlistItem".
 * @return the name of the UPnP 'object.item.playlistItem' class.
 */
extern const dav_didl_name* dav_didl_class_playlist_item(void);

/**
 * Returns the name of the UPnP 'object.item.textItem' class.
 * This name is "object.item.textItem".
 * @return the name of the UPnP 'object.item.textItem' class.
 */
extern const dav_didl_name* dav_didl_class_text_item(void);

/**
 * Returns the name of the UPnP 'object.container.person' class.
 * This name is "object.container.person".
 * @return the name of the UPnP 'object.container.person' class.
 */
extern const dav_didl_name* dav_didl_class_person(void);

/**
 * Returns the name of the UPnP 'object.container.playlistContainer' class.
 * This name is "object.container.playlistContainer".
 * @return the name of the UPnP 'object.container.playlistContainer' class.
 */
extern const dav_didl_name* dav_didl_class_playlist_container(void);

/**
 * Returns the name of the UPnP 'object.container.album' class.
 * This name is "object.container.album".
 * @return the name of the UPnP 'object.container.album' class.
 */
extern const dav_didl_name* dav_didl_class_album(void);

/**
 * Returns the name of the UPnP 'object.container.genre' class.
 * This name is "object.container.genre".
 * @return the name of the UPnP 'object.container.genre' class.
 */
extern const dav_didl_name* dav_didl_class_genre(void);

/**
 * Returns the name of the UPnP 'object.container.storageSystem' class.
 * This name is "object.container.storageSystem".
 * @return the name of the UPnP 'object.container.storageSystem' class.
 */
extern const dav_didl_name* dav_didl_class_storage_system(void);

/**
 * Returns the name of the UPnP 'object.container.storageVolume' class.
 * This name is "object.container.storageVolume".
 * @return the name of the UPnP 'object.container.storageVolume' class.
 */
extern const dav_didl_name* dav_didl_class_storage_volume(void);

/**
 * Returns the name of the UPnP 'object.container.storageFolder' class.
 * This name is "object.container.storageFolder".
 * @return the name of the UPnP 'object.container.storageFolder' class.
 */
extern const dav_didl_name* dav_didl_class_storage_folder(void);

/**
 * Returns the name of the UPnP 'object.item.imageItem.photo' class.
 * This name is "object.item.imageItem.photo".
 * @return the name of the UPnP 'object.item.imageItem.photo' class.
 */
extern const dav_didl_name* dav_didl_class_photo(void);

/**
 * Returns the name of the UPnP 'object.item.audioItem.musicTrack' class.
 * This name is "object.item.audioItem.musicTrack".
 * @return the name of the UPnP 'object.item.audioItem.musicTrack' class.
 */
extern const dav_didl_name* dav_didl_class_music_track(void);

/**
 * Returns the name of the UPnP 'object.item.audioItem.audioBroadcast' class.
 * This name is "object.item.audioItem.audioBroadcast".
 * @return the name of the UPnP 'object.item.audioItem.audioBroadcast' class.
 */
extern const dav_didl_name* dav_didl_class_audio_broadcast(void);

/**
 * Returns the name of the UPnP 'object.item.audioItem.audioBook' class.
 * This name is "object.item.audioItem.audioBook".
 * @return the name of the UPnP 'object.item.audioItem.audioBook' class.
 */
extern const dav_didl_name* dav_didl_class_audio_book(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem.movie' class.
 * This name is "object.item.videoItem.movie".
 * @return the name of the UPnP 'object.item.videoItem.movie' class.
 */
extern const dav_didl_name* dav_didl_class_movie(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem.videoBroadcast' class.
 * This name is "object.item.videoItem.videoBroadcast".
 * @return the name of the UPnP 'object.item.videoItem.videoBroadcast' class.
 */
extern const dav_didl_name* dav_didl_class_video_broadcast(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem.musicVideoClip' class.
 * This name is "object.item.videoItem.musicVideoClip".
 * @return the name of the UPnP 'object.item.videoItem.musicVideoClip' class.
 */
extern const dav_didl_name* dav_didl_class_music_video_clip(void);

/**
 * Returns the name of the UPnP 'object.container.person.musicArtist' class.
 * This name is "object.container.person.musicArtist".
 * @return the name of the UPnP 'object.container.person.musicArtist' class.
 */
extern const dav_didl_name* dav_didl_class_music_artist(void);

/**
 * Returns the name of the UPnP 'object.container.person.movieActor' class.
 * This name is "object.container.person.movieActor".
 * @return the name of the UPnP 'object.container.person.movieActor' class.
 */
extern const dav_didl_name* dav_didl_class_movie_actor(void);

/**
 * Returns the name of the UPnP 'object.container.album.musicAlbum' class.
 * This name is "object.container.album.musicAlbum".
 * @return the name of the UPnP 'object.container.album.musicAlbum' class.
 */
extern const dav_didl_name* dav_didl_class_music_album(void);

/**
 * Returns the name of the UPnP 'object.container.album.photoAlbum' class.
 * This name is "object.container.album.photoAlbum".
 * @return the name of the UPnP 'object.container.album.photoAlbum' class.
 */
extern const dav_didl_name* dav_didl_class_photo_album(void);

/**
 * Returns the name of the UPnP 'object.container.album.videoAlbum' class.
 * This name is "object.container.album.videoAlbum".
 * @return the name of the UPnP 'object.container.album.videoAlbum' class.
 */
extern const dav_didl_name* dav_didl_class_video_album(void);

/**
 * Returns the name of the UPnP 'object.container.genre.musicGenre' class.
 * This name is "object.container.genre.musicGenre".
 * @return the name of the UPnP 'object.container.genre.musicGenre' class.
 */
extern const dav_didl_name* dav_didl_class_music_genre(void);

/**
 * Returns the name of the UPnP 'object.container.genre.movieGenre' class.
 * This name is "object.container.genre.movieGenre".
 * @return the name of the UPnP 'object.container.genre.movieGenre' class.
 */
extern const dav_didl_name* dav_didl_class_movie_genre(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem.protectedMovie' class.
 * This name is "object.item.videoItem.protectedMovie".
 * @return the name of the UPnP 'object.item.videoItem.protectedMovie' class.
 */
extern const dav_didl_name* dav_didl_class_protected_movie(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem.cinema' class.
 * This name is "object.item.videoItem.cinema".
 * @return the name of the UPnP 'object.item.videoItem.cinema' class.
 */
extern const dav_didl_name* dav_didl_class_cinema(void);

/**
 * Returns the name of the UPnP 'object.item.videoItem.tv' class.
 * This name is "object.item.videoItem.tv".
 * @return the name of the UPnP 'object.item.videoItem.tv' class.
 */
extern const dav_didl_name* dav_didl_class_tv(void);

#ifdef __cplusplus
}
#endif

#endif
