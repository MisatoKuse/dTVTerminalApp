/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/**
 * @file dupnp_dvc.h
 * @brief The dupnp_dvc interface provides various methods of UPnP networking
 * for Device (such as enabling http server, setting device description
 * information file).
 * \anchor dupnp_dvc_desc1
 * Using dupnp_dvc modules for device application, you must set information
 * about devices and their services in the dupnp structure. dupnp_dvc_setup()
 * makes it easy to set these information. Developers can put the settings
 * in configuration files. The Configuration files are XML files and are
 * devided into three types, device configuration file, device description
 * file, and service description file.
 * <ul>
 *   <li> \ref dvconf_file "Device Configuration File" : Defines the location of each device
 *                                 description file, etc. </li>
 *   <li> \ref dvdesc_file "Device Description File" : Defines information of each device.</li>
 *   <li> \ref dvsvdesc_file "Service Description File" : Defines information of each service.</li>
 * </ul>
 * dupnp_dvc_setup() reads the settings from the configuration files and
 * stores that information in dupnp.
 *
 * \image html dupnp_dvc1.jpg
 *
 * You can get the information stored in dupnp using
 * dupnp_dvc_get_desc_array() or dupnp_dvc_get_service_array().
 * dupnp_dvcdsc structure contains the information for a root device.
 * Each device information is stored in dupnp_dvcdsc_device structure and
 * each service information is stored in dupnp_dvcdsc_service structure.
 * dupnp_dvcdsc_info structure contains the location and path information
 * specified in the device configuration file.
 *
 * About the relation of dupnp_dvcdsc structure to device and their services,
 * see dupnp_dvcdsc.h.
 */

#ifndef DUPNP_DVC_H
#define DUPNP_DVC_H

#include <dupnp.h>
#include <dupnp_dvcdsc_array.h>
#include <dupnp_dvc_service_array.h>
#include <dupnp_dvc_service_info_array.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Enables or disables HTTP server for Device.
 * This HTTP server receives request messages from UPnP Control Points.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if the data receiving is to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark default status is set to disabled.
 */
extern du_bool dupnp_dvc_enable_http_server(dupnp* upnp, du_bool flag);

/**
 * Opens a description information file which name is given by <em>descinfo_path</em>,
 * reads xml data from the file, and stores that information in dupnp.
 * Then opens the all xml file specified in the path value in the description information file
 * and stores all read information in dupnp.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] device_conf_path  description information file name.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_setup(dupnp* upnp, const du_uchar* device_conf_path);

/**
 * Registers <em>service</em> information in <em>upnp</em>.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] service  pointer to a dupnp_dvc_service_interface structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_register_service(dupnp* upnp, dupnp_dvc_service_interface* service);

/**
 * Returns a pointer to dupnp_dvcdsc_array data stored in <em>upnp</em>.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  pointer to dupnp_dvcdsc_array data.
 */
extern dupnp_dvcdsc_array* dupnp_dvc_get_desc_array(dupnp* upnp);

/**
 * Returns a pointer to dupnp_dvc_service_array data stored in <em>upnp</em>.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @return  pointer to dupnp_dvc_service_array data.
 */
extern dupnp_dvc_service_array* dupnp_dvc_get_service_array(dupnp* upnp);

/**
 * Enables or disables SSDP discovery search receive function.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if the data receiving is to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Default status is disabled.
 */
extern du_bool dupnp_dvc_enable_ssdp_listener(dupnp* upnp, du_bool flag);

/**
 * Enables or disables SSDP discovery advertisement.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] flag  true if SSDP discovery advertisement is set to be enabled; false otherwise.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Default status is disabled. If status sets from disabled to enabled,
 * SSDP advertisement message with ssdp:byebye will be sent, and SSDP advertisement message with
 * ssdp:alive will be sent periodically.
 * If status is changed from enabled to disabled, SSDP advertisement message
 *  with ssdp:byebye is sent. if you set the alive hook function or byebye hook function,
 *  the hook function is invoked instead of the default procedure to send the message.
 */
extern du_bool dupnp_dvc_enable_ssdp_advertisement(dupnp* upnp, du_bool flag);

