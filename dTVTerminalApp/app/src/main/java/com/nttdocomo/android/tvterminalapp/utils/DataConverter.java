/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;


import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.Map;

/**
 * データ転換用クラス.
 */
public class DataConverter {

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
    public static OtherContentsDetailData getOtherContentsDetailData(final ContentsData info, final String recommendFlg) {
        OtherContentsDetailData detailData = new OtherContentsDetailData();
        detailData.setTitle(info.getTitle());
        detailData.setDetail(info.getSynop());

        //レコメンド独自のデータ設定
        if (ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY.equals(recommendFlg)) {
            detailData.setServiceId(Integer.parseInt(info.getServiceId()));
            detailData.setContentCategory(ContentUtils.getRecommendContentsType(info));
            detailData.setThumb(info.getThumURL());
        } else {
            detailData.setThumb(info.getThumDetailURL());
            detailData.setContentCategory(ContentUtils.ContentsType.OTHER);
        }

        detailData.setMobileViewingFlg(info.getMobileViewingFlg());
        detailData.setmStartDate(info.getStartViewing());
        detailData.setmEndDate(info.getEndViewing());
        detailData.setCategoryId(info.getCategoryId());
        detailData.setChannelId(info.getChannelId());
        detailData.setChannelName(info.getChannelName());
        detailData.setPageId(info.getPageId());
        detailData.setGroupId(info.getGroupId());
        detailData.setRecommendMethodId(info.getRecommendMethodId());
        detailData.setRecommendFlg(recommendFlg);
        detailData.setReserved1(info.getReserved1());
        detailData.setReserved2(info.getReserved2());
        detailData.setReserved3(info.getReserved3());
        detailData.setReserved4(info.getReserved4());
        detailData.setReserved5(info.getReserved5());
        detailData.setDescription1(info.getDescription1());
        detailData.setDescription2(info.getDescription2());
        detailData.setDescription3(info.getDescription3());

        //コンテンツIDの受け渡しを追加
        detailData.setContentsId(info.getContentsId());
        detailData.setRecommendOrder(info.getRecommendOrder());

        //ひかりサーバデータ
        detailData.setDispType(info.getDispType());
        detailData.setDtv(info.getDtv());
        detailData.setTvService(info.getTvService());
        detailData.setContentsType(info.getContentsType());

        return detailData;
    }
}
