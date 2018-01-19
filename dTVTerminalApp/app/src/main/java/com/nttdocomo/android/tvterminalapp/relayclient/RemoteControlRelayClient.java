/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * キーコードをSTBへ送信する.
 *
 */
public class RemoteControlRelayClient {

    private static final int KEYCODE_UNKNOWN = 0;
    // TODO: STBがDMSとして動作しないためDMS機能が実装されるまで固定IPを使用する
    private String ｍRemoteHost = "192.168.11.23";

    // 受信キーコード名に対応する STBキーコード
    private static final Map<Integer, String> keyCodeNameMap = new HashMap<Integer, String>() {
        {
            put(R.id.remote_controller_bt_up, "KEYCODE_DPAD_UP");
        }         // カーソル (上下左右)

        {
            put(R.id.remote_controller_bt_down, "KEYCODE_DPAD_DOWN");
        }

        {
            put(R.id.remote_controller_bt_left, "KEYCODE_DPAD_LEFT");
        }

        {
            put(R.id.remote_controller_bt_right, "KEYCODE_DPAD_RIGHT");
        }

        {
            put(R.id.remote_controller_bt_toHome, "KEYCODE_HOME");
        }       // ホーム

        {
            put(R.id.remote_controller_bt_ten, "KEYCODE_0");
        }    // チャンネル (1～12) ※ チャンネル (10)は仕様書より KEYCODE_0となる

        {
            put(R.id.remote_controller_bt_one, "KEYCODE_1");
        }

        {
            put(R.id.remote_controller_bt_two, "KEYCODE_2");
        }

        {
            put(R.id.remote_controller_bt_three, "KEYCODE_3");
        }

        {
            put(R.id.remote_controller_bt_four, "KEYCODE_4");
        }

        {
            put(R.id.remote_controller_bt_five, "KEYCODE_5");
        }

        {
            put(R.id.remote_controller_bt_six, "KEYCODE_6");
        }

        {
            put(R.id.remote_controller_bt_seven, "KEYCODE_7");
        }

        {
            put(R.id.remote_controller_bt_eight, "KEYCODE_8");
        }

        {
            put(R.id.remote_controller_bt_nine, "KEYCODE_9");
        }

        {
            put(R.id.remote_controller_bt_eleven, "KEYCODE_11");
        }

        {
            put(R.id.remote_controller_bt_twelve, "KEYCODE_12");
        }

        {
            put(R.id.remote_controller_bt_degital, "KEYCODE_TV_TERRESTRIAL_DIGITAL");
        }  // 地デジ

        {
            put(R.id.remote_controller_bt_bs, "KEYCODE_TV_SATELLITE_BS");
        }  // BS

        {
            put(R.id.remote_controller_bt_iptv, "KEYCODE_TV");
        } // IPTV

        {
            put(R.id.remote_controller_bt_tv_program, "KEYCODE_GUIDE");
        }  // 番組表

        {
            put(R.id.remote_controller_bt_decide, "KEYCODE_DPAD_CENTER");
        }  // 決定

        {
            put(R.id.remote_controller_bt_back, "KEYCODE_BACK");
        }  // 戻る

        {
            put(R.id.remote_controller_iv_playOrStop, "KEYCODE_MEDIA_PLAY_PAUSE");
        } // 再生/停止

        {
            put(R.id.remote_controller_iv_blue, "KEYCODE_MEDIA_SKIP_BACKWARD");
        }  // カラー (青)/10秒戻し

        {
            put(R.id.remote_controller_iv_red, "KEYCODE_MEDIA_REWIND");
        }  // カラー (赤)/巻き戻し

        {
            put(R.id.remote_controller_iv_green, "KEYCODE_MEDIA_FAST_FORWARD");
        }  // カラー (緑)/早送り

        {
            put(R.id.remote_controller_iv_yellow, "KEYCODE_MEDIA_SKIP_FORWARD");
        }  // カラー (黄)/30秒送り

        {
            put(R.id.remote_controller_bt_channel_plus, "KEYCODE_CHANNEL_UP");
        }  // チャンネル (上下)

        {
            put(R.id.remote_controller_bt_channel_minus, "KEYCODE_CHANNEL_DOWN");
        }

        {
            put(R.id.remote_controller_bt_notice, "KEYCODE_INFO");
        }  // お知らせ

        {
            put(R.id.remote_controller_bt_ddata, "KEYCODE_TV_DATA_SERVICE");
        }  // dデータ

        {
            put(R.id.remote_controller_iv_power, "KEYCODE_POWER");
        }  // 電源(KEYCODE_STB_POWERの可能性あり)

        {
            put(R.id.remote_controller_bt_record_list, "KEYCODE_REC_LIST");
        }  // 録画リスト // 録画リスト
    };

