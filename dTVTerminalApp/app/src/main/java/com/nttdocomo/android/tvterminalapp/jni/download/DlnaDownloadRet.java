/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;

public enum DlnaDownloadRet {
    DownloadRet_Succeed,            //成功　
    DownloadRet_Unactivated,        //unactivated
    DownloadRet_CopyKeyFileFailed, //copy key file error
    DownloadRet_ParamError,
    DownloadRet_OtherError,
}
