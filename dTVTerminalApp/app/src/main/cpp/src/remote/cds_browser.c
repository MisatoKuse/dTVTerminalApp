/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <dupnp_gena.h>
#include <dupnp_type.h>
#include <du_param.h>
#include <dupnp.h>
#include <dav_cds.h>
#include <dav_didl_object.h>
#include <dav_didl_parser_packed.h>
#include <dav_urn.h>
#include <dupnp_soap.h>
#include <du_http.h>
#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include "dmp.h"
#include "cds_browser.h"

du_bool cds_browser_init(cds_browser* cb, dupnp* upnp, dupnp_cp_evtmgr* em, const du_uchar* user_agent) {
    du_byte_zero((du_uint8*)cb, sizeof(cds_browser));

    if (!du_mutex_create(&cb->_mutex)) return 0;
    if (!du_sync_create(&cb->_sync)) goto error;

    du_uchar_array_init(&cb->_udn);
    du_uchar_array_init(&cb->_control_url);
    du_uchar_array_init(&cb->_event_sub_url);

    du_str_array_init(&cb->_request_header);
    du_str_array_init(&cb->_response_header);

    du_uchar_array_init(&cb->_uchar_array);
    du_str_array_init(&cb->_prop_array);

    du_uchar_array_init(&cb->_bm_object_id);

    du_uchar_array_init(&cb->_container_id);
    du_uchar_array_init(&cb->_filter);
    du_uchar_array_init(&cb->_search_criteria);
    du_uchar_array_init(&cb->_sort_criteria);

    cb->_upnp = upnp;
    cb->_em = em;

    if (!dupnp_soap_header_set_content_type(&cb->_request_header)) goto error2;
    if (!dupnp_soap_header_set_user_agent(&cb->_request_header, user_agent)) goto error2;

    return 1;

error2:
    du_uchar_array_free(&cb->_udn);
    du_uchar_array_free(&cb->_control_url);
    du_uchar_array_free(&cb->_event_sub_url);

    du_str_array_free(&cb->_request_header);
    du_str_array_free(&cb->_response_header);

    du_uchar_array_free(&cb->_uchar_array);
    du_str_array_free(&cb->_prop_array);

    du_uchar_array_free(&cb->_bm_object_id);

    du_uchar_array_free(&cb->_container_id);
    du_uchar_array_free(&cb->_filter);
    du_uchar_array_free(&cb->_search_criteria);
    du_uchar_array_free(&cb->_sort_criteria);

    du_sync_free(&cb->_sync);
error:
    du_mutex_free(&cb->_mutex);
    return 0;
}

void cds_browser_free(cds_browser* cb) {
    du_uchar_array_free(&cb->_udn);
    du_uchar_array_free(&cb->_control_url);
    du_uchar_array_free(&cb->_event_sub_url);

    du_str_array_free(&cb->_request_header);
    du_str_array_free(&cb->_response_header);

    du_uchar_array_free(&cb->_uchar_array);
    du_str_array_free(&cb->_prop_array);

    du_uchar_array_free(&cb->_bm_object_id);

    du_uchar_array_free(&cb->_container_id);
    du_uchar_array_free(&cb->_filter);
    du_uchar_array_free(&cb->_search_criteria);
    du_uchar_array_free(&cb->_sort_criteria);

    du_sync_free(&cb->_sync);
    du_mutex_free(&cb->_mutex);
}

static du_bool parse_notify(dupnp_cp_event_info* info, du_str_array* prop_array, du_uint32* system_update_id, const du_uchar** container_update_ids) {
    const du_uchar* id;

    if (!dupnp_gena_parse_notify(prop_array, info->property_set_xml, info->property_set_xml_size)) return 0;

    id = du_param_get_value_by_name(prop_array, DU_UCHAR_CONST("SystemUpdateID"));
    if (!id) return 0;
    if (!du_str_scan_uint32(id, system_update_id)) return 0;
    *container_update_ids = du_param_get_value_by_name(prop_array, DU_UCHAR_CONST("ContainerUpdateIDs"));
    return 1;
}

