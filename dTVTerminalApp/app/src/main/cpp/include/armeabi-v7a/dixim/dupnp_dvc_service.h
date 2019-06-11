/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/**
 * @file dupnp_dvc_service.h
 * @brief The dupnp_dvc_service interface provides various methods for
 *  UPnP Device Services.
 *  (such as sending response message, starting/stopping service,
 *   data structures for services, etc.)
 */

#ifndef DUPNP_DVC_SERVICE_H
#define DUPNP_DVC_SERVICE_H

#include <dupnp_http_server.h>
#include <dupnp_dvc_evtmgr.h>
#include <dupnp_dvc_evtmgr_sub_state.h>
#include <dupnp_dvc_context.h>
#include <dupnp_dvcdsc.h>
#include <du_type.h>
#include <du_soapaction.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function.
 * <b>dupnp_dvc_service_query_interface</b> function is an application-defined
 *   callback function that returns the pointer of specified
 * <em>interface_name</em> function.
 * @param[in] interface_name interface name
 * @return  pointer of the function of specified <em>interface_name</em>. null otherwise.
 * @remark The following functions return the interface name used in dupnp_dv modules.
 * @li dupnp_dvc_service_interface_name_init() : return the interface name to initialize the sercice.
 * @li dupnp_dvc_service_interface_name_free() : return the interface name to free resources of the service.
 * @li dupnp_dvc_service_interface_name_start_before() : return the interface name to pre-start the service.
 * @li dupnp_dvc_service_interface_name_start() : return the interface name to start the service.
 * @li dupnp_dvc_service_interface_name_start_after() : return the interface name to post-start the service.
 * @li dupnp_dvc_service_interface_name_stop_before() : return the interface name to pre-stop service.
 * @li dupnp_dvc_service_interface_name_stop() : return the interface name to stop service.
 * @li dupnp_dvc_service_interface_name_stop_after() : return the interface name to post-stop service.
 * @li dupnp_dvc_service_interface_name_upnp_action() : return the interface name of the function invoked when UPnP control:action request message was received.
 * @li dupnp_dvc_service_interface_name_upnp_query() : return the interface name of the function invoked when UPnP control:query request message was received.
 * @li dupnp_dvc_service_interface_name_upnp_accept_subscription() : return the interface name of the function invoked when UPnP Eventing:subscription request message was received.
 */
typedef void* (*dupnp_dvc_service_query_interface)(const du_uchar* interface_name);

/**
 * This structure contains the information of each service to register. This structure is used when service is registered in the device.
 * @see dupnp_dvc_register_service
 */
typedef struct dupnp_dvc_service_interface {
    const du_uchar* service_id;   /**< service identifier */
    dupnp_dvc_service_query_interface query_interface; /**< function pointer. This function is used for getting pointers to functions that provide each services. After registration of the service methods using dupnp_dvc_register_service(), dupnp_dvc call this query_intercace function with necessary interface name and this function returns the function pointer of each service of specified interface name.
@see dupnp_dvc_service_query_interface() */
} dupnp_dvc_service_interface;

/**
 * This structure contains the information of the service and the device.
 */
typedef struct dupnp_dvc_service_info {
    dupnp* upnp;    //!< pointer to the <em>dupnp</em> data structure.
    dupnp_dvcdsc* desc;     //!< pointer to the <em>dupnp_dvcdsc</em> data structure.
    dupnp_dvcdsc_device* device_desc;   //!< pointer to the <em>dupnp_dvcdsc_device</em> data structure.
    dupnp_dvcdsc_service* service_desc; //!< pointer to the <em>dupnp_dvcdsc_service</em> data structure.
    void* user_data; //!< pointer to the user defined data area
} dupnp_dvc_service_info;

/**
 * This structure contains the information of each service.
 */
typedef struct dupnp_dvc_service {
    dupnp_dvc_service_interface service_interface;   /**< <em>dupnp_dvc_service_interface</em> data structure. */

    dupnp_dvc_service_info service_info;   /**< <em>dupnp_dvc_service_info</em> data structure. */

    dupnp_dvc_evtmgr _evtmgr;
} dupnp_dvc_service;

/**
 * This structure contains the information of common service request.
 */
