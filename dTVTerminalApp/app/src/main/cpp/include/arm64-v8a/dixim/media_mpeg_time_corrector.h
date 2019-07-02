/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */

#ifndef MEDIA_MPEG_TIME_CORRECTOR_H
#define MEDIA_MPEG_TIME_CORRECTOR_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* media_mpeg_time_corrector;

extern du_bool media_mpeg_time_corrector_init(media_mpeg_time_corrector* mmtc);
extern du_bool media_mpeg_time_corrector_correct(media_mpeg_time_corrector mmtc, du_uint8* tts_packets, du_uint32 count);
extern void media_mpeg_time_corrector_free(media_mpeg_time_corrector mmtc);


#ifdef __cplusplus
}
#endif

#endif

