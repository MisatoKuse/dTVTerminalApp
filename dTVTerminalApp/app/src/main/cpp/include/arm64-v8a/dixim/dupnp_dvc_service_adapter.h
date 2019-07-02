/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvc_service_adapter.h
 *  @brief The dupnp_dvc_service_adapter interface provides various methods for Adapter
 *  Service (such as getting query interface of adapter service and
 *  setting service component to adapter method).
 *  Adapter Service is one of implemented UPnP Device Services which is defined in dupnp_dvc_service.h
 *  Adapter Service enables to have a number of implemented Service Component which is defined
 *  in dupnp_dvc_service_component.h by calling <em>dupnp_dvc_service_adapter_set_component</em>.
 *  You can register a number of Service Components as a Service.
 *  It means that a number of Service Components enable to implement a UPnP Device Service.
 *  You can implement UPnP Device Services without Adapter Service, though it is easy to segmentalize
 *  Service implementation per a UPnP Device Service if you use Adapter Service.
 */

#ifndef DUPNP_DVC_SERVICE_ADAPTER_H
#define DUPNP_DVC_SERVICE_ADAPTER_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Sets a component's interface to the service specified by <em>service_id</em>.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] service_id service ID which is described in the device description xml.
 * @param[in] component_qif component interface.
 * @return true if the function succeeded. Otherwise false.
 */
extern du_bool dupnp_dvc_service_adapter_set_component(dupnp* upnp, const du_uchar* service_id, dupnp_dvc_service_query_interface component_qif);

/**
 * Gets an interface of service adapter.
 * @return the interface of service adapter.
 */
extern dupnp_dvc_service_query_interface dupnp_dvc_service_adapter_get_query_interface(void);

/**
 * Enables the function that callbacks the handler implemented by the components for
 * appending the event message.
 * If you enable this function, you need to implement
 *     dupnp_dvc_service_component_append_initial_event_message() and
 *     dupnp_dvc_service_component_append_event_message()
 * by at least one of the component registered in adapter.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] service_id  service ID which is described in the device description xml..
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark default status is set to disabled.
 * Do not use this function with dupnp_dvc_service_evtmgr_set_state_variable() in the same service.
 */
extern du_bool dupnp_dvc_service_adapter_enable_event_message_handler(dupnp* upnp, const du_uchar* service_id);

#ifdef __cplusplus
}
#endif

#endif