    // シングルトン
    private static RemoteControlRelayClient mInstance = new RemoteControlRelayClient();

    /**
     * アプリ起動要求種別.
     */
    public enum STB_APPLICATION_TYPES {
        // 初期値
        UNKNOWN,
        // dTV
        DTV,
        // dアニメストア
        DANIMESTORE,
        // dTVチャンネル
        DTVCHANNEL,
        // ひかりTV
        HIKARITV,
        // ダ・ゾーン
        DAZN
    }

    /**
     * リクエストコマンド種別.
     */
    public enum STB_REQUEST_COMMAND_TYPES {
        // 受信タイムアウト時
        COMMAND_UNKNOWN,
        // ユーザ登録チェック
        IS_USER_ACCOUNT_EXIST,
        // サービスアプリ：タイトル詳細表示起動要求
        TITLE_DETAIL,
        // サービスアプリ：起動要求
        START_APPLICATION
    }

    // ハッシュアルゴリズム指定
    private static final String HASH_ALGORITHME = "SHA-256";
    // アプリ起動要求に対応するアプリ名
    private static final String STB_APPLICATION_DTV = "dTV";  // dTV
    private static final String STB_APPLICATION_DANIMESTORE = "dANIMESTORE";  // dアニメストア
    private static final String STB_APPLICATION_DTVCHANNEL = "dTVCHANNEL";  // dTVチャンネル
    private static final String STB_APPLICATION_HIKARITV = "HIKARITV";  // ひかりTV
    private static final String STB_APPLICATION_DAZN = "DAZN";  // ダ・ゾーン
    // 中継アプリクライアントが送信するアプリ起動要求のメッセージ定数
    private static final String RELAY_COMMAND = "COMMAND";
    private static final String RELAY_COMMAND_TITLE_DETAIL = "TITLE_DETAIL";
    private static final String RELAY_COMMAND_IS_USER_ACCOUNT_EXIST = "IS_USER_ACCOUNT_EXIST";
    private static final String RELAY_COMMAND_START_APPLICATION = "START_APPLICATION";
    private static final String RELAY_COMMAND_UNKNOWN = "COMMAND_UNKNOWN";
    private static final String RELAY_COMMAND_APPLICATION_ID = "APP_ID";
    private static final String RELAY_COMMAND_REQUEST_COMMAND = "REQUEST_COMMAND";
    private static final String RELAY_COMMAND_USER_ID = "USER_ID";
    private static final String RELAY_COMMAND_CONTENTS_ID = "CONTENTS_ID";
    private static final String RELAY_RESULT = "RESULT";
    private static final String RELAY_RESULT_OK = "OK";
    private static final String RELAY_RESULT_ERROR = "ERROR";
    //    private static final String RELAY_RESULT_MESSAGE = "MESSAGE";
    // 中継アプリクライアントが送信するキーコードのメッセージ定数
    private static final String RELAY_KEYEVENT = "KEYEVENT";
    private static final String RELAY_KEYEVENT_ACTION = "ACTION";
    private static final String RELAY_KEYEVENT_ACTION_DOWN = "DOWN";
    private static final String RELAY_KEYEVENT_ACTION_UP = "UP";
    private static final String RELAY_KEYEVENT_ACTION_CANCELED = "CANCELED";
    private static final String RELAY_KEYEVENT_ACTION_CANCELED_TRUE = "TRUE";
    // 中継アプリのエラーコード定数
    private static final String RELAY_RESULT_ERROR_CODE = "ERROR_CODE";
    private static final String RELAY_RESULT_INTERNAL_ERROR = "INTERNAL_ERROR";
    private static final String RELAY_RESULT_APPLICATION_NOT_INSTALL = "APPLICATION_NOT_INSTALL";
    private static final String RELAY_RESULT_APPLICATION_ID_NOTEXIST = "APPLICATION_ID_NOTEXIST";
    private static final String RELAY_RESULT_APPLICATION_START_FAILED = "APPLICATION_START_FAILED";
    private static final String RELAY_RESULT_VERSION_CODE_INCOMPATIBLE = "VERSION_CODE_INCOMPATIBLE";
    private static final String RELAY_RESULT_CONTENTS_ID_NOTEXIST = "CONTENTS_ID_NOTEXIST";

