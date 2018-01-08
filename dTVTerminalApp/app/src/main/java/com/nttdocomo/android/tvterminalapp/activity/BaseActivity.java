/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.utils.DAccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountControl;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipRegistWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import java.util.List;

/**
 * クラス機能：
 * プロジェクトにて、すべての「Activity」のベースクラスである
 * 「Activity」全体にとって、共通の機能があれば、追加すること
 */

public class BaseActivity extends FragmentActivity implements MenuDisplayEventListener,
        DlnaDevListListener, View.OnClickListener, RemoteControllerView.OnStartRemoteControllerUIListener,
        ClipRegistWebClient.ClipRegistJsonParserCallback, ClipDeleteWebClient.ClipDeleteJsonParserCallback,
        DaccountControl.DaccountControlCallBack, UserInfoDataProvider.UserDataProviderCallback {

    private LinearLayout baseLinearLayout = null;
    private RelativeLayout headerLayout = null;
    protected TextView titleTextView = null;
    private ImageView mHeaderBackIcon = null;
    private ImageView mStbStatusIcon = null;
    private DlnaProvDevList mDlnaProvDevListForBase = null;

    private ImageView mMenuImageViewForBase = null;
    private RemoteControllerView remoteControllerView = null;
    private Context mContext = null;
    private Activity mActivity = null;
    private RemoteControlRelayClient mRemoteControlRelayClient = null;
    private UserState sUserState = UserState.LOGIN_NG;

    private long lastClickTime = 0;

    /** クリップ対象 */
    private String mClipTarget = null;

    /** stb status icon状態 */
    private boolean mIsStbStatusOn = false;

    /** タイムアウト時間 */
    public static final int LOAD_PAGE_DELAY_TIME = 1000;

    private static final int MIN_CLICK_DELAY_TIME = 1000;

    /** クリップ未登録状態 */
    private static final String CLIP_RESULT_STATUS = "1";

    /** dアカウント設定アプリ登録処理 */
    private DaccountControl mDaccountControl = null;

    /** 詳細画面起動元Classを保存 */
    private static String mSourceScreenClass = "";

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
        mHeaderBackIcon = findViewById(R.id.header_layout_back);
        mStbStatusIcon = findViewById(R.id.header_stb_status_icon);
        mMenuImageViewForBase = findViewById(R.id.header_layout_menu);
    }

    /**
     * 機能：ヘッダー色を変更する
     *
     * @param isColorRed true:ヘッダー色=赤 false:ヘッダー色=黒
     */
    protected void setHeaderColor(Boolean isColorRed) {
        if (null != headerLayout) {
            if (isColorRed) {
                headerLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.header_background_color_red));
            } else {
                headerLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.header_background_color_black));
            }
        }
    }

    /**
     * 機能：ヘッダーの戻るアイコン"<"有効
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableHeaderBackIcon(boolean isOn) {
        if (null != mHeaderBackIcon) {
            if (isOn) {
                mHeaderBackIcon.setVisibility(View.VISIBLE);
            } else {
                mHeaderBackIcon.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 機能：STB接続アイコンを有効
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableStbStatusIcon(boolean isOn) {
        if (this instanceof LaunchActivity
                //|| this instanceof RecordedListActivity
                || this instanceof DtvContentsDetailActivity) {
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
     * 機能：STB接続アイコンを表示
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void setStbStatusIconVisibility(boolean isOn) {
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

    /**
     * 機能：Global menuアイコンと×ボタンアイコンを切り替える
     *
     * @param isMenu true:menuアイコン false:×ボタンアイコン
     */
    protected void changeGlobalMenuIcon(boolean isMenu) {
        if (null != mMenuImageViewForBase) {
            if (isMenu) {
                mMenuImageViewForBase.setImageResource(R.mipmap.ic_menu_white_24dp);
            } else {
                mMenuImageViewForBase.setImageResource(R.mipmap.ic_clear_white_24dp);
            }
        }
    }

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
        mActivity = this;
        initView();

        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();
        mRemoteControlRelayClient.setDebugRemoteIp("192.168.11.35");
        //dアカウントの検知処理を追加する
        setDaccountControl();

        //ユーザー情報の変更検知
        UserInfoDataProvider dataProvider = new UserInfoDataProvider(getApplicationContext(),this);
        dataProvider.getUserInfo();

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
        registerDevListDlna();

        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null == dlnaDmsItem) {
            return;
        }
        if (null != mDlnaProvDevListForBase) {
            boolean isAvai = mDlnaProvDevListForBase.isDmsAvailable(dlnaDmsItem.mUdn);
            setStbStatus(isAvai);
        }

        DTVTLogger.debug("RestartFlag check " +
                SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        // 再起動フラグがtrueならば、再起動メッセージを表示する
        if (SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext())) {
            restartMessageDialog();
        }

        //再起動フラグをOFFにする
        SharedPreferencesUtils.setSharedPreferencesRestartFlag(getApplicationContext(),false);

        DTVTLogger.debug("RestartFlag falsed " +
                SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

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
        mRemoteControlRelayClient.resetHandler();
        //unregisterDevListDlna();
        DTVTLogger.end();
    }

    /**
     * 機能：ActivityのSTB接続アイコン
     */
    private void registerDevListDlna() {
        DTVTLogger.start();
        if (this instanceof STBSelectActivity
                || this instanceof LaunchActivity
                //|| this instanceof RecordedListActivity
                || this instanceof DtvContentsDetailActivity) {
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

    public Handler mRerayClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = "OK";
            switch (msg.what) {
                case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_OK:
                    menuRemoteController();
                    break;
                default:
                    int resultcode = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getResultCode();
                    RemoteControlRelayClient.STB_APPLICATION_TYPES appId
                            = ((RemoteControlRelayClient.ResponseMessage)msg.obj).getApplicationTypes();
                    switch (resultcode) {
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_NOT_INSTALL:
                            switch (appId) {
                                case DTV:
                                    message = getResources().getString(R.string.main_setting_dtv_uninstall_message);
                                    showErrorDialog(message);
                                    break;
                                case DANIMESTORE:
                                    message = getResources().getString(R.string.main_setting_d_anime_store_uninstall_message);
                                    showErrorDialog(message);
                                    break;
                                case DTVCHANNEL:
                                    message = getResources().getString(R.string.main_setting_dtv_channel_uninstall_message);
                                    showErrorDialog(message);
                                    break;
                                case HIKARITV:
                                    message = getResources().getString(R.string.main_setting_hikari_tv_uninstall_message);
                                    showErrorDialog(message);
                                    break;
                                case DAZN:
                                    message = getResources().getString(R.string.main_setting_dazn_uninstall_message);
                                    showErrorDialog(message);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_ID_NOTEXIST:
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_START_FAILED:
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_INTERNAL_ERROR:
                            message = getResources().getString(R.string.main_setting_connect_error_message);
                            showErrorDialog(message);
                            break;
                        case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_VERSION_CODE_INCOMPATIBLE:
                            switch (appId) {
                                case DTV:
                                    message = getResources().getString(R.string.main_setting_dtv_update_message);
                                    showErrorDialog(message);
                                    break;
                                case DANIMESTORE:
                                    message = getResources().getString(R.string.main_setting_d_anime_store_update_message);
                                    showErrorDialog(message);
                                    break;
                                case DTVCHANNEL:
                                    message = getResources().getString(R.string.main_setting_dtv_channel_update_message);
                                    showErrorDialog(message);
                                    break;
                                case HIKARITV:
                                    message = getResources().getString(R.string.main_setting_hikari_tv_update_message);
                                    showErrorDialog(message);
                                    break;
                                case DAZN:
                                    message = getResources().getString(R.string.main_setting_dazn_update_message);
                                    showErrorDialog(message);
                                    break;
                                default:
                                    break;
                            }
                          break;
                        default:
                            message = getResources().getString(R.string.main_setting_connect_error_message);
                            showErrorDialog(message);
                            break;
                    }
                    break;
            }
            mRemoteControlRelayClient.resetHandler();
        }
    };

    /**
     * 機能 エラーメッセージの表示
     *
     * @param errorMessage dialog content
     */
    protected void showErrorDialog(String errorMessage) {
        CustomDialog errorDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        errorDialog.setContent(errorMessage);
        errorDialog.showDialog();
    }

    /**
     * 再起動時のダイアログ・引数無しに対応するため、可変長引数とする
     *
     * @param message 省略した場合はdアカウント用メッセージを表示。指定した場合は、常に先頭文字列のみ使用される
     */
    private void restartMessageDialog(String... message) {

        //呼び出し用のアクティビティの退避
        final Activity activity = this;

        //出力メッセージのデフォルトはdアカウント用
        String printMessage = getString(R.string.d_account_chamge_message);

        //引数がある場合はその先頭を使用する
        if (message != null && message.length > 0) {
            printMessage = message[0];
        }

        //ダイアログを、OKボタンのコールバックありに設定する
        CustomDialog restartDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        restartDialog.setContent(printMessage);
        //startAppDialog.setTitle(getString(R.string.dTV_content_service_start_dialog));
        restartDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                //OKが押されたので、ホーム画面の表示
                DAccountUtils.reStartApplication(activity);
            }
        });
        restartDialog.showDialog();
    }

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
        //コンテンツ詳細画面をクローズする処理
        try {
            Class sourceClass = Class.forName(getSourceScreenClass());
            Intent intent = new Intent(this, sourceClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            setSourceScreenClass("");
        } catch (ClassNotFoundException e) {
            DTVTLogger.debug(e);
        }
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
        if (this instanceof DtvContentsDetailActivity) {
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
     *
     * @param visibility 表示非表示指定
     */
    protected void setRemoteControllerViewVisibility(int visibility) {
        if (remoteControllerView != null) {
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
     * クリップ登録/削除処理追加
     *
     * @param data クリップ処理用データ
     */
    public void sendClipRequest(ClipRequestData data) {
        if (data != null) {

            //クリップ対象を格納
            if (data.getIsNotify()) {
                mClipTarget = getString(R.string.epg_contents_message);
            } else {
                mClipTarget = getString(R.string.vod_contents_message);
            }
            boolean isParamCheck;
            ClipRegistWebClient registWebClient = new ClipRegistWebClient();
            isParamCheck = registWebClient.getClipRegistApi(data.getType(), data.getCrid(), data.getServiceId(),
                    data.getEventId(), data.getTitleId(), data.getTitle(), data.getRValue(),
                    data.getLinearStartDate(), data.getLinearEndDate(), data.getIsNotify(), this);

            //パラメータチェックではじかれたら失敗表示
            if (!isParamCheck) {
                showClipToast(R.string.clip_regist_error_message);
            }
        }
    }

    /**
     * クリップ処理でのトースト表示
     *
     * @param msgId 各ステータスのメッセージID
     */
    private void showClipToast(int msgId) {
        //クリップ対象がない場合には、トーストのメッセージに不整合が生じるため、表示しない
        if (mClipTarget != null && mClipTarget.length() > 0) {
            String[] strings = {mClipTarget, getString(msgId)};
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.clip_toast_layout, null);
            TextView textView = view.findViewById(R.id.clip_toast_textView);
            textView.setText(StringUtil.getConnectString(strings));
            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    @Override
    public void onClipRegistResult() {
        //TODO:クリップ登録成功時の正式なトースト実装
        showClipToast(R.string.clip_regist_result_message);
    }

    @Override
    public void onClipRegistFailure() {
        //TODO:クリップ登録失敗時の正式なトースト実装
        showClipToast(R.string.clip_regist_error_message);
    }

    @Override
    public void onClipDeleteResult() {
        //TODO:クリップ削除成功時の正式なトースト実装
        showClipToast(R.string.clip_delete_result_message);
    }

    @Override
    public void onClipDeleteFailure() {
        //TODO:クリップ削除失敗時の正式なトースト実装
        showClipToast(R.string.clip_delete_error_message);
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
            Toast.makeText(getApplicationContext(),
                    R.string.d_account_regist_error,Toast.LENGTH_LONG).show();
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
        //また、"dcmEval20150128.keystore"の署名ファイルが付加されずに実行された場合もこちらになる。署名ファイル無しではブロードキャストの
        //登録はできないので、こちらに来るのは自然な動作となる。
        DTVTLogger.end("d account error. no signature?");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /**
     * RemoteControlRelayClientにHandlerを設定する
     */
    public void setRelayClientHandler() {
        // TODO MenuDisplay修正時に合わせて修正する
        mRemoteControlRelayClient.setHandler(mRerayClientHandler);
    }

    /**
     * 機能: バックキー押下によるアプリ終了ダイアログを表示
     */
    protected void showTips() {
        DTVTLogger.start();
        if (checkRemoteControllerView()) {
            //リモコンUI表示時はリモコンUIを閉じてダイアログは表示しない
            return;
        }
        CustomDialog applicationFinishDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        applicationFinishDialog.setTitle(getString(R.string.app_finish_dialog_title));
        applicationFinishDialog.setContent(getString(R.string.app_finish_dialog_message));
        applicationFinishDialog.setCancelable(false);
        applicationFinishDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                //アプリ終了する
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();
            }
        });
        applicationFinishDialog.showDialog();
    }

    @Override
    public void userInfoListCallback(boolean isChange,List<UserInfoList> list) {
        //年齢情報に変化があったのでホーム画面に飛ぶ。ただし、初回実行時はチュートリアル等のスキップを防ぐために、必ずfalseになる
        if (isChange) {
            //以前の情報と異なっているので、メッセージの表示後にホーム画面に遷移
            restartMessageDialog(getString(R.string.h4d_agreement_change));
        }
    }

    /**
     * 機能: リモコンが表示されているか確認し、開いている場合は閉じる
     *
     * @return true:リモコン表示、リモコンを閉じる false:リモコン非表示
     */
    protected Boolean checkRemoteControllerView() {
        if (remoteControllerView != null && remoteControllerView.isTopRemoteControllerUI()) {
            remoteControllerView.closeRemoteControllerUI();
            return true;
        }
        return false;
    }

    /**
     * ユーザの年齢情報を返す
     * 未ログイン状態の時はPG12の値を返す
     *
     * @return 年齢情報
     */
    public int getAgeReq() {
        return SharedPreferencesUtils.getSharedPreferencesAgeReq(this);
    }

    /**
     * 詳細画面起動元のクラス名を保存するstaticクラス
     * @param className コンテンツ詳細画面起動元のクラス名
     */
    public synchronized static void setSourceScreen(String className) {
        BaseActivity.mSourceScreenClass = className;
    }

    /**
     * コンテンツ詳細画面起動元のクラス名を保持する
     * @param className クラス名
     */
    public void setSourceScreenClass(String className) {
        setSourceScreen(className);
    }

    /**
     * コンテンツ詳細画面起動元のクラス名を取得する
     * @return クラス名
     */
    public String getSourceScreenClass() {
        return mSourceScreenClass;
    }
}