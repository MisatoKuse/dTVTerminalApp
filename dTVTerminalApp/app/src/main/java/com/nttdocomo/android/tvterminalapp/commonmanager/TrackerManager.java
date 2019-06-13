/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.commonmanager;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nttdocomo.android.tvterminalapp.common.GoogleAnalyticsConstants;

/**
 * トラッカーマネージャ.
 */
public class TrackerManager {

    /**
     * singletone.
     */
    private static final TrackerManager sInstance = new TrackerManager();
    /**
     * トラッカー.
     */
    private Tracker mTracker;

    /**
     * コンテキスト.
     */
    private Application mAppContext;

    /**
     * TrackerManager.
     */
    private TrackerManager() {
    }

    /**
     * shared.
     * @return Instance
     */
    public static TrackerManager shared() {
        return sInstance;
    }

    /**
     * コンテキストset.
     * @param appContext コンテキスト
     */
    public void setAppContext(final Application appContext) {
        mAppContext = appContext;
    }

    /**
     * コンテキストget.
     * @return コンテキスト
     */
    public Application getAppContext() {
        return mAppContext;
    }

    /**
     * トラッカーの取得.
     *
     * @return トラッカーのインスタンス
     */
    synchronized public Tracker getDefaultTracker() {
        //トラッカーがヌルならば新たに取得
        if (mTracker == null && getAppContext() != null) {
            mTracker = GoogleAnalytics.getInstance(getAppContext()).newTracker(GoogleAnalyticsConstants.getGoogleAnalyticsId());
        }
        return mTracker;
    }
}
