/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplayEventListener;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItem;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItemParam;

/**
 *
 * クラス機能：
 *      プロジェクトにて、すべての「Activity」のベースクラスである
 *      「Activity」全体にとって、共通の機能があれば、追加すること
 */

public class BaseActivity extends Activity implements MenuDisplayEventListener {

    /**
     * Created on 2017/09/21.
     * 関数機能：
     *      「Activity」の「画面ID」を戻す。
     *      各ActivityにてOverrideする関数である。
     *
     * @return 「Activity」の「画面ID」を戻す。
     */
    public String getScreenID(){
        return "";
    }

    /**
     *  Created on 2017/09/21.
     *  関数機能：
     *      「Activity」の「画面タイトル」を戻す。
     *      各ActivityにてOverrideする関数である。
     * @return 「Activity」の「画面タイトル」を戻す。
     */
    public String getScreenTitle(){
        return "";
    }

    /**
     * 関数機能：
     *      Activityを起動する
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            //intent.putExtra("bundle", bundle);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String TAG = getLocalClassName();
        Log.i(TAG,TAG+"  OｎCreate()");
    }

    /**
     * 機能
     *      カレントユーザ名を戻す
     * @return
     *      カレントユーザ名
     */
    public String getUserName() {
        return "Test User";
    }

    private static UserState sUserState=UserState.LOGIN_NG;

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
                e.printStackTrace();
                return;
            }
            menu.changeUserState(param);
        }
    }

    public void displayMenu(){
        try {
            MenuDisplay.getInstance().display();
        } catch (Exception e) {
            e.printStackTrace();
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
