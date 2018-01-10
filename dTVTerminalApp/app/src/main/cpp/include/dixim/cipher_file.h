/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: cipher_file.h 8866 2014-06-02 09:43:48Z gondo $ 
 */ 

#ifndef CIPHER_FILE_H
#define CIPHER_FILE_H

#include <du_file.h>
#include <chunked_file.h>
#include <ddtcp.h>
#include <ddtcp_crypto_aes.h>
#include <ddtcp_util_scrambler.h>
#include <du_uchar_array.h>
#include <media_mpeg_time.h>
#include <media_mpeg_time_corrector.h>
#include <du_uint8_array.h>
#include <cipher_file_inter_source.h>

#ifdef __cplusplus
extern "C" {
#endif


#define CIPHER_FILE_CONTENT_SPECIFIC_KEY_SIZE DDTCP_CRYPTO_AES_BLOCK_SIZE_128
#define CIPHER_FILE_CONTENT_ID_RECIVED_TIME_SEC_SIZE 4
#define CIPHER_FILE_CONTENT_ID_RANDOM_SIZE 3
#define CIPHER_FILE_CONTENT_ID_SIZE (CIPHER_FILE_CONTENT_ID_RECIVED_TIME_SEC_SIZE + CIPHER_FILE_CONTENT_ID_RANDOM_SIZE)

#define CIPHER_FILE_LOCAL_MODE_INITIAL_DATA_SIZE 192

typedef struct {
    du_uint64 cipher_size;
    ddtcp_e_emi e_emi;
    du_uint8 content_id[CIPHER_FILE_CONTENT_ID_SIZE];
    du_uint32 footer_size;
    du_bool local_mode;
    du_bool send_local_mode_header;
} cipher_file_r;

typedef struct {
    ddtcp_util_scrambler scrambled_content_specific_key;
    du_uint8 content_specific_key_dummy[CIPHER_FILE_CONTENT_SPECIFIC_KEY_SIZE];
} cipher_file_w;

typedef enum {
    CIPHER_FILE_PACKET_SIZE_UNKNOWN = 0,
    CIPHER_FILE_PACKET_SIZE_TTS = 192,
    CIPHER_FILE_PACKET_SIZE_MP4 = 0,
} cipher_file_packet_size;

typedef struct {
    du_uint8 found_desc[0xFF];
    du_uint32 header_size;
    du_uint64 clear_size;
    du_uint64 didl_offset;
    du_uint16 didl_size;
    du_uint32 time_index_interval_size;
    du_uint64 time_index_offset;
    du_uint32 time_index_size;
    du_uint32 duration_msec;
    du_uint16 container_format;
    cipher_file_packet_size packet_size;
} clear_header_info;

typedef struct {    
    du_uint8 found_desc[0xFF];
    du_uint32 header_size;
    du_uint8 content_id[CIPHER_FILE_CONTENT_ID_SIZE];
    ddtcp_e_emi e_emi;
} cipher_header_info;

typedef struct {
    cipher_file_inter_source* source; 
    du_uchar_array fn;
    du_bool open_read;
    cipher_file_r r;
    cipher_file_w w;
    du_uint32 header_size;
    du_uint16 padding_size;
    du_bool is_failed;
    du_bool is_opened;
    media_mpeg_time mmt;
    clear_header_info clear_h;
    cipher_header_info cipher_h;
    media_mpeg_time_corrector mmtc;
    du_uint32 duration_msec_from_didl;
} cipher_file;

typedef struct {
    ddtcp_e_emi recorded_e_emi;
    du_uint8 count;
    du_uint8 _content_id[CIPHER_FILE_CONTENT_ID_SIZE];
} cipher_file_cci_status;

extern void cipher_file_enable_chunk_file(du_uint64 chunk_size);
extern du_bool cipher_file_get_status(const du_uchar* fn, du_file_status* buf);
extern du_bool cipher_file_seek_set(cipher_file* cf, du_uint64 pos, du_uint64* new_pos);
extern du_bool cipher_file_is_time_seek_supported(cipher_file* cf, du_bool* supported);
extern du_bool cipher_file_time_seek_set(cipher_file* cf, du_uint32 msec, du_uint32* new_msec);
extern du_bool cipher_file_seek_pos(cipher_file* cf, du_uint64* pos);
extern du_bool cipher_file_open_read(cipher_file* cf, const du_uchar* fn);
extern du_bool cipher_file_open_read_no_check(cipher_file* cf, const du_uchar* fn);
extern du_bool cipher_file_open_truncate(cipher_file* cf, const du_uchar* fn);
extern du_bool cipher_file_open_truncate_clone_id(cipher_file* cf, const du_uchar* fn, const du_uchar* fn_src);
extern du_bool cipher_file_close_read(cipher_file* cf);
extern du_bool cipher_file_close_truncate(cipher_file* cf, ddtcp_e_emi, du_bool make_usable);
extern du_bool cipher_file_close_truncate_clone_id(cipher_file* cf, du_bool make_usable, const du_uchar* fn_src);
extern du_bool cipher_file_read_e_emi(cipher_file* cf, ddtcp_e_emi* e_emi);
extern du_bool cipher_file_read(cipher_file* cf, du_uint8* buf, du_uint32 len, du_uint32* nbytes);
extern du_bool cipher_file_write(cipher_file* cf, const du_uint8* buf, du_uint32 len);
#define cipher_file_write_with_padding cipher_file_write
extern du_bool cipher_file_get_cci_status_no_check(const du_uchar* fn, cipher_file_cci_status* buf);
extern du_bool cipher_file_get_cci_status(const du_uchar* fn, cipher_file_cci_status* buf);
extern du_bool cipher_file_content_usable(const du_uchar* fn, du_uint8 initial_count);
extern du_bool cipher_file_content_unusable(const du_uchar* fn, du_uint8* present_count);
extern du_bool cipher_file_write_didl(const du_uchar* fn, const du_uchar* didl);
extern du_bool cipher_file_read_didl(const du_uchar* fn, du_uchar_array* didl);
extern du_bool cipher_file_get_duration(const du_uchar* fn, du_uint32* duration_msec, du_bool* supported);
extern du_bool cipher_file_backup_nmcc_db();
extern du_bool cipher_file_get_content_id(const du_uchar* fn, du_uchar_array* id);
extern void cipher_file_set_duration_from_didl(cipher_file* cf, du_uint32 duration_msec);
extern void cipher_file_set_local_mode(cipher_file* cf);
extern du_bool cipher_file_create_key(const du_uint8 content_specific_key[CIPHER_FILE_CONTENT_SPECIFIC_KEY_SIZE], du_uint8 key[DDTCP_CRYPTO_AES_BLOCK_SIZE_128]);

typedef enum {
    CIPHER_FILE_CONTAINER_FORMAT_UNKNOWN = 0,
    CIPHER_FILE_CONTAINER_FORMAT_TTS = 1,
    CIPHER_FILE_CONTAINER_FORMAT_DEFAULT = CIPHER_FILE_CONTAINER_FORMAT_TTS,
    CIPHER_FILE_CONTAINER_FORMAT_MP4 = 2,
} cipher_file_container_format;

extern du_bool cipher_file_set_container_format(cipher_file* cf, cipher_file_container_format container_format);
extern du_bool cipher_file_get_container_format(cipher_file* cf, cipher_file_container_format* container_format);
extern du_bool cipher_file_get_packet_size(cipher_file* cf, du_uint16* packet_size);

#ifdef __cplusplus
}
#endif

#endif
