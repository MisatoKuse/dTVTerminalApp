/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

/** @file dav_rcs.h
 *  @brief The dav_rcs interface provides methods for making SOAP request/response messages of
 *   RenderingControl actions. This interface provides methods for parsing SOAP request/response
 *   messages of RenderingControl actions.
 *  @see  RenderingControl:2 Service Template Version 1.01 For UPnP. Version 1.0 section 2.4
 */

#ifndef DAV_RCS_H
#define DAV_RCS_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Returns the action name of ListPresets.
  * @return the action name of ListPresets.
  */
extern const du_uchar* dav_rcs_action_name_list_presets(void);

/**
 * Appends a SOAP request message of ListPresets action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_list_presets(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of ListPresets action, stores each name and
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
extern du_bool dav_rcs_parse_list_presets(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of ListPresets action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_preset_name_list value of CurrentPresetNameList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_list_presets_response(du_uchar_array* xml, du_uint32 v, const du_uchar* current_preset_name_list);

/**
 * Parses a SOAP response message of ListPresets action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_preset_name_list pointer to the storage location for CurrentPresetNameList argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_list_presets_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** current_preset_name_list);

/**
  * Returns the action name of SelectPreset.
  * @return the action name of SelectPreset.
  */
extern const du_uchar* dav_rcs_action_name_select_preset(void);

/**
 * Appends a SOAP request message of SelectPreset action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] preset_name value of PresetName argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_select_preset(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* preset_name);

/**
 * Parses a SOAP request message of SelectPreset action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] preset_name pointer to the storage location for PresetName argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_select_preset(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** preset_name);

/**
 * Appends a SOAP response message of SelectPreset action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_select_preset_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SelectPreset action, stores each name and
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
extern du_bool dav_rcs_parse_select_preset_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetBrightness.
  * @return the action name of GetBrightness.
  */
extern const du_uchar* dav_rcs_action_name_get_brightness(void);

/**
 * Appends a SOAP request message of GetBrightness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_brightness(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetBrightness action, stores each name and
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
extern du_bool dav_rcs_parse_get_brightness(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetBrightness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_brightness value of CurrentBrightness argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_brightness_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_brightness);

/**
 * Parses a SOAP response message of GetBrightness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_brightness pointer to the storage location for CurrentBrightness argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_brightness_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_brightness);

/**
  * Returns the action name of SetBrightness.
  * @return the action name of SetBrightness.
  */
extern const du_uchar* dav_rcs_action_name_set_brightness(void);

/**
 * Appends a SOAP request message of SetBrightness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_brightness value of DesiredBrightness argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_brightness(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_brightness);

/**
 * Parses a SOAP request message of SetBrightness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_brightness pointer to the storage location for DesiredBrightness argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_brightness(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_brightness);

/**
 * Appends a SOAP response message of SetBrightness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_brightness_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetBrightness action, stores each name and
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
extern du_bool dav_rcs_parse_set_brightness_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetContrast.
  * @return the action name of GetContrast.
  */
extern const du_uchar* dav_rcs_action_name_get_contrast(void);

/**
 * Appends a SOAP request message of GetContrast action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_contrast(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetContrast action, stores each name and
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
extern du_bool dav_rcs_parse_get_contrast(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetContrast action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_contrast value of CurrentContrast argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_contrast_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_contrast);

/**
 * Parses a SOAP response message of GetContrast action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_contrast pointer to the storage location for CurrentContrast argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_contrast_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_contrast);

/**
  * Returns the action name of SetContrast.
  * @return the action name of SetContrast.
  */
extern const du_uchar* dav_rcs_action_name_set_contrast(void);

/**
 * Appends a SOAP request message of SetContrast action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_contrast value of DesiredContrast argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_contrast(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_contrast);

/**
 * Parses a SOAP request message of SetContrast action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_contrast pointer to the storage location for DesiredContrast argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_contrast(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_contrast);

/**
 * Appends a SOAP response message of SetContrast action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_contrast_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetContrast action, stores each name and
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
extern du_bool dav_rcs_parse_set_contrast_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetSharpness.
  * @return the action name of GetSharpness.
  */
extern const du_uchar* dav_rcs_action_name_get_sharpness(void);

