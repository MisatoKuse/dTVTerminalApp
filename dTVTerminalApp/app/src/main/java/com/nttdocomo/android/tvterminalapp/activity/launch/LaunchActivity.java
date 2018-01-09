/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
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

public class LaunchActivity extends BaseActivity implements View.OnClickListener,
        DlnaRecVideoListener, DlnaTerChListListener, DlnaBsChListListener {

    private static boolean mIsFirstRun = true;

    private Button firstLanchLanchYesActivity = null;
    private Button firstLanchLanchNoActivity = null;
    private DlnaProvRecVideo mDlnaProvRecVideo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
     * 画面設定を行う
     */
    private void setContents() {
        TextView title = findViewById(R.id.titleLanchActivity);
        title.setText(getScreenTitle());

        firstLanchLanchYesActivity = findViewById(R.id.firstLanchLanchYesActivity);
        firstLanchLanchYesActivity.setOnClickListener(this);

        firstLanchLanchNoActivity = findViewById(R.id.firstLanchLanchNoActivity);
        firstLanchLanchNoActivity.setOnClickListener(this);
        // TODO チュートリアル実装時にコメントアウトを外す
//        if(SharedPreferencesUtils.getSharedPreferencesIsDisplayedTutorial(this)) {
//            doScreenTransition();
//        } else {
//            startActivity(TutorialActivity.class, null);
//        }
    }

    /**
     * 機能： Dlnaを開始
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
            firstLanchLanchYesActivity.setVisibility(View.GONE);
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
    public void onClick(View v) {
        if (v.equals(firstLanchLanchYesActivity)) {
            onFirstLaunchYesButton();
        } else if (v.equals(firstLanchLanchNoActivity)) {
            doScreenTransition();
        }
    }

    /**
     * 初回起動判定
     *
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
    private void onFirstLaunchYesButton() {
        startActivity(TutorialActivity.class, null);
    }

    /**
     * 次画面遷移
     */
    private void doScreenTransition() {
        DTVTLogger.start();
        startActivity(CheckStatusActivity.class, null);
        DTVTLogger.end();
    }

    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {

    }

    @Override
    public void onListUpdate(DlnaTerChListInfo curInfo) {

    }

    @Override
    public void onListUpdate(DlnaBsChListInfo curInfo) {

    }

    @Override
    public String getCurrentDmsUdn() {
        return null;
    }
}