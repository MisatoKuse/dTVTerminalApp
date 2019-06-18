/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SVCDSC_PARSER_H
#define DUPNP_SVCDSC_PARSER_H

#include <dupnp_svcdsc.h>
#include <du_type.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Parses a service description xml.
 * @param[in,out] x service description structure.
 * @param[in] xml the xml of service description.
 * @param[in] xml_len the length of xml.
 * @return true if succeeded. Otherwise false.
 */
extern du_bool dupnp_svcdsc_parser_parse(dupnp_svcdsc* x, const du_uchar* xml, du_uint32 xml_len);

/**
 * Parses a service description xml file.
 * @param[in,out] x service description structure.
 * @param[in] xml_path the file path of xml.
 * @return true if succeeded. Otherwise false.
 */
extern du_bool dupnp_svcdsc_parser_parse_file(dupnp_svcdsc* x, const du_uchar* xml_path);

#ifdef __cplusplus
}
#endif

#endif

