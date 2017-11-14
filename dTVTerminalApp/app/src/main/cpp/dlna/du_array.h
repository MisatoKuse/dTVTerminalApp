/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_array interface provides various methods for manipulating array
 *  (such as allocation, initialization, searching, concatenation).

 *  Before using a du_array, use du_array_init() to establish component
 *  object size stored in the array (du_array support any type of data),
 *  and use du_array_allocate() or du_array_allocate_align() to allocate memory
 *  for it.
 *  The du_array supports arrays that can dynamically grow as necessary
 *  when you use du_array_cat().
 *  Array indexes always start at position 0.
 *  Freeing the allocated memory of du_array, use du_array_free().
 */

#ifndef DU_ARRAY_H
#define DU_ARRAY_H

#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  du_array structure contains data making up resizable array.
 */
typedef struct {
    du_uint8* p;
    du_uint32 member_size;
    du_uint32 allocated;
    du_uint32 used;
    du_bool failed;
} du_array;

/**
 *  The interface definition of a comparison function.
 *
 *  The comparison function is used to test whether @p lhs relates to
 *  @p rhs in the manner specified by @p comp_arg.
 *
 *  @param[in] lhs the value on the left side of the operator.
 *  @param[in] rhs the value on the right side of the operator.
 *  @param[in] comp_arg a parameter for the comparison function.
 *  @return a negative integer, zero, or a positive integer as the lhs argument
 *          is less than, equal to, or greater than the rhs.
 *  @see du_array_comparator_default
 */
typedef du_int (*du_array_comparator)(const void* lhs, const void* rhs, const void* comp_arg);

/**
 *  Compares its two arguments binary data.
 *
 *  @param[in] lhs the first binary data to be compared.
 *  @param[in] rhs the second binary data to be compared.
 *  @param[in] member_size the number of elements(bytes) to compare.
 *  @return a negative integer, zero, or a positive integer as the first argument is less than,
 *     equal to, or greater than the second.
 *  @remark du_array_comparator_default() is a default function implementing
 *          du_array_comparator interface.
 *  @see du_array_comparator
 */
extern du_int du_array_comparator_default(const void* lhs, const void* rhs, const void* member_size);

extern du_int du_array_comparator_defaultr(const void* lhs, const void* rhs, const void* member_size);

/**
 *  Initializes a du_array data structure x with its object size information.
 *
 *  @param[out] x pointer to the du_array data structure.
 *  @param[in] member_size  byte length of object.
 *  @post @p x must be freed by du_array_free().
 */
extern void du_array_init(du_array* x, du_uint32 member_size);

/**
 *  Makes sure that enough bytes are allocated in x for len objects.
 *
 *  If not enough bytes are allocated, it allocates more bytes,
 *  moving the dynamically allocated region if necessary.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @param[in] len number of objects to allocate.
 *  @return a pointer to the first object.
 *          0 if x has failed, or not enough memory is available.
 *  @pre @p x must be initialized by du_array_init().
 */
extern void* du_array_allocate(du_array* x, du_uint32 len);

/**
 *  Makes sure that enough bytes are allocated in x for at least len objects.
 *
 *  If not enough bytes are allocated, it allocates more bytes,
 *  moving the dynamically allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @param[in] len number of objects to allocate.
 *  @return a pointer to the first object.
 *          0 if x has failed, or not enough memory is available.
 *  @pre @p x must be initialized by du_array_init().
 */
extern void* du_array_allocate_align(du_array* x, du_uint32 len);

/**
 *  Returns a pointer to the first object stored in x.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @return a pointer to the first object.
 *  @pre @p x must be initialized by du_array_init().
 */
#define du_array_get(x) ((x)->p)

/**
 *  Returns a pointer to the specified object stored in x.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @param[in] pos position of the specified object.
 *  @return a pointer to the specified object.
 *          0 if x has failed, or if fewer than pos + 1 members are used.
 *  @pre @p x must be initialized by du_array_init().
 */
extern void* du_array_get_pos(const du_array* x, du_uint32 pos);

/**
 *  Returns a pointer to the last object stored in x.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @return a pointer to the last object of x.
 *          0 if x has failed, or if no members are used.
 *  @pre @p x must be initialized by du_array_init().
 */
extern void* du_array_get_end(const du_array* x);

/**
 *  Attempts to find the specified object in the array x
 *  and returns the position of that object.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @param[in] object the specified object to find.
 *  @return position of the specified object if, and only if, the specified
 *          object finds in the array.
 *          length of x if the specified object does not find in @p x or @p x has failed.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_uint32 du_array_find(const du_array* x, const void* object);

/**
 *  Attempts to find the specified object in the array x by using a
 *  comparison function(comparator) and returns the position of that object.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @param[in] object the specified object to find.
 *  @param[in] comparator a comparison function.
 *  @param[in] comp_arg a parameter of comparator function.
 *  @return position of the specified object if, and only if, the comparator returns zero.
 *          length of x if the specified object does not find in x
 *          or x has failed.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_uint32 du_array_find2(const du_array* x, const void* object, du_array_comparator comparator, const void* comp_arg);

extern du_uint32 du_array_find_in_sort(du_array* x, const du_uint8* from, du_array_comparator comparator, const void* comp_arg);

/**
 *  Returns the number of objects stored in the array x.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @return the number of objects stored in x.
 *  @pre @p x must be initialized by du_array_init().
 */
#define du_array_length(x) (((x)->used / (x)->member_size))

