/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_UI_H
#define DMP_UI_H

#include <dupnp_cp_dvcmgr.h>
#include <du_str_array.h>
#include <dav_capability.h>
#include <du_scheduled_task.h>
#include <dupnp_schedtaskmgr.h>
#include "dmp_event_adapter.h"
#include "cds_browser.h"
#include "player.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifdef WINCE
#define ITEM_COUNT_IN_LIST 6
#else
#define ITEM_COUNT_IN_LIST 15
#endif

typedef struct dmp_ui_view {
    void* _user_data;
    void (*_command)(void* _user_data, const du_uint argc, const du_uchar* argv[]);
    void (*_update_screen)(void* _user_data);
} dmp_ui_view;

typedef struct dmp_ui {
    dmp_ui_view* _current_view;
    dmp_ui_view _dms_view;
    dmp_ui_view _browse_view;
    dmp_ui_view _metadata_view;
    dmp_ui_view _player_view;
} dmp_ui;

extern void dmp_ui_change_view_to_dms(dmp_ui* ui);

extern void dmp_ui_change_view_to_browse(dmp_ui* ui);

extern void dmp_ui_change_view_to_metadata(dmp_ui* ui);

extern void dmp_ui_change_view_to_player(dmp_ui* ui);

extern void dmp_ui_change_view_to_select_mrd(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_current_view(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_dms_view(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_browse_view(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_metadata_view(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_player_view(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_select_mrd_view(dmp_ui* ui);

extern dmp_ui_view* dmp_ui_get_output_setting_view(dmp_ui* ui);

extern void dmp_ui_current_dms(dmp_ui* ui);

extern void dmp_ui_print_bar(void);

extern void dmp_ui_clear_screen(void);

extern void dmp_ui_update_screen(dmp_ui* ui);

extern du_bool dmp_ui_init(dmp_ui* ui, dupnp* upnp, dupnp_cp_dvcmgr* dm, cds_browser* cb_browse, cds_browser* cb_event, player* p, dmp_event_adapter* ea, dupnp_schedtaskmgr* event_schedtaskmgr, dav_capability* thumbnail_cap, const du_uchar* user_agent);

extern void dmp_ui_free(dmp_ui* ui);

extern du_bool dmp_ui_start(dmp_ui* ui);

#ifdef __cplusplus
}
#endif

#endif
