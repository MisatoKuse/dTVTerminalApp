/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_cp_dvcmgr.h
 *  @brief The dupnp_cp_dvcmgr interface provides some methods to manipulate the
 * dupnp_cp_dvcmgr structure that stores the UPnP Device information.
 * dupnp_cp_dvcmgr makes it easier to get and to manipulate device information
 * of specified device type.
 * The start and stop calling sequence of dupnp and dupnp_cp_dvcmgr functions
 * must be the following sequence.<br>
 *  dupnp_init();<br>
 *  dupnp_cp_dvcmgr_init();  : must be called after dupnp_init() <br>
 *  dupnp_start();<br>
 *  dupnp_cp_dvcmgr_start(); : must be called after dupnp_start() <br>
 *   ....<br>
 *  dupnp_stop();<br>
 *  dupnp_cp_dvcmgr_stop();  : must be called after dupnp_stop()<br>
 *  dupnp_cp_dvcmgr_free();  : must be called before dupnp_free()<br>
 *  dupnp_free();<br>
 */

#ifndef DUPNP_CP_DVCMGR_H
#define DUPNP_CP_DVCMGR_H

#include <dupnp.h>
#include <dupnp_impl.h>
#include <dupnp_cp_dvcmgr_device.h>
#include <dupnp_cp_dvcmgr_device_array.h>
#include <du_scheduled_task.h>
#include <du_str_array.h>
#include <du_ptr_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_cp_dvcmgr dupnp_cp_dvcmgr;

typedef struct dupnp_cp_dvcmgr_dvcdsc dupnp_cp_dvcmgr_dvcdsc;

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_dvcmgr_allow_join_handler</b> function is an application-defined
 *   callback function invoked when the device information of the specified
 *   device type sent from UPnP Device is received.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> structure.
 * @param[in] device pointer to the <em>dupnp_cp_dvcmgr_device</em> structure.
 *  The contents of this parameter are the information of the UPnP device
 *  (such as IP address, device type, UDN, expiration time, etc.).
 * @param[in] dvcdsc pointer to the <em>dupnp_cp_dvcmgr_dvcdsc</em> structure.
 *  The contents of this parameter are the device description information of the device
 *  (such as device description xml string).
 * @param[in] arg pointer to the user defined data area given by user_data parameter of
 *  dupnp_cp_dvcmgr_set_allow_join_handler() function. For example,
 *  you can use this parameter area to store some device-specific information selected from the
 *   <em>dvcdsc</em> device description parameters.
 * @return  true if you would register the device information in the dupnp_cp_dvcmgr.
 *          false otherwise.
 * @remark  If this handler returns false, the device information
 *          is not registered in <em>x</em>.
 *  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
