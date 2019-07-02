/*
 * Copyright (c) 2012 DigiOn, Inc. All rights reserved.
 */
/**
 * @file drag_dms_info.h
 * @brief DMS情報構造体の定義
 */
#ifndef DRAG_DMS_INFO_H
#define DRAG_DMS_INFO_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/** フレンドリーネームの長さ */
#define DRAG_DMS_INFO_FRIENDRY_NAME_SIZE    256
/** UDNの長さ */
#define DRAG_DMS_INFO_UDN_SIZE              64

/** DMS情報構造体 */
typedef struct dms_info {
	/** フレンドリーネーム */
    du_uchar friendly_name[DRAG_DMS_INFO_FRIENDRY_NAME_SIZE + 1];
	/** UDN */
    du_uchar udn[DRAG_DMS_INFO_UDN_SIZE];
  /** expire date - long long value represent seconds since January 1, 1970, 00:00:00(UTC) */
  /** 0 means no expiration */
    long long expire_date;
    du_bool has_tuner;

} dms_info;

/**
 * get dtla version number of dms_info.
 * @param dmp_info target DMP information.
 * @return 1: is version 1; 2: is version 2; 0: error
 */
extern du_uint32 drag_dms_info_get_version(struct dms_info* info);

#ifdef __cplusplus
}
#endif

#endif
