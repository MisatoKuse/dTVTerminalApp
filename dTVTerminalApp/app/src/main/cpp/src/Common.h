/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVT_COMMON_H
#define DTVT_COMMON_H

#include <jni.h>
#include <dupnp.h>
#include <dupnp_cp_dvcmgr.h>
#include <dupnp_cp_evtmgr.h>
#include <du_libxml.h>
#include <dav_didl_libxml.h>
#include <map>
#include <string>
#include <vector>

namespace dtvt {

    typedef struct soap {
        du_str_array request_header;
        du_mutex mutex;
        du_sync sync;
        du_uint32 id; // task ID of SOAP request.
    } soap;

    typedef struct dmp {
        dupnp upnp;             // UPnP stack instance.
        dupnp_cp_dvcmgr deviceManager;  // device manager.
        dupnp_cp_evtmgr eventManager;   // event manager.
        soap soap; // SOAP information.
    } dmp;

    typedef std::vector<std::string> StringVector;
    typedef std::map<int, std::string> XmlItemMap;
    typedef std::vector<XmlItemMap> MapVector;

    typedef enum {
        //Browseコンテンツ
        DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST = 0,
        //デバイスjoin
        DLNA_MSG_ID_DEV_DISP_JOIN = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 1,
        //デバイスleave
        DLNA_MSG_ID_DEV_DISP_LEAVE = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 2,
        //BSデジタルに関して、チャンネルリストを発見
        DLNA_MSG_ID_BS_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 3,
        //地上波(terrestrial)
        DLNA_MSG_ID_TER_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 4,
        //ひかりTV
        DLNA_MSG_ID_HIKARI_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 5,
        //Download progress
        DLNA_MSG_ID_DL_PROGRESS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 6,
        //Download progress
        DLNA_MSG_ID_DL_STATUS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 7,
        //Download param
        DLNA_MSG_ID_DL_XMLPARAM = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 8,
        //invalid value
        DLNA_MSG_ID_INVALID = 0xffffffff,
    } DLNA_MSG_ID;

//    typedef enum {
//        DOWNLOADER_STATUS_UNKNOWN,
//        DOWNLOADER_STATUS_MOVING,
//        DOWNLOADER_STATUS_COMPLETED,
//        DOWNLOADER_STATUS_CANCELLED,
//        DOWNLOADER_STATUS_ERROR_OCCURED
//    } DownloaderStatus;


    //const du_uchar* BDC_FILTER = ...      --> can not compile, so do it as below
    //const du_uchar BDC_FILTER[] = "res,res@duration,res@bitrate,res@sampleFrequency,res@bitsPerSample,res@nrAudioChannels,res@size,res@colorDepth,res@resolution,res@dlna:ifoFileURI,upnp:album,dc:date,dc:creator,upnp:originalTrackNumber,upnp:albumArtURI,upnp:albumArtURI@dlna:profileID";
    const du_uchar BDC_FILTER[] = "*";  //todo 実機でnasuneからの結果を見ると、すべての情報が取得できるが、標準なAPI仕様でサポートするかを確認する必要

    #define READ_TIMEOUT_MS (30000)
    #define DEFAULT_USER_AGENT (DU_UCHAR_CONST("DLNADOC/1.50"))

    //DlnaDmsItem フィールド定義
    const char * const DmsItem_Field_mUdn           ="mUdn";
    const char * const DmsItem_Field_mControlUrl    ="mControlUrl";
    const char * const DmsItem_Field_mHttp          ="mHttp";
    const char * const DmsItem_Field_mFriendlyName  ="mFriendlyName";
    const char * const DmsItem_Field_mIPAddress     ="mIPAddress";

    //DlnaRecVideoItem フィールド定義
    const char * const RecVideoItem_Field_mItemId     ="mItemId";
    const char * const RecVideoItem_Field_mTitle      ="mTitle";
    const char * const RecVideoItem_Field_mSize       ="mSize";
    const char * const RecVideoItem_Field_mDuration   ="mDuration";
    const char * const RecVideoItem_Field_mResolution ="mResolution";
    const char * const RecVideoItem_Field_mBitrate    ="mBitrate";
    const char * const RecVideoItem_Field_mResUrl     ="mResUrl";
    const char * const RecVideoItem_Field_mUpnpIcon   ="mUpnpIcon";
    const char * const RecVideoItem_Field_mDate       ="mDate";
    const char * const RecVideoItem_Field_mVideoType       ="mVideoType";
    const char * const RecVideoItem_Field_mClearTextSize   ="mClearTextSize";
    
    //DlnaBsChListItem フィールド定義
    const char * const DlnaBsChListItem_Field_mChannelNo ="mChannelNo";
    const char * const DlnaBsChListItem_Field_mTitle = "mTitle";
    const char * const DlnaBsChListItem_Field_mSize =  "mSize";
    const char * const DlnaBsChListItem_Field_mDuration =  "mDuration";
    const char * const DlnaBsChListItem_Field_mResolution =  "mResolution";
    const char * const DlnaBsChListItem_Field_mBitrate =  "mBitrate";
    const char * const DlnaBsChListItem_Field_mResUrl =  "mResUrl";
    //const char * const DlnaBsChListItem_Field_mThumbnail =  "mThumbnail";
    const char * const DlnaBsChListItem_Field_mDate =  "mDate";
    const char * const DlnaBsChListItem_Field_mVideoType       ="mVideoType";

