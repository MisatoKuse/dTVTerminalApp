/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_object.h
 *  @brief The dav_didl_object interface provides methods for manipulating
 *  the name and value of DIDL elements/attributes
 *  ( such as counting the elements/attributes which has specified name,
 *   getting the value of specified element/attribute).
 */

#ifndef DAV_DIDL_OBJECT_H
#define DAV_DIDL_OBJECT_H

#include <du_type.h>
#include <dav_didl.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DAV_DIDL_OBJECT_PADDING_SIZE 4

/**
 * This structure contains the information of a name of an attribute and its value.
 */
typedef struct dav_didl_object_attribute {
    const dav_didl_name* name;   //!< name of the attribute.
    const du_uchar* value;       //!< value of the attribute.
} dav_didl_object_attribute;

/**
 * This structure contains the list of the attributes.
 */
typedef struct dav_didl_object_attribute_list {
    du_uint32 count;                 //!< number of attributes stored in <em>list</em>.
    dav_didl_object_attribute* list; //!< pointer to the dav_didl_object_attribute structure data.
} dav_didl_object_attribute_list;

/**
 * This structure contains the information of a name of a property(element) and its value
 * and attribute(s).
 */
typedef struct dav_didl_object_property {
    const dav_didl_name* name;  //!< name of the property(element).
    const du_uchar* value;      //!< value of the property(element).
    dav_didl_object_attribute_list* attr_list; //!< pointer to the dav_didl_object_attribute_list structure data.
} dav_didl_object_property;

/**
 * This structure contains the list of the properties(elements).
 */
typedef struct dav_didl_object_property_list {
    du_uint32 count;            //!< number of the properties(elements) stored in <em>list</em>.
    dav_didl_object_property* list; //!< pointer to the dav_didl_object_property structure data.
} dav_didl_object_property_list;

/**
 * @struct dav_didl_object
 * This structure contains the information of a DIDL object.<br>
 *
 * Here
 * @li <em>name</em> name of the DIDL object.
 * @li <em>attr_list</em> pointer to the dav_didl_object_attribute_list structure data.
 * @li <em>prop_list</em> pointer to the dav_didl_object_property_list structure data.
 */
typedef struct dav_didl_object {
    const dav_didl_name* name; //!< name of the DIDL object.
    dav_didl_object_attribute_list* attr_list; //!< pointer to the dav_didl_object_attribute_list structure data.
    dav_didl_object_property_list* prop_list; //!< pointer to the dav_didl_object_property_list structure data.
    void (*_free)(struct dav_didl_object*);
    du_bool (*_clone)(const struct dav_didl_object*, struct dav_didl_object*);
    du_uint8 _padding[DAV_DIDL_OBJECT_PADDING_SIZE];
} dav_didl_object;


/**
 * Creates a new dav_didl_object by copying the original <em>x</em> dav_didl_object.
 * @param[in] x pointer to a source dav_didl_object structure to copy.
 * @param[in,out] clone pointer to a storage location for new dav_didl_object data.
 * <em>clone</em> doesn't have to refer to unallocated space.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_didl_object_clone(const dav_didl_object* x, dav_didl_object* clone);

/**
 * Gets the value of the attribute specified by attribute name in <em>x</em>.
 * @param[in] x pointer to the dav_didl_object_attribute_list structure.
 * @param[in] attr_name the specified attribute name.
 * @return  the pointer to the value to which <em>attr_name</em>,
 * or null if the <em>x</em> contains no data for this <em>attr_name</em>.
 * @see <b>dav_didl_normalize_attribute</b>
 */
extern const du_uchar* dav_didl_object_attribute_list_get_attribute_value(dav_didl_object_attribute_list* x, const dav_didl_name* attr_name);

/**
 * Counts the number of elements which has specified element name stored in <em>x</em>.
 * @param[in] x pointer to the dav_didl_object_property_list structure.
 * @param[in] elem_name the specified element name to count.
 * @return the number of specified elements stored in <em>x</em>.
 * @see <b>dav_didl_normalize_element</b>
 */
extern du_uint32 dav_didl_object_property_list_count_properties(dav_didl_object_property_list* x, const dav_didl_name* elem_name);

/**
 * Gets the value of the element specified by element name and position stored in <em>x</em>.
 * @param[in] x pointer to the dav_didl_object_property_list structure.
 * @param[in] elem_name the specified element name.
 * @param[in] pos  the specified position.
 * @return  the pointer to the dav_didl_object_property structure data
 * to which <em>elem_name</em> and <em>pos</em> specified,
 * or null if the <em>x</em> contains no data for this <em>elem_name</em> and <em>pos</em>.
 * @see <b>dav_didl_normalize_element</b>
 */
