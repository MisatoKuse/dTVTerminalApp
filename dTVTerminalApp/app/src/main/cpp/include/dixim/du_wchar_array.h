/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_wchar_array interface provides various methods for manipulating arrays of
 *    wchar_t ( such as allocation, initialization, searching, concatenation ).
 *    Before using a du_wchar_array, use <b>du_wchar_array_init</b> to initialize du_wchar_array.
 *    The du_wchar_array supports arrays that can dynamically grow as necessary.
 *    Array indexes always start at position 0.
 *    Deleting the allocated memory of du_wchar_array, use <b>du_wchar_array_free</b>.
 */

#ifndef DU_WCHAR_ARRAY_H
#define DU_WCHAR_ARRAY_H

#include <du_type.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains wchar_t values.
 */
typedef du_array du_wchar_array;

/**
 *  Initializes a du_wchar_array data area.
 *  @param[out] x   du_wchar_array data area.
 */
#define du_wchar_array_init(x) du_array_init((x), sizeof(wchar_t))

/**
 *  Makes sure that enough bytes are allocated for len wchar_t data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_wchar_array structure
 *  @param[in] len number of elements(wchar_t data) to allocate.
 *  @return
 *    a pointer to the first wchar_t data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_wchar_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len wchar_t data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_wchar_array structure
 *  @param[in] len number of elements(wchar_t data) to allocate.
 *  @return
 *    a pointer to the first wchar_t data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_wchar_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x pointer to the du_wchar_array structure.
 *  @return
 *    a pointer to the first wchar_t data.
 */
#define du_wchar_array_get(x) ((wchar_t*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_wchar_array structure.
 *  @param[in] pos index of the specified wchar_t data.
 *  @return
 *    a pointer to the specified wchar_t data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern wchar_t du_wchar_array_get_pos(const du_wchar_array* x, du_uint32 pos);

/**
 *  Gets the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    a pointer to the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern wchar_t du_wchar_array_get_end(const du_wchar_array* x);

/**
 *  Attempts to find the specified object.
 *  @param[in] x pointer to the wchar_t structure.
 *  @param[in] ch the specified wchar_t data to find.
 *  @return
 *    index of the specified element if, and only if, the specified element finds in the array.
 *    number of byte used in x if the specified object does not find in x
 *    or x has failed.
 */
extern du_uint32 du_wchar_array_find(const du_wchar_array* x, wchar_t ch);

/**
 *  Gets the length of the array.
 *  @param[in] x du_wchar_array structure.
 *  @return  the number of wchar_t data stored in x.
 */
#define du_wchar_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x pointer to the du_wchar_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_wchar_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_wchar_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_wchar_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_wchar_array structure.
 *  @return  true.
 */
#define du_wchar_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in] x pointer to the du_wchar_array structure.
 */
#define du_wchar_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_wchar_array structure.
 *  @return  true if x has failed.
 */
#define du_wchar_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_wchar_array structure data.
 *  @param[in] y pointer to another du_wchar_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_wchar_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_wchar_array structure.
 *  @param[in] y pointer to a wchar_t data.
 *  @return  true if, and only if, x and y have the same contents.
 */
extern du_bool du_wchar_array_equals(du_wchar_array* x, const wchar_t* y);

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from pointer to the source du_wchar_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_wchar_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from a pointer to the source element(s) ( wchar_t data).
 *  @param[in] len number of elements(wchar_t data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_wchar_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *  Appends len objects and a du_uchar zero value
 *  to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from a pointer to the source element(s) ( wchar_t data).
 *  @param[in] len number of elements(wchar_t data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
extern du_bool du_wchar_array_catn0(du_wchar_array* to, const wchar_t* from, du_uint32 len);

/**
 *  Appends a string to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from pointer to a wchar_t string data to be appended to to.
 *  @return  true if, and only if, all elements of from are appended successfully.
 */
extern du_bool du_wchar_array_cats(du_wchar_array* to, const wchar_t* from);

/**
 *  Appends null-terminated string,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from pointer to a wchar_t string data to be appended to to.
 *  @return  true if, and only if, all elements of from are appended successfully.
 */
extern du_bool du_wchar_array_cats0(du_wchar_array* to, const wchar_t* from);

/**
 *  Appends an object to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] ch the source element(wchar_t data).
 *  @return  true if, and only if, ch is appended successfully.
 */
extern du_bool du_wchar_array_cato(du_wchar_array* to, wchar_t ch);

/**
 *  Appends a zero value to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @return  true if, and only if, a wchar_t zero value datum is appended successfully.
 */
extern du_bool du_wchar_array_cat0(du_wchar_array* to);

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_wchar_array structure.
 *  @param[in] pos index of the element(wchar_t data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_wchar_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_wchar_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_wchar_array_remove_end(x) du_array_remove_end((x))

/**
 *  Removes the last character if the last character is 0.
 *  @param[in,out] to du_wchar_array structure.
 *  @return  true if, and only if, the element is removed successfully.
 */
extern du_bool du_wchar_array_remove0(du_wchar_array* to);

/**
 *  Inserts all characters of from into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from a pointer to the source elements(du_wchar data).
 *  @param[in] pos position of the element (du_wchar data) to insert.
 *  @return  true if, and only if, from elements is inserted successfully.
 */
#define du_wchar_array_insert(to, from, pos) du_array_insert((to), (from), (pos))

/**
 *  Inserts len characters of from
 *  into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from a pointer to the source elements(du_wchardata).
 *  @param[in] len number of element (du_wchar data) to insert.
 *  @param[in] pos position of the elements(du_wchar data) to insert.
 *  @return  true if, and only if, the len elements of from
 *   is inserted successfully.
 */
#define du_wchar_array_insertn(to, from, len, pos) du_array_insertn((to), (from), (len), (pos))

/**
 *  Inserts a character of from into to
 *  at the specified position, allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_wchar_array structure.
 *  @param[in] from a pointer to the source elements(du_wchar data).
 *  @param[in] pos position of the element (du_wchar data) to insert.
 *  @return  true if, and only if, the element is inserted successfully.
 */
#define du_wchar_array_inserto(to, from, pos) du_array_inserto((to), (from), (pos))

/**
 *  Inserts a zero value into a destination array at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_wchar_array structure.
 *  @param[in] pos position of the element (du_wchar data) to insert.
 *  @return  true if, and only if, a du_wchar zero value is inserted successfully.
 */
extern du_bool du_wchar_array_insert0(du_wchar_array* to, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
