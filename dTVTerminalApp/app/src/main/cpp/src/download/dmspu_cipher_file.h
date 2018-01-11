/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DMSPU_CIPHER_FILE_H
#define DMSPU_CIPHER_FILE_H

#include <cipher_file.h>
#include <du_str_array.h>
#include <cipher_file_context.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dmspu_cipher_file_read_param(const du_uchar* fn, du_str_array* param_array, cipher_file_context cfc);

extern du_bool dmspu_cipher_file_write_param(const du_uchar* fn, const du_str_array* param_array, cipher_file_context cfc);

extern const du_uchar* dmspu_cipher_file_param_name_version(void);

extern const du_uchar* dmspu_cipher_file_param_name_didl_lite(void);

extern const du_uchar* dmspu_cipher_file_param_name_tshr(void);


#ifdef __cplusplus
}
#endif

#endif

