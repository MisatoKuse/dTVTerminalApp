/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;

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
        requestData.setClipTarget(contentsData.getTitle()); //TODO:仕様確認中 現在はランキング画面ではトーストにタイトル名を表示することとしています
        requestData.setIsNotify(contentsData.getDispType(), contentsData.getContentsType(),
                contentsData.getLinearEndDate(), contentsData.getTvService(), contentsData.getDtv());
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
        requestData.setClipTarget("title"); //TODO:仕様確認中 現在はランキング画面ではトーストにタイトル名を表示することとしています
        requestData.setIsNotify("disp_type", "content_type",
                "display_end_date", "tv_service", "dtv");
        return requestData;
    }
}
