/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_net interface provides some methods for manipulating
 *  the du_net_task socket I/O.
 *  Before using a du_net, use <b>du_net_init</b> to initialize
 *  du_net data, and use <b>du_net_set_net_task</b> to store
 *  du_net_task data.
 *  Use <b>du_net_free</b> to free the region used by du_net.
 *  @see du_nettask.h
 */

#ifndef DU_NET_H
#define DU_NET_H

#include "du_poll_os.h"
#include "du_mutex.h"
#include "du_net_task_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_net.
 */
typedef struct du_net {
    du_poll poll;
    du_net_task_array net_task_array;
    du_net_task_array net_task_request_array;
    du_id32 latest_task_id;
    du_mutex net_task_mutex;

    du_net_task_array tmp_na;
    du_net_task_array tmp_na2;
} du_net;

/**
 *  Initializes a du_net data area.
 *  @param[out] x  pointer to the du_net structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_net_init(du_net* x);

/**
 *  Resets x to reuse this instance.
 *  @param[in] x pointer to the du_net structure.
 */
extern void du_net_reset(du_net* x);

/**
 *  Frees the resource used by du_net.
 *  @param[in] x pointer to the du_net structure.
 */
extern void du_net_free(du_net* x);

/**
 *  Sets a task.
 *  @param[in] x pointer to the du_net structure.
 *  @param[in] net_task pointer to du_net_task structure data to store in x</x.
 *  @param[in,out] task_id pointer to the storage area to receive the ID value of net_task.
 *      If you don't need the ID, set null to task_id.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_net_set_net_task(du_net* x, du_net_task* net_task, du_id32* task_id);

/**
 *  Modifies a task data.
 *  @param[in] x pointer to the du_net structure.
 *  @param[in] task_id ID value of net_task to modify.
 *  @param[in] state  state value of net_task.
 *    The state value is one of the constants described below.
 *             @li @c DU_NET_TASK_STATE_WAIT_READ : Waiting for reading data
 *             @li @c DU_NET_TASK_STATE_WAIT_WRITE : Waiting for output
 *             @li @c DU_NET_TASK_STATE_WAIT_READ_WRITE : Waiting for reading/writing data
 *             @li @c DU_NET_TASK_STATE_CANCEL : a connection is canceled.
 *             @li @c DU_NET_TASK_STATE_END : completion of reading/writing data.
 *  @param[in] limit_ms an absolute time to wait.
 *  @param[in] handler a function to set in the net_task.
 *  @param[in] arg a parameter for the handler function.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_net_modify_net_task(du_net* x, du_id32 task_id, du_net_task_state state, du_timel limit_ms, du_net_task_handler handler, void* arg);

/**
 *  Removes a task.
 *  @param[in] x pointer to the du_net structure.
 *  @param[in] task_id ID value of net_task to remove.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_net_remove_net_task(du_net* x, du_id32 task_id);

/**
 *  Cancels specified task that is waiting for some event.
 *  @param[in] x pointer to the du_net structure.
 *  @param[in] task_id ID value of net_task to cancel.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_net_cancel_net_task(du_net* x, du_id32 task_id);

/**
 *  Invokes task handlers if it receives network events.
 *  @param[in] x pointer to the du_net structure.
 *  @param[in] timeout_ms the length of time in milliseconds which
 *    <b>du_net_process</b> will wait for events before returning.
 *    A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 *  @see du_net_task, <b>du_net_set_net_task</b>
 */
extern du_bool du_net_process(du_net* x, du_int32 timeout_ms);

/**
 *  Cancels tasks.
 *   <b>du_net_process</b> returns immediately.
 *  @param[in] x pointer to the du_net structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @see <b>du_net_process</b>
 *  @remark This function is multithread safe.
 */
extern du_bool du_net_cancel(du_net* x);

/**
 *  Gets a task that matches a specified task_id.
 *  @param[in] x pointer to the du_net structure.
 *  @param[in] task_id ID value of net_task to get.
 *  @param[out] net_task pointer to the du_net_task structure.
 *  @return  true if the function succeeds and net_task found.
 *           false if the function fails or net_task not found.
 *  @remark This function is multithread safe.
 */
extern du_bool du_net_get_net_task(du_net* x, du_id32 task_id, du_net_task* net_task);

#ifdef __cplusplus
}
#endif

#endif
