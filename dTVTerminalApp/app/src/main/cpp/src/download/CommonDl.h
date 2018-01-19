/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVTERMINALAPP_COMMONDL_H
#define DTVTERMINALAPP_COMMONDL_H


namespace dtvt {
    //================================== define begin ==================================//
        #define DTCP_PLUS
    //=================================== define end ===================================//

    //================================== type define begin ==================================//
        typedef enum {
            DOWNLOADER_STATUS_UNKNOWN,
            DOWNLOADER_STATUS_MOVING,
            DOWNLOADER_STATUS_COMPLETED,
            DOWNLOADER_STATUS_CANCELLED,
            DOWNLOADER_STATUS_ERROR_OCCURED
        } DownloaderStatus;
    //=================================== type define end ===================================//
}

#endif //DTVTERMINALAPP_COMMONDL_H
