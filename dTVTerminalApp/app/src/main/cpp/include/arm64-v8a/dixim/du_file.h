/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_file interface provides methods for file management
 *   ( such as open, read/write, close, rename, remove ).
 *  @remark Use UTF-8 characters for file or directory name.
 *  @see du_log
 */

#ifndef DU_FILE_H
#define DU_FILE_H

#include <du_type.h>
#include <du_file_os.h>
#include <du_time_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains the file status.
 */
typedef struct du_file_status {
    /**
     *   True if it's a directory.
     */
    du_bool directory;

    /**
     *   Modified time.
     */
    du_time mtime;

    /**
     *   File size
     */
    du_uint64 size;

} du_file_status;

/**
 *  This structure contains the file system status.
 */
typedef struct du_file_system_status {
    /**
     *   Total space in file system.
     */
    du_uint64 total;

    /**
     *   Free space in file system.
     */
    du_uint64 free;

    /**
     *   Used space in file system.
     */
    du_uint64 used;

} du_file_system_status;

/**
 *  Returns a character of path separator.
 *  @return a character of path separator.
 */
extern du_uchar du_file_separator(void);

/**
 *  Returns line-feed data.
 *  @return line-feed data.
 */
extern const du_uchar* du_file_line_feed(void);

/**
 *  Returns a handle for the standard input.
 *  @return  a handle for the standard input.
 */
extern du_file du_file_stdin(void);

/**
 *  Returns a handle for the standard output.
 *  @return  a handle for the standard output.
 */
extern du_file du_file_stdout(void);

/**
 *  Returns a handle for the standard error.
 *  @return  a handle for the standard error.
 */
extern du_file du_file_stderr(void);

/**
 *  Checks whether fn is an absolute or full path name.
 *  @param[in] fn file or directory name.
 *  @return  true if fn is an absolute or full path name.
 *           false if fn is not an absolute or full path name.
 */
extern du_bool du_file_is_absolute_path(const du_uchar* fn);

/**
 *  Checks whether fn is a file.
 *  @param[in] fn file or directory name.
 *  @return  true if fn is a file.
 *           false if fn is not a file.
 */
extern du_bool du_file_is_file(const du_uchar* fn);

/**
 *  Checks whether fn is a directory.
 *  @param[in] fn file or directory name.
 *  @return  true if fn is a directory.
 *           false if fn is not a directory.
 */
extern du_bool du_file_is_dir(const du_uchar* fn);

/**
 *  Gets information about the specified file or directory and stores it in buf.
 *  @param[in] fn file or directory name.
 *  @param[out] buf a buffer that receives the information.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_get_status(const du_uchar* fn, du_file_status* buf);

/**
 *  Gets information about the specified file and stores it in buf.
 *  @param[in] f handle to the file.
 *  @param[out] buf a buffer that receives the information.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_get_fstatus(du_file f, du_file_status* buf);

/**
 *  Sets the modification time of a file.
 *  @param[in] fn file name to set the time.
 *  @param[in] mtime the modification time.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_set_time(const du_uchar* fn, du_time* mtime);

/**
 *  Renames a file, moving it between directories if required.
 *  @param[in] oldfn a string that names an existing file or directory.
 *  @param[in] newfn a string that specifies the new name of oldfn
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark On linux, if oldfn and newfn are not on the same mounted file system,
 *          renaming directory is not supported.
 */
extern du_bool du_file_rename(const du_uchar* oldfn, const du_uchar* newfn);

/**
 *  Renames a file, moving it between directories if required.
 *  This function retries renaming a file untill msec time elapses.
 *  if the operation fails.
 *  @param[in] oldfn a string that names an existing file or directory.
 *  @param[in] newfn a string that specifies the new name of oldfn
 *  @param[in] msec  the number of milliseconds to wait before this function
 *  gives up the renaming.
 *  @return  true if the function succeeds in time.
 *           false if the function fails.
 */
extern du_bool du_file_rename_wait(const du_uchar* oldfn, const du_uchar* newfn, du_uint32 msec);

