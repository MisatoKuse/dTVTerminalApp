/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.AccountList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.SerializablePreferencesData;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import java.util.List;

/**
 * ユーザ情報DataProvider.
 */
public class UserInfoDataProvider implements UserInfoWebClient.UserInfoJsonParserCallback {

    /**
     * コンテキストファイル.
     */
    private Context mContext;

    /**
     * データを返すコールバック.
     */
    private UserDataProviderCallback mUserDataProviderCallback;

    /**
     * 前回の日時.
     */
    private long mBeforeDate = 0;

    /**
     * dTV契約ステータス.
     */
    public static final String CONTRACT_STATUS_DTV = "001";
    /**
     * H4D契約ステータス.
     */
    public static final String CONTRACT_STATUS_H4D = "002";
    /**
     * UserInfoWebClient.
     */
    private UserInfoWebClient mUserInfoWebClient = null;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsStop = false;
    /**
     * エラー情報.
     */
    private ErrorState mErrorSate = null;

    @Override
    public void onUserInfoJsonParsed(final List<UserInfoList> userInfoLists) {
        DTVTLogger.start();

        if (userInfoLists != null) {
            //取得したデータが返ってきたので、格納する
            SerializablePreferencesData preferencesData = new SerializablePreferencesData();
            preferencesData.setUserInfoList(userInfoLists);

            DTVTLogger.debug("UserInfoDataProvider::onUserInfoJsonParsed setUserInfo");
            SharedPreferencesUtils.setSharedPreferencesSerializableData(mContext, preferencesData);
            //契約状態が「契約有り」→「無し」に変わった場合、ダウンロードしたコンテンツを削除
            DownloadDataProvider.clearAllDownloadContents(mContext, true);
            //後処理を行う
            afterProcess(userInfoLists);
        } else {
            //エラー情報を控えておく
            mErrorSate = mUserInfoWebClient.getError();

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
         *@param isDataChange データ更新
         *@param list コンテンツリスト
         *@param isUserContractChange 契約情報変更
         */
        void userInfoListCallback(final boolean isDataChange, final List<UserInfoList> list,
                                  final boolean isUserContractChange);
    }

    /**
     * コンストラクタ(getUserAge()用).
     *
     * @param mContext コンテキストファイル
     */
    public UserInfoDataProvider(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * コンストラクタ.
     * @param userDataProviderCallback callback
     *
     * @param context コンテキスト
     */
    public UserInfoDataProvider(final Context context, final UserDataProviderCallback userDataProviderCallback) {
        DTVTLogger.start();

        mContext = context;

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
        mBeforeDate = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);

        //通信不可とキャッシュ有効期限内
        if (!isOnline(mContext) || !isUserInfoTimeOut()) {
            DTVTLogger.debug("OffLine or Use cache");

            mUserDataProviderCallback.userInfoListCallback(false, SharedPreferencesUtils.getSharedPreferencesUserInfo(mContext), false);
            return;
        }

        if (!mIsStop) {
            //新たなデータを取得する
            mUserInfoWebClient = new UserInfoWebClient(mContext);
            mUserInfoWebClient.getUserInfoApi(this);
        } else {
            DTVTLogger.error("UserInfoDataProvider is stopping connect");
        }
        DTVTLogger.end();
    }

    /**
     * 通信可能確認.
     *
     * @param context コンテキスト
     * @return 通信可能ならばtrue
     */
    private static boolean isOnline(final Context context) {
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
    public boolean isUserInfoTimeOut() {
        DTVTLogger.start();

        //最終取得日時の取得
        long lastTime = SharedPreferencesUtils.getSharedPreferencesUserInfoDate(mContext);
        long now = DateUtils.getNowTimeFormatEpoch();
        //現在日時が最終取得日時+1時間以下ならば、まだデータは新しい
        DTVTLogger.end("UserInfo lastTime:" + lastTime + " now:" + now);
        if (now < lastTime + DateUtils.EPOCH_TIME_ONE_HOUR) {
            // ただし時刻が巻き戻った(最終取得が未来の)場合は再取得する.
            return now < lastTime;
        }
        //データは古くなった
        return true;
    }

    /**
     * データの取得やデータの取得の必要のない場合の後処理.
     *
     * @param userInfoLists 契約情報
     */
    private void afterProcess(final List<UserInfoList> userInfoLists) {
        DTVTLogger.start();
        DTVTLogger.debug("userInfoLists " + userInfoLists);

        // trueならば、データ更新扱い許可とするフラグ
        boolean changeFlag = true;

        //初回実行時はホーム画面に飛ばさないために、許可フラグはfalseにする
        if (mBeforeDate == Long.MIN_VALUE) {
            DTVTLogger.debug("first exec");
            changeFlag = false;
        }
        List<UserInfoList> tmpUserInfoLists = userInfoLists;

        if (tmpUserInfoLists == null) {
            DTVTLogger.debug("no user data");
            // データ指定がヌルなので、前回の値を指定する。
            tmpUserInfoLists = SharedPreferencesUtils.getSharedPreferencesUserInfo(mContext);
        } else if (tmpUserInfoLists.get(0).getLoggedinAccount().size() == 0) {
            //契約情報がゼロ件の場合、情報取得に失敗するので、userInfoListsをヌルにして、異常データであることを明示する
            tmpUserInfoLists = null;
        }

        //新旧の年齢データを比較する
        int beforeAge = SharedPreferencesUtils.getSharedPreferencesAgeReq(mContext);
        int newAge = UserInfoUtils.getUserAgeInfoWrapper(tmpUserInfoLists);

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

        //旧契約情報の取得
        String oldContractInfo = SharedPreferencesUtils.getSharedPreferencesContractInfo(mContext);

        //新契約情報の取得
        String newContractInfo = UserInfoUtils.getUserContractInfo(tmpUserInfoLists);

        //契約情報の変化・変わっているならばtrue
        boolean contractAnswer = false;

        //新旧契約情報の比較・契約無しの場合は"none"が入るので、単純な比較で契約情報の変更は検出できる
        if (!newContractInfo.equals(oldContractInfo)) {
            //新旧の契約情報が違うので、trueにする
            contractAnswer = true;
        }

        if (tmpUserInfoLists != null) {
            //常に最新のエリアコードを保存する
            String areaCode = tmpUserInfoLists.get(0).getLoggedinAccount().get(0).getAreaCode();
            String oldAreaCode = UserInfoUtils.getAreaCode(mContext);
            if (!TextUtils.isEmpty(oldAreaCode) && TextUtils.isEmpty(areaCode)) {
                DateUtils.clearTvScheduleDate(mContext);
            } else if (!TextUtils.isEmpty(areaCode) && (!areaCode.equals(oldAreaCode))) {
                DateUtils.clearTvScheduleDate(mContext);
            }
            SharedPreferencesUtils.setSharedKeyPreferencesAreaCode(mContext, areaCode);
        }

        //結果を返すコールバックを呼ぶ(userInfoListsはfinal付与の余波でヌルのままになる場合があるので、ここはtmpUserInfoListsを指定)
        mUserDataProviderCallback.userInfoListCallback(isChangeAge, tmpUserInfoLists, contractAnswer);

        DTVTLogger.end();
    }

    /**
     * 取得済みのユーザー年齢情報を取得する.
     * @return ユーザー年齢情報
     */
    public int getUserAge() {

        //ユーザ情報をDBから取得する
        int userAgeReq;
        List<UserInfoList> userInfoList = SharedPreferencesUtils.getSharedPreferencesUserInfo(mContext);

        //取得したユーザ情報から、年齢情報を抽出する
        userAgeReq = UserInfoUtils.getUserAgeInfoWrapper(userInfoList);

        return userAgeReq;
    }

    /**
     * 取得したユーザが h4dユーザかを返却.
     * h4dユーザ = "contract_status"の値が "002：ひかりTVfordocomo契約中".
     *
     * @return h4dユーザフラグ
     */
    boolean isH4dUser() {
        List<UserInfoList> userInfoList = SharedPreferencesUtils.getSharedPreferencesUserInfo(mContext);
        //loggedin_account が0番目 h4d_contracted_account が1番目に来ることを想定
        final int LOGGEDIN_ACCOUNT = 0;

        boolean isH4dUser = false;
        if (userInfoList != null && userInfoList.size() > 0) {
            List<AccountList> loggedinAccount = userInfoList.get(LOGGEDIN_ACCOUNT).getLoggedinAccount();
            String contractStatus = loggedinAccount.get(LOGGEDIN_ACCOUNT).getContractStatus();
            if (contractStatus.equals(CONTRACT_STATUS_H4D)) {
                isH4dUser = true;
            }
        }
        return isH4dUser;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsStop = true;
        if (mUserInfoWebClient != null) {
            mUserInfoWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsStop = false;
        if (mUserInfoWebClient != null) {
            mUserInfoWebClient.enableConnection();
        }
    }

    /**
     * ネットワークエラー情報のゲッター.
     *
     * @return ネットワークエラークラス
     */
    public ErrorState getError() {
        return mErrorSate;
    }
}
