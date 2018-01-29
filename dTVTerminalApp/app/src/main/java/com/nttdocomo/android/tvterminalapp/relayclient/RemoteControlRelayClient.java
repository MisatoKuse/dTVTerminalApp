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

    private static final String KEYCODE_DPAD_UP = "KEYCODE_DPAD_UP";
    private static final String KEYCODE_DPAD_DOWN = "KEYCODE_DPAD_DOWN";
    private static final String KEYCODE_DPAD_LEFT = "KEYCODE_DPAD_LEFT";
    private static final String KEYCODE_DPAD_RIGHT = "KEYCODE_DPAD_RIGHT";
    private static final String KEYCODE_HOME = "KEYCODE_HOME";
    private static final String KEYCODE_1 = "KEYCODE_1";
    private static final String KEYCODE_2 = "KEYCODE_2";
    private static final String KEYCODE_3 = "KEYCODE_3";
    private static final String KEYCODE_4 = "KEYCODE_4";
    private static final String KEYCODE_5 = "KEYCODE_5";
    private static final String KEYCODE_6 = "KEYCODE_6";
    private static final String KEYCODE_7 = "KEYCODE_7";
    private static final String KEYCODE_8 = "KEYCODE_8";
    private static final String KEYCODE_9 = "KEYCODE_9";
    private static final String KEYCODE_0 = "KEYCODE_0";
    private static final String KEYCODE_11 = "KEYCODE_11";
    private static final String KEYCODE_12 = "KEYCODE_12";
    private static final String KEYCODE_TV_TERRESTRIAL_DIGITAL = "KEYCODE_TV_TERRESTRIAL_DIGITAL";
    private static final String KEYCODE_TV_SATELLITE_BS = "KEYCODE_TV_SATELLITE_BS";
    private static final String KEYCODE_TV = "KEYCODE_TV";
    private static final String KEYCODE_GUIDE = "KEYCODE_GUIDE";
    private static final String KEYCODE_DPAD_CENTER = "KEYCODE_DPAD_CENTER";
    private static final String KEYCODE_BACK = "KEYCODE_BACK";
    private static final String KEYCODE_MEDIA_PLAY_PAUSE = "KEYCODE_MEDIA_PLAY_PAUSE";
    private static final String KEYCODE_MEDIA_SKIP_BACKWARD = "KEYCODE_MEDIA_SKIP_BACKWARD";
    private static final String KEYCODE_MEDIA_REWIND = "KEYCODE_MEDIA_REWIND";
    private static final String KEYCODE_MEDIA_FAST_FORWARD = "KEYCODE_MEDIA_FAST_FORWARD";
    private static final String KEYCODE_MEDIA_SKIP_FORWARD = "KEYCODE_MEDIA_SKIP_FORWARD";
    private static final String KEYCODE_CHANNEL_UP = "KEYCODE_CHANNEL_UP";
    private static final String KEYCODE_CHANNEL_DOWN = "KEYCODE_CHANNEL_DOWN";
    private static final String KEYCODE_INFO = "KEYCODE_INFO";
    private static final String KEYCODE_TV_DATA_SERVICE = "KEYCODE_TV_DATA_SERVICE";
    private static final String KEYCODE_POWER = "KEYCODE_POWER";
    private static final String KEYCODE_REC_LIST = "KEYCODE_REC_LIST";

    // 受信キーコード名に対応する STBキーコード
    private static final Map<Integer, String> keyCodeNameMap = new HashMap<Integer, String>() {
        {
            put(R.id.remote_controller_bt_up, KEYCODE_DPAD_UP); // カーソル (上下左右)
            put(R.id.remote_controller_bt_down, KEYCODE_DPAD_DOWN);
            put(R.id.remote_controller_bt_left, KEYCODE_DPAD_LEFT);
            put(R.id.remote_controller_bt_right, KEYCODE_DPAD_RIGHT);
            put(R.id.remote_controller_bt_toHome, KEYCODE_HOME); // ホーム
            // チャンネル (1～12) ※ チャンネル (10)は KEYCODE_0となる
            put(R.id.remote_controller_bt_one, KEYCODE_1);
            put(R.id.remote_controller_bt_two, KEYCODE_2);
            put(R.id.remote_controller_bt_three, KEYCODE_3);
            put(R.id.remote_controller_bt_four, KEYCODE_4);
            put(R.id.remote_controller_bt_five, KEYCODE_5);
            put(R.id.remote_controller_bt_six, KEYCODE_6);
            put(R.id.remote_controller_bt_seven, KEYCODE_7);
            put(R.id.remote_controller_bt_eight, KEYCODE_8);
            put(R.id.remote_controller_bt_nine, KEYCODE_9);
            put(R.id.remote_controller_bt_ten, KEYCODE_0); // ※ チャンネル (10)は (0)
            put(R.id.remote_controller_bt_eleven, KEYCODE_11);
            put(R.id.remote_controller_bt_twelve, KEYCODE_12);
            put(R.id.remote_controller_bt_degital, KEYCODE_TV_TERRESTRIAL_DIGITAL); // 地デジ
            put(R.id.remote_controller_bt_bs, KEYCODE_TV_SATELLITE_BS); // BS
            put(R.id.remote_controller_bt_iptv, KEYCODE_TV); // IPTV
            put(R.id.remote_controller_bt_tv_program, KEYCODE_GUIDE); // 番組表
            put(R.id.remote_controller_bt_decide, KEYCODE_DPAD_CENTER); // 決定
            put(R.id.remote_controller_bt_back, KEYCODE_BACK); // 戻る
            put(R.id.remote_controller_iv_playOrStop, KEYCODE_MEDIA_PLAY_PAUSE); // 再生/停止
            put(R.id.remote_controller_iv_blue, KEYCODE_MEDIA_SKIP_BACKWARD); // カラー (青)/10秒戻し
            put(R.id.remote_controller_iv_red, KEYCODE_MEDIA_REWIND); // カラー (赤)/巻き戻し
            put(R.id.remote_controller_iv_green, KEYCODE_MEDIA_FAST_FORWARD); // カラー (緑)/早送り
            put(R.id.remote_controller_iv_yellow, KEYCODE_MEDIA_SKIP_FORWARD); // カラー (黄)/30秒送り
            put(R.id.remote_controller_bt_channel_plus, KEYCODE_CHANNEL_UP); // チャンネル (上下)
            put(R.id.remote_controller_bt_channel_minus, KEYCODE_CHANNEL_DOWN);
            put(R.id.remote_controller_bt_notice, KEYCODE_INFO); // お知らせ
            put(R.id.remote_controller_bt_ddata, KEYCODE_TV_DATA_SERVICE); // dデータ
            put(R.id.remote_controller_iv_power, KEYCODE_POWER); // 電源
            put(R.id.remote_controller_bt_record_list, KEYCODE_REC_LIST); // 録画リスト
        }
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
     * サービス・カテゴリー分類.
     */
    public enum SERVICE_CATEGORY_TYPES {
        // 初期値
        UNKNOWN,
        // 放送
        DTV_CHANNEL_CATEGORY_BROADCAST,
        // VOD（見逃し）
        DTV_CHANNEL_CATEGORY_MISSED,
        // VOD（関連番組）
        DTV_CHANNEL_CATEGORY_RELATION,
        // 地デジ
        H4D_CATEGORY_TERRESTRIAL_DIGITAL,
        // BS
        H4D_CATEGORY_SATELLITE_BS,
        // H4D IPTV
        H4D_CATEGORY_IPTV,
        // H4D dTVチャンネル 放送
        H4D_CATEGORY_DTV_CHANNEL_BROADCAST,
        // H4D dTVチャンネル VOD（見逃し）
        H4D_CATEGORY_DTV_CHANNEL_MISSED,
        // H4D dTVチャンネル VOD（関連番組）
        H4D_CATEGORY_DTV_CHANNEL_RELATION,
        // ひかりTV 録画
        H4D_CATEGORY_RECORDED_CONTENTS,
        // H4D ひかりTV VOD
        H4D_CATEGORY_HIKARI_TV_VOD,
        // dTV SVOD
        H4D_CATEGORY_DTV_SVOD
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
    private static final String RELAY_COMMAND_KEYEVENT_KEYCODE_POWER = "KEYEVENT_KEYCODE_POWER";
    private static final String RELAY_COMMAND_UNKNOWN = "COMMAND_UNKNOWN";
    private static final String RELAY_COMMAND_APPLICATION_ID = "APP_ID";
    private static final String RELAY_COMMAND_REQUEST_COMMAND = "REQUEST_COMMAND";
    private static final String RELAY_COMMAND_USER_ID = "USER_ID";
    private static final String RELAY_COMMAND_CONTENTS_ID = "CONTENTS_ID";
    private static final String RELAY_COMMAND_SERVICE_CATEGORY_TYPE = "SERVICE_CATEGORY_TYPE";
    private static final String RELAY_RESULT = "RESULT";
    private static final String RELAY_RESULT_OK = "OK";
    private static final String RELAY_RESULT_ERROR = "ERROR";
    private static final String RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY = "APPLICATION_VERSION_COMPATIBILITY";
    private static final String RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_DTVT_APPLICATION = "dTVT_APPLICATION";
    private static final String RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_STB_RELAY_SERVICE = "STB_RELAY_SERVICE";
    private static final int DTVT_APPLICATION_VERSION_CODE = 1;
    private static final int STB_RELAY_SERVICE_VERSION_CODE = 3;
    // dTVチャンネル・カテゴリー分類に対応するカテゴリー・シンボル名
    private static final String STB_APPLICATION_DTVCHANNEL_CATEGORY_BROADCAST = "DTVCHANNEL_CATEGORY_BROADCAST";
    private static final String STB_APPLICATION_DTVCHANNEL_CATEGORY_MISSED = "DTVCHANNEL_CATEGORY_MISSED";
    private static final String STB_APPLICATION_DTVCHANNEL_CATEGORY_RELATION = "DTVCHANNEL_CATEGORY_RELATION";

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

    // dTVチャンネル・カテゴリー分類に対応するカテゴリー・シンボル名
    private static final Map<SERVICE_CATEGORY_TYPES, String> mServiceCategorySymbolMap = new HashMap<SERVICE_CATEGORY_TYPES, String>() {
        {
            put(SERVICE_CATEGORY_TYPES.DTV_CHANNEL_CATEGORY_BROADCAST, STB_APPLICATION_DTVCHANNEL_CATEGORY_BROADCAST);    // dTVチャンネル・放送
            put(SERVICE_CATEGORY_TYPES.DTV_CHANNEL_CATEGORY_MISSED, STB_APPLICATION_DTVCHANNEL_CATEGORY_MISSED);    // dTVチャンネル・VOD（見逃し）
            put(SERVICE_CATEGORY_TYPES.DTV_CHANNEL_CATEGORY_RELATION, STB_APPLICATION_DTVCHANNEL_CATEGORY_RELATION);  // dTVチャンネル・VOD（関連番組）
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
     * STB電源ON/OFF要求をSTBへ送信する.
     *
     * @return true 電源キーの場合
     */
    private boolean switchStbPowerRequest(String keycode, int action, boolean canceled, Context context) {
        String requestParam;

        // 電源キーをフックする
        if (KEYCODE_POWER.equals(keycode)) {
            DTVTLogger.debug(String.format("KEYCODE_POWER, action:%d canceled:%s", action, canceled));
            if (KeyEvent.ACTION_UP == action && !canceled) {
                //ユーザID取得
                String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(context);
                if (userId != null && !userId.isEmpty()) {
                    requestParam = setSwitchStbPowerRequest(userId);
                    if (requestParam != null) {
                        DTVTLogger.debug(String.format("KEYCODE_POWER, action:%d canceled:%s send", action, canceled));
                        // STB電源ON/OFF要求をSTBへ送信する
                        sendStartApplicationRequest(requestParam);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * キーコードをSTBへ送信する.
     *
     * @param keycodeRid
     */
    public void sendKeycode(int keycodeRid, int action, boolean canceled, Context context) {
        String keycode = convertKeycode(keycodeRid);
        if (keycode != null && mKeyeventActionMap.containsKey(action)) {
            if (switchStbPowerRequest(keycode, action, canceled, context)) {
                return;
            }
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
     * アプリ起動要求を受信してタイトル詳細表示のリクエストをSTBへ送信する.
     * ・dTVチャンネル・カテゴリー分類に対応
     * ・ひかりTV・カテゴリー分類に対応
     *
     * @param applicationType
     * @param contentsId      コンテンツID
     * @param serviceCategoryType  カテゴリー分類
     * @return
     */
    public boolean startApplicationRequest(STB_APPLICATION_TYPES applicationType, String contentsId, SERVICE_CATEGORY_TYPES serviceCategoryType, Context context) {
        String applicationId = getApplicationId(applicationType);
        String requestParam;

        //ユーザID取得
        String userId = SharedPreferencesUtils.getSharedPreferencesDaccountId(context);

        if (applicationId != null && contentsId != null) {
            requestParam = setTitleDetailRequest(applicationId, contentsId, serviceCategoryType, userId);
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
        if (userId != null && !userId.isEmpty()) {
            requestParam = setAccountCheckRequest(userId);
            if (requestParam != null) {
                // dアカチェック要求を受信してdアカチェックリクエストをSTBへ送信する
                sendStartApplicationRequest(requestParam);
            }
        }
        DTVTLogger.end();
    }

    /**
     * 電源ON/OFF要求のメッセージ（JSON形式）を作成する.
     *
     * @param userId
     * @return request JSON形式
     */
    private String setSwitchStbPowerRequest(String userId) {
        JSONObject requestJson = new JSONObject();
        String request = null;
        try {
            requestJson.put(RELAY_COMMAND, RELAY_COMMAND_KEYEVENT_KEYCODE_POWER);
            JSONObject versionJson = new JSONObject();
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_DTVT_APPLICATION, DTVT_APPLICATION_VERSION_CODE);
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_STB_RELAY_SERVICE, STB_RELAY_SERVICE_VERSION_CODE);
            requestJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY, versionJson);
            requestJson.put(RELAY_COMMAND_USER_ID, toHashValue(userId));
            request = requestJson.toString();
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return request;
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
            JSONObject versionJson = new JSONObject();
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_DTVT_APPLICATION, DTVT_APPLICATION_VERSION_CODE);
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_STB_RELAY_SERVICE, STB_RELAY_SERVICE_VERSION_CODE);
            requestJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY, versionJson);
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
     * dTVチャンネル・カテゴリー分類に対応するカテゴリー・シンボル名.
     *
     * @param serviceCategoryType
     * @return  dTVチャンネル・カテゴリー分類に対応するカテゴリー・シンボル名
     */
    private String getServiceCategorySymbol(SERVICE_CATEGORY_TYPES serviceCategoryType) {
        String serviceCategorySymbol = "";
        if (mServiceCategorySymbolMap.containsKey(serviceCategoryType)) {
            serviceCategorySymbol = mServiceCategorySymbolMap.get(serviceCategoryType);
        }
        return serviceCategorySymbol;
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
            JSONObject versionJson = new JSONObject();
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_DTVT_APPLICATION, DTVT_APPLICATION_VERSION_CODE);
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_STB_RELAY_SERVICE, STB_RELAY_SERVICE_VERSION_CODE);
            requestJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY, versionJson);
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
     * @param applicationId  アプリケーションID
     * @param contentsId      コンテンツID
     * @return
     */
    private String setTitleDetailRequest(String applicationId, String contentsId, String userId) {
        JSONObject requestJson = new JSONObject();
        String request = null;
        try {
            requestJson.put(RELAY_COMMAND, RELAY_COMMAND_TITLE_DETAIL);
            requestJson.put(RELAY_COMMAND_APPLICATION_ID, applicationId);
            requestJson.put(RELAY_COMMAND_CONTENTS_ID, contentsId);
            JSONObject versionJson = new JSONObject();
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_DTVT_APPLICATION, DTVT_APPLICATION_VERSION_CODE);
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_STB_RELAY_SERVICE, STB_RELAY_SERVICE_VERSION_CODE);
            requestJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY, versionJson);
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
     * ・dTVチャンネル・カテゴリー分類に対応
     * ・ひかりTV・カテゴリー分類に対応
     *
     * @param applicationId  アプリケーションID
     * @param contentsId      コンテンツID
     * @param serviceCategoryType  カテゴリー分類
     * @return
     */
    private String setTitleDetailRequest(String applicationId, String contentsId, SERVICE_CATEGORY_TYPES serviceCategoryType, String userId) {
        JSONObject requestJson = new JSONObject();
        String request = null;

        try {
            requestJson.put(RELAY_COMMAND, RELAY_COMMAND_TITLE_DETAIL);
            requestJson.put(RELAY_COMMAND_APPLICATION_ID, applicationId);
            requestJson.put(RELAY_COMMAND_SERVICE_CATEGORY_TYPE, getServiceCategorySymbol(serviceCategoryType));
            requestJson.put(RELAY_COMMAND_CONTENTS_ID, contentsId);
            JSONObject versionJson = new JSONObject();
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_DTVT_APPLICATION, DTVT_APPLICATION_VERSION_CODE);
            versionJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY_STB_RELAY_SERVICE, STB_RELAY_SERVICE_VERSION_CODE);
            requestJson.put(RELAY_COMMAND_APPLICATION_VERSION_COMPATIBILITY, versionJson);
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