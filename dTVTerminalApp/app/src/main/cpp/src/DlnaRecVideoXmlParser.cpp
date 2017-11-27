/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaRecVideoXmlParser.h"


namespace dtvt {

    DlnaRecVideoXmlParser::DlnaRecVideoXmlParser(){

    }
    void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1);
    void DlnaRecVideoXmlParser::parse(void *response, vector<StringVector>& out){

        /*IfNullReturn(response);*/
        dupnp_http_response *newRes = ((dupnp_http_response *) response);
        /*IfNullReturn((char*)(newRes->body);
        IfNullReturn((char*)(newRes->url);*/
        //to do: フォルダー階層がある場合
        char* xml= (char*)(newRes->body);
        char* controlUrl= (char*)(newRes->url);

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
        /*du_str_array_free(&param_array);*/
        xmlDocPtr didl_doc;
        xmlNodePtr root;
        didl_doc = dav_didl_libxml_make_doc(result, du_str_len(result));
        root = xmlDocGetRootElement(didl_doc);
        if(!root){
            goto error2;
        }
        parseXmlNode(root, out, recordVectorTmp);
        return;
        error1:
        du_str_array_free(&param_array);
        error2:
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
    }

    void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1)
    {
        xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
        while(NULL != xmlChildNode)
        {
            if(xmlChildNode->xmlChildrenNode != NULL)
            {
                std::vector<std::string> recordVectorTmp;
                bool isNew=false;
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)"item"))
                {
                    isNew=true;
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)"title"))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                    v1.push_back("2017-11-22 17:36:00");
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)"date"))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)"class"))
                {
                    if(!xmlStrcmp(xmlNodeGetContent(xmlChildNode), (const xmlChar*)"object.item.videoItem")){

                    }
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)"albumArtURI"))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)"res"))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                    out.push_back(v1);
                }
                if(isNew){
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
