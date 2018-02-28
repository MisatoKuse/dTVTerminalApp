/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include "dmp_ui_browse_view.h"
#include "dmp_ui_metadata_view.h"
#include <dav_cds.h>
#include <dav_urn.h>
#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include <dupnp_urn.h>
#include <stdio.h>
#include "dvcdsc_device.h"

#define MAX_EVENT_HOLDING_SECONDS 10

static void simple_help() {
    puts("{help, (q)uit, cd [index], cd .., set [index]}");
    puts("{(n)ext, (p)revious, home, ls}");
    printf("\n");
}

static const du_uchar* get_root_container() {
    return DU_UCHAR_CONST("0");
}

static const du_uchar* get_object_list_filter() {
    /*
     * "res" specified at the top is a workaround for the following Media
     * Sharing problem of WindowsMediaPlayer11(WMP11).
     * WMP11 issue: When res@XXX (without "res") is specified for Filter
     * forbrowsing, URI cannot be retrieved since WMP11 returns null character
     * as ares element value of the item.
     * Workaround: Added "res" to the Filter. Normally, "res" is not needed
     * when specifying "res@XXX", as described in DLNA Guideline7.3.63.2.
     * Also, when "res" is specified, its response always contains
     * "res@protocolInfo". Therefore, this Filter does not specify res@protocolInfo.
     */
    return DU_UCHAR_CONST("res,res@duration,res@bitrate,res@sampleFrequency,res@bitsPerSample,res@nrAudioChannels,res@size,res@colorDepth,res@resolution,res@dlna:ifoFileURI,upnp:album,dc:date,dc:creator,upnp:originalTrackNumber,upnp:albumArtURI,upnp:albumArtURI@dlna:profileID");
}

static const du_uchar* get_default_sort_criteria() {
    return DU_UCHAR_CONST("");
}

static const du_uchar* get_browse_metadata_filter() {
    return DU_UCHAR_CONST("*");
}

static void cancel_update_check_task(dmp_ui_browse_view* bv) {
    if (!bv->_event_update_check_task_id) return;
    if (!dupnp_schedtaskmgr_remove_scheduled_task(bv->_event_schedtaskmgr, bv->_event_update_check_task_id)) return;
    bv->_event_update_check_task_id = 0;
}

static const du_uchar* get_current_container(dmp_ui_browse_view* bv) {
    du_uint i;

    i = du_str_array_length(&bv->_view_history);
    if (!i) return 0;
    return du_str_array_get_pos(&bv->_view_history, i - 1);
}

static du_bool change_current_container(dmp_ui_browse_view* bv, const du_uchar* container_id) {
    if (!du_str_array_cato(&bv->_view_history, container_id)) return 0;
    return 1;
}

static du_bool go_back_history(dmp_ui_browse_view* bv) {
    du_uint i;

    i = du_str_array_length(&bv->_view_history);
    if (!i) return 0;
    if (!du_str_array_truncate_length(&bv->_view_history, i - 1)) return 0;
    return 1;
}

static du_bool set_root_container(dmp_ui_browse_view* bv) {
    du_str_array_truncate(&bv->_view_history);
    if (!du_str_array_cato(&bv->_view_history, get_root_container())) return 0;
    return 1;
}

static void reset_container_data(dmp_ui_browse_view* bv) {
    bv->_view_starting_index = 0;
    bv->_view_object_count = 0;
    bv->_view_total_objects = 0;
    bv->_view_update_id = 0;

    dav_didl_object_array_truncate_object(&bv->_view_object_list);
    dav_didl_object_free(&bv->_view_bm_object);

    bv->_event_list_updated = 0;
}

static du_bool reset_browse_data(dmp_ui_browse_view* bv) {
    reset_container_data(bv);
    if (!set_root_container(bv)) return 0;
    return 1;
}

void print_error_status(soap_response* sr) {
    if (sr->http_error != DU_SOCKET_ERROR_NONE) {
        printf("http error code: %u\n", sr->http_error);
    }
    if (sr->http_status) {
        printf("http error status: %s\n", sr->http_status);
    }
    if (sr->soap_error_code) {
        printf("soap error code: %s\n", sr->soap_error_code);
    }
}

