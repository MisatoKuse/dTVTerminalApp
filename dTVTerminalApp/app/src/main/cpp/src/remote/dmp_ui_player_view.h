/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_UI_PLAYER_VIEW_H
#define DMP_UI_PLAYER_VIEW_H

#include "dmp_ui.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dmp_ui_player_view {
    dmp_ui* _ui;
    player* _p;

    du_uint32 _available_actions;
    du_uchar_array _play_spped;
} dmp_ui_player_view;

extern du_bool dmp_ui_player_view_init(dmp_ui_view* view, dmp_ui* ui, player* p);

extern void dmp_ui_player_view_free(dmp_ui_view* view);

extern void dmp_ui_player_view_enable_resetcontroller(dmp_ui_view* view);

#ifdef __cplusplus
}
#endif

#endif
