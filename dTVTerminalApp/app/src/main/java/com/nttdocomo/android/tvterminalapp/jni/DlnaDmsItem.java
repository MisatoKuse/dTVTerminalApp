/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

/**
 * 機能：一台のDMS設備を表示するクラス
 */
public class DlnaDmsItem {
    /**
     * このクラスにて、フィールドは「public」に設定している理由は、
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */
    //デバイスのudn名
    public String mUdn="";
    //デバイスのコントロールurl
    public String mControlUrl="";
    //デバイスのhttp
    public String mHttp="";
    //Friendly名
    public String mFriendlyName="";

    //to do: 使用する必要があれば、新しいフィールドをここで追加

    /**
     * 機能：DMS情報クラスを構造
     */
    public DlnaDmsItem(){
    }
}
