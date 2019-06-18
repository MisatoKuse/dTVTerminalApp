/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ts2tts.h 2550 2008-08-18 07:18:20Z gondo $
 */

#ifndef TS2TTS_H
#define TS2TTS_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* ts2tts;

extern du_bool ts2tts_init(ts2tts* t2t);
extern void ts2tts_free(ts2tts* t2t);
extern du_bool ts2tts_convert(ts2tts t2t, du_uint8** ts, du_uint32* ts_size, du_uint8** tts, du_uint32* tts_size);
extern du_bool ts2tts_convert_flush(ts2tts t2t, du_uint8** tts, du_uint32* tts_size);
extern void ts2tts_set_duration(ts2tts t2t, du_uint32 msec);
extern du_bool ts2tts_is_duration_end(ts2tts t2t);

#ifdef __cplusplus
}
#endif

#endif