typedef struct dupnp_dvc_service_common_request {
    const du_ip* local_ip;   //!< IP address of local host.
    const du_ip* remote_ip;  //!< IP address of remote host.
    const du_uchar* method;  //!< the method of the request message.
    const du_uri* uri;       //!< the uri the request message has sent to.
    const du_uchar* version; //!< HTTP version.
    const du_str_array* header;  //!< string array of the HTTP headers in the request message.
    const du_uchar* body;  //!< the HTTP body data in the request message.
    du_uint32 body_size;  //!< byte length of the HTTP body data in the request message.

    const du_uchar* local_mac;   //!< MAC address of local host.
    const du_uchar* remote_mac;   //!< MAC address of local host.
} dupnp_dvc_service_common_request;

/**
 * This structure contains the information of HTTP service request.
 */
typedef struct dupnp_dvc_service_http_request {
    const du_ip* local_ip;   //!< IP address of local host.
    const du_ip* remote_ip;  //!< IP address of remote host.
    const du_uchar* method;  //!< the method of the request message.
    const du_uri* uri;       //!< the uri the request message has sent to.
    const du_uchar* version; //!< HTTP version.
    const du_str_array* header;  //!< string array of the HTTP headers in the request message.
    const du_uchar* body;  //!< the HTTP body data in the request message.
    du_uint32 body_size;  //!< byte length of the HTTP body data in the request message.
} dupnp_dvc_service_http_request;

/**
 * This structure contains the information of UPnP control request.
 */
typedef struct dupnp_dvc_service_upnp_control_request {
    const du_ip* local_ip;   //!< IP address of local host.
    const du_ip* remote_ip;  //!< IP address of remote host.
    const du_uchar* method;  //!< the method of the request message.
    const du_uri* uri;       //!< the uri the request message was sent to.
    const du_uchar* version; //!< HTTP version.
    const du_str_array* header;  //!< string array of the HTTP headers in the request message.
    const du_uchar* body;  //!< string array of the HTTP body data in the request message.
    du_uint32 body_size;  //!< byte length of the HTTP body data in the request message.

    const du_uchar* service_type; //!< service type which is described in the device description xml.
    const du_uchar* action_name; //!< the action name of this request.

    du_soapaction _sa;
} dupnp_dvc_service_upnp_control_request;

/**
 * This structure contains the information of GENA subscription request.
 */
typedef struct dupnp_dvc_service_upnp_accept_subscription_request {
    const du_ip* local_ip;   //!< IP address of local host.
    const du_ip* remote_ip;  //!< IP address of remote host.
    const du_uchar* method;  //!< the method of the request message.
    const du_uri* uri;       //!< the uri the request message was sent to.
    const du_uchar* version; //!< HTTP version.
    const du_str_array* header;  //!< string array of the HTTP headers in the request message.
    const du_uchar* body;  //!< string array of the HTTP body data in the request message.
    du_uint32 body_size;  //!< byte length of the HTTP body data in the request message.
} dupnp_dvc_service_upnp_accept_subscription_request;

/**
 * An application-defined callback function that initializes service.
 * @param[in] info pointer to the service information structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark dupnp_dvc_service_init() function is called when dupnp_dvc_register_service() is called.
 */
typedef du_bool (*dupnp_dvc_service_init)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called after the upnp functions are initialized successfully,
 * to perform special processing after the dupnp functions are initialized.
 * @param[in] info pointer to the service information structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This is a optional I/F. You need not implement this I/F in your services.
 */
typedef du_bool (*dupnp_dvc_service_start_before)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called before dupnp_free() is called,
 * to perform special processing before the dupnp functions are freed.
 * @param[in] info pointer to the service information structure.
 * @remark This is a optional I/F. You need not implement this I/F in your services.
 */
typedef void (*dupnp_dvc_service_stop_after)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called when dupnp_free() is called,
 * to perform special processing when the region used by <em>dupnp</em> is freed.
 * @param[in] info pointer to the service information structure.
 */
typedef void (*dupnp_dvc_service_free)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called when dupnp_start() is called,
 * to perform special processing when the dupnp functions are started.
 * @param[in] info pointer to the service information structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_start)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called after the upnp functions are started successfully,
 * to perform special processing after the dupnp functions are started.
 * @param[in] info pointer to the service information structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This is a optional I/F. You need not implement this I/F in your services.
 */
