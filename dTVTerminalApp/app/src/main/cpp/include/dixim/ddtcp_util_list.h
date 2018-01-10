/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_util_list.h 3135 2009-01-09 19:17:24Z gondo $ 
 */ 

#ifndef DDTCP_UTIL_LIST_H
#define DDTCP_UTIL_LIST_H

#include <ddtcp.h>
#include <ddtcp_util_list.h>
#include <ddtcp_func.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct ddtcp_util_list {
    void* data;
    du_uint32 size;
    struct ddtcp_util_list* next;
} ddtcp_util_list;

extern ddtcp_ret ddtcp_util_list_add_empty_entry(ddtcp_util_list** list, void** entry, du_uint32 entry_data_size);
extern ddtcp_ret ddtcp_util_list_add_entry(ddtcp_util_list** list, void** entry, void* data, du_uint32 size);
extern du_bool ddtcp_util_list_entry_is_in_list(ddtcp_util_list* list, void* entry);
extern du_bool ddtcp_util_list_get_count(ddtcp_util_list* list, du_uint32* count);
extern void ddtcp_util_list_remove_entry(ddtcp_util_list** list, void* entry);
extern void ddtcp_util_list_remove_entry_data(ddtcp_util_list** list, void* data, du_uint32 size);
extern void ddtcp_util_list_free(ddtcp_util_list** list);

#ifdef __cplusplus
}
#endif

#endif
