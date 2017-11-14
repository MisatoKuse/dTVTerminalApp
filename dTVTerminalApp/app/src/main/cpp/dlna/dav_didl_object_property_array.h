/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_object_property_array.h
 *  @brief The dav_didl_object_property_array interface provides various methods for manipulating arrays of
 *   dav_didl_object_property ( such as allocation, initialization, searching, concatenation ).
 *   Before using a dav_didl_object_property_array, use <b>dav_didl_object_property_array_init</b>
 *   to initialize dav_didl_object_property_array.
 *   The dav_didl_object_property_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of dav_didl_object_property_array, use <b>dav_didl_object_property_array_free</b>
 *   or <b>dav_didl_object_property_array_free_object</b>.
 */

#ifndef DAV_DIDL_OBJECT_PROPERTY_ARRAY_H
#define DAV_DIDL_OBJECT_PROPERTY_ARRAY_H

#include <du_type.h>
#include <du_array.h>

#include <dav_didl_object.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Resizable array which can contains dav_didl_object_property values.
 */
typedef du_array dav_didl_object_property_array;

/**
 * Initializes a dav_didl_object_property_array data area.
 * @param[out] x  pointer to the dav_didl_object_property_array data structure.
 */
#define dav_didl_object_property_array_init(x) du_array_init((x), sizeof(dav_didl_object_property))

/**
 * Makes sure that enough bytes are allocated for <em>len</em> dav_didl_object_property data.
 * If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 * allocated region if necessary.
 * @param[in,out] x pointer to the dav_didl_object_property_array structure
 * @param[in] len number of elements(dav_didl_object_property data) to allocate.
 * @return
 *   a pointer to the first dav_didl_object_property data.
 *   0 if x has failed, or not enough memory is available.
 */
#define dav_didl_object_property_array_allocate(x, len) du_array_allocate((x), (len))

/**
 * Makes sure that enough bytes are allocated for at least <em>len</em>
 * dav_didl_object_property data.
 * If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 * allocated region if necessary.
 * It often allocates somewhat more bytes than necessary, to save time later.
 * @param[in,out] x pointer to the dav_didl_object_property_array structure
 * @param[in] len number of elements(dav_didl_object_property data) to allocate.
 * @return
 *   a pointer to the first dav_didl_object_property data.
 *   0 if x has failed, or not enough memory is available.
 */
#define dav_didl_object_property_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 * Returns a pointer to the first element(dav_didl_object_property data).
 * @param[in] x pointer to the dav_didl_object_property_array structure.
 * @return
 *   a pointer to the first dav_didl_object_property data.
 */
#define dav_didl_object_property_array_get(x) ((dav_didl_object_property*)du_array_get(x))

/**
 * Returns a pointer to the specified element(dav_didl_object_property data).
 * @param[in] x pointer to the dav_didl_object_property_array structure.
 * @param[in] pos index of the specified dav_didl_object_property data.
 * @return
 *   a pointer to the specified dav_didl_object_property data.
 *   0 if x has failed, or if fewer than pos + 1 elements are used.
 */
#define dav_didl_object_property_array_get_pos(x, pos) du_array_get_pos((x), (pos))

/**
 * Returns the number of dav_didl_object_property data stored in the array.
 * @param[in] x pointer to the dav_didl_object_property_array structure.
 * @return  the number of dav_didl_object_property data stored in <em>x</em>.
 */
#define dav_didl_object_property_array_length(x) du_array_length(x)

/**
 * Returns the number of bytes used in the array.
 * @param[in] x pointer to the dav_didl_object_property_array structure.
 * @return  the number of bytes used in <em>x</em>.
 */
#define dav_didl_object_property_array_bytes(x) du_array_bytes(x)

/**
 * Reduces the number of dav_didl_object_property data stored in <em>x</em> to exactly len.
 * It does not change the allocation in <em>x</em>.
 * @param[in,out] x pointer to the dav_didl_object_property_array structure.
 * @param[in] len number of objects to set.
 * @return  true if the number of elements(dav_didl_object_property data) is truncated.
 *          false if the number of elements(dav_didl_object_property data)is not changed.
 */
#define dav_didl_object_property_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 * Reduces the number of elements in <em>x</em> to 0.
 * It does not change the allocation in <em>x</em>.
 * @param[in,out] x pointer to the dav_didl_object_property_array structure.
 * @return  true.
 */
#define dav_didl_object_property_array_truncate(x) du_array_truncate(x)

/**
 * Frees the region of <em>x</em> and switches to being unallocated.
 * @param[in,out] x pointer to the dav_didl_object_property_array structure.
 */
#define dav_didl_object_property_array_free(x) du_array_free(x)

/**
 * Tests <em>x</em> has failed.
 * @param[in] x pointer to the dav_didl_object_property_array structure.
 * @return  true if <em>x</em> has failed.
 */
#define dav_didl_object_property_array_failed(x) du_array_failed(x)

/**
 * Tests <em>x</em> and <em>y</em> have the same contents.
 * @param[in] x pointer to a dav_didl_object_property_array structure data.
 * @param[in] y pointer to another dav_didl_object_property_array structure data.
 * @return  true if, and only if, <em>x</em> and <em>y</em> have the same contents.
 */
#define dav_didl_object_property_array_equal(x, y) du_array_equal((x), (y))

/**
 * Appends all elements(dav_didl_object_property data) in the source array to a destination array,
 * allocating more space if necessary.
 * @param[in,out] to pointer to the destination dav_didl_object_property_array structure.
 * @param[in] from pointer to the source dav_didl_object_property_array structure.
 * @return  true if, and only if, elements in the source array are appended successfully.
 *         If source array has failed , sets destination array status to failed and returns false.
 */
#define dav_didl_object_property_array_cat(to, from) du_array_cat((to), (from))

/**
 * Appends len elements(dav_didl_object_property data) to a destination array,
 * allocating more space if necessary.
 * @param[in,out] to pointer to the destination dav_didl_object_property_array structure.
 * @param[in] from a pointer to the source element(s) (dav_didl_object_property data).
 * @param[in] len number of elements(dav_didl_object_property data) to append.
 * @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define dav_didl_object_property_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *  Appends a element(dav_didl_object_property data) to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination dav_didl_object_property_array structure.
 *  @param[in] dav_didl_object_property_ptr a pointer to the source element(dav_didl_object_property data).
 *  @return  true if, and only if, <em>ip</em> is appended successfully.
 */
#define dav_didl_object_property_array_cato(to, dav_didl_object_property_ptr) du_array_cato((to), (du_uint8*)(dav_didl_object_property_ptr))

/**
 * Removes a element(dav_didl_object_property data) in <em>x</em>.
 * @param[in,out] x pointer to the dav_didl_object_property_array structure.
 * @param[in] pos index of the element(dav_didl_object_property data) to remove.
 * @return  true if, and only if, the element is removed successfully.
 */
#define dav_didl_object_property_array_remove(x, pos) du_array_remove((x), (pos))

#ifdef __cplusplus
}
#endif

#endif