typedef du_bool (*dupnp_dvc_service_start_after)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called before dupnp_stop() is called,
 * to perform special processing before the dupnp functions are stopped.
 * @param[in] info pointer to the service information structure.
 * @remark This is a optional I/F. You need not implement this I/F in your services.
 */
typedef void (*dupnp_dvc_service_stop_before)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function called when dupnp_stop() is called,
 * to perform special processing when the dupnp functions are stopped.
 * @param[in] info pointer to the service information structure.
 */
typedef void (*dupnp_dvc_service_stop)(dupnp_dvc_service_info* info);

/**
 * An application-defined callback function invoked when HTTP request is received
 * and handles the request.
 * @param[in] info pointer to the service information structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @param[in] user_data a parameter application defined.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_http)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_http_request* request);

/**
 * An application-defined callback function invoked when UPnP control:action request is received
 * and handles the request.
 * @param[in] info pointer to the service information structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_upnp_action)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request);

/**
 * An application-defined callback function invoked when UPnP control:action request is received
 * and handles the request.
 * This callback function is invoked before invoking dupnp_dvc_service_upnp_action callback function.
 * @param[in] info pointer to the service information structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @param[in] user_data a parameter application defined.
 * @remark  In the callback function, don't response the request.
 */
typedef void (*dupnp_dvc_service_pre_upnp_action_handler)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request, void* user_data);

/**
 * An application-defined callback function invoked when UPnP control:query request is received
 * and handles the request.
 * @param[in] info pointer to the service information structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @param[in] user_data a parameter application defined.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_upnp_query)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_control_request* request);

/**
 * An application-defined callback function invoked when UPnP Eventing:subscription request is received
 * and handles the request.
 * @param[in] info pointer to the service information structure.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @param[in] user_data a parameter application defined.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_upnp_accept_subscription)(dupnp_dvc_service_info* info, dupnp_dvc_context* context, dupnp_dvc_service_upnp_accept_subscription_request* request);

/**
 * An application-defined callback function which provides component information by component name.
 * @param[in] info pointer to the service information structure.
 * @param[in] component_name component name.
 * @param[out] qif component interface.
 * @param[out] component_info the component information.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_get_component)(dupnp_dvc_service_info* info, const du_uchar* component_name, dupnp_dvc_service_query_interface* qif, dupnp_dvc_service_info** component_info);

/**
 * An application-defined callback function which provides component information by interface name.
 * @param[in] info pointer to the service information structure.
 * @param[in] interface_name interface name.
 * @param[out] qif component interface.
 * @param[out] component_info the component information.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_get_component2)(dupnp_dvc_service_info* info, const du_uchar* interface_name, dupnp_dvc_service_query_interface* qif, dupnp_dvc_service_info** component_info);

/**
 * Returns identification name of dupnp_dvc_service_init() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_init(void);

/**
 * Returns identification name of dupnp_dvc_service_start_before() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_start_before(void);

/**
 * Returns identification name of dupnp_dvc_service_stop_after() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_stop_after(void);

/**
 * Returns identification name of dupnp_dvc_service_free() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_free(void);

/**
 * Returns identification name of dupnp_dvc_service_start() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_start(void);

/**
 * Returns identification name of dupnp_dvc_service_start_after() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_start_after(void);

/**
 * Returns identification name of dupnp_dvc_service_stop_before() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_stop_before(void);

/**
 * Returns identification name of dupnp_dvc_service_stop() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_stop(void);

/**
 * Returns identification name of dupnp_dvc_service_http() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_http(void);

/**
 * Returns identification name of dupnp_dvc_service_upnp_action() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_upnp_action(void);

/**
 * Returns identification name of dupnp_dvc_service_upnp_query() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_upnp_query(void);

/**
 * Returns identification name of dupnp_dvc_service_upnp_accept_subscription() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_upnp_accept_subscription(void);

/**
 * Returns identification name of dupnp_dvc_service_interface_name_get_component() function.
 * @return identification name string.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_get_component(void);

/**
 * Returns identification name of dupnp_dvc_service_interface_name_get_component2() function.
 */
