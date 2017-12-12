/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：録画情報を表示するクラス
 */
public class DlnaRecVideoItem {

    /**
     * このクラスにて、フィールドは「public」に設定している理由は、
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */

    //mItemId
    public String mItemId="";
    //録画のタイトル
    public String mTitle="";
    //protocolInfo start
    public String mSize="";
    public String mDuration="";
    public String mResolution="";
    public String mBitrate="";
    public String mResUrl="";
    public String mUpnpIcon="";
    //protocolInfo end
    //録画の日付
    public String mDate="";

    //to do: 使用する必要があれば、新しいフィールドをここで追加
    //TODO コピー残り回数（まだ取得できない為ダミー）
    public int mAllowedUse = 0;
    /**
     * 機能：DlnaRecVideoItem情報クラスを構造
     */
    public DlnaRecVideoItem(){

    }

    /**
     * 機能：等しいか判断
     * @param item2
     * @return
     */
    public boolean equalTo(DlnaRecVideoItem item2){
        if (null==this || null==item2 || null==this.mTitle || null==item2.mTitle || null==this.mResUrl || null==item2.mResUrl) {
            return false;
        }
        boolean ret= (this.mTitle.equals(item2.mTitle) && this.mResUrl.equals(item2.mResUrl));
        return ret;
    }
}
