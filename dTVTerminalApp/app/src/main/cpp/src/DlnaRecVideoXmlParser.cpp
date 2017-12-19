/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <algorithm>
#include "DlnaRecVideoXmlParser.h"


namespace dtvt {

    DlnaRecVideoXmlParser::DlnaRecVideoXmlParser(): DlnaXmlParserBase(DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST){

    }
    void DlnaRecVideoXmlParser::parse(void *fileStr, vector<StringVector>& out){}

    static bool isVideo = false;

    #if defined(DLNA_KARI_DMS_UNIVERSAL)
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
                        if(protocolInfo.find(RecVideoType_Field_Mp4)!=string::npos /*||
                           protocolInfo.find(RecVideoType_Field_Mpeg)!=string::npos*/){

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
                            v1.push_back(RecVideoType_Field_Mp4_Ret);
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
    #elif defined(DLNA_KARI_DMS_NAS)
        void DlnaRecVideoXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1, std::string &containerId, std::string &isContainerId)
        {
            xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
            XmlItemMap itemXmlItemMap;
            bool isItVideo=false;
            while(NULL != xmlChildNode)
            {
                //item begin
                if(xmlChildNode->xmlChildrenNode != NULL)
                {
                    if (0==xmlStrcmp(xmlChildNode->name, (const xmlChar*)RecVideoParse_Field_Item))
                    {
//                        if(isItVideo && 0<itemXmlItemMap.size()){
//                            StringVector tmpStringVector;
//                            setXmlItemValues(tmpStringVector, Xml_Item_Id, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_Title, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_Size, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_Duration, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_Resolution, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_Bitrate, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_ResUrl, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_UpnpIcon, itemXmlItemMap);
//                            setXmlItemValues(tmpStringVector, Xml_Item_Date, itemXmlItemMap);
//                            out.push_back(tmpStringVector);
//                        }
                        itemXmlItemMap.clear();
                        isItVideo=false;

                        char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)RecVideoParse_Field_Id);
                        if(NULL!=tmpP){
                            itemXmlItemMap[Xml_Item_Id] = tmpP;
                        } else {
                            itemXmlItemMap[Xml_Item_Id] = "";
                        }

                        //children
                        xmlNodePtr child= xmlChildNode->children;
                        while(NULL!=child) {
                            if (0 == xmlStrcmp(child->name, (const xmlChar *) RecVideoParse_Field_Title)) {
                                char *tmpP = (char *) xmlNodeGetContent(child);
                                std::string title("");
                                if (NULL != tmpP) {
                                    title = tmpP;
                                }
                                itemXmlItemMap[Xml_Item_Title] = title;
                            }
                            if (0 == xmlStrcmp(child->name, (const xmlChar *) RecVideoParse_Field_Res)) {
                                char* tmpP= (char *) xmlGetProp(child, (const xmlChar *) RecVideoParse_Field_ProtocolInfo);
                                if(NULL==tmpP){
                                    return;
                                }
                                std::string protocolInfo(tmpP);
                                if (protocolInfo.find(RecVideoType_Field_Mp4) != string::npos ||
                                    string::npos != protocolInfo.find(RecVideoType_Field_Dtcp)) {

                                    if(protocolInfo.find(RecVideoType_Field_Mp4) != string::npos){
                                        itemXmlItemMap[Xml_Item_VideoType] = RecVideoType_Field_Mp4_Ret;
                                    }
                                    if(protocolInfo.find(RecVideoType_Field_Dtcp) != string::npos){
                                        itemXmlItemMap[Xml_Item_VideoType] = protocolInfo;
                                    }

                                    char *tmpP = (char *) xmlGetProp(child, (const xmlChar *) RecVideoParse_Field_Size);
                                    if (NULL == tmpP) {
                                        itemXmlItemMap[Xml_Item_Size] = "0";
                                    } else {
                                        itemXmlItemMap[Xml_Item_Size] = tmpP;
                                    }

                                    tmpP = (char *) xmlGetProp(child, (const xmlChar *) RecVideoParse_Field_Duration);
                                    if (NULL == tmpP) {
                                        itemXmlItemMap[Xml_Item_Duration] = "0";
                                    } else {
                                        itemXmlItemMap[Xml_Item_Duration] = tmpP;
                                    }

                                    tmpP = (char *) xmlGetProp(child, (const xmlChar *) RecVideoParse_Field_Resolution);
                                    if (NULL == tmpP) {
                                        itemXmlItemMap[Xml_Item_Resolution] = "";
                                    } else {
                                        itemXmlItemMap[Xml_Item_Resolution] = tmpP;
                                    }

                                    tmpP = (char *) xmlGetProp(child, (const xmlChar *) RecVideoParse_Field_Bitrate);
                                    if (NULL == tmpP) {
                                        itemXmlItemMap[Xml_Item_Bitrate] = "0";
                                    } else {
                                        itemXmlItemMap[Xml_Item_Bitrate] = tmpP;
                                    }

                                    //url
                                    tmpP = (char *) xmlNodeGetContent(child);
                                    if (NULL == tmpP) {
                                        itemXmlItemMap[Xml_Item_ResUrl] = "";
                                    } else {
                                        itemXmlItemMap[Xml_Item_ResUrl] = tmpP;
                                    }
                                }
                            }
                            if (0 == xmlStrcmp(child->name, (const xmlChar *) RecVideoParse_Field_AlbumArtURI)) {
                                if (0==xmlStrcmp((const xmlChar *) xmlGetProp(child, (const xmlChar *) RecVideoParse_Field_ProfileID),
                                               (const xmlChar *) RecVideoParse_Field_PNG_LRG)) {
                                    char* tmpP= (char *) xmlNodeGetContent(child);
                                    if(NULL!=tmpP){
                                        itemXmlItemMap[Xml_Item_UpnpIcon] = tmpP;
                                    } else {
                                        itemXmlItemMap[Xml_Item_UpnpIcon] = "";
                                    }
                                }
                            }
                            if (0 == xmlStrcmp(child->name, (const xmlChar *) RecVideoParse_Field_Date)) {
                                char* tmpP= (char *) xmlNodeGetContent(child);
                                if(NULL!=tmpP){
                                    itemXmlItemMap[Xml_Item_Date] = tmpP;
                                } else {
                                    itemXmlItemMap[Xml_Item_Date] = "";
                                }
                            }
                            if (0== xmlStrcmp(child->name, (const xmlChar *) RecVideoParse_Field_Class)) {
                                if (NULL != xmlStrstr(xmlNodeGetContent(child), (const xmlChar *) RecVideoParse_Field_VideoItem)) {
                                    isItVideo = true;
                                } else {
                                    isItVideo = false;
                                }
                            }
                            if(child){
                                child = child->next;
                            }
                        }
                    }
                }   //end of item

                if(isItVideo && 0<itemXmlItemMap.size()){
                    StringVector tmpStringVector;
                    setXmlItemValues(tmpStringVector, Xml_Item_Id, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Title, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Size, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Duration, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Resolution, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Bitrate, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_ResUrl, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_UpnpIcon, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Date, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_VideoType, itemXmlItemMap);
                    out.push_back(tmpStringVector);
                }
                itemXmlItemMap.clear();
                isItVideo=false;

                xmlChildNode = xmlChildNode->next;
            }
            return ;
        }
    #endif

    DlnaRecVideoXmlParser::~DlnaRecVideoXmlParser(){

    }

} //namespace dtvt
