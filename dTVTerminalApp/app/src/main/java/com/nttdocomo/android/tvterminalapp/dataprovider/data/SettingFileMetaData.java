/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import java.io.Serializable;

/**
 * 起動時等に読み込む設定ファイルの情報.
 */
public class SettingFileMetaData  implements Serializable {
    private static final long serialVersionUID = -1142550644372960915L;

    /**
     * アプリの実行を停止するならばtrue.
     */
    private boolean mIsStop;

    /**
     * 設定ファイルの読み込みに失敗した場合.
     */
    private boolean mIsFileReadError;

    /**
     * アプリ実行停止時に表示するメッセージ.
     */
    private String mDescription;

    /**
     * 強制アップデート対象バージョン・これより値が小さい場合は、GooglePlayに遷移.
     */
    private int mForceUpdateVersion;

    /**
     * アップデート対象バージョン・これより値が小さい場合は、GooglePlayへの遷移を促す.
     */
    private int mOptionalUpdateVersion;

    /**
     * コンストラクタ.
     */
    public SettingFileMetaData() {
        //内容の初期化
        mIsStop = false;
        mIsFileReadError = false;
        mDescription = "";
        mForceUpdateVersion = 0;
        mOptionalUpdateVersion = 0;
    }

    //個々の情報のゲッターとセッター・単純な物はコメント略

    /**
     * アプリの実行を停止するフラグを取得.
     * @return 停止：true else false
     */
    public boolean isIsStop() {
        return mIsStop;
    }

    /**
     * 設定ファイルの読み込みに失敗フラグを取得.
     * @return 失敗:true else false
     */
    public boolean isFileReadError() {
        return mIsFileReadError;
    }
    /**
     * 設定ファイルの読み込みに失敗フラグ設定する.
     * @param isFileReadError 設定ファイルの読み込みに失敗フラグ
     */
    public void setIsFileReadError(final boolean isFileReadError) {
        mIsFileReadError = isFileReadError;
    }

    /**
     * アプリの実行を停止するフラグを設定.
     * @param isStop アプリの実行を停止するフラグ
     */
    public void setIsStop(final boolean isStop) {
        mIsStop = isStop;
    }
    /**
     * アプリ実行停止時に表示するメッセージ取得.
     * @return アプリ実行停止時に表示するメッセージ
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * アプリ実行停止時に表示するメッセージ設定.
     * @param description アプリ実行停止時に表示するメッセージ
     */
    public void setDescription(final String description) {
        mDescription = description;
    }

    /**
     * 強制アップデート対象バージョン取得.
     * @return 強制アップデート対象バージョン
     */
    public int getForceUpdateVersion() {
        return mForceUpdateVersion;
    }

    /**
     * 与えられた強制アップデート対象バージョン情報を蓄積する.
     * <p>
     * (パーサーから取得したデータ用)
     *
     * @param forceUpdateVersion バージョン情報
     */
    public void setForceUpdateVersion(final String forceUpdateVersion) {
        DTVTLogger.start();

        //文字列が数字だけかどうかを確認
        if (DataBaseUtils.isNumber(forceUpdateVersion)) {
            //バージョン情報は数字文字列だったので、数値に変換する
            mForceUpdateVersion = Integer.parseInt(forceUpdateVersion);
        }

        DTVTLogger.end();
    }

    /**
     * 与えられた強制アップデート対象バージョン情報を蓄積する.
     * <p>
     * (前回取得から1時間以内の再取得データ設定用)
     *
     * @param forceUpdateVersion バージョン情報
     */
    public void setForceUpdateVersion(final int forceUpdateVersion) {
        DTVTLogger.start();
        mForceUpdateVersion = forceUpdateVersion;
        DTVTLogger.end();
    }

    /**
     * 与えられたアップデート対象バージョン情報を取得.
     * @return 与えられたアップデート対象バージョン情報
     */
    public int getOptionalUpdateVersion() {
        return mOptionalUpdateVersion;
    }

    /**
     * 与えられたアップデート対象バージョン情報を蓄積する.
     *
     * @param optionalUpdateVersion バージョン情報
     */
    public void setOptionalUpdateVersion(final String optionalUpdateVersion) {
        DTVTLogger.start();
        //文字列が数字だけかどうかを確認
        if (DataBaseUtils.isNumber(optionalUpdateVersion)) {
            //バージョン情報は数字の文字列だったので、数値に変換する
            mOptionalUpdateVersion = Integer.parseInt(optionalUpdateVersion);
        }
        DTVTLogger.end();
    }

    /**
     * 与えられたアップデート対象バージョン情報を蓄積する.
     * <p>
     * (前回取得から1時間以内の再取得データ設定用)
     *
     * @param optionalUpdateVersion バージョン情報
     */
    public void setOptionalUpdateVersion(final int optionalUpdateVersion) {
        DTVTLogger.start();
        mOptionalUpdateVersion = optionalUpdateVersion;
        DTVTLogger.end();
    }
}
