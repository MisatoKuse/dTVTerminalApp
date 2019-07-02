/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp.h
 *  @brief The dupnp interface provides various methods of UPnP networking
 *  (such as starting/stopping UPnP network communication, sending a request message,
 *  receiving a response message, setting/getting a port number etc.)
 */

#ifndef DUPNP_H
#define DUPNP_H

#include <du_socket.h>
#include <du_type.h>
#include <du_ip.h>
#include <du_str_array.h>
#include <du_netif_array.h>

#include <dupnp_netif.h>

#ifdef __cplusplus
extern "C" {
#endif

struct dupnp_taskmgr;
struct dupnp_dvc_service_info;
struct dupnp_dvc_service_common_request;

/**
 * Enumeration of the flags about IP version
 */
typedef enum dupnp_ip_version_flag {
    DUPNP_IP_VERSION_FLAG_UNKNOWN   = 0x00,   /**< Unknown */
    DUPNP_IP_VERSION_FLAG_V4        = 0x01,   /**< IPv4 */
    DUPNP_IP_VERSION_FLAG_V6        = 0x10,   /**< IPv6 */
} dupnp_ip_version_flag;

/**
 * Definition for invalid ID in dupnp.
 */
#define DUPNP_INVALID_ID DU_ID32_INVALID

/**
 * This structure contains the headers in a message sent from UPnP Devices
 * or Control Point.
 * The contents of <b>header</b> in this structure depend on the message type.
 */
typedef struct dupnp_ssdp_info {
    const du_ip* ip;             //!< address of sender that submits the message.
    const du_str_array* header;  //!< string array of the headers in the message sent.
} dupnp_ssdp_info;

/**
 * This structure contains the information of an HTTP response data.
 */
typedef struct dupnp_http_response {
    du_uint32 id;                //!< an id of connection
    const du_uchar* method;      //!< the method of the request message.
    const du_uchar* url;         //!< the uri the request message was sent to.
    du_socket_error error;       //!< socket error code. <b>status</b>, <b>url</b>, <b>header</b>, and <b>body</b> are set when <b>error</b> is DU_SOCKET_ERROR_NONE.
    du_uchar* status;            //!< HTTP status code of the response message.
    const du_str_array* header;  //!< string array of the HTTP headers in the response message.
    const du_uchar* body;        //!< string array of the HTTP body data in the response message.
    du_uint32 body_size;         //!< byte length of the HTTP body data in the response message.
} dupnp_http_response;

/**
 * This structure contains the information of UPnP stack.
 */
typedef struct dupnp {
    void* _impl;
} dupnp;

typedef struct dupnp_http_server_request_event {
    struct dupnp_dvc_service_info* si;
    struct dupnp_dvc_service_common_request* request;
    void* arg;
} dupnp_http_server_request_event;

/**
 * An interface definition of a handler.
 * <b>dupnp_http_response_handler</b> function is an application-defined
 *   callback function that processes a HTTP response data.
 * @param[in] response pointer to the dupnp_http_response structure.
 * @param[in] arg a parameter for the handler function.
 * @remark <em>response</em> is temporary data used inside of this handler,
 * and the created data in <em>response</em> are freed when this handler returns.
 */
typedef void (*dupnp_http_response_handler)(dupnp_http_response* response, void* arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_access_control_handler</b> function is an application-defined
 *   callback function invoked when the http request is recieved.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] info pointer to the service information structure.
 * @param[in] request pointer to dupnp_dvc_service_common_request structure.
 * @param[in] arg a parameter for the handler function.
 * @return  true if you would allow the http request.
 *          false otherwise.
 * @remark  If you return false, it response with 403 (forbidden) http status code.
 */
typedef du_bool (*dupnp_access_control_handler)(dupnp* upnp, struct dupnp_dvc_service_info* info, struct dupnp_dvc_service_common_request* request, void* arg);

typedef void (*dupnp_http_server_request_event_listener)(dupnp_http_server_request_event* event);

/**
 * Creates and initializes <em>upnp</em>.
 * @param[out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] stack_size the size of the stack used in dupnp in byte.
 * @param[in] port  port number to assign to SSDP discovery and HTTP server socket.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark if <em>stack_size</em> equals zero, uses the default stack size given by OS.
 * If <em>port</em> equals zero, uses the unused port number, greater than 1024 in general,
 *   assigned by OS.
 * If <em>port</em> doesn't equal zero, <em>port</em> should be greater than 1024 and not 1900,
 * because the port is also used for source port of SSDP discovery.
 * @remark If port element is defined in Device Configuration File, the element value overrides the <em>port</em> value of this function.
 */
extern du_bool dupnp_init(dupnp* upnp, du_uint32 stack_size, du_uint16 port);

/**
 * Frees the region used by <em>upnp</em>.
 * @param[in,out] upnp pointer to the <em>dupnp</em> structure.
 */
extern void dupnp_free(dupnp* upnp);

/**
 * Registers a <b>dupnp_netif_change_handler</b> function in <em>upnp</em>.
 * A <b>dupnp_netif_change_handler</b> function is an application-defined
 *   callback function called when the addresses associated with the adapters on the local computer
 *   is changed or the network interface adapters on the local computer
 *   is added or removed.
 * @param[out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function called when network interface is changed.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You can registers more than one handler function in
 * in <em>upnp</em> by calling this function several times. You must not call this function after
 * dupnp was started.
 * @see <b>dupnp_enable_netif_monitor</b>
 */
extern du_bool dupnp_set_netif_change_handler(dupnp* upnp, dupnp_netif_change_handler handler, void* arg);

/**
 * Removes a <em>handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_remove_netif_change_handler(dupnp* upnp, dupnp_netif_change_handler handler);

/**
 * Removes a <em>handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_remove_netif_change_handler2(dupnp* upnp, dupnp_netif_change_handler handler, void* arg);

/**
 * Enables or disables monitor of the change of the network interface.
 * When monitor is enabled, network interface is monitored periodically,
 * and if the <b>dupnp_netif_change_handler</b> function is registered as a handler,
 * the handler is called when the addresses associated with the adapters on the local computer
 *   is changed or the network interface adapters on the local computer
 *   is added or removed.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if the monitor is to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark default status of this monitor is set to disabled.
 * @see <b>dupnp_set_netif_change_handler</b>
 */
extern du_bool dupnp_enable_netif_monitor(dupnp* upnp, du_bool flag);

/**
 * Changes the monitor state of the network interface forcibly.
 *
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_change_netif_monitor_state_force(dupnp* upnp);

/**
 * Sets a callback function which selects local network interface to be used.
 * User can select items of a list which dupnp_get_netif_array() returns.
 * This callback is called by each interface found.
 *
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] filter a callback function implements <em>du_netif_list_filter</em> interface.
 * @param[in] arg a pointer to user data which is passed to the <em>filter</em>.
 * @remark This callback affects to results of all du_netif_get_list() calls in an application process.
 */
extern du_bool dupnp_set_netif_list_filter(dupnp* upnp, du_netif_list_filter filter, void* arg);

/**
 * Removes the callback function which is set by dupnp_set_netif_list_filter().
 *
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 */
extern void dupnp_remove_netif_list_filter(dupnp* upnp);

/**
 * Sets the maximum size for an HTTP body data.
 * The default size is 500KB.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] max_body_size maximum byte size for an HTTP body data.
 */
extern void dupnp_http_set_max_body_size(dupnp* upnp, du_uint32 max_body_size);

/**
 * Starts monitoring when monitor is enabled, starts HTTP server when HTTP server is enabled,
 * opens the multicast channel and port for SSDP listener when SSDP listener is enabled,
 * and open the UDP socket for SSDP discovery when SSDP discovery is enabled.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Default setting of the monitor, HTTP server, SSDP listener,
 * and SSDP discovery are all set to disabled. The method enables these functions
 * are <b>dupnp_enable_netif_monitor</b>, <b>dupnp_cp_enable_ssdp_listener</b>,
 * <b>dupnp_cp_enable_ssdp_search</b>, <b>dupnp_cp_enable_http_server</b>.
 * To stop all functions dupnp_start() starts, call <b>dupnp_stop()</b>.
 * For every call to dupnp_start(), there must be a call to dupnp_stop().
 * dupnp_start() and dupnp_stop() pair can be used multiple times in an application.<br>
 * This function creates a new thread for executing the dupnp functions.
 * @see <b>dupnp_enable_netif_monitor</b>, <b>dupnp_cp_enable_ssdp_listener</b>,
 * <b>dupnp_cp_enable_ssdp_search</b>, <b>dupnp_cp_enable_http_server</b>.
 *
 */
extern du_bool dupnp_start(dupnp* upnp);

/**
 * Starts monitoring when monitor is enabled, starts HTTP server when HTTP server is enabled,
 * opens the multicast channel and port for SSDP listener when SSDP listener is enabled,
 * and open the UDP socket for SSDP discovery when SSDP discovery is enabled.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Default setting of the monitor, HTTP server, SSDP listener,
 * and SSDP discovery are all set to disabled. The method enables these functions
 * are <b>dupnp_enable_netif_monitor</b>, <b>dupnp_cp_enable_ssdp_listener</b>,
 * <b>dupnp_cp_enable_ssdp_search</b>, <b>dupnp_cp_enable_http_server</b>.
 * To stop all functions dupnp_start_nothread() starts, call <b>dupnp_stop()</b>.
 * For every call to dupnp_start_nothread(), there must be a call to dupnp_stop().
 * dupnp_start_nothread() and dupnp_stop() pair can be used multiple times in an application.
 * This function does not create a thread for executing the dupnp functions.
 * You must call dupnp_taskmgr_process() periodically to execute the internal processes of
 * UPnP SDK .
 * @see dupnp_enable_netif_monitor(), dupnp_cp_enable_ssdp_listener(),
 * dupnp_cp_enable_ssdp_search(), dupnp_cp_enable_http_server().
 * dupnp_taskmgr.h
 */
extern du_bool dupnp_start_nothread(dupnp* upnp);

/**
 * Closes the multicast channel and port for SSDP and stops HTTP server and monitoring.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern void dupnp_stop(dupnp* upnp);

/**
 * Checks whether dupnp is running or not.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if dupnp is running, false otherwise.
 * @remark After dupnp_start() returned successfully(i.e. dupnp was started),
 * dupnp state is running state and dupnp_is_running() returns true.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_is_running(dupnp* upnp);

/**
 * Gets a port number assigned to HTTP server socket and stores it in <em>port</em>.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[out] port port number assigned to HTTP server socket.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern void dupnp_get_http_port(dupnp* upnp, du_uint16* port);

/**
 * Gets a HTTP server header value on this system.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  pointer to the server value string.
 * @remark Server is a concatenation of OS name, OS version, UPnP/1.0,
 *    product name, and product version.
 * @remark This function is multithread safe.
 */
extern const du_uchar* dupnp_get_server_header_value(dupnp* upnp);

/**
 * Sets a HTTP server header value on this system.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] value  value of server in http header to set.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Note that change with incorrect value occurs violation of UPnP Device Architecture 1.0.
 * Format of value must be "OS/version UPnP/1.0 product/version" specified by UPnP Device Architecture 1.0.
 * @remark This function is multithread safe if and only if calling this before calling dupnp_start().
 * @remark This function must be called after calling dupnp_init().
 * @remark This function should be called before calling dupnp_start().
 * @see UPnP Device Architecture 1.0.
 */
extern du_bool dupnp_set_server_header_value(dupnp* upnp, const du_uchar* value);

/**
 * Gets the enabled network interface information and stores it in <em>array</em>.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[out] array pointer to the <em>du_netif_array</em> to receive the
 *   network interface information.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>array</em> is a pointer to a <em>du_netif_array</em> data structure
 *  initialized by the <b>du_netif_array_init</b> function.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_get_netif_array(dupnp* upnp, du_netif_array* array);

/**
 * Gets pointer to dupnp_taskmgr structure.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  pointer to the dupnp_taskmgr structure stored in <em>upnp</em>.
 * @remark Using the pointer to dupnp_taskmgr, you can call the the task APIs of dupnp.
 * @remark This function is multithread safe.
 * @see dupnp_taskmgr.h
 */
extern struct dupnp_taskmgr* dupnp_get_taskmgr(dupnp* upnp);

/**
 * Cancels the connection specified <em>id</em>
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] id  an ID of a connection.
 * @remark <b>dupnp_http_response_handler</b> function will be called
 * with dupnp_http_response.error = DU_SOCKET_ERROR_CANCELD
 * after <b>dupnp_http_cancel</b> is called.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_cancel(dupnp* upnp, du_uint32 id);

/**
 * Issues an any HTTP request to the <em>url</em> with HTTP header, <em>header</em>,
 * and HTTP body, <em>request_xml</em>. Passes the response message
 * to the <em>handler</em> callback function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] method a method of the HTTP request.
 * @param[in] url a URL for request.
 * @param[in] header string array of HTTP headers to send. NULL if you don't want to use specific header fields.
 * @param[in] request_xml string of HTTP body data. NULL if you don't want to use a specific body.
 * @param[in] request_xml_size size of HTTP body data. 0 if request_xml is NULL.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] handler application-defined callback function that processes messages sent from
 *   device.
 * @param[in] arg a parameter for <em>handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  If error ocuured, the error code is stored into the dupnp_http_response.error variable
 * in the argument of <em>handler</em>.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_request(dupnp* upnp, const du_uchar* method, const du_uchar* url, const du_str_array* header, const du_uchar* request_xml, du_uint32 request_xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

/**
 * Issues an any HTTP request to the <em>url</em> with HTTP header, <em>header</em>,
 * and HTTP body, <em>request_xml</em>. Passes the response message
 * to the <em>handler</em> callback function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] method a method of the HTTP request.
 * @param[in] url a URL for request.
 * @param[in] proxy_host a host name (IP address) of proxy.
 * @param[in] proxy_port a port number of proxy.
 * @param[in] header string array of HTTP headers to send. NULL if you don't want to use specific header fields.
 * @param[in] request_xml string of HTTP body data. NULL if you don't want to use a specific body.
 * @param[in] request_xml_size size of HTTP body data. 0 if request_xml is NULL.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] handler application-defined callback function that processes messages sent from
 *   device.
 * @param[in] arg a parameter for <em>handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  If error ocuured, the error code is stored into the dupnp_http_response.error variable
 * in the argument of <em>handler</em>.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_request2(dupnp* upnp, const du_uchar* method, const du_uchar* url, const du_uchar* proxy_host, du_uint16 proxy_port, const du_str_array* header, const du_uchar* request_xml, du_uint32 request_xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

/**
 * Issues an HTTP GET request to the <em>url</em> with <em>header</em> lines
 * and passes HTTP response message to the <em>handler</em> callback function.
 * This function is useful for getting the UPnP device description information.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] url a URL for request.
 * @param[in] header string array of HTTP headers to send. NULL if you don't want to use specific header fields.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] handler application-defined callback function that processes messages sent from
 *   device.
 * @param[in] arg a parameter for <em>handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  If error ocuured, the error code is stored into the dupnp_http_response.error variable
 * in the argument of <em>handler</em>.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_get(dupnp* upnp, const du_uchar* url, const du_str_array* header, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

/**
 * Issues an HTTP GET request to the <em>url</em> with <em>header</em> lines
 * and passes HTTP response message to the <em>handler</em> callback function.
 * This function is useful for getting the UPnP device description information.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] url a URL for request.
 * @param[in] proxy_host a host name (IP address) of proxy.
 * @param[in] proxy_port a port number of proxy.
 * @param[in] header string array of HTTP headers to send. NULL if you don't want to use specific header fields.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] handler application-defined callback function that processes messages sent from
 *   device.
 * @param[in] arg a parameter for <em>handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  If error ocuured, the error code is stored into the dupnp_http_response.error variable
 * in the argument of <em>handler</em>.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_get2(dupnp* upnp, const du_uchar* url, const du_uchar* proxy_host, du_uint16 proxy_port, const du_str_array* header, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

/**
 * Issues an HTTP request defined by SOAP to the <em>url</em> with HTTP header, <em>header</em>,
 * and HTTP body, <em>request_xml</em>. Passes the response message
 * to the <em>handler</em> callback function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] url a URL for request.
 * @param[in] header string array of HTTP headers to send.
 * @param[in] request_xml string of HTTP body data.
 * @param[in] request_xml_size size of HTTP body data.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] handler application-defined callback function that processes messages sent from
 *   device.
 * @param[in] arg a parameter for <em>handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  If error ocuured, the error code is stored into the dupnp_http_response.error variable
 * in the argument of <em>handler</em>.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_soap(dupnp* upnp, const du_uchar* url, const du_str_array* header, const du_uchar* request_xml, du_uint32 request_xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

/**
 * Issues an HTTP request defined by SOAP to the <em>url</em> with HTTP header, <em>header</em>,
 * and HTTP body, <em>request_xml</em>. Passes the response message
 * to the <em>handler</em> callback function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] url a URL for request.
 * @param[in] proxy_host a host name (IP address) of proxy.
 * @param[in] proxy_port a port number of proxy.
 * @param[in] header string array of HTTP headers to send.
 * @param[in] request_xml string of HTTP body data.
 * @param[in] request_xml_size size of HTTP body data.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] handler application-defined callback function that processes messages sent from
 *   device.
 * @param[in] arg a parameter for <em>handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  If error ocuured, the error code is stored into the dupnp_http_response.error variable
 * in the argument of <em>handler</em>.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_http_soap2(dupnp* upnp, const du_uchar* url, const du_uchar* proxy_host, du_uint16 proxy_port, const du_str_array* header, const du_uchar* request_xml, du_uint32 request_xml_size, du_int32 timeout_ms, dupnp_http_response_handler handler, void* arg, du_uint32* id);

/**
 * Registers a <b>dupnp_access_control_handler</b> function in <em>upnp</em>.
 * The <b>dupnp_access_control_handler</b> function is an application-defined
 *   callback function invoked when when the http request is recieved.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the <b>dupnp_access_control_handler</b> function to store in <em>upnp</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the dupnp_init() function. You can register more than one handler function
 * in <em>upnp</em> by calling this function several times.
 * If registered <b>dupnp_access_control_handler</b> function
 * returns false, the device information is not registered in <em>upnp</em>.
 */
extern du_bool dupnp_set_access_control_handler(dupnp* upnp, dupnp_access_control_handler handler, void* arg);

/**
 * Removes a <b>dupnp_access_control_handler</b> function from <em>upnp</em>.
 * The <b>dupnp_access_control_handler</b> function is an application-defined
 *   callback function invoked when a information of a new UPnP device's description
 *   sent from UPnP Device is received.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the <b>dupnp_access_control_handler</b> function to store in <em>upnp</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 *  the dupnp_init() function.
 */
extern du_bool dupnp_remove_access_control_handler(dupnp* upnp, dupnp_access_control_handler handler, void* arg);

/**
 * Registers a <b>dupnp_http_server_request_event_listener</b> function in <em>upnp</em>.
 * The <b>dupnp_http_server_request_event_listener</b> function is an application-defined
 *   callback function invoked when when the http request is recieved.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] event_listener the <b>dupnp_http_server_request_event_listener</b> function to store in <em>upnp</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_set_http_server_request_event_listener(dupnp* upnp, dupnp_http_server_request_event_listener event_listener, void* arg);

/**
 * Removes a <b>dupnp_http_server_request_event_listener</b> function from <em>upnp</em>.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] event_listener the <b>dupnp_http_server_request_event_listener</b> function to store in <em>upnp</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_remove_http_server_request_event_listener(dupnp* upnp, dupnp_http_server_request_event_listener event_listener, void* arg);

/**
 * Enables IPv6.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] flag ip modes to enable.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark You need to call this function before calling dupnp_start().
 */
extern du_bool dupnp_set_ip_version(dupnp* upnp, dupnp_ip_version_flag flag);

/**
 * Gets information about enabled IP mode.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[out] flag enabled ip modes.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_get_ip_version(dupnp* upnp, dupnp_ip_version_flag* flag);

#ifdef __cplusplus
}
#endif

#endif
