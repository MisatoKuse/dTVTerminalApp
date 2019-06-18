/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dmrd_avt_entity.h
 * @brief The dmrd_avt_entity interface gives the interface definition of each AVT method(action).
 *
 * The interface provides 2 kinds of interfaces for handling each action, XXX and XXX_2.
 * (e.g. dmrd_avt_entity_set_av_transport_uri(XXX), dmrd_avt_entity_set_av_transport_uri_2(XXX_2))
 * The difference between XXX and XXX_2 is that only XXX_2 provides a parameter to handle a client information.
 *
 * Implementing one of XXX and XXX_2 is enough for handling an action corresponding to the interface.
 * If both XXX and XXX_2 interfaces are implemented, only XXX_2 is called by stub.
 *
 * @see http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf
 */

#ifndef DMRD_AVT_ENTITY_H
#define DMRD_AVT_ENTITY_H

#include <dupnp_dvc_service.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_av_transport_uri</b> function is an application-defined
 *   callback function that specifies the URI of the resource to be controlled by the
 *   this component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] current_uri reference, in the form of a URI of the resource.
 * @param[in] current_uri_meta_data  metadata, in the form of a DIDL-Lite XML fragment
 *   associated with the resource pointed to by <em>current_uri</em>.
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
typedef du_bool (*dmrd_avt_entity_set_av_transport_uri)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* current_uri, const du_uchar* current_uri_meta_data, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_av_transport_uri_2</b> function is an application-defined
 *   callback function that specifies the URI of the resource to be controlled by the
 *   this component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] current_uri reference, in the form of a URI of the resource.
 * @param[in] current_uri_meta_data  metadata, in the form of a DIDL-Lite XML fragment
 *   associated with the resource pointed to by <em>current_uri</em>.
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
typedef du_bool (*dmrd_avt_entity_set_av_transport_uri_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* current_uri, const du_uchar* current_uri_meta_data, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_next_av_transport_uri</b> function is an application-defined
 *   callback function that specifies the URI of the resource to be controlled when the
 *   playback of the current resource finishes.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] next_uri URI value to be played when the playback of the current URI finishes.
 * @param[in] next_uri_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *    associated with the resource pointed to by <em>next_url</em>.
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
typedef du_bool (*dmrd_avt_entity_set_next_av_transport_uri)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* next_uri, const du_uchar* next_uri_meta_data, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_next_av_transport_uri_2</b> function is an application-defined
 *   callback function that specifies the URI of the resource to be controlled when the
 *   playback of the current resource finishes.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] next_uri URI value to be played when the playback of the current URI finishes.
 * @param[in] next_uri_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *    associated with the resource pointed to by <em>next_url</em>.
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
typedef du_bool (*dmrd_avt_entity_set_next_av_transport_uri_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* next_uri, const du_uchar* next_uri_meta_data, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_media_info</b> function is an application-defined
 *   callback function that returns information associated with the current media.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] nr_tracks number of tracks.
 * @param[out] media_duration duration of the media, as identified by <em>current_uri</em>.
 * @param[out] current_uri reference, in the form of a URI of the resource.
 * @param[out] current_uri_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *   associated with the resource pointed to by <em>current_uri</em>
 * @param[out] next_uri URI value to be played when the playback of the current URI finishes.
 * @param[out] next_uri_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *    associated with the resource pointed to by <em>next_url</em>.
 * @param[out] play_medium the storage medium of the resource specified by <em>current_uri</em>.
 * @param[out] record_medium the storage medium where the resource specified by
 *     <em>current_uri</em> will be recorded.
 * @param[out] write_status write protection status of currently loaded media.
 * @li @c NOT_WRITABLE : indicates an inherent 'read-only' media or the device doesn't
 *    support recording on the current media. <br>
 * @li @c PROTECTED : indicates a writable media that is currently write-protected.<br>
 * @li @c UNKNOWN : no media is loaded.<br>
 * @li @c NOT_IMPLEMENTED : If the service implementation doesn't support recording.<br>
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
typedef du_bool (*dmrd_avt_entity_get_media_info)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint32* nr_tracks, du_uchar_array* media_duration, du_uchar_array* current_uri, du_uchar_array* current_uri_meta_data, du_uchar_array* next_uri, du_uchar_array* next_uri_meta_data, du_uchar_array* play_medium, du_uchar_array* record_medium, du_uchar_array* write_status, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_media_info_2</b> function is an application-defined
 *   callback function that returns information associated with the current media.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] nr_tracks number of tracks.
 * @param[out] media_duration duration of the media, as identified by <em>current_uri</em>.
 * @param[out] current_uri reference, in the form of a URI of the resource.
 * @param[out] current_uri_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *   associated with the resource pointed to by <em>current_uri</em>
 * @param[out] next_uri URI value to be played when the playback of the current URI finishes.
 * @param[out] next_uri_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *    associated with the resource pointed to by <em>next_url</em>.
 * @param[out] play_medium the storage medium of the resource specified by <em>current_uri</em>.
 * @param[out] record_medium the storage medium where the resource specified by
 *     <em>current_uri</em> will be recorded.
 * @param[out] write_status write protection status of currently loaded media.
 * @li @c NOT_WRITABLE : indicates an inherent 'read-only' media or the device doesn't
 *    support recording on the current media. <br>
 * @li @c PROTECTED : indicates a writable media that is currently write-protected.<br>
 * @li @c UNKNOWN : no media is loaded.<br>
 * @li @c NOT_IMPLEMENTED : If the service implementation doesn't support recording.<br>
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
typedef du_bool (*dmrd_avt_entity_get_media_info_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint32* nr_tracks, du_uchar_array* media_duration, du_uchar_array* current_uri, du_uchar_array* current_uri_meta_data, du_uchar_array* next_uri, du_uchar_array* next_uri_meta_data, du_uchar_array* play_medium, du_uchar_array* record_medium, du_uchar_array* write_status, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_transport_info</b> function is an application-defined
 *   callback function that returns information associated with the current transport
 *   state of this component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] current_transport_state state of the transport, e.g., whether it is
 *             playing, recording, etc. The following value are allowed,<br>
 * "PLAYING", "TRANSITIONING", "PAUSED_PLAYBACK", "PAUSED_RECORDING", "RECORDING",
 * "NO_MEDIA_PRESENT".
 * @param[out] current_transport_status status during operation of the AV Transport service.
 *             "OK" or "ERROR_OCCURRED".
 * @param[out] current_speed string representation of a rational fraction, indicates
 *             the speed relative to normal speed. Example values are '1', '1/2', '2', etc.
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
typedef du_bool (*dmrd_avt_entity_get_transport_info)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uchar_array* current_transport_state, du_uchar_array* current_transport_status, du_uchar_array* current_speed, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_transport_info_2</b> function is an application-defined
 *   callback function that returns information associated with the current transport
 *   state of this component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] current_transport_state state of the transport, e.g., whether it is
 *             playing, recording, etc. The following value are allowed,<br>
 * "PLAYING", "TRANSITIONING", "PAUSED_PLAYBACK", "PAUSED_RECORDING", "RECORDING",
 * "NO_MEDIA_PRESENT".
 * @param[out] current_transport_status status during operation of the AV Transport service.
 *             "OK" or "ERROR_OCCURRED".
 * @param[out] current_speed string representation of a rational fraction, indicates
 *             the speed relative to normal speed. Example values are '1', '1/2', '2', etc.
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
typedef du_bool (*dmrd_avt_entity_get_transport_info_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uchar_array* current_transport_state, du_uchar_array* current_transport_status, du_uchar_array* current_speed, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_x_dlna_get_byte_position_info</b> function is an application-defined
 *   callback function that returns information associated with the current transport
 *   state of this component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] track_size
 *   The file size information for the track currently being rendered.
 *   Controllers can poll this state variable to determine the file size.
 *   Empty string (""), or a string representing an integer number in the inclusive interval: [0, (2^64) − 1].
 * @param[out] rel_byte
 *   The playback position in bytes during playback.
 *   Controllers that implement controller-byte seek operations can poll this state variable to determine the current byte processed by the renderer.
 *   Empty string (""), or a string representing an integer number in the inclusive interval: [0, (2^64) − 1].
 * @param[out] abs_byte
 *   For future use. DLNA does not currently define the behavior of this state variable.
 *   Its value is vendor-dependent, as long as it conforms to the syntax defined in the DLNA Guidelines.
 *   Empty string (""), or a string representing an integer number in the inclusive interval: [0, (2^64) − 1].
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
typedef du_bool (*dmrd_avt_entity_x_dlna_get_byte_position_info)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uchar_array* track_size, du_uchar_array* rel_byte, du_uchar_array* abs_byte, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_x_dlna_get_byte_position_info_2</b> function is an application-defined
 *   callback function that returns information associated with the current transport
 *   state of this component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] track_size
 *   The file size information for the track currently being rendered.
 *   Controllers can poll this state variable to determine the file size.
 *   Empty string (""), or a string representing an integer number in the inclusive interval: [0, (2^64) − 1].
 * @param[out] rel_byte
 *   The playback position in bytes during playback.
 *   Controllers that implement controller-byte seek operations can poll this state variable to determine the current byte processed by the renderer.
 *   Empty string (""), or a string representing an integer number in the inclusive interval: [0, (2^64) − 1].
 * @param[out] abs_byte
 *   For future use. DLNA does not currently define the behavior of this state variable.
 *   Its value is vendor-dependent, as long as it conforms to the syntax defined in the DLNA Guidelines.
 *   Empty string (""), or a string representing an integer number in the inclusive interval: [0, (2^64) − 1].
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
typedef du_bool (*dmrd_avt_entity_x_dlna_get_byte_position_info_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uchar_array* track_size, du_uchar_array* rel_byte, du_uchar_array* abs_byte, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_position_info</b> function is an application-defined
 *   callback function that returns information associated with the current position of
 *   the transport of the component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] track sequence number of the currently selected track, starting at value 1.
 * @param[out] track_duration duration of the current track, specified as a string of the
 *     following form,<br>
 * H+:MM:SS[.F+] or H+:MM:SS[.F0/F1] <br>
 * where, <br>
 * @li @c H+ : one or more digits to indicate elapsed hours.
 * @li @c MM : exactly 2 digits to indicate minutes (00 to 59).
 * @li @c SS : exactly 2 digits to indicate seconds (00 to 59).
 * @li @c [.F+] : optionally a dot followed by one or more digits to indicate
 *     fractions of seconds.
 * @li @c [.F0/F1] : optionally a dot followed by a fraction, with F0 and F1 at
 *     least one digit long, and F0 < F1.
 * The string may be preceded by an optional + or . sign, and the decimal point
 *     itself may be omitted if there are no fractional second digits.
 * @param[out] track_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *    associated with the resource pointed to by <em>track_uri</em>
 * @param[out] track_uri reference, in the form of a URI, to the current track.
 * @param[out] rel_time the current position, in terms of time, from the beginning
 *    of the current track. The time format is the same as for <em>track_duration</em>.
 * @param[out] abs_time the current position, in terms of a time, from the beginning
 *    of the media. The time format is the same as for <em>track_duration</em>.
 * @param[out] rel_count the current position, in terms of a dimensionless counter,
 *    from the beginning of the current track.
 * @param[out] abs_count the current position, in terms of a dimensionless counter,
 *    from the beginning of the loaded media.
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
typedef du_bool (*dmrd_avt_entity_get_position_info)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uint32* track, du_uchar_array* track_duration, du_uchar_array* track_meta_data, du_uchar_array* track_uri, du_uchar_array* rel_time, du_uchar_array* abs_time, du_int32* rel_count, du_int32* abs_count, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_position_info_2</b> function is an application-defined
 *   callback function that returns information associated with the current position of
 *   the transport of the component.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] track sequence number of the currently selected track, starting at value 1.
 * @param[out] track_duration duration of the current track, specified as a string of the
 *     following form,<br>
 * H+:MM:SS[.F+] or H+:MM:SS[.F0/F1] <br>
 * where, <br>
 * @li @c H+ : one or more digits to indicate elapsed hours.
 * @li @c MM : exactly 2 digits to indicate minutes (00 to 59).
 * @li @c SS : exactly 2 digits to indicate seconds (00 to 59).
 * @li @c [.F+] : optionally a dot followed by one or more digits to indicate
 *     fractions of seconds.
 * @li @c [.F0/F1] : optionally a dot followed by a fraction, with F0 and F1 at
 *     least one digit long, and F0 < F1.
 * The string may be preceded by an optional + or . sign, and the decimal point
 *     itself may be omitted if there are no fractional second digits.
 * @param[out] track_meta_data metadata, in the form of a DIDL-Lite XML fragment
 *    associated with the resource pointed to by <em>track_uri</em>
 * @param[out] track_uri reference, in the form of a URI, to the current track.
 * @param[out] rel_time the current position, in terms of time, from the beginning
 *    of the current track. The time format is the same as for <em>track_duration</em>.
 * @param[out] abs_time the current position, in terms of a time, from the beginning
 *    of the media. The time format is the same as for <em>track_duration</em>.
 * @param[out] rel_count the current position, in terms of a dimensionless counter,
 *    from the beginning of the current track.
 * @param[out] abs_count the current position, in terms of a dimensionless counter,
 *    from the beginning of the loaded media.
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
typedef du_bool (*dmrd_avt_entity_get_position_info_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uint32* track, du_uchar_array* track_duration, du_uchar_array* track_meta_data, du_uchar_array* track_uri, du_uchar_array* rel_time, du_uchar_array* abs_time, du_int32* rel_count, du_int32* abs_count, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_device_capabilities</b> function is an application-defined
 *   callback function that returns information on device capabilities of the component,
 *    such as the supported playback and recording formats, and the supported quality
 *    levels for recording.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] possible_playback_storage_media a comma-separated list of storage media that
 *    the device can play.
 * @param[out] possible_record_storage_media a comma-separated list of storage media onto
 *    which the device can record.
 * @param[out] possible_record_quality_modes a comma-separated list of recording quality
 *    modes that the device supports.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of storage media and recording quality modes,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.2 and
 *   table 1.5.
 */
