/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DVCDSC_PARSER_H
#define DVCDSC_PARSER_H

#include "dvcdsc_device_array.h"

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dvcdsc_parser_parse(dvcdsc_device_array* device_array, const du_uchar* xml, du_uint32 xml_len, const du_uchar* location);

#ifdef __cplusplus
}
#endif

#endif
