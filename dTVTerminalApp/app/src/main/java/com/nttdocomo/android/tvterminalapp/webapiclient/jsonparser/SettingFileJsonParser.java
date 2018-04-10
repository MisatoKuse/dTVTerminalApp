/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.BuildConfig;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.SettingFileWebClient;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingFileJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendSettingFileResponse";
    private static final String RESPONSE = ". SettingFileResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private final SettingFileWebClient.SettingFileJsonParserCallback
            mSettingFileJsonParserCallback;
    // オブジェクトクラスの定義　
    private SettingFileResponse mSettingFileResponse;

    /**
     * コンストラクタ.
     * <p>
     * //     * @param genreListJsonParserCallback
     */
    public SettingFileJsonParser(final SettingFileWebClient.SettingFileJsonParserCallback
                                         settingFileJsonParserCallback) {
        mSettingFileJsonParserCallback = settingFileJsonParserCallback;
        mSettingFileResponse = new SettingFileResponse();
    }

    @Override
    protected void onPostExecute(final Object response) {
        if(response == null) {
            //ファイルの読み込みに失敗しているならばヌルを返す
            mSettingFileJsonParserCallback.
                    onSettingFileJsonParsed(null);
        } else {
            //読み込みに成功しているので、既に編集済みのクラスを返す
            mSettingFileJsonParserCallback.
                    onSettingFileJsonParsed(mSettingFileResponse);
        }
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        DTVTLogger.start();
        String result = (String) strings[0];
        SettingFileResponse response = settingFileSender(result);
        DTVTLogger.end();
        return response;
    }


    /**
     * ジャンル一覧Jsonデータを解析する.
     *
     * @param jsonStr ジャンル一覧Jsonデータ
     * @return ジャンル一覧取得：正常時レスポンスデータ
     */
    private SettingFileResponse settingFileSender(final String jsonStr) {
        //データがヌルならばそのまま返す
        if(jsonStr == null) {
            return null;
        }

        DTVTLogger.debugHttp(jsonStr);
        mSettingFileResponse = new SettingFileResponse();
        try {
            if (jsonStr != null) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                if (jsonObject != null) {
                    sendSettingFileResponse(jsonObject);
                    return mSettingFileResponse;
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        }
        return null;
    }

    /**
     * 設定ファイルのデータを取得
     *
     * @param jsonObject APIレスポンス Jsonデータ
     */
    private void sendSettingFileResponse(final JSONObject jsonObject) {

        try {
            SettingFileMetaData settingFileMetaData = new SettingFileMetaData();
            //アプリ実行停止情報
            JSONObject isStopData = jsonObject.getJSONObject(
                    JsonConstants.SETTING_FILE_IS_STOP);
            settingFileMetaData.setIsStop(isStopData.getBoolean(
                    JsonConstants.SETTING_FILE_IS_STOP_VALUE));
            settingFileMetaData.setDescription(
                    isStopData.getString(JsonConstants.SETTING_FILE_IS_STOP_DESCRIPTION));

            //アプリ強制アップデート情報
            JSONObject forceUpdateData = jsonObject.getJSONObject(
                    JsonConstants.SETTING_FILE_FOURCE_UPDATE);
            settingFileMetaData.setForceUpdateVersion(forceUpdateData.getString(
                    JsonConstants.SETTING_FILE_FOURCE_UPDATE_AOS));

            //アプリ推奨アップデート情報
            JSONObject optionalUpdateData = jsonObject.getJSONObject(
                    JsonConstants.SETTING_FILE_OPTIONAL_UPDATE);
            settingFileMetaData.setOptionalUpdateVersion(optionalUpdateData.getString(
                    JsonConstants.SETTING_FILE_OPTIONAL_UPDATE_AOS));

            mSettingFileResponse.setSettingFile(settingFileMetaData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}