typedef du_bool (*dmrd_avt_entity_get_device_capabilities)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uchar_array* possible_playback_storage_media, du_uchar_array* possible_record_storage_media, du_uchar_array* possible_record_quality_modes, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_device_capabilities_2</b> function is an application-defined
 *   callback function that returns information on device capabilities of the component,
 *    such as the supported playback and recording formats, and the supported quality
 *    levels for recording.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] possible_playback_storage_media a comma-separated list of storage media that
 *    the device can play.
 * @param[out] possible_record_storage_media a comma-separated list of storage media onto
 *    which the device can record.
 * @param[out] possible_record_quality_modes a comma-separated list of recording quality
 *    modes that the device supports.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of storage media and recording quality modes,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.2 and
 *   table 1.5.
 */
typedef du_bool (*dmrd_avt_entity_get_device_capabilities_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uchar_array* possible_playback_storage_media, du_uchar_array* possible_record_storage_media, du_uchar_array* possible_record_quality_modes, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_transport_settings</b> function is an application-defined
 *   callback function that returns information on various settings of the component,
 *    such as the current play mode and the current recording quality mode.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] play_mode current play mode (e.g., random play, repeated play, etc.).
 * @param[out] rec_quality_mode current recording quality mode.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of play mode and recording quality modes,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.3 and
 *       table 1.5.
 */
