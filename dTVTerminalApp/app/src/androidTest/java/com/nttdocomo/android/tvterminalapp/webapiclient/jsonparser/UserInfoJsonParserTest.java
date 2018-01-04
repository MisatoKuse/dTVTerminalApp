/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class UserInfoJsonParserTest {

    private String mJsonStrH4dStatus = null;
    private String mJsonStrH4dNoneStatus = null;
    private String mJsonStrDchStatus = null;
    private String mJsonStrOtherStatus = null;
    private String mJsonStrNoneStatus = null;
    private String mJsonStrBlank = null;

    private final int DEFAULT_AGE_REQ = 8;
    private final String DCH_AGE_REQ = "dch_age_req";
    private final String H4D_AGE_REQ = "h4d_age_req";
    private final String CONTRACT_STATUS = "contract_status";
    private final String H4D_CONTRACT_STATUS = "001";
    private final String DCH_CONTRACT_STATUS = "002";
    private final String NONE_CONTRACT_STATUS = "";
    private final String LOGIN_ACCOUNT_DCH_AGE_REQ = "10";
    private final String LOGIN_ACCOUNT_H4D_AGE_REQ = "17";
    private final String LOGIN_ACCOUNT_H4D_NONE_AGE_REQ = "";
    private final String H4D_CONTRACTED_ACCOUNT_DCH_AGE_REQ = "12";
    private final String H4D_CONTRACTED_ACCOUNT_H4D_AGE_REQ = "19";
    private UserInfoJsonParser mUserInfoJsonParser = null;
    private UserInfoWebClient.UserInfoJsonParserCallback mUserInfoJsonParserCallback = null;
    private List<HashMap<String, String>> mMapList = null;

    @Before
    public void setUp() throws Exception {
        mUserInfoJsonParserCallback = new UserInfoWebClient.UserInfoJsonParserCallback() {
            @Override
            public void getUserInfoResult(int ageReq) {

            }

            @Override
            public void getUserInfoFailure() {

            }
        };

        mUserInfoJsonParser = new UserInfoJsonParser(mUserInfoJsonParserCallback);

        //Contract_Statusが001の場合
        mJsonStrH4dStatus = "{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"loggedin_account\": {\n" +
                "    \"contract_status\": \"" +
                H4D_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                LOGIN_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                LOGIN_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  },\n" +
                "  \"h4d_contracted_account\": {\n" +
                "    \"contract_status\": \"" +
                H4D_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  }\n" +
                "}";

        //Contract_Statusが001かつH4dAccountがない場合
        mJsonStrH4dNoneStatus = "{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"loggedin_account\": {\n" +
                "    \"contract_status\": \"" +
                H4D_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                LOGIN_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                LOGIN_ACCOUNT_H4D_NONE_AGE_REQ +
                "\"\n" +
                "  },\n" +
                "  \"h4d_contracted_account\": {\n" +
                "    \"contract_status\": \"" +
                H4D_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  }\n" +
                "}";

        //Contract_Statusが002の場合
        mJsonStrDchStatus = "{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"loggedin_account\": {\n" +
                "    \"contract_status\": \"" +
                DCH_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                LOGIN_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                LOGIN_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  },\n" +
                "  \"h4d_contracted_account\": {\n" +
                "    \"contract_status\": \"" +
                DCH_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  }\n" +
                "}";

        //Contract_Statusが空の場合
        mJsonStrNoneStatus = "{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"loggedin_account\": {\n" +
                "    \"contract_status\": \"" +
                NONE_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                LOGIN_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                LOGIN_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  },\n" +
                "  \"h4d_contracted_account\": {\n" +
                "    \"contract_status\": \"" +
                NONE_CONTRACT_STATUS +
                "\",\n" +
                "    \"dch_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"h4d_age_req\": \"" +
                H4D_CONTRACTED_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  }\n" +
                "}";

        //Jsonが空の場合
        mJsonStrBlank = "{}";


        //その他テスト用可変設定値(それぞれの値にblank,null等を設定して使用)
        final String OTHER_STATUS_KEY = "status";
        final String OTHER_STATUS = "OK";
        final String OTHER_CONTRACT_STATUS_KEY = "contract_status";
        final String OTHER_CONTRACT_STATUS = "001";
        final String OTHER_LOGGED_IN_ACCOUNT = "loggedin_account";
        final String OTHER_H4D_CONTRACTED_ACCOUNT = "h4d_contracted_account";
        final String OTHER_DCH_AGE_REQ = "dch_age_req";
        final String OTHER_H4D_AGE_REQ = "h4d_age_req";
        final String OTHER_LOGIN_ACCOUNT_DCH_AGE_REQ = "";
        final String OTHER_LOGIN_ACCOUNT_H4D_AGE_REQ = "";

        //その他テスト用
        mJsonStrOtherStatus = "{\n" +
                "  \"" +
                OTHER_STATUS_KEY +
                "\": \"" +
                OTHER_STATUS +
                "\",\n" +
                "  \"" +
                OTHER_LOGGED_IN_ACCOUNT +
                "\": {\n" +
                "    \"" +
                OTHER_CONTRACT_STATUS_KEY +
                "\": \"" +
                OTHER_CONTRACT_STATUS +
                "\",\n" +
                "    \"" +
                OTHER_DCH_AGE_REQ +
                "\": \"" +
                OTHER_LOGIN_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"" +
                OTHER_H4D_AGE_REQ +
                "\": \"" +
                OTHER_LOGIN_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  },\n" +
                "  \"" +
                OTHER_H4D_CONTRACTED_ACCOUNT +
                "\": {\n" +
                "    \"" +
                OTHER_CONTRACT_STATUS_KEY +
                "\": \"" +
                DCH_CONTRACT_STATUS +
                "\",\n" +
                "    \"" +
                OTHER_DCH_AGE_REQ +
                "\": \"" +
                H4D_CONTRACTED_ACCOUNT_DCH_AGE_REQ +
                "\",\n" +
                "    \"" +
                OTHER_H4D_AGE_REQ +
                "\": \"" +
                H4D_CONTRACTED_ACCOUNT_H4D_AGE_REQ +
                "\"\n" +
                "  }\n" +
                "}";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void userInfoListSender() throws Exception {
        //「contract_status」が「001」の場合
        //年齢情報の優先度はH4d＞dChとし、H4d側で値が取得できない場合はdChの値を使用すること。
        //「contract_status」が「002」の場合
        //dChの年齢値を使用すること。
        //「contract_status」が「none」/未ログインのユーザの場合
        //PG12制限(8が返却された場合と同等の状態を固定値で再現)を行う。

        //contract_statusが「001」の場合->期待値:loggedin_accountのh4d_age_reqの値
        assertEquals(mUserInfoJsonParser.userInfoListSender(mJsonStrH4dStatus), Integer.parseInt(LOGIN_ACCOUNT_H4D_AGE_REQ));

        //contract_statusが「001」かつH4d側で値が取得できない場合->期待値:loggedin_accountのdch_age_reqの値
        assertEquals(mUserInfoJsonParser.userInfoListSender(mJsonStrH4dNoneStatus), Integer.parseInt(LOGIN_ACCOUNT_DCH_AGE_REQ));

        //contract_statusが「002」の場合->期待値:loggedin_accountのdch_age_reqの値
        assertEquals(mUserInfoJsonParser.userInfoListSender(mJsonStrDchStatus), Integer.parseInt(LOGIN_ACCOUNT_DCH_AGE_REQ));

        //contract_statusが空の場合->期待値:8
        assertEquals(mUserInfoJsonParser.userInfoListSender(mJsonStrNoneStatus), DEFAULT_AGE_REQ);

        //Jsonデータに正しくない値が入っていた場合->期待値:8
        assertEquals(mUserInfoJsonParser.userInfoListSender(mJsonStrOtherStatus), DEFAULT_AGE_REQ);

        //JsonデータがNullの場合->期待値:8
        assertEquals(mUserInfoJsonParser.userInfoListSender(null), DEFAULT_AGE_REQ);

        //Jsonデータが空の場合->期待値:8
        assertEquals(mUserInfoJsonParser.userInfoListSender(mJsonStrBlank), DEFAULT_AGE_REQ);
    }
}