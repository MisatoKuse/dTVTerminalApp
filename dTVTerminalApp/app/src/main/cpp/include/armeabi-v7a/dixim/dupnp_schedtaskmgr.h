/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_schedtaskmgr.h
 *  @brief The dupnp_schedtaskmgr interface provides some methods for manipulating
 * the <em>du_scheduled_task</em>(such as registering/modifying/removing
 * <em>du_scheduled_task</em>).
 */

#ifndef DUPNP_SCHEDTASKMGR_H
#define DUPNP_SCHEDTASKMGR_H

#include <dupnp_impl.h>
#include <du_type.h>
#include <du_scheduled_task.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Initializes a scheduled task manager structure.
 * @param[in,out] x a pointer to a dupnp_schedtaskmgr structure
 * @param[in] taskmgr a pointer to a task manager.
 */
extern du_bool dupnp_schedtaskmgr_init(dupnp_schedtaskmgr* x, dupnp_taskmgr* taskmgr);

/**
 * Frees a scheduled task manager structure.
 * @param[in,out] x a pointer to a dupnp_schedtaskmgr structure
 */
extern void dupnp_schedtaskmgr_free(dupnp_schedtaskmgr* x);

/**
 * Resets a scheduled task manager structure. You can reuse the
 * same dupnp_schedtaskmgr instance after calling this function.
 * @param[in,out] x a pointer to a dupnp_schedtaskmgr structure
 */
extern void dupnp_schedtaskmgr_reset(dupnp_schedtaskmgr* x);

/**
 * Proccesses the scheduled tasks.
 * @param[in,out] x a pointer to a dupnp_schedtaskmgr structure
 */
extern du_bool dupnp_schedtaskmgr_process(dupnp_schedtaskmgr* x);

/**
 * Registers a <em>du_scheduled_task</em> in <em>x</em>.
 * @param[in] x pointer to the dupnp_schedtaskmgr structure.
 * @param[in] scheduled_task pointer to the du_scheduled_task structure.
 * @param[out] id ID value given to the registered <em>scheduled_task</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_schedtaskmgr_register_scheduled_task(dupnp_schedtaskmgr* x, du_scheduled_task* scheduled_task, du_uint32* id);

/**
 * Modifies a <em>du_scheduled_task</em> stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_schedtaskmgr structure.
 * @param[in] id ID value of the <em>du_scheduled_task</em> to modify.
 * @param[in] time time value to invoke a handler function of the <em>du_scheduled_task</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_schedtaskmgr_modify_scheduled_task(dupnp_schedtaskmgr* x, du_uint32 id, du_time time);

/**
 * Removes a <em>du_scheduled_task</em> stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_schedtaskmgr structure.
 * @param[in] id ID value of the <em>du_scheduled_task</em> to remove.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_schedtaskmgr_remove_scheduled_task(dupnp_schedtaskmgr* x, du_uint32 id);

/**
 * Gets a scheduled_task that matches a specified <em>id</em> stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_schedtaskmgr structure.
 * @param[in] id ID value of <em>scheduled_task</em> to get.
 * @param[out] scheduled_task pointer to the du_scheduled_task structure.
 * @return  true if the function succeeds and scheduled_task found.
 *          false if the function fails or scheduled_task not found.
 */
extern du_bool dupnp_schedtaskmgr_get_scheduled_task(dupnp_schedtaskmgr* x, du_uint32 id, du_scheduled_task* scheduled_task);

#ifdef __cplusplus
}
#endif

#endif
