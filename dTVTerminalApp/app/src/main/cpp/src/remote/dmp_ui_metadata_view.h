/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_UI_METADATA_VIEW_H
#define DMP_UI_METADATA_VIEW_H

#include "dmp_ui.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dmp_ui_metadata_view {
    dmp_ui* _ui;

    dav_didl_object* _object;
    du_bool _show_verbosely;
} dmp_ui_metadata_view;

extern du_bool dmp_ui_metadata_view_init(dmp_ui_view* view, dmp_ui* ui);

extern void dmp_ui_metadata_view_free(dmp_ui_view* view);

extern void dmp_ui_metadata_view_set_show_verbosely(dmp_ui_view* view, du_bool show_verbosely);

extern void dmp_ui_metadata_view_set_object(dmp_ui_view* view, dav_didl_object* object);

#ifdef __cplusplus
}
#endif

#endif
