/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DLNA_XML_PARSER_BASE_H
#define DLNA_XML_PARSER_BASE_H

#include <string>
#include <vector>
#include "Common.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    class DlnaXmlParserBase {

    public:
        DlnaXmlParserBase(DLNA_MSG_ID id);

        /**
         * 機能：xxxParserクラスのベースクラス
         * @param fileStr xml to be parsed
         * @param out output param, 失敗の場合out.size()は0
         */
        virtual void parse(void* fileStr, vector<VectorString>& out) = 0;

        /**
         * 機能：再帰する必要があるxmlにとって、xxxParserクラスで実現
         * @param children
         * @param out
         * @param v1
         * @param containerId
         * @param isContainerId
         */
        virtual void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<VectorString>& out, VectorString& v1, std::string &containerId, std::string &isContainerId)=0;

        /**
         * 機能：再帰する必要があるxmlにとって、インターフェース
         * @param response
         * @param out
         * @param containerId
         * @param isContainerId
         */
        virtual void parse(void *response, vector<VectorString>& out, std::string &containerId, std::string &isContainerId);
        
        virtual ~DlnaXmlParserBase(){}

        /**
         * 機能：DMSデバイス以外のsub parser用、sub parserを表示する。DMSデバイスのsub classは　DLNA_MSG_ID_DEV_DISP_JOIN　を固定
         * @return mDlnaMsgIdD
         */
        DLNA_MSG_ID getMsgId();
        int getImageQuality();

    protected:
        void setXmlItemValues(VectorString& out, const int key, XmlItemMap& itemMap);
        void next(xmlNodePtr& xmlChildNode, XmlItemMap& itemXmlItemMap, bool& isItVideo );
        void setXmlItemMapDefaultKey(XmlItemMap& itemMap, int key, string& defValue);
        bool hasXmlItemMapKey(XmlItemMap& itemMap, int key);

    private:
        DLNA_MSG_ID mDlnaMsgIdD;
        int mImageQuality;
    };
    
}   //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */


#endif //DLNA_XML_PARSER_BASE_H
