/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifdef __cplusplus
extern "C" {
#endif

#include "DmsInfo.h"
#include <dav_urn.h>
#include <dupnp_urn.h>
#include <du_libxml.h>
//#include <du_uchar_array.h>
#include <du_uri.h>
#include <du_str.h>
#include <du_alloc.h>
//#include <libxml/parser.h>

// デバイスディスクリプションの情報
static const du_uchar *URN_DEVICE = DU_UCHAR_CONST("urn:schemas-upnp-org:device-1-0");

/**
 * デバイスタイプ
 *
 * @param node ノード
 * @param udn UDN
 * @param device_type デバイスタイプ
 * @return
 */
static du_bool isTargetDevice(xmlNodePtr node, const du_uchar *udn, const du_uchar *device_type) {
    xmlNodePtr nptr;
    du_uchar_array ua;
    const du_uchar *device_type_;

    du_uchar_array_init(&ua);

    // UDN(UUID) を取得する
    nptr = du_libxml_get_element_by_name(node->children, URN_DEVICE,
                                         DU_UCHAR_CONST(DMS_UUID_ELEMENT));
    if (!nptr) {
        goto error;
    }
    // UDN(UUID)を比較
    if (!du_libxml_content_equal(nptr, udn)) {
        goto error;
    }

    /**
     * デバイスタイプを取得する
     * 子ノード
     * 名前空間
     * デバイスタイプ要素
     */
    device_type_ = du_libxml_get_content_by_name_s(node->children, URN_DEVICE,
                                                   DU_UCHAR_CONST(DMS_DEVICE_TYPE), &ua);
    if (!device_type_) {
        goto error;
    }
    // デバイスタイプを比較
    if (!dupnp_urn_version_le(device_type, device_type_)) {
        goto error;
    }

    du_uchar_array_free(&ua);
    return 1;

    error:
    du_uchar_array_free(&ua);
    return 0;
}

/**
 * UDN とデバイスタイプで DMSを検索する
 *
 * @param doc XML Doc
 * @param children ノード
 * @param udn UDN
 * @param device_type デバイスタイプ
 * @return
 */
static xmlNodePtr findUdnXmlDoc(xmlDocPtr doc, xmlNodePtr children, const du_uchar *udn,
                                const du_uchar *device_type) {
    xmlNodePtr n;
    xmlNodePtr n2;

    for (n = children; n; n = n->next) {
        // device 要素
        if (!du_libxml_element_name_equal(n, URN_DEVICE, DU_UCHAR_CONST(DMS_DEVICE_ELEMENT))) {
            continue;
        }
        // デバイスタイプ
        if (isTargetDevice(n, udn, device_type)) {
            return n;
        }

        // embedded devices のチェック
        n2 = du_libxml_get_element_by_name(n->children, URN_DEVICE,
                                           DU_UCHAR_CONST(DMS_DEVICE_LIST_ELEMENT));
        if (!n2) {
            continue;
        }
        // 子要素の検索
        n2 = findUdnXmlDoc(doc, n2->children, udn, device_type);
        if (n2) {
            return n2;
        }
    }
    return 0;
}

/**
 * ノードを取得する
 *
 * @param children
 * @param service_type
 * @return
 */
static xmlNodePtr findServiceXmlDoc(xmlNodePtr children, const du_uchar *service_type) {
    xmlNodePtr n;
    xmlNodePtr n2;
    xmlNodePtr ret = 0;
    du_uchar_array ua;

    du_uchar_array_init(&ua);

    for (n = children; n; n = n->next) {
        if (!du_libxml_element_name_equal(n, URN_DEVICE, DU_UCHAR_CONST(DMS_SERVICE_ELEMENT))) {
            continue;
        }
        n2 = du_libxml_get_element_by_name(n->children, URN_DEVICE,
                                           DU_UCHAR_CONST(DMS_SERVICE_TYPE));
        if (!n2) {
            continue;
        }
        if (dupnp_urn_version_le(service_type, du_libxml_get_content_s(n2, &ua))) {
            ret = n;
            break;
        }
    }

    du_uchar_array_free(&ua);
    return ret;
}

/**
 *
 *
 * @param location
 * @param url
 * @param absolute_url
 * @return
 */
static du_bool
to_absolute_url(const du_uchar *location, const du_uchar *url, du_uchar **absolute_url) {
    du_uchar_array ua;

    du_uchar_array_init(&ua);
    if (!du_uri_resolve(location, url, &ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!du_str_clone(du_uchar_array_get(&ua), absolute_url)) {
        goto error;
    }
    du_uchar_array_free(&ua);
    return 1;

    error:
    du_uchar_array_free(&ua);
    return 0;
}

/**
 * プロパティの設定
 *
 * @param info
 * @param node
 * @param location
 * @return
 */
static du_bool setDmsInfoXmlDoc(dms_info *info, xmlNodePtr node, const du_uchar *location) {
    xmlNodePtr nptr;
    du_uchar_array ua;

    du_uchar_array_init(&ua);

    // UUIDを設定
    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE, DU_UCHAR_CONST(DMS_UUID_ELEMENT),
                                       &ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!du_str_clone(du_uchar_array_get(&ua), &info->udn)) {
        goto error;
    }

    // サービスリストを設定
    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE,
                                       DU_UCHAR_CONST(DMS_FRIENDLY_NAME_ELEMENT), &ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!du_str_clone(du_uchar_array_get(&ua), &info->friendly_name)) {
        goto error;
    }

    // サービスリストを設定
    nptr = du_libxml_get_element_by_name(node->children, URN_DEVICE,
                                         DU_UCHAR_CONST(DMS_SERVICE_LIST));
    if (!nptr) {
        goto error;
    }
    nptr = findServiceXmlDoc(nptr->children, dav_urn_cds(1));
    if (!nptr) {
        goto error;
    }

    // コントロールURLを設定
    if (!du_libxml_get_content_by_name(nptr->children, URN_DEVICE, DU_UCHAR_CONST(DMS_CONTROL_URL),
                                       &ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!to_absolute_url(location, du_uchar_array_get(&ua), &info->cds.control_url)) {
        goto error;
    }

    // イベントサブURLを設定
    if (!du_libxml_get_content_by_name(nptr->children, URN_DEVICE, DU_UCHAR_CONST(DMS_EVENTSUB_URL),
                                       &ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!to_absolute_url(location, du_uchar_array_get(&ua), &info->cds.event_sub_url)) {
        goto error;
    }

    //STB2号機限定用情報の追加
    // モデル名を設定
    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE,
                                       DU_UCHAR_CONST(DMS_MODEL_NAME), &ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!du_str_clone(du_uchar_array_get(&ua), &info->modelName)) {
        goto error;
    }

    // 製造元を設定
    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE,
                                       DU_UCHAR_CONST(DMS_MANUFACTURER),&ua)) {
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) {
        goto error;
    }
    if (!du_str_clone(du_uchar_array_get(&ua), &info->manufacture)) {
        goto error;
    }

    du_uchar_array_free(&ua);
    return 1;

    error:
    du_uchar_array_free(&ua);
    return 0;
}

