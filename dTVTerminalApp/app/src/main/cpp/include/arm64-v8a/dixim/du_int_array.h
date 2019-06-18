/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_int_array interface provides various methods for manipulating arrays of
 *    du_int  ( such as allocation, initialization, searching, concatenation ).
 *    Before using a du_int_array, use <b>du_int_array_init</b> to initialize the du_int_array.
 *    The du_int_array supports arrays that can dynamically grow as necessary.
 *    Array indexes always start at position 0.
 *    Deleting the allocated memory of du_int_array, use <b>du_int_array_free</b>.
 */

#ifndef DU_INT_ARRAY_H
#define DU_INT_ARRAY_H

#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_int values.
 */
typedef du_array du_int_array;

/**
 *  Initializes a du_int_array data area.
 *  @param[out] x   du_int_array data area.
 */
#define du_int_array_init(x) du_array_init((x), sizeof(du_int))

/**
 *  Makes sure that enough bytes are allocated for len pointers.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_int_array structure
 *  @param[in] len number of elements(du_int data) to allocate.
 *  @return
 *    a pointer to the first du_int data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_int_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len du_int data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_int_array structure
 *  @param[in] len number of elements(du_int data) to allocate.
 *  @return
 *    a pointer to the first du_int data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_int_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x pointer to the du_int_array structure.
 *  @return
 *    a pointer to the first du_int data.
 */
#define du_int_array_get(x) ((du_int*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_int_array structure.
 *  @param[in] pos index of the specified du_int data.
 *  @return
 *    a pointer to the specified du_int data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern du_int du_int_array_get_pos(const du_int_array* x, du_int pos);

/**
 *  Gets the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_int du_int_array_get_end(const du_int_array* x);

/**
 *  Attempts to find the specified object.
 *  and returns the position of that data.
 *  @param[in] x pointer to the du_int_array structure.
 *  @param[in] s the specified du_int data to find.
 *  @return
 *    position of the specified du_int data if, and only if,
 *    the specified du_int data finds in the array.
 *    length of x if the specified du_int data does not find in x
 *    or x has failed.
 */
#define du_int_array_find(x, s) du_array_find((x), &(s));

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_int_array structure.
 *  @return  the number of du_int data stored in x.
 */
#define du_int_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x pointer to the du_int_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_int_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_int_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_int_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_int_array structure.
 *  @return  true.
 */
#define du_int_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] x pointer to the du_int_array structure.
 */
#define du_int_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_int_array structure.
 *  @return  true if x has failed.
 */
#define du_int_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_int_array structure data.
 *  @param[in] y pointer to another du_int_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_int_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all objects in the source array from
 *   to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_int_array structure.
 *  @param[in] from pointer to the source du_int_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_int_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_int_array structure.
 *  @param[in] from a pointer to the source element(s) (du_int data).
 *  @param[in] len number of elements(du_int data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_int_array_catn(to, from, len) du_array_catn((to), (const du_uint8*)(from), (len))

/**
 *   Appends an object to a destination array to,
 *   allocating more space if necessary.
 *   @param[in,out] to pointer to the destination du_int_array structure.
 *   @param[in] s the source element(du_int data).
 *   @return  true if, and only if, ch is appended successfully.
 */
#define du_int_array_cato(to, s) du_array_cato((to), (const du_uint8*)&(s))

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_int_array structure.
 *  @param[in] pos index of the element(du_int data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_int_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_int_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_int_array_remove_end(x) du_array_remove_end((x))

#ifdef __cplusplus
}
#endif

#endif
