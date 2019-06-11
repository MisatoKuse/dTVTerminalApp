/*
 * Copyright (c) 2018 DigiOn, Inc. All rights reserved.
 */

/**
 * @file dupnp_dvc_evtmgr_sub_state.h
 */

#ifndef DUPNP_DVC_EVTMGR_SUB_STATE_H
#define DUPNP_DVC_EVTMGR_SUB_STATE_H

#include <du_type.h>
#include <dupnp_dvc_evtmgr_sub_info.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Enumeration of the state of subscription.
 */
typedef enum dupnp_dvc_evtmgr_sub_state_type {
    DUPNP_DVC_EVTMGR_SUB_STATE_TYPE_UNKNOWN,        /**< Unknown */
    DUPNP_DVC_EVTMGR_SUB_STATE_TYPE_SUBSCRIBE,      /**< Subscribed */
    DUPNP_DVC_EVTMGR_SUB_STATE_TYPE_UNSUBSCRIBE,    /**< Unsubscribed */
    DUPNP_DVC_EVTMGR_SUB_STATE_TYPE_RENEW,          /**< Renewed */
    DUPNP_DVC_EVTMGR_SUB_STATE_TYPE_EXPIRE,         /**< Expired */
} dupnp_dvc_evtmgr_sub_state_type;

/**
 * An interface definition of a handler.
 * <b>dupnp_dvc_evtmgr_sub_state_handler</b> function is an application-defined
 *   callback function that is called back after changing subscription state.
 * @param[in] type state of subscription.
 * @param[in] info information of subscription.
 * @param[in] arg a parameter for the handler function.
 */
typedef void (*dupnp_dvc_evtmgr_sub_state_handler)(dupnp_dvc_evtmgr_sub_state_type type, const dupnp_dvc_evtmgr_sub_info* info, void* arg);

#ifdef __cplusplus
}
#endif

#endif