/**
 * Appends a SOAP request message of GetSharpness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_sharpness(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetSharpness action, stores each name and
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
extern du_bool dav_rcs_parse_get_sharpness(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetSharpness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_sharpness value of CurrentSharpness argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_sharpness_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_sharpness);

/**
 * Parses a SOAP response message of GetSharpness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_sharpness pointer to the storage location for CurrentSharpness argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_sharpness_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_sharpness);

/**
  * Returns the action name of SetSharpness.
  * @return the action name of SetSharpness.
  */
extern const du_uchar* dav_rcs_action_name_set_sharpness(void);

/**
 * Appends a SOAP request message of SetSharpness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_sharpness value of DesiredSharpness argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_sharpness(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_sharpness);

/**
 * Parses a SOAP request message of SetSharpness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_sharpness pointer to the storage location for DesiredSharpness argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_sharpness(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_sharpness);

/**
 * Appends a SOAP response message of SetSharpness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_sharpness_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetSharpness action, stores each name and
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
extern du_bool dav_rcs_parse_set_sharpness_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetRedVideoGain.
  * @return the action name of GetRedVideoGain.
  */
extern const du_uchar* dav_rcs_action_name_get_red_video_gain(void);

/**
 * Appends a SOAP request message of GetRedVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_red_video_gain(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetRedVideoGain action, stores each name and
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
extern du_bool dav_rcs_parse_get_red_video_gain(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetRedVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_red_video_gain value of CurrentRedVideoGain argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_red_video_gain_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_red_video_gain);

/**
 * Parses a SOAP response message of GetRedVideoGain action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_red_video_gain pointer to the storage location for CurrentRedVideoGain argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_red_video_gain_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_red_video_gain);

/**
  * Returns the action name of SetRedVideoGain.
  * @return the action name of SetRedVideoGain.
  */
extern const du_uchar* dav_rcs_action_name_set_red_video_gain(void);

/**
 * Appends a SOAP request message of SetRedVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_red_video_gain value of DesiredRedVideoGain argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_red_video_gain(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_red_video_gain);

/**
 * Parses a SOAP request message of SetRedVideoGain action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_red_video_gain pointer to the storage location for DesiredRedVideoGain argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_red_video_gain(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_red_video_gain);

/**
 * Appends a SOAP response message of SetRedVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_red_video_gain_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetRedVideoGain action, stores each name and
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
extern du_bool dav_rcs_parse_set_red_video_gain_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetGreenVideoGain.
  * @return the action name of GetGreenVideoGain.
  */
extern const du_uchar* dav_rcs_action_name_get_green_video_gain(void);

/**
 * Appends a SOAP request message of GetGreenVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_green_video_gain(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetGreenVideoGain action, stores each name and
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
extern du_bool dav_rcs_parse_get_green_video_gain(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetGreenVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_green_video_gain value of CurrentGreenVideoGain argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_green_video_gain_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_green_video_gain);

/**
 * Parses a SOAP response message of GetGreenVideoGain action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_green_video_gain pointer to the storage location for CurrentGreenVideoGain argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_green_video_gain_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_green_video_gain);

/**
  * Returns the action name of SetGreenVideoGain.
  * @return the action name of SetGreenVideoGain.
  */
extern const du_uchar* dav_rcs_action_name_set_green_video_gain(void);

/**
 * Appends a SOAP request message of SetGreenVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_green_video_gain value of DesiredGreenVideoGain argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_green_video_gain(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_green_video_gain);

/**
 * Parses a SOAP request message of SetGreenVideoGain action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_green_video_gain pointer to the storage location for DesiredGreenVideoGain argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_green_video_gain(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_green_video_gain);

/**
 * Appends a SOAP response message of SetGreenVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_green_video_gain_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetGreenVideoGain action, stores each name and
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
extern du_bool dav_rcs_parse_set_green_video_gain_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetBlueVideoGain.
  * @return the action name of GetBlueVideoGain.
  */
extern const du_uchar* dav_rcs_action_name_get_blue_video_gain(void);

