/*
 * Copyright (c) 2009 DigiOn, Inc. All rights reserved.
 */

/**
 * @file dav_didl_libxml.h
 * @brief The dav_didl_libxml interface provides methods for accessing DIDL-Lite objects
 *  using libxml2 library.
 *  The functions provided by this interface treats the namespaces strictly. When
 *  an element name or an attribute name are given with prefix, the functions will also
 *  check the corresponded namespace. So before using this interface, you have to register
 *  the pair of the prefix and the namespace by dav_didl_add_namespace function if the pair
 *  is not registered by default. See dav_didl_add_namespace API document more details.
 */

#ifndef DAV_DIDL_LIBXML_H
#define DAV_DIDL_LIBXML_H

#include <dav_didl_object_array.h>
#include <du_uchar.h>
#include <libxml/tree.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Tests if the name of the element equals to the specified name.
 * @param[in] element the element.
 * @param[in] pname the element name which may have a prefix. ex. "dc:title".
 * @return true if the name of the element equals to the specified name,
 *         otherwise false.
 */
extern du_bool dav_didl_libxml_element_name_equal(const xmlNodePtr element, const du_uchar* pname);

/**
 * Tests if the name of the attribute equals to the specified name.
 * @param[in] attr the attribute.
 * @param[in] pname the attribute name which may have a prefix. ex. "dlna:profileID".
 * @return true if the name of the attribute equals to the specified name,
 *         otherwise false.
 */
extern du_bool dav_didl_libxml_attribute_name_equal(const xmlAttrPtr attr, const du_uchar* pname);

/**
 * Returns the first child element that matches the given name.
 * @param[in] children the node list.
 * @param[in] pname the element name which may have a prefix. ex. "dc:title".
 * @return the first element child that matches the given name,
 *         NULL if not found.
 */
extern xmlNodePtr dav_didl_libxml_get_element_by_name(const xmlNodePtr children, const du_uchar* pname);

/**
 * Returns the <em>n</em>-th child element that matches the given name.
 * @param[in] children the node list.
 * @param[in] pname the element name which may have a prefix. ex. "dc:title".
 * @param[in] n position starting from 0.
 *              You should specify 1 if you want to get element that maches the given name secondarily.
 * @return the <em>n</em>th element child that matches the given name,
 *         NULL if not found.
 */
extern xmlNodePtr dav_didl_libxml_get_element_by_name2(const xmlNodePtr children, const du_uchar* pname, du_uint32 n);

/**
 * Returns the attribute that matches the given name.
 * @param[in] node the node.
 * @param[in] pname the element name which may have a prefix. ex. "dlna:profileID".
 * @return the first element child that matches the given name,
 *         NULL if not found.
 */
extern xmlAttrPtr dav_didl_libxml_get_attribute_by_name(const xmlNodePtr node, const du_uchar* pname);

/**
 * Gets the content of the first element child that matches the given name.
 * @param[in] children the node list.
 * @param[in] pname the element name which may have a prefix. ex. "dc:title".
 * @param[out] contents the contents.
 * @return  true if the function succeeds, otherwise false.
 */
extern du_bool dav_didl_libxml_get_content_by_name(const xmlNodePtr children, const du_uchar* pname, du_uchar_array* contents);

/**
 * Returns the content of the first element child that matches the given name.
 * @param[in] children the node list.
 * @param[in] pname the element name which may have a prefix. ex. "dc:title".
 * @param[in] tmp this is used for storing content of the element.
 * @return the content of the element. return null if the content of the element is empty string.
 * @remark Returned value points to the memory that allocated in <em>tmp</em>.
 *         After you change <em>tmp</em>, returned value becomes invalid.
 */
extern const du_uchar* dav_didl_libxml_get_content_by_name_s(const xmlNodePtr children, const du_uchar* pname, du_uchar_array* tmp);

/**
 * Returns the value of the attribute that matches the given name.
 * @param[in] node the node.
 * @param[in] pname the attribute name which may have a prefix. ex. "dlna:profileID".
 * @param[in] value matched value of the <em>pname</em>.
 * @return the content of the element. return null if the content of the element is empty string.
 * @remark Returned value points to the memory that allocated in <em>tmp</em>.
 *         After you change <em>tmp</em>, returned value becomes invalid.
 */
extern du_bool dav_didl_libxml_get_attribute_value_by_name(const xmlNodePtr node, const du_uchar* pname, du_uchar_array* value);

