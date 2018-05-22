/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.bs;


/**
 * 機能：BSデジタルに関して、チャンネルリストを表示するクラス.
 */
public class DlnaBsChListItem {

    /**
     * このクラスにて、フィールドは「public」に設定している理由は、.
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */

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
    /**日付.*/
    public String mDate = "";
    /**ビデオタイプ.*/
    public String mVideoType = "";
    /**チャンネル名.*/
    public String mChannelName = "";

    /**
     * 機能：DlnaBsChListItem情報クラスを構造.
     */
    public DlnaBsChListItem() {

    }

    /**
     * 機能：等しいか判断.
     * @param item2 item2
     * @return yes or no
     */
    public boolean equalTo(final DlnaBsChListItem item2) {
        return !(null == item2 || null == this.mChannelName || null == item2.mChannelName
                || null == this.mResUrl || null == item2.mResUrl) && this.mChannelName.
                equals(item2.mChannelName) && this.mResUrl.equals(item2.mResUrl);
    }
}
