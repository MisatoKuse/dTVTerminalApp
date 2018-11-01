/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.AccountList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ユーザ情報関連のUtilクラス.
 */
public class UserInfoUtils {

    /**
     * 契約状態 none.
     */
    public static final String CONTRACT_INFO_NONE = "none";
    /**
     * 契約状態 DTV契約.
     */
    public static final String CONTRACT_INFO_DTV = "001";
    /**
     * 契約状態 ひかり契約.
     */
    public static final String CONTRACT_INFO_H4D = "002";

    /**
     * 契約情報を取得する.
     *
     * @param userInfoList ユーザーデータ
     * @return 契約しているかどうか
     */
    public static String getUserContractInfo(final List<UserInfoList> userInfoList) {
        final int INT_LIST_HEAD = 0;
        String contractStatus;

        //ユーザ情報がないときは契約情報は無し
        if (userInfoList == null || userInfoList.size() < 1) {
            return CONTRACT_INFO_NONE;
        }

        UserInfoList infoList = userInfoList.get(INT_LIST_HEAD);

        //ユーザ情報がないときは契約情報は無し
        if (infoList == null) {
            return CONTRACT_INFO_NONE;
        }

        List<AccountList> mLoggedInAccountList = infoList.getLoggedinAccount();

        //TODO :dアカログイン状態はdアカ情報で判断するようにする
        //ログインユーザ情報がないときは契約情報は無し
        if (mLoggedInAccountList == null || mLoggedInAccountList.size() < 1 || userInfoList.size() < 1) {
            return CONTRACT_INFO_NONE;
        }

        AccountList mLoggedInAccount = mLoggedInAccountList.get(INT_LIST_HEAD);

        //ユーザ情報がないときは契約情報は無し
        if (mLoggedInAccount == null) {
            return CONTRACT_INFO_NONE;
        }

        // 契約中であれば契約情報を返す
        contractStatus = mLoggedInAccount.getContractStatus();
        if (contractStatus.equals(CONTRACT_INFO_DTV) || contractStatus.equals(CONTRACT_INFO_H4D)) {
            return contractStatus;
        }

        return CONTRACT_INFO_NONE;
    }

    /**
     * UserInfoListのリストから年齢情報を取得する.
     *
     * @param userInfoLists ユーザー情報のリスト
     * @return 年齢情報
     */
    public static int getUserAgeInfoWrapper(final List<UserInfoList> userInfoLists) {
        //変換後のユーザー情報
        List<Map<String, String>> newUserInfoLists = new ArrayList<>();

        if (userInfoLists != null) {
            //ユーザー情報の数だけ回る
            for (UserInfoList userInfo : userInfoLists) {
                Map<String, String> dataBuffer1 = getAccountList(userInfo.getLoggedinAccount());
                Map<String, String> dataBuffer2 = getAccountList(userInfo.getH4dContractedAccount());

                newUserInfoLists.add(dataBuffer1);
                newUserInfoLists.add(dataBuffer2);
            }
        } else {
            //ユーザ情報がないときはPG12制限値を返却
            return StringUtils.DEFAULT_USER_AGE_REQ;
        }
        //年齢情報の取得
        return getUserAgeInfo(newUserInfoLists);
    }

