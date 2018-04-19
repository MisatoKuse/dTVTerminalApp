/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  ビデオ＞ジャンル/サブジャンル一覧データ管理クラス.
 */
public class VideoGenreList implements Serializable {

    private static final long serialVersionUID = -9201423763597476237L;
    /**ジャンルID.*/
    private String mGenreId;
    /**タイトル.*/
    private String mTitle;
    /**パレンタル設定値.*/
    private String mRValue;
    /**コンテンツ数.*/
    private String mContentCount;
    /**本データを親とする子データのサブジャンルIDリスト.*/
    private List<String> mSubGenreIdList = null;

    /**
     * ジャンルID取得.
     * @return ジャンルID
     */
    public String getGenreId() {
        return mGenreId;
    }

    /**
     * ジャンルID設定.
     * @param genreId ジャンルID
     */
    public void setGenreId(final String genreId) {
        this.mGenreId = genreId;
    }
    /**
     * タイトル取得.
     * @return タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * タイトル設定.
     * @param title タイトル
     */
    public void setTitle(final String title) {
        this.mTitle = title;
    }

    /**
     * コンテンツ数取得.
     * @return コンテンツ数
     */
    public String getContentCount() {
        return mContentCount;
    }

    /**
     * コンテンツ数設定.
     * @param contentCount コンテンツ数
     */
    public void setContentCount(final String contentCount) {
        this.mContentCount = contentCount;
    }

    /**
     * パレンタル設定値取得.
     * @return パレンタル設定値
     */
    public String getRValue() {
        return mRValue;
    }

    /**
     * パレンタル設定値設定.
     * @param rValue パレンタル設定値
     */
    public void setRValue(final String rValue) {
        this.mRValue = rValue;
    }

    /**
     * サブジャンルデータ.
     * @param subGenreId サブジャンルID
     */
    public void addSubGenreList(final String subGenreId) {
        if (mSubGenreIdList == null) {
            mSubGenreIdList = new ArrayList<String>();
        }
        mSubGenreIdList.add(subGenreId);
    }

    /**
     * サブジャンル取得.
     * @return サブジャンルデータ
     */
    public List<String> getSubGenre() {
        return mSubGenreIdList;
    }
}
