/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserInfoWebClientTest {

    private UserInfoWebClient mUserInfoWebClient = null;
    private UserInfoWebClient.UserInfoJsonParserCallback mUserInfoJsonParserCallback = null;

    @Before
    public void setUp() throws Exception {
        //テスト実行前のSetUp
        mUserInfoWebClient = new UserInfoWebClient();

        mUserInfoJsonParserCallback = new UserInfoWebClient.UserInfoJsonParserCallback() {
            @Override
            public void getUserInfoResult(int ageReq) {

            }

            @Override
            public void getUserInfoFailure() {

            }
        };
    }

    @After
    public void tearDown() throws Exception {
        //Test実行後に何かしたいときはここ
    }

    @Test
    public void getUserInfoApi() throws Exception {
        //正常系
        assertEquals(mUserInfoWebClient.getUserInfoApi(mUserInfoJsonParserCallback), true);

        //異常系
        assertEquals(mUserInfoWebClient.getUserInfoApi(null), false);
    }
}