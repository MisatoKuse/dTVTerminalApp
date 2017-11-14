/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_xml interface provides methods for manipulating XML string
 *  ( such as checking code, appending element or attribute ).
 */

#ifndef DU_XMLH
#define DU_XMLH

#include "du_uchar_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Tests if the encoding of the string is UTF-8.
 *  @param[in] value string to check.
 *  @return  true if value is a string of UTF-8 characters.
 */
extern du_bool du_xml_is_legal_value(const du_uchar* value);

/**
 *  Appends s string to a destination array, converting '<' '>' '&' '"' '\''
 *  characters in s to "&lt;" "&gt;" "&amp;" "&quot;" "&apos;".
 *  @param[in,out] ca destination du_uchar_array structure.
 *  @param[in] s string to be appended to ca.
 *  @return  true if, and only if, s is appended successfully.
 */
extern du_bool du_xml_encode_cat(du_uchar_array* ca, const du_uchar* s);

/**
 *  Appends element start tag to a destination array.
 *  @param[in,out] ca destination du_uchar_array structure.
 *  @param[in] name string of tag name.
 *  @param[in] close set true if you append "<name>" to ca, otherwise,
 *             "<name" string is appended to ca.
 *  @return true if, and only if, the element start tag is appended successfully.
 *  @remark du_xml_element_start() appends "<name>" or "<name" string to ca.
 */
extern du_bool du_xml_element_start(du_uchar_array* ca, const du_uchar* name, du_bool close);

/**
 *  Appends element end tag to a destination array.
 *  @param[in,out] ca destination du_uchar_array structure.
 *  @param[in] name string of tag name.
 *  @return  true if, and only if, the element end tag is appended successfully.
 *  @remark du_xml_element_end() appends "</name>" string to ca.
 */
extern du_bool du_xml_element_end(du_uchar_array* ca, const du_uchar* name);

/**
 *  Appends xmlns string to a destination array.
 *  @param[in,out] ca destination du_uchar_array structure.
 *  @param[in] prefix prefix string. If prefix = 0 or prefix = "" then predix string is not
 *             appended to the destination array.
 *  @param[in] ns namespace string.
 *  @return  true if, and only if, the xmlns string is appended successfully.
 *  @remark du_xml_ns_cat() appends ' xmlns:prefix="ns"' or ' xmlns="ns"' string to ca.
 */
extern du_bool du_xml_ns_cat(du_uchar_array* ca, const du_uchar* prefix, const du_uchar* ns);

/**
 *  Appends a xml element string to a destination array.
 *  @param[in,out] ca destination du_uchar_array structure.
 *  @param[in] name string of tag name.
 *  @param[in] value string of content.
 *  @return  true if, and only if, the a xml element string is appended successfully.
 *  @remark du_xml_element_cat appends
 *  "<name>value</name>" string and CRLF codes to
 *  ca.
 */
extern du_bool du_xml_element_cat(du_uchar_array* ca, const du_uchar* name, const du_uchar* value);

/**
 *  Appends a xml attribute string to a destination array.
 *  @param[in,out] ca destination du_uchar_array structure.
 *  @param[in] name string of attribute name.
 *  @param[in] value string of value.
 *  @return  true if, and only if, the xml attribute string is appended successfully.
 *  @remark du_xml_attribute_cat appends
 *  ' name = "value"' string to ca.
 */
extern du_bool du_xml_attribute_cat(du_uchar_array* ca, const du_uchar* name, const du_uchar* value);

#ifdef __cplusplus
}
#endif

#endif
