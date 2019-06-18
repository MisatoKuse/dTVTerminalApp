/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_parser_packed.h
 *  @brief dav_didl_parser_packed interface provides methods for parsing a DIDL XML document.
 */

#ifndef DAV_DIDL_PARSER_PACKED_H
#define DAV_DIDL_PARSER_PACKED_H

#include <du_type.h>
#include <dav_didl_object_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Parses an <em>xml</em> DIDL document and stores the content in <em>obj_array</em>.
 * @param[in] xml a string containing all of the document to parse.
 * @param[in] xml_len length of xml.
 * @param[out] obj_array pointer to the destination dav_didl_object_array structure.
 *             This array isn't truncated in this function.
 *             dav_didl_object(s) obtained by pasing DIDL is concatenated to this array.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>obj_array</em> is a pointer to a <em>dav_didl_object_array</em> initialized by
 * the <b>dav_didl_object_array_init</b> function.
 * <em>obj_array</em> remains until it is freed by dav_didl_object_array_free().
 * This API isn't recomended to use. Use dav_didl_parser_packed_parse2().
 */
extern du_bool dav_didl_parser_packed_parse(const du_uchar* xml, du_uint32 xml_len, dav_didl_object_array* obj_array);

/**
 * Parses an <em>xml</em> DIDL document and stores the content in <em>obj_array</em>.
 * @param[in] xml a string containing all of the document to parse.
 * @param[in] xml_len length of xml.
 * @param[in] truncate_in_advance set true if you want to truncate <em>obj_array</em> in advance.
 * @param[out] obj_array pointer to the destination dav_didl_object_array structure.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>obj_array</em> is a pointer to a <em>dav_didl_object_array</em> initialized by
 * the <b>dav_didl_object_array_init</b> function.
 * <em>obj_array</em> remains until it is freed by dav_didl_object_array_free().
 */
extern du_bool dav_didl_parser_packed_parse2(const du_uchar* xml, du_uint32 xml_len, du_bool truncate_in_advance, dav_didl_object_array* obj_array);

/**
 * Parses a DIDL document in <em>file_name</em> file and stores the content in <em>obj_array</em>.
 * @param[in] file_name a name of file that containes all of the document to parse.
 * @param[out] obj_array pointer to the destination dav_didl_object_array structure.
 *             This array isn't truncated in this function.
 *             dav_didl_object(s) obtained by pasing DIDL is concatenated to this array.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>obj_array</em> is a pointer to a <em>dav_didl_object_array</em> initialized by
 * the <b>dav_didl_object_array_init</b> function.
 * <em>obj_array</em> remains until it is freed by dav_didl_object_array_free().
 * This API isn't recomended to use. Use dav_didl_parser_packed_parse_file2().
 */
extern du_bool dav_didl_parser_packed_parse_file(const du_uchar* file_name, dav_didl_object_array* obj_array);

/**
 * Parses a DIDL document in <em>file_name</em> file and stores the content in <em>obj_array</em>.
 * @param[in] file_name a name of file that containes all of the document to parse.
 * @param[in] truncate_in_advance set true if you want to truncate <em>obj_array</em> in advance.
 * @param[out] obj_array pointer to the destination dav_didl_object_array structure.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>obj_array</em> is a pointer to a <em>dav_didl_object_array</em> initialized by
 * the <b>dav_didl_object_array_init</b> function.
 * <em>obj_array</em> remains until it is freed by dav_didl_object_array_free().
 */
extern du_bool dav_didl_parser_packed_parse_file2(const du_uchar* file_name, du_bool truncate_in_advance, dav_didl_object_array* obj_array);

#ifdef __cplusplus
}
#endif

#endif
