/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import java.util.List;

/**
 * ActivityがこのListenerを実現する
 */
public interface DlDataProviderListener extends DownloadListener {

    /**
     * DlDataProviderは初期化完了の時、これをコール
     * DlDataProviderListenerクラスは、この関数をコールされてから、
     * DlDataProviderListenerクラスの別の関数を使う
     */
    void dlDataProviderAvailable();

    /**
     * DlDataProviderは使えない状態になる
     */
    void dlDataProviderUnavailable();


    /**
     * ダウンロード状態取得
     */
    void onDownLoadListCallBack(List<DlData> list);
}
