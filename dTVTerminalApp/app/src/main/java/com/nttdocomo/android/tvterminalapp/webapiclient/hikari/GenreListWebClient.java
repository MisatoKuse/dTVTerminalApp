/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.GenreListJsonParser;

/**
 * ジャンルリスト取得用Webクライアント.
 */
public class GenreListWebClient extends WebApiBasePlala
        implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface GenreListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param genreListResponse JSONパース後のデータ
         */
        void onGenreListJsonParsed(GenreListResponse genreListResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private GenreListJsonParserCallback mGenreListJsonParserCallback;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public GenreListWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mGenreListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new GenreListJsonParser(mGenreListJsonParserCallback).execute(returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mGenreListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mGenreListJsonParserCallback
                    .onGenreListJsonParsed(null);
        }
    }

    /**
     * ジャンル一覧の取得.
     *
     * @param genreListJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getGenreListApi(final GenreListJsonParserCallback genreListJsonParserCallback) {
        DTVTLogger.start();
        if (mIsCancel) {
            //通信禁止時はfalseで帰る
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(genreListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            DTVTLogger.end_ret(String.valueOf(false));
            return false;
        }

        //コールバックのセット
        mGenreListJsonParserCallback =
                genreListJsonParserCallback;

        //ジャンル一覧ファイルを読み込む
        DTVTLogger.debug("Get genre list file");
        openUrl(UrlConstants.WebApiUrl.GENRE_LIST_FILE, "", this);

        DTVTLogger.end();
        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param genreListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final GenreListJsonParserCallback genreListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (genreListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
    }

    @Override
    protected String getRequestMethod() {
        return WebApiBasePlala.REQUEST_METHOD_GET;
    }
}