/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_log interface provides various methods for logging messages for applications.
 *   Logging messages will be forwarded to a variety of destinations, including consoles, files etc.
 *   The destination is set by DU_LOG_PATH environment variable.
 *   The default destination is "STDERR". <br>
 *   du_log has a "Level" associated with it. This du_log "Level" is set by DU_LOG_LEVEL environment
 *   variable. No messages will be logged by default.
 *   This Level reflects a minimum Level that this du_log cares about. <br>
 *   Each logging messages also has levels of importance associated with them.
 *   When a du_log method is called, the du_log compares its own level with
 *   the level associated with the method call. If the du_log's level is higher than
 *   the method call's, no logging message is actually generated.
 *   If you wish to filter log messages, set DU_LOG_LEVEL environment variable.<br>
 *   The levels provided are
 *    @li DU_LOG_LEVEL_ERROR
 *    @li DU_LOG_LEVEL_WARNING
 *    @li DU_LOG_LEVEL_INFO
 *    @li DU_LOG_LEVEL_DEBUG  <br>
 *
 *   du_log has a "category" associated with it. This du_log "category" is set by
 *   DU_LOG_INCLUDE and/or DU_LOG_EXCLUDE environment variables.<br>
 *   Each logging messages also has category parameter associated with them.
 *   When a du_log method is called, the du_log compares category of message with
 *   the "category" value of DU_LOG_INCLUDE and/or DU_LOG_EXCLUDE environment variables.<br>
 *   Both format are CSV.
 *   DU_LOG_INCLUDE: The log messages with one of this "category" values are logged out.<br>
 *   DU_LOG_EXCLUDE: The log messages with one of this "category" values are not logged out.<br>
 *   In the case that DU_LOG_INCLUDE and DU_LOG_EXCLUDE environment variables are not set,
 *     all categories are enable for logging.
 */

#ifndef DU_LOG_H
#define DU_LOG_H

#include <du_int.h>
#include <stdarg.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Enumeration of log levels.
 */
typedef enum {
    /**
     *   Never used.
     */
  DU_LOG_LEVEL_UNKNOWN,

    /**
     *   Error.
     */
  DU_LOG_LEVEL_ERROR,

    /**
     *   Warning.
     */
  DU_LOG_LEVEL_WARNING,

    /**
     *   Information
     */
  DU_LOG_LEVEL_INFO,

    /**
     *   Debug
     */
  DU_LOG_LEVEL_DEBUG,

} du_log_level;

/**
 *  Enumeration of log output modes.
 */
typedef enum {
    /**
     *   Never used.
     */
  DU_LOG_OUTPUT_MODE_UNKNOWN,

    /**
     *   Open the file at the first.
     */
  DU_LOG_OUTPUT_MODE_OPEN_FIRST,

    /**
     *   Open/Close the file by the logging message.
     */
  DU_LOG_OUTPUT_MODE_OPEN_BY_MESSAGE,

} du_log_output_mode;

extern void du_log_reset(void);

extern du_bool du_log_set_path(const du_uchar* path);

extern void du_log_set_level(du_log_level level);

extern du_bool du_log_set_output_mode(du_log_output_mode mode);

/**
 *  An interface definition of a du_log_output_v_handler function.
 *  <b>du_log_output_v_handler</b> function is an application-defined
 *    callback function for logging a string message.
 *
 *  @param[in] level log level of the message.
 *  @param[in] need_timestamp true if need to output the current timestamp, otherwise false.
 *  @param[in] category category of the message. may be 0.
 *  @param[in] fmt format of the message.
 *  @param[in] argp parameters of the message.
 *  @param[in] arg a parameter for the handler function.
 */
typedef void (*du_log_output_v_handler)(du_log_level level, du_bool need_timestamp, const du_uchar* category, const du_uchar* fmt, va_list argp, void* arg);

/**
 *  An interface definition of a du_log_output_n_handler function.
 *  <b>du_log_output_n_handler</b> function is an application-defined
 *    callback function for logging a binary message.
 *
 *  @param[in] level log level of the message.
 *  @param[in] need_timestamp true if need to output the current timestamp, otherwise false.
 *  @param[in] category category of the message. may be 0.
 *  @param[in] buf data of the message.
 *  @param[in] len length of buf data.
 *  @param[in] need_hex true if need to output buf data in hexadecimal, otherwise false.
 *  @param[in] arg a parameter for the handler function.
 */
typedef void (*du_log_output_n_handler)(du_log_level level, du_bool need_timestamp, const du_uchar* category, const du_uint8* buf, du_uint32 len, du_bool need_hex, void* arg);

/**
 *  An interface definition of a du_log_free_arg_handler function.
 *  <b>du_log_free_arg_handler</b> function is an application-defined
 *    callback function for freeing arg.
 *
 *  @param[in] arg a parameter for the handler function.
 */
