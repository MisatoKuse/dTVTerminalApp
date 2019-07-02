/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_ptr_array interface provides various methods for manipulating arrays of
 *   pointers ( such as allocation, initialization, searching, concatenation ).
 *   Before using a du_ptr_array, use <b>du_ptr_array_init</b> to initialize the array.
 *   The du_ptr_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of du_ptr_array, use <b>du_ptr_array_free</b>.
 */

#ifndef DU_PTR_ARRAY_H
#define DU_PTR_ARRAY_H

#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains pointer values.
 */
typedef du_array du_ptr_array;

/**
 *  Initializes a du_ptr_array data area.
 *  @param[out] x   du_ptr_array data area.
 */
#define du_ptr_array_init(x) du_array_init((x), sizeof(void*))

/**
 *  Makes sure that enough bytes are allocated for len pointers.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_ptr_array structure
 *  @param[in] len number of elements(pointers) to allocate.
 *  @return
 *    a pointer to the first du_ptr data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_ptr_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len pointers data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_ptr_array structure
 *  @param[in] len number of elements(pointers data) to allocate.
 *  @return
 *    a pointer to the first element(pointer data) stored in x.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_ptr_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @return
 *    a pointer to the first element(pointer data).
 */
#define du_ptr_array_get(x) ((void**)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @param[in] pos index of the specified element(pointer data).
 *  @return
 *    a pointer to the specified element(pointer data).
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern void* du_ptr_array_get_pos(const du_ptr_array* x, du_uint32 pos);

/**
 *  Gets the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern void* du_ptr_array_get_end(const du_ptr_array* x);

/**
 *  Attempts to find the specified object.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @param[in] ptr the specified pointer data to find.
 *  @return
 *    index of the specified element if, and only if, the specified element finds in the array.
 *    number of byte used in x if the specified object does not find in x
 *    or x has failed.
 */
extern du_uint32 du_ptr_array_find(du_ptr_array* x, const void* ptr);

/**
 *  Attempts to find the specified object by using a comparison function.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @param[in] object the specified object to find.
 *  @param[in] comparator a comparison function.
 *  @param[in] comp_arg a parameter of comparator function.
 *  @return
 *    index of the specified object if, and only if, the comparator returns zero.
 *    length of x if the specified object does not find in x
 *    or x has failed.
 */
extern du_uint32 du_ptr_array_find2(du_ptr_array* x, const void* object, du_array_comparator comparator, const void* comp_arg);

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @return  the number of elements stored in x.
 */
#define du_ptr_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_ptr_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_ptr_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_ptr_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_ptr_array structure.
 *  @return  true.
 */
#define du_ptr_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in] x pointer to the du_ptr_array structure.
 */
#define du_ptr_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_ptr_array structure.
 *  @return  true if x has failed.
 */
#define du_ptr_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_ptr_array structure data.
 *  @param[in] y pointer to another du_ptr_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_ptr_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_ptr_array structure.
 *  @param[in] from pointer to the source du_ptr_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_ptr_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_ptr_array structure.
 *  @param[in] from a pointer to the source element(s) (pointer data).
 *  @param[in] len number of elements(pointer data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_ptr_array_catn(to, from, len) du_array_catn((to), (const du_uint8*)(from), (len))

/**
 *   Appends an object to a destination array to,
 *   allocating more space if necessary.
 *   @param[in,out] x pointer to the destination du_ptr_array structure.
 *   @param[in] ptr the source element(pointer data).
 *   @return  true if, and only if, ch is appended successfully.
 */
extern du_bool du_ptr_array_cato(du_ptr_array* x, void* ptr);

/**
 *   Appends a zero value datum to a destination array,
 *   allocating more space if necessary.
 *   @param[in,out] x pointer to the destination du_ptr_array structure.
 *   @return  true if, and only if, a zero value datum is appended successfully.
 */
extern du_bool du_ptr_array_cat0(du_ptr_array* x);

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_ptr_array structure.
 *  @param[in] pos index of the element(pointer data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_ptr_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_ptr_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_ptr_array_remove_end(x) du_array_remove_end((x))

/**
 *  Inserts all elements of from to to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_ptr_array structure.
 *  @param[in] from a pointer to the source elements(pointer data).
 *  @param[in] pos position of the element(pointer data) to insert.
 *  @return  true if, and only if, from elements is inserted successfully.
 */
#define du_ptr_array_insert(to, from, pos) du_array_insert((to), (from), (pos))

/**
 *  Inserts len elements of from
 *  to to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_ptr_array structure.
 *  @param[in] from a pointer to the source elements(pointer data).
 *  @param[in] len number of element(pointer data) to insert.
 *  @param[in] pos position of the elements(pointer data) to insert.
 *  @return  true if, and only if, the len elements of from
 *   is inserted successfully.
 */
#define du_ptr_array_insertn(to, from, len, pos) du_array_insertn((to), (from), (len), (pos))

/**
 *  Inserts an element to to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] x pointer to the destination du_ptr_array structure.
 *  @param[in] ptr a pointer to the source elements(pointer data).
 *  @param[in] pos position of the element(pointer data) to insert.
 *  @return  true if, and only if, the element is inserted successfully.
 */
extern du_bool du_ptr_array_inserto(du_ptr_array* x, void* ptr, du_uint32 pos);

#define du_ptr_array_sort(x) du_array_sort((x), du_array_comparator_default, &((x)->member_size))

#define du_ptr_array_sortr(x) du_array_sort((x), du_array_comparator_defaultr, &((x)->member_size))

extern du_bool du_ptr_array_sort2(du_ptr_array* x, du_array_comparator comparator, const void* arg);

extern du_uint32 du_ptr_array_find_in_sort(du_ptr_array* x, const void* ptr);

extern du_uint32 du_ptr_array_find_in_sortr(du_ptr_array* x, const void* ptr);

extern du_uint32 du_ptr_array_find_in_sort2(du_ptr_array* x, const void* ptr, du_array_comparator comparator, const void* comp_arg);

extern du_bool du_ptr_array_inserto_in_sort2(du_ptr_array* x, const void* ptr, du_array_comparator comparator, const void* comp_arg);

#ifdef __cplusplus
}
#endif

#endif
