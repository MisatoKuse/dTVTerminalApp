/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#include "dmp_ui_dms_view.h"
#include "dmp_ui_browse_view.h"
#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include <stdio.h>
#include "dmp.h"
#include "dvcdsc_device.h"
#include "local_registration.h"
#include <drag_cp.h>
#include <drag_dms_info.h>


static void simple_help(dmp_ui_dms_view* mv) {
    if (mv->_is_display_remote) {
        puts("{help, (q)uit, ls, rls, conn}");
    } else {
        puts("{help, (q)uit, ls, rls, cd [index], (n)ext, (p)revious, lr [index], ur [index]}");
    }
    printf("\n");
}

static du_bool count_dms(dmp_ui_dms_view* mv, du_uint32* count) {
    if (!dupnp_cp_dvcmgr_count_devices2(mv->_dm, dmp_get_dms_type(), count)) return 0;
    return 1;
}

static void command_count_dms(dmp_ui_dms_view* mv) {
    du_uint32 count = 0;

    if (!count_dms(mv, &count)) goto error;
    printf("*number of media servers: %u\n", count);
    return;

error:
    puts("Error");
}

typedef struct list_devices_context {
    du_uint32 current_index;
    du_uint32 starting_index;
    du_uint32 requested_count;
    du_uint32 number_processed;
    du_uint32 total_devices;
    du_str_array* udn_array;
} list_devices_context;

static du_bool show_device_list_visitor(dupnp_cp_dvcmgr_device* device, void* arg) {
    list_devices_context* context = (list_devices_context*)arg;
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;

    if (context->starting_index <= context->current_index && context->current_index < context->starting_index + context->requested_count) {
        printf("%d. %s", context->current_index, dd->friendly_name);
	if (dd->x_dps.dps_is_v2) printf(" [dlpa2.0]");
	printf("\n");
        ++context->number_processed;
        if (!du_str_array_cato(context->udn_array, device->udn)) goto error;
    }
    ++context->current_index;
    ++context->total_devices;

    return 1;

error:
    puts("Error");
    return 0;
}

static du_bool list_devices(dmp_ui_dms_view* mv, const du_uchar* device_type, du_uint32 starting_index, du_uint32 requested_count) {
    list_devices_context context = {0, starting_index, requested_count, 0, 0, &mv->_udn_array};

    du_str_array_truncate(&mv->_udn_array);

    dmp_ui_clear_screen();
    dmp_ui_print_bar();
    if (!dupnp_cp_dvcmgr_visit_device_type(mv->_dm, device_type, show_device_list_visitor, &context)) goto error;
    if (!context.number_processed) puts("No Devices.");
    dmp_ui_print_bar();

    printf("index: %d-%d, total %d dmss\n", starting_index, starting_index + context.number_processed - 1, context.total_devices);

    mv->_is_display_remote = 0;
    return 1;

error:
    puts("Error");
    return 0;
}

static void command_remove_device(dmp_ui_dms_view* mv, const du_uchar* index, const du_uchar* device_type) {
    const du_uchar* udn;
    du_uint32 index_num;

    if (!du_str_scan_uint32(index, &index_num)) goto error;
    if (index_num < mv->_starting_index) goto error;
    index_num -= mv->_starting_index;
    udn = du_str_array_get_pos(&mv->_udn_array, index_num);
    if (!udn) goto error;
    if (!dupnp_cp_dvcmgr_remove_device(mv->_dm, udn)) goto error;

    return;

error:
    puts("Error");
}

