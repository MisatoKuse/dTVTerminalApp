/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;

public class ChannelDataProvider {

    private Context mContext = null;

    /**
     * ディスプレイタイプ.
     */
    private static final String[] DISPLAY_TYPE = {"", "hikaritv", "dch"};

    public ChannelDataProvider(Context mContext) {
        this.mContext = mContext;
    }

    //仮メソッド
    //チャンネル一覧用データリクエスト～DB保存処理作成
    //DtvContentsDetailDataProvider等3つくらいのクラスで別々にやってるので統合
    /**
     * CH一覧取得.
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void requestChannelList(final int limit, final int offset, final String filter, final int type) {
//        DateUtils dateUtils = new DateUtils(mContext);
//        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
//        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
//            //データをDBから取得する
//            Handler handler = new Handler(); //チャンネル情報更新
//            try {
//                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
//                t.start();
//            } catch (Exception e) {
//                DTVTLogger.debug(e);
//            }
//        } else {
//            dateUtils.addLastProgramDate(DateUtils.CHANNEL_LAST_UPDATE);
//            ChannelWebClient mChannelList = new ChannelWebClient(mContext);
//            mChannelList.getChannelApi(limit, offset, filter, DISPLAY_TYPE[type],
//                    (ChannelWebClient.ChannelJsonParserCallback) mContext);
//        }
    }
}