typedef du_bool (*dupnp_cp_dvcmgr_allow_join_handler)(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, dupnp_cp_dvcmgr_dvcdsc* dvcdsc, void* arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_dvcmgr_join_handler</b> function is an application-defined
 *   callback function invoked when the device was registered in <em>x</em>.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> structure.
 * @param[in] device pointer to the <em>dupnp_cp_dvcmgr_device</em> structure.
 *  The contents of this parameter are the information of the UPnP device
 *  (such as IP address, device type, UDN, expiration time, etc.).
 * @param[in] dvcdsc pointer to the <em>dupnp_cp_dvcmgr_dvcdsc</em> structure.
 *  The contents of this parameter are the device description information of the device
 *  (such as device description xml string).
 * @param[in] arg pointer to the user defined data area given by user_data parameter of
 *  dupnp_cp_dvcmgr_set_join_handler() function. For example,
 *  you can use this parameter area to store some device-specific information selected from the
 *   <em>dvcdsc</em> device description parameters.
 * @remark <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
typedef void (*dupnp_cp_dvcmgr_join_handler)(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, dupnp_cp_dvcmgr_dvcdsc* dvcdsc, void* arg);

/**
 * An interface definition of a handler.
 * <b>dupnp_cp_dvcmgr_leave_handler</b> function is an application-defined
 *   callback function invoked when a ssdp:byebye message from the UPnP device
 *   registered in <em>x</em> is received or the duration of the advertisement
 *   of the device expired.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> structure.
 * @param[in] device pointer to the <em>dupnp_cp_dvcmgr_device</em> structure.
 *  The contents of this parameter are the information of the UPnP device to leave.
 * @param[in] arg pointer to the user defined data area given by user_data parameter of
 *  dupnp_cp_dvcmgr_set_leave_handler() function. For example,
 *  you can release this parameter area in this handler.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
typedef du_bool (*dupnp_cp_dvcmgr_leave_handler)(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, void* arg);

/**
 * An interface definition of a dupnp_cp_dvcmgr_http_get_hook function.
 * <b>dupnp_cp_dvcmgr_http_get_hook</b> function is an application-defined
 *   callback function that hooks the HTTP GET sending request
 *   and sends the HTTP GET message for getting the device description information
 *   instead of default procedure.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> structure.
 * @param[in] si pointer to the <em>dupnp_ssdp_info</em> structure.
 *  The contents of this parameter are du_ip information of a UPnP device
 *  and the header strings sent from the UPnP device.
 * @param[in] arg a parameter for the <em>handler</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark
 *  Setting this hook function, you can change the default request method for getting the
 *  device description information in this hook function.
 *  For example, you can set the specific parameter such as connection timeout value,
 *  HTTP Header, location value, etc.
 *  You must call dupnp_cp_dvcmgr_http_get() function in dupnp_cp_dvcmgr_http_get_hook function.
 *  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
typedef du_bool (*dupnp_cp_dvcmgr_http_get_hook)(dupnp_cp_dvcmgr* x, dupnp_ssdp_info* si, void* arg);

/**
 * An interface definition of a dupnp_cp_dvcmgr_http_response_hook function.
 * <b>dupnp_cp_dvcmgr_http_response_hook</b> function is an application-defined
 *   callback function for changing the manipulation process of the device description
 *   message sent from UPnP device.
 *   If you set this hook function using dupnp_cp_dvcmgr_set_http_response_hook(),
 *   this function is invoked when the device description message sent from
 *   UPnP device has received. You can define the user-defind process to
 *   manipulate the device description message (such as checking the parameters, getting the
 *   specified data in the message) in this function.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] response pointer to the <em>dupnp_http_response</em> data structure.
 * @param[in] arg pointer to user defined data area.
 * @return  true if you would register the received device information in the dupnp_cp_dvcmgr.
 *          false otherwise.
 * @remark If this hook function returns false, the device information sent from UPnP device
 *          is not registered in <em>x</em>.
 *     <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *     the dupnp_cp_dvcmgr_init() function.
 */
typedef du_bool (*dupnp_cp_dvcmgr_http_response_hook)(dupnp_cp_dvcmgr* x, dupnp_http_response* response, void* arg);

/**
 * An interface definition of a dupnp_cp_dvcmgr_visitor function.
 * <b>dupnp_cp_dvcmgr_visitor</b> function is an application-defined
 *   callback function called for accessing the device information stored in
 *   dupnp_cp_dvcmgr structure.
 *   To use this function, call one of the following function.<br>
 * @li @c dupnp_cp_dvcmgr_visit()
 * @li @c dupnp_cp_dvcmgr_visit_device_type()
 * @li @c dupnp_cp_dvcmgr_visit_udn()
 * @param[in] device pointer to the <em>dupnp_cp_dvcmgr_devic</em> data structure.
 * @param[in] arg pointer to user defined data area.
 * @return  true or false.
 */
typedef du_bool (*dupnp_cp_dvcmgr_visitor)(dupnp_cp_dvcmgr_device* device, void* arg);

/**
 * This structure contains the device description in a message sent from UPnP Devices
 * (HTTP GET response message data).
 */
struct dupnp_cp_dvcmgr_dvcdsc {
    const du_uchar* location;  /**< URL for UPnP description. */
    const du_uchar* xml;       /**< UPnP Device Description XML data. Note: This string data is not NULL terminated. */
    du_uint32 xml_len;         /**< byte length of xml string */
    const du_str_array* header;  //!< string array of the HTTP headers in the response message.
};

/**
 * A device manager structure.
 */
struct dupnp_cp_dvcmgr {
    dupnp_impl* _upnp;

    du_bool _running;

    du_scheduled_task _scheduled_task;
    du_id32 _scheduled_task_id;
    du_uint32 _search_mx;

    du_mutex _mutex;
    du_str_array _type_array;
    dupnp_cp_dvcmgr_device_array _dvc_array;
    dupnp_cp_dvcmgr_device_array _tmp_dvc_array;

    du_ptr_array _allow_join_handler_array;
    du_ptr_array _join_handler_array;
    du_ptr_array _leave_handler_array;

    dupnp_cp_dvcmgr_http_get_hook _http_get_hook;
    void* _http_get_hook_arg;
    dupnp_cp_dvcmgr_http_response_hook _http_response_hook;
    void* _http_response_hook_arg;

    du_uchar_array _buf;
    du_str_array _ssdp_search_header;
    du_uchar* _user_agent;
};

/**
 * Creates and initializes <em>x</em>.
 * @param[out] x  pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark
 *  This function registers ssdp_alive handler, ssdp_byebye handler,
 *  ssdp_response handler, and netif_change handler used for dupnp_cp_dvcmgr internally
 *  in <em>upnp</em>.
 */
extern du_bool dupnp_cp_dvcmgr_init(dupnp_cp_dvcmgr* x, dupnp* upnp);

/**
 * Frees the region used by <em>x</em>.
 * @param[in] x  pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @remark You must call this function before dupnp_free() called.
 */
extern void dupnp_cp_dvcmgr_free(dupnp_cp_dvcmgr* x);

/**
 * Starts dupnp_cp_dvcmgr.
 * Sends the ssdp::search request for every device_type stored in <em>x</em>
 * set by dupnp_cp_dvcmgr_add_device_type() function, and starts checking
 * of the expiration of UPnP devices stored in <em>x</em>.
 * @param[in] x  pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark You must call this function after dupnp started.
 *  If dupnp didn't start, this function call returns false.
 */
extern du_bool dupnp_cp_dvcmgr_start(dupnp_cp_dvcmgr* x);

/**
 * Stops dupnp_cp_dvcmgr.
 * Stops checking of the expiration of UPnP devices and removes all device information
 * stored in <em>x</em>.
 * @param[in] x  pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark You must call this function after dupnp stopped.
 *  If dupnp didn't stop, this function call returns false.
 */
extern du_bool dupnp_cp_dvcmgr_stop(dupnp_cp_dvcmgr* x);

/**
 * Checks whether dupnp_cp_dvcmgr is running or not.
 * @param[in] x  pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @return  true if dupnp_cp_dvcmgr is running, false otherwise.
 * @remark After dupnp_cp_dvcmgr_start() returned successfully(i.e. dupnp_cp_dvcmgr was started),
 * dupnp_cp_dvcmgr state is running state and dupnp_cp_dvcmgr_is_running() returns true.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_is_running(dupnp_cp_dvcmgr* x);

/**
 * Registers a <b>dupnp_cp_dvcmgr_allow_join_handler</b> function in <em>x</em>.
 * The <b>dupnp_cp_dvcmgr_allow_join_handler</b> function is an application-defined
 *   callback function invoked when the device information of the specified
 *   device type sent from UPnP Device is received.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_allow_join_handler</b> function to store in <em>x</em>.
 * @param[in] user_data pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 * the dupnp_cp_dvcmgr_init() function. You can register more than one handler function
 * in <em>x</em> by calling this function several times.
 * If registered <b>dupnp_cp_dvcmgr_allow_join_handler</b> function
 * returns false, the device information is not registered in <em>x</em>.
 */
extern du_bool dupnp_cp_dvcmgr_set_allow_join_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_allow_join_handler handler, void* user_data);

