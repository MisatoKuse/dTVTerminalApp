/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_util_log.h 8900 2014-07-31 05:38:47Z gondo $ 
 */ 

#ifndef DDTCP_UTIL_LOG_H
#define DDTCP_UTIL_LOG_H

#include <du_log.h>
#include <stdio.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

#ifdef ANDROID_DEBUG
#include <android/log.h>
#define log_printf(fmt, ...) __android_log_print(ANDROID_LOG_VERBOSE, "DUL", fmt, ##__VA_ARGS__)
#define log_printh(data, len) { \
    int i; \
    for (i = 0; i < (int)len; ++i) { \
        __android_log_print(ANDROID_LOG_VERBOSE, "DUL", "%02x", ((unsigned char*)(data))[i]); \
        if (i % 16 == 15) { \
            __android_log_print(ANDROID_LOG_VERBOSE, "DUL", "\n"); \
        } else { \
            __android_log_print(ANDROID_LOG_VERBOSE, "DUL", " "); \
        } \
    } \
    __android_log_print(ANDROID_LOG_VERBOSE, "DUL", "\n"); \
}
#elif defined WIN32
#else
#define log_printf(fmt, ...)
#define log_printh(data, len)
#endif

extern const du_uchar* ddtcp_util_log_category;

#ifdef DDTCP_LOG
#define ddtcp_util_log_d(category, msg) du_log_d((category), (msg));
#define ddtcp_util_log_i(category, msg) du_log_i((category), (msg));
#define ddtcp_util_log_w(category, msg) du_log_w((category), (msg));
#define ddtcp_util_log_e(category, msg) du_log_e((category), (msg));
#define ddtcp_util_log_dt(category, msg) du_log_dt((category), (msg));
#define ddtcp_util_log_it(category, msg) du_log_it((category), (msg));
#define ddtcp_util_log_wt(category, msg) du_log_wt((category), (msg));
#define ddtcp_util_log_et(category, msg) du_log_et((category), (msg));
#define ddtcp_util_log_dn(category, buf, len) du_log_dn((category), (buf), (len));
#define ddtcp_util_log_in(category, buf, len) du_log_in((category), (buf), (len));
#define ddtcp_util_log_wn(category, buf, len) du_log_wn((category), (buf), (len));
#define ddtcp_util_log_en(category, buf, len) du_log_en((category), (buf), (len));
#define ddtcp_util_log_mark_d(category) du_log_mark_d((category))
#define ddtcp_util_log_mark_dt(category) du_log_mark_dt((category))
#define ddtcp_util_log_mark_i(category) du_log_mark_i((category))
#define ddtcp_util_log_mark_it(category) du_log_mark_it((category))
#define ddtcp_util_log_mark_w(category) du_log_mark_w((category))
#define ddtcp_util_log_mark_wt(category) du_log_mark_wt((category))
#define ddtcp_util_log_mark_e(category) du_log_mark_e((category))
#define ddtcp_util_log_mark_et(category) du_log_mark_et((category))
extern void ddtcp_util_log_dump_impl(const du_uchar* msg, const du_uint8* buf, du_uint32 len);
#define ddtcp_util_log_dump(msg, buf, len) ddtcp_util_log_dump_impl((msg), (buf), (len))
#define ddtcp_util_log_printf(format_and_variable_param_list) du_log_dv format_and_variable_param_list;
#else
#define ddtcp_util_log_d(category, msg)
#define ddtcp_util_log_i(category, msg)
#define ddtcp_util_log_w(category, msg)
#define ddtcp_util_log_e(category, msg)
#define ddtcp_util_log_dt(category, msg)
#define ddtcp_util_log_it(category, msg)
#define ddtcp_util_log_wt(category, msg)
#define ddtcp_util_log_et(category, msg)
#define ddtcp_util_log_dn(category, buf, len)
#define ddtcp_util_log_in(category, buf, len)
#define ddtcp_util_log_wn(category, buf, len)
#define ddtcp_util_log_en(category, buf, len)
#define ddtcp_util_log_mark_d(category)
#define ddtcp_util_log_mark_dt(category)
#define ddtcp_util_log_mark_i(category)
#define ddtcp_util_log_mark_it(category)
#define ddtcp_util_log_mark_w(category)
#define ddtcp_util_log_mark_wt(category)
#define ddtcp_util_log_mark_e(category)
#define ddtcp_util_log_mark_et(category)
#define ddtcp_util_log_dump(msg, buf, len)
#define ddtcp_util_log_printf(format_and_variable_param_list)
#endif

//#define DEBUG_LOG
//#define DEBUG_LOG_2

#ifdef DEBUG_LOG
#ifdef WIN32
#pragma message("XXX: DEBUG_LOG")
#define debug_print_time(msg) { \
    du_timel t; \
 \
    du_time_msec(&t); \
    du_log_dv(ddtcp_util_log_category, DU_UCHAR("XXX: time=%I64d(msec) %s"), t, msg); \
}
#else
#define debug_print_time(msg) { \
    du_timel t; \
 \
    du_time_msec(&t); \
    du_log_dv(ddtcp_util_log_category, DU_UCHAR("XXX: time=%lld(msec) %s"), t, msg); \
}
#endif
#else
#define debug_print_time(msg)
#endif

#ifdef __cplusplus
}
#endif

#endif

