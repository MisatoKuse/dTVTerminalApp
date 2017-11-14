/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_uri interface provides methods for manipulating of a uniform resource
 *  identifier (URI) string and easy access to the parts of the URI.
 *  The following is an example URI and its component parts: <br>
 *    http://example.com:8042/over/there?name1=value1&name2=value2 <br>
 *      "http" protocol <br>
 *      "example.com" host <br>
 *      "8042"  port number <br>
 *      "/over/there" path <br>
 *       "name1=value1&name2=value2"   query <br>
 *    Before using a du_uri, use <b>du_uri_init</b> to initialize du_uri data structure.
 *    Deleting the allocated memory of du_uri, use <b>du_uri_free</b>.
 */

#ifndef DU_URI_H
#define DU_URI_H

#include "du_type.h"
#include "du_str_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_uri.
 */
typedef struct du_uri {
    du_str_array array;
} du_uri;

/**
 *  Initializes a du_uri data area.
 *  @param[out] u  pointer to the du_uri structure.
 */
extern void du_uri_init(du_uri* u);

/**
 *  Parses the given URI.
 *  @param[out] u pointer to the du_uri structure.
 *  @param[in] uri URI string
 *  @remark u is a pointer to a du_uri initialized by
 *          the <b>du_uri_init</b> function.
 */
extern du_bool du_uri_parse(du_uri* u, const du_uchar* uri);

/**
 *  Resets u to reuse it.
 *  @param[in] u pointer to the du_uri structure.
 */
extern void du_uri_reset(du_uri* u);

/**
 *  Frees the resources used by u.
 *  @param[in] u pointer to the du_uri structure.
 */
extern void du_uri_free(du_uri* u);

/**
 *  Gets the protocol information ( like "http" , "file", etc.)
 *   specified in the du_uri u.
 *  @param[in] u pointer to the du_uri structure.
 *  @return the protocol information string. If the protocol information
 *  is not stored in u, returns NULL string.
 */
extern const du_uchar* du_uri_get_protocol(const du_uri* u);

/**
 *  Gets the host information ( Domain Name System (DNS) host name,
 *   or IP address ) specified in the du_uri u.
 *  @param[in] u pointer to the du_uri structure.
 *  @return the host information string. If the host information
 *  is not stored in u, returns NULL string.
 */
extern const du_uchar* du_uri_get_host(const du_uri* u);

/**
 *  Gets the port number specified in the du_uri u.
 *  @param[in] u du_uri structure.
 *  @return the port number information string. If the port number information
 *  is not stored in u, returns NULL string.
 */
extern const du_uchar* du_uri_get_port(const du_uri* u);

/**
 *  Gets the path information of the specified du_uri u.
 *  @param[in] u pointer to the du_uri structure.
 *  @return the path information string. If the path information
 *  is not stored in u, returns NULL string.
 */
extern const du_uchar* du_uri_get_path(const du_uri* u);

/**
 *  Gets any query information included in the specified du_uri u.
 *  @param[in] u pointer to the du_uri structure.
 *  @return the query information string. If the query information
 *  is not stored in u, returns NULL string.
 */
extern const du_uchar* du_uri_get_query(const du_uri* u);

/**
 *  Gets a string representation for the specified du_uri u.
 *  @param[in] u pointer to the du_uri structure.
 *  @param[out] ca string representation for the specified du_uri u.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_uri_to_string(const du_uri* u, du_uchar_array* ca);

/**
 *  Parses the query string.
 *  @param[in] query a string containing any query information.
 *  @param[out] name_array pointer to the du_str_array to receive the name strings.
 *  @param[out] value_array pointer to the du_str_array to receive the value strings.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark name_array and value_array are pointers to a du_str_array
 *  initialized by the <b>du_str_array_init</b> function.
 */
extern du_bool du_uri_parse_query(const du_uchar* query, du_str_array* name_array, du_str_array* value_array);

