/*
 * Copyright (c) 2013 DigiOn, Inc. All rights reserved.
 */

#include <du_log.h>
#include <android/log.h>

#include "android_log_handler.h"

int convert_log_level(du_log_level level) {
    switch (level){
    case DU_LOG_LEVEL_UNKNOWN:   return ANDROID_LOG_UNKNOWN;
    case DU_LOG_LEVEL_ERROR:     return ANDROID_LOG_ERROR;
    case DU_LOG_LEVEL_WARNING:   return ANDROID_LOG_WARN;
    case DU_LOG_LEVEL_INFO:      return ANDROID_LOG_INFO;
    case DU_LOG_LEVEL_DEBUG:     return ANDROID_LOG_DEBUG;
    }

    return ANDROID_LOG_DEFAULT;
}

void log_output_v_handler(du_log_level level, du_bool need_timestamp, const du_uchar* category, const du_uchar* fmt, va_list argp, void* arg) {
    __android_log_vprint(convert_log_level(level), (const char *)category, (const char *)fmt, argp);
}

void log_output_n_handler(du_log_level level, du_bool need_timestamp, const du_uchar* category, const du_uint8* buf, du_uint32 len, du_bool need_hex, void* arg) {
    char buff[256];

    int i;
    for (i=0; i < len/16; i++){
        sprintf(buff, "%02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x",
        buf[i*16], buf[i*16+1], buf[i*16+2], buf[i*16+3], buf[i*16+4], buf[i*16+5], buf[i*16+6], buf[i*16+7],
        buf[i*16+8], buf[i*16+9], buf[i*16+10], buf[i*16+11], buf[i*16+12], buf[i*16+13], buf[i*16+14], buf[i*16+15]);
        __android_log_write(convert_log_level(level), (const char *)category, buff);
    }

    if (len%16){
        buff[0]='\0';
        for (i=0; i < len%16; i++){
            sprintf(&buff[i*3], "%02x ", buf[(len/16*16)+i]);
        }
        __android_log_write(convert_log_level(level), (const char *)category, buff);
    }
}

void log_free_arg_handler(void* arg) {
}

du_log_callback log_callback = {
    log_output_v_handler,
    log_output_n_handler,
    NULL,
    log_free_arg_handler
};

du_bool set_android_log_handler() {
#ifndef NDEBUG
    du_log_set_level(DU_LOG_LEVEL_DEBUG);
#endif
    return du_log_set_callback(&log_callback);
}
