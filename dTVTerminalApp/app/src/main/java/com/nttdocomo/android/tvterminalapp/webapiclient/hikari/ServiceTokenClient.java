/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * サービストークン取得APIの呼び出し.
 */
public class ServiceTokenClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 正常時識別パラメータ.
     * 自動リダイレクト処理を強制的に中断させる為、プロトコル名を敢えて不正な物にしている
     */
    //private static final String SUCCESS_STRING = "https://www.google.co.jp";
    private static final String SUCCESS_STRING = "aaaa%3a%2f%2fwww%2egoogle%2eco%2ejp";

    /**
     * エラー時識別パラメータ.
     * 自動リダイレクト処理を強制的に中断させる為、プロトコル名を敢えて不正な物にしている
     */
    //private static final String ERROR_STRING = "https://www.goo.ne.jp/";
    private static final String ERROR_STRING = "bbbb%3a%2f%2fwww%2egoo%2ene%2ejp%2f";

    // 送信パラメータの最初の固定部
    private static final String FIRST_PARAMETER = "state=client=dremote urlok=";

    // 送信パラメータの2番目の固定部
    private static final String SECOND_PARAMETER = " urlng=";

    // 送信パラメータの3番目の固定部
    private static final String THIRD_PARAMETER = "&authotp=";

    //統合送信パラメータ
    private static final String UNITE_SEND_PARAMETER = StringUtils.getConnectStrings(
            FIRST_PARAMETER, SUCCESS_STRING,
            SECOND_PARAMETER, ERROR_STRING,
            THIRD_PARAMETER);

    //コールバックのインスタンス
    private TokenGetCallback mTokenGetCallback;

    /**
     * コールバック.
     */
    public interface TokenGetCallback {
        /**
         * 終了した場合に呼ばれるコールバック.
         *
         * @param success 取得に成功した場合はtrue
         */
        void onTokenGot(boolean success);
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
     * 結果識別用にOK URLをデコードして返却.
     *
     * @return デコード後のOK URL
     */
    public static String getOkUrlString() {
        try {
            //デコード
            return URLDecoder.decode(SUCCESS_STRING, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 結果識別用にNG URLをデコードして返却.
     *
     * @return デコード後のNG URL
     */
    public static String getNgUrlString() {
        try {
            //デコード
            return URLDecoder.decode(ERROR_STRING, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //取得に成功したので、trueを返す
        mTokenGetCallback.onTokenGot(true);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        //エラーが発生したのでfalseを返す
        mTokenGetCallback.onTokenGot(false);
    }

    /**
     * サービストークン取得.
     *
     * @param oneTimePass      ワンタイムパスワード
     * @param tokenGetCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getServiceTokenApi(String oneTimePass,
                                      TokenGetCallback tokenGetCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(oneTimePass, tokenGetCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックの設定
        mTokenGetCallback = tokenGetCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(oneTimePass);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (TextUtils.isEmpty(sendParameter)) {
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
                                         TokenGetCallback channelJsonParserCallback) {
        if (TextUtils.isEmpty(oneTimePass)) {
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
        String answerText = null;

        //送信パラメータの作成
        try {
            answerText = StringUtils.getConnectStrings(UNITE_SEND_PARAMETER,
                    URLEncoder.encode(oneTimePass, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            DTVTLogger.debug(e);
        }

        return answerText;
    }
}
