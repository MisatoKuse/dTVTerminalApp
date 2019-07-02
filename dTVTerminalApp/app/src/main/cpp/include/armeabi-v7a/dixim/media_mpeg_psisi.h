/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */

#ifndef MEDIA_MPEG_PSISI_H
#define MEDIA_MPEG_PSISI_H

#include <du_type_os.h>
#include <du_uint16_array.h>
#include <tr_util.h>
#include <tr_util_rename.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    du_uint16 program_number;
    du_uint16 pid;
} media_mpeg_psisi_pat_loop;

typedef struct {
    du_uint8 table_id;
    du_uint16 transport_stream_id;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    tru_ptr_array loop;
} media_mpeg_psisi_pat;

typedef struct {
    du_uint8 stream_type;
    du_uint16 elementary_pid;
    tru_uint8_array descriptor;
} media_mpeg_psisi_pmt_es_info_loop;

#define COUNTRY_CODE_SIZE 3

typedef struct {
    du_uint8 table_id;
    du_uint16 program_number;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    du_uint16 pcr_pid;
    tru_uint8_array program_info_loop;
    tru_ptr_array es_info_loop;
    du_uchar country_code[COUNTRY_CODE_SIZE];
} media_mpeg_psisi_pmt;

#define START_TIME_SIZE 5
#define DURATION_SIZE 3

typedef struct {
    du_uint16 event_id;
    du_uint8 start_time[START_TIME_SIZE];
    du_uint8 duration[DURATION_SIZE];
    du_uint8 free_ca_mode;
    tru_uint8_array descriptor;
} media_mpeg_psisi_eit_loop;

typedef struct {
    du_uint8 table_id;
    du_uint16 service_id;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    du_uint16 transport_stream_id;
    du_uint16 original_network_id;
    du_uint8 segment_last_section_number;
    du_uint8 last_table_id;
    tru_ptr_array loop;
} media_mpeg_psisi_eit;

typedef struct {
    du_uint16 transport_stream_id;
    du_uint16 original_network_id;
    tru_uint8_array descriptor;
} media_mpeg_psisi_nit_transport_stream_loop;

typedef struct {
    du_uint16 service_id;
    du_uint8 eit_user_defined_flags;
    du_uint8 eit_schedule_flag;
    du_uint8 eit_present_following_flag;
    du_uint8 running_status;
    du_uint8 free_ca_mode;
    tru_uint8_array descriptor;
} media_mpeg_psisi_sdt_loop;

typedef struct {
    du_uint8 table_id;
    du_uint16 transport_stream_id;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    du_uint16 original_network_id;
    tru_ptr_array loop;
} media_mpeg_psisi_sdt;

typedef struct {
    du_uint8 table_id;
    du_uint16 network_id;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    tru_uint8_array network_descriptor;
    tru_ptr_array transport_stream_loop;
} media_mpeg_psisi_nit;

typedef struct {
    du_uint8 broadcaster_id;
    tru_uint8_array descriptor;
} media_mpeg_psisi_bit_broadcaster_loop;

typedef struct {
    du_uint8 table_id;
    du_uint16 original_network_id;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    tru_uint8_array descriptor;
    tru_ptr_array broadcaster_loop;
} media_mpeg_psisi_bit;

#define JST_TIME_SIZE 5

typedef struct {
    du_uint8 table_id;
    du_uint8 jst_time[JST_TIME_SIZE];
    tru_uint8_array descriptor;
} media_mpeg_psisi_tot;

typedef struct {
    du_uint16 service_id;
    tru_uint8_array descriptor;
} media_mpeg_psisi_sit_service_loop;

typedef struct {
    du_uint8 table_id;
    du_uint8 version_number;
    du_uint8 current_next_indicator;
    du_uint8 section_number;
    du_uint8 last_section_number;
    tru_uint8_array descriptor;
    tru_ptr_array service_loop;
    du_uchar country_code[COUNTRY_CODE_SIZE];
} media_mpeg_psisi_sit;

#define MM_PID_INVALID (0xffff)

extern du_bool media_mpeg_psisi_get_version_number(const du_uint8* data, du_uint32 size, du_uint8* version_number);
extern du_bool media_mpeg_psisi_get_current_next_indicator(const du_uint8* data, du_uint32 size, du_uint8* current_next_indicator);
extern du_bool media_mpeg_psisi_get_section_pid(media_mpeg_psisi_pat* pat, media_mpeg_psisi_pmt* pmt, du_uint8 table_id, du_uint16* pid);

extern void media_mpeg_psisi_pat_init(media_mpeg_psisi_pat* pat);
extern du_bool media_mpeg_psisi_pat_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_pat* pat);
extern void media_mpeg_psisi_pat_free(media_mpeg_psisi_pat* pat);
extern du_bool media_mpeg_psisi_pat_create(media_mpeg_psisi_pat* pat, tru_uint8_array* dst);
extern du_bool media_mpeg_psisi_pat_create_partial_ts(media_mpeg_psisi_pat* org_pat, du_uint16 program_number, media_mpeg_psisi_pat* pat);
extern du_bool media_mpeg_psisi_pat_get_pmt_pids(media_mpeg_psisi_pat* pat, du_uint16_array* pid);
extern du_bool media_mpeg_psisi_pat_get_pmt_pid(media_mpeg_psisi_pat* pat, du_uint16 program_number, du_uint16* pid);
extern du_bool media_mpeg_psisi_pat_get_program_number(media_mpeg_psisi_pat* pat, du_uint16_array* program_number);

