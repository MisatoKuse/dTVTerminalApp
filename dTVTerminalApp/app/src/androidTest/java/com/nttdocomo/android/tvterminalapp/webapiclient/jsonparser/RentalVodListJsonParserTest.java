/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.util.Log;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sasaki yoshiaki on 2017/10/31.
 */
public class RentalVodListJsonParserTest
        implements RentalVodListWebClient.RentalVodListJsonParserCallback {

    private String mJson_data = "{\n" +
            "  \"status\": \"OK\",\n" +
            "  \"list\": [\n" +
            "    {\n" +
            "      \"crid\": \"1192017103101\",\n" +
            "      \"cid\": \"119vod2017103101\",\n" +
            "      \"title_id\": \"title_id30811\",\n" +
            "      \"episode_id\": \"episode_id30811\",\n" +
            "      \"title\": \"美女と野獣（吹替版）\",\n" +
            "      \"epititle\": \"\",\n" +
            "      \"titleruby\": \"\",\n" +
            "      \"disp_type\": \"video_program\",\n" +
            "      \"display_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"display_end_date\": \"2017-10-16T05:50:00+09:00\",\n" +
            "      \"avail_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"avail_end_date\": \"2017-10-16T05:50:00+09:00\",\n" +
            "      \"publish_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"publish_end_date\": \"2017-10-16T05:50:00+09:00\",\n" +
            "      \"newa_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"newa_end_date\": \"2017-10-16T05:50:00+09:00\",\n" +
            "      \"copyright\": \"(C) 2017 Disney\",\n" +
            "      \"thumb\": \"https://img.hikaritv.net/thumbnail/VOD640/a033d4c.jpg\",\n" +
            "      \"dur\": \"7800\",\n" +
            "      \"demong\": \"\",\n" +
            "      \"bvflg\": \"\",\n" +
            "      \"4kflg\": \"\",\n" +
            "      \"hdrflg\": \"\",\n" +
            "      \"avail_status\": \"avail_status\",\n" +
            "      \"delivery\": \"1.2\",\n" +
            "      \"r_value\": \"r_value\",\n" +
            "      \"adult\": \"\",\n" +
            "      \"ms\": \"\",\n" +
            "      \"ng_func\": \"\",\n" +
            "      \"genre_id_array\": [\n" +
            "        \"0600\"\n" +
            "      ],\n" +
            "      \"synop\": \"【吹替版】魔女の呪いによって野獣の姿に変えられてしまった王子。呪いを解く鍵は、魔法のバラの花びらが全て散る前に誰かを心から愛し、そして愛されること―。だが野獣の姿になった彼を愛するものなどいるはずがない。独り心を閉ざしていく中、心に孤独を抱えながらも、自分の輝きを信じて生きる、聡明で美しい女性、ベルと出会うが。。。\",\n" +
            "      \"puid\": \"\",\n" +
            "      \"price\": \"432\",\n" +
            "      \"qrange\": \"\",\n" +
            "      \"qunit\": \"\",\n" +
            "      \"pu_s\": \"\",\n" +
            "      \"pu_e\": \"\",\n" +
            "      \"credits\": \"\",\n" +
            "      \"rating\": \"4.5\",\n" +
            "      \"dtv\": \"\",\n" +
            "      \"PLIT\": [\n" +
            "        {\n" +
            "          \"pli_vis\": \"\",\n" +
            "          \"pli_vie\": \"\",\n" +
            "          \"plicense\": [\n" +
            "            {\n" +
            "              \"pli_puid\": \"\",\n" +
            "              \"pli_crid\": \"\",\n" +
            "              \"pli_title\": \"\",\n" +
            "              \"pli_epititle\": \"\",\n" +
            "              \"pli_disp_type\": \"\",\n" +
            "              \"pli_price\": \"\",\n" +
            "              \"pli_qunit\": \"\",\n" +
            "              \"pli_qrange\": \"\",\n" +
            "              \"pli_pu_s\": \"\",\n" +
            "              \"pli_pu_e\": \"\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"crid\": \"1192017103102\",\n" +
            "      \"cid\": \"119vod2017103102\",\n" +
            "      \"title_id\": \"title_id65602\",\n" +
            "      \"episode_id\": \"episode_id65602\",\n" +
            "      \"title\": \"それいけ！アンパンマン フランケンロボくんとハロウィンマン【アンパンマンチャンネル】\",\n" +
            "      \"epititle\": \"\",\n" +
            "      \"titleruby\": \"\",\n" +
            "      \"disp_type\": \"video_program\",\n" +
            "      \"display_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"display_end_date\": \"2018-08-31T23:59:59+09:00\",\n" +
            "      \"avail_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"avail_end_date\": \"2018-08-31T23:59:59+09:00\",\n" +
            "      \"publish_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"publish_end_date\": \"2018-08-31T23:59:59+09:00\",\n" +
            "      \"newa_start_date\": \"2017-10-16T05:00:00+09:00\",\n" +
            "      \"newa_end_date\": \"2018-08-31T23:59:59+09:00\",\n" +
            "      \"copyright\": \"（C）やなせたかし／フレーベル館・TMS・NTV \",\n" +
            "      \"thumb\": \"https://img.hikaritv.net/thumbnail/VOD640/a03523a.jpg\",\n" +
            "      \"dur\": \"600\",\n" +
            "      \"demong\": \"\",\n" +
            "      \"bvflg\": \"\",\n" +
            "      \"4kflg\": \"\",\n" +
            "      \"hdrflg\": \"\",\n" +
            "      \"avail_status\": \"avail_status\",\n" +
            "      \"delivery\": \"1.2\",\n" +
            "      \"r_value\": \"r_value\",\n" +
            "      \"adult\": \"\",\n" +
            "      \"ms\": \"\",\n" +
            "      \"ng_func\": \"\",\n" +
            "      \"genre_id_array\": [\n" +
            "        \"0409\",\n" +
            "        \"0702\"\n" +
            "      ],\n" +
            "      \"synop\": \"今日は楽しいハロウィンパーティー！みんな色んな恰好をして、お菓子をもらいに行くんだ。フランケンロボくんも、パパと一緒にハロウィンマンを探しに行くんだ。おいしいお菓子を食べて一緒にハロウィンを楽しむよ！\",\n" +
            "      \"puid\": \"\",\n" +
            "      \"price\": \"108\",\n" +
            "      \"qrange\": \"\",\n" +
            "      \"qunit\": \"\",\n" +
            "      \"pu_s\": \"\",\n" +
            "      \"pu_e\": \"\",\n" +
            "      \"credits\": \"戸田恵子/中尾隆聖\",\n" +
            "      \"rating\": \"3.7\",\n" +
            "      \"dtv\": \"\",\n" +
            "      \"PLIT\": [\n" +
            "        {\n" +
            "          \"pli_vis\": \"\",\n" +
            "          \"pli_vie\": \"\",\n" +
            "          \"plicense\": [\n" +
            "            {\n" +
            "              \"pli_puid\": \"\",\n" +
            "              \"pli_crid\": \"\",\n" +
            "              \"pli_title\": \"\",\n" +
            "              \"pli_epititle\": \"\",\n" +
            "              \"pli_disp_type\": \"\",\n" +
            "              \"pli_price\": \"\",\n" +
            "              \"pli_qunit\": \"\",\n" +
            "              \"pli_qrange\": \"\",\n" +
            "              \"pli_pu_s\": \"\",\n" +
            "              \"pli_pu_e\": \"\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    PurchasedVodListResponse mPurchasedVodListResponse;

    /**
     * テスト用ダミーコールバック(不使用)
     *
     * @param RentalVodListResponse JSONパース後のデータ
     */
    @Override
    public void onRentalVodListJsonParsed(PurchasedVodListResponse RentalVodListResponse) {
    }

    RentalVodListJsonParser mRentalVodListJsonParser;
    // RentalVodListJsonParser をテスト
    @Before
    public void setUp() throws Exception {
        mRentalVodListJsonParser = new RentalVodListJsonParser(this);
    }

    /**
     * test purchasedVodListSender
     *
     * @throws Exception
     */
    @Test
    public void purchasedVodListSender() throws Exception {
        // jsonStr = null の場合 null を返す
        mPurchasedVodListResponse = mRentalVodListJsonParser.PurchasedVodListSender(null);
        assertEquals("PurchasedVodListSender(null)", null, mPurchasedVodListResponse);
        // jsonStr != null の場合 PurchasedVodListResponse を返す
        mPurchasedVodListResponse = mRentalVodListJsonParser.PurchasedVodListSender(mJson_data);
        assertEquals("PurchasedVodListSender(mJson_data)", "OK", mPurchasedVodListResponse.getStatus());
    }

    /**
     * test sendStatus
     *
     * @throws Exception
     */
    @Test
    public void sendStatus() throws Exception {
        JSONObject jsonObj = new JSONObject(mJson_data);

        try {
            mRentalVodListJsonParser.sendStatus(jsonObj);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    /**
     * test sendPurchasedVodListResponse
     *
     * @throws Exception
     */
    @Test
    public void sendPurchasedVodListResponse() throws Exception {
        JSONObject jsonObj = new JSONObject(mJson_data);

        try {
            mRentalVodListJsonParser.sendStatus(jsonObj);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

}