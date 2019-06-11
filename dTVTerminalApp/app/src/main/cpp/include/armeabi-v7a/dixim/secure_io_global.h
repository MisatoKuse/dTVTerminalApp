#ifndef SECURE_IO_GLOBAL_H
#define SECURE_IO_GLOBAL_H

#include "secure_io.h"

#ifdef __cplusplus
extern "C" {
#endif

extern int secure_io_global_create(void);
extern int secure_io_global_free(void);
extern secure_io secure_io_global_get_instance(void);

#ifdef __cplusplus
}
#endif

#endif
