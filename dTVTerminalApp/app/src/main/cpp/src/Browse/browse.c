/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include <dav_cds.h>
#include <dav_didl.h>
#include <dav_didl_libxml.h>
#include <du_str.h>
#include <du_alloc.h>
#include <du_byte.h>
#include <du_libxml.h>

#include "browse.h"
#include "dms_info.h"

#include "../DlnaDefine.h"

void browse_init(BrowseInfo* b, du_uint32 requested_count) {
    du_byte_zero((du_uint8*)b, sizeof(BrowseInfo));
    b->requestedCount = requested_count;
}

void browse_free(BrowseInfo* b) {
    dms_info_free(b->dmsInfo);
    du_alloc_free(b->containerId);
    if (b->didlDoc) xmlFreeDoc(b->didlDoc);
}

du_bool browse_set_dms(BrowseInfo* b, DmsInfo* di) {
    dms_info_free(b->dmsInfo);
    du_alloc_free(b->containerId);
    b->dmsInfo = 0;
    b->containerId = 0;

    b->dmsInfo = dms_info_clone(di);
    if (!b->dmsInfo) goto error;
    if (!du_str_clone(DU_UCHAR_CONST("0"), &b->containerId)) goto error;
    return 1;

error:
    dms_info_free(b->dmsInfo);
    du_alloc_free(b->containerId);
    b->dmsInfo = 0;
    b->containerId = 0;
    return 0;
}

const du_uchar* browse_get_control_url(BrowseInfo* b) {
    if (!b->dmsInfo) return 0;
    return b->dmsInfo->cdsInfo.controlUrl;
}

const du_uchar* browse_get_event_sub_url(BrowseInfo* b) {
    if (!b->dmsInfo) return 0;
    return b->dmsInfo->cdsInfo.eventSubscriptionUrl;
}

du_bool browse_set_container_id(BrowseInfo* b, const du_uchar* id) {
    du_alloc_free(b->containerId);
    b->containerId = 0;
    return du_str_clone(id, &b->containerId);
}

du_bool browse_set_response(BrowseInfo* b, const du_uchar* result) {
//    du_str_array param_array;
//    const du_uchar* result;
    xmlNodePtr root;
//    du_uint32 number_returned;
//    du_uint32 update_id;
    xmlDoc* didl_doc = 0;
    xmlNodePtr n = 0;
    xmlNodePtr n2 = 0;

    // parse browse response
//    du_str_array_init(&param_array);
//    if (!dav_cds_parse_browse_response(soap_response, soap_response_len, &param_array, &result, &number_returned, &b->totalMatches, &update_id)) goto error;
//
//    if (b->requestedCount < number_returned) goto error;
//
//    b->updateId = update_id;
//    b->numberReturned += number_returned;

    didl_doc = dav_didl_libxml_make_doc(result, du_str_len(result));
    if (!didl_doc) goto error;

    // verify the document root.
    root = xmlDocGetRootElement(didl_doc);
    if (!root) goto error;
    if (!dav_didl_libxml_element_name_equal(root, dav_didl_element_didl_lite())) goto error;

    if (!b->didlDoc) {
        b->didlDoc = didl_doc;
        didl_doc = 0;
    } else {
        du_uint32 i = 0;

        while ((n = du_libxml_get_element_by_index(root->children, i++))) {
            n2 = xmlCopyNodeList(n);
            if (!xmlAddChildList(xmlDocGetRootElement(b->didlDoc), n2)) goto error;
            n2 = 0;
        }
    }

    if (didl_doc) xmlFreeDoc(didl_doc);
//    du_str_array_free(&param_array);
    return 1;

error:
//    du_str_array_free(&param_array);
    if (didl_doc) xmlFreeDoc(didl_doc);
    if (n2) xmlFreeNodeList(n2);
    b->numberReturned = 0;
    b->totalMatches = 0;
    b->updateId = 0;
    return 0;
}