static du_bool init_event_response(dupnp_cp_event_info* info, cds_browser_event_response* er, du_str_array* prop_array) {
    du_byte_zero((du_uint8*)er, sizeof(cds_browser_event_response));

    er->header = info->header;
    er->error = info->error;
    if (er->error != DUPNP_CP_EVENT_ERROR_NONE) return 0;
    if (!parse_notify(info, prop_array, &er->system_update_id, &er->container_update_ids)) return 0;
    if (!info->seq) er->initial_event = 1;
    return 1;
}

static void event_handler(dupnp_cp_event_info* info, void* arg) {
    cds_browser* cb = (cds_browser*)arg;
    cds_browser_event_response er;

    if (!init_event_response(info, &er, &cb->_prop_array)) goto error;
    if (cb->_event_response_handler) cb->_event_response_handler(&er, cb->_event_response_handler_arg);
    return;

error:
    er.error_occurred = 1;
    if (cb->_event_response_handler) cb->_event_response_handler(&er, cb->_event_response_handler_arg);

}

du_bool cds_browser_set_dms(cds_browser* cb, const du_uchar* udn, const du_uchar* control_url, const du_uchar* event_sub_url, cds_browser_event_response_handler handler, void* handler_arg) {
    du_uchar_array_truncate(&cb->_event_sub_url);
    if (!du_uchar_array_copys0(&cb->_udn, udn)) return 0;
    if (!du_uchar_array_copys0(&cb->_control_url, control_url)) return 0;
    cb->_event_response_handler = handler;
    cb->_event_response_handler_arg = handler_arg;
    if (event_sub_url) {
        if (!handler) return 0;
        if (!du_uchar_array_cats0(&cb->_event_sub_url, event_sub_url)) return 0;
        dupnp_cp_evtmgr_remove(cb->_em, event_sub_url);
        dupnp_cp_evtmgr_subscribe(cb->_em, event_sub_url, event_handler, cb);
    }

    return 1;
}

void cds_browser_unsubscribe_dms(cds_browser* cb) {
    if (!du_uchar_array_length(&cb->_event_sub_url)) return;
    dupnp_cp_evtmgr_unsubscribe(cb->_em, du_uchar_array_get(&cb->_event_sub_url));
}

const du_uchar* cds_browser_get_udn(cds_browser* cb) {
    if (!du_uchar_array_length(&cb->_udn)) return 0;
    return du_uchar_array_get(&cb->_udn);
}

static du_bool cancel(cds_browser* cb) {
    du_bool r = 0;

    if (cb->_connection_id != DUPNP_INVALID_ID) {
        dupnp_http_cancel(cb->_upnp, cb->_connection_id);
        while (cb->_connection_id != DUPNP_INVALID_ID) {
            r = du_sync_wait(&cb->_sync, &cb->_mutex);
        }
    }
    return r;
}

static void init_soap_response(dupnp_http_response* response, soap_response* sr) {
    du_byte_zero((du_uint8*)sr, sizeof(soap_response));
    sr->http_error = response->error;
    sr->http_status = response->status;
    sr->http_response_header = response->header;
}

static void init_metadata_response(cds_browser_metadata_response* mr, dav_didl_object* object) {
    du_byte_zero((du_uint8*)mr, sizeof(cds_browser_metadata_response));
    mr->object = object;
}

static void set_metadata_response(cds_browser_metadata_response* mr, const du_uchar* object_id, du_uint32 update_id) {
    mr->object_id = object_id;
    mr->update_id = update_id;
}

