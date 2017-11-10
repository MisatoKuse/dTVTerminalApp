/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplayEventListener;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItem;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItemParam;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;

/**
 * クラス機能：
 * プロジェクトにて、すべての「Activity」のベースクラスである
 * 「Activity」全体にとって、共通の機能があれば、追加すること
 */

public class BaseActivity extends FragmentActivity implements MenuDisplayEventListener {

    private LinearLayout baseLinearLayout;
    private RelativeLayout headerLayout;
    private TextView titleTextView;

    /**
     * Created on 2017/09/21.
     * 関数機能：
     * 「Activity」の「画面ID」を戻す。
     * 各ActivityにてOverrideする関数である。
     *
     * @return 「Activity」の「画面ID」を戻す。
     */
    public String getScreenID() {
        return "";
    }

    /**
     * Created on 2017/09/21.
     * 関数機能：
     * 「Activity」の「画面タイトル」を戻す。
     * 各ActivityにてOverrideする関数である。
     *
     * @return 「Activity」の「画面タイトル」を戻す。
     */
    public String getScreenTitle() {
        return "";
    }

    /**
     * 関数機能：
     * Activityを起動する
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);

            //コンテンツ種別によるコンテンツ詳細(プレイヤーあり/なし)起動判定
            OtherContentsDetailData detailData =
                    bundle.getParcelable(DtvContentsDetailActivity.DTV_INFO_BUNDLE_KEY);
            if (detailData != null) {
                int intServiceId = detailData.getServiceId();

                switch (intServiceId) {
                    case DtvContentsDetailActivity.DTV_CONTENTS_SERVICE_ID:
                    case DtvContentsDetailActivity.D_ANIMATION_CONTENTS_SERVICE_ID:
                    case DtvContentsDetailActivity.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                        Class<?> aClass = DtvContentsDetailActivity.class;
                        intent = new Intent(this, aClass);
                        intent.putExtra(DtvContentsDetailActivity.DTV_INFO_BUNDLE_KEY, detailData);
                        startActivity(intent);
                    default:
                        //TODO:プレイヤー付きのコンテンツ詳細画面を起動
                }
            }
        }
        startActivity(intent);
    }

    /**
     * タイトルビュー
     *
     * @param resId
     */
    @Override
    public void setContentView(int resId) {
        DTVTLogger.start("resId:" + resId);
        View view = getLayoutInflater().inflate(resId, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        baseLinearLayout.addView(view);
        DTVTLogger.end();
    }

    /**
     * タイトルビユー初期化
     */
    private void initView() {
        DTVTLogger.start();
        baseLinearLayout = findViewById(R.id.base_ll);
        headerLayout = findViewById(R.id.base_title);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getHeightDensity() / 15);
        headerLayout.setLayoutParams(lp);
        titleTextView = findViewById(R.id.header_layout_text);
        DTVTLogger.end();
    }

    /**
     * タイトルを隠す
     */
    protected void setNoTitle() {
        if (headerLayout != null) {
            headerLayout.setVisibility(View.GONE);
        }
    }

    /**
     * タイトル内容を設定
     *
     * @param c
     */
    protected void setTitleText(CharSequence c) {
        if (titleTextView != null) {
            titleTextView.setText(c);
        }
    }

    //契約・ペアリング済み用
    protected void onSampleGlobalMenuButton_PairLoginOk() {
        MenuItemParam param = new MenuItemParam();
        param.setParamForContractOkPairingOk(3, 1, 2, 6, 8);
        setUserState(param);
        displayMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DTVTLogger.start();
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initView();
        DTVTLogger.end();
    }

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 機能
     * ダブルクリック防止
     */
    protected boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 機能
     * 密度取得
     *
     * @return 密度
     */
    protected float getDensity() {
        return getDisplayMetrics().density;
    }

    /**
     * 機能
     * ディスプレイ幅さ取得
     *
     * @return ディスプレイ幅さ
     */
    protected int getWidthDensity() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 機能
     * ディスプレイ幅さ取得
     *
     * @return ディスプレイ幅さ
     */
    protected int getHeightDensity() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 機能
     * ディスプレイインスタンス取得
     *
     * @return ディスプレイインスタンス
     */
    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 機能
     * カレントユーザ名を戻す
     *
     * @return カレントユーザ名
     */
    public String getUserName() {
        return "Test User";
    }

    private static UserState sUserState = UserState.LOGIN_NG;

    public UserState getUserState() {
        return sUserState;
    }

    public void setUserState(MenuItemParam param) {
        synchronized (this) {
            sUserState = param.getUserState();
            MenuDisplay menu = MenuDisplay.getInstance();
            try {
                menu.setActivityAndListener(this, this);
            } catch (Exception e) {
                DTVTLogger.debug(e);
                return;
            }
            menu.changeUserState(param);
        }
    }

    public void displayMenu() {
        try {
            MenuDisplay.getInstance().display();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ステータスバーの色変更
     *
     * @param resId colorリソースID
     */
    protected void setStatusBarColor(int resId) {
        //ステータスバーの色変更方法をLOLLIPOPを境界に変更する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor(getString(resId)));
        } else {
            setTheme(R.style.ContentsDetailTheme);
        }
    }

    @Override
    public void onPreUserStateChange(UserState oldUserState, UserState newUserState) {

    }

    @Override
    public void onUserStateChanged(UserState oldUserState, UserState newUserState) {

    }

    @Override
    public void onMenuItemSelected(MenuItem menuItem) {

    }
}
