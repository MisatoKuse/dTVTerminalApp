/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <dupnp_cp.h>
#include <dupnp_dvc_service.h>
#include <dupnp_dvc_service_adapter.h>
#include <dupnp_dvcdsc_device.h>
#include <dupnp_dvcdsc_device_array.h>
#include <dmrd_avt_stub.h>
#include <dmrd_cms_stub.h>
#include <dmrd_rcs_stub.h>
#include <dupnp_taskmgr.h>
#include <dav_urn.h>
#include <dupnp_urn.h>
#include <du_byte.h>
#include <du_str.h>
#include <du_signal.h>
#include <du_log.h>
#include <stdio.h>
#include "dmp.h"
#include "dmp_conf.h"
#include "dmp_event_adapter.h"

#include <drag_cp.h>

#define CURRENT_BITRATE 8 * 1024 * 1024 * 50
#define MONITOR_WIDTH 1280
#define MONITOR_HEIGHT 720

const du_uchar* dmp_get_dms_type() {
    return dav_urn_msd(1);
}

const du_uchar* dmp_get_user_agent() {
    return DU_UCHAR_CONST("DLNADOC/1.50");
}

const du_uchar* get_av_transport_service_id() {
    return DU_UCHAR_CONST("urn:upnp-org:serviceId:AVTransport");
}

const du_uchar* get_connection_manager_service_id() {
    return DU_UCHAR_CONST("urn:upnp-org:serviceId:ConnectionManager");
}

const du_uchar* get_rendering_control_service_id() {
    return DU_UCHAR_CONST("urn:upnp-org:serviceId:RenderingControl");
}

static du_bool dmp_cp_init(DmpRm* d) {
    if (!dupnp_cp_dvcmgr_init(&d->dm, &d->upnp)) return 0;
    if (!cds_browser_init(&d->cb_browse, &d->upnp, &d->em, dmp_get_user_agent())) goto error;
    if (!cds_browser_init(&d->cb_event, &d->upnp, &d->em, dmp_get_user_agent())) goto error2;

    if (!dupnp_cp_dvcmgr_add_device_type(&d->dm, dmp_get_dms_type())) goto error3;

    if (!dupnp_enable_netif_monitor(&d->upnp, 1)) goto error3;
    if (!dupnp_cp_enable_ssdp_listener(&d->upnp, 1)) goto error3;
    if (!dupnp_cp_enable_ssdp_search(&d->upnp, 1)) goto error3;
    if (!dupnp_cp_enable_http_server(&d->upnp, 1)) goto error3;

    return 1;

error3:
    cds_browser_free(&d->cb_event);
error2:
    cds_browser_free(&d->cb_browse);
error:
    dupnp_cp_dvcmgr_free(&d->dm);
    return 0;
}

static du_bool capability_init(DmpRm* p, const du_uchar* cap_path) {
    if (!dav_capability_init(&p->cap)) return 0;

    if (!dav_capability_set_capability_file(&p->cap, cap_path)) goto error;

    dav_capability_set_max_bitrate(&p->cap, CURRENT_BITRATE);
    dav_capability_set_resolution(&p->cap, MONITOR_WIDTH, MONITOR_HEIGHT);

    return 1;

error:
    dav_capability_free(&p->cap);
    return 0;
}

static void capability_free(DmpRm* p) {
    dav_capability_free(&p->cap);
}

static void dmp_cp_free(DmpRm* d) {
    dav_capability_free(&d->cap);
    dupnp_cp_dvcmgr_free(&d->dm);
    cds_browser_free(&d->cb_browse);
    cds_browser_free(&d->cb_event);
}

static du_bool dmp_cp_start(DmpRm* d) {
    if (!dupnp_cp_dvcmgr_start(&d->dm)) return 0;

    return 1;
}

static void dmp_cp_stop(DmpRm* d) {
    dupnp_cp_dvcmgr_stop(&d->dm);
}

/*
static void dcp_connect_status_handler(drag_cp_connect_status status, void* arg) {
    switch(status) {
    case DRAG_CP_CONNECT_STATUS_UNKNOWN:
        puts("DRAG_CP_CONNECT_STATUS_UNKNOWN");
        break;
    case DRAG_CP_CONNECT_STATUS_READY:
        puts("DRAG_CP_CONNECT_STATUS_READY");
        break;
    case DRAG_CP_CONNECT_STATUS_CONNECTED:
        puts("DRAG_CP_CONNECT_STATUS_CONNECTED");
        break;
    case DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION:
        puts("DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION");
        break;
    case DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION:
        puts("DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION");
        break;
    }
}
 */

