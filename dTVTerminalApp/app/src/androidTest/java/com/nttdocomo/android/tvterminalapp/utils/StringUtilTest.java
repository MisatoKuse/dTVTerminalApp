/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StringUtilTest {

    private Context mContext = null;
    private final String CON_001 = "001";
    private final String CON_002 = "002";
    private final String BLANK = "";
    private final String STR_A = "A";
    private final String NULL_VL = null;
    private final String DFT_008 = "8";
    private final String H4D_018 = "18";
    private final String DCH_015 = "15";

    @Before
    public void setUp() throws Exception {
        //コンテクストを取得
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() throws Exception {
        //None
    }

    @Test
    public void getUserInfo() throws Exception {
        //網羅Test
        // 年齢情報を「【dR】【共通系IF】インターフェース仕様書_20171128.doc」2.2ユーザ情報取得で得られる「loggedin_account」リスト内の「dch_age_req」、「h4d_age_req」をする。
        //「contract_status」が「001」の場合
        // 年齢情報の優先度はH4d＞dChとし、H4d側で値が取得できない場合はdChの値を使用すること。
        //「contract_status」が「002」の場合
        // dChの年齢値を使用すること。
        //「contract_status」が「none」/未ログインのユーザの場合
        // PG12制限(8が返却された場合と同等の状態を固定値で再現)を行う。
        String contractStatus[] = {CON_001, CON_001, CON_001, CON_001, CON_001, CON_002, CON_002, CON_002, CON_002, BLANK,    BLANK,   NULL_VL, STR_A};
        String dchAgeReq[] =      {DCH_015, DCH_015, STR_A,   BLANK,    NULL_VL, DCH_015, STR_A,   BLANK,   NULL_VL,  DCH_015, DCH_015, DCH_015, DCH_015};
        String h4dAgeReq[] =      {H4D_018, BLANK,   BLANK,    BLANK,   NULL_VL, H4D_018, BLANK,   BLANK,    NULL_VL, H4D_018, H4D_018, H4D_018, BLANK};
        String answer[] =         {H4D_018, DCH_015, DFT_008, DFT_008, DFT_008, DCH_015, DFT_008, DFT_008, DFT_008, DFT_008, DFT_008, DFT_008, DFT_008};
        for (int i = 0; i < contractStatus.length; i++) {
            UserInfoList.AccountList infoList = new UserInfoList.AccountList();
            infoList.setContractStatus(contractStatus[i]);
            infoList.setDchAgeReq(dchAgeReq[i]);
            infoList.setH4dAgeReq(h4dAgeReq[i]);
            List<UserInfoList.AccountList> mLoggedinAccount = new ArrayList<>();
            mLoggedinAccount.add(infoList);
            Map<String, String> userInfoMap = new HashMap<>();
            userInfoMap.put(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS, contractStatus[i]);
            userInfoMap.put(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ, h4dAgeReq[i]);
            userInfoMap.put(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ, dchAgeReq[i]);
            List<Map<String, String>> userInfoMapList = new ArrayList<>();
            userInfoMapList.add(userInfoMap);

            //その他
            assertEquals(UserInfoUtils.getUserAgeInfo(userInfoMapList), Integer.parseInt(answer[i]));
        }
    }

    @Test
    public void convertRValueToAgeReq() throws Exception {

        //null 期待値：0
        assertEquals(StringUtils.convertRValueToAgeReq(mContext, null), StringUtils.DEFAULT_R_VALUE);

        //blank 期待値：0
        assertEquals(StringUtils.convertRValueToAgeReq(mContext, ""), StringUtils.DEFAULT_R_VALUE);

        //文字 期待値：0
        assertEquals(StringUtils.convertRValueToAgeReq(mContext, "あ"), StringUtils.DEFAULT_R_VALUE);

        //PG12 期待値：9
        assertEquals(StringUtils.convertRValueToAgeReq(mContext,
                mContext.getString(R.string.parental_pg_12)), StringUtils.USER_AGE_REQ_PG12);

        //R15 期待値：12
        assertEquals(StringUtils.convertRValueToAgeReq(mContext,
                mContext.getString(R.string.parental_r_15)), StringUtils.USER_AGE_REQ_R15);

        //R18 期待値：15
        assertEquals(StringUtils.convertRValueToAgeReq(mContext,
                mContext.getString(R.string.parental_r_18)), StringUtils.USER_AGE_REQ_R18);

        //R20 期待値：17
        assertEquals(StringUtils.convertRValueToAgeReq(mContext,
                mContext.getString(R.string.parental_r_20)), StringUtils.USER_AGE_REQ_R20);
    }
}