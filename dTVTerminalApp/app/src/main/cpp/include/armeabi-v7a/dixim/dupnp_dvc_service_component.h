/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvc_service_component.h
 *  @brief The dupnp_dvc_service_component interface provides various methods for Service Component
 *  (such as getting query interface name for component, interface definition of compoment method).
 *  Service Component is set by Service Adapter which is defined in dupnp_dvc_service_adapter.h.
 */

#ifndef DUPNP_DVC_SERVICE_COMPONENT_H
#define DUPNP_DVC_SERVICE_COMPONENT_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function for initializing the component.
 * @param[in] info  pointer to the dupnp_dvc_service_info structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_init)(dupnp_dvc_service_info* info);

/**
 * An interface definition of a function for starting the component.
 * @param[in] info  pointer to the dupnp_dvc_service_info structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_start)(dupnp_dvc_service_info* info);

/**
 * An interface definition of a function for stopping the component.
 * @param[in] info  pointer to the dupnp_dvc_service_info structure.
 */
typedef void (*dupnp_dvc_service_component_stop)(dupnp_dvc_service_info* info);

/**
 * An interface definition of a function for freeing resources of the component.
 * @param[in] info  pointer to the dupnp_dvc_service_info structure.
 */
typedef void (*dupnp_dvc_service_component_free)(dupnp_dvc_service_info* info);

/**
 * An interface definition of a function for getting a component name.
 * @return  pointer to the component name.
 *          false if the function fails.
 */
typedef const du_uchar* (*dupnp_dvc_service_component_get_component_name)(void);

/**
 * An interface definition of a function for appending a initial event message.
 * If you implement this interface in component, you need to enable the function that
 * callbacks event message handler by dupnp_dvc_service_adapter_enable_event_message_handler().
 * @param[in] info  pointer to the dupnp_dvc_service_info structure.
 * @param[in,out] property_set_xml  pointer to the initial event message.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_append_initial_event_message)(dupnp_dvc_service_info* info, du_uchar_array* property_set_xml);

/**
 * An interface definition of a function for appending a event message.
 * If you implement this interface in component, you need to enable the function that
 * callbacks event message handler by dupnp_dvc_service_adapter_enable_event_message_handler().
 * @param[in] info  pointer to the dupnp_dvc_service_info structure.
 * @param[in,out] property_set_xml  pointer to the initial event message.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_component_append_event_message)(dupnp_dvc_service_info* info, du_uchar_array* property_set_xml);

/**
 * An interface definition of a function to get the pointer to a query interface
 *  function for component.
 * @return pointer to a query interface function for component.
 */
typedef dupnp_dvc_service_query_interface (*dupnp_dvc_service_component_get_query_interface)(void);

/**
 * Returns query interface name of the method for initializing the component.
 * @return query interface name string for initializing the component.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_init(void);

/**
 * Returns query interface name of the method for for starting the component.
 * @return query interface name string for starting the component.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_start(void);

/**
 * Returns query interface name of the method for for stopping the component.
 * @return query interface name string for stopping the component.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_stop(void);

/**
 * Returns query interface name of the method for freeing resources of the component.
 * @return query interface name string for freeing resources of the component.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_free(void);

/**
 * Returns query interface name of the method for getting the component name.
 * @return query interface name string for getting the component name.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_get_component_name(void);

/**
 * Returns query interface name of the method for appending the initial event message name.
 * @return query interface name string for appending the initial event message name.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_append_initial_event_message(void);

/**
 * Returns query interface name of the method for appending the event message name.
 * @return query interface name string for appending the event message name.
 */
extern const du_uchar* dupnp_dvc_service_component_interface_name_append_event_message(void);

#ifdef __cplusplus
}
#endif

#endif
