/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */ 
  
#ifndef MEDIA_MPEG_PARTIAL_TS_H
#define MEDIA_MPEG_PARTIAL_TS_H

#include <du_type_os.h>
#include <tr_util.h>
#include <tr_util_rename.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* media_mpeg_partial_ts;

extern du_bool media_mpeg_partial_ts_init(media_mpeg_partial_ts* mmpts);
extern void media_mpeg_partial_ts_free(media_mpeg_partial_ts mmpts);
extern void media_mpeg_partial_ts_set_data_es_enabled(media_mpeg_partial_ts mmpts, du_bool enabled);
extern void media_mpeg_partial_ts_set_dtcp_desc_enabled(media_mpeg_partial_ts mmpts, du_bool enabled);
extern du_bool media_mpeg_partial_ts_set_country_code(media_mpeg_partial_ts mmpts, const du_uchar* code);
extern du_bool media_mpeg_partial_ts_set_program_number(media_mpeg_partial_ts mmpts, du_uint16 program_number);
extern du_bool media_mpeg_partial_ts_set_network_id(media_mpeg_partial_ts mmpts, du_uint16 network_id);
extern du_bool media_mpeg_partial_ts_set_section(media_mpeg_partial_ts mmpts, const du_uint8* data, du_uint32 size);
extern du_bool media_mpeg_partial_ts_set_section_2(media_mpeg_partial_ts mmpts, const du_uint8* data, du_uint32 size, tru_uint8_array** packets);
extern du_bool media_mpeg_partial_ts_set_section_3(media_mpeg_partial_ts mmpts, du_uint8* data, du_uint32 size, tru_uint8_array** section);
extern du_bool media_mpeg_partial_ts_convert(media_mpeg_partial_ts mmpts, du_uint8 table_id);
extern du_bool media_mpeg_partial_ts_get_packets(media_mpeg_partial_ts mmpts, du_uint8 table_id, tru_uint8_array** packets);
extern du_bool media_mpeg_partial_ts_get_section(media_mpeg_partial_ts mmpts, du_uint8 table_id, tru_uint8_array** section);

#ifdef __cplusplus
}
#endif

#endif