extern dav_didl_object_property* dav_didl_object_property_list_get_property(dav_didl_object_property_list* x, const dav_didl_name* elem_name, du_uint32 pos);

/**
 * Initializes a dav_didl_object data area.
 * @param[out] x  pointer to the dav_didl_object data structure.
 */
extern void dav_didl_object_init(dav_didl_object* x);

/**
 * Frees the region used by <em>x</em>.
 * @param[in] x pointer to the dav_didl_object structure.
 */
extern void dav_didl_object_free(dav_didl_object* x);

/**
 * Tests if a value of the UPnP 'class' element in <em>x</em> is derived
 * from <em>super_class</em>.
 * @param[in] x pointer to the dav_didl_object structure.
 * @param[in] super_class a superclass ( prefix ) string.
 * @return true if the value of the UPnP 'class' element in <em>x</em>
 *   equals <em>super_class</em> or the character sequence represented by
 *   <em>super_class</em> followed by "."
 *    character is a prefix of the character sequence represented by
 *    the value of the UPnP 'class' element in <em>x</em>.
 *    Otherwise false.
 */
extern du_bool dav_didl_object_derived_from(dav_didl_object* x, const du_uchar* super_class);

/**
 * Make XML string from dav_didl_object structure.
 * @param[in] x pointer to the dav_didl_object structure.
 * @param[out] xml destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Result of this API contains xml namespace declarations which are same as result of dav_didl_get_namespace_list().
 * Use dav_didl_object_make_xml3() instead of this, if you want stripped-down xml namespace declarations.
 */
extern du_bool dav_didl_object_make_xml(const dav_didl_object* x, du_uchar_array* xml);

/**
 * Make XML string from dav_didl_object structure.
 * @param[in] x pointer to the dav_didl_object structure.
 * @param[in] include_didl_root_element true if the result contains DIDL-Lite root element.
 * @param[out] xml destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Result of this API contains xml namespace declarations which are same as result of dav_didl_get_namespace_list().
 * Use dav_didl_object_make_xml3() instead of this, if you want stripped-down xml namespace declarations.
 */
extern du_bool dav_didl_object_make_xml2(const dav_didl_object* x, du_bool include_didl_root_element, du_uchar_array* xml);

/**
 * Make XML string from dav_didl_object structure.
 * @param[in] x pointer to the dav_didl_object structure.
 * @param[in] include_didl_root_element true if the result contains DIDL-Lite root element.
 * @param[in] remove_unused_xmlns_declaration true if the result doesn't contain unused namespace declaration in the DIDL-Lite root element.
 * This parameter has no effect if include_didl_root_element is false.
 * @param[out] xml destination du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_didl_object_make_xml3(const dav_didl_object* x, du_bool include_didl_root_element, du_bool remove_unused_xmlns_declaration, du_uchar_array* xml);

/**
 * Checks whether specified object can be destroyed by OCM(Optional Content Management) operations defined DLNA.
 * @param[in] x pointer to the dav_didl_object structure.
 * @param[out] destroyable true if <em>x</em> is destroyable, otherwise false.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_didl_object_is_ocm_destroyable(const dav_didl_object* x, du_bool* destroyable);

/**
 * Checks whether an item can be uploaded to specified container by OCM(Optional Content Management) operations defined DLNA.
 * @param[in] x pointer to the dav_didl_object structure.
 * @param[out] uploadable true if <em>x</em> is uploadable, otherwise false.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_didl_object_is_ocm_uploadable_container(const dav_didl_object* x, du_bool* uploadable);

/**
 * Compares specified two dav_didl_object.
 * @param[in] a a first dav_didl_object.
 * @param[in] b a second dav_didl_object.
 * @return true if <em>a</em> is found to match <em>b</em>.
           false if <em>a</em> is not equal to <em>b</em>.
 */
extern du_bool dav_didl_object_equal(const dav_didl_object* a, const dav_didl_object* b);

/**
 * Compares specified two dav_didl_object_property.
 * @param[in] a a first dav_didl_object_property.
 * @param[in] b a second dav_didl_object_property.
 * @return true if <em>a</em> is found to match <em>b</em>.
           false if <em>a</em> is not equal to <em>b</em>.
 */
extern du_bool dav_didl_object_property_equal(const dav_didl_object_property* a, const dav_didl_object_property* b);

/**
 * Compares specified two dav_didl_object_attribute.
 * @param[in] a a first dav_didl_object_attribute.
 * @param[in] b a second dav_didl_object_attribute.
 * @return true if <em>a</em> is found to match <em>b</em>.
           false if <em>a</em> is not equal to <em>b</em>.
 */
extern du_bool dav_didl_object_attribute_equal(const dav_didl_object_attribute* a, const dav_didl_object_attribute* b);

#ifdef __cplusplus
}
#endif

#endif
