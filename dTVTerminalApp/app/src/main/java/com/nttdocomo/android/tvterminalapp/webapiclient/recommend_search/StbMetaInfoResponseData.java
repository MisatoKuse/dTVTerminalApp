/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import java.util.ArrayList;

/**
 * STBメタデータ取得結果返却用データクラス.
 */
public class StbMetaInfoResponseData {

    /**
     * コンテンツ情報.
     */
    public class Content {
        /** サービスId.*/
        public String mServiceId;
        /** カテゴリーID.*/
        public String mCategoryId;
        /** チャンネルID.*/
        public String mChannelId;
        /** コンテンツId.*/
        public String mContentsId;
        /** タイトル.*/
        public String mTitle;
        /** チャンネル名.*/
        public String mChannelName;
        /** ジャンル名.*/
        String mGenreName;
        /** サムネイルURL1.*/
        public String mCtPicURL1;
        /** サムネイルURL2.*/
        String mCtPicURL2;
        /** 視聴可能期間開始日時.*/
        public String mStartViewing;
        /** 視聴可能期間終了日時.*/
        public String mEndViewing;
        /** 課金方法フラグ.*/
        String mPaymentFlg;
        /** モバイル視聴可否フラグ.*/
        public String mMobileViewingFlg;
        /** 視聴可能下限年齢.*/
        String mViewableAge;
        /** 作品種別.*/
        public String mTitleKind;
        /** 監督名.*/
        String mDirector;
        /** 声優・キャスト名.*/
        String mCast;
        /** アーティスト名.*/
        String mArtist;
        /** 商品詳細1.*/
        public String mDescription1;
        /** 商品詳細2.*/
        public String mDescription2;
        /** 商品詳細3.*/
        public String mDescription3;
        /** 制作年.*/
        String mProductionYear;
        /** コピーライト.*/
        String mCopyright;
        /** コンテンツ長.*/
        int mContentsLength;
        /** 視聴エリアコード.*/
        String mArea;
        /** 予備1.*/
        public String mReserved1;
        /** 予備2.*/
        public String mReserved2;
        /** 予備3.*/
        public String mReserved3;
        /** 予備4.*/
        public String mReserved4;
        /** 予備5.*/
        public String mReserved5;
        /** 予備6.*/
        String mReserved6;
        /** 予備7.*/
        String mReserved7;
        /** 予備8.*/
        String mReserved8;
        /** 予備9.*/
        String mReserved9;
        /** 予備10.*/
        String mReserved10;
        /** エピソード総数.*/
        int mTotalEpisodeCount;
        /** エピソードリスト.*/
        ArrayList<Episode> mEpisodeList;
    }

    /**
     * エピソード情報.
     */
    public class Episode {
        /** エピソードID.*/
        String mEpisodeId;
        /** サブタイトル.*/
        String mSubTitle;
        /** あらすじ.*/
        String mSummary;
        /** サムネイル用画像URL1.*/
        String mCtPicURL1;
        /** サムネイル用画像URL2.*/
        String mCtPicURL2;
        /** 視聴可能期間開始日時.*/
        String mStartViewing;
        /** 視聴可能期間終了日時.*/
        String mEndViewing;
        /** 話数番号.*/
        String mEpisodeNumber;
        /** 話数表記.*/
        String mEpisodeNumberNotation;
        /** コンテンツ長.*/
        int mContentsLength;
        /** 予備1.*/
        String mReserved1;
        /** 予備2.*/
        String mReserved2;
        /** 予備3.*/
        String mReserved3;
        /** 予備4.*/
        String mReserved4;
        /** 予備5.*/
        String mReserved5;
        /** 予備6.*/
        String mReserved6;
        /** 予備7.*/
        String mReserved7;
        /** 予備8.*/
        String mReserved8;
        /** 予備9.*/
        String mReserved9;
        /** 予備10.*/
        String mReserved10;
    }

    /** 処理結果.*/
    private String mStatus = "ok";
    /** 返却コンテンツ情報.*/
    private Content mContent;
    /** 検索結果合計件数.*/
    private int mTotalCount = 1;

    /**
     * コンストラクタ.
     */
    StbMetaInfoResponseData() {
        mContent = new Content();
        ArrayList<Episode> episodeList = new ArrayList<>();
        mContent.mEpisodeList = episodeList;
    }

    /**
     * 処理結果を設定する.
     *
     * @param status 処理結果
     */
    public void setStatus(final String status) {
        this.mStatus = status;
    }

    /**
     * 検索結果合計件数を設定する.
     * @param totalCount 検索結果合計件数
     */
    void setTotalCount(final int totalCount) {
        this.mTotalCount = totalCount;
    }

    /**
     * 検索結果合計件数を返却する.
     * @return  totalCount 検索結果合計件数
     */
    public int getTotalCount() {
        return mTotalCount;
    }

    /**
     * コンテンツ情報を取得する.
     *
     * @return コンテンツ情報
     */
    public Content getContent() {
        return this.mContent;
    }

    /**
     * エピソード追加
     */
    void appendEpisode() {
        this.mContent.mEpisodeList.add(new Episode());
    }

    /**
     * エピソード・リストより処理対象のエピソードを取得する
     *
     * @return 処理対象のエピソード
     */
    Episode getLastEpisode() {
        int index = this.mContent.mEpisodeList.size() - 1;
        return this.mContent.mEpisodeList.get(index);
    }
}
