/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClipUtilsTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void isCanClip() throws Exception {

    }

    @Test
    public void setClipStatusToDB() throws Exception {

        String dtvBlank = "";
        String dtv0 = "0";
        String dtv1 = "1";
        String searchOkBlank = "";
        String searchOk0 = "0";
        String searchOk1 = "1";
        String dtvTypeBlank = "";
        String dtvType1 = "1";
        String dtvType2 = "2";
        String dtvType3 = "3";
        //異常系
        assertFalse(ClipUtils.isCanClip(null, null, null, null));
        assertFalse(ClipUtils.isCanClip("", null, null, null));
        assertFalse(ClipUtils.isCanClip(null, "", null, null));
        assertFalse(ClipUtils.isCanClip(null, null, "", null));
        assertFalse(ClipUtils.isCanClip(null, null, null, ""));

        //tv_programの時
        // メタレスポンス「disp_type」が「tv_program」
        //「search_ok」が「0」、未設定
        // クリップ不可
        //「search_ok」が「1」
        // クリップ可
        //正常系True
        assertTrue(ClipUtils.isCanClip("tv_program", searchOk1, "", ""));
        //正常系False
        assertFalse(ClipUtils.isCanClip("tv_program", searchOk0, "", ""));
        assertFalse(ClipUtils.isCanClip("tv_program", searchOkBlank, "", ""));
        //異常系
        assertFalse(ClipUtils.isCanClip("tv_program", "", "", ""));
        assertFalse(ClipUtils.isCanClip("tv_program", "", "", ""));
        assertFalse(ClipUtils.isCanClip("tv_program", null, "", ""));
        assertFalse(ClipUtils.isCanClip("tv_program", "", null, ""));
        assertFalse(ClipUtils.isCanClip("tv_program", "", "", null));

        //tv_program以外の時
        // メタレスポンス「disp_type」が「video_program」、「video_series」、「video_package」、「subscription_package」、「series_svod」
        String[] dispType = {"video_program", "video_series", "video_package", "subscription_package", "series_svod"};
        for (String aDispType : dispType) {
            //「dtv」が「0」、未設定
            //「search_ok」が「0」、未設定
            // クリップ不可
            //「search_ok」が「1」
            // クリップ可
            //正常系True
            assertTrue(ClipUtils.isCanClip(aDispType, searchOk1, dtv0, ""));
            assertTrue(ClipUtils.isCanClip(aDispType, searchOk1, dtvBlank, ""));
            //正常系False
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk0, dtv0, ""));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk0, dtvBlank, ""));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOkBlank, dtvBlank, ""));

            //「dtv」が「1」
            //「search_ok」が「0」、未設定
            // クリップ不可
            //「search_ok」が「1」
            //「dtv_type」が「1」、「2」、「3」
            // クリップ不可
            //「dtv_type」が未設定
            // クリップ可
            //正常系True
            assertTrue(ClipUtils.isCanClip(aDispType, searchOk1, dtv1, dtvTypeBlank));
            //正常系False
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk0, dtv1, dtvTypeBlank));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOkBlank, dtv1, dtvTypeBlank));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk1, dtv1, dtvType1));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk1, dtv1, dtvType2));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk1, dtv1, dtvType3));

            //異常系False
            assertFalse(ClipUtils.isCanClip(aDispType, "", dtv1, ""));
            assertFalse(ClipUtils.isCanClip(aDispType, null, dtv1, ""));
            assertFalse(ClipUtils.isCanClip(aDispType, "", dtv1, null));
            assertFalse(ClipUtils.isCanClip(aDispType, "", dtv0, ""));
            assertFalse(ClipUtils.isCanClip(aDispType, null, dtv0, ""));
            assertFalse(ClipUtils.isCanClip(aDispType, "", dtv0, null));
            assertFalse(ClipUtils.isCanClip(aDispType, searchOk1, dtv1, null));
            assertFalse(ClipUtils.isCanClip(aDispType, null, dtv1, "3"));
        }
    }

}