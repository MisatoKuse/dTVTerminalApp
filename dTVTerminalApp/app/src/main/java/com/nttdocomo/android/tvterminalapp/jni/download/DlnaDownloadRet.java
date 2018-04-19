/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;

/**
 * DlnaDownload結果.
 */
public enum DlnaDownloadRet {
    /**成功.*/
    DownloadRet_Succeed,            //成功　
    /**unactivated.*/
    DownloadRet_Unactivated,        //unactivated
    /**copy key file error.*/
    DownloadRet_CopyKeyFileFailed, //copy key file error
    /**ParamError.*/
    DownloadRet_ParamError,
    /**OtherError.*/
    DownloadRet_OtherError,
}
