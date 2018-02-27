/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

/**
 * 検索結果詳細クラス.
 */
public class SearchContentInfo {
    /**
     * クリップフラグ.
     */
    public boolean clipFlag;
    /**
     * コンテンツID.
     */
    public String contentId;
    /**
     * サービスID.
     */
    public int serviceId;
    /**
     * サムネイルURL1.
     */
    public String contentPictureUrl1;
    /**
     * サムネイルURL2.
     */
    public String contentPictureUrl2;
    /**
     * タイトル.
     */
    public String title;
    /**
     * あらすじ.
     */
    public String synop;
    /**
     * 解説.
     */
    public String comment;
    /**
     * みどころ.
     */
    public String highlight;
    /**
     * ランク.
     */
    public String rank;
    /**
     *  mobileViewingFlg.
     */
    public String  mobileViewingFlg;

    /**
     * コンストラクタ.
     *
     * @param clipFlag  クリップフラグ
     * @param contentId コンテンツID
     * @param serviceId サービスID
     * @param contentPictureUrl1    サムネイルURL1
     * @param contentPictureUrl2    サムネイルURL2
     * @param title     タイトル
     * @param rank      ランク
     */
    public SearchContentInfo(final boolean clipFlag, final String contentId, final int serviceId,
                             final String contentPictureUrl1, final String contentPictureUrl2,
                             final String title, final int rank, final String mobileViewingFlg) {
        this.clipFlag = clipFlag;
        this.contentId = contentId;
        this.serviceId = serviceId;
        this.contentPictureUrl1 = contentPictureUrl1;
        this.contentPictureUrl2 = contentPictureUrl2;
        this.title = title;
        this.rank = String.valueOf(rank);
        this.mobileViewingFlg = mobileViewingFlg;
        //TODO:↓レコメンドサーバからコンテンツ詳細情報が取得できるようになったら、synop、comment、highlight取得に関する一連の処理を追加する
        this.synop = "";
        this.comment = "※解説(ダミー)【吹替版】" +
                "魔女の呪いによって野獣の姿に変えられてしまった王子。呪いを解く鍵は、"
                + "魔法のバラの花びらが全て散る前に誰かを心から愛し、そして愛されること―。"
                + "だが野獣の姿になった彼を愛するものなどいるはずがない。"
                + "独り心を閉ざしていく中、心に孤独を抱えながらも、自分の輝きを信じて生きる、"
                + "聡明で美しい女性、ベルと出会うが。。。";
        this.highlight = "※みどころ(ダミー)【吹替版】"
                + "魔女の呪いによって野獣の姿に変えられてしまった王子。呪いを解く鍵は、"
                + "魔法のバラの花びらが全て散る前に誰かを心から愛し、そして愛されること―。"
                + "だが野獣の姿になった彼を愛するものなどいるはずがない。"
                + "独り心を閉ざしていく中、心に孤独を抱えながらも、自分の輝きを信じて生きる、"
                + "聡明で美しい女性、ベルと出会うが。。。";
    }
}
