/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_svcdsc_state_variable_array.h
 *  @brief The dupnp_svcdsc_state_variable_array interface provides various methods for
 *   manipulating arrays of dupnp_svcdsc_state_variable structures ( such as allocation,
 *   initialization, searching, concatenation ).
 *   Before using a dupnp_svcdsc_state_variable_array,
 *   use <b>dupnp_svcdsc_state_variable_array_init</b> to initialize
 *   dupnp_svcdsc_state_variable_array.
 *   The dupnp_svcdsc_state_variable_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of dupnp_svcdsc_state_variable_array,
 *   use <b>dupnp_svcdsc_state_variable_array_free</b>.
 *  @see dupnp_dvcdsc.h, dupnp_dvc.h
 */

#ifndef DUPNP_SVCDSC_STATE_VARIABLE_ARRAY_H
#define DUPNP_SVCDSC_STATE_VARIABLE_ARRAY_H

#include <dupnp_svcdsc_state_variable.h>
#include <du_type.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Resizable array which can contains dupnp_svcdsc_state_variable values.
 */
typedef du_array dupnp_svcdsc_state_variable_array;

/**
 * Initializes a dupnp_svcdsc_state_variable_array data area.
 * @param[out] x   pointer to the dupnp_svcdsc_state_variable_array data area.
 */
#define dupnp_svcdsc_state_variable_array_init(x) du_array_init((x), sizeof(dupnp_svcdsc_state_variable))

/**
 * Makes sure that enough bytes are allocated for <em>len</em> dupnp_svcdsc_state_variable
 * structures.
 * If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 * allocated region if necessary.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure
 * @param[in] len number of elements(dupnp_svcdsc_state_variable structures) to allocate.
 * @return
 *   a pointer to the first dupnp_svcdsc_state_variable structure.
 *   0 if x has failed, or not enough memory is available.
 */
#define dupnp_svcdsc_state_variable_array_allocate(x, len) du_array_allocate((x), (len))

/**
 * Makes sure that enough bytes are allocated for at least <em>len</em>dupnp_svcdsc_state_variable
 * structures.
 * If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 * allocated region if necessary.
 * It often allocates somewhat more bytes than necessary, to save time later.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure
 * @param[in] len number of elements(dupnp_svcdsc_state_variable structures) to allocate.
 * @return
 *   a pointer to the first dupnp_svcdsc_state_variable structure.
 *   0 if x has failed, or not enough memory is available.
 */
#define dupnp_svcdsc_state_variable_array_allocate_align(x, len) du_array_allocate_align((x), (len))
/**
 * Gets a pointer to the first object(dupnp_svcdsc_state_variable structure data) stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @return
 *   a pointer to the first dupnp_svcdsc_state_variable structure.
 */
#define dupnp_svcdsc_state_variable_array_get(x) ((dupnp_svcdsc_state_variable*)du_array_get(x))

/**
 * Gets a pointer to the specified element(dupnp_svcdsc_state_variable structure data) stored
 * in <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @param[in] pos index of the specified dupnp_svcdsc_state_variable structure data.
 * @return
 *   a pointer to the specified dupnp_svcdsc_state_variable structure data.
 *   0 if x has failed, or if fewer than pos + 1 elements are used.
 */
#define dupnp_svcdsc_state_variable_array_get_pos(x, pos) du_array_get_pos((x), (pos))

/**
 * Attempts to find the dupnp_svcdsc_state_variable data which has the specified <em>name</em>
 * in the array <em>x</em> and returns the position of that data.
 * @param[in] x pointer to the dav_didl_object_array structure.
 * @param[in] name the state variable name to find.
 * @return
 *   position of the specified dupnp_svcdsc_state_variable data if, and only if,
 *   the dupnp_svcdsc_state_variable data which has the specified <em>name</em> is found in the array.
 *   length of <em>x</em> if the specified dupnp_svcdsc_state_variable data is not found in <em>x</em>
 *   or <em>x</em> has failed.
 */
extern du_uint32 dupnp_svcdsc_state_variable_array_find_by_name(const dupnp_svcdsc_state_variable_array* x, const du_uchar* name);

/**
 * Gets the number of dupnp_svcdsc_state_variable structures stored in the array <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @return  the number of dupnp_svcdsc_state_variable structures stored in <em>x</em>.
 */
#define dupnp_svcdsc_state_variable_array_length(x) du_array_length(x)

/**
 * Gets the number of bytes used in the array <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @return  the number of bytes used in <em>x</em>.
 */
#define dupnp_svcdsc_state_variable_array_bytes(x) du_array_bytes(x)

/**
 * Reduces the number of elements(dupnp_svcdsc_state_variable structure data) in <em>x</em>
 * to exactly <em>len</em>.
 * It does not change the allocation in <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @param[in] len number of elements to set.
 * @return  true if the number of elements is truncated.
 *          false if the number of elements is not changed.
 */
