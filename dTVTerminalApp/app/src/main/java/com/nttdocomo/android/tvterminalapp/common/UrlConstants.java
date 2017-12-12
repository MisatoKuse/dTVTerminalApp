/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;


import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

public class UrlConstants {

    public class WebApiUrl {
        //検索画面
        public static final String totalSearchUrl =
                "https://sea.dtv.dmkt-sp.jp/irengine/search/index.do";

        /**
         * レコメンド情報取得APIのURL
         */
        public static final String RECOMMEND_LIST_GET_URL =
                //"http://ve.m.support2.smt.docomo.ne.jp/srermd/recommend/index.do";
                "https://ve.m.support2.smt.docomo.ne.jp/srermd/recommend/index.do";

        //TODO: 本物のぷららサーバーが提供されるまでは、テストサーバーのアドレスを指定する
        /**
         * ぷららサーバーAPIアクセス用のベースURL
         */
        private static final String PLALA_BASE_URL = "http://192.168.2.3/";

        /**
         * APIアクセスプロキシパス
         * TODO: サーバーの準備を行うまでは、ひとまず同じ値とする
         */
        private static final String PLALA_C_CLIENT = PLALA_BASE_URL;// + "/dtt_stub/c_client";
        private static final String PLALA_AUTH = PLALA_BASE_URL;// + "/dtt_stub/auth";
        private static final String PLALA_CLIENT = PLALA_BASE_URL;// + "/dtt_stub/client";

        // ログ以外は文字列を+演算子で連結するのは禁止だが、ここはstaticなので+で連結せざるを得ない。
        /**
         * VODクリップ一覧の呼び出し先
         * 
         */
        public static final String VOD_CLIP_LIST =
                PLALA_CLIENT + "vod_clip/list";

        /**
         * 視聴中ビデオ一覧
         */
        public static final String WATCH_LISTEN_VIDEO_LIST =
                PLALA_CLIENT + "viewingvideo/list";

        /**
         * TVクリップ一覧の呼び出し先
         */
        public static final String TV_CLIP_LIST =
                PLALA_CLIENT + "tv_clip/list";

        /**
         * チャンネル一覧の呼び出し先
         */
        public static final String CHANNEL_LIST =
                PLALA_CLIENT + "channel/list";

        /**
         * チャンネル毎番組一覧の呼び出し先
         */
        public static final String TV_SCHEDULE_LIST =
                PLALA_CLIENT + "channel/program/get";

        /**
         * 日毎ランク一覧の呼び出し先
         */
        public static final String DAILY_RANK_LIST =
                PLALA_CLIENT + "dayclip/count/programranking/get";

        /**
         * 週毎ランク一覧の呼び出し先
         */
        public static final String WEEKLY_RANK_LIST =
                PLALA_CLIENT + "weekclip/count/programranking/get";

        /**
         * ジャンル毎コンテンツ数
         */
        public static final String CONTENTS_NUMBER_PER_GENRE_WEB_CLIENT =
                PLALA_CLIENT + "genre/contents/count/get";

        /**
         * ジャンル毎コンテンツ一覧
         */
        public static final String CONTENTS_LIST_PER_GENRE_WEB_CLIENT =
                PLALA_CLIENT + "genre/contents/list";

        /**
         * 購入済みVOD一覧取得(レンタルビデオ用)
         */
        public static final String RENTAL_VOD_LIST_WEB_CLIENT =
                PLALA_CLIENT + "purchasedvod/list";

        /**
         * リモート録画予約一覧
         */
        public static final String REMOTE_RECORDING_RESERVATION_LIST_WEB_CLIENT =
                PLALA_C_CLIENT + "remoterecording/reservation/list";

        /**
         * 録画予約一覧
         */
        public static final String RECORDING_RESERVATION_LIST_WEB_CLIENT =
                PLALA_CLIENT + "recording/reservation/list";

        /**
         * ジャンル一覧リストファイル：こちらはAPIではなく、ファイルの直接読み込みとのこと。
         * APIではないので、例外としてURL全体を指定する
         * TODO: 当然後ほど変更する事となる。
         */
        public static final String GENRE_LIST_FILE =
                PLALA_BASE_URL +  "genreList_sample_1445.json";

        /**
         * ロール一覧リストファイル：こちらはAPIではなく、ファイルの直接読み込みとのこと。
         * APIではないので、例外としてURL全体を指定する
         * TODO: 当然後ほど変更する事となる。
         */
        public static final String ROLE_LIST_FILE =
                PLALA_BASE_URL +  "roleList_sample.json";

        /**
         * ジャンル毎コンテンツ数取得
         */
        public static final String GENRE_COUNT_GET_WEB_CLIENT =
                PLALA_BASE_URL + "genre/contents/count/get";

        /**
         * コンテンツ詳細取得
         */
        public static final String CONTENTS_DETAIL_GET_WEB_CLIENT =
                PLALA_BASE_URL + "contents/specifics/get";
    }
}
