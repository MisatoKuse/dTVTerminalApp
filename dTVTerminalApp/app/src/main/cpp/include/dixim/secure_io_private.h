#ifndef SECURE_IO_PRIVATE_H
#define SECURE_IO_PRIVATE_H

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool secure_io_is_additional_key_file_enabled();
extern void secure_io_enable_additional_key_file(du_bool enable);

#ifdef __cplusplus
}
#endif

#endif
