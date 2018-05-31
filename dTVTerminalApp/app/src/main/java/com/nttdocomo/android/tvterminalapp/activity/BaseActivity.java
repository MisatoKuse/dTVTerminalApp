/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.ChildContentListActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;
import com.nttdocomo.android.tvterminalapp.activity.common.ProcessSettingFile;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.PremiumVideoActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecommendActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordReservationListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RentalListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.WatchingVideoListActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.DaccountInductionActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.DaccountResettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.DaccountSettingHelpActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.PairingHelpActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbConnectActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbSelectErrorActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.RankingTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.NewsActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.SettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoTopActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.application.TvtApplication;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileMetaData;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.relayclient.RelayServiceResponseMessage;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DeviceStateUtils;
import com.nttdocomo.android.tvterminalapp.utils.RuntimePermissionUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountControl;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOtt;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipRegistWebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * クラス機能：
 * プロジェクトにて、すべての「Activity」のベースクラスである
 * 「Activity」全体にとって、共通の機能があれば、追加すること.
 */
@SuppressLint("Registered")
public class BaseActivity extends FragmentActivity implements
        View.OnClickListener,
        RemoteControllerView.OnStartRemoteControllerUIListener,
        ClipRegistWebClient.ClipRegistJsonParserCallback,
        ClipDeleteWebClient.ClipDeleteJsonParserCallback,
        DaccountControl.DaccountControlCallBack,
        CustomDialog.DismissCallback, HomeRecyclerViewAdapter.ItemClickCallback, StbConnectionManager.ConnectionListener {
    /**
     * ヘッダーBaseレイアウト.
     */
    private LinearLayout mBaseLinearLayout = null;
    /**
     * 　ステータスレイアウト.
     */
    private LinearLayout mStatusLinearLayout = null;
    /**
     * 　タイトルレイアウト.
     */
    private RelativeLayout mHeaderTitleLayout = null;
    /**
     * ヘッダーレイアウト.
     */
    private RelativeLayout mHeaderLayout = null;
    /**
     * ヘッダータイトル.
     */
    protected TextView mTitleTextView = null;
    /**
     * ヘッダーロゴ用ImageView.
     */
    private ImageView mTitleImageView = null;
    /**
     * 番組表タイトル横矢印.
     */
    protected ImageView mTitleArrowImage = null;
    /**
     * ヘッダーの戻るボタン.
     */
    private ImageView mHeaderBackIcon = null;
    /**
     * ペアリングアイコン.
     */
    private ImageView mStbStatusIcon = null;
    /**
     * メニューアイコン.
     */
    private ImageView mMenuImageViewForBase = null;
    /**
     * リモコンレイアウト.
     */
    private RemoteControllerView mRemoteControllerView = null;
    /**
     * Activityのコンテキスト.
     */
    private Context mContext = null;
    /**
     * Activityのコンテキスト.
     */
    private Activity mActivity = null;
    /**
     * 中継アプリ連携クライアント.
     */
    protected RemoteControlRelayClient mRemoteControlRelayClient = null;
    /**
     * ダブルクリック防止用.
     */
    private long mLastClickTime = 0;
    /**
     * クリップボタン.
     */
    private ImageView mClipButton = null;
    /**
     * クリップリクエスト用データ.
     */
    private ClipRequestData mClipRequestData = null;
    /**
     * クリップ実行中フラグ.
     */
    private boolean mClipRunTime = false;
    /**
     * GoogleAnalytics用.
     */
    private Tracker mTracker;
    /**
     * GooglePlayのドコテレアプリページ.
     * 現在
     */
    protected static final String DTVTERMINAL_GOOGLEPLAY_DOWNLOAD_URL =
            "https://www.nttdocomo.co.jp/product/docomo_select/tt01/index.html";
    /** DialogQue. **/
    private final LinkedList<CustomDialog> mLinkedList = new LinkedList<>();

    /**
     * タイムアウト時間.
     */
    protected static final int LOAD_PAGE_DELAY_TIME = 1000;

    /**
     * ページング単位.
     */
    protected static final int NUM_PER_PAGE = 50;

    /**
     * webViewの読み込み完了値.
     */
    protected final static int PROGRESS_FINISH = 100;

    /**
     * ダブルクリック抑止用 DELAY.
     */
    private static final int MIN_CLICK_DELAY_TIME = 500;

    /**
     * スプラッシュ画面用のファイル設定ファイル用ダイアログ表示識別文字列.
     */
    protected final static String SHOW_SETTING_FILE_DIALOG = "SHOW_SETTING_FILE_DIALOG";
    /**
     * スプラッシュ画面用のファイル設定ファイル用ダイアログ表示内容表示識別文字列.
     */
    protected final static String SHOW_SETTING_FILE_DIALOG_DATA
            = "SHOW_SETTING_FILE_DIALOG_DATA";

    /**
     * dアカウント設定アプリ登録処理.
     */
    private DaccountControl mDAccountControl = null;
    /**
     * 初回dアカウント取得失敗時のダイアログを呼び出すハンドラー.
     */
    private Handler mFirstDaccountErrorHandler = null;

    /**
     * 詳細画面起動元Classを保存.
     */
    private static String sSourceScreenClass = "";

    /**
     * ヘッダーに表示されているアイコンがメニューアイコンか×ボタンアイコンかを判別するタグ(menu).
     */
    private static final String HEADER_ICON_MENU = "menu";
    /**
     * ヘッダーに表示されているアイコンがメニューアイコンか×ボタンアイコンかを判別するタグ(close).
     */
    private static final String HEADER_ICON_CLOSE = "close";
    /**
     * requestPermissions()表示によるonPauseを判断するためのフラグ.
     */
    private boolean mShowPermissionDialogFlag = false;
    /**
     * 表示中ダイアログ.
     */
    private CustomDialog mShowDialog = null;
    /**
     * 設定ファイル処理クラス.
     */
    protected ProcessSettingFile mCheckSetting = null;
    /**
     * 国内通信 MCC (440 Japan).
     */
    private static final int DOMESTIC_COMMUNICATION_MCC_1 = 440;
    /**
     * 国内通信 MCC (441 Japan).
     */
    private static final int DOMESTIC_COMMUNICATION_MCC_2 = 441;
    /**
     * dアカウント関連処理の必要有無判定.
     */
    private boolean mNecessaryDAccountRegistService = true;

    /**
     * クリップ状態.
     */
    public static final String CLIP_ACTIVE_STATUS = "active";
    /**
     * 未クリップ状態.
     */
    public static final String CLIP_OPACITY_STATUS = "opacity";

    /**
     * アダプタ内でのリスト識別用定数.
     */
    private final static int HOME_CONTENTS_DISTINCTION_ADAPTER = 10;

    /**
     * 関数機能：
     * Activityを起動する.
     *
     * @param clz    起動するアクティビティ
     * @param bundle 受け渡すパラメータ
     */
    protected void startActivity(final Class<?> clz, final Bundle bundle) {
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
        mStatusLinearLayout = findViewById(R.id.header_status_linear);
        mHeaderTitleLayout = findViewById(R.id.header_title_relative_layout);
        mHeaderLayout = findViewById(R.id.base_title);
        mTitleTextView = findViewById(R.id.header_layout_text);
        mTitleImageView = findViewById(R.id.header_layout_title_image);
        mTitleArrowImage = findViewById(R.id.tv_program_list_main_layout_calendar_arrow);
        mHeaderBackIcon = findViewById(R.id.header_layout_back);
        mStbStatusIcon = findViewById(R.id.header_stb_status_icon);
        mMenuImageViewForBase = findViewById(R.id.header_layout_menu);
        DTVTLogger.end();
    }

    /**
     * 機能：ステータスバー色を変更する(Android4.4用).
     *
     * @param isColorRed true:ステータスバー色 = 赤 false:ステータスバー色 = 黒
     */
    protected void setStatusBarColor(final Boolean isColorRed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(this);

            View view = new View(getApplicationContext());
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getLayoutParams().height = statusBarHeight;
            ((ViewGroup) window.getDecorView()).addView(view);
            if (isColorRed) {
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.header_background_color_red));
            } else {
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.header_background_color_black));
            }

            LinearLayout linearLayout = findViewById(R.id.baseStatusBarLayout);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.setMargins(0, statusBarHeight, 0, 0);
            linearLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * ステータスバーの高さを取得する.
     *
     * @param activity アクティビティ
     * @return ステータスバーの高さ
     */
    private static int getStatusBarHeight(final Activity activity) {
        int result = 0;
        Resources res = activity.getResources();
        int resourceId = res.getIdentifier(
                activity.getString(R.string.status_bar_height_name),
                activity.getString(R.string.status_bar_height_deftype),
                activity.getString(R.string.status_bar_height_defpackage));
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
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
                mHeaderBackIcon.setOnClickListener(this);
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
        if (mStbStatusIcon != null) {
            mStbStatusIcon.setVisibility(isOn ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 機能：STBステータスを変更.
     *
     * @param isOn true: stb接続中   false: stb未接続
     */
    private void setStbStatus(final boolean isOn) {
        if (mStbStatusIcon == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStbStatusIcon.setImageResource(R.mipmap.header_material_icon_tv_active);
            }
        });
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
     * ドコモテレビターミナル画像を出す場合は空文字を指定し、enableStbStatusIcon()より後に呼ぶ必要がある.
     * //TODO タイトルとSTBアイコンが重なった場合の処理
     *
     * @param text 設定する文字列
     */
    protected void setTitleText(final CharSequence text) {
        if (this instanceof LaunchActivity || this instanceof StbConnectActivity
                || (this instanceof StbSelectActivity && text.equals(getString(R.string.str_app_title))
                || this instanceof StbSelectErrorActivity || this instanceof PairingHelpActivity || this instanceof HomeActivity
                || this instanceof DaccountInductionActivity || this instanceof DaccountResettingActivity
                || this instanceof DaccountSettingHelpActivity)) {
            if (mTitleImageView != null) {
                //ヘッダーに「ドコモテレビターミナル」画像を表示する対応
                mTitleTextView.setVisibility(View.GONE);
                mTitleImageView.setVisibility(View.VISIBLE);
            }
            if (this instanceof HomeActivity) {
                changeTitlePosition(mTitleImageView, mStatusLinearLayout);
            }
        } else if (mTitleTextView != null) {
            mTitleTextView.setVisibility(View.VISIBLE);
            mTitleImageView.setVisibility(View.GONE);
            mTitleTextView.setText(text);
            changeTitleLayout(mHeaderTitleLayout, mStatusLinearLayout);
        }
    }

    /**
     * 　タイトルの描画を監視する.
     *
     * @param mHeaderTitleLayout  タイトルレイアウト
     * @param mStatusLinearLayout 　ステータスアイコンレイアウト
     */

    private void changeTitleLayout(final RelativeLayout mHeaderTitleLayout, final LinearLayout mStatusLinearLayout) {

        mStatusLinearLayout.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mStatusLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayTitleLayout(mHeaderTitleLayout, mStatusLinearLayout);
                        return true;
                    }
                });
    }

    /**
     * タイトルのレイアウトを変える.
     *
     * @param mHeaderTitleLayout 　タイトルレイアウト
     * @param statusIcon         　ステータスアイコンレイアウト
     */

    private void displayTitleLayout(final RelativeLayout mHeaderTitleLayout, final LinearLayout statusIcon) {
        if (statusIcon.getVisibility() == View.VISIBLE && mHeaderTitleLayout.getX() + mHeaderTitleLayout.
                getMeasuredWidth() > statusIcon.getX()) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.header_status_linear);
            mHeaderTitleLayout.setLayoutParams(layoutParams);
        }
    }


    /**
     * 　タイトルの描画を監視する.
     *
     * @param mTitleImageView     タイトルレイアウト
     * @param mStatusLinearLayout 　ステータスアイコンレイアウト
     */
    private void changeTitlePosition(final ImageView mTitleImageView, final LinearLayout mStatusLinearLayout) {
        mStatusLinearLayout.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mStatusLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        displayTitlePosition(mTitleImageView, mStatusLinearLayout);
                        return true;
                    }
                });
    }

    /**
     * タイトルのレイアウトを変える.
     *
     * @param mTitleImageView 　タイトルレイアウト
     * @param statusIcon      　ステータスアイコンレイアウト
     */
    private void displayTitlePosition(final ImageView mTitleImageView, final LinearLayout statusIcon) {
        if (mTitleImageView.getX() + mTitleImageView.getMeasuredWidth() > statusIcon.getX()) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.header_status_linear);
            mTitleImageView.setLayoutParams(layoutParams);
        }
    }

    /**
     * タイトル内容を取得.
     *
     * @return タイトル内容
     */
    protected CharSequence getTitleText() {
        if (mTitleTextView != null) {
            return mTitleTextView.getText();
        }
        return "";
    }

    /**
     * 番組表タイトル矢印表示非表示を設定.
     *
     * @param visible 表示状態
     */
    protected void setTvProgramTitleArrowVisibility(final Boolean visible) {
        if (mTitleArrowImage != null) {
            if (visible) {
                mTitleArrowImage.setVisibility(View.VISIBLE);
            } else {
                mTitleArrowImage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * グローバルメニュー表示.
     */
    protected void displayGlobalMenu() {
        UserState param = UserInfoUtils.getUserState(this);
        DTVTLogger.debug("displayGlobalMenu userState=" + param);
        setUserState(param);
        displayMenu();
    }

    /**
     * 機能：onCreate.
     * Sub classにて、super.onCreate(savedInstanceState)をコールする必要がある
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        DTVTLogger.start();

        // TODO 認証付きアプリのデバッグ時に有効化すると、デバッグのアタッチが行われるまでここで停止する。最終的には削除を行う事
        //android.os.Debug.waitForDebugger();

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        mActivity = this;
        initView();
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();

        //dアカウントの検知処理を追加する
        setRelayClientHandler();

        //STB接続状態を反映.
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null != dlnaDmsItem && dlnaDmsItem.mIPAddress != null && dlnaDmsItem.mIPAddress.length() > 0) {
            mRemoteControlRelayClient.setRemoteIp(dlnaDmsItem.mIPAddress);
        }

        //インテントにダイアログ表示依頼があるかどうかを見る
        checkDialogShowRequest();

        DTVTLogger.end();
    }

    /**
     * スプラッシュ画面からダイアログの表示の依頼を受けたかどうかのチェック.
     */
    private void checkDialogShowRequest() {
        DTVTLogger.start();
        Intent intent = getIntent();

        if (intent.getBooleanExtra(SHOW_SETTING_FILE_DIALOG, false)) {
            //設定ファイルエラーダイアログの表示依頼があったので、表示する
            ProcessSettingFile settingFileInfo = new ProcessSettingFile(this, false);

            Bundle bundle = intent.getBundleExtra(SHOW_SETTING_FILE_DIALOG_DATA);
            SettingFileMetaData metaData =
                    (SettingFileMetaData) bundle.getSerializable(SHOW_SETTING_FILE_DIALOG_DATA);
            DTVTLogger.debug("metaData=" + metaData);
            settingFileInfo.processControlEmulate(metaData);
        }
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
        TvtApplication app = (TvtApplication) getApplication();

        //Googleアナリティクスに画面名を送信する
        mTracker = app.getDefaultTracker();
        //TODO : 難読化を行うと、getClassがランダムな名前になる模様。その前に個別の画面名の設定が必要になる。
        //TODO : 近いうちのスプリントで詳細な情報出力設定が行われる筈なので、ひとまず問題は無い。
        mTracker.setScreenName(this.getClass().getSimpleName());
        mTracker.send(
                new com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder().build());

        // BG → FG でのonResumeかを判定
        if (app.getIsChangeApplicationVisible()) {
            if (DeviceStateUtils.isRootDevice()) {
                showApFinishDialog(getString(R.string.common_root_device_detection_message));
                //Root化を検知した時点で以降の処理はしない
                return;
            }
            permissionCheckExec();
        } else {
            // 通常のライフサイクル
            if (mNecessaryDAccountRegistService) {
                setDaccountControl();
            }
            onStartCommunication();
        }

        StbConnectionManager.shared().mConnectionListener = this;
        DTVTLogger.end();
    }

    @Override
    protected void onRestart() {
        DTVTLogger.start();
        super.onRestart();

        DTVTLogger.end();
    }

    /**
     * dアカウントチェックの再実行.
     */
    protected void checkDAccountOnRestart() {
        //BGからFGに遷移するときにdアカチェックを行う
        if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN) {
            RemoteControlRelayClient.getInstance().isUserAccountExistRequest(getApplicationContext());
        }
    }

    /**
     * 機能：onStop
     * Sub classにて、super.onStop()をコールする必要がある.
     */
    @Override
    protected void onStop() {
        super.onStop();
        DTVTLogger.start();
        resetRelayClientHandler(true);
        TvtApplication app = (TvtApplication) getApplication();
        if (app.getIsChangeApplicationInvisible()) {
            // FG → BG になったためDlnaをstopする
            DTVTLogger.debug("do dlnaOnStop");
        }
        DTVTLogger.end();
    }

    /**
     * 機能：ActivityのSTB接続アイコン.
     */
    private void registerDevListDlna() {
        DTVTLogger.start();
        if (this instanceof StbSelectActivity
                || this instanceof LaunchActivity
            //|| this instanceof RecordedListActivity
                ) {
            DTVTLogger.end();
            return;
        }
        DTVTLogger.end();
    }


    /**
     * STB送信ハンドラ.
     */
    private final Handler mRelayClientHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            DTVTLogger.debug(String.format("msg:%s", msg));
            setRemoteProgressVisible(View.GONE);
            onStbClientResponse(msg);
            resetRelayClientHandler(false);
        }
    };

    /**
     * STBからの応答を通知ハンドラーを解除する.
     *
     * @param isTopRemoteControllerUI 応答ハンドラの解除条件 false:リモコンが表示されていない／true:無条件で解除
     */
    private void resetRelayClientHandler(final boolean isTopRemoteControllerUI) {
        if (null != mRemoteControllerView) {
            // ※リモコンの電源ON/OFF操作中の応答時にリモコンが表示されている間は解除しない
            if ((isTopRemoteControllerUI == mRemoteControllerView.isTopRemoteControllerUI())
                    || isTopRemoteControllerUI) {
                mRemoteControlRelayClient.resetHandler();
            }
        }
    }

    /**
     * リモコンの表示.
     */
    private void showRemoteControllerView() {
        // グローバルメニューまたはコンテンツ詳細からのサービスアプリ連携の正常応答時にリモコンが表示されてない場合のみ表示する.
        if ((null != mRemoteControllerView && !mRemoteControllerView.isTopRemoteControllerUI())
                || null == mRemoteControllerView) {
            menuRemoteController();
        }
    }

    /**
     * STB応答時処理.
     *
     * @param msg 応答メッセージ
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    protected void onStbClientResponse(final Message msg) {
        RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES requestCommand
                = ((RelayServiceResponseMessage) msg.obj).getRequestCommandTypes();
        DTVTLogger.debug(String.format("msg.what:%s requestCommand:%s", msg.what, requestCommand));
        switch (msg.what) {
            case RelayServiceResponseMessage.RELAY_RESULT_OK:
                switchResultOkRequest(requestCommand);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_ERROR:
                int resultCode = ((RelayServiceResponseMessage) msg.obj).getResultCode();
                DTVTLogger.debug(String.format("resultCode:%s", resultCode));
                switch (requestCommand) {
                    case KEYEVENT_KEYCODE_POWER:
                        // STB_REQUEST_COMMAND_TYPES misses case 抑制.
                        // ※RELAY_RESULT_ERROR 応答時は requestCommand に KEYEVENT_KEYCODE_POWER は設定されない
                        break;
                    case START_APPLICATION:
                    case TITLE_DETAIL:
                        RemoteControlRelayClient.STB_APPLICATION_TYPES appId
                                = ((RelayServiceResponseMessage) msg.obj).getApplicationTypes();
                        startApplicationErrorHandler(resultCode, appId);
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                        switchAccountExistCode(resultCode);
                        break;
                    case SET_DEFAULT_USER_ACCOUNT:
                        switchDefaultAccountCode(resultCode);
                        break;
                    case CHECK_APPLICATION_VERSION_COMPATIBILITY:
                        switchApplicationVersionCode(resultCode);
                        break;
                    case CHECK_APPLICATION_REQUEST_PROCESSING:
                        //中継アプリからの応答待ち中に新しい要求を行った場合
                    case COMMAND_UNKNOWN:
                        switchOtherResultCode(resultCode);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * アプリケーション要求処理中チェック（エラー応答時）/受信タイムアウト時.
     * @param resultCode resultCode
     */
    private void switchOtherResultCode(final int resultCode) {
        switch (resultCode) {
            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN) {
                    showErrorDialogOffer(getResources().getString(R.string.main_setting_connect_error_message));
                    //ペアリングアイコンをOFFにする
                    setStbStatus(false);
                }
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY: // 他の端末の要求処理中
                //中継アプリからの応答待ち中に新しい要求を行った場合
                showErrorDialogOffer(getResources().getString(R.string.main_setting_stb_busy_error_message));
                break;
            default:
                break;
        }
    }

    /**
     * アプリケーションバージョンチェック（エラー応答時）.
     * @param resultCode resultCode
     */
    private void switchApplicationVersionCode(final int resultCode) {
        switch (resultCode) {
            case RelayServiceResponseMessage.RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE:
                CustomDialog dTVTUpDateDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
                dTVTUpDateDialog.setContent(getResources().getString(R.string.d_tv_terminal_application_version_update_dialog));
                dTVTUpDateDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        toGooglePlay(DTVTERMINAL_GOOGLEPLAY_DOWNLOAD_URL);
                    }
                });
                //次のダイアログを呼ぶ為の処理
                dTVTUpDateDialog.setDialogDismissCallback(this);

                //ダイアログを表示
                offerDialog(dTVTUpDateDialog);
                //dTVTUpDateDialog.showDialog();
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE:
                showErrorDialogOffer(getResources().getString(R.string.stb_application_version_update));
                break;
            default:
                break;
        }
    }

    /**
     * ユーザーアカウント切り替え（エラー応答時）.
     * @param resultCode resultCode
     */
    private void switchDefaultAccountCode(final int resultCode) {
        switch (resultCode) {
            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR:
                //サーバエラー
            case RelayServiceResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE:
                //ユーザアカウントチェックサービス未登録
            case RelayServiceResponseMessage.RELAY_RESULT_USER_INVALID_STATE:
                //STBの中継アプリ~応答が無かった場合(要求はできたのでSTBとの通信はOK)
                showErrorDialogOffer(getResources().getString(R.string.main_setting_connect_error_message));
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                // チェック処理の状態で処理を分岐する
                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                // TODO アプリのキャッシュをきれいにクリアする処理が必要
                showDAccountRegDialog();
                break;
            default:
                break;
        }
    }

    /**
     * ユーザ登録チェック.
     * @param resultCode resultCode
     */
    private void switchAccountExistCode(final int resultCode) {
        switch (resultCode) {
            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR:
                //サーバエラー
            case RelayServiceResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE:
                //ユーザアカウントチェックサービス未登録
                showErrorDialogOffer(getResources().getString(R.string.main_setting_connect_error_message));
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID://指定ユーザIDなし
                showDAccountRegDialog();
                break;
            default:
                break;
        }
    }

    /**
     * result ok.
     * @param requestCommand requestCommand
     */
    private void switchResultOkRequest(final RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES requestCommand) {
        switch (requestCommand) {
            case START_APPLICATION:
            case TITLE_DETAIL:
                showRemoteControllerView();
                break;
            case IS_USER_ACCOUNT_EXIST:
                // 処理なし
                break;
            case SET_DEFAULT_USER_ACCOUNT:
            case CHECK_APPLICATION_VERSION_COMPATIBILITY:
            case CHECK_APPLICATION_REQUEST_PROCESSING:
                // STB_REQUEST_COMMAND_TYPES misses case 抑制.
                // ※RELAY_RESULT_OK 応答時は requestCommand に SET_DEFAULT_USER_ACCOUNT
                //   /CHECK_APPLICATION_VERSION_COMPATIBILITY /CHECK_APPLICATION_REQUEST_PROCESSING は設定されない
            case KEYEVENT_KEYCODE_POWER:
            case COMMAND_UNKNOWN:
                // 処理なし
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
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    private void startApplicationErrorHandler(final int resultCode, final RemoteControlRelayClient.STB_APPLICATION_TYPES appId) {
        String message;
        switch (resultCode) {
            case RelayServiceResponseMessage.RELAY_RESULT_APPLICATION_NOT_INSTALL:
                switch (appId) {
                    case DTV:
                    case DANIMESTORE:
                    case DTVCHANNEL:
                    case HIKARITV:
                    case DAZN:
                        message = getResources().getString(R.string.main_setting_stb_application_launch_fail);
                        showErrorDialog(message);
                        break;
                    //ENUMをSwitchで使用する場合、未使用項目も記載しなければアナライザーがエラー扱いにしてしまうのでUNKNOWNを追加した、
                    case UNKNOWN:
                    default:
                        break;
                }
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_VERSION_CODE_INCOMPATIBLE:
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
            case RelayServiceResponseMessage.RELAY_RESULT_CONNECTION_TIMEOUT:
                //dTV アプリが STBの中継アプリに接続できない場合
                setStbStatus(false);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
            case RelayServiceResponseMessage.RELAY_RESULT_APPLICATION_ID_NOTEXIST:
            case RelayServiceResponseMessage.RELAY_RESULT_APPLICATION_START_FAILED:
            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR:
            default:
                message = getResources().getString(R.string.main_setting_stb_application_launch_fail);
                showErrorDialog(message);
                break;
        }
    }

    /**
     * 機能　GooglePlayのAPPページへ.
     *
     * (設定ファイル制御からも呼ぶので、publicに変更)
     * @param downLoadUrl 当アプリはGooglePlayのダウンロードURL
     */
    public void toGooglePlay(final String downLoadUrl) {
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
     * 機能 エラーメッセージの表示(重複ダイアログ用処理付き).
     *
     * @param errorMessage dialog content
     */
    private void showErrorDialogOffer(final String errorMessage) {
        CustomDialog errorDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        errorDialog.setContent(errorMessage);

        //閉じたときに次のダイアログを呼ぶ処理
        errorDialog.setDialogDismissCallback(this);

        //ダイアログをキューにためる処理
        offerDialog(errorDialog);
    }

    /**
     * 再起動時のダイアログ・引数無しに対応するため、可変長引数とする.
     *
     * @param message 省略した場合はdアカウント用メッセージを表示。指定した場合は、常に先頭文字列のみ使用される
     */
    protected void restartMessageDialog(final String... message) {

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
            public void onOKCallback(final boolean isOK) {
                //OKが押されたので、ホーム画面の表示
                reStartApplication();
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
        //TODO :実装中のため仮の値を返却
        return "Test User";
    }

    /**
     * ユーザ状態設定.
     *
     * @param userState ユーザ状態(ペアリング・契約・ログイン)
     */
    private void setUserState(final UserState userState) {
        synchronized (this) {
            MenuDisplay menu = MenuDisplay.getInstance();
            menu.setActivityAndListener(this, this);
            menu.changeUserState(userState);
        }
    }

    /**
     * ユーザ状態毎の表示.
     */
    private void displayMenu() {
        MenuDisplay.getInstance().display();
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
     * 戻るボタン.
     *
     * @param view 戻るボタンのビュー
     */
    protected void contentsDetailBackKey(final View view) {
        if (this instanceof RecommendActivity
                || this instanceof SearchTopActivity
                || this instanceof TvProgramListActivity
                || this instanceof ChannelListActivity
                || this instanceof RecordedListActivity
                || this instanceof RankingTopActivity
                || this instanceof WatchingVideoListActivity
                || this instanceof ClipListActivity
                || this instanceof VideoTopActivity
                || this instanceof PremiumVideoActivity
                || this instanceof RentalListActivity
                || this instanceof RecordReservationListActivity
                || this instanceof NewsActivity
                || this instanceof SettingActivity) {
            Intent intent = mActivity.getIntent();
            intent.setClass(mActivity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            finish();
        }
    }

    /**
     * コンテンツ詳細のクローズボタン.
     *
     * @param view クローズボタンのビュー
     */
    protected void contentsDetailCloseKey(final View view) {
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
     * リモコン画面を生成する.
     *
     * @param isFirstVisible 表示状態
     */
    protected void createRemoteControllerView(final Boolean isFirstVisible) {
        DTVTLogger.debug("CreateRemoteControllerView");
        RelativeLayout layout = findViewById(R.id.base_remote_controller_rl);
        mRemoteControllerView = layout.findViewById(R.id.remote_control_view);
        mRemoteControllerView.init(this);
        mRemoteControllerView.setIsFirstVisible(isFirstVisible);
        mRemoteControllerView.setOnStartRemoteControllerUI(this);
        int visibility = View.VISIBLE;
        setRemoteControllerViewMargin(visibility);
        layout.setVisibility(visibility);
    }

    @Override
    protected void onPause() {
        if (mRemoteControllerView != null && mRemoteControllerView.isTopRemoteControllerUI()) {
            // onPause時にリモコンUIを閉じる
            mRemoteControllerView.closeRemoteControllerUI();
        }

        dismissDialogOnPause();
        super.onPause();

        //ワンタイムトークン取得のキャンセル
        cancelOttConnection();
    }

    /**
     * リモコンUIにリスナーを設定する.
     *
     * @param listener リスナー
     */
    protected void setStartRemoteControllerUIListener(final RemoteControllerView.OnStartRemoteControllerUIListener listener) {
        mRemoteControllerView.setOnStartRemoteControllerUI(listener);
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
        mRemoteControllerView.sendStartApplicationRequest(type, contentsId);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ・dTVチャンネル
     *
     * @param type                アプリのタイプ
     * @param crid                crid
     * @param chno                チャンネル番号
     * @param serviceCategoryType サービス形態
     */
    protected void requestStartApplicationDtvChannel(
            final RemoteControlRelayClient.STB_APPLICATION_TYPES type,
            final RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES serviceCategoryType,
            final String crid, final String chno) {
        mRemoteControllerView.sendStartApplicationDtvChannelRequest(type, serviceCategoryType, crid, chno);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ・ひかりTVのVOD
     *
     * @param licenseId ライセンスID
     * @param cid       コンテンツID
     * @param crid      crid
     */
    protected void requestStartApplicationHikariTvCategoryHikaritvVod(
            final String licenseId, final String cid, final String crid) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryHikaritvVodRequest(licenseId, cid, crid);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTV内dTVのVOD
     *
     * @param episodeId エピソードID
     */
    protected void requestStartApplicationHikariTvCategoryDtvVod(final String episodeId) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvVodRequest(episodeId);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTV内VOD(dTV含む)のシリーズ
     *
     * @param crid crid
     */
    protected void requestStartApplicationHikariTvCategoryDtvSvod(final String crid) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvSvodRequest(crid);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTV内dTVチャンネルの番組
     * ひかりTV内dTVチャンネルの番組(見逃し、関連VOD予定だが未配信)
     *
     * @param chno チャンネル番号
     */
    protected void requestStartApplicationHikariTvCategoryDtvchannelBroadcast(final String chno) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvchannelBroadcastRequest(chno);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTV内dTVチャンネルのVOD（見逃し）
     *
     * @param tvCid コンテンツID
     */
    protected void requestStartApplicationHikariTvCategoryDtvchannelMissed(final String tvCid) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvchannelMissedRequest(tvCid);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTV内 dTVチャンネル VOD（関連番組）
     *
     * @param tvCid コンテンツID
     */
    protected void requestStartApplicationHikariTvCategoryDtvchannelRelation(final String tvCid) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvchannelRelationRequest(tvCid);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTVの番組 (地デジ)
     *
     * @param chno チャンネル番号
     */
    protected void requestStartApplicationHikariTvCategoryTerrestrialDigital(final String chno) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryTerrestrialDigitalRequest(chno);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTVの番組 （BS）
     *
     * @param chno チャンネル番号
     */
    protected void requestStartApplicationHikariTvCategorySatelliteBs(final String chno) {
        mRemoteControllerView.sendStartApplicationHikariTvCategorySatelliteBsRequest(chno);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTVの番組 （IPTV）
     *
     * @param chno チャンネル番号
     */
    protected void requestStartApplicationHikariTvCategoryIptv(final String chno) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryIptvRequest(chno);
    }

    /**
     * リモコンUI画面を取得.
     *
     * @return RemoteControllerView
     */
    protected RemoteControllerView getRemoteControllerView() {
        return mRemoteControllerView;
    }

    /**
     * リモコンUI画面用 onClickListener.
     */
    protected final View.OnClickListener mRemoteControllerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.header_stb_status_icon:
                    if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN) {
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
                        getRemoteControllerView().startRemoteUI(true);
                    }
                    break;
                default:
                    DTVTLogger.debug("Close Controller");
                    getRemoteControllerView().closeRemoteControllerUI();
                    break;
            }
        }
    };

    /**
     * リモートコントローラーViewのVisibilityを変更.
     *
     * @param visibility 表示非表示指定
     */
    protected void setRemoteControllerViewVisibility(final int visibility) {
        if (mRemoteControllerView != null) {
            setRemoteControllerViewMargin(visibility);
            findViewById(R.id.base_remote_controller_rl).setVisibility(visibility);
        }
    }

    /**
     * リモコン表示時にScrollViewにマージンを設ける.
     * レイアウトファイルの変更で対応できなかったためリモコンボタンの表示領域確保を本メソッドで行う.
     *
     * @param visibility 表示状態
     */
    private void setRemoteControllerViewMargin(final int visibility) {
        DTVTLogger.start();
        //TODO 横画面の情報がないため縦画面の時のみ実装
        if (visibility == View.VISIBLE) {
            //リモコン表示時にScrollViewにマージンを設ける
            Resources resources = getResources();
            int remoteButtonMargin = resources.getDimensionPixelSize(R.dimen.remote_control_display_button_height)
                    + resources.getDimensionPixelSize(R.dimen.remote_control_display_button_top_margin);
            mBaseLinearLayout.setPadding(0, 0, 0, remoteButtonMargin);
            mBaseLinearLayout.setBackgroundResource(R.color.dtv_contents_detail_tab_background_color);
        } else {
            mBaseLinearLayout.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * グローバルメニューからのリモコン表示.
     */
    private void menuRemoteController() {
        if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN) {
            DTVTLogger.debug("Start RemoteControl");
            createRemoteControllerView(false);
            getRemoteControllerView().startRemoteUI(true);
        }
    }

    /**
     * コンテンツ詳細画面から契約するのリモコン表示.
     */
    protected void contentDetailRemoteController() {
        DTVTLogger.debug("Start RemoteControl");
        createRemoteControllerView(true);
        getRemoteControllerView().startRemoteUI(false);
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

            boolean isParamCheck;

            //クリップ状態によりクリップ登録/削除実行
            if (data.isClipStatus()) {
                ClipDeleteWebClient deleteWebClient = new ClipDeleteWebClient(getApplicationContext());
                isParamCheck = deleteWebClient.getClipDeleteApi(data.getType(), data.getCrid(),
                        data.getTitleId(), this);
            } else {
                ClipRegistWebClient registWebClient = new ClipRegistWebClient(getApplicationContext());
                isParamCheck = registWebClient.getClipRegistApi(
                        data.getType(), data.getCrid(),
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
        //指定された文字リソースを表示する
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();

        //クリップ処理終了メッセージ後にフラグを実行中から終了に変更
        mClipRunTime = false;
    }

    /**
     * dアカウントの切り替えや無効化を受信できるように設定を行う.
     */
    protected void setDaccountControl() {
        DTVTLogger.start();

        //UIスレッド上の動作である間にエラー用ダイアログ表示準備を行う
        mFirstDaccountErrorHandler = new Handler();

        //dアカウント関連の処理を依頼する
        mDAccountControl = new DaccountControl();
        mDAccountControl.registService(getApplicationContext(), this);

        DTVTLogger.end();
    }

    /**
     * dアカウントOTTの取得契機で呼ばれる.
     *
     * @param result 正常終了ならばtrue
     */
    protected void onDaccountOttGetComplete(final boolean result) {
        DTVTLogger.start("this = " + this);
        DTVTLogger.end();
    }

    /**
     * dアカウントダイアログ表示を行う.
     *
     * (オーバーライドの為にprotected指定で分離)
     */
    protected void showDAccountErrorDialog() {
        //現状エラー文言は1種類なので、文言切り替えは必要ない
        CustomDialog errorDialog = new CustomDialog(
                BaseActivity.this, CustomDialog.DialogType.ERROR);

        //失敗原因コードを取得
        if (mDAccountControl != null) {
            DTVTLogger.debug("daccount error code = " + mDAccountControl.getResult());
        }

        errorDialog.setContent(getString(R.string.d_account_regist_error));
        //次のダイアログを呼ぶ為の処理
        errorDialog.setDialogDismissCallback(BaseActivity.this);

        //showDialogの代わり・重複ダイアログ実現用
        offerDialog(errorDialog);
    }

    /**
     * RemoteControlRelayClientにHandlerを設定する.
     */
    public void setRelayClientHandler() {
        // TODO MenuDisplay修正時に合わせて修正する
        mRemoteControlRelayClient.setHandler(mRelayClientHandler);
    }

//    TODO 検討中
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
        if (mRemoteControllerView != null && mRemoteControllerView.isTopRemoteControllerUI()) {
            mRemoteControllerView.closeRemoteControllerUI();
            return true;
        }
        return false;
    }

    /**
     * 詳細画面起動元のクラス名を保存するstaticクラス.
     *
     * @param className コンテンツ詳細画面起動元のクラス名
     */
    private synchronized static void setSourceScreen(final String className) {
        BaseActivity.sSourceScreenClass = className;
    }

    /**
     * コンテンツ詳細画面起動元のクラス名を保持する.
     *
     * @param className クラス名
     */
    protected void setSourceScreenClass(final String className) {
        setSourceScreen(className);
    }

    /**
     * コンテンツ詳細画面起動元のクラス名を取得する.
     *
     * @return クラス名
     */
    private String getSourceScreenClass() {
        return sSourceScreenClass;
    }

    /**
     * dip → px変換.
     *
     * @param dip 変換値
     * @return 変換後の値 px値
     */
    public int dip2px(final int dip) {
        return (int) (dip * getDensity() + 0.5f);
    }

    /**
     * プログレスの表示設定.
     *
     * @param visible 表示値
     */
    public void setRemoteProgressVisible(final int visible) {
        findViewById(R.id.base_progress_rl).setVisibility(visible);
    }

    /**
     * ウイザード（多階層コンテンツ）画面に遷移する.
     *
     * @param contentsData コンテンツデータ
     */
    public void startChildContentListActivity(@NonNull final ContentsData contentsData) {
        Intent intent = new Intent(this, ChildContentListActivity.class);
        intent.putExtra(ChildContentListActivity.INTENT_KEY_CRID, contentsData.getCrid());
        intent.putExtra(ChildContentListActivity.INTENT_KEY_TITLE, contentsData.getTitle());
        intent.putExtra(ChildContentListActivity.INTENT_KEY_DISP_TYPE, contentsData.getDispType());
        startActivity(intent);
    }

    /**
     * dアカウントの削除や変更後に、自アプリがフォアグラウンドならば、アプリの再起動を行う.
     */
    protected void reStartApplication() {
        DTVTLogger.start();

        //再起動処理を行う
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        DTVTLogger.end();
    }

    /**
     * アニメーション付きスタートアクティビティ.
     *
     * @param intent アクティビティ呼び出し情報
     */
    @Override
    public void startActivity(final Intent intent) {
        if (isFastClick()) {
            //普通にアクティビティを起動する
            super.startActivity(intent);

            //dアカウントアプリのバインドを解除する
            final DaccountGetOtt getOtt = new DaccountGetOtt(getApplicationContext());
            if (getOtt != null) {
                DTVTLogger.debug("startActivity before unbind");
                //他の画面に遷移する前に、dアカウントアプリとの連携を終わらせる
                getOtt.daccountServiceEnd();
            }

            //飛び先画面として指定されていた名前を取得する
            String callName = "";
            if (intent != null && intent.getComponent() != null) {
                callName = intent.getComponent().toShortString();
            }
            //飛び先がSTB選択の関連画面ならば、アニメは付加せず帰る
            if (callName.contains(StbSelectActivity.class.getSimpleName())
                    || callName.contains(StbSelectErrorActivity.class.getSimpleName())) {
                //ただし、設定画面から呼ばれた場合はアニメーションは行うので帰らない
                if (!(this instanceof SettingActivity)) {
                    return;
                }
            }

            //アニメーションを付加する
            overridePendingTransition(R.anim.in_righttoleft, R.anim.out_lefttoright);
        }
    }

    /**
     * SIM情報取得permission取得 / 海外通信判定実行.
     */
    private void permissionCheckExec() {
        DTVTLogger.start();
        if (!mShowPermissionDialogFlag) {
            // 許可されているかを判定
            if (RuntimePermissionUtils.hasSelfPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
                checkCommunication();
            } else {
                // "今後表示しない"判定
                if (RuntimePermissionUtils.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    // ・パーミッション要求ダイアログが拒否された場合（「今後は確認しない」チェックなし）
                    // ・一度は許可したが設定アプリから拒否された場合
                    SharedPreferencesUtils.setSharedPreferencesIsDisplayedPermissionDialogTwice(mContext);
                    mShowDialog = createPermissionDetailDialog();
                    mShowDialog.showDialog();
                } else {
                    // ・初めてパーミッション要求ダイアログが表示された場合
                    // ・パーミッション要求ダイアログが拒否された場合（「今後は確認しない」チェックあり）
                    if (!SharedPreferencesUtils.getSharedPreferencesIsDisplayedPermissionDialogTwice(mContext)) {
                        // 「今後は確認しない」チェックなし
                        mShowDialog = createPermissionDetailDialog();
                        mShowDialog.showDialog();
                    } else {
                        // 海外判定を行わず通信を行う
                        onReStartCommunication();
                    }
                }
            }
        } else {
            mShowPermissionDialogFlag = false;
            if (RuntimePermissionUtils.hasSelfPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
                // 海外判定の実施
                checkCommunication();
            } else {
                // 海外判定を行わず通信を行う
                onReStartCommunication();
            }
        }
        DTVTLogger.end();
    }

    /**
     * 海外通信不許可時ダイアログ.
     *
     * @return 表示するCustomDialog
     */
    private CustomDialog createPermissionDeniedDialog() {
        DTVTLogger.start();
        CustomDialog dialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        dialog.setOnTouchOutside(false);
        dialog.setCancelable(false);
        // TODO 文言は仮実装
        dialog.setContent(this.getResources().getString(R.string.permission_denied_dialog_content));
        // TODO 文言は仮実装
        dialog.setTitle(this.getResources().getString(R.string.permission_denied_dialog_title));
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                mShowDialog = null;
                finishAffinity();
            }
        });
        DTVTLogger.end();
        return dialog;
    }

    /**
     * 海外通信警告ダイアログの設定・表示.
     *
     * @return 表示するCustomDialog
     */
    private CustomDialog createTransoceanicCommunicationDialog() {
        DTVTLogger.start();
        CustomDialog dialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
        dialog.setCancelable(false);
        dialog.setOnTouchOutside(false);
        dialog.setConfirmText(R.string.transoceanic_communication_dialog_confirm);
        dialog.setConfirmVisibility(View.VISIBLE);
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                onReStartCommunication();
            }
        });
        dialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                //Serviceでダウンロード中のタスクもキャンセル
                DownloadDataProvider.cancelAll();
                mShowDialog = createPermissionDeniedDialog();
                mShowDialog.showDialog();
            }
        });
        dialog.setContent(this.getResources().getString(
                R.string.transoceanic_communication_dialog_content));
        dialog.setTitle(this.getResources().getString(
                R.string.transoceanic_communication_dialog_title));
        DTVTLogger.end();
        return dialog;
    }

    /**
     * permissionの用途を表示するダイアログ.
     * TODO 文言は仮実装
     *
     * @return 表示するCustomDialog
     */
    private CustomDialog createPermissionDetailDialog() {
        DTVTLogger.start();
        CustomDialog dialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        dialog.setDialogDismissCallback(this);
        dialog.setCancelable(false);
        dialog.setOnTouchOutside(false);
        dialog.setConfirmText(R.string.permission_detail_dialog_confirm);
        dialog.setConfirmVisibility(View.VISIBLE);
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // permission許可ダイアログ表示中判定Flagを立てる
                    mShowPermissionDialogFlag = true;
                    mShowDialog = null;
                    // permission許可ダイアログを表示
                    mActivity.requestPermissions(
                            new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
                }
            }
        });
        dialog.setContent(this.getResources().getString(
                R.string.permission_detail_dialog_content));
        dialog.setTitle(this.getResources().getString(
                R.string.permission_detail_dialog_title));
        DTVTLogger.end();
        return dialog;
    }

    /**
     * 海外通信判定を実施.
     */
    private void checkCommunication() {
        DTVTLogger.start();
        // ネットワーク網からMCC情報取得　日本(440,441) 以外で海外判定となる
        TelephonyManager telManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        DTVTLogger.debug("mcc + mnc : " + telManager.getNetworkOperator());
        String strMccMnc = telManager.getNetworkOperator();
        // MCC情報取得判定
        if (strMccMnc.length() > 0) {
            String strMcc = strMccMnc.substring(0, 3);
            int intMcc = Integer.parseInt(strMcc);
            if (intMcc == DOMESTIC_COMMUNICATION_MCC_1
                    || intMcc == DOMESTIC_COMMUNICATION_MCC_2) {
                // 国内通信
                onReStartCommunication();
            } else {
                // 海外通信
                showTransoceanicCommunicationDialog();
                mShowDialog = createTransoceanicCommunicationDialog();
                mShowDialog.showDialog();
            }
        } else {
            // SIM情報なし
            onReStartCommunication();
        }
        DTVTLogger.end();
    }

    /**
     * オーバーライド先でdismissDialogを無効化する為に独立化.
     */
    protected void dismissDialogOnPause() {
        dismissDialog();
    }

    /**
     * 表示中のダイアログがある場合、閉じる.
     */
    private void dismissDialog() {
        DTVTLogger.start();
        if (mShowDialog != null) {
            mShowDialog.dismissDialog();
            DTVTLogger.debug("Show Dialog Dismiss");
        }
        DTVTLogger.end();
    }

    /**
     * 海外通信警告ダイアログの表示通知.
     * サービスを含む全通信を止める.
     */
    private void showTransoceanicCommunicationDialog() {
        DTVTLogger.start();
        resetRelayClientHandler(true);
        mRemoteControlRelayClient.stopConnect();
        // dアカウント通信を止める
        if (mDAccountControl != null) {
            mDAccountControl.stopCommunication();
        }

        //ワンタイムトークンに通信停止を通知する
        setOttDisconnectionFlag(true);

        DTVTLogger.end();
    }

    /**
     * 通信可能通知.
     * onResume処理及び止めた通信の再開処理を行う.
     * TODO 各ActivityはこのメソッドをOverrideしてResume処理を行う.
     * TODO ※必ずBaseActivityのメソッドを呼び出してから各ActivityのResume処理を行う
     */
    protected void onStartCommunication() {
        DTVTLogger.start();
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null == dlnaDmsItem) {
            return;
        }

        DTVTLogger.debug("RestartFlag check "
                + SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        // 再起動フラグがtrueならば、再起動メッセージを表示する
        if (SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext())) {
            restartMessageDialog();
        }

        //再起動フラグをOFFにする
        SharedPreferencesUtils.setSharedPreferencesRestartFlag(getApplicationContext(), false);

        DTVTLogger.debug("RestartFlag false "
                + SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        //ワンタイムトークンに通信可能を通知する
        setOttDisconnectionFlag(false);

        DTVTLogger.end();
    }

    /**
     * アプリがBGからFGに遷移した際のResume処理.
     */
    protected void onReStartCommunication() {
        DTVTLogger.start();
        setRelayClientHandler();
        mRemoteControlRelayClient.resetIsCancel();
        if (mNecessaryDAccountRegistService) {
            setDaccountControl();
        }
        //アプリ設定ファイルの判定
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkSettingFile();
            }
        }).start();

        checkDAccountOnRestart();
        onStartCommunication();

        //ワンタイムトークンに通信再開可能を通知する
        setOttDisconnectionFlag(false);

        DTVTLogger.end();
    }

    /**
     * 設定ファイルのチェック.
     *
     * (このタイミングでチェックを行うと、自ずとアプリ起動時か、BG→FG遷移時でのチェックになる)
     */
    protected void checkSettingFile() {
        //ダイアログ非表示スイッチ・ダイアログは表示
        boolean noDialogSw = false;

        //スプラッシュ画面かどうかの確認
        if (this instanceof LaunchActivity) {
            //スプラッシュ画面ならばダイアログは表示しない
            noDialogSw = true;
        }

        //アプリ起動時か、BG→FG遷移時は設定ファイルの処理を呼び出す
        mCheckSetting = new ProcessSettingFile(this, noDialogSw);

        //今回はコールバックは使用しないので、ヌルを指定する
        mCheckSetting.controlAtSettingFile(null);
    }

    /**
     * ワンタイムトークン取得完全停止.
     *
     * @param disconnectionFlag 通信を止めるならばtrue
     */
    private void setOttDisconnectionFlag(final boolean disconnectionFlag) {
        DTVTLogger.start();
        final DaccountGetOtt getOtt = new DaccountGetOtt();
        if (getOtt != null) {
            DTVTLogger.debug("setDisconnectionFlag = " + disconnectionFlag);
            //通信切断フラグを指定された値にセット
            getOtt.setDisconnectionFlag(disconnectionFlag);
        }
        DTVTLogger.end();
    }

    /**
     * 現在のワンタイムトークン取得のキャンセル.
     */
    private void cancelOttConnection() {
        DTVTLogger.start();
        final DaccountGetOtt getOtt = new DaccountGetOtt();
        if (getOtt != null) {
            //通信キャンセル呼び出し
            getOtt.cancelConnection();
        }
        DTVTLogger.end();
    }

    /**
     * dアカウント処理が不要なクラスはこのメソッドを実行する.
     */
    protected void setUnnecessaryDaccountRegistService() {
        // STBSelectActivity以前のActivityが対象
        mNecessaryDAccountRegistService = false;
    }

    /**
     * HomeとRankingTopのコンテンツビュー作成.
     *
     * @param contentsCount ContentsPosition
     * @return ContentView
     */
    protected View setContentsView(final int contentsCount) {
        View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
        view.setTag(contentsCount);
        view.setVisibility(View.GONE);
        return view;
    }

    /**
     * データが取得失敗した時のトースト表示.
     */
    protected void showGetDataFailedToast() {
        Toast.makeText(
                this, R.string.common_get_data_failed_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * データが取得失敗した時のトースト表示(メッセージ付き).
     *
     * @param message トースト表示するメッセージ
     */
    protected void showGetDataFailedToast(final String message) {
        if (TextUtils.isEmpty(message)) {
            //メッセージが空文字ならば、既存のメッセージ表示を呼び出す
            showGetDataFailedToast();
        } else {
            //指定されたメッセージを表示する
            Toast.makeText(
                    this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * データが取得失敗した時のダイアログ表示（メッセージ指定）.
     *
     * @param context コンテキスト
     * @param message メッセージ
     */
    protected void showDialogToClose(final Context context, final String message) {
        CustomDialog closeDialog = new CustomDialog(context, CustomDialog.DialogType.ERROR);
        closeDialog.setContent(message);
        closeDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                contentsDetailBackKey(null);
            }
        });
        //戻るボタン等でダイアログが閉じられた時もOKと同じ挙動
        closeDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                //NOP
            }

            @Override
            public void otherDismissCallback() {
                contentsDetailBackKey(null);
            }
        });
        closeDialog.setCancelable(false);
        closeDialog.showDialog();
    }

    /**
     * データが取得失敗した時のダイアログ表示.
     * @param context コンテキスト
     */
    protected void showDialogToClose(final Context context) {
        //文字列リソースを取得して、メッセージ指定側に処理を移譲
        showDialogToClose(this, getApplicationContext().getString(
                R.string.common_get_data_failed_message));
    }

    /**
     * dアカウント登録ヘルプ画面遷移するダイアログ表示.
     */
    private void showDAccountRegDialog() {
        CustomDialog dAccountRegDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        dAccountRegDialog.setContent(getResources().getString(
                R.string.main_setting_stb_application_launch_fail_id_notexist));
        dAccountRegDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                Intent intent = new Intent(getApplicationContext(), DaccountSettingHelpActivity.class);
                startActivity(intent);
            }
        });
        dAccountRegDialog.showDialog();
    }

    /**
     * アプリ終了ダイアログ.
     *
     * @param errorMessage エラーメッセージ
     */
    private void showApFinishDialog(final String errorMessage) {
        CustomDialog apFinishDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        apFinishDialog.setContent(errorMessage);
        apFinishDialog.showDialog();
        apFinishDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                finish();
            }
            @Override
            public void otherDismissCallback() {
                //NOP
            }
        });
    }

    /**
     * ダイアログをキューに追加.
     *
     * (設定ファイル処理から呼ぶためにパブリック化)
     * @param dialog キュー表示するダイアログ
     */
    public void offerDialog(final CustomDialog dialog) {
        DTVTLogger.start();

        //現在表示しているダイアログと新たに蓄積されるダイアログの本文を比較する
        String contentText = dialog.getContent();
        if (mShowDialog != null && mShowDialog.getContent().equals(contentText)) {
            //現在表示しているダイアログと本文が同じなので、蓄積せずに帰る
            return;
        }

        //蓄積しているキューの中に同じ文言の物があるかどうかをチェック
        for (CustomDialog customDialog : mLinkedList) {
            if (customDialog.getContent().equals(contentText)) {
                //本文が同じ物が見つかったので、蓄積せずに帰る
                return;
            }
        }

        mLinkedList.offer(dialog);
        pollDialog();
        DTVTLogger.end();
    }

    /**
     * キューにあるダイアログを順に表示.
     */
    private void pollDialog() {
        DTVTLogger.start();
        DTVTLogger.debug("mLinkedList.size()=" + mLinkedList.size());
        if ((mShowDialog == null || !mShowDialog.isShowing()) && mLinkedList.size() > 0) {
            mShowDialog = mLinkedList.poll();
            mShowDialog.showDialog();
        } else {
            if (mShowDialog != null) {
                startNextProcess();
                DTVTLogger.debug("show=" + mShowDialog.isShowing()
                        + ":mLinkedList.size()=" + mLinkedList.size());
            }
        }
        DTVTLogger.end();
    }

    /**
     * 機能
     * 外部ブラウザーを起動する.
     *
     * @param url 起動URL
     */
    protected void startBrowser(final String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
     *コンテンツ一覧ビューを設定.
     * @param contentsDataList コンテンツ情報
     * @param tag 遷移先
     * @param linearLayout linearLayout
     */
   protected void setRecyclerView(final List<ContentsData> contentsDataList, final int tag, final LinearLayout linearLayout) {
       String typeContentName = getContentTypeName(tag);
       View view = linearLayout.getChildAt(tag);
       view.setVisibility(View.VISIBLE);
       TextView typeTextView = view.findViewById(R.id.home_main_item_type_tx);
       ImageView rightArrowImageView = view.findViewById(R.id.home_main_item_right_arrow);
       //各一覧を遷移すること

       rightArrowImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View view) {
               startTo(tag);
           }
       });
       RecyclerView recyclerView = view.findViewById(R.id.home_main_item_recyclerview);
       //コンテンツタイプを設定
       setCountTextView(typeContentName, view, tag);
       typeTextView.setText(typeContentName);
       //リサイクルビューデータ設定
       setRecyclerViewData(recyclerView, contentsDataList, tag);

   }

    /**
     * コンテンツタイプを設定（NOW ON AIR）.
     * @param typeContentName コンテンツタイプ
     * @param view View
     * @param tag tag
     */
    protected void setCountTextView(final String typeContentName, final View view, final int tag) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * コンテンツ一覧タイトル取得.
     * @param tag tag
     * @return コンテンツ一覧タイトル.
     */
    protected String getContentTypeName(final int tag) {
        DTVTLogger.start();
        DTVTLogger.end();
        return null;
    }


    /**
     * コンテンツ一覧ビューを設定.
     * @param recyclerView リサイクルビュー
     * @param contentsDataList コンテンツ情報
     * @param index 遷移先
     */

    private void setRecyclerViewData(final RecyclerView recyclerView, final  List<ContentsData> contentsDataList, final int index) {
        int i = 0;
        if (mActivity instanceof HomeActivity) {
            i = HOME_CONTENTS_DISTINCTION_ADAPTER;
        } else if (mActivity instanceof RankingTopActivity) {
            i = HomeRecyclerViewAdapter.RANKING_CONTENTES_TODAY_SORT;
        }
        HomeRecyclerViewAdapter horizontalViewAdapter = new HomeRecyclerViewAdapter(
                this, contentsDataList, index + i);
        horizontalViewAdapter.setOnItemClickCallBack(this);
        recyclerView.setAdapter(horizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, recyclerView, false);
        RelativeLayout moreData = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        //もっと見るの遷移先を設定
        moreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startTo(index);
            }
        });
        //リサイクルビューデータの設定
        horizontalViewAdapter.setFooterView(footer);
        saveAdapter(index, horizontalViewAdapter);
    }

    /**
     * 各アダプタを保存.
     * @param index 遷移先
     * @param horizontalViewAdapter アダプタ
     */
    protected void saveAdapter(final int index, final HomeRecyclerViewAdapter horizontalViewAdapter) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * 機能
     * 遷移先を設定.
     *
     * @param index 遷移先
     */
    protected void startTo(final int index) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * ページングを行った回数を取得.
     * @param mContentsList ContentsData
     * @return ページング回数
     */
    protected int getCurrentNumber(final List<ContentsData> mContentsList) {
        if (null == mContentsList || 0 == mContentsList.size()) {
            return 0;
        } else if (mContentsList.size() < NUM_PER_PAGE) {
            return 1;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }
    /**
     * ソートを行う.
     *
     * @param channels チャンネル情報リスト
     */
    protected void channelSort(final List<ChannelInfo> channels) {
        for (ChannelInfo channel : channels) {

            ArrayList<ScheduleInfo> scheduleInfo = channel.getSchedules();
            boolean isContinue = false;
            for (ScheduleInfo info : scheduleInfo) {
                if (TextUtils.isEmpty(info.getStartTime())) {
                    DTVTLogger.debug("getStartTime() : null");
                    isContinue = true;
                    break;
                }
            }
            if (isContinue) {
                continue;
            }

            Collections.sort(channel.getSchedules(), new CalendarComparator());
        }
    }

    /**
     * ページのリセット.
     * @param rankingFragmentFactory rankingFragmentFactory
     * @param viewPager viewPager
     */
    protected void resetPaging(final ViewPager viewPager, final RankingFragmentFactory rankingFragmentFactory) {
        synchronized (this) {
            RankingBaseFragment baseFragment = getCurrentFragment(viewPager, rankingFragmentFactory);
            if (null != baseFragment && null != baseFragment.mData) {
                baseFragment.mData.clear();
                baseFragment.noticeRefresh();
            }
        }
    }
    /**
     * Fragmentの取得.
     * @param viewPager viewPager
     * @param rankingFragmentFactory rankingFragmentFactory
     * @return Fragment
     */
    protected RankingBaseFragment getCurrentFragment(final ViewPager viewPager, final RankingFragmentFactory rankingFragmentFactory) {
        int i = viewPager.getCurrentItem();
        if (rankingFragmentFactory != null) {
            if (mActivity instanceof VideoRankingActivity) {

                return rankingFragmentFactory.createFragment(ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, i);
            } else if (mActivity instanceof WeeklyTvRankingActivity) {
                return rankingFragmentFactory.createFragment(ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK, i);
            }
        }
        return null;
    }
    /**
     * tabの関連Viewを初期化.
     * @param tabLayout tabLayout
     * @param tabNames  tabNames
     * @return tabLayout
     */
    protected TabItemLayout initTabData(final TabItemLayout tabLayout, final String[] tabNames) {
        DTVTLogger.start();
        TabItemLayout tmpTabItemLayout = tabLayout;
        if (tabLayout == null) {
            tmpTabItemLayout = new TabItemLayout(mContext);
            tmpTabItemLayout.setTabClickListener((TabItemLayout.OnClickTabTextListener) this);
            RelativeLayout tabRelativeLayout = new RelativeLayout(this);
            if (mActivity instanceof VideoRankingActivity) {
                tmpTabItemLayout.initTabView(tabNames, TabItemLayout.ActivityType.VIDEO_RANKING_ACTIVITY);
                tabRelativeLayout = findViewById(R.id.rl_video_ranking_tab);
            } else if (mActivity instanceof WeeklyTvRankingActivity) {
                tmpTabItemLayout.initTabView(tabNames, TabItemLayout.ActivityType.WEEKLY_RANKING_ACTIVITY);
                tabRelativeLayout = findViewById(R.id.rl_weekly_ranking_tab);
            } else if (mActivity instanceof RecommendActivity) {
                tmpTabItemLayout.initTabView(tabNames, TabItemLayout.ActivityType.RECOMMEND_LIST_ACTIVITY);
                tabRelativeLayout = findViewById(R.id.rl_recommend_tab);
            } else if (mActivity instanceof ClipListActivity) {
                tmpTabItemLayout.initTabView(tabNames, TabItemLayout.ActivityType.CLIP_LIST_ACTIVITY);
                tabRelativeLayout = findViewById(R.id.rl_clip_list_tab);
            } else if (mActivity instanceof SearchTopActivity) {
                tmpTabItemLayout.initTabView(tabNames, TabItemLayout.ActivityType.SEARCH_ACTIVITY);
                tabRelativeLayout = findViewById(R.id.rl_search_tab);
            }
            tabRelativeLayout.addView(tmpTabItemLayout);
        } else {
            tmpTabItemLayout.resetTabView(tabNames);
        }
        DTVTLogger.end();
        return tmpTabItemLayout;
    }

    /**
     * 次に行う処理があれば、オーバーライドしてください.
     */
    protected void startNextProcess() {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * たまっているキューがあれば数を表示する.
     *
     * @return キューの個数。ダイアログが無い場合はゼロ
     */
    protected int getDialogQurCount() {
        if (mShowDialog == null) {
            //ダイアログが存在していないならばゼロを返す
            return 0;
        }

        //キューにたまっていれば個数を返す
        if (mLinkedList.size() > 0) {
            return mLinkedList.size();
        }

        //今表示中ならば1を返す
        if (mShowDialog.isShowing()) {
            return 1;
        }

        //それら以外はゼロを返す
        return 0;
    }
    // region callback
    /**
     * 機能：onClick event for menu.
     *
     * @param view メニューアイコンのビュー
     */
    @Override
    public void onClick(final View view) {
        if (mMenuImageViewForBase == view) {
            if (HEADER_ICON_CLOSE.equals(mMenuImageViewForBase.getTag())) {
                // コンテンツ詳細画面の×ボタン時はコンテンツ詳細画面を閉じる
                contentsDetailCloseKey(view);
            } else {
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    displayGlobalMenu();
                }
            }
        } else if (mHeaderBackIcon == view) {
            contentsDetailBackKey(null);
        }
    }

    @Override
    public void onStartRemoteControl(final boolean isFromHeader) {
        DTVTLogger.debug("base_start_control");
        View base = findViewById(R.id.base_motion_detection_rl);
        base.setOnClickListener(mRemoteControllerOnClickListener);
        base.setVisibility(View.VISIBLE);
        setRelayClientHandler();
    }

    @Override
    public void onEndRemoteControl() {
        DTVTLogger.debug("base_end_control");
        View base = findViewById(R.id.base_motion_detection_rl);
        base.setOnClickListener(null);
        base.setVisibility(View.GONE);
    }


    @Override
    public void onClipRegistResult() {
        mClipButton.setBackgroundResource(R.mipmap.icon_circle_active_clip);
        mClipButton.setTag(CLIP_ACTIVE_STATUS);
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
        mClipButton.setTag(CLIP_OPACITY_STATUS);

        //DB削除開始
        ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
        clipKeyListDataProvider.clipResultDelete(mClipRequestData);
    }

    @Override
    public void onClipDeleteFailure() {
        showClipToast(R.string.clip_delete_error_message);
    }

    @Override
    public void daccountControlCallBack(final boolean result) {
        DTVTLogger.start();

        //エラー表示を行うかどうかのスイッチ
        boolean firstDaccountError = false;

        //初回dアカウント取得かどうかの判定
        if (SharedPreferencesUtils.isFirstDaccountGetProcess(getApplicationContext()) && !result) {
            //初回のdアカウント取得かつ問題が発生していた場合はtrueにする
            firstDaccountError = true;
        } else {
            //初回以外はダイアログを出さないので、処理中フラグはリセット
            mDAccountControl.setDAccountBusy(false);
        }

        //初回dアカウント取得が行われていた場合は終わらせる
        SharedPreferencesUtils.setFirstExecEnd(getApplicationContext());

        onDaccountOttGetComplete(result);
        //dアカウントの登録結果を受け取るコールバック
        if (result) {
            //処理に成功したので、帰る
            DTVTLogger.end("normal end");
            return;
        }

        //dアカウント処理クラスがヌルか、初回起動時にdアカウント取得で問題が発生したかどうかを確認
        if (mDAccountControl == null || firstDaccountError) {
            //処理には失敗したが、動作の続行ができないので、ここで終わらせる。ただ、このコールバックを受けている以上、ヌルになることありえないはず
            //今はUIスレッドではないので、ダイアログの処理を移譲する
            mFirstDaccountErrorHandler.post(new Runnable() {
                @Override
                public void run() {
                    //初回実行時に限り、エラーダイアログを表示する。
                    showDAccountErrorDialog();

                    //ダイアログを出す場合はここで処理中フラグをリセット
                    mDAccountControl.setDAccountBusy(false);
                }
            });

            DTVTLogger.end("null end or first daccount get error");
            return;
        }

        if (mDAccountControl.isOneTimePass() || mDAccountControl.isCheckService()) {
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
    public void allDismissCallback() {
        if (mShowDialog != null) {
            //次のダイアログの判定の為に、今のダイアログの文言をクリアする
            mShowDialog.clearContentText();
        }

        pollDialog();
    }

    @Override
    public void otherDismissCallback() {
        //NOP
    }

    @Override
    public void onItemClickCallBack(final ContentsData contentsData, final OtherContentsDetailData detailData) {

    }

    @Override
    public void onConnectionChangeCallback(final boolean isStbConnectedHomeNetwork) {
        DTVTLogger.warning("isStbConnectedHomeNetwork = " + isStbConnectedHomeNetwork);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStbStatusIcon.setImageResource(isStbConnectedHomeNetwork ? R.mipmap.header_material_icon_tv : R.mipmap.header_material_icon_tv_active);
            }
        });
    }
    // endregion callback
}