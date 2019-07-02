/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_str_array interface provides various methods for manipulating arrays of
 *   string ( such as allocation, initialization, concatenation ).
 *   Before using a du_str_array, use <b>du_str_array_init</b> to initialize the array.
 *   The du_str_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the du_str_array data, use <b>du_str_array_free</b>.
 */


#ifndef DU_STR_ARRAY_H
#define DU_STR_ARRAY_H

#include <du_ptr_array.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains string values.
 */
typedef struct du_str_array {
    du_ptr_array ptr_array;
    du_uchar_array char_array;
} du_str_array;

/**
 *  Initializes a du_str_array data area.
 *  @param[out] sa  pointer to the du_str_array data structure.
 */
extern void du_str_array_init(du_str_array* sa);

/**
 *  Makes sure that enough bytes are allocated for len bytes.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] sa du_str_array structure
 *  @param[in] len number of byte to allocate.
 *  @return
 *    a pointer to the first character.
 *    0 if sa has failed, or not enough memory is available.
 */
extern void* du_str_array_allocate(du_str_array* sa, du_uint32 len);

/**
 *  Makes sure that enough bytes are allocated for at least len bytes.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] sa du_str_array structure
 *  @param[in] len number of byte to allocate.
 *  @return
 *    a pointer to the first character.
 *    0 if sa has failed, or not enough memory is available.
 */
extern void* du_str_array_allocate_align(du_str_array* sa, du_uint32 len);

/**
 *  Gets the first object.
 *  @param[in] sa pointer to the du_str_array structure.
 *  @return
 *    a pointer to the first string data.
 */
extern du_uchar** du_str_array_get(const du_str_array* sa);

/**
 *  Gets an object by position.
 *  @param[in] sa pointer to the du_str_array structure.
 *  @param[in] pos index of the specified string data.
 *  @return
 *    a pointer to the specified string data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern du_uchar* du_str_array_get_pos(const du_str_array* sa, du_uint32 pos);

/**
 *  Gets the last object.
 *  @param[in] sa pointer to the du_str_array structure.
 *  @return
 *    a pointer to the last string data.
 *    0 if x has failed, or if no elements are used.
 */
extern du_uchar* du_str_array_get_end(const du_str_array* sa);

/**
 *  Compares specified two arrays.
 *  @param[in] a a first array.
 *  @param[in] b a second array.
 *  @return false if the value of specified two arrays is definitely same.
 *  otherwise, return true.
 */
extern du_bool du_str_array_diff(const du_str_array* a, const du_str_array* b);

/**
 *  Compares the first len object of specified two arrays.
 *  @param[in] a a first array.
 *  @param[in] len The number of objects to compare.
 *  @param[in] b a second array.
 *  @return false if the first len value of specified two arrays is definitely same.
 *  otherwise, return true.
 */
extern du_bool du_str_array_diffn(const du_str_array* a, du_uint32 len, const du_str_array* b);

/**
 *  Compares specified two arrays.
 *  @param[in] a a first array.
 *  @param[in] b a second array.
 *  @return true if the value of specified two arrays is definitely same.
 *  otherwise, return false.
 */
#define du_str_array_equal(a, b) (!du_str_array_diff((a), (b)))

/**
 *  Compares the first len object of specified two arrays.
 *  @param[in] a a first array.
 *  @param[in] len The number of objects to compare.
 *  @param[in] b a second array.
 *  @return true if the first len value of specified two arrays is definitely same.
 *  otherwise, return false.
 */
#define du_str_array_equaln(a, len, b) (!du_str_array_diffn((a), (len), (b)))

/**
 *  Attempts to find the specified object.
 *  @param[in] sa pointer to the du_str_array structure.
 *  @param[in] s the specified string to find.
 *  @return
 *    position of the specified string if, and only if, the specified string s
 *    finds in the array.
 *    length of sa if s does not find in sa
 *    or sa has failed.
 */
extern du_uint32 du_str_array_find(const du_str_array* sa, const du_uchar* s);

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_str_array structure.
 *  @return  the number of string data stored in sa.
 */
#define du_str_array_length(x) du_ptr_array_length(&(x)->ptr_array)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in sa.
 *  @param[in,out] sa pointer to the du_str_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
extern du_bool du_str_array_truncate_length(du_str_array* sa, du_uint32 len);

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in sa.
 *  @param[in,out] sa pointer to the du_str_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_str_array_truncate(du_str_array* sa);

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] sa pointer to the du_str_array structure.
 */
extern void du_str_array_free(du_str_array* sa);

/**
 *  Tests sa has failed.
 *  @param[in] sa pointer to the du_str_array structure.
 *  @return  true if sa has failed.
 */
#define du_str_array_failed(x) (du_ptr_array_failed(&((x)->ptr_array)) || du_uchar_array_failed(&((x)->char_array)))

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_str_array structure.
 *  @param[in] from pointer to the source du_str_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
