/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RoleListJsonParser;

public class RoleListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface RoleListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param roleListResponse JSONパース後のデータ
         */
        void onRoleListJsonParsed(RoleListResponse roleListResponse);
    }

    //コールバックのインスタンス
    private RoleListJsonParserCallback mRoleListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public RoleListWebClient(Context context) {
        super(context);
    }

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (mRoleListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RoleListJsonParser(mRoleListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        if (mRoleListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRoleListJsonParserCallback.onRoleListJsonParsed(null);
        }
    }

    /**
     * ジャンル一覧の取得
     *
     * @param roleListJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getRoleListApi(RoleListJsonParserCallback roleListJsonParserCallback) {
        DTVTLogger.start();

        //パラメーターのチェック
        if (!checkNormalParameter(roleListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            DTVTLogger.end_ret(String.valueOf(false));
            return false;
        }

        //コールバックのセット
        mRoleListJsonParserCallback = roleListJsonParserCallback;

        //ジャンル一覧ファイルを読み込む
        openUrl(UrlConstants.WebApiUrl.ROLE_LIST_FILE, "", this);

        DTVTLogger.end();
        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param roleListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(RoleListJsonParserCallback
                                                 roleListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (roleListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
