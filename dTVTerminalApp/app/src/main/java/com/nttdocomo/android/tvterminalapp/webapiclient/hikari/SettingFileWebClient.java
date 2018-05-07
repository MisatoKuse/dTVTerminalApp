/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.SettingFileJsonParser;

/**
 * 設定ファイルWebClient.
 */
public class SettingFileWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {
    /**
     * タイムアウト時間.
     *
     * (テストサーバーが見えない環境を考慮して短めとする。なお、テストサーバーが見えていれば、200ミリ秒でも読み込みに成功する)
     * (元の2000ミリ秒の場合スプラッシュ画面のタイムアウトと同じ時間なので、ファイルが見えない場合に
     * 必ずウェイト表示になってしまう。若干値を減らすことで、スプラッシュ画面の最初の終了判定時に
     * 確実にタイムアウトしている状態とすることで、ウェイト表示を未表示とする）、
     */
    private final static int TIME_OUT_TIME = 1900;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface SettingFileJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param settingFileResponse JSONパース後のデータ
         */
        void onSettingFileJsonParsed(SettingFileResponse settingFileResponse);
    }

    /**コールバックのインスタンス.*/
    private SettingFileJsonParserCallback mSettingFileJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public SettingFileWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mSettingFileJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new SettingFileJsonParser(mSettingFileJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mSettingFileJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mSettingFileJsonParserCallback.onSettingFileJsonParsed(null);
        }
    }

    /**
     * 設定ファイルの取得.
     *
     * @param settingFileJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getSettingFileApi(final SettingFileJsonParserCallback settingFileJsonParserCallback) {
        DTVTLogger.start();

        if (mIsCancel) {
            DTVTLogger.error("SettingFileWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(settingFileJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            DTVTLogger.end_ret(String.valueOf(false));
            return false;
        }

        //コールバックのセット
        mSettingFileJsonParserCallback = settingFileJsonParserCallback;

        //設定ファイルを読み込む
        DTVTLogger.debug("Get setting file");
        openUrl(UrlConstants.WebApiUrl.SETTING_FILE, "", TIME_OUT_TIME, this);

        DTVTLogger.end();
        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param settingFileJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final SettingFileJsonParserCallback
                                                 settingFileJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (settingFileJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }

    @Override
    protected String getRequestMethod() {
        return WebApiBasePlala.REQUEST_METHOD_GET;
    }
}