/**
 * Appends a SOAP request message of GetBlueVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_blue_video_gain(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetBlueVideoGain action, stores each name and
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
extern du_bool dav_rcs_parse_get_blue_video_gain(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetBlueVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_blue_video_gain value of CurrentBlueVideoGain argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_blue_video_gain_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_blue_video_gain);

/**
 * Parses a SOAP response message of GetBlueVideoGain action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_blue_video_gain pointer to the storage location for CurrentBlueVideoGain argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_blue_video_gain_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_blue_video_gain);

/**
  * Returns the action name of SetBlueVideoGain.
  * @return the action name of SetBlueVideoGain.
  */
extern const du_uchar* dav_rcs_action_name_set_blue_video_gain(void);

/**
 * Appends a SOAP request message of SetBlueVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_blue_video_gain value of DesiredBlueVideoGain argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_blue_video_gain(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_blue_video_gain);

/**
 * Parses a SOAP request message of SetBlueVideoGain action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_blue_video_gain pointer to the storage location for DesiredBlueVideoGain argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_blue_video_gain(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_blue_video_gain);

/**
 * Appends a SOAP response message of SetBlueVideoGain action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_blue_video_gain_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetBlueVideoGain action, stores each name and
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
extern du_bool dav_rcs_parse_set_blue_video_gain_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetRedVideoBlackLevel.
  * @return the action name of GetRedVideoBlackLevel.
  */
extern const du_uchar* dav_rcs_action_name_get_red_video_black_level(void);

/**
 * Appends a SOAP request message of GetRedVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_red_video_black_level(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetRedVideoBlackLevel action, stores each name and
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
extern du_bool dav_rcs_parse_get_red_video_black_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetRedVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_red_video_black_level value of CurrentRedVideoBlackLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_red_video_black_level_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_red_video_black_level);

/**
 * Parses a SOAP response message of GetRedVideoBlackLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_red_video_black_level pointer to the storage location for CurrentRedVideoBlackLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_red_video_black_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_red_video_black_level);

/**
  * Returns the action name of SetRedVideoBlackLevel.
  * @return the action name of SetRedVideoBlackLevel.
  */
extern const du_uchar* dav_rcs_action_name_set_red_video_black_level(void);

/**
 * Appends a SOAP request message of SetRedVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_red_video_black_level value of DesiredRedVideoBlackLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_red_video_black_level(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_red_video_black_level);

/**
 * Parses a SOAP request message of SetRedVideoBlackLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_red_video_black_level pointer to the storage location for DesiredRedVideoBlackLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_red_video_black_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_red_video_black_level);

/**
 * Appends a SOAP response message of SetRedVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_red_video_black_level_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetRedVideoBlackLevel action, stores each name and
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
extern du_bool dav_rcs_parse_set_red_video_black_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetGreenVideoBlackLevel.
  * @return the action name of GetGreenVideoBlackLevel.
  */
extern const du_uchar* dav_rcs_action_name_get_green_video_black_level(void);

/**
 * Appends a SOAP request message of GetGreenVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_green_video_black_level(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetGreenVideoBlackLevel action, stores each name and
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
extern du_bool dav_rcs_parse_get_green_video_black_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetGreenVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_green_video_black_level value of CurrentGreenVideoBlackLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_green_video_black_level_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_green_video_black_level);

/**
 * Parses a SOAP response message of GetGreenVideoBlackLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_green_video_black_level pointer to the storage location for CurrentGreenVideoBlackLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_green_video_black_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_green_video_black_level);

/**
  * Returns the action name of SetGreenVideoBlackLevel.
  * @return the action name of SetGreenVideoBlackLevel.
  */
extern const du_uchar* dav_rcs_action_name_set_green_video_black_level(void);

/**
 * Appends a SOAP request message of SetGreenVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_green_video_black_level value of DesiredGreenVideoBlackLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_green_video_black_level(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_green_video_black_level);

/**
 * Parses a SOAP request message of SetGreenVideoBlackLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_green_video_black_level pointer to the storage location for DesiredGreenVideoBlackLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_green_video_black_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_green_video_black_level);

/**
 * Appends a SOAP response message of SetGreenVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_green_video_black_level_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetGreenVideoBlackLevel action, stores each name and
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
extern du_bool dav_rcs_parse_set_green_video_black_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetBlueVideoBlackLevel.
  * @return the action name of GetBlueVideoBlackLevel.
  */
