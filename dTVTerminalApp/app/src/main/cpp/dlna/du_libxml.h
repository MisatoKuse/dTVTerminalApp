/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_libxml interface provides utility methods for libxml2.
 */

#ifndef DU_LIBXML_H
#define DU_LIBXML_H

#include "du_uchar_array.h"
#include <libxml/tree.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Tests if the name of the element equals to the specified name.
 *  @param[in] node the element.
 *  @param[in] ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in] name the element name without prefix. ex. "friendlyName".
 *  @return true if the name of the element equals to the specified name,
 *          otherwise false.
 */
extern du_bool du_libxml_element_name_equal(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name);

/**
 *  Tests if the name of the attribute equals to the specified name.
 *  @param[in] attr the attribute.
 *  @param[in] ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in] name the attribute name without prefix. ex. "friendlyName".
 *  @return true if the name of the attribute equals to the specified name,
 *          otherwise false.
 */
extern du_bool du_libxml_attribute_name_equal(const xmlAttrPtr attr, const du_uchar* ns, const du_uchar* name);

/**
 *  Tests if the a and b are the same element.
 *  @param[in] a the element.
 *  @param[in] b the element.
 *  @return true if the a and b are the same element,
 *          otherwise false.
 *  @remark  This API doesn't check identity of elements recursively.
 */
extern du_bool du_libxml_element_equal(const xmlNodePtr a, const xmlNodePtr b);

/**
 *  Tests if the a and b are the same attribute.
 *  @param[in] a the attribute.
 *  @param[in] b the attribute.
 *  @return true if the a and b are the same attribute,
 *          otherwise false.
 */
extern du_bool du_libxml_attribute_equal(const xmlNodePtr a, const xmlNodePtr b);

/**
 *  Tests if the content of the element equals to the specified value.
 *  @param[in] node the element.
 *  @param[in] content the content to be compared.
 *  @return true if the content of the element equals to the specified content,
 *          otherwise false.
 */
extern du_bool du_libxml_content_equal(const xmlNodePtr node, const du_uchar* content);

/**
 *  Tests if the value of the attr equals to the specified value.
 *  @param[in] attr the attribute.
 *  @param[in] value the value of attribute to be compared.
 *  @return true if the value of the attr equals to the specified value,
 *          otherwise false.
 */
extern du_bool du_libxml_attribute_value_equal(const xmlAttrPtr attr, const du_uchar* value);

/**
 *  Returns the first element child that matches the given name.
 *  @param[in] node the node list.
 *  @param[in] ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in] name the element name without prefix. ex. "friendlyName".
 *  @return the first element child that matches the given name,
 *          NULL if not found.
 */
extern xmlNodePtr du_libxml_get_element_by_name(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name);

/**
 *  Returns the n-th element child that matches the given name.
 *  @param[in] node the node list.
 *  @param[in] ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in] name the element name without prefix. ex. "friendlyName".
 *  @param[in] n position starting from 0.
 *               You should specify 1 if you want to get element that maches the given name secondarily.
 *  @return the n-th element child that matches the given name,
 *          NULL if not found.
 */
extern xmlNodePtr du_libxml_get_element_by_name2(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, du_uint32 n);

/**
 *  Returns the n-th element child.
 *  @param[in] node the node list.
 *  @param[in] n position starting from 0.
 *               You should specify 1 if you want to get element secondarily.
 *  @return the n-th element child,
 *          NULL if not found.
 */
extern xmlNodePtr du_libxml_get_element_by_index(const xmlNodePtr node, du_uint32 n);

/**
 *  Returns the first attribute child that matches the given name.
 *  @param[in] node the node list.
 *  @param[in] ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in] name the attribute name without prefix. ex. "refID".
 *  @return the first attribute that matches the given name,
 *          NULL if not found.
 */
extern xmlAttrPtr du_libxml_get_attribute_by_name(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name);

/**
 *  Returns the n-th attribute.
 *  @param[in] node the node list.
 *  @param[in] n position starting from 0.
 *               You should specify 1 if you want to get attribute secondarily.
 *  @return the n-th attribute,
 *          NULL if not found.
 */
