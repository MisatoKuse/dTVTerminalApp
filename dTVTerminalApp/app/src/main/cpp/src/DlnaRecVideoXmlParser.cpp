/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaRecVideoXmlParser.h"


namespace dtvt {

    DlnaRecVideoXmlParser::DlnaRecVideoXmlParser(){

    }
    void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1);
    void DlnaRecVideoXmlParser::parse(void *response, vector<StringVector>& out){
        //録画一覧XMLパーサー
        IfNullReturn(response);
        dupnp_http_response *newRes = ((dupnp_http_response *) response);
        IfNullReturn((char*)(newRes->body));
        const du_uchar* result;
        du_str_array param_array;
        du_uint32 number_returned;
        du_uint32 total_matches;
        du_uint32 update_id;
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
        parseXmlNode(root, out, recordVectorTmp);
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
        return;
        error1:
        du_str_array_free(&param_array);
        error2:
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
    }

    bool isVideo = false;
    void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1)
    {
        xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
        while(NULL != xmlChildNode)
        {
            if(xmlChildNode->xmlChildrenNode != NULL)
            {
                std::vector<std::string> recordVectorTmp;
                bool isItem=false;
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Item))
                {
                    isItem=true;
                    recordVectorTmp.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Id));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Title))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Res))
                {
                    std::string protocolInfo((char *)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_ProtocolInfo));
                    if(protocolInfo.find(RecVideoType_Field_Mp4)!=string::npos ||
                            protocolInfo.find(RecVideoType_Field_Mpeg)!=string::npos){
                        v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Size));
                        v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Duration));
                        v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Resolution));
                        v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Bitrate));
                        isVideo = true;
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
                    parseXmlNode(xmlChildNode, out, recordVectorTmp);
                }
            }
            xmlChildNode = xmlChildNode->next;
        }
        return ;
    }

    DlnaRecVideoXmlParser::~DlnaRecVideoXmlParser(){

    }

} //namespace dtvt
