/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_OP_PARAM_H
#define DAV_OP_PARAM_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Seek type of op-param.
 */
typedef enum {
    DAV_OP_PARAM_TYPE_BYTE_BASED_SEEK, /*< Byte-based seek */
    DAV_OP_PARAM_TYPE_TIME_BASED_SEEK, /*< Time-based seek */
} dav_op_param_type;

/**
 * Tests the specified <em>type</em> of seek operation is supported.
 * @param[in] op_param op-param used in content features or in the additional info of protocol info.  ex. "10".
 * You can specify null as "00".
 * @param[in] type type of seek operation.
 * @return  <em>type</em> of seek operation supported ( true or false ).
 */
extern du_bool dav_op_param_is_supported(const du_uchar* op_param, dav_op_param_type type);

#ifdef __cplusplus
}
#endif

#endif
