/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * 中継アプリとの Socket通信処理
 */

public class StbConnectRelayClient {
    private Socket mSocket = null;
    private DatagramSocket mSocketDatagram = null;
    private String mRemoteIp;
    private static final int REMOTE_SOCKET_PORT = 1025;
    private static final int REMOTE_DATAGRAM_PORT = 1026;
    private static final int SEND_RECV_TIMEOUT = 1000;
    private int mRemoteSocketPort = REMOTE_SOCKET_PORT;
    private int mRemoteDatagramPort = REMOTE_DATAGRAM_PORT;

    // シングルトン
    private static StbConnectRelayClient mInstance = new StbConnectRelayClient();

    /**
     * シングルトン
     */
    private StbConnectRelayClient() {
    }

    /**
     * シングルトン・インスタンス
     *
     * @return mInstance
     */
    public static StbConnectRelayClient getInstance() {
        return mInstance;
    }

    /**
     * STBとSocket通信を開始する
     * Socketに送受信タイムアウト（接続タイムアウト含む）を設定する
     *
     * @return
     */
    public boolean connect() {
        this.disconnect();
        if (mRemoteIp == null) {
            return false;
        }
        SocketAddress remoteAddress = new InetSocketAddress(mRemoteIp, mRemoteSocketPort);
        mSocket = new Socket();

        try {
            mSocket.connect(remoteAddress, SEND_RECV_TIMEOUT);
        } catch (SocketTimeoutException e) {
            DTVTLogger.debug(e);
            return false;
        } catch (IllegalArgumentException e) {
            DTVTLogger.debug(e);
            return false;
        } catch (IOException e) {
            DTVTLogger.debug(e);
            return false;
        }
        return true;
    }

    /**
     * Socket通信を切断する
     */
    public void disconnect() {
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                    mSocket = null;
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
        }
    }

    /**
     * 　STBへメッセージ（JSON形式）を送信後する
     *
     * @param data
     * @return
     */
    public boolean send(String data) {
        PrintWriter writer = null;
        OutputStreamWriter out = null;

        if (mSocket == null) {
            return false;
        }

        try {
            out = new OutputStreamWriter(mSocket.getOutputStream(), StandardCharsets.UTF_8);
            writer = new PrintWriter(out, true);
            writer.write(data);
            writer.flush();

        } catch (IOException e) {
            DTVTLogger.debug(e);
            return false;
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
        }
        return true;
    }

    /**
     * STBへメッセージ送信後に応答（JSON形式）を受信する
     *
     * @return
     */
    public String recv() {
        InputStreamReader in = null;
        BufferedReader reader = null;
        StringBuilder data = null;
        String recvdata = null;

        if (mSocket == null) {
            return null;
        }
        try {
            in = new InputStreamReader(mSocket.getInputStream(), StandardCharsets.UTF_8);
            reader = new BufferedReader(in);

            data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
            if (data != null) {
                recvdata = data.toString();
            }
        }
        return recvdata;
    }

    /**
     * 　STBへメッセージを送信後する
     *
     * @param data
     * @return
     */
    public void sendDatagram(String data) {
        try {
            if (data != null) {
                if (mRemoteIp == null) {
                    return;
                }
                byte[] buff = data.getBytes();
                DatagramPacket packet = new DatagramPacket(buff, buff.length, new InetSocketAddress(mRemoteIp, mRemoteDatagramPort));
                mSocketDatagram = new DatagramSocket();
                mSocketDatagram.send(packet);
            }
        } catch (SocketException e) {
            DTVTLogger.debug(e);
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } finally {
            if (mSocketDatagram != null) {
                mSocketDatagram.close();
                mSocketDatagram = null;
            }
        }
    }


    /**
     * Socket通信の送信先のIPアドレスを設定
     *
     * @param remoteIp
     */
    public void setRemoteIp(String remoteIp) {
        mRemoteIp = remoteIp;
    }

    /**
     * Socket通信の送信先の受信ポート
     * @param remoteSocketPort
     */
    public void setRemoteSocketPort(int remoteSocketPort) {
        mRemoteSocketPort = remoteSocketPort;
    }

    /**
     * Datagram通信の送信先の受信ポート
     * @param remoteDatagramPort
     */
    public void setRemoteDatagramPort(int remoteDatagramPort) {
        mRemoteDatagramPort = remoteDatagramPort;
    }

}
