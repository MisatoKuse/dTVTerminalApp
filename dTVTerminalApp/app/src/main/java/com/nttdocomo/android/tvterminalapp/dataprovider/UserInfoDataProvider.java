/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.UserInfoInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import java.util.List;

public class UserInfoDataProvider implements UserInfoWebClient.UserInfoJsonParserCallback {

    private Context mContext = null;

    //データを返すコールバック
    private final UserDataProviderCallback mUserDataProviderCallback;

    //データーマネージャー
    private UserInfoInsertDataManager mDataManager = null;

    //前回の日時
    private long beforeDate = 0;

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
        } else {
            //取得ができなかったので、DBから取得する
            afterProcess(null);
        }
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
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public UserInfoDataProvider(Context context, UserDataProviderCallback userDataProviderCallback) {
        mContext = context;

        //データマネージャーの初期化
        mDataManager = new UserInfoInsertDataManager(mContext);

        //コールバックの定義
        mUserDataProviderCallback = userDataProviderCallback;
    }

    /**
     * ユーザーデータ取得を開始する
     */
    public void getUserInfo() {
        //今設定されている最終更新日時を控えておく
        beforeDate = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);

        //通信状況の取得
        if (!isOnline(mContext)) {
            //通信不能なので、ヌルを指定して、前回の値をそのまま使用する。
            afterProcess(null);
            return;
        }

        //通信日時の確認
        if (!isUserInfoTimeOut()) {
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

        //通信手段が無い場合は、networkInfoがヌルになる
        //手段があっても接続されていないときは、isConnected()がfalseになる
        //どちらの場合も通信は不可能なので、falseを返す
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * データの取得日時を見て、データ取得が必要かどうかを見る
     *
     * @return データが古いので、取得が必要ならばtrue
     */
    private boolean isUserInfoTimeOut() {
        //最終取得日時の取得
        long lastTime = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);

        //現在日時が最終取得日時+1時間以下ならば、まだデータは新しい
        if (System.currentTimeMillis() < lastTime + (DateUtils.EPOCH_TIME_ONE_HOUR * 1000)) {
            return false;
        }

        //データは古くなった
        return true;
    }

    /**
     * データの取得やデータの取得の必要のない場合の後処理
     *
     * @param userInfoLists 契約情報
     */
    private void afterProcess(List<UserInfoList> userInfoLists) {
        // trueならば、データ更新扱い許可とするフラグ
        boolean changeFlag = true;

        //初回実行時はホーム画面に飛ばさないために、許可フラグはfalseにする
        if (beforeDate == Long.MIN_VALUE) {
            changeFlag = false;
        }

        if (userInfoLists == null) {
            // データ指定がヌルなので、前回の値を指定する。
            // データマネージャーの取得に失敗していた場合は、以前の値を読めないので、リストはヌルのまま
            if (mDataManager != null) {
                // DBから契約情報を取得する
                mDataManager.readUserInfoInsertList();
                userInfoLists = mDataManager.getmUserData();
            }
        }

        //新旧の年齢データを比較する
        int beforeAge = SharedPreferencesUtils.getSharedPreferencesAgeReq(mContext);
        int newAge = StringUtil.getUserInfo(userInfoLists);
        boolean isChangeAge = false;
        if (beforeAge != newAge) {
            //新しい年齢情報を保存する
            SharedPreferencesUtils.setSharedPreferencesAgeReq(mContext, newAge);

            if (changeFlag) {
                //年齢情報が変化し初回実行でもないので、ホーム画面遷移フラグをONにする
                isChangeAge = true;
            }
        }

        //結果を返すコールバックを呼ぶ
        mUserDataProviderCallback.userInfoListCallback(isChangeAge, userInfoLists);
    }
}