extern const du_uchar* dav_rcs_action_name_get_blue_video_black_level(void);

/**
 * Appends a SOAP request message of GetBlueVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_blue_video_black_level(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetBlueVideoBlackLevel action, stores each name and
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
extern du_bool dav_rcs_parse_get_blue_video_black_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetBlueVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_blue_video_black_level value of CurrentBlueVideoBlackLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_blue_video_black_level_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_blue_video_black_level);

/**
 * Parses a SOAP response message of GetBlueVideoBlackLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_blue_video_black_level pointer to the storage location for CurrentBlueVideoBlackLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_blue_video_black_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_blue_video_black_level);

/**
  * Returns the action name of SetBlueVideoBlackLevel.
  * @return the action name of SetBlueVideoBlackLevel.
  */
extern const du_uchar* dav_rcs_action_name_set_blue_video_black_level(void);

/**
 * Appends a SOAP request message of SetBlueVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_blue_video_black_level value of DesiredBlueVideoBlackLevel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_blue_video_black_level(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_blue_video_black_level);

/**
 * Parses a SOAP request message of SetBlueVideoBlackLevel action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_blue_video_black_level pointer to the storage location for DesiredBlueVideoBlackLevel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_blue_video_black_level(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_blue_video_black_level);

/**
 * Appends a SOAP response message of SetBlueVideoBlackLevel action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_blue_video_black_level_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetBlueVideoBlackLevel action, stores each name and
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
extern du_bool dav_rcs_parse_set_blue_video_black_level_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetColorTemperature.
  * @return the action name of GetColorTemperature.
  */
extern const du_uchar* dav_rcs_action_name_get_color_temperature(void);

/**
 * Appends a SOAP request message of GetColorTemperature action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_color_temperature(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetColorTemperature action, stores each name and
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
extern du_bool dav_rcs_parse_get_color_temperature(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetColorTemperature action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_color_temperature value of CurrentColorTemperature argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_color_temperature_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_color_temperature);

/**
 * Parses a SOAP response message of GetColorTemperature action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_color_temperature pointer to the storage location for CurrentColorTemperature argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_color_temperature_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_color_temperature);

/**
  * Returns the action name of SetColorTemperature.
  * @return the action name of SetColorTemperature.
  */
extern const du_uchar* dav_rcs_action_name_set_color_temperature(void);

/**
 * Appends a SOAP request message of SetColorTemperature action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_color_temperature value of DesiredColorTemperature argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_color_temperature(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_uint16 desired_color_temperature);

/**
 * Parses a SOAP request message of SetColorTemperature action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_color_temperature pointer to the storage location for DesiredColorTemperature argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_color_temperature(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_uint16* desired_color_temperature);

/**
 * Appends a SOAP response message of SetColorTemperature action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_color_temperature_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetColorTemperature action, stores each name and
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
extern du_bool dav_rcs_parse_set_color_temperature_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetHorizontalKeystone.
  * @return the action name of GetHorizontalKeystone.
  */
extern const du_uchar* dav_rcs_action_name_get_horizontal_keystone(void);

/**
 * Appends a SOAP request message of GetHorizontalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_horizontal_keystone(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetHorizontalKeystone action, stores each name and
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
extern du_bool dav_rcs_parse_get_horizontal_keystone(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetHorizontalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_horizontal_keystone value of CurrentHorizontalKeystone argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_horizontal_keystone_response(du_uchar_array* xml, du_uint32 v, du_int16 current_horizontal_keystone);

/**
 * Parses a SOAP response message of GetHorizontalKeystone action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_horizontal_keystone pointer to the storage location for CurrentHorizontalKeystone argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_horizontal_keystone_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int16* current_horizontal_keystone);

/**
  * Returns the action name of SetHorizontalKeystone.
  * @return the action name of SetHorizontalKeystone.
  */
extern const du_uchar* dav_rcs_action_name_set_horizontal_keystone(void);

