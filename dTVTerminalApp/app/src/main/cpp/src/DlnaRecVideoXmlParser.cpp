/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaRecVideoXmlParser.h"

namespace dtvt {

    /**
     *
     * @return
     */
    DlnaRecVideoXmlParser::DlnaRecVideoXmlParser(){

    }

    /**
     *
     * @param fileStr
     * @param out
     */
    void DlnaRecVideoXmlParser::parse(void *fileStr, vector<StringVector>& out){
        //to do: Device Dispcription Fileのdvcdscを解析
        IfNullReturn(fileStr);
        char* xml= (char *) fileStr;
//サンプルとして、xmlを解析して、下記のように、戻り値をoutに設定する
//            std::vector<std::string> v1;
//            std::string dms1_udn="udn";
//            std::string dms1_url="url";
//            std::string dms1_http="http";
//            std::string dms1_friend="friend";
//            v1.push_back(dms1_udn);
//            v1.push_back(dms1_url);
//            v1.push_back(dms1_http);
//            v1.push_back(dms1_friend);
//            out.push_back(v1);
//
//            std::vector<std::string> v2;
//            std::string dms2_udn="udn 2";
//            std::string dms2_url="url 2";
//            std::string dms2_http="http 2";
//            std::string dms2_friend="friend 2";
//            v2.push_back(dms2_udn);
//            v2.push_back(dms2_url);
//            v2.push_back(dms2_http);
//            v2.push_back(dms2_friend);
//            out.push_back(v2);
//              ...
    }

    /**
     *
     */
    DlnaRecVideoXmlParser::~DlnaRecVideoXmlParser(){

    }

} //namespace dtvt
