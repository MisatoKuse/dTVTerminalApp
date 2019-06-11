/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_SERVICE_DESC_H
#define DUPNP_DVC_SERVICE_DESC_H

#include <dupnp_dvc_service.h>
#include <dupnp_dvc.h>

#ifdef __cplusplus
extern "C" {
#endif

extern dupnp_dvc_service_interface* dupnp_dvc_service_desc_get_service_interface(void);

extern const du_uchar* dupnp_dvc_service_desc_get_service_id(void);

extern void dupnp_dvc_service_desc_set_substitution_params_set_hook(dupnp_dvc_service_info* si, dupnp_dvc_dvcdsc_substitution_params_set_hook hook, void* arg);

#ifdef __cplusplus
}
#endif

#endif