/**
 * Appends a SOAP request message of SetHorizontalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_horizontal_keystone value of DesiredHorizontalKeystone argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_horizontal_keystone(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_int16 desired_horizontal_keystone);

/**
 * Parses a SOAP request message of SetHorizontalKeystone action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_horizontal_keystone pointer to the storage location for DesiredHorizontalKeystone argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_horizontal_keystone(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_int16* desired_horizontal_keystone);

/**
 * Appends a SOAP response message of SetHorizontalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_horizontal_keystone_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetHorizontalKeystone action, stores each name and
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
extern du_bool dav_rcs_parse_set_horizontal_keystone_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetVerticalKeystone.
  * @return the action name of GetVerticalKeystone.
  */
extern const du_uchar* dav_rcs_action_name_get_vertical_keystone(void);

/**
 * Appends a SOAP request message of GetVerticalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_vertical_keystone(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id);

/**
 * Parses a SOAP request message of GetVerticalKeystone action, stores each name and
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
extern du_bool dav_rcs_parse_get_vertical_keystone(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id);

/**
 * Appends a SOAP response message of GetVerticalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_vertical_keystone value of CurrentVerticalKeystone argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_vertical_keystone_response(du_uchar_array* xml, du_uint32 v, du_int16 current_vertical_keystone);

/**
 * Parses a SOAP response message of GetVerticalKeystone action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_vertical_keystone pointer to the storage location for CurrentVerticalKeystone argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_vertical_keystone_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int16* current_vertical_keystone);

/**
  * Returns the action name of SetVerticalKeystone.
  * @return the action name of SetVerticalKeystone.
  */
extern const du_uchar* dav_rcs_action_name_set_vertical_keystone(void);

/**
 * Appends a SOAP request message of SetVerticalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] desired_vertical_keystone value of DesiredVerticalKeystone argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_vertical_keystone(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, du_int16 desired_vertical_keystone);

/**
 * Parses a SOAP request message of SetVerticalKeystone action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] desired_vertical_keystone pointer to the storage location for DesiredVerticalKeystone argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_vertical_keystone(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, du_int16* desired_vertical_keystone);

/**
 * Appends a SOAP response message of SetVerticalKeystone action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_vertical_keystone_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetVerticalKeystone action, stores each name and
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
extern du_bool dav_rcs_parse_set_vertical_keystone_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetMute.
  * @return the action name of GetMute.
  */
extern const du_uchar* dav_rcs_action_name_get_mute(void);

/**
 * Appends a SOAP request message of GetMute action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_mute(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel);

/**
 * Parses a SOAP request message of GetMute action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_mute(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel);

/**
 * Appends a SOAP response message of GetMute action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_mute value of CurrentMute argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_mute_response(du_uchar_array* xml, du_uint32 v, du_bool current_mute);

/**
 * Parses a SOAP response message of GetMute action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_mute pointer to the storage location for CurrentMute argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_mute_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* current_mute);

/**
  * Returns the action name of SetMute.
  * @return the action name of SetMute.
  */
extern const du_uchar* dav_rcs_action_name_set_mute(void);

/**
 * Appends a SOAP request message of SetMute action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @param[in] desired_mute value of DesiredMute argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_mute(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel, du_bool desired_mute);

/**
 * Parses a SOAP request message of SetMute action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @param[out] desired_mute pointer to the storage location for DesiredMute argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_mute(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel, du_bool* desired_mute);

/**
 * Appends a SOAP response message of SetMute action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_mute_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetMute action, stores each name and
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
extern du_bool dav_rcs_parse_set_mute_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetVolume.
  * @return the action name of GetVolume.
  */
extern const du_uchar* dav_rcs_action_name_get_volume(void);

/**
 * Appends a SOAP request message of GetVolume action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_volume(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel);

/**
 * Parses a SOAP request message of GetVolume action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_volume(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel);

/**
 * Appends a SOAP response message of GetVolume action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_volume value of CurrentVolume argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_volume_response(du_uchar_array* xml, du_uint32 v, du_uint16 current_volume);

/**
 * Parses a SOAP response message of GetVolume action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_volume pointer to the storage location for CurrentVolume argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_volume_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint16* current_volume);

/**
  * Returns the action name of SetVolume.
  * @return the action name of SetVolume.
  */
extern const du_uchar* dav_rcs_action_name_set_volume(void);

