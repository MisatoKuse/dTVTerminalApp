/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_bool_array interface provides various methods for manipulating arrays of
 *   du_bool  ( such as allocation, initialization, searching, concatenation ).
 *   Before using a du_bool_array, use <b>du_bool_array_init</b> to initialize the du_bool_array.
 *   The du_bool_array supports arrays that can dynamically grow as necessary when you use <b>du_bool_array_cat</b>.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of du_bool_array, use <b>du_bool_array_free</b>.
 */

#ifndef DU_BOOL_ARRAY_H
#define DU_BOOL_ARRAY_H

#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_bool values.
 */
typedef du_array du_bool_array;

/**
 *  Initializes a du_bool_array data area.
 *  @param[out] x   du_bool_array data area.
 */
#define du_bool_array_init(x) du_array_init((x), sizeof(du_bool))

/**
 *  Makes sure that enough bytes are allocated for len pointers.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_bool_array structure
 *  @param[in] len number of elements(du_bool data) to allocate.
 *  @return
 *    a pointer to the first du_bool data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_bool_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len du_bool data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_bool_array structure
 *  @param[in] len number of elements(du_bool data) to allocate.
 *  @return
 *    a pointer to the first du_bool data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_bool_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Returns a pointer to the first element(du_bool data).
 *  @param[in] x pointer to the du_bool_array structure.
 *  @return
 *    a pointer to the first du_bool data.
 */
#define du_bool_array_get(x) ((du_bool*)du_array_get(x))

/**
 *  Returns a index to the specified element(du_bool data).
 *  @param[in] x pointer to the du_bool_array structure.
 *  @param[in] pos index of the specified du_bool data.
 *  @return
 *    a pointer to the specified du_bool data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern du_bool du_bool_array_get_pos(const du_bool_array* x, du_uint32 pos);

/**
 *  Returns a last element(du_bool data) of x.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_bool du_bool_array_get_end(const du_bool_array* x);

/**
 *  Attempts to find the specified du_bool data in the array x
 *  and returns the position of that data.
 *  @param[in] x pointer to the du_bool_array structure.
 *  @param[in] s the specified du_bool data to find.
 *  @return
 *    position of the specified du_bool data if, and only if,
 *    the specified du_bool data finds in the array.
 *    length of x if the specified du_bool data does not find in x
 *    or x has failed.
 */
#define du_bool_array_find(x, s) du_array_find((x), &(s));

/**
 *  Returns the number of du_bool data stored in the array.
 *  @param[in] x pointer to the du_bool_array structure.
 *  @return  the number of du_bool data stored in x.
 */
#define du_bool_array_length(x) du_array_length(x)

/**
 *  Returns the number of bytes used in the array.
 *  @param[in] x pointer to the du_bool_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_bool_array_bytes(x) du_array_bytes(x)

/**
 *  Reduces the number of elements(du_bool data) to exactly len.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_bool_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_bool_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Reduces the number of elements in x to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_bool_array structure.
 *  @return  true.
 */
#define du_bool_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the region of x and switches to being unallocated.
 *  @param[in,out] x pointer to the du_bool_array structure.
 */
#define du_bool_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_bool_array structure.
 *  @return  true if x has failed.
 */
#define du_bool_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same contents.
 *  @param[in] x pointer to a du_bool_array structure data.
 *  @param[in] y pointer to another du_bool_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_bool_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all elements(du_bool data) in the source array to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_bool_array structure.
 *  @param[in] from pointer to the source du_bool_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_bool_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len elements(du_bool data) to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_bool_array structure.
 *  @param[in] from a pointer to the source element(s) (du_bool data).
 *  @param[in] len number of elements(du_bool data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_bool_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *   Appends a element(du_bool data) to a destination array,
 *   allocating more space if necessary.
 *   @param[in,out] to pointer to the destination du_bool_array structure.
 *   @param[in] s the source element(du_bool data).
 *   @return  true if, and only if, ch is appended successfully.
 */
#define du_bool_array_cato(to, s) du_array_cato((to), (du_uint8*)&(s))

/**
 *  Removes a element(du_bool data) in x.
 *  @param[in,out] x pointer to the du_bool_array structure.
 *  @param[in] pos index of the element(du_bool data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_bool_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes a last element(du_bool data) of x.
 *  @param[in,out] x pointer to the du_bool_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_bool_array_remove_end(x) du_array_remove_end((x))

#ifdef __cplusplus
}
#endif

#endif
