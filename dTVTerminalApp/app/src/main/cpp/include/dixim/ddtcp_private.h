/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_private.h 9028 2014-09-16 04:39:19Z gondo $ 
 */ 

/** @file ddtcp_private.h
 *  @brief Functions defined in this file are unofficial functions.<br>
 */
 
#ifndef DDTCP_PRIVATE_H
#define DDTCP_PRIVATE_H

#include <du_uint8_array.h>
#include <du_thread.h>
#include <ddtcp.h>
#include <ddtcp_crypto_sha.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DDTCP_DEVICE_ID_SIZE 5

typedef enum {
    DDTCP_POST_RET_UNKNOWN,
    DDTCP_POST_RET_SUCCESS,
    DDTCP_POST_RET_END_SDK,
    DDTCP_POST_RET_CLOSE_HANDLE,
    DDTCP_POST_RET_CLOSE_HANDLE_AND_RETRY,
} ddtcp_post_ret;

typedef struct {
    du_uint32 kc;
    du_uint32 ivc;
    du_bool set_cf;
    du_uint32 cf;
} ddtcp_aes_mode_value;

typedef struct {
    ddtcp_aes_mode_value aes_mode_value;
    du_bool set_ecc_timing_back;
} ddtcp_grobal_param;

extern ddtcp_grobal_param g_ddtcp_grobal_param;

extern ddtcp_ret ddtcp_set_aes_mode_value(du_uint32 kc, du_uint32 ivc);
extern ddtcp_ret ddtcp_set_aes_mode_value_for_copy_free(du_uint32 aes_mode);
extern ddtcp_post_ret ddtcp_check_post_ret(ddtcp_ret ret);
extern ddtcp_ret ddtcp_set_ecc_timing_back();
extern ddtcp_ret ddtcp_tr_load(const du_uchar* arc_load_dll_path);
extern ddtcp_ret ddtcp_tr_free();
extern ddtcp_ret ddtcp_set_sink_ckc_max_retry_count(ddtcp dtcp, du_uint32 count);
extern ddtcp_ret ddtcp_set_sink_ckc_min_retry_interval(ddtcp dtcp, du_uint32 interval_msec);
extern ddtcp_ret ddtcp_set_sink_ckc_retry_count_each(ddtcp dtcp, du_uint32 count);
extern ddtcp_ret ddtcp_set_sink_ckc_retry_interval_each(ddtcp dtcp, du_uint32 interval_msec);
extern ddtcp_ret ddtcp_set_source_disable_move(ddtcp dtcp);
extern ddtcp_ret ddtcp_set_ake_exclusive(ddtcp dtcp);
extern ddtcp_ret ddtcp_set_source_listen_max_ake_connection(ddtcp dtcp, du_uint32 max);
extern ddtcp_ret ddtcp_set_source_connection_timeout(ddtcp dtcp, du_uint32 timeout_msec);
extern ddtcp_ret ddtcp_disable_source_ckc_connection_timeout(ddtcp dtcp);
extern void ddtcp_disable_validating_ap_flag_in_certificate();

typedef struct {
    du_uint8 ur_mode;
    du_uint8 content_type;
    du_uint8 aps;
    du_uint8 ict;
    du_uint8 ast;
    du_uint8 dot;
} ddtcp_pcp_ur;

extern ddtcp_ret ddtcp_pack_pcp_ur(ddtcp_pcp_ur* x, du_uint8 pcp_ur[DDTCP_PCP_UR_SIZE]);
extern ddtcp_ret ddtcp_unpack_pcp_ur(du_uint8 pcp_ur[DDTCP_PCP_UR_SIZE], ddtcp_pcp_ur* x);

extern ddtcp_ret ddtcp_get_device_id_hash(ddtcp dtcp, du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE]); // XXX: @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.
extern ddtcp_ret ddtcp_source_is_device_registered_in_rsr(ddtcp dtcp, du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE], du_bool* registered); // XXX: @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.
extern ddtcp_ret ddtcp_source_remove_device_in_rsr(ddtcp dtcp, du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE]); // XXX: @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.

#define ddtcp_device_id_hash_array du_uint8_array
#define ddtcp_device_id_hash_array_init(x) du_uint8_array_init(x)
#define ddtcp_device_id_hash_array_free(x) du_uint8_array_free(x)
#define ddtcp_device_id_hash_array_length(x) (du_uint8_array_length((x)) / DDTCP_CRYPTO_SHA1_DIGEST_SIZE)
#define ddtcp_device_id_hash_array_get(x) du_uint8_array_get(x)
#define ddtcp_device_id_hash_array_truncate(x) du_uint8_array_truncate(x)
#define ddtcp_device_id_hash_array_cato(to, s) du_array_catn((to), (du_uint8*)(s), DDTCP_CRYPTO_SHA1_DIGEST_SIZE)
extern du_uint8* ddtcp_device_id_hash_array_get_pos(ddtcp_device_id_hash_array* x, du_uint32 pos);

extern ddtcp_ret ddtcp_source_get_device_id_hash_array_in_rsr(ddtcp dtcp, ddtcp_device_id_hash_array* x); // XXX: @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.

extern ddtcp_ret ddtcp_get_device_id(ddtcp dtcp, du_uint8 device_id[DDTCP_DEVICE_ID_SIZE]); // XXX: @remark If this function is used, call between ddtcp_startup and ddtcp_shutdown.
extern ddtcp_ret ddtcp_generate_device_id_hash(du_uint8 device_id[DDTCP_DEVICE_ID_SIZE], du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE]);



extern ddtcp_ret ddtcp_set_ake_thread_priority(ddtcp dtcp, du_thread_priority priority);
extern ddtcp_ret ddtcp_get_ake_thread_priority(ddtcp dtcp, du_thread_priority* priority);

typedef struct {
    du_uint8 packet_type;
    du_uint8 c_a2;
    du_uint8 c_a;
    ddtcp_e_emi e_emi;
    du_uint8 exchange_key_label;
    du_uint8 nc[DDTCP_NC_SIZE];
    du_uint8 pcp_ur[DDTCP_PCP_UR_SIZE];
    du_uint32 cl;
    du_bool pcp_start_indicator;
} ddtcp_pcp; 

#ifdef __cplusplus
}
#endif

#endif
