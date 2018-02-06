/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import com.nttdocomo.android.tvterminalapp.activity.launch.DAccountReSettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.STBSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.STBSelectErrorActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.SettingActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.callback.TransoceanicCommunicationAlertDialogCallback;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.utils.DAccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountControl;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipRegistWebClient;

import java.util.List;

/**
 * クラス機能：
 * プロジェクトにて、すべての「Activity」のベースクラスである
 * 「Activity」全体にとって、共通の機能があれば、追加すること.
 */

public class BaseActivity extends FragmentActivity implements MenuDisplayEventListener,
        DlnaDevListListener, View.OnClickListener, RemoteControllerView.OnStartRemoteControllerUIListener,
        ClipRegistWebClient.ClipRegistJsonParserCallback, ClipDeleteWebClient.ClipDeleteJsonParserCallback,
        DaccountControl.DaccountControlCallBack, TransoceanicCommunicationAlertDialogCallback {

    private LinearLayout mBaseLinearLayout = null;
    private RelativeLayout mHeaderLayout = null;
    protected TextView titleTextView = null;
    protected ImageView mTitleArrowImage = null;
    private ImageView mHeaderBackIcon = null;
    private ImageView mStbStatusIcon = null;
    private DlnaProvDevList mDlnaProvDevListForBase = null;

    private ImageView mMenuImageViewForBase = null;
    private RemoteControllerView remoteControllerView = null;
    private Context mContext = null;
    private Activity mActivity = null;
    private RemoteControlRelayClient mRemoteControlRelayClient = null;
    private UserState mUserState = UserState.LOGIN_NG;

    private long mLastClickTime = 0;

    private ImageView mClipButton = null;
    private ClipRequestData mClipRequestData = null;

    private boolean mClipRunTime = false;
    /**
     * クリップ対象.
     */
    private String mClipTarget = null;

    /**
     * stb status icon状態.
     */
    private boolean mIsStbStatusOn = false;
    /**
     * GooglePlayのドコテレアプリページ
     * 現在
     */
    private static final String DTVTERMINAL_GOOGLEPLAY_DOWNLOAD_URL =
            "https://www.nttdocomo.co.jp/product/docomo_select/tt01/index.html";

    /**
     * タイムアウト時間.
     */
    public static final int LOAD_PAGE_DELAY_TIME = 1000;

    private static final int MIN_CLICK_DELAY_TIME = 1000;

    /**
     * クリップ未登録状態.
     */
    private static final String CLIP_RESULT_STATUS = "1";

    /**
     * dアカウント設定アプリ登録処理.
     */
    private DaccountControl mDaccountControl = null;

    /**
     * 詳細画面起動元Classを保存.
     */
    private static String mSourceScreenClass = "";

    /**
     * 契約情報.
     */
    private List<UserInfoList> mUserInfo;

    /**
     * ヘッダーに表示されているアイコンがメニューアイコンか×ボタンアイコンかを判別するタグ.
     */
    private static final String HEADER_ICON_MENU = "menu";
    private static final String HEADER_ICON_CLOSE = "close";

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
     * Activityを起動する.
     *
     * @param clz    起動するアクティビティ
     * @param bundle 受け渡すパラメータ
     */
    public void startActivity(final Class<?> clz, final Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * タイトルビュー.
     *
     * @param resId リソースID
     */
    @Override
    public void setContentView(final int resId) {
        DTVTLogger.start("resId:" + resId);
        View view = getLayoutInflater().inflate(resId, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        mBaseLinearLayout.addView(view);
        DTVTLogger.end();
    }

    /**
     * タイトルビユー初期化.
     */
    private void initView() {
        DTVTLogger.start();
        mBaseLinearLayout = findViewById(R.id.base_ll);
        mHeaderLayout = findViewById(R.id.base_title);
        titleTextView = findViewById(R.id.header_layout_text);
        mTitleArrowImage = findViewById(R.id.tv_program_list_main_layout_calendar_arrow);
        DTVTLogger.end();
        mHeaderBackIcon = findViewById(R.id.header_layout_back);
        mStbStatusIcon = findViewById(R.id.header_stb_status_icon);
        mMenuImageViewForBase = findViewById(R.id.header_layout_menu);
    }

    /**
     * 機能：ヘッダー色を変更する.
     *
     * @param isColorRed true:ヘッダー色=赤 false:ヘッダー色=黒
     */
    protected void setHeaderColor(final Boolean isColorRed) {
        if (null != mHeaderLayout) {
            if (isColorRed) {
                mHeaderLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.header_background_color_red));
            } else {
                mHeaderLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.header_background_color_black));
            }
        }
    }

    /**
     * 機能：ヘッダーの戻るアイコン"<"有効.
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableHeaderBackIcon(final boolean isOn) {
        if (null != mHeaderBackIcon) {
            if (isOn) {
                mHeaderBackIcon.setVisibility(View.VISIBLE);
            } else {
                mHeaderBackIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 機能：STB接続アイコンを有効.
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableStbStatusIcon(final boolean isOn) {
        if (this instanceof LaunchActivity
            //|| this instanceof RecordedListActivity
                ) {
            return;
        }
        if (null != mStbStatusIcon) {
            if (isOn) {
                mStbStatusIcon.setVisibility(View.VISIBLE);
            } else {
                mStbStatusIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 機能：STB接続アイコンを表示.
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void setStbStatusIconVisibility(final boolean isOn) {
        if (null != mStbStatusIcon) {
            if (isOn) {
                mStbStatusIcon.setVisibility(View.VISIBLE);
            } else {
                mStbStatusIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 機能：Global menuアイコンを有効.
     *
     * @param isOn true: 表示  false: 非表示
     */
    protected void enableGlobalMenuIcon(final boolean isOn) {
        if (null != mMenuImageViewForBase) {
            if (isOn) {
                mMenuImageViewForBase.setVisibility(View.VISIBLE);
                mMenuImageViewForBase.setOnClickListener(this);
            } else {
                mMenuImageViewForBase.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 機能：Global menuアイコンと×ボタンアイコンを切り替える.
     *
     * @param isMenu true:menuアイコン false:×ボタンアイコン
     */
    protected void changeGlobalMenuIcon(final boolean isMenu) {
        if (null != mMenuImageViewForBase) {
            if (isMenu) {
                mMenuImageViewForBase.setImageResource(R.mipmap.header_material_icon_menu);
                mMenuImageViewForBase.setTag(HEADER_ICON_MENU);
            } else {
                mMenuImageViewForBase.setImageResource(R.mipmap.header_material_icon_close);
                mMenuImageViewForBase.setTag(HEADER_ICON_CLOSE);
            }
        }
    }

    /**
     * 機能：STBステータスを変更.
     *
     * @param isOn true: stb接続中   false: stb未接続
     */
    protected void setStbStatus(final boolean isOn) {
        if (null != mStbStatusIcon) {
            mStbStatusIcon.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            if (isOn) {
                                mStbStatusIcon.setImageResource(R.mipmap.header_material_icon_tv);
                                //ペアリングアイコンがOFF→ON(点灯)になった際にdアカチェックを行う
                                if (!mIsStbStatusOn) {
                                    checkDAccountOnRestart();
                                }

                            } else {
                                mStbStatusIcon.setImageResource(R.mipmap.header_material_icon_tv_active);
                            }
                            mIsStbStatusOn = isOn;
                        }
                    } catch (Exception e) {
                        DTVTLogger.debug("BaseActivity::setStbStatus, stb status png file not found");
                    }
                }
            });
        }
    }

    /**
     * 機能：STB接続ステータスを戻す.
     *
     * @return true: stb接続中   false: stb未接続
     */
    public boolean getStbStatus() {
        return mIsStbStatusOn;
    }

    /**
     * タイトルの表示非表示を設定.
     *
     * @param visible 表示状態
     */
    protected void setTitleVisibility(final Boolean visible) {
        if (mHeaderLayout != null) {
            if (visible) {
                mHeaderLayout.setVisibility(View.VISIBLE);
            } else {
                mHeaderLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * タイトル内容を設定.
     *
     * @param text 設定する文字列
     */
    protected void setTitleText(final CharSequence text) {
        if (titleTextView != null) {
            titleTextView.setText(text);
        }
    }

    /**
     * タイトル内容を取得.
     *
     * @return タイトル内容
     */
    protected CharSequence getTitleText() {
        if (titleTextView != null) {
            return titleTextView.getText();
        }
        return "";
    }

    /**
     * 番組表タイトル矢印表示非表示を設定.
     *
     * @param visible 表示状態
     */
    protected void setTvProgramTitleArrowVisibility(Boolean visible) {
        if (mTitleArrowImage != null) {
            if (visible) {
                mTitleArrowImage.setVisibility(View.VISIBLE);
            } else {
                mTitleArrowImage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 契約・ペアリング済み用.
     */
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

        //TODO: STBのIPアドレスはSTBにDMS機能が搭載された暁にはペアリング時に保存した値を用いるようにする.
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();
        mRemoteControlRelayClient.setDebugRemoteIp("192.168.11.51");
        //dアカウントの検知処理を追加する
        setDaccountControl();

        //ユーザー情報の変更検知
        //検討中
//        UserInfoDataProvider dataProvider = new UserInfoDataProvider(getApplicationContext(), this);
//        dataProvider.getUserInfo();
        DTVTLogger.end();
    }

    /**
     * 機能：onResume
     * Sub classにて、super.onResume()をコールする必要がある.
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

        DTVTLogger.debug("RestartFlag check "
                + SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        // 再起動フラグがtrueならば、再起動メッセージを表示する
        if (SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext())) {
            restartMessageDialog();
        }

        //再起動フラグをOFFにする
        SharedPreferencesUtils.setSharedPreferencesRestartFlag(getApplicationContext(), false);

        DTVTLogger.debug("RestartFlag falsed "
                + SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        DTVTLogger.end();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DTVTLogger.start();

        checkDAccountOnRestart();
    }

    protected void checkDAccountOnRestart() {
        //BGからFGに遷移するときにdアカチェックを行う
        setRelayClientHandler();
        RemoteControlRelayClient.getInstance().isUserAccountExistRequest(getApplicationContext());
        DTVTLogger.end();
    }

    /**
     * 機能：onStop
     * Sub classにて、super.onStop()をコールする必要がある.
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
     * 機能：ActivityのSTB接続アイコン.
     */
    private void registerDevListDlna() {
        DTVTLogger.start();
        if (this instanceof STBSelectActivity
                || this instanceof LaunchActivity
            //|| this instanceof RecordedListActivity
                ) {
            DTVTLogger.end();
            return;
        }
        mDlnaProvDevListForBase = new DlnaProvDevList();
        mDlnaProvDevListForBase.start(this);
        DTVTLogger.end();
    }

    /**
     * 機能：ActivityのSTB接続アイコン.
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

    /**
     * STB送信ハンドラ.
     */
    public Handler mRelayClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DTVTLogger.debug(String.format("msg:%s", msg));
            setRemoteProgressVisible(View.GONE);
            onStbClientResponce(msg);
            mRemoteControlRelayClient.resetHandler();
        }
    };

    /**
     * STB応答時処理.
     */
    protected void onStbClientResponce(Message msg){
        RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES requestCommand
                = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getRequestCommandTypes();
        DTVTLogger.debug("msg.what: " + msg.what + "requestCommand: " + requestCommand);
        DTVTLogger.debug(String.format("requestCommand:%s", requestCommand));
        switch (msg.what) {
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_OK:
                switch (requestCommand) {
                    case START_APPLICATION:
                        break;
                    case TITLE_DETAIL:
//                        menuRemoteController();
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        break;
                    case COMMAND_UNKNOWN:
                    case CHECK_APPLICATION_VERSION_COMPATIBILITY:
                        break;
                    default:
                        break;
                }
                break;
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_ERROR:
                int resultcode = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getResultCode();
                DTVTLogger.debug("resultcode: " + resultcode);
                switch (requestCommand) {
                    case START_APPLICATION:
                    case TITLE_DETAIL:
                        RemoteControlRelayClient.STB_APPLICATION_TYPES appId
                                = ((RemoteControlRelayClient.ResponseMessage) msg.obj).getApplicationTypes();
                        startApplicationErrorHander(resultcode, appId);
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        switch (resultcode) {
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_INTERNAL_ERROR://サーバエラー
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE://ユーザアカウントチェックサービス未登録
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY:// //中継アプリからの応答待ち中に新しい要求を行った場合
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_CONNECTION_TIMEOUT: //STBの中継アプリ~応答が無かった場合(要求はできたのでSTBとの通信はOK)
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                                // チェック処理の状態で処理を分岐する
                                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                                //TODO:アプリのキャッシュをきれいにクリアする処理が必要
                                Intent intent = new Intent(mActivity, DAccountReSettingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                //ペアリングアイコンをOFFにする
                                setStbStatus(false);
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE:
                                CustomDialog dTVTUpDateDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                                dTVTUpDateDialog.setTitle(getResources().getString(R.string.stb_application_version_update));
                                dTVTUpDateDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                                    @Override
                                    public void onOKCallback(boolean isOK) {
                                        toGooglePlay(DTVTERMINAL_GOOGLEPLAY_DOWNLOAD_URL);
                                    }
                                });
                                dTVTUpDateDialog.showDialog();
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE:
                                showErrorDialog(getResources().getString(R.string.stb_application_version_update));
                                break;
                            default:
                                break;
                        }
                        break;
                    case CHECK_APPLICATION_VERSION_COMPATIBILITY:
                        switch (resultcode) {
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE:
                                CustomDialog dTVTUpDateDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                                dTVTUpDateDialog.setTitle(getResources().getString(R.string.stb_application_version_update));
                                dTVTUpDateDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                                    @Override
                                    public void onOKCallback(boolean isOK) {
                                        toGooglePlay(DTVTERMINAL_GOOGLEPLAY_DOWNLOAD_URL);
                                    }
                                });
                                dTVTUpDateDialog.showDialog();
                                break;
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE:
                                showErrorDialog(getResources().getString(R.string.stb_application_version_update));
                                break;
                            default:
                                break;
                        }
                        break;
                    case COMMAND_UNKNOWN:
                        switch (resultcode) {
                            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                                showErrorDialog(getResources().getString(R.string.main_setting_connect_error_message));
                                //ペアリングアイコンをOFFにする
                                setStbStatus(false);
                                break;
                            default:
                                break;
                        }
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }


    /**
     * dTVアプリ起動エラーハンドラ.
     *
     * @param resultCode 実行コード
     * @param appId      アプリID
     */
    protected void startApplicationErrorHander(final int resultCode, final RemoteControlRelayClient.STB_APPLICATION_TYPES appId) {
        String message;
        switch (resultCode) {
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
                    //ENUMをSwitchで使用する場合、未使用項目も記載しなければアナライザーがエラー扱いにしてしまうのでUNKNOWNを追加した、
                    case UNKNOWN:
                    default:
                        break;
                }
                break;
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_ID_NOTEXIST:
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_APPLICATION_START_FAILED:
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_INTERNAL_ERROR:
                message = getResources().getString(R.string.main_setting_stb_application_launch_fail);
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
                    //ENUMをSwitchで使用する場合、未使用項目も記載しなければアナライザーがエラー扱いにしてしまうのでUNKNOWNを追加した、
                    case UNKNOWN:
                    default:
                        break;
                }
                break;
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_CONNECTION_TIMEOUT:
                //dTV アプリが STBの中継アプリに接続できない場合
                setStbStatus(false);
                break;
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY:
                //中継アプリからの応答待ち中に新しい要求を行った場合
                break;
            case RemoteControlRelayClient.ResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
            default:
                message = getResources().getString(R.string.main_setting_stb_application_launch_fail);
                showErrorDialog(message);
                break;
        }
    }
    /**
     * 機能　GooglePlayのAPPページへ
     *
     * @param downLoadUrl 当アプリはGooglePlayのダウンロードURL
     */
    protected void toGooglePlay(final String downLoadUrl) {
        Uri uri = Uri.parse(downLoadUrl);
        Intent installIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(installIntent);
    }

    /**
     * 機能 エラーメッセージの表示.
     *
     * @param errorMessage dialog content
     */
    protected void showErrorDialog(final String errorMessage) {
        CustomDialog errorDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        errorDialog.setContent(errorMessage);
        errorDialog.showDialog();
    }

    /**
     * 再起動時のダイアログ・引数無しに対応するため、可変長引数とする.
     *
     * @param message 省略した場合はdアカウント用メッセージを表示。指定した場合は、常に先頭文字列のみ使用される
     */
    private void restartMessageDialog(final String... message) {

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
     * ダブルクリック防止.
     *
     * @return クリック状態
     */
    protected boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - mLastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        mLastClickTime = curClickTime;
        return flag;
    }

    /**
     * 機能
     * 密度取得.
     *
     * @return 密度
     */
    protected float getDensity() {
        return getDisplayMetrics().density;
    }

    /**
     * 機能
     * ディスプレイ幅取得.
     *
     * @return ディスプレイ幅
     */
    protected int getWidthDensity() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 機能
     * ディスプレイ高さ取得.
     *
     * @return ディスプレイ高さ
     */
    protected int getHeightDensity() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 機能
     * ディスプレイインスタンス取得.
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
     * カレントユーザ名を戻す.
     *
     * @return カレントユーザ名
     */
    public String getUserName() {
        return "Test User";
    }

    public UserState getUserState() {
        return mUserState;
    }

    /**
     * ユーザ状態設定.
     *
     * @param param メニュー表示情報
     */
    public void setUserState(final MenuItemParam param) {
        synchronized (this) {
            mUserState = param.getUserState();
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

    /**
     * ユーザ状態毎の表示.
     */
    public void displayMenu() {
        try {
            MenuDisplay.getInstance().display();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ステータスバーの色変更.
     *
     * @param resId colorリソースID
     */
    protected void setStatusBarColor(final int resId) {
        //ステータスバーの色変更方法をLOLLIPOPを境界に変更する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor(getString(resId)));
        } else {
            setTheme(R.style.ContentsDetailTheme);
        }
    }

    /**
     * コンテンツ詳細の戻るボタン.
     *
     * @param view 戻るボタンのビュー
     */
    public void contentsDetailBackKey(final View view) {
        finish();
    }

    /**
     * コンテンツ詳細のクローズボタン.
     *
     * @param view クローズボタンのビュー
     */
    public void contentsDetailCloseKey(final View view) {
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
     * 録画コンテンツダウンロード済みかどうか.
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
     * 機能：DMSを加入する場合コールされる.
     *
     * @param curInfo カレントDlnaDMSInfo
     * @param newItem 新しいDms情報
     */
    @Override
    public void onDeviceJoin(final DlnaDMSInfo curInfo, final DlnaDmsItem newItem) {
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
     * 機能：DMSをなくなる場合コールされる.
     *
     * @param curInfo     　　　カレントDlnaDMSInfo
     * @param leaveDmsUdn 　消えるDmsのudn名
     */
    @Override
    public void onDeviceLeave(final DlnaDMSInfo curInfo, final String leaveDmsUdn) {
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
     * 機能：DLNAはerrorを発生する場合コールされる.
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(final String msg) {
        DTVTLogger.debug("BaseActivity.onError, dlna err message: " + msg);
    }

    /**
     * 機能：onClick event for menu.
     *
     * @param view メニューアイコンのビュー
     */
    @Override
    public void onClick(final View view) {
        if (mMenuImageViewForBase == view) {
            if (HEADER_ICON_CLOSE.equals(mMenuImageViewForBase.getTag())) {
                //コンテンツ詳細画面の×ボタン時はコンテンツ詳細画面を閉じる
                contentsDetailCloseKey(view);
            } else {
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    onSampleGlobalMenuButton_PairLoginOk();
                }
            }
        }
    }

    /**
     * リモコン画面を生成する.
     *
     * @param isFirstVisible 表示状態
     */
    public void createRemoteControllerView(final Boolean isFirstVisible) {
        DTVTLogger.debug("CreateRemoteControllerView");
        RelativeLayout layout = findViewById(R.id.base_remote_controller_rl);
        remoteControllerView = layout.findViewById(R.id.remote_control_view);
        remoteControllerView.init(this);
        remoteControllerView.setIsFirstVisible(isFirstVisible);
        remoteControllerView.setOnStartRemoteControllerUI(this);
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
     * リモコンUIにリスナーを設定する.
     *
     * @param listener リスナ
     */
    protected void setStartRemoteControllerUIListener(final RemoteControllerView.OnStartRemoteControllerUIListener listener) {
        remoteControllerView.setOnStartRemoteControllerUI(listener);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ・dTV
     * ・dアニメストア
     *
     * @param type       アプリのタイプ
     * @param contentsId コンテンツID
     */
    protected void requestStartApplication(final RemoteControlRelayClient.STB_APPLICATION_TYPES type, final String contentsId) {
        remoteControllerView.sendStartApplicationRequest(type, contentsId);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ・dTVチャンネル
     *
     * @param type
     * @param crid
     * @param chno
     * @param serviceCategoryType
     */
    protected void requestStartApplicationDtvChannel(final RemoteControlRelayClient.STB_APPLICATION_TYPES type,
                                                        final RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES serviceCategoryType,
                                                        final String crid, final String chno) {
        remoteControllerView.sendStartApplicationDtvChannelRequest(type, serviceCategoryType, crid, chno);
    }

    /**
     * リモコンUI画面を取得.
     *
     * @return RemoteControllerView
     */
    protected RemoteControllerView getRemoteControllerView() {
        return remoteControllerView;
    }

    /**
     * リモコンUI画面用 onClickListener.
     */
    protected View.OnClickListener mRemoteControllerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.header_stb_status_icon:
                    if (getStbStatus()) {
                        DTVTLogger.debug("Start RemoteControl");
                        if (v.getContext() instanceof ContentDetailActivity) {
                            if (((ContentDetailActivity) v.getContext()).getControllerVisible()) {
                                // コンテンツ詳細画面でコントローラのヘッダーを表示する場合
                                createRemoteControllerView(true);
                            } else {
                                // コンテンツ詳細画面でコントローラのヘッダーを表示しない場合
                                createRemoteControllerView(false);
                            }
                        } else {
                            // コンテンツ詳細画面以外の場合
                            createRemoteControllerView(false);
                        }
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
    public void onStartRemoteControl(boolean isFromHeader) {
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
     * リモートコントローラーViewのVisibilityを変更.
     *
     * @param visibility 表示非表示指定
     */
    protected void setRemoteControllerViewVisibility(final int visibility) {
        if (remoteControllerView != null) {
            findViewById(R.id.base_remote_controller_rl).setVisibility(visibility);
        }
    }

    /**
     * グローバルメニューからのリモコン表示.
     */
    public void menuRemoteController() {
        if (getStbStatus()) {
            DTVTLogger.debug("Start RemoteControl");
            createRemoteControllerView(false);
            getRemoteControllerView().startRemoteUI();
        }
    }

    /**
     * クリップ登録/削除処理追加.
     *
     * @param data       クリップ処理用データ
     * @param clipButton クリップボタン
     */
    public void sendClipRequest(final ClipRequestData data, final ImageView clipButton) {

        if (data != null && clipButton != null && !mClipRunTime) {

            //クリップ多重実行に対応していないため実行中フラグで管理
            mClipRunTime = true;

            mClipButton = clipButton;
            mClipRequestData = data;

            //クリップ対象を格納
            if (data.getIsNotify()) {
                mClipTarget = getString(R.string.epg_contents_message);
            } else {
                mClipTarget = getString(R.string.vod_contents_message);
            }
            boolean isParamCheck;

            //クリップ状態によりクリップ登録/削除実行
            if (data.isClipStatus()) {
                ClipDeleteWebClient deleteWebClient =
                        new ClipDeleteWebClient(getApplicationContext());
                isParamCheck = deleteWebClient.getClipDeleteApi(data.getType(), data.getCrid(),
                        data.getTitle(), this);
            } else {
                ClipRegistWebClient registWebClient =
                        new ClipRegistWebClient(getApplicationContext());
                isParamCheck = registWebClient.getClipRegistApi(data.getType(), data.getCrid(),
                        data.getServiceId(), data.getEventId(), data.getTitleId(), data.getTitle(),
                        data.getRValue(), data.getLinearStartDate(), data.getLinearEndDate(),
                        data.getIsNotify(), this);
            }

            //パラメータチェックではじかれたら失敗表示
            if (!isParamCheck) {
                showClipToast(R.string.clip_regist_error_message);
            }
        }
    }

    /**
     * クリップ処理でのトースト表示.
     *
     * @param msgId 各ステータスのメッセージID
     */
    private void showClipToast(final int msgId) {
        //クリップ対象がない場合には、トーストのメッセージに不整合が生じるため、表示しない
        if (mClipTarget != null && mClipTarget.length() > 0) {
            String[] strings = {mClipTarget, getString(msgId)};
            Toast.makeText(this, StringUtils.getConnectString(strings), Toast.LENGTH_SHORT).show();
        }
        //クリップ処理終了メッセージ後にフラグを実行中から終了に変更
        mClipRunTime = false;
    }

    @Override
    public void onClipRegistResult() {
        mClipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip);
        showClipToast(R.string.clip_regist_result_message);

        //DB登録開始
        ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
        clipKeyListDataProvider.clipResultInsert(mClipRequestData);

    }

    @Override
    public void onClipRegistFailure() {
        showClipToast(R.string.clip_regist_error_message);
    }

    @Override
    public void onClipDeleteResult() {
        mClipButton.setBackgroundResource(R.mipmap.icon_circle_opacity_clip);
        showClipToast(R.string.clip_delete_result_message);

        //DB削除開始
        ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
        clipKeyListDataProvider.clipResultDelete(mClipRequestData);
    }

    @Override
    public void onClipDeleteFailure() {
        showClipToast(R.string.clip_delete_error_message);
    }

    /**
     * dアカウントの切り替えや無効化を受信できるように設定を行う.
     */
    private void setDaccountControl() {
        //dアカウント関連の処理を依頼する
        mDaccountControl = new DaccountControl();
        mDaccountControl.registService(getApplicationContext(), this);
    }

    @Override
    public void daccountControlCallBack(boolean result) {
        DTVTLogger.start();
        //dアカウントの登録結果を受け取るコールバック
        if (result) {
            //処理に成功したので、帰る
            DTVTLogger.end("normal end");
            return;
        }

        if (mDaccountControl == null) {
            //処理には失敗したが、動作の続行ができないので、ここで終わらせる。ただ、このコールバックを受けている以上、ヌルになることありえないはず
            CustomDialog errorDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
            errorDialog.setContent(getString(R.string.d_account_regist_error));
            errorDialog.showDialog();
            DTVTLogger.end("null end");
            return;
        }

        if (mDaccountControl.isOneTimePass() || mDaccountControl.isCheckService()) {
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
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * RemoteControlRelayClientにHandlerを設定する.
     */
    public void setRelayClientHandler() {
        // TODO MenuDisplay修正時に合わせて修正する
        mRemoteControlRelayClient.setHandler(mRelayClientHandler);
    }

    /**
     * 機能: バックキー押下によるアプリ終了ダイアログを表示.
     */
    protected void showTips() {
        DTVTLogger.start();
        if (checkRemoteControllerView()) {
            //リモコンUI表示時はリモコンUIを閉じてダイアログは表示しない
            return;
        }
        CustomDialog applicationFinishDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
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
//検討中
//    @Override
//    public void userInfoListCallback(boolean isChange, List<UserInfoList> list) {
//        //年齢情報に変化があったのでホーム画面に飛ぶ。ただし、初回実行時はチュートリアル等のスキップを防ぐために、必ずfalseになる
//        if (isChange) {
//            //以前の情報と異なっているので、メッセージの表示後にホーム画面に遷移
//            restartMessageDialog(getString(R.string.h4d_agreement_change));
//
//            //持ち出しコンテンツをすべて削除する
//            DownLoadListDataManager downLoadListDataManager =
//                    new DownLoadListDataManager(getApplicationContext());
//            downLoadListDataManager.deleteDownloadAllContents();
//
//        }
//    }

    /**
     * 機能: リモコンが表示されているか確認し、開いている場合は閉じる.
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
     * 詳細画面起動元のクラス名を保存するstaticクラス.
     *
     * @param className コンテンツ詳細画面起動元のクラス名
     */
    public synchronized static void setSourceScreen(final String className) {
        BaseActivity.mSourceScreenClass = className;
    }

    /**
     * コンテンツ詳細画面起動元のクラス名を保持する.
     *
     * @param className クラス名
     */
    public void setSourceScreenClass(final String className) {
        setSourceScreen(className);
    }

    /**
     * コンテンツ詳細画面起動元のクラス名を取得する.
     *
     * @return クラス名
     */
    public String getSourceScreenClass() {
        return mSourceScreenClass;
    }

    /** dip → px */
    public int dip2px(int dip) {
        return (int) (dip * getDensity() + 0.5f);
    }

    public void setRemoteProgressVisible(int visible){
        findViewById(R.id.base_progress_rl).setVisibility(visible);
    }

    /**
     * アニメーション付きスタートアクティビティ
     *
     * @param intent アクティビティ呼び出し情報
     */
    @Override
    public void startActivity(Intent intent) {
        //普通にアクティビティを起動する
        super.startActivity(intent);

        //飛び先画面として指定されていた名前を取得する
        String callName = "";
        if (intent != null && intent.getComponent() != null) {
            callName = intent.getComponent().toShortString();
        }

        //飛び先がSTB選択の関連画面ならば、アニメは付加せず帰る
        if (callName.contains(STBSelectActivity.class.getSimpleName()) ||
                callName.contains(STBSelectErrorActivity.class.getSimpleName())) {
            //ただし、設定画面から呼ばれた場合はアニメーションは行うので帰らない
            if(!(this instanceof SettingActivity)) {
                return;
            }
        }

        //アニメーションを付加する
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_lefttoright);
    }

    /**
     * 海外通信警告ダイアログの設定・表示
     */
    private void showTransoceanicCommunicationDialog() {
        CustomDialog dialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        dialog.setCancelable(false);
        dialog.setOnTouchOutside(false);
        dialog.setConfirmText(R.string.transoceanic_communication_dialog_confirm);
        dialog.setConfirmVisibility(View.VISIBLE);
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                selectOkTransoceanicCommunicationDialogCallback();
            }
        });
        dialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                selectNgTransoceanicCommunicationDialogCallback();
            }
        });
        dialog.setContent(getResources().getString(R.string.transoceanic_communication_dialog_content));
        dialog.setTitle(getResources().getString(R.string.transoceanic_communication_dialog_title));
        dialog.showDialog();
        showTransoceanicCommunicationDialogCallback();
    }


    @Override
    public void showTransoceanicCommunicationDialogCallback() {
        // 海外判定となり警告ダイアログが表示された場合、サービスを含む全通信を止める
        // TODO 各画面はこのメソッドをOverrideし、各通信を止める
        DTVTLogger.start();
        DTVTLogger.end();
    }

    @Override
    public void selectOkTransoceanicCommunicationDialogCallback() {
        // TODO 止めた通信を再開する
        DTVTLogger.start();
        DTVTLogger.end();
    }

    @Override
    public void selectNgTransoceanicCommunicationDialogCallback() {
        // TODO 海外通信を許可しない場合の処理
    }

}
