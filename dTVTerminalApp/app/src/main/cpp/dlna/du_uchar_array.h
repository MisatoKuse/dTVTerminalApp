/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_uchar_array interface provides various methods for manipulating arrays of
 *    du_uchar ( such as allocation, initialization, searching, concatenation ).
 *    Before using a du_uchar_array, use <b>du_uchar_array_init</b> to initialize du_uchar_array.
 *    The du_uchar_array supports arrays that can dynamically grow as necessary.
 *    Array indexes always start at position 0.
 *    Deleting the allocated memory of du_uchar_array, use <b>du_uchar_array_free</b>.
 */

#ifndef DU_UCHAR_ARRAY_H
#define DU_UCHAR_ARRAY_H

#include "du_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_uchar values.
 */
typedef du_array du_uchar_array;

/**
 *  Initializes a du_uchar_array data area.
 *  @param[out] x   du_uchar_array data area.
 */
#define du_uchar_array_init(x) du_array_init((x), sizeof(du_uchar))

/**
 *  Makes sure that enough bytes are allocated for len du_uchar data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x du_uchar_array structure
 *  @param[in] len number of elements(du_uchar data) to allocate.
 *  @return
 *    a pointer to the first du_uchar data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_uchar_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len du_uchar data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x du_uchar_array structure
 *  @param[in] len number of elements(du_uchar data) to allocate.
 *  @return
 *    a pointer to the first du_uchar data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_uchar_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x du_uchar_array structure.
 *  @return
 *    a pointer to the first du_uchar data.
 */
#define du_uchar_array_get(x) ((du_uchar*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x du_uchar_array structure.
 *  @param[in] pos index of the specified du_uchar data.
 *  @return
 *    the value of the specified du_uchar data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern du_uchar du_uchar_array_get_pos(const du_uchar_array* x, du_uint32 pos);

/**
 *  Gets the last object.
 *  Returns a last character of x.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    the value of the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_uchar du_uchar_array_get_end(const du_uchar_array* x);

/**
 *  Attempts to find the specified object.
 *  @param[in] x du_uchar structure.
 *  @param[in] ch the specified du_uchar data to find.
 *  @return
 *    index of the specified element if, and only if, the specified element finds in the array.
 *    number of byte used in x if the specified object does not find in x
 *    or x has failed.
 */
extern du_uint32 du_uchar_array_find(const du_uchar_array* x, du_uchar ch);

/**
 *  Gets the length of the array.
 *  @param[in] x du_uchar_array structure.
 *  @return  the number of du_uchar data stored in x.
 */
#define du_uchar_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x du_uchar_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_uchar_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x du_uchar_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_uchar_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x du_uchar_array structure.
 *  @return  true.
 */
#define du_uchar_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] x du_uchar_array structure.
 */
#define du_uchar_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_uchar_array structure.
 *  @return  true if x has failed.
 */
#define du_uchar_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x du_uchar_array structure data.
 *  @param[in] y du_uchar_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_uchar_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to the du_uchar_array structure data.
 *  @param[in] y pointer to the du_uchar data.
 *  @return  true if, and only if, x and y have the same contents.
 */
