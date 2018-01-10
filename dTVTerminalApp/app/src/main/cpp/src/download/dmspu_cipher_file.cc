/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "dmspu_cipher_file.h"
#include <du_csv.h>
#include <du_log.h>
#include <du_param.h>
#include <du_str.h>

static const du_uchar* CIPHER_FILE_VERSION = DU_UCHAR_CONST("2.0");

du_bool cipher_file_write_didl(const du_uchar* fn, const du_uchar* didl, cipher_file_context cfc){
    du_bool ret= cipher_file_write_didl(fn, didl);
    return ret;
}

static du_bool make_normalized_csv(const du_str_array* param_array, du_uchar_array* csv) {
    const du_uchar* s;
    du_uint32 len;
    du_uint32 i;

    du_uchar_array_truncate(csv);

    if (!du_csv_cat(csv, dmspu_cipher_file_param_name_version())) return 0;
    if ((s = du_param_get_value_by_name(param_array, dmspu_cipher_file_param_name_version()))) {
        if (!du_csv_cat(csv, s)) return 0;
    } else {
        if (!du_csv_cat(csv, CIPHER_FILE_VERSION)) return 0;
    }

    len = du_param_length(param_array);
    for (i = 0; i < len; ++i) {
        s = du_param_get_name(param_array, i);
        if (du_str_equal(s, dmspu_cipher_file_param_name_version())) continue;
        if (!du_csv_cat(csv, s)) return 0;

        s = du_param_get_value(param_array, i);
        if (!du_csv_cat(csv, s)) return 0;
    }
    if (!du_csv_end(csv)) return 0;
    return 1;
}

du_bool dmspu_cipher_file_write_param(const du_uchar* fn, const du_str_array* param_array, cipher_file_context cfc) {
    du_uchar_array ua;
    const du_uchar* s;

    du_uchar_array_init(&ua);

    if (!du_str_array_length(param_array)) goto error;

    s = du_str_array_get_pos(param_array, 0);
    if (du_str_equal(s, dmspu_cipher_file_param_name_version())) {
        if (!du_csv_make(param_array, &ua)) goto error;
    } else {
        if (!make_normalized_csv(param_array, &ua)) goto error;
    }

    if (!du_uchar_array_cat0(&ua)) goto error;
    //extern du_bool cipher_file_write_didl(const du_uchar* fn, const du_uchar* didl);
    if (!cipher_file_write_didl(fn, du_uchar_array_get(&ua), cfc)) goto error;

    du_uchar_array_free(&ua);
    return 1;

error:
    du_uchar_array_free(&ua);
    return 0;
}

const du_uchar* dmspu_cipher_file_param_name_version() {
    return DU_UCHAR_CONST("CIPHER-FILE-VERSION");
}

const du_uchar* dmspu_cipher_file_param_name_didl_lite() {
    return DU_UCHAR_CONST("DIDL-LITE");
}

const du_uchar* dmspu_cipher_file_param_name_tshr() {
    return DU_UCHAR_CONST("TSHR");
}