/**
 * Registers a <b>dupnp_cp_dvcmgr_join_handler</b> function in <em>x</em>.
 * The <b>dupnp_cp_dvcmgr_join_handler</b> function is an application-defined
 *   callback function invoked when the device was registered in <em>x</em>.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_join_handler</b> function to store in <em>x</em>.
 * @param[in] user_data pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 * the dupnp_cp_dvcmgr_init() function. You can register more than one handler function
 * in <em>x</em> by calling this function several times.
 */
extern du_bool dupnp_cp_dvcmgr_set_join_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_join_handler handler, void* user_data);

/**
 * Registers a <b>dupnp_cp_dvcmgr_leave_handler</b> function in <em>x</em>.
 * <b>dupnp_cp_dvcmgr_leave_handler</b> function is an application-defined
 *   callback function invoked when a ssdp:byebye message from the UPnP device
 *   registered in <em>x</em> is received or the duration of the advertisement
 *   of the device expired.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_leave_handler</b> function to store in <em>x</em>.
 * @param[in] user_data pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 * the dupnp_cp_dvcmgr_init() function. You can register more than one handler function
 * in <em>x</em> by calling this function several times.
 */
extern du_bool dupnp_cp_dvcmgr_set_leave_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_leave_handler handler, void* user_data);

/**
 * Removes a <b>dupnp_cp_dvcmgr_allow_join_handler</b> function from <em>x</em>.
 * The <b>dupnp_cp_dvcmgr_allow_join_handler</b> function is an application-defined
 *   callback function invoked when a information of a new UPnP device's description
 *   sent from UPnP Device is received.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_allow_join_handler</b> function to remove from <em>x</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern du_bool dupnp_cp_dvcmgr_remove_allow_join_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_allow_join_handler handler);

/**
 * Removes a <b>dupnp_cp_dvcmgr_allow_join_handler</b> function from <em>x</em>.
 * The <b>dupnp_cp_dvcmgr_allow_join_handler</b> function is an application-defined
 *   callback function invoked when a information of a new UPnP device's description
 *   sent from UPnP Device is received.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_allow_join_handler</b> function to remove from <em>x</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern du_bool dupnp_cp_dvcmgr_remove_allow_join_handler2(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_allow_join_handler handler, void* arg);

/**
 * Removes a <b>dupnp_cp_dvcmgr_join_handler</b> function from <em>x</em>.
 * The <b>dupnp_cp_dvcmgr_join_handler</b> function is an application-defined
 *   callback function invoked when the device was registered in <em>x</em>.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_join_handler</b> function to remove from <em>x</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern du_bool dupnp_cp_dvcmgr_remove_join_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_join_handler handler);

/**
 * Removes a <b>dupnp_cp_dvcmgr_join_handler</b> function from <em>x</em>.
 * The <b>dupnp_cp_dvcmgr_join_handler</b> function is an application-defined
 *   callback function invoked when the device was registered in <em>x</em>.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_join_handler</b> function to store in <em>x</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern du_bool dupnp_cp_dvcmgr_remove_join_handler2(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_join_handler handler, void* arg);

/**
 * Removes a <b>dupnp_cp_dvcmgr_leave_handler</b> function from <em>x</em>.
 * <b>dupnp_cp_dvcmgr_leave_handler</b> function is an application-defined
 *   callback function invoked when a ssdp:byebye message from the UPnP device
 *   registered in <em>x</em> is received or the duration of the advertisement
 *   of the device expired.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_leave_handler</b> function to store in <em>x</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern du_bool dupnp_cp_dvcmgr_remove_leave_handler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_leave_handler handler);

/**
 * Removes a <b>dupnp_cp_dvcmgr_leave_handler</b> function from <em>x</em>.
 * <b>dupnp_cp_dvcmgr_leave_handler</b> function is an application-defined
 *   callback function invoked when a ssdp:byebye message from the UPnP device
 *   registered in <em>x</em> is received or the duration of the advertisement
 *   of the device expired.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] handler the <b>dupnp_cp_dvcmgr_leave_handler</b> function to store in <em>x</em>.
 * @param[in] arg pointer to the user defined data area.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern du_bool dupnp_cp_dvcmgr_remove_leave_handler2(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_leave_handler handler, void* arg);

/**
 * Set the hook function that hooks the request to send HTTP GET for getting device description
 * information.
 * If the hook function is not set, default procedure handles the request and sends the
 *  message. If the hook function is set, the hook function is called to send the message
 *  instead of the default procedure.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] hook the <b>dupnp_cp_dvcmgr_http_get_hook</b> function to store in <em>x</em>.
 * @param[in] user_data pointer to the user defined data area.
 * @remark
 *  Setting this hook function, you can change the default request method for getting the
 *  device description information in this hook function.
 *  For example, you can set the specific parameter such as connection timeout value,
 *  HTTP Header, location value, etc.
 *  If you need to change the HTTP header of the request message, hook the HTTP GET request
 *   using this function and change the HTTP header in dupnp_cp_dvcmgr_http_get_hook function,
 *   and send the modified HTTP GET request message using dupnp_cp_dvcmgr_http_get function.
 *  You must call dupnp_cp_dvcmgr_http_get() function in dupnp_cp_dvcmgr_http_get_hook function.
 *  <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *  the dupnp_cp_dvcmgr_init() function.
 */
