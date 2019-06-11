#ifndef ACTC_H
#define ACTC_H

#ifdef _WIN_RT
#include "PortLib.h"
#endif
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

#define ACTC_OK 0x00000000

// client
#define ACTC_ERROR_INVALID_PARAMETER 0x00011001
#define ACTC_ERROR_MEMORY_ALLOCATION 0x00011002
#define ACTC_ERROR_HWID_GET 0x00011003
#define ACTC_ERROR_HTTP_CONNECT 0x00011004
#define ACTC_ERROR_FILE_IO 0x00011005
#define ACTC_ERROR_INVALID_SERVER_RES 0x00011006
#define ACTC_ERROR_GET_PROXY 0x00011007
#define ACTC_ERROR_CANT_GET_ACTIVE_USERS_PROXY_SETTING 0x00011008
#define ACTC_ERROR 0x00011FFF

// server
#define ACTC_ERROR_SERVER 0x00020000
#define ACTC_ERROR_SERVER_ACCESS_INVALID_USERID 0x00021001
#define ACTC_ERROR_SERVER_ACCESS_INVALID_HWID 0x00021002
#define ACTC_ERROR_SERVER_ACCESS_INVALID_SECURE	0x00021003
#define ACTC_ERROR_SERVER_ACCESS 0x00021FFF
#define ACTC_ERROR_SERVER_ACT_OVER_HWID 0x00022001
#define ACTC_ERROR_SERVER_ACT_OVER_COUNT 0x00022002
#define ACTC_ERROR_SERVER_ACT_OVER_COUNT_FIXED 0x00022003
#define ACTC_ERROR_SERVER_ACT_OVER_INFINITE 0x00022004
#define ACTC_ERROR_SERVER_ACT 0x00022FFF
#define ACTC_ERROR_SERVER_DB 0x00023FFF
#define ACTC_ERROR_SERVER_RES_SECURE_VER 0x0002F001
#define ACTC_ERROR_SERVER_RES_EMPTY_DEVICEKEY 0x0002F002
#define ACTC_ERROR_SERVER_RES_MAINTENANCE 0x0002F103
#define ACTC_ERROR_SERVER_RES 0x0002FFFF


extern du_uint32 actc_go(const du_uchar* private_data_home);
extern du_uint32 actc_go_2(const du_uchar* private_data_home, const du_uchar* id2);
extern du_bool actc_is_done(const du_uchar* private_data_home, du_bool* is_done);
extern du_bool actc_reset(const du_uchar* private_data_home);

typedef enum {
    ACTC_TYPE_CLIENT = 0,
    ACTC_TYPE_SERVER = 1,
} actc_type;

extern du_uint32 actc_go_3(const du_uchar* private_data_home, actc_type at);
extern du_uint32 actc_go_4(const du_uchar* private_data_home, const du_uchar* serial_number);
extern du_uint32 actc_go_5(const du_uchar* private_data_home, du_uchar* timeinfo);

#ifdef __cplusplus
}
#endif

#endif
