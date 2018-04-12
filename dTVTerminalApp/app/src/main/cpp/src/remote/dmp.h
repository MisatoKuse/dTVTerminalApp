/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_H
#define DMP_H

#include <dupnp_cp_dvcmgr_device.h>
#include <dupnp_cp_evtmgr.h>
#include <dav_capability.h>
#include "dmp_event_adapter.h"
#include "dmp_ui.h"
#include "cds_browser.h"
#include "player.h"

#ifdef __cplusplus
extern "C" {
#endif

#define SOAP_TIMEOUT_MS 30000

//typedef struct dmp {
//    dmp_event_adapter ea;
//    dupnp upnp;
//    dupnp_cp_dvcmgr dm;
//    dupnp_cp_evtmgr em;
//    cds_browser cb_browse;
//    cds_browser cb_event;
//    player p;
//    dmp_ui ui;
//    dav_capability cap;
//} dmp;
typedef struct DmpRm {
    dmp_event_adapter ea;
    dupnp upnp;
    dupnp_cp_dvcmgr dm;
    dupnp_cp_evtmgr em;
    cds_browser cb_browse;
    cds_browser cb_event;
    player p;
    dmp_ui ui;
    dav_capability cap;
} DmpRm;

extern const du_uchar* dmp_get_dms_type(void);

extern const du_uchar* dmp_get_user_agent(void);


#ifdef __cplusplus
}
#endif

#endif
