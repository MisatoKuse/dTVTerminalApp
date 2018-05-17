/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DLNA_SOAP_H
#define DLNA_SOAP_H

#include <du_str_array.h>
#include <du_mutex.h>
#include <du_sync.h>

#ifdef __cplusplus
extern "C" {
#endif
typedef struct SoapInfo SoapInfo;

extern du_bool soap_init(SoapInfo* s, const du_uchar* userAgent);

extern void soap_free(SoapInfo* s);

#ifdef __cplusplus
}
#endif

#endif
