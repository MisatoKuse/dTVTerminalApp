/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_UI_DMS_VIEW_H
#define DMP_UI_DMS_VIEW_H

#include "dmp_ui.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dmp_ui_dms_view {
    dmp_ui* _ui;
    dupnp* _upnp;
    dupnp_cp_dvcmgr* _dm;
    const du_uchar* user_agent;
    player* _player;

    du_str_array _udn_array;
    du_uint32 _starting_index;
    du_bool _is_display_remote;
} dmp_ui_dms_view;

extern du_bool dmp_ui_dms_view_init(dmp_ui_view* view, dmp_ui* ui, dupnp* upnp, dupnp_cp_dvcmgr* dm, player* p, const du_uchar* user_agent);

extern void dmp_ui_dms_view_free(dmp_ui_view* view);

#ifdef __cplusplus
}
#endif

#endif
