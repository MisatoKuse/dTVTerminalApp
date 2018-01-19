/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DOWNLOADER_H
#define DOWNLOADER_H

#include <ddtcp.h>
#include <du_str_array.h>
#include <dupnp.h>
#include <dupnp_dvcdsc_device.h>
#include "../Common.h"
#include "CommonDl.h"

#ifdef __cplusplus
extern "C" {
#endif


    typedef struct downloader {
        du_bool cancel_requested;
    } downloader;

    typedef void (*downloader_status_handler)(dtvt::DownloaderStatus status,
                                              const du_uchar *http_status, void *arg);

    typedef void (*downloader_progress_handler)(du_uint64 sent_size, du_uint64 total_size,
                                                void *arg);

    extern du_bool downloader_download(ddtcp dtcp,
                                       const du_uchar *dtcp1host,
                                       du_uint16 dtcp1port,
                                       const du_uchar *url,
                                       du_bool move,
                                       downloader_status_handler status_handler,
                                       downloader_progress_handler progress_handler,
                                       void *handler_arg,
                                       const du_uchar *dixim_file,
                                       const du_uchar *didl_xml,
                                       du_uint64 size,
                                       du_str_array *request_header);

    extern du_bool
    downloader_download2(downloader *x, ddtcp dtcp, const du_uchar *dtcp1host, du_uint16 dtcp1port,
                         const du_uchar *url, du_bool move,
                         downloader_status_handler status_handler,
                         downloader_progress_handler progress_handler, void *handler_arg,
                         const du_uchar *dixim_file, const du_uchar *didl_xml, du_uint64 size,
                         du_str_array *request_header);

    extern void downloader_cancel();

    extern void downloader_cancel2(downloader *x);


#ifdef __cplusplus
}
#endif

#endif
