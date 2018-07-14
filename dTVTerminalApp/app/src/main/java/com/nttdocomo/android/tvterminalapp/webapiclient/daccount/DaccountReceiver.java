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
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

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

    /**
     * 結果を返すコールバック(STB選択画面用).
     */
    public interface DaccountChangedCallBackForStb {
        /**
         * dアカ変更があった時に通知する(STB画面用).
         * @param dAccount dアカウント
         */
        void onChangeAndSendDaccount(String dAccount);
    }

    /**前回受信時間.*/
    private static long sBeforeReceiveTime = 0;

    /**連続通信扱い時間・5秒とする.*/
    private static final long REPEAT_RECIEVE_TIME = 5000L;

    /**通知Intentのextrasのキー名.*/
    private static final String DOCOMO_ID = "DOCOMO_ID";

    /**コールバッククラス.*/
    private static DaccountChangedCallBack sDaccountChangedCallBack;

    /**コールバッククラス(STB画面用).*/
    private volatile static DaccountChangedCallBackForStb sDaccountChangedCallBackForStb;

    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
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

        Object docomoIdObject = intentBundle.get(DaccountReceiver.DOCOMO_ID);
        //内容で処理を振り分ける
        switch (role) {
            case DaccountConstants.SET_ID_RECEIVER:
                //デフォルトIDの設定通知を受け取った。
                DTVTLogger.debug("SET_ID_RECEIVER");

                //ユーザーが登録された場合は、キャッシュクリアを呼ぶ。
                DaccountControl.cacheClear(context);
                if (sDaccountChangedCallBack != null) {
                    sDaccountChangedCallBack.onChanged();
                }
                DTVTLogger.debug("sDaccountChangedCallBackForStb="
                        + sDaccountChangedCallBackForStb);
                //特殊な動きをするSTB選択画面に、dアカウントの変更を伝える（STB選択画面以外はコールバックがヌルなので、動作しない）
                if (sDaccountChangedCallBackForStb != null && docomoIdObject != null) {
                    sDaccountChangedCallBackForStb.onChangeAndSendDaccount(
                            docomoIdObject.toString());
                }

                break;
            case DaccountConstants.USER_AUTH_RECEIVER:
                //ユーザー認証通知を受け取った。
                DTVTLogger.debug("USER_AUTH_RECEIVER");

                if (docomoIdObject != null && docomoIdObject.toString().equals(SharedPreferencesUtils.getSharedPreferencesDaccountId(context))) {
                    //ServiceTokenを使い回す実装をする場合、ServiceTokenをここでクリアする必要がある。
                } else {
                    //再認証されたdocomoIdとDTVTアプリで使われているIdが違う場合に、キャッシュクリアを呼ぶ。
                    DaccountControl.cacheClear(context);

                }
                if (sDaccountChangedCallBack != null) {
                    sDaccountChangedCallBack.onChanged();
                }

                //特殊な動きをするSTB選択画面に、dアカウントの変更を伝える（STB選択画面以外はコールバックがヌルなので、動作しない）
                if (sDaccountChangedCallBackForStb != null) {
                    sDaccountChangedCallBackForStb.onChangeAndSendDaccount(
                            docomoIdObject.toString());
                }

                break;
            case DaccountConstants.DELETE_ID_RECEIVER:
                //ユーザー削除通知を受け取った。
                DTVTLogger.debug("DELETE_ID_RECEIVER");

                //DTVTアプリログインしているユーザーが削除されていた場合のみ、キャッシュクリアを呼ぶ。
                if (docomoIdObject != null && docomoIdObject.toString().equals(SharedPreferencesUtils.getSharedPreferencesDaccountId(context))) {
                    DaccountControl.cacheClear(context);
                    if (sDaccountChangedCallBack != null) {
                        sDaccountChangedCallBack.onChanged();
                    }

                    //特殊な動きをするSTB選択画面に、dアカウントの削除を伝える（STB選択画面以外はコールバックがヌルなので、動作しない）
                    if (sDaccountChangedCallBackForStb != null) {
                        sDaccountChangedCallBackForStb.onChangeAndSendDaccount("");
                    }

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

    /**
     * コールバック設定/解除.
     * @param callback コールバッククラス
     */
    public static void setDaccountChangedCallBack(final DaccountChangedCallBack callback) {
        sDaccountChangedCallBack = callback;
    }


    /**
     * コールバック設定/解除(STB選択画面用).
     * @param callback コールバッククラス
     */
    public static void setDaccountChangedCallBackForStb(
            final DaccountChangedCallBackForStb callback) {
        DTVTLogger.start("setDaccountChangedCallBackForStb="+callback);
        //コールバックの指定・コールバック解除の場合ヌルが指定されるので、ここはヌルチェックは無用
        sDaccountChangedCallBackForStb = callback;
        DTVTLogger.end();
    }
}