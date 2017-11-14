/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SVCMGR_H
#define DUPNP_SVCMGR_H

#include <dupnp_impl.h>
#include <dupnp_dvc_service.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_svcmgr_init(dupnp_svcmgr* svcmgr, dupnp_impl* upnp);

extern du_bool dupnp_svcmgr_start_before(dupnp_svcmgr* svcmgr);

extern void dupnp_svcmgr_stop_after(dupnp_svcmgr* svcmgr);

extern void dupnp_svcmgr_free(dupnp_svcmgr* svcmgr);

extern du_bool dupnp_svcmgr_allocate(dupnp_svcmgr* svcmgr, du_uint32 len);

extern du_bool dupnp_svcmgr_start(dupnp_svcmgr* svcmgr);

extern du_bool dupnp_svcmgr_start_after(dupnp_svcmgr* svcmgr);

extern void dupnp_svcmgr_stop_before(dupnp_svcmgr* svcmgr);

extern void dupnp_svcmgr_stop(dupnp_svcmgr* svcmgr);

extern du_bool dupnp_svcmgr_register_service(dupnp_svcmgr* svcmgr, dupnp_dvc_service_interface* sif);

extern du_bool dupnp_svcmgr_register_http_service_desc(dupnp_svcmgr* svcmgr);

extern du_bool dupnp_svcmgr_register_http_service_doc(dupnp_svcmgr* svcmgr);

#ifndef DISABLE_DYNAMIC_LINK

extern du_bool dupnp_svcmgr_register_plugin(dupnp_svcmgr* svcmgr, const du_uchar* plugin_dir);

#endif

extern dupnp_dvc_service* dupnp_svcmgr_get_service_by_url_path(dupnp_svcmgr* svcmgr, const du_uchar* url_path);

extern du_bool dupnp_svcmgr_evtmgr_set_state_variable(dupnp_svcmgr* svcmgr, const du_uchar* service_id, const du_uchar* udn, const du_uchar* name, const du_uchar* value);

extern du_bool dupnp_svcmgr_evtmgr_notify_state_variable_changed(dupnp_svcmgr* svcmgr, const du_uchar* service_id, const du_uchar* udn);

extern du_bool dupnp_svcmgr_evtmgr_set_message_handler(dupnp_svcmgr* svcmgr, const du_uchar* service_id, const du_uchar* udn, dupnp_dvc_service_evtmgr_message_handler initial_message_handler, void* initial_message_handler_arg, dupnp_dvc_service_evtmgr_message_handler message_handler, void* message_handler_arg);

extern du_bool dupnp_svcmgr_get_service_info(dupnp_svcmgr* svcmgr, const du_uchar* service_id, dupnp_dvc_service_info_array* si_array);

extern du_bool dupnp_svcmgr_add_pre_upnp_action_handler(dupnp_svcmgr* svcmgr, dupnp_dvc_service_pre_upnp_action_handler handler, void* handler_arg);

extern du_bool dupnp_svcmgr_remove_pre_upnp_action_handler(dupnp_svcmgr* svcmgr, dupnp_dvc_service_pre_upnp_action_handler handler, void* handler_arg);

extern void dupnp_svcmgr_callback_pre_upnp_action_handler(dupnp_svcmgr* svcmgr, dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request);

#ifdef __cplusplus
}
#endif

#endif
