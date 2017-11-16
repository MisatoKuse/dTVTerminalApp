/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_net_task interface provides some methods for manipulating
 *  du_net_task ( such as initializing/modifying du_net_task structure ).
 *  du_net_process() manages all du_net_task stored in the du_net structure,
 *  checks and sets each state value of the du_net_task, invokes the handler function of
 *  the du_net_task.
 *  @see du_net.h

 */

#ifndef DU_NET_TASK_H
#define DU_NET_TASK_H

#include <du_poll.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Enumeration of du_net_task state.
 */
typedef enum {
    /**
     *   Never used.
     */
    DU_NET_TASK_STATE_UNKNOWN,

    /**
     *   Wait until the socket gets readable.
     */
    DU_NET_TASK_STATE_WAIT_READ,

    /**
     *   Wait until the socket gets writable.
     */
    DU_NET_TASK_STATE_WAIT_WRITE,

    /**
     *   Wait until the socket gets readable or writable.
     */
    DU_NET_TASK_STATE_WAIT_READ_WRITE,

    /**
     *   Do nothing.
     */
    DU_NET_TASK_STATE_HOLD,

    /**
     *   Timed out.
     */
    DU_NET_TASK_STATE_TIMEOUT,

    /**
     *   Canceled.
     */
    DU_NET_TASK_STATE_CANCEL,

    /**
     *   Ended.
     */
    DU_NET_TASK_STATE_END,

} du_net_task_state;

typedef struct du_net_task du_net_task;

/**
 *  The interface definition for an application-defined function invoked after
 *  receiving net_task I/O event.
 *  @param[out] task pointer to the du_net_task structure.
 */
typedef void (*du_net_task_handler)(du_net_task* task);

/**
 *  This structure contains network task data.
 */
struct du_net_task {
    /**
     *   task state
     */
    du_net_task_state state;

    du_timel limit_ms;
    /**
     *   socket
     */
    du_socket socket;

    du_net_task_handler handler;
    /**
     *   handler arg
     */
    void* arg;

    /**
     *   the result of poll
     */
    du_poll_flag flags;

    /**
     *   task ID
     */
    du_id32 task_id;

    du_bool cancelable;
};

/**
 *  Initializes a du_net_task.
 *  @param[out] task pointer to the du_net_task structure.
 *  @param[in] state  state value of du_net_task.
 *    The state value is one of the constants described below.
 *             @li @c DU_NET_TASK_STATE_WAIT_READ : Waiting for reading data
 *             @li @c DU_NET_TASK_STATE_WAIT_WRITE : Waiting for output
 *             @li @c DU_NET_TASK_STATE_WAIT_READ_WRITE : Waiting for reading/writing data
 *  @param[in] limit_ms an absolute time to wait.
 *  @param[in] socket du_socket data referencing a socket.
 *  @param[in] handler a function to set in the task.
 *  @param[in] arg a parameter for the handler function.
 */
extern void du_net_task_init(du_net_task* task, du_net_task_state state, du_timel limit_ms, du_socket socket, du_net_task_handler handler, void* arg);

/**
 *  Modifies a du_net_task.
 *  @param[out] task pointer to the du_net_task structure.
 *  @param[in] state  state value of du_net_task.
 *    The state value is one of the constants described below.
 *             @li @c DU_NET_TASK_STATE_WAIT_READ : Waiting for reading data
 *             @li @c DU_NET_TASK_STATE_WAIT_WRITE : Waiting for output
 *             @li @c DU_NET_TASK_STATE_WAIT_READ_WRITE : Waiting for reading/writing data
 *             @li @c DU_NET_TASK_STATE_CANCEL : a connection is canceled.
 *             @li @c DU_NET_TASK_STATE_END : completion of reading/writing data.
 *  @param[in] limit_ms an absolute time to wait.
 *  @param[in] socket du_socket data referencing a socket.
 *  @param[in] handler a function to set in the task.
 *  @param[in] arg a parameter for the handler function.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_net_task_modify(du_net_task* task, du_net_task_state state, du_timel limit_ms, du_socket socket, du_net_task_handler handler, void* arg);

/**
 *  Sets the cancel enable/disable flag.
 *  @param[out] task pointer to the du_net_task structure.
 *  @param[in] cancelable  true if the cancel request is to be disabled; false otherwise.
 */
extern void du_net_task_set_cancelable(du_net_task* task, du_bool cancelable);

#ifdef __cplusplus
}
#endif

#endif
