/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef PLAYER_MEDIA_INFO_H
#define PLAYER_MEDIA_INFO_H

#include <du_ip.h>
#include <dav_http.h>
#include <dav_didl_object.h>
#include <dav_didl_duration.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    PMI_ADDITIONAL_MM_FLAGS_PARAM_NONE,
    PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_COPY,
    PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_MOVE,
} pmi_additional_mm_flags_param;

typedef struct player_media_info {
    du_uchar* title;
    du_uchar* uri;
    du_uchar* mime_type;
    du_bool byte_seek_supported;
    du_bool time_seek_supported;
    du_bool connection_stalling_supported;
    du_uint32 duration_ms;
    dav_http_transfer_mode transfer_mode;
    du_uchar* protocol_info;

    du_uchar* original_uri;
    du_uchar* original_mime_type;

    du_bool dtcp;
    du_uchar dtcp1host[DU_IP_STR_SIZE];
    du_uint16 dtcp1port;
    du_uint16 dtcp1raport;
    pmi_additional_mm_flags_param mm_flags;
} player_media_info;

extern void player_media_info_init(player_media_info* mi);

extern void player_media_info_free(player_media_info* mi);

extern void player_media_info_reset(player_media_info* mi);

extern du_bool player_media_info_set_property(player_media_info* mi, const du_uchar* uri, dav_didl_object* object);

extern const du_uchar* player_media_info_get_title(player_media_info* mi);

extern du_uint32 player_media_info_get_duration(player_media_info* mi);

extern du_bool player_media_info_get_byte_seek_supported(player_media_info* mi);

extern du_bool player_media_info_get_time_seek_supported(player_media_info* mi);

extern du_bool player_media_info_is_connection_stalling_supported(player_media_info* mi);

extern const du_uchar* player_media_info_get_uri(player_media_info* mi);

extern const du_uchar* player_media_info_get_mime_type(player_media_info* mi);

extern dav_http_transfer_mode player_media_info_get_transfer_mode(player_media_info* mi);

extern du_bool player_media_info_get_protocol_info(player_media_info* mi, du_uchar_array* protocol_info);

extern const du_uchar* player_media_info_get_original_uri(player_media_info* mi);

extern const du_uchar* player_media_info_get_original_mime_type(player_media_info* mi);

extern const du_bool player_media_info_is_dtcp(player_media_info* mi);

extern const du_uchar* player_media_info_get_dtcp1host(player_media_info* mi);

extern du_uint16 player_media_info_get_dtcp1port(player_media_info* mi);

extern du_uint16 player_media_info_get_dtcp1raport(player_media_info* mi);

extern pmi_additional_mm_flags_param player_media_info_get_additional_mm_flags_param(player_media_info* mi);


#ifdef __cplusplus
}
#endif

#endif
