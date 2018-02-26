/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;

/**
 * C側の「DownloaderStatus」と対応する.
 */
public enum DlnaDlStatus {
    DOWNLOADER_STATUS_UNKNOWN,
    DOWNLOADER_STATUS_MOVING,
    DOWNLOADER_STATUS_COMPLETED,
    DOWNLOADER_STATUS_CANCELLED,
    DOWNLOADER_STATUS_ERROR_OCCURED
}
