/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dmrd_rcs_entity.h
 * @brief The dmrd_rcs_entity interface gives the interface definition of each RCS method(action).
 *
 * The interface provides 2 kinds of interfaces for handling each action, XXX and XXX_2.
 * (e.g. dmrd_rcs_entity_list_presets(XXX), dmrd_rcs_entity_list_presets_2(XXX_2))
 * The difference between XXX and XXX_2 is that only XXX_2 provides a parameter to handle a client information.
 *
 * Implementing one of XXX and XXX_2 is enough for handling an action corresponding to the interface.
 * If both XXX and XXX_2 interfaces are implemented, only XXX_2 is called by stub.
 *
 * @see http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf
 */

#ifndef DMRD_RCS_ENTITY_H
#define DMRD_RCS_ENTITY_H

#include <dupnp_dvc_service.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_list_presets</b> function is an application-defined
 *   callback function that returns a list of the currently defined presets.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_preset_name_list the name of a device preset.
 *   This may be any of the predefined presets ("FactoryDefaults" or "InstallationDefaults")
 *   or any of the valid preset names supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of preset name,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.21.
 */
typedef du_bool (*dmrd_rcs_entity_list_presets)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uchar_array* current_preset_name_list, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_list_presets_2</b> function is an application-defined
 *   callback function that returns a list of the currently defined presets.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_preset_name_list the name of a device preset.
 *   This may be any of the predefined presets ("FactoryDefaults" or "InstallationDefaults")
 *   or any of the valid preset names supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of preset name,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.21.
 */
typedef du_bool (*dmrd_rcs_entity_list_presets_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uchar_array* current_preset_name_list, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_select_preset</b> function is an application-defined
 *   callback function that restores (a subset) of the state variables to the values
 *   associated with the specified preset.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] preset_name the name of a device preset.
 *   This may be any of the predefined presets ("FactoryDefaults" or "InstallationDefaults")
 *   or any of the valid preset names supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of preset name,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.21.
 */
typedef du_bool (*dmrd_rcs_entity_select_preset)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* preset_name, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_select_preset_2</b> function is an application-defined
 *   callback function that restores (a subset) of the state variables to the values
 *   associated with the specified preset.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] preset_name the name of a device preset.
 *   This may be any of the predefined presets ("FactoryDefaults" or "InstallationDefaults")
 *   or any of the valid preset names supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of preset name,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.21.
 */
