/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.search;


public class SearchContentInfo {
    public boolean clipFlag; 
    public String contentId;
    public int  serviceId;
    public String contentPictureUrl;
    public String title;
    public String contentsDetailInfo;

    public SearchContentInfo(boolean clipFlag, String contentId, int serviceId, String contentPictureUrl, String title){
        this.clipFlag=clipFlag;
        this.contentId=contentId;
        this.serviceId=serviceId;
        this.contentPictureUrl=contentPictureUrl;
        this.title=title;
        //TODO:↓レコメンドサーバからコンテンツ詳細情報が取得できるようになったら、contentsDetailInfo取得に関する一連の処理を追加する
        this.contentsDetailInfo = "※データなしのためダミー表示【吹替版】" +
                "魔女の呪いによって野獣の姿に変えられてしまった王子。呪いを解く鍵は、" +
                "魔法のバラの花びらが全て散る前に誰かを心から愛し、そして愛されること―。" +
                "だが野獣の姿になった彼を愛するものなどいるはずがない。" +
                "独り心を閉ざしていく中、心に孤独を抱えながらも、自分の輝きを信じて生きる、" +
                "聡明で美しい女性、ベルと出会うが。。。";    }
}
