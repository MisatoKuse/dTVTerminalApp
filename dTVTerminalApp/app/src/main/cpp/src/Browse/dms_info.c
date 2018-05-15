/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include <dav_urn.h>
#include <dupnp_urn.h>
#include <du_libxml.h>
#include <du_uchar_array.h>
#include <du_uri.h>
#include <du_str.h>
#include <du_alloc.h>
#include <libxml/parser.h>

#include "dms_info.h"
#include "../DlnaDefine.h"

static const du_uchar* URN_DEVICE = DU_UCHAR_CONST("urn:schemas-upnp-org:device-1-0");

static du_bool is_target_device(xmlNodePtr node, const du_uchar* udn, const du_uchar* device_type) {
    xmlNodePtr n;
    du_uchar_array ua;
    const du_uchar* device_type_;

    du_uchar_array_init(&ua);

    n = du_libxml_get_element_by_name(node->children, URN_DEVICE, DU_UCHAR_CONST("UDN"));
    if (!n) goto error;
    if (!du_libxml_content_equal( n, udn)) goto error;

    device_type_ = du_libxml_get_content_by_name_s(node->children, URN_DEVICE, DU_UCHAR_CONST("deviceType"), &ua);
    if (!device_type_) goto error;

    if (!dupnp_urn_version_le(device_type, device_type_)) goto error;

    du_uchar_array_free(&ua);
    return 1;

error:
    du_uchar_array_free(&ua);
    return 0;
}

static xmlNodePtr find_dms(xmlDocPtr doc, xmlNodePtr children, const du_uchar* udn, const du_uchar* device_type) {
    xmlNodePtr n;
    xmlNodePtr n2;

    for (n = children; n; n = n->next) {
        if (!du_libxml_element_name_equal(n, URN_DEVICE, DU_UCHAR_CONST("device"))) continue;
        if (is_target_device(n, udn, device_type)) return n;

        // check embedded devices.
        n2 = du_libxml_get_element_by_name(n->children, URN_DEVICE, DU_UCHAR_CONST("deviceList"));
        if (!n2) continue;
        n2 = find_dms(doc, n2->children, udn, device_type);
        if (n2) return n2;
    }
    return 0;
}

static xmlNodePtr find_service(xmlNodePtr children, const du_uchar* service_type) {
    xmlNodePtr n;
    xmlNodePtr n2;
    xmlNodePtr ret = 0;
    du_uchar_array ua;

    du_uchar_array_init(&ua);

    for (n = children; n; n = n->next) {
        if (!du_libxml_element_name_equal(n, URN_DEVICE, DU_UCHAR_CONST("service"))) continue;
        n2 = du_libxml_get_element_by_name(n->children, URN_DEVICE, DU_UCHAR_CONST("serviceType"));
        if (!n2) continue;
        if (dupnp_urn_version_le(service_type, du_libxml_get_content_s(n2, &ua))) {
            ret = n;
            break;
        }
    }

    du_uchar_array_free(&ua);
    return ret;
}

static du_bool to_absolute_url(const du_uchar* location, const du_uchar* url, du_uchar** absolute_url) {
    du_uchar_array ua;

    du_uchar_array_init(&ua);
    if (!du_uri_resolve(location, url, &ua)) goto error;
    if (!du_uchar_array_cat0(&ua)) goto error;
    if (!du_str_clone(du_uchar_array_get(&ua), absolute_url)) goto error;
    du_uchar_array_free(&ua);
    return 1;

error:
    du_uchar_array_free(&ua);
    return 0;
}

