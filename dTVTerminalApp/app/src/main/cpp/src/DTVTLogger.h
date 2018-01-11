/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVTLOGGER_H
#define DTVTLOGGER_H
#include <android/log.h>
// ログレベル設定(リリース版ではDEBUGログは無効化する。ERRORとINFOは残す）
#define DTVTLOGGER_TAG "[dTVT]"

// DEBUGレベルログマクロ(リリース版では Gradleで DEBUGログは無効化される)
#define ENABLE_LOG_DEBUG    //todo リリース版だったら、undefineにする
#ifdef ENABLE_LOG_DEBUG
#define DTVT_LOG_DBG(...) (__android_log_print(ANDROID_LOG_DEBUG, DTVTLOGGER_TAG, __VA_ARGS__))
#define DTVT_LOG_DBG_VERBOSE(fmt, ...) (__android_log_print(ANDROID_LOG_DEBUG, DTVTLOGGER_TAG, \
                fmt "\n[%s]\n(%s:%d)", ##__VA_ARGS__, __FILE__, __FUNCTION__, __LINE__))
#else /* ENABLE_LOG_DEBUG */
#define DTVT_LOG_DBG(...)
#define DTVT_LOG_DBG_VERBOSE(...)
#endif /* ENABLE_LOG_DEBUG */

// INFOレベルログマクロ
#ifdef ENABLE_LOG_INFO
#define DTVT_LOG_INFO(...) (__android_log_print(ANDROID_LOG_INFO, DTVTLOGGER_TAG, __VA_ARGS__))
#define DTVT_LOG_INFO_VERBOSE(fmt, ...) (__android_log_print(ANDROID_LOG_INFO, DTVTLOGGER_TAG, \
                fmt "\n[%s]\n(%s:%d)", ##__VA_ARGS__, __FILE__, __FUNCTION__, __LINE__))
#else /* ENABLE_LOG_INFO */
#define DTVT_LOG_INFO(...)
#define DTVT_LOG_INFO_VERBOSE(...)
#endif /* ENABLE_LOG_INFO */

// ERRORレベルログマクロ
#ifdef ENABLE_LOG_ERROR
#define DTVT_LOG_ERR(...) (__android_log_print(ANDROID_LOG_ERROR, DTVTLOGGER_TAG, __VA_ARGS__))
#define DTVT_LOG_ERR_VERBOSE(fmt, ...) (__android_log_print(ANDROID_LOG_ERROR, DTVTLOGGER_TAG, \
                fmt "\n[%s]\n(%s:%d)", ##__VA_ARGS__, __FILE__, __FUNCTION__, __LINE__))
#else /* ENABLE_LOG_ERROR */
#define DTVT_LOG_ERR(...)
#define DTVT_LOG_ERR_VERBOSE(...)
#endif /* ENABLE_LOG_ERROR */

#endif // DTVTLOGGER_H
