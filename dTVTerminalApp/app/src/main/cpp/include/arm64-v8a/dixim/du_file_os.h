/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file du_file_os.h
 * @brief The du_file_os interface provides methods for file management on Linux.
 */

#ifndef DU_FILE_OS_H
#define DU_FILE_OS_H

#include <du_type.h>
#include <limits.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef int du_file;

/**
 *  DU_FILE_INVALID : value of invalid file handle.
 */
#define DU_FILE_INVALID -1

/**
 *  DU_FILE_SEPARATOR : path separator character.
 */
#define DU_FILE_SEPARATOR "/"

/**
 *  DU_FILE_MAX_PATH_SIZE : maximum charcters in a path name including null.
 */
#define DU_FILE_MAX_PATH_SIZE PATH_MAX

/**
 *  DU_FILE_MAX_FILE_NAME_SIZE : maximum charcters in a file name including null.
 */
#define DU_FILE_MAX_FILE_NAME_SIZE NAME_MAX

#ifdef __cplusplus
}
#endif

#endif