static du_bool browse_metadata_response_processor(dupnp_http_response* response, cds_browser* cb, du_uchar_array* object_id, soap_response* sr, cds_browser_metadata_response* mr) {
    const du_uchar* result;
    du_uint32 number_returned;
    du_uint32 total_matches;
    du_uint32 update_id;
    dav_didl_object_array object_list;
    dav_didl_object* object;
    du_uint32 len;

    if (du_str_equal(sr->http_status, du_http_status_internal_server_error())) {
        dupnp_soap_parse_error_response(response->body, response->body_size, &cb->_response_header, &sr->soap_error_code, &sr->soap_error_description);
        return 0;
    }
    if (!du_http_status_is_successful(sr->http_status)) return 0;

    if (!dav_cds_parse_browse_response(response->body, response->body_size, &cb->_prop_array, &result, &number_returned, &total_matches, &update_id)) return 0;
    if (number_returned != 1 || total_matches != 1) return 0;

    dav_didl_object_array_init(&object_list);
    if (!dav_didl_parser_packed_parse(result, du_str_len(result), &object_list)) return 0;
    len = dav_didl_object_array_length(&object_list);
    if (len != 1) goto error;

    object = dav_didl_object_array_get(&object_list);
    *cb->_bm_object = *object;
    dav_didl_object_array_free(&object_list);
    set_metadata_response(mr, du_uchar_array_get(object_id), update_id);
    return 1;

error:
    dav_didl_object_array_free_object(&object_list);
    return 0;
}

static void browse_metadata_response_handler(dupnp_http_response* response, void* arg) {
    cds_browser* cb = (cds_browser*)arg;
    soap_response sr;
    cds_browser_metadata_response mr;
    cds_browser_browse_metadata_response_handler handler;
    void* handler_arg;

    handler = cb->_browse_metadata_response_handler;
    handler_arg = cb->_browse_metadata_response_handler_arg;

    if (!du_mutex_lock(&cb->_mutex)) {
        cb->_connection_id = DUPNP_INVALID_ID;
        du_sync_notify(&cb->_sync);
        goto error;
    }
    cb->_connection_id = DUPNP_INVALID_ID;

    init_soap_response(response, &sr);
    init_metadata_response(&mr, cb->_bm_object);
    if (sr.http_error) goto error2;

    if (!browse_metadata_response_processor(response, cb, &cb->_bm_object_id, &sr, &mr)) goto error2;
    cb->_browse_in_progress = 0;
    du_sync_notify(&cb->_sync);
    du_mutex_unlock(&cb->_mutex);

    if (handler) handler(&sr, &mr, handler_arg);
    return;

error2:
    cb->_browse_in_progress = 0;
    du_sync_notify(&cb->_sync);
    du_mutex_unlock(&cb->_mutex);
error:
    sr.error_occurred = 1;
    if (handler) handler(&sr, &mr, handler_arg);
}

static du_bool browse_metadata(cds_browser* cb, const du_uchar* object_id, const du_uchar* filter) {
    if (!du_uchar_array_length(&cb->_control_url)) return 0;
    if (!dupnp_soap_header_set_soapaction(&cb->_request_header, dav_urn_cds(1), DU_UCHAR_CONST("Browse"))) return 0;
    if (!dav_cds_make_browse(&cb->_uchar_array, 1, object_id, DU_UCHAR_CONST("BrowseMetadata"), filter, 0, 0, DU_UCHAR_CONST(""))) return 0;
    if (!dupnp_http_soap(cb->_upnp, du_uchar_array_get(&cb->_control_url), &cb->_request_header, du_uchar_array_get(&cb->_uchar_array), du_uchar_array_length(&cb->_uchar_array), SOAP_TIMEOUT_MS, browse_metadata_response_handler, cb, &cb->_connection_id)) return 0;
    return 1;
}

du_bool cds_browser_browse_metadata(cds_browser* cb, const du_uchar* object_id, const du_uchar* filter, dav_didl_object* object, cds_browser_browse_metadata_response_handler response_handler, void* response_handler_arg) {
    if (!du_mutex_lock(&cb->_mutex)) return 0;
    if (cb->_browse_in_progress) {
        if (!cancel(cb)) goto error;
    }

    if (!du_uchar_array_copys0(&cb->_bm_object_id, object_id)) goto error;
    cb->_bm_object = object;
    cb->_browse_metadata_response_handler = response_handler;
    cb->_browse_metadata_response_handler_arg = response_handler_arg;

    if (!browse_metadata(cb, object_id, filter)) goto error;
    cb->_browse_in_progress = 1;
    du_mutex_unlock(&cb->_mutex);

    return 1;

error:
    du_mutex_unlock(&cb->_mutex);
    return 0;
}

