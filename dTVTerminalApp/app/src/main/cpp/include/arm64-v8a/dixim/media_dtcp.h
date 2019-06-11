/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 */

#ifndef MEDIA_DTCP_H
#define MEDIA_DTCP_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool media_dtcp_is_mime_type_supported(const du_uchar* mime_type);

extern du_bool media_dtcp_is_extension_supported(const du_uchar* extension);

extern du_bool media_dtcp_is_pn_param_supported(const du_uchar* pn_param);

#ifdef __cplusplus
}
#endif

#endif