static du_bool list_remote_devices(dmp_ui_dms_view* mv) {
    dms_info_array dia;
    dms_info* info;
    du_uint32 i;
    du_uint32 len;

    du_str_array_truncate(&mv->_udn_array);

    dms_info_array_init(&dia);
    if (!drag_cp_get_dms_list(&dia)) goto error;

    dmp_ui_clear_screen();
    dmp_ui_print_bar();
    puts("REMOTE DEVICES");
    dmp_ui_print_bar();

    len = dms_info_array_length(&dia);
    info = dms_info_array_get(&dia);
    for (i = 0; i < len; ++i, ++info) {
        printf("%d. %s\n", i, info->friendly_name);
        if (!du_str_array_cato(&mv->_udn_array, info->udn)) goto error;
    }
    if (!len) puts("No Devices.");
    dmp_ui_print_bar();

    printf("index: %d-%d, total %d dmss\n", 0, len - 1, len);
    dms_info_array_free(&dia);

    mv->_is_display_remote = 1;

    return 1;

error:
    puts("Error");
    dms_info_array_free(&dia);
    return 0;
}
static void command_ls(dmp_ui_dms_view* mv) {
    list_devices(mv, dmp_get_dms_type(), 0, ITEM_COUNT_IN_LIST);
    mv->_starting_index = 0;
    simple_help(mv);
}

static void command_rls(dmp_ui_dms_view* mv) {
    list_remote_devices(mv);
    simple_help(mv);
}

static void command_next(dmp_ui_dms_view* mv) {
    du_uint32 count;
    du_uint32 new_starting_index;

    if (!count_dms(mv, &count)) goto error;
    new_starting_index = mv->_starting_index + ITEM_COUNT_IN_LIST;
    if (count <= new_starting_index) goto not_found;
    list_devices(mv, dmp_get_dms_type(), new_starting_index, ITEM_COUNT_IN_LIST);
    mv->_starting_index = new_starting_index;
    simple_help(mv);
    return;

not_found:
    puts("Not Found");
    return;
error:
    puts("Error");
}

static void command_previous(dmp_ui_dms_view* mv) {
    du_uint32 new_starting_index;

    if (mv->_starting_index < ITEM_COUNT_IN_LIST) goto not_found;

    new_starting_index = mv->_starting_index - ITEM_COUNT_IN_LIST;
    list_devices(mv, dmp_get_dms_type(), new_starting_index, ITEM_COUNT_IN_LIST);
    mv->_starting_index = new_starting_index;
    simple_help(mv);
    return;

not_found:
    puts("Not Found");
    return;
}

typedef struct select_dms_visitor_context {
    dmp_ui_dms_view* mv;
    const du_uchar* udn;
    du_bool found;
    du_bool succeeded;
} select_dms_visitor_context;

static du_bool select_dms_visitor(dupnp_cp_dvcmgr_device* device, void* arg) {
    select_dms_visitor_context* context = (select_dms_visitor_context*)arg;
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;

    if (du_str_diff(context->udn, device->udn)) return 1;

    context->found = 1;
    if (!dmp_ui_browse_view_set_dms(dmp_ui_get_browse_view(context->mv->_ui), dd->udn, dd->cds.control_url, dd->cds.event_sub_url)) return 0;
    context->succeeded = 1;
    return 0;
}

static void command_cd(dmp_ui_dms_view* mv, const du_uchar* index) {
    dmp_ui* ui = mv->_ui;
    du_uint32 index_num;
    select_dms_visitor_context context;

    du_byte_zero((du_uint8*)&context, sizeof context);
    context.mv = mv;
    if (!du_str_scan_uint32(index, &index_num)) goto error;
    if (index_num < mv->_starting_index) goto error;
    index_num -= mv->_starting_index;
    context.udn = du_str_array_get_pos(&mv->_udn_array, index_num);
    if (!context.udn) goto error;

    if (!dupnp_cp_dvcmgr_visit_device_type(mv->_dm, dmp_get_dms_type(), select_dms_visitor, &context)) goto error;
    if (!context.found) goto not_found;
    if (!context.succeeded) goto error;

    dmp_ui_change_view_to_browse(ui);
    return;

error:
    puts("Error");
    return;

not_found:
    puts("Not Found");
}