typedef du_bool (*dmrd_rcs_entity_select_preset_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* preset_name, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_brightness</b> function is an application-defined
 *   callback function that retrieves the current value of brightness.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_brightness the current brightness setting value.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_brightness)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_brightness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_brightness_2</b> function is an application-defined
 *   callback function that retrieves the current value of brightness.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_brightness the current brightness setting value.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_brightness_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_brightness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_brightness</b> function is an application-defined
 *   callback function that sets brightness to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_brightness  brightness value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change
 *    that is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_brightness)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_brightness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_brightness_2</b> function is an application-defined
 *   callback function that sets brightness to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_brightness  brightness value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change
 *    that is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_brightness_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_brightness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_contrast</b> function is an application-defined
 *   callback function that retrieves the current value of contrast.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_contrast the current contrast setting value.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_contrast)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_contrast, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_contrast_2</b> function is an application-defined
 *   callback function that retrieves the current value of contrast.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_contrast the current contrast setting value.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_contrast_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_contrast, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_contrast</b> function is an application-defined
 *   callback function that sets contrast to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_contrast contrast value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_contrast)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_contrast, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_contrast_2</b> function is an application-defined
 *   callback function that sets contrast to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_contrast contrast value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_contrast_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_contrast, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_sharpness</b> function is an application-defined
 *   callback function that retrieves the current value of sharpness.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_sharpness the current sharpness value.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_sharpness)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_sharpness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_sharpness_2</b> function is an application-defined
 *   callback function that retrieves the current value of sharpness.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_sharpness the current sharpness value.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_sharpness_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_sharpness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_sharpness</b> function is an application-defined
 *   callback function that sets sharpness to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_sharpness sharpness value to be set.
 *   Its value ranges from a minimum of 0 to some device specific maximum.
 *   A numerical change of 1 corresponds to the smallest incremental change that is
 *   supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_sharpness)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_sharpness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_sharpness_2</b> function is an application-defined
 *   callback function that sets sharpness to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_sharpness sharpness value to be set.
 *   Its value ranges from a minimum of 0 to some device specific maximum.
 *   A numerical change of 1 corresponds to the smallest incremental change that is
 *   supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_sharpness_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_sharpness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_red_video_gain</b> function is an application-defined
 *   callback function that retrieves the current value of red gain control.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_red_video_gain the current setting of the red gain control
 *    for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_red_video_gain)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_red_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_red_video_gain_2</b> function is an application-defined
 *   callback function that retrieves the current value of red gain control.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_red_video_gain the current setting of the red gain control
 *    for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_red_video_gain_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_red_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_red_video_gain</b> function is an application-defined
 *   callback function that sets the value of red gain control to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_red_video_gain red gain control value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_red_video_gain)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_red_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_red_video_gain_2</b> function is an application-defined
 *   callback function that sets the value of red gain control to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_red_video_gain red gain control value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_red_video_gain_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_red_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_green_video_gain</b> function is an application-defined
 *   callback function that retrieves the current value of green gain control.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_green_video_gain the current setting of the green gain control
 *    for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_green_video_gain)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_green_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_green_video_gain_2</b> function is an application-defined
 *   callback function that retrieves the current value of green gain control.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_green_video_gain the current setting of the green gain control
 *    for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_green_video_gain_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_green_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_green_video_gain</b> function is an application-defined
 *   callback function that sets the value of green gain control to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_green_video_gain green gain control value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_green_video_gain)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_green_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_green_video_gain_2</b> function is an application-defined
 *   callback function that sets the value of green gain control to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_green_video_gain green gain control value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_green_video_gain_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_green_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_blue_video_gain</b> function is an application-defined
 *   callback function that retrieves the current value of blue gain control.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_blue_video_gain the current setting of the blue gain control
 *    for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_blue_video_gain)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_blue_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_blue_video_gain_2</b> function is an application-defined
 *   callback function that retrieves the current value of blue gain control.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_blue_video_gain the current setting of the blue gain control
 *    for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_blue_video_gain_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_blue_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_blue_video_gain</b> function is an application-defined
 *   callback function that sets the value of blue gain control to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_blue_video_gain blue gain control value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_blue_video_gain)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_blue_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_blue_video_gain_2</b> function is an application-defined
 *   callback function that sets the value of blue gain control to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_blue_video_gain blue gain control value to be set.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that
 *    is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_blue_video_gain_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_blue_video_gain, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_red_video_black_level</b> function is an application-defined
 *   callback function that retrieves the current value of the minimum output
 *    intensity of red.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_red_video_black_level the current setting for the minimum output
 *    intensity of red for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_red_video_black_level)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_red_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_red_video_black_level_2</b> function is an application-defined
 *   callback function that retrieves the current value of the minimum output
 *    intensity of red.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_red_video_black_level the current setting for the minimum output
 *    intensity of red for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_red_video_black_level_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_red_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_red_video_black_level</b> function is an application-defined
 *   callback function that sets the value of the minimum output intensity of red
 *   to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_red_video_black_level the specified value for the minimum
 *    output intensity of red.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_red_video_black_level)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_red_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_red_video_black_level_2</b> function is an application-defined
 *   callback function that sets the value of the minimum output intensity of red
 *   to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_red_video_black_level the specified value for the minimum
 *    output intensity of red.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_red_video_black_level_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_red_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_green_video_black_level</b> function is an application-defined
 *   callback function that retrieves the current value of the minimum output
 *    intensity of green.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_green_video_black_level the current setting for the minimum output
 *    intensity of green for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_green_video_black_level)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_green_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_green_video_black_level_2</b> function is an application-defined
 *   callback function that retrieves the current value of the minimum output
 *    intensity of green.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_green_video_black_level the current setting for the minimum output
 *    intensity of green for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_green_video_black_level_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_green_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_green_video_black_level</b> function is an application-defined
 *   callback function that sets the value of the minimum output intensity of green
 *   to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_green_video_black_level the specified value for the minimum
 *    output intensity of green.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_green_video_black_level)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_green_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_green_video_black_level_2</b> function is an application-defined
 *   callback function that sets the value of the minimum output intensity of green
 *   to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_green_video_black_level the specified value for the minimum
 *    output intensity of green.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_green_video_black_level_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_green_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_blue_video_black_level</b> function is an application-defined
 *   callback function that retrieves the current value of the minimum output
 *    intensity of blue.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_blue_video_black_level the current setting for the minimum output
 *    intensity of blue for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_blue_video_black_level)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_blue_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_blue_video_black_level_2</b> function is an application-defined
 *   callback function that retrieves the current value of the minimum output
 *    intensity of blue.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_blue_video_black_level the current setting for the minimum output
 *    intensity of blue for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_blue_video_black_level_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_blue_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_blue_video_black_level</b> function is an application-defined
 *   callback function that sets the value of the minimum output intensity of blue
 *   to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_blue_video_black_level the specified value for the minimum
 *    output intensity of blue.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_blue_video_black_level)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_blue_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_blue_video_black_level_2</b> function is an application-defined
 *   callback function that sets the value of the minimum output intensity of blue
 *   to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_blue_video_black_level the specified value for the minimum
 *    output intensity of blue.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_blue_video_black_level_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_blue_video_black_level, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_color_temperature</b> function is an application-defined
 *   callback function that retrieves the current value of the color quality
 *      of white.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_color_temperature  the current setting for the color quality
 *      of white for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_color_temperature)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16* current_color_temperature, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_color_temperature_2</b> function is an application-defined
 *   callback function that retrieves the current value of the color quality
 *      of white.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_color_temperature  the current setting for the color quality
 *      of white for the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_get_color_temperature_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16* current_color_temperature, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_color_temperature</b> function is an application-defined
 *   callback function that sets the value of the color quality of white to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_color_temperature the specified value for the color quality of white.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_color_temperature)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint16 desired_color_temperature, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_color_temperature_2</b> function is an application-defined
 *   callback function that sets the value of the color quality of white to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_color_temperature the specified value for the color quality of white.
 *    Its value ranges from a minimum of 0 to some device specific maximum.
 *     A numerical change of 1 corresponds to the smallest incremental change that
 *     is supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_rcs_entity_set_color_temperature_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint16 desired_color_temperature, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_horizontal_keystone</b> function is an application-defined
 *   callback function that retrieves the current level of compensation for
 *    horizontal distortion.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_horizontal_keystone the current level of compensation for
 *    horizontal distortion of the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of horizontal keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.13.
 */
