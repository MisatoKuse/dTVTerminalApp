/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;

/**
 * クリップ関連のUtilクラス.
 */
public class ClipUtils {

    /**
     * 種別：ひかりＴＶビデオ、dTVチャンネル.
     */
    private static final int HIKARI_TV_VIDEO_CLIP_KEY = 0;

    /**
     * 種別：多チャンネル.
     */
    private static final int HIKARI_MULTI_CHANNEL_CLIP_KEY = 1;

    /**
     * 種別：dTV.
     */
    private static final int DTV_CLIP_KEY = 2;

    /**
     * クリップ可否フラグを返却する.
     *
     * @param dispType 表示タイプ
     * @param searchOk クリップ判定情報
     * @param dtv      dTVフラグ
     * @param dtvType  dTVタイプ
     * @return クリップ可：true,クリップ不可：false
     */
    public static boolean isCanClip(final String dispType, final String searchOk,
                                    final String dtv, final String dtvType) {

        //クリップ可否判定用
        final String DISP_TYPE_TV_PROGRAM = "tv_program";
        final String DISP_TYPE_VIDEO_PROGRAM = "video_program";
        final String DISP_TYPE_VIDEO_SERIES = "video_series";
        final String DISP_TYPE_VIDEO_PACKAGE = "video_package";
        final String DISP_TYPE_SUBSCRIPTION_PACKAGE = "subscription_package";
        final String DISP_TYPE_SERIES_SVOD = "series_svod";
        final String SEARCH_OK_FLAG_BLANK = "";
        final String SEARCH_OK_FLAG_ZERO = "0";
        final String SEARCH_OK_FLAG_ONE = "1";
        final String DTV_FLAG_BLANK = "";
        final String DTV_FLAG_ZERO = "0";
        final String DTV_FLAG_ONE = "1";
        final String DTV_TYPE_ONE = "1";
        final String DTV_TYPE_BLANK = "";
        final String DTV_TYPE_TWO = "2";
        final String DTV_TYPE_THREE = "3";

        if (dispType == null || searchOk == null || dtv == null || dtvType == null) {
            return false;
        }

        switch (dispType) {
            case DISP_TYPE_TV_PROGRAM:
                //tv_program の時は searchOk の値によってクリップ可否判定をする
                switch (searchOk) {
                    case SEARCH_OK_FLAG_BLANK:
                    case SEARCH_OK_FLAG_ZERO:
                        return false;
                    case SEARCH_OK_FLAG_ONE:
                        return true;
                    default:
                        return false;
                }
            case DISP_TYPE_VIDEO_PROGRAM:
            case DISP_TYPE_VIDEO_SERIES:
            case DISP_TYPE_VIDEO_PACKAGE:
            case DISP_TYPE_SUBSCRIPTION_PACKAGE:
            case DISP_TYPE_SERIES_SVOD:
                //tv_program 以外の時は dtv、dtvType、searchOk の各値を元に判定する
                switch (dtv) {
                    case DTV_FLAG_BLANK:
                    case DTV_FLAG_ZERO:
                        //dTVフラグ "0" の時は searchOk の値によってクリップ可否判定をする
                        switch (searchOk) {
                            case SEARCH_OK_FLAG_ZERO:
                            case SEARCH_OK_FLAG_BLANK:
                                return false;
                            case SEARCH_OK_FLAG_ONE:
                                return true;
                            default:
                                return false;
                        }
                    case DTV_FLAG_ONE:
                        //dTVフラグ "1" の時は searchOk と dtvType の値によってクリップ可否判定をする
                        switch (searchOk) {
                            case SEARCH_OK_FLAG_ZERO:
                            case SEARCH_OK_FLAG_BLANK:
                                return false;
                            case SEARCH_OK_FLAG_ONE:
                                switch (dtvType) {
                                    case DTV_TYPE_ONE:
                                    case DTV_TYPE_TWO:
                                    case DTV_TYPE_THREE:
                                        //dtvType"1"、"2"、"3"の時はクリップ不可
                                        return false;
                                    case DTV_TYPE_BLANK:
                                        //dtvType"未設定はクリップ可
                                        return true;
                                    default:
                                        return false;
                                }
                            default:
                                return false;
                        }
                }
            default:
                return false;
        }
    }

    /**
     * クリップ種別を返却.
     *
     * @param data クリップリクエスト用データ
     * @return クリップ種別(該当なしの場合は負の値を返却)
     */
    public static int getClipType(final ClipRequestData data) {
        //TODO：他のクラスから使用されない場合はprivateに変更する
        int clipStatus = -1;

        //各データの有無によりクリップ種別を取得する
        if (data != null) {
            String crId = data.getCrid();
            String service_id = data.getServiceId();
            String event_id = data.getEventId();
            String type = data.getType();
            String title_id = data.getTitleId();
            if (crId != null && crId.length() > 0) {
                clipStatus = HIKARI_TV_VIDEO_CLIP_KEY;
            } else if (service_id != null && event_id != null && type != null
                    && service_id.length() > 0 && event_id.length() > 0 && type.length() > 0) {
                clipStatus = HIKARI_MULTI_CHANNEL_CLIP_KEY;
            } else if (title_id != null && title_id.length() > 0) {
                clipStatus = DTV_CLIP_KEY;
            }
        }
        return clipStatus;
    }

    /**
     * クリップ状態をDB保存.
     *
     * @param data クリップリクエスト用データ
     * @return DB保存実行開始フラグ
     */
    public static boolean setClipStatusToDB(final ClipRequestData data) {

        boolean result = false;

        //クリップ種別に応じたDB保存処理を実施
        if (data != null) {
            int clipType = getClipType(data);
            switch (clipType) {
                case HIKARI_TV_VIDEO_CLIP_KEY:
                    //TODO:クリップDB登録処理
                    result = true;
                    break;
                case HIKARI_MULTI_CHANNEL_CLIP_KEY:
                    //TODO:クリップDB登録処理
                    result = true;
                    break;
                case DTV_CLIP_KEY:
                    //TODO:クリップDB登録処理
                    result = true;
                    break;
                default:
                    result = false;
                    break;
            }
        }
        return result;
    }
}
