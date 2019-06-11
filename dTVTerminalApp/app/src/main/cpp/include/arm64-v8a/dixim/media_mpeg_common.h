/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 
 
#ifndef MEDIA_MPEG_COMMON_H
#define MEDIA_MPEG_COMMON_H

#include <du_type_os.h>
#include <tr_util.h>
#include <tr_util_rename.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum { // XXX: arib STD B10
    TABLE_ID_UNKNOWN = 0xff,
    TABLE_ID_PAT = 0x00,
    TABLE_ID_CAT = 0x01,
    TABLE_ID_PMT = 0x02,
    TABLE_ID_NIT = 0x40,
    TABLE_ID_NIT_OTHER = 0x41,
    TABLE_ID_SDT = 0x42,
    TABLE_ID_SDT_OTHER = 0x46,
    TABLE_ID_BAT = 0x4a,
    TABLE_ID_EIT = 0x4e,
    TABLE_ID_EIT_OTHER = 0x4f,
    TABLE_ID_EIT_SCHEDULE_MIN = 0x50,
    TABLE_ID_EIT_SCHEDULE_MAX = 0x5f,
    TABLE_ID_EIT_OTHER_SCHEDULE_MIN = 0x60,
    TABLE_ID_EIT_OTHER_SCHEDULE_MAX = 0x6f,
    TABLE_ID_TDT = 0x70,
    TABLE_ID_TOT = 0x73,
    TABLE_ID_DIT = 0x7e,
    TABLE_ID_SIT = 0x7f,
    TABLE_ID_BIT = 0xc4,
} table_id;

typedef enum { // XXX: arib STD B10
    PID_PAT = 0x0000,
    PID_CAT = 0x0001,
    PID_NIT = 0x0010,
    PID_SDT = 0x0011,
    PID_BAT = 0x0011,
    PID_EIT = 0x0012, // XXX: 
    PID_TDT = 0x0014,
    PID_TOT = 0x0014,
    PID_DIT = 0x001e,
    PID_SIT = 0x001f,
    PID_BIT = 0x0024,
} pid;

typedef enum {
    DESC_TAG_CONDITIONAL_ACCESS = 0x09,
    DESC_TAG_SERVICE = 0x48,
    DESC_TAG_SHORT_EVENT = 0x4d,
    DESC_TAG_EXTENDED_EVENT = 0x4e,
    DESC_TAG_COMPONENT = 0x50,
    DESC_TAG_STREAM_IDENTIFIER = 0x52,
    DESC_TAG_CONTENT = 0x54,
    DESC_TAG_PARENTAL_RATE = 0x55,
    DESC_TAG_LOCAL_TIME_OFFSET = 0x58,
    DESC_TAG_PARTIAL_TRANSPORT_STREAM = 0x63,
    DESC_TAG_BROADCAST_ID = 0x85,
    DESC_TAG_DTCP = 0x88,
    DESC_TAG_HIERARCHICAL_TRANSMISSION = 0xc0,
    DESC_TAG_DIGITAL_COPY_CONTROL = 0xc1,
    DESC_TAG_NETWORK_IDENTIFICATION = 0xc2,
    DESC_TAG_PARTIAL_TRANSPORT_STREAM_TIME = 0xc3,
    DESC_TAG_AUDIO_COMPONENT = 0xc4,
    DESC_TAG_HYPERLINK = 0xc5,
    DESC_TAG_TARGET_REGION = 0xc6,
    DESC_TAG_DATA_CONTENTS = 0xc7,
    DESC_TAG_VIDEO_DECODE_CONTROL = 0xc8,
    DESC_TAG_TS_INFORMATION = 0xcd,
    DESC_TAG_EXTENDED_BROADCASTER = 0xce,
    DESC_TAG_SERIES = 0xd5,
    DESC_TAG_EVENT_GROUP = 0xd6,
    DESC_TAG_BROADCASTER_NAME = 0xd8,
    DESC_TAG_COMPONENT_GROUP = 0xd9,
    DESC_TAG_CONTENT_AVAILABILITY = 0xde,
    DESC_TAG_URGENT_INFORMATION = 0xfc,
    DESC_TAG_DATA_COMPONENT = 0xfd,
} desc_tag;

