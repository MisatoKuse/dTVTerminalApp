/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private Socket mSocket = null;
    private String mRemoteIp = null;
    private int mRemotePort = 0;

    // STBスタンバイ状態からの電源ONとユーザアカウント切り替えに必要な最大待ち時間（ミリ秒）
    public static final int SEND_RECV_TIMEOUT = 7000;

    public TcpClient() {
        mSocket = null;
        mRemoteIp = null;
    }

    /**
     * Socket通信でSTBへ接続する.
     *
     * @param remoteIp
     * @param remotePort
     * @return
     */
    public boolean connect(String remoteIp, int remotePort) {
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
     * @return
     */
    public synchronized String receive() {
        StringBuilder data = null;
        String recvdata = null;

        DTVTLogger.debug("receive start");
        if (mSocket == null) {
            DTVTLogger.debug("mSocket is null!");
            return null;
        }
        try {
            DTVTLogger.debug("getInputStream()");
            InputStream inputStream = mSocket.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(mSocket.getInputStream(), StandardCharsets.UTF_8);

            data = new StringBuilder();

            while (inputStream.available() >= 0) {
                // 受信待ち
                if (inputStream.available() == 0) {
                    continue;
                }
                DTVTLogger.debug(String.format(Locale.getDefault(), "streamReader.read(%d)", inputStream.available()));
                char[] line = new char[inputStream.available()];
                if (streamReader.read(line) == -1) {
                    continue;
                }
                data.append(String.valueOf(line));
                break;
            }
        }  catch (NullPointerException | IOException e) {
            // SocketException はIOExceptionに含まれる
            DTVTLogger.debug(String.format("??? :%s", e.getMessage()));
        } finally {
            if (data != null && data.length() != 0) {
                recvdata = data.toString();
            }
        }
        return recvdata;
    }

    /**
     * STBへメッセージ（JSON形式）を送信する.
     *
     * @param data
     * @return true 送信した場合
     */
    public boolean send(String data) {

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
}