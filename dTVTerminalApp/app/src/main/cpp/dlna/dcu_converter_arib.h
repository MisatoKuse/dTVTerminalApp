/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DCU_CONVERTER_ARIB_H
#define DCU_CONVERTER_ARIB_H

#include "dcu_converter.h"

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dcu_converter_arib_convert_to_utf8(const du_uchar** in, du_uint32* in_bytes_left, du_uchar** out, du_uint32* out_bytes_left);

extern du_bool dcu_converter_arib_get_out_bytes(const du_uchar* in, du_uint32 in_bytes, du_uint32* out_bytes);

#ifdef __cplusplus
}
#endif

#endif
