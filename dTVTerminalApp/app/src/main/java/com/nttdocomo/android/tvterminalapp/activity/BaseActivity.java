/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplayEventListener;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItem;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuItemParam;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.STBSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.utils.DAccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountControl;

/**
 * クラス機能：
 * プロジェクトにて、すべての「Activity」のベースクラスである
 * 「Activity」全体にとって、共通の機能があれば、追加すること
 */

public class BaseActivity extends FragmentActivity implements MenuDisplayEventListener,
        DlnaDevListListener, View.OnClickListener,
        RemoteControllerView.OnStartRemoteControllerUIListener ,
        DaccountControl.DaccountControlCallBack {

    private LinearLayout baseLinearLayout;
    private RelativeLayout headerLayout;
    protected TextView titleTextView;
    private ImageView mStbStatusIcon;
    private DlnaProvDevList mDlnaProvDevListForBase;
    private ImageView mMenuImageViewForBase;
    private RemoteControllerView remoteControllerView = null;
    private Context mContext = null;
    private RemoteControlRelayClient mRemoteControlRelayClient = null;

    /** dアカウント設定アプリ登録処理 */
    private DaccountControl mDaccountControl = null;

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
     * @param clz 起動するアクティビティ
     * @param bundle 受け渡すパラメータ
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * タイトルビュー
     *
     * @param resId リソースID
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
        mStbStatusIcon = findViewById(R.id.header_stb_status_icon);
        mMenuImageViewForBase = findViewById(R.id.header_layout_menu);
    }

    /**
     * 機能：STB接続アイコンを有効
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableStbStatusIcon(boolean isOn) {
        if (this instanceof LaunchActivity
                //|| this instanceof RecordedListActivity
                || this instanceof DtvContentsDetailActivity){
            return;
        }
        if (null != mStbStatusIcon) {
            if (isOn) {
                mStbStatusIcon.setVisibility(View.VISIBLE);
            } else {
                mStbStatusIcon.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 機能：Global menuアイコンを有効
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableGlobalMenuIcon(boolean isOn) {
        if (null != mMenuImageViewForBase) {
            if (isOn) {
                mMenuImageViewForBase.setVisibility(View.VISIBLE);
                mMenuImageViewForBase.setOnClickListener(this);
            } else {
                mMenuImageViewForBase.setVisibility(View.GONE);
            }
        }
    }

    //stb status icon状態
    private boolean mIsStbStatusOn = false;

    /**
     * 機能：STBステータスを変更
     *
     * @param isOn true: stb接続中   false: stb未接続
     */
    protected void setStbStatus(final boolean isOn) {
        //mStbStatusIcon.
        if (null != mStbStatusIcon) {
            mStbStatusIcon.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            if (isOn) {
                                mStbStatusIcon.setImageResource(R.mipmap.ic_stb_status_icon_white);
                                mIsStbStatusOn = true;
                            } else {
                                mStbStatusIcon.setImageResource(R.mipmap.ic_stb_status_icon_gray);
                                mIsStbStatusOn = false;
                            }
                        }
                    } catch (Exception e) {
                        DTVTLogger.debug("BaseActivity::setStbStatus, stb status png file not found");
                        return;
                    }
                }
            });
        }
    }

    /**
     * 機能：STB接続ステータスを戻す
     *
     * @return true: stb接続中   false: stb未接続
     */
    public boolean getStbStatus() {
        return mIsStbStatusOn;
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
     * @param text 設定する文字列
     */
    protected void setTitleText(CharSequence text) {
        if (titleTextView != null) {
            titleTextView.setText(text);
        }
    }

    //契約・ペアリング済み用
    protected void onSampleGlobalMenuButton_PairLoginOk() {
        MenuItemParam param = new MenuItemParam();
        param.setParamForContractOkPairingOk(3, 1, 2, 6, 8);
        setUserState(param);
        displayMenu();
    }

    /**
     * 機能：onCreate
     * Sub classにて、super.onCreate(savedInstanceState)をコールする必要がある
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DTVTLogger.start();

        //TODO: 認証付きアプリのデバッグ時に有効化すると、デバッグのアタッチが行われるまでここで停止する。最終的には削除を行う事
        //android.os.Debug.waitForDebugger();

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        initView();
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();
        mRemoteControlRelayClient.setHandler(ｍRerayClientHandler);
        mRemoteControlRelayClient.setDebugRemoteIp("192.168.11.19");

        //dアカウントの検知処理を追加する
        setDaccountControl();

        DTVTLogger.end();
    }

    /**
     * 機能：onResume
     * Sub classにて、super.onResume()をコールする必要がある
     */
    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        enableStbStatusIcon(true);
        registerDevListDlna();

        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null == dlnaDmsItem) {
            return;
        }
        if (null != mDlnaProvDevListForBase) {
            boolean isAvai = mDlnaProvDevListForBase.isDmsAvailable(dlnaDmsItem.mUdn);
            setStbStatus(isAvai);
        }
        DTVTLogger.end();
    }

    /**
     * 機能：onStop
     * Sub classにて、super.onStop()をコールする必要がある
     */
    @Override
    protected void onStop() {
        super.onStop();
        DTVTLogger.start();
        //unregisterDevListDlna();
        DTVTLogger.end();
    }

    /**
     * 機能：ActivityのSTB接続アイコン
     */
    private void registerDevListDlna() {
        DTVTLogger.start();
        if (this instanceof LaunchActivity
                //|| this instanceof RecordedListActivity
                || this instanceof DtvContentsDetailActivity){
            DTVTLogger.end();
            return;
        }
        mDlnaProvDevListForBase = new DlnaProvDevList();
        mDlnaProvDevListForBase.start(this);
        DTVTLogger.end();
    }

    /**
     * 機能：ActivityのSTB接続アイコン
     */
    private void unregisterDevListDlna() {
        DTVTLogger.start();
        if (this instanceof STBSelectActivity) {
            DTVTLogger.end();
            return;
        }
        if (null != mDlnaProvDevListForBase) {
            mDlnaProvDevListForBase.stopListen();
        }
        DTVTLogger.end();
    }

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    public Handler ｍRerayClientHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = "OK";
            switch (msg.what) {
                case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_OK:
                    menuRemoteController();
                    break;
                default:
                    int resultcode = ((RemoteControlRelayClient.ResponseMessage)msg.obj).getResultCode();
                    switch (resultcode){
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_NOT_INSTALL:
                            message = "APPLICATION_NOT_INSTALL";
                            break;
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_ID_NOTEXIST:
                            message = "APPLICATION_ID_NOTEXIST";
                            break;
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_START_FAILED:
                            message = "APPLICATION_START_FAILED";
                            break;
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_INTERNAL_ERROR:
                            message = "INTERNAL_ERROR";
                            break;
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_VERSION_CODE_INCOMPATIBLE:
                            message = "VERSION_CODE_INCOMPATIBLE";
                            break;
                        default:
                            message = "INTERNAL_ERROR";
                            break;
                    }
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

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

    private UserState sUserState = UserState.LOGIN_NG;

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

    /**
     * コンテンツ詳細の戻るボタン
     *
     * @param view 戻るボタンのビュー
     */
    public void contentsDetailBackKey(View view) {
        finish();
    }

    /**
     * コンテンツ詳細のクローズボタン
     *
     * @param view クローズボタンのビュー
     */
    public void contentsDetailCloseKey(View view) {
        //TODO:コンテンツ詳細系の画面をクローズする処理を記載する
        finish();
    }

    /**
     * 録画コンテンツダウンロード済みかどうか
     *
     * @return DL済み true
     */
    public Boolean getDownloadContentsFalag() {
        // TODO DL済み/未　判定
        // 現時点データが取得できない為、固定でfalseを返却
        return false;
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

    /**
     * 機能：DMSを加入する場合コールされる
     *
     * @param curInfo カレントDlnaDMSInfo
     * @param newItem 新しいDms情報
     */
    @Override
    public void onDeviceJoin(DlnaDMSInfo curInfo, DlnaDmsItem newItem) {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null == dlnaDmsItem) {
            setStbStatus(false);
            return;
        }
        if (dlnaDmsItem.mUdn.equals(newItem.mUdn)) {
            setStbStatus(true);
        }
    }

    /**
     * 機能：DMSをなくなる場合コールされる
     *
     * @param curInfo     　　　カレントDlnaDMSInfo
     * @param leaveDmsUdn 　消えるDmsのudn名
     */
    @Override
    public void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn) {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null == dlnaDmsItem) {
            setStbStatus(false);
            return;
        }
        if (dlnaDmsItem.mUdn.equals(leaveDmsUdn)) {
            setStbStatus(false);
        }
    }

    /**
     * 機能：DLNAはerrorを発生する場合コールされる
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(String msg) {

    }

    /**
     * 機能：onClick event for menu
     *
     * @param view メニューアイコンのビュー
     */
    @Override
    public void onClick(View view) {
        if (mMenuImageViewForBase == view) {
            //ダブルクリックを防ぐ
            if (isFastClick()) {
                onSampleGlobalMenuButton_PairLoginOk();
            }
        }
    }

    /**
     * リモコン画面を生成する
     */
    public void createRemoteControllerView() {
        DTVTLogger.debug("CreateRemoteControllerView");
        RelativeLayout layout = findViewById(R.id.base_remote_controller_rl);
        remoteControllerView = layout.findViewById(R.id.remote_control_view);
        remoteControllerView.init(this);
        if(this instanceof DtvContentsDetailActivity) {
            // nop.
            DTVTLogger.debug("DtvContentsDetailActivity");
        } else {
            remoteControllerView.setOnStartRemoteControllerUI(this);
        }
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        if (remoteControllerView != null && remoteControllerView.isTopRemoteControllerUI()) {
            // onPause時にリモコンUIを閉じる
            remoteControllerView.closeRemoteControllerUI();
        }
        super.onPause();
    }

    /**
     * リモコンUIにリスナーを設定する
     */
    protected void setStartRemoteControllerUIListener(RemoteControllerView.OnStartRemoteControllerUIListener listener) {
        remoteControllerView.setOnStartRemoteControllerUI(listener);
    }

    /**
     * 中継アプリのdアプリ起動リクエスト処理を実行
     *
     * @param type       アプリのタイプ
     * @param contentsId コンテンツID
     */
    protected void requestStartApplication(RemoteControlRelayClient.STB_APPLICATION_TYPES type, String contentsId) {
        remoteControllerView.sendStartApplicationRequest(type, contentsId);
    }

    /**
     * リモコンUI画面を取得
     *
     * @return RemoteControllerView
     */
    protected RemoteControllerView getRemoteControllerView() {
        return remoteControllerView;
    }

    /**
     * リモコンUI画面用 onClickListener
     */
    protected View.OnClickListener mRemoteControllerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.header_stb_status_icon:
                    if (getStbStatus()) {
                        DTVTLogger.debug("Start RemoteControl");
                        createRemoteControllerView();
                        getRemoteControllerView().startRemoteUI();
                    }
                    break;
                default:
                    DTVTLogger.debug("Close Controller");
                    getRemoteControllerView().closeRemoteControllerUI();
                    break;
            }
        }
    };

    @Override
    public void onStartRemoteControl() {
        DTVTLogger.debug("base_start_control");
        View base = findViewById(R.id.base_motion_detection_rl);
        base.setOnClickListener(mRemoteControllerOnClickListener);
        base.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEndRemoteControl() {
        DTVTLogger.debug("base_end_control");
        View base = findViewById(R.id.base_motion_detection_rl);
        base.setOnClickListener(null);
        base.setVisibility(View.GONE);
    }

    /**
     * リモートコントローラーViewのVisibilityを変更
     * @param visibility 表示非表示指定
     */
    protected void setRemoteControllerViewVisibility(int visibility) {
        if(remoteControllerView != null) {
            findViewById(R.id.base_remote_controller_rl).setVisibility(visibility);
        }
    }

    /**
     * グローバルメニューからのリモコン表示
     */
    public void menuRemoteController() {
        if (getStbStatus()) {
            DTVTLogger.debug("Start RemoteControl");
            createRemoteControllerView();
            getRemoteControllerView().startRemoteUI();
        }
    }

    /**
     * dアカウントの切り替えや無効化を受信できるように設定を行う
     */
    private void setDaccountControl() {
        //dアカウント関連の処理を依頼する
        mDaccountControl = new DaccountControl();
        mDaccountControl.registService(getApplicationContext(),this);
    }

    @Override
    public void daccountControlCallBack(boolean result) {
        DTVTLogger.start();
        //dアカウントの登録結果を受け取るコールバック
        if(result) {
            //処理に成功したので、帰る
            DTVTLogger.end("normal end");
            return;
        }

        if(mDaccountControl == null) {
            //処理には失敗したが、動作の続行ができないので、ここで終わらせる。ただ、このコールバックを受けている以上、ヌルになることありえないはず
            Toast.makeText(getApplicationContext(),R.string.d_account_regist_error,Toast.LENGTH_LONG).show();
            DTVTLogger.end("null end");
            return;
        }

        if(mDaccountControl.isOneTimePass() || mDaccountControl.isCheckService()) {
            //エラーが発生したのはワンタイムパスワード取得かチェックサービスとなる。dアカウント未認証なので、本処理ではなにもしない
            DTVTLogger.end("d account not regist");
            return;
        }

        //サービスチェックに成功したが、ブロードキャストの登録に失敗した場合。チェック後に急に通信不全になるような事が無ければ発生しないはず。
        //発生時は次のアクティビティ表示時のリトライにゆだねる。
        DTVTLogger.end("d account error");
    }
}