static du_bool set_dms_info(DmsInfo* info, xmlNodePtr node, const du_uchar* location) {
    xmlNodePtr n;
    du_uchar_array ua;

    du_uchar_array_init(&ua);

    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE, DU_UCHAR_CONST("UDN"), &ua)) goto error;
    if (!du_uchar_array_cat0(&ua)) goto error;
    if (!du_str_clone(du_uchar_array_get(&ua), &info->udn)) goto error;

    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE, DU_UCHAR_CONST("friendlyName"), &ua)) goto error;
    if (!du_uchar_array_cat0(&ua)) goto error;
    if (!du_str_clone(du_uchar_array_get(&ua), &info->friendly_name)) goto error;

    n = du_libxml_get_element_by_name(node->children, URN_DEVICE, DU_UCHAR_CONST("serviceList"));
    if (!n) goto error;
    n = find_service(n->children, dav_urn_cds(1));
    if (!n) goto error;

    if (!du_libxml_get_content_by_name(n->children, URN_DEVICE, DU_UCHAR_CONST("controlURL"), &ua)) goto error;
    if (!du_uchar_array_cat0(&ua)) goto error;
    if (!to_absolute_url(location, du_uchar_array_get(&ua), &info->cdsInfo.controlUrl)) goto error;

    if (!du_libxml_get_content_by_name(n->children, URN_DEVICE, DU_UCHAR_CONST("eventSubURL"), &ua)) goto error;
    if (!du_uchar_array_cat0(&ua)) goto error;
    if (!to_absolute_url(location, du_uchar_array_get(&ua), &info->cdsInfo.eventSubscriptionUrl)) goto error;

    du_uchar_array_free(&ua);
    return 1;

error:
    du_uchar_array_free(&ua);
    return 0;
}

static du_bool find_url_base(xmlNodePtr node, du_uchar_array* ua) {
    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE, DU_UCHAR_CONST("URLBase"), ua)) return 0;
    return 1;
}

DmsInfo* dms_info_create(const du_uchar* xml, du_uint32 xml_len, const du_uchar* udn, const du_uchar* device_type, const du_uchar* location) {
    xmlDocPtr doc = 0;
    xmlNodePtr node;
    DmsInfo* info = 0;
    du_uchar_array url_base;

    du_uchar_array_init(&url_base);

    // parse device description.
    doc = xmlReadMemory((const char*)xml, (int)xml_len, "device_description", 0, XML_PARSE_NOERROR | XML_PARSE_NOWARNING | XML_PARSE_NOBLANKS | XML_PARSE_NONET);
    if (!doc) goto error;

    // get the root element.
    node = xmlDocGetRootElement(doc);
    if (!node) goto error;
    if (!du_libxml_element_name_equal(node, URN_DEVICE, DU_UCHAR_CONST("root"))) goto error;

    // find the url base.
    if (find_url_base(node, &url_base)) {
        if (!du_uchar_array_cat0(&url_base)) goto error;
        location = du_uchar_array_get(&url_base);
    }

    // find the dms which has specified UDN and device type.
    node = find_dms(doc, node->children, udn, device_type);
    if (!node) goto error;

    // allocate initialized memory.
    info = du_alloc_zero(sizeof(DmsInfo));
    if (!info) goto error;

    // set properties.
    if (!set_dms_info(info, node, location)) goto error;

    xmlFreeDoc(doc);
    du_uchar_array_free(&url_base);
    return info;

error:
    if (doc) xmlFreeDoc(doc);
    dms_info_free(info);
    du_uchar_array_free(&url_base);
    return 0;
}

void dms_info_free(DmsInfo* info) {
    if (!info) return;
    du_alloc_free(info->udn);
    du_alloc_free(info->friendly_name);
    du_alloc_free(info->cdsInfo.controlUrl);
    du_alloc_free(info->cdsInfo.eventSubscriptionUrl);
    du_alloc_free(info);
}

DmsInfo* dms_info_clone(DmsInfo* info) {
    DmsInfo* i;

    i = du_alloc_zero(sizeof(DmsInfo));
    if (!du_str_clone(info->udn, &i->udn)) goto error;
    if (!du_str_clone(info->friendly_name, &i->friendly_name)) goto error;
    if (!du_str_clone(info->cdsInfo.controlUrl, &i->cdsInfo.controlUrl)) goto error;
    if (!du_str_clone(info->cdsInfo.eventSubscriptionUrl, &i->cdsInfo.eventSubscriptionUrl)) goto error;
    return i;

error:
    dms_info_free(i);
    return 0;
}
