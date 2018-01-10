/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_ex.h 6666 2012-04-16 11:06:49Z gondo $ 
 */ 

#ifndef DDTCP_EX_H
#define DDTCP_EX_H

#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    DDTCP_RET_EX_FAILURE_BASE = 0x3000,
    DDTCP_RET_EX_FAILURE_HWID_GET = 0x3001,
    DDTCP_RET_EX_FAILURE_HWID_INVALID = 0x3002,
    DDTCP_RET_EX_FAILURE_DEVICE_KEY_NOT_FOUND = 0x3003,
    DDTCP_RET_EX_FAILURE_DEVICE_KEY_INVALID = 0x3004,
    DDTCP_RET_EX_FAILURE_END = 0x3fff,
} ddtcp_ret_ex;

#ifdef __cplusplus
}
#endif

#endif
