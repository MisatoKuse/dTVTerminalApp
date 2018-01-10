/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: media_mpeg_pes2ts.h 4062 2009-08-18 03:59:20Z gondo $
 */

#ifndef MEDIA_MPEG_PES2TS_H
#define MEDIA_MPEG_PES2TS_H

#include <du_type_os.h>
#include <tr_util.h>
#include <tr_util_rename.h>


#ifdef __cplusplus
extern "C" {
#endif

extern du_bool media_mpeg_pes2ts(du_uint8* src, du_uint32 src_size, du_uint16 pid, du_uint8* continuity_counter, tru_uint8_array* dst);
extern du_bool media_mpeg_pes2ts_2(du_uint8* src, du_uint32 src_size, du_uint16 pid, du_uint8* continuity_counter, tru_uint8_array* dst, du_bool padding_by_adaptation_field);
extern du_bool media_mpeg_section2ts(du_uint8* src, du_uint32 src_size, du_uint16 pid, du_uint8* continuity_counter, tru_uint8_array* dst);

#ifdef __cplusplus
}
#endif

#endif