extern void dupnp_cp_dvcmgr_set_http_get_hook(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_http_get_hook hook, void* user_data);

/**
 * Set the hook function that changes the manipulation process of the device description
 *   message sent from UPnP device.
 *   After setting the hook function, the hook function is called just after receiving the device description message send from UPnP device. So you can change the procedure
 *   handling the received device description information.
 * @param[in,out] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] hook the <b>dupnp_cp_dvcmgr_http_response_hook</b> function to store in <em>x</em>.
 * @param[in] user_data pointer to the user defined data area.
 * @remark <em>x</em> is a pointer to a <em>dupnp_cp_dvcmgr</em> data structure initialized by
 *     the dupnp_cp_dvcmgr_init() function.
 */
extern void dupnp_cp_dvcmgr_set_http_response_hook(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_http_response_hook hook, void* user_data);

/**
 * Issues an HTTP GET request to the <em>location</em> with <em>header</em> lines.
 * When the HTTP response message is received, dupnp_cp_dvcmgr will invoke
 * <b>dupnp_cp_dvcmgr_allow_join_handler</b> function.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] location a URL for request.
 * @param[in] header string array of HTTP headers to send.
 * @param[in] timeout_ms the length of time in milliseconds which will be waited for receiving
 *    the response message.
 *    A negative value means infinite timeout.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function can be used only in dupnp_cp_dvcmgr_http_response_hook() and
 * you must not call this function outside dupnp_cp_dvcmgr_http_response_hook().
 */
