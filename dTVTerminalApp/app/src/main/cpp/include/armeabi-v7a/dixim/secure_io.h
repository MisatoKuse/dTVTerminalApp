#ifndef SECURE_IO_H
#define SECURE_IO_H

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    void* dummy;
} secure_io_dummy, * secure_io;

typedef enum {
    SECURE_IO_ID_UNKNOWN = 0,
    SECURE_IO_ID_DTCP_IP_DEVICE_KEY,
    SECURE_IO_ID_DTCP_IP_SRM,
    SECURE_IO_ID_HWID,
    SECURE_IO_ID_LOCAL_ENCRYPTION_KEY_INDEX,
    SECURE_IO_ID_LOCAL_ENCRYPTION_KEY,
    SECURE_IO_ID_DB_HASH,
    SECURE_IO_ID_DTCP_IP_RSR_HASH,
    SECURE_IO_ID_EXPORTED_LOCAL_ENCRYPTION_KEY, // XXX: for Modern UI background windows desktop service.
} secure_io_id;

extern int secure_io_create(secure_io* sio);
extern int secure_io_free(secure_io sio);
extern int secure_io_get(secure_io sio, secure_io_id id, unsigned char* buf, unsigned int size, unsigned int* nbytes, void* additional);
extern int secure_io_set(secure_io sio, secure_io_id id, const unsigned char* data, unsigned int size, void* additional);

#ifdef __cplusplus
}
#endif

#endif
