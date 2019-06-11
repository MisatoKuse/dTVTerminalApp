/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_taskmgr.h
 *  @brief The dupnp_taskmgr manages dupnp_nettaskmgr and dupnp_schedtaskmgr
 *  (such as getting the dupnp_nettaskmgr/dupnp_schedtaskmgr, processing the tasks
 *  registered in dupnp_taskmgr).
 */

#ifndef DUPNP_TASKMGR_H
#define DUPNP_TASKMGR_H

#include <dupnp_impl.h>
#include <du_scheduled_task.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_taskmgr_init(dupnp_taskmgr* x, dupnp_impl* upnp);

extern void dupnp_taskmgr_free(dupnp_taskmgr* x);

extern du_bool dupnp_taskmgr_start(dupnp_taskmgr* x);

extern void dupnp_taskmgr_stop(dupnp_taskmgr* x);

extern void dupnp_taskmgr_reset(dupnp_taskmgr* x);

extern du_bool dupnp_taskmgr_is_stopping(dupnp_taskmgr* x);

extern void dupnp_taskmgr_enable_thread(dupnp_taskmgr* x, du_bool flag);

extern void dupnp_taskmgr_set_stack_size(dupnp_taskmgr* x, du_uint32 stack_size);

/**
 * Gets pointer to dupnp_nettaskmgr.
 * @param[in] x  pointer to the <em>dupnp_taskmgr</em> data structure.
 * @return  pointer to the dupnp_nettaskmgr stored in <em>x</em>.
 * @remark Using the pointer to dupnp_nettaskmgr, you can call the APIs to register/modify/remove
 *  <em>du_net_task</em>.
 * @see dupnp_nettaskmgr.h
 */
extern dupnp_nettaskmgr* dupnp_taskmgr_get_nettaskmgr(dupnp_taskmgr* x);

/**
 * Gets pointer to dupnp_schedtaskmgr.
 * @param[in] x  pointer to the <em>dupnp_taskmgr</em> data structure.
 * @return  pointer to the dupnp_schedtaskmgr stored in <em>x</em>.
 * @remark Using the pointer to dupnp_schedtaskmgr, you can call the APIs to register/modify/remove
 *  <em>du_scheduled_task</em>.
 * @see dupnp_schedtaskmgr.h
 */
extern dupnp_schedtaskmgr* dupnp_taskmgr_get_schedtaskmgr(dupnp_taskmgr* x);

/**
 * Gets pointer to du_thread.
 * @param[in] x  pointer to the <em>dupnp_taskmgr</em> data structure.
 * @return  pointer to the du_thread stored in <em>x</em>.
 * @remark If you start the dupnp using dupnp_start_nothread, which doesn't create
 *  a internal thread, you must not call this function.
 */
extern du_thread* dupnp_taskmgr_get_thread(dupnp_taskmgr* x);

/**
 * Checks the tasks registered in <em>x</em> and invokes the handlers required.
 * When you start the dupnp using dupnp_start_nothread(), which does not
 * create a internal thread, you should call this function periodically
 * to execute the internal process of UPnP SDK.
 * @param[in] x  pointer to the <em>dupnp_taskmgr</em> data structure.
 * @param[in] net_process_timeout_ms the length of time in milliseconds which
 *   <em>du_net</em> will wait for events before returning.
 *   A negative value means infinite timeout.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark If you start the dupnp using dupnp_start, which creates
 *  a internal thread, you must not call this function.
 */
extern du_bool dupnp_taskmgr_process(dupnp_taskmgr* x, du_int32 net_process_timeout_ms);

#ifdef __cplusplus
}
#endif

#endif
