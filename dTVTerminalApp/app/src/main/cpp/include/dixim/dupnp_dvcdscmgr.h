/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVCDSCMGR_H
#define DUPNP_DVCDSCMGR_H

#include <dupnp_impl.h>
#include <dupnp_dvcdsc.h>
#include <dupnp_dvcdsc_device.h>
#include <dupnp_dvcdsc_service.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_dvcdscmgr_init(dupnp_dvcdscmgr* dvcdscmgr, dupnp_impl* upnp);

extern void dupnp_dvcdscmgr_free(dupnp_dvcdscmgr* dvcdscmgr);

extern du_bool dupnp_dvcdscmgr_start(dupnp_dvcdscmgr* dvcdscmgr);

extern void dupnp_dvcdscmgr_stop(dupnp_dvcdscmgr* dvcdscmgr);

typedef du_bool (*dupnp_dvcdscmgr_service_desc_visitor)(dupnp_dvcdsc_service* service_desc, dupnp_dvcdsc* dvcdsc, dupnp_dvcdsc_device* device_desc, void* arg);

extern void dupnp_dvcdscmgr_visit_service_desc(dupnp_dvcdscmgr* dvcdscmgr, dupnp_dvcdscmgr_service_desc_visitor visitor, void* arg);

extern du_bool dupnp_dvcdscmgr_register_dvcdsc(dupnp_dvcdscmgr* dvcdscmgr, const du_uchar* location, const du_uchar* path, du_str_array* placeholder_params);

extern void dupnp_dvcdscmgr_clear_dvcdsc_array(dupnp_dvcdscmgr* dvcdscmgr);

extern du_bool dupnp_dvcdscmgr_write_element(dupnp_dvcdscmgr* dvcdscmgr, const du_uchar* udn, const du_uchar* name, const du_uchar* value);

extern du_bool dupnp_dvcdscmgr_read_element(dupnp_dvcdscmgr* dvcdscmgr, const du_uchar* udn, const du_uchar* name, du_uchar_array* value);

#ifdef __cplusplus
}
#endif

#endif
