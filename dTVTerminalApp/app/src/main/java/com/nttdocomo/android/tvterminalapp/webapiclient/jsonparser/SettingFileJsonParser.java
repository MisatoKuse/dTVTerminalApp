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

    //仮のセッティングファイル名
    private static final String PROVISIONAL_SETTING_JSON_FILE_NAME = "/setting.json";

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
    protected void onPostExecute(final Object s) {
        mSettingFileJsonParserCallback.
                onSettingFileJsonParsed(mSettingFileResponse);
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

        //TODO: セッティングファイルのデータに置き換えるスタブを設置。後で外す事
        String jsonStringBuffer = getSettingFileStub(jsonStr);

        DTVTLogger.debugHttp(jsonStringBuffer);
        mSettingFileResponse = new SettingFileResponse();
        try {
            if (jsonStringBuffer != null) {
                JSONObject jsonObject = new JSONObject(jsonStringBuffer);
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
     * デバッグ用のセッティングファイルの取得.
     * <p>
     * TODO:セッティングファイルの正式な場所が確定した場合は消す事
     *
     * @return セッティングファイル
     */
    private String getSettingFileStub(String sourceString) {
        //デバッグモードか否かで処理を切り替える
        if (BuildConfig.DEBUG) {
            String answerText = "{\n" +
                    "  \"is_stop\":  {\n" +
                    "    \"value\": false,\n" +
                    "    \"description\": \"おしまい\"\n" +
                    "  },\n" +
                    "  \"force_update\": {\n" +
                    "    \"AOS\": \"111\",\n" +
                    "    \"iOS\": \"version\"\n" +
                    "  },\n" +
                    "  \"optional_update\": {\n" +
                    "    \"AOS\": \"222\",\n" +
                    "    \"iOS\": \"version\"\n" +
                    "  }\n" +
                    "}";
            DTVTLogger.debug(answerText);
            return answerText;
        } else {
            //本番ではこの処理自体を消すべきだが、最悪忘れた場合でも正常に動作させるように、
            //読み込みデータをそのまま渡す
            return sourceString;
        }
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