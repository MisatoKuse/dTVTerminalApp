#ifndef LOCAL_REGISTRATION_H
#define LOCAL_REGISTRATION_H

#include <du_type.h>
#include <du_socket.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
   LOCAL_REGISTRATION_ERROR_TYPE_NONE,
   LOCAL_REGISTRATION_ERROR_TYPE_SOCKET,
   LOCAL_REGISTRATION_ERROR_TYPE_SOAP,
   LOCAL_REGISTRATION_ERROR_TYPE_HTTP,
   LOCAL_REGISTRATION_ERROR_TYPE_CANCELED,
   LOCAL_REGISTRATION_ERROR_TYPE_INVALID_ACTION,
   LOCAL_REGISTRATION_ERROR_TYPE_OTHER,
} local_registration_error_type;

typedef struct local_registration_error_info {
    local_registration_error_type type;
    du_socket_error socket_error;
    const du_uchar* soap_error_code;
    const du_uchar* soap_error_description;
    const du_uchar* http_status;
} local_registration_error_info;

typedef void(*local_registration_register_response_handler)(du_uint32 requeseted_id, local_registration_error_info* error_info);

extern du_bool local_registration_prepare_registration(
 dupnp* upnp,
 const du_uchar* user_agent,
 const du_uchar* control_url,
 local_registration_register_response_handler response_handler,
 du_uint32* id,
 du_uint32 version);

extern du_bool local_registration_register(
 dupnp* upnp,
 const du_uchar* user_agent,
 const du_uchar* control_url,
 const du_uchar* device_id,
 const du_uchar* device_name,
 const du_uchar* dtla_device_id_hash,
 local_registration_register_response_handler response_handler,
 du_uint32* id,
 du_uint32 version);

extern du_bool local_registration_unregister(
 dupnp* upnp,
 const du_uchar* user_agent,
 const du_uchar* control_url,
 const du_uchar* device_id,
 const du_uchar* dtla_device_id_hash,
 local_registration_register_response_handler response_handler,
 du_uint32* id,
 du_uint32 version);

typedef void(*local_registration_get_registered_devices_response_handler)(du_uint32 requeseted_id, local_registration_error_info* error_info, const du_uchar* devices);

extern du_bool local_registration_get_registered_devices(
 dupnp* upnp,
 const du_uchar* user_agent,
 const du_uchar* control_url,
 local_registration_get_registered_devices_response_handler response_handler,
 du_uint32* id,
 du_uint32 version);

#ifdef __cplusplus
}
#endif

#endif /* LOCAL_REGISTRATION_H */
