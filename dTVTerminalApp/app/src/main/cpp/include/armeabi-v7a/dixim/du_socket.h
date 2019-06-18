/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_socket interface provides various methods for networking application
 *  based on the socket interface ( such as socket, bind, listen, accept, send, receive ).
 */

#ifndef DU_SOCKET_H
#define DU_SOCKET_H

#include <du_ip_os.h>
#include <du_type.h>
#include <du_socket_os.h>
#include <du_file_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Enumeration of socket errors.
 */
typedef enum {
    /**
     *   No error occurred.
     */
    DU_SOCKET_ERROR_NONE,

    /**
     *   General error.
     */
    DU_SOCKET_ERROR,

    /**
     *   No buffer space available or too many open sockets.
     */
    DU_SOCKET_ERROR_NO_RESOURCE,

    /**
     *   Connection refused.
     */
    DU_SOCKET_ERROR_CONNECTION_REFUSED,

    /**
     *   Connection timed out.
     */
    DU_SOCKET_ERROR_CONNECTION_TIMED_OUT,

    /**
     *   Network is unreachable or no route to host.
     */
    DU_SOCKET_ERROR_NETWORK_UNREACHABLE,

    /**
     *   Address already in use.
     */
    DU_SOCKET_ERROR_ADDRESS_ALREADY_IN_USE,

    /**
     *   Cannot be completed immediately.
     */
    DU_SOCKET_ERROR_WOULD_BLOCK,

    /**
     *   Read timed out.
     */
    DU_SOCKET_ERROR_READ_TIMED_OUT,

    /**
     *   Write timed out.
     */
    DU_SOCKET_ERROR_WRITE_TIMED_OUT,

    /**
     *   Canceled.
     */
    DU_SOCKET_ERROR_CANCELED,

} du_socket_error;

/**
 *  Enumeration of event types.
 */
typedef enum du_socket_event {
    /**
     *  Unknown event.
     */
    DU_SOCKET_EVENT_UNKNOWN,

    /**
     *  Event before calling connect.
     */
    DU_SOCKET_EVENT_PRE_CONNECT,

    /**
     *  Event after calling accept.
     */
    DU_SOCKET_EVENT_POST_ACCEPT,

    /**
     *  Event after calling recvfrom.
     */
    DU_SOCKET_EVENT_POST_RECV_FROM
} du_socket_event;

/**
 *  Event handler.
 */
typedef void (*du_socket_event_handler)(du_socket s, du_socket_event event, du_ip* ip, void* arg);

/**
 *  Gets the error status of the last operation.
 *  @return  the error status for the last operation that failed.
*/
extern du_socket_error du_socket_get_last_error(void);

/**
 *  Creates a TCP socket with no-delay option (enables the nonblocking mode of socket)
 *  and IPv6-only option (sending and receiving IPv6 packets only).
 *  @param[out] s  pointer to the du_socket data referencing the new socket.
 *  @param[in] ip pointer to the du_ip structure data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark Before using du_socket_tcp, set the address family of the IP address in the ip.
 *  @see    du_ip interface.
 */
extern du_bool du_socket_tcp(du_socket* s, const du_ip* ip);

/**
 *  Creates a UDP socket with no-delay option (enables the nonblocking mode of socket)
 *  and IPv6-only option (sending and receiving IPv6 packets only).
 *  @param[out] s  pointer to the du_socket data referencing the new socket.
 *  @param[in] ip pointer to the du_ip structure data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark Before using du_socket_udp, set the address family of the IP address in the ip.
 *  @see    du_ip interface.
 */
extern du_bool du_socket_udp(du_socket* s, const du_ip* ip);

/**
 *  Closes an existing socket if it opened.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark  If s equals DU_SOCKET_INVALID, returns immediately with true.
 */
extern du_bool du_socket_close(du_socket s);

/**
 *  Creates a connection to a specified foreign host using ip.
 *  If the socket is UDP then the ip address is
 *   to which datagrams are sent by default, and the only address from which
 *  datagrams are received. If the socket is TCP, this call
 *  attempts to make a connection to another socket specified by ip.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @param[in] ip pointer to the du_ip structure data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark With a nonblocking socket, the connection attempt cannot be completed immediately.
 *  In this case, this function will return false.
 *  It is possible to use du_poll_wait_write to determine whether connect
 *  completed successfully or unsuccessfully. Until the connection attempt
 *  completes on a nonblocking socket, all subsequent calls to connect on the
 *  same socket will fail.
 *  @see du_poll_wait_write
 */
extern du_bool du_socket_connect(du_socket s, const du_ip* ip);

/**
 *  Binds a socket to a local address.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @param[in] ip pointer to the du_ip structure data stored local address.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_bind(du_socket s, const du_ip* ip);

/**
 *  Binds a socket to a local address with shared mode.
 *  You can bind sockets on the same address.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @param[in] ip pointer to the du_ip structure data stored local address.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_bind_reuse(du_socket s, const du_ip* ip);

/**
 *  Listens for connections made to the socket.
 *  The backlog specifies the maximum number of queued connections.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @param[in] backlog the maximum length of the queue of pending connections.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_listen(du_socket s, du_uint32 backlog);

/**
 *  Accepts a connection on a socket.
 *  This function extracts the first connection request on the queue of
 *  pending connections, creates a new connected socket, and returns a
 *  new du_socket for the socket.
 *  @param[in] s  the du_socket data referencing the socket
 *     that has been placed in a listening state.
 *  @param[out] c pointer to the du_socket data referencing the new socket.
 *  @param[out] ip pointer to the du_ip to receive the address
 *    bound to the socket on the other end of the connection.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  remark The s socket must be bound to an address and listening for connections.
 */