/**
 * Sends a SSDP advertisement message with ssdp:alive.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] additional_header string array of the HTTP headers ( except HOST, CACHE-CONTROL,
 *            LOCATION, NT, NTS, SERVER, and USN headers ) to be appended to the message.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This request to send the message will not be hooked by dupnp_dvc_ssdp_hook function.
 */
extern du_bool dupnp_dvc_send_ssdp_alive(dupnp* upnp, const du_str_array* additional_header);

/**
 * Sends a SSDP advertisement message with ssdp:byebye.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] additional_header string array of the HTTP headers
 *            (except HOST, NT, NTS, and USN headers )
 *            to be appended to the message.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This request to send the message will not be hooked by dupnp_dvc_ssdp_hook function.
 */
extern du_bool dupnp_dvc_send_ssdp_byebye(dupnp* upnp, const du_str_array* additional_header);

/**
 * An interface definition of a ssdp adv hook function.
 * <b>dupnp_dvc_ssdp_adv_hook</b> function is an application-defined
 *   callback function that hooks the sending request of ssdp:alive or ssdp:byebye
 *   and sends the user-defind SSDP advertisement message.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] arg a parameter for the hook function.
 * @remark It is neccessary to send the advertisement message in
 *   this hook function using dupnp_dvc_send_ssdp_alive() or dupnp_dvc_send_ssdp_byebye().
 */
typedef void (*dupnp_dvc_ssdp_adv_hook)(dupnp* upnp, void* arg);

/**
 * Set the hook function that hooks the request to send SSDP advertisement
 *  message with ssdp:alive message.
 * If the hook function is not set, default procedure handles the request and sends the
 *  message. If the hook function is set, the hook function is called to send the message
 *  instead of the default procedure.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] hook  pointer to the hook function.
 * @param[in] arg a parameter for the hook function.
 */
extern void dupnp_dvc_set_ssdp_alive_hook(dupnp* upnp, dupnp_dvc_ssdp_adv_hook hook, void* arg);

/**
 * Set the hook function that hooks the request to send SSDP advertisement
 *  with ssdp:byebye message.
 * If the hook function is not set, default procedure handles the request and sends the
 *  message. If the hook function is set, the hook function is called to send the message
 *  instead of the default procedure.
 * @param[in] upnp  pointer to the <em>dupnp</em> data structure.
 * @param[in] hook  pointer to the hook function.
 * @param[in] arg a parameter for the hook function.
 */
extern void dupnp_dvc_set_ssdp_byebye_hook(dupnp* upnp, dupnp_dvc_ssdp_adv_hook hook, void* arg);

/**
 * Sends the response message of the SSDP search request.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] additional_header string array of the HTTP headers to be appended to the message.
 * @param[in] s du_socket data referencing a socket.
 * @param[in] local_ip IP address of local host
 * @param[in] remote_ip IP address of remote host
 * @param[in] mx value of the maximum wait.
 * @param[in] st search target data defined by SSDP.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This function can be used only in dupnp_dvc_ssdp_search_response_hook() and
 * you must not call this function outside dupnp_dvc_ssdp_search_response_hook().
 */
extern du_bool dupnp_dvc_send_ssdp_search_response(dupnp* upnp, du_str_array* additional_header, du_socket s, const du_ip* local_ip, const du_ip* remote_ip, du_uint32 mx, const du_uchar* st);

/**
 * An interface definition of a dupnp_dvc_ssdp_search_response_hook function.
 * <b>dupnp_dvc_ssdp_search_response_hook</b> function is an application-defined
 *   callback function for sending the response message of the SSDP search request.
 *   If you set this hook function using dupnp_dvc_set_ssdp_search_response_hook(),
 *   this function is invoked when the response message of the SSDP search request.
 *   You can define the user-defind process to send the response message
 *   (such as checking the parameters, adding the specified HTTP header in the message)
 *   in this function.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] info pointer to the <em>dupnp_ssdp_info</em> data structure.
 * @param[in] s du_socket data referencing a socket.
 * @param[in] local_ip IP address of local host
 * @param[in] mx value of the maximum wait.
 * @param[in] st search target data defined by SSDP.
 * @param[in] arg a parameter for the hook function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Use dupnp_dvc_send_ssdp_search_response() to send the response message
 *         in this function.
 */

