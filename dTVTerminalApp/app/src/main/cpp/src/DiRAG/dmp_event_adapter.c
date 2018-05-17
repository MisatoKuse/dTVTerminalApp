/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include "dmp_event_adapter.h"
#include <dav_urn.h>
#include <dupnp_urn.h>
#include <du_str.h>
#include <du_alloc.h>
#include <du_byte.h>
#include "dvcdsc_device.h"
#include "dvcdsc_device_array.h"
#include "dvcdsc_parser.h"
#include "../DlnaDefine.h"

void dmp_event_adapter_init(dmp_event_adapter* ea, dupnp* upnp, dupnp_cp_dvcmgr* dm, dupnp_cp_evtmgr* em) {
    du_byte_zero((du_uint8*)ea, sizeof(dmp_event_adapter));

    du_ptr_array_init(&ea->_join_handler_array);
    du_ptr_array_init(&ea->_leave_handler_array);
    du_ptr_array_init(&ea->_netif_change_handler_array);
    ea->_upnp = upnp;
    ea->_dm = dm;
    ea->_em = em;
}

void dmp_event_adapter_free(dmp_event_adapter* ea) {
    du_ptr_array_free(&ea->_join_handler_array);
    du_ptr_array_free(&ea->_leave_handler_array);
    du_ptr_array_free(&ea->_netif_change_handler_array);
}

static void netif_change_handler(void* arg) {
    dmp_event_adapter* ea = (dmp_event_adapter*)arg;
    du_uint32 i;
    du_uint32 len;
    void** handler;

    len = du_ptr_array_length(&ea->_netif_change_handler_array);
    handler = du_ptr_array_get(&ea->_netif_change_handler_array);
    for (i = 0; i < len; i += 2) {
        ((dmp_event_adapter_netif_change_handler)handler[i])(handler[i + 1]);
    }
}

static du_bool get_ra_dvcdsc_device(dupnp_cp_dvcmgr_dvcdsc* dvcdsc, dupnp_cp_dvcmgr_device* device, dvcdsc_device** dd) {
    dvcdsc_device_array dd_array;
    dvcdsc_device* dd_;
    du_uint32 len;
    du_uint32 pos;

    dvcdsc_device_array_init(&dd_array);

    if (!dvcdsc_parser_parse(&dd_array, dvcdsc->xml, dvcdsc->xml_len, dvcdsc->location)) goto error;

    len = dvcdsc_device_array_length(&dd_array);
    pos = dvcdsc_device_array_find_by_udn_and_device_type(&dd_array, device->udn, device->device_type);
    if (pos == len) goto error;
    dd_ = (dvcdsc_device*)dvcdsc_device_array_get_pos(&dd_array, pos);
    if (du_str_len(dd_->diximcap) == du_str_find(dd_->diximcap, DU_UCHAR_CONST("dtcp-plus:"))) goto error;
    if (!(*dd = (dvcdsc_device*)du_alloc(sizeof(dvcdsc_device)))) goto error;
    du_byte_copy((du_uint8*)*dd, sizeof(dvcdsc_device), (du_uint8*)dd_);

    dvcdsc_device_array_remove(&dd_array, pos);
    dvcdsc_device_array_free_object(&dd_array);
    return 1;

error:
    dvcdsc_device_array_free_object(&dd_array);
    return 0;
}

static du_bool allow_join_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, dupnp_cp_dvcmgr_dvcdsc* dvcdsc, void* arg) {
    dvcdsc_device* dd = 0;

    if (!get_ra_dvcdsc_device(dvcdsc, device, &dd)) return 0;
    device->user_data = (void*)dd;

    return 1;
}

static void join_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, dupnp_cp_dvcmgr_dvcdsc* dvcdsc, void* arg) {
    dmp_event_adapter* ea = (dmp_event_adapter*)arg;
    du_uint32 i;
    du_uint32 len;
    void** handler;

    len = du_ptr_array_length(&ea->_join_handler_array);
    handler = du_ptr_array_get(&ea->_join_handler_array);
    for (i = 0; i < len; i += 2) {
        ((dmp_event_adapter_join_handler)handler[i])(device, handler[i + 1]);
    }
}

static du_bool leave_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, void* arg) {
    dmp_event_adapter* ea = (dmp_event_adapter*)arg;
    dvcdsc_device* dd = (dvcdsc_device*)device->user_data;
    du_uint32 i;
    du_uint32 len;
    void** handler;

    len = du_ptr_array_length(&ea->_leave_handler_array);
    handler = du_ptr_array_get(&ea->_leave_handler_array);
    for (i = 0; i < len; i += 2) {
        ((dmp_event_adapter_leave_handler)handler[i])(device, handler[i + 1]);
    }

    dvcdsc_device_free(dd);
    du_alloc_free(dd);
    return 1;
}

du_bool dmp_event_adapter_set_join_handler(dmp_event_adapter* ea, dmp_event_adapter_join_handler handler, void* handler_arg) {
    du_uint32 len;
    du_uint32 pos;
    void* pv;

    len = du_ptr_array_length(&ea->_join_handler_array);
    pos = du_ptr_array_find(&ea->_join_handler_array, (const void*)handler);

    if (len != pos) {
        pv = du_ptr_array_get_pos(&ea->_join_handler_array, pos + 1);
        if (pv == handler_arg) return 1;
    }

    if (!du_ptr_array_cato(&ea->_join_handler_array,  (void*)handler)) return 0;
    if (!du_ptr_array_cato(&ea->_join_handler_array, handler_arg)) return 0;

    if (!len) {
        if (!dupnp_cp_dvcmgr_set_allow_join_handler(ea->_dm, allow_join_handler, ea)) return 0;
        if (!dupnp_cp_dvcmgr_set_join_handler(ea->_dm, join_handler, ea)) return 0;
    }

    return 1;
}

du_bool dmp_event_adapter_set_leave_handler(dmp_event_adapter* ea, dmp_event_adapter_leave_handler handler, void* handler_arg) {
    du_uint32 len;
    du_uint32 pos;
    void* pv;

    len = du_ptr_array_length(&ea->_leave_handler_array);
    pos = du_ptr_array_find(&ea->_leave_handler_array,  (const void*)handler);

    if (len != pos) {
        pv = du_ptr_array_get_pos(&ea->_leave_handler_array, pos + 1);
        if (pv == handler_arg) return 1;
    }

    if (!du_ptr_array_cato(&ea->_leave_handler_array,  (void*)handler)) return 0;
    if (!du_ptr_array_cato(&ea->_leave_handler_array, handler_arg)) return 0;

    if (!len) {
        if (!dupnp_cp_dvcmgr_set_leave_handler(ea->_dm, leave_handler, ea)) return 0;
    }

    return 1;
}

du_bool dmp_event_adapter_set_netif_change_handler(dmp_event_adapter* ea, dmp_event_adapter_netif_change_handler handler, void* handler_arg) {
    du_uint32 len;
    du_uint32 pos;

    len = du_ptr_array_length(&ea->_netif_change_handler_array);
    pos = du_ptr_array_find(&ea->_netif_change_handler_array,  (const void*)handler);

    if (len == pos) {
        if (!du_ptr_array_cato(&ea->_netif_change_handler_array,  (void*)handler)) return 0;
        if (!du_ptr_array_cato(&ea->_netif_change_handler_array, handler_arg)) return 0;
    }

    if (!dupnp_set_netif_change_handler(ea->_upnp, netif_change_handler, ea)) return 0;

    return 1;
}
