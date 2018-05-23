/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import java.util.List;

/**
 * ユーザー情報取得WebClient.
 */
public class UserInfoWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface UserInfoJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param userInfoLists JSONパース後のデータ
         */
        void onUserInfoJsonParsed(List<UserInfoList> userInfoLists);
    }

    /**
     * コールバックのインスタンス.
     */
    private UserInfoJsonParserCallback mUserInfoJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public UserInfoWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new UserInfoJsonParser(mUserInfoJsonParserCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mUserInfoJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mUserInfoJsonParserCallback.onUserInfoJsonParsed(null);
        }
    }

    /**
     * 契約情報取得.
     *
     * @param userInfoJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getUserInfoApi(final UserInfoJsonParserCallback userInfoJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("UserInfoWebClient is stopping connection");
            return false;
        }
        //パラメーターのチェック
        if (!checkNormalParameter(userInfoJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mUserInfoJsonParserCallback = userInfoJsonParserCallback;

        //契約情報取得をワンタイムトークン付きで呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.USER_INFO_WEB_CLIENT, "",
                this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param userInfoJsonParserCallback コールバック
     * @return おかしい値があるならばfalse
     */
    private boolean checkNormalParameter(final UserInfoJsonParserCallback userInfoJsonParserCallback) {

        //コールバックが指定されていないならばfalse
        if (userInfoJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}
