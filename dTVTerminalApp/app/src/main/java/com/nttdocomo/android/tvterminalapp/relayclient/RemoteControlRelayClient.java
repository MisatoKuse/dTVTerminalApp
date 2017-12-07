/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * キーコードをSTBへ送信する
 */
public class RemoteControlRelayClient {

    private static final int KEYCODE_UNKNOWN = 0;
    // TODO: STBがDMSとして動作しないためDMS機能が実装されるまで固定IPを使用する
    private String ｍRemoteHost = "192.168.11.23";

    // 受信キーコード名に対応する STBキーコード
    private static final Map<Integer, String> keyCodeNameMap = new HashMap<Integer, String>() {
        {
            put(R.id.keycode_dpad_up, "KEYCODE_DPAD_UP");
        }         // カーソル (上下左右)

        {
            put(R.id.keycode_dpad_down, "KEYCODE_DPAD_DOWN");
        }

        {
            put(R.id.keycode_dpad_left, "KEYCODE_DPAD_LEFT");
        }

        {
            put(R.id.keycode_dpad_right, "KEYCODE_DPAD_RIGHT");
        }

        {
            put(R.id.keycode_dpad_home, "KEYCODE_HOME");
        }       // ホーム

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_0");
        }    // チャンネル (1～12) ※ チャンネル (10)は未定義

        {
            put(R.id.keycode_1, "KEYCODE_1");
        }

        {
            put(R.id.keycode_2, "KEYCODE_2");
        }

