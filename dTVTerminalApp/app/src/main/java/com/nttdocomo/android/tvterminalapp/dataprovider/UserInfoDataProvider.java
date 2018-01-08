/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.UserInfoInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.UserInfoDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import java.util.List;
import java.util.Map;

public class UserInfoDataProvider implements UserInfoWebClient.UserInfoJsonParserCallback {

    private Context mContext = null;

    //データを返すコールバック
    private UserDataProviderCallback mUserDataProviderCallback;

    private UserInfoInsertDataManager mDataManager = null;

    List<UserInfoList> mUserInfoLists;

    @Override
    public void onUserInfoJsonParsed(List<UserInfoList> userInfoLists) {
        if (userInfoLists != null && mDataManager != null) {
            //取得したデータが返ってきたので、DBに格納する
            mDataManager.execute(userInfoLists);

            //今の日時を取得日時とする
            SharedPreferencesUtils.setSharedPreferencesUserInfoDate(mContext,
                    System.currentTimeMillis());

            //後処理を行う
            afterProcess(userInfoLists);
        }
        //WEBAPIを取得できなかった場合は、既存のデータをそのまま使用するので、何もしない
    }

    /**
     * Ranking Top画面用データを返却するためのコールバック
     */
    public interface UserDataProviderCallback {

        /**
         * ユーザー情報一覧用コールバック
         *
         * @param list コンテンツリスト
         */
        void userInfoListCallback(boolean isDataChange, List<UserInfoList> list);
    }

    /**
     * コンストラクタ(getUserAge()用)
     *
     * @param mContext コンテキストファイル
     */
    public UserInfoDataProvider(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public UserInfoDataProvider(Context context, UserDataProviderCallback userDataProviderCallback) {
        mContext = context;
        //mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
        //mSetDB = false;

        mDataManager = new UserInfoInsertDataManager(mContext);

        //コールバックの定義
        mUserDataProviderCallback = userDataProviderCallback;
    }

    /**
     * ユーザーデータ取得を開始する
     */
    public void getUserInfo() {
        //通信状況の取得
        if (!isOnline(mContext)) {
            //通信不能なので、ヌルを指定して、前回の値をそのまま使用する。
            afterProcess(null);
            return;
        }

        //通信日時の確認
        if (isUserInfoTimeOut()) {
            //まだ取得後1時間が経過していないので、ヌルを指定して前回の値をそのまま使用する。
            afterProcess(null);
            return;
        }

        //新たなデータを取得する
        UserInfoWebClient userInfoWebClient = new UserInfoWebClient();
        userInfoWebClient.getUserInfoApi(mContext, this);
    }

    /**
     * 通信可能確認
     *
     * @param context コンテキスト
     * @return 通信可能ならばtrue
     */
    private static boolean isOnline(Context context) {
        //システムのネットワーク情報を取得する
        ConnectivityManager connectManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectManager != null) {
            networkInfo = connectManager.getActiveNetworkInfo();
        }

        //通信手段が無い場合は、networkInfoがヌルになるらしい。
        //手段があっても接続されていないときは、isConnected()がfalseになる
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * データ取得が必要かどうかを見る
     *
     * @return データが古いので、取得が必要ならばtrue
     */
    private boolean isUserInfoTimeOut() {
        //最終取得日時の取得
        long lastTime = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);

        //現在日時+1時間が最終取得日時以下ならば、まだデータは新しい
        if (((DateUtils.EPOCH_TIME_ONE_HOUR * 1000) + System.currentTimeMillis()) < lastTime) {
            return false;
        }

        //データは古くなった
        return true;
    }

    /**
     * データの取得やデータの取得の必要のない場合の後処理
     */
    private void afterProcess(List<UserInfoList> userInfoLists) {
        if (userInfoLists == null) {
            //データ指定がヌルなので、前回の値を指定する。更新フラグは自動的にfalseになる
            mDataManager.readUserInfoInsertList();
            mUserDataProviderCallback.userInfoListCallback(false,
                    mDataManager.getmUserData());
            return;
        }

        //データを読み込んだ場合
        mUserDataProviderCallback.userInfoListCallback(true, userInfoLists);

    }

    /**
     * 取得済みのユーザー年齢情報を取得する
     */
    public int getUserAge() {

        //ユーザ情報をDBから取得する
        int userAgeReq = StringUtil.DEFAULT_USER_AGE_REQ;
        UserInfoDataManager userInfoDataManager = new UserInfoDataManager(mContext);
        List<Map<String, String>> list = userInfoDataManager.selectUserAgeInfo();

        //取得したユーザ情報から、年齢情報を抽出する
        if (list != null && list.size() > 0) {
            userAgeReq = StringUtil.getUserAgeInfo(list);
        }

        return userAgeReq;
    }
}