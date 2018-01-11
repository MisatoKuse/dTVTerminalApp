#ifndef CIPHER_FILE_UUID_H
#define CIPHER_FILE_UUID_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

#define CIPHER_FILE_UUID_SIZE 16

#define cipher_file_define_uuids \
    cipher_file_define_uuid(LOCAL_MODE, 0x131dc1ce, 0xa0ad, 0x4589, 0x8833, 0xe81cc829dc73ULL)

#define cipher_file_define_uuid(name, time_low, time_mid, time_high_and_version, clock_seq, node) \
extern const du_uchar CIPHER_FILE_UUID_ ## name [];
    //cipher_file_define_uuid(AAA, 0xd9418b25, 0x2de5, 0x451f, 0x8dd9, 0x2a25373dfbf5)
cipher_file_define_uuids
#undef cipher_file_define_uuid

#define cipher_file_unpack_2(a) \
    (du_uint8)((a) >> 8), (du_uint8)(a)

#define cipher_file_unpack_4(a) \
    cipher_file_unpack_2((du_uint16)((a) >> 16)), \
    cipher_file_unpack_2((du_uint16)(a))

#define cipher_file_unpack_6(a) \
    cipher_file_unpack_2((du_uint16)((a) >> 32)), \
    cipher_file_unpack_2((du_uint16)((a) >> 16)), \
    cipher_file_unpack_2((du_uint16)(a))

#define cipher_file_define_uuid(name, time_low, time_mid, time_high_and_version, clock_seq, node) \
const du_uchar CIPHER_FILE_UUID_ ## name [] = { \
    cipher_file_unpack_4(time_low), \
    cipher_file_unpack_2(time_mid), \
    cipher_file_unpack_2(time_high_and_version), \
    cipher_file_unpack_2(clock_seq), \
    cipher_file_unpack_6(node) \
};

#ifdef __cplusplus
}
#endif

#endif
