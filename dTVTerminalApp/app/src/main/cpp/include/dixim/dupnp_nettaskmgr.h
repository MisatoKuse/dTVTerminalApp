/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_nettaskmgr.h
 *  @brief The dupnp_nettaskmgr interface provides some methods for manipulating
 * the <em>du_net_task</em>(such as registering/modifying/removing <em>du_net_task</em>).
 */

#ifndef DUPNP_NETTASKMGR_H
#define DUPNP_NETTASKMGR_H

#include <dupnp_impl.h>
#include <du_type.h>
#include <du_net_task.h>
#include <du_socket.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_nettaskmgr_init(dupnp_nettaskmgr* x, dupnp_taskmgr* taskmgr);

extern void dupnp_nettaskmgr_free(dupnp_nettaskmgr* x);

extern du_bool dupnp_nettaskmgr_process(dupnp_nettaskmgr* x, du_int32 net_process_timeout_ms);

extern du_bool dupnp_nettaskmgr_cancel(dupnp_nettaskmgr* x);

extern void dupnp_nettaskmgr_reset(dupnp_nettaskmgr* x);

/**
 * Registers a new <em>du_net_task</em> in <em>x</em>.
 * @param[in] x pointer to the dupnp_nettaskmgr structure.
 * @param[in] state  state value of du_net_task.
 *   The <em>state</em> value is one of the constants described below.
 *            @li @c DU_NET_TASK_STATE_WAIT_READ : Waiting for reading data
 *            @li @c DU_NET_TASK_STATE_WAIT_WRITE : Waiting for output
 *            @li @c DU_NET_TASK_STATE_WAIT_READ_WRITE : Waiting for reading/writing data
 *            @li @c DU_NET_TASK_STATE_TIMEOUTE : a specified timeout has expired.
 *            @li @c DU_NET_TASK_STATE_CANCEL : a connection is canceled.
 *            @li @c U_NETTASK_STATE_END : completion of reading/writing data.
 * @param[in] limit_ms an absolute time to wait.
 * @param[in] socket du_socket data referencing a socket.
 * @param[in] handler a function to set in the <em>du_net_task</em>.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @param[in] cancelable  true if the cancel request of dupnp_http_cancel()
 *    is to be disabled; false otherwise.
 * @param[out] task_id ID value given to the <em>du_net_task</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_nettaskmgr_register_net_task(dupnp_nettaskmgr* x, du_net_task_state state, du_timel limit_ms, du_socket socket, du_net_task_handler handler, void* arg, du_bool cancelable, du_id32* task_id);

/**
 * Modifies a <em>du_net_task</em> stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_nettaskmgr structure.
 * @param[in] state  state value of du_net_task.
 *   The <em>state</em> value is one of the constants described below.
 *            @li @c DU_NET_TASK_STATE_WAIT_READ : Waiting for reading data
 *            @li @c DU_NET_TASK_STATE_WAIT_WRITE : Waiting for output
 *            @li @c DU_NET_TASK_STATE_WAIT_READ_WRITE : Waiting for reading/writing data
 *            @li @c DU_NET_TASK_STATE_TIMEOUTE : a specified timeout has expired.
 *            @li @c DU_NET_TASK_STATE_CANCEL : a connection is canceled.
 *            @li @c U_NETTASK_STATE_END : completion of reading/writing data.
 * @param[in] limit_ms an absolute time to wait.
 * @param[in] task_id ID value of the <em>du_net_task</em> to modify.
 * @param[in] handler a function to set in the <em>du_net_task</em>.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_nettaskmgr_modify_net_task(dupnp_nettaskmgr* x, du_net_task_state state, du_timel limit_ms, du_id32 task_id, du_net_task_handler handler, void* arg);

/**
 * Removes a <em>du_net_task</em> stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_nettaskmgr structure.
 * @param[in] task_id ID value of the <em>du_net_task</em> to remove.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_nettaskmgr_remove_net_task(dupnp_nettaskmgr* x, du_id32 task_id);

/**
 * Cancels a specified <em>du_net_task</em> stored in <em>x</em> that is waiting for some event.
 * @param[in] x pointer to the dupnp_nettaskmgr structure.
 * @param[in] task_id ID value of the <em>du_net_task</em> to cancel.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_nettaskmgr_cancel_net_task(dupnp_nettaskmgr* x, du_id32 task_id);

/**
 * Gets a net_task that matches a specified <em>task_id</em> stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_nettaskmgr structure.
 * @param[in] task_id ID value of <em>net_task</em> to get.
 * @param[out] net_task pointer to the du_net_task structure.
 * @return  true if the function succeeds and net_task found.
 *          false if the function fails or net_task not found.
 */
extern du_bool dupnp_nettaskmgr_get_net_task(dupnp_nettaskmgr* x, du_id32 task_id, du_net_task* net_task);

#ifdef __cplusplus
}
#endif

#endif
