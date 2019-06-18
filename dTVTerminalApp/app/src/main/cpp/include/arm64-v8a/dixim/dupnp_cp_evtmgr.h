/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_cp_evtmgr.h
 *  @brief
 * The dupnp_cp_evtmgr interface provides some methods to manipulate the
 * dupnp_cp_evtmgr structure that stores the event-subscribing status of
 *   the events interested in.
 * UPnP Control Point can manage the eventing more easy using dupnp_cp_evtmgr.
 * The start and stop calling sequence of dupnp and dupnp_cp_evtmgr functions
 * must be the following sequence.<br>
 *   ....<br>
 *  dupnp_start();<br>
 *  dupnp_cp_evtmgr_start(); : must be called after dupnp_start() <br>
 *   ....<br>
 *  dupnp_cp_evtmgr_stop();  : must be called before dupnp_stop()<br>
 *  dupnp_stop();<br>
 */

#ifndef DUPNP_CP_EVTMGR_H
#define DUPNP_CP_EVTMGR_H

#include <dupnp_impl.h>
#include <dupnp_cp.h>
#include <dupnp_cp_evtmgr_sub_info_array.h>
#include <du_sync.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * A event manager structure.
 */
typedef struct dupnp_cp_evtmgr {
    dupnp_impl* _upnp;
    du_bool _running;
    du_mutex _mutex;
    dupnp_cp_evtmgr_sub_info_array _si_array;
    dupnp_cp_evtmgr_sub_info_array _tmp_si_array;
    du_uchar* _callback_url_path;

    du_sync _sync;
    du_uint32 _unsubscribe_count;
    du_uint32 _unsubscribe_all_timeout_ms;
} dupnp_cp_evtmgr;

/**
 * Creates and initializes <em>evtmgr</em>.
 * @param[out] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_cp_evtmgr_init(dupnp_cp_evtmgr* evtmgr, dupnp* upnp);

/**
 * Frees the region used by <em>evtmgr</em>.
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 */
extern void dupnp_cp_evtmgr_free(dupnp_cp_evtmgr* evtmgr);

/**
 * Starts dupnp_cp_evtmgr.
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark You must call this function after dupnp started.
 */
extern du_bool dupnp_cp_evtmgr_start(dupnp_cp_evtmgr* evtmgr);

/**
 * Stops dupnp_cp_evtmgr.
 * Send the canceling subscription message to all URLs that is still
 *   managed by dupnp_cp_evtmgr.
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark You must call this function before dupnp stops.
 */
extern void dupnp_cp_evtmgr_stop(dupnp_cp_evtmgr* evtmgr);

/**
 * Sends the subscription message defined by GENA with method SUBSCRIBE
 * to the specified <em>event_sub_url</em>.
 * Starts the management of the eventing of <em>event_sub_url</em>.
 * If the event received, event handler <em>handler</em> will be invoked.
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @param[in] event_sub_url the URL for eventing.
 * @param[in] handler event handler function.
 *   dupnp_cp_gena_event_handler() function is an application-defined
 *   callback function that processes a GENA event message sent from
 *   UPnP Devices.
 * @param[in] arg a parameter for the <em>event_handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_evtmgr_subscribe(dupnp_cp_evtmgr* evtmgr, const du_uchar* event_sub_url, dupnp_cp_gena_event_handler handler, void* arg);

/**
 * Sends the canceling a subscription message defined by GENA with method
 * UNSUBSCRIBE to the specified <em>event_sub_url</em>.
 * Stops the management of the eventing of <em>event_sub_url</em>.
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @param[in] event_sub_url the URL for eventing.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_evtmgr_unsubscribe(dupnp_cp_evtmgr* evtmgr, const du_uchar* event_sub_url);

/**
 * Stops the management of the eventing of <em>event_sub_url</em>.
 * This method does not send the canceling a subscription message
 * defined by GENA with method UNSUBSCRIBE to the specified
 * <em>event_sub_url</em>.
 * Use this method if sending the UNSUBSCRIBE message is meaningless
 * in such a case device has stopped (ssdp:byebye message has been sent).
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @param[in] event_sub_url the URL for eventing.
 * @remark This function is multithread safe.
 */
extern void dupnp_cp_evtmgr_remove(dupnp_cp_evtmgr* evtmgr, const du_uchar* event_sub_url);

/**
 * Sets the length of time in milliseconds which will be waited for stopping dupnp_cp_evtmgr
 * with dupnp_cp_evtmgr_stop().
 * @param[in] evtmgr  pointer to the <em>dupnp_cp_evtmgr</em> data structure.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for stopping
 *    dupnp_cp_evtmgr.
 * @remark Default value is 3000 milliseconds.
 */
extern void dupnp_cp_evtmgr_set_stop_timeout(dupnp_cp_evtmgr* evtmgr, du_uint32 timeout_ms);

#ifdef __cplusplus
}
#endif

#endif
