/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;

/* 作成中 */
/*
 * 番組クラス
 * 　　機能： 番組の属性を纏めるクラスである
 */
public class Schedule {

    private String mContent = "";
    private String mStartTime = "";
    private String mEndTime = "";
    private String mImageUrl = "";
    private String mChno = "";

    private int mHeight = 0;
    private int mTopPadding = 0;
    private int mBottomPadding = 0;

    /*
     * コンテンツを取得する
     */
    public String getContent() {
        return mContent;
    }

    /*
     * コンテンツを設定する
     */
    public void setContent(String content) {
        mContent = content;
    }

    /*
     * 開始時間を取得する
     */
    public String getStartTime() {
        return mStartTime;
    }

    /*
     * 開始時間を設定する
     */
    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    /*
     * 終了時間を取得する
     */
    public String getEndTime() {
        return mEndTime;
    }

    /*
     * 終了時間を設定する
     */
    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    /*
     * Thumbnailを取得する
     */
    public String getImageUrl() {
        return mImageUrl;
    }

    /*
     * Thumbnailを設定する
     */
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    /*
     * チャンネルNOを取得する
     */
    public String getmChno() {
        return mChno;
    }

    /*
     * チャンネルNOを設定する
     */
    public void setmChno(String mChno) {
        this.mChno = mChno;
    }

    /*
         * 高さを取得する
         */
    public int getHeight() {
        return mHeight;
    }

    /*
     * 高さを設定する
     */
    public void setHeight(int height) {
        mHeight = height;
    }

    /*
     * Top Paddingを設定する
     */
    public void setTopPadding(int p){
        mTopPadding = p;
    }

    /*
     * Top Paddingを取得する
     */
    public int getTopPadding(){
        return mTopPadding;
    }

    /*
     * Bottom Paddingを設定する
     */
    public void setBottomPadding(int p){
        mBottomPadding = p;
    }

    public int getBottomPadding(){
        return mBottomPadding;
    }

    /*
     * Top Paddingと高さを取得する
     */
    public int getHeightWithTopPadding(){
        return mTopPadding + mHeight;
    }

    /*
     * Top Padding、Bottom Paddingと高さを取得する
     */
    public int getAllHeight(){
        return mTopPadding + mHeight + mBottomPadding;
    }
}
