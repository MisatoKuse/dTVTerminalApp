/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <dupnp_cp.h>
#include <du_file_input_buffer.h>
#include <du_file.h>
#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include <stdio.h>
#include "dmp_ui.h"
#include "dmp_ui_dms_view.h"
#include "dmp_ui_browse_view.h"
#include "dmp_ui_metadata_view.h"
#include "dmp_ui_player_view.h"
#include "dmp.h"
#include "dvcdsc_device.h"

#define TOKEN_NUM 5
#define THUBNAIL_WIDTH 160
#define THUMBNAIL_HEIGHT 120

void dmp_ui_print_bar() {
    puts("----------------------------------------");
}

void dmp_ui_clear_screen() {
    printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
}

static void change_view(dmp_ui* ui, dmp_ui_view* new_view) {
    ui->_current_view = new_view;
    ui->_current_view->_update_screen(ui->_current_view->_user_data);
}

void dmp_ui_change_view_to_dms(dmp_ui* ui) {
    change_view(ui, &ui->_dms_view);
}

void dmp_ui_change_view_to_browse(dmp_ui* ui) {
    change_view(ui, &ui->_browse_view);
}

void dmp_ui_change_view_to_metadata(dmp_ui* ui) {
    change_view(ui, &ui->_metadata_view);
}

void dmp_ui_change_view_to_player(dmp_ui* ui) {
    change_view(ui, &ui->_player_view);
}

dmp_ui_view* dmp_ui_get_current_view(dmp_ui* ui) {
    return ui->_current_view;
}

dmp_ui_view* dmp_ui_get_dms_view(dmp_ui* ui) {
    return &ui->_dms_view;
}

dmp_ui_view* dmp_ui_get_browse_view(dmp_ui* ui) {
    return &ui->_browse_view;
}

dmp_ui_view* dmp_ui_get_metadata_view(dmp_ui* ui) {
    return &ui->_metadata_view;
}

dmp_ui_view* dmp_ui_get_player_view(dmp_ui* ui) {
    return &ui->_player_view;
}

static du_bool read_line(du_file_input_buffer* fb, du_uchar line[], du_uint32 line_size) {
#ifdef WINCE
    gets(line);
#else
    if (!du_file_input_buffer_read_line(fb, line, line_size)) return 0;
#endif
    return 1;
}

static void command(dmp_ui* ui) {
    du_file_input_buffer fb;
    du_uchar fb_buf[256];
    du_uchar line[256];
    du_str_input_buffer sb;
    du_uint argc = 0;
    du_uchar token[TOKEN_NUM][256];
    const du_uchar* argv[TOKEN_NUM];

    du_file_input_buffer_init(&fb, fb_buf, sizeof fb_buf, du_file_stdin());

    argv[0] = DU_UCHAR_CONST("help");
    ui->_current_view->_command(ui->_current_view->_user_data, argc, argv);

    for (;;) {
        printf("> "); fflush(stdout);
        if (!read_line(&fb, line, sizeof line)) break;

        du_str_input_buffer_init(&sb, line);

        for (argc = 0; argc < sizeof(argv) / sizeof(argv[0]); ++argc) {
            if (!du_str_input_buffer_read_token(&sb, token[argc], sizeof token, DU_UCHAR_CONST(" \t"))) break;
            argv[argc] = token[argc];
        }
        if (!argc) continue;

        if (du_str_equal(argv[0], DU_UCHAR_CONST("q")) || du_str_equal(argv[0], DU_UCHAR_CONST("quit")) ) {
            break;
        } else {
            ui->_current_view->_command(ui->_current_view->_user_data, argc, argv);
        }
    }
}

static void join_handler(dupnp_cp_dvcmgr_device* device, void* arg) {
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;

    printf("+ %s\n", dd->friendly_name);
}

static void leave_handler(dupnp_cp_dvcmgr_device* device, void* arg) {
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;

    printf("- %s\n", dd->friendly_name);
}

static void netif_change_handler(void* arg) {
    puts("* network interface changed");
}

du_bool dmp_ui_init(dmp_ui* ui, dupnp* upnp, dupnp_cp_dvcmgr* dm, cds_browser* cb_browse, cds_browser* cb_event, player* p, dmp_event_adapter* ea, dupnp_schedtaskmgr* event_schedtaskmgr, dav_capability* thumbnail_cap, const du_uchar* user_agent) {
    du_byte_zero((du_uint8*)ui, sizeof(dmp_ui));

    dav_capability_set_resolution_for_thumbnail(thumbnail_cap, THUBNAIL_WIDTH, THUMBNAIL_HEIGHT);

    if (!dmp_event_adapter_set_join_handler(ea, join_handler, ui)) return 0;
    if (!dmp_event_adapter_set_leave_handler(ea, leave_handler, ui)) return 0;
    if (!dmp_event_adapter_set_netif_change_handler(ea, netif_change_handler, ui)) return 0;

    if (!dmp_ui_dms_view_init(&ui->_dms_view, ui, upnp, dm, p, user_agent)) return 0;
    if (!dmp_ui_browse_view_init(&ui->_browse_view, ui, cb_browse, cb_event, p, ea, event_schedtaskmgr, thumbnail_cap, dm)) goto error;
    if (!dmp_ui_metadata_view_init(&ui->_metadata_view, ui)) goto error2;
    if (!dmp_ui_player_view_init(&ui->_player_view, ui, p)) goto error3;
    ui->_current_view = &ui->_dms_view;

    dmp_ui_clear_screen();
    return 1;

error3:
    dmp_ui_metadata_view_free(&ui->_metadata_view);
error2:
    dmp_ui_browse_view_free(&ui->_browse_view);
error:
    dmp_ui_dms_view_free(&ui->_dms_view);
    return 0;
}

void dmp_ui_free(dmp_ui* ui) {
    dmp_ui_dms_view_free(&ui->_dms_view);
    dmp_ui_browse_view_free(&ui->_browse_view);
    dmp_ui_metadata_view_free(&ui->_metadata_view);
    dmp_ui_player_view_free(&ui->_player_view);
}

du_bool dmp_ui_start(dmp_ui* ui) {
    command(ui);
    return 1;
}
