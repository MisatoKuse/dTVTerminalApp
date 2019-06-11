/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dav_protocol_info.h
 *  @brief The dav_protocol_info interface provides some methods for manipulating "Protocol Info"
 *   data ( such as parsing, getting each element data).
 * "Protocol Info" is used in Connection Manager service in order to determine
 *  (a certain level of) compatibility between the streaming mechanisms of two
 *  UPnP controlled devices. For more information about Protocol Info, see<br>
 * http://www.upnp.org/standardizeddcps/documents/ConnectionManager1.0.pdf
 * section 2.5.2. ProtocolInfo Concept.
 */

#ifndef DAV_PROTOCOL_INFO_H
#define DAV_PROTOCOL_INFO_H

#include <du_type.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * This structure contains the information of "Protocol Info".
 */
typedef struct dav_protocol_info {
    du_str_array _array;
} dav_protocol_info;

/**
 * Initializes a dav_protocol_info structure area.
 * @param[out] x  pointer to the dav_protocol_info structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dav_protocol_info_init(dav_protocol_info *x);

/**
 * Frees the region of <em>x</em>.
 * @param[out] x  pointer to the dav_protocol_info structure.
 */
extern void dav_protocol_info_free(dav_protocol_info *x);

/**
 * Reset all information stored in <em>x</em>.
 * @param[in] x  pointer to the dav_protocol_info structure.
 */
extern void dav_protocol_info_reset(dav_protocol_info *x);

/**
 * Parses <em>protocol_info</em> string and stores the information in <em>x</em>.
 * @param[out] x pointer to dav_protocol_info structure.
 * @param[in] protocol_info protocol_info format string.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>protocol_info</em> is a string formatted as <br>
 * '\<protocol\>':'\<network\>':'\<contentFormat\>':'\<additionalInfo\>' <br>
 * described by Connection Manager service. For more information, see<br>
 * http://www.upnp.org/standardizeddcps/documents/ConnectionManager1.0.pdf
 * section 2.5.2. ProtocolInfo Concept.
 */
extern du_bool dav_protocol_info_parse(dav_protocol_info *x, const du_uchar* protocol_info);

/**
 * Get the pointer to the protocol element(string) in <em>x</em>.
 * @param[in] x  pointer to the dav_protocol_info structure.
 * @return  pointer to the protocol element(string).
 *   0 if the function fails.
 */
extern const du_uchar* dav_protocol_info_get_protocol(dav_protocol_info *x);

/**
 * Get the pointer to the network element(string) in <em>x</em>.
 * @param[in] x  pointer to the dav_protocol_info structure.
 * @return  pointer to the network element(string).
 *   0 if the function fails.
 */
extern const du_uchar* dav_protocol_info_get_network(dav_protocol_info *x);

/**
 * Get the pointer to the contentFormat element(string) in <em>x</em>.
 * @param[in] x  pointer to the dav_protocol_info structure.
 * @return  pointer to the contentFormat element(string).
 *   0 if the function fails.
 */
extern const du_uchar* dav_protocol_info_get_content_format(dav_protocol_info *x);

/**
 * Get the pointer to the additionalInfo element(string) in <em>x</em>.
 * @param[in] x  pointer to the dav_protocol_info structure.
 * @return  pointer to the additionalInfo element(string).
 *   0 if the function fails.
 */
extern const du_uchar* dav_protocol_info_get_additional_info(dav_protocol_info *x);

#ifdef __cplusplus
}
#endif

#endif
