/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVCCONF_H
#define DUPNP_DVCCONF_H

#include <dupnp_dvcdsc_info_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_dvcconf {
    du_uint32 max_age;
    dupnp_dvcdsc_info_array dvcdsc_info_array;
    du_uchar* plugin;
    du_str_array url_alias_param_array;
    du_str_array placeholder_param_array;
    du_uint16 port;
} dupnp_dvcconf;

extern void dupnp_dvcconf_init(dupnp_dvcconf* conf);

extern void dupnp_dvcconf_free(dupnp_dvcconf* conf);

extern du_bool dupnp_dvcconf_set_plugin(dupnp_dvcconf* conf, const du_uchar* http_plugin_path);

extern du_bool dupnp_dvcconf_set_url_alias(dupnp_dvcconf* conf, const du_uchar* url, const du_uchar* alias);

extern du_bool dupnp_dvcconf_set_placeholder(dupnp_dvcconf* conf, const du_uchar* key, const du_uchar* value);

extern const du_uchar* dupnp_dvcconf_get_placeholder(dupnp_dvcconf* conf, const du_uchar* key);

extern du_bool dupnp_dvcconf_parse(dupnp_dvcconf* conf, const du_uchar* path);

#ifdef __cplusplus
}
#endif

#endif

