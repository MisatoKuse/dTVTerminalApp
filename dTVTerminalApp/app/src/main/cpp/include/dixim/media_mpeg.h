/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: media_mpeg.h 7143 2012-07-12 11:07:03Z gondo $ 
 */ 
 
#ifndef MEDIA_MPEG_H
#define MEDIA_MPEG_H

#include <du_type_os.h>
#include <du_time_os.h>
#include <tr_util.h>
#include <tr_util_rename.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    int dummy;
} media_mpeg_dummy;

typedef media_mpeg_dummy* media_mpeg;

typedef enum {
    MEDIA_MPEG_CMD_UNKNOWN = 0,
    MEDIA_MPEG_CMD_CHECK_CCI_DESC = 1,
    MEDIA_MPEG_CMD_ADD_CCI_DESC = 2,
    MEDIA_MPEG_CMD_COPY = 4,
    MEDIA_MPEG_CMD_MOVE = 8,
    MEDIA_MPEG_CMD_COG = 16,
    MEDIA_MPEG_CMD_EXISTS = 32,
    MEDIA_MPEG_CMD_TEST = 64,
} media_mpeg_cmd;

typedef enum {
    MEDIA_MPEG_DOMAIN_UNKNOWN = 0x0,
    MEDIA_MPEG_DOMAIN_ARIB = 0x1,
    MEDIA_MPEG_DOMAIN_DTCP = 0x2,
    MEDIA_MPEG_DOMAIN_SAFIA = 0x4,
    MEDIA_MPEG_DOMAIN_LOCAL = 0x8,
    MEDIA_MPEG_DOMAIN_UNDIFINED = 0x10,
} media_mpeg_domain;

typedef enum {
    MEDIA_MPEG_STATE_KEEP_STREAMING,
    MEDIA_MPEG_STATE_DISCARD_SAVED_DATA,
    MEDIA_MPEG_STATE_STOP_STREAMING,
} media_mpeg_state;

typedef struct {
    media_mpeg_state state;
    du_bool discard_this_chunk;
    du_uint32 most_restrictive_e_emi;
} media_mpeg_cci_status;

typedef enum {
    MEDIA_MPEG_CCI_CF = 0x00,
    MEDIA_MPEG_CCI_NMC = 0x01,
    MEDIA_MPEG_CCI_COG = 0x02,
    MEDIA_MPEG_CCI_CN = 0x03,
} media_mpeg_cci;

typedef struct {
    du_uint8 retention_move_mode;
    du_uint8 retention_state;
    du_uint8 epn;
    media_mpeg_cci cci;
    du_uint8 ast;
    du_uint8 image_constraint_token;
    du_uint8 aps;
} media_mpeg_cci_desc_info;

extern du_bool media_mpeg_init(media_mpeg* mm);
extern void media_mpeg_free(media_mpeg* mm);

extern void media_mpeg_set_cmd(media_mpeg* mm, du_int32 cmd);
extern void media_mpeg_set_src_domain(media_mpeg* mm, du_int32 domain);
extern void media_mpeg_set_dst_domain(media_mpeg* mm, du_int32 domain);
extern void media_mpeg_set_margin_time(media_mpeg* mm, du_timel elapse_msec);
extern void media_mpeg_set_margin_time_2(media_mpeg* mm, du_timel elapse_msec, du_uint32 bitrate);
extern du_bool media_mpeg_check(media_mpeg* mm);
extern du_bool media_mpeg_enable_parental_rate_check(media_mpeg mm, du_uint8 parental_rate);

extern du_bool media_mpeg_convert(media_mpeg mm, du_uint8** src, du_uint32* src_size, du_uint8** dst, du_uint32* dst_size, du_uint32* nbytes);
extern du_bool media_mpeg_convert_flush(media_mpeg mm, du_uint8** dst, du_uint32* dst_size, du_uint32* nbytes);
extern du_bool media_mpeg_convert_flush_discard(media_mpeg mm);
extern void media_mpeg_cci_status_init(media_mpeg_cci_status* s); 
extern du_bool media_mpeg_cci_get_status(media_mpeg mm, du_uint32 e_emi, media_mpeg_cci_status* s); 
extern du_bool media_mpeg_cci_desc_is_need(media_mpeg mm, du_bool* need, du_bool* cont);
extern du_bool media_mpeg_get_pat(media_mpeg mm, tru_uint8_array* pat);
extern du_bool media_mpeg_get_pmt(media_mpeg mm, tru_uint8_array* pmt);
extern du_bool media_mpeg_set_cci_desc(media_mpeg mm, media_mpeg_cci_desc_info* info);


extern du_bool test_media_mpeg_cci_get_status();

#ifdef __cplusplus
}
#endif

#endif
