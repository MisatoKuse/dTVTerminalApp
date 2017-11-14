/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_poll interface provides polling methods for receiving
 *   socket I/O events.
 *   Before using a du_poll, use <b>du_poll_create</b> to initialize
 *   du_poll data, and use du_poll_set to store
 *   socket handle polling for I/O events.
 *   Use <b>du_poll_free</b> to free the du_poll data area.
 */

#ifndef DU_POLL_H
#define DU_POLL_H

#include "du_socket_os.h"
#include "du_poll_os.h"
#include "du_pollinfo_array.h"
#include "du_time_os.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Enumeration of the poll result.
 */
typedef enum {
    /**
     *   This value is Never used.
     */
    DU_POLL_FLAG_NONE = 0,

    /**
     *   Readable.
     */
    DU_POLL_FLAG_READ = 1,

    /**
     *   Writable.
     */
    DU_POLL_FLAG_WRITE = 2,

    /**
     *   Error occurred.
     */
    DU_POLL_FLAG_ERROR = 4,

    /**
     *   Remote peer disconnected the connection.
     *   (Requires Linux 2.6.17 or later)
     *
     *   When polling a receiver socket, this flag will be turned on whenever
     *   the FIN packet is received even if there is unread data in the socket buffer.
     *   Please check the buffer before handling the disconnection with the peer.
     */
    DU_POLL_FLAG_RDHUP = 8
} du_poll_flag;

/**
 *  Creates a du_poll data structure.
 *  @param[out] p  pointer to the du_poll data structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_poll_create(du_poll* p);

/**
 *  Resets data to reuse it.
 *  @param[out] p  pointer to the du_poll data structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern void du_poll_reset(du_poll* p);

/**
 *  Frees resources used by p.
 *  @param[in,out] p pointer to the du_poll structure.
 */
extern void du_poll_free(du_poll* p);

/**
 *  Sets a socket handle with flags specifying the type of events
 *  the application is interested in.
 *  @param[in,out] p pointer to the du_poll structure.
 *  @param[in] s socket handle to store in the p.
 *  @param[in] flags specifying the bitmask of the type events the application is interested in.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_poll_set(du_poll* p, du_socket s, du_poll_flag flags);

/**
 *  Gets the status of the specified socket handle.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] s specified socket handle to get status.
 *  @param[out] flags to the variable to receive the status of s
 *    stored in p. The status value is a bitmask.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_poll_get_status(du_poll* p, du_socket s, du_poll_flag* flags);

/**
 *  Removes a socket handle.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] s specified socket handle to remove.
 *  @return  true if the data of specified socket handle is removed successfully
 *  or the specified socket handle is not stored in p.
 *           false if the function fails.
 */
extern du_bool du_poll_remove(du_poll* p, du_socket s);

/**
 *  Waits for events on socket handles.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] timeout_ms the length of time in milliseconds which
 *    the system will wait for events before returning.
 *    A negative value means infinite timeout.
 *  @param[out] num_events pointer to the variable to receive
 *    the number of socket handles in p with events or errors reported.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_poll_check(du_poll* p, du_int32 timeout_ms, du_uint32* num_events);

/**
 *  Cancels waiting for events.
 *  @param[in] p pointer to the du_poll structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_poll_cancel(du_poll* p);

/**
 *  Waits for read event.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] s specified socket handle to wait.
 *  @param[in] timeout_ms the length of time in milliseconds which
 *    the system will wait for events before returning.
 *    A negative value means infinite timeout.
 *  @return  true if, and only if, the specified socket handle's status
 *    is DU_POLL_FLAG_READ ( there is data to read ).
*/
extern du_bool du_poll_wait_read(du_poll* p, du_socket s, du_int32 timeout_ms);

/**
 *  Waits for read event until the specified time.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] s specified socket handle to wait.
 *  @param[in] limit_ms pointer to an absolute time to wait.
 *  @return  true if, and only if, the specified socket handle's status
 *    is DU_POLL_FLAG_READ ( there is data to read ).
 *  @remarks The du_timel limit_ms is represented as milliseconds
 *   elapsed since midnight (00:00:00), January 1, 1970.
*/
extern du_bool du_poll_wait_read_abs(du_poll* p, du_socket s, du_timel* limit_ms);

/**
 *  Waits for write event.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] s specified socket handle to wait.
 *  @param[in] timeout_ms the length of time in milliseconds which
 *    the system will wait for events before returning.
 *    A negative value means infinite timeout.
 *  @return  true if, and only if, the specified socket handle's status
 *    is DU_POLL_FLAG_WRITE ( ready for output ).
*/
extern du_bool du_poll_wait_write(du_poll* p, du_socket s, du_int32 timeout_ms);

/**
 *  Waits for write event until the specified time.
 *  until the time specified limit_ms.
 *  @param[in] p pointer to the du_poll structure.
 *  @param[in] s specified socket handle to wait.
 *  @param[in] limit_ms pointer to an absolute time to wait.
 *  @return  true if, and only if, the specified socket handle's status
 *    is DU_POLL_FLAG_WRITE ( ready for output ).
 *  @remarks The du_timel limit_ms is represented as milliseconds
 *   elapsed since midnight (00:00:00), January 1, 1970.
*/
extern du_bool du_poll_wait_write_abs(du_poll* p, du_socket s, du_timel* limit_ms);

/**
 *  Checks if waiting process has canceled.
 *  @param[in] p pointer to the du_poll structure.
 *  @return  true if p was canceled, otherwise false.
*/
extern du_bool du_poll_is_canceled(du_poll* p);

/**
 *  Checks if waiting process timed out.
 *  @param[in] p pointer to the du_poll structure.
 *  @return  true if p was timed out, otherwise false.
*/
extern du_bool du_poll_is_timed_out(du_poll* p);

#ifdef __cplusplus
}
#endif

#endif
