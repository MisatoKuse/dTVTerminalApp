/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */
/** @file dupnp_dvc_context.h
 *  @brief The dupnp_dvc_context interface provides some methods of UPnP networking for Device
 * (such as setting/getting the connection persistency policy,etc.).
 */

#ifndef DUPNP_DVC_CONTEXT_H
#define DUPNP_DVC_CONTEXT_H

#include <dupnp.h>
#include <du_http_server.h>
#include <du_net_task.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_dvc_context dupnp_dvc_context;

typedef du_bool (*dupnp_dvc_context_timeout_handler)(dupnp_dvc_context* context, void* user_data_by_service, void* user_data_by_request);

/**
 * Policy about connection persistency whether to keep the connection or not.
 */
typedef enum dupnp_dvc_context_persistent_connection_policy {
    DUPNP_DVC_CONTEXT_PERSISTENT_CONNECTION_POLICY_UNKNOWN, /**< Never used. */
    DUPNP_DVC_CONTEXT_PERSISTENT_CONNECTION_POLICY_FORCE_KEEP, /**< Keep the connection. */
    DUPNP_DVC_CONTEXT_PERSISTENT_CONNECTION_POLICY_FORCE_CLOSE, /**< Close the connection. */
    DUPNP_DVC_CONTEXT_PERSISTENT_CONNECTION_POLICY_AUTO /**< Close If the request is HTTP/1.0 or has a Connection: close header. */
} dupnp_dvc_context_persistent_connection_policy;

/**
 * This structure contains the device context information.
 */
struct dupnp_dvc_context {
    dupnp* _upnp;
    du_bool _async_process;
    du_net_task _task;
    du_http_server* _hs;
    du_uint32 _id;
    du_bool _net_task_removed;
};

/**
 * Returns pointer to the du_http_server data stored in the dupnp_dvc_context structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @return pointer to the du_http_server data stored in the dupnp_dvc_context structure.
 */
extern du_http_server* dupnp_dvc_context_get_http_server(dupnp_dvc_context* context);

/**
 * Returns pointer to the dupnp data stored in the dupnp_dvc_context structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @return pointer to the dupnp data stored in the dupnp_dvc_context structure.
 */
extern dupnp* dupnp_dvc_context_get_dupnp(dupnp_dvc_context* context);

/**
 * Sets the policy about connection persistency whether to keep the connection or not.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] policy  policy value about connection persistency.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_context_set_persistent_connection(dupnp_dvc_context* context, dupnp_dvc_context_persistent_connection_policy policy);

/**
 * Checks the policy about connection persistency whether to keep the connection or not.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @return  true if the policy about connection persistency is set to keep the connection.
 *          false otherwise.
 */
extern du_bool dupnp_dvc_context_is_persistent_connection(dupnp_dvc_context* context);

/**
 * Set the state of the service context to completion of reading/writing data.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_context_async_task_completed(dupnp_dvc_context* context);

/**
 * Sets the service invocation mode to asynchronous mode.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[out] id pointer to the storage area to receive the id value of dupnp_dvc_context.
 * @remark This call specifies that the service-process function is invoked asynchronously.
 *         If id is set null, id value is not outputed.
 */
extern void dupnp_dvc_context_set_async_mode(dupnp_dvc_context* context, du_uint32* id);

extern dupnp_dvc_context* dupnp_dvc_context_create(void);

extern du_bool dupnp_dvc_context_set(dupnp_dvc_context* context, dupnp* upnp, du_http_server* hs, du_net_task* task, du_bool async_process);

extern void dupnp_dvc_context_free(dupnp_dvc_context* context);

extern void dupnp_dvc_context_continue_response(dupnp_dvc_context* context);

#ifdef __cplusplus
}
#endif

#endif