    /**
     * 契約情報をマップで蓄積.
     *
     * @param accountBuffers 契約情報リスト
     * @return マップ化契約情報
     */
    private static Map<String, String> getAccountList(
            final List<AccountList> accountBuffers) {
        Map<String, String> buffer = new HashMap<>();

        //契約情報の数だけ回ってマップに変換する
        for (AccountList accountBuffer : accountBuffers) {
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ,
                    accountBuffer.getDchAgeReq());
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ,
                    accountBuffer.getH4dAgeReq());
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT, null);
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS,
                    accountBuffer.getContractStatus());
        }

        return buffer;
    }

    /**
     * ユーザ情報から年齢情報を取得する.
     *
     * @param userInfoList ユーザ情報
     * @return 年齢パレンタル情報
     */
    public static int getUserAgeInfo(final List<Map<String, String>> userInfoList) {
        final int INT_LIST_HEAD = 0;
        String age = null;

        //ユーザ情報がないときはPG12制限値を返却
        if (userInfoList == null || userInfoList.size() < 1) {
            return StringUtils.DEFAULT_USER_AGE_REQ;
        }

        Map<String, String> infoMap = userInfoList.get(INT_LIST_HEAD);

        //ユーザ情報がないときはPG12制限値を返却
        if (infoMap == null) {
            return StringUtils.DEFAULT_USER_AGE_REQ;
        }

        String contractStatus = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS);

        //contractStatusがないときはPG12制限値を設定
        int intAge = StringUtils.DEFAULT_USER_AGE_REQ;
        if (contractStatus != null) {
            if (contractStatus.equals(UserInfoDataProvider.CONTRACT_STATUS_DTV)) {
                //001 ならdch_age_reqを参照する
                age = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ);
            } else if (contractStatus.equals(UserInfoDataProvider.CONTRACT_STATUS_H4D)) {
                //002 ならh4d_age_reqを優先。ひかりが無いならdch_age_reqを参照する。
                age = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ);
                if (age == null || age.length() < 1) {
                    age = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ);
                }
            }
        }
        //年齢情報が数字ならINTに変換
        if (DataBaseUtils.isNumber(age)) {
            intAge = Integer.parseInt(age);
        }
        return intAge;
    }

    /**
     * クリップ非活性表示フラグ(未ログイン又は未契約)取得.
     *
     * @param context コンテキストファイル
     * @return クリップ活性(true)/非活性(false)
     */
    public static boolean getClipActive(final Context context) {
        return !(getUserState(context).equals(UserState.LOGIN_NG) || !isContract(context));
    }

    /**
     * ユーザ状態取得.
     *
     * @param context コンテキストファイル
     * @return ユーザ状態
     */
    public static UserState getUserState(final Context context) {
        UserState param;
        // dアカログイン状態取得
        String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(context);
        if (userId == null || userId.isEmpty()) {
            DTVTLogger.debug("HomeActivity::showHomeBanner::isEmpty");
            // dアカ未ログイン
            param = UserState.LOGIN_NG;
        } else {
            // dアカログイン状態なら契約状態判断.
            //保存されているUserInfoから契約情報を確認する
            String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(context));
            DTVTLogger.debug("contractInfo: " + contractInfo);
            //契約なし、またはDTVのみ契約の時は未契約扱い.
            if (contractInfo == null
                    || contractInfo.isEmpty()
                    || UserInfoUtils.CONTRACT_INFO_NONE.equals(contractInfo)
                    || UserInfoUtils.CONTRACT_INFO_DTV.equals(contractInfo)) {
                param = UserState.LOGIN_OK_CONTRACT_NG;
                // 契約済の場合はペアリング状態によって変わる
            } else {
                // ペアリング済みかどうか.
                if (SharedPreferencesUtils.getSharedPreferencesDecisionParingSettled(context)) {
                    //契約済みかつペアリング済み
                    param = UserState.CONTRACT_OK_PARING_OK;
                } else {
                    //契約済みかつ未ペアリング
                    param = UserState.CONTRACT_OK_PAIRING_NG;
                }
            }
        }
        return param;
    }

    /**
     * 契約有無を返却.
     *
     * @param context コンテキストファイル
     * @return 契約あり true || 契約なし false
     */
    public static boolean isContract(final Context context) {
        //契約ありの場合のみ「NOW ON AIR」を表示する ※番組表表示のためデータ取得の必要があるため
        UserState userState = getUserState(context);
        return userState != null && (userState.equals(UserState.CONTRACT_OK_PAIRING_NG) || userState.equals(UserState.CONTRACT_OK_PARING_OK));
    }
}