static du_bool get_object_by_index(dmp_ui_browse_view* bv, du_uint index, dav_didl_object** object) {
    dav_didl_object* _object;

    if (index < bv->_view_starting_index) return 0;
    if (bv->_view_starting_index + bv->_view_object_count <= index) return 0;
    index -= bv->_view_starting_index;

    _object = dav_didl_object_array_get(&bv->_view_object_list);
    *object = _object + index;

    return 1;
}

static du_bool show_current_device_visitor(dupnp_cp_dvcmgr_device* device, void* arg) {
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;
    du_bool* found = (du_bool*)arg;

    *found = 1;
    printf("friendly name: %s\n", dd->friendly_name);
    printf("udn: %s\n", device->udn);
    return 0;
}

static void command_current_dms(dmp_ui_browse_view* bv) {
    du_bool found = 0;
    const du_uchar* udn;

    if (!(udn = cds_browser_get_udn(bv->_browse_cb))) goto not_found;
    if (!dupnp_cp_dvcmgr_visit_udn(bv->_dm, udn, show_current_device_visitor, (void*)&found)) goto error;
    if (!found) goto not_found;
    return;

not_found:
    puts("not found");
    return;

error:
    puts("error");
}

static const du_uchar* get_object_type(dav_didl_object* object) {
    dav_didl_object_property* upnp_class;

    upnp_class = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_upnp_class(), 0);
    if (!upnp_class) return DU_UCHAR_CONST("U");
    if (dav_didl_derived_from(upnp_class->value, dav_didl_class_container())) return DU_UCHAR_CONST("C");
    if (dav_didl_derived_from(upnp_class->value, dav_didl_class_item())) return DU_UCHAR_CONST("I");
    return DU_UCHAR_CONST("U");
}

static du_bool show_object(dmp_ui_browse_view* bv, dav_didl_object* object, du_uint current_index) {
    dav_didl_object_property* property;
    const du_uchar* id;
    const du_uchar* dc_title;
    const du_uchar* upnp_class;
    const du_uchar* object_type;
    du_bool supported = 0;

    id = dav_didl_object_attribute_list_get_attribute_value(object->attr_list, dav_didl_attribute_id());
    if (!id) goto error;

    property = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_dc_title(), 0);
    if (!property) goto error;
    dc_title = property->value;

    property = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_upnp_class(), 0);
    if (!property) return 0;
    upnp_class = property->value;

    object_type = get_object_type(object);

    if (dav_didl_derived_from(upnp_class, dav_didl_class_item())) {
        if (player_sort_resource(bv->_p, object)) {
            supported = player_is_supported(bv->_p, object);
        }
    } else if (dav_didl_derived_from(upnp_class, dav_didl_class_container())){
        supported = 1;
    }

    printf("%d. [%s%s] %s\n", bv->_view_starting_index + current_index, object_type, supported ? DU_UCHAR_CONST("") : DU_UCHAR_CONST("x"), dc_title);
    return 1;

error:
    puts("error");
    return 0;
}

static void show_object_list(dmp_ui_browse_view* bv) {
    du_uint32 i;
    dav_didl_object* object;

    dmp_ui_clear_screen();
    dmp_ui_print_bar();

    object = dav_didl_object_array_get(&bv->_view_object_list);
    for (i = 0; i < bv->_view_object_count; ++i) {
        if (!show_object(bv, object + i, i)) goto error;
    }

    dmp_ui_print_bar();
    printf("container id: %s\n", get_current_container(bv));
    if (bv->_view_object_count) {
        printf("index: %u-%u, total: %u objects\n", bv->_view_starting_index, bv->_view_starting_index + bv->_view_object_count - 1, bv->_view_total_objects);
    } else {
        puts("no objects");
    }
    simple_help();
    return;

error:
    puts("error");
}

static du_bool set_object_list(dmp_ui_browse_view* bv, cds_browser_list_response* lr) {
    dav_didl_object_array_truncate_object(&bv->_view_object_list);
    if (!dav_didl_object_array_cat(&bv->_view_object_list, lr->object_list)) return 0;
    dav_didl_object_array_truncate(lr->object_list);

    bv->_view_starting_index = lr->starting_index;
    bv->_view_object_count = lr->number_returned;
    bv->_view_total_objects = lr->total_matches;
    bv->_view_update_id = lr->update_id;
    bv->_event_list_updated = 0;

    return 1;
}

