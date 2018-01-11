#ifndef CIPHER_FILE_CONSTANT_H
#define CIPHER_FILE_CONSTANT_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

#define CIPHER_FILE_CONSTANT_SIZE 16

extern void cipher_file_constant_get_0(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void cipher_file_constant_get_1(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void cipher_file_constant_get_2(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void cipher_file_constant_get_3(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void trenc_cipher_file_constant_get_0(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void trenc_cipher_file_constant_get_1(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void trenc_cipher_file_constant_get_2(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);
extern void trenc_cipher_file_constant_get_3(du_uint8 x[CIPHER_FILE_CONSTANT_SIZE], du_uint8 y[CIPHER_FILE_CONSTANT_SIZE]);

#ifdef __cplusplus
}
#endif

#endif






