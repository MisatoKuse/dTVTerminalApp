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
        DlnaXmlParserBase(){}

        /**
         * 機能：xxxParserクラスのベースクラス
         * @param fileStr xml to be parsed
         * @param out output param, 失敗の場合out.size()は0
         */
        virtual void parse(void* fileStr, vector<StringVector>& out) = 0;
        
        virtual ~DlnaXmlParserBase(){}
    };
    
}   //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */


#endif //DLNA_XML_PARSER_BASE_H
