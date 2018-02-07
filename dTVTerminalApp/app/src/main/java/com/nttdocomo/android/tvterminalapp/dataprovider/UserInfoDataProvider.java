/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.UserInfoInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.UserInfoDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import java.util.List;
import java.util.Map;

public class UserInfoDataProvider implements UserInfoWebClient.UserInfoJsonParserCallback {

    private Context mContext = null;

    //データを返すコールバック
    private UserDataProviderCallback mUserDataProviderCallback;

    //データーマネージャー
    private UserInfoInsertDataManager mDataManager = null;

    //前回の日時
    private long beforeDate = 0;

    public static final String CONTRACT_STATUS_NONE = "none";
    public static final String CONTRACT_STATUS_DTV = "001";
    public static final String CONTRACT_STATUS_H4D = "002";

    @Override
    public void onUserInfoJsonParsed(List<UserInfoList> userInfoLists) {
        DTVTLogger.start();

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

        DTVTLogger.end();
    }

    /**
     * Ranking Top画面用データを返却するためのコールバック.
     */
    public interface UserDataProviderCallback {

        /**
         * ユーザー情報一覧用コールバック.
         *
         * @param list コンテンツリスト
         */
        void userInfoListCallback(boolean isDataChange, List<UserInfoList> list);
    }

    /**
     * コンストラクタ(getUserAge()用).
     *
     * @param mContext コンテキストファイル
     */
    public UserInfoDataProvider(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public UserInfoDataProvider(Context context, UserDataProviderCallback userDataProviderCallback) {
        DTVTLogger.start();

        mContext = context;

        //データマネージャーの初期化
        mDataManager = new UserInfoInsertDataManager(mContext);

        //コールバックの定義
        mUserDataProviderCallback = userDataProviderCallback;

        DTVTLogger.end();
    }

    /**
     * ユーザーデータ取得を開始する.
     */
    public void getUserInfo() {
        DTVTLogger.start();

        //今設定されている最終更新日時を控えておく
        beforeDate = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);

        //通信状況の取得
        if (!isOnline(mContext)) {
            DTVTLogger.debug("OffLine");

            //通信不能なので、ヌルを指定して、前回の値をそのまま使用する。
            afterProcess(null);
            return;
        }

        //通信日時の確認
        if (!isUserInfoTimeOut()) {
            DTVTLogger.debug("Less than 1 hour");

            //まだ取得後1時間が経過していないので、ヌルを指定して前回の値をそのまま使用する。
            afterProcess(null);
            return;
        }

        //新たなデータを取得する
        UserInfoWebClient userInfoWebClient = new UserInfoWebClient(mContext);
        userInfoWebClient.getUserInfoApi(this);

        DTVTLogger.end();
    }

    /**
     * 通信可能確認.
     *
     * @param context コンテキスト
     * @return 通信可能ならばtrue
     */
    private static boolean isOnline(Context context) {
        DTVTLogger.start();

        //システムのネットワーク情報を取得する
        ConnectivityManager connectManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectManager != null) {
            networkInfo = connectManager.getActiveNetworkInfo();
        }

        DTVTLogger.end();

        //通信手段が無い場合は、networkInfoがヌルになる
        //手段があっても接続されていないときは、isConnected()がfalseになる
        //どちらの場合も通信は不可能なので、falseを返す
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * データの取得日時を見て、データ取得が必要かどうかを見る.
     *
     * @return データが古いので、取得が必要ならばtrue
     */
    private boolean isUserInfoTimeOut() {
        DTVTLogger.start();

        //最終取得日時の取得
        long lastTime = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);

        //現在日時が最終取得日時+1時間以下ならば、まだデータは新しい
        if (DateUtils.getNowTimeFormatEpoch() < lastTime + DateUtils.EPOCH_TIME_ONE_HOUR) {
            DTVTLogger.end("false");
            return false;
        }

        //データは古くなった
        DTVTLogger.end("true");
        return true;
    }

    /**
     * データの取得やデータの取得の必要のない場合の後処理.
     *
     * @param userInfoLists 契約情報
     */
    private void afterProcess(List<UserInfoList> userInfoLists) {
        DTVTLogger.start();
        DTVTLogger.debug("userInfoLists " + userInfoLists);

        // trueならば、データ更新扱い許可とするフラグ
        boolean changeFlag = true;

        //初回実行時はホーム画面に飛ばさないために、許可フラグはfalseにする
        if (beforeDate == Long.MIN_VALUE) {
            DTVTLogger.debug("first exec");
            changeFlag = false;
        }

        if (userInfoLists == null) {
            DTVTLogger.debug("no user data");
            // データ指定がヌルなので、前回の値を指定する。
            // データマネージャーの取得に失敗していた場合は、以前の値を読めないので、リストはヌルのまま
            if (mDataManager != null) {
                DTVTLogger.debug("no data manager");
                // DBから契約情報を取得する
                mDataManager.readUserInfoInsertList();
                userInfoLists = mDataManager.getmUserData();
            }
        } else if (userInfoLists.get(0).getLoggedinAccount().size() == 0) {
            //契約情報がゼロ件の場合、情報取得に失敗するので、userInfoListsをヌルにして、異常データであることを明示する
            userInfoLists = null;
        }

        //新旧の年齢データを比較する
        int beforeAge = SharedPreferencesUtils.getSharedPreferencesAgeReq(mContext);
        int newAge = StringUtils.getUserAgeInfoWrapper(userInfoLists);

        DTVTLogger.debug("before age " + beforeAge);
        DTVTLogger.debug("new age " + newAge);

        boolean isChangeAge = false;
        if (beforeAge != newAge) {
            DTVTLogger.debug("age data change");

            //新しい年齢情報を保存する
            SharedPreferencesUtils.setSharedPreferencesAgeReq(mContext, newAge);

            if (changeFlag) {
                DTVTLogger.debug("go home");
                //年齢情報が変化し初回実行でもないので、ホーム画面遷移フラグをONにする
                isChangeAge = true;
            }
        }

        //結果を返すコールバックを呼ぶ
        mUserDataProviderCallback.userInfoListCallback(isChangeAge, userInfoLists);

        DTVTLogger.end();
    }

    /**
     * 取得済みのユーザー年齢情報を取得する.
     */
    public int getUserAge() {

        //ユーザ情報をDBから取得する
        int userAgeReq = StringUtils.DEFAULT_USER_AGE_REQ;
        UserInfoDataManager userInfoDataManager = new UserInfoDataManager(mContext);
        List<Map<String, String>> list = userInfoDataManager.selectUserAgeInfo();

        //取得したユーザ情報から、年齢情報を抽出する
        if (list != null && list.size() > 0) {
            userAgeReq = StringUtils.getUserAgeInfo(list);
        }

        return userAgeReq;
    }

    /**
     * 取得したユーザが h4dユーザかを返却.
     * h4dユーザ = "contract_status"の値が "002：ひかりTVfordocomo契約中".
     *
     * @return h4dユーザフラグ
     */
    public boolean isH4dUser() {
        UserInfoDataManager userInfoDataManager = new UserInfoDataManager(mContext);
        List<Map<String, String>> list = userInfoDataManager.selectUserAgeInfo();
        //loggedin_account が0番目 h4d_contracted_account が1番目に来ることを想定
        final int LOGGEDIN_ACCOUNT = 0;

        boolean isH4dUser = false;
        if (list != null && list.size() > 0) {
            String contractStatus = list.get(LOGGEDIN_ACCOUNT).get(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS);
            if (contractStatus.equals(CONTRACT_STATUS_H4D)) {
                isH4dUser = true;
            }
        }
        return isH4dUser;
    }
}