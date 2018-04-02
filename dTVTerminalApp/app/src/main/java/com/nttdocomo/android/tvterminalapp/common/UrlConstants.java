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

    /**
     * ウェブUrl.
     */
    public static class WebUrl {

        /**
         * 外部ブラウザー遷移先URL.
         */
        public static final String PR_URL = "https://www.hikaritv.net/video";

        /**
         * 表示するWebPageのURL.
         * TODO 仮のHTMLファイル
         */
        public static final String STB_REGIST_D_ACCOUNT_URL = "file:///android_asset/first_pairing_d_account_help.html";

        /**
         * DTVグーグルショップ起動URL.
         */
        public static final String GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=jp.co.nttdocomo.dtv";
        /**
         * DTV起動URL.
         */
        public static final String SUPER_SPEED_START_TYPE = "dmktvideosc:///openLiveTitle?deliveryTitleId=";
        /**
         * DTV起動URL.
         */
        public static final String WORK_START_TYPE = "dmktvideosc:///openEpisode?episodeId=";
        /**
         * DTV起動URL.
         */
        public static final String TITTLE_START_TYPE = "dmktvideosc:///openTitle?titleId=";

        /**
         * dアニメストアグーグルショップ起動用URL.
         */
        public static final String DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.danimeapp";
        /**
         * dアニメストア起動用URL.
         */
        public static final String DANIMESTORE_START_URL = "danimestore://openWebView?url=[URL]";

        /**
         * dTVチャンネル起動用URL(テレビ).
         */
        public static final String DTVCHANNEL_TELEVISION_START_URL = "dch://android.dch.nttdocomo.com/viewing?chno=";
        /**
         * dTVチャンネル起動用URL(ビデオ).
         */
        public static final String DTVCHANNEL_VIDEO_START_URL = "dch://android.dch.nttdocomo.com/viewing_video?crid=";
        /**
         * dTVチャンネルグーグルショップ起動用URL.
         */
        public static final String DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.dch";

        /**
         * 設定画面 プライバシーポリシー URL TODO 仮のURL.
         */
        public final static String SETTING_MENU_PRIVACY_POLICY_URL = "https://www.nttdocomo.co.jp/";

        /**
         * 利用規約 URL TODO のHTMLファイル.
         */
        public final static String SETTING_MENU_TERMS_OF_SERVICE_HTML = "file:///android_asset/terms_of_service.html";

        /**
         * ライセンス URL TODO 仮のHTMLファイル.
         */
        public final static String SETTING_MENU_LICENSE_URL = "file:///android_asset/osslicense.html";

        /**
         * FAQ画面 TODO 仮のURL.
         */
        public final static String SETTING_MENU_FAQ_URL = "https://www.nttdocomo.co.jp/";

        /**
         * APP画面 TODO 仮のURL.
         */
        public final static String SETTING_MENU_APP_URL = "https://www.nttdocomo.co.jp/";

        /**
         * お知らせ画面.
         * TODO 仮のHTMLファイル
         */
        public final static String NEWS_URL = "https://www.nttdocomo.co.jp/";

        /**
         * 初回起動ペアリング(ヘルプページ) TODO 仮のHTMLファイル.
         */
        public final static String SETTING_HELP_PAIRING_URL = "file:///android_asset/first_pairing_help.html";

        /**
         * ペアリングヘルプHTTP URL.
         */
        public final static String SETTING_SUPPORT_PAIRING_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/support/pairing.html";
    }

    /**
     * ウェブApiUrl.
     */
    public static class WebApiUrl {
        /**
         * 検索サーバAPIのURL.
         */
        public static final String TOTAL_SEARCH_URL = BaseUrlConstants.TOTAL_SEARCH_URL;

        /**
         * レコメンド情報取得APIのURL.
         */
        public static final String RECOMMEND_LIST_GET_URL =
                BaseUrlConstants.RECOMMEND_BASE_URL + "recommend/index.do";

        /**
         * ログ送信用URL.
         */
        public static final String RECOMMEND_SEND_OPERATE_LOG_URL =
                BaseUrlConstants.RECOMMEND_BASE_URL + "operateLog/index.do";

        /**
         * レコメンドサーバー用のCIRCUS認証URL.
         */
        public static final String ONE_TIME_PASSWORD_AUTH_URL =
                BaseUrlConstants.ONE_TIME_PASSWORD_AUTH_URL;

        /**
         * ひかりサーバ サービストークン取得URL.
         */
        public static final String ONE_TIME_TOKEN_GET_URL =
                BaseUrlConstants.PLALA_AUTH + "daccount/authentication";
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
         */
        public static final String GENRE_LIST_FILE = "https://tconf.hikaritv-docomo.jp/common/client/genreList.json";

        /**
         * ロール一覧リストファイル：こちらはAPIではなく、ファイルの直接読み込みとのこと.
         * APIではないので、例外としてURL全体を指定する
         */
        public static final String ROLE_LIST_FILE =
                BaseUrlConstants.PLALA_ID_FILE_URL + "roleList.json";

        /**
         * ジャンル毎コンテンツ数取得.
         */
        public static final String GENRE_COUNT_GET_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "meta/genrecontents/count";

        /**
         * コンテンツ詳細取得.
         */
        public static final String CONTENTS_DETAIL_GET_WEB_CLIENT =
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