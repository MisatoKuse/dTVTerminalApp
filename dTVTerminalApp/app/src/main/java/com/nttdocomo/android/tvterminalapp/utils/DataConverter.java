/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;


import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
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

        //レコメンド独自のデータ設定
        if (ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY.equals(recommendFlg)) {
            int servieId = Integer.parseInt(info.getServiceId());
            detailData.setServiceId(servieId);
            detailData.setContentCategory(ContentUtils.getRecommendContentsType(info));
            detailData.setThumb(info.getThumURL());
            if (servieId != ContentDetailActivity.DTV_HIKARI_CONTENTS_SERVICE_ID) {
                detailData.setDetail(info.getSynopFromDescription());
            }

        } else {
            detailData.setThumb(info.getThumDetailURL());
            detailData.setContentCategory(ContentUtils.ContentsType.OTHER);
            detailData.setDetail(info.getSynop());
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
}