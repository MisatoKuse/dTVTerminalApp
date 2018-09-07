/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 番組表情報取得WebClientを実行し、終了するまで待つ事で、同期処理を行うラッパー.
 */
public class TvScheduleWebClientSync implements TvScheduleWebClient.TvScheduleJsonParserCallback {

    /** コンテキスト. */
    private Context mContext = null;
    /** チャンネル情報ウェブクライアント. */
    private TvScheduleWebClient mTvScheduleWebClient = null;

    /** 番組表情報. */
    private ChannelInfoList mChannelInfoList = null;
    /** エラー情報. */
    private ErrorState mTvScheduleError = null;

    /** カウントがゼロになるまで処理を停止させるクラス. */
    private CountDownLatch mLatch = null;

    /** 停止クラスの初期値. */
    private static final int LATCH_COUNT_MAX = 1;

    /**
     * コンストラクタ.
     * @param mContext コンテキスト
     */
    public TvScheduleWebClientSync(final Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList, final int[] chnos) {
        DTVTLogger.start();

        //コールバックが返ってきた
        if (tvScheduleList != null) {
            List<Map<String, String>> tvList = tvScheduleList.get(0).geTvsList();

            mChannelInfoList = new ChannelInfoList();
            for (int chno : chnos) {
                ChannelInfo channelInfo = new ChannelInfo();
                channelInfo.setChannelNo(chno);
                ArrayList<ScheduleInfo> scheduleInfoList = new ArrayList<>();
                String ch = String.valueOf(chno);
                for (int j = 0; j < tvList.size(); j++) {
                    if (ch.equals(tvList.get(j).get(JsonConstants.META_RESPONSE_CHNO))) {
                        ScheduleInfo scheduleInfo = DataConverter.convertScheduleInfo(tvList.get(j), null);
                        scheduleInfoList.add(scheduleInfo);
                    }
                }
                if (scheduleInfoList.size() < 1) {
                    scheduleInfoList.add(DataConverter.convertScheduleInfo(DataConverter.getDummyContentMap(mContext, ch, false), null));
                }
                channelInfo.setSchedules(scheduleInfoList);
                mChannelInfoList.addChannel(channelInfo);
            }
        } else {
            //ヌルだったので、エラーを蓄積する
            mTvScheduleError = mTvScheduleWebClient.getError();
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
        return mTvScheduleError;
    }

    /**
     * チャンネル毎番組一覧取得.
     *
     * @param context  コンテキスト
     * @param chno   チャンネル番号
     * @param date   日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return パラメータエラーならばfalse
     */
    public ChannelInfoList getTvScheduleApi(final Context context, final int[] chno, final String[] date, final String filter) {
        DTVTLogger.start();
        mTvScheduleWebClient = new TvScheduleWebClient(context);
        boolean answer = mTvScheduleWebClient.getTvScheduleApi(chno, date, filter, this);

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
            mChannelInfoList = null;
        }

        //ここにたどり着いたときは、既に処理済みとなっている
        if (mChannelInfoList == null) {
            //ヌルならば、エラーとして処理
            mTvScheduleError = mTvScheduleWebClient.getError();
            DTVTLogger.end("web api error");
            return null;
        }

        DTVTLogger.end("normal end");

        //値が存在したので、それを返す
        return mChannelInfoList;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        //通信再開時は番組リスト取得中フラグを初期化する
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.enableConnection();
        }
    }
}