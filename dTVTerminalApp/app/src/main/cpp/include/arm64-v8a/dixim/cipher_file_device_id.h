#ifndef CIPHER_FILE_DEVICE_ID_H
#define CIPHER_FILE_DEVICE_ID_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

#define CIPHER_FILE_DEVICE_ID_SIZE 32

extern du_bool cipher_file_device_id_get(du_uint8 id[CIPHER_FILE_DEVICE_ID_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
