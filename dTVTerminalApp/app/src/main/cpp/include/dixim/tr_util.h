/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 *
 * $Id: tr_util.h 8335 2013-08-29 04:39:46Z gondo $
 */

#ifndef TR_UTIL_H
#define TR_UTIL_H

#include <du_mutex.h>

#ifdef WIN32
#define ENABLE_TR
#endif

#ifdef ENABLE_TR
#define ENABLE_DIXIM_TR // XXX: or ENABLE_ARCSOFT_TR
#define ENABLE_TR_UTIL
#else
 #ifdef WIN32
  #pragma message("XXX: in WIN32 tr util OFF")
 #endif
#endif

#ifdef ENABLE_ARCSOFT_TR
 #define TRENC_ARCTR_APICRYPT ARCTR_APICRYPT
 #include <LibLoadProtect.h>
 #include <LibTRArcSecret.h>
 #include <TRArcDllMacro.h>
#else
 #define LoadTRProtection(a, b)
 #define ARCTR_APIENDCODE
 #define ARCTR_APILIST(a, b)
 #define ARCTR_VARDATALIST(a, b)
 #define ARCTR_ARRAYLIST(a, b)
#endif

#ifdef ENABLE_TR_UTIL
 #include <tru_alloc.h>
 #include <tru_array.h>
 #include <tru_base64.h>
 #include <tru_byte.h>
 #include <tru_int.h>
 #include <tru_ptr_array.h>
 #include <tru_str.h>
 #include <tru_str_array.h>
 #include <tru_time.h>
 #include <tru_time_os.h>
 #include <tru_uchar_array.h>
 #include <tru_uint8_array.h>
#else
 #include <du_alloc.h>
 #include <du_array.h>
 #include <du_base64.h>
 #include <du_byte.h>
 #include <du_int.h>
 #include <du_ptr_array.h>
 #include <du_str.h>
 #include <du_str_array.h>
 #include <du_time.h>
 #include <du_time_os.h>
 #include <du_uchar_array.h>
 #include <du_uint8_array.h>
#endif

#ifdef ENABLE_ARCSOFT_TR
    extern du_mutex g_truenc__ddtcp_private_data_io__restore_key__mutex;
    extern du_mutex g_truenc__ddtcp_constant_data_io__restore_key__mutex;
    extern du_mutex g_truenc__ddtcp_ake_exhchange_key__scramble_mv_exch_key__mutex;
    extern du_mutex g_truenc__ddtcp_pcp__create_content_key__mutex;
    extern du_mutex g_truenc__ddtcp_util_scrambler__ddtcp_util_scrambler_scramble__mutex;
    extern du_mutex g_truenc__ddtcp_util_scrambler__ddtcp_util_descrambler_scramble__mutex;
    extern du_mutex g_truenc__ddtcp_private_data_io__restore_device_specific_rsr_hash_key__mutex;
    extern du_mutex g_truenc__secure_io_local_key__secure_io_local_key__mutex;
    extern du_bool truenc_init(const du_uchar* tr_arc_load_dll_path);
    extern void truenc_end();
    extern void truenc_decrypt_func(void* func);
    extern void truenc_encrypt_func(void* func);
 #define truenc_secret_encrypt_l TRArcSecretEncryptL
 #define truenc_secret_decrypt_l TRArcSecretDecryptL
    extern void truenc__ddtcp_private_data_io__restore_key__mutex_lock();
    extern void truenc__ddtcp_constant_data_io__restore_key__mutex_lock();
    extern void truenc__ddtcp_ake_exhchange_key__scramble_mv_exch_key__mutex_lock();
    extern void truenc__ddtcp_pcp__create_content_key__mutex_lock();
    extern void truenc__ddtcp_util_scrambler__ddtcp_util_scrambler_scramble__mutex_lock();
    extern void truenc__ddtcp_util_scrambler__ddtcp_util_descrambler_scramble__mutex_lock();
    extern void truenc__ddtcp_private_data_io__restore_device_specific_rsr_hash_key__mutex_lock();
    extern void truenc__secure_io_local_key__secure_io_local_key__mutex_lock();
    extern void truenc__ddtcp_private_data_io__restore_key__mutex_unlock();
    extern void truenc__ddtcp_constant_data_io__restore_key__mutex_unlock();
    extern void truenc__ddtcp_ake_exhchange_key__scramble_mv_exch_key__mutex_unlock();
    extern void truenc__ddtcp_pcp__create_content_key__mutex_unlock();
    extern void truenc__ddtcp_util_scrambler__ddtcp_util_scrambler_scramble__mutex_unlock();
    extern void truenc__ddtcp_util_scrambler__ddtcp_util_descrambler_scramble__mutex_unlock();
    extern void truenc__ddtcp_private_data_io__restore_device_specific_rsr_hash_key__mutex_unlock();
    extern void truenc__secure_io_local_key__secure_io_local_key__mutex_unlock();
#else
 #define truenc_init(a)
 #define truenc_end
 #define truenc_decrypt_func(a)
 #define truenc_encrypt_func(a)
 #define truenc_secret_encrypt_l(data, size)
 #define truenc_secret_decrypt_l(data, size)
 #define truenc__ddtcp_private_data_io__restore_key__mutex_lock()
 #define truenc__ddtcp_constant_data_io__restore_key__mutex_lock()
 #define truenc__ddtcp_ake_exhchange_key__scramble_mv_exch_key__mutex_lock()
 #define truenc__ddtcp_pcp__create_content_key__mutex_lock()
 #define truenc__ddtcp_util_scrambler__ddtcp_util_scrambler_scramble__mutex_lock()
 #define truenc__ddtcp_util_scrambler__ddtcp_util_descrambler_scramble__mutex_lock()
 #define truenc__ddtcp_private_data_io__restore_device_specific_rsr_hash_key__mutex_lock()
 #define truenc__secure_io_local_key__secure_io_local_key__mutex_lock()
 #define truenc__ddtcp_private_data_io__restore_key__mutex_unlock()
 #define truenc__ddtcp_constant_data_io__restore_key__mutex_unlock()
 #define truenc__ddtcp_ake_exhchange_key__scramble_mv_exch_key__mutex_unlock()
 #define truenc__ddtcp_pcp__create_content_key__mutex_unlock()
 #define truenc__ddtcp_util_scrambler__ddtcp_util_scrambler_scramble__mutex_unlock()
 #define truenc__ddtcp_util_scrambler__ddtcp_util_descrambler_scramble__mutex_unlock()
 #define truenc__ddtcp_private_data_io__restore_device_specific_rsr_hash_key__mutex_unlock()
 #define truenc__secure_io_local_key__secure_io_local_key__mutex_unlock()
#endif

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#endif
