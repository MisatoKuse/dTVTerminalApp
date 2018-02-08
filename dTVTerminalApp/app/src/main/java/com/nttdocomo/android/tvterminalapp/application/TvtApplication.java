/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * クラス機能：
 * プロセスとして起動する際に呼び出されるApplicationクラス.
 */

public class TvtApplication extends Application implements Application.ActivityLifecycleCallbacks {
    /**
     * ActivityのLifecycle記録用マップ.
     */
    private Map<String, Map<LIFECYCLE_TYPE, Integer>> mActivityMap = null;

    /**
     * LifecycleMap用のキー値.
     */
    private enum LIFECYCLE_TYPE {
        /**
         * LifecycleMap用のキー値（onStart).
         */
        START,
        /**
         * LifecycleMap用のキー値（onResume).
         */
        RESUME,
        /**
         * LifecycleMap用のキー値（onPause).
         */
        PAUSE,
        /**
         * LifecycleMap用のキー値（onStop).
         */
        STOP
    }
    /**
     * 非表示 → 表示 判定.
     */
    private boolean mIsChangeApplicationVisible = false;
    /**
     * BG → FG 判定.
     */
    private boolean mIsChangeApplicationForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        DTVTLogger.debug("application onCreate");
        mActivityMap = new HashMap<>();
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
        if (!mActivityMap.containsKey(activity.getLocalClassName())) {
            mActivityMap.put(activity.getLocalClassName(), createNewActivityMap());
        }
        DTVTLogger.end();
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());
        mActivityMap.remove(activity.getLocalClassName());
        DTVTLogger.end();
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());

        Map<LIFECYCLE_TYPE, Integer> map = mActivityMap.get(activity.getLocalClassName());
        // Pause後であるならばカウントは一致する
        mIsChangeApplicationForeground = (map.get(LIFECYCLE_TYPE.RESUME) != 0
                && map.get(LIFECYCLE_TYPE.RESUME).equals(map.get(LIFECYCLE_TYPE.PAUSE)));
        Integer cnt = map.get(LIFECYCLE_TYPE.RESUME) + 1;
        map.put(LIFECYCLE_TYPE.RESUME, cnt);
        mActivityMap.put(activity.getLocalClassName(), map);

        DTVTLogger.end();
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());

        Map<LIFECYCLE_TYPE, Integer> map = mActivityMap.get(activity.getLocalClassName());
        Integer cnt = map.get(LIFECYCLE_TYPE.PAUSE) + 1;
        map.put(LIFECYCLE_TYPE.PAUSE, cnt);
        mActivityMap.put(activity.getLocalClassName(), map);

        DTVTLogger.end();
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());

        Map<LIFECYCLE_TYPE, Integer> map = mActivityMap.get(activity.getLocalClassName());
        // Stop後であるならばカウントは一致する
        mIsChangeApplicationVisible = (map.get(LIFECYCLE_TYPE.START) != 0
                && map.get(LIFECYCLE_TYPE.START).equals(map.get(LIFECYCLE_TYPE.STOP)));
        Integer cnt = map.get(LIFECYCLE_TYPE.START) + 1;
        map.put(LIFECYCLE_TYPE.START, cnt);
        mActivityMap.put(activity.getLocalClassName(), map);

        DTVTLogger.end();
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        DTVTLogger.start("ActivityLocalClassName : " + activity.getLocalClassName());

        Map<LIFECYCLE_TYPE, Integer> map = mActivityMap.get(activity.getLocalClassName());
        Integer cnt = map.get(LIFECYCLE_TYPE.STOP) + 1;
        map.put(LIFECYCLE_TYPE.STOP, cnt);
        mActivityMap.put(activity.getLocalClassName(), map);

        DTVTLogger.end();
    }

    /**
     * アプリケーションが非表示から変わったかどうかを判定.
     *
     * @return 判定結果
     */
    public boolean getIsChangeApplicationVisible() {
        return mIsChangeApplicationVisible;
    }

    /**
     * アプリケーションがFGに変わったかどうかを判定.
     *
     * @return 判定結果
     */
    public boolean getIsChangeApplicationInForeground() {
        return mIsChangeApplicationForeground;
    }

    /**
     * 新規Activity用のLifecycleMapを生成.
     *
     * @return 初期化されたLifecycleMap
     */
    private Map<LIFECYCLE_TYPE, Integer> createNewActivityMap() {
        DTVTLogger.start();
        Map<LIFECYCLE_TYPE, Integer> lifecycleMap = new HashMap<>();
        lifecycleMap.put(LIFECYCLE_TYPE.START, 0);
        lifecycleMap.put(LIFECYCLE_TYPE.RESUME, 0);
        lifecycleMap.put(LIFECYCLE_TYPE.PAUSE, 0);
        lifecycleMap.put(LIFECYCLE_TYPE.STOP, 0);

        DTVTLogger.end();
        return lifecycleMap;
    }
}