extern du_bool du_socket_accept(du_socket s, du_socket* c, du_ip* ip);

/**
 *  Receives a datagram packet.
 *  @param[in] s the du_socket data referencing the a bound socket.
 *  @param[out] buf buffer for the incoming data.
 *  @param[in] len length of the buf parameter.
 *  @param[out] ip pointer to a du_ip that will receive the source address.
 *    ip must be non-null value.
 *  @param[out] nbytes the number of bytes received.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark This API fails if the ip is null.
 */
extern du_bool du_socket_recv_from(du_socket s, du_uint8* buf, du_uint32 len, du_ip* ip, du_uint32* nbytes);

/**
 *  Receives data from a connected socket.
 *  @param[in] s the du_socket data referencing the a connected socket.
 *  @param[out] buf buffer for the incoming data.
 *  @param[in] len length of the buf parameter.
 *  @param[out] nbytes the number of bytes received.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_recv(du_socket s, du_uint8* buf, du_uint32 len, du_uint32* nbytes);

/**
 *  Sends data to a specific destination.
 *  @param[in] s  the du_socket data referencing the socket
 *  @param[in] buf containing the data to be transmitted.
 *  @param[in] len length of the data in the buf parameter.
 *  @param[in] ip pointer to a du_ip stored the address of the target.
 *  @param[out] nbytes the total number of bytes sent.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_send_to(du_socket s, const du_uint8* buf, du_uint32 len, const du_ip* ip, du_uint32* nbytes);

/**
 *  Sends data to a connected socket.
 *  @param[in] s the du_socket data referencing the a connected socket.
 *  @param[in] buf containing the data to be transmitted.
 *  @param[in] len length of the data in the buf parameter.
 *  @param[out] nbytes the total number of bytes sent.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_send(du_socket s, const du_uint8* buf, du_uint32 len, du_uint32* nbytes);

/**
 *  Sends file data over a connected socket.
 *  @param[in] s the du_socket data referencing the a connected socket.
 *  @param[in] f the file handle to the open file that this function sends.
 *  @param[in] offset du_int64 data holding the input file pointer position from which
 *      this function will start reading data.
 *  @param[in] size total number of bytes to send.
 *  @param[out] nbytes the total number of bytes sent.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_send_file(du_socket s, du_file f, du_uint64 offset, du_uint64 size, du_uint64* nbytes);

/**
 *  Sets the maximum receive buffer size.
 *  @param[in] s  the du_socket data referencing the socket
 *  @param[in] size buffer size to set.
 */
extern void du_socket_try_reserve_in(du_socket s, du_uint32 size);

/**
 *  Sets the maximum send buffer size.
 *  @param[in] s  the du_socket data referencing the socket
 *  @param[in] size buffer size to set.
 */
extern void du_socket_try_reserve_out(du_socket s, du_uint32 size);

/**
 *  Shutdowns the connection.
 *  @param[in] s  the du_socket data referencing the socket
 *  @param[in] how flag that describes what types of operation
 *       will no longer be allowed.
 *       If how is 0, further receives will be disallowed.
 *       If how is 1, further sends will be disallowed.
 *       If how is 2, further sends and receives will be disallowed.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_shutdown(du_socket s, du_uint32 how);

/**
 *  Enables the nonblocking mode of socket s.
 *  @param[in] s  the du_socket data referencing the socket
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_ndelay_on(du_socket s);

/**
 *  Disables the nonblocking mode of socket s.
 *  @param[in] s  the du_socket data referencing the socket
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_ndelay_off(du_socket s);

/**
 *  Gets the local address of a socket.
 *  @param[in] s  the du_socket data referencing the socket
 *  @param[out] ip pointer to a du_ip to receive the local address.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_local(du_socket s, du_ip* ip);

/**
 *  Gets the remote address of the peer to which a socket is connected.
 *  @param[in] s  the du_socket data referencing the socket
 *  @param[out] ip pointer to a du_ip to receive the remote address.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_remote(du_socket s, du_ip* ip);

/**
 *  Enables the transmission of broadcast messages on the socket.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_socket_bcast_on(du_socket s);

/**
 *  Enables restricting IPv6 packets only.
 *
 *  If this flag is set to true, then the socket is restricted to sending and receiving IPv6 packets only.
 *  In this case, an IPv4 and an IPv6 application can bind to a single port at the same time.
 *
 *  @param[in] s  the du_socket data referencing the socket.
 *  @param[in] flag  set true if restricting IPv6 packets only.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark On Linux platforms, this API works since Linux 2.4.21 and 2.6.
 *  @remark This API doesn't work on Windows Modern UI.
 */
extern du_bool du_socket_set_ipv6_only(du_socket s, du_bool flag);

/**
 * Adds the user-defined callback function that is called before/after calling specified system call about socket.
 *
 * @param[in] handler the <b>du_socket_event_handler</b> function to be set.
 * @param[in] arg pointer to the user defined data area. This is passed to the <em>handler</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Don't call the du_socket_* APIs in the <em>handler</em>.
 */
extern du_bool du_socket_add_event_handler(du_socket_event_handler handler, void* handler_arg);

/**
 * Removes the user-defined callback function that is called before/after calling specified system call about socket.
 *
 * @param[in] handler the <b>du_socket_event_handler</b> function to be removed.
 * @param[in] arg pointer to the user defined data area. This is passed to the <em>handler</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool du_socket_del_event_handler(du_socket_event_handler handler, void* handler_arg);

extern du_bool du_socket_init(void);

extern du_bool du_socket_notify_event(du_socket s, du_socket_event event, du_ip* ip);

#ifdef __cplusplus
}
#endif

#endif
