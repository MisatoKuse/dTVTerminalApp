/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;

/**
 * C側の「DownloaderStatus」と対応する.
 */
public enum DlnaDlStatus {
    /**UNKNOWN.*/
    DOWNLOADER_STATUS_UNKNOWN,
    /**MOVING.*/
    DOWNLOADER_STATUS_MOVING,
    /**COMPLETED.*/
    DOWNLOADER_STATUS_COMPLETED,
    /**CANCELLED.*/
    DOWNLOADER_STATUS_CANCELLED,
    /**ERROR_OCCURED.*/
    DOWNLOADER_STATUS_ERROR_OCCURED
}