/**
 *  Deletes an existing file.
 *  @param[in] fn the name string that specifies the file to be deleted.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_remove(const du_uchar* fn);

/**
 *  Opens an existing file for read, gets a handle that can be used to access the file,
 *  and stores the handle in f.
 *  @param[out] f  the file handle that can be used to access the file.
 *  @param[in] fn the name string that specifies the name of the file to open.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_open_read(du_file* f, const du_uchar* fn);

/**
 *  Creates a file for write, gets a handle that can be used to access the file,
 *  and stores the handle in f.
 *  If the file already exists the function will fail.
 *  @param[out] f pointer to the file handle that can be used to access the file.
 *  @param[in] fn the name string that specifies the name of the file to open.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_open_create(du_file* f, const du_uchar* fn);

/**
 *  Opens or creates a file for write, gets a handle that can be used to access the file,
 *  and stores the handle in f.
 *  If the file does not exist it will be created.
 *  If the file already exists it will be truncated.
 *  @param[out] f pointer to the file handle that can be used to access the file.
 *  @param[in] fn the name string that specifies the name of the file to open.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_open_truncate(du_file* f, const du_uchar* fn);

/**
 *  Opens or creates a temporary file for write, gets a handle that can be used to access the file,
 *  and stores the handle in f.
 *  The file may not be indexed by file system.
 *  If the file does not exist it will be created.
 *  If the file already exists it will be truncated.
 *  @param[out] f pointer to the file handle that can be used to access the file.
 *  @param[in] fn the name string that specifies the name of the file to open.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_open_truncate_tmp(du_file* f, const du_uchar* fn);

/**
 *  Opens or creates a file for write, gets a handle that can be used to access the file,
 *  and stores the handle in f.
 *  If the file does not exist it will be created.
 *  @param[out] f pointer to the file handle that can be used to access the file.
 *  @param[in] fn the name string that specifies the name of the file to open.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_open_write(du_file* f, const du_uchar* fn);

/**
 *  Opens or creates a file for read/write, gets a handle that can be used to access the file,
 *  and stores the handle in f.
 *  If the file does not exist it will be created.
 *  @param[out] f pointer to the file handle that can be used to access the file.
 *  @param[in] fn the name string that specifies the name of the file to open.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_open_readwrite(du_file* f, const du_uchar* fn);

/**
 *  Closes an open file handle.
 *  @param[in] f the handle to an open file.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark  If f equals DU_FILE_INVALID, returns immediately with true.
 */
extern du_bool du_file_close(du_file f);

/**
 *  Reads data from a file and stores in buf.
 *  @param[in] f the handle to the file.
 *  The file handle must be opened.
 *  @param[out] buf the buffer that receives the data read from the file.
 *  @param[in] len number of bytes to be read from the file.
 *  @param[out] nbytes pointer to the variable that receives the number of bytes read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_read(du_file f, du_uint8* buf, du_uint32 len, du_uint32* nbytes);

/**
 *  Writes data in buf to a file.
 *  @param[in] f the handle to the file.
 *  @param[in] buf the buffer containing the data to be written to the file.
 *  @param[in] len number of bytes to be written to the file.
 *  @param[out] nbytes pointer to the variable that receives the number of bytes written.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_write(du_file f, const du_uint8* buf, du_uint32 len, du_uint32* nbytes);

/**
 *  Writes data to a file.
 *  This function ensures that all data is written successfully.
 *  @param[in] f the handle to the file.
 *  @param[in] buf the buffer containing the data to be written to the file.
 *  @param[in] len number of bytes to be written to the file.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_write_all(du_file f, const du_uint8* buf, du_uint32 len);

/**
 *  Flushes all modified in-core data of the file referred to by the file descriptor
 *  to the disk device.
 *  @param[in] f handle to an open file.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_flush(du_file f);

/**
 *  Repositions the offset of the open file associated with the file descriptor.
 *  @param[in] f handle to the file whose file pointer is to be moved.
 *  @param[in] pos number of bytes to move the file pointer.
 *  @param[out] new_pos pointer to the variable that receives the new file pointer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_seek_set(du_file f, du_uint64 pos, du_uint64* new_pos);

/**
 *  Gets the offset of the open file associated with the file descriptor.
 *  @param[in] f handle to the file.
 *  @param[out] pos pointer to the variable that receives the current file pointer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_seek_pos(du_file f, du_uint64* pos);

/**
 *  Gets the file suffix(file name extension) from the fn file name.
 *  @param[in] fn the file name string.
 *  @return  pointer to the file suffix string or null if fn has no suffix.
 */
extern const du_uchar* du_file_get_suffix(const du_uchar* fn);

/**
 *  Gets the first line data from the specified file.
 *  @param[in] fn the file name string.
 *  @param[out] buf the buffer that receives the data read from the file.
 *  @param[in] len maximum number of bytes to be read from the file (less than or equal to the
 *    byte length of buf).
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_get_first_line(const du_uchar* fn, du_uint8* buf, du_uint32 len);

/**
 *  Gets file system information that contains the specified directory and stores it in buf.
 *  @param[in] fn directory name.
 *  @param[out] status a buffer that receives the information to retrieve.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_get_system_status(const du_uchar* fn, du_file_system_status* status);

/**
 *  Returns the invalid characters for file name.
 *  @return  pointer to the invalid characters for file name.
 */
extern const du_uchar* du_file_get_invalid_file_name_chars(void);

#ifdef __cplusplus
}
#endif

#endif
