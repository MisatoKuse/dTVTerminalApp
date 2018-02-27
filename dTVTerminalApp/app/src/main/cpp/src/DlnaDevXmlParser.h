/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DLNA_DEV_PARSER_H
#define DLNA_DEV_PARSER_H

#include "DlnaXmlParserBase.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    class DlnaDevXmlParser :public DlnaXmlParserBase{

    public:
        DlnaDevXmlParser();

        /**
         * 機能：Dlnaのxml parserライブラリを使って、Dmsのdescription fileを解析して、値を戻す
         * @return
         */
        void parse(void *info, vector<VectorString>& out);

        /**
         * 機能：dmsのdescription fileは再帰する必要はないので、空で実現
         * @param children
         * @param out
         * @param v1
         * @param containerId
         * @param isContainerId
         */
        void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<VectorString>& out, VectorString& v1, std::string &containerId, std::string &isContainerId){}

        virtual ~DlnaDevXmlParser();
    };

}   //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */


#endif //DLNA_DEV_PARSER_H
