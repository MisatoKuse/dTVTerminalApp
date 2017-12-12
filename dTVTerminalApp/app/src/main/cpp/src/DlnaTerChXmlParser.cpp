/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaTerChXmlParser.h"


namespace dtvt {

    DlnaTerChXmlParser::DlnaTerChXmlParser(): DlnaXmlParserBase(DLNA_MSG_ID_BS_CHANNEL_LIST){

    }

    void DlnaTerChXmlParser::parse(void *fileStr, vector<StringVector>& out){

    }

    static bool isVideo = false;
    void DlnaTerChXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1, std::string &containerId, std::string &isContainerId) {
        xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
        while(NULL != xmlChildNode)
        {
            if(xmlChildNode->xmlChildrenNode != NULL)
            {
                std::vector<std::string> recordVectorTmp;
                bool isItem=false;
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_Container))
                {
                    if(isContainerId.find("0")!=string::npos){
                        containerId = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Id);
                    }
                    isItem = true;
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_Item))
                {
                    isItem = true;
                    recordVectorTmp.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Id));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_Title))
                {
                    std::string title((char*)xmlNodeGetContent(xmlChildNode));
                    if(title.find(TerChXmlParser_Field_Videos) != string::npos){
                        isContainerId = "1";
                        return;
                    }else{
                        v1.push_back(title);
                    }
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_Res))
                {
                    std::string protocolInfo((char *)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_ProtocolInfo));
                    if(protocolInfo.find(TerChXmlParser_Field_Mp4)!=string::npos ||
                       protocolInfo.find(TerChXmlParser_Field_Mpeg)!=string::npos){

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Size));
                        char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Size);
                        if(NULL==tmpP){
                            v1.push_back("0");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Duration));
                        tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Duration);
                        if(NULL==tmpP){
                            v1.push_back("");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Resolution));
                        tmpP = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Resolution);
                        if(NULL==tmpP){
                            v1.push_back("");
                        } else {
                            v1.push_back(tmpP);
                        }

                        //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Bitrate));
                        tmpP = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_Bitrate);
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
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_AlbumArtURI))
                {
                    if(!xmlStrcmp((const xmlChar*)xmlGetProp(xmlChildNode, (const xmlChar*)TerChXmlParser_Field_ProfileID), (const xmlChar*)TerChXmlParser_Field_PNG_LRG)){
                        v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                    }
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_Date))
                {
                    v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                }
                if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)TerChXmlParser_Field_Class))
                {
                    if(!xmlStrcmp(xmlNodeGetContent(xmlChildNode), (const xmlChar*)TerChXmlParser_Field_VideoItem) && isVideo){
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

    DlnaTerChXmlParser::~DlnaTerChXmlParser(){

    }

} //namespace dtvt