static void lr_register_response_handler(du_uint32 requeseted_id, local_registration_error_info* error_info) {
    if (error_info->type == LOCAL_REGISTRATION_ERROR_TYPE_NONE) {
        puts("Finsh PrepareRegistration/Local (Un)Registration");
    } else {
        puts("Error PrepareRegistration/Local (Un)Registration");
        printf("%d\n", error_info->type);
        printf("%s\n", error_info->http_status);
        printf("%s\n", error_info->soap_error_code);
        printf("%s\n", error_info->soap_error_description);
    }
}

typedef struct regist_dms_visitor_context {
    dmp_ui_dms_view* mv;
    const du_uchar* udn;
    du_uchar* control_url;
    du_uchar* dtcp1_host;
    du_uint16 dtcp1_port;
    du_bool is_v2;
    du_bool found;
    du_bool succeeded;
} regist_dms_visitor_context;

static du_bool regist_dms_visitor(dupnp_cp_dvcmgr_device* device, void* arg) {
    regist_dms_visitor_context* context = (regist_dms_visitor_context*)arg;
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;

    if (du_str_diff(context->udn, device->udn)) return 1;

    context->found = 1;
    if (!du_str_clone(dd->x_dps.control_url, &context->control_url)) return 0;
    if (!du_str_clone(dd->rs_regi_socket_host, &context->dtcp1_host)) goto error;
    context->dtcp1_port = dd->rs_regi_socket_port;
    context->is_v2 = dd->x_dps.dps_is_v2;
    context->succeeded = 1;
    return 0;

error:
    du_alloc_free(&context->control_url);
    context->control_url = 0;
    return 0;
}

#ifndef DDTCP_CRYPTO_SHA1_DIGEST_SIZE
#  define DDTCP_CRYPTO_SHA1_DIGEST_SIZE 20
#endif
static void command_lr(dmp_ui_dms_view* mv, const du_uchar* index) {
    du_uint32 index_num;
    regist_dms_visitor_context context;
    du_uchar_array device_id;
    du_uchar_array hash;
    du_uint32 id;
    du_uint32 version;
    du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE];

    du_uchar_array_init(&device_id);
    du_uchar_array_init(&hash);

    du_byte_zero((du_uint8*)&context, sizeof context);
    du_byte_zero((du_uint8*)device_id_hash, DDTCP_CRYPTO_SHA1_DIGEST_SIZE);
    context.mv = mv;
    if (!du_str_scan_uint32(index, &index_num)) goto error;
    if (index_num < mv->_starting_index) goto error;
    index_num -= mv->_starting_index;
    context.udn = du_str_array_get_pos(&mv->_udn_array, index_num);
    if (!context.udn) goto error;

    if (!dupnp_cp_dvcmgr_visit_device_type(mv->_dm, dmp_get_dms_type(), regist_dms_visitor, &context)) goto error;
    if (!context.found) goto not_found;
    if (!context.succeeded) goto error;

    version = context.is_v2 ? 2 : 1; // set DLPA version
    if (!local_registration_prepare_registration(mv->_upnp, mv->user_agent, context.control_url, lr_register_response_handler, &id, version)) goto error2;

#ifdef ENABLE_DTCP
    if (!player_sink_ra_register(mv->_player, context.dtcp1_host, context.dtcp1_port)) goto error2;
    if (!player_get_device_id_hash(mv->_player, &hash)) goto error2;
    if (!du_uchar_array_cat0(&hash)) goto error2;
#endif
    if (!drag_cp_service_init(&device_id, version)) goto error2;
    if (!local_registration_register(mv->_upnp, mv->user_agent, context.control_url, du_uchar_array_get(&device_id),  DU_UCHAR_CONST("DRAG_CP Sample"),
				     du_uchar_array_get(&hash), lr_register_response_handler, &id, version)) goto error2;

    du_uchar_array_free(&hash);
    //du_uchar_array_free(&device_id);
	dms_info_array_free(&device_id);
    du_alloc_free(context.control_url);
    du_alloc_free(context.dtcp1_host);
    return;

