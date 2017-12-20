/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

/**
 * リクエストパラメータ定数定義クラス
 */
public class JsonContents {

    public static final String UNDER_LINE = "_";

    /**
     * VOD＆EPGマージメタレスポンス（縮小版）
     * listパラメータ
     */
    public static final String[] LIST_PARA = {JsonContents.META_RESPONSE_CRID,
            JsonContents.META_RESPONSE_CID, JsonContents.META_RESPONSE_TITLE_ID,
            JsonContents.META_RESPONSE_EPISODE_ID, JsonContents.META_RESPONSE_TITLE,
            JsonContents.META_RESPONSE_EPITITLE, JsonContents.META_RESPONSE_TITLERUBY,
            JsonContents.META_RESPONSE_DISP_TYPE, JsonContents.META_RESPONSE_DISPLAY_START_DATE,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, JsonContents.META_RESPONSE_AVAIL_START_DATE,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, JsonContents.META_RESPONSE_PUBLISH_START_DATE,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, JsonContents.META_RESPONSE_NEWA_START_DATE,
            JsonContents.META_RESPONSE_NEWA_END_DATE, JsonContents.META_RESPONSE_THUMB_640,
            JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_DTV_THUMB_640,
            JsonContents.META_RESPONSE_DTV_THUMB_448, JsonContents.META_RESPONSE_COPYRIGHT,
            JsonContents.META_RESPONSE_DUR, JsonContents.META_RESPONSE_DEMONG,
            JsonContents.META_RESPONSE_BVFLG, JsonContents.META_RESPONSE_4KFLG,
            JsonContents.META_RESPONSE_HDRFLG, JsonContents.META_RESPONSE_DELIVERY,
            JsonContents.META_RESPONSE_R_VALUE, JsonContents.META_RESPONSE_ADULT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, JsonContents.META_RESPONSE_SYNOP,
            JsonContents.META_RESPONSE_SYNOP_SHORT, JsonContents.META_RESPONSE_PUID,
            JsonContents.META_RESPONSE_PRICE, JsonContents.META_RESPONSE_QRANGE,
            JsonContents.META_RESPONSE_QUNIT, JsonContents.META_RESPONSE_PU_START_DATE,
            JsonContents.META_RESPONSE_PU_END_DATE, JsonContents.META_RESPONSE_CREDIT_ARRAY,
            JsonContents.META_RESPONSE_RATING, JsonContents.META_RESPONSE_DTV,
            JsonContents.META_RESPONSE_CHSVOD, JsonContents.META_RESPONSE_SEARCH_OK,
            JsonContents.META_RESPONSE_LIINF_ARRAY, JsonContents.META_RESPONSE_PUINF,
            JsonContents.META_RESPONSE_CAPL, JsonContents.META_RESPONSE_BILINGAL,
            JsonContents.META_RESPONSE_TV_CID, JsonContents.META_RESPONSE_SERVICE_ID,
            JsonContents.META_RESPONSE_EVENT_ID, JsonContents.META_RESPONSE_CHNO,
            JsonContents.META_RESPONSE_TV_SERVICE, JsonContents.META_RESPONSE_CONTENT_TYPE,
            JsonContents.META_RESPONSE_VOD_START_DATE, JsonContents.META_RESPONSE_VOD_END_DATE,
            JsonContents.META_RESPONSE_MAIN_GENRE, JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY,
            JsonContents.META_RESPONSE_COPY, JsonContents.META_RESPONSE_ADINFO_ARRAY,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY};

    /**
     * VOD＆EPGマージメタレスポンス（縮小版）
     * list ＞ PUINF パラメータ
     */
    public static final String[] PUINF_PARA = {JsonContents.META_RESPONSE_PUID,
            JsonContents.META_RESPONSE_CRID, JsonContents.META_RESPONSE_TITLE,
            JsonContents.META_RESPONSE_EPITITLE, JsonContents.META_RESPONSE_DISP_TYPE,
            JsonContents.META_RESPONSE_CHSVOD, JsonContents.META_RESPONSE_PRICE,
            JsonContents.META_RESPONSE_QUNIT, JsonContents.META_RESPONSE_QRANGE,
            JsonContents.META_RESPONSE_PU_START_DATE, JsonContents.META_RESPONSE_PU_END_DATE};

