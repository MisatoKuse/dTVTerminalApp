/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DLNA_BROWSE_H
#define DLNA_BROWSE_H

#include <du_str_array.h>
#include <du_type.h>
#include <du_uchar.h>

#ifdef __cplusplus
extern "C" {
#endif
typedef struct BrowseInfo BrowseInfo;
typedef struct DmsInfo DmsInfo;

extern void browse_init(BrowseInfo* b, du_uint32 requested_count);
extern void browse_free(BrowseInfo* b);
    
extern du_bool browse_set_dms(BrowseInfo* b, DmsInfo* di);
extern const du_uchar* browse_get_control_url(BrowseInfo* b);
extern const du_uchar* browse_get_event_sub_url(BrowseInfo* b);
    
extern du_bool browse_set_container_id(BrowseInfo* b, const du_uchar* id);
extern du_bool browse_set_response(BrowseInfo* b, const du_uchar* result);
    


#ifdef __cplusplus
}
#endif

#endif
