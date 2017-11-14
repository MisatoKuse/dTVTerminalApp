/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_multiparam interface provides methods for manipulating name and multiple values data.
 */

#ifndef DU_MULTIPARAM_H
#define DU_MULTIPARAM_H

#include "du_type.h"
#include "du_str_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Sets the named value. If you want to set multiple values, call this function several times with the same name.
 */
extern du_bool du_multiparam_set(du_str_array* x, const du_uchar* name, const du_uchar* value);

/**
 *  Gets the number of names.
 *  @param[in] x pointer to du_str_array structure.
 *  @return number of name-value pairs (elements) in x.
 */
extern du_uint32 du_multiparam_length(const du_str_array* x);

/**
 *  Gets a name by index.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] index index of the name-value pair (element) of x.
 *  @return pointer to the name data of the specified element in x,
 *   null if there is no index-element in x.
 */
extern const du_uchar* du_multiparam_get_name_by_index(const du_str_array* x, du_uint32 index);

/**
 *  Gets a value by index.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] index index of the name-value pair (element) of x.
 *  @return pointer to the value data of the specified element in x,
 *   null if there is no index-element in x.
 */
extern const du_uchar* du_multiparam_get_value_by_index(const du_str_array* x, du_uint32 index);

/**
 *  Gets the list of names.
 */
extern du_bool du_multiparam_get_name_list(const du_str_array* x, du_str_array* name_list);

/**
 *  Gets the list of all values.
 */
extern du_bool du_multiparam_get_value_list(const du_str_array* x, du_str_array* value_list);

/**
 *  Gets the list of names which has the specified value.
 */
extern du_bool du_multiparam_get_name_list_by_value(const du_str_array* x, const du_uchar* value, du_str_array* name_list);

/**
 *  Gets the list of values by name.
 */
extern du_bool du_multiparam_get_value_list_by_name(const du_str_array* x, const du_uchar* name, du_str_array* value_list);

/**
 *  Removes specified value with name.
 */
extern du_bool du_multiparam_remove(du_str_array* x, const du_uchar* name, const du_uchar* value);

/**
 *  Removes a name-values entry by index.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] index index of the name-value pair (element) of x.
 */
extern void du_multiparam_remove_by_index(du_str_array* x, du_uint32 index);

/**
 *  Removes a name-values entry by name.
 */
extern du_bool du_multiparam_remove_by_name(du_str_array* x, const du_uchar* name);

/**
 *  Removes specifyed value.
 */
extern du_bool du_multiparam_remove_by_value(du_str_array* x, const du_uchar* value);

/**
 *  Tests if x multiparam contains the specified name and value string pair.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] name name string to test.
 *  @param[in] value value string to test.
 *  @return true if x contains name and value string pair.
 *     false otherwise.
 */
extern du_bool du_multiparam_contains(const du_str_array* x, const du_uchar* name, const du_uchar* value);

/**
 *  Tests if x multiparam contains the specified name string.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] name name string to test.
 *  @return true if x contains name string,
 *     false otherwise.
 */
extern du_bool du_multiparam_contains_by_name(const du_str_array* x, const du_uchar* name);

/**
 *  Tests if x multiparam contains the specified value string.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] value value string to test.
 *  @return true if x contains value string,
 *     false otherwise.
 */
extern du_bool du_multiparam_contains_by_value(const du_str_array* x, const du_uchar* value);

/**
 *  Attempts to find the specified name and value.
 *  @param[in] x pointer to the du_str_array structure.
 *  @param[in] name name string to find.
 *  @param[in] value value string to find.
 *  @return
 *    position of the specified name and value if, and only if,
 *    the specified name and value finds in the array.
 *    length of x if the specified name and value does not find in x
 *    or x has failed.
 */
extern du_uint32 du_multiparam_find(const du_str_array* x, const du_uchar* name, const du_uchar* value);

#ifdef __cplusplus
}
#endif

#endif
