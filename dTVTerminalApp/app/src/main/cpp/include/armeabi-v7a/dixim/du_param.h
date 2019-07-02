/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_param interface provides methods for manipulating name-value pairs.
 *   (such as setting, getting and removing).
 */

#ifndef DU_PARAM_H
#define DU_PARAM_H

#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Gets the length of pairs.
 *  @param[in] param_array pointer to du_str_array structure.
 *  @return number of name-value pairs (elements) in param_array.
 */
extern du_uint32 du_param_length(const du_str_array* param_array);

/**
 *  Gets name by index.
 *  @param[in] param_array pointer to du_str_array structure.
 *  @param[in] idx index of the name-value pair (element) of param_array.
 *  @return pointer to the name data of the specified element in param_array,
 *   null if there is no index-element in param_array.
 */
extern const du_uchar* du_param_get_name(const du_str_array* param_array, du_uint32 idx);

/**
 *  Gets value by index.
 *  @param[in] param_array pointer to du_str_array structure.
 *  @param[in] idx index of the name-value pair (element) of param_array.
 *  @return pointer to the value data of the specified element in param_array,
 *   null if there is no index-element in param_array.
 */
extern const du_uchar* du_param_get_value(const du_str_array* param_array, du_uint32 idx);

/**
 *  Gets value by name.
 *  @param[in] param_array pointer to du_str_array structure.
 *  @param[in] name the name that identifies the element to be looked up.
 *  @return  pointer to the value string if the element identified by name
 *    is found, otherwise null.
 */
extern const du_uchar* du_param_get_value_by_name(const du_str_array* param_array, const du_uchar* name);

/**
 *  Sets a name-value pair.
 *  @param[in,out] param_array pointer to the destination du_str_array structure.
 *  @param[in] name the name string.
 *  @param[in] value the value string to the name.
 *  @return  true if, and only if, name-value pair is set successfully.
 *  @remark If the same name data previously contained in the param_array,
 *   the value is replaced to new value.
 */
extern du_bool du_param_set(du_str_array* param_array, const du_uchar* name, const du_uchar* value);

/**
 *  Removes the name-value pair by name.
 *  @param[in,out] param_array pointer to du_str_array structure.
 *  @param[in] name name for the element to be removed.
 *  @return  true if, and only if, name-value pair is found and removed successfully.
 */
extern du_bool du_param_remove(du_str_array* param_array, const du_uchar* name);

/**
 *  Removes the name-value pair by index.
 *  @param[in] param_array pointer to du_str_array structure.
 *  @param[in] idx index of the name-value pair (element) of param_array.
 */
extern void du_param_remove_by_index(du_str_array* param_array, du_uint32 idx);

/**
 *  Tests if x contains the specified name and value string pair.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] name name string to test.
 *  @param[in] value value string to test.
 *  @return true if x contains name and value string pair.
 *     false otherwise.
 */
extern du_bool du_param_contains(const du_str_array* x, const du_uchar* name, const du_uchar* value);

/**
 *  Tests if x contains the specified name string.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] name name string to test.
 *  @return true if x contains name string,
 *     false otherwise.
 */
extern du_bool du_param_contains_by_name(const du_str_array* x, const du_uchar* name);

/**
 *  Tests if x contains the specified value string.
 *  @param[in] x pointer to du_str_array structure.
 *  @param[in] value value string to test.
 *  @return true if x contains value string,
 *     false otherwise.
 */
extern du_bool du_param_contains_by_value(const du_str_array* x, const du_uchar* value);

#ifdef __cplusplus
}
#endif

#endif
