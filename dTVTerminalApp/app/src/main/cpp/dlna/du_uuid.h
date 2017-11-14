/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_uuid interface provides method for creating a new
 *  universally unique identifier (UUID).
 */

#ifndef DU_UUID_H
#define DU_UUID_H

#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of an UUID.
 */
#define DU_UUID_SIZE 16

/**
 *  Maximum size of a string representation of an UUID.
 */
#define DU_UUID_FMT_SIZE 37

/**
 *  Creates a new universally unique identifier(UUID).
 *  @param[out] b pointer to du_uint8 array to receive the created UUID.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark The b is a 16-byte binary data.
 */
extern du_bool du_uuid_create(du_uint8 b[DU_UUID_SIZE]);

/**
 *  Converts a binary universally unique identifier(UUID) to a string.
 *  @param[out] s pointer to string (du_uchar array) to receive the UUID.
 *  @param[in] b pointer to du_uint8 array stored the binary UUID value.
 *  @return  the length of s string.
 *  @remark s is a UUID value using hexadecimal digits
 *  in the following format: 12345678-1234-1234-1234-123456789ABC.
 */
extern du_uint32 du_uuid_fmt(du_uchar s[DU_UUID_FMT_SIZE], du_uint8 b[DU_UUID_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
