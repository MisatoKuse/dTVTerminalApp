/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvc_service_component_entity.h
 *  @brief The dupnp_dvc_service_component_entity interface provides various methods for Entity Service Component
 * (such as getting query interface name for entity component, interface definition of entity compoment method).
 * These interfaces's implementation is necessary and sufficient condition to make Entity Service Component.
 * Entity Service Component is one which is extended from Service Component.
 * Entity Service Component mostly handle business logic of UPnP Device Service
 * Engity Service Component doesn't handle SOAP layer (e.g. parsing/makeing XML from/to UPnP Control Point).
 * Instead, Stub Service Componet handle SOAP layer.
 */

#ifndef DUPNP_DVC_SERVICE_COMPONENT_ENTITY_H
#define DUPNP_DVC_SERVICE_COMPONENT_ENTITY_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function for UPnP Eventing:subscription request.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] context pointer to the dupnp_dvc_context structure.
 * @param[in] request pointer to the dupnp_dvc_service_upnp_accept_subscription_request structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_entity_upnp_accept_subscription)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_accept_subscription_request* request);

/**
 * An interface definition of a function for control request.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] context pointer to the dupnp_dvc_context structure.
 * @param[in] request pointer to the dupnp_dvc_service_upnp_control_request structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_entity_upnp_accept_control_request)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request);

/**
 * An interface definition of a function for getting state variable.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] variable_name variable name.
 * @param[out] variable_value variable value.
 * @param[out] error_code pointer to the storage location to receive the error_code
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_entity_upnp_get_state_variable)(dupnp_dvc_service_info* info, const du_uchar* variable_name, du_uchar_array* variable_value, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * Returns query interface name of the method for UPnP Eventing:subscription request.
 * @return query interface name string for UPnP Eventing:subscription request.
 */
extern const du_uchar* dupnp_dvc_service_component_entity_interface_name_upnp_accept_subscription(void);

/**
 * Returns query interface name of the method for control request.
 * @return query interface name string for UPnP control request.
 */
extern const du_uchar* dupnp_dvc_service_component_entity_interface_name_upnp_accept_control_request(void);

/**
 * Returns query interface name of the method for getting state variable.
 * @return query interface name string for getting state variables.
 */
extern const du_uchar* dupnp_dvc_service_component_entity_interface_name_upnp_get_state_variable(void);

#ifdef __cplusplus
}
#endif

#endif
