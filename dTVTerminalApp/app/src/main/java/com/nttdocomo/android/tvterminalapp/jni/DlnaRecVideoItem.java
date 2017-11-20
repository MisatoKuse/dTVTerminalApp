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

    //録画のタイトル
    public String mTitle="";
    //録画の日付
    public String mDate="";
    //録画のアイオン
    public String mUpnpIcon="";
    //録画のurl
    public String mResUrl="";

    //to do: 使用する必要があれば、新しいフィールドをここで追加

    /**
     * 機能：DlnaRecVideoItem情報クラスを構造
     */
    public DlnaRecVideoItem(){

    }

}
