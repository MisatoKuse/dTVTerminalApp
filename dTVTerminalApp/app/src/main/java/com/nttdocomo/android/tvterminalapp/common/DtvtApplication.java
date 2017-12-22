/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */


package com.nttdocomo.android.tvterminalapp.common;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * アプリのフォアグラウンドとバックグラウンドを検知可能にするクラス
 */
public class DtvtApplication extends Application {
    private ActivityStateChangeHandler mActivityStateChangeHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //アクティビティの状態検知クラスを定義してシステムに登録する
        mActivityStateChangeHandler = new ActivityStateChangeHandler();
        registerActivityLifecycleCallbacks(mActivityStateChangeHandler);
    }

    /**
     * アプリが画面に表示されているかどうかを見る
     *
     * @return 画面に表示されているならばtrue
     */
    public boolean isApplicationVisible() {
        return mActivityStateChangeHandler.startCounter >
                mActivityStateChangeHandler.stopCounter;
    }

    /**
     * アプリがフォアグラウンド状態かどうかを見る
     *
     * @return フォアグラウンド状態ならばtrue
     */
    public boolean isApplicationInForeground() {
        return mActivityStateChangeHandler.resumeCounter >
                mActivityStateChangeHandler.pauseCounter;
    }
}

/**
 * アプリがアクティブか非アクティブかを検知する為に、アクティビティのライフサイクルを見張るクラス
 */
class ActivityStateChangeHandler implements Application.ActivityLifecycleCallbacks {

    /**
     * レジュームカウンター
     */
    int resumeCounter;
    /**
     * ポーズカウンター
     */
    int pauseCounter;
    /**
     * スタートカウンター
     */
    int startCounter;
    /**
     * ストップカウンター
     */
    int stopCounter;

    @Override
    public void onActivityStarted(Activity activity) {
        //スタートしたので、スタートカウンターを加算
        startCounter++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //ストップしたので、ストップカウンターを加算
        stopCounter++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //レジュームしたので、レジュームカウンターを加算
        resumeCounter++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //ポーズしたので、ポーズカウンターを加算
        pauseCounter++;
    }

    //以下はインターフェースの仕様で作成を強要されているだけで、使用しない
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}