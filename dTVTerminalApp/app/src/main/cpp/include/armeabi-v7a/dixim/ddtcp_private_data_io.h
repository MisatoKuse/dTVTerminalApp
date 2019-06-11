/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

/** @file ddtcp_private_data_io.h
 *  @brief The ddtcp_private_data_io provides interface to read/write Private Data.<br>
 *         - <b>ddtcp_private_data_io_initialize</b> initializes the <em>private_data_io</em> instance.<br>
 *         - <b>ddtcp_private_data_io_read</b> read data.<br>
 *         - <b>ddtcp_private_data_io_write</b> writes data.<br>
 *         - <b>ddtcp_private_data_io_initialize</b> finalizes the <em>private_data_io</em> instance.<br>
 *  @see ddtcp.h
 */
 
#ifndef DDTCP_PRIVATE_DATA_IO_H
#define DDTCP_PRIVATE_DATA_IO_H

#include <ddtcp.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Type of handle to private_data_io instance
 */
typedef void* ddtcp_private_data_io;

/**
 * Enumerated type of private data
 */
typedef enum {
    DDTCP_PRIVATE_DATA_TYPE_CRT, /**< Device Certificate */
    DDTCP_PRIVATE_DATA_TYPE_RNG, /**< Random Number Generator Seed */
    DDTCP_PRIVATE_DATA_TYPE_PVK, /**< Private Key */
    DDTCP_PRIVATE_DATA_TYPE_SRM, /**< System Renewability Messages(SRM) */
    DDTCP_PRIVATE_DATA_TYPE_IDU, /**< Device IDu(When the Device Certificate is Common Device Certificate, it is necessary) */
    DDTCP_PRIVATE_DATA_TYPE_RSR, /**< Remote Sink Registry */
} ddtcp_private_data_type;

/**
 * Initializes private_data_io instantce.
 * @param[out] private_data_io private_data_io instantce
 * @param[in] additional_param additional parameter
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 * @remark This function is called by <b>ddtcp_startup</b>.
 *         <em>additional_param</em> is passed by <b>ddtcp_set_additional_param</b>
 *         (<em>type</em> param is <em>DDTCP_ADDITINAL_PARAM_TYPE_PRIVATE_DATA_IO</em>).
 */
extern ddtcp_ret ddtcp_private_data_io_initialize(ddtcp_private_data_io* private_data_io, void* additional_param);

/**
 * Finalizes private_data_io instantce.
 * @param[in] private_data_io private_data_io instantce
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 * @remark This function is called by <b>ddtcp_shutdown</b>.
 */
extern ddtcp_ret ddtcp_private_data_io_finalize(ddtcp_private_data_io private_data_io);

/**
 * Read private data
 * @param[in] private_data_io private_data_io instantce
 * @param[in] type type of private data
 * @param[out] buf buffer to read data
 * @param[in] size size of buffer
 * @param[out] nbytes number of bytes that have been read
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_private_data_io_read(ddtcp_private_data_io private_data_io, ddtcp_private_data_type type, du_uint8* buf, du_uint32 size, du_uint32* nbytes);

/**
 * Writes private data
 * @param[in] private_data_io private_data_io instantce
 * @param[in] type type of private data that has one of the following values.
              DDTCP_PRIVATE_DATA_TYPE_RNG<br>
              DDTCP_PRIVATE_DATA_TYPE_SRM<br>
 * @param[in] buf buffer to write data
 * @param[in] size size of buffer
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_private_data_io_write(ddtcp_private_data_io private_data_io, ddtcp_private_data_type type, const du_uint8* buf, du_uint32 size);

#ifdef __cplusplus
}
#endif

#endif