error2:
    du_alloc_free(context.control_url);
    du_alloc_free(context.dtcp1_host);
error:
    du_uchar_array_free(&device_id);
    du_uchar_array_free(&hash);
    puts("Error");
    return;

not_found:
    puts("Not Found");
}

static void command_ur(dmp_ui_dms_view* mv, const du_uchar* index) {
    du_uint32 index_num;
    regist_dms_visitor_context context;
    du_uchar_array device_id;
    du_uchar_array hash;
    du_uint32 id;
    du_uint32 version;
    du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE];

    du_uchar_array_init(&device_id);
    du_uchar_array_init(&hash);

    du_byte_zero((du_uint8*)&context, sizeof context);
    du_byte_zero((du_uint8*)device_id_hash, DDTCP_CRYPTO_SHA1_DIGEST_SIZE);
    context.mv = mv;
    if (!du_str_scan_uint32(index, &index_num)) goto error;
    if (index_num < mv->_starting_index) goto error;
    index_num -= mv->_starting_index;
    context.udn = du_str_array_get_pos(&mv->_udn_array, index_num);
    if (!context.udn) goto error;

    if (!dupnp_cp_dvcmgr_visit_device_type(mv->_dm, dmp_get_dms_type(), regist_dms_visitor, &context)) goto error;
    if (!context.found) goto not_found;
    if (!context.succeeded) goto error;

#ifdef ENABLE_DTCP
    if (!player_sink_ra_register(mv->_player, context.dtcp1_host, context.dtcp1_port)) goto error2;
    if (!player_get_device_id_hash(mv->_player, &hash)) goto error2;
    if (!du_uchar_array_cat0(&hash)) goto error2;
#endif
    version = context.is_v2 ? 2 : 1; // set DLPA version
    if (!drag_cp_service_init(&device_id, version)) goto error2;
    if (!local_registration_unregister(mv->_upnp, mv->user_agent, context.control_url, du_uchar_array_get(&device_id),
				       du_uchar_array_get(&hash), lr_register_response_handler, &id, version)) goto error2;

    du_uchar_array_free(&hash);
    //du_uchar_array_free(&device_id);
	dms_info_array_free(&device_id);
    du_alloc_free(context.control_url);
    du_alloc_free(context.dtcp1_host);
    return;

error2:
    du_alloc_free(context.control_url);
    du_alloc_free(context.dtcp1_host);
error:
    du_uchar_array_free(&device_id);
    du_uchar_array_free(&hash);
    puts("Error");
    return;

not_found:
    puts("Not Found");
}

static void command_conn(dmp_ui_dms_view* mv, const du_uchar* index) {
    du_uint32 index_num;
    du_uchar* udn;

    if (!du_str_scan_uint32(index, &index_num)) goto error;
    udn = du_str_array_get_pos(&mv->_udn_array, index_num);
    if (!drag_cp_connect_to_dms(udn)) goto error;

    return;

error:
    puts("Error");
    return;
}

static void command_disconn(dmp_ui_dms_view* mv, const du_uchar* index) {
    du_uint32 index_num;
    du_uchar* udn;

    if (!du_str_scan_uint32(index, &index_num)) goto error;
    udn = du_str_array_get_pos(&mv->_udn_array, index_num);
    if (!drag_cp_disconnect_from_dms(udn)) goto error;

    return;

error:
    puts("Error");
    return;
}
static void help(dmp_ui_dms_view* mv) {
    dmp_ui_print_bar();
    puts("available commands:");
    puts("  ls");
    puts("  rls");
    if (mv->_is_display_remote) {
        puts("  conn index");
        puts("  disconn index");
    } else {
        puts("  cd index");
        puts("  n(next_page)");
        puts("  p(previous_page)");
        puts("  rm(remove_dms) index");
        puts("  nm(count_dms)");
        puts("  cm(current_dms)");
        puts("  lr(local_registration) index");
    }
    puts("  q(quit)");
    puts("  help");
    dmp_ui_print_bar();
}

