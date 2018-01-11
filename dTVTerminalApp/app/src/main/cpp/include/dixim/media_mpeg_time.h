/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: media_mpeg_time.h 3784 2009-05-18 05:42:52Z gondo $ 
 */ 
 
#ifndef MEDIA_MPEG_TIME_H
#define MEDIA_MPEG_TIME_H

#include <du_type_os.h>
#include <du_uint8_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* media_mpeg_time;

typedef enum {
    MEDIA_MPEG_TYPE_TTS,
} media_mpeg_type;

#define MEDIA_MPEG_TIME_INDEX_ENTRY_SIZE 4

extern du_bool media_mpeg_time_init(media_mpeg_time* mmt, media_mpeg_type type);
extern du_bool media_mpeg_time_parse(media_mpeg_time mmt, const du_uint8* data, du_uint32 size);
extern du_bool media_mpeg_time_parse_end(media_mpeg_time mmt);
extern du_bool media_mpeg_time_get_index(media_mpeg_time mmt, du_uint8_array** index);
extern void media_mpeg_time_get_interval_size(du_uint32* size);
extern void media_mpeg_time_get_duration_msec(media_mpeg_time mmt, du_uint32* msec);
extern void media_mpeg_time_free(media_mpeg_time* mmt);

#ifdef __cplusplus
}
#endif

#endif
