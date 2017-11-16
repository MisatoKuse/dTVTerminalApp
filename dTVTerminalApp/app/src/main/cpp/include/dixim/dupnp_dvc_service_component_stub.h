/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvc_service_component_stub.h
 *  @brief The dupnp_dvc_service_component_stub interface provides various methods for Stub Service Component
 * (such as getting query interface name for stub component, interface definition of stub compoment method).
 * These interfaces's implementation is necessary and sufficient condition to make Stub Service Component.
 * Stub Service Component is one which is extended from Service Component.
 * Stub Service Component mostly handle SOAP layer (e.g. parsing/makeing XML from/to UPnP Control Point)
 * Stub Service Component doesn't have implementation about business logic of UPnP Device Service.
 * Instead, Entity Service Componet has business logic.
 */


#ifndef DUPNP_DVC_SERVICE_COMPONENT_STUB_H
#define DUPNP_DVC_SERVICE_COMPONENT_STUB_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function for requesting action.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] context pointer to the dupnp_dvc_context structure.
 * @param[in] request pointer to the dupnp_dvc_service_upnp_control_request structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_stub_upnp_action)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request);

/**
 * An interface definition of a function for requesting query state variable.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] context pointer to the dupnp_dvc_context structure.
 * @param[in] request pointer to the dupnp_dvc_service_upnp_control_request structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_stub_upnp_query)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request);

/**
 * An interface definition of a function for checking specified action is supported.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] action_name action name to be checked.
 * @return  true if the action is supported. Otherwise false.
 */
typedef du_bool (*dupnp_dvc_service_component_stub_upnp_is_action_supported)(dupnp_dvc_service_info* info, const du_uchar* action_name);

/**
 * An interface definition of a function for checking specified state variable is available.
 * @param[in] info pointer to the dupnp_dvc_service_info structure.
 * @param[in] variable_name variable name to be checked.
 * @return  true if the variable name is available. Otherwise false.
 */
typedef du_bool (*dupnp_dvc_service_component_stub_upnp_is_state_variable_supported)(dupnp_dvc_service_info* info, const du_uchar* variable_name);

/**
 * Returns query interface name of the method for requesting action.
 * @return query interface name string for requesting action.
 */
extern const du_uchar* dupnp_dvc_service_component_stub_interface_name_upnp_action(void);

/**
 * Returns query interface name of the method for requesting query state variable.
 * @return query interface name string for requesting query state variable.
 */
extern const du_uchar* dupnp_dvc_service_component_stub_interface_name_upnp_query(void);

/**
 * Returns query interface name of the method for checking specified action is supported.
 * @return query interface name string for checking specified action is supported.
 */
extern const du_uchar* dupnp_dvc_service_component_stub_interface_name_upnp_is_action_supported(void);

/**
 * Returns query interface name of the method for checking specified state variable is available.
 * @return query interface name string for checking specified state variable is available.
 */
extern const du_uchar* dupnp_dvc_service_component_stub_interface_name_upnp_is_state_variable_supported(void);

#ifdef __cplusplus
}
#endif

#endif
