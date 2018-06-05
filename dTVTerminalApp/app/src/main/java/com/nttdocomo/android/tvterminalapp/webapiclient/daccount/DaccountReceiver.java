/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;

import java.util.Set;

/**
 * dアカウントで状況の変化が発生した場合の通知を受け取るレシーバー.
 */
public class DaccountReceiver extends BroadcastReceiver {

    /**
     * 結果を返すコールバック.
     */
    public interface DaccountChangedCallBack {
        /**
         * dアカ変更があった時に通知する.
         */
        void onChanged();
    }

    /**前回受信時間.*/
    private static long sBeforeReceiveTime = 0;

    /**連続通信扱い時間・5秒とする.*/
    private static final long REPEAT_RECIEVE_TIME = 5000L;

    public static DaccountChangedCallBack dAccountChangedCallBack;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DTVTLogger.start();
        //現在日時取得
        long nowTime = System.currentTimeMillis();

        DTVTLogger.debug("receive time:" + nowTime + " before time:" + sBeforeReceiveTime
                + " difference:" + (nowTime - sBeforeReceiveTime)
                + " INTERVAL:" + REPEAT_RECIEVE_TIME
                + " answer:" + (sBeforeReceiveTime + REPEAT_RECIEVE_TIME > nowTime));

        if (sBeforeReceiveTime + REPEAT_RECIEVE_TIME > nowTime) {
            // 前回のdアカウント設定アプリからの通知から極端に短い時間で再通知を受けた場合は、ユーザー切り替えとなる。
            // キャッシュ削除処理の重複防止用に、処理をスキップする
            DTVTLogger.debug("receive short repaeat skip role=" + intent.getAction());
            return;
        }

        //今の時間を前回時間にする
        sBeforeReceiveTime = nowTime;

        //ブロードキャストの原因の取得
        String role = intent.getAction();

        //送られてきたデータをログに表示
        Bundle intentBundle = intent.getExtras();
        Set<String> bundleLists = intentBundle.keySet();
        for (String key : bundleLists) {
            Object bundleData = intentBundle.get(key);
            if (bundleData != null) {
                String buffer = bundleData.toString();
                DTVTLogger.debug("in Data " + key + "=" + buffer);
            }
        }

        //内容で処理を振り分ける
        switch (role) {
            case DaccountConstants.SET_ID_RECEIVER:
                //デフォルトIDの設定通知を受け取った。
                DTVTLogger.debug("SET_ID_RECEIVER");

                //ユーザーが登録された場合は、キャッシュクリアを呼ぶ。
                DaccountControl.cacheClear(context);
                if (dAccountChangedCallBack != null) {
                    dAccountChangedCallBack.onChanged();
                }

                break;
            case DaccountConstants.USER_AUTH_RECEIVER:
                //ユーザー認証通知を受け取った。
                DTVTLogger.debug("USER_AUTH_RECEIVER");

                //ユーザーが登録された場合は、キャッシュクリアを呼ぶ。
                DaccountControl.cacheClear(context);
                if (dAccountChangedCallBack != null) {
                    dAccountChangedCallBack.onChanged();
                }

                break;
            case DaccountConstants.DELETE_ID_RECEIVER:
                //ユーザー削除通知を受け取った。
                DTVTLogger.debug("DELETE_ID_RECEIVER");

                //ユーザーが削除されていた場合は、キャッシュクリアを呼ぶ。
                DaccountControl.cacheClear(context);
                if (dAccountChangedCallBack != null) {
                    dAccountChangedCallBack.onChanged();
                }

                break;
            case DaccountConstants.INVALIDATE_ID_RECEIVER:
                //ユーザー無効化通知を受け取った。
                DTVTLogger.debug("INVALIDATE_ID_RECEIVER");

                //ユーザーが無効化されていた場合は、次のOTT取得時にチェックするので、ここでは処理しない
                break;
            case DaccountConstants.LINKED_LINE_RECEIVER:
                //回線登録通知を受け取った。
                DTVTLogger.debug("INVALIDATE_ID_RECEIVER");
                break;
            case DaccountConstants.SERVICEAPP_REMOVED_RECEIVER:
                //アプリ除外通知を受け取った。
                DTVTLogger.debug("SERVICE_APP_REMOVED_RECEIVER");
                break;
            default:
                //アナライザー対策なので無処理
                break;
        }
    }
}