/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_ASYNCSVCMGR_INFO_H
#define DUPNP_ASYNCSVCMGR_INFO_H

#include <dupnp_dvc_context.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void (*dupnp_asyncsvcmgr_info_request_free)(void*);

typedef struct dupnp_asyncsvcmgr_info {
    du_uint32 id;
    dupnp_dvc_context* context;
    void* request;
    dupnp_asyncsvcmgr_info_request_free request_free;
} dupnp_asyncsvcmgr_info;

extern void dupnp_asyncsvcmgr_info_init(dupnp_asyncsvcmgr_info* x, dupnp_asyncsvcmgr_info_request_free request_free);

extern void dupnp_asyncsvcmgr_info_free(dupnp_asyncsvcmgr_info* x);

#ifdef __cplusplus
}
#endif

#endif
