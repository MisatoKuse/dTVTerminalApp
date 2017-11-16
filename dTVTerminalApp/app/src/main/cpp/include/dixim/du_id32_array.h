/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_id32_array interface provides various methods for manipulating arrays of
 *    du_id32  ( such as allocation, initialization, searching, concatenation ).
 *    Before using a du_id32_array, use <b>du_id32_array_init</b> to initialize du_id32_array.
 *    The du_id32_array supports arrays that can dynamically grow as necessary.
 *    Array indexes always start at position 0.
 *    Deleting the allocated memory of du_id32_array, use <b>du_id32_array_free</b>.
 */

#ifndef DU_ID32_ARRAY_H
#define DU_ID32_ARRAY_H

#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_id32 values.
 */
typedef du_array du_id32_array;

/**
 *  Initializes a du_id32_array data area.
 *  @param[out] x   du_id32_array data area.
 */
#define du_id32_array_init(x) du_array_init((x), sizeof(du_id32))

/**
 *  Makes sure that enough bytes are allocated for len objects.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_id32_array structure
 *  @param[in] len number of elements(du_id32 data) to allocate.
 *  @return
 *    a pointer to the first du_id32 data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_id32_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len objects.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_id32_array structure
 *  @param[in] len number of elements(du_id32 data) to allocate.
 *  @return
 *    a pointer to the first du_id32 data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_id32_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets a pointer to the first object.
 *  @param[in] x pointer to the du_id32_array structure.
 *  @return
 *    a pointer to the first du_id32 data.
 */
#define du_id32_array_get(x) ((du_id32*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_id32_array structure.
 *  @param[in] pos index of the specified du_id32 data.
 *  @return
 *    a pointer to the specified du_id32 data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern du_uint32 du_id32_array_get_pos(const du_id32_array* x, du_uint32 pos);

/**
 *  Returns the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_uint32 du_id32_array_get_end(const du_id32_array* x);

/**
 *  Attempts to find the specified object.
 *  @param[in] x pointer to the du_id32_array structure.
 *  @param[in] s the specified du_id32 data to find.
 *  @return
 *    position of the specified du_id32 data if, and only if,
 *    the specified du_id32 data finds in the array.
 *    length of x if the specified du_id32 data does not find in x
 *    or x has failed.
 */
#define du_id32_array_find(x, s) du_array_find((x), &(s));

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_id32_array structure.
 *  @return  the number of du_id32 data stored in x.
 */
#define du_id32_array_length(x) du_array_length(x)

/**
 *  Gets the length of array in bytes.
 *  @param[in] x pointer to the du_id32_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_id32_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_id32_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_id32_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_id32_array structure.
 *  @return  true.
 */
#define du_id32_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] x pointer to the du_id32_array structure.
 */
#define du_id32_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_id32_array structure.
 *  @return  true if x has failed.
 */
#define du_id32_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_id32_array structure data.
 *  @param[in] y pointer to another du_id32_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_id32_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all objects in the source array to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_id32_array structure.
 *  @param[in] from pointer to the source du_id32_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_id32_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_id32_array structure.
 *  @param[in] from a pointer to the source element(s) (du_id32 data).
 *  @param[in] len number of elements(du_id32 data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_id32_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *   Appends an object to a destination array,
 *   allocating more space if necessary.
 *   @param[in,out] to pointer to the destination du_id32_array structure.
 *   @param[in] s the source element(du_id32 data).
 *   @return  true if, and only if, ch is appended successfully.
 */
#define du_id32_array_cato(to, s) du_array_cato((to), (du_uint8*)&(s))

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_id32_array structure.
 *  @param[in] pos index of the element(du_id32 data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_id32_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_id32_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_id32_array_remove_end(x) du_array_remove_end((x))

#ifdef __cplusplus
}
#endif

#endif