typedef du_bool (*dmrd_rcs_entity_get_horizontal_keystone)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_int16* current_horizontal_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_horizontal_keystone_2</b> function is an application-defined
 *   callback function that retrieves the current level of compensation for
 *    horizontal distortion.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_horizontal_keystone the current level of compensation for
 *    horizontal distortion of the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of horizontal keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.13.
 */
typedef du_bool (*dmrd_rcs_entity_get_horizontal_keystone_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_int16* current_horizontal_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_horizontal_keystone</b> function is an application-defined
 *   callback function that sets the the level of compensation for
 *    horizontal distortion to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_horizontal_keystone the specified level of compensation for horizontal
 *     distortion.
 *     Its value ranges from device-specific negative number to a device specific positive number.
 *     A numerical change of 1 corresponds to the smallest incremental change that is
 *     supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of horizontal keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.13.
 */
typedef du_bool (*dmrd_rcs_entity_set_horizontal_keystone)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_int16 desired_horizontal_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_horizontal_keystone_2</b> function is an application-defined
 *   callback function that sets the the level of compensation for
 *    horizontal distortion to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_horizontal_keystone the specified level of compensation for horizontal
 *     distortion.
 *     Its value ranges from device-specific negative number to a device specific positive number.
 *     A numerical change of 1 corresponds to the smallest incremental change that is
 *     supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of horizontal keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.13.
 */
typedef du_bool (*dmrd_rcs_entity_set_horizontal_keystone_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_int16 desired_horizontal_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_vertical_keystone</b> function is an application-defined
 *   callback function that retrieves the current level of compensation for vertical
 *    distortion.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_vertical_keystone the current level of compensation for vertical
 *    distortion of the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of vertical keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.14.
 */
typedef du_bool (*dmrd_rcs_entity_get_vertical_keystone)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_int16* current_vertical_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_vertical_keystone_2</b> function is an application-defined
 *   callback function that retrieves the current level of compensation for vertical
 *    distortion.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_vertical_keystone the current level of compensation for vertical
 *    distortion of the associated display device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of vertical keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.14.
 */
typedef du_bool (*dmrd_rcs_entity_get_vertical_keystone_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_int16* current_vertical_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_vertical_keystone</b> function is an application-defined
 *   callback function that sets the level of compensation for vertical
 *     distortion to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_vertical_keystone the specified level of compensation for vertical
 *     distortion.
 *     Its value ranges from device-specific negative number to a device specific positive number.
 *     A numerical change of 1 corresponds to the smallest incremental change that is
 *     supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of vertical keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.14.
 */
typedef du_bool (*dmrd_rcs_entity_set_vertical_keystone)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_int16 desired_vertical_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_vertical_keystone_2</b> function is an application-defined
 *   callback function that sets the level of compensation for vertical
 *     distortion to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] desired_vertical_keystone the specified level of compensation for vertical
 *     distortion.
 *     Its value ranges from device-specific negative number to a device specific positive number.
 *     A numerical change of 1 corresponds to the smallest incremental change that is
 *     supported by the device.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information of vertical keystone,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.14.
 */
typedef du_bool (*dmrd_rcs_entity_set_vertical_keystone_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_int16 desired_vertical_keystone, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_mute</b> function is an application-defined
 *   callback function that retrieves the current value of the mute setting of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_mute the current mute setting of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_get_mute)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_bool* current_mute, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_mute_2</b> function is an application-defined
 *   callback function that retrieves the current value of the mute setting of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_mute the current mute setting of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_get_mute_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_bool* current_mute, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_mute</b> function is an application-defined
 *   callback function that sets mute of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_mute boolean variable specifies the mute setting.
 *   TRUE indicates that the output of the associated audio channel is currently muted
 *    (i.e. that channel is not producing any sound).
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_set_mute)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_bool desired_mute, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_mute_2</b> function is an application-defined
 *   callback function that sets mute of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_mute boolean variable specifies the mute setting.
 *   TRUE indicates that the output of the associated audio channel is currently muted
 *    (i.e. that channel is not producing any sound).
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_set_mute_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_bool desired_mute, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_volume</b> function is an application-defined
 *   callback function that retrieves the current value of the volume of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_volume the current volume setting of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_get_volume)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_uint16* current_volume, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_volume_2</b> function is an application-defined
 *   callback function that retrieves the current value of the volume of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_volume the current volume setting of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_get_volume_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_uint16* current_volume, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_volume</b> function is an application-defined
 *   callback function that sets volume of the specified channel to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_volume volume value to be set.
 *   Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that is
 *    supported by the device
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_set_volume)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_uint16 desired_volume, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_volume_2</b> function is an application-defined
 *   callback function that sets volume of the specified channel to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_volume volume value to be set.
 *   Its value ranges from a minimum of 0 to some device specific maximum.
 *    A numerical change of 1 corresponds to the smallest incremental change that is
 *    supported by the device
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.
 */
