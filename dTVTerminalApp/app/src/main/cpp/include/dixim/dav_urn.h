/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_urn.h
 *  @brief The dav_urn interface provides methods for getting a
 *  specific URN for UPnP AV.
 */

#ifndef DAV_TYPE_H
#define DAV_TYPE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the MediaServer.
 * This value is "urn:schemas-upnp-org:device:MediaServer:v".
 * @param[in] v version number of the device. It must be between 1 and 3.
 * @return  "urn:schemas-upnp-org:device:MediaServer:v" string.
 */
extern const du_uchar* dav_urn_msd(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the MediaRenderer.
 * This value is "urn:schemas-upnp-org:device:MediaRenderer:v".
 * @param[in] v version number of the device. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:device:MediaRenderer:v" string.
 */
extern const du_uchar* dav_urn_mrd(du_uint32 v);

/**
 * Returns a URN string for the serviceType element value required for a
 * XML device description of the ContentDirectory.
 * This value is "urn:schemas-upnp-org:service:ContentDirectory:v".
 * @param[in] v version number of the service. It must be between 1 and 3.
 * @return  "urn:schemas-upnp-org:service:ContentDirectory:v" string.
 */
extern const du_uchar* dav_urn_cds(du_uint32 v);

/**
 * Returns a URN string for the serviceType element value required for a
 * XML device description of the ConnectionManager.
 * This value is "urn:schemas-upnp-org:service:ConnectionManager:v".
 * @param[in] v version number of the service. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:service:ConnectionManager:v" string.
 */
extern const du_uchar* dav_urn_cms(du_uint32 v);

/**
 * Returns a URN string for the serviceType element value required for a
 * XML device description of the AVTransport.
 * This value is "urn:schemas-upnp-org:service:AVTransport:v".
 * @param[in] v version number of the service. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:service:AVTransport:v" string.
 */
extern const du_uchar* dav_urn_avt(du_uint32 v);

/**
 * Returns a URN string for the serviceType element value required for a
 * XML device description of the RenderingControl.
 * This value is "urn:schemas-upnp-org:service:RenderingControl:v".
 * @param[in] v version number of the service. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:service:RenderingControl:v" string.
 */
extern const du_uchar* dav_urn_rcs(du_uint32 v);

/**
 * Returns a URN string for the serviceType element value required for a
 * XML device description of the ScheduledRecording.
 * This value is "urn:schemas-upnp-org:service:ScheduledRecording:v".
 * @param[in] v version number of the service. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:service:ScheduledRecording:v" string.
 */
extern const du_uchar* dav_urn_srs(du_uint32 v);

/**
 * Returns a URN string of the XML namespace name required for
 * common data types for use in AV. schemas.
 * of structure and metadata for UPnP AV.
 * This value is "urn:schemas-upnp-org:av:av".
 * @return  "urn:schemas-upnp-org:av:av" string.
 */
extern const du_uchar* dav_urn_upnp_av_av(void);

/**
 * Returns a URN string of the XML namespace name required for the description
 * of structure and metadata for evented LastChange state variable for ContentDirectory.
 * This value is "urn:schemas-upnp-org:av:cds-event".
 * @return  "urn:schemas-upnp-org:av:cds-event" string.
 */
extern const du_uchar* dav_urn_upnp_av_cds_event(void);

/**
 * Returns a URN string of the XML namespace name required for
 * metadata and structure for ScheduledRecording.
 * of structure and metadata for UPnP AV.
 * This value is "urn:schemas-upnp-org:av:srs".
 * @return  "urn:schemas-upnp-org:av:srs" string.
 */
extern const du_uchar* dav_urn_upnp_av_srs(void);

/**
 * Returns a URN string of the XML namespace name required for the description
 * of structure and metadata for evented LastChange state variable for ScheduledRecording.
 * This value is "urn:schemas-upnp-org:av:srs-event".
 * @return  "urn:schemas-upnp-org:av:srs-event" string.
 */
extern const du_uchar* dav_urn_upnp_av_srs_event(void);

/**
 * Returns a URN string of the XML namespace name required for the description
 * of structure and metadata for ContentDirectory.
 * This value is "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".
 * @return  "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/" string.
 */
extern const du_uchar* dav_urn_upnp_metadata1_didl_lite(void);

/**
 * Returns a URN string of the XML namespace name required for the description
 * of structure and metadata for ContentDirectory.
 * This value is "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite".
 * @return  "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite" string.
 */
extern const du_uchar* dav_urn_upnp_metadata1_didl_lite_noslash(void);

/**
 * Returns a URN string of the XML namespace name required for the description of
 * the metadata for ContentDirectory.
 * This value is "urn:schemas-upnp-org:metadata-1-0/upnp/".
 * @return  "urn:schemas-upnp-org:metadata-1-0/upnp/" string.
 */
extern const du_uchar* dav_urn_upnp_metadata1_upnp(void);

/**
 * Returns a URN string of the XML namespace name required the description of
 * the metadata for ContentDirectory.
 * This value is "urn:schemas-upnp-org:metadata-1-0/upnp".
 * @return  "urn:schemas-upnp-org:metadata-1-0/upnp" string.
 */
extern const du_uchar* dav_urn_upnp_metadata1_upnp_noslash(void);

/**
 * Returns a URN string of the XML namespace name required the description of
 * the metadata for evented LastChange state variable for AVTransport.
 * This value is "urn:schemas-upnp-org:metadata-1-0/upnp/AVT/".
 * @return  "urn:schemas-upnp-org:metadata-1-0/upnp/AVT/" string.
 */
extern const du_uchar* dav_urn_upnp_metadata1_upnp_avt(void);

/**
 * Returns a URN string of the XML namespace name required the description of
 * the metadata for evented LastChange state variable for RenderingControl.
 * This value is "urn:schemas-upnp-org:metadata-1-0/upnp/RCS/".
 * @return  "urn:schemas-upnp-org:metadata-1-0/upnp/RCS/" string.
 */
extern const du_uchar* dav_urn_upnp_metadata1_upnp_rcs(void);

/**
 * Returns a URN string of the XML namespace name required for the
 * Dublin Core Metadata Element Set.
 * This value is "http://purl.org/dc/elements/1.1/".
 * @return  "http://purl.org/dc/elements/1.1/" string.
 */
extern const du_uchar* dav_urn_dc_elements1_1(void);

/**
 * Returns a URN string of the XML namespace name for the DiXiM metadata.
 * This value is "urn:schemas-digion-com:metadata-1-0/dixim/DIDL-Lite/".
 * @return  "urn:schemas-digion-com:metadata-1-0/dixim/DIDL-Lite/" string.
 */
extern const du_uchar* dav_urn_digion_metadata1_dixim_didl_lite(void);

/**
 * Returns a URN string of the XML namespace name required for the
 * DLNA(Digital Living Network Alliance) metadata.
 * This value is "urn:schemas-dlna-org:metadata-1-0/".
 * @return  "urn:schemas-dlna-org:metadata-1-0/" string.
 */
extern const du_uchar* dav_urn_dlna_metadata1(void);

/**
 * Returns a URN string of the XML namespace name required for the
 * ARIB(Association of Radio Industries and Businesses) elements.
 * This value is "urn:schemas-arib-or-jp:elements-1-0/".
 * @return  "urn:schemas-arib-or-jp:elements-1-0/" string.
 */
extern const du_uchar* dav_urn_arib_elements1(void);

/**
 * Returns a URN string of the XML namespace name required for the
 * DTCP-IP elements.
 * This value is "urn:schemas-dtcp-com:metadata-1-0/".
 * @return  "urn:schemas-dtcp-com:metadata-1-0/" string.
 */
extern const du_uchar* dav_urn_dtcp_metadata1(void);

/**
 * Returns a URN string of the XML namespace name required for the
 * Microsoft metadata.
 * This value is "urn:schemas-microsoft-com:WMPNSS-1-0/".
 * @return  "urn:schemas-microsoft-com:WMPNSS-1-0/" string.
 */
extern const du_uchar* dav_urn_microsoft_wmpnss1(void);

/**
 * Returns a URN string of the XML namespace name required for the
 * DLPA(Digital Life Promotion Association) metadata.
 * This value is "urn:schemas-dlpa-jp:metadata-1-0/".
 * @return  "urn:schemas-dlpa-jp:metadata-1-0/" string.
 */
extern const du_uchar* dav_urn_dlpa_metadata1(void);

#ifdef __cplusplus
}
#endif

#endif
