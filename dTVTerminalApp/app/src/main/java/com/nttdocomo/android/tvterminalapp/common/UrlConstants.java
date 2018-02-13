/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

/**
 * 各サーバAPIのURL.
 * ベースとなるURLはBuildValiant毎にBaseUrlConstantsクラスに定義.
 * ここではそれらに続く部分を定義する.
 */
public class UrlConstants {

    public class WebApiUrl {
        /**
         * 検索サーバAPIのURL.
         */
        public static final String TOTAL_SEARCH_URL = BaseUrlConstants.TOTAL_SEARCH_URL;

        /**
         * レコメンド情報取得APIのURL.
         */
        public static final String RECOMMEND_LIST_GET_URL = BaseUrlConstants.RECOMMEND_LIST_GET_URL;

        /**
         * ワンタイムパスワードでレコメンドサーバーを認証するURL.
         */
        public static final String ONE_TIME_PASSWORD_AUTH_URL = BaseUrlConstants.ONE_TIME_PASSWORD_AUTH_URL;
        //"https://ve.m.service.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

        /**
         * ワンタイムパスワードでサービストークンを取得する認証するURL.
         * TODO: 内容が固まるまでは、BaseUrlConstsへの記載は取りやめ。
         */
        public static final String ONE_TIME_TOKEN_GET_URL =
                "https://tif.hikaritv-docomo.jp/dtt_stub_mix/auth/daccount/authorization";
                //"https://tif.hikaritv-docomo.jp/dtt_stub_mix/auth/daccount/authentication";
                //BaseUrlConstants.ONE_TIME_TOKEN_GET_URL;

        /**
         * VODクリップ一覧の呼び出し先.
         * （定義名に最初と最後のスラッシュは無用なので注意）
         */
        public static final String VOD_CLIP_LIST =
                BaseUrlConstants.PLALA_CLIENT + "user/clip/vod";

        /**
         * 視聴中ビデオ一覧.
         */
        public static final String WATCH_LISTEN_VIDEO_LIST =
                BaseUrlConstants.PLALA_CLIENT + "user/resume/list";

        /**
         * TVクリップ一覧の呼び出し先.
         */
        public static final String TV_CLIP_LIST =
                BaseUrlConstants.PLALA_CLIENT + "user/clip/epg";

        /**
         * チャンネル一覧の呼び出し先.
         */
        public static final String CHANNEL_LIST =
                BaseUrlConstants.PLALA_CLIENT + "meta/channel";

        /**
         * チャンネル毎番組一覧の呼び出し先.
         */
        public static final String TV_SCHEDULE_LIST =
                BaseUrlConstants.PLALA_CLIENT + "meta/channelprogram";

        /**
         * 日毎ランク一覧の呼び出し先.
         */
        public static final String DAILY_RANK_LIST =
                BaseUrlConstants.PLALA_CLIENT + "meta/dailyranking";

        /**
         * 週毎ランク一覧の呼び出し先.
         */
        public static final String WEEKLY_RANK_LIST =
                BaseUrlConstants.PLALA_CLIENT + "meta/weeklyranking";

        /**
         * ジャンル毎コンテンツ数.
         */
        public static final String CONTENTS_NUMBER_PER_GENRE_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "meta/genrecontents/count";

        /**
         * ジャンル毎コンテンツ一覧.
         */
        public static final String CONTENTS_LIST_PER_GENRE_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "meta/genrecontents/list";

        /**
         * 購入済みチャンネル一覧取得.
         */
        public static final String RENTAL_CH_LIST_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/activelist/ch";

        /**
         * 購入済みVOD一覧取得(レンタルビデオ用).
         */
        public static final String RENTAL_VOD_LIST_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/activelist/vod";

        /**
         * リモート録画予約一覧.
         */
        public static final String REMOTE_RECORDING_RESERVATION_LIST_WEB_CLIENT =
                BaseUrlConstants.PLALA_C_CLIENT + "user/reservation/remote";

        /**
         * マイチャンネル一覧取得.
         */
        public static final String MY_CHANNEL_LIST_WEB_CLIENT =
                BaseUrlConstants.PLALA_C_CLIENT + "user/mychannel";

        /**
         * マイチャンネル登録.
         */
        public static final String MY_CHANNEL_SET_WEB_CLIENT =
                BaseUrlConstants.PLALA_C_CLIENT + "user/mychannel/register";

        /**
         * マイチャンネル解除.
         */
        public static final String MY_CHANNEL_RELEASE_WEB_CLIENT =
                BaseUrlConstants.PLALA_C_CLIENT + "user/mychannel/delete";

        /**
         * 録画予約一覧.
         */
        public static final String RECORDING_RESERVATION_LIST_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/reservation/stb";

        /**
         * ジャンル一覧リストファイル：こちらはAPIではなく、ファイルの直接読み込みとのこと.
         * APIではないので、例外としてURL全体を指定する
         * TODO: 当然後ほど変更する事となる。
         */
        public static final String GENRE_LIST_FILE =
                BaseUrlConstants.PLALA_BASE_URL + "genreList_sample_1445.json";

        /**
         * ロール一覧リストファイル：こちらはAPIではなく、ファイルの直接読み込みとのこと.
         * APIではないので、例外としてURL全体を指定する
         * TODO: 当然後ほど変更する事となる。
         */
        public static final String ROLE_LIST_FILE =
                BaseUrlConstants.PLALA_BASE_URL + "roleList_sample.json";

        /**
         * ジャンル毎コンテンツ数取得.
         */
        public static final String GENRE_COUNT_GET_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "meta/genrecontents/count";

        /**
         * コンテンツ詳細取得.
         */
        public static final String CONTENTS_DETAIL_GET_WEB_CLIENT =
                //PLALA_CLIENT + "meta/contentsdetail";
                BaseUrlConstants.PLALA_CLIENT + "meta/contentsdetail";

        /**
         * クリップ登録.
         */
        public static final String CLIP_REGISTER_GET_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/clip/register";

        /**
         * クリップ削除.
         */
        public static final String CLIP_DELETE_GET_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/clip/delete";

        /**
         * リモート録画予約登録.
         */
        public static final String REMOTE_RECORDING_RESERVATION_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/reservation/remote/register";

        /**
         * ユーザ情報取得.
         */
        public static final String USER_INFO_WEB_CLIENT =
                BaseUrlConstants.PLALA_AUTH + "user/info";

        /**
         * クリップキー一覧の呼び出し先.
         */
        public static final String CLIP_KEY_LIST_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "user/clip/key";
    }
}