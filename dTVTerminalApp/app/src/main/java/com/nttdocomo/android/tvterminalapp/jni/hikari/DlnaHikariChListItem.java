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
    public String mChannelNo = "";
    /**チャンネルタイトル.*/
    public String mTitle = "";
    //protocolInfo start
    /**コンテンツサイズ.*/
    public String mSize = "";
    /**総再生時間(ms).*/
    public String mDuration = "";
    /**解像度.*/
    public String mResolution = "";
    /**ビット毎秒.*/
    public String mBitrate = "";
    /**ダウンロードUrl.*/
    public String mResUrl = "";
    /**サムネイル.*/
    public String mThumbnail = "";
    //protocolInfo end
    //日付
    /**日付.*/
    public String mDate = "";
    /**ビデオタイプ.*/
    public String mVideoType = "";
    /**ChannelNo.*/
    public String mChannelNr = "";
    //to do: 使用する必要があれば、新しいフィールドをここで追加

    /**
     * 機能：DlnaBsChListItem情報クラスを構造.
     */
    public DlnaHikariChListItem() {

    }
}