/**
 *  Parses the query string.
 *  @param[in] query a string containing any query information.
 *  @param[out] param to be stored the name-value pairs
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark param is a pointer to a du_str_array
 *  initialized by the <b>du_str_array_init</b> function.
 */
extern du_bool du_uri_parse_query2(const du_uchar* query, du_str_array* param);

/**
 *  Resolves the absolute URI from the base and relative URIs.
 *  @param[in] base the base URI used to resolve the relative URI.
 *  @param[in] uri the URI to resolve. The uri can be absolute or relative.
 *   If absolute, this value effectively replaces the base value.
 *   If relative, it combines with the base to make an absolute URI.
 *  @param[out] absolute a Uri representing the absolute URI.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark absolute is a pointer to a du_uchar_array
 *  initialized by the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_uri_resolve(const du_uchar* base, const du_uchar* uri, du_uchar_array* absolute);

/**
 *  Converts an absolute file path to a URI.
 *
 *  @param[in] utf8_abs_path an absolute path string.
 *  @param[out] uri pointer to du_uchar_array structure to receive the converted URI form data.
 *              The form of @uri is "file:///path".
 *  @retval 1 the function succeeded.
 *  @retval 0 the function failed.
 *  @remark "."/".." in @utf8_abs_path must be resolved
 *          prior to passing it to this function.
 *  @remark For Windows, the path must be started with a drive letter.
 *          (ex. "C:\path\to\resource")
 */
extern du_bool du_uri_path_to_file_uri(const du_uchar* utf8_abs_path, du_uchar_array* uri);

/**
 *  Removes all characters before the path part in s from the beginning of s.
 *  @param[in,out] uri a string to be removed from the beginning.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_uri_trim_path_prefix(du_uchar* uri);

/**
 *  Unescapes the URL escaped string.
 *  @param[in] s source string.
 *  @param[out] ca result string.
 *  @remark ca is a pointer to a du_uchar_array
 *  initialized by the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_uri_unescape(const du_uchar* s, du_uchar_array* ca);

/**
 *  Escapes a string in application/x-www-form-urlencoded manner.
 *  This function can be used for escaping query string values.
 *
 *  @param[in] s source string.
 *  @param[out] ca result string.
 *  @remark ca is a pointer to a du_uchar_array
 *  initialized by the <b>du_uchar_array_init</b> function.
 *  escaped ( percent-encoded ) format is the pattern "%hexhex",
 *   where "hex" is a digit from 0-9 or a letter from A-F (case-insensitive).
 */
extern du_bool du_uri_escape(const du_uchar* s, du_uchar_array* ca);

/**
 *  Escapes a URI component in URI escaping manner.
 *
 *  @param s source string.
 *  @param ca result string.
 *  @retval 1 the function succeeded.
 *  @retval 0 the function failed.
 *  @remark The main difference between du_uri_escape() and this function
 *  is that du_uri_escape() encodes a space to "+", but this function
 *  encodes it to "%20".
 */
extern du_bool du_uri_escape_uri_component(const du_uchar* s, du_uchar_array* ca);

/**
 *  Checks whether a string is URL escaped.
 *  @param[in] s string to check
 *  @return true if s has escaped ( percent-encoded ) format characters, otherwise, false.
 *  @remark escaped ( percent-encoded ) format is the pattern "%hexhex",
 *   where "hex" is a digit from 0-9 or a letter from A-F (case-insensitive).
 */
extern du_bool du_uri_is_escaped(const du_uchar* s);

/**
 *  Checks whether a string should be URL escaped.
 *  @param[in] s string to check
 *  @return true if s has the specific character to convert, otherwise, false.
 */
extern du_bool du_uri_should_escape(const du_uchar* s);

// for internal use.
extern du_bool du_uri_escape_uri_components(du_str_array* components, du_str_array* dest);

extern du_bool du_uri_is_supported_path(const du_uchar* utf8_path);

#ifdef __cplusplus
}
#endif

#endif
