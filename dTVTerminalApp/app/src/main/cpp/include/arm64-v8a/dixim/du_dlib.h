/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_dlib interface implements the interface to the dynamic
 *  linking loader.
 *
 *  @remark du_dlib functions do not run on the OS
 *          which does not support dynamic-link library.
 *  @remark Use UTF-8 characters for file or directory name.
 */

#ifndef DISABLE_DYNAMIC_LINK
#ifndef DU_DLIB_H
#define DU_DLIB_H

#include <du_type.h>
#include <du_dlib_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Loads the dynamic library file named @p name, gets
 *  a handle for the dynamic library, and stores it in @p d.
 *
 *  @param[out] d pointer to the variable to store the handle for
 *              the dynamic library.
 *  @param[in] name the file name of the dynamic library.
 *  @retval 1 The function succeeds.
 *  @retval 0 The function fails.
 *  @remark
 *   @li On Windows system: If no file name extension is specified in
 *   @p name, the default library extension .dll is appended.
 *   When @p name is a path, be sure to use backslashes (\),
 *   not forward slashes (/).
 *   @li On linux system: When @p name is not a path,
 *   the prefix 'lib' and the default library extension .so is added to
 *   the name.
 *   (ex. If @p name is "dixim_util", then du_dlib_load() searches
 *   "libdixim_util.so" file).
 *   When @p name is a path, be sure to use forward slashes (/),
 *   not backslashes (\).
 */
extern du_bool du_dlib_load(du_dlib* d, const du_uchar* name);

/**
 *  Gets the address where the name symbol (function or variable name) is
 *  loaded into memory.
 *
 *  @param[in] d a handle to the dynamic library returned by du_dlib_load().
 *  @param[in] name the function or variable name.
 *  @return pointer the address where the symbol is loaded into memory.
 *          null otherwise.
 */
extern void* du_dlib_name(du_dlib* d, const du_uchar* name);

/**
 *  Frees the dynamic library.
 *
 *  @param[in] d a handle to the dynamic library returned by du_dlib_load().
 *  @retval 1 The function succeeds.
 *  @retval 0 The function fails.
 */
extern du_bool du_dlib_free(du_dlib* d);

#ifdef __cplusplus
}
#endif

#endif
#endif