/**
 * <URLBase> の判定
 *
 * @param node
 * @param ua
 * @return
 */
static du_bool findUrlBaseXmlDoc(xmlNodePtr node, du_uchar_array *ua) {
    if (!du_libxml_get_content_by_name(node->children, URN_DEVICE,
                                       DU_UCHAR_CONST(DMS_URLBASE_ELEMENT), ua)) {
        return 0;
    }
    return 1;
}

/**
 * デバイスディスクリプションを解析してデバイス情報を設定する
 *
 * @param xml
 * @param xml_len
 * @param udn
 * @param device_type
 * @param location
 * @return
 */
dms_info *createDmsInfoXmlDoc(const du_uchar *xml, du_uint32 xml_len, const du_uchar *udn,
                              const du_uchar *device_type, const du_uchar *location) {
    xmlDocPtr doc = 0;
    xmlNodePtr node;
    dms_info *info = 0;
    du_uchar_array url_base;

    du_uchar_array_init(&url_base);

    // デバイスディスクリプションをパースする
    doc = xmlReadMemory((const char *) xml, (int) xml_len, DMS_DEVICE_DESCRIPTION, 0,
                        XML_PARSE_NOERROR | XML_PARSE_NOWARNING | XML_PARSE_NOBLANKS |
                        XML_PARSE_NONET);
    if (!doc) {
        goto error;
    }

    // ルート要素を取得
    node = xmlDocGetRootElement(doc);
    if (!node) {
        goto error;
    }
    if (!du_libxml_element_name_equal(node, URN_DEVICE, DU_UCHAR_CONST(DMS_ROOT_ELEMENT))) {
        goto error;
    }

    // <URLBase> の判定
    if (findUrlBaseXmlDoc(node, &url_base)) {
        if (!du_uchar_array_cat0(&url_base)) {
            goto error;
        }
        // <URLBase> の取得
        location = du_uchar_array_get(&url_base);
    }

    // UDN とデバイスタイプで DMSを検索する
    node = findUdnXmlDoc(doc, node->children, udn, device_type);
    if (!node) {
        goto error;
    }

    // メモリー初期化
    info = du_alloc_zero(sizeof(dms_info));
    if (!info) {
        goto error;
    }

    // プロパティの設定
    if (!setDmsInfoXmlDoc(info, node, location)) {
        goto error;
    }

    xmlFreeDoc(doc);
    du_uchar_array_free(&url_base);
    return info;

    error:
    if (doc) {
        xmlFreeDoc(doc);
    }
    freeDmsInfoXmlDoc(info);
    du_uchar_array_free(&url_base);
    return 0;
}

/**
 * DMS情報を解放
 *
 * @param info
 */
void freeDmsInfoXmlDoc(dms_info *info) {
    if (!info) {
        return;
    }
    du_alloc_free(info->udn);
    du_alloc_free(info->friendly_name);
    du_alloc_free(info->cds.control_url);
    du_alloc_free(info->cds.event_sub_url);
    du_alloc_free(info);
}

#ifdef __cplusplus
}
#endif