typedef void (*du_log_free_arg_handler)(void* arg);

/**
 *  This structure stores the callback handlers and argument
 *  for logging message.
 */
typedef struct du_log_callback {
    /**
     *  Callback handler for logging string message.
     */
    du_log_output_v_handler output_v_handler;

    /**
     *  Callback handler for logging binary message.
     */
    du_log_output_n_handler output_n_handler;

    /**
     *  Argument of the above callback handlers.
     */
    void* arg;

    /**
     *  Callback handler for freeing arg.
     */
    du_log_free_arg_handler free_arg_handler;
} du_log_callback;

/**
 *  Sets the callback handlers which logs the message.
 *
 *  @param[in] callback callback handlers for logging the message.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 */
extern du_bool du_log_set_callback(const du_log_callback* callback);

/**
 *  Logs a message with category and DU_LOG_LEVEL_DEBUG level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_d(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message with category and DU_LOG_LEVEL_INFO level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_i(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message with category and DU_LOG_LEVEL_WARNING level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_w(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message with category and DU_LOG_LEVEL_ERROR level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_e(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_DEBUG level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_dt(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_INFO level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_it(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_WARNING level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_wt(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_ERROR level.
 *  @param[in] category category of the message.
 *  @param[in] msg  the string message .
 */
extern void du_log_et(const du_uchar* category, const du_uchar* msg);

/**
 *  Logs a message with category and DU_LOG_LEVEL_DEBUG level, specifying a message format string
 *  with an array of arguments.
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_dv(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message with category and DU_LOG_LEVEL_INFO level, specifying a message format string
 *  with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_iv(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message with category and DU_LOG_LEVEL_WARNING level, specifying a message format string
 *  with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_wv(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message with category and DU_LOG_LEVEL_ERROR level, specifying a message format string
 *  with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_ev(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_DEBUG level,
 *  specifying a message format string with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_dvt(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_INFO level,
 *  specifying a message format string with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_ivt(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_WARNING level,
 *  specifying a message format string with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_wvt(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a message and timestamp with category and DU_LOG_LEVEL_ERROR level,
 *  specifying a message format string with an array of arguments,
 *  @param[in] category category of the message.
 *  @param[in]  fmt the message format string.
 */
extern void du_log_evt(const du_uchar* category, const du_uchar* fmt, ...);

/**
 *  Logs a buf message which length is len byte
 *  with category and DU_LOG_LEVEL_DEBUG level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_dn(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte
 *  with category and DU_LOG_LEVEL_INFO level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_in(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte
 *  with category and DU_LOG_LEVEL_WARNING level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_wn(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte
 *  with category and DU_LOG_LEVEL_ERROR level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_en(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte by hex format
 *  with category and DU_LOG_LEVEL_DEBUG level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_dh(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte by hex format
 *  with category and DU_LOG_LEVEL_INFO level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_ih(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte by hex format
 *  with category and DU_LOG_LEVEL_WARNING level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_wh(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a buf message which length is len byte by hex format
 *  with category and DU_LOG_LEVEL_ERROR level.
 *  @param[in] category category of the message.
 *  @param[in]  buf the log message data.
 *  @param[in]  len the byte length of buf.
 */
extern void du_log_eh(const du_uchar* category, const du_uint8* buf, du_uint32 len);

/**
 *  Logs a file name and line number information with category and DU_LOG_LEVEL_DEBUG level.
 */
#define du_log_mark_d(category) du_log_dv(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name ,line number, and timestamp information with category and DU_LOG_LEVEL_DEBUG level.
 */
#define du_log_mark_dt(category) du_log_dvt(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name and line number information with category and DU_LOG_LEVEL_INFO level.
 */
#define du_log_mark_i(category) du_log_iv(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name ,line number, and timestamp information with category and DU_LOG_LEVEL_INFO level.
 */
#define du_log_mark_it(category) du_log_ivt(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name and line number information with category and DU_LOG_LEVEL_WARNING level.
 */
#define du_log_mark_w(category) du_log_wv(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name ,line number, and timestamp information with category and DU_LOG_LEVEL_WARNING level.
 */
#define du_log_mark_wt(category) du_log_wvt(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name and line number information with category and DU_LOG_LEVEL_ERROR level.
 */
#define du_log_mark_e(category) du_log_ev(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

/**
 *  Logs a file name ,line number, and timestamp information with category and DU_LOG_LEVEL_ERROR level.
 */
#define du_log_mark_et(category) du_log_evt(category, DU_UCHAR_CONST("%s:%u"), __FILE__, __LINE__);

#ifdef __cplusplus
}
#endif

#endif