typedef du_bool (*dmrd_avt_entity_get_transport_settings)(dupnp_dvc_service_info* info, du_uint32 intstance_id, du_uchar_array* play_mode, du_uchar_array* rec_quality_mode, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_transport_settings_2</b> function is an application-defined
 *   callback function that returns information on various settings of the component,
 *    such as the current play mode and the current recording quality mode.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] play_mode current play mode (e.g., random play, repeated play, etc.).
 * @param[out] rec_quality_mode current recording quality mode.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of play mode and recording quality modes,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.3 and
 *       table 1.5.
 */
typedef du_bool (*dmrd_avt_entity_get_transport_settings_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 intstance_id, du_uchar_array* play_mode, du_uchar_array* rec_quality_mode, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_stop</b> function is an application-defined
 *   callback function that stops the progression of the current resource.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_stop)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_stop_2</b> function is an application-defined
 *   callback function that stops the progression of the current resource.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_stop_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_play</b> function is an application-defined
 *   callback function that starts playing the resource.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] speed string representation of a rational fraction, indicates
 *             the speed relative to normal speed. Example values are '1', '1/2', '2', etc.
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
typedef du_bool (*dmrd_avt_entity_play)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* speed, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_play_2</b> function is an application-defined
 *   callback function that starts playing the resource.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] speed string representation of a rational fraction, indicates
 *             the speed relative to normal speed. Example values are '1', '1/2', '2', etc.
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
typedef du_bool (*dmrd_avt_entity_play_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* speed, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_pause</b> function is an application-defined
 *   callback function that halts the progression of the resource while the device is
 *   in a playing state.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_pause)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_pause_2</b> function is an application-defined
 *   callback function that halts the progression of the resource while the device is
 *   in a playing state.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_pause_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_record</b> function is an application-defined
 *   callback function that starts recording, at the current position on the media,
 *   according to the currently specified recording quality, and return immediately.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_record)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_record_2</b> function is an application-defined
 *   callback function that starts recording, at the current position on the media,
 *   according to the currently specified recording quality, and return immediately.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_record_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_seek</b> function is an application-defined
 *   callback function that starts seeking through the resource as fast as possible
 *   to the specified target position.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] unit the allowed units in which the amount of seeking to be performed is specified.
 *   It can be specified as a time (relative or absolute), a count (relative or absolute),
 *   a track number, a tape-index (e.g., for tapes with a indexing facility) or even a video frame.
 * @param[in] target the target position of the seek action, in terms of units defined
 *    by <em>unit</em>.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of unit,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.8.
 */
