/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;

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
        //TODO: JSONパーサー完成前なので、別のレスポンスを仮使用
        void onGenreListJsonParsed(RemoteRecordingReservationListResponse genreListResponse);
    }

    //コールバックのインスタンス
    private GenreListJsonParserCallback
            mGenreListJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (mGenreListJsonParserCallback != null) {
            //TODO: JSONパーサー完成後にコメントを外す
            //JSONをパースして、データを返す
//            new GenreListJsonParser(
//                    mGenreListJsonParserCallback)
//                    .execute(returnCode.bodyData);
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
        openUrl(API_NAME_LIST.GENRE_LIST_FILE.getString(), "", this);

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
