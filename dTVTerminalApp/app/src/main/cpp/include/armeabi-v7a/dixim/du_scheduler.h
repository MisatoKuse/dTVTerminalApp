/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_scheduler interface provides some methods for manipulating
 *  the du_scheduled_task.
 *  Before using a du_scheduler, use <b>du_scheduler_init</b> to initialize
 *  du_scheduler data, and use <b>du_scheduler_set_scheduled_task</b> to store
 *  du_scheduled_task data.
 *  Use <b>du_scheduler_free</b> to free the region used by du_scheduler.
 *  @see du_scheduled_task.h
 */

#ifndef DU_SCHEDULER_H
#define DU_SCHEDULER_H

#include <du_mutex.h>
#include <du_scheduled_task_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_scheduler.
 */
typedef struct du_scheduler {
    du_scheduled_task_array scheduled_task_array;
    du_scheduled_task_array scheduled_task_request_array;
    du_mutex mutex;
    du_id32 latest_task_id;
} du_scheduler;

/**
 *  Initializes a du_scheduler data area.
 *  @param[out] x  pointer to the du_scheduler structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_scheduler_init(du_scheduler* x);

/**
 *  Resets x to reuse it.
 *  @param[in] x pointer to the du_scheduler structure.
 */
extern void du_scheduler_reset(du_scheduler* x);

/**
 *  Frees the resources used by x.
 *  @param[in] x pointer to the du_scheduler structure.
 */
extern void du_scheduler_free(du_scheduler* x);

/**
 *  Sets a scheduled task.
 *  @param[in] x pointer to the du_scheduler structure.
 *  @param[in] scheduled_task pointer to du_scheduled_task structure data to store
 *   in x</net.
 *  @param[in,out] task_id pointer to the storage area to receive the ID value of
 *      du_scheduled_task. If you don't need the ID, set null to task_id.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_scheduler_set_scheduled_task(du_scheduler* x, du_scheduled_task* scheduled_task, du_id32* task_id);

/**
 *  Modifies a scheduled time.
 *  @param[in] x pointer to the du_scheduler structure.
 *  @param[in] id  id value to specify the du_scheduled_task data stored in x.
 *  @param[in] t time value to set in the the du_scheduled_task data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_scheduler_modify_scheduled_task(du_scheduler* x, du_uint32 id, du_time t);

/**
 *  Removes a scheduled task.
 *  @param[in] x pointer to the du_scheduler structure.
 *  @param[in] id  id value to specify the du_scheduled_task data stored in x.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_scheduler_remove_scheduled_task(du_scheduler* x, du_uint32 id);

/**
 *  Invokes the handler function if it is time to run.
 *  @param[in] x pointer to the du_scheduler structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This function is multithread safe.
 */
extern du_bool du_scheduler_process(du_scheduler* x);

/**
 *  Gets a scheduled task.
 *  @param[in] x pointer to the du_scheduler structure.
 *  @param[in] id ID value of scheduled_task to get.
 *  @param[out] scheduled_task pointer to the du_scheduled_task structure.
 *  @return  true if the function succeeds and scheduled_task found.
 *           false if the function fails or scheduled_task not found.
 *  @remark This function is multithread safe.
 */
extern du_bool du_scheduler_get_scheduled_task(du_scheduler* x, du_uint32 id, du_scheduled_task* scheduled_task);

#ifdef __cplusplus
}
#endif

#endif
