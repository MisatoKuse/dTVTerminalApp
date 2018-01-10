/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: media_mpeg_pcr.h 3953 2009-06-17 09:33:29Z gondo $ 
 */ 
 
#ifndef MEDIA_MPEG_PCR_H
#define MEDIA_MPEG_PCR_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    du_uint32 _interval_ms;
    du_uint64 _pcr_ts_present_27mhz;
    du_uint64 _sync_ts_present_27mhz;
} media_mpeg_pcr;

extern void media_mpeg_pcr_init(du_uint32 interval_ms, du_uint64 sync_ts_base_27mhz, media_mpeg_pcr* pcr);
extern du_bool media_mpeg_check_pcr_interval(media_mpeg_pcr* pcr, du_uint64 sync_ts_27mhz);
extern void media_mpeg_get_and_reset_pcr(media_mpeg_pcr* pcr, du_uint64 sync_ts_27mhz, du_uint64* pcr_27mhz); 
extern void media_mpeg_pcr_create_pcr_ts_packet(du_uint64 pcr, du_uint16 pid, du_uint8 ts[188]);

#ifdef __cplusplus
}
#endif

#endif


