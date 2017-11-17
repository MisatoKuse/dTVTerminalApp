/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DLNA_REC_VIDEO_PARSER_H
#define DLNA_REC_VIDEO_PARSER_H

#include "DlnaXmlParserBase.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    class DlnaRecVideoXmlParser :public DlnaXmlParserBase{

    public:
        DlnaRecVideoXmlParser();

        /**
         * 機能：Dlnaのxml parserライブラリを使って、コンテンツ一覧を解析して、値を戻す
         * @return
         */
        void parse(void *fileStr, vector<StringVector>& out);

        virtual ~DlnaRecVideoXmlParser();
    };

}   //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */


#endif //DLNA_REC_VIDEO_PARSER_H
