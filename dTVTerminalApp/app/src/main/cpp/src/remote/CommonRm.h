/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVTERMINALAPP_COMMONRM_H
#define DTVTERMINALAPP_COMMONRM_H

#include <dupnp_cp_dvcmgr_device.h>
#include <dupnp_cp_evtmgr.h>
#include <dav_capability.h>
#include "dmp_event_adapter.h"
#include "dmp_ui.h"
#include "cds_browser.h"
#include "player.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

    namespace dtvt {
        //================================== define begin ==================================//
        typedef std::vector<std::string> StringVector2;

        typedef struct {
            int parameter;
        } connect_status_arg_s;
        //=================================== define end ===================================//

        //================================== type define begin ==================================//
    //        typedef struct {
    //            dmp_event_adapter ea;
    //            dupnp upnp;
    //            dupnp_cp_dvcmgr dm;
    //            dupnp_cp_evtmgr em;
    //            cds_browser cb_browse;
    //            cds_browser cb_event;
    //            player p;
    //            dmp_ui ui;
    //            dav_capability cap;
    //        } DmpRm;
        //=================================== type define end ===================================//

        //================================== function begin ==================================//
    //        extern const du_uchar* dmp_get_dms_type(void);
    //
    //        extern const du_uchar* dmp_get_user_agent(void);
        //================================== function begin ==================================//
    }

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif //DTVTERMINALAPP_COMMONRM_H
