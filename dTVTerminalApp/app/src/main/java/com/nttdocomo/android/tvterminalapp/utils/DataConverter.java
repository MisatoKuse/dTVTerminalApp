/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;


import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataConverter {

    /**
     * 取得したマップでContentsDataを生成する
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

}