typedef du_bool (*dmrd_rcs_entity_set_volume_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_uint16 desired_volume, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_volume_db</b> function is an application-defined
 *   callback function that retrieves the current value of the volume in decibel (dB) of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_volume_db the current volume setting in units of 1/256 of a decibel (dB)
 *     of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 *    For more information about volume db,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.17.
 */
typedef du_bool (*dmrd_rcs_entity_get_volume_db)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_int16* current_volume_db, const du_uchar** error_code, du_uchar_array* error_description);


/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_volume_db_2</b> function is an application-defined
 *   callback function that retrieves the current value of the volume in decibel (dB) of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_volume_db the current volume setting in units of 1/256 of a decibel (dB)
 *     of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 *    For more information about volume db,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.17.
 */
typedef du_bool (*dmrd_rcs_entity_get_volume_db_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_int16* current_volume_db, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_volume_db</b> function is an application-defined
 *   callback function that sets the volume in decibel (dB) of the specified channel to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_volume_db volume setting value in units of 1/256 of a decibel (dB)
 *     of the associated audio channel.
 *     The value range for this variable is a minimum value of -32,767 (which equals -127.9961dB)
 *      and a maximum value of +32,767 (which equals +127.9961dB).
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 *    For more information about volume db,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.17.
 */
typedef du_bool (*dmrd_rcs_entity_set_volume_db)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_int16 desired_volume_db, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_volume_db_2</b> function is an application-defined
 *   callback function that sets the volume in decibel (dB) of the specified channel to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_volume_db volume setting value in units of 1/256 of a decibel (dB)
 *     of the associated audio channel.
 *     The value range for this variable is a minimum value of -32,767 (which equals -127.9961dB)
 *      and a maximum value of +32,767 (which equals +127.9961dB).
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 *    For more information about volume db,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.17.
 */
typedef du_bool (*dmrd_rcs_entity_set_volume_db_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_int16 desired_volume_db, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_volume_db_range</b> function is an application-defined
 *   callback function that retrieves the valid range for the volume in decibel (dB) of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] min_value minimum value of volume db in units of 1/256 dB.
 * @param[out] max_value maximum value of volume db in units of 1/256 dB.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 *    For more information about volume db,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.17.
 */
typedef du_bool (*dmrd_rcs_entity_get_volume_db_range)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_int16* min_value, du_int16* max_value, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_volume_db_range_2</b> function is an application-defined
 *   callback function that retrieves the valid range for the volume in decibel (dB) of the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] min_value minimum value of volume db in units of 1/256 dB.
 * @param[out] max_value maximum value of volume db in units of 1/256 dB.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 *    For more information about volume db,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.17.
 */
