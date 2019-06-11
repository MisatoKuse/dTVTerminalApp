/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */

#ifndef MEDIA_MPEG_ES2PES_H
#define MEDIA_MPEG_ES2PES_H

#include <du_type_os.h>
#include <tr_util.h>
#include <tr_util_rename.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool media_mpeg_es2pes_create_pes_header(du_uint32 es_size, du_uint8 stream_id, du_uint64 pts_27mhz, du_uint64* dts_27mhz, tru_uint8_array* header);

typedef enum {
    MEDIA_MPEG_CAPTION_CONVERSION_TYPE_HD_SIDE_PANEL = 1,
    MEDIA_MPEG_CAPTION_CONVERSION_TYPE_SD_4_3 = 2,
    MEDIA_MPEG_CAPTION_CONVERSION_TYPE_SD_WIDE_SIDE_PANEL = 3,
    MEDIA_MPEG_CAPTION_CONVERSION_TYPE_MOBILE_CLOSED_CAPTION = 4
} media_mpeg_caption_conversion_type;

typedef enum {
    MEDIA_MPEG_DRCS_CONVERSION_TYPE_DRCS_CONVERSION_MODE_A = 0,
    MEDIA_MPEG_DRCS_CONVERSION_TYPE_DRCS_CONVERSION_MODE_B = 1,
    MEDIA_MPEG_DRCS_CONVERSION_TYPE_MOBLIE_DRCS = 2,
    MEDIA_MPEG_DRCS_CONVERSION_TYPE_DRCS_CONVERSION_NOT_POSSIBLE = 3
} media_mpeg_drcs_conversion_type;

extern du_bool media_mpeg_es2pes_create_pes_header_for_ancillary_data(du_uint32 es_size, du_uint8 stream_id, du_uint64 pts_27mhz, media_mpeg_caption_conversion_type cct, media_mpeg_drcs_conversion_type dct, tru_uint8_array* header);

#ifdef __cplusplus
}
#endif

#endif