typedef du_bool (*dupnp_dvc_ssdp_search_response_hook)(dupnp* upnp, dupnp_ssdp_info* info, du_socket s, const du_ip* local_ip, du_uint32 mx, const du_uchar* st, void* arg);

/**
 * Set the hook function that changes the sending process of a response message
 * of the SSDP search request.
 * If the hook function is not set, default procedure sends the default response
 *  message. If the hook function is set, the hook function is called to send the message
 *  instead of the default procedure.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] hook the <b>dupnp_dvc_ssdp_search_response_hook</b> function to store in
 *                 <em>upnp</em>.
 * @param[in] arg a parameter for the hook function.
 */
extern void dupnp_dvc_set_ssdp_search_response_hook(dupnp* upnp, dupnp_dvc_ssdp_search_response_hook hook, void* arg);

/**
 * Sets max_age of device to <em>upnp</em>.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] max_age new max_age
 * @remark Prohibited to use when dupnp is started.
 */
extern void dupnp_dvc_set_ssdp_max_age(dupnp* upnp, du_uint32 max_age);

/**
 * Gets array of the <em>dupnp_dvc_service_info</em> of service that specified service_id.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] service_id service identifier of the target service
 * @param[out] si_array array of the <em>dupnp_dvc_service_info</em> data structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_get_service_info(dupnp* upnp, const du_uchar* service_id, dupnp_dvc_service_info_array* si_array);

/**
 * Writes element of device specified udn to device description used from <em>upnp</em>.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] udn udn of target device described in device description.
 * @param[in] name name of element.
 * @param[in] value value of element.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark If you will use this function, it is necessary to describe specified element
 * below element of \<UDN\> in device description.
 * In addition, this function overwrite device description xml file.
 */
extern du_bool dupnp_dvc_write_dvcdsc_element(dupnp* upnp, const du_uchar* udn, const du_uchar* name, const du_uchar* value);

/**
 * Reads element of device specified udn from device description used from <em>upnp</em>.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] udn udn of target device described in device description.
 * @param[in] name name of element.
 * @param[out] value value of element.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark If you will use this function, it is necessary to describe specified element
 * below element of \<UDN\> in device description.
 */
extern du_bool dupnp_dvc_read_dvcdsc_element(dupnp* upnp, const du_uchar* udn, const du_uchar* name, du_uchar_array* value);

/*
 * Sets a atribute/element name which you want to publish in device/service description.
 * By default, sent device/service description doesn't contain attributes/elements of digion namespace.
 * even if original device/service description contains them.
 * Use this API when you want to send device/service desctiption to Control Points
 * with particular attributes/elements of dixim namespace.
 * @param[in] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] ns namespace URI of <em>name</em>.
 * @param[in] name name of attribute or element.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_set_filtered_name(dupnp* upnp, const du_uchar* ns, const du_uchar* name);

/**
 * An interface definition of a dupnp_dvc_dvcdsc_substitution_params_set_hook function.
 * <b>dupnp_dvc_dvcdsc_substitution_params_set_hook</b> function is an application-defined
 *   callback function for setting the parameter which is the substitution string
 *   in the device description.
 *   If you set this hook function using dupnp_dvc_set_dvcdsc_substitution_params_set_hook(),
 *   this function is invoked when the device description is sent.
 *   You can define the parameters which is the substitution string in the device description
 *   on demand from the client.
 * @param[in] context pointer to the <em>dupnp_dvc_context</em> data structure.
 * @param[in] request pointer to the <em>dupnp_dvc_service_http_request</em> data structure.
 * @param[in/out] substitution_param_array parameter array to substitute the string.
 * @param[in] arg a parameter for the hook function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dupnp_dvc_dvcdsc_substitution_params_set_hook)(dupnp_dvc_context* context, dupnp_dvc_service_http_request* request, du_str_array* substitution_param_array, void* arg);

/**
 * Set the hook function that sets the parameter which is the substitution string in the
 * device description.
 * If the hook function is not set, the default parameter is only used.
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] hook the <b>dupnp_dvc_dvcdsc_substitution_params_set_hook</b> function to store in
 *                 <em>upnp</em>.
 * @param[in] arg a parameter for the hook function.
 */