/**
 *  Returns the number of bytes used in x.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @return  the number of bytes used in x.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_uint32 du_array_bytes(const du_array* const x);

/**
 *  Reduces the number of objects in x to exactly len.
 *
 *  It does not change the allocation in x.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @param[in] len number of objects to set.
 *  @retval 1 the number of objects is truncated.
 *  @retval 0 the number of objects is not changed.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_bool du_array_truncate_length(du_array* x, du_uint32 len);

/**
 *  Reduces the number of objects in x to 0.
 *
 *  It does not change the allocation in x.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @return true certainly.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_bool du_array_truncate(du_array* x);

/**
 *  Frees the region used by x and switches to being unallocated.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @pre @p x must be initialized by du_array_init().
 */
extern void du_array_free(du_array* x);

/**
 *  Switches <em>x</em> to have failed.
 *
 *  @param[in,out] x pointer to the du_array structure initialized by du_array_init().
 *  @pre @p x must be initialized by du_array_init().
 */
extern void du_array_fail(du_array* x);

/**
 *  Tests <em>x</em> has failed.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @retval 1 @p x has failed.
 *  @retval 0 @p x doesn't have failed.
 *  @pre @p x must be initialized by du_array_init().
 */
#define du_array_failed(x) ((x)->failed)

/**
 *  Tests x is unallocated.
 *
 *  @param[in] x pointer to the du_array structure.
 *  @retval 1 x is unallocated.
 *  @retval 0 x isn't unallocated.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_bool du_array_unallocated(const du_array* x);

/**
 *  Tests x and y have the same contents.
 *
 *  @param[in] x pointer to a du_array structure.
 *  @param[in] y pointer to another du_array structure.
 *  @retval 1 x and y have the same contents.
 *  @retval 0 x and y have the different contents.
 *  @pre @p x must be initialized by du_array_init().
 *  @pre @p y must be initialized by du_array_init().
 */
extern du_bool du_array_equal(const du_array* const x, const du_array* const y);

/**
 *  Appends all objects in the source array from to a destination
 *  array to, allocating more space if necessary.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from pointer to the source du_array structure.
 *  @return true if, and only if, objects in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 *  @pre @p to must be initialized by du_array_init().
 *  @pre @p from must be initialized by du_array_init().
 */
extern du_bool du_array_cat(du_array* to, const du_array* const from);

/**
 *  Appends len objects in the source array from
 *  to a destination array to, allocating more space if necessary initialized by du_array_init().
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source object(s).
 *  @param[in] len number of object to append.
 *  @return true if, and only if, object(s) is(are) appended successfully.
 *  @pre @p to must be initialized by du_array_init().
 */
extern du_bool du_array_catn(du_array* to, const du_uint8* from, du_uint32 len);

/**
 *  Appends a object in the source array from
 *  to a destination array to, allocating more space if necessary.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source object.
 *  @return true if, and only if, from object is appended successfully.
 *  @pre @p to must be initialized by du_array_init().
 */
#define du_array_cato(to, from) du_array_catn((to), (from), 1)

/**
 *  Removes a object in @p x.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @param[in] pos position of the object to remove.
 *  @return  true if, and only if, the object is removed successfully.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_bool du_array_remove(du_array* x, du_uint32 pos);

/**
 *  Removes a last object in @p x.
 *
 *  @param[in,out] x pointer to the du_array structure.
 *  @return true if, and only if, the object is removed successfully.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_bool du_array_remove_end(du_array* x);

/**
 *  Inserts all objects of from into to at the specified position,
 *  allocating more space if necessary.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source du_array structure.
 *  @param[in] pos position of the object to insert.
 *  @return  true if, and only if, from objects is inserted successfully.
 *  @pre @p to must be initialized by du_array_init().
 *  @pre @p from must be initialized by du_array_init().
 */
extern du_bool du_array_insert(du_array* to, const du_array* const from, du_uint32 pos);

/**
 *  Inserts len objects of from into to at the specified position,
 *  allocating more space if necessary.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source object(s).
 *  @param[in] len number of object to insert.
 *  @param[in] pos position of the object to insert.
 *  @return  true if, and only if, the len objects of from
 *   is inserted successfully.
 *  @pre @p to must be initialized by du_array_init().
 */
extern du_bool du_array_insertn(du_array* to, const du_uint8* from, du_uint32 len, du_uint32 pos);

/**
 *  Inserts an object of from into to at the specified position,
 *  allocating more space if necessary.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source object.
 *  @param[in] pos position of the object to insert.
 *  @return true if, and only if, the from object is inserted successfully.
 *  @pre @p to must be initialized by du_array_init().
 */
#define du_array_inserto(to, from, pos) du_array_insertn((to), (from), 1, pos)

/**
 *  Inserts an object of from into to at the estimated position with comparator,
 *  allocating more space if necessary.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source object initialized by du_array_init().
 *  @param[in] comparator a comparison function to decide inserting position.
 *  @param[in] comp_arg a user-defined parameter for comparator function.
 *  @return  true if, and only if, the from object is inserted successfully.
 *  @pre @p to must be initialized by du_array_init().
 */
extern du_bool du_array_inserto_in_sort(du_array* to, const du_uint8* from, du_array_comparator comparator, const void* comp_arg);

/**
 *  Replaces an object of from into to at the specified position.
 *
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source object.
 *  @param[in] pos position of the object to replace.
 *  @return true if, and only if, the from object is replaced successfully.
 *  @pre @p to must be initialized by du_array_init().
 */
extern du_bool du_array_seto(du_array* to, const du_uint8* from, du_uint32 pos);

/**
 *  Sorts x with comparator.
 *
 *  @param[in,out] x pointer to du_array structure to sort.
 *  @param[in] comparator a comparison function to sort.
 *  @param[in] comp_arg a user-defined parameter for comparator function.
 *  @return true if, and only if, x is sorted successfully.
 *  @pre @p x must be initialized by du_array_init().
 */
extern du_bool du_array_sort(du_array* x, du_array_comparator comparator, const void* comp_arg);

#ifdef __cplusplus
}
#endif

#endif
