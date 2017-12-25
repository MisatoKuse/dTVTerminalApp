/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;

import java.util.HashMap;

public class ClipDataProvider {

    /**
     * クリップリクエストに必要なデータを作成する
     *
     * @param contentsData 各ListItem毎のデータ
     * @return Clipリクエストに必要なデータ
     */
    public static ClipRequestData setClipData(ContentsData contentsData) {
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(contentsData.getCrid());
        requestData.setServiceId(contentsData.getServiceId());
        requestData.setEventId(contentsData.getEventId());
        requestData.setTitleId(contentsData.getTitleId());
        requestData.setTitle(contentsData.getTitle());
        requestData.setRValue(contentsData.getRValue());
        requestData.setLinearStartDate(contentsData.getLinearStartDate());
        requestData.setLinearEndDate(contentsData.getLinearEndDate());
        requestData.setSearchOk(contentsData.getSearchOk());
        requestData.setClipTarget(contentsData.getTitle()); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています
        requestData.setIsNotify(contentsData.getDispType(), contentsData.getContentsType(),
                contentsData.getLinearEndDate(), contentsData.getTvService(), contentsData.getDtv());
        return requestData;
    }

    /**
     * クリップリクエストに必要なデータを作成する(コンテンツ詳細用)
     *
     * @param metaFullData VODメタデータ
     * @return Clipリクエストに必要なデータ
     */
    public static ClipRequestData setClipData(VodMetaFullData metaFullData){
        //コンテンツ詳細は、メタデータを丸ごと持っているため、そのまま利用する
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(metaFullData.getCrid());
        requestData.setServiceId(metaFullData.getmService_id());
        requestData.setEventId(metaFullData.getmEvent_id());
        requestData.setTitleId(metaFullData.getTitle_id());
        requestData.setTitle(metaFullData.getTitle());
        requestData.setRValue(metaFullData.getR_value());
        requestData.setLinearStartDate(String.valueOf(metaFullData.getAvail_start_date()));
        requestData.setLinearEndDate(String.valueOf(metaFullData.getAvail_end_date()));
        requestData.setSearchOk(metaFullData.getmSearch_ok());
        requestData.setClipTarget(metaFullData.getTitle()); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています
        requestData.setIsNotify(metaFullData.getDisp_type(), metaFullData.getmContent_type(),
                String.valueOf(metaFullData.getAvail_end_date()), metaFullData.getmTv_service(), metaFullData.getDtv());
        return requestData;
    }

    /**
     * クリップリクエストに必要なデータを作成する(番組表用)
     *
     * @param hashMap 番組表データ
     * @return Clipリクエストに必要なデータ
     */
    public static ClipRequestData setClipData(HashMap<String, String> hashMap){
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(hashMap.get(JsonContents.META_RESPONSE_CRID));
        requestData.setServiceId(hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID));
        requestData.setEventId(hashMap.get(JsonContents.META_RESPONSE_EVENT_ID));
        requestData.setTitleId(hashMap.get(JsonContents.META_RESPONSE_TITLE_ID));
        requestData.setTitle(hashMap.get(JsonContents.META_RESPONSE_TITLE));
        requestData.setRValue(hashMap.get(JsonContents.META_RESPONSE_R_VALUE));
        requestData.setLinearStartDate(String.valueOf(hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE)));
        requestData.setLinearEndDate(String.valueOf(hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE)));
        requestData.setSearchOk(hashMap.get(JsonContents.META_RESPONSE_SEARCH_OK));
        requestData.setClipTarget(hashMap.get(JsonContents.META_RESPONSE_TITLE)); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています
        requestData.setIsNotify(hashMap.get(JsonContents.META_RESPONSE_DISP_TYPE),
                hashMap.get(JsonContents.META_RESPONSE_CONTENT_TYPE),
                String.valueOf(hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE)),
                hashMap.get(JsonContents.META_RESPONSE_TV_SERVICE), hashMap.get(JsonContents.META_RESPONSE_DTV));
        return requestData;
    }
    /**
     * ダミーレスポンス(TODO:テスト用)
     *
     * @return
     */
    public static ClipRequestData setClipData() {
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid("672017101601");
        requestData.setServiceId("672017101601");
        requestData.setEventId("14c2");
        requestData.setTitleId("");
        requestData.setTitle("【サイエンスアワー】新語・流行語大賞 「インスタ映え」などノミネート");
        requestData.setRValue("G");
        requestData.setLinearStartDate("1513071135");
        requestData.setLinearEndDate("1513306982");
        requestData.setSearchOk("0");
        requestData.setClipTarget("title"); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています
        requestData.setIsNotify("disp_type", "content_type",
                "display_end_date", "tv_service", "dtv");
        return requestData;
    }
}
