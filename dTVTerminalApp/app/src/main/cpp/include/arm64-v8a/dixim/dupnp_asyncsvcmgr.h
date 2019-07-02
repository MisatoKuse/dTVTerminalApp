/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_ASYNCSVCMGR_H
#define DUPNP_ASYNCSVCMGR_H

#include <dupnp_impl.h>
#include <dupnp_asyncsvcmgr_info.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_asyncsvcmgr_init(dupnp_asyncsvcmgr* x, dupnp_svcmgr* svcmgr);

extern void dupnp_asyncsvcmgr_free(dupnp_asyncsvcmgr* x);

extern du_bool dupnp_asyncsvcmgr_generate_id(dupnp_asyncsvcmgr* x, du_uint32* id);

extern du_bool dupnp_asyncsvcmgr_set(dupnp_asyncsvcmgr* x, dupnp_asyncsvcmgr_info* info);

extern du_bool dupnp_asyncsvcmgr_remove(dupnp_asyncsvcmgr* x, du_uint32 id);

#ifdef __cplusplus
}
#endif

#endif