typedef enum {
    STREAM_TYPE_MPEG1_VIDEO = 0x01,
    STREAM_TYPE_MPEG2_VIDEO = 0x02,
    STREAM_TYPE_MPEG2_SYSTEM_PES = 0x06,
    STREAM_TYPE_DSMCC = 0x0d,
    STREAM_TYPE_MPEG2_AAC = 0x0f,
    STREAM_TYPE_MPEG4_AVC = 0x1b,
} stream_type;

typedef enum {
    ARIB_CCI_CF = 0x00,
    ARIB_CCI_NMC = 0x02,
    ARIB_CCI_CN = 0x03,
} arib_cci;

#define TS_SIZE 188
#define TTS_SIZE 192
#define TS_HEADER_SIZE 4
#define TTS_HEADER_SIZE 4
#define SECTION_HEADER_SIZE 3
#define SECTION_EXTENDED_HEADER_SIZE 8
#define CRC32_SIZE 4
#define SYNC_BYTE 0x47
#define PAT_SECTION_MAX_LEN 1024
#define PMT_SECTION_MAX_LEN 1024
#define SIT_SECTION_MAX_LEN 4096
#define MALTI_SECTION_MAX_LEN 4096
#define PAYLOAD_MAX_LEN (MALTI_SECTION_MAX_LEN + TS_SIZE)
#define PAT_HEADER_SIZE SECTION_EXTENDED_HEADER_SIZE
#define PMT_HEADER_SIZE (SECTION_EXTENDED_HEADER_SIZE + 4)
#define SIT_HEADER_SIZE (SECTION_EXTENDED_HEADER_SIZE + 2)
#define PROGRAM_LOOP_LEN 4
#define TS_PAYLOAD_SIZE (TS_SIZE - TS_HEADER_SIZE)
#define POINTER_FIELD_SIZE 1
#define DESC_MAX_SIZE (2 + 0xff) 
#define COUNTRY_CODE_SIZE 3
#define START_TIME_SIZE 5
#define DURATION_SIZE 3
#define JST_TIME_SIZE 5

#define ROUND_UP(a, b) ((a) ? (((a) - 1) / (b) + 1) * (b) : 0)
#define ENTRY_SIZE(a) (sizeof((a)) / sizeof((a)[0]))

#define TTS_TIME_STAMP_CYCLE (27000000) // XXX: 27MHz
#define TTS_TIME_STAMP_MAX_TICKS (TTS_TIME_STAMP_CYCLE * 1) // XXX: 1s
#define INTERVAL_SIZE (4 * 1000 * 1000) // XXX: if bitrate == 16Mbps, INTERVAL_SIZE is 2 seconds data
#define TTS_TIME_STAMP_TICKS_PAR_MSEC (TTS_TIME_STAMP_CYCLE / 1000)

#define PARENTAL_RATE_UNDIFINED 0x00
#define PARENTAL_RATE_INVALID 0xff
#define SPTV_PARENTAL_RATE_MIN 0x01
#define SPTV_PARENTAL_RATE_MAX 0x11

extern du_bool media_mpeg_desc_get(du_uint8* descs, du_uint32 descs_size, du_uint8 tag, du_uint8** desc, du_uint32* size);
extern du_bool media_mpeg_desc_get_2(tru_uint8_array* descs, du_uint8 tag, du_uint8** desc, du_uint32* size);
extern du_bool media_mpeg_desc_get_3(du_uint8* descs, du_uint32 descs_size, du_uint8 tag, du_uint8** desc);
extern du_bool media_mpeg_desc_get_4(tru_uint8_array* descs, du_uint8 tag, du_uint8** desc);
extern du_bool media_mpeg_desc_remove(tru_uint8_array* ua, du_uint8 tag);

#ifdef __cplusplus
}
#endif

#endif