/**
 * Returns the value of the attribute that matches the given name.
 * @param[in] node the node.
 * @param[in] pname the attribute name which may have a prefix. ex. "dlna:profileID".
 * @param[in] tmp this is used for storing content of the element.
 * @return  true if the function succeeds, otherwise false.
 * @remark Returned value points to the memory that allocated in <em>tmp</em>.
 *         After you change <em>tmp</em>, returned value becomes invalid.
 */
extern const du_uchar* dav_didl_libxml_get_attribute_value_by_name_s(const xmlNodePtr node, const du_uchar* pname, du_uchar_array* tmp);

/**
 * Normalizes <em>attr</em> attribute name string. Checks <em>attr</em> to ensure that
 * <em>attr</em> is a consistent name of an attribute.
 * @param[in] attr the attribute name which may have a prefix. ex. "dlna:profileID".
 * @return the pointer to the nomalized string of <em>attr</em> attribute name if <em>attr</em>
 *  is a consistent attribute name, otherwise null.
 */
extern const dav_didl_name* dav_didl_libxml_normalize_attribute(const xmlAttrPtr attr);

/**
 * Normalizes <em>node</em> element name string. Checks <em>node</em> to ensure that
 * <em>node</em> is a consistent name of element.
 * @param[in] node name of element to nomalize.
 * @return the pointer to the nomalized string of <em>node</em> if <em>node</em>
 *  is a consistent element name, otherwise null.
 */
extern const dav_didl_name* dav_didl_libxml_normalize_element(const xmlNodePtr node);

/**
 * Adds the element in the child of the <em>node</em>.
 * @param[in]  node the node.
 * @param[in]  pname the element name which may have a prefix. ex. "dc:title".
 * @param[in]  contents the content of the element.
 * @return return the added element.
 */
extern xmlNodePtr dav_didl_libxml_add_child_element(xmlNodePtr node, const du_uchar* pname, const du_uchar* contents);

/**
 * Replace or add the element in the child of the <em>node</em>.
 * @param[in]  node the node.
 * @param[in]  pname the element name which may have a prefix. ex. "dc:title".
 * @param[in]  contents the content of the element.
 * @param[in]  replace if same name of element exists in the child of the <em>node</em>:
 *                     repalce element if specified true, otherwise nothing is done.
 * @return return the replaced or added element.
 */
extern xmlNodePtr dav_didl_libxml_set_child_element(xmlNodePtr node, const du_uchar* pname, const du_uchar* contents, du_bool replace);

/**
 * Sets the attribute.
 * @param[in]  node the node.
 * @param[in]  pname the element name which may have a prefix. ex. "dc:title".
 * @param[in]  value the attribute value.
 * @return created attribute if succeeded. otherwise return null.
 */
extern xmlAttrPtr dav_didl_libxml_set_attribute(xmlNodePtr node, const du_uchar* pname, const du_uchar* value);

/**
 * Converts an xmlNode item or container object to a dav_didl_object.
 * @param[in] doc the document.
 * @param[out] doa dav_didl_object_array.
 * @return  true if the function succeeds, otherwise false.
 * @remark the dav_didl_object must be initialized by dav_didl_object_init function
 *  before using this function, and the returned object must be freed by
 *  dav_didl_object_free function.
 */
extern du_bool dav_didl_libxml_convert_object_to_didl_object(const xmlDocPtr doc, dav_didl_object_array* doa);

/**
 * Removes the attribute that matches the given name.
 * @param[in]  node the node.
 * @param[in]  pname the element name which may have a prefix. ex. "dc:title".
 * @return true if succeeded, otherwise return false.
 */
extern du_bool dav_didl_libxml_remove_attribute_by_name(const xmlNodePtr node, const du_uchar* pname);

/**
 * Removes the element that matches the given name.
 * @param[in]  node the node.
 * @param[in]  pname the element name which may have a prefix. ex. "dc:title".
 * @return true if succeeded, otherwise return false.
 */
extern du_bool dav_didl_libxml_remove_element_by_name(const xmlNodePtr node, const du_uchar* pname);

 /**
  * Creates xmlDocPtr structure (<em>doc</em>) from XML (<em>xml</em>).
  * @param[in] xml DIDL-Lite string.
  * @param[in] xml_len XML string length in bytes.
  * @return pointor to created xmlDocPtr.
  * @remark <em>xml</em> is treated as UTF-8 encoding forcibly.
  */
extern xmlDocPtr dav_didl_libxml_make_doc(const du_uchar* xml, du_uint32 xml_len);


#ifdef __cplusplus
}
#endif

#endif
