/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include "DlnaDmsBrowse.h"

#include <vector>
#include <dcu_converter_arib.h>
#include <dav_didl_libxml.h>
#include <du_csv.h>
#include <du_param.h>
#include <dupnp_gena.h>
#include <dupnp_soap.h>
#include <dav_dc_date.h>
#include <dav_capability_libxml.h>
#include <dav_cds.h>
#include <string.h>

#include "DlnaMacro.h"
#include "Browse/browse.h"
#include "DiRAG/dvcdsc_device.h"

#define READ_TIMEOUT_MS 30000

#define MY_LOG_FLAG
#ifdef MY_LOG_FLAG
    #define MY_LOG(fmt, ...) LOG_WITH(fmt, ##__VA_ARGS__)
#else
    #define MY_LOG(fmt, ...)
#endif


std::function<void(std::vector<ContentInfo> contentList, const char* containerId, bool complete)> DlnaDmsBrowse::ContentBrowseCallback = nullptr;
std::function<void(const char* containerId, eDlnaErrorType error)> DlnaDmsBrowse::ContentBrowseErrorCallback = nullptr;
std::function<void(const char* containerUpdateIds)> DlnaDmsBrowse::EventHandlerCallback = nullptr;

extern du_bool browseDirectChildren2(DMP* d, const du_uchar* controlUrl);

const du_uchar* BDC_FILTER = DU_UCHAR_CONST("@id=\"\",@restricted=\"0\",dc:title,upnp:class,res,res@protocolInfo,res@duration,res@dlna:cleartextSize,upnp:genre,arib:objectType,upnp:rating,upnp:channelName,upnp:channelNr,dc:date,@res@allowedUse,upnp:lastPlaybackPosition,res@bitrate");

bool DlnaDmsBrowse::selectContainerWithContainerId(DMP *dmp, du_uint32 offset, du_uint32 limit, const du_uchar* containerId, const du_uchar* controlUrlString) {
    LOG_WITH_PARAM("containerId = %s, offset = %u, limit = %u", containerId, offset, limit);
    du_alloc_free(dmp->browseInfo.containerId);
    dmp->browseInfo.containerId = 0;
    du_str_clone(containerId, &dmp->browseInfo.containerId);
    return browseDirectChildren(dmp, offset, controlUrlString, limit);
}

du_bool DlnaDmsBrowse::selectContainer(DMP* d, du_uint32 index) {
    LOG_WITH_PARAM("index = %u", index);
    xmlNodePtr root;
    xmlNodePtr node;
    du_uint32 i;
    du_uchar_array ua;
    
    du_mutex_lock(&d->soapInfo.mutex);
    du_uchar_array_init(&ua);
    
    // check the range of the list.
    if (index < d->browseInfo.startingIndex || d->browseInfo.startingIndex + d->browseInfo.numberReturned <= index) {
        LOG_WITH_PARAM("print_out_of_range");
        goto error;
    }
    
    // get the object by index.
    i = d->browseInfo.startingIndex;
    root = xmlDocGetRootElement(d->browseInfo.didlDoc);
    for (node = root->children; node; node = node->next) {
        if (!dav_didl_libxml_element_name_equal(node, dav_didl_element_container()) && !dav_didl_libxml_element_name_equal(node, dav_didl_element_item())) continue;
        if (i == index) break;
        ++i;
    }
    if (!node) {
        LOG_WITH_PARAM("print_not_found");
        goto error;
    }
    
    // check if the object is a container.
    if (!dav_didl_libxml_element_name_equal(node, dav_didl_element_container())) {
        LOG_WITH_PARAM("not_container");
        goto error;
    }
    
    // get the object ID.
    if (!dav_didl_libxml_get_attribute_value_by_name(node, dav_didl_attribute_id(), &ua)) {
        LOG_WITH_PARAM("unexpected_error_occurred");
        goto error;
    }
    if (!du_uchar_array_cat0(&ua)) goto error;
    
    // set the object ID.
    if (!browse_set_container_id(&d->browseInfo, du_uchar_array_get(&ua))) {
        LOG_WITH_PARAM("d->browseInfo.container_id = %s", du_uchar_array_get(&ua));
        
        LOG_WITH_PARAM("unexpected_error_occurred");
        goto error;
    }
    
    du_uchar_array_free(&ua);
    du_mutex_unlock(&d->soapInfo.mutex);
    LOG_WITH_PARAM("success");
    return 1;
    
error:
    du_uchar_array_free(&ua);
    du_mutex_unlock(&d->soapInfo.mutex);
    LOG_WITH_PARAM("error");
    return 0;
}