extern du_bool dupnp_dvc_set_dvcdsc_substitution_params_set_hook(dupnp* upnp, dupnp_dvc_dvcdsc_substitution_params_set_hook hook, void* arg);

/**
 * Sets value of placeholder in DDD.
 *
 * @param[in] upnp upnp instance.
 * @param[in] key placeholder key to be filled by the value.
 * @param[in] value the value to fill the place.
 * @return true if the function succeeded, false otherwise.
 */
extern du_bool dupnp_dvc_set_dvcdsc_placeholder(dupnp* upnp, const du_uchar* key, const du_uchar* value);

/**
 * Gets value corresponding to the placeholder key in DDD.
 *
 * @param[in] upnp upnp instance.
 * @param[in] key placeholder key to get value.
 * @return the value if it exists. Otherwise NULL.
 */
extern const du_uchar* dupnp_dvc_get_dvcdsc_placeholder(dupnp* upnp, const du_uchar* key);

/**
 * Gets placeholder key-value pairs registered.
 *
 * @param[in] upnp upnp instance.
 * @return du_param array includes key-value pairs of placeholders.
 */
extern const du_str_array* dupnp_dvc_get_dvcdsc_placeholders(dupnp* upnp);

/**
 * An interface definition of a dupnp_dvc_ssdp_pre_adv_hook function.
 * <b>dupnp_dvc_ssdp_pre_adv_hook</b> function is an application-defined
 * callback function that is called before sending SSDP Advertisement.
 * This hook can be used to filter network interfaces which are used to send SSDP Advertisement(ssdp:alive and ssdp:byebye).
 *
 * @param[in] upnp pointer to the <em>dupnp</em> structure.
 * @param[in] local_ip pointer to the <em>du_ip</em> structure which describes a local network interface which is about to be used.
 * @param[out] skip set 1 if you'd like to skip the advertisement for the <em>local_ip</em>. Default is 0.
 * @param[in] arg a parameter for the hook function.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark This hook is called for each network interface available on the environment where CP is running.
 * @remark Returning false aborts advertisements for the rest of local network interfaces.
 */
typedef du_bool (*dupnp_dvc_ssdp_pre_adv_hook)(dupnp* upnp, du_ip* local_ip, du_bool* skip, void* arg);

/**
 * Sets the hook function that is called before sending SSDP Advertisement(ssdp:alive and ssdp:byebye).
 *
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] hook the <b>dupnp_dvc_ssdp_pre_adv_hook</b> function to be set.
 * @param[in] user_data pointer to the user defined data area. This is passed to the <em>hook</em>.
 * @remark The <em>hook</em> is called for each network interface available on the environment where CP is running.
 */
extern void dupnp_dvc_set_ssdp_pre_adv_hook(dupnp* upnp, dupnp_dvc_ssdp_pre_adv_hook hook, void* arg);

/**
 * Adds the user-defined callback function that is called before processing the action to respond.
 *
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the <b>dupnp_dvc_service_pre_upnp__action</b> function to be set.
 * @param[in] arg pointer to the user defined data area. This is passed to the <em>handler</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark Don't respond to the action in the <em>handler</em>.
 */
extern du_bool dupnp_dvc_add_pre_upnp_action_handler(dupnp* upnp, dupnp_dvc_service_pre_upnp_action_handler handler, void* arg);

/**
 * Removes the user-defined callback function that is called before processing the action to respond.
 *
 * @param[in,out] upnp pointer to the <em>dupnp</em> data structure.
 * @param[in] handler the <b>dupnp_dvc_service_pre_upnp_action_handler</b> function to be removed.
 * @param[in] arg pointer to the user defined data area. This is passed to the <em>handler</em>.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_dvc_remove_pre_upnp_action_handler(dupnp* upnp, dupnp_dvc_service_pre_upnp_action_handler handler, void* arg);

extern du_bool dupnp_dvc_set_software_code(dupnp* upnp, const du_uchar* software_code);

extern const du_uchar* dupnp_dvc_get_software_code(dupnp* upnp);

#ifdef __cplusplus
}
#endif

#endif
