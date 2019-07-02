/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_cp.h
 *  @brief The dupnp_cp interface provides various methods of UPnP networking for Control Point
 * (such as sending discovery request).
 */

#ifndef DUPNP_CP_H
#define DUPNP_CP_H

#include <dupnp.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_ssdp_alive_handler</b> function is an application-defined
 *   callback function that processes a SSDP advertisement message with ssdp:alive
 *   sent from UPnP Devices.
 * @param[in] info pointer to the <em>dupnp_ssdp_info</em> structure.
 *  The contents of this parameter are du_ip information of a UPnP device
 *  and the header strings sent from the UPnP device.
 * @param[in] arg a parameter for the handler function.
 * @remark <em>info</em> is temporary data used inside of this handler,
 * and the created data in <em>info</em> are freed when this handler returns.
 */
typedef void (*dupnp_cp_ssdp_alive_handler)(dupnp_ssdp_info* info, void* arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_ssdp_byebye_handler</b> function is an application-defined
 *   callback function that processes a SSDP advertisement message with ssdp:byebye
 *   sent from UPnP Devices.
 * @param[in] info pointer to the <em>dupnp_ssdp_info</em> structure.
 *  The contents of this parameter are du_ip information of a UPnP device
 *  and the header strings sent from the UPnP device.
 * @param[in] arg a parameter for the handler function.
 * @remark <em>info</em> is temporary data used inside of this handler,
 * and the created data in <em>info</em> are freed when this handler returns.
 */
typedef void (*dupnp_cp_ssdp_byebye_handler)(dupnp_ssdp_info* info, void* arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_ssdp_response_handler</b> function is an application-defined
 *   callback function that processes a SSDP search response from UPnP Devices.
 * @param[in] info pointer to the <em>dupnp_ssdp_info</em> structure.
 *  The contents of this parameter are du_ip information of a UPnP device
 *  and the header strings sent from the UPnP device.
 * @param[in] arg a parameter for the handler function.
 * @remark <em>info</em> is temporary data used inside of this handler,
 * and the created data in <em>info</em> are freed when this handler returns.
 */
typedef void (*dupnp_cp_ssdp_response_handler)(dupnp_ssdp_info* info, void* arg);

/**
 * Registers a <b>dupnp_cp_ssdp_alive_handler</b> function in <em>upnp</em>.
 * The <b>dupnp_cp_ssdp_alive_handler</b> function is an application-defined
 *   callback function that processes a SSDP advertisement message with ssdp:alive
 *   sent from UPnP Devices.
 * @param[out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function using to process the ssdp:alive message
 *   to store in <em>upnp</em>.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You can registers more than one handler function in
 * in <em>upnp</em> by calling this function several times. You must not call this function after
 * dupnp was started.
 */
extern du_bool dupnp_cp_set_ssdp_alive_handler(dupnp* upnp, dupnp_cp_ssdp_alive_handler handler, void* arg);

/**
 * Registers a <b>dupnp_cp_ssdp_byebye_handler</b> function in <em>upnp</em>.
 * The <b>dupnp_cp_ssdp_byebye_handler</b> function is an application-defined
 *   callback function that processes a SSDP advertisement message with ssdp:byebye
 *   sent from UPnP Devices.
 * @param[out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function using to process the ssdp:byebye message
 *   to store in <em>upnp</em>.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You can registers more than one handler function in
 * in <em>upnp</em> by calling this function several times. You must not call this function after
 * dupnp was started.
 */
extern du_bool dupnp_cp_set_ssdp_byebye_handler(dupnp* upnp, dupnp_cp_ssdp_byebye_handler handler, void* arg);

/**
 * Registers a <b>dupnp_cp_ssdp_response_handler</b> function in <em>upnp</em>.
 * The <b>dupnp_cp_ssdp_response_handler</b> function is an application-defined
 *   callback function that processes a SSDP response data.
 * @param[out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function using to process the SSDP response data
 *   to store in <em>upnp</em>.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You can registers more than one handler function in
 * in <em>upnp</em> by calling this function several times. You must not call this function after
 * dupnp was started.
 */
extern du_bool dupnp_cp_set_ssdp_response_handler(dupnp* upnp, dupnp_cp_ssdp_response_handler handler, void* arg);

/**
 * Removes a <em>dupnp_cp_ssdp_alive_handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_cp_remove_ssdp_alive_handler(dupnp* upnp, dupnp_cp_ssdp_alive_handler handler);

/**
 * Removes a <em>dupnp_cp_ssdp_alive_handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_cp_remove_ssdp_alive_handler2(dupnp* upnp, dupnp_cp_ssdp_alive_handler handler, void* arg);

/**
 * Removes a <em>dupnp_cp_ssdp_byebye_handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_cp_remove_ssdp_byebye_handler(dupnp* upnp, dupnp_cp_ssdp_byebye_handler handler);

/**
 * Removes a <em>dupnp_cp_ssdp_byebye_handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_cp_remove_ssdp_byebye_handler2(dupnp* upnp, dupnp_cp_ssdp_byebye_handler handler, void* arg);

/**
 * Removes a <em>dupnp_cp_ssdp_response_handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_cp_remove_ssdp_response_handler(dupnp* upnp, dupnp_cp_ssdp_response_handler handler);

/**
 * Removes a <em>dupnp_cp_ssdp_response_handler</em> function from <em>upnp</em>.
 * @param[in,out] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the function to remove.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>upnp</em> is a pointer to a <em>dupnp</em> data structure initialized by
 * the <b>dupnp_init</b> function. You must not call this function after dupnp was started.
 */
extern du_bool dupnp_cp_remove_ssdp_response_handler2(dupnp* upnp, dupnp_cp_ssdp_response_handler handler, void* arg);

/**
 * Enables or disables SSDP discovery advertisement receive function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if the data receiving is to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark default status is set to disabled.
 */
extern du_bool dupnp_cp_enable_ssdp_listener(dupnp* upnp, du_bool flag);

/**
 * Enables or disables SSDP discovery search function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if the data sending is to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark default status is set to disabled.
 */
extern du_bool dupnp_cp_enable_ssdp_search(dupnp* upnp, du_bool flag);

/**
 * Enables or disables HTTP server for Control Point.
 * This HTTP server receives event messages from UPnP Devices.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if the data receiving is to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark default status is set to disabled.
 */
extern du_bool dupnp_cp_enable_http_server(dupnp* upnp, du_bool flag);

/**
 * Sends a multicast UPnP discovery:search with method M-SEARCH.
 * When a Control Point is added to the network, the multicast UPnP discovery search
 * request allows that Control Point to search for devices (or services) of interest
 * on the network.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] header string array of the HTTP headers ( except man,mx, and st headers )
 *   to be appended to the request message.
 * @param[in] st search target data defined by SSDP.
 * @param[in] mx value of the maximum wait.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark this function sends a multicast request in the following format. <br>
 *  M-SEARCH * HTTP/1.1 <br>
 *  HOST: 239.25.255.250:1900 <br>
 *  MAN: "ssdp:discover" <br>
 *  MX: <em>mx</em> <br>
 *  ST: <em>st</em> <br>
 *  <em>header</em>(line(s)) <br>
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_ssdp_search(dupnp* upnp, const du_str_array* header, const du_uchar* st, du_uint32 mx);

/**
 * Returns ST string value of ssdp:all.
 * The exactory value is "ssdp:all".
 * @return  "ssdp:all" string.
 */
extern const du_uchar* dupnp_cp_ssdp_st_value_ssdp_all(void);

/**
 * Enumeration of the errors of receiving events.
 */
typedef enum {
    DUPNP_CP_EVENT_ERROR_NONE, /**< No error. */
    DUPNP_CP_EVENT_ERROR, /**< General error. */
    DUPNP_CP_EVENT_ERROR_INVALID_HEADER, /**< Some headers are not valid. */
    DUPNP_CP_EVENT_ERROR_NOT_INITIAL_EVENT, /**< No initial event received */
    DUPNP_CP_EVENT_ERROR_INVALID_SEQ, /**< The sequence number of event message is invalid */
} dupnp_cp_event_error;

/**
 * This structure contains the information of a GENA event message.
 */
typedef struct dupnp_cp_event_info {
    const du_uchar* event_sub_url;    //!< subscription URL of the event message.
    dupnp_cp_event_error error;       //!< error code.
    const du_str_array* header;       //!< string array of the HTTP headers in the GENA event message.
    du_uint32 seq;                    //!< event key value of the event message.
    const du_uchar* property_set_xml; //!< body data of the event message. Note: This string data is not NULL terminated.
    du_uint32 property_set_xml_size;  //!< byte length of the body data.
} dupnp_cp_event_info;

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_gena_event_handler</b> function is an application-defined
 *   callback function that processes a GENA event message sent from UPnP Devices.
 * @param[in] info pointer to the dupnp_cp_event_info structure.
 * @param[in] arg a parameter for the handler function.
 * @remark <em>info</em> is temporary data used inside of this handler,
 * and the created data in <em>info</em> are freed when this handler returns.
 */
typedef void (*dupnp_cp_gena_event_handler)(dupnp_cp_event_info* info, void* arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_gena_renewal_alarm_handler</b> function is an application-defined
 *   callback function invoked when the renewing a subscription is necessary.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] sid  SID (Subscription Identifier) string.
 * @param[in] arg a parameter for the handler function.
 */
typedef void (*dupnp_cp_gena_renewal_alarm_handler)(dupnp* upnp, const du_uchar* sid, void* arg);

/**
 * Sends the subscription message defined by GENA with method SUBSCRIBE.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] event_sub_url the URL of the event publisher.
 * @param[in] header string array of the HTTP headers ( except nt and timeout headers )
 *   to be appended to the request message.
 * @param[in] sub_timeout requested subscription duration in second.
 *    If <em>timeout</em> is less than zero, then the duration is infinite.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] subscribe_response_handler the function to store in <em>upnp</em>.
 * <em>subscribe_response_handler</em> function is an application-defined
 *   callback function that processes a response message of the subscription request
 *   sent from UPnP Devices.
 * @param[in] subscribe_response_handler_arg a parameter for the
 *   <em>subscribe_response_handler</em> function.
 * @param[in] renewal_alarm_handler the function to store in <em>upnp</em>.
 * <em>dupnp_cp_gena_renewal_alarm_handler</em> function is an application-defined
 *   callback function that invoked when the renewing a subscription is necessary.
 * @param[in] renewal_alarm_handler_arg a parameter for the
 *   <em>dupnp_cp_gena_renewal_alarm_handler</em> function.
 * @param[in] event_handler the function to store in <em>upnp</em>.
 * <b>dupnp_cp_gena_event_handler</b> function is an application-defined
 *   callback function that processes a GENA event message sent from UPnP Devices.
 * @param[in] event_handler_arg a parameter for the <em>event_handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark this function sends a request in the following format. <br>
 *  SUBSCRIBE <em>event_sub_url</em> HTTP/1.1 <br>
 *  HOST: <em>event_sub_url</em> host:<em>event_sub_url</em> port <br>
 *  NT: "upnp:event" <br>
 *  TIMEOUT: Second-<em>timeout</em> <br>
 *  <em>header</em>(line(s)) <br>
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_gena_subscribe(dupnp* upnp, const du_uchar* event_sub_url, const du_str_array* header, du_uint32 sub_timeout, du_int32 timeout_ms, dupnp_http_response_handler subscribe_response_handler, void* subscribe_response_handler_arg, dupnp_cp_gena_renewal_alarm_handler renewal_alarm_handler, void* renewal_alarm_handler_arg, dupnp_cp_gena_event_handler event_handler, void* event_handler_arg, du_uint32* id);

/**
 * Sends the canceling a subscription message defined by GENA with method UNSUBSCRIBE.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] header string array of the HTTP headers ( except sid headers )
 *   to be appended to the request message.
 * @param[in] event_sub_url the URL for eventing.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] unsubscribe_response_handler the function to store in <em>upnp</em>.
 * <em>unsubscribe_response_handler</em> function is an application-defined
 *   callback function that processes a response message of the cancellation request
 *   sent from UPnP Devices.
 * @param[in] unsubscribe_response_handler_arg a parameter for the
 *   <em>unsubscribe_response_handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em> for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark this function sends a multicast request in the following format. <br>
 *  UNSUBSCRIBE <em>event_sub_url</em> HTTP/1.1 <br>
 *  HOST: <em>event_sub_url</em> host:<em>event_sub_url</em> port <br>
 *  SID: uuid:subscription UUID  <br>
 *  <em>header</em>(line(s)) <br>
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_gena_unsubscribe(dupnp* upnp, const du_str_array* header, const du_uchar* event_sub_url, du_int32 timeout_ms, dupnp_http_response_handler unsubscribe_response_handler, void* unsubscribe_response_handler_arg, du_uint32* id);

/**
 * Sends the renewing a subscription message defined by GENA with method SUBSCRIBE.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] sid  SID (Subscription Identifier) string.
 * @param[in] header string array of the HTTP headers ( except sid headers )
 *   to be appended to the request message.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @param[in] renewal_response_handler the function to store in <em>upnp</em>.
 * <em>renewal_response_handler</em> function is an application-defined
 *   callback function that processes a response message of the renewing request
 *   sent from UPnP Devices.
 * @param[in] renewal_response_handler_arg a parameter for the
 *   <em>renewal_response_handler</em> function.
 * @param[out] id an id of connection. It can be used with <em>dupnp_http_cancel</em>
 *   for canceling connection.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark this function sends a multicast request in the following format. <br>
 *  SUBSCRIBE <em>event_sub_url</em> HTTP/1.1 <br>
 *  HOST: <em>event_sub_url</em> host:<em>event_sub_url</em> port <br>
 *  SID: uuid:subscription UUID  <br>
 *  TIMEOUT: Second-<em>timeout</em> <br>
 *  <em>header</em>(line(s)) <br>
 * @remark This function is multithread safe.
 */

extern du_bool dupnp_cp_gena_subscribe_renewal(dupnp* upnp, const du_uchar* sid, const du_str_array* header, du_int32 timeout_ms, dupnp_http_response_handler renewal_response_handler, void* renewal_response_handler_arg, du_uint32* id);

/**
 * An interface definition of a dupnp_cp_ssdp_pre_search_hook function.
 * <b>dupnp_cp_ssdp_pre_search_hook</b> function is an application-defined
 * callback function that is called before sending SSDP M-Search.
 * This hook can be used to filter network interfaces which are used to send SSDP M-Search.
 *
 * @param[in] upnp pointer to the <em>dupnp</em> structure.
 * @param[in] local_ip pointer to the <em>du_ip</em> structure which describes a local network interface which is about to be used.
 * @param[out] skip set 1 if you'd like to skip the M-Search for the <em>local_ip</em>. Default is 0.
 * @param[in] arg a parameter for the hook function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This hook is called for each network interface available on the environment where CP is running.
 * @remark Returning false aborts M-Search for the rest of local network interfaces.
 */
typedef du_bool (*dupnp_cp_ssdp_pre_search_hook)(dupnp* upnp, du_ip* local_ip, du_bool* skip, void* arg);

/**
 * Sets the hook function that is called before sending SSDP M-Search.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] hook the <b>dupnp_cp_ssdp_pre_search_hook</b> function to be set.
 * @param[in] user_data pointer to the user defined data area. This is passed to the <em>hook</em>.
 * @remark The <em>hook</em> is called for each network interface available on the environment where CP is running.
 */
extern void dupnp_cp_set_ssdp_pre_search_hook(dupnp* upnp, dupnp_cp_ssdp_pre_search_hook hook, void* user_data);

#ifdef __cplusplus
}
#endif

#endif
