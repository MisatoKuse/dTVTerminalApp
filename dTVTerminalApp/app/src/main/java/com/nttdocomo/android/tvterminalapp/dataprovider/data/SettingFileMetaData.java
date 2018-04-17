/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

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
     * 設定ファイルの読み込みに失敗した場合
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
     * コンストラクタ
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

    public boolean isIsStop() {
        return mIsStop;
    }

    public boolean isFileReadError() {
        return mIsFileReadError;
    }

    public void setIsFileReadError(boolean isFileReadError) {
        mIsFileReadError = isFileReadError;
    }

    public void setIsStop(boolean isStop) {
        mIsStop = isStop;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

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
        if (DBUtils.isNumber(forceUpdateVersion)) {
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
        if (DBUtils.isNumber(optionalUpdateVersion)) {
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
