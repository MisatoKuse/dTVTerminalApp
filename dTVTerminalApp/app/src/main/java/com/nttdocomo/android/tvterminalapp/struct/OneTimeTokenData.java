/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

/**
 * ワンタイムトークンの情報伝達用構造体.
 */
public class OneTimeTokenData {
    /**
     * ワンタイムトークンのデータ分離用文字列.
     */
    private static final String ONE_TIME_TOKEN_SPLITTER = "／";

    /**
     * ワンタイムトークンのパラメータ順.
     */
    private static final int ONE_TIME_TOKEN_POSITION = 0;

    /**
     * ワンタイムトークン取得日時のパラメータ順.
     */
    private static final int ONE_TIME_TOKEN_TIME_POSITION = 1;

    /**
     * ワンタイムトークン.
     */
    private String mOneTimeToken;

    /**
     * ワンタイムトークン取得日時（エポック秒で記録）.
     */
    private long mOneTimeTokenTime;

    /**
     * コンストラクタ.
     */
    public OneTimeTokenData() {
        //初期化を行う
        initData();
    }

    /**
     * 初期データ付きコンストラクタ.
     * @param source 元になる文字列
     */
    public OneTimeTokenData(final String source) {
        //初期化を行う
        initData();

        //データを解析して格納する
        analyzeOneTimeTokenString(source);
    }

    /**
     * ワンタイムトークン取得.
     * @return ワンタイムトークン
     */
    public String getOneTimeToken() {
        return mOneTimeToken;
    }

    /**
     * ワンタイムトークン設定.
     * @param oneTimeToken ワンタイムトークン
     */
    public void setOneTimeToken(final String oneTimeToken) {
        mOneTimeToken = oneTimeToken;
    }

    /**
     * ワンタイムトークン使用期限のエポック秒.
     *
     * @return 使用期限のエポック秒
     */
    public long getOneTimeTokenGetTime() {
        return mOneTimeTokenTime;
    }

    /**
     * ワンタイムトークン使用期限のエポック秒設定.
     * @param oneTimeTokenGetTime  ワンタイムトークン使用期限のエポック秒
     */
    public void setOneTimeTokenGetTime(final long oneTimeTokenGetTime) {
        mOneTimeTokenTime = oneTimeTokenGetTime;
    }


    /**
     * 値の初期化.
     */
    private void initData() {
        mOneTimeToken = "";
        mOneTimeTokenTime = Long.MIN_VALUE;
    }

    /**
     * ワンタイムトークン用データを解析して格納する.
     *
     * @param source 元になる文字列
     */
    private void analyzeOneTimeTokenString(final String source) {
        //値が空か、分割できないならば初期化して帰る
        if (TextUtils.isEmpty(source) || !source.contains(ONE_TIME_TOKEN_SPLITTER)) {
            initData();
            return;
        }

        //元の文字列を分割する
        String[] buffer = source.split(ONE_TIME_TOKEN_SPLITTER);

        //トークンを取得
        mOneTimeToken = buffer[ONE_TIME_TOKEN_POSITION];

        //数字判定を行う
        if (DataBaseUtils.isNumber(buffer[ONE_TIME_TOKEN_TIME_POSITION])) {
            //取得日時を取得
            mOneTimeTokenTime = Long.parseLong(
                    buffer[ONE_TIME_TOKEN_TIME_POSITION]);
        } else {
            //数字ではなかったので、最大値にする
            mOneTimeTokenTime = Long.MIN_VALUE;
        }
    }

    /**
     * プリファレンス書き込み用文字列の作成.
     *
     * @return 整形後のトークン情報
     */
    public String makeOneTimeTokenString() {
        //書き込み文字列の作成
        String buffer = StringUtils.getConnectStrings(mOneTimeToken, ONE_TIME_TOKEN_SPLITTER,
                String.valueOf(mOneTimeTokenTime));

        //整形下したデータを返す
        return buffer;
    }
}