/**
 * Appends a SOAP request message of SetVolume action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @param[in] desired_volume value of DesiredVolume argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_volume(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel, du_uint16 desired_volume);

/**
 * Parses a SOAP request message of SetVolume action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @param[out] desired_volume pointer to the storage location for DesiredVolume argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_volume(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel, du_uint16* desired_volume);

/**
 * Appends a SOAP response message of SetVolume action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_volume_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetVolume action, stores each name and
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
extern du_bool dav_rcs_parse_set_volume_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetVolumeDB.
  * @return the action name of GetVolumeDB.
  */
extern const du_uchar* dav_rcs_action_name_get_volume_db(void);

/**
 * Appends a SOAP request message of GetVolumeDB action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_volume_db(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel);

/**
 * Parses a SOAP request message of GetVolumeDB action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_volume_db(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel);

/**
 * Appends a SOAP response message of GetVolumeDB action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_volume value of CurrentVolume argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_volume_db_response(du_uchar_array* xml, du_uint32 v, du_int16 current_volume);

/**
 * Parses a SOAP response message of GetVolumeDB action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_volume pointer to the storage location for CurrentVolume argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_volume_db_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int16* current_volume);

/**
  * Returns the action name of SetVolumeDB.
  * @return the action name of SetVolumeDB.
  */
extern const du_uchar* dav_rcs_action_name_set_volume_db(void);

/**
 * Appends a SOAP request message of SetVolumeDB action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @param[in] desired_volume value of DesiredVolume argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_volume_db(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel, du_int16 desired_volume);

/**
 * Parses a SOAP request message of SetVolumeDB action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @param[out] desired_volume pointer to the storage location for DesiredVolume argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_volume_db(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel, du_int16* desired_volume);

/**
 * Appends a SOAP response message of SetVolumeDB action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_volume_db_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetVolumeDB action, stores each name and
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
extern du_bool dav_rcs_parse_set_volume_db_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetVolumeDBRange.
  * @return the action name of GetVolumeDBRange.
  */
extern const du_uchar* dav_rcs_action_name_get_volume_db_range(void);

/**
 * Appends a SOAP request message of GetVolumeDBRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_volume_db_range(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel);

/**
 * Parses a SOAP request message of GetVolumeDBRange action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_volume_db_range(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel);

/**
 * Appends a SOAP response message of GetVolumeDBRange action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] min_value value of MinValue argument.
 * @param[in] max_value value of MaxValue argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_volume_db_range_response(du_uchar_array* xml, du_uint32 v, du_int16 min_value, du_int16 max_value);

/**
 * Parses a SOAP response message of GetVolumeDBRange action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] min_value pointer to the storage location for MinValue argument value.
 * @param[out] max_value pointer to the storage location for MaxValue argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_volume_db_range_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_int16* min_value, du_int16* max_value);

/**
  * Returns the action name of GetLoudness.
  * @return the action name of GetLoudness.
  */
extern const du_uchar* dav_rcs_action_name_get_loudness(void);

/**
 * Appends a SOAP request message of GetLoudness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_loudness(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel);

/**
 * Parses a SOAP request message of GetLoudness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_loudness(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel);

/**
 * Appends a SOAP response message of GetLoudness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] current_loudness value of CurrentLoudness argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_loudness_response(du_uchar_array* xml, du_uint32 v, du_bool current_loudness);

/**
 * Parses a SOAP response message of GetLoudness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the response envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] current_loudness pointer to the storage location for CurrentLoudness argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_loudness_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_bool* current_loudness);

/**
  * Returns the action name of SetLoudness.
  * @return the action name of SetLoudness.
  */
extern const du_uchar* dav_rcs_action_name_set_loudness(void);

