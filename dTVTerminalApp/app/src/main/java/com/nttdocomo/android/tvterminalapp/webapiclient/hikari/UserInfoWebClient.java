/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import java.util.List;

public class UserInfoWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface UserInfoJsonParserCallback {
        /**
         * 登録が正常に終了した場合に呼ばれるコールバック
         */
        void getUserInfoResult(int ageReq);

        /**
         * 登録が正常に終了した場合に呼ばれるコールバック
         */
        void getUserInfoFailure();
    }

    //コールバックのインスタンス
    private UserInfoJsonParserCallback mUserInfoJsonParserCallback;

    /**
     * 通信成功時のコールバック
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new UserInfoJsonParser(mUserInfoJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        mUserInfoJsonParserCallback.getUserInfoFailure();
    }

    /**
     * ユーザ情報取得
     *
     * @param userInfoJsonParserCallback callback
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getUserInfoApi(UserInfoJsonParserCallback userInfoJsonParserCallback) {
        //パラメーターのチェック
        if (userInfoJsonParserCallback == null) {
            //callbackがなければ、falseで帰る
            return false;
        }

        //コールバックの設定
        mUserInfoJsonParserCallback = userInfoJsonParserCallback;

        //パラメータがないのでBlankを設定する
        String sendParameter = "";

        //チャンネル一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.USER＿INFO_GET_WEB_CLIENT, sendParameter, this);

        //現状失敗は無いのでtrue
        return true;
    }
}
