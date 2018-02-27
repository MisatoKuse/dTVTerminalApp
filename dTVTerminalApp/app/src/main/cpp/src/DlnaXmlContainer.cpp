/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <algorithm>
#include "DlnaXmlContainer.h"
#include "Dlna.h"

namespace dtvt {

    DlnaXmlContainer::DlnaXmlContainer() : mDLNA_MSG_ID(DLNA_MSG_ID_INVALID) {

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
            if(tXml){
                memset(tXml, 0x00, size * sizeof(du_uchar));
                memcpy((void *) tXml, xml, size * sizeof(du_uchar));
                mXmls.push_back(tXml);
            }
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
        std::vector<du_uchar*>::iterator it = mXmls.begin();
        for(; it != mXmls.end(); ++it){
            du_uchar* xml = *it;
            if(xml){
                bool found = Dlna::getItemStringByItemId(xml, itemId, outXml);
                if(found){
                    return true;
                }
            }
        }
        return false;
    }

    void DlnaXmlContainer::cleanAll(){
        mVVectorString.clear();
        for(std::vector<du_uchar*>::iterator i = mXmls.begin(); i != mXmls.end(); ++i){
            du_uchar* xml = *i;
            DelIfNotNull(xml);
        }
    }

}   //namespace dtvt
