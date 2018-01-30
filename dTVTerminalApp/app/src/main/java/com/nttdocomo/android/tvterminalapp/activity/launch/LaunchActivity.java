/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvBsChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvTerChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListListener;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * アプリ起動時に最初に呼び出されるActivity.
 */
public class LaunchActivity extends BaseActivity implements View.OnClickListener,
        DlnaRecVideoListener, DlnaTerChListListener, DlnaBsChListListener {

    /**
     * 初回起動判定Flag.
     */
    private static boolean mIsFirstRun = true;
    /**
     * test用ボタン.
     */
    private Button mFirstLaunchLaunchYesActivity = null;
    /**
     * test用ボタン.
     */
    private Button mFirstLaunchLaunchNoActivity = null;
    /**
     * 録画ビデオ一覧問い合わせ用.
     */
    private DlnaProvRecVideo mDlnaProvRecVideo = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DlnaProvTerChList dlnaProvTerChList;
        DlnaProvBsChList dlnaProvBsChList;

        setContentView(R.layout.launch_main_layout);
        enableHeaderBackIcon(false);

        boolean isDlnaOk = startDlna();
        if (!isDlnaOk) {
            DTVTLogger.debug("BaseActivity");
            /*
             * to do: DLNA起動失敗の場合、仕様はないので、ここで将来対応
             */
        } else {
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
            if (null == dlnaDmsItem) {
                /*
                 * to do: ペアリングするか、ここで将来対応
                 */
                return;
            }
            mDlnaProvRecVideo = new DlnaProvRecVideo();
            mDlnaProvRecVideo.start(dlnaDmsItem, this);
            mDlnaProvRecVideo.browseRecVideoDms();

            dlnaProvTerChList = new DlnaProvTerChList();
            dlnaProvTerChList.start(dlnaDmsItem, this);
            dlnaProvTerChList.browseChListDms();

            dlnaProvBsChList = new DlnaProvBsChList();
            dlnaProvBsChList.start(dlnaDmsItem, this);
            dlnaProvBsChList.browseChListDms();
        }
        setContents();
    }

    /**
     * 画面設定を行う.
     */
    private void setContents() {
        TextView title = findViewById(R.id.titleLanchActivity);
        title.setText(getScreenTitle());

        mFirstLaunchLaunchYesActivity = findViewById(R.id.firstLanchLanchYesActivity);
        mFirstLaunchLaunchYesActivity.setOnClickListener(this);

        mFirstLaunchLaunchNoActivity = findViewById(R.id.firstLanchLanchNoActivity);
        mFirstLaunchLaunchNoActivity.setOnClickListener(this);
        // TODO チュートリアル実装時にコメントアウトを外す
//        if(SharedPreferencesUtils.getSharedPreferencesIsDisplayedTutorial(this)) {
//            doScreenTransition();
//        } else {
//            startActivity(TutorialActivity.class, null);
//        }
    }

    /**
     * 機能： Dlnaを開始.
     *
     * @return true: 成功　　false: 失敗
     */
    private boolean startDlna() {
        DlnaInterface di = DlnaInterface.getInstance();
        boolean ret;
        if (null == di) {
            ret = false;
        } else {
            ret = di.startDlna();
        }
        return ret;
    }

    @Override
    protected void onResume() {
        if (!mIsFirstRun) {
            mFirstLaunchLaunchYesActivity.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        mDlnaProvRecVideo.stopListen();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public String getScreenID() {
        return getString(R.string.str_launch_title);
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_launch_title);
    }

    // TODO チュートリアル画面作成時に削除
    @Override
    public void onClick(final View v) {
        if (v.equals(mFirstLaunchLaunchYesActivity)) {
            onFirstLaunchYesButton();
        } else if (v.equals(mFirstLaunchLaunchNoActivity)) {
            doScreenTransition();
        }
    }

    /**
     * 初回起動判定.
     *
     * @return 初回起動かどうか
     */
    public static boolean isFirstRun() {
        return mIsFirstRun;
    }

    /**
     * 初回起動判定値設定.
     */
    public static void setNotFirstRun() {
        LaunchActivity.mIsFirstRun = false;
    }


    /**
     * チュートリアル画面へ遷移.
     */
    // TODO チュートリアル画面作成時に削除
    private void onFirstLaunchYesButton() {
        startActivity(TutorialActivity.class, null);
    }

    /**
     * 次画面遷移.
     */
    private void doScreenTransition() {
        DTVTLogger.start();
        if (SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
            // ペアリング済み HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, true);
            startActivity(HomeActivity.class, null);
            DTVTLogger.debug("ParingOK Start HomeActivity");
        } else if (SharedPreferencesUtils.getSharedPreferencesStbSelect(this)) {
            // 次回から表示しないをチェック済み
            // 未ペアリング HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, false);
            startActivity(HomeActivity.class, null);
            DTVTLogger.debug("ParingNG Start HomeActivity");
        } else {
            // STB選択画面へ遷移
            Intent intent = new Intent(getApplicationContext(), STBSelectActivity.class);
            intent.putExtra(STBSelectActivity.FROM_WHERE, STBSelectActivity.STBSelectFromMode.STBSelectFromMode_Launch.ordinal());
            startActivity(intent);
            DTVTLogger.debug("Start STBSelectActivity");
        }
        DTVTLogger.end();
    }

    @Override
    public void onVideoBrows(final DlnaRecVideoInfo curInfo) {

    }

    @Override
    public void onListUpdate(final DlnaTerChListInfo curInfo) {

    }

    @Override
    public void onListUpdate(final DlnaBsChListInfo curInfo) {

    }

    @Override
    public String getCurrentDmsUdn() {
        return null;
    }
}