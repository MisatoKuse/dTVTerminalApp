/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef CDS_BROWSER_H
#define CDS_BROWSER_H

#include <dupnp.h>
#include <dupnp_cp_evtmgr.h>
#include <dav_didl_object.h>
#include <dav_didl_object_array.h>
#include <du_uchar_array.h>
#include <du_sync.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct soap_response {
    du_bool error_occurred;
    du_socket_error http_error;
    du_uchar* http_status;
    const du_str_array* http_response_header;
    const du_uchar* soap_error_code;
    const du_uchar* soap_error_description;
} soap_response;

typedef struct cds_browser_list_response {
    const du_uchar* container_id;
    dav_didl_object_array* object_list;
    du_uint32 starting_index;
    du_uint32 number_returned;
    du_uint32 total_matches;
    du_uint32 update_id;
} cds_browser_list_response;

typedef struct cds_browser_metadata_response {
    const du_uchar* object_id;
    dav_didl_object* object;
    du_uint32 update_id;
} cds_browser_metadata_response;

typedef struct cds_browser_event_response {
    du_bool error_occurred;
    dupnp_cp_event_error error;
    const du_str_array* header;
    du_bool initial_event;
    du_uint32 system_update_id;
    const du_uchar* container_update_ids;
} cds_browser_event_response;

typedef du_bool (*cds_browser_visitor)(dav_didl_object* object, void* arg);

typedef void (*cds_browser_object_list_response_handler)(soap_response* sr, cds_browser_list_response* lr, void* arg);

typedef void (*cds_browser_browse_metadata_response_handler)(soap_response* sr, cds_browser_metadata_response* mr, void* arg);

typedef void (*cds_browser_event_response_handler)(cds_browser_event_response* er, void* arg);

typedef struct cds_browser {
    du_mutex _mutex;

    dupnp* _upnp;
    dupnp_cp_evtmgr* _em;

    du_bool _browse_in_progress;                // need lock
    du_sync _sync;

    du_uint32 _connection_id;
    du_uchar_array _udn;
    du_uchar_array _control_url;
    du_uchar_array _event_sub_url;

    du_str_array _request_header;               // need lock
    du_str_array _response_header;

    du_uchar_array _uchar_array;                // need lock
    du_str_array _prop_array;

    dav_didl_object_array* _object_list;        // need lock
    dav_didl_object* _bm_object;                // need lock
    du_uchar_array _container_id;               // need lock
    du_uchar_array _bm_object_id;               // need lock
    du_uint32 _starting_index;                  // need lock
    du_uint32 _requested_count;                 // need lock
    du_uint32 _number_returned;                 // need lock
    du_uint32 _total_matches;
    du_uint32 _container_update_id;
    du_uchar_array _filter;                     // need lock
    du_uchar_array _search_criteria;            // need lock
    du_uchar_array _sort_criteria;              // need lock
    du_bool _list_data_available;               // need lock

    cds_browser_object_list_response_handler _browse_direct_children_response_handler;
    void* _browse_direct_children_response_handler_arg;
    cds_browser_object_list_response_handler _search_response_handler;
    void* _search_response_handler_arg;
    cds_browser_browse_metadata_response_handler _browse_metadata_response_handler;
    void* _browse_metadata_response_handler_arg;
    cds_browser_event_response_handler _event_response_handler;
    void* _event_response_handler_arg;
} cds_browser;

extern du_bool cds_browser_init(cds_browser* cb, dupnp* upnp, dupnp_cp_evtmgr* em, const du_uchar* user_agent);

extern void cds_browser_free(cds_browser* cb);

extern du_bool cds_browser_set_dms(cds_browser* cb, const du_uchar* udn, const du_uchar* control_url, const du_uchar* event_sub_url, cds_browser_event_response_handler handler, void* handler_arg);

extern void cds_browser_unsubscribe_dms(cds_browser* cb);

extern const du_uchar* cds_browser_get_udn(cds_browser* cb);

extern du_bool cds_browser_browse_metadata(cds_browser* cb, const du_uchar* object_id, const du_uchar* filter, dav_didl_object* object, cds_browser_browse_metadata_response_handler handler, void* handler_arg);

extern du_bool cds_browser_browse_direct_children(cds_browser* cb, const du_uchar* container_id, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria, const du_uchar* filter, dav_didl_object_array* object_list, cds_browser_object_list_response_handler handler, void* handler_arg);

extern du_bool cds_browser_search(cds_browser* cb, const du_uchar* container_id, const du_uchar* search_criteria, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria, const du_uchar* filter, dav_didl_object_array* object_list, cds_browser_object_list_response_handler handler, void* handler_arg);

extern du_bool cds_browser_cancel_request(cds_browser* cb);

extern const du_uchar* cds_browser_get_last_search_criteria(cds_browser* cb);

extern const du_uchar* cds_browser_get_last_sort_criteria(cds_browser* cb);

#ifdef __cplusplus
}
#endif

#endif
