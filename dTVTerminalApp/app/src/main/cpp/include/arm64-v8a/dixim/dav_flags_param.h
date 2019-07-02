/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_flags_param.h
 *  @brief The dav_flags_param interface provides methods for getting/setting flags.
 */

#ifndef DAV_FLAGS_PARAM_H
#define DAV_FLAGS_PARAM_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Size of flags-param data.
 */
#define DAV_FLAGS_PARAM_SIZE 33

/**
 * primary-flags of flags-param.
 */
typedef enum {
    DAV_FLAGS_PARAM_PF_UNKNOWN = -1, /**< Invalid value. */
    DAV_FLAGS_PARAM_PF_DIS_DTCP_MOVE = 12, /**< DIS-DTCP-move flag (DTCP-IP DIS Move Flag) */
    DAV_FLAGS_PARAM_PF_DIS_DTCP_COPY, /**< DIS-DTCP-copy flag (DTCP-IP DIS Copy Flag) */
    DAV_FLAGS_PARAM_PF_LOP_CLEAR_TEXT_BYTES, /**< lop-cleartextbytes flag (Cleartext Limited Data Seek Flag) */
    DAV_FLAGS_PARAM_PF_CLEAR_TEXT_BYTE_SEEK_FULL, /**< cleartextbyteseek-full flag (Cleartext Byte Full Data Seek Flag) */
    DAV_FLAGS_PARAM_PF_LP,             /**< LP-flag (Link Protected Content Flag) */
    DAV_FLAGS_PARAM_PF_DLNA_V15 = 20,  /**< dlna-v1.5-flag (DLNA v1.5 versioning flag) */
    DAV_FLAGS_PARAM_PF_HTTP_STALLING,  /**< http-stalling (HTTP Connection Stalling Flag) */
    DAV_FLAGS_PARAM_PF_TM_B,           /**< tm-b (Background Mode Flag) */
    DAV_FLAGS_PARAM_PF_TM_I,           /**< tm-i (Interactive Mode Flag) */
    DAV_FLAGS_PARAM_PF_TM_S,           /**< tm-s (Streaming Mode Flag) */
    DAV_FLAGS_PARAM_PF_RTSP_PAUSE,     /**< rtsp-pause (Pause media operation support for RTP Serving Endpoints) */
    DAV_FLAGS_PARAM_PF_SN_INCREASING,  /**< sN-increasing (UCDAM sN Increasing Flag) */
    DAV_FLAGS_PARAM_PF_S0_INCREASING,  /**< s0-increasing (UCDAM s0 Increasing Flag) */
    DAV_FLAGS_PARAM_PF_PLAYCONTAINER_PARAM, /**< playcontainer-param (DLNA PlayContainer Flag) */
    DAV_FLAGS_PARAM_PF_LOP_BYTES,      /**< lop-bytes (Limited Operations Flags: Byte-Based Seek) */
    DAV_FLAGS_PARAM_PF_LOP_NPT,        /**< lop-npt (Limited Operations Flags: Time-Based Seek) */
    DAV_FLAGS_PARAM_PF_SP              /**< sp-flag (Sender Paced Flag) */
} dav_flags_param_pf;

/**
 * primary-flags of flags-param.
 */
typedef dav_flags_param_pf du_flags_param_pf;

/**
 * Initializes a <em>flags_param</em>.
 * @param[out] flags_param pointer to the du_uchar data to store a DLNA primary-flags.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remarks <em>flags_param</em> size is 33 byte (primary-flags = 8 hexdigit +
 * reserved-data = 24 reserved-hexdigit + null terminator).
 * This function stores "00000000000000000000000000000000" in <em>flags_param</em>.
 */
extern void dav_flags_param_init(du_uchar flags_param[DAV_FLAGS_PARAM_SIZE]);

/**
 * Sets the specified <em>flag</em> flag true.
 * @param[in,out] flags_param pointer to the du_uchar data stored a DLNA primary-flags. ex. "00000000000000000000000000000000"
 * @param[in] flag flag.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_flags_param_set_primary_flag(du_uchar flags_param[DAV_FLAGS_PARAM_SIZE], dav_flags_param_pf flag);

/**
 * Gets the specified <em>flag</em> flag value.
 * @param[in] flags_param flags-param used in content features or in the additional info of protocol info.  ex. "00f00000000000000000000000000000"
 * @param[in] flag flag.
 * @return  <em>flag</em> flag value ( true or false ).
 */
extern du_bool dav_flags_param_get_primary_flag(const du_uchar flags_param[DAV_FLAGS_PARAM_SIZE], dav_flags_param_pf flag);

#ifdef __cplusplus
}
#endif

#endif