static void browse_direct_children_response_handler(soap_response* sr, cds_browser_list_response* lr, void* arg) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)arg;

    if (sr->error_occurred) {
        if (sr->http_error == DU_SOCKET_ERROR_CANCELED) return;
        goto error;
    }

    if (!du_mutex_lock(&bv->_mutex)) goto error2;

    if (!set_object_list(bv, lr)) goto error3;
    show_object_list(bv);

    du_mutex_unlock(&bv->_mutex);

    return;

error3:
    du_mutex_unlock(&bv->_mutex);
error2:
    dav_didl_object_array_truncate_object(lr->object_list);
error:
    dmp_ui_clear_screen();
    dmp_ui_print_bar();
    puts("browse_direct_children error");
    print_error_status(sr);
}

static du_bool update_page(dmp_ui_browse_view* bv, du_uint32 starting_index, du_uint32 requested_count) {
    const du_uchar* container_id;

    container_id = get_current_container(bv);

    cancel_update_check_task(bv);
    if (!cds_browser_browse_direct_children(bv->_browse_cb, container_id, starting_index, requested_count, get_default_sort_criteria(), get_object_list_filter(), &bv->_browse_object_list, browse_direct_children_response_handler, bv)) return 0;
    return 1;
}

static void command_ls(dmp_ui_browse_view* bv) {
    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (bv->_view_starting_index == 0 && bv->_view_object_count) {
        show_object_list(bv);
        du_mutex_unlock(&bv->_mutex);
        return;
    }

    if (!update_page(bv, 0, ITEM_COUNT_IN_LIST)) goto error2;

    du_mutex_unlock(&bv->_mutex);
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static void command_next_page(dmp_ui_browse_view* bv) {
    du_uint new_starting_index;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (bv->_view_total_objects <= bv->_view_starting_index + ITEM_COUNT_IN_LIST) goto not_found;
    new_starting_index = bv->_view_starting_index + ITEM_COUNT_IN_LIST;
    if (!update_page(bv, new_starting_index, ITEM_COUNT_IN_LIST)) goto error2;

    du_mutex_unlock(&bv->_mutex);
    return;

not_found:
    du_mutex_unlock(&bv->_mutex);
    puts("not found");
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static void command_previous_page(dmp_ui_browse_view* bv) {
    du_uint new_starting_index;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (bv->_view_starting_index < ITEM_COUNT_IN_LIST) goto not_found;
    new_starting_index = bv->_view_starting_index - ITEM_COUNT_IN_LIST;
    if (!update_page(bv, new_starting_index, ITEM_COUNT_IN_LIST)) goto error2;

    du_mutex_unlock(&bv->_mutex);
    return;

not_found:
    du_mutex_unlock(&bv->_mutex);
    puts("not found");
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static du_bool update_current_page(dmp_ui_browse_view* bv) {
    return update_page(bv, bv->_view_starting_index, ITEM_COUNT_IN_LIST);
}

static du_bool set_container(dmp_ui_browse_view* bv, dav_didl_object* object) {
    const du_uchar* id;
    dav_didl_object_property* upnp_class;

    upnp_class = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_upnp_class(), 0);
    if (!upnp_class) return 0;
    if (!dav_didl_derived_from(upnp_class->value, dav_didl_class_container())) return 0;
    id = dav_didl_object_attribute_list_get_attribute_value(object->attr_list, dav_didl_attribute_id());
    if (!id) return 0;
    if (!change_current_container(bv, id)) return 0;
    reset_container_data(bv);

    return 1;
}

static void change_container(dmp_ui_browse_view* bv, du_uint index) {
    dav_didl_object* object;

    if (!get_object_by_index(bv, index, &object)) goto not_found;
    if (!set_container(bv, object)) goto error;
    if (!update_page(bv, 0, ITEM_COUNT_IN_LIST)) goto error;
    if (!update_current_page(bv)) goto error;

    return;

not_found:
    puts("not found");
    return;

error:
    puts("error");
}

static du_bool release_dms(dmp_ui_browse_view* bv) {
    cds_browser_unsubscribe_dms(bv->_event_cb);
    cancel_update_check_task(bv);
    if (!cds_browser_cancel_request(bv->_browse_cb)) return 0;
    if (!cds_browser_cancel_request(bv->_event_cb)) return 0;

    if (!du_mutex_lock(&bv->_mutex)) return 0;
    du_uchar_array_truncate(&bv->_udn);
    du_mutex_unlock(&bv->_mutex);

    return 1;
}

static du_bool change_view_to_dms(dmp_ui_browse_view* bv) {
    dmp_ui* ui = bv->_ui;

    if (!release_dms(bv)) return 0;
    dmp_ui_change_view_to_dms(ui);
    return 1;
}

static void cd_up(dmp_ui_browse_view* bv) {
    const du_uchar* current_container;

    current_container = get_current_container(bv);
    if (du_str_diff(current_container, get_root_container())) {
        if (!go_back_history(bv)) goto error;
        reset_container_data(bv);
        if (!update_current_page(bv)) goto error;
    } else {
        if (!change_view_to_dms(bv)) goto error;
    }
    return;

error:
    puts("error");
}

static void command_cd(dmp_ui_browse_view* bv, const du_uchar* index) {
    du_uint index_num;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (du_str_equal(index, DU_UCHAR_CONST(".."))) {
        cd_up(bv);
    } else {
        if (!du_str_scan_uint(index, &index_num)) goto error2;
        change_container(bv, index_num);
    }

    du_mutex_unlock(&bv->_mutex);
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static void browse_metadata_response_handler(soap_response* sr, cds_browser_metadata_response* mr, void* arg) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)arg;
    dmp_ui* ui = bv->_ui;

    if (sr->error_occurred) {
        if (sr->http_error == DU_SOCKET_ERROR_CANCELED) return;
        goto error;
    }

    dmp_ui_metadata_view_set_object(dmp_ui_get_metadata_view(ui), mr->object);
    dmp_ui_change_view_to_metadata(ui);

    return;

error:
    dmp_ui_clear_screen();
    dmp_ui_print_bar();
    puts("browse_metadata error");
    print_error_status(sr);
}

static du_bool browse_metadata(dmp_ui_browse_view* bv, dav_didl_object* object) {
    const du_uchar* id;

    id = dav_didl_object_attribute_list_get_attribute_value(object->attr_list, dav_didl_attribute_id());
    if (!id) return 0;
    dav_didl_object_free(&bv->_view_bm_object);
    if (!cds_browser_browse_metadata(bv->_browse_cb, id, get_browse_metadata_filter(), &bv->_view_bm_object, browse_metadata_response_handler, bv)) return 0;
    return 1;
}

static void command_browse_metadata(dmp_ui_browse_view* bv, const du_uchar* index, du_bool show_verbosely) {
    dav_didl_object* object;
    du_uint index_num;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (!du_str_scan_uint(index, &index_num)) goto error2;
    if (!get_object_by_index(bv, index_num, &object)) goto error2;

    dmp_ui_metadata_view_set_show_verbosely(dmp_ui_get_metadata_view(bv->_ui), show_verbosely);

    if (!browse_metadata(bv, object)) goto error2;

    du_mutex_unlock(&bv->_mutex);
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static du_bool container_updated(dmp_ui_browse_view* bv, cds_browser_metadata_response* mr) {
    if (du_str_diff(get_current_container(bv), mr->object_id)) return 0;
    if (mr->update_id == bv->_view_update_id) return 0;
    return 1;
}

static void browse_metadata_response_handler_for_event(soap_response* sr, cds_browser_metadata_response* mr, void* arg){
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)arg;
    dmp_ui* ui = bv->_ui;

    if (sr->error_occurred) {
        if (sr->http_error == DU_SOCKET_ERROR_CANCELED) return;
        goto error;
    }

    if (!du_mutex_lock(&bv->_mutex)) goto error2;

    if (!container_updated(bv, mr)) goto cancel;

    if (dmp_ui_get_current_view(ui) == dmp_ui_get_browse_view(ui)) {
        if (!cds_browser_browse_direct_children(bv->_event_cb, get_current_container(bv), bv->_view_starting_index, ITEM_COUNT_IN_LIST, get_default_sort_criteria(), get_object_list_filter(), &bv->_event_object_list, browse_direct_children_response_handler, bv)) goto error3;
    } else {
        bv->_event_list_updated = 1;
    }

    du_mutex_unlock(&bv->_mutex);
    dav_didl_object_free(mr->object);
    return;

cancel:
    du_mutex_unlock(&bv->_mutex);
    dav_didl_object_free(mr->object);
    return;

error3:
    du_mutex_unlock(&bv->_mutex);
error2:
    dav_didl_object_free(mr->object);
error:
    puts("event error");
    print_error_status(sr);
    bv->_event_last_update_check_time = 0;
}

static void check_container_update_task(du_time now, du_time* time, void* arg) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)arg;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (!cds_browser_browse_metadata(bv->_event_cb, get_current_container(bv), DU_UCHAR_CONST(""), &bv->_event_bm_object, browse_metadata_response_handler_for_event, bv)) goto error2;
    bv->_event_last_update_check_time = now;
    bv->_event_update_check_task_id = 0;

    du_mutex_unlock(&bv->_mutex);
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    bv->_event_update_check_task_id = 0;
    puts("event error");
}