extern xmlAttrPtr du_libxml_get_attribute_by_index(const xmlNodePtr node, du_uint32 n);

/**
 *  Gets the content of the element.
 *  @param[in]  node the node.
 *  @param[out] content content of the node.
 *              It doesn't terminated by null (0).
 *  @return true if succeeded. otherwise return false.
 */
extern du_bool du_libxml_get_content(const xmlNodePtr node, du_uchar_array* content);

/**
 *  Gets the content of the element.
 *  @param[in] node the node.
 *  @param[in] tmp this is used for storing content of the element.
 *  @return the content of the element. return null if the content of the element is empty string.
 *  @remark Returned value points to the memory that allocated in tmp.
 *          After you change tmp, returned value becomes invalid.
 */
extern const du_uchar* du_libxml_get_content_s(const xmlNodePtr node, du_uchar_array* tmp);

/**
 *  Gets the content of the first element child that matches the given name.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the element name without prefix. ex. "friendlyName".
 *  @param[out] content content of the first element child that matches the given name.
 *  @return true if succeeded. otherwise return false.
 */
extern du_bool du_libxml_get_content_by_name(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, du_uchar_array* content);

/**
 *  Gets the content of the first element child that matches the given name.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the element name without prefix. ex. "friendlyName".
 *  @param[in]  tmp this is used for storing content of the element.
 *  @return the content of the element that matches the given name.
 *          return null if the content of the element is empty string.
 *  @remark Returned value points to the memory that allocated in tmp.
 *          After you change tmp, returned value becomes invalid.
 */
extern const du_uchar* du_libxml_get_content_by_name_s(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, du_uchar_array* tmp);

/**
 *  Gets the value of the attribute.
 *  @param[in]  attr the attribute.
 *  @param[out] value value of the attr.
 *              It doesn't terminated by null (0).
 *  @return true if succeeded. otherwise return false.
 */
extern du_bool du_libxml_get_attribute_value(const xmlAttrPtr attr, du_uchar_array* value);

/**
 *  Gets the value of the attribute.
 *  @param[in]  attr the attribute.
 *  @param[in]  tmp this is used for storing value of the attribute.
 *  @return the value of the attribute.
 *          return null if the content of the element is empty string.
 *  @remark Returned value points to the memory that allocated in tmp.
 *          After you change tmp, returned value becomes invalid.
 */
extern const du_uchar* du_libxml_get_attribute_value_s(const xmlAttrPtr attr, du_uchar_array* tmp);

/**
 *  Gets the value of the attribute that matches the given name.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the attribute name without prefix. ex. "refID".
 *  @param[out] value value of the attribute that matches the given name.
 *  @return true if succeeded. otherwise return false.
 */
extern du_bool du_libxml_get_attribute_value_by_name(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, du_uchar_array* value);

/**
 *  Gets the value of the attribute that matches the given name.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the attribute name without prefix. ex. "refID".
 *  @param[in]  tmp this is used for storing value of the attribute.
 *  @return the value of the attribute.
 *          return null if the content of the element is empty string.
 *  @remark Returned value points to the memory that allocated in tmp.
 *          After you change tmp, returned value becomes invalid.
 */
extern const du_uchar* du_libxml_get_attribute_value_by_name_s(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, du_uchar_array* tmp);

/**
 *  Sets the attribute.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the attribute name without prefix. ex. "refID".
 *  @param[in]  value the attribute value.
 *  @return created attribute if succeeded. otherwise return null.
 */
extern xmlAttrPtr du_libxml_set_attribute(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, const du_uchar* value);

/**
 *  Sets the attribute value.
 *  @param[in]  attr the attribute.
 *  @param[in]  value the attribute value.
 *  @return true if succeeded. otherwise return false.
 */
extern du_bool du_libxml_set_attribute_value(const xmlAttrPtr attr, const du_uchar* value);

/**
 *  Sets the content of the element.
 *  @param[in]  node the node.
 *  @param[in]  content the content of the element.
 *  @return true if succeeded. otherwise return false.
 */
extern du_bool du_libxml_set_content(const xmlNodePtr node, const du_uchar* content);

