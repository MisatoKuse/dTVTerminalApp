/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include <dupnp.h>
#include <dupnp_soap.h>
#include <du_alloc.h>
#include "ddps/ddps.h"
#include "ddps/ddps_urn.h"
#include <du_log.h>
#include <du_byte.h>
#include <du_http.h>
#include <du_str.h>

#include "../DlnaMacro.h"
#include "../DlnaDefine.h"
#include "local_registration.h"

static const du_uchar* LOG_TAG = DU_UCHAR_CONST("local_registration_register");

static void check_soap_response_error(dupnp_http_response* response, local_registration_error_info* ei, du_str_array* soap_param_array) {
    du_byte_zero((du_uint8*)ei, sizeof(local_registration_error_info));
    ei->socket_error = response->error;
    ei->http_status = response->status;

    if (response->error == DU_SOCKET_ERROR_CANCELED) {
        ei->type = LOCAL_REGISTRATION_ERROR_TYPE_CANCELED;
    } else if (response->error != DU_SOCKET_ERROR_NONE) {
        ei->type = LOCAL_REGISTRATION_ERROR_TYPE_SOCKET;
    } else if (!du_http_status_is_successful(response->status)) {
        ei->type = LOCAL_REGISTRATION_ERROR_TYPE_HTTP;
        if (du_str_equal(response->status, du_http_status_internal_server_error())) {
            if (dupnp_soap_parse_error_response(response->body, response->body_size, soap_param_array, &ei->soap_error_code, &ei->soap_error_description)) {
                if(du_str_equal(ei->soap_error_code, DU_UCHAR_CONST("401"))) {
                    ei->type = LOCAL_REGISTRATION_ERROR_TYPE_INVALID_ACTION;
                } else {
                    ei->type = LOCAL_REGISTRATION_ERROR_TYPE_SOAP;
                }
            } else {
                ei->type = LOCAL_REGISTRATION_ERROR_TYPE_OTHER;
            }
        } // du_str_equal(response->status
    } else {
        ei->type = LOCAL_REGISTRATION_ERROR_TYPE_NONE;
    }
}

static void prepare_registration_response_callback( dupnp_http_response* response, void* arg )
{
	local_registration_register_response_handler response_handler = (local_registration_register_response_handler)arg;
	local_registration_error_info ei;
	du_str_array param_array;
    
	du_log_dv(LOG_TAG, DU_UCHAR_CONST("register response: id = %u, method = %s, url = %s, error = %d, status = %s\n"),
		  response->id, response->method, response->url, response->error, response->status);
	du_log_dv(LOG_TAG, DU_UCHAR_CONST("register response: body = %s\n"), response->body);

	du_str_array_init(&param_array);

	check_soap_response_error(response, &ei, &param_array);

	if( ei.type == LOCAL_REGISTRATION_ERROR_TYPE_NONE ) {//ここが成功時点
		if( !ddps_parse_prepare_registration_response(response->body, response->body_size, &param_array) ) {
            ei.type = LOCAL_REGISTRATION_ERROR_TYPE_OTHER;
        }
    } else {
        LOG_WITH("!!!!!!!!!!!!!!!!!!!!!!!! エラー");
    }

	if( response_handler )
	{ (*response_handler)(response->id, &ei); }

	du_str_array_free(&param_array);
}

static void register_response_callback( dupnp_http_response* response, void* arg )
{
	local_registration_register_response_handler response_handler = (local_registration_register_response_handler)arg;
	local_registration_error_info ei;
	du_str_array param_array;

	du_log_dv(LOG_TAG, DU_UCHAR_CONST("register response: id = %u, method = %s, url = %s, error = %d, status = %s\n"),
		  response->id, response->method, response->url, response->error, response->status);
	du_log_dv(LOG_TAG, DU_UCHAR_CONST("register response: body = %s\n"), response->body);

	du_str_array_init(&param_array);

	check_soap_response_error(response, &ei, &param_array);

	if( ei.type == LOCAL_REGISTRATION_ERROR_TYPE_NONE )
	{
		if( !ddps_parse_register_device_response(response->body, response->body_size, &param_array) )
		{ ei.type = LOCAL_REGISTRATION_ERROR_TYPE_OTHER; }
	}

	if( response_handler )
	{ (*response_handler)(response->id, &ei); }

	du_str_array_free(&param_array);
}

