/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sasaki yoshiaki on 2017/10/27.
 */
public class VodMetaFullDataTest {
    private VodMetaFullData mVodMetaFullData;
    private String mjson_data = "{\n" +
            "      \"crid\": \"682017101601\",\n" +
            "      \"cid\": \"68vod2017101601\",\n" +
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
            "      \"qrange\": \"qrange\",\n" +
            "      \"qunit\": \"\",\n" +
            "      \"pu_s\": \"\",\n" +
            "      \"pu_e\": \"\",\n" +
            "      \"credits\": \"\",\n" +
            "      \"rating\": \"4.5\",\n" +
            "      \"dtv\": \"dtv0\",\n" +
            "      \"PLIT\": [\n" +
            "        {\n" +
            "          \"pli_vis\": \"pli_vis1\",\n" +
            "          \"pli_vie\": \"pli_vie2\",\n" +
            "          \"plicense\": [\n" +
            "            {\n" +
            "              \"pli_puid\": \"pli_puid1\",\n" +
            "              \"pli_crid\": \"pli_crid1\",\n" +
            "              \"pli_title\": \"pli_title1\",\n" +
            "              \"pli_epititle\": \"pli_epititle1\",\n" +
            "              \"pli_disp_type\": \"pli_disp_type1\",\n" +
            "              \"pli_price\": \"pli_price1\",\n" +
            "              \"pli_qunit\": \"pli_qunit1\",\n" +
            "              \"pli_qrange\": \"pli_qrange1\",\n" +
            "              \"pli_pu_s\": \"pli_pu_s1\",\n" +
            "              \"pli_pu_e\": \"pli_pu_e1\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pli_puid\": \"pli_puid2\",\n" +
            "              \"pli_crid\": \"pli_crid2\",\n" +
            "              \"pli_title\": \"pli_title2\",\n" +
            "              \"pli_epititle\": \"pli_epititle2\",\n" +
            "              \"pli_disp_type\": \"pli_disp_type2\",\n" +
            "              \"pli_price\": \"pli_price2\",\n" +
            "              \"pli_qunit\": \"pli_qunit2\",\n" +
            "              \"pli_qrange\": \"pli_qrange2\",\n" +
            "              \"pli_pu_s\": \"pli_pu_s2\",\n" +
            "              \"pli_pu_e\": \"pli_pu_e2\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "   }\n";
    private JSONObject json;
    @Before
    public void setUp() throws Exception {
        mVodMetaFullData = new VodMetaFullData();
        json = new JSONObject(mjson_data);
    }

    @Test
    public void setData() throws Exception {
        VodMetaFullData.Plit[] plits;
        VodMetaFullData.Plit.Plicense[] plicenses;
        mVodMetaFullData.setData(json);
        plits = mVodMetaFullData.getPlits();
        int plits_len = plits.length;
        plicenses = mVodMetaFullData.getPlits()[0].getPlicenses();
        int plicenses_len = plicenses.length;
        assertEquals(1, plicenses_len, 0);
    }

    @Test
    public void getPlits() throws Exception {
    }

}