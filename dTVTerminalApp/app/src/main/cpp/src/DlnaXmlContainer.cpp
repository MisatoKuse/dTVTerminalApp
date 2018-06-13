/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <algorithm>
#include "DlnaXmlContainer.h"
#include "Dlna.h"


    DlnaXmlContainer::DlnaXmlContainer() {

    }

    DlnaXmlContainer::~DlnaXmlContainer() {
        uninit();
    }

    void DlnaXmlContainer::uninit(){
        std::vector<du_uchar*>::iterator it = mXmls.begin();
        for(; it != mXmls.begin(); ++it){
            du_uchar* uc = *it;
            DelIfNotNull(uc);
        }
    }

    void DlnaXmlContainer::addXml(du_uchar* xml, size_t size) {
        if(NULL != xml && 0 < size){
            du_uchar* tXml=new du_uchar[size + 1];
            memset(tXml, 0x00, size * sizeof(du_uchar));
            memcpy((void *) tXml, xml, size * sizeof(du_uchar));
            mXmls.push_back(tXml);
        }
    }

    void DlnaXmlContainer::addVVectorString(VVectorString & vs){
        if(0 < vs.size()){
            std::copy(vs.begin(), vs.end(), std::back_inserter(mVVectorString));
        }
    }

    void DlnaXmlContainer::getDidlLiteDocHead(du_uchar* allRecordedVideoXml, std::string& outStr){
        static string begin = RecVideoXml_Item_Begin_Tag;

        if(NULL==allRecordedVideoXml || 1>du_str_len(allRecordedVideoXml)){
            outStr="";
            return;
        }

        string xml((char*)allRecordedVideoXml);
        int posBegin = xml.find(begin);
        if(1>posBegin){
            outStr="";
            return;
        }

        int len= posBegin;
        outStr = xml.substr(0, len);
    }

    void DlnaXmlContainer::getDidlLiteDocTail(du_uchar* allRecordedVideoXml, std::string& outStr){
        static string end= RecVideoXml_Item_End_Tag;

        if(NULL==allRecordedVideoXml || 1>du_str_len(allRecordedVideoXml)){
            outStr="";
            return;
        }

        string xml((char*)allRecordedVideoXml);
        int endBegin = xml.rfind(end);
        if(1>endBegin){
            outStr="";
            return;
        }

        int len= xml.length() - endBegin - end.length();
        outStr = xml.substr(endBegin + end.length(), len);
    }

    bool DlnaXmlContainer::getItemStringByItemId(du_uchar* allRecordedVideoXml, std::string itemId, du_uchar** outXmlStr){
        bool ret=true;
        string begin = RecVideoXml_Item_Begin_Tag + itemId;
        static string end= RecVideoXml_Item_End_Tag;

        string tagBegin;
        string tagEnd;
        getDidlLiteDocHead(allRecordedVideoXml, tagBegin);
        getDidlLiteDocTail(allRecordedVideoXml, tagEnd);
        if(tagBegin=="" || tagEnd==""){
            return false;
        }

        if(NULL==allRecordedVideoXml || 1>du_str_len(allRecordedVideoXml) || 1>itemId.length() || NULL==outXmlStr){
            return false;
        }

        const xmlChar* retBegin = xmlStrstr(allRecordedVideoXml, (du_uchar*)begin.c_str());
        if(NULL==retBegin){
            return false;
        }

        const xmlChar* retEnd = xmlStrstr(retBegin, (du_uchar*)end.c_str());
        if(NULL==retEnd){
            return false;
        }

        int lenXml=(retEnd - retBegin)/sizeof(xmlChar) + end.length();
        int len= lenXml + tagBegin.length() + tagEnd.length() + 1;
        *outXmlStr = new du_uchar[len];
        if(NULL==*outXmlStr){
            return false;
        }
        du_uchar* outXmlStrRef= *outXmlStr;
        memset(outXmlStrRef, 0x00, len*sizeof(du_uchar));
        int len2= tagBegin.length();
        memcpy(outXmlStrRef, tagBegin.c_str(), len2);
        memcpy(&outXmlStrRef[len2], retBegin, lenXml);
        memcpy(&outXmlStrRef[len2 + lenXml], tagEnd.c_str(), tagEnd.length());
        return ret;
    }


    bool DlnaXmlContainer::getXml(std::string itemId, du_uchar** outXml){
        if(0 == itemId.size() || NULL == outXml || 0 == mXmls.size()){
            return false;
        }
        std::vector<du_uchar*>::iterator it = mXmls.begin();
        for(; it != mXmls.end(); ++it){
            du_uchar* xml = *it;
            if(xml){
                bool found = getItemStringByItemId(xml, itemId, outXml);
                if(found){
                    return true;
                }
            }
        }
        return false;
    }

    void DlnaXmlContainer::cleanAll(){
//        mVVectorString.clear();
        if (mXmls.size() > 0) {
            mXmls.clear();
        }
    }


