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
         * dアカウント登録ヘルプのURL.
         */
        public static final String STB_REGIST_D_ACCOUNT_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/support/idRegist.html";

        /**
         * DTVアプリGoogle Play ストア‎URL.
         */
        public static final String DTV_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=jp.co.nttdocomo.dtv";

        // --------------dtv 旧バージョン (「60000」未満) schema url start--------------
        /**
         * DTV起動URL.
         * 超スピード配信タイトル指定起動
         */
        public static final String SUPER_SPEED_START_TYPE = "dmktvideosc:///openLiveTitle?deliveryTitleId=";
        /**
         * DTV起動URL.
         * 作品指定アプリ起動
         */
        public static final String WORK_START_TYPE = "dmktvideosc:///openEpisode?episodeId=";
        /**
         * DTV起動URL.
         * タイトル指定アプリ起動(作品IDがない場合)
         */
        public static final String TITTLE_START_TYPE = "dmktvideosc:///openTitle?titleId=";
        /**
         * DTV起動URL.
         * タイトル指定アプリ起動(作品IDが設定されていた場合)
         */
        public static final String TITTLE_EPISODE_START_TYPE = "dmktvideosc:///openTitle?titleId=%s&episodeId=%s";
        // --------------dtv 旧バージョン (「60000」未満) schema url end--------------

        // --------------dtv 新バージョン(「60000」以上) schema url start --------------
        /**
         * DTV起動URL.
         * 超スピード配信タイトル指定起動
         */
        public static final String NEW_SUPER_SPEED_START_TYPE = "https://dtv5wyhmk.page.link/?link=https%3A%2F%2Fsmart.video.dmkt-sp.jp%2Ftitle%2Fplay%2FXXXX"
                + "&apn=jp.co.nttdocomo.dtv&ibi=com.nttdocomo.mb.dvideo&ipbi=com.nttdocomo.mb.dvideo&isi=705923000&ofl=https%3A%2F%2Fvideo.dmkt-sp.jp%2F&efr=1";
        /**
         * DTV起動URL.
         * 作品指定アプリ起動
         */
        public static final String NEW_WORK_START_TYPE = "https://dtv5wyhmk.page.link/?link=https%3A%2F%2Fsmart.video.dmkt-sp.jp%2Fproduct%2Fview%2FXXXX"
                + "&apn=jp.co.nttdocomo.dtv&ibi=com.nttdocomo.mb.dvideo&ipbi=com.nttdocomo.mb.dvideo&isi=705923000&ofl=https%3A%2F%2Fvideo.dmkt-sp.jp%2F&efr=1";
        /**
         * DTV起動URL.
         * タイトル指定アプリ起動(作品IDがない場合)
         */
        public static final String NEW_TITTLE_START_TYPE = "https://dtv5wyhmk.page.link/?link=https%3A%2F%2Fsmart.video.dmkt-sp.jp%2Ftitle%2Fview%2FXXXX"
                + "&apn=jp.co.nttdocomo.dtv&ibi=com.nttdocomo.mb.dvideo&ipbi=com.nttdocomo.mb.dvideo&isi=705923000&ofl=https%3A%2F%2Fvideo.dmkt-sp.jp%2F&efr=1";
        /**
         * DTV起動URL.
         * タイトル指定アプリ起動(作品IDが設定されていた場合)
         */
        public static final String NEW_TITTLE_EPISODE_START_TYPE = "https://dtv5wyhmk.page.link/?link=https%3A%2F%2Fsmart.video.dmkt-sp.jp%2Ftitle%2Fplay%2FXXXX"
                + "&apn=jp.co.nttdocomo.dtv&ibi=com.nttdocomo.mb.dvideo&ipbi=com.nttdocomo.mb.dvideo&isi=705923000&ofl=https%3A%2F%2Fvideo.dmkt-sp.jp%2F&efr=1";
        // --------------dtv 新バージョン(「60000」以上) schema url end --------------

        /**
         * dアニメストアアプリGoogle Play ストア‎URL.
         */
        public static final String DANIMESTORE_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.danimeapp";
        /**
         * dアニメストア起動用URL(アニメ（映像）).
         */
        public static final String DANIMESTORE_START_URL = "danimestore://openWebView?url=https://anime.dmkt-sp.jp/animestore/ci?workId=";
        /**
         * dアニメストア起動用URL(アニソン（音楽）).
         */
        public static final String D_ANIME_SONG_STORE_START_URL = "danimestore://openWebView?url=https://anime.dmkt-sp.jp/animestore/mci?workId=";

        /**
         * DAZNアプリGoogle Play ストア‎URL.
         */
        public static final String DAZN_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.dazn";

        /**
         * DAZN起動用URL.
         */
        public static final String DAZN_START_URL = "https://open.dazn.com/playVideo?eventId=";

        /**
         * dTVチャンネル起動用URL(テレビ).
         */
        public static final String DTVCHANNEL_TELEVISION_START_URL = "dch://android.dch.nttdocomo.com/viewing?chno=";
        /**
         * dTVチャンネル起動用URL(ビデオ).
         */
        public static final String DTVCHANNEL_VIDEO_START_URL = "dch://android.dch.nttdocomo.com/viewing_video?crid=";
        /**
         * dTVチャンネルアプリGoogle Play ストア‎URL.
         */
        public static final String DTVCHANNEL_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.dch";

        /**
         * 設定画面 プライバシーポリシー のURL.
         */
        public final static String SETTING_MENU_PRIVACY_POLICY_URL = "https://www.nttdocomo.co.jp/utility/privacy/";

        /**
         * 利用規約のURL.
         */
        public final static String SETTING_MENU_AGREEMENT_HTML = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/docs/agreement.html";

        /**
         * ライセンス情報のローカル HTMLファイル.
         */
        public final static String SETTING_MENU_LICENSE_URL = "file:///android_asset/licenses/oss_licenses.html";

        /**
         * FAQ画面のURL.
         */
        public final static String SETTING_MENU_FAQ_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/support/faqList.html";

        /**
         * APP画面のURL.
         */
        public final static String SETTING_MENU_APP_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/docs/app_tvt_sp.html";

        /**
         * お知らせ画面のURL.
         */
        public final static String NOTICE_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/notice/noticeList.html";

        /**
         * 初回起動ペアリング(ヘルプページ) .
         */
        public final static String SETTING_HELP_PAIRING_URL = "file:///android_asset/pairing_help/dRM-18-008.html";

        /**
         * ペアリングヘルプHTTP URL.
         */
        public final static String SETTING_SUPPORT_PAIRING_URL = "https://apl.d.dmkt-sp.jp/dtv2/tvt_sp/support/pairing.html";

        /**
         * ドコテレアプリGoogle Play ストア‎URL.
         */
        public static final String DTVT_GOOGLEPLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.tvterminalapp";

        /**
         * 契約ページのURL.
         */
        public static final String CONTRACT_URL = "https://www.hikaritv-docomo.jp/";

        /**
         * ひかりTV for docomoガイドのURL.
         */
        public static final String H4D_GUIDE_URL = "https://www.hikaritv-docomo.jp/user/guide";

        /**
         * 契約リンク先.
         */
        public static final String CONTRACT_LINK = "https://www.nttdocomo.co.jp/";
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
         * STBメタデータ取得APIのURL.
         */
        public static final String STB_META_DATA_URL = BaseUrlConstants.STB_META_DATA_URL;

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
        public static final String GENRE_LIST_FILE =
                BaseUrlConstants.PLALA_FIXED_FILE_BASE_URL + "common/client/genreList.json";

        /**
         * アプリ起動設定ファイル：こちらはAPIではなく、ファイルの直接読み込みとなる.
         * APIではないので、例外としてURL全体を指定する
         */
        public static final String SETTING_FILE =
                BaseUrlConstants.PLALA_FIXED_FILE_BASE_URL + "dtt/setting_aos.json";
                //"http://192.168.2.3:80/setting_aos.json"; //ローカルテスト用IemonサーバーのURL

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
         * 子コンテンツ詳細取得.
         */
        public static final String CHILD_CONTENTS_GET_WEB_CLIENT =
                BaseUrlConstants.PLALA_CLIENT + "meta/member";
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
                BaseUrlConstants.PLALA_C_CLIENT + "user/reservation/remote/register";

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