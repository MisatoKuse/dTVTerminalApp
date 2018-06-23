/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.hikari;


/**
 * 機能：ひかりTVに関して、チャンネルリストを表示するクラス.
 */
public class DlnaHikariChListItem {

    /**
     * このクラスにて、フィールドは「public」に設定している理由は、.
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */

    //mItemId
    @SuppressWarnings("PublicField")
    public String mChannelNo = "";
    /**チャンネルタイトル.*/
    @SuppressWarnings("PublicField")
    public String mTitle = "";
    //protocolInfo start
    /**コンテンツサイズ.*/
    @SuppressWarnings("PublicField")
    public String mSize = "";
    /**総再生時間(ms).*/
    @SuppressWarnings("PublicField")
    public String mDuration = "";
    /**解像度.*/
    @SuppressWarnings("PublicField")
    public String mResolution = "";
    /**ビット毎秒.*/
    @SuppressWarnings("PublicField")
    public String mBitrate = "";
    /**ダウンロードUrl.*/
    @SuppressWarnings("PublicField")
    public String mResUrl = "";
    /**サムネイル.*/
    @SuppressWarnings("PublicField")
    public String mThumbnail = "";
    //protocolInfo end
    //日付
    /**日付.*/
    @SuppressWarnings("PublicField")
    public String mDate = "";
    /**ビデオタイプ.*/
    @SuppressWarnings("PublicField")
    public String mVideoType = "";
    /**ChannelNo.*/
    @SuppressWarnings("PublicField")
    public String mChannelNr = "";
    //to do: 使用する必要があれば、新しいフィールドをここで追加

    /**
     * 機能：DlnaBsChListItem情報クラスを構造.
     */
    public DlnaHikariChListItem() {

    }
}
