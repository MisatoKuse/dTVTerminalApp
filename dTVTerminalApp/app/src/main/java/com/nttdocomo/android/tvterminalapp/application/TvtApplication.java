/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

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

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        DTVTLogger.debug("application onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
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

}