typedef du_bool (*dmrd_avt_entity_seek)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* unit, const du_uchar* target, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_seek_2</b> function is an application-defined
 *   callback function that starts seeking through the resource as fast as possible
 *   to the specified target position.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] unit the allowed units in which the amount of seeking to be performed is specified.
 *   It can be specified as a time (relative or absolute), a count (relative or absolute),
 *   a track number, a tape-index (e.g., for tapes with a indexing facility) or even a video frame.
 * @param[in] target the target position of the seek action, in terms of units defined
 *    by <em>unit</em>.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of unit,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.8.
 */
typedef du_bool (*dmrd_avt_entity_seek_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* unit, const du_uchar* target, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_next</b> function is an application-defined
 *   callback function to advance to the next track.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_next)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_next_2</b> function is an application-defined
 *   callback function to advance to the next track.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_next_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_previous</b> function is an application-defined
 *   callback function to advance to the previous track.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_previous)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_previous_2</b> function is an application-defined
 *   callback function to advance to the previous track.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
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
typedef du_bool (*dmrd_avt_entity_previous_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_play_mode</b> function is an application-defined
 *   callback function that sets the play mode.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] new_play_mode new play mode (e.g., random play, repeated play, etc.).
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of play mode,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.3.
 */
typedef du_bool (*dmrd_avt_entity_set_play_mode)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* new_play_mode, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_play_mode_2</b> function is an application-defined
 *   callback function that sets the play mode.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] new_play_mode new play mode (e.g., random play, repeated play, etc.).
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of play mode,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.3.
 */
