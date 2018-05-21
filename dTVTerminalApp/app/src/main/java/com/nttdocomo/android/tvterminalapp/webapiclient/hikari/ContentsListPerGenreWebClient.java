/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * ジャンル毎コンテンツ一覧.
 */
public class ContentsListPerGenreWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface ContentsListPerGenreJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param contentsListPerGenre JSONパース後のデータ
         * @param genreId リクエストしたジャンルID
         */
        void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre, String genreId);
    }

    /**
     * コールバックのインスタンス.
     */
    private ContentsListPerGenreJsonParserCallback mContentsListPerGenreJsonParserCallback;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * リクエストジャンル.
     */
    private String mGenreId = "";
    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public ContentsListPerGenreWebClient(final Context context) {
        super(context);
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //拡張情報付きでパースを行う
        VideoRankJsonParser videoRankJsonParser = new VideoRankJsonParser(
                mContentsListPerGenreJsonParserCallback, returnCode.extraData, mGenreId);

        //JSONをパースして、データを返す
        videoRankJsonParser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        if (mContentsListPerGenreJsonParserCallback != null) {
            mContentsListPerGenreJsonParserCallback.onContentsListPerGenreJsonParsed(null, mGenreId);
        }
    }

    /**
     * ジャンル毎コンテンツ一覧取得.
     *
     * @param limit                                  取得する最大件数(値は1以上)
     * @param offset                                 取得位置(値は1以上)
     * @param filter                                 フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                                 年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param genreId                                ジャンルID（ヌルや空文字ならば出力されず、無指定となる）
     * @param sort                                   ソート指定（titleruby_asc/avail_s_asc/avail_e_desc/play_count_desc
     * @param contentsListPerGenreJsonParserCallback コールバック
     * @return パラメータエラー等ならばfalse
     */
    public boolean getContentsListPerGenreApi(
            final int limit, final int offset, final String filter, final int ageReq,
            final String genreId, final String sort,
            final ContentsListPerGenreJsonParserCallback contentsListPerGenreJsonParserCallback) {
        if (mIsCancel) {
            //通信禁止中はfalseで帰る
            DTVTLogger.error("ContentsListPerGenreWebClient is stopping connection");
            return false;
        }
        // リクエストしたジャンルIDを保持.
        mGenreId = genreId;

        //パラメーターのチェック(genreIdはヌルを受け付けるので、チェックしない)
        if (!checkNormalParameter(limit, offset, filter, ageReq, genreId, sort,
                contentsListPerGenreJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックを呼べるようにする
        mContentsListPerGenreJsonParserCallback = contentsListPerGenreJsonParserCallback;

        //取得コンテンツタイプを"すべて"に固定
        final String CONTENTS_LIST_TYPE = "";
        //送信用パラメータの作成
        String sendParameter = makeSendParameter(limit, offset, filter, ageReq, genreId, CONTENTS_LIST_TYPE, sort);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //拡張情報を追加する
        Bundle bundle = new Bundle();
        bundle.putString("genreId", genreId);

        //ジャンル毎コンテンツ一覧取得を呼び出す
        openUrlWithExtraData(UrlConstants.WebApiUrl.CONTENTS_LIST_PER_GENRE_WEB_CLIENT,
                sendParameter, this, bundle);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param limit                                  取得する最大件数(値は1以上)
     * @param offset                                 取得位置(値は1以上)
     * @param filter                                 フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                                 年齢制限の値 1から17を指定。範囲外の値は1or17に丸めるのでチェックしない
     * @param genreId                                ジャンルID（ヌルや空文字ならば出力されず、無指定となる）
     * @param sort                                   ソート指定（titleruby_asc/avail_s_asc/avail_e_desc/play_count_desc
     * @param contentsListPerGenreJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(
            final int limit, final int offset, final String filter, final int ageReq,
            final String genreId, final String sort,
            final ContentsListPerGenreJsonParserCallback contentsListPerGenreJsonParserCallback) {
        // 各値が下限以下ならばfalse
        if (limit < 1) {
            return false;
        }
        if (offset < 1) {
            return false;
        }

        //フィルター用の固定値をひとまとめにする
        List<String> filterList = makeStringArry(FILTER_RELEASE, FILTER_TESTA, FILTER_DEMO);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (filterList.indexOf(filter) == -1) {
            //空文字ならば有効なので、それ以外はfalse
            if (!filter.isEmpty()) {
                return false;
            }
        }

        //タイプ用の固定値をひとまとめにする
        List<String> typeList = makeStringArry(TYPE_HIKARI_TV_VOD, TYPE_DTV_VOD,
                TYPE_HIKARI_TV_AND_DTV_VOD);

        //ソート用の固定値をひとまとめにする
        List<String> sortList = makeStringArry(SORT_TITLE_RUBY_ASC, SORT_AVAIL_S_ASC,
                SORT_AVAIL_E_DESC, SORT_PLAY_COUNT_DESC);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (sortList.indexOf(sort) == -1) {
            //空文字ならば有効なので、それ以外はfalse
            if (!sort.isEmpty()) {
                return false;
            }
        }

        //コールバックがヌルならばエラー
        if (contentsListPerGenreJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param limit   取得する最大件数(値は1以上)
     * @param offset  取得位置(値は1以上)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq  年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param genreId ジャンルID
     * @param type    タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vod/指定なしはすべてのVOD）
     * @param sort    ソート指定（titleruby_asc/avail_s_asc/avail_e_desc/play_count_desc
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final int limit, final int offset, final String filter,
                                     final int ageReq, final String genreId, final String type,
                                     final String sort) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        int intAgeReq = ageReq;
        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put(JsonConstants.META_RESPONSE_PAGER_LIMIT, limit);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_OFFSET, offset);
            jsonObject.put(JsonConstants.META_RESPONSE_PAGER, jsonPagerObject);

            //その他
            jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);

            //数字がゼロの場合は無指定と判断して1にする.また17より大きい場合は17に丸める.
            if (intAgeReq < WebApiBasePlala.AGE_LOW_VALUE) {
                intAgeReq = 1;
            } else if (intAgeReq > WebApiBasePlala.AGE_HIGH_VALUE) {
                intAgeReq = 17;
            }

            jsonObject.put(JsonConstants.META_RESPONSE_AGE_REQ, intAgeReq);

            //ヌルや空文字ではないならば、値を出力する
            if (genreId != null && !genreId.isEmpty()) {
                jsonObject.put(JsonConstants.META_RESPONSE_GENRE_ID, genreId);
            }

            //ヌルではないならば、値を出力する
            if (type != null) {
                jsonObject.put(JsonConstants.META_RESPONSE_TYPE, type);
            }

            if (sort != null) {
                jsonObject.put(JsonConstants.META_RESPONSE_SORT, sort);
            }

            //結果の出力
            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
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
}
