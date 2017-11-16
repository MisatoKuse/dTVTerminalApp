/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVCDSC_REWRITER_H
#define DUPNP_DVCDSC_REWRITER_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_dvcdsc_rewriter_rewrite_udn(const du_uchar* path);

extern du_bool dupnp_dvcdsc_rewriter_rewrite_element(const du_uchar* path, const du_uchar* udn, const du_uchar* name, const du_uchar* value);

#ifdef __cplusplus
}
#endif

#endif