    private static final String RELAY_RESULT_NOT_REGISTERED_SERVICE = "NOT_REGISTERED_SERVICE";
    private static final String RELAY_RESULT_UNREGISTERED_USER_ID = "UNREGISTERED_USER_ID";
    private static final String RELAY_RESULT_CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";
    private static final String RELAY_RESULT_RELAY_SERVICE_BUSY = "SERVICE_BUSY";
    private static final String RELAY_RESULT_USER_INVALID_STATE = "USER_INVALID_STATE";

    // アプリ起動要求種別に対応するアプリ名シンボル
    private static final Map<STB_APPLICATION_TYPES, String> mStbApplicationSymbolMap = new HashMap<STB_APPLICATION_TYPES, String>() {
        {
            put(STB_APPLICATION_TYPES.DTV, STB_APPLICATION_DTV);    // dTV
            put(STB_APPLICATION_TYPES.DANIMESTORE, STB_APPLICATION_DANIMESTORE);    // dアニメストア
            put(STB_APPLICATION_TYPES.DTVCHANNEL, STB_APPLICATION_DTVCHANNEL);  // dTVチャンネル
            put(STB_APPLICATION_TYPES.HIKARITV, STB_APPLICATION_HIKARITV);    // ひかりTV
            put(STB_APPLICATION_TYPES.DAZN, STB_APPLICATION_DAZN);    // ダ・ゾーン
        }
    };

    // キーイベントアクション
    private static final Map<Integer, String> mKeyeventActionMap = new HashMap<Integer, String>() {
        {
            put(KeyEvent.ACTION_DOWN, RELAY_KEYEVENT_ACTION_DOWN);
            put(KeyEvent.ACTION_UP, RELAY_KEYEVENT_ACTION_UP);
        }
    };

    // 処理結果応答を通知するハンドラー
    private Handler mHandler = null;

    // 最後にSTBへアプリケーション要求したシステム時刻からの経過時間
    private long mRequestStbElapsedTime = 0;

    /**
     * 最後にSTBへアプリケーション要求したシステム時刻からの経過時間.
     * @return
     */
    private long getRequestStbElapsedTime() {
        return SystemClock.elapsedRealtime() - mRequestStbElapsedTime;
    }

    /**
     * 最後にSTBへアプリケーション要求したシステム時刻からの経過時間.
     * @return
     */
    private boolean isRequestStbElapsedTime() {
        if (getRequestStbElapsedTime() > TcpClient.SEND_RECV_TIMEOUT) {
            DTVTLogger.debug(String.format("RequestStbElapsedTime [%d] exceeded TCP timeout :[%d]", getRequestStbElapsedTime(), TcpClient.SEND_RECV_TIMEOUT));
            return true;
        }
        return false;
    }

