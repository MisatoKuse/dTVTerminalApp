/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_soapaction interface provides methods for manipulating the
 *  SOAPAction header field defined by SOAP.
 *  UPnP uses SOAP to deliver control messages to devices and return results or errors
 *  back to control points.
 */

#ifndef DU_SOAPACTION_H
#define DU_SOAPACTION_H

#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  SOAPAction structure member.
 */
typedef struct du_soapaction {
    /**
     *   Pointer to the uri string of SOAPAction header field.
     */
    const du_uchar* uri;

    /**
     *   Pointer to the actionName string of SOAPAction header field if defined.
     */
    const du_uchar* fragment;


    du_uchar* _buf;
} du_soapaction;

/**
 *  Initializes a du_soapaction data area.
 *  @param[out] x pointer to the du_soapaction data structure.
 */
extern void du_soapaction_init(du_soapaction* x);

/**
 *  Frees the resources in x.
 *  @param[in,out] x pointer to the du_soapaction structure.
 */
extern void du_soapaction_free(du_soapaction* x);

/**
 *  Parses soapaction string.
 *  @param[in,out] x pointer to the du_soapaction structure.
 *  @param[in] soapaction pointer to the soapaction header field string.
 *  @remark soapaction is given as the following format on UPnP message,<br>
 *  "urn:shemas-upnp-org:service:serviceType:version#actionName"<br>
 *  If soapaction is given this format, this function parses the
 *  soapaction parameter and sets <br>
 *   x->uri : pointer to "urn:shemas-upnp-org:service:serviceType:version" string. <br>
 *   x->fragment: pointer to "actionName" string. <br>
 */
extern du_bool du_soapaction_parse(du_soapaction* x, const du_uchar* soapaction);

#ifdef __cplusplus
}
#endif

#endif
