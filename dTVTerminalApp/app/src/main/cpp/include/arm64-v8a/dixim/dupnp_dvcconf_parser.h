/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVCCONF_PARSER_H
#define DUPNP_DVCCONF_PARSER_H

#include <dupnp_dvcconf.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_dvcconf_parser_parse(dupnp_dvcconf* conf, const du_uchar* path);

#ifdef __cplusplus
}
#endif

#endif