void command_dms(void* user_data, const du_uint argc, const du_uchar* argv[]) {
    dmp_ui_dms_view* mv = (dmp_ui_dms_view*)user_data;
    const du_uchar* command = argv[0];

    if (du_str_equal(command, DU_UCHAR_CONST("nm")) || du_str_equal(command, DU_UCHAR_CONST("count_dms"))) {
        if (mv->_is_display_remote) goto help;
        command_count_dms(mv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("rm")) || du_str_equal(command, DU_UCHAR_CONST("remove_dms"))) {
        const du_uchar* index;

        if (mv->_is_display_remote) goto help;
        if (argc < 2) goto help;
        index = argv[1];
        command_remove_device(mv, index, dmp_get_dms_type());
    } else if (du_str_equal(command, DU_UCHAR_CONST("cd"))) {
        const du_uchar* index;

        if (mv->_is_display_remote) goto help;
        if (argc < 2) goto help;
        index = argv[1];
        command_cd(mv, index);
    } else if (du_str_equal(command, DU_UCHAR_CONST("ls"))) {
        command_ls(mv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("rls"))) {
        command_rls(mv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("conn"))) {
        const du_uchar* index;

        if (!mv->_is_display_remote) goto help;
        if (argc < 2) goto help;
        index = argv[1];
        command_conn(mv, index);
    } else if (du_str_equal(command, DU_UCHAR_CONST("disconn"))) {
        const du_uchar* index;

        if (!mv->_is_display_remote) goto help;
        if (argc < 2) goto help;
        index = argv[1];
        command_disconn(mv, index);
    } else if (du_str_equal(command, DU_UCHAR_CONST("n")) || du_str_equal(command, DU_UCHAR_CONST("next_page"))) {
        if (mv->_is_display_remote) goto help;
        command_next(mv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("p")) || du_str_equal(command, DU_UCHAR_CONST("previous_page"))) {
        if (mv->_is_display_remote) goto help;
        command_previous(mv);
    } else if (du_str_equal(command, DU_UCHAR_CONST("lr")) || du_str_equal(command, DU_UCHAR_CONST("local_registration"))) {
        const du_uchar* index;

        if (mv->_is_display_remote) goto help;
        if (argc < 2) goto help;
        index = argv[1];
        command_lr(mv, index);
    } else if (du_str_equal(command, DU_UCHAR_CONST("ur"))) {
        const du_uchar* index;

        if (mv->_is_display_remote) goto help;
        if (argc < 2) goto help;
        index = argv[1];
        command_ur(mv, index);
    } else {
        goto help;
    }
    return;

help:
    help(mv);
}

static void update_screen(void* user_data) {
    dmp_ui_dms_view* mv = (dmp_ui_dms_view*)user_data;

    command_ls(mv);
}

du_bool dmp_ui_dms_view_init(dmp_ui_view* view, dmp_ui* ui, dupnp* upnp, dupnp_cp_dvcmgr* dm, player* p, const du_uchar* user_agent) {
    dmp_ui_dms_view* mv = 0;

    mv = du_alloc_zero(sizeof(dmp_ui_dms_view));
    if (!mv) return 0;
    mv->_ui = ui;
    mv->_upnp = upnp;
    mv->user_agent = user_agent;
    mv->_dm = dm;
    mv->_player = p;

    du_str_array_init(&mv->_udn_array);

    view->_user_data = mv;
    view->_command = command_dms;
    view->_update_screen = update_screen;
    return 1;
}

void dmp_ui_dms_view_free(dmp_ui_view* view) {
    dmp_ui_dms_view* mv = view->_user_data;

    du_str_array_free(&mv->_udn_array);
    du_alloc_free(mv);
}