static void event_response_handler(cds_browser_event_response* er, void* arg) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)arg;
    du_time now;
    du_time check_time;

    if (er->error_occurred) goto error;
    if (er->error != DUPNP_CP_EVENT_ERROR_NONE) goto error;
    if (er->initial_event) return;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (bv->_event_update_check_task_id) {
        du_mutex_unlock(&bv->_mutex);
        return;
    }

    du_time_sec(&now);
    if (bv->_event_last_update_check_time + MAX_EVENT_HOLDING_SECONDS < now) {
        check_time = now;
    } else {
        check_time = bv->_event_last_update_check_time + MAX_EVENT_HOLDING_SECONDS;
    }
    du_scheduled_task_set_time(&bv->_event_update_check_task, check_time);
    if (!dupnp_schedtaskmgr_register_scheduled_task(bv->_event_schedtaskmgr, &bv->_event_update_check_task, &bv->_event_update_check_task_id)) goto error2;

    du_mutex_unlock(&bv->_mutex);
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("event error");
}

du_bool dmp_ui_browse_view_set_dms(dmp_ui_view* view, const du_uchar* udn, const du_uchar* control_url, const du_uchar* event_sub_url) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)view->_user_data;

    if (!reset_browse_data(bv)) return 0;
    if (!cds_browser_set_dms(bv->_browse_cb, udn, control_url, 0, 0, 0)) return 0;
    if (!cds_browser_set_dms(bv->_event_cb, udn, control_url, event_sub_url, event_response_handler, bv)) return 0;

    if (!du_mutex_lock(&bv->_mutex)) return 0;
    if (!du_uchar_array_copys0(&bv->_udn, udn)) goto error;
    du_mutex_unlock(&bv->_mutex);

    return 1;

