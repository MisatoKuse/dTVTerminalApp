/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 */

#ifndef MEDIA_DTCP_PARSER_H
#define MEDIA_DTCP_PARSER_H

#include <dmedia_parser.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dmedia_parser media_dtcp_parser;

extern media_dtcp_parser* media_dtcp_parser_create();

extern void media_dtcp_parser_enable_no_check_open(media_dtcp_parser* x, du_bool flag);

#ifdef __cplusplus
}
#endif

#endif
