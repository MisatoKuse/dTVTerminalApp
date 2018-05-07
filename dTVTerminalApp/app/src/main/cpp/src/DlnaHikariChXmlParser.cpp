/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaHikariChXmlParser.h"


namespace dtvt {

    DlnaHikariChXmlParser::DlnaHikariChXmlParser(): DlnaXmlParserBase(DLNA_MSG_ID_HIKARI_CHANNEL_LIST){

    }

    void DlnaHikariChXmlParser::parse(void *fileStr, vector<VectorString>& out){

    }

    static bool isVideo = false;

#if defined(DLNA_KARI_DMS_UNIVERSAL)
    void DlnaBSDigitalXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<VectorString>& out, VectorString& v1, std::string &containerId, std::string &isContainerId)
        {
            xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
            while(NULL != xmlChildNode)
            {
                if(xmlChildNode->xmlChildrenNode != NULL)
                {
                    std::vector<std::string> recordVectorTmp;
                    bool isItem=false;
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Container))
                    {
                        if(isContainerId.find("0")!=string::npos){
                            containerId = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Id);
                        }
                        isItem = true;
                    }
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Item))
                    {
                        isItem = true;
                        recordVectorTmp.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Id));
                    }
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Title))
                    {
                        std::string title((char*)xmlNodeGetContent(xmlChildNode));
                        if(title.find(HikariChXmlParser_Field_Videos) != string::npos){
                            isContainerId = "1";
                            return;
                        }else{
                            v1.push_back(title);
                        }
                    }
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Res))
                    {
                        std::string protocolInfo((char *)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_ProtocolInfo));
                        if(protocolInfo.find(HikariChXmlParser_Field_Mp4)!=string::npos /*||
                           protocolInfo.find(HikariChXmlParser_Field_Mpeg)!=string::npos*/){

                            //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Size));
                            char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Size);
                            if(NULL==tmpP){
                                v1.push_back("0");
                            } else {
                                v1.push_back(tmpP);
                            }

                            //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Duration));
                            tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Duration);
                            if(NULL==tmpP){
                                v1.push_back("");
                            } else {
                                v1.push_back(tmpP);
                            }

                            //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Resolution));
                            tmpP = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Resolution);
                            if(NULL==tmpP){
                                v1.push_back("");
                            } else {
                                v1.push_back(tmpP);
                            }

                            //v1.push_back((char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Bitrate));
                            tmpP = (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Bitrate);
                            if(NULL==tmpP){
                                v1.push_back("0");
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
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_AlbumArtURI))
                    {
                        if(!xmlStrcmp((const xmlChar*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_ProfileID), (const xmlChar*)HikariChXmlParser_Field_PNG_LRG)){
                            v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                        }
                    }
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Date))
                    {
                        v1.push_back((char*)xmlNodeGetContent(xmlChildNode));
                    }
                    if (!xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Class))
                    {
                        if(!xmlStrcmp(xmlNodeGetContent(xmlChildNode), (const xmlChar*)HikariChXmlParser_Field_VideoItem) && isVideo){
                            v1.push_back(HikariChXmlParser_Field_Mp4_Ret);
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
    //    void DlnaBSDigitalXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<VectorString>& out, VectorString& v1, std::string &containerId, std::string &isContainerId)
//        {
//            xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
//            XmlItemMap itemXmlItemMap;
//            bool isItVideo=false;
//            while(NULL != xmlChildNode)
//            {
//                //item begin
//                if(xmlChildNode->xmlChildrenNode != NULL)
//                {
//                    if (0==xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Item))
//                    {
//                        itemXmlItemMap.clear();
//                        isItVideo=false;
//
//                        char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Id);
//                        if(NULL!=tmpP){
//                            itemXmlItemMap[Xml_Item_Id] = tmpP;
//                        } else {
//                            itemXmlItemMap[Xml_Item_Id] = "";
//                        }
//
//                        //children
//                        xmlNodePtr child= xmlChildNode->children;
//                        while(NULL!=child) {
//                            if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Title)) {
//                                char *tmpP = (char *) xmlNodeGetContent(child);
//                                std::string title("");
//                                if (NULL != tmpP) {
//                                    title = tmpP;
//                                }
//                                itemXmlItemMap[Xml_Item_Title] = title;
//                            }
//                            if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Res)) {
//                                char* tmpP= (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_ProtocolInfo);
//                                if(NULL==tmpP){
//                                    return;
//                                }
//                                std::string protocolInfo(tmpP);
//                                if (protocolInfo.find(HikariChXmlParser_Field_Mp4) != string::npos ||
//                                    string::npos != protocolInfo.find(HikariChXmlParser_Field_Dtcp)) {
//
//                                    if(protocolInfo.find(HikariChXmlParser_Field_Mp4) != string::npos){
//                                        itemXmlItemMap[Xml_Item_VideoType] = HikariChXmlParser_Field_Mp4_Ret;
//                                    }
//                                    if(protocolInfo.find(HikariChXmlParser_Field_Dtcp) != string::npos){
//                                        itemXmlItemMap[Xml_Item_VideoType] = protocolInfo;
//                                    }
//
//                                    char *tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Size);
//                                    if (NULL == tmpP) {
//                                        itemXmlItemMap[Xml_Item_Size] = "0";
//                                    } else {
//                                        itemXmlItemMap[Xml_Item_Size] = tmpP;
//                                    }
//
//                                    tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Duration);
//                                    if (NULL == tmpP) {
//                                        itemXmlItemMap[Xml_Item_Duration] = "0";
//                                    } else {
//                                        itemXmlItemMap[Xml_Item_Duration] = tmpP;
//                                    }
//
//                                    tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Resolution);
//                                    if (NULL == tmpP) {
//                                        itemXmlItemMap[Xml_Item_Resolution] = "";
//                                    } else {
//                                        itemXmlItemMap[Xml_Item_Resolution] = tmpP;
//                                    }
//
//                                    tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Bitrate);
//                                    if (NULL == tmpP) {
//                                        itemXmlItemMap[Xml_Item_Bitrate] = "0";
//                                    } else {
//                                        itemXmlItemMap[Xml_Item_Bitrate] = tmpP;
//                                    }
//
//                                    //url
//                                    tmpP = (char *) xmlNodeGetContent(child);
//                                    if (NULL == tmpP) {
//                                        itemXmlItemMap[Xml_Item_ResUrl] = "";
//                                    } else {
//                                        itemXmlItemMap[Xml_Item_ResUrl] = tmpP;
//                                    }
//                                }
//                            }
//                            if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_AlbumArtURI)) {
//                                if (0==xmlStrcmp((const xmlChar *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_ProfileID),
//                                               (const xmlChar *) HikariChXmlParser_Field_PNG_LRG)) {
//                                    char* tmpP= (char *) xmlNodeGetContent(child);
//                                    if(NULL!=tmpP){
//                                        itemXmlItemMap[Xml_Item_UpnpIcon] = tmpP;
//                                    } else {
//                                        itemXmlItemMap[Xml_Item_UpnpIcon] = "";
//                                    }
//                                }
//                            }
//                            if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Date)) {
//                                char* tmpP= (char *) xmlNodeGetContent(child);
//                                if(NULL!=tmpP){
//                                    itemXmlItemMap[Xml_Item_Date] = tmpP;
//                                } else {
//                                    itemXmlItemMap[Xml_Item_Date] = "";
//                                }
//                            }
//                            if (0== xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Class)) {
//                                if (NULL != xmlStrstr(xmlNodeGetContent(child), (const xmlChar *) HikariChXmlParser_Field_VideoItem)) {
//                                    isItVideo = true;
//                                } else {
//                                    isItVideo = false;
//                                }
//                            }
//                            if(child){
//                                child = child->next;
//                            }
//                        }
//                    }
//                }   //end of item
//
//                if(isItVideo && 0<itemXmlItemMap.size()){
//                    VectorString tmpStringVector;
//                    setXmlItemValues(tmpStringVector, Xml_Item_Id, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_Title, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_Size, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_Duration, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_Resolution, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_Bitrate, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_ResUrl, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_UpnpIcon, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_Date, itemXmlItemMap);
//                    setXmlItemValues(tmpStringVector, Xml_Item_VideoType, itemXmlItemMap);
//                    out.push_back(tmpStringVector);
//                }
//                itemXmlItemMap.clear();
//                isItVideo=false;
//
//                xmlChildNode = xmlChildNode->next;
//            }
//            return ;
//        }
            void DlnaBSDigitalXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<VectorString>& out, VectorString& v1, std::string &containerId, std::string &isContainerId)
            {
                xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
                XmlItemMap itemXmlItemMap;
                bool isItVideo=false;
                bool error=false;
                while(NULL != xmlChildNode)
                {
                    error=false;
                    //item begin
                    if(xmlChildNode->xmlChildrenNode != NULL)
                    {
                        if (0==xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Item))
                        {
                            itemXmlItemMap.clear();
                            isItVideo=false;

                            char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Id);
                            if(NULL!=tmpP){
                                itemXmlItemMap[Xml_Item_Id] = tmpP;
                            } else {
                                itemXmlItemMap[Xml_Item_Id] = "";
                            }

                            //children
                            xmlNodePtr child= xmlChildNode->children;
                            while(NULL!=child && !error) {
                                if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Title)) {
                                    char *tmpP = (char *) xmlNodeGetContent(child);
                                    std::string title("");
                                    if (NULL != tmpP) {
                                        title = tmpP;
                                    }
                                    itemXmlItemMap[Xml_Item_Title] = title;
                                }
                                if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Res)) {
                                    char* tmpP= (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_ProtocolInfo);
                                    if(NULL==tmpP){
                                        error=true;
                                        continue;
                                    }
                                    std::string protocolInfo(tmpP);
                                    if (protocolInfo.find(HikariChXmlParser_Field_Mp4) != string::npos ||
                                        string::npos != protocolInfo.find(HikariChXmlParser_Field_Dtcp)) {

                                        if(protocolInfo.find(HikariChXmlParser_Field_Mp4) != string::npos){
                                            itemXmlItemMap[Xml_Item_VideoType] = HikariChXmlParser_Field_Mp4_Ret;
                                        }else if(protocolInfo.find(HikariChXmlParser_Field_Dtcp) != string::npos){
                                            itemXmlItemMap[Xml_Item_VideoType] = protocolInfo;
                                        } else {
                                            next(xmlChildNode, itemXmlItemMap, isItVideo);
                                            continue;
                                        }

                                        char *tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Size);
                                        if (NULL == tmpP) {
                                            itemXmlItemMap[Xml_Item_Size] = "0";
                                        } else {
                                            itemXmlItemMap[Xml_Item_Size] = tmpP;
                                        }

                                        tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Duration);
                                        if (NULL == tmpP) {
                                            itemXmlItemMap[Xml_Item_Duration] = "0";
                                        } else {
                                            itemXmlItemMap[Xml_Item_Duration] = tmpP;
                                        }

                                        tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Resolution);
                                        if (NULL == tmpP) {
                                            itemXmlItemMap[Xml_Item_Resolution] = "";
                                        } else {
                                            itemXmlItemMap[Xml_Item_Resolution] = tmpP;
                                        }

                                        tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Bitrate);
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
        //                        if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_AlbumArtURI)) {
        //                            if (0==xmlStrcmp((const xmlChar *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_ProfileID),
        //                                             (const xmlChar *) HikariChXmlParser_Field_PNG_LRG)) {
        //                                char* tmpP= (char *) xmlNodeGetContent(child);
        //                                if(NULL!=tmpP){
        //                                    itemXmlItemMap[Xml_Item_UpnpIcon] = tmpP;
        //                                } else {
        //                                    itemXmlItemMap[Xml_Item_UpnpIcon] = "";
        //                                }
        //                            }
        //                        }
                                if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Date)) {
                                    char* tmpP= (char *) xmlNodeGetContent(child);
                                    if(NULL!=tmpP){
                                        itemXmlItemMap[Xml_Item_Date] = tmpP;
                                    } else {
                                        itemXmlItemMap[Xml_Item_Date] = "";
                                    }
                                }
                                if (0== xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Class)) {
                                    if (NULL != xmlStrstr(xmlNodeGetContent(child), (const xmlChar *) HikariChXmlParser_Field_VideoItem)) {
                                        isItVideo = true;
                                    } else {
                                        isItVideo = false;
                                    }
                                }
                                if (0== xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_ChannelName)) {
                                    char* tmpP= (char *) xmlNodeGetContent(child);
                                    if(NULL!=tmpP){
                                        itemXmlItemMap[Xml_Item_ChannelName] = tmpP;
                                    } else {
                                        itemXmlItemMap[Xml_Item_ChannelName] = "";
                                    }
                                }
                                if(child){
                                    child = child->next;
                                }
                            } //inner while
                        }
                    } else {
                        next(xmlChildNode, itemXmlItemMap, isItVideo);
                        continue;
                    }

                    if(isItVideo && 0<itemXmlItemMap.size()){
                        VectorString tmpStringVector;

                        if(!hasXmlItemMapKey(itemXmlItemMap, Xml_Item_ResUrl) ||
                           !hasXmlItemMapKey(itemXmlItemMap, Xml_Item_VideoType) ||
                           !hasXmlItemMapKey(itemXmlItemMap, Xml_Item_Duration) ){
                        } else {
                            string def("");
                            setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Id, def);
                            setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Title, def);
                            setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Size, def);
                            setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Resolution, def);
                            setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Bitrate, def);
                            setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Date, def);

                            setXmlItemValues(tmpStringVector, Xml_Item_Id, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_Title, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_Size, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_Duration, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_Resolution, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_Bitrate, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_ResUrl, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_Date, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_VideoType, itemXmlItemMap);
                            setXmlItemValues(tmpStringVector, Xml_Item_ChannelName, itemXmlItemMap);
                            out.push_back(tmpStringVector);
                        }
                    }
                    next(xmlChildNode, itemXmlItemMap, isItVideo);
                }
                return ;
            }

