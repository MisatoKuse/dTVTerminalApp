/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <dav_cds.h>
#include <du_str.h>
#include <iostream>
#include "DlnaXmlParserBase.h"


namespace dtvt {

    //test data begin
    char test_bs_xml[]= "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n<s:Body>\r\n<u:BrowseResponse xmlns:u=\"urn:schemas-upnp-org:service:ContentDirectory:1\">\r\n<Result>&lt;DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:sec=\"http://www.sec.co.kr/\" xmlns:pv=\"http://www.pv.com/pvns/\"&gt;&lt;container id=\"256364\" childCount=\"10\" parentID=\"0\" restricted=\"1\"&gt;&lt;dc:title&gt;Videos&lt;/dc:title&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_SM;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000JPEG_SM_Videos.jpg&lt;/res&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000JPEG_TN_Videos.jpg&lt;/res&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/png:DLNA.ORG_PN=PNG_LRG;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000PNG_LRG_Videos.png&lt;/res&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/png:DLNA.ORG_PN=PNG_TN;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000PNG_TN_Videos.png&lt;/res&gt;&lt;upnp:albumArtURI dlna:profileID=\"JPEG_SM\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000JPEG_SM_Videos.jpg&lt;/upnp:albumArtURI&gt;&lt;upnp:albumArtURI dlna:profileID=\"JPEG_TN\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000JPEG_TN_Videos.jpg&lt;/upnp:albumArtURI&gt;&lt;upnp:albumArtURI dlna:profileID=\"PNG_LRG\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000PNG_LRG_Videos.png&lt;/upnp:albumArtURI&gt;&lt;upnp:albumArtURI dlna:profileID=\"PNG_TN\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256364/thumbnail0000PNG_TN_Videos.png&lt;/upnp:albumArtURI&gt;&lt;dc:date&gt;2017-11-30T10:50:30&lt;/dc:date&gt;&lt;upnp:class&gt;object.container.storageFolder&lt;/upnp:class&gt;&lt;/container&gt;&lt;container id=\"256365\" childCount=\"6\" parentID=\"0\" restricted=\"1\"&gt;&lt;dc:title&gt;Web&lt;/dc:title&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_SM;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000JPEG_SM_Web.jpg&lt;/res&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000JPEG_TN_Web.jpg&lt;/res&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/png:DLNA.ORG_PN=PNG_LRG;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000PNG_LRG_Web.png&lt;/res&gt;&lt;res xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" protocolInfo=\"http-get:*:image/png:DLNA.ORG_PN=PNG_TN;DLNA.ORG_FLAGS=00900000000000000000000000000000\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000PNG_TN_Web.png&lt;/res&gt;&lt;upnp:albumArtURI dlna:profileID=\"JPEG_SM\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000JPEG_SM_Web.jpg&lt;/upnp:albumArtURI&gt;&lt;upnp:albumArtURI dlna:profileID=\"JPEG_TN\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000JPEG_TN_Web.jpg&lt;/upnp:albumArtURI&gt;&lt;upnp:albumArtURI dlna:profileID=\"PNG_LRG\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000PNG_LRG_Web.png&lt;/upnp:albumArtURI&gt;&lt;upnp:albumArtURI dlna:profileID=\"PNG_TN\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"&gt;http://192.168.11.29:5001/get/256365/thumbnail0000PNG_TN_Web.png&lt;/upnp:albumArtURI&gt;&lt;upnp:class&gt;object.container.storageFolder&lt;/upnp:class&gt;&lt;/container&gt;&lt;/DIDL-Lite&gt;</Result>\r\n<NumberReturned>2</NumberReturned>\r\n<TotalMatches>2</TotalMatches>\r\n<UpdateID>1</UpdateID>\r\n</u:BrowseResponse>\r\n</s:Body>\r\n</s:Envelope>\r\n";
    //test data end

    DlnaXmlParserBase::DlnaXmlParserBase(DLNA_MSG_ID id):mDlnaMsgIdD(id) {}

    DLNA_MSG_ID DlnaXmlParserBase::getMsgId(){
        return mDlnaMsgIdD;
    }

    void DlnaXmlParserBase::parse(void *response, vector<StringVector>& out, std::string &containerId, std::string &isContainerId){
        //録画一覧XMLパーサー
        IfNullReturn(response);
        dupnp_http_response *newRes = ((dupnp_http_response *) response);
        IfNullReturn((char*)(newRes->body));
        /*char* xml= (char*)(newRes->body);
        char* controlUrl= (char*)(newRes->url);*/
        const du_uchar* result;
        du_str_array param_array;
        du_uint32 number_returned;
        du_uint32 total_matches;
        du_uint32 update_id;
        //test data begin
        string testXml;
        char str_0[1024+1] = {0};
        char str_1[1024+1] = {0};
        char str_2[1024+1] = {0};
        char str_3[1024+1] = {0};
        char str_4[1024+1] = {0};
        char str_5[1024+1] = {0};
        memcpy(str_0, newRes->body, 1024);
        memcpy(str_1, &newRes->body[1024], 1024);
        memcpy(str_2, &newRes->body[1024*2], 1024);
        char * ss= (char*)&newRes->body[1024*3];
        if(strlen(ss)<1024){
            strcpy(str_3, ss);
        } else {
            memcpy(str_3, ss, 1024);
            ss= (char*)&newRes->body[1024*4];
            if(strlen(ss)<1024){
                strcpy(str_4, ss);
            } else {
                memcpy(str_4, ss, 1024);
                ss= (char*)&newRes->body[1024*5];
                if(strlen(ss)<1024){
                    strcpy(str_5, ss);
                } else {
                    memcpy(str_5, ss, 1024);
                }
            }
        }


        std::cout<<newRes->body;
        DLNA_MSG_ID id= getMsgId();
        switch (id){
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                testXml=test_bs_xml;
                newRes->body= (const du_uchar *) testXml.c_str();
                newRes->body_size=testXml.length();
                break;
        }
        //test data end
        std::vector<std::string> recordVectorTmp;
        du_str_array_init(&param_array);
        if(!dav_cds_parse_browse_response(
                newRes->body,
                newRes->body_size,
                &param_array,
                &result,
                &number_returned,
                &total_matches,
                &update_id)){
            goto error1;
        }
        xmlDocPtr didl_doc;
        xmlNodePtr root;
        didl_doc = dav_didl_libxml_make_doc(result, du_str_len(result));
        root = xmlDocGetRootElement(didl_doc);
        if(!root){
            goto error2;
        }
        parseXmlNode(root, out, recordVectorTmp, containerId, isContainerId);
        //test data begin
        switch (id){
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                containerId="";
                break;
        }
        //test data end
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
        return;
        error1:
        du_str_array_free(&param_array);
        error2:
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
    }

} //end of dtvt