extern du_bool du_str_array_cat(du_str_array* to, const du_str_array* from);

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_str_array structure.
 *  @param[in] from pointer to the source du_str_array structure.
 *  @param[in] len number of elements(string data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
extern du_bool du_str_array_catn(du_str_array* to, const du_str_array* from, du_uint32 len);

/**
 *  Appends an object to a destination array sa,
 *  allocating more space if necessary.
 *  @param[in,out] sa pointer to the destination du_str_array structure.
 *  @param[in] str the source string.
 *  @return  true if, and only if, str is appended successfully.
 */
extern du_bool du_str_array_cato(du_str_array* sa, const du_uchar* str);

/**
 *  Appends len characters of a str string to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] sa pointer to the destination du_str_array structure.
 *  @param[in] str the source string.
 *  @param[in] len number of characters of str to append.
 *   @return  true if, and only if, len characters of str
 *   is appended successfully.
 */
extern du_bool du_str_array_caton(du_str_array* sa, const du_uchar* str, du_uint32 len);

/**
 *   Appends a zero value to a destination array,
 *   allocating more space if necessary.
 *   @param[in,out] sa pointer to the destination du_str_array structure.
 *   @return  true if, and only if, a zero value datum is appended successfully.
 */
extern du_bool du_str_array_cat0(du_str_array* sa);

/**
 *  Copys all objects in the source array to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_str_array structure.
 *  @param[in] from source du_str_array structure.
 *  @return  true if, and only if, elements in the source array are copied successfully.
 */
extern du_bool du_str_array_copy(du_str_array* to, const du_str_array* const from);

/**
 *  Copys len objects to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_str_array structure.
 *  @param[in] from a pointer to the source element(s) ( du_uchar data).
 *  @param[in] len number of elements(du_uchar data) to copy.
 *  @return  true if, and only if, element(s) is(are) copied successfully.
 */
extern du_bool du_str_array_copyn(du_str_array* to, const du_str_array* from, du_uint32 len);

/**
 *  Removes an object from array.
 *  @param[in,out] sa pointer to the du_str_array structure.
 *  @param[in] pos index of the element(string data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
extern du_bool du_str_array_remove(du_str_array* sa, du_uint32 pos);

/**
 *  Removes the last object of array.
 *  @param[in,out] sa pointer to the du_str_array structure.
 *  @return  true if, and only if, the element is removed successfully.
 */
extern du_bool du_str_array_remove_end(du_str_array* sa);

/**
 *  Inserts all objects of from into to
 *  at the specified position, allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_str_array structure.
 *  @param[in] from a pointer to the source du_str_array structure.
 *  @param[in] pos position of the element(string data) to insert.
 *  @return  true if, and only if, the elements is inserted successfully.
 */
extern du_bool du_str_array_insert(du_str_array* to, const du_str_array* const from, du_uint32 pos);

/**
 *  Inserts len objects of from into to
 *  at the specified position, allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_str_array structure.
 *  @param[in] from a pointer to the source du_str_array structure.
 *  @param[in] len number of element(string data) to insert.
 *  @param[in] pos position of the element(string data) to insert.
 *  @return  true if, and only if, the elements is inserted successfully.
 */
extern du_bool du_str_array_insertn(du_str_array* to, const du_str_array* const from, du_uint len, du_uint32 pos);

/**
 *  Inserts an object into a destination array at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] sa pointer to the destination du_str_array structure.
 *  @param[in] str the source string.
 *  @param[in] pos position of the string to insert.
 *  @return  true if, and only if, the string is inserted successfully.
 */
extern du_bool du_str_array_inserto(du_str_array* sa, const du_uchar* str, du_uint32 pos);

/**
 *  Inserts len characters of a str string into a destination array
 *  at the specified position, allocating more space if necessary.
 *  @param[in,out] sa pointer to the destination du_str_array structure.
 *  @param[in] str the source string.
 *  @param[in] pos position of the string to insert.
 *  @param[in] len number of characters of str to insert.
 *  @param[in] pos position of the string to insert.
 *   @return  true if, and only if, len characters of str
 *   is inserted successfully.
 */
extern du_bool du_str_array_inserton(du_str_array* sa, const du_uchar* str, du_uint32 len, du_uint32 pos);

extern du_int du_str_array_comparator_default(const void* p1, const void* p2, const void* arg);

extern du_int du_str_array_comparator_defaultr(const void* p1, const void* p2, const void* arg);

#define du_str_array_sort(x) du_ptr_array_sort2((&((x)->ptr_array)), du_str_array_comparator_default, 0)

#define du_str_array_sortr(x) du_ptr_array_sort2((&((x)->ptr_array)), du_str_array_comparator_defaultr, 0)

#define du_str_array_find_in_sort(x, s) du_ptr_array_find_in_sort2((&((x)->ptr_array)), s, du_str_array_comparator_default, 0)

#define du_str_array_find_in_sortr(x, s) du_ptr_array_find_in_sort2((&((x)->ptr_array)), s, du_str_array_comparator_defaultr, 0)

#ifdef __cplusplus
}
#endif

#endif
