/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherUtil;

import java.io.IOException;
import java.io.InputStream;
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

    /**STBスタンバイ状態からの電源ONとユーザアカウント切り替えに必要な最大待ち時間（ミリ秒）.*/
    public static final int SEND_RECIEVE_TIMEOUT = 15000;
    /**
     * 鍵交換の送受信タイムアウト.
     * ※鍵交換は電文の送信前にメインスレッドから同期処理で通信が開始されるため
     * 　必要十分な短い時間（1500ms以下）を設定する。
     * 　鍵交換処理は通常は 500ms 以内で終了する。
     */
    public static final int KEY_EXCHANGE_TIMEOUT = 1500;

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
    public boolean connect(final String remoteIp, final int remotePort, final int... sendRecieveTimeout) {
        int timeout = SEND_RECIEVE_TIMEOUT;
        if (sendRecieveTimeout != null && sendRecieveTimeout.length > 0 && sendRecieveTimeout[0] > 0) {
            timeout = sendRecieveTimeout[0];
        }
        mRemoteIp = remoteIp;
        DTVTLogger.debug(String.format(Locale.US, "remote: IP address [%s:%d] connection timeout %dms",
                mRemoteIp, remotePort, timeout));
        SocketAddress remoteAddress = new InetSocketAddress(mRemoteIp, remotePort);
        mSocket = new Socket();

        try {
            mSocket.connect(remoteAddress, timeout);
            // アプリ起動時にここで mSocket が null になる場合がある
            if (null == mSocket) {
                DTVTLogger.debug("mSocket is null!");
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
            DTVTLogger.debug(String.format(Locale.US, "connection timeout %dms", timeout));
            DTVTLogger.debug(e);
            this.disconnect();
            return false;
        } catch (IllegalArgumentException e) {
            DTVTLogger.debug(e);
            this.disconnect();
            return false;
        } catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            // 非同期処理で同時に TcpClient が使用されると mSocket が null になることを想定した Fail safe
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
            try {
                if (mSocket.isConnected()) {
                    DTVTLogger.debug("socket is connected.");
                    DTVTLogger.debug("socket close.");
                    mSocket.close();
                }
            }  catch (NullPointerException | IOException e) {
                // SocketException はIOExceptionに含まれる
                // 非同期処理で同時に TcpClient が使用されると mSocket が null になることを想定した Fail safe
                DTVTLogger.debug(e);
            }
            DTVTLogger.debug("socket is set null");
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
        DTVTLogger.start();
        String receiveData = null;

        if (mSocket == null) {
            DTVTLogger.warning("mSocket is null!");
            return null;
        }
        try {
            DTVTLogger.debug("getInputStream()");
            InputStream inputStream = mSocket.getInputStream();

            while (inputStream.available() >= 0) {
                int availableSize = inputStream.available();
                // 受信待ち
                if (availableSize == 0) {
                    continue;
                }
                DTVTLogger.debug("inputStream.available() = " + availableSize);

                byte[] bytes = new byte[availableSize];
                int readSize = inputStream.read(bytes, 0, availableSize);
                // 受信バイト数が264バイト以下のため要求したバイト数よりも少ないバイト数を読み出した場合を想定しません。一旦無視します。
                if (readSize == availableSize) {
                    String decodeString = CipherUtil.decodeData(bytes);
                    if (null != decodeString) {
                        DTVTLogger.debug("decodeString = " + decodeString);
                        receiveData = decodeString;
                    }
                } else {
                    DTVTLogger.warning(String.format(Locale.US, "readSize :%dbytes is not availableSize:%dbytes!",
                            readSize, availableSize));
                }
                break;
            }
        }  catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            DTVTLogger.debug(String.format("??? :%s", e.getMessage()));
        }
        DTVTLogger.debug("receiveData = " + receiveData);
        DTVTLogger.end();
        return receiveData;
    }
    /**
     * 鍵交換Socket通信のメッセージを受信し、共有鍵を設定する.
     *
     * @return 成功:true
     */
    public synchronized boolean receiveExchangeKey() {
        DTVTLogger.start();
        boolean result = false;
        if (mSocket == null) {
            DTVTLogger.warning("mSocket is null!");
            return false;
        }
        try {
            InputStream inputStream = mSocket.getInputStream();
            while (inputStream.available() >= 0) {
                int availableSize = inputStream.available();
                // 受信待ち
                if (availableSize == 0) {
                    continue;
                }
                byte[] dataBytes = new byte[availableSize];
                int readSize = inputStream.read(dataBytes, 0, dataBytes.length);
                DTVTLogger.debug(String.format(Locale.US, "readSize :%dbytes", readSize));
                // 受信バイト数が264バイト以下のため要求したバイト数よりも少ないバイト数を読み出した場合を想定しません。一旦無視します。
                if (readSize == availableSize) {
                    result = CipherUtil.setShareKey(dataBytes);
                    break;
                } else {
                    DTVTLogger.warning(String.format(Locale.US, "readSize :%dbytes is not availableSize:%dbytes!",
                            readSize, availableSize));
                    break;
                }
            }
        } catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            // 非同期処理で同時に TcpClient が使用されると mSocket が null になることを想定した Fail safe
            DTVTLogger.debug(e);
        }
        DTVTLogger.debug(String.format("result = %s", result));
        DTVTLogger.end();
        return result;
    }
    /**
     * STBへメッセージ（JSON形式）を送信する.
     *
     * @param data JSON形式メッセージ
     * @return true 送信した場合
     */
    public boolean send(final String data) {
        DTVTLogger.start();
        if (mSocket == null) {
            DTVTLogger.debug("socket is null!");
            return false;
        }

        OutputStreamWriter out = null;
        try {
            DTVTLogger.debug("data:" + data);
            out = new OutputStreamWriter(mSocket.getOutputStream(), StandardCharsets.UTF_8);
            out.write(data);
            out.flush();

        } catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            // 非同期処理で同時に TcpClient が使用されると mSocket が null になることを想定した Fail safe
            DTVTLogger.debug(e);
            return false;
        }
        DTVTLogger.end();
        return true;
    }

    /**
     * STBへメッセージ（暗号化されたbinaryデータ）を送信する.
     * @param data byte配列
     * @param length byte配列長
     * @return 成功:true
     */
    public boolean send(final byte[] data, final int length) {
        DTVTLogger.start();
        if (mSocket == null) {
            DTVTLogger.debug("socket is null!");
            return false;
        }

        boolean result = false;
        OutputStream out;
        try {
            out = mSocket.getOutputStream();
            out.write(data, 0, length);
            out.flush();
            result = true;
        } catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            // 非同期処理で同時に TcpClient が使用されると mSocket が null になることを想定した Fail safe
            DTVTLogger.debug(e);
        }
        DTVTLogger.debug("result = " + result);
        DTVTLogger.end();
        return result;
    }
}