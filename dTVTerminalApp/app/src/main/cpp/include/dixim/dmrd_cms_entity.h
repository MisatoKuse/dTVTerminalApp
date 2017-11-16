/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dmrd_cms_entity.h
 * @brief The dmrd_cms_entity interface gives the interface definition of each CMS method(action).
 *
 * The interface provides 2 kinds of interfaces for handling each action, XXX and XXX_2.
 * (e.g. dmrd_cms_entity_get_protocol_info(XXX), dmrd_cms_entity_get_protocol_info_2(XXX_2))
 * The difference between XXX and XXX_2 is that only XXX_2 provides a parameter to handle a client information.
 *
 * Implementing one of XXX and XXX_2 is enough for handling an action corresponding to the interface.
 * If both XXX and XXX_2 interfaces are implemented, only XXX_2 is called by stub.
 *
 * @see http://www.upnp.org/standardizeddcps/documents/ConnectionManager1.0.pdf
 */


#ifndef DMRD_CMS_ENTITY_H
#define DMRD_CMS_ENTITY_H

#include <dupnp_dvc_service.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function.
 * <b>dmrd_cms_entity_get_protocol_info</b> function is an application-defined
 *   callback function that gets the protocol-related information that this
 *   ConnectionManager supports in its current state, as a Comma-Separated Value(CVS)
 *   list of strings.
 *   Protocol-related information for 'sinking'(receiving) data is returned
 *   in the <em>sink</em> argument.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[out] sink a CSV list of information on protocols ConnectionManager supports
 *               for 'sinking'(receiving) data in its current state.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_cms_entity_get_protocol_info)(dupnp_dvc_service_info* info, const du_uchar** sink, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_cms_entity_get_protocol_info</b> function is an application-defined
 *   callback function that gets the protocol-related information that this
 *   ConnectionManager supports in its current state, as a Comma-Separated Value(CVS)
 *   list of strings.
 *   Protocol-related information for 'sinking'(receiving) data is returned
 *   in the <em>sink</em> argument.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[out] sink a CSV list of information on protocols ConnectionManager supports
 *               for 'sinking'(receiving) data in its current state.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_cms_entity_get_protocol_info_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, const du_uchar** sink, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_cms_entity_get_current_protocol_info</b> function is an application-defined
 *   callback function that gets the protocol-related information that current content
 *   protocol_info of strings.
 *   Protocol-related information for 'sinking'(receiving) data is returned
 *   in the <em>sink</em> argument.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[out] protocol_info current content protocol_info.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_cms_entity_get_current_protocol_info)(dupnp_dvc_service_info* info, du_uchar_array* protocol_info);

/**
 * An interface definition of a function.
 * <b>dmrd_cms_entity_get_current_protocol_info</b> function is an application-defined
 *   callback function that gets the protocol-related information that current content
 *   protocol_info of strings.
 *   Protocol-related information for 'sinking'(receiving) data is returned
 *   in the <em>sink</em> argument.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[out] protocol_info current content protocol_info.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
typedef du_bool (*dmrd_cms_entity_get_current_protocol_info_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uchar_array* protocol_info);

/*
 * Returns the interface name of dmrd_cms_entity_get_protocol_info.
 * @return the interface name of dmrd_cms_entity_get_protocol_info.
 */
extern const du_uchar* dmrd_cms_entity_interface_name_get_protocol_info(void);

/*
 * Returns the interface name of dmrd_cms_entity_get_protocol_info_2.
 * @return the interface name of dmrd_cms_entity_get_protocol_info_2.
 */
extern const du_uchar* dmrd_cms_entity_interface_name_get_protocol_info_2(void);

/*
 * Returns the interface name of dmrd_cms_entity_get_current_protocol_info.
 * @return the interface name of dmrd_cms_entity_get_current_protocol_info.
 */
extern const du_uchar* dmrd_cms_entity_interface_name_get_current_protocol_info(void);

/*
 * Returns the interface name of dmrd_cms_entity_get_current_protocol_info_2.
 * @return the interface name of dmrd_cms_entity_get_current_protocol_info_2.
 */
extern const du_uchar* dmrd_cms_entity_interface_name_get_current_protocol_info_2(void);

#ifdef __cplusplus
}
#endif

#endif