extern const du_uchar* dupnp_dvc_service_interface_name_get_component2(void);

/**
 * Sets common ( server and date ) headers in <em>header</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[out] header pointer to the du_str_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Adds the following lines in <em>header</em>.<br>
 *  "server: server nameCR+LF" <br>
 *  "date: current date valueCR+LF" <br>
 * and if flag of persistent connection is set false, adds the following line
 *  in <em>header</em>.<br>
 *  "connection: closeCR+LF" <br>
 * <em>header</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dupnp_dvc_service_set_common_response_header(dupnp_dvc_context* context, du_str_array* header);

/**
 * Sets common ( server, date, ext, and content type ) headers in <em>header</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[out] header pointer to the du_str_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Adds the following lines in <em>header</em>.<br>
 *  "server: server_nameCR+LF" <br>
 *  "date: current date valueCR+LF" <br>
 *  "ext: CR+LF" <br>
 *  "content-type: text/xml;charset=\"utf-8\"CR+LF" <br>
 * and if flag of persistent connection is set false, adds the following line
 *  in <em>header</em>.<br>
 *  "connection: closeCR+LF" <br>
 * <em>header</em> is a pointer to a <em>du_str_array</em> initialized by
 * the <b>du_str_array_init</b> function.
 */
extern du_bool dupnp_dvc_service_set_common_soap_response_header(dupnp_dvc_context* context, du_str_array* header);

/**
 * Writes a status line of response messages to output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] status  pointer to the variable of the status code.
 * @param[in] reason a reason phrase string. If NULL is specified, the default reason phrase is used.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_status_line(dupnp_dvc_context* context, const du_uchar status[4], const du_uchar* reason);

/**
 * Writes <em>header</em> lines to output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] header  pointer to the HTTP header lines data.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_header(dupnp_dvc_context* context, const du_str_array* header);

/**
 * Writes <em>data</em> to output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] data pointer to the data.
 * @param[in] size the number of bytes to be written to the output buffer.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write(dupnp_dvc_context* context, const du_uint8* data, du_uint32 size);

/**
 * Writes <em>data</em> to output buffer defined in <em>context</em> with chunk size.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] data pointer to the data.
 * @param[in] size the number of bytes to be written to the output buffer.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_chunk(dupnp_dvc_context* context, const du_uint8* data, du_uint32 size);

/**
 * Writes end of stream for chunked encoding to output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_chunk_eos(dupnp_dvc_context* context);

/**
 * Writes file data to the socket with nonblocking manner.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] f the file handle to the open file that this function sends.
 * @param[in] offset du_int64 data holding the input file pointer position from which
 *     this function will start reading data.
 * @param[in] size total number of bytes to send.
 * @param[out] nbytes the total number of bytes sent.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_send_file_nonblock(dupnp_dvc_context* context, du_file f, du_uint64 offset, du_uint64 size, du_uint64* nbytes);

/**
 * Writes file data to the socket.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] f the file handle to the open file that this function sends.
 * @param[in] offset du_int64 data holding the input file pointer position from which
 *     this function will start reading data.
 * @param[in] size total number of bytes to send.
 * @param[out] nbytes the total number of bytes sent.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_send_file(dupnp_dvc_context* context, du_file f, du_uint64 offset, du_uint64 size, du_uint64* nbytes);

/**
 * Flushes output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_flush_output(dupnp_dvc_context* context);

/**
 * Writes header lines with status code and its reason phrase and body data to output buffer,
 * and flushes the ouput buffer.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] status  pointer to the variable of the status code.
 * @param[in] reason a reason phrase string. If NULL is specified, the default reason phrase is used.
 * @param[in] header  pointer to the HTTP header lines data. If NULL is specified, the default header values are used.
 * @param[in] body  pointer to the body data.
 * @param[in] body_size the number bytes of body data to be written to the output buffer.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_response(dupnp_dvc_context* context, const du_uchar status[4], const du_uchar* reason, du_str_array* header, const du_uchar* body, du_uint32 body_size);

/**
 * Writes a response message which body containes the error reason phrase
 *  to output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] status  pointer to the variable of the status code.
 * @param[in] reason a reason phrase string. If NULL is specified, the default reason phrase is used.
 * @param[in] header HTTP header string. If NULL is specified, the default header values are used.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_error_response(dupnp_dvc_context* context, const du_uchar status[4], const du_uchar* reason, du_str_array* header);

/**
 * Writes a response message which body containes the error reason phrase
 *  to output buffer defined in <em>context</em>.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] status  pointer to the variable of the status code.
 * @param[in] reason a reason phrase string. If NULL is specified, the default reason phrase is used.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_write_error_response2(dupnp_dvc_context* context, const du_uchar status[4], const du_uchar* reason);

/**
 * Sets the state variable(name-value pair) that is sent as an event message.
 * If you use this function, it is necessary to set the state variables when the service starts.
 * And, if a state variable is updated while the service is running, you need to set the new value.
 * If the <em>name</em> event variable is updated, upnp stack send event message to the subscriber
 * automatically.
 * @param[in] si  pointer to dupnp_dvc_service_info structure.
 * @param[in] name the name string.
 * @param[in] value the value string to the <em>name</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Do not use this function with
 *    dupnp_dvc_service_evtmgr_set_message_handler(),
 *    dupnp_dvc_service_adapter_enable_event_message_handler(),
 *    dupnp_dvc_service_evtmgr_notify_state_variable_changed() and
 *    dupnp_dvc_service_evtmgr_message_handler()
 *    in the same service.
 */
