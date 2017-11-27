/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include "DlnaDevXmlParser.h"
#include "DmsInfo.h"

namespace dtvt {

    /**
     *
     * @return
     */
    DlnaDevXmlParser::DlnaDevXmlParser(){

    }

    /**
     * IPアドレスを取得する
     *  TODO: ポート番号を取得すると Location と異なるポートが取得されるため使用しない
     *
     * @param ip
     * @param ipstr
     * @return
     */
    static int getIpAddressString(const du_ip* ip, du_uchar* ipstr){
        if (NULL != ip && NULL != ipstr){
            if (du_ip_get(ip, 0, ipstr, 0)){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 期限の時刻文字列を取得する
     *
     * @param expiration 期限
     * @param expiration_str
     * @return
     */
/*
    static int getExpirationString(const time_t *expiration, char* expiration_str){
        struct tm *ptm;
        char buf[64];

        ptm = localtime(expiration);
        strftime(buf, sizeof(buf), "%Y/%m/%d %H:%M:%S", ptm);
        strcpy(expiration_str, buf);
        return 1;
    }
*/

    /**
     * DMS情報を取得する ※DiXiM SDKライブラリ（DmsInfo.c）を使用する
     *
     * @param info
     * @param out
     */
    void DlnaDevXmlParser::parse(void *info, vector<StringVector>& out){
        du_uchar ipaddress[DU_IP_STR_SIZE] = {'\0'};    // IP アドレス
        //to do: Device information の device->user_data を解析
        IfNullReturn(info);
        dupnp_cp_dvcmgr_device *device = (dupnp_cp_dvcmgr_device*)info;
        dms_info *device_info = (dms_info*)device->user_data;

        std::vector<std::string> v1;
        std::string dms1_udn = (char*)device->udn;  // ユニーク・デバイス名
        std::string dms1_url = (char*)device->location;    // ベースURL
        std::string dms1_http = (char*)device_info->cds.control_url; // CDS情報：コントロール URL
        std::string dms1_friend = (char*)device_info->friendly_name;  // フレンドリー名
        getIpAddressString(&device->ip, ipaddress);
        std::string dms1_ipaddress = (char*)ipaddress;  // IPアドレス
/*
        // todo: その他の user_data 項目 ※必要な場合に使用する

        // デバイスタイプ
        std::string dms1_device_type = (char*)device->device_type;
        // 有効期限
        char expiration_str[32];
        getExpirationString(&device->expiration, expiration_str);
        // CDS情報：イベント・サブ URL
        std::string dms1_event_sub_url = (char*)device_info->cds.event_sub_url;
*/
        v1.push_back(dms1_udn);
        v1.push_back(dms1_url);
        v1.push_back(dms1_http);
        v1.push_back(dms1_friend);
        v1.push_back(dms1_ipaddress);
        out.push_back(v1);
    }

    DlnaDevXmlParser::~DlnaDevXmlParser(){

    }

} //namespace dtvt