#define dupnp_svcdsc_state_variable_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 * Reduces the number of elements(dupnp_svcdsc_state_variable structure data) in <em>x</em>
 * to exactly <em>len</em> and free the truncated dupnp_svcdsc_state_variable data area.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @param[in] len number of elements to set.
 * @return  true if the number of elements is truncated.
 *          false if the number of elements is not changed.
 */
extern du_bool dupnp_svcdsc_state_variable_array_truncate_length_object(dupnp_svcdsc_state_variable_array* x, du_uint32 len);

/**
 * Reduces the number of elements in <em>x</em> to 0.
 * It does not change the allocation in <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @return  true.
 */
#define dupnp_svcdsc_state_variable_array_truncate(x) du_array_truncate(x)

/**
 * Reduces the number of elements in <em>x</em> to 0, and
 * free the truncated dupnp_svcdsc_state_variable data area.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @return  true if the element(s) of <em>x</em> is(are) truncated, otherwise false.
 */
extern du_bool dupnp_svcdsc_state_variable_array_truncate_object(dupnp_svcdsc_state_variable_array* x);

/**
 * Frees the region used by <em>x</em> and switches to being unallocated.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 */
#define dupnp_svcdsc_state_variable_array_free(x) du_array_free(x)

/**
 * Frees the region used by <em>x</em> and switches to being unallocated, and
 * free the all dupnp_svcdsc_state_variable element(s) stored in <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 */
extern void dupnp_svcdsc_state_variable_array_free_object(dupnp_svcdsc_state_variable_array* x);

/**
 * Tests <em>x</em> has failed.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @return  true if <em>x</em> has failed.
 */
#define dupnp_svcdsc_state_variable_array_failed(x) du_array_failed(x)

/**
 * Tests <em>x</em> and <em>y</em> have the same contents.
 * @param[in] x pointer to a dupnp_svcdsc_state_variable_array structure data.
 * @param[in] y pointer to another dupnp_svcdsc_state_variable_array structure data.
 * @return  true if, and only if, <em>x</em> and <em>y</em> have the same contents.
 */
#define dupnp_svcdsc_state_variable_array_equal(x, y) du_array_equal((x), (y))

/**
 * Appends all elements(dupnp_svcdsc_state_variable structures) in the source array <em>from</em>
 *  to a destination array <em>to</em>,
 * allocating more space if necessary.
 * @param[in,out] to pointer to the destination dupnp_svcdsc_state_variable_array structure.
 * @param[in] from pointer to the source dupnp_svcdsc_state_variable_array structure.
 * @return  true if, and only if, elements in the source array are appended successfully.
 *         If source array has failed , sets destination array status to failed and returns false.
 */
#define dupnp_svcdsc_state_variable_array_cat(to, from) du_array_cat((to), (from))

/**
 * Appends <em>len</em> elements(dupnp_svcdsc_state_variable structures) of <em>from</em>
 * to a destination array <em>to</em>,
 * allocating more space if necessary.
 * @param[in,out] to pointer to the destination dupnp_svcdsc_state_variable_array structure.
 * @param[in] from a pointer to the source element(s) ( dupnp_svcdsc_state_variable structure(s)).
 * @param[in] len number of elements(dupnp_svcdsc_state_variable structures) to append.
 * @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define dupnp_svcdsc_state_variable_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *  Appends a element(dupnp_svcdsc_state_variable structure) <em>object_ptr</em> to a destination
 *   array <em>to</em>, allocating more space if necessary.
 *  @param[in,out] to pointer to the destination dupnp_svcdsc_state_variable_array structure.
 *  @param[in] object_ptr a pointer to the source element(dupnp_svcdsc_state_variable structure data).
 *  @return  true if, and only if, <em>object_ptr</em> is appended successfully.
 */
#define dupnp_svcdsc_state_variable_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

/**
 * Removes a element(dupnp_svcdsc_state_variable structure) from <em>x</em>.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @param[in] pos index of the element(dupnp_svcdsc_state_variable structure data) to remove.
 * @return  true if, and only if, the element is removed successfully.
 */
#define dupnp_svcdsc_state_variable_array_remove(x, pos) du_array_remove((x), (pos))

/**
 * Removes a element(dupnp_svcdsc_state_variable structure) from <em>x</em> and
 * free the dupnp_svcdsc_state_variable structure data area.
 * @param[in] x pointer to the dupnp_svcdsc_state_variable_array structure.
 * @param[in] pos index of the element(dupnp_svcdsc_state_variable structure data) to remove.
 * @return  true if, and only if, the element is removed and freed successfully.
 */
extern du_bool dupnp_svcdsc_state_variable_array_remove_object(dupnp_svcdsc_state_variable_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
