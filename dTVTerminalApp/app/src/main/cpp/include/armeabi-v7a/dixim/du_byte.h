/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_byte interface provides methods for array of bytes manipulation
 *  (such as copy, compare, initialize).
 */

#ifndef DU_BYTE_H
#define DU_BYTE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Copies elements from one array of bytes to another.
 *
 *  Copies from[0] to to[0], from[1] to to[1], ... and from[n-1] to to[n-1].
 *
 *  @param[out] to the destination array of bytes.
 *  @param[in] n the number of elements(bytes) to copy.
 *  @param[in] from the source array of bytes.
 *  @return the number of bytes copyed.
 */
extern du_uint32 du_byte_copy(du_uint8* to, du_uint32 n, const du_uint8* from);

/**
 *  Compares the contents of the two arrays of bytes.
 *
 *  Returns negative, 0, or positive, depending on whether the a[0], a[1],
 *  .., a[n-1] is lexicographically smaller than, equal to, or greater than
 *  the b[0], b[1], ... b[n-1].
 *
 *  @param[in] a an array of bytes to compare.
 *  @param[in] n the number of elements(bytes) to compare.
 *  @param[in] b another array of bytes to compare.
 *  @return less than, equal to, or greater than zero if the first n bytes of
 *          a is found, respectively, to be less than, to match, or be greater than b.
 */
extern du_int32 du_byte_diff(const du_uint8* a, du_uint32 n, const du_uint8* b);

/**
 *  Sets the contents of a array of bytes to zero.
 *
 *  Sets the bytes s[0], s[1], .., s[n-1] to zero.
 *
 *  @param[out] s a array of bytes to set to zero.
 *  @param[in] n the number of elements(bytes) to set to zero.
 */
extern void du_byte_zero(du_uint8* s, du_uint32 n);

/**
 *  Compares the contents of the two arrays of bytes.
 *
 *  Returns non-zero when a[0], a[1], .., a[n-1] is lexicographically equal to
 *  the b[0], b[1], ... b[n-1].
 *
 *  @param[in] a an array of bytes to compare.
 *  @param[in] n the number of elements(bytes) to compare.
 *  @param[in] b another array of bytes to compare.
 *  @return non-zero if the first n bytes of a is found
 *          respectively to match b.
 */
#define du_byte_equal(a, n, b) (!du_byte_diff((a), (n), (b)))

/**
 *  Returns an index to the first occurrence of @p nb byte @p b in
 *  @p na byte @p a.
 *
 *  Returns @p na value if @p b does not appear in @p a.
 *
 *  @param[in] a an array of bytes to search.
 *  @param[in] na the number of elements(bytes) to search.
 *  @param[in] b another array of bytes to search for.
 *  @param[in] nb the number of elements(bytes) to search for.
 *  @return an index to the first occurrence of b in a.
 *          If b does not appear in a, returns na value.
 */
extern du_uint32 du_byte_find(const du_uint8* a, du_uint32 na, const du_uint8* b, du_uint32 nb);

#ifdef __cplusplus
}
#endif

#endif
