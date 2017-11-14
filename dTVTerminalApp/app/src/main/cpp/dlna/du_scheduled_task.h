/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_scheduled_task interface provides some methods for manipulating
 *  du_scheduled_task structure(such as initializing/modifying
 *  du_scheduled_task structure).
 *  The du_scheduled_task structure contains a time value, pointer to
 *  a handler function that is invoked at the specified time, etc.
 *  A du_scheduler manages multiple du_scheduled_task, and
 *  du_scheduler_process() manages all du_scheduled_task stored in the du_scheduler structure,
 *  checks each time value of the du_scheduled_task, invokes the handler function of
 *  the du_scheduled_task if it is time to run.
 *  Before using a du_scheduled_task, use <b>du_scheduled_task_init</b> to initialize
 *  du_scheduled_task data.
 *  @see du_scheduler.h
 */

#ifndef DU_SCHEDULED_TASK_H
#define DU_SCHEDULED_TASK_H

#include "du_time.h"
#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Use this value for the time value of scheduled_task_handler if you want to
 *  set a task should be invoked as soon as possible.
 */
#define DU_SCHEDULED_TASK_TIME_ASAP 0

/**
 *  The interface definition of an application-defined function that is invoked at a specified time.
 *  @param[in] now  time this handler invoked.
 *  @param[in,out] time a specified time to invoke this handler.
 *                 If you set DU_SCHEDULED_TASK_TIME_ASAP to time, task is invoked as soon as possible.
 *  @param[in] arg a parameter for this handler.
 */
typedef void (*du_scheduled_task_handler)(du_time now, du_time* time, void* arg);

/**
 *  The interface definition of an application-defined function that is invoked
 *  when du_scheduled_task_free() is called.
 *  @param[in] arg a parameter for this handler.
 */
typedef void (*du_scheduled_task_free_handler)(void* arg);

/**
 *  Structure of du_scheduled_task.
 */
typedef struct du_scheduled_task {
    du_time time;
    du_scheduled_task_handler handler;
    void* arg;
    du_scheduled_task_free_handler free;
    du_id32 id;
    du_bool removed;
} du_scheduled_task;

/**
 *  Initializes a du_scheduled_task.
 *  @param[out] task pointer to the du_scheduled_task structure.
 *  @param[in] handler a scheduled_task_handler function to set in the task.
 *  @param[in] arg a parameter for the handler function.
 *  @param[in] free a scheduled_task_handler function to set in the task.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_scheduled_task_init(du_scheduled_task* task, du_scheduled_task_handler handler, void* arg, du_scheduled_task_free_handler free);

/**
 *  Invokes a scheduled_task_free() handler function.
 *  @param[in] task pointer to the du_scheduled_task structure.
 */
extern void du_scheduled_task_free(du_scheduled_task* task);

/**
 *  Sets a time for the task.
 *  @param[in] task pointer to the du_scheduled_task structure.
 *  @param[in] time time value to invoke a scheduled_task_handler() function.
 *  @remark task is a pointer to a du_scheduled_task structure
 *  initialized by the <b>du_scheduled_task_init</b> function.
 */
extern void du_scheduled_task_set_time(du_scheduled_task* task, du_time time);

/**
 *  Sets an ID for the task.
 *  @param[in] task pointer to the du_scheduled_task structure.
 *  @param[in] id id of the task to set.
 *  @remark task is a pointer to a du_scheduled_task structure
 *  initialized by the <b>du_scheduled_task_init</b> function.
 */
extern void du_scheduled_task_set_id(du_scheduled_task* task, du_id32 id);

#ifdef __cplusplus
}
#endif

#endif
