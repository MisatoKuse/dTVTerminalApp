/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DLNA_BS_DIGITAL_PARSER_H
#define DLNA_BS_DIGITAL_PARSER_H

#include "DlnaXmlParserBase.h"
#include <dav_cds.h>
#include <du_str.h>

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    class DlnaBSDigitalXmlParser :public DlnaXmlParserBase{

    public:
        DlnaBSDigitalXmlParser();

        /**
         * 機能：Dlnaのxml parserライブラリを使って、コンテンツ一覧を解析して、値を戻す
         * @return
         */
        void parse(void *fileStr, vector<StringVector>& out);

        /**
         * 機能：BSデジタル xmlを解析
         * @param children
         * @param out
         * @param v1
         * @param containerId
         * @param isContainerId
         */
        void parseXmlNode(const xmlNodePtr & xmlRootNode, vector<StringVector>& out, StringVector& v1, std::string &containerId, std::string &isContainerId);

        virtual ~DlnaBSDigitalXmlParser();
    };

    //フィールド定義
    const char * const BsXmlParser_Field_Container    ="container";
    const char * const BsXmlParser_Field_Item         ="item";
    const char * const BsXmlParser_Field_Id           ="id";
    const char * const BsXmlParser_Field_Title        ="title";
    const char * const BsXmlParser_Field_Res          ="res";
    const char * const BsXmlParser_Field_ProtocolInfo ="protocolInfo";
    const char * const BsXmlParser_Field_Size         ="size";
    const char * const BsXmlParser_Field_Duration     ="duration";
    const char * const BsXmlParser_Field_Resolution   ="resolution";
    const char * const BsXmlParser_Field_Bitrate      ="bitrate";
    const char * const BsXmlParser_Field_AlbumArtURI  ="albumArtURI";
    const char * const BsXmlParser_Field_ProfileID    ="profileID";
    const char * const BsXmlParser_Field_PNG_LRG      ="PNG_LRG";
    const char * const BsXmlParser_Field_Date         ="date";
    const char * const BsXmlParser_Field_Class        ="class";
    const char * const BsXmlParser_Field_VideoItem    ="object.item.videoItem";
    const char * const BsXmlParser_Field_Videos       ="Videos";

    //ビデオ種類
    const char * const BsXmlParser_Field_Mp4           ="http-get:*:video/mp4";
    const char * const BsXmlParser_Field_Mpeg          ="http-get:*:video/mpeg";

}   //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */


#endif //DLNA_BS_DIGITAL_PARSER_H
