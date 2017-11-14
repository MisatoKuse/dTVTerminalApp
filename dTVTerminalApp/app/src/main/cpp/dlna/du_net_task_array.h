/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_net_task_array interface provides various methods for manipulating arrays of
 *   du_net_task  ( such as allocation, initialization, searching, concatenation ).
 *   Before using a du_net_task_array, use <b>du_net_task_array_init</b> to initialize
 *   du_net_task_array.
 *   The du_net_task_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of du_net_task_array, use <b>du_net_task_array_free</b>.
 */

#ifndef DU_NET_TASK_ARRAY_H
#define DU_NET_TASK_ARRAY_H

#include "du_socket_os.h"
#include "du_array.h"
#include "du_net_task.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_net_task values.
 */
typedef du_array du_net_task_array;

/**
 *  Initializes a du_net_task_array data area.
 *  @param[out] x   du_net_task_array data area.
 */
#define du_net_task_array_init(x) du_array_init((x), sizeof(du_net_task))

/**
 *  Makes sure that enough bytes are allocated for len pointers.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_net_task_array structure
 *  @param[in] len number of elements(du_net_task data) to allocate.
 *  @return
 *    a pointer to the first du_net_task data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_net_task_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len du_net_task data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_net_task_array structure
 *  @param[in] len number of elements(du_net_task data) to allocate.
 *  @return
 *    a pointer to the first du_net_task data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_net_task_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @return
 *    a pointer to the first du_net_task data.
 */
#define du_net_task_array_get(x) ((du_net_task*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @param[in] pos index of the specified du_net_task data.
 *  @return
 *    a pointer to the specified du_net_task data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
#define du_net_task_array_get_pos(x, pos) du_array_get_pos((x), (pos))

/**
 *  Gets the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    a pointer to the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_net_task* du_net_task_array_get_end(const du_net_task_array* x);

/**
 *  Attempts to find the specified object.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @param[in] s the specified du_socket data to find.
 *  @return
 *    position of the specified du_net_task data if, and only if,
 *    the specified du_socket data finds in the array.
 *    length of x if the specified du_socket data does not find in x
 *    or x has failed.
 */
extern du_uint32 du_net_task_array_find(const du_net_task_array* x, du_socket s);

/**
 *  Attempts to find the specified object by task ID.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @param[in] task_id the specified du_uint32 data to find.
 *  @return
 *    position of the specified du_net_task data if, and only if,
 *    the specified task id finds in the array.
 *    length of x if the specified task id does not find in x
 *    or x has failed.
 */
extern du_uint32 du_net_task_array_find_by_task_id(const du_net_task_array* x, du_id32 task_id);

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @return  the number of du_net_task data stored in x.
 */
#define du_net_task_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_net_task_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_net_task_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_net_task_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_net_task_array structure.
 *  @return  true.
 */
#define du_net_task_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] x pointer to the du_net_task_array structure.
 */
#define du_net_task_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_net_task_array structure.
 *  @return  true if x has failed.
 */
#define du_net_task_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_net_task_array structure data.
 *  @param[in] y pointer to another du_net_task_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_net_task_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_net_task_array structure.
 *  @param[in] from pointer to the source du_net_task_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_net_task_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_net_task_array structure.
 *  @param[in] from a pointer to the source element(s) (du_net_task data).
 *  @param[in] len number of elements(du_net_task data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_net_task_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *   Appends an object to a destination array to,
 *   allocating more space if necessary.
 *   @param[in,out] to pointer to the destination du_net_task_array structure.
 *   @param[in] net_task_ptr a pointer to the source element(du_net_task data).
 *   @return  true if, and only if, ch is appended successfully.
 */
#define du_net_task_array_cato(to, net_task_ptr) du_array_cato((to), (du_uint8*)(net_task_ptr))

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_net_task_array structure.
 *  @param[in] pos index of the element(du_net_task data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_net_task_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_net_task_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_net_task_array_remove_end(x) du_array_remove_end((x))

#ifdef __cplusplus
}
#endif

#endif