extern du_bool local_registration_prepare_registration(dupnp* upnp, const du_uchar* user_agent, const du_uchar* control_url, local_registration_register_response_handler response_handler, du_uint32* id, du_uint32 ver) {
	du_bool result = 0;
	du_str_array request_header;
	du_uchar_array request_body;

	du_str_array_init(&request_header);
	du_uchar_array_init(&request_body);

	if( !dupnp_soap_header_set_content_type(&request_header))
	{ goto error; }

	if( !dupnp_soap_header_set_user_agent(&request_header, user_agent) )
	{ goto error; }

	if( !dupnp_soap_header_set_soapaction(&request_header, ddps_urn_dps(ver), ddps_action_name_prepare_registration()) )
	{ goto error; }

	if( !ddps_make_prepare_registration(&request_body, ver) )
	{ goto error; }

	du_uchar_array_cat0(&request_body);
//    du_log_dv(LOG_TAG, DU_UCHAR_CONST("request_header:\n%s\n"), du_uchar_array_get(&request_header));
	du_log_dv(LOG_TAG, DU_UCHAR_CONST("request:\n%s\n"), du_uchar_array_get(&request_body));

	result = dupnp_http_soap(
	 upnp,
	 control_url,
	 &request_header,
	 du_uchar_array_get(&request_body),
	 du_uchar_array_length(&request_body),
	 SOAP_TIMEOUT_MS,
	 prepare_registration_response_callback,
	 response_handler,
	 id );

error:

	du_str_array_free(&request_header);
	du_uchar_array_free(&request_body);
	return result;
}

du_bool local_registration_register(dupnp* upnp, const du_uchar* user_agent, const du_uchar* control_url, const du_uchar* device_id, const du_uchar* device_name, const du_uchar* dtla_device_id_hash, local_registration_register_response_handler response_handler, du_uint32* id, du_uint32  ver) {
	du_bool result = 0;
	du_str_array request_header;
	du_uchar_array request_body;

	du_str_array_init(&request_header);
	du_uchar_array_init(&request_body);

	if( !dupnp_soap_header_set_content_type(&request_header))
	{ goto error; }

	if( !dupnp_soap_header_set_user_agent(&request_header, user_agent) )
	{ goto error; }

	if( !dupnp_soap_header_set_soapaction(&request_header, ddps_urn_dps(ver), ddps_action_name_register_device()) )
	{ goto error; }

	if( !ddps_make_register_device(&request_body, device_id, device_name, dtla_device_id_hash, ver) )
	{ goto error; }

	du_uchar_array_cat0(&request_body);
	du_log_dv(LOG_TAG, DU_UCHAR_CONST("request:\n%s\n"), du_uchar_array_get(&request_body));

	result = dupnp_http_soap(
	 upnp,
	 control_url,
	 &request_header,
	 du_uchar_array_get(&request_body),
	 du_uchar_array_length(&request_body),
	 SOAP_TIMEOUT_MS,
	 register_response_callback,
	 response_handler,
	 id );

error:

	du_str_array_free(&request_header);
	du_uchar_array_free(&request_body);
	return result;
}

////////

static void unregister_response_callback( dupnp_http_response* response, void* arg ) {
	local_registration_register_response_handler response_handler = (local_registration_register_response_handler)arg;
	local_registration_error_info ei;
	du_str_array param_array;

	du_str_array_init(&param_array);

    du_log_dv(LOG_TAG, DU_UCHAR_CONST("unregister response: id = %u, method = %s, url = %s, error = %d, status = %s\n"),
            response->id, response->method, response->url, response->error, response->status);
    du_log_dv(LOG_TAG, DU_UCHAR_CONST("unregister response: body = %s\n"), response->body);

	check_soap_response_error(response, &ei, &param_array);

	if( ei.type == LOCAL_REGISTRATION_ERROR_TYPE_NONE ) {
		if( !ddps_parse_unregister_device_response(response->body, response->body_size, &param_array) ) {
            ei.type = LOCAL_REGISTRATION_ERROR_TYPE_OTHER;
        }
	}

	if( response_handler ) {
        (*response_handler)(response->id, &ei);
    }

	du_str_array_free(&param_array);
}

