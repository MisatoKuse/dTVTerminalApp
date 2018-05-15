/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include <dupnp.h>
#include <dupnp_soap.h>
#include <du_byte.h>
#include <stdio.h>
#include "soap.h"
#include "../DlnaDefine.h"
#include "../DlnaMacro.h"

du_bool soap_init(SoapInfo* s, const du_uchar* userAgent) {
    du_bool retVal = 1;
    du_byte_zero((du_uint8*)s, sizeof(SoapInfo));

    du_str_array_init(&s->requestHeader);
    do {
        retVal = 0;
        BREAK_IF(!dupnp_soap_header_set_content_type(&s->requestHeader));
        BREAK_IF(!dupnp_soap_header_set_user_agent(&s->requestHeader, userAgent));
        
        BREAK_IF(!du_mutex_create(&s->mutex));
        if (du_sync_create(&s->sync)) {
        } else {
            du_mutex_free(&s->mutex);
            break;
        }
        retVal = 1;
    } while (0);
    if (0 == retVal) {
        du_str_array_free(&s->requestHeader);
    }
    return retVal;
}

void soap_free(SoapInfo* s) {
    du_str_array_free(&s->requestHeader);
    du_mutex_free(&s->mutex);
    du_sync_free(&s->sync);
}
