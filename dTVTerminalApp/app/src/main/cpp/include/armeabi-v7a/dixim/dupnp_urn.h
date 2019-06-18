/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_urn.h
 *  @brief The dupnp_urn interface provides various methods for manipulating a
 *  URN(Uniform Resource Name) string ( such as checking the type of URN, comparing URNs,
 *   getting a specific URN for UPnP).
 */

#ifndef DUPNP_URN_H
#define DUPNP_URN_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Checks whether <em>urn</em> is a URN(Uniform Resource Name) of a device or not.
 * @param[in] urn  URN string.
 * @return  true if <em>urn</em> is a URN of device.
 *          false if <em>urn</em> is not a URN of device.
 */
extern du_bool dupnp_urn_is_device(const du_uchar* urn);

/**
 * Checks whether <em>urn</em> is a URN(Uniform Resource Name) of a service or not.
 * @param[in] urn  URN string.
 * @return  true if <em>urn</em> is a URN of service.
 *          false if <em>urn</em> is not a URN of service.
 */
extern du_bool dupnp_urn_is_service(const du_uchar* urn);

/**
 * Checks whether <em>urn</em> is a URN(Uniform Resource Name) of a serviceId or not.
 * @param[in] urn  URN string.
 * @return  true if <em>urn</em> is a URN of serviceId.
 *          false if <em>urn</em> is not a URN of serviceId.
 */
extern du_bool dupnp_urn_is_service_id(const du_uchar* urn);

/**
 * Compares the type of specified two URN.
 * @param[in] urn1 a first URN.
 * @param[in] urn2 a second URN.
 * @return true if, and only if, the type of <em>urn1</em> is found to
 *  match the type of <em>urn2</em>, false otherwise.
 */
extern du_bool dupnp_urn_type_equal(const du_uchar* urn1, const du_uchar* urn2);

/**
 * Compares the version of specified two URN.
 * @param[in] urn1 a first URN.
 * @param[in] urn2 a second URN.
 * @return true if, and only if, the version of <em>urn1</em> is less than or equal
 *  to the version of <em>urn2</em>, false otherwise.
 */
extern du_bool dupnp_urn_version_le(const du_uchar* urn1, const du_uchar* urn2);

/**
 * Compares the version of specified two URN.
 * @param[in] urn1 a first URN.
 * @param[in] urn2 a second URN.
 * @return true if, and only if, the version of <em>urn1</em> is greater than or equal to
 *  the version of <em>urn2</em>, false otherwise.
 */
extern du_bool dupnp_urn_version_ge(const du_uchar* urn1, const du_uchar* urn2);

/**
 * Returns a URN string for the xmlns attribute required for the UPnP device architecture.
 * This attribute value is "urn:schemas-upnp-org".
 * @return  "urn:schemas-upnp-org" string.
 */
extern const du_uchar* dupnp_urn_schemas(void);

/**
 * Returns a URN string for the xmlns attribute required for the UPnP device description.
 * This attribute value is "urn:schemas-upnp-org:device-1-0".
 * @return  "urn:schemas-upnp-org:device-1-0" string.
 */
extern const du_uchar* dupnp_urn_upnp_device1(void);

/**
 * Returns a URN string for the xmlns attribute required for the UPnP service description.
 * This attribute value is "urn:schemas-upnp-org:service-1-0".
 * @return  "urn:schemas-upnp-org:service-1-0" string.
 */
extern const du_uchar* dupnp_urn_upnp_service1(void);

/**
 * Returns a URN string for the xmlns attribute required for the UPnP control.
 * This attribute value is "urn:schemas-upnp-org:control-1-0".
 * @return  "urn:schemas-upnp-org:control-1-0" string.
 */
extern const du_uchar* dupnp_urn_upnp_control1(void);

/**
 * Returns a URN string for the xmlns attribute required for the
 * DLNA(Digital Living Network Alliance) UPnP description.
 * This attribute value is "schemas-dlna-org:device-1-0".
 * @return  "urn:schemas-dlna-org:device-1-0" string.
 */
extern const du_uchar* dupnp_urn_dlna_device1(void);

/**
 * Returns a URN string for the xmlns attribute required for the UPnP event messages.
 * This attribute value is "urn:schemas-upnp-org:event-1-0".
 * @return  "urn:schemas-upnp-org:event-1-0" string.
 */
extern const du_uchar* dupnp_urn_upnp_event1(void);

/**
 * Returns a URN string for the xmlns attribute required for the digion UPnP device description.
 * This attribute value is "urn:schemas-digion-com:device-1-0".
 * @return  "urn:schemas-digion-com:device-1-0" string.
 */
extern const du_uchar* dupnp_urn_digion_device1(void);

/**
 * Returns a URN string for the xmlns attribute required for the digion UPnP device description.
 * This attribute value is "urn:schemas-digion-com:service:document:1".
 * @return  "urn:schemas-digion-com:service:document:1" string.
 */
extern const du_uchar* dupnp_urn_digion_service_document1(void);

/**
 * Returns a URN string for the xmlns attribute required for the digion UPnP device description.
 * This attribute value is "urn:schemas-digion-com:service:description:1".
 * @return  "urn:schemas-digion-com:service:description:1" string.
 */
extern const du_uchar* dupnp_urn_digion_service_description1(void);

/**
 * Returns a URN string for the xmlns attribute required for the digion UPnP device description infomation.
 * This attribute value is "urn:schemas-digion-com:device_conf-1-0".
 * @return  "urn:schemas-digion-com:device_conf-1-0" string.
 */
extern const du_uchar* dupnp_urn_digion_device_conf1(void);

/**
 * Returns a URN string for the xmlns attribute required for the
 * DLPA(Digital Life Promotion Association) UPnP device description.
 * This attribute value is "urn:schemas-dlpa-jp:device-1-0".
 * @return  "urn:schemas-dlpa-jp:device-1-0" string.
 */
extern const du_uchar* dupnp_urn_dlpa_device1(void);

#ifdef __cplusplus
}
#endif

#endif
