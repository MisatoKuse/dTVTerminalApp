/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;

import com.nttdocomo.android.ocsplib.bouncycastle.util.encoders.EncoderException;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherConfig;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 中継アプリとの Socket通信処理.
 */

public class StbConnectRelayClient {
    /**リモートIP.*/
    private String mRemoteIp;
    /**socket port初期値.*/
    private static final int REMOTE_SOCKET_PORT = 1025;
    /**datagram port初期値.*/
    private static final int REMOTE_DATAGRAM_PORT = 1026;
    /**charset_utf8.*/
    private static final String CHARSET_UTF8 = "UTF-8";
    /**SocketPort.*/
    private int mRemoteSocketPort = REMOTE_SOCKET_PORT;
    /**datagram port.*/
    private int mRemoteDatagramPort = REMOTE_DATAGRAM_PORT;
    /**TcpClient.*/
    private TcpClient mTcpClient;

    /**
     * TcpClient.
     */
    private static final StbConnectRelayClient mInstance = new StbConnectRelayClient();

    /**
     * シングルトン.
     */
    private StbConnectRelayClient() {
    }

    /**
     * シングルトン・インスタンス.
     *
     * @return mInstance
     */
    public static StbConnectRelayClient getInstance() {
        return mInstance;
    }

    /**
     * STBとSocket通信を開始する.
     * Socketに送受信タイムアウト（接続タイムアウト含む）を設定する
     *
     * @return 成功:true
     */
    public boolean connect() {
        if (mRemoteIp == null) {
            DTVTLogger.warning("mRemoteIp is null!");
            return false;
        }
        this.disconnect();

        mTcpClient = new TcpClient();
        return mTcpClient.connect(mRemoteIp, mRemoteSocketPort);
    }

    /**
     * Socket通信を切断する.
     */
    public void disconnect() {
        if (mTcpClient != null) {
            mTcpClient.disconnect();
            mTcpClient = null;
        }
    }

    /**
     * 　STBへメッセージ（JSON形式）を送信後する.
     *
     * @param data data
     * @return 成功:true
     */
    public boolean send(final String data) {
        boolean ret = false;
        if (mTcpClient == null) {
            DTVTLogger.debug("mTcpClient is null!");
            return false;
        }
        try {
            byte[] actionBytes = new byte[CipherConfig.BYTE_LENGTH_ACTION];
            CipherUtil.writeInt(CipherConfig.ACTION_COMMON, actionBytes);

            byte[] encodedData = CipherUtil.encodeData(data);
            byte[] sum = new byte[actionBytes.length + encodedData.length];
            System.arraycopy(actionBytes, 0, sum, 0, actionBytes.length);
            System.arraycopy(encodedData, 0, sum, actionBytes.length, encodedData.length);

            ret = mTcpClient.send(sum, sum.length);
        } catch (EncoderException e) {
            DTVTLogger.debug(e);
        }
        return ret;
    }

    /**
     * STBへメッセージを送信する.
     * @param data 送信するメッセージ
     * @param length メッセージbyte配列長
     * @return 成功true
     */
    public boolean send(final byte[] data, final int length) {
        boolean ret;
        if (mTcpClient == null) {
            DTVTLogger.warning("mTcpClient is null!");
            return false;
        }

        ret = mTcpClient.send(data, length);
        return ret;
    }

    /**
     * STBへメッセージ送信後に応答（JSON形式）を受信する.
     *
     * @return 成功:true
     */
    public String receive() {
        String receivedData = null;
        if (mTcpClient == null) {
            DTVTLogger.warning("mTcpClient is null!");
            return null;
        }
        receivedData = mTcpClient.receive();
        DTVTLogger.debug("receivedData = " + receivedData);
        return receivedData;
    }
    /**
     * 鍵交換リクエストの結果を受信する.
     *
     * @return 成功:true
     */
    public boolean receiveExchangeKey() {
        boolean result = false;
        if (mTcpClient == null) {
            DTVTLogger.debug("mTcpClient is null!");
            return false;
        }
        result = mTcpClient.receiveExchangeKey();
        DTVTLogger.debug("result = " + result);
        return result;
    }
    /**
     * 　STBへメッセージを送信後する.
     *
     * @param data data
     */
    public void sendDatagram(final String data) {

        DatagramSocket dataSocket = null;
        try {
            if (data != null) {
                if (mRemoteIp == null) {
                    return;
                }
                byte[] buff = CipherUtil.encodeData(data);
                int buffLength = 0;
                if (buff != null) {
                    buffLength = buff.length;
                }
                DatagramPacket packet = new DatagramPacket(buff, buffLength, new InetSocketAddress(mRemoteIp, mRemoteDatagramPort));
                dataSocket = new DatagramSocket();
                try {
                    dataSocket.send(packet);
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
        } catch (SocketException e) {
            DTVTLogger.debug(e);
        } finally {
            if (dataSocket != null) {
                dataSocket.close();
            }
        }
    }


    /**
     * Socket通信の送信先のIPアドレスを設定.
     *
     * @param remoteIp remoteIp
     */
    public void setRemoteIp(final String remoteIp) {
        mRemoteIp = remoteIp;
    }

    /**
     * Socket通信の送信先の受信ポート.
     *
     * @param remoteSocketPort remoteSocketPort
     */
    public void setRemoteSocketPort(final int remoteSocketPort) {
        mRemoteSocketPort = remoteSocketPort;
    }

    /**
     * Datagram通信の送信先の受信ポート.
     *
     * @param remoteDatagramPort remoteDatagramPort
     */
    public void setRemoteDatagramPort(final int remoteDatagramPort) {
        mRemoteDatagramPort = remoteDatagramPort;
    }

}
