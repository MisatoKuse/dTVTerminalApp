/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.rec;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * 機能：録画情報を表示するクラス.
 */
public class DlnaRecVideoItem {

    /**
     * このクラスにて、フィールドは「public」に設定している理由は、.
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */

    //mItemId
    public String mItemId = "";
    /**録画のタイトル.*/
    public String mTitle = "";
    //protocolInfo start
    /**コンテンツサイズ.*/
    public String mSize = "";
    /**総再生時間.*/
    public String mDuration = "";     //e.g.  "00:26:54"
    /**解像度.*/
    public String mResolution = "";
    /**ビット毎秒.*/
    public String mBitrate = "";
    /**ダウンロードUrl.*/
    public String mResUrl = "";
    /**UpnpIcon.*/
    public String mUpnpIcon = "";
    //protocolInfo end
    //録画の日付
    /**録画の日付.*/
    public String mDate = "";

    //to do: 使用する必要があれば、新しいフィールドをここで追加
    //TODO コピー残り回数（まだ取得できない為ダミー）
    /**コピー残り回数.*/
    public int mAllowedUse = 0;
    /**ビデオタイプ.*/
    public String mVideoType = "";
    /**ClearTextSize.*/
    public String mClearTextSize = "";
    /**channelName.*/
    public String mChannelName = "";    //upnp:channelName

    /**
     * 機能：DlnaRecVideoItem情報クラスを構造.
     */
    public DlnaRecVideoItem() {

    }

    /**
     * 「"00:26:54"」から分に変換.
     * @return String str
     */
    public String getDurationInMinutes() {
        if (null == mDuration) {
            return null;
        }
        String duration = mDuration.trim();
        if (8 == duration.length()) {
            try {
                String hour = duration.substring(0, 2);
                String min = duration.substring(3, 5);
                int h = Integer.parseInt(hour);
                int m = Integer.parseInt(min);
                return String.valueOf(60 * h + m);
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
        return null;
    }
}
