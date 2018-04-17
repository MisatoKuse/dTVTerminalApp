/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VideoRankJsonParser extends AsyncTask<Object, Object, Object> {

    private ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback mContentsListPerGenreJsonParserCallback;
    // オブジェクトクラスの定義
    private VideoRankList mVideoRankList;

    public static final String[] PAGER_PARA = {JsonConstants.META_RESPONSE_PAGER_LIMIT, JsonConstants.META_RESPONSE_OFFSET,
            JsonConstants.META_RESPONSE_COUNT, JsonConstants.META_RESPONSE_TOTAL};

    /**
     * 拡張情報.
     **/
    private Bundle mExtraData = null;

    /**
     * リクエストジャンル.
     */
    private String mGenreId = "";

    /**
     * コンストラクタ.
     *
     * @param mContentsListPerGenreJsonParserCallback
     */
    public VideoRankJsonParser(final ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback mContentsListPerGenreJsonParserCallback) {
        this.mContentsListPerGenreJsonParserCallback = mContentsListPerGenreJsonParserCallback;
    }

    /**
     * 拡張情報付きコンストラクタ.
     * @param contentsListPerGenreJsonParserCallback コールバック用
     * @param extraDataSrc                           拡張情報
     * @param genreId リクエストしたジャンルID
     */
    public VideoRankJsonParser(final ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback
                                       contentsListPerGenreJsonParserCallback, final Bundle extraDataSrc, String genreId) {
        this.mContentsListPerGenreJsonParserCallback = contentsListPerGenreJsonParserCallback;
        //拡張情報の追加
        mExtraData = extraDataSrc;
        mGenreId = genreId;
    }

    @Override
    protected void onPostExecute(final Object object) {
        //拡張情報が存在すれば、入れ込む
        List<VideoRankList> rankLists = (List<VideoRankList>) object;
        if (mExtraData != null) {
            for (VideoRankList rankList : rankLists) {
                rankList.setExtraData(mExtraData);
            }
        }

        mContentsListPerGenreJsonParserCallback.onContentsListPerGenreJsonParsed((List<VideoRankList>) object, mGenreId);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        List<VideoRankList> resultList = VideoRankListSender(result);
        return resultList;
    }

    /**
     * ジャンル毎一覧Jsonデータを解析する.
     *
     * @param jsonStr 　String形式のJSONデータ
     * @return List<VideoRankList> ObjectクラスをList形式で返却
     */
    public List<VideoRankList> VideoRankListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mVideoRankList = new VideoRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendVrList(arrayList);
            }
            List<VideoRankList> vrList = Arrays.asList(mVideoRankList);
            return vrList;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに格納.
     *
     * @param jsonObj ステータスを含んだJSONオブジェクト
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                map.put(JsonConstants.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);

                for (String pagerBuffer : PAGER_PARA) {
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }

            if (mVideoRankList != null) {
                mVideoRankList.setVrMap(map);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツリストをList<HashMap>の形式でObjectクラスへ格納する.
     *
     * @param arrayList
     */
    public void sendVrList(final JSONArray arrayList) {
        try {
            List<HashMap<String, String>> vrList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> vrListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonConstants.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonConstants.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonConstants.PUINF_PARA) {
                                //書き込み用項目名の作成
                                StringBuilder stringBuffer = new StringBuilder();
                                stringBuffer.append(JsonConstants.META_RESPONSE_PUINF);
                                stringBuffer.append(JsonConstants.UNDER_LINE);
                                stringBuffer.append(puinfBuffer);
                                String para = puinfObj.getString(puinfBuffer);
                                vrListMap.put(JsonConstants.META_RESPONSE_PUINF + JsonConstants.UNDER_LINE + puinfBuffer, para);
                            }
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            vrListMap.put(listBuffer, para);
                        }
                    }
                }
                vrList.add(vrListMap);
            }

            if (mVideoRankList != null) {
                mVideoRankList.setVrList(vrList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}