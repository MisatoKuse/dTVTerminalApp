/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_UI_BROWSE_VIEW_H
#define DMP_UI_BROWSE_VIEW_H

#include "dmp_ui.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dmp_ui_browse_view {
    du_mutex _mutex;

    dmp_ui* _ui;
    player* _p;
    dav_capability* _thumbnail_cap;
    dupnp_cp_dvcmgr* _dm;

    du_str_array _view_history;          //need lock
    du_uint _view_starting_index;        //need lock
    du_uint32 _view_object_count;        //need lock
    du_uint32 _view_total_objects;       //need lock
    du_uint32 _view_update_id;
    dav_didl_object_array _view_object_list;//need lock
    dav_didl_object _view_bm_object;

    du_uchar_array _udn;

    cds_browser* _browse_cb;
    dav_didl_object_array _browse_object_list;

    cds_browser* _event_cb;
    dav_didl_object_array _event_object_list;
    dav_didl_object _event_bm_object;
    du_time _event_last_update_check_time;
    dupnp_schedtaskmgr* _event_schedtaskmgr;
    du_scheduled_task _event_update_check_task;
    du_uint _event_update_check_task_id; //need lock
    du_bool _event_list_updated;         //need lock
} dmp_ui_browse_view;

extern du_bool dmp_ui_browse_view_init(dmp_ui_view* view, dmp_ui* ui, cds_browser* cb_browse, cds_browser* cb_event, player* p, dmp_event_adapter* ea, dupnp_schedtaskmgr* event_schedtaskmgr, dav_capability* thumbnail_cap, dupnp_cp_dvcmgr* dm);

extern void dmp_ui_browse_view_free(dmp_ui_view* view);

extern du_bool dmp_ui_browse_view_set_dms(dmp_ui_view* view, const du_uchar* udn, const du_uchar* control_url, const du_uchar* event_sub_url);

#ifdef __cplusplus
}
#endif

#endif
