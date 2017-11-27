/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    public static final String mStateFromTutorialActivity="fromTutorialActivity";

    private final static String STATUS = "status";

    private static boolean mIsFirstRun=true;

    Button firstLanchLanchYesActivity=null;
    Button firstLanchLanchNoActivity=null;

    private String mState="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_main_layout);

        setContents();
    }

    /**
     * 画面設定を行う
     */
    private void setContents() {
        TextView title= (TextView)findViewById(R.id.titleLanchActivity);
        title.setText(getScreenTitle());

        firstLanchLanchYesActivity= (Button)findViewById(R.id.firstLanchLanchYesActivity);
        firstLanchLanchYesActivity.setOnClickListener(this);

        firstLanchLanchNoActivity= (Button)findViewById(R.id.firstLanchLanchNoActivity);
        firstLanchLanchNoActivity.setOnClickListener(this);
        // TODO チュートリアル実装時にコメントアウトを外す
//        if(SharedPreferencesUtils.getSharedPreferencesIsDisplayedTutorial(this)) {
//            doScreenTransition();
//        } else {
//            startActivity(TutorialActivity.class, null);
//        }
    }

    @Override
    protected void onResume(){
        if(!mIsFirstRun){
            firstLanchLanchYesActivity.setVisibility(View.GONE);
        }
        super.onResume();
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
    public void onClick(View v) {
        if (v.equals(firstLanchLanchYesActivity)) {
            onFirstLaunchYesButton();
        } else if (v.equals(firstLanchLanchNoActivity)) {
            doScreenTransition();
        }
    }

    /**
     * 初回起動判定
     * @return
     */
    public static boolean isFirstRun() {
        return mIsFirstRun;
    }

    /**
     * 初回起動判定値設定
     */
    public static void setNotFirstRun() {
        LaunchActivity.mIsFirstRun = false;
    }


    /**
     * チュートリアル画面へ遷移
     */
    // TODO チュートリアル画面作成時に削除
    private void onFirstLaunchYesButton(){
        startActivity(TutorialActivity.class, null);
    }

    /**
     * 次画面遷移判定
     */
    private void doScreenTransition() {
        DTVTLogger.start();
        if(SharedPreferencesUtils.getSharedPreferencesStbConnect(this)) {
            // ペアリング済み HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                    this, true);
            startActivity(HomeActivity.class, null);
            DTVTLogger.debug("ParingOK Start HomeActivity");
        } else if(SharedPreferencesUtils.getSharedPreferencesStbSelect(this)){
            // 次回から表示しないをチェック済み
            // 未ペアリング HOME画面遷移
            SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                    this, false);
            startActivity(HomeActivity.class, null);
            DTVTLogger.debug("ParingNG Start HomeActivity");
        } else {
            // STB選択画面へ遷移
            startActivity(STBSelectActivity.class, null);
            DTVTLogger.debug("Start STBSelectActivity");
        }
        DTVTLogger.end();
    }
}
