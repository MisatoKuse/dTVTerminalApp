/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DlnaMacro_h
#define DlnaMacro_h

#pragma mark - marco define

#pragma mark - common define

#define BREAK_IF(cond) if(cond) break

#define DEBUG 1

#pragma mark - debug define
#if DEBUG > 0
#include <android/log.h>
#define NDK_TAG "DTVT_NDK"
//    __android_log_print(ANDROID_LOG_VERBOSE, "TRACKERS", "%s", Str);
    // non instance
    #define LOG_WITH(fmt, ...) __android_log_print(ANDROID_LOG_INFO, NDK_TAG, "[%s]" fmt , __FUNCTION__, ##__VA_ARGS__ )
    #define LOG_WITH_BOOL(target, fmt, ...) __android_log_print(ANDROID_LOG_INFO, NDK_TAG, "[%s] BOOL:<%s>" fmt, __FUNCTION__, target ? "YES" : "NO",##__VA_ARGS__ )
    #ifdef __cplusplus
    // instance
    #include <typeinfo>
    #define LOG_WITH_PARAM(fmt, ...) __android_log_print(ANDROID_LOG_INFO, NDK_TAG, "[%s::%s]" fmt, typeid(this).name(), __FUNCTION__, ##__VA_ARGS__ )
    #define LOG_WITH_BOOL_PARAM(target, fmt, ...) __android_log_print(ANDROID_LOG_INFO, NDK_TAG, "[%s::%s] BOOL:<%s>" fmt, typeid(this).name(), __FUNCTION__, target ? "YES" : "NO",##__VA_ARGS__ )
    #else

    #endif // #ifdef __cplusplus

#pragma mark - release define
#else

    #define LOG_WITH(fmt, ...)
    #define LOG_WITH_BOOL(target, fmt, ...)

    #define LOG_WITH_PARAM(fmt, ...)
    #define LOG_WITH_BOOL_PARAM(target, fmt, ...)

#endif // #ifdef DEBUG

#endif /* DlnaMacro_h */
