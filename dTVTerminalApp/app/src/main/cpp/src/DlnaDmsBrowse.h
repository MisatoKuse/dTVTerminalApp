/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DlnaDmsBrowse_h
#define DlnaDmsBrowse_h

#include <stdio.h>
#include <functional>
#include <vector>
#include "DlnaDefine.h"

class DlnaDmsBrowse {
public:
    DlnaDmsBrowse();
    ~DlnaDmsBrowse();
    
    // callback
public:
    static std::function<void(std::vector<ContentInfo> contentList, const char* containerId)> ContentBrowseCallback;
    static std::function<void(const char* containerId, eDlnaErrorType error)> ContentBrowseErrorCallback;
    static std::function<void(const char* containerUpdateIds)> EventHandlerCallback;
    
    /** dmsと接続 */
    bool connectDmsWithUdn(DMP *dmp, const du_uchar* udn);
    bool connectDmsDirect(DMP *dmp, const char* friendlyName, const char* udn, const char* controlUrl, const char* eventSubUrl);
    
    void browse();
    
    /** Container選択*/
    du_bool selectContainer(DMP* d, du_uint32 index);
    bool selectContainerWithContainerId(DMP *dmp, du_uint32 offset, du_uint32 limit, const du_uchar* containerId);
    
private:
    bool browseDirectChildren(DMP* d, du_uint32 offset, du_uint32 limit = DLNA_LIMITMAX);
    
    SelectDmsContext _context;
};


#endif /* DlnaDmsBrowse_h */
