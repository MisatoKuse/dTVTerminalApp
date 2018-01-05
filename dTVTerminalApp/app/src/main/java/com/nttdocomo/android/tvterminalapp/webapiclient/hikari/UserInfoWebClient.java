/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

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
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param userInfoLists JSONパース後のデータ
         */
        void onUserInfoJsonParsed(List<UserInfoList> userInfoLists);
    }

    //コールバックのインスタンス
    private UserInfoJsonParserCallback mUserInfoJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new UserInfoJsonParser(mUserInfoJsonParserCallback).execute(returnCode.bodyData);
    }

    @Override
    public void onError() {
        if (mUserInfoJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mUserInfoJsonParserCallback.onUserInfoJsonParsed(null);
        }
    }

    /**
     * 契約情報取得
     *
     * @param userInfoJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getUserInfoApi(Context context,
                                  UserInfoJsonParserCallback userInfoJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(context, userInfoJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mUserInfoJsonParserCallback = userInfoJsonParserCallback;

        //契約情報取得をワンタイムトークン付きで呼び出す
        openUrlAddOtt(context, UrlConstants.WebApiUrl.USER_INFO_WEB_CLIENT, "",
                this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param context                    コンテキスト
     * @param userInfoJsonParserCallback コールバック
     * @return おかしい値があるならばfalse
     */
    private boolean checkNormalParameter(Context context,
                                         UserInfoJsonParserCallback userInfoJsonParserCallback) {

        //コンテキストが指定されていないならばfalse
        if (context == null) {
            return false;
        }

        //コールバックが指定されていないならばfalse
        if (userInfoJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