    /**
     * 1：番組指定予約
     * 3：日時指定予約
     * 4：日時指定定期予約
     */
    public static final String META_RESPONSE_RESV_TYPE = "resv_type";
    /**
     * 開始予定エポック秒
     * loop_type_numが
     * 0の場合：予約開始時エポック秒
     * 0以外の場合：0時0分0秒からの通算秒
     */
    public static final String META_RESPONSE_START_TIME = "start_time";
    /**
     * 定期予約指定値、0~10
     * 定期予約しない：0
     * 毎週月曜：1 ～ 毎週日曜：7
     * 毎週月～金：8、 毎週月～土：9、 毎日：10
     */
    public static final String META_RESPONSE_LOOP_TYPE_NUM = "loop_type_num";
    /**
     * 同期状態
     * 1：チューナー反映待ち
     * 2：チューナー反映中
     * 3：チューナー反映済み
     * 4：チューナー反映失敗
     */
    public static final String META_RESPONSE_SYNC_STATUS = "sync_status";
    /**
     * 同期失敗理由
     * 1：ネットワークエラー
     * 2：パラメータエラー
     * 3：最大予約件数超過
     * 4：重複した予約
     * 5：予約時間異常
     * 6：予約チャンネル異常
     * 9：その他のエラー
     */
    public static final String META_RESPONSE_ = "sync_error_reason";
    /**
     * titleruby_asc：タイトルルビ昇順
     * avail_s_asc：配信開始日昇順
     * avail_e_desc：配信終了日降順
     * play_count_desc：人気順（前日の視聴回数数降順）
     */
    public static final String META_RESPONSE_SORT = "sort";
    /**
     * dch：dチャンネル
     * hikaritv：ひかりTVの多ch
     * 指定なしの場合：すべて
     */
    public static final String META_RESPONSE_TYPE = "type";
    /**
     * release、testa、demo
     * ※指定なしの場合release
     */
    public static final String META_RESPONSE_FILTER = "filter";
    // 予約時間の長さ（秒）
    public static final String META_RESPONSE_DURATION = "duration";
    // プラットフォームタイプ
    public static final String META_RESPONSE_PLATFORM_TYPE = "platform_type";
    // レジューム位置
    public static final String META_RESPONSE_STOP_POSITION = "stopposition";
    // レジュームメタ情報
    public static final String META_RESPONSE_METADATE_LIST = "metadata_list";
    // レジューム位置情報
    public static final String META_RESPONSE_RESUME_LIST = "resume_list";
    // ch_list
    public static final String META_RESPONSE_CH_LIST = "ch_list";
    // date_list
    public static final String META_RESPONSE_DATE_LIST = "date_list";
    // 日付、now
    public static final String META_RESPONSE_DATE = "date";
    // 取得方向
    public static final String META_RESPONSE_DIRECTION = "direction";
    // 年齢設定値
    public static final String META_RESPONSE_AGE_REQ = "age_req";
    // ジャンルID
    public static final String META_RESPONSE_GENRE_ID = "genre_id";
    // 有効期限一覧
    public static final String META_RESPONSE_ACTIVE_LIST = "active_list";
    // ライセンスID
    public static final String META_RESPONSE_LICENSE_ID = "license_id";
    // 有効期限
    public static final String META_RESPONSE_VAILD_END_DATE = "valid_end_date";
    // 予約ID
    public static final String META_RESPONSE_RESV_ID = "resv_id";
    // OK
    public static final String META_RESPONSE_STATUS = "status";
    //OK
    public static final String META_RESPONSE_STATUS_OK = "OK";
    // コンテンツ配列
    public static final String META_RESPONSE_LIST = "list";
    // ページャ
    public static final String META_RESPONSE_PAGER = "pager";
    // レスポンスの最大件数
    public static final String META_RESPONSE_PAGER_LIMIT = "limit";
    // レスポンスの最大件数
    public static final String META_RESPONSE_UPPER_LIMIT = "upper_limit";
    // レスポンスの最小件数
    public static final String META_RESPONSE_LOWER_LIMIT = "lower_limit";
    // 取得位置(1～)
    public static final String META_RESPONSE_OFFSET = "offset";
    // レスポンス(list)件数
    public static final String META_RESPONSE_COUNT = "count";
    // 全体の件数
    public static final String META_RESPONSE_TOTAL = "total";
    // crid
    public static final String META_RESPONSE_CRID = "crid";
    // コンテンツID
    public static final String META_RESPONSE_CONTENTS_ID = "id";
    // コンテンツNAME
    public static final String META_RESPONSE_CONTENTS_NAME = "name";
    // サービスID
    public static final String META_RESPONSE_SERVICE_ID = "service_id";
    // チャンネル番号
    public static final String META_RESPONSE_CHNO = "chno";
    // タイトル
    public static final String META_RESPONSE_TITLE = "title";
    // タイトルルビ
    public static final String META_RESPONSE_TITLERUBY = "titleruby";
    // index
    public static final String META_RESPONSE_INDEX = "index";
    // 表示タイプ
    public static final String META_RESPONSE_DISP_TYPE = "disp_type";
    // サービス
    public static final String META_RESPONSE_SERVICE = "service";
    // チャンネルタイプ
    public static final String META_RESPONSE_CH_TYPE = "ch_type";
    // 有効開始日時
    public static final String META_RESPONSE_AVAIL_START_DATE = "avail_start_date";
    // 有効期限日時
    public static final String META_RESPONSE_AVAIL_END_DATE = "avail_end_date";
    // サムネイル(
    public static final String META_RESPONSE_DEFAULT_THUMB = "thumb";
    // サムネイル（640＊360）
    public static final String META_RESPONSE_THUMB_640 = "thumb_640_360";
    // サムネイル（448＊252）
    public static final String META_RESPONSE_THUMB_448 = "thumb_448_252";
    // dtvサムネイル（640＊360）
    public static final String META_RESPONSE_DTV_THUMB_640 = "dtv_thumb_640_360";
    // dtvサムネイル（448＊252）
    public static final String META_RESPONSE_DTV_THUMB_448 = "dtv_thumb_448_252";
    // デモフラグ
    public static final String META_RESPONSE_DEMONG = "demong";
    // 4Kフラグ
    public static final String META_RESPONSE_4KFLG = "4kflg";
    // 配信ステータス(チャンネルメタレスポンス)
    public static final String META_RESPONSE_AVAIL_STATUS = "avail_status";
    // 配信ステータス
    public static final String META_RESPONSE_DELIVERY = "delivery";
    // パレンタル情報
    public static final String META_RESPONSE_R_VALUE = "r_value";
    // アダルトフラグ
    public static final String META_RESPONSE_ADULT = "adult";
    // NGファンク
    public static final String META_RESPONSE_NG_FUNC = "ng_func";
    // ジャンル（ARIB）
    public static final String META_RESPONSE_GENRE_ARRAY = "genre_array";
    // あらすじ（long）
    public static final String META_RESPONSE_SYNOP = "synop";
    // あらすじ（short）
    public static final String META_RESPONSE_SYNOP_SHORT = "synop_short";
    // パーチャスID
    public static final String META_RESPONSE_PUID = "puid";
    // サブパーチャスID
    public static final String META_RESPONSE_SUB_PUID = "sub_puid";
    // 価格(税込)
    public static final String META_RESPONSE_PRICE = "price";
    // 購入単位の期間(3日の3)
    public static final String META_RESPONSE_QRANGE = "qrange";
    // 購入単位の単位(3日の「日」)
    public static final String META_RESPONSE_QUNIT = "qunit";
    // 販売開始日時
    public static final String META_RESPONSE_PU_START_DATE = "pu_start_date";
    // 販売終了日時
    public static final String META_RESPONSE_PU_END_DATE = "pu_end_date";
    // チャンネルパック情報
    public static final String META_RESPONSE_CHPACK = "CHPACK";
    // コンテンツID
    public static final String META_RESPONSE_CID = "cid";
    // タイトルID（dTV）
    public static final String META_RESPONSE_TITLE_ID = "title_id";
    // エピソードID（dTV）
    public static final String META_RESPONSE_EPISODE_ID = "episode_id";
    // エピソードタイトル
    public static final String META_RESPONSE_EPITITLE = "epititle";
    // 表示開始日時
    public static final String META_RESPONSE_DISPLAY_START_DATE = "display_start_date";
    // 表示終了日時
    public static final String META_RESPONSE_DISPLAY_END_DATE = "display_end_date";
    // 有効開始日時
    public static final String META_RESPONSE_PUBLISH_START_DATE = "publish_start_date";
    // 有効期限日時
    public static final String META_RESPONSE_PUBLISH_END_DATE = "publish_end_date";
    // 新着期間開始
    public static final String META_RESPONSE_NEWA_START_DATE = "newa_start_date";
    // 新着期間終了
    public static final String META_RESPONSE_NEWA_END_DATE = "newa_end_date";
    // コピーライト
    public static final String META_RESPONSE_COPYRIGHT = "copyright";
    // 尺長
    public static final String META_RESPONSE_DUR = "dur";
    // 見放題フラグ
    public static final String META_RESPONSE_BVFLG = "bvflg";
    // HDRフラグ
    public static final String META_RESPONSE_HDRFLG = "hdrflg";
    // 出演者情報（ロール|出演者名）
    public static final String META_RESPONSE_CREDIT_ARRAY = "credit_array";
    // レーティング値
    public static final String META_RESPONSE_RATING = "rating";
    // dTVフラグ
    public static final String META_RESPONSE_DTV = "dtv";
    // CHSVOD
    public static final String META_RESPONSE_CHSVOD = "chsvod";
    // クリップ判定に利用
    public static final String META_RESPONSE_SEARCH_OK = "search_ok";
    // ライセンス情報リスト
    public static final String META_RESPONSE_LIINF_ARRAY = "liinf_array";
    // 販売情報リスト
    public static final String META_RESPONSE_PUINF = "PUINF";
    // 字幕
    public static final String META_RESPONSE_CAPL = "capl";
    // 二ヶ国語
    public static final String META_RESPONSE_BILINGAL = "bilingal";
    // コンテンツID（見逃し、関連VOD用）
    public static final String META_RESPONSE_TV_CID = "tv_cid";
    // イベントID
    public static final String META_RESPONSE_EVENT_ID = "event_id";
    // 放送種別
    public static final String META_RESPONSE_TV_SERVICE = "tv_service";
    // 見逃しタイプ
    public static final String META_RESPONSE_CONTENT_TYPE = "content_type";
    // VOD配信開始日時
    public static final String META_RESPONSE_VOD_START_DATE = "vod_start_date";
    // VOD配信終了日時
    public static final String META_RESPONSE_VOD_END_DATE = "vod_end_date";
    // 主ジャンル（ARIB）
    public static final String META_RESPONSE_MAIN_GENRE = "main_genre";
    // 副ジャンル（ARIB）
    public static final String META_RESPONSE_SECOND_GENRE_ARRAY = "second_genre_array";
    // コピー制御
    public static final String META_RESPONSE_COPY = "copy";
    // 音声情報
    public static final String META_RESPONSE_ADINFO_ARRAY = "adinfo_array";
    // 関連VODのcrid
    public static final String META_RESPONSE_RELATIONAL_ID_ARRAY = "relational_id_array";
}