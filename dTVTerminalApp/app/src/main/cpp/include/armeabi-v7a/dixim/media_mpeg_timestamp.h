/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 
 
#ifndef MEDIA_MPEG_TIMESTAMP_H
#define MEDIA_MPEG_TIMESTAMP_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

#define MEDIA_MPEG_TIMESTAMP_INVALID DU_UINT64_CONST(0xffffffffffffffff)

extern void media_mpeg_timestamp_init_tmp_ts(du_uint64* temp_ts);
extern void media_mpeg_timestamp_calc_dts(du_uint64* tmp_ts, du_uint64* pts, du_uint64* dts);
extern void media_mpeg_timestamp_100ns_to_27mhz(du_uint64 _100ns, du_uint64* _27mhz);

#ifdef __cplusplus
}
#endif

#endif
