/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * データ転換用クラス.
 */
public class DataConverter {

    /**
     * コンテンツ取得失敗時のパラメータ名.
     */
    public static final String FAILED_CONTENT_DATA = "failed_content_data";
    /**
     * コンテンツ0件取得時のパラメータ名.
     */
    public static final String NO_CONTENT_DATA = "no_content_data";

    /**
     * 取得したマップでContentsDataを生成する.
     *
     * @param srcMap コンテンツデータMap
     * @return ContentsData
     */
    public static ContentsData generateContentData(final Map<String, String> srcMap) {
        ContentsData contentInfo = new ContentsData();

        contentInfo.setTime(srcMap.get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
        contentInfo.setTitle(srcMap.get(JsonConstants.META_RESPONSE_TITLE));
        contentInfo.setThumURL(srcMap.get(JsonConstants.META_RESPONSE_THUMB_448));
        contentInfo.setThumDetailURL(srcMap.get(JsonConstants.META_RESPONSE_THUMB_640));
        contentInfo.setPublishStartDate(srcMap.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE));
        contentInfo.setPublishEndDate(srcMap.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE));
        contentInfo.setServiceId(srcMap.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        contentInfo.setTvService(srcMap.get(JsonConstants.META_RESPONSE_TV_SERVICE));
        contentInfo.setChannelNo(srcMap.get(JsonConstants.META_RESPONSE_CHNO));
        contentInfo.setRValue(srcMap.get(JsonConstants.META_RESPONSE_R_VALUE));
        contentInfo.setDispType(srcMap.get(JsonConstants.META_RESPONSE_DISP_TYPE));
        contentInfo.setContentsType(srcMap.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
        contentInfo.setContentsId(srcMap.get(JsonConstants.META_RESPONSE_CRID));
        contentInfo.setCrid(srcMap.get(JsonConstants.META_RESPONSE_CRID));
        contentInfo.setRatStar(srcMap.get(JsonConstants.META_RESPONSE_RATING));
        contentInfo.setSynop(srcMap.get(JsonConstants.META_RESPONSE_SYNOP));
        contentInfo.setEventId(srcMap.get(JsonConstants.META_RESPONSE_EVENT_ID));
        contentInfo.setAvailStartDate(DateUtils.getSecondEpochTime(srcMap.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
        contentInfo.setAvailEndDate(DateUtils.getSecondEpochTime(srcMap.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
        contentInfo.setVodStartDate(DateUtils.getSecondEpochTime(srcMap.get(JsonConstants.META_RESPONSE_VOD_START_DATE)));
        contentInfo.setVodEndDate(DateUtils.getSecondEpochTime(srcMap.get(JsonConstants.META_RESPONSE_VOD_END_DATE)));

        return contentInfo;
    }

    /**
     * コンテンツ詳細に必要なデータを返す.
     *
     * @param info         レコメンド情報
     * @param recommendFlg Bundleキー
     * @return コンテンツ情報
     */
    public static OtherContentsDetailData getContentDataToContentsDetail(final ContentsData info, final String recommendFlg) {
        OtherContentsDetailData detailData = new OtherContentsDetailData();
        detailData.setContentsId(info.getContentsId());
        if (DataBaseUtils.isNumber(info.getServiceId())) {
            int serviceId = Integer.parseInt(info.getServiceId());
            detailData.setServiceId(serviceId);
        }
        detailData.setRecommendFlg(recommendFlg);
        detailData.setCategoryId(info.getCategoryId());
        detailData.setRecommendOrder(info.getRecommendOrder());
        detailData.setPageId(info.getPageId());
        detailData.setGroupId(info.getGroupId());
        detailData.setRecommendMethodId(info.getRecommendMethodId());
        return detailData;
    }

    /**
     * チャンネルリストからマッピングデータを抽出する.
     * ※マイ番組一覧に、番組情報が存在しないため、通常の番組一覧から番組表用のマイ番組一覧を作成する.
     *
     * @param myChannelDataList マイチャンネル一覧
     * @param hikariChannels チャンネル一覧
     * @return マイ番組表に表示するチャンネル情報
     */
    public static ArrayList<ChannelInfo> executeMapping(final ArrayList<MyChannelMetaData> myChannelDataList, final ArrayList<ChannelInfo> hikariChannels) {
        ArrayList<ChannelInfo> myChannels = new ArrayList<>();
        if (myChannelDataList != null) {
            for (int i = 0; i < myChannelDataList.size(); i++) {
                for (int j = 0; j < hikariChannels.size(); j++) {
                    //サービスIDでマッピング
                    if (myChannelDataList.get(i).getServiceId().equals(hikariChannels.get(j).getServiceId())) {
                        //マイ番組表を作成する際に、マイ番組表のタグを追加する
                        hikariChannels.get(j).setService(ProgramDataManager.CH_SERVICE_MY_CHANNEL);
                        myChannels.add(hikariChannels.get(j));
                    }
                }
            }
        }
        return myChannels;
    }

    /**
     * hashMap情報からScheduleInfo情報を組み立てる.
     *
     * @param map マップ
     * @param clipKeyList クリップキーリスト
     * @return ScheduleInfo情報
     */
    @SuppressWarnings("OverlyLongMethod")
    public static ScheduleInfo convertScheduleInfo(final Map<String, String> map, final List<Map<String, String>> clipKeyList) {
        ScheduleInfo mSchedule = new ScheduleInfo();
        String startDate = map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE);
        String endDate = map.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE);
        String thumb = map.get(JsonConstants.META_RESPONSE_THUMB_448);
        String thumbDetail = map.get(JsonConstants.META_RESPONSE_THUMB_640);
        String title = map.get(JsonConstants.META_RESPONSE_TITLE);
        String detail = map.get(JsonConstants.META_RESPONSE_SYNOP);
        String chNo = map.get(JsonConstants.META_RESPONSE_CHNO);
        String rValue = map.get(JsonConstants.META_RESPONSE_R_VALUE);
        String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
        String contentType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
        String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
        String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
        String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);
        String eventId = map.get(JsonConstants.META_RESPONSE_EVENT_ID);
        String serviceId = map.get(JsonConstants.META_RESPONSE_SERVICE_ID);
        String titleId = map.get(JsonConstants.META_RESPONSE_TITLE_ID);
        String tvService = map.get(JsonConstants.META_RESPONSE_TV_SERVICE);
        String serviceIdUniq = map.get(JsonConstants.META_RESPONSE_SERVICE_ID_UNIQ);
        mSchedule.setStartTime(startDate);
        mSchedule.setEndTime(endDate);
        mSchedule.setImageUrl(thumb);
        mSchedule.setImageDetailUrl(thumbDetail);
        mSchedule.setTitle(title);
        mSchedule.setDetail(detail);
        mSchedule.setChNo(chNo);
        mSchedule.setRValue(rValue);
        mSchedule.setContentType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
        mSchedule.setDtv(dtv);
        mSchedule.setDispType(dispType);
        mSchedule.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
        mSchedule.setTvService(map.get(JsonConstants.META_RESPONSE_TV_SERVICE));
        mSchedule.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        mSchedule.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
        mSchedule.setCrId(map.get(JsonConstants.META_RESPONSE_CRID));
        mSchedule.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
        mSchedule.setClipRequestData(ScaledDownProgramListDataProvider.setClipData(map));
        mSchedule.setEventId(eventId);
        mSchedule.setServiceId(serviceId);
        mSchedule.setServiceIdUniq(serviceIdUniq);
        mSchedule.setContentType(contentType);
        mSchedule.setTitleId(titleId);
        mSchedule.setTvService(tvService);
        mSchedule.setClipStatus(ClipUtils.setClipStatusScheduleInfo(mSchedule, clipKeyList));
        return mSchedule;
    }

    /**
     * コンテンツがないときのダミーデータを作成する().
     *
     * @param context コンテキスト
     * @param serviceIdUniq サービスユニーク
     * @param isNullResponse nullレスポンスフラグ
     * @return ダミーデータ
     */
    public static Map<String, String> getDummyContentMap(final Context context, final String serviceIdUniq, final boolean isNullResponse) {
        Map<String, String> map = new HashMap<>();
        map.put(JsonConstants.META_RESPONSE_CRID, "");
        map.put(JsonConstants.META_RESPONSE_CID, "");
        if (isNullResponse) {
            map.put(JsonConstants.META_RESPONSE_TITLE, context.getString(R.string.common_failed_data_message));
            map.put(JsonConstants.META_RESPONSE_DISP_TYPE, FAILED_CONTENT_DATA);
        } else {
            map.put(JsonConstants.META_RESPONSE_TITLE, context.getString(R.string.common_empty_data_message));
            map.put(JsonConstants.META_RESPONSE_DISP_TYPE, NO_CONTENT_DATA);
        }
        map.put(JsonConstants.META_RESPONSE_PUBLISH_START_DATE, String.valueOf(DateUtils.getTodayDesignTimeFormatEpoch(4)));
        map.put(JsonConstants.META_RESPONSE_PUBLISH_END_DATE, String.valueOf(DateUtils.getTodayDesignTimeFormatEpoch(5)));
        map.put(JsonConstants.META_RESPONSE_DUR, "");
        map.put(JsonConstants.META_RESPONSE_4KFLG, "");
        map.put(JsonConstants.META_RESPONSE_R_VALUE, "G");
        map.put(JsonConstants.META_RESPONSE_ADULT, "");
        map.put(JsonConstants.META_RESPONSE_RATING, "");
        map.put(JsonConstants.META_RESPONSE_SERVICE_ID, "");
        map.put(JsonConstants.META_RESPONSE_EVENT_ID, "");
        map.put(JsonConstants.META_RESPONSE_CHNO, "");
        map.put(JsonConstants.META_RESPONSE_SERVICE_ID_UNIQ, serviceIdUniq);
        return map;
    }
}