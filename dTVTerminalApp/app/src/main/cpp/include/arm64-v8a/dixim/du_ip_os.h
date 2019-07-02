/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DU_IP_OS_H
#define DU_IP_OS_H

#include <sys/socket.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Structure of du_ip.
 */
typedef struct du_ip {
    struct sockaddr_storage ss;
    socklen_t ss_len;
} du_ip;

#ifdef __cplusplus
}
#endif

#endif