bool getDeviceInfoVisitor(dupnp_cp_dvcmgr_device* device, void* arg) {
    SelectDmsContext* context = static_cast<SelectDmsContext*>(arg);
    LOG_WITH("context->current_index = %u", context->currentIndex);
    dvcdsc_device* info = static_cast<dvcdsc_device*>(device->user_data);
    bool result = false;
    DmsInfo dmsInfo;
    do {
        BREAK_IF(!du_str_clone(info->udn, &dmsInfo.udn));
        BREAK_IF(!du_str_clone(info->friendly_name, &dmsInfo.friendly_name));
        BREAK_IF(!du_str_clone(info->cds.control_url, &dmsInfo.cdsInfo.controlUrl));
        BREAK_IF(!du_str_clone(info->cds.event_sub_url, &dmsInfo.cdsInfo.eventSubscriptionUrl));
        result = true;
    } while (false);

    result &= browse_set_dms(&context->dmp->browseInfo, &dmsInfo);
    return result;
}

/** gena event handler. */
du_bool isContainerUpdated(const du_uchar* container_update_ids_list, const du_uchar* container_id, du_uint32 update_id) {
    const du_uchar* s;
    du_str_array param_array;
    du_uint32 id;
    
    du_str_array_init(&param_array);
    
    // parse ContainerUpdateIDs string.
    if (!du_csv_split(container_update_ids_list, &param_array)) goto error;
    
    // get the container update ID of the given container.
    s = du_param_get_value_by_name(&param_array, container_id);
    if (!s) goto error;
    
    // test if the update ID is updated.
    if (!du_str_scan_uint32(s, &id)) goto error;
    if (id == update_id) goto error;
    
    du_str_array_free(&param_array);
    return 1;
    
error:
    du_str_array_free(&param_array);
    return 0;
}

void eventHandler(dupnp_cp_event_info* info, void* arg) {
    LOG_WITH(">>> START");
    DMP* d = static_cast<DMP*>(arg);
    du_str_array param_array;
    const du_uchar* s;
    
    du_str_array_init(&param_array);
    
    if (info->error != DUPNP_CP_EVENT_ERROR_NONE) goto error;
    
    // parse event.
    if (!dupnp_gena_parse_notify(&param_array, info->property_set_xml, info->property_set_xml_size)) goto error;
    
    // check SystemUpdateID.
    if (!du_param_get_value_by_name(&param_array, DU_UCHAR_CONST("SystemUpdateID"))) goto error;
    
    // check ContainerUpdateIDs.
    s = du_param_get_value_by_name(&param_array, DU_UCHAR_CONST("ContainerUpdateIDs"));
    if (s) {
        LOG_WITH("check_update container_id");
        if (isContainerUpdated(s, d->browseInfo.containerId, d->browseInfo.updateId)) {
            LOG_WITH("Current container updated");
        }
    } else {
        // ContainerUpdateIDs variable is not supported.
        // you should check the update ID of the current container by BrowseMetadata.
    }
    
    if (DlnaDmsBrowse::EventHandlerCallback != nullptr) {
        if (s) {
            DlnaDmsBrowse::EventHandlerCallback((char*)s);
        } else {
            const char* empty = "";
            DlnaDmsBrowse::EventHandlerCallback(empty);
        }
    }
    
    LOG_WITH("<<< END");
    du_str_array_free(&param_array);
    return;
    
error:
    du_str_array_free(&param_array);
    LOG_WITH("invalid_event");
}

