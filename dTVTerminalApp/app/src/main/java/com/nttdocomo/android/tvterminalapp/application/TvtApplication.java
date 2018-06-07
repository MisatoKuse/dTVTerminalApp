/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nttdocomo.android.tvterminalapp.broadcastreceiver.NetworkBroadcastReceiver;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.GoogleAnalyticsConstants;

/**
 * クラス機能：
 * プロセスとして起動する際に呼び出されるApplicationクラス.
 */

public class TvtApplication extends Application implements Application.ActivityLifecycleCallbacks {
    /**
     * Started状態のActivityの数.
     */
    private int mOnStartedCounter = 0;
    /**
     * Started状態の前回状態を保存.
     */
    private int mTmpStartedCounter = 0;
    /**
     * GoogleAnalytics用クラス(Google提示の設定例がどちらもstaticになっている).
     */
    private static GoogleAnalytics sAnalytics;
    /**
     * Tracker.
     */
    private static Tracker sTracker;
    /**
     * ネットワーク監視.
     */
    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        //Googleアナリティクスの情報収集
        registReceiver();
        sAnalytics = GoogleAnalytics.getInstance(this);
        registerActivityLifecycleCallbacks(this);
        DTVTLogger.debug("application onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNetworkBroadcastReceiver);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        DTVTLogger.end();
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        DTVTLogger.end();
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        DTVTLogger.end();
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        DTVTLogger.end();
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        // Startedのカウントを増やす
        mTmpStartedCounter = mOnStartedCounter;
        mOnStartedCounter++;

        DTVTLogger.debug("Started : " + mOnStartedCounter);
        DTVTLogger.end();
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        // Startedのカウントを減らす
        mOnStartedCounter--;

        DTVTLogger.debug("Started : " + mOnStartedCounter);
        DTVTLogger.end();
    }

    /**
     * アプリケーションが非表示から変わったかどうかを判定.
     *
     * @return 判定結果
     */
    public boolean getIsChangeApplicationVisible() {
        return !(mTmpStartedCounter > 0);
    }

    /**
     * アプリケーションが非表示に変わったかどうかを判定.
     *
     * @return 判定結果
     */
    public boolean getIsChangeApplicationInvisible() {
        return (mTmpStartedCounter <= 0);
    }

    /**
     * トラッカーの取得.
     *
     * @return トラッカーのインスタンス
     */
    synchronized public Tracker getDefaultTracker() {
        //トラッカーがヌルならば新たに取得
       if (sTracker == null) {
            sTracker = sAnalytics.newTracker(GoogleAnalyticsConstants.GOOGLE_ANALYTICS_ID);
        }
        return sTracker;
    }

    /**
     * レシーバー登録.
     */
    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkBroadcastReceiver = new NetworkBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNetworkBroadcastReceiver, filter);
    }
}