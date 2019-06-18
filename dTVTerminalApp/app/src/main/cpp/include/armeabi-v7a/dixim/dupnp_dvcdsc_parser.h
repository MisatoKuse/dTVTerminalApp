/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVCDSC_PARSER_H
#define DUPNP_DVCDSC_PARSER_H

#include <dupnp_dvcdsc.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_dvcdsc_parser_parse(dupnp_dvcdsc* dvcdsc, du_str_array* translate_param_array);

extern du_bool dupnp_dvcdsc_parser_get_element(const du_uchar* path, const du_uchar* target_udn, const du_uchar* name, du_uchar_array* value, du_str_array* translate_param_array);

extern du_bool dupnp_dvcdsc_parser_translate_placeholders(du_uchar_array* text, du_uchar_array* translated_text, const du_str_array* translate_param_array);

#ifdef __cplusplus
}
#endif

#endif
