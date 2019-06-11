/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 */

#ifndef MEDIA_DTCP_PARSER_FACTORY_H
#define MEDIA_DTCP_PARSER_FACTORY_H

#include <dmedia_parser_factory.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dmedia_parser_factory media_dtcp_parser_factory;

extern media_dtcp_parser_factory* media_dtcp_parser_factory_create(void);

#ifdef __cplusplus
}
#endif

#endif