void subscribe(DMP* d) {
    const du_uchar* s;
    
    s = browse_get_event_sub_url(&d->browseInfo);
    if (!s) return;
    LOG_WITH("event_sub_url = %s", s);
    dupnp_cp_evtmgr_subscribe(&d->eventManager, s, eventHandler, d);
}

void cancelIfInProgress(DMP* d) {
    if (d->soapInfo.taskId != DUPNP_INVALID_ID) {
        dupnp_http_cancel(&d->upnpInstance, d->soapInfo.taskId);
        
        // wait until the cancel is completed.
        while (d->soapInfo.taskId != DUPNP_INVALID_ID) {
            du_sync_wait(&d->soapInfo.sync, &d->soapInfo.mutex);
        }
    }
}

du_bool checkSoapResponseError(dupnp_http_response* response, du_bool* completed) {
    du_str_array param_array;
    
    du_str_array_init(&param_array);
    
    // check cancel.
    if (response->error == DU_SOCKET_ERROR_CANCELED) {
        LOG_WITH("print_canceled");
        goto error;
    }
    
    // check general error.
    if (response->error != DU_SOCKET_ERROR_NONE) {
        LOG_WITH("print_network_error");
        goto error;
    }

    // check SOAP error.
    if (du_str_equal(response->status, du_http_status_internal_server_error())) {
        const du_uchar* soap_error_code;
        const du_uchar* soap_error_description;
        
        if (!dupnp_soap_parse_error_response(response->body, response->body_size, &param_array, &soap_error_code, &soap_error_description)) goto error;

        if (du_str_equal(soap_error_code, dav_cds_error_code_cannot_process_the_request())) {
            *completed = 1;
            du_str_array_free(&param_array);
            return 1;
        }
        
        LOG_WITH("print_soap_error(soap_error_code = %s, soap_error_description) = %s", soap_error_code, soap_error_description);
        goto error;
    }
    
    // check HTTP error.
    if (!du_http_status_is_successful(response->status)) {
        LOG_WITH("print_http_error(response->status) = %s", response->status);
        goto error;
    }
    
    if (du_str_equal(response->status, du_http_status_internal_server_error())) {
        const du_uchar* soap_error_code;
        const du_uchar* soap_error_description;
        dupnp_soap_parse_error_response(response->body, response->body_size, &param_array, &soap_error_code, &soap_error_description);
        LOG_WITH("soap_error_code = %s, soap_error_description = %s", soap_error_code, soap_error_description);
        goto error;
        
    }
    
    
    du_str_array_free(&param_array);
    return 1;
    
error:
    du_str_array_free(&param_array);
    return 0;
}

