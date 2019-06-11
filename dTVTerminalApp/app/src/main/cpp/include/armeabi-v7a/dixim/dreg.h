/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dreg.h
 *  @brief The dreg interface provides various basic methods for accessing SQLite DB
 * (such as opening DB, closing DB, searching, updating, deleting data).
 */

#ifndef DREG_H
#define DREG_H

#include <du_str_array.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Transaction mode.
 */
typedef enum {
    DREG_TRANSACTION_MODE_UNKNOWN,
    DREG_TRANSACTION_MODE_READ_ONLY,  /**< Transaction only of reading. */
    DREG_TRANSACTION_MODE_READ_WRITE,  /**< Transaction of reading and writing. */
} dreg_transaction_mode;

/**
 * This structure contains information for accessing SQLite DB.
 */
typedef struct dreg dreg;

typedef struct dreg_connection dreg_connection;

/**
 * Frees the region used by <em>dreg</em>.
 * @param[in] reg pointer to the dreg structure.
 */
typedef void (*dreg_free)(dreg* reg);

/**
 * Executes 'begin transaction'.
 * @param[in] reg pointer to the dreg structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_begin)(dreg* reg);

/**
 * Executes 'begin transaction' with transaction mode.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] mode mode of transaction.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_begin2)(dreg* reg, dreg_transaction_mode transaction_mode);

/**
 * Executes 'commit transaction'.
 * @param[in] reg pointer to the dreg structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_commit)(dreg* reg);

/**
 * Executes 'rollback transaction'.
 * @param[in] reg pointer to the dreg structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_rollback)(dreg* reg);

/**
 * Clears registry.
 * @param[in] reg pointer to the dreg structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_clear)(dreg* reg);

/**
 * Returns a array of the keys contained in this registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[out] keys pointer to the variable that receives the keys.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_keys)(dreg* reg, du_str_array* keys);

/**
 * Gets a array of the keys that starts with specified character string
 * contained in this registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key_prefix this prefix.
 * @param[out] keys pointer to the variable that receives the keys.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_keys_prefix)(dreg* reg, const du_uchar* key_prefix, du_str_array* keys);

/**
 * Removes a key-value mapping from registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be removed.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_remove)(dreg* reg, const du_uchar* key);

/**
 * Returns true if this registry contains a mapping for the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be tested.
 * @param[out] exists true if database contains a mapping for the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_exists)(dreg* reg, const du_uchar* key, du_bool* exists);

/**
 * Gets the boolean value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_bool)(dreg* reg, const du_uchar* key, du_bool* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value boolean value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_bool)(dreg* reg, const du_uchar* key, du_bool value);

/**
 * Gets the int16 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_int16)(dreg* reg, const du_uchar* key, du_int16* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value int16 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_int16)(dreg* reg, const du_uchar* key, du_int16 value);

/**
 * Gets the uint16 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_uint16)(dreg* reg, const du_uchar* key, du_uint16* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value uint16 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_uint16)(dreg* reg, const du_uchar* key, du_uint16 value);

/**
 * Gets the int32 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_int32)(dreg* reg, const du_uchar* key, du_int32* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value int32 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_int32)(dreg* reg, const du_uchar* key, du_int32 value);

/**
 * Gets the uint32 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_uint32)(dreg* reg, const du_uchar* key, du_uint32* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value uint32 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_uint32)(dreg* reg, const du_uchar* key, du_uint32 value);

/**
 * Gets the int64 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_int64)(dreg* reg, const du_uchar* key, du_int64* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value int64 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_int64)(dreg* reg, const du_uchar* key, du_int64 value);

/**
 * Gets the uint64 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_uint64)(dreg* reg, const du_uchar* key, du_uint64* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value uint64 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_uint64)(dreg* reg, const du_uchar* key, du_uint64 value);

/**
 * Gets the float32 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_float32)(dreg* reg, const du_uchar* key, du_float32* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value float32 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_float32)(dreg* reg, const du_uchar* key, du_float32 value);

/**
 * Gets the float64 value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_float64)(dreg* reg, const du_uchar* key, du_float64* value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value float64 value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_float64)(dreg* reg, const du_uchar* key, du_float64 value);

/**
 * Gets the string value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf pointer to the variable that receives the value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_string)(dreg* reg, const du_uchar* key, du_uchar** value);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] value string value to be associated with the specified key.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_string)(dreg* reg, const du_uchar* key, const du_uchar* value);

/**
 * Gets the binary value in registry corresponding to the specified key.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key the key whose associated value is to be returned.
 * @param[out] buf a buffer that receives the value.
 * @param[out] length pointer to the variable that receives the number of bytes.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_get_binary)(dreg* reg, const du_uchar* key, du_uint8* buf, du_uint32* length);

/**
 * Puts a key-value mapping to registry.
 * @param[in] reg pointer to the dreg structure.
 * @param[in] key key with which the specified value is to be associated.
 * @param[in] buf the buffer containing the binary data to be put to registry.
 * @param[in] length number of bytes to be put to registry.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dreg_put_binary)(dreg* reg, const du_uchar* key, const du_uint8* buf, du_uint32 length);

struct dreg {
    dreg_free free;
    dreg_begin begin;
    dreg_begin2 begin2;
    dreg_commit commit;
    dreg_clear clear;
    dreg_rollback rollback;
    dreg_get_keys get_keys;
    dreg_get_keys_prefix get_keys_prefix;
    dreg_remove remove;
    dreg_exists exists;
    dreg_get_bool get_bool;
    dreg_put_bool put_bool;
    dreg_get_int16 get_int16;
    dreg_put_int16 put_int16;
    dreg_get_uint16 get_uint16;
    dreg_put_uint16 put_uint16;
    dreg_get_int32 get_int32;
    dreg_put_int32 put_int32;
    dreg_get_uint32 get_uint32;
    dreg_put_uint32 put_uint32;
    dreg_get_int64 get_int64;
    dreg_put_int64 put_int64;
    dreg_get_uint64 get_uint64;
    dreg_put_uint64 put_uint64;
    dreg_get_float32 get_float32;
    dreg_put_float32 put_float32;
    dreg_get_float64 get_float64;
    dreg_put_float64 put_float64;
    dreg_get_string get_string;
    dreg_put_string put_string;
    dreg_get_binary get_binary;
    dreg_put_binary put_binary;
};

#ifdef __cplusplus
}
#endif

#endif
