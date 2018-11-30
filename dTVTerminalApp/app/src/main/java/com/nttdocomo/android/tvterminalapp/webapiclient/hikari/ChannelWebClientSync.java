/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * チャンネル一覧取得WebClientの実行し、終了するまで待つ事で、同期処理を行うラッパー.
 */
public class ChannelWebClientSync implements ChannelWebClient.ChannelJsonParserCallback {
    /** チャンネル情報ウェブクライアント. */
    private ChannelWebClient mChannelWebClient = null;

    /** チャンネルリスト情報. */
    private List<ChannelList> mChannelLists = null;

    /** エラー情報. */
    private ErrorState mChannelError = null;

    /** カウントがゼロになるまで処理を停止させるクラス. */
    private CountDownLatch mLatch = null;

    /** 停止クラスの初期値. */
    private static final int LATCH_COUNT_MAX = 1;

    @Override
    public void onChannelJsonParsed(final List<ChannelList> channelLists) {
        DTVTLogger.start();

        //コールバックが返ってきた
        if (channelLists != null) {
            //値が返ってきたので格納する
            mChannelLists = channelLists;
        } else {
            //ヌルだったので、エラーを蓄積する
            mChannelError = mChannelWebClient.getError();
        }

        //ウェイトカウンターを減らす
        mLatch.countDown();

        DTVTLogger.end();
    }

    /**
     * 蓄積されているエラーを返す.
     * @return エラー情報
     */
    public ErrorState getError() {
        return mChannelError;
    }

    /**
     * チャンネル一覧を取得し、結果を返す.
     *
     * @param context コンテキスト
     * @param pagetLimit 取得する最大件数(値は1以上)
     * @param pagerOffset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @param areaCode エリアコード
     * @return チャンネル一覧情報
     */
    public List<ChannelList> getChannelApi(final Context context, final int pagetLimit, final int pagerOffset,
                         final String filter, final String type, final String areaCode) {
        DTVTLogger.start();

        mChannelWebClient = new ChannelWebClient(context);
        boolean answer = mChannelWebClient.getChannelApi(pagetLimit, pagerOffset, filter, type, areaCode, this);

        if (!answer) {
            //パラメータエラーだったので、そのまま帰る
            DTVTLogger.end("web api param error");
            return null;
        }

        //WebAPIの処理が終わるまで待機をさせる
        mLatch = new CountDownLatch(LATCH_COUNT_MAX);
        try {
            mLatch.await();
        } catch (InterruptedException e) {
            //リストにヌルを入れて、以下の処理に任せる
            mChannelLists = null;
        }

        //ここにたどり着いたときは、既に処理済みとなっている
        if (mChannelLists == null) {
            //ヌルならば、エラーとして処理
            mChannelError = mChannelWebClient.getError();
            DTVTLogger.end("web api error");
            return null;
        }

        DTVTLogger.end("normal end");

        //値が存在したので、それを返す
        return mChannelLists;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        if (mChannelWebClient != null) {
            mChannelWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        //通信再開時は番組リスト取得中フラグを初期化する
        if (mChannelWebClient != null) {
            mChannelWebClient.enableConnection();
        }
    }
}