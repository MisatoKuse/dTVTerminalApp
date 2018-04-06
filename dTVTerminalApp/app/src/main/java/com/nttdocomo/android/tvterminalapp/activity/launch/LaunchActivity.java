/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.ChildContentListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.launch_main_layout);
        setTitleText(getString(R.string.str_launch_title));
        enableHeaderBackIcon(false);
        setStatusBarColor(true);

        //アプリ起動時のサービストークン削除を行う
        SharedPreferencesUtils.deleteOneTimeTokenData(getApplicationContext());

        boolean isDlnaOk = startDlna();
        if (!isDlnaOk) {
            DTVTLogger.debug("BaseActivity");
            /*
             * to do: DLNA起動失敗の場合、仕様はないので、ここで将来対応
             */
        }
        setContents();
    }

    /**
     * 画面設定を行う.
     */
    private void setContents() {
        mFirstLaunchLaunchYesActivity = findViewById(R.id.firstLanchLanchYesActivity);
        mFirstLaunchLaunchYesActivity.setOnClickListener(this);

        mFirstLaunchLaunchNoActivity = findViewById(R.id.firstLanchLanchNoActivity);
        mFirstLaunchLaunchNoActivity.setOnClickListener(this);

        mFirstLaunchLaunchYesActivity.setEnabled(false);
        mFirstLaunchLaunchYesActivity.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        mFirstLaunchLaunchNoActivity.setEnabled(false);
        mFirstLaunchLaunchNoActivity.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
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
        super.onResume();
    }

    @Override
    protected void onStop() {
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
//            onFirstLaunchYesButton();

            // TODO: 子コンテンツ取得テストのための臨時コードSprintブランチマージ時点で削除
            Intent intent = new Intent(this, ChildContentListActivity.class);
            intent.putExtra(ChildContentListActivity.INTENT_KEY_CRID, "crid://plala.iptvf.jp/group/b0009c3");
            intent.putExtra(ChildContentListActivity.INTENT_KEY_TITLE, "【33％OFF】明日、ママがいない　全9話パック");
            intent.putExtra(ChildContentListActivity.INTENT_KEY_DISP_TYPE, DTVTConstants.DISP_TYPE_VIDEO_PACKAGE);
            startActivity(intent);

        } else if (v.equals(mFirstLaunchLaunchNoActivity)) {
            doScreenTransition();
        }
    }

    /**
     * チュートリアル画面へ遷移.
     */
    // TODO チュートリアル画面作成時に削除
    private void onFirstLaunchYesButton() {
        startActivity(TutorialActivity.class, null);
    }

    @Override
    protected void onDaccountOttGetComplete() {
        super.onDaccountOttGetComplete();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // アプリの起動時はdアカOTTが取得できるまで待つ。他画面には遷移させない。
                mFirstLaunchLaunchYesActivity.setEnabled(true);
                mFirstLaunchLaunchYesActivity.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                mFirstLaunchLaunchNoActivity.setEnabled(true);
                mFirstLaunchLaunchNoActivity.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            }
        });
    }

    /**
     * 次画面遷移.
     */
    private void doScreenTransition() {
        DTVTLogger.start();
        if (SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
            // ペアリング済み HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, true);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            DTVTLogger.debug("ParingOK Start HomeActivity");
        } else if (SharedPreferencesUtils.getSharedPreferencesStbSelect(this)) {
            // 次回から表示しないをチェック済み
            // 未ペアリング HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(this, false);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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

    @Override
    protected void restartMessageDialog(final String... message) {
        // dアカが変わってもHOME遷移させない
    }
}
