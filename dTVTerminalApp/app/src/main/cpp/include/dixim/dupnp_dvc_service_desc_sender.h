/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_SERVICE_DESC_SENDER_H
#define DUPNP_DVC_SERVICE_DESC_SENDER_H

#include <du_type.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_dvc_service_desc_sender_send(dupnp_dvc_context* context, const du_uchar* path, const du_str_array* translate_param_array, const du_str_array* response_header);

#ifdef __cplusplus
}
#endif

#endif

