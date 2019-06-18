/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_gena.h
 *  @brief The dupnp_gena interface provides some methods for manipulating UPnP event messages
 *  ( such as parsing the UPnP event message, getting a value of a specified name in the
 *    UPnP event messages).
 */

#ifndef DUPNP_GENA_H
#define DUPNP_GENA_H

#include <dupnp_impl.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_gena_init(dupnp_gena* gena, dupnp_impl* upnp);

extern void dupnp_gena_free(dupnp_gena* gena);

extern du_bool dupnp_gena_start(dupnp_gena* gena);

extern void dupnp_gena_stop(dupnp_gena* gena);

/**
 * Parses an <em>xml</em> UPnP event message and stores the variable names and values
 * in <em>prop_array</em>.
 * @param[out] prop_array pointer to the destination du_str_array structure.
 * @param[in] xml a string containing all of the UPnP event message to parse.
 * @param[in] xml_len length of xml.
 * @return  If a parse error occurred, it returns false. Otherwise it returns true.
 * @remark <em>prop_array</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dupnp_gena_parse_notify(du_str_array* prop_array, const du_uchar* xml, du_uint32 xml_len);

/**
 * Returns a value of a specified <em>name</em> variable name stored in
 * <em>prop_array</em> array.
 * @param[in] prop_array pointer to the du_str_array structure.
 * @param[in] name the name whose associated value is to be returned.
 * @return  the pointer to the value to which <em>name</em> specified,
 * or null if the <em>prop_array</em> contains no data for this <em>name</em>.
 */
extern const du_uchar* dupnp_gena_prop_array_get_value(du_str_array* prop_array, const du_uchar* name);

/**
 * Appends a tag of body data of an event message to <em>xml</em>.
 * @param[in] xml pointer to the du_uchar_array.
 * @remark This function appends the the following string in <em>xml</em><br>
 * \<e:propertyset xmlns:e="urn:schemas-upnp-org:event-1-0"\> <br>
 */
extern du_bool dupnp_gena_property_set_start(du_uchar_array* xml);

/**
 * Appends a tag of body data of an event message to <em>xml</em>.
 * @param[in] xml pointer to the du_uchar_array.
 * @remark This function appends the the following string in <em>xml</em><br>
 * \</e:propertyset\> <br>
 */
extern du_bool dupnp_gena_property_set_end(du_uchar_array* xml);

/**
 * Appends an element of body data of an event message to <em>xml</em>.
 * @param[in] xml pointer to the du_uchar_array.
 * @param[in] name variable name of event message.
 * @param[in] value value of event message.
 * @remark This function appends the elememt in <em>xml</em> in the following format. <br>
 * \<e:property\><br>
 * \<name\>value\</name\><br>
 * \</e:property\><br>
 */
extern du_bool dupnp_gena_add_property(du_uchar_array* xml, const du_uchar* name, const du_uchar* value);

#ifdef __cplusplus
}
#endif

#endif
