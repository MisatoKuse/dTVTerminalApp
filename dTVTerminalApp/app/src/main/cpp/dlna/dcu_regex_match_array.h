/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


#ifndef DCU_REGEX_MATCH_ARRAY_H
#define DCU_REGEX_MATCH_ARRAY_H

/**
 * @file
 *    The dcu_regex_match_array interface provides various methods for manipulating arrays of
 *    dcu_regex_match ( such as allocation, initialization, searching, concatenation ).
 *    Before using a dcu_regex_match_array, use <b>dcu_regex_match_array_init</b>
 *    to initialize dcu_regex_match_array.
 *    The dcu_regex_match_array supports arrays that can dynamically grow as necessary.
 *    Array indexes always start at position 0.
 *    Deleting the allocated memory of dcu_regex_match_array, use <b>dcu_regex_match_array_free</b>
 *    or <b>dcu_regex_match_array_free_object</b>.
 */

#include "dcu_regex_match.h"
#include "du_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains dcu_regex_match values.
 */
typedef du_array dcu_regex_match_array;

/**
 *  Initializes a dcu_regex_match_array data area.
 *  @param[out] x  pointer to the dcu_regex_match_array data structure.
 */
#define dcu_regex_match_array_init(x) du_array_init((x), sizeof(dcu_regex_match))

/**
 *  Makes sure that enough bytes are allocated for len dcu_regex_match data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the dcu_regex_match_array structure
 *  @param[in] len number of elements(dcu_regex_match data) to allocate.
 *  @return
 *    a pointer to the first dcu_regex_match data.
 *    0 if x has failed, or not enough memory is available.
 */
#define dcu_regex_match_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len
 *  dcu_regex_match data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the dcu_regex_match_array structure
 *  @param[in] len number of elements(dcu_regex_match data) to allocate.
 *  @return
 *    a pointer to the first dcu_regex_match data.
 *    0 if x has failed, or not enough memory is available.
 */
#define dcu_regex_match_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Returns a pointer to the first element(dcu_regex_match data).
 *  @param[in] x pointer to the dcu_regex_match_array structure.
 *  @return
 *    a pointer to the first dcu_regex_match data.
 */
#define dcu_regex_match_array_get(x) ((dcu_regex_match*)du_array_get(x))

/**
 *  Returns a pointer to the specified element(dcu_regex_match data).
 *  @param[in] x pointer to the dcu_regex_match_array structure.
 *  @param[in] pos index of the specified dcu_regex_match data.
 *  @return
 *    a pointer to the specified dcu_regex_match data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
#define dcu_regex_match_array_get_pos(x, pos) du_array_get_pos((x), (pos))

/**
 *  Returns the number of dcu_regex_match data stored in the array.
 *  @param[in] x pointer to the dcu_regex_match_array structure.
 *  @return  the number of dcu_regex_match data stored in x.
 */
#define dcu_regex_match_array_length(x) du_array_length(x)

/**
 *  Returns the number of bytes used in the array.
 *  @param[in] x pointer to the dcu_regex_match_array structure.
 *  @return  the number of bytes used in x.
 */
#define dcu_regex_match_array_bytes(x) du_array_bytes(x)

/**
 *  Reduces the number of dcu_regex_match data stored in x to exactly len.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the dcu_regex_match_array structure.
 *  @param[in] len number of objects to set.
 *  @return  true if the number of elements(dcu_regex_match data) is truncated.
 *           false if the number of elements(dcu_regex_match data)is not changed.
 */
#define dcu_regex_match_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Reduces the number of elements in x to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the dcu_regex_match_array structure.
 *  @return  true.
 */
#define dcu_regex_match_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the region of x and switches to being unallocated.
 *  @param[in,out] x pointer to the dcu_regex_match_array structure.
 */
#define dcu_regex_match_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the dcu_regex_match_array structure.
 *  @return  true if x has failed.
 */
#define dcu_regex_match_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same contents.
 *  @param[in] x pointer to a dcu_regex_match_array structure data.
 *  @param[in] y pointer to another dcu_regex_match_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define dcu_regex_match_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all elements(dcu_regex_match data) in the source array to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination dcu_regex_match_array structure.
 *  @param[in] from pointer to the source dcu_regex_match_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define dcu_regex_match_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len elements(dcu_regex_match data) to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination dcu_regex_match_array structure.
 *  @param[in] from a pointer to the source element(s) (dcu_regex_match data).
 *  @param[in] len number of elements(dcu_regex_match data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define dcu_regex_match_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *   Appends a element(dcu_regex_match data) to a destination array,
 *   allocating more space if necessary.
 *   @param[in,out] to pointer to the destination dcu_regex_match_array structure.
 *   @param[in] object_ptr a pointer to the source element(dcu_regex_match data).
 *   @return  true if, and only if, ip is appended successfully.
 */
#define dcu_regex_match_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

/**
 *  Removes a element(dcu_regex_match data) in x.
 *  @param[in,out] x pointer to the dcu_regex_match_array structure.
 *  @param[in] pos index of the element(dcu_regex_match data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define dcu_regex_match_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Inserts all elements of from into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source element(s).
 *  @param[in] pos position of the element to insert.
 *  @return  true if, and only if, from elements is inserted successfully.
 */
#define dcu_regex_match_array_insert(to, from, pos) du_array_insert((to), (from), (pos))

/**
 *  Inserts an element(dcu_regex_match data) of s into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination dcu_regex_match_array structure.
 *  @param[in] s a pointer to the source element.
 *  @param[in] pos position of the element to insert.
 *  @return  true if, and only if, the s element is inserted successfully.
 */
#define dcu_regex_match_array_inserto(to, s, pos) du_array_inserto((to), (du_uint8*)(s), (pos))

#ifdef __cplusplus
}
#endif

#endif