du_bool local_registration_unregister(dupnp* upnp, const du_uchar* user_agent, const du_uchar* control_url, const du_uchar* device_id, const du_uchar* dtla_device_id_hash, local_registration_register_response_handler response_handler, du_uint32* id, du_uint32 ver) {
	du_bool result = 0;
	du_str_array request_header;
	du_uchar_array request_body;

	du_str_array_init(&request_header);
	du_uchar_array_init(&request_body);

	if( !dupnp_soap_header_set_content_type(&request_header))
	{ goto error; }

	if( !dupnp_soap_header_set_user_agent(&request_header, user_agent) )
	{ goto error; }

	if( !dupnp_soap_header_set_soapaction(&request_header, ddps_urn_dps(ver), ddps_action_name_unregister_device()) )
	{ goto error; }

	if( !ddps_make_unregister_device(&request_body, device_id, dtla_device_id_hash, ver) )
	{ goto error; }

    du_uchar_array_cat0(&request_body);
    du_log_dv(LOG_TAG, DU_UCHAR_CONST("request:\n%s\n"), du_uchar_array_get(&request_body));
	result = dupnp_http_soap(
	 upnp,
	 control_url,
	 &request_header,
	 du_uchar_array_get(&request_body),
	 du_uchar_array_length(&request_body),
	 SOAP_TIMEOUT_MS,
	 unregister_response_callback,
	 response_handler,
	 id );

error:

	du_str_array_free(&request_header);
	du_uchar_array_free(&request_body);
	return result;
}

////////

static void get_registered_devices_response_callback( dupnp_http_response* response, void* arg )
{
	local_registration_get_registered_devices_response_handler response_handler = (local_registration_get_registered_devices_response_handler)arg;
	local_registration_error_info ei;
	du_str_array param_array;
	const du_uchar* devices = NULL;

    du_log_dv(LOG_TAG, DU_UCHAR_CONST("get_registered_devices response: id = %u, method = %s, url = %s, error = %d, status = %s\n"),
            response->id, response->method, response->url, response->error, response->status);
    du_log_dv(LOG_TAG, DU_UCHAR_CONST("get_registered_devices response: body = %s\n"), response->body);

	du_str_array_init(&param_array);

	check_soap_response_error(response, &ei, &param_array);

	if( ei.type == LOCAL_REGISTRATION_ERROR_TYPE_NONE ) {
		if( !ddps_parse_get_registered_devices_response(response->body, response->body_size, &param_array, &devices) ) {
            ei.type = LOCAL_REGISTRATION_ERROR_TYPE_OTHER;
        }
	}

	if( response_handler )
	{ (*response_handler)(response->id, &ei, devices); }

	du_str_array_free(&param_array);
}

du_bool local_registration_get_registered_devices(dupnp* upnp, const du_uchar* user_agent, const du_uchar* control_url, local_registration_get_registered_devices_response_handler response_handler, du_uint32* id, du_uint32  ver) {
	du_bool result = 0;
	du_str_array request_header;
	du_uchar_array request_body;

	du_str_array_init(&request_header);
	du_uchar_array_init(&request_body);

	if( !dupnp_soap_header_set_content_type(&request_header))
	{ goto error; }

	if( !dupnp_soap_header_set_user_agent(&request_header, user_agent) )
	{ goto error; }

	if( !dupnp_soap_header_set_soapaction(&request_header, ddps_urn_dps(ver), ddps_action_name_get_registered_devices()) )
	{ goto error; }

	if( !ddps_make_get_registered_devices(&request_body, ver) )
	{ goto error; }

    du_uchar_array_cat0(&request_body);
    du_log_dv(LOG_TAG, DU_UCHAR_CONST("local_registration_get_registered_devices request:\n%s\n"), du_uchar_array_get(&request_body));
	result = dupnp_http_soap(
	 upnp,
	 control_url,
	 &request_header,
	 du_uchar_array_get(&request_body),
	 du_uchar_array_length(&request_body),
	 SOAP_TIMEOUT_MS,
	 get_registered_devices_response_callback,
	 response_handler,
	 id );

error:

	du_str_array_free(&request_header);
	du_uchar_array_free(&request_body);
	return result;
}
