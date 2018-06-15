/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;

import java.util.List;
import java.util.Map;

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
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static boolean isCanClip(final String dispType, final String searchOk,
                                    final String dtv, final String dtvType) {

        //クリップ可否判定用
        final String DISP_TYPE_BLANK = "";
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

        switch (dispType == null ? "" : dispType) {
            case DISP_TYPE_TV_PROGRAM:
                //tv_program の時クリップ可
                return true;
            case DISP_TYPE_VIDEO_PROGRAM:
            case DISP_TYPE_VIDEO_SERIES:
            case DISP_TYPE_VIDEO_PACKAGE:
            case DISP_TYPE_SUBSCRIPTION_PACKAGE:
            case DISP_TYPE_SERIES_SVOD:
                //tv_program 以外の時は dtv、dtvType、searchOk の各値を元に判定する
                switch (dtv == null ? "" : dtv) {
                    case DTV_FLAG_BLANK:
                    case DTV_FLAG_ZERO:
                        //dTVフラグ "0" の時は searchOk の値によってクリップ可否判定をする
                        switch (searchOk == null ? "" : searchOk) {
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
                        switch (searchOk == null ? "" : searchOk) {
                            case SEARCH_OK_FLAG_ZERO:
                            case SEARCH_OK_FLAG_BLANK:
                                return false;
                            case SEARCH_OK_FLAG_ONE:
                                switch (dtvType == null ? "" : dtvType) {
                                    case DTV_TYPE_ONE:
                                    case DTV_TYPE_TWO:
                                    case DTV_TYPE_THREE:
                                        //dtvType"1"、"2"、"3"の時はクリップ不可
                                        return false;
                                    case DTV_TYPE_BLANK:
                                        //dtvType"未設定はクリップ可
                                    default:
                                        return true;
                                }
                            default:
                                return false;
                        }
                }
            case DISP_TYPE_BLANK:
            default:
                return false;
        }
    }

    /**
     * クリップキーリストと番組情報を比較してクリップ状態を設定する.
     * @param scheduleInfo 番組表情報
     * @param mapList クリップキーリスト
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static boolean setClipStatusFromMap(final ScheduleInfo scheduleInfo, final List<Map<String, String>> mapList) {
        DTVTLogger.start();
        ClipKeyListDao.ContentTypeEnum contentType = ClipKeyListDataProvider.searchContentsType(
                scheduleInfo.getDispType(), scheduleInfo.getDtv(), scheduleInfo.getTvService());
        if (contentType != null) {
            DTVTLogger.debug("setClipStatusFromMap start contentType != null");
            switch (contentType) {
                case TV:
                    for (int k = 0; k < mapList.size(); k++) {
                        String serviceId = mapList.get(k).get(JsonConstants.META_RESPONSE_SERVICE_ID);
                        String eventId = mapList.get(k).get(JsonConstants.META_RESPONSE_EVENT_ID);
                        if (serviceId != null && serviceId.equals(scheduleInfo.getServiceId())
                                && eventId != null && eventId.equals(scheduleInfo.getEventId())) {
                            return true;
                        }
                    }
                    break;
                case VOD:
                    for (int k = 0; k < mapList.size(); k++) {
                        String crId = mapList.get(k).get(JsonConstants.META_RESPONSE_CRID);
                        if (crId != null && crId.equals(scheduleInfo.getCrId())) {
                            return true;
                        }
                    }
                    break;
                case DTV:
                    for (int k = 0; k < mapList.size(); k++) {
                        String crId = mapList.get(k).get(JsonConstants.META_RESPONSE_CRID);
                        if (crId != null && crId.equals(scheduleInfo.getCrId())) {
                            return true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        DTVTLogger.end();
        return false;
    }
}
