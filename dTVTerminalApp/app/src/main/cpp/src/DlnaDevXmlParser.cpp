/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaDevXmlParser.h"

namespace dtvt {

    DlnaDevXmlParser::DlnaDevXmlParser(){

    }

    void DlnaDevXmlParser::parse(void *info, vector<StringVector>& out){
        //to do: Device Dispcription Fileのdvcdscを解析
        IfNullReturn(info);
        dupnp_cp_dvcmgr_dvcdsc* dvcdsc= (dupnp_cp_dvcmgr_dvcdsc *) info;
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
    }

    DlnaDevXmlParser::~DlnaDevXmlParser(){

    }

} //namespace dtvt