void printObject(DMP* d, const xmlNodePtr element, du_uint32 index, du_bool is_container, dav_capability_class cls, ContentInfo &contentInfo) {
    xmlChar* s = 0;
    xmlNodePtr p;
    du_uchar dc_date[DAV_DC_DATE_SIZE];
    du_uchar_array ua;
    xmlNodePtr curNode;
    int resPos = 0;
    int resCount = 0;
    
    du_uchar_array_init(&ua);
    
    // title
    if (!dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_dc_title(), &ua)) goto error;
    if (!du_uchar_array_cat0(&ua)) goto error;
    MY_LOG("%u. [%s]%s", index, is_container ? "C" : "I", du_uchar_array_get(&ua));
    contentInfo.isContainer = is_container;
    contentInfo.index = index;
    snprintf(contentInfo.name, sizeof(contentInfo.name), "%s",du_uchar_array_get(&ua));

    // date
    *dc_date = 0;
    if (dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_dc_date(), &ua)) {
        if (!du_uchar_array_cat0(&ua)) goto error;
        if (!dav_dc_date_normalize(du_uchar_array_get(&ua), dc_date)) goto error;
        // dc_date[du_str_chr(dc_date, (du_uchar)'T')] = 0;
        // snprintf(contentInfo.date,sizeof(contentInfo.date), "%s",dc_date);
        // LOG_WITH("   date: %s", dc_date);
        snprintf(contentInfo.date,sizeof(contentInfo.date), "%s",du_uchar_array_get(&ua));
        MY_LOG("   date: %s", du_uchar_array_get(&ua));
    }
    
    curNode = element->children;
    while(curNode != NULL)
    {
        if ((!xmlStrcmp(curNode->name, (const xmlChar *)"res"))){
            resCount ++;
        }
        curNode = curNode->next;
    }
    
    if (resCount > 0) {
        resPos = resCount - 1;
    }
    
    // thumbnail URL
    p = dav_capability_libxml_get_supported_property2(&d->davCapability, element, DAV_CAPABILITY_THUMBNAIL, resPos);
    if (p) {
        s = xmlNodeListGetString(d->browseInfo.didlDoc, p->children, 1);
        if (!s) goto error;
        MY_LOG("   thumbnail: %s", s);
        snprintf(contentInfo.thumbnailPath, sizeof(contentInfo.thumbnailPath), "%s" , s);
        xmlFree(s);
        s = 0;
    }

    // content URL
    if (!is_container) {
        p = dav_capability_libxml_get_supported_property2(&d->davCapability, element, cls, resPos);
        if (p) {
            s = xmlNodeListGetString(d->browseInfo.didlDoc, p->children, 1);
            if (!s) goto error;
            snprintf(contentInfo.contentPath, sizeof(contentInfo.contentPath), "%s" , s);
            MY_LOG("   content: %s", s);
            xmlFree(s);
            s = 0;
            
            // duration
            if (dav_didl_libxml_get_attribute_value_by_name(p, dav_didl_attribute_duration(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.duration, sizeof(contentInfo.duration), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   duration: %s", du_uchar_array_get(&ua));
            }
            
            // size
            if (dav_didl_libxml_get_attribute_value_by_name(p, dav_didl_attribute_size(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.size, sizeof(contentInfo.size), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   size: %s", du_uchar_array_get(&ua));
            }

            // cleartextSize
            if (dav_didl_libxml_get_attribute_value_by_name(p, dav_didl_attribute_dlna_cleartext_size(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.cleartextSize, sizeof(contentInfo.cleartextSize), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   size: %s", du_uchar_array_get(&ua));
            }

            // protocol_info
            if (dav_didl_libxml_get_attribute_value_by_name(p, dav_didl_attribute_protocol_info(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.protocolInfo, sizeof(contentInfo.protocolInfo), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   protocol_info: %s", du_uchar_array_get(&ua));
            }

            // objectId
            if (dav_didl_libxml_get_attribute_value_by_name(element, dav_didl_attribute_id(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.objectId, sizeof(contentInfo.objectId), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   objectId: %s", du_uchar_array_get(&ua));
            }
            
            // upnp:genre
            if (dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_upnp_genre(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.genre, sizeof(contentInfo.genre), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   genre : %s", du_uchar_array_get(&ua));
            }
            
            // upnp:class
            if (dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_upnp_class(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.uClass, sizeof(contentInfo.uClass), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   class : %s", du_uchar_array_get(&ua));
            }
            
            // upnp:channelName
            if (dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_upnp_channel_name(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.channelName, sizeof(contentInfo.channelName), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   channelName : %s", du_uchar_array_get(&ua));
            }
            
            // upnp:channelNr
            if (dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_upnp_channel_nr(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.channelNr, sizeof(contentInfo.channelNr), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   channelNr : %s", du_uchar_array_get(&ua));
            }
            
            // upnp:rating
            if (dav_didl_libxml_get_content_by_name(element->children, dav_didl_element_upnp_rating(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.rating, sizeof(contentInfo.rating), "%s" , du_uchar_array_get(&ua));
                LOG_WITH("   rating : %s", du_uchar_array_get(&ua));
            }

            // bitrate
            if (dav_didl_libxml_get_attribute_value_by_name(p, dav_didl_attribute_bitrate(), &ua)) {
                if (!du_uchar_array_cat0(&ua)) goto error;
                snprintf(contentInfo.bitrate, sizeof(contentInfo.bitrate), "%s" , du_uchar_array_get(&ua));
                MY_LOG("   bitrate: %s", du_uchar_array_get(&ua));
            }

        } else {
            MY_LOG("   Unsupported Format.");
        }
    }

    du_uchar_array_free(&ua);
    return;
    
error:
    if (s) xmlFree(s);
    du_uchar_array_free(&ua);
    LOG_WITH("print_unexpected_error_occurred");
}

void listObject(DMP* d, const du_uchar* result) {
    du_uint32 i;
    du_uint32 number_returned;
    xmlNodePtr root;
    xmlNodePtr node;
    dav_capability_class cls;
    du_bool is_container;
    std::vector<ContentInfo> contentList;
    number_returned = d->browseInfo.numberReturned;
    if (d->browseInfo.requestedCount < number_returned) number_returned = d->browseInfo.requestedCount;
    
    xmlChar *xmlbuff;
    int buffersize;
    xmlDocDumpFormatMemory(d->browseInfo.didlDoc, &xmlbuff, &buffersize, 1);
    LOG_WITH("[xml] list_object :d->browse.didl_doc: %s", xmlbuff); //response xml確認時使用
    
    i = 0;
    root = xmlDocGetRootElement(d->browseInfo.didlDoc);
    for (node = root->children; node; node = node->next) {
        if (dav_didl_libxml_element_name_equal(node, dav_didl_element_container())) {
            is_container = 1;
            cls = (dav_capability_class)0;
        } else if (dav_didl_libxml_element_name_equal(node, dav_didl_element_item())) {
            is_container = 0;
            if (!dav_capability_libxml_get_media_class_from_object(&d->davCapability, node, &cls)) continue;
        } else {
            continue;
        }
        ContentInfo content;
        memset(&content, 0x0, sizeof(ContentInfo));
        printObject(d, node, d->browseInfo.startingIndex + i, is_container, cls, content);
        if (i == 0) {
            content.xml = (char*)result;
        }
        contentList.push_back(content);
        ++i;
        if (i == number_returned) break;
    }
    LOG_WITH("====================================");
    if (number_returned) {
        LOG_WITH("index: %u-%u, total:%u\n", d->browseInfo.startingIndex, d->browseInfo.startingIndex + number_returned - 1, d->browseInfo.totalMatches);
    } else {
        LOG_WITH("no content");
    }
    du_bool completed = 0;
    if (d->browseInfo.totalMatches == 0 && number_returned == 0) {
        completed = 1;
    } else if (d->browseInfo.totalMatches >= 1) {
        if (d->browseInfo.startingIndex + number_returned == d->browseInfo.totalMatches) {
            completed = 1;
        }
    }
    auto containerId = d->browseInfo.containerId;
    if(DlnaDmsBrowse::ContentBrowseCallback) {
        DlnaDmsBrowse::ContentBrowseCallback(contentList, (const char*)containerId, completed);
    }
    return;
}

void DlnaDmsBrowse::browseDirectChildrenResponseHandler(dupnp_http_response* response, void* arg) {
    LOG_WITH(">>> response->url = %s", response->url);
    LOG_WITH("START response->body = %s", response->body);// 実際のxmlを確認したい場合にコメントアウトを外してください。
    DMP* d = static_cast<DMP*>(arg);
    
    du_mutex_lock(&d->soapInfo.mutex);
    d->soapInfo.taskId = DUPNP_INVALID_ID;
    DlnaDmsBrowse *thiz = (DlnaDmsBrowse *) arg;
    du_str_array param_array;
    du_str_array_init(&param_array);
    const du_uchar* soap_response = response->body;
    du_uint32 soap_response_len = response->body_size;
    const du_uchar* result;
    du_uint32 number_returned;
    BrowseInfo* b = &d->browseInfo;
    du_uint32 update_id;
    du_bool completed = 0;

    if (!checkSoapResponseError(response, &completed)) goto error;

    if (completed) {
        goto error1;
    }

    // parse browse response
    if (!dav_cds_parse_browse_response(soap_response, soap_response_len, &param_array, &result, &number_returned, &b->totalMatches, &update_id)) goto error1;
    if (b->requestedCount < number_returned) goto error1;
    b->updateId = update_id;
    b->numberReturned += number_returned;

    if (!browse_set_response(&d->browseInfo, result)) goto error;
    if (d->browseInfo.startingIndex + d->browseInfo.numberReturned < d->browseInfo.totalMatches && d->browseInfo.numberReturned < d->browseInfo.requestedCount) {
        browseDirectChildren2(d, nullptr);
    } else {
        listObject(d, result);
    }
    du_str_array_free(&param_array);
    du_sync_notify(&d->soapInfo.sync);
    du_mutex_unlock(&d->soapInfo.mutex);
    LOG_WITH("<<<");
    return;
error1:
    du_sync_notify(&d->soapInfo.sync);
    du_mutex_unlock(&d->soapInfo.mutex);
    du_str_array_free(&param_array);
    b->numberReturned = 0;
    b->totalMatches = 0;
    b->updateId = 0;
error:
    LOG_WITH("<<< ERROR");
    du_sync_notify(&d->soapInfo.sync);
    du_mutex_unlock(&d->soapInfo.mutex);
    //コンテンツブラウズエラー
    auto containerId = d->browseInfo.containerId;
    if (completed) {
        if(DlnaDmsBrowse::ContentBrowseCallback) {
            std::vector<ContentInfo> contentList;
            DlnaDmsBrowse::ContentBrowseCallback(contentList, (const char*)containerId, completed);
        }
    } else {
        if(DlnaDmsBrowse::ContentBrowseErrorCallback) {
            DlnaDmsBrowse::ContentBrowseErrorCallback((const char*)containerId, responseError);
        }
    }
}

du_bool browseDirectChildren2(DMP* d, const du_uchar* controlUrl) {
    LOG_WITH(">>>");
    du_uchar_array request_body;
    
    du_uchar_array_init(&request_body);
    du_mutex_lock(&d->soapInfo.mutex);
    auto containerId = d->browseInfo.containerId;
    auto startingIndex = d->browseInfo.startingIndex + d->browseInfo.numberReturned;
    auto requestedCount = d->browseInfo.requestedCount - d->browseInfo.numberReturned;
    auto sortCriteria = DU_UCHAR_CONST("");
    if (du_str_case_end(containerId, (const du_uchar*) "rec/all")) {
        sortCriteria = DU_UCHAR_CONST("-dc:date");
    }
    LOG_WITH("containerId = %s, startingIndex = %u, requestedCount = %d", containerId, startingIndex, requestedCount);
    if (!dav_cds_make_browse(&request_body, 1, containerId, DU_UCHAR_CONST("BrowseDirectChildren"), BDC_FILTER, startingIndex, requestedCount, sortCriteria)) goto error;
    
    if (!dupnp_soap_header_set_soapaction(&d->soapInfo.requestHeader, dav_urn_cds(1), DU_UCHAR_CONST("Browse"))) goto error;
//    LOG_WITH("d->browseInfo.dms->cds.control_url = %s", d->browseInfo.dmsInfo->cdsInfo.controlUrl);
    if (d->browseInfo.dmsInfo != nullptr) {
        controlUrl = d->browseInfo.dmsInfo->cdsInfo.controlUrl;
    }
        LOG_WITH("d->browseInfo.dms->cds.control_url = %s", controlUrl);
    if (!dupnp_http_soap(&d->upnpInstance, controlUrl, &d->soapInfo.requestHeader, du_uchar_array_get(&request_body), du_uchar_array_length(&request_body), READ_TIMEOUT_MS, DlnaDmsBrowse::browseDirectChildrenResponseHandler, d, &d->soapInfo.taskId)) goto error;
//    LOG_WITH("request_body = %s", du_uchar_array_get(&request_body)); //soapリクエストの中身を見たい場合に使ってください。
    du_mutex_unlock(&d->soapInfo.mutex);
    du_uchar_array_free(&request_body);
    LOG_WITH("<<<");
    return 1;
    
error:
    LOG_WITH("<<< error");
    du_mutex_unlock(&d->soapInfo.mutex);
    du_uchar_array_free(&request_body);
    return 0;
}

#pragma mark - Instance method
DlnaDmsBrowse::DlnaDmsBrowse() {
    DlnaDmsBrowse::ContentBrowseCallback = nullptr;
    DlnaDmsBrowse::ContentBrowseErrorCallback = nullptr;
    DlnaDmsBrowse::EventHandlerCallback = nullptr;
}

DlnaDmsBrowse::~DlnaDmsBrowse() {
    DlnaDmsBrowse::ContentBrowseCallback = nullptr;
    DlnaDmsBrowse::ContentBrowseErrorCallback = nullptr;
    DlnaDmsBrowse::EventHandlerCallback = nullptr;
}

bool DlnaDmsBrowse::browseDirectChildren(DMP* d, du_uint32 offset, const du_uchar* controlUrlString, du_uint32 limit) {
    bool result = false;
    LOG_WITH_PARAM(">>>");
    cancelIfInProgress(d);
    d->browseInfo.requestedCount = limit;
    d->browseInfo.startingIndex = offset;
    d->browseInfo.numberReturned = 0;
    d->browseInfo.updateId = 0;
    if (d->browseInfo.didlDoc){
        xmlFreeDoc(d->browseInfo.didlDoc);
    }
    d->browseInfo.didlDoc = 0;
    do {
        BREAK_IF(!browseDirectChildren2(d, controlUrlString));
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, " <<<");
    return result;
}

bool DlnaDmsBrowse::connectDmsWithUdn(DMP *dmp, const du_uchar* udn) {
    _context.currentIndex = 0;
    _context.targetIndex = 0;
    _context.dmp = dmp;
    if (!dupnp_cp_dvcmgr_visit_udn(&dmp->deviceManager, udn, getDeviceInfoVisitor, &_context)) {
        LOG_WITH_PARAM("dupnp_cp_dvcmgr_visit_udn ERROR");
        return false;
    }
    
    if (!dmp->browseInfo.dmsInfo) {
        LOG_WITH_PARAM("dmp->browseInfo.dms ERROR");
        return false;
    }
    subscribe(dmp);
    return true;
}

bool DlnaDmsBrowse::connectDmsDirect(DMP *dmp, const char* friendlyName, const char* udn, const char* controlUrl, const char* eventSubUrl) {
    bool result = true;
    _context.dmp = dmp;
    
    DmsInfo info;
    info.friendly_name = DU_UCHAR(friendlyName);
    info.udn = DU_UCHAR(udn);
    info.cdsInfo.controlUrl = DU_UCHAR(controlUrl);
    info.cdsInfo.eventSubscriptionUrl = DU_UCHAR(eventSubUrl);
    result = browse_set_dms(&dmp->browseInfo, &info);
    subscribe(dmp);
    
    return result;
}

void DlnaDmsBrowse::browse() {
//    browseDirectChildren(_context.dmp, 0);
}
