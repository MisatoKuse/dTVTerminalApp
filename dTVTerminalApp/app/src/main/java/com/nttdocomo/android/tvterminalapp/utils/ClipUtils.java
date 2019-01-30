/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;

import java.util.List;
import java.util.Map;

/**
 * クリップ関連のUtilクラス.
 */
public class ClipUtils {
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
     * クリップキーリストとコンテンツ情報を比較してクリップ状態を設定する.
     * @param vodMetaFullData コンテンツ情報
     * @param mapList クリップ状態
     */
    public static boolean setClipStatusVodMetaData(final VodMetaFullData vodMetaFullData, final List<Map<String, String>> mapList) {
        return vodMetaFullData != null && setClipStatusFromMap(mapList,
                vodMetaFullData.getDisp_type(),
                vodMetaFullData.getDtv(),
                vodMetaFullData.getmTv_service(),
                vodMetaFullData.getmService_id(),
                vodMetaFullData.getmEvent_id(),
                vodMetaFullData.getCrid(),
                vodMetaFullData.getTitle_id());
    }

    /**
     * クリップキーリストと番組情報を比較してクリップ状態を設定する.
     * @param scheduleInfo 番組表情報
     * @param mapList クリップ状態
     */
    public static boolean setClipStatusScheduleInfo(final ScheduleInfo scheduleInfo, final List<Map<String, String>> mapList) {
        return scheduleInfo != null && setClipStatusFromMap(mapList,
                scheduleInfo.getDispType(),
                scheduleInfo.getDtv(),
                scheduleInfo.getTvService(),
                scheduleInfo.getServiceId(),
                scheduleInfo.getEventId(),
                scheduleInfo.getCrId(),
                scheduleInfo.getTitleId());
    }

    /**
     * クリップキーリストとコンテンツ情報を比較してクリップ状態を設定する.
     * @param contentsData コンテンツ情報
     * @param mapList クリップ状態
     */
    public static boolean setClipStatusContentsData(final ContentsData contentsData, final List<Map<String, String>> mapList) {
        return contentsData != null && setClipStatusFromMap(mapList,
                contentsData.getDispType(),
                contentsData.getDtv(),
                contentsData.getTvService(),
                contentsData.getServiceId(),
                contentsData.getEventId(),
                contentsData.getCrid(),
                contentsData.getTitleId());
    }

    /**
     * クリップキーリストと各種情報を比較してクリップ状態を設定する.
     *
     * @param mapList クリップキーリスト
     * @param dispType dispType
     * @param dtv dtvフラグ
     * @param tvService tvService
     * @param serviceId serviceId
     * @param eventId eventId
     * @param crId crId
     * @param titleId titleId
     * @return クリップ状態
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static boolean setClipStatusFromMap(final List<Map<String, String>> mapList,
                                               final String dispType,
                                               final String dtv,
                                               final String tvService,
                                               final String serviceId,
                                               final String eventId,
                                               final String crId,
                                               final String titleId) {

        //クリップキーリストがない場合はfalseを返却
        if (mapList == null || mapList.size() < 1) {
            return false;
        }

        ClipKeyListDao.ContentTypeEnum contentType
                = ClipKeyListDataProvider.searchContentsType(dispType, dtv, tvService);
        if (contentType != null) {
            DTVTLogger.debug("setClipStatusFromMap start contentType != null");
            switch (contentType) {
                case SERVICE_ID_AND_EVENT_ID_REFERENCE:
                    //多チャンネル放送の場合 service_id、event_id
                    for (int k = 0; k < mapList.size(); k++) {
                        String mapServiceId = mapList.get(k).get(JsonConstants.META_RESPONSE_SERVICE_ID);
                        String mapEventId = mapList.get(k).get(JsonConstants.META_RESPONSE_EVENT_ID);
                        if (mapServiceId != null && mapServiceId.equals(serviceId)
                                && mapEventId != null && mapEventId.equals(eventId)) {
                            return true;
                        }
                    }
                    break;
                case CRID_REFERENCE:
                    //ひかりＴＶビデオ、dTVチャンネル、地デジ、BSの場合　crid
                    for (int k = 0; k < mapList.size(); k++) {
                        String mapCrId = mapList.get(k).get(JsonConstants.META_RESPONSE_CRID);
                        if (mapCrId != null && mapCrId.equals(crId)) {
                            return true;
                        }
                    }
                    break;
                case TITLE_ID_REFERENCE:
                    //dTVの場合 title_id
                    for (int k = 0; k < mapList.size(); k++) {
                        String mapTitleId = mapList.get(k).get(JsonConstants.META_RESPONSE_TITLE_ID);
                        if (mapTitleId != null && mapTitleId.equals(titleId)) {
                            return true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
