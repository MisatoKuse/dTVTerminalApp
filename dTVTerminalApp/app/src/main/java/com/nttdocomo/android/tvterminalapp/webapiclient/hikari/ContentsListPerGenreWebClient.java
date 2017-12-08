/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContentsListPerGenreWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    //ジャンル毎コンテンツ一覧

    /**
     * コールバック
     */
    public interface ContentsListPerGenreJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param contentsListPerGenre JSONパース後のデータ
         */
        void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre);
    }

    //コールバックのインスタンス
    private ContentsListPerGenreJsonParserCallback
            mContentsListPerGenreJsonParserCallback;

    /**
     * 通信成功時のコールバック
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //拡張情報付きでパースを行う
        VideoRankJsonParser videoRankJsonParser = new VideoRankJsonParser(
                mContentsListPerGenreJsonParserCallback,returnCode.extraData);

        //JSONをパースして、データを返す
        videoRankJsonParser.execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        mContentsListPerGenreJsonParserCallback.onContentsListPerGenreJsonParsed(null);
    }


    /**
     * ジャンル毎コンテンツ一覧取得
     *
     * @param limit                                  取得する最大件数(値は1以上)
     * @param offset                                 取得位置(値は1以上)
     * @param filter                                 フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                                 年齢設定値1-17（ゼロの場合は1扱い）
     * @param genreId                                ジャンルID（ヌルや空文字ならば出力されず、無指定となる）
     * @param type                                   タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vod/指定なしはすべてのVOD）
     * @param sort                                   ソート指定（titleruby_asc/avail_s_asc/avail_e_desc/play_count_desc
     * @param contentsListPerGenreJsonParserCallback コールバック
     * @return パラメータエラー等ならばfalse
     */
    public boolean getContentsListPerGenreApi(int limit, int offset, String filter, int ageReq,
                                              String genreId, String type, String sort,
                                              ContentsListPerGenreJsonParserCallback
                                                      contentsListPerGenreJsonParserCallback) {
        //パラメーターのチェック(genreIdはヌルを受け付けるので、チェックしない)
        if (!checkNormalParameter(limit, offset, filter, ageReq, genreId, type, sort,
                contentsListPerGenreJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックを呼べるようにする
        mContentsListPerGenreJsonParserCallback = contentsListPerGenreJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(limit, offset, filter, ageReq, genreId, type, sort);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //拡張情報を追加する
        Bundle bundle = new Bundle();
        bundle.putString("genreId",genreId);

        //ジャンル毎コンテンツ一覧取得を呼び出す
        openUrlWithExtraData(UrlConstants.WebApiUrl.CONTENTS_LIST_PER_GENRE_WEB_CLIENT,
                sendParameter, this,bundle);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param limit                                  取得する最大件数(値は1以上)
     * @param offset                                 取得位置(値は1以上)
     * @param filter                                 フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                                 年齢設定値1-17（ゼロの場合は1扱い）
     * @param genreId                                ジャンルID（ヌルや空文字ならば出力されず、無指定となる）
     * @param type                                   タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vod/指定なしはすべてのVOD）
     * @param sort                                   ソート指定（titleruby_asc/avail_s_asc/avail_e_desc/play_count_desc
     * @param contentsListPerGenreJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int limit, int offset, String filter, int ageReq,
                                         String genreId, String type, String sort,
                                         ContentsListPerGenreJsonParserCallback
                                                 contentsListPerGenreJsonParserCallback) {
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

        //年齢情報の件0から17までの間以外はエラー
        if (ageReq < 1 || ageReq > 17) {
            return false;
        }

        //タイプ用の固定値をひとまとめにする
        List<String> typeList = makeStringArry(TYPE_HIKARI_TV_VOD, TYPE_DTV_VOD,
                TYPE_HIKARI_TV_AND_DTV_VOD);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (typeList.indexOf(type) == -1) {
            //空文字ならば有効なので、それ以外はfalse
            if (!type.isEmpty()) {
                return false;
            }
        }

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
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param limit   取得する最大件数(値は1以上)
     * @param offset  取得位置(値は1以上)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq  年齢設定値1-17（ゼロの場合は1扱い）
     * @param genreId ジャンルID
     * @param type    タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vod/指定なしはすべてのVOD）
     * @param sort    ソート指定（titleruby_asc/avail_s_asc/avail_e_desc/play_count_desc
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int limit, int offset, String filter, int ageReq,
                                     String genreId, String type, String sort) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put(JsonContents.META_RESPONSE_PAGER_LIMIT, limit);
            jsonPagerObject.put(JsonContents.META_RESPONSE_OFFSET, offset);
            jsonObject.put(JsonContents.META_RESPONSE_PAGER, jsonPagerObject);

            //その他
            jsonObject.put(JsonContents.META_RESPONSE_FILTER, filter);

            //数字がゼロの場合は無指定と判断して1にする
            if (ageReq == 0) {
                ageReq = 1;
            }

            jsonObject.put(JsonContents.META_RESPONSE_AGE_REQ, ageReq);

            //ヌルや空文字ではないならば、値を出力する
            if (genreId != null && !genreId.isEmpty()) {
                jsonObject.put(JsonContents.META_RESPONSE_GENRE_ID, genreId);
            }

            //ヌルではないならば、値を出力する
            if (type != null) {
                jsonObject.put(JsonContents.META_RESPONSE_TYPE, type);
            }

            if (sort != null) {
                jsonObject.put(JsonContents.META_RESPONSE_SORT, sort);
            }

            //結果の出力
            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
