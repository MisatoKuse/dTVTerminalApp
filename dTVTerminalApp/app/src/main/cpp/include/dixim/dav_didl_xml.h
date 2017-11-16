/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */
/** @file dav_didl_xml.h
 * @brief The dav_didl_xml interface provides methods for manipulating didl XML string
 * (such as appending didl start tag).
 */

#ifndef DAV_DIDL_XML_H
#define DAV_DIDL_XML_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Appends didl XML start tag to a destination array.
 * xmlns(es) which are declared in the didl XML start tag are same as the result of dav_didl_get_namespace_list().
 * @param[in,out] xml destination du_uchar_array structure.
 * @return true if, and only if, the didl XML start tag is appended successfully.
 */
extern du_bool dav_didl_xml_start(du_uchar_array* xml);

/**
 * Appends didl XML start tag to a destination array.
 * xmlns(es) which are specified in xmlns_prefix_param_array are declared in the didl XML start tag.
 * @param[in] xmlns_prefix_param_array pairs of xmlns prefix and xmlns to declar in the didl XML start tag.
 * If null is specified, the result is same as dav_didl_xml_start().
 * @param[in,out] xml destination du_uchar_array structure.
 * @return true if, and only if, the didl XML start tag is appended successfully.
 */
extern du_bool dav_didl_xml_start2(du_str_array* xmlns_prefix_param_array, du_uchar_array* xml);

/**
 * Appends didl XML end tag to a destination array.
 * @param[in,out] xml destination du_uchar_array structure.
 * @return true if, and only if, the didl XML end tag is appended successfully.
 */
extern du_bool dav_didl_xml_end(du_uchar_array* xml);

#ifdef __cplusplus
}
#endif

#endif