typedef du_bool (*response_parser)(const du_uchar* xml, du_uint32 xml_len, du_str_array* param_array, const du_uchar** result, du_uint32* number_returned, du_uint32* total_matches, du_uint32* update_id);

typedef du_bool (*action_requester)(cds_browser* cb);

static du_bool parse_object_list_response(cds_browser* cb, dupnp_http_response* response, response_parser parser, soap_response* sr, du_uint32* update_id, du_bool* completed) {
    const du_uchar* result;
    du_uint32 number_returned;
    du_uint32 previous_number_returned;

    if (du_str_equal(sr->http_status, du_http_status_internal_server_error())) {
        if (!dupnp_soap_parse_error_response(response->body, response->body_size, &cb->_response_header, &sr->soap_error_code, &sr->soap_error_description)) return 0;
        if (!cb->_requested_count && du_str_equal(sr->soap_error_code, dav_cds_error_code_cannot_process_the_request())) {
            *completed = 1;
            return 1;
        }
        return 0;
    }
    if (!du_http_status_is_successful(sr->http_status)) return 0;

    if (!parser(response->body, response->body_size, &cb->_prop_array, &result, &number_returned, &cb->_total_matches, update_id)) return 0;

    if (!cb->_number_returned) {
        // first response
        dav_didl_object_array_truncate_object(cb->_object_list);
        cb->_container_update_id = *update_id;
    }
    if (cb->_container_update_id != *update_id || !number_returned) {
        *completed = 1;
        return 1;
    }
    if (cb->_requested_count < number_returned) return 0;

    previous_number_returned = dav_didl_object_array_length(cb->_object_list);
    if (!dav_didl_parser_packed_parse(result, du_str_len(result), cb->_object_list)) return 0;
    cb->_number_returned = dav_didl_object_array_length(cb->_object_list);

    if (number_returned && previous_number_returned == cb->_number_returned) {
        // Workaround for server that responses 0 < NumberReturned and empty DIDL.
        *completed = 1;
        return 1;
    }

    if (cb->_total_matches && cb->_total_matches < cb->_requested_count) {
        cb->_requested_count = cb->_total_matches;
    }

    if (!cb->_total_matches) return 1;
    if (cb->_starting_index + cb->_number_returned < cb->_total_matches) {
        if (!cb->_requested_count) return 1;
        if (cb->_number_returned < cb->_requested_count) return 1;
    }

    *completed = 1;
    return 1;
}

static void init_list_response(cds_browser_list_response* lr, dav_didl_object_array* object_list){
    du_byte_zero((du_uint8*)lr, sizeof(cds_browser_list_response));
    lr->object_list = object_list;
}

static void set_list_response(cds_browser_list_response* lr, const du_uchar* container_id, du_uint32 starting_index, du_uint32 number_returned, du_uint32 total_matches, du_uint32 update_id) {
    lr->container_id = container_id;
    lr->starting_index = starting_index;
    lr->number_returned = number_returned;
    lr->total_matches = total_matches;
    lr->update_id = update_id;
}

static void object_list_response_processor(dupnp_http_response* response, void* arg, response_parser parser, action_requester requester, cds_browser_object_list_response_handler response_handler, void* response_handler_arg) {
    cds_browser* cb = (cds_browser*)arg;
    cds_browser_list_response lr;
    soap_response sr;
    du_uint32 update_id;
    du_bool completed = 0;

    if (!du_mutex_lock(&cb->_mutex)) {
        cb->_connection_id = DUPNP_INVALID_ID;
        du_sync_notify(&cb->_sync);
        goto error;
    }
    cb->_connection_id = DUPNP_INVALID_ID;

    init_soap_response(response, &sr);
    init_list_response(&lr, cb->_object_list);
    if (sr.http_error) goto error2;

    if (!parse_object_list_response(cb, response, parser, &sr, &update_id, &completed)) goto error2;
    if (!completed) {
        du_mutex_unlock(&cb->_mutex);
        requester(cb);
        return;
    }
    cb->_browse_in_progress = 0;
    cb->_list_data_available = 1;
    set_list_response(&lr, du_uchar_array_get(&cb->_container_id), cb->_starting_index, cb->_number_returned, cb->_total_matches, update_id);
    du_sync_notify(&cb->_sync);
    du_mutex_unlock(&cb->_mutex);

    if (response_handler) response_handler(&sr, &lr, response_handler_arg);
    return;

error2:
    cb->_browse_in_progress = 0;
    dav_didl_object_array_truncate_object(cb->_object_list);
    du_sync_notify(&cb->_sync);
    du_mutex_unlock(&cb->_mutex);
error:
    sr.error_occurred = 1;
    if (response_handler) response_handler(&sr, &lr, response_handler_arg);
}

