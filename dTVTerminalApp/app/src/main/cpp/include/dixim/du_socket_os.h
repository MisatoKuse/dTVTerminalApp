/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */
/** @file du_socket_os.h
 * @brief The du_socket_os interface provides some methods for networking application
 * based on the socket interface on Linux.
 */

#ifndef DU_SOCKET_OS_H
#define DU_SOCKET_OS_H

#ifdef __cplusplus
extern "C" {
#endif

typedef int du_socket;

/**
 *  DU_SOCKET_INVALID : socket handle value of invalid socket.
 */
#define DU_SOCKET_INVALID -1

#ifdef __cplusplus
}
#endif

#endif
