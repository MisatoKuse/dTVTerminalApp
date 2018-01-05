/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.support.test.InstrumentationRegistry;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    private List<UserInfoList> mUserInfoH4dList = null;
    private List<UserInfoList> mUserInfoH4dToDchList = null;
    private List<UserInfoList> mUserInfoDchList = null;
    private List<UserInfoList> mUserInfoOtherList = null;
    private StringUtil mStringUtils = null;
    private final String CONTRACT_H4D = "001";
    private final String CONTRACT_DCH = "002";
    private final String DCH_AGE = "9";
    private final String H4D_AGE = "18";
    private final String DEFAULT_AGE = "8";

    @Before
    public void setUp() throws Exception {

        mStringUtils = new StringUtil(InstrumentationRegistry.getTargetContext());

        //ContractStatus = 001：h4d情報あり
        UserInfoList.AccountList infoList = new UserInfoList.AccountList();
        infoList.setmContractStatus(CONTRACT_H4D);
        infoList.setmDchAgeReq(DCH_AGE);
        infoList.setmH4dAgeReq(H4D_AGE);
        List<UserInfoList.AccountList> mLoggedinAccount = new ArrayList<>();
        mLoggedinAccount.add(infoList);
        UserInfoList userInfoList = new UserInfoList();
        userInfoList.setmLoggedinAccount(mLoggedinAccount);
        mUserInfoH4dList = new ArrayList<>();
        mUserInfoH4dList.add(userInfoList);

        //ContractStatus = 001：h4d情報なし
        infoList = new UserInfoList.AccountList();
        infoList.setmContractStatus(CONTRACT_H4D);
        infoList.setmDchAgeReq(DCH_AGE);
        infoList.setmH4dAgeReq("");
        mLoggedinAccount = new ArrayList<>();
        mLoggedinAccount.add(infoList);
        userInfoList = new UserInfoList();
        userInfoList.setmLoggedinAccount(mLoggedinAccount);
        mUserInfoH4dToDchList = new ArrayList<>();
        mUserInfoH4dToDchList.add(userInfoList);

        //ContractStatus = 002
        infoList = new UserInfoList.AccountList();
        infoList.setmContractStatus(CONTRACT_DCH);
        infoList.setmDchAgeReq(DCH_AGE);
        infoList.setmH4dAgeReq(H4D_AGE);
        mLoggedinAccount = new ArrayList<>();
        mLoggedinAccount.add(infoList);
        userInfoList = new UserInfoList();
        userInfoList.setmLoggedinAccount(mLoggedinAccount);
        mUserInfoDchList = new ArrayList<>();
        mUserInfoDchList.add(userInfoList);

        //その他 網羅用(値を変更して使用)
        String contractStatus = "001";
        String dchAgeReq = "";
        String h4dAgeReq = "";
        infoList = new UserInfoList.AccountList();
        infoList.setmContractStatus(contractStatus);
        infoList.setmDchAgeReq(dchAgeReq);
        infoList.setmH4dAgeReq(h4dAgeReq);
//        infoList = null; nullチェック
        mLoggedinAccount = new ArrayList<>();
        mLoggedinAccount.add(infoList);
        userInfoList = new UserInfoList();
        userInfoList.setmLoggedinAccount(mLoggedinAccount);
//        userInfoList = null; nullチェック
        mUserInfoOtherList = new ArrayList<>();
        mUserInfoOtherList.add(userInfoList);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getUserInfo() throws Exception {

        //ContractStatus = 001：h4d情報あり
        assertEquals(mStringUtils.getUserInfo(mUserInfoH4dList), Integer.parseInt(H4D_AGE));

        //ContractStatus = 001：h4d情報なし
        assertEquals(mStringUtils.getUserInfo(mUserInfoH4dToDchList), Integer.parseInt(DCH_AGE));

        //ContractStatus = 002
        assertEquals(mStringUtils.getUserInfo(mUserInfoDchList), Integer.parseInt(DCH_AGE));

        //その他
        assertEquals(mStringUtils.getUserInfo(mUserInfoOtherList), Integer.parseInt(DEFAULT_AGE));

        //null
        assertEquals(mStringUtils.getUserInfo(null), Integer.parseInt(DEFAULT_AGE));

    }

    @Test
    public void convertRValueToAgeReq() throws Exception {

        //null 期待値：9
        assertEquals(mStringUtils.convertRValueToAgeReq(null), StringUtil.USER_AGE_REQ_PG12);

        //blank 期待値：9
        assertEquals(mStringUtils.convertRValueToAgeReq(""), StringUtil.USER_AGE_REQ_PG12);

        //文字 期待値：9
        assertEquals(mStringUtils.convertRValueToAgeReq("あ"), StringUtil.USER_AGE_REQ_PG12);

        //PG12 期待値：9
        assertEquals(mStringUtils.convertRValueToAgeReq(StringUtil.USER_R_VALUE_PG12), StringUtil.USER_AGE_REQ_PG12);

        //R15 期待値：12
        assertEquals(mStringUtils.convertRValueToAgeReq(StringUtil.USER_R_VALUE_R15), StringUtil.USER_AGE_REQ_R15);

        //R18 期待値：15
        assertEquals(mStringUtils.convertRValueToAgeReq(StringUtil.USER_R_VALUE_R18), StringUtil.USER_AGE_REQ_R18);

        //R20 期待値：17
        assertEquals(mStringUtils.convertRValueToAgeReq(StringUtil.USER_R_VALUE_R20), StringUtil.USER_AGE_REQ_R20);
    }
}