extern du_bool dupnp_cp_dvcmgr_http_get(dupnp_cp_dvcmgr* x, const du_uchar* location, const du_str_array* header, du_int32 timeout_ms);

/**
 * Registers the device type dupnp_cp_dvcmgr manipulates in <em>x</em>.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] device_type device type string.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_cp_dvcmgr_add_device_type(dupnp_cp_dvcmgr* x, const du_uchar* device_type);

/**
 * Removes the specified device type from <em>x</em>.
 * If <em>x</em> has device information of the specified device type, removes the
 * device information from <em>x</em> and dupnp_cp_dvcmgr_leave_handler will be invoked.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] device_type device type string.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_cp_dvcmgr_remove_device_type(dupnp_cp_dvcmgr* x, const du_uchar* device_type);

/**
 * Sets the mx value of SSDP discovery search message.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] mx value of the maximum wait.
 * @remark the default mx value is 3.
 */
extern void dupnp_cp_dvcmgr_set_search_mx(dupnp_cp_dvcmgr* x, du_uint32 mx);

/**
 * Sets the additional http request headers of SSDP discovery search message.
 *
 * Call this API if you want to add http headers to SSDP discovery search request.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] header string array of HTTP headers to send.
 * @remark The following field names aren't able to override to comply with UPnP Device Architecture:
 *         HOST, MAN, MX, ST
 */
extern du_bool dupnp_cp_dvcmgr_set_search_header(dupnp_cp_dvcmgr* x, du_str_array* header);

/**
 * Sets User-Agent value in device description request.
 *
 * Call this API if you want to add UserAgent to device description request.
 * If this API isn't called, User-Agent isn't added to the request.
 *
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] user_agent User-Agent value to send.
 * @remark user_agent should be the following format: UPnP/1.0 DLNADOC/1.5
 */
extern du_bool dupnp_cp_dvcmgr_set_user_agent(dupnp_cp_dvcmgr* x, const du_uchar* user_agent);