        {
            put(R.id.keycode_3, "KEYCODE_3");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_4");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_5");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_6");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_7");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_8");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_9");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_11");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_12");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_TV_TERRESTRIAL_DIGITAL");
        }  // 地デジ

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_TV_SATELLITE_BS");
        }  // BS

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_TV");
        } // IPTV

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_GUIDE");
        }  // 番組表

        {
            put(R.id.keycode_dpad_center, "KEYCODE_DPAD_CENTER");
        }  // 決定

        {
            put(R.id.keycode_dpad_back, "KEYCODE_BACK");
        }  // 戻る

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_PLAY_PAUSE");
        } // 再生/停止

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_SKIP_BACKWARD");
        }  // カラー (青)/10秒戻し

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_REWIND");
        }  // カラー (赤)/巻き戻し

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_FAST_FORWARD");
        }  // カラー (緑)/早送り

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_SKIP_FORWARD");
        }  // カラー (黄)/30秒送り

        {
            put(R.id.keycode_channel_up, "KEYCODE_CHANNEL_UP");
        }  // チャンネル (上下)

        {
            put(R.id.keycode_channel_down, "KEYCODE_CHANNEL_DOWN");
        }

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_INFO");
        }  // お知らせ

        {
            put(KEYCODE_UNKNOWN, "KEYCODE_TV_DATA_SERVICE");
        }  // dデータ
    };
    //    private Context mContext;
    private String mKeycodeName;

    // シングルトン
    private static RemoteControlRelayClient mInstance = new RemoteControlRelayClient();

    /**
     * アプリ起動要求種別
     */
    public enum STB_APPLICATION_TYPES {
        // dTV
        DTV,
        // dアニメストア
        DANIMESTORE,
        // dTVチャンネル
        DTVCHANNEL
    }

    // アプリ起動要求に対応するアプリ名
    private static final String STB_APPLICATION_DTV = "dTV";  // dTV
    private static final String STB_APPLICATION_DANIMESTORE = "dANIMESTORE";  // dアニメストア
    private static final String STB_APPLICATION_DTVCHANNEL = "dTVCHANNEL";  // dTVチャンネル
    //
    private static final String RERAY_COMMAND = "COMMAND";
    private static final String RERAY_COMMAND_TITLE_DETAIL = "TITLE_DETAIL";
    private static final String RERAY_COMMAND_APPLICATION_ID = "APP_ID";
    private static final String RERAY_COMMAND_CONTENTS_ID = "CONTENTS_ID";
    private static final String RERAY_RESULT = "RESULT";
    private static final String RERAY_RESULT_OK = "0";
    private static final String RERAY_RESULT_ERROR = "1";
    private static final String RERAY_RESULT_MESSAGE = "MESSAGE";


    // アプリ起動要求種別に対応するするアプリ名シンボル
    private static final Map<STB_APPLICATION_TYPES, String> mStbApplicationSymbolMap = new HashMap<STB_APPLICATION_TYPES, String>() {
        {
            put(STB_APPLICATION_TYPES.DTV, STB_APPLICATION_DTV);    // dTV
        }

        {
            put(STB_APPLICATION_TYPES.DANIMESTORE, STB_APPLICATION_DANIMESTORE);    // dアニメストア
        }

        {
            put(STB_APPLICATION_TYPES.DTVCHANNEL, STB_APPLICATION_DTVCHANNEL);  // dTVチャンネル
        }
    };

    /**
     * シングルトン
     */
    private RemoteControlRelayClient() {
    }

    /**
     * シングルトン・インスタンス
     *
     * @return mInstance
     */
    public static RemoteControlRelayClient getInstance() {
        return mInstance;
    }

    /**
     * キーコード名をキーコードに変換する
     *
     * @param keycodeRid キーコードR.id
     * @return キーコード名（キーコードがない場合は null）
     */
    private String convertKeycode(int keycodeRid) {
        String keyCodeName = null;
        if (keyCodeNameMap.containsKey(keycodeRid)) {
            keyCodeName = keyCodeNameMap.get(keycodeRid);
        }
        return keyCodeName;
    }

    /**
     * キーコードをSTBへ送信する
     *
     * @param keycodeRid
     */
    public void sendKeycode(int keycodeRid) {
        mKeycodeName = convertKeycode(keycodeRid);
        if (mKeycodeName != null) {
            // キーコード送信スレッドを開始
            new Thread(new KeycodeRerayTask(mKeycodeName)).start();
        }
    }

    /**
     * キーコードをSTBへ送信するスレッド
     */
    private class KeycodeRerayTask implements Runnable {
        private String mSendKeycodeName;

        public KeycodeRerayTask(String keycodeName) {
            mSendKeycodeName = keycodeName;
        }

        /**
         * キーコードをSTBへ送信する
         */
        @Override
        public void run() {
            StbConnectRelayClient stbDatagram = StbConnectRelayClient.getInstance();  // Socket通信
            stbDatagram.setRemoteIp(ｍRemoteHost);
            if (mSendKeycodeName != null) {
                stbDatagram.sendDatagram(mSendKeycodeName);
            }
        }
    }

    /**
     * アプリ起動要求を受信してインテントをSTBへ送信する
     *
     * @param applicationType
     * @param contentsId
     * @return
     */
    public boolean startApplicationRequest(STB_APPLICATION_TYPES applicationType, String contentsId) {
        String applicationId = getApplicationId(applicationType);
        String requestParam;

        if (applicationId != null && contentsId != null) {
            requestParam = setTitleDetailRequest(applicationId, contentsId);
            if (requestParam != null) {
                // アプリ起動要求を受信してインテントをSTBへ送信する
                sendStartApplicationRequest(requestParam);
                return true;
            }
        }
        return false;
    }

    /**
     * アプリ起動要求種別に対応するアプリID
     *
     * @param applicationType
     * @return アプリ起動要求種別に対応するアプリID（アプリ起動要求種別が不明の場合は null）
     */
    private String getApplicationId(STB_APPLICATION_TYPES applicationType) {
        String applicationId = null;
        if (mStbApplicationSymbolMap.containsKey(applicationType)) {
            applicationId = mStbApplicationSymbolMap.get(applicationType);
        }
        return applicationId;
    }

    /**
     * アプリ起動要求をSTBへ送信するスレッド
     */
    private class StartApplicationRequestTask implements Runnable {

        private String mRequestParam;

        public StartApplicationRequestTask(String requestParam) {
            mRequestParam = requestParam;
        }

        /**
         * アプリ起動要求をSTBへ送信して処理結果応答を取得する
         */
        @Override
        public void run() {
            StbConnectRelayClient stbConnection = StbConnectRelayClient.getInstance();  // Socket通信
            String recvData;
            String message = null;

            if (mRequestParam != null) {
                stbConnection.setRemoteIp(ｍRemoteHost);
                // アプリ起動要求をSTBへ送信して処理結果応答を取得する
                if (stbConnection.connect()) {
                    if (stbConnection.send(mRequestParam)) {
                        recvData = stbConnection.recv();
                        message = getRecvResult(recvData);
                        if (message != null) {
                            DTVTLogger.debug(message);
                        }
                    }
                    stbConnection.disconnect();
                } else {
                    DTVTLogger.debug("STBとの接続に失敗しました。");
                }
            }
        }

        private String getRecvResult(String recvResult) {
            JSONObject recvJson = new JSONObject();
            String message = null;
            try {
                if (recvResult != null) {
                    recvJson = new JSONObject(recvResult);
                }
                if (!RERAY_RESULT_OK.equals(recvJson.get(RERAY_RESULT).toString())) {
                    message = recvJson.get(RERAY_RESULT_MESSAGE).toString();
                }
            } catch (JSONException e) {
                DTVTLogger.debug(e);
            }
            return message;
        }
    }

    /**
     * アプリ起動要求送信スレッドを開始
     */
    private void sendStartApplicationRequest(String requestParam) {
        new Thread(new StartApplicationRequestTask(requestParam)).start();
    }

    /**
     * アプリ起動要求のメッセージ（JSON形式）を作成する
     *
     * @param applicationId
     * @param contentsId
     * @return
     */
    private static String setTitleDetailRequest(String applicationId, String contentsId) {
        JSONObject requestJson = new JSONObject();
        String jsonStr = null;
        try {
            requestJson.put(RERAY_COMMAND, RERAY_COMMAND_TITLE_DETAIL);
            requestJson.put(RERAY_COMMAND_APPLICATION_ID, applicationId);
            requestJson.put(RERAY_COMMAND_CONTENTS_ID, contentsId);
            jsonStr = requestJson.toString();
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return jsonStr;
    }

    /**
     * Socket通信／データグラム送信の送信先のIPアドレスを設定
     * TODO: デバッグ用
     *
     * @param remoteIp
     */
    public void setDebugRemoteIp(String remoteIp) {
        ｍRemoteHost = remoteIp;
    }
}