error:
    du_mutex_unlock(&bv->_mutex);
    return 0;
}

static void command_set_media(dmp_ui_browse_view* bv, const du_uchar* index) {
    dav_didl_object* object;
    du_uint index_num;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (!du_str_scan_uint(index, &index_num)) goto error2;
    if (!get_object_by_index(bv, index_num, &object)) goto error2;

    if (!player_is_supported(bv->_p, object)) goto unsupported;
    if (!player_set_media(bv->_p, 0, object)) goto error2;

    du_mutex_unlock(&bv->_mutex);

    dmp_ui_change_view_to_player(bv->_ui);
    return;

unsupported:
    du_mutex_unlock(&bv->_mutex);
    puts("error: unsupported format");
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static void command_home(dmp_ui_browse_view* bv) {
    if (!change_view_to_dms(bv)) goto error;
    return;

error:
    puts("error");
}

static void update_screen(void* user_data) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)user_data;

    if (!du_mutex_lock(&bv->_mutex)) goto error;

    if (bv->_view_object_count && !bv->_event_list_updated) {
        show_object_list(bv);
    } else {
        if (!update_current_page(bv)) goto error2;
    }

    du_mutex_unlock(&bv->_mutex);
    return;

error2:
    du_mutex_unlock(&bv->_mutex);
error:
    puts("error");
}

static void help() {
    dmp_ui_print_bar();
    puts("available commands:");
    puts("  ls");
    puts("  cd index");
    puts("  cd ..");
    puts("  n(next_page)");
    puts("  p(previous_page)");
    puts("  home");
    puts("  current_dms");
    puts("  bm(browse_metabv) index [show_verbosely]");
    puts("  set(set_media) index");
    puts("  q(quit)");
    puts("  help");
    dmp_ui_print_bar();
}