extern void media_mpeg_psisi_pmt_init(media_mpeg_psisi_pmt* pmt);
extern du_bool media_mpeg_psisi_pmt_set_country_code(media_mpeg_psisi_pmt* pmt, const du_uchar* country_code);
extern du_bool media_mpeg_psisi_pmt_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_pmt* pmt);
extern void media_mpeg_psisi_pmt_free(media_mpeg_psisi_pmt* pmt);
extern du_bool media_mpeg_psisi_pmt_create(media_mpeg_psisi_pmt* pmt, tru_uint8_array* dst);
extern du_bool media_mpeg_psisi_pmt_create_partial_ts(media_mpeg_psisi_pmt* org_pmt, du_uint16 network_id, media_mpeg_psisi_pmt* pmt);
extern du_bool media_mpeg_psisi_pmt_create_partial_ts_2(media_mpeg_psisi_pmt* org_pmt, media_mpeg_psisi_nit* org_nit, media_mpeg_psisi_pmt* pmt);
extern du_bool media_mpeg_psisi_pmt_get_program_number(const du_uint8* data, du_uint32 size, du_uint16* program_number);
extern du_bool media_mpeg_psisi_pmt_get_es_pid(media_mpeg_psisi_pmt* pmt, du_uint8 stream_type, du_uint16* pid);
extern du_bool media_mpeg_psisi_pmt_update_dtcp_desc(media_mpeg_psisi_pmt* pmt);
extern du_bool media_mpeg_psisi_pmt_update_dtcp_desc_for_playready(media_mpeg_psisi_pmt* pmt, du_uint8 aps);
extern du_bool media_mpeg_psisi_pmt_remove_data_es(media_mpeg_psisi_pmt* pmt);
extern du_bool media_mpeg_psisi_pmt_check_match_pids(const du_uint8* data, du_uint32 size, du_uint16* pids, du_uint32 pids_count, du_uint16* program_number, du_bool* match);

extern void media_mpeg_psisi_sdt_init(media_mpeg_psisi_sdt* sdt);
extern du_bool media_mpeg_psisi_sdt_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_sdt* sdt);
extern void media_mpeg_psisi_sdt_free(media_mpeg_psisi_sdt* sdt);

extern void media_mpeg_psisi_nit_init(media_mpeg_psisi_nit* nit);
extern du_bool media_mpeg_psisi_nit_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_nit* nit);
extern void media_mpeg_psisi_nit_free(media_mpeg_psisi_nit* nit);

extern void media_mpeg_psisi_eit_init(media_mpeg_psisi_eit* eit);
extern du_bool media_mpeg_psisi_eit_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_eit* eit);
extern void media_mpeg_psisi_eit_free(media_mpeg_psisi_eit* eit);
extern du_bool media_mpeg_psisi_eit_get_service_id(const du_uint8* data, du_uint32 size, du_uint16* service_id);
extern du_bool media_mpeg_psisi_eit_get_section_number(const du_uint8* data, du_uint32 size, du_uint8* section_number);
extern du_bool media_mpeg_psisi_eit_get_event_ids(media_mpeg_psisi_eit* eit, du_uint16_array* event_id);
extern du_bool media_mpeg_psisi_eit_get_event_id(media_mpeg_psisi_eit* eit, du_uint16* event_id);

extern void media_mpeg_psisi_bit_init(media_mpeg_psisi_bit* bit);
extern du_bool media_mpeg_psisi_bit_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_bit* bit);
extern void media_mpeg_psisi_bit_free(media_mpeg_psisi_bit* bit);

extern void media_mpeg_psisi_tot_init(media_mpeg_psisi_tot* tot);
extern du_bool media_mpeg_psisi_tot_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_tot* tot);
extern void media_mpeg_psisi_tot_free(media_mpeg_psisi_tot* tot);

extern void media_mpeg_psisi_sit_init(media_mpeg_psisi_sit* sit);
extern du_bool media_mpeg_psisi_sit_parse(const du_uint8* data, du_uint32 size, media_mpeg_psisi_sit* sit);
extern void media_mpeg_psisi_sit_free(media_mpeg_psisi_sit* sit);
extern du_bool media_mpeg_psisi_sit_create(media_mpeg_psisi_sit* sit, tru_uint8_array* dst);

extern du_bool media_mpeg_psisi_sit_create_partial_ts(media_mpeg_psisi_nit* org_nit, media_mpeg_psisi_sdt* org_sdt, media_mpeg_psisi_eit* org_eit, media_mpeg_psisi_bit* org_bit, du_uint16 network_id, du_uint16 program_number, du_uint8 version_number, media_mpeg_psisi_sit* sit);
extern du_bool media_mpeg_psisi_sit_create_partial_ts_2(media_mpeg_psisi_nit* org_nit, media_mpeg_psisi_sdt* org_sdt, media_mpeg_psisi_eit* org_eit, media_mpeg_psisi_bit* org_bit, du_uint16 program_number, du_uint8 version_number, media_mpeg_psisi_sit* sit);

extern du_bool media_mpeg_psisi_sit_set_country_code(media_mpeg_psisi_sit* sit, const du_uchar* country_code);
extern du_bool media_mpeg_psisi_sit_update_partial_transport_stream_time_desc(media_mpeg_psisi_sit* sit, media_mpeg_psisi_eit* org_eit, media_mpeg_psisi_tot* org_tot, du_uint8 event_version_number, du_uint8 other_descriptor_status);

extern du_bool media_mpeg_psisi_pmt_get_parental_rate(media_mpeg_psisi_pmt* pmt, du_uint8* parental_rate);
extern du_bool media_mpeg_psisi_sit_get_parental_rate(media_mpeg_psisi_sit* sit, du_uint8* parental_rate);

#ifdef __cplusplus
}
#endif

#endif

