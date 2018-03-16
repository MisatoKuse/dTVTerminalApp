/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

/**
 * WebAPIエラー情報クラス.
 *
 * アクティビティ側で表示する際に使用する、ネットワーク情報のエラーやAPIのエラーの情報を格納します
 */
public class ErrorState {

    /**
     * 通信時エラー情報.
     */
    private DTVTConstants.ERROR_TYPE errorType;

    /**
     * エラーメッセージ.
     */
    private String errorMessage;

    /**
     * エラーコード.
     */
    private String errorCode;

    /**
     * コンストラクタ.
     */
    public ErrorState() {
        //エラー情報を初期化
        errorType = DTVTConstants.ERROR_TYPE.SUCCESS;
        errorMessage = "";
        errorCode = "";
    }

    /**
     * エラーメッセージとして、エラーメッセージとエラーコードを組み合わせた物を返す.
     *
     * @param context 指定していればエラーコードは文字リソースから取得した括弧で囲まれる。なければそのまま出力
     * @return エラーメッセージ
     */
    public String getApiErrorMessage(Context context) {
        //エラーメッセージが無ければ空文字で帰る
        if (TextUtils.isEmpty(errorMessage)) {
            return "";
        }

        String openString = "";
        String closeString = "";
        //コンテキストの指定があれば、囲む括弧の文字を取得
        if (context != null) {
            openString = context.getString(R.string.home_contents_front_bracket);
            closeString = context.getString(R.string.home_contents_back_bracket);
        }

        String answer;
        //エラーコードの有無の判定
        if (TextUtils.isEmpty(errorCode)) {
            //エラーコードは無いので、メッセージだけを返す
            answer = errorMessage;
        } else {
            //文字列を結合する
            answer = StringUtils.getConnectStrings(
                    errorMessage,
                    openString,
                    errorCode,
                    closeString);
        }

        //結果を返す
        return answer;
    }

    //各ゲッター・セッター群（コメントは略）

    public DTVTConstants.ERROR_TYPE getErrorType() {
        return errorType;
    }

    public void setErrorType(DTVTConstants.ERROR_TYPE errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
