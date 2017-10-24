/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;
import com.nttdocomo.android.tvterminalapp.R;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * キーコードをSTBへ送信する
 */
public class RemoteControlDataProvider {

    static final int SERVER_PORT = 3000;    // udp 受信ポート
    static final int KEYCODE_UNKNOWN = 0;
    // 受信キーコード名に対応する STBキーコード
    static final Map<Integer, String> keyCodeNameMap = new HashMap<Integer, String>() {
        {put(R.id.keycode_dpad_up, "KEYCODE_DPAD_UP");}         // カーソル (上下左右)
        {put(R.id.keycode_dpad_down, "KEYCODE_DPAD_DOWN");}
        {put(R.id.keycode_dpad_left, "KEYCODE_DPAD_LEFT");}
        {put(R.id.keycode_dpad_right, "KEYCODE_DPAD_RIGHT");}
        {put(R.id.keycode_dpad_home, "KEYCODE_HOME");}       // ホーム
        {put(KEYCODE_UNKNOWN, "KEYCODE_0");}    // チャンネル (1～12) ※ チャンネル (10)は未定義
        {put(R.id.keycode_1, "KEYCODE_1");}
        {put(R.id.keycode_2, "KEYCODE_2");}
        {put(R.id.keycode_3, "KEYCODE_3");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_4");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_5");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_6");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_7");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_8");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_9");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_11");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_12");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_TV_TERRESTRIAL_DIGITAL");}  // 地デジ
        {put(KEYCODE_UNKNOWN, "KEYCODE_TV_SATELLITE_BS");}  // BS
        {put(KEYCODE_UNKNOWN, "KEYCODE_TV");} // IPTV
        {put(KEYCODE_UNKNOWN, "KEYCODE_GUIDE");}  // 番組表
        {put(R.id.keycode_dpad_center, "KEYCODE_DPAD_CENTER");}  // 決定
        {put(R.id.keycode_dpad_back, "KEYCODE_BACK");}  // 戻る
        {put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_PLAY_PAUSE");} // 再生/停止
        {put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_SKIP_BACKWARD");}  // カラー (青)/10秒戻し
        {put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_REWIND");}  // カラー (赤)/巻き戻し
        {put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_FAST_FORWARD");}  // カラー (緑)/早送り
        {put(KEYCODE_UNKNOWN, "KEYCODE_MEDIA_SKIP_FORWARD");}  // カラー (黄)/30秒送り
        {put(R.id.keycode_channel_up, "KEYCODE_CHANNEL_UP");}  // チャンネル (上下)
        {put(R.id.keycode_channel_down, "KEYCODE_CHANNEL_DOWN");}
        {put(KEYCODE_UNKNOWN, "KEYCODE_INFO");}  // お知らせ
        {put(KEYCODE_UNKNOWN, "KEYCODE_TV_DATA_SERVICE");}  // dデータ
    };
    private InetSocketAddress mRemoteAddress = null;
    private Context mContext;
    private String mKeycodeName;
    private String ｍRemoteHost = "192.168.11.30";
//    private String ｍRemoteHost = "127.0.0.1";

    /**
     * データプロバイダ初期化
     * @param context
     */
    public RemoteControlDataProvider(Context context) {
        mRemoteAddress = new InetSocketAddress(ｍRemoteHost, SERVER_PORT);
        Log.i("RemoteControl", "mRemoteAddress:" + mRemoteAddress);
        mContext = context;
    }

    /**
     * キーコード名をキーコードに変換する
     *
     * @param keycodeRid キーコードR.id
     * @return キーコード名（キーコードがない場合は null）
     */
    private String convertKeycode(int keycodeRid){
        String keyCodeName = null;
        if (keyCodeNameMap.containsKey(keycodeRid)) {
            keyCodeName = keyCodeNameMap.get(keycodeRid);
        }
        Log.i("RemoteControl", "convertKeycode:" + keycodeRid + "=" +keyCodeName);
        return keyCodeName;
    }

    /**
     * キーコードをSTBへ送信する
     * @param keycodeRid
     */
    public void sendKeycode(int keycodeRid){
        mKeycodeName = convertKeycode(keycodeRid);
        Log.i("sendKeycode", "mKeycodeName:" + mKeycodeName);
        if (mKeycodeName != null){
            // キーコード送信スレッドを開始
            new Thread(new KeycodeRerayTask(mKeycodeName)).start();
        }
    }

    private class KeycodeRerayTask  implements Runnable {
        private String mSendKeycodeName;

        public KeycodeRerayTask(String keycodeName) {
            mSendKeycodeName = keycodeName;
        }

        /**
         *  キーコード
         *  キーコードをSTBへ送信する
         */
        @Override
        public void run() {
            try {
                Log.i("KeycodeRerayTask", "mSendKeycodeName:" + mSendKeycodeName);
                Log.i("KeycodeRerayTask", "mRemoteAddress:" + mRemoteAddress);
                if (mSendKeycodeName != null) {
                    byte[] buff = mSendKeycodeName.getBytes();
                    DatagramPacket packet = new DatagramPacket(buff, buff.length, mRemoteAddress);
                    new DatagramSocket().send(packet);
                }
            } catch (SocketException e) {
                Log.i("KeycodeRerayTask", e.getMessage());
            } catch (IOException e ) {
                Log.i("KeycodeRerayTask", e.getMessage());
            }
        }
    }

}
