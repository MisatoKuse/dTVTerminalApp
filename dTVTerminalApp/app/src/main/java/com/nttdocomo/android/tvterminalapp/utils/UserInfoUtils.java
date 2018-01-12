/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.UserInfoInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;

import java.util.List;

public class UserInfoUtils {

    /**
     * レコメンドサーバ用に、ユーザ年齢情報を＋3して返却する
     *
     * @param userAge
     * @return
     */
    public static String getRecommendUserAge(int userAge) {
        final int RECOMMEND_AGE_MARGIN = 3;
        if (userAge >= 0) {
            userAge = userAge + RECOMMEND_AGE_MARGIN;
            return String.valueOf(userAge);
        } else {
            return null;
        }
    }

    /**
     * ログイン状態を返却する.
     *
     * @param context
     * @return
     */
    public static boolean getUserLoggedinInfo(Context context) {
        final int INT_LIST_HEAD = 0;

        UserInfoInsertDataManager dataManager = new UserInfoInsertDataManager(context);
        if (dataManager == null) {
            return false;
        }
        dataManager.readUserInfoInsertList();
        List<UserInfoList> userInfoList = dataManager.getmUserData();

        //ユーザ情報がないときは契約情報は無し
        if (userInfoList == null || userInfoList.size() < 1) {
            return false;
        }

        UserInfoList infoList = userInfoList.get(INT_LIST_HEAD);

        //ユーザ情報がないときは契約情報は無し
        if (infoList == null) {
            return false;
        }

        List<UserInfoList.AccountList> mLoggedInAccountList = infoList.getLoggedinAccount();
        UserInfoList.AccountList mLoggedInAccount = mLoggedInAccountList.get(INT_LIST_HEAD);

        //ユーザ情報がないときは契約情報は無し
        if (mLoggedInAccount == null) {
            return false;
        }

        return true;
    }
}