typedef du_bool (*dmrd_rcs_entity_get_volume_db_range_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_int16* min_value, du_int16* max_value, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_loudness</b> function is an application-defined
 *   callback function that retrieves the current value of the loudness setting of
 *   the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_loudness the current loudness setting of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 */
typedef du_bool (*dmrd_rcs_entity_get_loudness)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_bool* current_loudness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_get_loudness_2</b> function is an application-defined
 *   callback function that retrieves the current value of the loudness setting of
 *   the specified channel.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[out] current_loudness the current loudness setting of the associated audio channel.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 */
typedef du_bool (*dmrd_rcs_entity_get_loudness_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_bool* current_loudness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_loudness</b> function is an application-defined
 *   callback function that sets the loudness of the channel to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_loudness boolean variable to set loudness setting of the associated
 *     audio channel. A value of TRUE indicates that the loudness effect is active.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 */
typedef du_bool (*dmrd_rcs_entity_set_loudness)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* channel, du_bool desired_loudness, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_rcs_entity_set_loudness_2</b> function is an application-defined
 *   callback function that sets the loudness of the channel to the specified value.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] channel a channel of an audio output stream.
 * @param[in] desired_loudness boolean variable to set loudness setting of the associated
 *     audio channel. A value of TRUE indicates that the loudness effect is active.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark For more information about channel,
 *   see  http://www.upnp.org/standardizeddcps/documents/RenderingControl1.0.pdf section 2.2.19
 *     and table 2-15.<br>
 */
typedef du_bool (*dmrd_rcs_entity_set_loudness_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* channel, du_bool desired_loudness, const du_uchar** error_code, du_uchar_array* error_description);

/*
 * Returns the interface name of dmrd_rcs_entity_list_presets.
 * @return the interface name of dmrd_rcs_entity_list_presets.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_list_presets(void);

/*
 * Returns the interface name of dmrd_rcs_entity_list_presets_2.
 * @return the interface name of dmrd_rcs_entity_list_presets_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_list_presets_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_select_preset.
 * @return the interface name of dmrd_rcs_entity_select_preset.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_select_preset(void);

/*
 * Returns the interface name of dmrd_rcs_entity_select_preset_2.
 * @return the interface name of dmrd_rcs_entity_select_preset_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_select_preset_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_brightness.
 * @return the interface name of dmrd_rcs_entity_get_brightness.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_brightness(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_brightness_2.
 * @return the interface name of dmrd_rcs_entity_get_brightness_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_brightness_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_brightness.
 * @return the interface name of dmrd_rcs_entity_set_brightness.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_brightness(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_brightness_2.
 * @return the interface name of dmrd_rcs_entity_set_brightness_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_brightness_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_contrast.
 * @return the interface name of dmrd_rcs_entity_get_contrast.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_contrast(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_contrast_2.
 * @return the interface name of dmrd_rcs_entity_get_contrast_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_contrast_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_contrast.
 * @return the interface name of dmrd_rcs_entity_set_contrast.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_contrast(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_contrast_2.
 * @return the interface name of dmrd_rcs_entity_set_contrast_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_contrast_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_sharpness.
 * @return the interface name of dmrd_rcs_entity_get_sharpness.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_sharpness(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_sharpness_2.
 * @return the interface name of dmrd_rcs_entity_get_sharpness_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_sharpness_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_sharpness.
 * @return the interface name of dmrd_rcs_entity_set_sharpness.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_sharpness(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_sharpness_2.
 * @return the interface name of dmrd_rcs_entity_set_sharpness_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_sharpness_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_red_video_gain.
 * @return the interface name of dmrd_rcs_entity_get_red_video_gain.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_red_video_gain(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_red_video_gain_2.
 * @return the interface name of dmrd_rcs_entity_get_red_video_gain_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_red_video_gain_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_red_video_gain.
 * @return the interface name of dmrd_rcs_entity_set_red_video_gain.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_red_video_gain(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_red_video_gain_2.
 * @return the interface name of dmrd_rcs_entity_set_red_video_gain_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_red_video_gain_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_green_video_gain.
 * @return the interface name of dmrd_rcs_entity_get_green_video_gain.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_green_video_gain(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_green_video_gain_2.
 * @return the interface name of dmrd_rcs_entity_get_green_video_gain_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_green_video_gain_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_green_video_gain.
 * @return the interface name of dmrd_rcs_entity_set_green_video_gain.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_green_video_gain(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_green_video_gain_2.
 * @return the interface name of dmrd_rcs_entity_set_green_video_gain_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_green_video_gain_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_blue_video_gain.
 * @return the interface name of dmrd_rcs_entity_get_blue_video_gain.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_blue_video_gain(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_blue_video_gain_2.
 * @return the interface name of dmrd_rcs_entity_get_blue_video_gain_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_blue_video_gain_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_blue_video_gain.
 * @return the interface name of dmrd_rcs_entity_set_blue_video_gain.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_blue_video_gain(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_blue_video_gain_2.
 * @return the interface name of dmrd_rcs_entity_set_blue_video_gain_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_blue_video_gain_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_red_video_black_level.
 * @return the interface name of dmrd_rcs_entity_get_red_video_black_level.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_red_video_black_level(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_red_video_black_level_2.
 * @return the interface name of dmrd_rcs_entity_get_red_video_black_level_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_red_video_black_level_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_red_video_black_level.
 * @return the interface name of dmrd_rcs_entity_set_red_video_black_level.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_red_video_black_level(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_red_video_black_level_2.
 * @return the interface name of dmrd_rcs_entity_set_red_video_black_level_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_red_video_black_level_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_green_video_black_level.
 * @return the interface name of dmrd_rcs_entity_get_green_video_black_level.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_green_video_black_level(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_green_video_black_level_2.
 * @return the interface name of dmrd_rcs_entity_get_green_video_black_level_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_green_video_black_level_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_green_video_black_level.
 * @return the interface name of dmrd_rcs_entity_set_green_video_black_level.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_green_video_black_level(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_green_video_black_level_2.
 * @return the interface name of dmrd_rcs_entity_set_green_video_black_level_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_green_video_black_level_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_blue_video_black_level.
 * @return the interface name of dmrd_rcs_entity_get_blue_video_black_level.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_blue_video_black_level(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_blue_video_black_level_2.
 * @return the interface name of dmrd_rcs_entity_get_blue_video_black_level_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_blue_video_black_level_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_blue_video_black_level.
 * @return the interface name of dmrd_rcs_entity_set_blue_video_black_level.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_blue_video_black_level(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_blue_video_black_level_2.
 * @return the interface name of dmrd_rcs_entity_set_blue_video_black_level_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_blue_video_black_level_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_color_temperature.
 * @return the interface name of dmrd_rcs_entity_get_color_temperature.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_color_temperature(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_color_temperature_2.
 * @return the interface name of dmrd_rcs_entity_get_color_temperature_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_color_temperature_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_color_temperature.
 * @return the interface name of dmrd_rcs_entity_set_color_temperature.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_color_temperature(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_color_temperature_2.
 * @return the interface name of dmrd_rcs_entity_set_color_temperature_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_color_temperature_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_horizontal_keystone.
 * @return the interface name of dmrd_rcs_entity_get_horizontal_keystone.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_horizontal_keystone(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_horizontal_keystone_2.
 * @return the interface name of dmrd_rcs_entity_get_horizontal_keystone_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_horizontal_keystone_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_horizontal_keystone.
 * @return the interface name of dmrd_rcs_entity_set_horizontal_keystone.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_horizontal_keystone(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_horizontal_keystone_2.
 * @return the interface name of dmrd_rcs_entity_set_horizontal_keystone_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_horizontal_keystone_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_vertical_keystone.
 * @return the interface name of dmrd_rcs_entity_get_vertical_keystone.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_vertical_keystone(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_vertical_keystone_2.
 * @return the interface name of dmrd_rcs_entity_get_vertical_keystone_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_vertical_keystone_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_vertical_keystone.
 * @return the interface name of dmrd_rcs_entity_set_vertical_keystone.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_vertical_keystone(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_vertical_keystone_2.
 * @return the interface name of dmrd_rcs_entity_set_vertical_keystone_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_vertical_keystone_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_mute.
 * @return the interface name of dmrd_rcs_entity_get_mute.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_mute(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_mute_2.
 * @return the interface name of dmrd_rcs_entity_get_mute_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_mute_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_mute.
 * @return the interface name of dmrd_rcs_entity_set_mute.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_mute(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_mute_2.
 * @return the interface name of dmrd_rcs_entity_set_mute_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_mute_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_volume.
 * @return the interface name of dmrd_rcs_entity_get_volume.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_volume(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_volume_2.
 * @return the interface name of dmrd_rcs_entity_get_volume_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_volume_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_volume.
 * @return the interface name of dmrd_rcs_entity_set_volume.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_volume(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_volume_2.
 * @return the interface name of dmrd_rcs_entity_set_volume_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_volume_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_volume_db.
 * @return the interface name of dmrd_rcs_entity_get_volume_db.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_volume_db(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_volume_db_2.
 * @return the interface name of dmrd_rcs_entity_get_volume_db_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_volume_db_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_volume_db.
 * @return the interface name of dmrd_rcs_entity_set_volume_db.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_volume_db(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_volume_db_2.
 * @return the interface name of dmrd_rcs_entity_set_volume_db_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_volume_db_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_volume_db_range.
 * @return the interface name of dmrd_rcs_entity_get_volume_db_range.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_volume_db_range(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_volume_db_range_2.
 * @return the interface name of dmrd_rcs_entity_get_volume_db_range_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_volume_db_range_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_loudness.
 * @return the interface name of dmrd_rcs_entity_get_loudness.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_loudness(void);

/*
 * Returns the interface name of dmrd_rcs_entity_get_loudness_2.
 * @return the interface name of dmrd_rcs_entity_get_loudness_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_get_loudness_2(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_loudness.
 * @return the interface name of dmrd_rcs_entity_set_loudness.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_loudness(void);

/*
 * Returns the interface name of dmrd_rcs_entity_set_loudness_2.
 * @return the interface name of dmrd_rcs_entity_set_loudness_2.
 */
extern const du_uchar* dmrd_rcs_entity_interface_name_set_loudness_2(void);

#ifdef __cplusplus
}
#endif

#endif