typedef du_bool (*dmrd_avt_entity_set_play_mode_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* new_play_mode, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_record_quality_mode</b> function is an application-defined
 *   callback function that sets the record quality mode.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[in] new_record_quality_mode recording quality mode.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of recording quality modes,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.5.
 */
typedef du_bool (*dmrd_avt_entity_set_record_quality_mode)(dupnp_dvc_service_info* info, du_uint32 instance_id, const du_uchar* new_record_quality_mode, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_set_record_quality_mode_2</b> function is an application-defined
 *   callback function that sets the record quality mode.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[in] new_record_quality_mode recording quality mode.
 * @param[out] error_code pointer to the storage location to receive the error_code.
 *             error_code is a value code identifying what error was encountered.
 *             error_code isn't freed in DiXiM SDK.
 *             If you allocate memory to error_code, you should free it after
 *             user-dupnp_dvc_service_component_stop()-implemented function is called.
 * @param[out] error_description pointer to the storage location to receive the error description.
 *             error_description is short description of the error.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark About the allowed value list of recording quality modes,
 *   see  http://www.upnp.org/standardizeddcps/documents/AVTransport1.0.pdf table 1.5.
 */
typedef du_bool (*dmrd_avt_entity_set_record_quality_mode_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, const du_uchar* new_record_quality_mode, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_current_transport_actions</b> function is an application-defined
 *   callback function that returns the CurrentTransportActions state variable.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] instance_id an InstanceID.
 * @param[out] actions a comma-separated list of transport-controlling actions that can
 *  be successfully invoked for the current resource at this point. The list will
 *  contain a subset of the following actions: Play, Stop, Pause, Seek, Next, Previous and Record.
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
typedef du_bool (*dmrd_avt_entity_get_current_transport_actions)(dupnp_dvc_service_info* info, du_uint32 instance_id, du_uchar_array* actions, const du_uchar** error_code, du_uchar_array* error_description);

/**
 * An interface definition of a function.
 * <b>dmrd_avt_entity_get_current_transport_actions_2</b> function is an application-defined
 *   callback function that returns the CurrentTransportActions state variable.
 * @param[in] info pointer to dupnp_dvc_service_info structure data.
 * @param[in] request pointer to dupnp_dvc_service_upnp_control_request structure data.
 *            If the callback is called to respond to SOAP request, an applicable request information is set.
 *            Otherwise, null is set.(e.g. the other component called it directly)
 * @param[in] instance_id an InstanceID.
 * @param[out] actions a comma-separated list of transport-controlling actions that can
 *  be successfully invoked for the current resource at this point. The list will
 *  contain a subset of the following actions: Play, Stop, Pause, Seek, Next, Previous and Record.
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
typedef du_bool (*dmrd_avt_entity_get_current_transport_actions_2)(dupnp_dvc_service_info* info, dupnp_dvc_service_upnp_control_request* request, du_uint32 instance_id, du_uchar_array* actions, const du_uchar** error_code, du_uchar_array* error_description);


/*
 * Returns the interface name of dmrd_avt_entity_set_av_transport_uri.
 * @return the interface name of dmrd_avt_entity_set_av_transport_uri.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_av_transport_uri(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_av_transport_uri_2.
 * @return the interface name of dmrd_avt_entity_set_av_transport_uri_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_av_transport_uri_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_next_av_transport_uri.
 * @return the interface name of dmrd_avt_entity_set_next_av_transport_uri.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_next_av_transport_uri(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_next_av_transport_uri_2.
 * @return the interface name of dmrd_avt_entity_set_next_av_transport_uri_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_next_av_transport_uri_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_media_info.
 * @return the interface name of dmrd_avt_entity_get_media_info.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_media_info(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_media_info_2.
 * @return the interface name of dmrd_avt_entity_get_media_info_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_media_info_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_transport_info.
 * @return the interface name of dmrd_avt_entity_get_transport_info.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_transport_info(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_transport_info_2.
 * @return the interface name of dmrd_avt_entity_get_transport_info_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_transport_info_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_x_dlna_get_byte_position_info.
 * @return the interface name of dmrd_avt_entity_x_dlna_get_byte_position_info.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_x_dlna_get_byte_position_info(void);

/*
 * Returns the interface name of dmrd_avt_entity_x_dlna_get_byte_position_info_2.
 * @return the interface name of dmrd_avt_entity_x_dlna_get_byte_position_info_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_x_dlna_get_byte_position_info_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_position_info.
 * @return the interface name of dmrd_avt_entity_get_position_info.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_position_info(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_position_info_2.
 * @return the interface name of dmrd_avt_entity_get_position_info_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_position_info_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_device_capabilities.
 * @return the interface name of dmrd_avt_entity_get_device_capabilities.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_device_capabilities(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_device_capabilities_2.
 * @return the interface name of dmrd_avt_entity_get_device_capabilities_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_device_capabilities_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_transport_settings.
 * @return the interface name of dmrd_avt_entity_get_transport_settings.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_transport_settings(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_transport_settings_2.
 * @return the interface name of dmrd_avt_entity_get_transport_settings_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_transport_settings_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_stop.
 * @return the interface name of dmrd_avt_entity_stop.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_stop(void);

/*
 * Returns the interface name of dmrd_avt_entity_stop_2.
 * @return the interface name of dmrd_avt_entity_stop_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_stop_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_play.
 * @return the interface name of dmrd_avt_entity_play.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_play(void);

/*
 * Returns the interface name of dmrd_avt_entity_play_2.
 * @return the interface name of dmrd_avt_entity_play_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_play_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_pause.
 * @return the interface name of dmrd_avt_entity_pause.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_pause(void);

/*
 * Returns the interface name of dmrd_avt_entity_pause_2.
 * @return the interface name of dmrd_avt_entity_pause_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_pause_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_record.
 * @return the interface name of dmrd_avt_entity_record.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_record(void);

/*
 * Returns the interface name of dmrd_avt_entity_record_2.
 * @return the interface name of dmrd_avt_entity_record_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_record_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_seek.
 * @return the interface name of dmrd_avt_entity_seek.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_seek(void);

/*
 * Returns the interface name of dmrd_avt_entity_seek_2.
 * @return the interface name of dmrd_avt_entity_seek_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_seek_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_next.
 * @return the interface name of dmrd_avt_entity_next.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_next(void);

/*
 * Returns the interface name of dmrd_avt_entity_next_2.
 * @return the interface name of dmrd_avt_entity_next_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_next_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_previous.
 * @return the interface name of dmrd_avt_entity_previous.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_previous(void);

/*
 * Returns the interface name of dmrd_avt_entity_previous_2.
 * @return the interface name of dmrd_avt_entity_previous_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_previous_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_play_mode.
 * @return the interface name of dmrd_avt_entity_set_play_mode.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_play_mode(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_play_mode_2.
 * @return the interface name of dmrd_avt_entity_set_play_mode_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_play_mode_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_record_quality_mode.
 * @return the interface name of dmrd_avt_entity_set_record_quality_mode.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_record_quality_mode(void);

/*
 * Returns the interface name of dmrd_avt_entity_set_record_quality_mode_2.
 * @return the interface name of dmrd_avt_entity_set_record_quality_mode_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_set_record_quality_mode_2(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_current_transport_actions.
 * @return the interface name of dmrd_avt_entity_get_current_transport_actions.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_current_transport_actions(void);

/*
 * Returns the interface name of dmrd_avt_entity_get_current_transport_actions_2.
 * @return the interface name of dmrd_avt_entity_get_current_transport_actions_2.
 */
extern const du_uchar* dmrd_avt_entity_interface_name_get_current_transport_actions_2(void);


#ifdef __cplusplus
}
#endif

#endif

