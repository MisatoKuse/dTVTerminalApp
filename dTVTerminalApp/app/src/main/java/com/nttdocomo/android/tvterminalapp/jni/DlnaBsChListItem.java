/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：BSデジタルに関して、チャンネルリストを表示するクラス.
 */
public class DlnaBsChListItem {

    /**
     * このクラスにて、フィールドは「public」に設定している理由は、.
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */

    //チャンネルタイトル
    public String mTitle = "";
    //protocolInfo start
    public String mSize = "";
    public String mDuration = "";
    public String mResolution = "";
    public String mBitrate = "";
    public String mResUrl = "";
    //protocolInfo end

    public String mVideoType = "";

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
        if (null == item2 || null == this.mTitle || null == item2.mTitle || null == this.mResUrl || null == item2.mResUrl) {
            return false;
        }
        return this.mTitle.equals(item2.mTitle) && this.mResUrl.equals(item2.mResUrl);
    }
}
