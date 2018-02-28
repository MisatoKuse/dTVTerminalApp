/*
 * Copyright (c) 2012 DigiOn, Inc. All rights reserved.
 */
/**
 * @file drag_dms_info_array.h
 * @brief DMS情報構造体の配列に関するAPIを提供する。
 */
#ifndef DRAG_DMS_INFO_ARRAY_H
#define DRAG_DMS_INFO_ARRAY_H

#include "drag_dms_info.h"
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dms_info_array;

/** DMS構造体配列の初期化 */
#define dms_info_array_init(x) du_array_init((x), sizeof(dms_info))
/** DMS構造体配列の領域確保 */
#define dms_info_array_allocate(x, len) du_array_allocate((x), (len))
/** 配列の先頭要素を取得 */
#define dms_info_array_get(x) ((dms_info*)du_array_get(x))
/** 配列のpos番目の要素を取得 */
#define dms_info_array_get_pos(x, pos) ((dms_info*)du_array_get_pos((x), (pos)))
/** 配列要素数を取得 */
#define dms_info_array_length(x) du_array_length(x)
/** 配列の最後に配列を追加する */
#define dms_info_array_cat(to, from) du_array_cat((to), (from))
/** 配列のn番目に要素を追加する */
#define dms_info_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))
/** 配列の要素を1つ追加する */
#define dms_info_array_cato(to, from) du_array_cato((to), (du_uint8*)(from))
/** DMS構造体配列の開放 */
#ifdef WIN32
extern void dms_info_array_free(dms_info_array* x);
#else
#define dms_info_array_free(x) du_array_free(x)
#endif
#ifdef __cplusplus
}
#endif

#endif
