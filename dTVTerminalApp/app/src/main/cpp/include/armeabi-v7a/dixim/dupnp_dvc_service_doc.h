/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_SERVICE_DOC_H
#define DUPNP_DVC_SERVICE_DOC_H

#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

extern dupnp_dvc_service_interface* dupnp_dvc_service_doc_get_service_interface(void);

extern const du_uchar* dupnp_dvc_service_doc_get_service_id(void);

#ifdef __cplusplus
}
#endif

#endif