extern du_bool dupnp_dvc_service_evtmgr_set_state_variable(dupnp_dvc_service_info* si, const du_uchar* name, const du_uchar* value);

/**
 * Gets the value of state variable by name.
 * This function is usable only if eventing method is 'managed type' (e.g. dupnp_dvc_service_evtmgr_set_state_variable-used service).
 * @param[in] si  pointer to dupnp_dvc_service_info structure.
 * @param[in] name name of state variable to get.
 * @param[out] value_sent sent value of state variable to the <em>name</em>.
 * @param[out] value_unsent unsent value of state variable to the <em>name</em>.
 *     <em>value_unsent</em> is empty if there is no value to be sent to subscriber.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Do not use this function with
 *    dupnp_dvc_service_evtmgr_set_message_handler(),
 *    dupnp_dvc_service_adapter_enable_event_message_handler(),
 *    dupnp_dvc_service_evtmgr_notify_state_variable_changed() and
 *    dupnp_dvc_service_evtmgr_message_handler()
 *    in the same service.
 * @remark <em>value_sent</em> and <em>value_unsent</em> must be initialized by du_uchar_array_init().
 */
extern du_bool dupnp_dvc_service_evtmgr_get_state_variable(dupnp_dvc_service_info* si, const du_uchar* name, du_uchar_array* value_sent, du_uchar_array* value_unsent);

/**
 * Notifies that the state valiables sent as an event message has changed.
 * @param[in] si  pointer to dupnp_dvc_service_info structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Do not use this function with dupnp_dvc_service_evtmgr_set_state_variable() in the same service.
 */
extern du_bool dupnp_dvc_service_evtmgr_notify_state_variable_changed(dupnp_dvc_service_info* si);

/**
 * An interface definition of a handler.
 * <b>dupnp_dvc_service_evtmgr_message_handler</b> function is an application-defined
 * callback function that generates an event message for initial event or
 * an event message that is sent when state variable has been changed.
 * @param[in] property_set_xml body data of an event message.
 * @param[in] arg a parameter for the handler function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_evtmgr_message_handler)(du_uchar_array* property_set_xml, void* arg);

/**
 * Registers a dupnp_dvc_service_evtmgr_message_handler() function in <em>si</em> in upnp stack.
 * A dupnp_dvc_service_evtmgr_message_handler() function is an application-defined
 *  callback function that generates an event message for initial event or
 *  an event message that is sent when state variable has been changed.
 * @param[out] si  pointer to the <em>dupnp_dvc_service_info</em> data structure.
 * @param[in] initial_message_handler the function for generating an event message
 *                 for initial event.
 * @param[in] initial_message_handler_arg a parameter for the <em>initial_message_handler</em>
 *                 function.
 * @param[in] message_handler the function for generating an event message that
 *                 is sent when state variable has been changed.
 * @param[in] message_handler_arg a parameter for the <em>message_handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Do not use this function with dupnp_dvc_service_evtmgr_set_state_variable() in the same service.
 */