static du_bool browse_direct_children(cds_browser* cb, const du_uchar* container_id, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria);

static du_bool browse_direct_children_requester(cds_browser* cb) {
    if (!du_mutex_lock(&cb->_mutex)) return 0;
    if (!browse_direct_children(cb, du_uchar_array_get(&cb->_container_id), cb->_starting_index + cb->_number_returned, cb->_requested_count ? cb->_requested_count - cb->_number_returned : 0, du_uchar_array_get(&cb->_sort_criteria))) goto error;
    du_mutex_unlock(&cb->_mutex);
    return 1;

error:
    du_mutex_unlock(&cb->_mutex);
    return 0;
}

static void browse_direct_children_response_handler(dupnp_http_response* response, void* arg) {
    cds_browser* cb = (cds_browser*)arg;

    object_list_response_processor(response, arg, dav_cds_parse_browse_response, browse_direct_children_requester, cb->_browse_direct_children_response_handler, cb->_browse_direct_children_response_handler_arg);
}

static du_bool browse_direct_children(cds_browser* cb, const du_uchar* container_id, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria) {
    if (!dupnp_soap_header_set_soapaction(&cb->_request_header, dav_urn_cds(1), DU_UCHAR_CONST("Browse"))) return 0;
    if (!dav_cds_make_browse(&cb->_uchar_array, 1, container_id, DU_UCHAR_CONST("BrowseDirectChildren"), du_uchar_array_get(&cb->_filter), starting_index, requested_count, sort_criteria)) return 0;
    if (!dupnp_http_soap(cb->_upnp, du_uchar_array_get(&cb->_control_url), &cb->_request_header, du_uchar_array_get(&cb->_uchar_array), du_uchar_array_length(&cb->_uchar_array), SOAP_TIMEOUT_MS, browse_direct_children_response_handler, cb, &cb->_connection_id)) return 0;
    return 1;
}

du_bool cds_browser_browse_direct_children(cds_browser* cb, const du_uchar* container_id, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria, const du_uchar* filter, dav_didl_object_array* object_list, cds_browser_object_list_response_handler handler, void* handler_arg) {
    if (!du_mutex_lock(&cb->_mutex)) return 0;
    if (cb->_browse_in_progress) {
        if (!cancel(cb)) goto error;
    }
    if (!du_uchar_array_length(&cb->_control_url)) goto error;
    cb->_starting_index = starting_index;
    cb->_requested_count = requested_count;
    cb->_number_returned = 0;
    if (!du_uchar_array_copys0(&cb->_container_id, container_id)) goto error;
    du_uchar_array_truncate(&cb->_search_criteria);
    if (!du_uchar_array_cat0(&cb->_search_criteria)) goto error;
    if (!du_uchar_array_copys0(&cb->_sort_criteria, sort_criteria)) goto error;
    if (!du_uchar_array_copys0(&cb->_filter, filter)) goto error;
    cb->_object_list = object_list;
    cb->_browse_direct_children_response_handler = handler;
    cb->_browse_direct_children_response_handler_arg = handler_arg;

    cb->_list_data_available = 0;

    if (!browse_direct_children(cb, container_id, starting_index, requested_count, du_uchar_array_get(&cb->_sort_criteria))) goto error;
    cb->_browse_in_progress = 1;
    du_mutex_unlock(&cb->_mutex);
    return 1;

error:
    du_mutex_unlock(&cb->_mutex);
    return 0;
}

