/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_SERVICE_ADAPTER_COMPONENT_ARRAY_H
#define DUPNP_DVC_SERVICE_ADAPTER_COMPONENT_ARRAY_H

#include <dupnp_dvc_service_adapter_component.h>
#include <du_type.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dupnp_dvc_service_adapter_component_array;

#define dupnp_dvc_service_adapter_component_array_init(x) du_array_init((x), sizeof(dupnp_dvc_service_adapter_component))

#define dupnp_dvc_service_adapter_component_array_allocate(x, len) du_array_allocate((x), (len))

#define dupnp_dvc_service_adapter_component_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dupnp_dvc_service_adapter_component_array_get(x) ((dupnp_dvc_service_adapter_component*)du_array_get(x))

#define dupnp_dvc_service_adapter_component_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dupnp_dvc_service_adapter_component_array_find_name(const dupnp_dvc_service_adapter_component_array* x, const du_uchar* name);

#define dupnp_dvc_service_adapter_component_array_length(x) du_array_length(x)

#define dupnp_dvc_service_adapter_component_array_bytes(x) du_array_bytes(x)

#define dupnp_dvc_service_adapter_component_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dupnp_dvc_service_adapter_component_array_truncate_length_object(dupnp_dvc_service_adapter_component_array* x, du_uint32 len);

#define dupnp_dvc_service_adapter_component_array_truncate(x) du_array_truncate(x)

extern du_bool dupnp_dvc_service_adapter_component_array_truncate_object(dupnp_dvc_service_adapter_component_array* x);

#define dupnp_dvc_service_adapter_component_array_free(x) du_array_free(x)

extern void dupnp_dvc_service_adapter_component_array_free_object(dupnp_dvc_service_adapter_component_array* x);

#define dupnp_dvc_service_adapter_component_array_failed(x) du_array_failed(x)

#define dupnp_dvc_service_adapter_component_array_equal(x, y) du_array_equal((x), (y))

#define dupnp_dvc_service_adapter_component_array_cat(to, from) du_array_cat((to), (from))

#define dupnp_dvc_service_adapter_component_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

#define dupnp_dvc_service_adapter_component_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

#define dupnp_dvc_service_adapter_component_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dupnp_dvc_service_adapter_component_array_remove_object(dupnp_dvc_service_adapter_component_array* x, du_uint32 pos);

extern du_bool dupnp_dvc_service_adapter_component_array_sort(dupnp_dvc_service_adapter_component_array* x, const du_str_array* component_order);

#ifdef __cplusplus
}
#endif

#endif
