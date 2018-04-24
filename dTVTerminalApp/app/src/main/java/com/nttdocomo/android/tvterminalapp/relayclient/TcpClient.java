/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherApi;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * TCPクライアント.
 */
public class TcpClient {
    /**Socket.*/
    private Socket mSocket = null;
    /**RemoteIp.*/
    private String mRemoteIp = null;
    /**RemotePort.*/
    private int mRemotePort = 0;

    /**STBスタンバイ状態からの電源ONとユーザアカウント切り替えに必要な最大待ち時間（ミリ秒）.*/
    public static final int SEND_RECV_TIMEOUT = 15000;

    /**
     * コンストラクタ.
     */
    public TcpClient() {
        mSocket = null;
        mRemoteIp = null;
    }

    /**
     * Socket通信でSTBへ接続する.
     *
     * @param remoteIp remoteIp
     * @param remotePort remotePort
     * @return 成功:true
     */
    public boolean connect(final String remoteIp, final int remotePort) {
        mRemoteIp = remoteIp;
        mRemotePort = remotePort;
        DTVTLogger.debug("mRemoteIp:" + mRemoteIp);
        DTVTLogger.debug("mRemotePort:" + mRemotePort);
        SocketAddress remoteAddress = new InetSocketAddress(mRemoteIp, mRemotePort);
        mSocket = new Socket();

        try {
            mSocket.connect(remoteAddress, SEND_RECV_TIMEOUT);
            // アプリ起動時にここで mSocket が null になる場合がある
            if (null == mSocket) {
                DTVTLogger.debug("connect is null!");
                disconnect();
                return false;
            }
            if (mSocket.isConnected() && mSocket.isBound()) {
                mSocket.setTcpNoDelay(true);
            } else {
                disconnect();
                return false;
            }
        } catch (SocketTimeoutException e) {
            DTVTLogger.debug(e);
            this.disconnect();
            return false;
        } catch (IllegalArgumentException e) {
            DTVTLogger.debug(e);
            this.disconnect();
            return false;
        } catch (IOException e) {
            DTVTLogger.debug(e);
            this.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Socket通信を切断する.
     */
    public void disconnect() {
        if (mSocket != null) {
            DTVTLogger.debug("disconnecting");
            if (mSocket.isConnected()) {
                DTVTLogger.debug("isConnected() is true");
                try {
                    DTVTLogger.debug("Socket.close()");
                    mSocket.close();
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
            DTVTLogger.debug("Socket is set null");
            mSocket = null;
        }
    }

    /**
     * Socket通信のメッセージを受信する.
     * 中継アプリから JSON形式の応答メッセージを受信する
     *
     * @return 応答メッセージ
     */
    public synchronized String receive() {
        String recvdata = null;

        DTVTLogger.debug("receive start");
        if (mSocket == null) {
            DTVTLogger.debug("mSocket is null!");
            return null;
        }
        try {
            DTVTLogger.debug("getInputStream()");
            InputStream inputStream = mSocket.getInputStream();

            while (inputStream.available() >= 0) {
                // 受信待ち
                if (inputStream.available() == 0) {
                    continue;
                }
                DTVTLogger.warning("inputStream.available() = " + inputStream.available());

                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes, 0, inputStream.available());
                try {
                    String decodeString = CipherUtil.decodeData(bytes);
                    DTVTLogger.warning("decodeString = " + decodeString);
                    return decodeString;
                } catch (Exception e) {
                    DTVTLogger.error(e.getMessage());
                }
                recvdata = new String(bytes);
                break;
            }
        }  catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            DTVTLogger.debug(String.format("??? :%s", e.getMessage()));
        }
        DTVTLogger.error("recvdata = " + recvdata);
        return recvdata;
    }
    /**
     * 鍵交換Socket通信のメッセージを受信し、共有鍵を設定する
     *
     * @return 成功:true
     */
    public synchronized boolean receiveExchangeKey() {
        boolean result = false;
        if (mSocket == null) {
            DTVTLogger.warning("mSocket == null");
            return false;
        }

        try {
            InputStream inputStream = mSocket.getInputStream();
            while (inputStream.available() >= 0) {
                // 受信待ち
                if (inputStream.available() == 0) {
                    continue;
                }
                byte[] dataBytes = new byte[inputStream.available()];
                inputStream.read(dataBytes, 0, dataBytes.length);
                CipherUtil.setShareKey(dataBytes);
                result = true;
                break;
            }
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return result;
    }
    /**
     * STBへメッセージ（JSON形式）を送信する.
     *
     * @param data JSON形式メッセージ
     * @return true 送信した場合
     */
    public boolean send(final String data) {

        OutputStreamWriter out = null;
        if (mSocket == null) {
            DTVTLogger.debug("mSocket is null!");
            return false;
        }

        try {
            DTVTLogger.debug("data:" + data);
            out = new OutputStreamWriter(mSocket.getOutputStream(), StandardCharsets.UTF_8);
            out.write(data);
            out.flush();

        } catch (IOException e) {
            DTVTLogger.debug(e);
            return false;
        }
        return true;
    }

    /**
     * STBへメッセージ（暗号化されたbinaryデータ）を送信する.
     * @param data byte配列
     * @param length byte配列長
     * @return 成功:true
     */
    public boolean send(final byte[] data, final int length) {
        DTVTLogger.debug(" >>>");
        boolean result = false;
        OutputStream out;
        try {
            out = mSocket.getOutputStream();
            out.write(data, 0, length);
            out.flush();
            result = true;
        } catch (IOException e) {
            DTVTLogger.debug(e);
        }
        DTVTLogger.debug(" <<< result = " + result);
        return result;
    }
    
}