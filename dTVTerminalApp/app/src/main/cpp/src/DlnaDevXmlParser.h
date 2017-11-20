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
        void parse(void *info, vector<StringVector>& out);

        virtual ~DlnaDevXmlParser();
    };

}   //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */


#endif //DLNA_DEV_PARSER_H
