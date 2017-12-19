/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <dav_cds.h>
#include <du_str.h>
#include "DlnaXmlParserBase.h"


namespace dtvt {

    DlnaXmlParserBase::DlnaXmlParserBase(DLNA_MSG_ID id):mDlnaMsgIdD(id) {}

    DLNA_MSG_ID DlnaXmlParserBase::getMsgId(){
        return mDlnaMsgIdD;
    }

    void DlnaXmlParserBase::parse(void *response, vector<StringVector>& out, std::string &containerId, std::string &isContainerId){
        //録画一覧XMLパーサー
        IfNullReturn(response);
        dupnp_http_response *newRes = ((dupnp_http_response *) response);
        IfNullReturn((char*)(newRes->body));
        /*char* xml= (char*)(newRes->body);
        char* controlUrl= (char*)(newRes->url);*/
        const du_uchar* result;
        du_str_array param_array;
        du_uint32 number_returned;
        du_uint32 total_matches;
        du_uint32 update_id;
        std::vector<std::string> recordVectorTmp;
        du_str_array_init(&param_array);
        if(!dav_cds_parse_browse_response(
                newRes->body,
                newRes->body_size,
                &param_array,
                &result,
                &number_returned,
                &total_matches,
                &update_id)){
            goto error1;
        }
        xmlDocPtr didl_doc;
        xmlNodePtr root;
        didl_doc = dav_didl_libxml_make_doc(result, du_str_len(result));
        root = xmlDocGetRootElement(didl_doc);
        if(!root){
            goto error2;
        }
        parseXmlNode(root, out, recordVectorTmp, containerId, isContainerId);
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
        return;
        error1:
        du_str_array_free(&param_array);
        error2:
        du_str_array_free(&param_array);
        xmlFreeDoc(didl_doc);
    }

    void DlnaXmlParserBase::setXmlItemValues(StringVector& out, const int key, XmlItemMap& itemMap){
        XmlItemMap::iterator i = itemMap.find(key);
        if(i==itemMap.end()){
            out.push_back("");
        } else {
            out.push_back(i->second);
        }
    }

} //end of dtvt
