/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DlnaDefine_h
#define DlnaDefine_h

#pragma mark - struct define

// pl&upnp
#include <du_str_array.h>
#include <du_mutex.h>
#include <du_sync.h>
#include <libxml/SAX.h>
#include <dupnp.h>
#include <dav_capability.h>
#include <dupnp_cp_dvcmgr.h>
#include <dupnp_cp_evtmgr.h>
#include <du_str.h>
#include <du_alloc.h>
#include <tr_util.h>
#include <dav_urn.h>
#include <du_log.h>

// dirag
#include <ddtcp.h>

#define SOAP_TIMEOUT_MS 30000 // 30秒
#define DLNA_LIMITMAX 50

typedef struct SoapInfo {
    du_str_array requestHeader;
    du_mutex mutex;
    du_sync sync;
    du_uint32 taskId; // task ID of SOAP request.
} SoapInfo;

// CDS information which is a member of dms_info structure.
typedef struct CdsInfo {
    du_uchar* controlUrl;
    du_uchar* eventSubscriptionUrl;
} CdsInfo;

// DMS information which is stored in device manager.
typedef struct DmsInfo {
    du_uchar* udn; // unique device name.
    du_uchar* friendly_name; // friendly name of device.
    CdsInfo cdsInfo; // information of content directory service.
} DmsInfo;

typedef struct BrowseInfo {
    DmsInfo* dmsInfo; // current DMS.
    du_uchar* containerId; // current Container ID.
    xmlDoc* didlDoc; // result of BrowseDirectChildren.
    du_uint32 startingIndex; // starting index of BrowseDirectChildren.
    du_uint32 requestedCount; // requested count of BrowseDirectChildren.
    du_uint32 numberReturned; // number returned of BrowseDirectChildren.
    du_uint32 totalMatches; // total matches of BrowseDirectChildren.
    du_uint32 updateId; // update ID of the container.
} BrowseInfo;

typedef struct DMP {
    dupnp upnpInstance;
    dav_capability davCapability;
    
    dupnp_cp_dvcmgr deviceManager;
    dupnp_cp_evtmgr eventManager;
    
    SoapInfo soapInfo;
    BrowseInfo browseInfo;
    
    ddtcp dtcp;
    du_uchar* private_data_home;
} DMP;

struct SelectDmsContext {
    du_uint32 currentIndex;
    du_uint32 targetIndex;
    DMP* dmp;
};

struct ContentInfo {
    char name[256];
    char thumbnailPath[512];
    char date[256];
    du_uint32 index;
    du_bool isContainer;
    
    // item of info
    char contentPath[256];
    char duration[256];
    char size[256];
    char protocolInfo[512];
    char objectId[256];
    char genre[256];
    char uClass[256];        // upnp:class
    char channelName[256];
    char channelNr[256];
    char rating[256];
};
enum eDiragConnectStatus {
    DiragConnectStatusNone
    , DiragConnectStatusReady
    , DiragConnectStatusConnected
    , DiragConnectStatusDetectedDisconnection
    , DiragConnectStatusGaveupReconnection
};

enum eDlnaErrorType {
    initError,
    startError,
    responseError,
};

#endif /* DlnaDefine_h */