void command_browse(void* user_data, const du_uint argc, const du_uchar* argv[]) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)user_data;
    const du_uchar* command = argv[0];

    if (du_str_equal(command, DU_UCHAR_CONST("home"))) {
        command_home(bv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("cd"))) {
        const du_uchar* index;

        if (argc < 2) goto help;
        index = argv[1];
        command_cd(bv, index);
    } else if (du_str_equal(command, DU_UCHAR_CONST("ls"))) {
        command_ls(bv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("n")) || du_str_equal(command, DU_UCHAR_CONST("next_page"))) {
        command_next_page(bv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("p")) || du_str_equal(command, DU_UCHAR_CONST("previous_page"))) {
        command_previous_page(bv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("bm")) || du_str_equal(command, DU_UCHAR_CONST("browse_metadata"))) {
        const du_uchar* index;

        if (argc < 2) {
            goto help;
        } else if (argc == 2) {
            index = argv[1];
            command_browse_metadata(bv, index, 0);
        } else {
            index = argv[1];
            command_browse_metadata(bv, index, 1);
        }
    } else if (du_str_equal(command, DU_UCHAR_CONST("set")) || du_str_equal(command, DU_UCHAR_CONST("set_media"))) {
        const du_uchar* index;

        if (argc < 2) goto help;
        index = argv[1];
        command_set_media(bv, index);
    } else if (du_str_equal(command, DU_UCHAR_CONST("cm")) || du_str_equal(command, DU_UCHAR_CONST("current_dms"))) {
        command_current_dms(bv);
    } else {
        goto help;
    }
    return;

help:
    help();
}

du_bool dmp_ui_browse_view_init(dmp_ui_view* view, dmp_ui* ui, cds_browser* cb_browse, cds_browser* cb_event, player* p, dmp_event_adapter* ea, dupnp_schedtaskmgr* event_schedtaskmgr, dav_capability* thumbnail_cap, dupnp_cp_dvcmgr* dm) {
    dmp_ui_browse_view* bv = 0;

    bv = (dmp_ui_browse_view*)du_alloc_zero(sizeof(dmp_ui_browse_view));
    if (!bv) return 0;

    if (!du_mutex_create(&bv->_mutex)) goto error;

    bv->_ui = ui;
    bv->_p = p;
    bv->_thumbnail_cap = thumbnail_cap;
    bv->_dm = dm;

    du_str_array_init(&bv->_view_history);
    dav_didl_object_array_init(&bv->_view_object_list);
    dav_didl_object_init(&bv->_view_bm_object);

    du_uchar_array_init(&bv->_udn);

    bv->_browse_cb = cb_browse;
    dav_didl_object_array_init(&bv->_browse_object_list);

    bv->_event_cb = cb_event;
    dav_didl_object_array_init(&bv->_event_object_list);
    dav_didl_object_init(&bv->_event_bm_object);

    if (!du_scheduled_task_init(&bv->_event_update_check_task, check_container_update_task, bv, 0)) goto error2;
    bv->_event_schedtaskmgr = event_schedtaskmgr;

    view->_user_data = bv;
    view->_command = command_browse;
    view->_update_screen = update_screen;

    if (!reset_browse_data(bv)) goto error3;

    return 1;

error3:
    du_scheduled_task_free(&bv->_event_update_check_task);
error2:
    du_mutex_free(&bv->_mutex);
    du_str_array_free(&bv->_view_history);
    dav_didl_object_array_free_object(&bv->_view_object_list);
    dav_didl_object_free(&bv->_view_bm_object);
    dav_didl_object_array_free_object(&bv->_browse_object_list);
    dav_didl_object_array_free_object(&bv->_event_object_list);
    dav_didl_object_free(&bv->_event_bm_object);
    du_uchar_array_free(&bv->_udn);
error:
    du_alloc_free(bv);
    return 0;
}

void dmp_ui_browse_view_free(dmp_ui_view* view) {
    dmp_ui_browse_view* bv = (dmp_ui_browse_view*)view->_user_data;

    du_mutex_free(&bv->_mutex);
    du_str_array_free(&bv->_view_history);
    dav_didl_object_array_free_object(&bv->_view_object_list);
    dav_didl_object_free(&bv->_view_bm_object);
    dav_didl_object_array_free_object(&bv->_browse_object_list);
    dav_didl_object_array_free_object(&bv->_event_object_list);
    dav_didl_object_free(&bv->_event_bm_object);
    du_uchar_array_free(&bv->_udn);
    du_scheduled_task_free(&bv->_event_update_check_task);
    du_alloc_free(bv);
}