extern du_bool du_uchar_array_equals(du_uchar_array* x, const du_uchar* y);

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] from source du_uchar_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 */
#define du_uchar_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] from a pointer to the source element(s) ( du_uchar data).
 *  @param[in] len number of elements(du_uchar data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_uchar_array_catn(to, from, len) du_array_catn((to), (from), (len))

/**
 *  Appends len objects and a du_uchar zero value
 *  to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] from a pointer to the source element(s) ( du_uchar data).
 *  @param[in] len number of elements(du_uchar data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
extern du_bool du_uchar_array_catn0(du_uchar_array* to, const du_uchar* from, du_uint32 len);

/**
 *  Appends a string to a destination array,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] from pointer to a null-terminated du_uchar data to be appended to to.
 *  @return  true if, and only if, all elements of from are appended successfully.
 */
extern du_bool du_uchar_array_cats(du_uchar_array* to, const du_uchar* from);

/**
 *  Appends null-terminated string,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] from pointer to a null-terminated du_uchar data to be appended to to.
 *  @return  true if, and only if, all elements of from are appended successfully.
 */
extern du_bool du_uchar_array_cats0(du_uchar_array* to, const du_uchar* from);

/**
 *  Appends an object to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] ch the source element (du_uchar data).
 *  @return  true if, and only if, ch is appended successfully.
 */
extern du_bool du_uchar_array_cato(du_uchar_array* to, du_uchar ch);

/**
 *   Appends a zero value to a destination array,
 *   allocating more space if necessary.
 *   @param[in,out] to destination du_uchar_array structure.
 *   @return  true if, and only if, a du_uchar zero value is appended successfully.
 */
extern du_bool du_uchar_array_cat0(du_uchar_array* to);

/**
 *  Copys all characters in the source array to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_uchar_array structure.
 *  @param[in] from source du_uchar_array structure.
 *  @return  true if, and only if, elements in the source array are copied successfully.
 */
extern du_bool du_uchar_array_copy(du_uchar_array* to, const du_uchar_array* const from);

/**
 *  Copys len characters to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_uchar_array structure.
 *  @param[in] from a pointer to the source element(s) ( du_uchar data).
 *  @param[in] len number of elements(du_uchar data) to copy.
 *  @return  true if, and only if, element(s) is(are) copied successfully.
 */
extern du_bool du_uchar_array_copyn(du_uchar_array* to, const du_uchar* from, du_uint32 len);

/**
 *  Copys len characters and a zero value
 *  to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_uchar_array structure.
 *  @param[in] from a pointer to the source element(s) ( du_uchar data).
 *  @param[in] len number of elements(du_uchar data) to copy.
 *  @return  true if, and only if, element(s) is(are) copied successfully.
 */
extern du_bool du_uchar_array_copyn0(du_uchar_array* to, const du_uchar* from, du_uint32 len);

/**
 *  Copys a string to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_uchar_array structure.
 *  @param[in] from pointer to a null-terminated du_uchar string to be copied to to.
 *  @return  true if, and only if, all elements of from are copied successfully.
 */
extern du_bool du_uchar_array_copys(du_uchar_array* to, const du_uchar* from);

/**
 *  Copys null-terminated string to a destination array,
 *  allocating more space if necessary.
 *  @param[out] to destination du_uchar_array structure.
 *  @param[in] from pointer to a null-terminated du_uchar string to be copied to to.
 *  @return  true if, and only if, all elements of from are copied successfully.
 */
extern du_bool du_uchar_array_copys0(du_uchar_array* to, const du_uchar* from);

/**
 *  Removes an object from array.
 *  @param[in,out] x du_uchar_array structure.
 *  @param[in] pos index of the element (du_uchar data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_uchar_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_uchar_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_uchar_array_remove_end(x) du_array_remove_end((x))

/**
 *  Removes the last character if the last character is 0.
 *  @param[in,out] to du_uchar_array structure.
 *  @return  true if, and only if, the element is removed successfully.
 */
extern du_bool du_uchar_array_remove0(du_uchar_array* to);

/**
 *  Inserts all characters of from into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_uchar_array structure.
 *  @param[in] from a pointer to the source elements(du_uchar data).
 *  @param[in] pos position of the element (du_uchar data) to insert.
 *  @return  true if, and only if, from elements is inserted successfully.
 */
#define du_uchar_array_insert(to, from, pos) du_array_insert((to), (from), (pos))

/**
 *  Inserts len characters of from
 *  into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_uchar_array structure.
 *  @param[in] from a pointer to the source elements(du_uchardata).
 *  @param[in] len number of element (du_uchar data) to insert.
 *  @param[in] pos position of the elements(du_uchar data) to insert.
 *  @return  true if, and only if, the len elements of from
 *   is inserted successfully.
 */
#define du_uchar_array_insertn(to, from, len, pos) du_array_insertn((to), (from), (len), (pos))

/**
 *  Inserts a character of from into to
 *  at the specified position, allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_uchar_array structure.
 *  @param[in] from a pointer to the source elements(du_uchar data).
 *  @param[in] pos position of the element (du_uchar data) to insert.
 *  @return  true if, and only if, the element is inserted successfully.
 */
#define du_uchar_array_inserto(to, from, pos) du_array_inserto((to), (from), (pos))

/**
 *  Inserts a zero value into a destination array at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to destination du_uchar_array structure.
 *  @param[in] pos position of the element (du_uchar data) to insert.
 *  @return  true if, and only if, a du_uchar zero value is inserted successfully.
 */
extern du_bool du_uchar_array_insert0(du_uchar_array* to, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
