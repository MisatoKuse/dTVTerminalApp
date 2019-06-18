/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvcdsc_info_array.h
 *  @brief The dupnp_dvcdsc_info_array interface provides various methods for
 *   manipulating arrays of dupnp_dvcdsc_info structures ( such as allocation,
 *   initialization, searching, concatenation ).
 *   Before using a dupnp_dvcdsc_info_array, use <b>dupnp_dvcdsc_info_array_init</b> to
 *   initialize dupnp_dvcdsc_info_array.
 *   The dupnp_dvcdsc_info_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of dupnp_dvcdsc_info_array,
 *   use <b>dupnp_dvcdsc_info_array_free</b>.
 *  @see dupnp_dvc.h
 */

#ifndef DUPNP_DVCDSC_INFO_ARRAY_H
#define DUPNP_DVCDSC_INFO_ARRAY_H

#include <dupnp_dvcdsc_info.h>
#include <du_type.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Resizable array which can contains dupnp_dvcdsc_info values.
 * @see dupnp_dvc.h
 */
typedef du_array dupnp_dvcdsc_info_array;

/**
 * Initializes an array.
 * @param[out] x   pointer to the array.
 */
#define dupnp_dvcdsc_info_array_init(x) du_array_init((x), sizeof(dupnp_dvcdsc_info))

/**
 * Makes sure that enough bytes are allocated for <em>len</em> elements.
 * If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 * allocated region if necessary.
 * @param[in] x pointer to the array.
 * @param[in] len number of elements to allocate.
 * @return
 *   a pointer to the first element.
 *   0 if x has failed, or not enough memory is available.
 */
#define dupnp_dvcdsc_info_array_allocate(x, len) du_array_allocate((x), (len))

/**
 * Makes sure that enough bytes are allocated for at least <em>len</em> elements.
 * If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 * allocated region if necessary.
 * It often allocates somewhat more bytes than necessary, to save time later.
 * @param[in] x pointer to the array.
 * @param[in] len number of elements to allocate.
 * @return
 *   a pointer to the first element.
 *   0 if x has failed, or not enough memory is available.
 */
#define dupnp_dvcdsc_info_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 * Gets a pointer to the first element stored in <em>x</em>.
 * @param[in] x pointer to the array.
 * @return a pointer to the first element.
 */
#define dupnp_dvcdsc_info_array_get(x) ((dupnp_dvcdsc_info*)du_array_get(x))

/**
 * Gets a pointer to the element stored in <em>x</em> by position.
 * @param[in] x pointer to the array.
 * @param[in] pos the position of element.
 * @return
 *   a pointer to the element.
 *   0 if x has failed, or if fewer than pos + 1 elements are used.
 */
#define dupnp_dvcdsc_info_array_get_pos(x, pos) du_array_get_pos((x), (pos))

/**
 * Gets the number of elements stored in the array <em>x</em>.
 * @param[in] x pointer to the array.
 * @return  the number of elements.
 */
#define dupnp_dvcdsc_info_array_length(x) du_array_length(x)

/**
 * Gets the number of elements stored in the array <em>x</em>.
 * @param[in] x pointer to the array.
 * @return  the number of elements.
 */
#define dupnp_dvcdsc_info_array_bytes(x) du_array_bytes(x)

/**
 * Reduces the length of array.
 * It does not change the allocation in <em>x</em>.
 * @param[in] x pointer to the array.
 * @param[in] len length
 * @return  true if the number of elements is truncated.
 *          false if the number of elements is not changed.
 */
#define dupnp_dvcdsc_info_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 * Reduces the length of array and frees the truncated element object.
 * @param[in] x pointer to the array.
 * @param[in] len length
 * @return  true if the number of elements is truncated.
 *          false if the number of elements is not changed.
 */
extern du_bool dupnp_dvcdsc_info_array_truncate_length_object(dupnp_dvcdsc_info_array* x, du_uint32 len);

/**
 * Reduces the length of array to 0.
 * It does not change the allocation in <em>x</em>.
 * @param[in] x pointer to the array.
 * @return  true.
 */
#define dupnp_dvcdsc_info_array_truncate(x) du_array_truncate(x)

/**
 * Reduces the length of array to 0 and frees the truncated element object.
 * It does not change the allocation in <em>x</em>.
 * @param[in] x pointer to the array.
 * @return  true.
 */
extern du_bool dupnp_dvcdsc_info_array_truncate_object(dupnp_dvcdsc_info_array* x);

/**
 * Frees the region used by <em>x</em>l
 * @param[in] x pointer to the array.
 */
#define dupnp_dvcdsc_info_array_free(x) du_array_free(x)

/**
 * Frees the region used by <em>x</em> and frees the all element objects.
 * @param[in] x pointer to the array.
 */
extern void dupnp_dvcdsc_info_array_free_object(dupnp_dvcdsc_info_array* x);

/**
 * Tests <em>x</em> has failed.
 * @param[in] x pointer to the array.
 * @return  true if <em>x</em> has failed.
 */
#define dupnp_dvcdsc_info_array_failed(x) du_array_failed(x)

/**
 * Tests <em>x</em> and <em>y</em> have the same value.
 * @param[in] x pointer to the array.
 * @param[in] y pointer to the array to compare.
 * @return  true if, and only if, <em>x</em> and <em>y</em> have the same value.
 */
#define dupnp_dvcdsc_info_array_equal(x, y) du_array_equal((x), (y))

/**
 * Appends all elements in the source array to the destination array allocating
 * more space if necessary.
 * @param[in,out] to pointer to the destination array.
 * @param[in] from pointer to the source array.
 * @return  true if, and only if, elements in the source array are appended successfully.
 *         If source array has failed , sets destination array status to failed and returns false.
 */
#define dupnp_dvcdsc_info_array_cat(to, from) du_array_cat((to), (from))

/**
 * Appends <em>len</em> elements in the source array to the destination array
 * allocating more space if necessary.
 * @param[in,out] to pointer to the destination array.
 * @param[in] from a pointer to the source array.
 * @param[in] len number of elements to append.
 * @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define dupnp_dvcdsc_info_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *  Appends an element to the destination array, allocating more space if necessary.
 *  @param[in,out] to pointer to the destination array.
 *  @param[in] object_ptr a pointer to the element.
 *  @return  true if, and only if, <em>object_ptr</em> is appended successfully.
 */
#define dupnp_dvcdsc_info_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

/**
 * Removes an element from <em>x</em>.
 * @param[in] x pointer to the array.
 * @param[in] pos position of the element to remove.
 * @return  true if, and only if, the element is removed successfully.
 */
#define dupnp_dvcdsc_info_array_remove(x, pos) du_array_remove((x), (pos))

/**
 * Removes an element from <em>x</em> and frees the element object.
 * @param[in] x pointer to the array.
 * @param[in] pos position of the element to remove.
 * @return  true if, and only if, the element is removed and freed successfully.
 */
extern du_bool dupnp_dvcdsc_info_array_remove_object(dupnp_dvcdsc_info_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif

