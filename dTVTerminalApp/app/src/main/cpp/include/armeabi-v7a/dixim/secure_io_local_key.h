#ifndef SECURE_IO_LOCAL_KEY_H
#define SECURE_IO_LOCAL_KEY_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

#define SECURE_IO_LOCAL_KEY_SIZE 32

extern du_bool secure_io_local_key_get_local_key(du_uint8 local_key[SECURE_IO_LOCAL_KEY_SIZE]);

#ifdef __cplusplus
}
#endif

#endif