#elif defined(DLNA_KARI_DMS_RELEASE)
    void DlnaHikariChXmlParser::parseXmlNode(const xmlNodePtr & xmlRootNode, vector<VectorString>& out, VectorString& v1, std::string &containerId, std::string &isContainerId)
    {
        xmlNodePtr xmlChildNode = xmlRootNode->xmlChildrenNode;
        XmlItemMap itemXmlItemMap;
        bool isItVideo=false;
        bool error=false;
        while(NULL != xmlChildNode)
        {
            error=false;
            //item begin
            if(xmlChildNode->xmlChildrenNode != NULL)
            {
                if (0==xmlStrcmp(xmlChildNode->name, (const xmlChar*)HikariChXmlParser_Field_Item))
                {
                    itemXmlItemMap.clear();
                    isItVideo=false;

                    char* tmpP= (char*)xmlGetProp(xmlChildNode, (const xmlChar*)HikariChXmlParser_Field_Id);
                    if(NULL!=tmpP){
                        itemXmlItemMap[Xml_Item_Id] = tmpP;
                    } else {
                        itemXmlItemMap[Xml_Item_Id] = "";
                    }

                    //children
                    xmlNodePtr child= xmlChildNode->children;
                    while(NULL!=child && !error) {
                        if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Title)) {
                            char *tmpP = (char *) xmlNodeGetContent(child);
                            std::string title("");
                            if (NULL != tmpP) {
                                title = tmpP;
                            }
                            itemXmlItemMap[Xml_Item_Title] = title;
                        }
                        if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Res)) {
                            char* tmpP= (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_ProtocolInfo);
                            if(NULL==tmpP){
                                error=true;
                                continue;
                            }
                            std::string protocolInfo(tmpP);
                            if (protocolInfo.find(HikariChXmlParser_Field_Mp4) != string::npos ||
                                string::npos != protocolInfo.find(HikariChXmlParser_Field_Dtcp)) {

                                if(protocolInfo.find(HikariChXmlParser_Field_Mp4) != string::npos){
                                    itemXmlItemMap[Xml_Item_VideoType] = HikariChXmlParser_Field_Mp4_Ret;
                                }else if(protocolInfo.find(HikariChXmlParser_Field_Dtcp) != string::npos){
                                    itemXmlItemMap[Xml_Item_VideoType] = protocolInfo;
                                } else {
                                    next(xmlChildNode, itemXmlItemMap, isItVideo);
                                    continue;
                                }

                                char *tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Size);
                                if (NULL == tmpP) {
                                    itemXmlItemMap[Xml_Item_Size] = "0";
                                } else {
                                    itemXmlItemMap[Xml_Item_Size] = tmpP;
                                }

                                tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Duration);
                                if (NULL == tmpP) {
                                    itemXmlItemMap[Xml_Item_Duration] = "0";
                                } else {
                                    itemXmlItemMap[Xml_Item_Duration] = tmpP;
                                }

                                tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Resolution);
                                if (NULL == tmpP) {
                                    itemXmlItemMap[Xml_Item_Resolution] = "";
                                } else {
                                    itemXmlItemMap[Xml_Item_Resolution] = tmpP;
                                }

                                tmpP = (char *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_Bitrate);
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
//                        if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_AlbumArtURI)) {
//                            if (0==xmlStrcmp((const xmlChar *) xmlGetProp(child, (const xmlChar *) HikariChXmlParser_Field_ProfileID),
//                                             (const xmlChar *) HikariChXmlParser_Field_PNG_LRG)) {
//                                char* tmpP= (char *) xmlNodeGetContent(child);
//                                if(NULL!=tmpP){
//                                    itemXmlItemMap[Xml_Item_UpnpIcon] = tmpP;
//                                } else {
//                                    itemXmlItemMap[Xml_Item_UpnpIcon] = "";
//                                }
//                            }
//                        }
                        if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Date)) {
                            char* tmpP= (char *) xmlNodeGetContent(child);
                            if(NULL!=tmpP){
                                itemXmlItemMap[Xml_Item_Date] = tmpP;
                            } else {
                                itemXmlItemMap[Xml_Item_Date] = "";
                            }
                        }
                        if (0== xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_Class)) {
                            if (NULL != xmlStrstr(xmlNodeGetContent(child), (const xmlChar *) HikariChXmlParser_Field_VideoItem)) {
                                isItVideo = true;
                            } else {
                                isItVideo = false;
                            }
                        }
                        if (0 == xmlStrcmp(child->name, (const xmlChar *) HikariChXmlParser_Field_ChannelNr)) {
                            char *tmpP = (char *) xmlNodeGetContent(child);
                            std::string channelNr("");
                            if (NULL != tmpP) {
                                channelNr = tmpP;
                            }
                            itemXmlItemMap[Xml_Item_ChannelNr] = channelNr;
                        }
                        if(child){
                            child = child->next;
                        }
                    } //inner while
                }
            } else {
                next(xmlChildNode, itemXmlItemMap, isItVideo);
                continue;
            }

            if(isItVideo && 0<itemXmlItemMap.size()){
                VectorString tmpStringVector;

                if(!hasXmlItemMapKey(itemXmlItemMap, Xml_Item_ResUrl) ||
                   !hasXmlItemMapKey(itemXmlItemMap, Xml_Item_VideoType) ||
                   !hasXmlItemMapKey(itemXmlItemMap, Xml_Item_Duration) ){
                } else {
                    string def("");
                    setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Id, def);
                    setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Title, def);
                    setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Size, def);
                    setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Resolution, def);
                    setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Bitrate, def);
                    setXmlItemMapDefaultKey(itemXmlItemMap, Xml_Item_Date, def);

                    setXmlItemValues(tmpStringVector, Xml_Item_Id, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Title, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Size, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Duration, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Resolution, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Bitrate, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_ResUrl, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_Date, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_VideoType, itemXmlItemMap);
                    setXmlItemValues(tmpStringVector, Xml_Item_ChannelNr, itemXmlItemMap);
                    out.push_back(tmpStringVector);
                }
            }
            next(xmlChildNode, itemXmlItemMap, isItVideo);
        }
        return ;
    }

#endif

    DlnaHikariChXmlParser::~DlnaHikariChXmlParser(){

    }

} //namespace dtvt