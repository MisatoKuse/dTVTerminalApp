package com.nttdocomo.android.tvterminalapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadService;

/**
 * クラス機能：
 * プロセスとして起動する際に呼び出されるApplicationクラス
 */

public class TvtApplication extends Application implements Application.ActivityLifecycleCallbacks{

    public enum LifecycleStatus {
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY,
    }

    private static LifecycleStatus mLifecycleStatus = null;

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mLifecycleStatus = LifecycleStatus.CREATE;
        DTVTLogger.debug(activity.getLocalClassName() + " : Activity Status onCreate");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mLifecycleStatus = LifecycleStatus.DESTROY;
        DTVTLogger.debug(activity.getLocalClassName() + " : Activity Status onDestroy");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(mLifecycleStatus == LifecycleStatus.PAUSE) {
            // BG → FG
            DTVTLogger.debug(activity.getLocalClassName() + " : Change Application : Foreground");
        }
        mLifecycleStatus = LifecycleStatus.RESUME;
        DTVTLogger.debug(activity.getLocalClassName() + " : Activity Status onResume");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mLifecycleStatus = LifecycleStatus.PAUSE;
        DTVTLogger.debug(activity.getLocalClassName() + " : Activity Status onPause");
        DTVTLogger.debug(activity.getLocalClassName() + " : Change Application : Background");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(mLifecycleStatus == LifecycleStatus.STOP) {
            // BG → FG
            DTVTLogger.debug(activity.getLocalClassName() + " : Change Application Status : Foreground");
        }
        mLifecycleStatus = LifecycleStatus.START;
        DTVTLogger.debug(activity.getLocalClassName() + " : Activity Status onStart");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mLifecycleStatus = LifecycleStatus.STOP;
        DTVTLogger.debug(activity.getLocalClassName() + " : Activity Status onStop");
        DTVTLogger.debug(activity.getLocalClassName() + " : Change Application : Background");
    }

    /**
     * アプリケーションがFGであるかどうかを判定
     * @return
     */
    public static boolean isApplicationForeground() {
        boolean result;
        switch (mLifecycleStatus) {
            case CREATE:
            case START:
            case RESUME:
                result = true;
                break;
            case PAUSE:
            case STOP:
            case DESTROY:
                result = false;
                break;
            default:
                result = true;
                break;
        }
        return result;
    }
}
