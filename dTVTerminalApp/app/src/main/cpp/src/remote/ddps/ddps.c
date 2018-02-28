/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#include <ddps.h>
#include <ddps_urn.h>
#include <dupnp_type.h>
#include <du_soap.h>
#include <du_xml.h>
#include <du_str.h>

const du_uchar* ddps_action_name_prepare_registration() {
    return DU_UCHAR_CONST("PrepareRegistion");
}

const du_uchar* ddps_action_name_register_device() {
    return DU_UCHAR_CONST("RegisterDevice");
}

du_bool ddps_make_prepare_registration(du_uchar_array* xml, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("PrepareRegistion");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_request_start(xml, ddps_urn_dps(ver), action_name)) return 0;
    if (!du_soap_envelope_request_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_make_register_device(du_uchar_array* xml, const du_uchar* device_id, const du_uchar* device_name, const du_uchar* dtla_device_id_hash, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("RegisterDevice");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_request_start(xml, ddps_urn_dps(ver), action_name)) return 0;

    if (!du_xml_element_cat(xml, DU_UCHAR_CONST("DeviceID"), device_id)) return 0;

    if (!du_xml_element_cat(xml, DU_UCHAR_CONST("DeviceName"), device_name)) return 0;

    if (!du_xml_element_cat(xml, DU_UCHAR_CONST("DTLADeviceIDHash"), dtla_device_id_hash)) return 0;

    if (!du_soap_envelope_request_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_parse_register_device(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** device_id, const du_uchar** device_name, const du_uchar** dtla_device_id_hash) {

    if (!du_soap_envelope_parse_request(param_array, xml, xml_len)) return 0;

    *device_id = du_soap_param_array_get_value(param_array, DU_UCHAR_CONST("DeviceID"));
    if (!*device_id) return 0;

    *device_name = du_soap_param_array_get_value(param_array, DU_UCHAR_CONST("DeviceName"));
    if (!*device_name) return 0;

    *dtla_device_id_hash = du_soap_param_array_get_value(param_array, DU_UCHAR_CONST("DTLADeviceIDHash"));
    if (!*dtla_device_id_hash) return 0;

    return 1;
}

du_bool ddps_make_register_device_response(du_uchar_array* xml, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("RegisterDevice");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_response_start(xml, ddps_urn_dps(ver), action_name)) return 0;

    if (!du_soap_envelope_response_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_parse_prepare_registration_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array) {

    if (!du_soap_envelope_parse_response(param_array, xml, xml_len)) return 0;

    return 1;
}

du_bool ddps_parse_register_device_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array) {

    if (!du_soap_envelope_parse_response(param_array, xml, xml_len)) return 0;

    return 1;
}

const du_uchar* ddps_action_name_unregister_device() {
    return DU_UCHAR_CONST("UnregisterDevice");
}

du_bool ddps_make_unregister_device(du_uchar_array* xml, const du_uchar* device_id, const du_uchar* dtla_device_id_hash, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("UnregisterDevice");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_request_start(xml, ddps_urn_dps(ver), action_name)) return 0;

    if (!du_xml_element_cat(xml, DU_UCHAR_CONST("DeviceID"), device_id)) return 0;

    if (!du_xml_element_cat(xml, DU_UCHAR_CONST("DTLADeviceIDHash"), dtla_device_id_hash)) return 0;

    if (!du_soap_envelope_request_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_parse_unregister_device(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** device_id, const du_uchar** dtla_device_id_hash) {

    if (!du_soap_envelope_parse_request(param_array, xml, xml_len)) return 0;

    *device_id = du_soap_param_array_get_value(param_array, DU_UCHAR_CONST("DeviceID"));
    if (!*device_id) return 0;

    *dtla_device_id_hash = du_soap_param_array_get_value(param_array, DU_UCHAR_CONST("DTLADeviceIDHash"));
    if (!*dtla_device_id_hash) return 0;

    return 1;
}

du_bool ddps_make_unregister_device_response(du_uchar_array* xml, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("UnregisterDevice");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_response_start(xml, ddps_urn_dps(ver), action_name)) return 0;

    if (!du_soap_envelope_response_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_parse_unregister_device_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array) {

    if (!du_soap_envelope_parse_response(param_array, xml, xml_len)) return 0;

    return 1;
}

const du_uchar* ddps_action_name_get_registered_devices() {
    return DU_UCHAR_CONST("GetRegisteredDevices");
}

du_bool ddps_make_get_registered_devices(du_uchar_array* xml, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("GetRegisteredDevices");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_request_start(xml, ddps_urn_dps(ver), action_name)) return 0;

    if (!du_soap_envelope_request_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_parse_get_registered_devices(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array) {

    if (!du_soap_envelope_parse_request(param_array, xml, xml_len)) return 0;

    return 1;
}

du_bool ddps_make_get_registered_devices_response(du_uchar_array* xml, const du_uchar* devices, du_uint32 ver) {
    static const du_uchar* action_name = DU_UCHAR_CONST("GetRegisteredDevices");

    du_uchar_array_truncate(xml);
    if (!du_soap_envelope_start(xml)) return 0;
    if (!du_soap_envelope_response_start(xml, ddps_urn_dps(ver), action_name)) return 0;

    if (!du_xml_element_cat(xml, DU_UCHAR_CONST("Devices"), devices)) return 0;

    if (!du_soap_envelope_response_end(xml, action_name)) return 0;
    if (!du_soap_envelope_end(xml)) return 0;
    return 1;
}

du_bool ddps_parse_get_registered_devices_response(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** devices) {

    if (!du_soap_envelope_parse_response(param_array, xml, xml_len)) return 0;

    *devices = du_soap_param_array_get_value(param_array, DU_UCHAR_CONST("Devices"));
    if (!*devices) return 0;

    return 1;
}


const du_uchar* ddps_error_code_device_already_registered() {
    return DU_UCHAR_CONST("800");
}

const du_uchar* ddps_error_code_device_not_registered() {
    return DU_UCHAR_CONST("801");
}

const du_uchar* ddps_error_code_no_space_left() {
    return DU_UCHAR_CONST("802");
}
