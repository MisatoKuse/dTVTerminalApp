/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_scheduled_task_array interface provides various methods for manipulating
 *  arrays of du_scheduled_task( such as allocation, initialization, searching, concatenation ).
 *  Before using a du_scheduled_task_array, use <b>du_scheduled_task_array_init</b> to
 *  initialize du_scheduled_task_array.
 *   The du_scheduled_task_array supports arrays that can dynamically grow as necessary.
 *  Array indexes always start at position 0.
 *  Deleting the allocated memory of du_scheduled_task_array, use
 *  <b>du_scheduled_task_array_free</b>.
 */

#ifndef DU_SCHEDULED_TASK_ARRAY_H
#define DU_SCHEDULED_TASK_ARRAY_H

#include <du_scheduled_task.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_scheduled_task values.
 */
typedef du_array du_scheduled_task_array;

/**
 *  Initializes a du_scheduled_task_array data area.
 *  @param[out] x   du_scheduled_task_array data area.
 */
#define du_scheduled_task_array_init(x) du_array_init((x), sizeof(du_scheduled_task))

/**
 *  Makes sure that enough bytes are allocated for len pointers.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure
 *  @param[in] len number of elements(du_scheduled_task data) to allocate.
 *  @return
 *    a pointer to the first du_scheduled_task data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_scheduled_task_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len du_scheduled_task data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure
 *  @param[in] len number of elements(du_scheduled_task data) to allocate.
 *  @return
 *    a pointer to the first du_scheduled_task data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_scheduled_task_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x pointer to the du_scheduled_task_array structure.
 *  @return
 *    a pointer to the first du_scheduled_task data.
 */
#define du_scheduled_task_array_get(x) ((du_scheduled_task*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_scheduled_task_array structure.
 *  @param[in] pos index of the specified du_scheduled_task data.
 *  @return
 *    a pointer to the specified du_scheduled_task data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
#define du_scheduled_task_array_get_pos(x, pos) du_array_get_pos((x), (pos))

/**
 *  Gets the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    a pointer to the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_scheduled_task* du_scheduled_task_array_get_end(const du_scheduled_task_array* x);

/**
 *  Attempts to find an object by ID.
 *  in the array x and returns the position of that data.
 *  @param[in] x pointer to the du_scheduled_task_array structure.
 *  @param[in] id the specified id value to find.
 *  @return
 *    position of the specified du_scheduled_task data if, and only if,
 *    the du_scheduled_task data which has the specified id is found in the array.
 *    length of x if the specified du_scheduled_task data is not found in x
 *    or x has failed.
 */
extern du_uint32 du_scheduled_task_array_find_id(const du_scheduled_task_array* x, const du_uint32 id);

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_scheduled_task_array structure.
 *  @return  the number of du_scheduled_task data stored in x.
 */
#define du_scheduled_task_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x pointer to the du_scheduled_task_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_scheduled_task_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_scheduled_task_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array and frees the objects.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
extern du_bool du_scheduled_task_array_truncate_length_object(du_scheduled_task_array* x, du_uint32 len);

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @return  true.
 */
#define du_scheduled_task_array_truncate(x) du_array_truncate(x)

/**
 *  Truncates the length of array to 0 and frees all objects.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @return  true if the element(s) of x is(are) truncated, otherwise false.
 */

extern du_bool du_scheduled_task_array_truncate_object(du_scheduled_task_array* x);

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 */
#define du_scheduled_task_array_free(x) du_array_free(x)


/**
 *  Frees the allocated space for the array and objects.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 */

extern void du_scheduled_task_array_free_object(du_scheduled_task_array* x);

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_scheduled_task_array structure.
 *  @return  true if x has failed.
 */
#define du_scheduled_task_array_failed(x) (du_array_bytes(x) == -1)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_scheduled_task_array structure data.
 *  @param[in] y pointer to another du_scheduled_task_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_scheduled_task_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_scheduled_task_array structure.
 *  @param[in] from pointer to the source du_scheduled_task_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_scheduled_task_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_scheduled_task_array structure.
 *  @param[in] from a pointer to the source element(s) (du_scheduled_task data).
 *  @param[in] len number of elements(du_scheduled_task data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_scheduled_task_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *   Appends an object to a destination array to,
 *   allocating more space if necessary.
 *   @param[in,out] to pointer to the destination du_scheduled_task_array structure.
 *   @param[in] scheduled_task_ptr a pointer to the source element(du_scheduled_task data).
 *   @return  true if, and only if, ch is appended successfully.
 */
#define du_scheduled_task_array_cato(to, scheduled_task_ptr) du_array_cato((to), (du_uint8*)(scheduled_task_ptr))

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @param[in] pos index of the element(du_scheduled_task data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_scheduled_task_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_scheduled_task_array_remove_end(x) du_array_remove_end((x))

/**
 *  Removes an object from array and frees it.
 *  free the du_scheduled_task structure data area.
 *  @param[in,out] x pointer to the du_scheduled_task_array structure.
 *  @param[in] pos index of the element(du_scheduled_task structure data) to remove.
 *  @return  true if, and only if, the element is removed and freed successfully.
 */
extern du_bool du_scheduled_task_array_remove_object(du_scheduled_task_array* x, du_uint32 pos);

/**
 *  Inserts all elements of from into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source element(s).
 *  @param[in] pos position of the element to insert.
 *  @return  true if, and only if, from elements is inserted successfully.
 */
#define du_scheduled_task_array_insert(to, from, pos) du_array_insert((to), (from), (pos))

/**
 *  Inserts an element of s into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_scheduled_task_array structure.
 *  @param[in] s a pointer to the source element.
 *  @param[in] pos position of the element to insert.
 *  @return  true if, and only if, the s element is inserted successfully.
 */
#define du_scheduled_task_array_inserto(to, s, pos) du_array_inserto((to), (du_uint8*)(s), (pos))

#ifdef __cplusplus
}
#endif

#endif
