/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaRecVideoXmlParser.h"


namespace dtvt {

    DlnaRecVideoXmlParser::DlnaRecVideoXmlParser(): DlnaXmlParserBase(DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST){

    }
    void DlnaRecVideoXmlParser::parse(void *fileStr, vector<StringVector>& out){}
    //void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1, std::string &containerId, std::string &isContainerId);
//    void DlnaRecVideoXmlParser::parseXml(void *response, vector<StringVector>& out, std::string &containerId, std::string &isContainerId){
//
//        //録画一覧XMLパーサー
//        IfNullReturn(response);
//        dupnp_http_response *newRes = ((dupnp_http_response *) response);
//        IfNullReturn((char*)(newRes->body));
//        /*char* xml= (char*)(newRes->body);
//        char* controlUrl= (char*)(newRes->url);*/
//        const du_uchar* result;
//        du_str_array param_array;
//        du_uint32 number_returned;
//        du_uint32 total_matches;
//        du_uint32 update_id;
//        std::vector<std::string> recordVectorTmp;
//        du_str_array_init(&param_array);
//        if(!dav_cds_parse_browse_response(
//                newRes->body,
//                newRes->body_size,
//                &param_array,
//                &result,
//                &number_returned,
//                &total_matches,
//                &update_id)){
//            goto error1;
//        }
//        xmlDocPtr didl_doc;
//        xmlNodePtr root;
//        didl_doc = dav_didl_libxml_make_doc(result, du_str_len(result));
//        root = xmlDocGetRootElement(didl_doc);
//        if(!root){
//            goto error2;
//        }
//        parseXmlNode(root, out, recordVectorTmp, containerId, isContainerId);
//        du_str_array_free(&param_array);
//        xmlFreeDoc(didl_doc);
//        return;
//        error1:
//        du_str_array_free(&param_array);
//        error2:
//        du_str_array_free(&param_array);
//        xmlFreeDoc(didl_doc);
//    }

    static bool isVideo = false;
    void DlnaRecVideoXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1, std::string &containerId, std::string &isContainerId)
    {
        xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
        while(NULL != xmlChildNode)
        {
            if(xmlChildNode->xmlChildrenNode != NULL)
            {
                std::vector<std::string> recordVectorTmp;
                bool isItem=false;
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Container))
                {
                    if(isContainerId.find("0")!=string::npos){
                        containerId = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Id);
                    }
                    isItem = true;
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Item))
                {
                    isItem = true;
                    recordVectorTmp.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Id));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Title))
                {
                    std::string title((char*)xmlNodeGetContent(xmlChildNode));
                    if(title.find(RecVideoParse_Field_Videos) != string::npos){
                        isContainerId = "1";
                        return;
                    }else{
                        v1.push_back(title);
                    }
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Res))
                {
                    std::string protocolInfo((char *)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_ProtocolInfo));
                    if(protocolInfo.find(RecVideoType_Field_Mp4)!=string::npos ||
                            protocolInfo.find(RecVideoType_Field_Mpeg)!=string::npos){

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Size));
                        char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Size);
                        if(NULL==tmpP){
                            v1.push_back("0");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Duration));
                        tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Duration);
                        if(NULL==tmpP){
                            v1.push_back("");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Resolution));
                        tmpP = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Resolution);
                        if(NULL==tmpP){
                            v1.push_back("");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Bitrate));
                        tmpP = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Bitrate);
                        if(NULL==tmpP){
                            v1.push_back("");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                        tmpP = (char*)xmlNodeGetContent(xmlChildNode);
                        if(NULL==tmpP){
                            v1.push_back("");
                            isVideo = false;
                        } else {
                            v1.push_back(tmpP);
                            isVideo = true;
                        }

                        //isVideo = true;
                    }
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_AlbumArtURI))
                {
                    if(!xmlStrcmp((const xmlChar*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_ProfileID), (const xmlChar*)RecVideoParse_Field_PNG_LRG)){
                        v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                    }
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Date))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Class))
                {
                    if(!xmlStrcmp(xmlNodeGetContent(xmlChildNode), (const xmlChar*)RecVideoParse_Field_VideoItem) && isVideo){
                        out.push_back(v1);
                    }
                    isVideo = false;
                }
                if(isItem){
                    parseXmlNode(xmlChildNode, out, recordVectorTmp, containerId, isContainerId);
                }
            }
            xmlChildNode = xmlChildNode->next;
        }
        return ;
    }

    DlnaRecVideoXmlParser::~DlnaRecVideoXmlParser(){

    }

} //namespace dtvt
