/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_dir interface provides methods for directory management
 *  ( such as creating directory, removing directory, reading directory entry ).
 *
 *  @remark Use UTF-8 characters for directory name.
 */

#ifndef DU_DIR_H
#define DU_DIR_H

#include <du_type.h>
#include <du_dir_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Creates a new directory named fn.
 *
 *  @param[in] fn directory name.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p fn must be UTF-8 character string.
 */
extern du_bool du_dir_make(const du_uchar* fn);

/**
 *  Deletes an existing empty directory named fn.
 *
 *  @param[in] fn directory name to be removed.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p fn must be UTF-8 character string.
 *  @remark The directory to be removed must be empty.
 */
extern du_bool du_dir_remove(const du_uchar* fn);

/**
 *  Opens a directory corresponding to the directory name fn,
 *  and outputs the directory information to the du_dir d.
 *
 *  The du_dir d receives information about a found file or subdirectory,
 *  and is used in a subsequent call to du_dir_read() or du_dir_close().
 *
 *  @param[out] d pointer to the du_dir structure to receive the information.
 *  @param[in] fn the directory name.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p fn must be UTF-8 character string.
 *  @post @p d must be closed by du_dir_close().
 */
extern du_bool du_dir_open(du_dir* d, const du_uchar* fn);

/**
 *  Returns a directory entry (file or subdirectory) name from @p d information.
 *
 *  It returns NULL on reaching the end-of-file or if an error occurred.
 *
 *  @param[in] d pointer to the du_dir structure.
 *  @return  pointer to the directory entry (file or subdirectory) name.
 *           NULL on reaching the end-of-file or if an error occurred.
 *  @pre @p d must be opened by du_dir_open().
 *  @remarks The du_dir d can be used in subsequent calls to du_dir_read() to
 *           get the next directory entry name.
 */
extern const du_uchar* du_dir_read(du_dir* d);

/**
 *  Closes the directory associated with d.
 *
 *  @p d is not available after this call.
 *
 *  @param[in] d pointer to the du_dir structure.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p d must be opened by du_dir_open().
 */
extern du_bool du_dir_close(du_dir* d);

#ifdef __cplusplus
}
#endif

#endif
