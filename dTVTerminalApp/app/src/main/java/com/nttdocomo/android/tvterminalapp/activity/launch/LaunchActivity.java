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


public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    public static final String mStateFromTutorialActivity="fromTutorialActivity";
    public static final String mStateToHomePairingOk="ホーム画面（ペアリング済）";
    public static final String mStateToHomePairingNg="ホーム画面（未ペアリング）";

    private static boolean mIsFirstRun=true;

    Button firstLanchLanchYesActivity=null;
    Button firstLanchLanchNoActivity=null;

    Button pairYesLanchActivity=null;
    Button pairNoLanchActivity=null;

    Button mStbWifiYesLanchActivity=null;
    Button mStbWifiNoLanchActivity=null;



    private String mState="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_main_layout);

        setContens();
    }

    private void setContens() {
        TextView title= (TextView)findViewById(R.id.titleLanchActivity);
        title.setText(getScreenTitle());

        firstLanchLanchYesActivity= (Button)findViewById(R.id.firstLanchLanchYesActivity);
        firstLanchLanchYesActivity.setOnClickListener(this);

        firstLanchLanchNoActivity= (Button)findViewById(R.id.firstLanchLanchNoActivity);
        firstLanchLanchNoActivity.setOnClickListener(this);

        pairYesLanchActivity= (Button)findViewById(R.id.pairYesLanchActivity);
        pairYesLanchActivity.setOnClickListener(this);

        pairNoLanchActivity= (Button)findViewById(R.id.pairNoLanchActivity);
        pairNoLanchActivity.setOnClickListener(this);

        mStbWifiYesLanchActivity= (Button)findViewById(R.id.stbWifiYesLanchActivity);
        mStbWifiYesLanchActivity.setOnClickListener(this);

        mStbWifiNoLanchActivity= (Button)findViewById(R.id.stbWifiNoLanchActivity);
        mStbWifiNoLanchActivity.setOnClickListener(this);


        Bundle b= getIntent().getExtras();
        try {
            mState = b.getString("state");
        } catch (Exception e) {

        }
        if(mState.equals(mStateFromTutorialActivity)){
            state1();
        }
    }

    private void state1(){
        onFirstLanchNoButton();
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

    @Override
    public void onClick(View v) {
        if(v.equals(firstLanchLanchYesActivity)){
            onFirstLanchYesButton();
        } else if (v.equals(firstLanchLanchNoActivity)) {
            onFirstLanchNoButton();
        } else if (v.equals(pairYesLanchActivity)) {
            onPairYesButton();
        } else if(v.equals(pairNoLanchActivity)) {
            onPairNoButton();
        } else if (v.equals(mStbWifiYesLanchActivity)) {
            onStbWifiYesButton();
        } else if(v.equals(mStbWifiNoLanchActivity)) {
            onStbWifiNoButton();
        }
    }

    public static boolean isFirstRun() {
        return mIsFirstRun;
    }

    public static void setNotFirstRun() {
        LaunchActivity.mIsFirstRun = false;
    }

    /**
     * STB選択画面へ
     */
    private void onStbWifiYesButton() {
        startActivity(STBSelectActivity.class, null);
    }

    /**
     * ペアリング勧誘
     * ※一度表示されたら以降表示されない
     */
    private void onStbWifiNoButton() {
        startActivity(STBParingInvitationActivity.class, null);
    }

    private void onPairNoButton() {
        //showDAccountYesNoButtons(View.VISIBLE);
        showPairYesNoButtons(View.GONE);
        showStbWifiYesNoButtons(View.VISIBLE);
    }


    /**
     * HOME(ペアリング済)画面へ遷移
     */
    private void onPairYesButton() {
        Bundle b=new Bundle();
        b.putString("state", mStateToHomePairingOk);
        startActivity(HomeActivity.class, b);
    }

    /**
     * チュートリアル画面へ遷移
     */
    private void onFirstLanchYesButton(){
        startActivity(TutorialActivity.class, null);
    }

    private void onFirstLanchNoButton() {
        //mIsFirstLanch=false;
        showPairYesNoButtons(View.VISIBLE);
        hideFirstLanchButtons(View.GONE);
    }

    private void showPairYesNoButtons(int visibility){
        pairYesLanchActivity.setVisibility(visibility);
        pairNoLanchActivity.setVisibility(visibility);
    }

    private void hideFirstLanchButtons(int visibility){
        firstLanchLanchYesActivity.setVisibility(visibility);
        firstLanchLanchNoActivity.setVisibility(visibility);
    }

    private void showStbWifiYesNoButtons(int visibility){
        mStbWifiYesLanchActivity.setVisibility(visibility);
        mStbWifiNoLanchActivity.setVisibility(visibility);
    }

}
