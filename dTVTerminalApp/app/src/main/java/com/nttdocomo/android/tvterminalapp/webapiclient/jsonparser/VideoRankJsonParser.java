/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
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

    public static final String[] PAGER_PARA = {JsonContents.META_RESPONSE_PAGER_LIMIT, JsonContents.META_RESPONSE_OFFSET,
            JsonContents.META_RESPONSE_COUNT, JsonContents.META_RESPONSE_TOTAL};

    /**
     * 拡張情報
     **/
    Bundle mExtraData = null;

    /**
     * コンストラクタ
     *
     * @param mContentsListPerGenreJsonParserCallback
     */
    public VideoRankJsonParser(ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback mContentsListPerGenreJsonParserCallback) {
        this.mContentsListPerGenreJsonParserCallback = mContentsListPerGenreJsonParserCallback;
    }

    /**
     * 拡張情報付きコンストラクタ
     * @param contentsListPerGenreJsonParserCallback コールバック用
     * @param extraDataSrc                           拡張情報
     */
    public VideoRankJsonParser(ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback
                                       contentsListPerGenreJsonParserCallback, Bundle extraDataSrc) {
        this.mContentsListPerGenreJsonParserCallback = contentsListPerGenreJsonParserCallback;

        //拡張情報の追加
        mExtraData = extraDataSrc;
    }

    @Override
    protected void onPostExecute(Object s) {
        //拡張情報が存在すれば、入れ込む
        List<VideoRankList> rankLists = (List<VideoRankList>) s;
        if (mExtraData != null) {
            for (VideoRankList rankList : rankLists) {
                rankList.setExtraData(mExtraData);
            }
        }

        mContentsListPerGenreJsonParserCallback.onContentsListPerGenreJsonParsed((List<VideoRankList>) s);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<VideoRankList> resultList = VideoRankListSender(result);
        return resultList;
    }

    /**
     * ジャンル毎一覧Jsonデータを解析する
     *
     * @param jsonStr 　String形式のJSONデータ
     * @return List<VideoRankList> ObjectクラスをList形式で返却
     */
    public List<VideoRankList> VideoRankListSender(String jsonStr) {

        mVideoRankList = new VideoRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
                sendVrList(arrayList);
            }
            List<VideoRankList> vrList = Arrays.asList(mVideoRankList);
            return vrList;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに格納
     *
     * @param jsonObj ステータスを含んだJSONオブジェクト
     */
    private void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                map.put(JsonContents.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonContents.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonContents.META_RESPONSE_PAGER);

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
     * コンテンツリストをList<HashMap>の形式でObjectクラスへ格納する
     *
     * @param arrayList
     */
    public void sendVrList(JSONArray arrayList) {
        try {
            List<HashMap<String, String>> vrList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> vrListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonContents.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonContents.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonContents.PUINF_PARA) {
                                String para = puinfObj.getString(puinfBuffer);
                                vrListMap.put(JsonContents.META_RESPONSE_PUINF + JsonContents.UNDER_LINE + puinfBuffer, para);
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