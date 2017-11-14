/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_socket_array interface provides various methods for manipulating arrays of
 *   du_socket  ( such as allocation, initialization, searching, concatenation ).
 *   Before using a du_socket_array, use <b>du_socket_array_init</b> to initialize du_socket_array.
 *   The du_socket_array supports arrays that can dynamically grow as necessary.
 *   Array indexes always start at position 0.
 *   Deleting the allocated memory of du_socket_array, use <b>du_socket_array_free</b>.
 */

#ifndef DU_SOCKET_ARRAY_H
#define DU_SOCKET_ARRAY_H

#include "du_array.h"
#include "du_socket_array.h"
#include "du_netif_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Resizable array which can contains du_socket values.
 */
typedef du_array du_socket_array;

/**
 *  Initializes a du_socket_array data area.
 *  @param[out] x   du_socket_array data area.
 */
#define du_socket_array_init(x) du_array_init((x), sizeof(du_socket))

/**
 *  Makes sure that enough bytes are allocated for len pointers.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  @param[in,out] x pointer to the du_socket_array structure
 *  @param[in] len number of elements(du_socket data) to allocate.
 *  @return
 *    a pointer to the first du_socket data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_socket_array_allocate(x, len) du_array_allocate((x), (len))

/**
 *  Makes sure that enough bytes are allocated for at least len du_socket data.
 *  If not enough bytes are allocated, it allocates more bytes, moving the dynamically
 *  allocated region if necessary.
 *  It often allocates somewhat more bytes than necessary, to save time later.
 *  @param[in,out] x pointer to the du_socket_array structure
 *  @param[in] len number of elements(du_socket data) to allocate.
 *  @return
 *    a pointer to the first du_socket data.
 *    0 if x has failed, or not enough memory is available.
 */
#define du_socket_array_allocate_align(x, len) du_array_allocate_align((x), (len))

/**
 *  Gets the first object.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @return
 *    a pointer to the first du_socket data.
 */
#define du_socket_array_get(x) ((du_socket*)du_array_get(x))

/**
 *  Gets an object by position.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @param[in] pos index of the specified du_socket data.
 *  @return
 *    a pointer to the specified du_socket data.
 *    0 if x has failed, or if fewer than pos + 1 elements are used.
 */
extern du_socket du_socket_array_get_pos(const du_socket_array* x, du_uint32 pos);

/**
 *  Gets the last object.
 *  @param[in] x pointer to the du_array structure.
 *  @return
 *    the last object of x.
 *    0 if x has failed, or if no members are used.
 */
extern du_socket du_socket_array_get_end(const du_socket_array* x);

/**
 *  Attempts to find the specified object.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @param[in] s the specified du_socket data to find.
 *  @return
 *    position of the specified du_socket data if, and only if,
 *    the specified du_socket data is found in the array.
 *    length of x if the specified du_socket data is not found in x
 *    or x has failed.
 */
#define du_socket_array_find(x, s) du_array_find((x), &(s));

/**
 *  Attempts to find the specified object by IP.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @param[in] ip the specified du_ip data to find.
 *  @return
 *    position of the specified du_socket data if, and only if,
 *    the du_socket data which has the specified du_ip data is found in the array.
 *    length of x if the specified du_socket data is not found in x
 *    or x has failed.
 */
extern du_uint32 du_socket_array_find_ip(const du_socket_array* x, const du_ip* ip);

/**
 *  Gets the length of the array.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @return  the number of du_socket data stored in x.
 */
#define du_socket_array_length(x) du_array_length(x)

/**
 *  Gets the length of the array in bytes.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @return  the number of bytes used in x.
 */
#define du_socket_array_bytes(x) du_array_bytes(x)

/**
 *  Truncates the length of array.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_socket_array structure.
 *  @param[in] len number of elements to set.
 *  @return  true if the number of elements is truncated.
 *           false if the number of elements is not changed.
 */
#define du_socket_array_truncate_length(x, len) du_array_truncate_length((x), (len))

/**
 *  Truncates the length of array to 0.
 *  It does not change the allocation in x.
 *  @param[in,out] x pointer to the du_socket_array structure.
 *  @return  true.
 */
#define du_socket_array_truncate(x) du_array_truncate(x)

/**
 *  Frees the allocated space for the array.
 *  @param[in,out] x pointer to the du_socket_array structure.
 */
#define du_socket_array_free(x) du_array_free(x)

/**
 *  Tests x has failed.
 *  @param[in] x pointer to the du_socket_array structure.
 *  @return  true if x has failed.
 */
#define du_socket_array_failed(x) du_array_failed(x)

/**
 *  Tests x and y have the same content.
 *  @param[in] x pointer to a du_socket_array structure data.
 *  @param[in] y pointer to another du_socket_array structure data.
 *  @return  true if, and only if, x and y have the same contents.
 */
#define du_socket_array_equal(x, y) du_array_equal((x), (y))

/**
 *  Appends all objects in the source array from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_socket_array structure.
 *  @param[in] from pointer to the source du_socket_array structure.
 *  @return  true if, and only if, elements in the source array are appended successfully.
 *          If source array has failed , sets destination array status to failed and returns false.
 */
#define du_socket_array_cat(to, from) du_array_cat((to), (from))

/**
 *  Appends len objects of from
 *  to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_socket_array structure.
 *  @param[in] from a pointer to the source element(s) (du_socket data).
 *  @param[in] len number of elements(du_socket data) to append.
 *  @return  true if, and only if, element(s) is(are) appended successfully.
 */
#define du_socket_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

/**
 *  Appends an object to a destination array to,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_socket_array structure.
 *  @param[in] s the source element(du_socket data).
 *  @return  true if, and only if, ch is appended successfully.
 */
#define du_socket_array_cato(to, s) du_array_cato((to), (du_uint8*)&(s))

/**
 *  Removes an object from array.
 *  @param[in,out] x pointer to the du_socket_array structure.
 *  @param[in] pos index of the element(du_socket data) to remove.
 *  @return  true if, and only if, the element is removed successfully.
 */
#define du_socket_array_remove(x, pos) du_array_remove((x), (pos))

/**
 *  Removes the last object of array.
 *  @param[in,out] x pointer to the du_socket_array structure.
 *  @return  true if, and only if, the object is removed successfully.
 */
#define du_socket_array_remove_end(x) du_array_remove_end((x))

/**
 *  Inserts all objects of from into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_array structure.
 *  @param[in] from a pointer to the source element(s).
 *  @param[in] pos position of the element to insert.
 *  @return  true if, and only if, from elements is inserted successfully.
 */
#define du_socket_array_insert(to, from, pos) du_array_insert((to), (from), (pos))

/**
 *  Inserts an object of s into to at the specified position,
 *  allocating more space if necessary.
 *  @param[in,out] to pointer to the destination du_socket_array structure.
 *  @param[in] s a pointer to the source element.
 *  @param[in] pos position of the element to insert.
 *  @return  true if, and only if, the s element is inserted successfully.
 */
#define du_socket_array_inserto(to, s, pos) du_array_inserto((to), (du_uint8*)&(s), (pos))

/**
 *  Tests socket_array and netif_array have the same IP addresses.
 *  @param[in] socket_array pointer to a du_socket_array structure data.
 *  @param[in] netif_array pointer to another du_netif_array structure data.
 *  @return  true if, and only if, socket_array and netif_array
 *  have the same IP addresses.
 */
extern du_bool du_socket_array_match_netif_array(const du_socket_array* socket_array, const du_netif_array* netif_array);

#ifdef __cplusplus
}
#endif

#endif