extern du_bool dupnp_dvc_service_evtmgr_set_message_handler(dupnp_dvc_service_info* si, dupnp_dvc_service_evtmgr_message_handler initial_message_handler, void* initial_message_handler_arg, dupnp_dvc_service_evtmgr_message_handler message_handler, void* message_handler_arg);

/**
 * Registers a dupnp_dvc_evtmgr_sub_state_handler() function in <em>si</em> in upnp stack.
 * A dupnp_dvc_evtmgr_sub_state_handler() function is an application-defined
 *  callback function that is called back after changing state of subscription
 *  (e.g. subscribe/unsubscribe/renew/expire)
 * @param[out] si  pointer to the <em>dupnp_dvc_service_info</em> data structure.
 * @param[in] handler the function that is called back after changing state of subscription.
 *                 Set 0 if you want remove the registered handler.
 * @param[in] handler_arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_evtmgr_set_sub_state_handler(dupnp_dvc_service_info* si, dupnp_dvc_evtmgr_sub_state_handler handler, void* handler_arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_dvc_service_http_response_handler</b> function is an application-defined
 * callback function invoked when sending the response data is enabled.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @param[in] arg a parameter for the handler function.
 * @param[out] completed
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_service_http_response_handler)(dupnp_dvc_context* context, dupnp_dvc_service_http_request* request, void* arg, du_bool* completed);

/**
 * An interface definition of a handler.
 * <b>dupnp_dvc_service_http_end_handler</b> function is an application-defined
 * callback function invoked when sending the response data is completed.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] request pointer to dupnp_dvc_service_request structure.
 * @param[in] arg a parameter for the handler function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef void (*dupnp_dvc_service_http_end_handler)(dupnp_dvc_context* context, dupnp_dvc_service_http_request* request, void* arg);

/**
 * Registers a dupnp_dvc_service_http_response_handler() and dupnp_dvc_service_http_end_handler()
 *  functions in <em>context</em>.
 *  dupnp_dvc_service_http_response_handler function is an application-defined
 * callback function invoked when sending the response data is enabled.<br>
 * dupnp_dvc_service_http_end_handler function is an application-defined
 * callback function invoked when sending the response data is completed.<br>
 * This function is used in the case that sending the response data operation is not completed
 * because socket is in nonblocking mode.
 * @param[in] context pointer to dupnp_dvc_context structure.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for sending
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] response_handler dupnp_dvc_service_http_response_handler() function.
 * @param[in] end_handler dupnp_dvc_service_http_end_handler() function.
 * @param[in] arg a parameter for the handler functions.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_service_http_set_response_handler(dupnp_dvc_context* context, du_int32 timeout_ms, dupnp_dvc_service_http_response_handler response_handler, dupnp_dvc_service_http_end_handler end_handler, void* arg);


extern dupnp_dvc_service_http_request* dupnp_dvc_service_http_request_create(void);

extern dupnp_dvc_service_upnp_control_request* dupnp_dvc_service_upnp_control_request_create(void);

extern dupnp_dvc_service_upnp_accept_subscription_request* dupnp_dvc_service_upnp_accept_subscription_request_create(void);

extern void dupnp_dvc_service_common_request_set(dupnp_dvc_service_common_request* request, dupnp_http_server* dhs, const du_uchar* local_mac, const du_uchar* remote_mac);

extern void dupnp_dvc_service_http_request_set(dupnp_dvc_service_http_request* request, dupnp_http_server* dhs);

extern void dupnp_dvc_service_upnp_control_request_set(dupnp_dvc_service_upnp_control_request* request, dupnp_http_server* dhs);

extern void dupnp_dvc_service_upnp_accept_subscription_request_set(dupnp_dvc_service_upnp_accept_subscription_request* request, dupnp_http_server* dhs);

extern void dupnp_dvc_service_http_request_free(dupnp_dvc_service_http_request* request);

extern void dupnp_dvc_service_upnp_control_request_free(dupnp_dvc_service_upnp_control_request* request);

extern void dupnp_dvc_service_upnp_accept_subscription_request_free(dupnp_dvc_service_upnp_accept_subscription_request* request);

#ifdef __cplusplus
}
#endif

#endif