    //DlnaTerChListItem フィールド定義
    const char * const DlnaTerChListItem_Field_mChannelNo ="mChannelNo";
    const char * const DlnaTerChListItem_Field_mTitle = "mTitle";
    const char * const DlnaTerChListItem_Field_mSize =  "mSize";
    const char * const DlnaTerChListItem_Field_mDuration =  "mDuration";
    const char * const DlnaTerChListItem_Field_mResolution =  "mResolution";
    const char * const DlnaTerChListItem_Field_mBitrate =  "mBitrate";
    const char * const DlnaTerChListItem_Field_mResUrl =  "mResUrl";
    //const char * const DlnaTerChListItem_Field_mThumbnail =  "mThumbnail";
    const char * const DlnaTerChListItem_Field_mDate =  "mDate";
    const char * const DlnaTerChListItem_Field_mVideoType       ="mVideoType";

    //DlnaHikariChListItem フィールド定義
    const char * const DlnaHikariChListItem_Field_mChannelNo ="mChannelNo";
    const char * const DlnaHikariChListItem_Field_mTitle = "mTitle";
    const char * const DlnaHikariChListItem_Field_mSize =  "mSize";
    const char * const DlnaHikariChListItem_Field_mDuration =  "mDuration";
    const char * const DlnaHikariChListItem_Field_mResolution =  "mResolution";
    const char * const DlnaHikariChListItem_Field_mBitrate =  "mBitrate";
    const char * const DlnaHikariChListItem_Field_mResUrl =  "mResUrl";
    //const char * const DlnaHikariChListItem_Field_mThumbnail =  "mThumbnail";
    const char * const DlnaHikariChListItem_Field_mDate =  "mDate";
    const char * const DlnaHikariChListItem_Field_mVideoType       ="mVideoType";

    //java string path
    //const char * const Dlna_Java_String_Path = "java/lang/String";  error on android 8.x
    const char * const Dlna_Java_String_Path = "Ljava/lang/String;";

    //xml item key from DlnaRecVideoItem.java、「DlnaRecVideoXmlParser、DlnaTerChXmlParser、DlnaBsDigitalXmlParser」に共用される
    const int Xml_Item_Id=1;
    const int Xml_Item_Title= Xml_Item_Id + 1;
    const int Xml_Item_Size= Xml_Item_Id + 2;
    const int Xml_Item_Duration= Xml_Item_Id + 3;
    const int Xml_Item_Resolution= Xml_Item_Id + 4;
    const int Xml_Item_Bitrate= Xml_Item_Id + 5;
    const int Xml_Item_ResUrl= Xml_Item_Id + 6;
    const int Xml_Item_UpnpIcon= Xml_Item_Id + 7;
    const int Xml_Item_Date= Xml_Item_Id + 8;
    const int Xml_Item_AllowedUse= Xml_Item_Id + 9;
    const int Xml_Item_VideoType= Xml_Item_Id + 10;   //mVideoType
    const int Xml_Item_ClearTextSize= Xml_Item_Id + 11;   //mClearTextSize

    //for searching end tag
    const char * const RecVideoXml_Item_Begin_Tag ="<item id=\"";
    const char * const RecVideoXml_Item_End_Tag ="</item>";



    #define IfNullGoTo(var, where) { if (NULL == (var) ) { goto  where; } }
    //#define IfGoTo(var, where) { if ( !(var) ) { goto  where; } }
    #define IfNullReturn(var) { if (NULL == (var) ) { return; } }
    #define IfNullReturnFalse(var) { if (NULL == (var) ) { return false; } }
    #define DelIfNotNull(obj) {  if(NULL != (obj) ) { delete obj; obj = NULL; }  }
    #define DelIfNotNullArray(obj) {  if(NULL!=(obj)) { delete[] obj; obj = NULL; }  }

    //開発段階にて、本番のDMSはないので、仮DMSを使っていますが、違うDMSを定義し、どのDMSを選択できるよう
    //#define DLNA_KARI_DMS_UNIVERSAL
    #define DLNA_KARI_DMS_NAS
    //#define DLNA_KARI_DMS_RELEASE

    #if defined(DLNA_KARI_DMS_UNIVERSAL)
        const char* const DLNA_DMS_ROOT = "0";
    #elif defined(DLNA_KARI_DMS_NAS)
        const char* const DLNA_DMS_ROOT = "0/video/all";
    #elif defined(DLNA_KARI_DMS_RELEASE)
        //チューナールート/スマホ向け/録画一覧
        //const char* const DLNA_DMS_RECORD_LIST = "0/smartphone/rec/all"; //本番
        const char* const DLNA_DMS_RECORD_LIST = "0/video/all"; //nasでテスト

        //チューナールート/スマホ向け/多チャンネル
        const char* const DLNA_DMS_MULTI_CHANNEL = "0/smartphone/ip";

        //チューナールート/スマホ向け/地上デジタル
        //const char* const DLNA_DMS_TER_CHANNEL = "0/smartphone/tb"; //本番
        const char* const DLNA_DMS_TER_CHANNEL = "0/video/all"; //nasでテスト

        //チューナールート/スマホ向け/BSデジタル
        //const char* const DLNA_DMS_BS_CHANNEL = "0/smartphone/bs"; //本番
        const char* const DLNA_DMS_BS_CHANNEL = "0/video/all"; //nasでテスト
    #endif

    /*----------------------------- function begin --------------------------------*/
    extern bool setJavaObjectField(JNIEnv *env, jclass cls, const char* const  fieldName, const char* const classPath, std::string& value, jobject obj);
    /*------------------------------ function end ---------------------------------*/

} //namespace dtvt

#endif //DTVT_COMMON_H