/**
 * Appends a SOAP request message of SetLoudness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] channel value of Channel argument.
 * @param[in] desired_loudness value of DesiredLoudness argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_loudness(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* channel, du_bool desired_loudness);

/**
 * Parses a SOAP request message of SetLoudness action, stores each name and
 * value of argumentName element(s) of the envelope element
 * in <em>param_array</em>, and stores each argument value of the action to each parameter.
 * If a parse error occurred, it returns false.
 * Otherwise it returns true.
 * @param[in] xml a string containing all of the document of the request envelope element.
 * @param[in] xml_len length of xml.
 * @param[out] param_array pointer to the destination du_str_array structure
 * @param[out] instance_id pointer to the storage location for InstanceID argument value.
 * @param[out] channel pointer to the storage location for Channel argument value.
 * @param[out] desired_loudness pointer to the storage location for DesiredLoudness argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_loudness(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** channel, du_bool* desired_loudness);

/**
 * Appends a SOAP response message of SetLoudness action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_loudness_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of SetLoudness action, stores each name and
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
extern du_bool dav_rcs_parse_set_loudness_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of GetStateVariables.
  * @return the action name of GetStateVariables.
  */
extern const du_uchar* dav_rcs_action_name_get_state_variables(void);

/**
 * Appends a SOAP request message of GetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] state_variable_list value of StateVariableList argument.
 * @param[in] state_variable_value_pairs value of StateVariableValuePairs argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_state_variables(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* state_variable_list, const du_uchar* state_variable_value_pairs);

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
 * @param[out] state_variable_value_pairs pointer to the storage location for StateVariableValuePairs argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_get_state_variables(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** state_variable_list, const du_uchar** state_variable_value_pairs);

/**
 * Appends a SOAP response message of GetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_get_state_variables_response(du_uchar_array* xml, du_uint32 v);

/**
 * Parses a SOAP response message of GetStateVariables action, stores each name and
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
extern du_bool dav_rcs_parse_get_state_variables_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array);

/**
  * Returns the action name of SetStateVariables.
  * @return the action name of SetStateVariables.
  */
extern const du_uchar* dav_rcs_action_name_set_state_variables(void);

/**
 * Appends a SOAP request message of SetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] instance_id value of InstanceID argument.
 * @param[in] rendering_control_udn value of RenderingControlUDN argument.
 * @param[in] service_type value of ServiceType argument.
 * @param[in] service_id value of ServiceId argument.
 * @param[in] state_variable_value_pairs value of StateVariableValuePairs argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_state_variables(du_uchar_array* xml, du_uint32 v, du_uint32 instance_id, const du_uchar* rendering_control_udn, const du_uchar* service_type, const du_uchar* service_id, const du_uchar* state_variable_value_pairs);

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
 * @param[out] rendering_control_udn pointer to the storage location for RenderingControlUDN argument value.
 * @param[out] service_type pointer to the storage location for ServiceType argument value.
 * @param[out] service_id pointer to the storage location for ServiceId argument value.
 * @param[out] state_variable_value_pairs pointer to the storage location for StateVariableValuePairs argument value.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>param_appay</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dav_rcs_parse_set_state_variables(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, du_uint32* instance_id, const du_uchar** rendering_control_udn, const du_uchar** service_type, const du_uchar** service_id, const du_uchar** state_variable_value_pairs);

/**
 * Appends a SOAP response message of SetStateVariables action to a <em>xml</em> array.
 * @param[in] xml pointer to the destination du_uchar_array structure.
 * @param[in] v version number of this service.
 * @param[in] state_variable_list value of StateVariableList argument.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_rcs_make_set_state_variables_response(du_uchar_array* xml, du_uint32 v, const du_uchar* state_variable_list);

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
extern du_bool dav_rcs_parse_set_state_variables_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** state_variable_list);

/**
 * the state variable name of 'PresetNameList'.
 * @return  "PresetNameList" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_preset_name_list(void);

/**
 * the state variable name of 'LastChange'.
 * @return  "LastChange" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_last_change(void);

/**
 * the state variable name of 'Brightness'.
 * @return  "Brightness" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_brightness(void);

/**
 * the state variable name of 'Contrast'.
 * @return  "Contrast" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_contrast(void);

/**
 * the state variable name of 'Sharpness'.
 * @return  "Sharpness" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_sharpness(void);

/**
 * the state variable name of 'RedVideoGain'.
 * @return  "RedVideoGain" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_red_video_gain(void);

/**
 * the state variable name of 'GreenVideoGain'.
 * @return  "GreenVideoGain" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_green_video_gain(void);

/**
 * the state variable name of 'BlueVideoGain'.
 * @return  "BlueVideoGain" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_blue_video_gain(void);

/**
 * the state variable name of 'RedVideoBlackLevel'.
 * @return  "RedVideoBlackLevel" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_red_video_black_level(void);

/**
 * the state variable name of 'GreenVideoBlackLevel'.
 * @return  "GreenVideoBlackLevel" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_green_video_black_level(void);

/**
 * the state variable name of 'BlueVideoBlackLevel'.
 * @return  "BlueVideoBlackLevel" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_blue_video_black_level(void);

/**
 * the state variable name of 'ColorTemperature'.
 * @return  "ColorTemperature" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_color_temperature(void);

/**
 * the state variable name of 'HorizontalKeystone'.
 * @return  "HorizontalKeystone" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_horizontal_keystone(void);

/**
 * the state variable name of 'VerticalKeystone'.
 * @return  "VerticalKeystone" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_vertical_keystone(void);

/**
 * the state variable name of 'Mute'.
 * @return  "Mute" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_mute(void);

/**
 * the state variable name of 'Volume'.
 * @return  "Volume" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_volume(void);

/**
 * the state variable name of 'VolumeDB'.
 * @return  "VolumeDB" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_volume_db(void);

/**
 * the state variable name of 'Loudness'.
 * @return  "Loudness" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_loudness(void);

/**
 * the state variable name of 'A_ARG_TYPE_Channel'.
 * @return  "A_ARG_TYPE_Channel" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_channel(void);

/**
 * the state variable name of 'A_ARG_TYPE_InstanceID'.
 * @return  "A_ARG_TYPE_InstanceID" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_instance_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_PresetName'.
 * @return  "A_ARG_TYPE_PresetName" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_preset_name(void);

/**
 * the state variable name of 'A_ARG_TYPE_DeviceUDN'.
 * @return  "A_ARG_TYPE_DeviceUDN" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_device_udn(void);

/**
 * the state variable name of 'A_ARG_TYPE_ServiceType'.
 * @return  "A_ARG_TYPE_ServiceType" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_service_type(void);

/**
 * the state variable name of 'A_ARG_TYPE_ServiceID'.
 * @return  "A_ARG_TYPE_ServiceID" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_service_id(void);

/**
 * the state variable name of 'A_ARG_TYPE_StateVariableValuePairs'.
 * @return  "A_ARG_TYPE_StateVariableValuePairs" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_state_variable_value_pairs(void);

/**
 * the state variable name of 'A_ARG_TYPE_StateVariableList'.
 * @return  "A_ARG_TYPE_StateVariableList" string.
 */