    /**
     * 最後にSTBへアプリケーション要求したシステム時刻.
     */
    private void setRequestStbElapsedTime() {
        mRequestStbElapsedTime = SystemClock.elapsedRealtime();
        DTVTLogger.debug(String.format("RequestStbElapsedTime:[%d]", mRequestStbElapsedTime));
    }

    /**
     * 最後にSTBへアプリケーション要求した時刻のリセット
     */
    private void resetRequestStbElapsedTime() {
        mRequestStbElapsedTime = 0;
        DTVTLogger.debug(String.format("resetRequestStbElapsedTime"));
    }

    /**
     * シングルトン.
     */
    private RemoteControlRelayClient() {
    }

    /**
     * シングルトン・インスタンス.
     *
     * @return mInstance
     */
    public static RemoteControlRelayClient getInstance() {
        return mInstance;
    }

    /**
     * 処理結果応答を通知するハンドラーの設定.
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * 処理結果応答を通知するハンドラーの設定の解除.
     */
    public void resetHandler() {
        mHandler = null;
    }

    /**
     * キーコード名をキーコードに変換する.
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
     * キーコードをSTBへ送信する.
     *
     * @param keycodeRid
     */
    public void sendKeycode(int keycodeRid, int action, boolean canceled) {
        String keycode = convertKeycode(keycodeRid);
        if (keycode != null && mKeyeventActionMap.containsKey(action)) {
            // キーコード送信スレッドを開始
            new Thread(new KeycodeRerayTask(keycode, action, canceled)).start();
        }
    }

    /**
     * 処理結果応答を通知する情報.
     */
    public class ResponseMessage {
        public static final int RELAY_RESULT_OK = 0;
        public static final int RELAY_RESULT_ERROR = 1;
        public static final int RELAY_RESULT_SUCCESS = 0;
        public static final int RELAY_RESULT_INTERNAL_ERROR = 11;
        public static final int RELAY_RESULT_APPLICATION_NOT_INSTALL = 12;
        public static final int RELAY_RESULT_APPLICATION_ID_NOTEXIST = 13;
        public static final int RELAY_RESULT_APPLICATION_START_FAILED = 14;
        public static final int RELAY_RESULT_VERSION_CODE_INCOMPATIBLE = 15;

        public static final int RELAY_RESULT_NOT_REGISTERED_SERVICE = 16;
        public static final int RELAY_RESULT_UNREGISTERED_USER_ID = 17;
        public static final int RELAY_RESULT_CONNECTION_TIMEOUT = 18;
        public static final int RELAY_RESULT_RELAY_SERVICE_BUSY = 19;
        public static final int RELAY_RESULT_USER_INVALID_STATE = 20;
        public static final int RELAY_RESULT_DISTINATION_UNREACHABLE = 21;

        private int mResult = RELAY_RESULT_OK;
        private int mResultCode = RELAY_RESULT_SUCCESS;
        private STB_APPLICATION_TYPES mApplicationTypes = STB_APPLICATION_TYPES.UNKNOWN;
        private STB_REQUEST_COMMAND_TYPES mRequestCommandTypes = STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN;

        // 応答結果の変換
        public final Map<String, Integer> mResultMap = new HashMap<String, Integer>() {
            {
                put(RemoteControlRelayClient.RELAY_RESULT_OK, RELAY_RESULT_OK);
                put(RemoteControlRelayClient.RELAY_RESULT_ERROR, RELAY_RESULT_ERROR);
            }
        };

