/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */



/**
 * @file
 *   The du_xml_parser_config interface provides methods for manipulating XML parser configurations.
 *  Setting values set by using these APIs influences all XML parser in dixim sdk
 *  (e.g. du_soap_envelope_parse_request(), dav_didl_parser_packed_parse(), etc.).
 */

#ifndef DU_XML_PARSER_CONFIG_H
#define DU_XML_PARSER_CONFIG_H

#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Sets characters to be condensed.
 *  If XML text contains a seaquence of specified characters, that seaquence is substituted for a single space.
 *  Default condensed characters aren't set.
 *  @param[in] condensed_characters condensed characters
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_xml_parser_config_set_condensed_characters(const du_uchar* condensed_characters);

/**
 *  Gets condensed characters.
 *  @return  condensed characters.
 *  @see   du_xml_parser_config_set_condensed_characters
 */
extern const du_uchar* du_xml_parser_config_get_condensed_characters(void);

#ifdef __cplusplus
}
#endif

#endif
