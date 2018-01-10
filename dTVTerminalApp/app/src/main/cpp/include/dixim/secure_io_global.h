#ifndef SECURE_IO_GLOBAL_H
#define SECURE_IO_GLOBAL_H

#include "secure_io.h"

#ifdef __cplusplus
extern "C" {
#endif

extern int secure_io_global_create();
extern int secure_io_global_free();
extern secure_io secure_io_global_get_instance();

#ifdef __cplusplus
}
#endif

#endif