        // 応答結果コードの変換
        public final Map<String, Integer> mResultCodeMap = new HashMap<String, Integer>() {
            {
                put(RemoteControlRelayClient.RELAY_RESULT_INTERNAL_ERROR, RELAY_RESULT_INTERNAL_ERROR);
                put(RemoteControlRelayClient.RELAY_RESULT_APPLICATION_NOT_INSTALL, RELAY_RESULT_APPLICATION_NOT_INSTALL);
                put(RemoteControlRelayClient.RELAY_RESULT_APPLICATION_ID_NOTEXIST, RELAY_RESULT_APPLICATION_ID_NOTEXIST);
                put(RemoteControlRelayClient.RELAY_RESULT_APPLICATION_START_FAILED, RELAY_RESULT_APPLICATION_START_FAILED);
                put(RemoteControlRelayClient.RELAY_RESULT_VERSION_CODE_INCOMPATIBLE, RELAY_RESULT_VERSION_CODE_INCOMPATIBLE);

                put(RemoteControlRelayClient.RELAY_RESULT_NOT_REGISTERED_SERVICE, RELAY_RESULT_NOT_REGISTERED_SERVICE);
                put(RemoteControlRelayClient.RELAY_RESULT_UNREGISTERED_USER_ID, RELAY_RESULT_UNREGISTERED_USER_ID);
                put(RemoteControlRelayClient.RELAY_RESULT_CONNECTION_TIMEOUT, RELAY_RESULT_CONNECTION_TIMEOUT);
                put(RemoteControlRelayClient.RELAY_RESULT_RELAY_SERVICE_BUSY, RELAY_RESULT_RELAY_SERVICE_BUSY);
                put(RemoteControlRelayClient.RELAY_RESULT_USER_INVALID_STATE, RELAY_RESULT_USER_INVALID_STATE);
            }
        };
        // リクエストコマンド応答結果コードの変換
        public final Map<String, STB_REQUEST_COMMAND_TYPES> mRequestCommandMap = new HashMap<String, STB_REQUEST_COMMAND_TYPES>() {
            {
                put(RemoteControlRelayClient.RELAY_COMMAND_UNKNOWN, STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN);
                put(RemoteControlRelayClient.RELAY_COMMAND_IS_USER_ACCOUNT_EXIST, STB_REQUEST_COMMAND_TYPES.IS_USER_ACCOUNT_EXIST);
                put(RemoteControlRelayClient.RELAY_COMMAND_TITLE_DETAIL, STB_REQUEST_COMMAND_TYPES.TITLE_DETAIL);
                put(RemoteControlRelayClient.RELAY_COMMAND_START_APPLICATION, STB_REQUEST_COMMAND_TYPES.START_APPLICATION);
            }
        };

        // アプリ名シンボルに対するアプリ起動要求種別
        private final Map<String, STB_APPLICATION_TYPES> mStbApplicationEnumMap = new HashMap<String, STB_APPLICATION_TYPES>() {
            {
                put(STB_APPLICATION_DTV, STB_APPLICATION_TYPES.DTV);    // dTV
                put(STB_APPLICATION_DANIMESTORE, STB_APPLICATION_TYPES.DANIMESTORE);    // dアニメストア
                put(STB_APPLICATION_DTVCHANNEL, STB_APPLICATION_TYPES.DTVCHANNEL);  // dTVチャンネル
                put(STB_APPLICATION_HIKARITV, STB_APPLICATION_TYPES.HIKARITV);    // ひかりTV
                put(STB_APPLICATION_DAZN, STB_APPLICATION_TYPES.DAZN);    // ダ・ゾーン
            }
        };

        public ResponseMessage() {
            mResult = RELAY_RESULT_OK;
            mResultCode = RELAY_RESULT_SUCCESS;
        }

        public ResponseMessage(int result, int resultCode, String message) {
            mResult = result;
            mResultCode = resultCode;
        }

        public int getResult() {
            return mResult;
        }

        public void setResult(int result) {
            mResult = result;
        }

        public void setResultCode(int resultCode) {
            mResultCode = resultCode;
        }

        public int getResultCode() {
            return mResultCode;
        }

        public STB_APPLICATION_TYPES getApplicationTypes() {
            return mApplicationTypes;
        }

        public void setApplicationTypes(STB_APPLICATION_TYPES applicationTypes) {
            mApplicationTypes = applicationTypes;
        }

        public void setRequestCommandTypes(STB_REQUEST_COMMAND_TYPES requestCommandTypes) {
            mRequestCommandTypes = requestCommandTypes;
        }

