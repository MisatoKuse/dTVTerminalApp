/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DU_DIR_OS_H
#define DU_DIR_OS_H

#include <dirent.h>
#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct du_dir {
    DIR* dir;
} du_dir;

#ifdef __cplusplus
}
#endif

#endif
