/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.GenreListJsonParser;

public class GenreListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface GenreListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param genreListResponse JSONパース後のデータ
         */
        void onGenreListJsonParsed(GenreListResponse genreListResponse);
    }

    //コールバックのインスタンス
    private GenreListJsonParserCallback
            mGenreListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public GenreListWebClient(Context context) {
        super(context);
    }

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (mGenreListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new GenreListJsonParser(
                    mGenreListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (mGenreListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mGenreListJsonParserCallback
                    .onGenreListJsonParsed(null);
        }
    }

    /**
     * ジャンル一覧の取得
     *
     * @param genreListJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getGenreListApi(
            GenreListJsonParserCallback
                    genreListJsonParserCallback) {
        DTVTLogger.start();

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
        openUrl(UrlConstants.WebApiUrl.GENRE_LIST_FILE, "", this);

        DTVTLogger.end();
        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param genreListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(GenreListJsonParserCallback
                                                 genreListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (genreListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
