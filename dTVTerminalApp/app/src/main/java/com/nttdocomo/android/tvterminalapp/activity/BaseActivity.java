package com.nttdocomo.android.tvterminalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplayEventListener;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItem;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItemParam;
import com.nttdocomo.android.tvterminalapp.common.UserState;

/**
 *
 * クラス機能：
 *      プロジェクトにて、すべての「Activity」のベースクラスである
 *      「Activity」全体にとって、共通の機能があれば、追加すること
 */

public class BaseActivity extends FragmentActivity implements MenuDisplayEventListener {

    private LinearLayout titleLinearLayout;
    private TextView titleTextView;

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

    /**
     * タイトルビュー
     * @param resId
     */
    @Override
    public void setContentView(int resId) {
        View view = getLayoutInflater().inflate(resId, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        titleLinearLayout.addView(view);
    }

    /**
     *タイトルビユー初期化
     *
     */
    private void initView(){
        titleLinearLayout = findViewById(R.id.base_ll);
        titleTextView = findViewById(R.id.header_layout_text);
    }

    /**
     *タイトルを隠す
     *
     */
    protected void setNoTitle(){
        findViewById(R.id.base_title).setVisibility(View.GONE);
    }

    /**
     *
     * タイトル内容を設定
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
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initView();
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
