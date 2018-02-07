/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ServiceTokenData;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TokenParser;

import java.util.List;

/**
 * サービストークン取得APIの呼び出し.
 */
public class ServiceTokenClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 正常時識別パラメータ.
     * 後の判定用にパブリック
     */
    public static final String SUCCESS_STRING = "https%3a%2f%2fwww%2egoogle%2eco%2ejp";

    /**
     * エラー時識別パラメータ.
     * 後のエラー判定用にパブリック
     */
    public static final String ERROR_STRING = "https%3a%2f%2fwww%2egoo%2ene%2ejp%2f";

    // 送信パラメータの最初の固定部
    private static final String FIRST_PARAMETER = "state=client=dremote urlok=";

    // 送信パラメータの2番目の固定部
    private static final String SECOND_PARAMETER = " urlng=";

    // 送信パラメータの3番目の固定部
    private static final String THIRD_PARAMETER = "&authotp=";

    //統合送信パラメータ
    private static final String UNITE_SEND_PARAMETER = StringUtils.getConnectStrings(
            FIRST_PARAMETER,SUCCESS_STRING,
            SECOND_PARAMETER,ERROR_STRING,
            THIRD_PARAMETER);

    //コールバックのインスタンス
    private TokenJsonParserCallback mTokenJsonParserCallback;

    /**
     * コールバック.
     */
    public interface TokenJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param channelLists JSONパース後のデータ
         */
        void onTokenJsonParsed(List<ServiceTokenData> channelLists);
    }

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public ServiceTokenClient(Context context) {
        super(context);
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new TokenParser(mTokenJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        mTokenJsonParserCallback.onTokenJsonParsed(null);
    }

    /**
     * サービストークン取得.
     *
     * @param oneTimePass             ワンタイムパスワード
     * @param tokenJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getServiceTokenApi(String oneTimePass,
                                      TokenJsonParserCallback tokenJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(oneTimePass, tokenJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックの設定
        mTokenJsonParserCallback = tokenJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(oneTimePass);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //ワンタイムトークン取得を呼び出す
        openOneTimeTokenGetUrl(sendParameter, this);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param oneTimePass               ワンタイムパスワード
     * @param channelJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(String oneTimePass,
                                         TokenJsonParserCallback channelJsonParserCallback) {
        if (oneTimePass == null || oneTimePass.isEmpty()) {
            //ワンタイムパスワードがヌルや空文字ならばエラー
            return false;
        }

        if (channelJsonParserCallback == null) {
            //コールバックがヌルならばfalse
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param oneTimePass ワンタイムパスワード
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(String oneTimePass) {
        String answerText;

        //送信パラメータの作成
        answerText = StringUtils.getConnectStrings(UNITE_SEND_PARAMETER,
                TextUtils.htmlEncode(oneTimePass));

        return answerText;
    }
}
