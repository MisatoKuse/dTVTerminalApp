/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_SERVICE_ADAPTER_COMPONENT_H
#define DUPNP_DVC_SERVICE_ADAPTER_COMPONENT_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_dvc_service_adapter_component {
    dupnp_dvc_service_query_interface qif;
    dupnp_dvc_service_info si;
    du_uchar* name;
} dupnp_dvc_service_adapter_component;

extern du_bool dupnp_dvc_service_adapter_component_init(dupnp_dvc_service_adapter_component* x, dupnp_dvc_service_query_interface qif, dupnp_dvc_service_info* si);

extern du_bool dupnp_dvc_service_adapter_component_start(dupnp_dvc_service_adapter_component* x);

extern void dupnp_dvc_service_adapter_component_stop(dupnp_dvc_service_adapter_component* x);

extern void dupnp_dvc_service_adapter_component_free(dupnp_dvc_service_adapter_component* x);

extern du_bool dupnp_dvc_service_adapter_component_append_initial_event_message(dupnp_dvc_service_adapter_component* x, du_uchar_array* property_set_xml);

extern du_bool dupnp_dvc_service_adapter_component_append_event_message(dupnp_dvc_service_adapter_component* x, du_uchar_array* property_set_xml);

#ifdef __cplusplus
}
#endif

#endif
