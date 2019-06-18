/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 *
 * $Id: du_file.h 669 2008-10-28 11:40:08Z ohira $
 */

#ifndef CHUNKED_FILE_H
#define CHUNKED_FILE_H

#include <du_type.h>
#include <du_file.h>
#include <du_time.h>
#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum chunked_file_open_mode {
    CHUNKED_FILE_OPEN_MODE_UNKNOWN,
    CHUNKED_FILE_OPEN_MODE_READ,
    CHUNKED_FILE_OPEN_MODE_WRITE,
    CHUNKED_FILE_OPEN_MODE_READWRITE,
    CHUNKED_FILE_OPEN_MODE_CREATE,
    CHUNKED_FILE_OPEN_MODE_TRUNCATE,
} chunked_file_open_mode;

typedef struct chunked_file {
    du_uchar* base_path;
    du_uchar_array path;
    du_str_array path_list;
    du_uint32 base_len;
    chunked_file_open_mode mode;
    du_file f;
    du_uint64 chunk_size;
    du_uint64 pos;
    du_uint32 current_file_number;
} chunked_file;

/**
 * This structure contains the file status.
 */
typedef struct chunked_file_status {
    du_time mtime; /**< Modified time. */
    du_uint64 size; /**< File size */
} chunked_file_status;

extern du_bool chunked_file_is_file(const du_uchar* fn);

extern du_bool chunked_file_get_status(const du_uchar* fn, chunked_file_status* buf);

extern du_bool chunked_file_get_fstatus(chunked_file* f, chunked_file_status* buf);

extern du_bool chunked_file_set_time(const du_uchar* fn, du_time* mtime);

extern du_bool chunked_file_rename(const du_uchar* oldfn, const du_uchar* newfn);

extern du_bool chunked_file_rename_wait(const du_uchar* oldfn, const du_uchar* newfn, du_uint32 msec);

extern du_bool chunked_file_remove(const du_uchar* fn);

extern du_bool chunked_file_open_read(chunked_file* f, const du_uchar* fn, du_uint64 chunk_size);

extern du_bool chunked_file_open_create(chunked_file* f, const du_uchar* fn, du_uint64 chunk_size);

extern du_bool chunked_file_open_truncate(chunked_file* f, const du_uchar* fn, du_uint64 chunk_size);

extern du_bool chunked_file_open_write(chunked_file* f, const du_uchar* fn, du_uint64 chunk_size);

extern du_bool chunked_file_open_readwrite(chunked_file* f, const du_uchar* fn, du_uint64 chunk_size);

extern du_bool chunked_file_close(chunked_file* f);

extern du_bool chunked_file_read(chunked_file* f, du_uint8* buf, du_uint32 len, du_uint32* nbytes);

extern du_bool chunked_file_write(chunked_file* f, const du_uint8* buf, du_uint32 len, du_uint32* nbytes);

extern du_bool chunked_file_write_all(chunked_file* f, const du_uint8* buf, du_uint32 len);

extern du_bool chunked_file_flush(chunked_file* f);

extern du_bool chunked_file_seek_set(chunked_file* f, du_uint64 pos, du_uint64* new_pos);

extern du_bool chunked_file_seek_pos(chunked_file* f, du_uint64* pos);

extern du_bool chunked_file_get_first_line(const du_uchar* fn, du_uint8* buf, du_uint32 len);

#ifdef __cplusplus
}
#endif

#endif