extern const du_uchar* dav_rcs_state_variable_name_a_arg_type_state_variable_list(void);

/**
 * Returns an error code of 'invalid Name'.
 * This error code is "701".
 * @return  "701" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_name(void);

/**
 * Returns an error code of 'invalid InstanceID'.
 * This error code is "702".
 * @return  "702" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_instance_id(void);

/**
 * Returns an error code of 'Invalid Channel'.
 * This error code is "703".
 * @return  "703" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_channel(void);

/**
 * Returns an error code of 'Invalid StateVariableList'.
 * This error code is "704".
 * @return  "704" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_statevariablelist(void);

/**
 * Returns an error code of 'Ill-formed CSV List'.
 * This error code is "705".
 * @return  "705" string.
 */
extern const du_uchar* dav_rcs_error_code_ill_formed_csv_list(void);

/**
 * Returns an error code of 'Invalid State Variable Value'.
 * This error code is "706".
 * @return  "706" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_state_variable_value(void);

/**
 * Returns an error code of 'Invalid MediaRenderer’s UDN'.
 * This error code is "707".
 * @return  "707" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_mediarenderers_udn(void);

/**
 * Returns an error code of 'Invalid Service Type'.
 * This error code is "708".
 * @return  "708" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_service_type(void);

/**
 * Returns an error code of 'Invalid Service Id'.
 * This error code is "709".
 * @return  "709" string.
 */
extern const du_uchar* dav_rcs_error_code_invalid_service_id(void);

/**
 * Returns an error code of 'State Variables Specified Improperly'.
 * This error code is "710".
 * @return  "710" string.
 */
extern const du_uchar* dav_rcs_error_code_state_variables_specified_improperly(void);

#ifdef __cplusplus
}
#endif

#endif
