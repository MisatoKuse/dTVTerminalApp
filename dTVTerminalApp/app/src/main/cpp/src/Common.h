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


    //const du_uchar* BDC_FILTER = ...      --> can not compile, so do it as below
    const du_uchar BDC_FILTER[] = "res,res@duration,res@bitrate,res@sampleFrequency,res@bitsPerSample,res@nrAudioChannels,res@size,res@colorDepth,res@resolution,res@dlna:ifoFileURI,upnp:album,dc:date,dc:creator,upnp:originalTrackNumber,upnp:albumArtURI,upnp:albumArtURI@dlna:profileID";

    #define READ_TIMEOUT_MS (30000)
    #define DEFAULT_USER_AGENT (DU_UCHAR_CONST("DLNADOC/1.50"))

    //DlnaDmsItem フィールド定義
    const char * const DmsItem_Field_mUdn           ="mUdn";
    const char * const DmsItem_Field_mControlUrl    ="mContFalserolUrl";
    const char * const DmsItem_Field_mHttp          ="mHttp";
    const char * const DmsItem_Field_mFriendlyName  ="mFriendlyName";

    //DlnaRecVideoItem フィールド定義
    const char * const RecVideoItem_Field_mTitle    ="mTitle";
    const char * const RecVideoItem_Field_mDate     ="mDate";
    const char * const RecVideoItem_Field_mUpnpIcon ="mUpnpIcon";
    const char * const RecVideoItem_Field_mResUrl   ="mResUrl";

    //java string path
    const char * const Dlna_Java_String_Path = "java/lang/String";    //"Ljava/lang/String;";

    #define IfNullGoTo(var, where) { if (NULL == (var) ) { goto  where; } }
    //#define IfGoTo(var, where) { if ( !(var) ) { goto  where; } }
    #define IfNullReturn(var) { if (NULL == (var) ) { return; } }
    #define DelIfNotNull(obj) {  if(NULL!=(obj)) { delete obj; obj = NULL; }  }

} //namespace dtvt

#endif //DTVT_COMMON_H
