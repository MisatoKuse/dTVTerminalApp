/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

/**
 * WebAPIエラー情報クラス.
 * <p>
 * アクティビティ側で表示する際に使用する、ネットワーク情報のエラーやAPIのエラーの情報を格納します
 */
public class ErrorState {

    /**
     * 通信時エラー情報.
     */
    private DTVTConstants.ERROR_TYPE mErrorType;

    /**
     * エラーメッセージ.
     */
    private String mErrorMessage;

    /**
     * エラーコード.
     */
    private String mErrorCode;

    /**
     * 空白文字.
     */
    private static final String SPACE_STRING = " ";

    /**
     * 正常終了コード.
     */
    private static final String NORMAL_RESULT_TEXT = "0";

    /**
     * XMLの結果コードの名前
     */
    private static final String XML_RESULT_STRING = "id";

    /**
     * コンストラクタ.
     */
    public ErrorState() {
        //エラー情報を初期化
        mErrorType = DTVTConstants.ERROR_TYPE.SUCCESS;
        mErrorMessage = "";
        mErrorCode = "";
    }

    /**
     * エラーメッセージとして、エラーメッセージとエラーコードを組み合わせた物を返す.
     *
     * @param context 指定していればエラーコードは文字リソースから取得した括弧で囲まれる。なければそのまま出力
     * @return エラーメッセージ
     */
    public String getApiErrorMessage(Context context) {
        //エラーメッセージが無ければ空文字で帰る
        if (TextUtils.isEmpty(mErrorMessage)) {
            return "";
        }

        String openString;
        String closeString = "";
        //コンテキストの指定があれば、囲む括弧の文字を取得
        if (context != null) {
            openString = context.getString(R.string.home_contents_front_bracket);
            closeString = context.getString(R.string.home_contents_back_bracket);
        } else {
            //文字列リソースが取得できないので、空白で分離
            openString = SPACE_STRING;
        }

        String answer;
        //エラーコードの有無の判定
        if (TextUtils.isEmpty(mErrorCode)) {
            //エラーコードは無いので、メッセージだけを返す
            answer = mErrorMessage;
        } else {
            //文字列を結合する
            answer = StringUtils.getConnectStrings(
                    mErrorMessage,
                    openString,
                    mErrorCode,
                    closeString);
        }

        //結果を返す
        return answer;
    }

    /**
     * 現在のエラー情報に合わせたエラーメッセージを取得する.
     *
     * @param context コンテキスト
     */
    public void addErrorMessage(Context context) {
        //コンテキストが無いとリソースにアクセスできないので、帰る
        if (context == null) {
            return;
        }

        //エラーメッセージの選択
        //TODO: 仮ワーディングで現在は2種類になっている
        switch (mErrorType) {
            case SUCCESS:
                //正常なのでメッセージは無し。
                break;
            case SSL_ERROR:
                //SSLエラー用メッセージの取得
                mErrorMessage = context.getString(R.string.nw_error_ssl_message);
                break;
            case SERVER_ERROR:
            case TOKEN_ERROR:
            case NETWORK_ERROR:
            case HTTP_ERROR:
                //case API_ERROR:
                //その他Lエラー用メッセージの取得
                mErrorMessage = context.getString(R.string.nw_error_message);
                break;
        }
    }

    /**
     * 送られてきた文字列を格納する。JSONだった場合はエラー番号判定を行ってエラー番号だけ格納する.
     *
     * @param errorCode HTTPステータス文字列やJSONのエラー情報
     * @return 結果がエラーならばtrue、正常ならばfalse
     */
    public boolean setErrorCode(String errorCode) {
        //送られてきた文字列がJSONかどうかを見る為に、変換を行う
        JSONObject json;
        try {
            json = new JSONObject(errorCode);
        } catch (JSONException e) {
            //JSONではなかったので、そのまま使用する
            this.mErrorCode = errorCode;
            return false;
        }

        //ステータスの確認
        try {
            String status = json.getString(JsonConstants.META_RESPONSE_STATUS);
            if (status.equals(JsonConstants.META_RESPONSE_STATUS_OK)) {
                //ステータスはOKだったので、正常で帰る
                return false;

            }
        } catch (JSONException e) {
            DTVTLogger.debug("no error num");
            //エラー番号は無いので、何もしない
        }

        //最終結果
        boolean answer = false;

        //エラー番号を持っているかどうかを見る
        try {
            //エラー番号が取得できたので、それを採用する
            this.mErrorCode = json.getString(JsonConstants.META_RESPONSE_NG_ERROR_NO);
            answer = true;
        } catch (JSONException e) {
            DTVTLogger.debug("no error num");
            //エラー番号は無いので、何もしない
        }

        return answer;
    }

    /**
     * 出力されたXMLから、結果コードを先行して取得して、エラー番号としてセットする
     *
     * @param xmlString XML文字列
     */
    public void setXmlErrorCode(String xmlString) {
        DTVTLogger.debugHttp(xmlString);

        if (TextUtils.isEmpty(xmlString)) {
            //空文字が送られてきた場合は空データなので帰る
            return;
        }

        //パーサーの準備
        XmlPullParser parser = Xml.newPullParser();
        try {
            //パーサーにXML文字列をセット
            parser.setInput(new StringReader(xmlString));
            //最初の項目の種別を取得する
            int eventType = parser.getEventType();
            boolean endFlg = false;
            while (!endFlg) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        //開始タグの場合、結果コードかどうかを見る
                        if (XML_RESULT_STRING.equals(parser.getName())) {
                            //結果コードなので、次の値のタイプを取得
                            eventType = parser.next();
                            if (eventType == XmlPullParser.TEXT) {
                                //文字列なのでゼロかどうかを見る
                                String buffer = parser.getText();
                                if (!buffer.equals(NORMAL_RESULT_TEXT)) {
                                    //ゼロ以外ならばエラー値として格納する
                                    mErrorCode = buffer;
                                }

                                DTVTLogger.debug("Result = " + mErrorCode);
                                return;
                            }
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        endFlg = true;

                        //本来はここまで来る前に結果コードを取得できるので、エラーとする
                        mErrorType = DTVTConstants.ERROR_TYPE.NETWORK_ERROR;
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException | NullPointerException e) {
            //正常なXMLが来ていないので、エラーとする
            mErrorType = DTVTConstants.ERROR_TYPE.NETWORK_ERROR;
        }
    }

    //各ゲッター・セッター群（基本的にコメントは略）

    public DTVTConstants.ERROR_TYPE getErrorType() {
        return mErrorType;
    }

    public void setErrorType(DTVTConstants.ERROR_TYPE errorType) {
        this.mErrorType = errorType;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    public String getErrorCode() {
        return mErrorCode;
    }
}