/*
static du_bool dmp_init(DmpRm* d, const du_uchar* conf_path) {
    du_uchar_array ua;
    du_uchar_array ua2;
    dupnp_taskmgr* taskmgr;
    dupnp_schedtaskmgr* schedtaskmgr;

    du_uchar_array_init(&ua);
    du_uchar_array_init(&ua2);

    du_byte_zero((du_uint8*)d, sizeof(DmpRm));

    if (!dupnp_init(&d->upnp, 0, 0)) goto error;
    if (!dmp_cp_init(d)) goto error2;
    dmp_event_adapter_init(&d->ea, &d->upnp, &d->dm, &d->em);

    if (!dmp_conf_get_capability_xml_path(conf_path, &ua)) goto error3;
    if (!du_uchar_array_cat0(&ua)) goto error3;
    if (!capability_init(d, du_uchar_array_get(&ua))) goto error3;

    if (!dmp_conf_get_download_dir_path(conf_path, &ua, &ua2)) goto error4;
    if (!du_uchar_array_cat0(&ua)) goto error4;
    if (!player_init(&d->p, 0, &d->cap, du_uchar_array_get(&ua))) goto error4;
#ifdef ENABLE_DTCP
    if (!dmp_conf_get_private_data_home_path(conf_path, &ua)) goto error5;
    if (!du_uchar_array_cat0(&ua)) goto error5;
    if (!player_set_private_data_home(&d->p, du_uchar_array_get(&ua))) goto error5;
#endif

    if (!dupnp_cp_evtmgr_init(&d->em, &d->upnp)) goto error5;
    taskmgr = dupnp_get_taskmgr(&d->upnp);
    if (!taskmgr) goto error6;
    schedtaskmgr = dupnp_taskmgr_get_schedtaskmgr(taskmgr);
    if (!schedtaskmgr) goto error6;
    if (!dmp_ui_init(&d->ui, &d->upnp, &d->dm, &d->cb_browse, &d->cb_event,&d->p, &d->ea, schedtaskmgr, &d->cap, dmp_get_user_agent())) goto error6;
    dupnp_enable_netif_monitor(&d->upnp, 1);

    if (!dmp_conf_get_dirag_path(conf_path, &ua)) goto error6;
    if (!du_uchar_array_cat0(&ua)) goto error6;
    if (!drag_cp_initialize(du_uchar_array_get(&ua))||
	!drag_cp_lrsys_start()||
	!drag_cp_rasys_start(dcp_connect_status_handler, d)) goto error6;

    du_uchar_array_free(&ua);
    du_uchar_array_free(&ua2);

    return 1;

error6:
    dmp_event_adapter_free(&d->ea);
    dupnp_cp_evtmgr_free(&d->em);
error5:
    player_free(&d->p);
error4:
    capability_free(d);
error3:
    dmp_cp_free(d);
error2:
    dupnp_free(&d->upnp);
error:
    du_uchar_array_free(&ua);
    du_uchar_array_free(&ua2);
    return 0;
}
*/

static void dmp_free(DmpRm* d) {
    player_free(&d->p);
    dmp_event_adapter_free(&d->ea);
    dupnp_cp_evtmgr_free(&d->em);
    capability_free(d);
    dmp_cp_free(d);
    dupnp_free(&d->upnp);
    dmp_ui_free(&d->ui);
    drag_cp_stop();
}

static du_bool dmp_start(DmpRm* d) {
    if (!dupnp_start(&d->upnp)) return 0;
    if (!dupnp_cp_evtmgr_start(&d->em)) goto error;
    if (!dmp_cp_start(d)) goto error2;
//    if (!player_start(&d->p)) goto error3;
    if (!dmp_ui_start(&d->ui)) goto error4;
    return 1;

error4:
    player_stop(&d->p);
error3:
    dmp_cp_stop(d);
error2:
    dupnp_cp_evtmgr_stop(&d->em);
error:
    dupnp_stop(&d->upnp);
    return 0;
}

static void dmp_stop(DmpRm* d) {
    dupnp_cp_evtmgr_stop(&d->em);
    dupnp_stop(&d->upnp);
    dmp_cp_stop(d);
    player_stop(&d->p);
}

static du_bool run(const du_uchar* conf_path) {
    DmpRm d;

    //if (!dmp_init(&d, conf_path)) return 0;
    if (!dmp_start(&d)) goto error;
    dmp_stop(&d);
    dmp_free(&d);
    return 1;

error:
    dmp_free(&d);
    return 0;
}

int main(int argc, char** argv) {
    const du_uchar* conf_path;

    if (argc != 2) {
        fprintf(stderr, "Usage: %s conf_path\n", argv[0]);
        goto error;
    }

    conf_path = DU_UCHAR_CONST(argv[1]);

    du_signal_ignore(du_signal_id_pipe());

    if (!run(conf_path)) {
        puts("error");
        goto error;
    }
    return 0;

error:
    return 1;
}