/**
 *  Replace or add the element in the child of the node.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the element name without prefix. ex. "friendlyName".
 *  @param[in]  content the content of the element.
 *  @param[in]  replace if same name of element exists in the child of the node:
 *                      repalce element if specified true, otherwise nothing is done.
 *  @return return the replaced or added element.
 */
extern xmlNodePtr du_libxml_set_child_element(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, const du_uchar* content, du_bool replace);

/**
 *  Adds the element in the child of the node.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the element name without prefix. ex. "friendlyName".
 *  @param[in]  content the content of the element.
 *  @return return the added element.
 */
extern xmlNodePtr du_libxml_add_child_element(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name, const du_uchar* content);

/**
 *  Removes the element.
 *  @param[in]  node the node to remove.
 *  @return true if succeeded, otherwise return false.
 */
extern du_bool du_libxml_remove_element(const xmlNodePtr node);

/**
 *  Removes the element that matches the given name.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the element name without prefix. ex. "friendlyName".
 *  @return true if succeeded, otherwise return false.
 */
extern du_bool du_libxml_remove_element_by_name(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name);

/**
 *  Removes the n-th element in the node.
 *  @param[in]  node the node.
 *  @param[in]  n position starting from 0.
 *                You should specify 1 if you want to remove second element.
 *  @return true if succeeded, otherwise return false.
 */
extern du_bool du_libxml_remove_element_by_index(const xmlNodePtr node, du_uint32 n);

/**
 *  Removes the attribute.
 *  @param[in]  attr the attribute to remove.
 *  @return true if succeeded, otherwise return false.
 */
extern du_bool du_libxml_remove_attribute(const xmlAttrPtr attr);

/**
 *  Removes the attribute that matches the given name.
 *  @param[in]  node the node.
 *  @param[in]  ns the namespace. ex. "urn:schemas-upnp-org:device-1-0"
 *  @param[in]  name the element name without prefix. ex. "refID".
 *  @return true if succeeded, otherwise return false.
 */
extern du_bool du_libxml_remove_attribute_by_name(const xmlNodePtr node, const du_uchar* ns, const du_uchar* name);

/**
 *  Removes the n-th attribute in the node.
 *  @param[in]  node the node.
 *  @param[in]  index position starting from 0.
 *                You should specify 1 if you want to remove second attribute.
 *  @return true if succeeded, otherwise return false.
 */
extern du_bool du_libxml_remove_attribute_by_index(const xmlNodePtr node, du_uint32 index);

/**
 *  Copies the src element to the child of tgt_parent element.
 *  @param[in]  tgt_parent the node.
 *  @param[in]  src the node.
 *  @return copied element if succeeded, otherwise return NULL.
 */
extern xmlNodePtr du_libxml_copy_element(const xmlNodePtr tgt_parent, const xmlNodePtr src);

/**
 *  Copies the attr attribute to the tgt element.
 *  @param[in]  tgt the node.
 *  @param[in]  attr the attribute.
 *  @return copied element if succeeded, otherwise return NULL.
 */
extern xmlAttrPtr du_libxml_copy_attribute(const xmlNodePtr tgt, const xmlAttrPtr attr);

 /**
  * Generates XML (<em>xml</em>) from xmlDocPtr structure (<em>doc</em>).
  * @param[in] doc target xmlDocPtr to generate XML.
  * @param[in] xml XML string where output is stored.
  * @param[in] disable_declaration specify true if you don't want to contain XML declaration in generated XML.
  * @return true if succeeded, otherwise return false.
  */
extern du_bool du_libxml_make_xml(const xmlDocPtr doc, du_uchar_array* xml, du_bool disable_declaration);

/**
 *  Creates xmlDocPtr structure (doc) from XML (xml).
 *  @param[in] xml XML string.
 *  @param[in] xml_len XML string length in bytes.
 *  @param[in] encoding XML encoding, or NULL.
 *  @return pointor to created xmlDocPtr.
 */
extern xmlDocPtr du_libxml_make_doc(const du_uchar* xml, du_uint32 xml_len, const du_uchar* encoding);

#ifdef __cplusplus
}
#endif

#endif