/**
 * Sends the ssdp::search request for every device_type stored in <em>x</em>
 * which set by dupnp_cp_dvcmgr_add_device_type() function.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_ssdp_search(dupnp_cp_dvcmgr* x);

/**
 * Returns the number of device information stored in <em>x</em>.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[out] count pointer to the area for receiving the number of device information
 *   stored in <em>x</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_count_devices(dupnp_cp_dvcmgr* x, du_uint32* count);

/**
 * Returns the number of device information of specified device type stored in <em>x</em>.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] device_type string specifies the device type.
 * @param[out] count pointer to the area for receiving the number of device information
 *   stored in <em>x</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_count_devices2(dupnp_cp_dvcmgr* x, const du_uchar* device_type, du_uint32* count);

/**
 * Removes the device information of specified UDN device from <em>x</em>.
 * If <em>x</em> has device information of the specified UDN, removes the
 * device information from <em>x</em> and dupnp_cp_dvcmgr_leave_handler will be invoked.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] udn string specifies UDN(Unique Device Name).
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_remove_device(dupnp_cp_dvcmgr* x, const du_uchar* udn);

/**
 * Removes all device information stored in <em>x</em> and
 * dupnp_cp_dvcmgr_leave_handler will be invoked.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_remove_all_devices(dupnp_cp_dvcmgr* x);

/**
 * Invokes <em>visitor</em> function with each device information stored in <em>x</em>.
 * You can define an user-defined visitor function for manipulating each device
 * information in <em>x</em>.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] visitor application-defined visitor function.
 * @param[in] arg a parameter for the <em>visitor</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  The process is assured of locking the device list in the visitor
 *   function. The device list is changed when a device is added or removed.
 * We recommend that you use the visitor when you get or change the information
 * in the device list.<br>
 *  If <em>visitor</em> function returns true, the <em>visitor</em> function is called
 * repeatedly for manipulating next device information in <em>x</em>.
 * If the <em>visitor</em> function returns false, this function stops to call
 * the <em>visitor</em> function and returns true.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_visit(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_visitor visitor, void* arg);

/**
 * Searches devices of specified device type in the device list
 * and calls the <em>visitor</em> dupnp_cp_dvcmgr_visitor callback function.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] device_type string specifies the device type.
 * @param[in] visitor application-defined visitor function.
 * @param[in] arg a parameter for the <em>visitor</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  The process is assured of locking the device list in the visitor
 *   function. The device list is changed when a device is added or removed.
 * We recommend that you use the visitor when you get or change the information
 * in the device list.
 *  If <em>visitor</em> function returns true, the <em>visitor</em> function is called
 * repeatedly for manipulating next device information in <em>x</em>.
 * If the <em>visitor</em> function returns false, this function stops to call
 * the <em>visitor</em> function and returns true.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_visit_device_type(dupnp_cp_dvcmgr* x, const du_uchar* device_type, dupnp_cp_dvcmgr_visitor visitor, void* arg);

/**
 * Searches devices of specified UDN(Unique Device Name) in the device list
 * and calls the <em>visitor</em> dupnp_cp_dvcmgr_visitor callback function.
 * @param[in] x pointer to the <em>dupnp_cp_dvcmgr</em> data structure.
 * @param[in] udn string specifies the UDN(Unique Device Name).
 * @param[in] visitor application-defined visitor function.
 * @param[in] arg a parameter for the <em>visitor</em> function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark  The process is assured of locking the device list in the visitor
 *   function. The device list is changed when a device is added or removed.
 * We recommend that you use the visitor when you get or change the information
 * in the device list.
 *  If <em>visitor</em> function returns true, the <em>visitor</em> function is called
 * repeatedly for manipulating next device information in <em>x</em>.
 * If the <em>visitor</em> function returns false, this function stops to call
 * the <em>visitor</em> function and returns true.
 * @remark This function is multithread safe.
 */
extern du_bool dupnp_cp_dvcmgr_visit_udn(dupnp_cp_dvcmgr* x, const du_uchar* udn, dupnp_cp_dvcmgr_visitor visitor, void* arg);

#ifdef __cplusplus
}
#endif

#endif