        public STB_REQUEST_COMMAND_TYPES getRequestCommandTypes() {
            return mRequestCommandTypes;
        }
    }

    /**
     * キーコードをSTBへ送信するスレッド.
     */
    private class KeycodeRerayTask implements Runnable {
        private String mKeycodeRequest;

        public KeycodeRerayTask(String keycode, int action, boolean canceled) {
            mKeycodeRequest = setKeyeventRequest(keycode, action, canceled);
        }

        /**
         * キーコードをSTBへ送信する.
         */
        @Override
        public void run() {
            StbConnectRelayClient stbDatagram = StbConnectRelayClient.getInstance();  // Socket通信
            stbDatagram.setRemoteIp(ｍRemoteHost);
            if (mKeycodeRequest != null) {
                stbDatagram.sendDatagram(mKeycodeRequest);
            }
        }
    }

    /**
     * アプリ起動要求を受信してアプリ起動リクエストをSTBへ送信する.
     *
     * @param applicationType
     * @return
     */
    public boolean startApplicationRequest(STB_APPLICATION_TYPES applicationType, Context context) {
        String applicationId = getApplicationId(applicationType);
        String requestParam;
        //ユーザID取得
        String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(context);

        if (applicationId != null) {
            requestParam = setApplicationStartRequest(applicationId, userId);
            if (requestParam != null) {
                // アプリ起動要求を受信してインテントをSTBへ送信する
                sendStartApplicationRequest(requestParam);
                return true;
            }
        }
        return false;
    }

    /**
     * アプリ起動要求を受信してタイトル詳細表示のリクエストをSTBへ送信する.
     *
     * @param applicationType
     * @param contentsId      コンテンツID
     * @return
     */
    public boolean startApplicationRequest(STB_APPLICATION_TYPES applicationType, String contentsId, Context context) {
        String applicationId = getApplicationId(applicationType);
        String requestParam;

        //ユーザID取得
        String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(context);

        if (applicationId != null && contentsId != null) {
            requestParam = setTitleDetailRequest(applicationId, contentsId, userId);
            if (requestParam != null) {
                // アプリ起動要求を受信してインテントをSTBへ送信する
                sendStartApplicationRequest(requestParam);
                return true;
            } else {
                ((BaseActivity) context).setRemoteProgressVisible(View.GONE);
            }
        } else {
            ((BaseActivity) context).setRemoteProgressVisible(View.GONE);
        }
        return false;
    }

    /**
     * dアカチェック要求を受信してdアカチェックリクエストをSTBへ送信する.
     *
     * @param context
     * @return
     */
    public void isUserAccountExistRequest(Context context) {
        DTVTLogger.start();
        String requestParam;

        //ユーザID取得
        String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(context);
        if (userId != null && !userId.equals("")) {
            requestParam = setAccountCheckRequest(userId);
            if (requestParam != null) {
                // dアカチェック要求を受信してdアカチェックリクエストをSTBへ送信する
                sendStartApplicationRequest(requestParam);
            }
        }
        DTVTLogger.end();
    }

