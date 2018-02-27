/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <algorithm>
#include "DlnaXmlContainer.h"

namespace dtvt {

    DlnaXmlContainer::DlnaXmlContainer() : mDLNA_MSG_ID(DLNA_MSG_ID_INVALID) {

    }

    DlnaXmlContainer::~DlnaXmlContainer() {
        uninit();
    }

    void DlnaXmlContainer::uninit(){
        std::map<std::string, du_uchar*>::iterator it = mXmls.begin();
        for(; it != mXmls.begin(); ++it){
            du_uchar* uc = it->second;
            DelIfNotNull(uc);
        }

    }

    void DlnaXmlContainer::addXml(std::string itemId, du_uchar* xml) {
        if(0 < itemId.length() && NULL != xml){
            mXmls.insert(std::pair<std::string, du_uchar*>(itemId, xml));
        }
    }

    void DlnaXmlContainer::addVVectorString(VVectorString & vs){
        if(0 < vs.size()){
            std::copy(vs.begin(), vs.end(), std::back_inserter(mVVectorString));
        }
    }


    bool DlnaXmlContainer::getXml(std::string itemId, du_uchar** outXml){
        if(0 == itemId.size() || NULL == *outXml || NULL == **outXml || 0 == mXmls.size()){
            return false;
        }
        std::map<std::string, du_uchar*>::iterator it = mXmls.begin();
        for(; it != mXmls.end(); ++it){
            if(itemId.compare(it->first)){
                *outXml = it->second;
                return true;
            }
        }
        return false;
    }

    void DlnaXmlContainer::cleanAll(){
        mVVectorString.clear();
        for(std::map<std::string, du_uchar*>::iterator i = mXmls.begin(); i != mXmls.end(); ++i){
            du_uchar* xml = i->second;
            DelIfNotNull(xml);
        }
    }

}   //namespace dtvt
