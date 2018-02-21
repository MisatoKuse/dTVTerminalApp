/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

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
     * TVクリップとVODクリップの取得方向（逆順）.
     */
    public static final String DIRECTION_PREV = "prev";

    /**
     * TVクリップとVODクリップの取得方向（整順）.
     */
    public static final String DIRECTION_NEXT = "next";

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
}