    /**
     * dアカチェック要求のメッセージ（JSON形式）を作成する.
     *
     * @param userId
     * @return
     */
    private String setAccountCheckRequest(String userId) {
        JSONObject requestJson = new JSONObject();
        String request = null;
        try {
            requestJson.put(RELAY_COMMAND, RELAY_COMMAND_IS_USER_ACCOUNT_EXIST);
            requestJson.put(RELAY_COMMAND_USER_ID, toHashValue(userId));
            request = requestJson.toString();
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return request;
    }

    /**
     * アプリ起動要求種別に対応するアプリID.
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
     * アプリ起動要求をSTBへ送信するスレッド.
     */
    private class StartApplicationRequestTask implements Runnable {

        private String mRequestParam;

        public StartApplicationRequestTask(String requestParam) {
            mRequestParam = requestParam;
        }

        /**
         * アプリ起動要求をSTBへ送信して処理結果応答を取得する.
         */
        @Override
        public void run() {
            StbConnectRelayClient stbConnection = StbConnectRelayClient.getInstance();  // Socket通信
            String recvData;
            ResponseMessage response = new ResponseMessage();

            if (mRequestParam != null) {
                stbConnection.setRemoteIp(ｍRemoteHost);
                // アプリ起動要求をSTBへ送信して処理結果応答を取得する
                if (stbConnection.connect()) {
                    if (stbConnection.send(mRequestParam)) {
                        recvData = stbConnection.receive();
                        DTVTLogger.debug("recvData:" + recvData);
                        response = setResponse(recvData);
                    }
                    stbConnection.disconnect();
                } else {
                    DTVTLogger.debug("failed to connect to the STB");
                    response.setResult(ResponseMessage.RELAY_RESULT_ERROR);
                    response.setResultCode(ResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE);
                    response.setRequestCommandTypes(STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN);
                }
                sendResponseMessage(response);
            }
        }

        /**
         * 処理結果応答をハンドラーへ通知する.
         *
         * @param response
         */
        private void sendResponseMessage(ResponseMessage response) {
            resetRequestStbElapsedTime();
            if (mHandler != null) {
                mHandler.sendMessage(mHandler.obtainMessage(response.getResult(), response));
            }
        }

        /**
         * 中継アプリの応答を返す.
         *
         * @param recvResult
         * @return
         */
        private ResponseMessage setResponse(String recvResult) {
            JSONObject recvJson = new JSONObject();
            String message = null;
            String errorcode = null;
            String requestCommand= null;
            String appId = null;
            ResponseMessage response = new ResponseMessage();
            try {
                response.setResult(ResponseMessage.RELAY_RESULT_ERROR);
                response.setResultCode(ResponseMessage.RELAY_RESULT_INTERNAL_ERROR);
                response.setRequestCommandTypes(STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN);
                if (recvResult != null) {
                    recvJson = new JSONObject(recvResult);
                    response.setResult(response.mResultMap.get(recvJson.get(RELAY_RESULT).toString()));
                    // エラーの場合、応答結果コードをエラーコード値に変換
                    if (recvJson.has(RELAY_RESULT_ERROR_CODE)) {
                        errorcode = recvJson.get(RELAY_RESULT_ERROR_CODE).toString();
                        if (response.mResultCodeMap.containsKey(errorcode)) {
                            response.setResultCode(response.mResultCodeMap.get(errorcode));
                        } else {
                            response.setResultCode(ResponseMessage.RELAY_RESULT_INTERNAL_ERROR);
                        }
                    }
                    // STBアプリ起動要求のアプリ種別をアプリ種別コードに変換
                    if (recvJson.has(RELAY_COMMAND_APPLICATION_ID)) {
                        appId = recvJson.get(RELAY_COMMAND_APPLICATION_ID).toString();
                        if (response.mStbApplicationEnumMap.containsKey(appId)) {
                            response.setApplicationTypes(response.mStbApplicationEnumMap.get(appId));
                        } else {
                            response.setResultCode(ResponseMessage.RELAY_RESULT_INTERNAL_ERROR);
                        }
                    }
                    // dアカチェック要求のリクエストコマンド種別をリクエストコマンド種別に変換
                    if (recvJson.has(RELAY_COMMAND_REQUEST_COMMAND)) {
                        requestCommand = recvJson.get(RELAY_COMMAND_REQUEST_COMMAND).toString();
                        if (response.mRequestCommandMap.containsKey(requestCommand)) {
                            response.setRequestCommandTypes(response.mRequestCommandMap.get(requestCommand));
                        } else {
                            response.setResultCode(ResponseMessage.RELAY_RESULT_INTERNAL_ERROR);
                        }
                    }
                }
            } catch (JSONException e) {
                DTVTLogger.debug(e);
                response.setResult(ResponseMessage.RELAY_RESULT_ERROR);
                response.setResultCode(ResponseMessage.RELAY_RESULT_INTERNAL_ERROR);
                response.setRequestCommandTypes(STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN);
            }
            return response;
        }
    }

    /**
     * アプリ起動要求送信スレッドを開始.
     */
    private void sendStartApplicationRequest(String requestParam) {
        if (!isRequestStbElapsedTime()) {
            return;
        }
        setRequestStbElapsedTime();

        Thread mThread = new Thread(new StartApplicationRequestTask(requestParam));
        mThread.start();
//        new Thread(new StartApplicationRequestTask(requestParam)).start();
    }

    /**
     * アプリ起動要求のメッセージ（JSON形式）を作成する.
     *
     * @param applicationId
     * @return
     */
    private String setApplicationStartRequest(String applicationId, String userId) {
        JSONObject requestJson = new JSONObject();
        String request = null;
        try {
            requestJson.put(RELAY_COMMAND, RELAY_COMMAND_START_APPLICATION);
            requestJson.put(RELAY_COMMAND_APPLICATION_ID, applicationId);
            requestJson.put(RELAY_COMMAND_USER_ID, toHashValue(userId));
            request = requestJson.toString();
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return request;
    }

    /**
     * アプリ起動要求のメッセージ（JSON形式）を作成する.
     * タイトル詳細表示のリクエスト
     *
     * @param applicationId
     * @param contentsId
     * @return
     */
    private String setTitleDetailRequest(String applicationId, String contentsId, String userId) {
        JSONObject requestJson = new JSONObject();
        String request = null;
        try {
            requestJson.put(RELAY_COMMAND, RELAY_COMMAND_TITLE_DETAIL);
            requestJson.put(RELAY_COMMAND_APPLICATION_ID, applicationId);
            requestJson.put(RELAY_COMMAND_CONTENTS_ID, contentsId);
            requestJson.put(RELAY_COMMAND_USER_ID, toHashValue(userId));
            request = requestJson.toString();
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return request;
    }

    /**
     * キーイベント送信要求のメッセージ（JSON形式）を作成する.
     *
     * @param keycode
     * @param action
     * @param canceled
     * @return
     */
    private static String setKeyeventRequest(String keycode, int action, boolean canceled) {
        JSONObject requestJson = new JSONObject();
        String jsonStr = null;
        try {
            requestJson.put(RELAY_KEYEVENT, keycode);
            requestJson.put(RELAY_KEYEVENT_ACTION, mKeyeventActionMap.get(action));
            requestJson.put(RELAY_KEYEVENT_ACTION_CANCELED, canceled ? RELAY_KEYEVENT_ACTION_CANCELED_TRUE : "");
            jsonStr = requestJson.toString();
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return jsonStr;
    }

    /**
     * Socket通信／データグラム送信の送信先のIPアドレスを設定.
     * TODO: デバッグ用
     *
     * @param remoteIp
     */
    public void setDebugRemoteIp(String remoteIp) {
        ｍRemoteHost = remoteIp;
    }

    /**
     * 文字列から ハッシュ値を取得する.
     *
     * @return ハッシュ値
     */
    public String toHashValue(String value) {
        byte[] hashValue = {};
        StringBuilder sb = new StringBuilder();

        hashValue = toHashBytes(value);
        if (hashValue == null) {
            DTVTLogger.debug("hash value is null");
            return null;
        }
        for (byte hb : hashValue) {
            String hex = String.format("%02x", hb);
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * ハッシュ化したソルトを取得.
     * ※ハッシュアルゴリズム：SHA-256
     *
     * @param salt ソルト
     * @return ハッシュ化したバイト配列
     */
    private static byte[] toHashBytes(String salt) {
        MessageDigest digest;

        if (salt == null) {
            return null;
        }
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHME);
        } catch (NoSuchAlgorithmException e) {
            DTVTLogger.debug(e);
            return null;
        }
        digest.update(salt.getBytes(StandardCharsets.UTF_8));
        return digest.digest();
    }
}