static du_bool search(cds_browser* cb, const du_uchar* container_id, const du_uchar* search_criteria, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria);

static du_bool search_requester(cds_browser* cb) {
    if (!du_mutex_lock(&cb->_mutex)) return 0;
    if (!search(cb, du_uchar_array_get(&cb->_container_id), du_uchar_array_get(&cb->_search_criteria), cb->_starting_index + cb->_number_returned, cb->_requested_count ? cb->_requested_count - cb->_number_returned : 0, du_uchar_array_get(&cb->_sort_criteria))) goto error;
    du_mutex_unlock(&cb->_mutex);
    return 1;

error:
    du_mutex_unlock(&cb->_mutex);
    return 0;
}

static void search_response_handler(dupnp_http_response* response, void* arg) {
    cds_browser* cb = (cds_browser*)arg;

    object_list_response_processor(response, arg, dav_cds_parse_search_response, search_requester, cb->_search_response_handler, cb->_search_response_handler_arg);
}

static du_bool search(cds_browser* cb, const du_uchar* container_id, const du_uchar* search_criteria, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria) {
    if (!dupnp_soap_header_set_soapaction(&cb->_request_header, dav_urn_cds(1), DU_UCHAR_CONST("Search"))) return 0;
    if (!dav_cds_make_search(&cb->_uchar_array, 1, container_id, search_criteria, du_uchar_array_get(&cb->_filter), starting_index, requested_count, sort_criteria)) return 0;
    if (!dupnp_http_soap(cb->_upnp, du_uchar_array_get(&cb->_control_url), &cb->_request_header, du_uchar_array_get(&cb->_uchar_array), du_uchar_array_length(&cb->_uchar_array), SOAP_TIMEOUT_MS, search_response_handler, cb, &cb->_connection_id)) return 0;
    return 1;
}

du_bool cds_browser_search(cds_browser* cb, const du_uchar* container_id, const du_uchar* search_criteria, du_uint32 starting_index, du_uint32 requested_count, const du_uchar* sort_criteria, const du_uchar* filter, dav_didl_object_array* object_list, cds_browser_object_list_response_handler handler, void* handler_arg) {
    if (!du_mutex_lock(&cb->_mutex)) return 0;
    if (cb->_browse_in_progress) {
        if (!cancel(cb)) goto error;
    }
    if (!du_uchar_array_length(&cb->_control_url)) goto error;
    cb->_starting_index = starting_index;
    cb->_requested_count = requested_count;
    cb->_number_returned = 0;
    if (!du_uchar_array_copys0(&cb->_container_id, container_id)) goto error;
    if (!du_uchar_array_copys0(&cb->_search_criteria, search_criteria)) goto error;
    if (!du_uchar_array_copys0(&cb->_sort_criteria, sort_criteria)) goto error;
    if (!du_uchar_array_copys0(&cb->_filter, filter)) goto error;
    cb->_object_list = object_list;
    cb->_search_response_handler = handler;
    cb->_search_response_handler_arg = handler_arg;

    cb->_list_data_available = 0;

    if (!search(cb, container_id, search_criteria, starting_index, requested_count, du_uchar_array_get(&cb->_sort_criteria))) goto error;
    cb->_browse_in_progress = 1;
    du_mutex_unlock(&cb->_mutex);
    return 1;

error:
    du_mutex_unlock(&cb->_mutex);
    return 0;
}

du_bool cds_browser_cancel_request(cds_browser* cb) {
    if (!du_mutex_lock(&cb->_mutex)) return 0;
    if (cb->_browse_in_progress) {
        cancel(cb);
    }
    du_mutex_unlock(&cb->_mutex);
    return 1;
}

const du_uchar* cds_browser_get_last_search_criteria(cds_browser* cb) {
    if (!du_uchar_array_length(&cb->_search_criteria)) return 0;

    return du_uchar_array_get(&cb->_search_criteria);
}

const du_uchar* cds_browser_get_last_sort_criteria(cds_browser* cb) {
    if (!du_uchar_array_length(&cb->_sort_criteria)) return 0;

    return du_uchar_array_get(&cb->_sort_criteria);
}
