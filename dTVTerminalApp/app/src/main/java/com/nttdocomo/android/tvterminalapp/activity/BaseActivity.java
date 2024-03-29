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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Pair;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.common.ChildContentListActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.CustomDrawerLayout;
import com.nttdocomo.android.tvterminalapp.activity.common.MenuDisplay;
import com.nttdocomo.android.tvterminalapp.activity.common.ProcessSettingFile;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.EpisodeAllReadActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecommendActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordReservationListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.DaccountResettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.DaccountSettingHelpActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchStbActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchTermsOfServiceActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.PairingHelpActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.RemoteSetActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.RemoteSetIntroduceActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbConnectActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbSelectErrorActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.StbWifiSetActivity;
import com.nttdocomo.android.tvterminalapp.activity.launch.TutorialActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.RankingTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.NoticeActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.SettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoTopActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.application.TvtApplication;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.commonmanager.TrackerManager;
import com.nttdocomo.android.tvterminalapp.datamanager.RebuildDatabaseTableManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.SettingFileMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.relayclient.RelayServiceResponseMessage;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherUtil;
import com.nttdocomo.android.tvterminalapp.service.download.DownloadDataProvider;
import com.nttdocomo.android.tvterminalapp.struct.CalendarComparator;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ContentDetailUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DeviceStateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.GoogleAnalyticsUtils;
import com.nttdocomo.android.tvterminalapp.utils.RuntimePermissionUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;
import com.nttdocomo.android.tvterminalapp.view.RemoteControllerView;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountControl;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOtt;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountReceiver;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.IDimDefines;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.OttGetAuthSwitch;
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
        CustomDialog.DismissCallback,
        CustomDialog.ApiOKCallback,
        CustomDialog.ApiCancelCallback,
        CustomDialog.ApiListItemClickCallback,
        HomeRecyclerViewAdapter.ItemClickCallback,
        StbConnectionManager.ConnectionListener,
        ProcessSettingFile.OnShowSettingDialogListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        DaccountReceiver.DaccountChangedCallBack {
    /** ヘッダーBaseレイアウト. */
    private LinearLayout mBaseLinearLayout = null;
    /**　ステータスレイアウト. */
    private LinearLayout mStatusLinearLayout = null;
    /**　タイトルレイアウト. */
    private RelativeLayout mHeaderTitleLayout = null;
    /** ヘッダーレイアウト. */
    private RelativeLayout mHeaderLayout = null;
    /** ヘッダータイトル. */
    protected TextView mTitleTextView = null;
    /** ヘッダーロゴ用ImageView. */
    private ImageView mTitleImageView = null;
    /** 番組表タイトル横矢印. */
    protected ImageView mTitleArrowImage = null;
    /** ヘッダーの戻るボタン. */
    private ImageView mHeaderBackIcon = null;
    /** ペアリングアイコン. */
    private ImageView mStbStatusIcon = null;
    /** メニューアイコン. */
    private ImageView mMenuImageViewForBase = null;
    /** リモコンレイアウト. */
    private RemoteControllerView mRemoteControllerView = null;
    /** Activityのコンテキスト. */
    private Context mContext = null;
    /** Activityのコンテキスト. */
    private Activity mActivity = null;
    /** 中継アプリ連携クライアント. */
    protected RemoteControlRelayClient mRemoteControlRelayClient = null;
    /** ダブルクリック防止用. */
    private long mLastClickTime = 0;
    /** クリップボタン. */
    private ImageView mClipButton = null;
    /** クリップリクエスト用データ. */
    private ClipRequestData mClipRequestData = null;
    /**
     * クリップ登録／解除実行中かを取得.
     * @return クリップ登録／解除実行中状態
     */
    public boolean isClipRunTime() {
        return mClipRunTime;
    }
    /** クリップ実行中フラグ.*/
    private boolean mClipRunTime = false;
    /** DialogQue. **/
    private final LinkedList<CustomDialog> mLinkedList = new LinkedList<>();
    /** タイムアウト時間. */
    protected static final int LOAD_PAGE_DELAY_TIME = 1000;
    /** ページング単位.*/
    protected static final int NUM_PER_PAGE = 50;
    /** webViewの読み込み完了値. */
    protected final static int PROGRESS_FINISH = 100;
    /** ダブルクリック抑止用 DELAY. */
    private static final int MIN_CLICK_DELAY_TIME = 500;
    /** スプラッシュ画面用のファイル設定ファイル用ダイアログ表示識別文字列. */
    protected final static String SHOW_SETTING_FILE_DIALOG = "SHOW_SETTING_FILE_DIALOG";
    /** スプラッシュ画面用のファイル設定ファイル用ダイアログ表示内容表示識別文字列. */
    protected final static String SHOW_SETTING_FILE_DIALOG_DATA
            = "SHOW_SETTING_FILE_DIALOG_DATA";
    /** STB選択画面でログアウトが発生してランチャーを再起動する際のパラメータ. */
    protected final static String STB_SELECT_ACTIVITY_RESTART
            = "STB_SELECT_ACTIVITY_RESTART";
    /** dアカウント設定アプリ登録処理. */
    private DaccountControl mDAccountControl = null;
    /** 初回dアカウント取得失敗時のダイアログを呼び出すハンドラー. */
    private Handler mFirstDaccountErrorHandler = null;
    /** 詳細画面起動元Classを保存. */
    private static String sSourceScreenClass = "";
    /**起動時チェックフラグ.*/
    private static boolean sIsLaunchChecked = false;
    /** ヘッダーに表示されているアイコンがメニューアイコンか×ボタンアイコンかを判別するタグ(menu). */
    private static final String HEADER_ICON_MENU = "menu";
    /** ヘッダーに表示されているアイコンがメニューアイコンか×ボタンアイコンかを判別するタグ(close). */
    private static final String HEADER_ICON_CLOSE = "close";
    /** requestPermissions()表示によるonPauseを判断するためのフラグ. */
    private boolean mShowPermissionDialogFlag = false;
    /** 表示中ダイアログ. */
    private CustomDialog mShowDialog = null;
    /** 設定ファイル処理クラス. */
    protected ProcessSettingFile mCheckSetting = null;
    /** 国内通信 MCC (440 Japan). */
    private static final int DOMESTIC_COMMUNICATION_MCC_1 = 440;
    /** 国内通信 MCC (441 Japan). */
    private static final int DOMESTIC_COMMUNICATION_MCC_2 = 441;
    /** dアカウント関連処理の必要有無判定. */
    private boolean mNecessaryDAccountRegistService = true;
    /** ワンタイムトークン取得クラス（dアカウントチェック用）. */
    private DaccountGetOtt mGetOtt = null;
    /** クリップ状態. */
    public static final String CLIP_ACTIVE_STATUS = "active";
    /** 未クリップ状態. */
    public static final String CLIP_OPACITY_STATUS = "opacity";
    /** クリップ状態. */
    public static final String CLIP_SCHEDULE_END_ACTIVE_STATUS = "schedule_end_active";
    /** 未クリップ状態. */
    public static final String CLIP_SCHEDULE_END_OPACITY_STATUS = "schedule_end_opacity";
    /** アダプタ内でのリスト識別用定数. */
    private final static int HOME_CONTENTS_DISTINCTION_ADAPTER = 10;
    /**
     * リモコン表示時の鍵交換処理フラグ.
     * ※ペアリングアイコンからのリモコン表示時のみ鍵交換を行う
     */
    private static boolean mKeyExchangeFlag = false;
    /** 終了後にタスク一覧からどこテレアプリを削除する場合に指定するフラグ. */
    public static final  String FORCE_FINISH = "FORCE_FINISH";
    /** DisplayMetrics.*/
    private DisplayMetrics mDisplayMetrics = null;
    /** Toast（連続して出さない為に保持）. */
    public Toast mToast = null;
    /** 番組表情報取得DataProvider. */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /** 遷移先のクラス名. */
    private String mClassName = null;
    /** CustomDrawerLayout.*/
    private CustomDrawerLayout mDrawerLayout;
    /**メニューリストビュー.*/
    private ListView mGlobalMenuListView = null;
    /** コンテンツ種別1のコンテンツ種別名のひかりTVタイプ.*/
    private ContentUtils.HikariType mHikariType = ContentUtils.HikariType.H4D;
    /** 詳細画面STB連携タイトル名.*/
    private String mTitle = "";
    /** コンテンツ種別2のコンテンツ種別名.*/
    private ContentUtils.ContentsType mContentsType = ContentUtils.ContentsType.VOD;
    /** bg→fg区別.*/
    protected boolean mIsFromBgFlg = false;

    /**
     * リモコン表示時の鍵交換の必要性.
     */
    private static boolean isKeyExchangeRequired() {
        DTVTLogger.debug(String.format("isKeyExchangeRequired:%s", mKeyExchangeFlag));
        return mKeyExchangeFlag;
    }

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
        mDrawerLayout = findViewById(R.id.baseCustomDrawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mGlobalMenuListView = findViewById(R.id.menu_list);
        changeGlobalMenuNewIcon(false);
        DTVTLogger.end();
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
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    protected void enableStbStatusIcon(final boolean isOn) {
        if (mStbStatusIcon != null) {
            mStbStatusIcon.setVisibility(isOn ? View.VISIBLE : View.INVISIBLE);
            if (!isOn) {
                return;
            }
            switch (StbConnectionManager.shared().getConnectionStatus()) {
                case HOME_IN:
                    setStbStatus(true);
                    break;
                case NONE_PAIRING:
                case NONE_LOCAL_REGISTRATION:
                case HOME_OUT:
                case HOME_OUT_CONNECT:
                default:
                    setStbStatus(false);
                    break;
            }
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
                if (isOn) {
                    mStbStatusIcon.setImageResource(R.mipmap.header_material_icon_tv);
                    //ペアリングアイコンがOFF→ON(点灯)になった際にdアカチェックを行う
                } else {
                    mStbStatusIcon.setImageResource(R.mipmap.header_material_icon_tv_active);
                }
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
                mMenuImageViewForBase.setImageResource(R.drawable.header_menu_selector);
                mMenuImageViewForBase.setTag(HEADER_ICON_MENU);
            } else {
                mMenuImageViewForBase.setImageResource(R.mipmap.header_material_icon_close);
                mMenuImageViewForBase.setTag(HEADER_ICON_CLOSE);
            }
        }
    }

    /**
     * 新着お知らせのメニューアイコンを切り替え
     * @param isNewly   true: 新着お知らせあり。false: 新着お知らせなし
     */
    protected void changeGlobalMenuNewIcon(final boolean isNewly) {
        if (null != mMenuImageViewForBase && !HEADER_ICON_CLOSE.equals(mMenuImageViewForBase.getTag())) {
            if (isNewly || SharedPreferencesUtils.getUnreadNewlyNotice(mContext)) {
                mMenuImageViewForBase.setImageResource(R.drawable.header_menu_news_selector);
            } else {
                mMenuImageViewForBase.setImageResource(R.drawable.header_menu_selector);
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
     *
     * @param text 設定する文字列
     */
    protected void setTitleText(final CharSequence text) {
        if (this instanceof HomeActivity) {
            if (mTitleImageView != null) {
                //ヘッダーに「ドコモテレビターミナル」画像を表示する対応
                mTitleTextView.setVisibility(View.GONE);
                mTitleImageView.setVisibility(View.VISIBLE);
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
     * グローバルメニューアイコンのインスタンスのゲッター.
     *
     * @return グローバルメニューのアイコンのビュー
     */
    protected View getMenuImageViewForBase() {
        return mMenuImageViewForBase;
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

        // 認証付きアプリのデバッグ時に有効化すると、デバッグのアタッチが行われるまでここで停止する。デバッグ時に有効化して利用。
        //android.os.Debug.waitForDebugger();

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        if (!(this instanceof LaunchActivity)) {
            if (!DlnaManager.shared().dlnaIsLaunched() && DlnaManager.shared().getContext() == null) {
                DlnaManager.shared().dlnaRestart(getApplicationContext());
            }
        }
        mContext = this;
        mActivity = this;
        initView();
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();

        //全アクティビティ終了とタスク一覧削除の仕上げの最終確認
        checkForceFinishAndTaskRemove();

        //dアカウントの検知処理を追加する
        setRelayClientHandler();

        //STB接続状態を反映.
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null != dlnaDmsItem && dlnaDmsItem.mIPAddress != null && dlnaDmsItem.mIPAddress.length() > 0) {
            mRemoteControlRelayClient.setRemoteIp(dlnaDmsItem.mIPAddress);
        }

        //インテントにダイアログ表示依頼があるかどうかを見る
        checkDialogShowRequest();

        //その画面の最初のワンタイムトークン取得処理では、認証画面を表示するようにフラグを初期化する
        OttGetAuthSwitch.INSTANCE.setNowAuth(true, this);

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
            mCheckSetting = new ProcessSettingFile(this, false);
            mCheckSetting.setOnShowSettingDialogListener(this);

            Bundle bundle = intent.getBundleExtra(SHOW_SETTING_FILE_DIALOG_DATA);
            SettingFileMetaData metaData =
                    (SettingFileMetaData) bundle.getSerializable(SHOW_SETTING_FILE_DIALOG_DATA);
            DTVTLogger.debug("metaData=" + metaData);
            mCheckSetting.processControlEmulate(metaData);
        }
        DTVTLogger.end();
    }

    /**
     * アプリ起動不可状態で、最終的にタスク一覧から削除を行う処理.
     */
    private void checkForceFinishAndTaskRemove() {
        DTVTLogger.start();
        Intent intent = getIntent();

        //アプリ起動不可状態でホーム画面が起動されたかどうかの判定
        if (intent.getBooleanExtra(FORCE_FINISH, false)) {
            //Android4.4端末でここに来る事はないが、一応チェック
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //アクティビティを終了し、タスク一覧から削除を行う
                finishAndRemoveTask();
            }
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
        if (TrackerManager.shared().getAppContext() == null) {
            TrackerManager.shared().setAppContext(app);
        }

        DaccountReceiver.setDaccountChangedCallBack(this);

        //dアカウント認証画面の表示制御のインスタンスを取得
        OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.getInstance();

        // BG → FG でのonResumeかを判定
        // dアカウント変更後の再起動フラグやパーミッションチェックスキップフラグが立っているならば、パーミッションチェックなどは行わない
        if (app.getIsChangeApplicationVisible()
                && !SharedPreferencesUtils.getSharedPreferencesRestartFlag(
                getApplicationContext())
                && !ottGetAuthSwitch.isSkipPermission()) {
            if (DeviceStateUtils.isRootDevice()) {
                onStopAutoTransition();
                showApFinishDialog(getString(R.string.common_root_device_detection_message));
                //Root化を検知した時点で以降の処理はしない
                return;
            }
            permissionCheckExec();
        } else {
            onStartCommunication();
            //パーミッションチェックフラグの効果は1度のみなので、リセットする
            ottGetAuthSwitch.setSkipPermission(false);
        }
        mIsFromBgFlg = false;
        //BGからFGに遷移した場合の検知
        if (app.getIsChangeApplicationVisible()) {
            //dアカウントチェックを呼ぶ
            chkDaccountRegist();

            //ユーザー情報変更判定を呼ぶ
            checkUserInfoChange();
            if (!(this instanceof LaunchActivity)) {
                //リモート視聴設定期限表示
                localRegisterExpireDateCheck();
            }
            mIsFromBgFlg = true;
            // BG → FG でのonResumeで TvProgramIntentService を開始
            startTvProgramIntentService();
        } else if (!sIsLaunchChecked && this instanceof HomeActivity) {
            localRegisterExpireDateCheck();
            downloadStatusCheck();
            sIsLaunchChecked = true;
        }
        mClassName = null;
        StbConnectionManager.shared().setConnectionListener(this);
        DTVTLogger.end();
    }

    /**
     * アプリ起動時に番組表データ(先頭10件)取得.
     */
    protected void startTvProgramIntentService() {
        //番組表画面では別の番組表データ取得処理が実行されるのでサービスを実行しない
        if (this instanceof TvProgramListActivity) {
            return;
        }
        DTVTLogger.debug("do TvProgramIntentService start");
        mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(BaseActivity.this);
        mScaledDownProgramListDataProvider.startTvProgramIntentService();
    }

    @Override
    protected void onRestart() {
        DTVTLogger.start();
        super.onRestart();

        DTVTLogger.end();
    }

    /**
     * dアカウントチェックの再実行(STB連絡用).
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
            DlnaManager.shared().StopDmp();
            if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_OUT_CONNECT) {
                DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
                if (dlnaDmsItem != null) {
                    DlnaManager.shared().RequestRemoteDisconnect(dlnaDmsItem.mUdn);
                }
            }
            DTVTLogger.debug("do dlnaOnStop");
            if (mScaledDownProgramListDataProvider != null) {
                // FG → BG時に TvProgramIntentService を stop する
                DTVTLogger.debug("do TvProgramIntentService stop");
                mScaledDownProgramListDataProvider.stopTvProgramIntentService();
                mScaledDownProgramListDataProvider = null;
            }
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
                || this instanceof StbConnectActivity
                || this instanceof TutorialActivity
                || this instanceof LaunchStbActivity
                || this instanceof StbWifiSetActivity
                || this instanceof RemoteSetActivity
                || this instanceof RemoteSetIntroduceActivity
                || this instanceof StbSelectErrorActivity
                || this instanceof DaccountResettingActivity
                || this instanceof PairingHelpActivity
                ) {
            DTVTLogger.end();
            return;
        }
        if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.NONE_PAIRING) {
            return;
        }
        if (DlnaManager.shared().getContext() == null) {
            DlnaManager.shared().setContext(getApplicationContext());
        }
        DlnaManager.shared().StartDmp();
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
     * @param isFromDetail true コンテンツ詳細画面、false メニュー
     */
    private void showRemoteControllerView(final boolean isFromDetail) {
        // グローバルメニューまたはコンテンツ詳細からのサービスアプリ連携の要求時にリモコンが表示されてない場合のみ表示する.
        if ((null != mRemoteControllerView && !mRemoteControllerView.isTopRemoteControllerUI())
                || null == mRemoteControllerView) {
            if (isFromDetail) {
                contentDetailRemoteController();
            } else {
                menuRemoteController();
            }
        }
    }

    /**
     * メニューからのサービスアプリ起動時にリモコンを表示する.
     */
    public void showRemoteControllerViewWithStartApplication() {
        showRemoteControllerView(false);
    }

    /**
     * result ok.
     * @param isFromMenu requestCommand
     * @param appType appType
     */
    @SuppressWarnings("OverlyLongMethod")
    private void sendGoogleAnalyticsEvent(final boolean isFromMenu, final RemoteControlRelayClient.STB_APPLICATION_TYPES appType) {
        String category = "";
        String action = "";
        String label = "";
        String contentsType1 = "";
        boolean isUnknownService = false;
        switch (appType) {
            case DTV:
                category = getString(R.string.google_analytics_category_service_name_dtv);
                if (isFromMenu) {
                    action = getString(R.string.google_analytics_category_action_dtv);
                } else {
                    contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_pure_dtv);
                    action = getString(R.string.google_analytics_category_action_watch_tv);
                    label = getTitleText().toString();
                }
                break;
            case DANIMESTORE:
                category = getString(R.string.google_analytics_category_service_name_danime);
                if (isFromMenu) {
                    action = getString(R.string.google_analytics_category_action_danime);
                } else {
                    contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_pure_danime);
                    action = getString(R.string.google_analytics_category_action_watch_tv);
                    label = getTitleText().toString();
                }
                break;
            case DTVCHANNEL:
                category = getString(R.string.google_analytics_category_service_name_dtv_ch);
                if (isFromMenu) {
                    action = getString(R.string.google_analytics_category_action_dtv_ch);
                } else {
                    contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_pure_dtv_ch);
                    action = getString(R.string.google_analytics_category_action_watch_tv);
                    label = getTitleText().toString();
                }
                break;
            case HIKARITV:
                category = getString(R.string.google_analytics_category_service_name_h4d);
                if (isFromMenu) {
                    action = getString(R.string.google_analytics_category_action_h4d);
                } else {
                    contentsType1 = ContentUtils.getContentsType1(BaseActivity.this, mHikariType);
                    action = getString(R.string.google_analytics_category_action_watch_tv);
                    label = mTitle;
                }
                break;
            case DAZN:
                category = getString(R.string.google_analytics_category_service_name_dazn);
                contentsType1 = getString(R.string.google_analytics_custom_dimension_contents_type1_pure_dazn);
                if (isFromMenu) {
                    action = getString(R.string.google_analytics_category_action_dazn);
                } else {
                    action = getString(R.string.google_analytics_category_action_watch_tv);
                    label = getTitleText().toString();
                }
                break;
            case UNKNOWN:
            default:
                isUnknownService = true;
                break;
        }
        if (!isUnknownService) {
            SparseArray<String> customDimensions = null;
            if (!isFromMenu) {
                customDimensions = new SparseArray<>();
                customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTENTSTYPE1, contentsType1);
                switch (mContentsType) {
                    case TV:
                        customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTENTSTYPE2, getString(R.string.google_analytics_custom_dimension_contents_type2_live));
                        break;
                    case VOD:
                        customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTENTSTYPE2, getString(R.string.google_analytics_custom_dimension_contents_type2_void));
                        break;
                    default:
                        break;
                }
                customDimensions.put(ContentUtils.CUSTOMDIMENSION_DEVICE, getString(R.string.google_analytics_custom_dimension_device_tv));
            }
            sendEvent(category, action, label, customDimensions);
        }
    }

    /**
     * コンテンツ種別1のコンテンツ種別名のひかりTV種別設定.
     * @param hikariType ひかりTV種別
     */
    protected void setHikariType(final ContentUtils.HikariType hikariType) {
        mHikariType = hikariType;
    }

    /**
     * アクション名設定.
     * @param titleName タイトル名
     */
    protected void setActionName(final String titleName) {
        mTitle = titleName;
    }

    /**
     * コンテンツ種別2のコンテンツ種別名.
     * @param contentsType コンテンツ種別
     */
    protected void setContentsType(final ContentUtils.ContentsType contentsType) {
        mContentsType = contentsType;
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
                RemoteControlRelayClient.STB_APPLICATION_TYPES appType
                        = ((RelayServiceResponseMessage) msg.obj).getApplicationTypes();
                switch (requestCommand) {
                    case KEYEVENT_KEYCODE_POWER:
                        sendEvent(getString(R.string.google_analytics_category_remote_control),
                                getString(R.string.google_analytics_category_action_remote_control_power),
                                null, null);
                        break;
                    case START_APPLICATION:
                        sendGoogleAnalyticsEvent(true, appType);
                        break;
                    case TITLE_DETAIL:
                        sendGoogleAnalyticsEvent(false, appType);
                        RemoteControllerView remoteControllerView = getRemoteControllerView();
                        if (remoteControllerView != null) {
                            TextView headerTextView = remoteControllerView.findViewById(R.id.watch_by_tv);
                            headerTextView.setText(getString(R.string.remote_controller_viewpager_text_use_remote));
                        }
                        break;
                    case IS_USER_ACCOUNT_EXIST:
                    case SET_DEFAULT_USER_ACCOUNT:
                    case CHECK_APPLICATION_VERSION_COMPATIBILITY:
                    case CHECK_APPLICATION_REQUEST_PROCESSING:
                    case COMMAND_UNKNOWN:
                    default:
                        break;
                }
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
                    showCommonControlErrorDialog(getResources().getString(R.string.str_launch_stb_communication_failed_error), null, null, null, null);
                    //ペアリングアイコンをOFFにする
                    setStbStatus(false);
                } else {
                    showCommonControlErrorDialog(getResources().getString(R.string.main_setting_connect_error_message), null, null, null, null);
                }
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_RELAY_SERVICE_BUSY: // 他の端末の要求処理中
                //中継アプリからの応答待ち中に新しい要求を行った場合
                showCommonControlErrorDialog(getResources().getString(R.string.main_setting_stb_busy_error_message), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR:// 中継アプリの内部エラー
                showCommonControlErrorDialog(getResources().getString(R.string.main_setting_connect_error_message), null, null, null, null);
                break;
            default:
                break;
        }
    }

    /**
     * アプリケーションバージョンチェック（エラー応答時）.
     * @param resultCode resultCode
     */
    protected void switchApplicationVersionCode(final int resultCode) {
        switch (resultCode) {
            case RelayServiceResponseMessage.RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE:
                CustomDialog dTVTUpDateDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.CONFIRM);
                dTVTUpDateDialog.setContent(getResources().getString(R.string.d_tv_terminal_application_version_update_dialog));
                dTVTUpDateDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        DTVTLogger.debug("Application version check incompatible dialog [ok] to GooglePlay");
                        toGooglePlay(UrlConstants.WebUrl.DTVT_GOOGLEPLAY_DOWNLOAD_URL);
                    }
                });
                //次のダイアログを呼ぶ為の処理
                dTVTUpDateDialog.setDialogDismissCallback(this);

                //ダイアログを表示
                offerDialog(dTVTUpDateDialog);
                //dTVTUpDateDialog.showDialog();
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE:
                showCommonControlErrorDialog(getResources().getString(R.string.stb_application_version_update), null, null, null, null);
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
            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR: // 中継アプリの内部エラー
                showCommonControlErrorDialog(getResources().getString(R.string.main_setting_connect_error_message), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_SET_DEFAULT_USER_ACCOUNT_REJECTED: // 要求受付失敗
                showCommonControlErrorDialog(getResources().getString(R.string.common_request_acceptance_failure_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE: // サービス未登録、又は、署名による呼び出し元不正
                showCommonControlErrorDialog(getResources().getString(R.string.common_service_unregistered_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID: // 指定ユーザIDなし
                // チェック処理の状態で処理を分岐する
                SharedPreferencesUtils.resetSharedPreferencesStbInfo(getApplicationContext());
                // TODO アプリのキャッシュをきれいにクリアする処理が必要
                showCommonControlErrorDialog(getString(R.string.main_setting_stb_application_launch_fail_id_notexist), CustomDialog.ShowDialogType.D_ACCOUNT_REGISTRATION_HELP,
                        null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_ID_CHANGED: // 要求時とは別docomoIDで指定ユーザに切り替え成功 ※本来は正常終了だが異常終了とする
                showCommonControlErrorDialog(getResources().getString(R.string.common_different_user_switch_successfully_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_TIMEOUT: // ユーザタイムアウト
                showCommonControlErrorDialog(getResources().getString(R.string.common_user_time_out_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_CANCEL: // ユーザ中断
                showCommonControlErrorDialog(getResources().getString(R.string.common_user_cancel_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_INVALID_STATE: // ユーザ状態異常
                showCommonControlErrorDialog(getResources().getString(R.string.common_user_state_abnormality_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_SERVER_ERROR: // ユーザアカウント切り替えのサーバエラー
                showCommonControlErrorDialog(getResources().getString(R.string.common_switch_user_account_server_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_NETWORK_ERROR: // ユーザアカウント切り替えのネットワークエラー
                showCommonControlErrorDialog(getResources().getString(R.string.common_switch_user_account_network_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_INTERNAL_ERROR: // ユーザアカウント切り替えの内部エラー
                showCommonControlErrorDialog(getResources().getString(R.string.common_switch_user_account_internal_error), null, null, null, null);
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
            case RelayServiceResponseMessage.RELAY_RESULT_INTERNAL_ERROR: // 中継アプリの内部エラー
                showCommonControlErrorDialog(getResources().getString(R.string.main_setting_connect_error_message), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_SET_DEFAULT_USER_ACCOUNT_REJECTED: // 要求受付失敗
                showCommonControlErrorDialog(getResources().getString(R.string.common_request_acceptance_failure_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_NOT_REGISTERED_SERVICE: // サービス未登録、又は、署名による呼び出し元不正
                showCommonControlErrorDialog(getResources().getString(R.string.common_service_unregistered_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_UNREGISTERED_USER_ID: // 指定ユーザIDなし
                showCommonControlErrorDialog(getString(R.string.main_setting_stb_application_launch_fail_id_notexist), CustomDialog.ShowDialogType.D_ACCOUNT_REGISTRATION_HELP,
                        null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_ID_CHANGED: // 要求時とは別docomoIDで指定ユーザに切り替え成功 ※本来は正常終了だが異常終了とする
                showCommonControlErrorDialog(getResources().getString(R.string.common_different_user_switch_successfully_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_TIMEOUT: // ユーザタイムアウト
                showCommonControlErrorDialog(getResources().getString(R.string.common_user_time_out_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_CANCEL: // ユーザ中断
                showCommonControlErrorDialog(getResources().getString(R.string.common_user_cancel_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USER_INVALID_STATE: // ユーザ状態異常
                showCommonControlErrorDialog(getResources().getString(R.string.common_user_state_abnormality_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_SERVER_ERROR: // ユーザアカウント切り替えのサーバエラー
                showCommonControlErrorDialog(getResources().getString(R.string.common_switch_user_account_server_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_NETWORK_ERROR: // ユーザアカウント切り替えのネットワークエラー
                showCommonControlErrorDialog(getResources().getString(R.string.common_switch_user_account_network_error), null, null, null, null);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_USERACCOUNT_INTERNAL_ERROR: // ユーザアカウント切り替えの内部エラー
                showCommonControlErrorDialog(getResources().getString(R.string.common_switch_user_account_internal_error), null, null, null, null);
                break;
            default:
                showCommonControlErrorDialog(getResources().getString(R.string.main_setting_stb_application_launch_fail), null, null, null, null);
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
                        message = getResources().getString(R.string.common_start_dtv_application_uninstall_error);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case DANIMESTORE:
                        message = getResources().getString(R.string.common_start_d_animation_application_uninstall_error);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case DTVCHANNEL:
                        message = getResources().getString(R.string.common_start_dtv_channel__application_uninstall_error);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case HIKARITV:
                        message = getResources().getString(R.string.common_start_hikari_application_uninstall_error);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case DAZN:
                        message = getResources().getString(R.string.common_start_dazn_application_uninstall_error);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    //ENUMをSwitchで使用する場合、未使用項目も記載しなければアナライザーがエラー扱いにしてしまうのでUNKNOWNを追加した、
                    case UNKNOWN:
                    default:
                        message = getResources().getString(R.string.main_setting_stb_application_launch_fail);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                }
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_VERSION_CODE_INCOMPATIBLE:
                switch (appId) {
                    case DTV:
                        message = getResources().getString(R.string.main_setting_dtv_update_message);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case DANIMESTORE:
                        message = getResources().getString(R.string.main_setting_d_anime_store_update_message);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case DTVCHANNEL:
                        message = getResources().getString(R.string.main_setting_dtv_channel_update_message);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case HIKARITV:
                        message = getResources().getString(R.string.main_setting_hikari_tv_update_message);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    case DAZN:
                        message = getResources().getString(R.string.main_setting_dazn_update_message);
                        showCommonControlErrorDialog(message, null, null, null, null);
                        break;
                    //ENUMをSwitchで使用する場合、未使用項目も記載しなければアナライザーがエラー扱いにしてしまうのでUNKNOWNを追加した、
                    case UNKNOWN:
                    default:
                        break;
                }
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_DISTINATION_UNREACHABLE: // STBに接続できない場合
                //dTV アプリが STBの中継アプリに接続できない場合
                setStbStatus(false);
                break;
            case RelayServiceResponseMessage.RELAY_RESULT_APPLICATION_ID_NOTEXIST:
            case RelayServiceResponseMessage.RELAY_RESULT_APPLICATION_START_FAILED:
            default:
                switchAccountExistCode(resultCode);
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
     * 機能 エラー制御ダイアログ.
     *
     * @param errorMessage      errorMessage
     * @param showDialogType    showDialogType
     * @param apiOKCallback     apiOKCallback
     * @param apiCancelCallback apiCancelCallback
     * @param dismissCallback   dismissCallback　バックキーによるダイアログクローズ
     */
    protected synchronized void showCommonControlErrorDialog(final String errorMessage, final CustomDialog.ShowDialogType showDialogType,
                                                             final CustomDialog.ApiOKCallback apiOKCallback,
                                                             final CustomDialog.ApiCancelCallback apiCancelCallback,
                                                             final CustomDialog.DismissCallback dismissCallback) {
        DTVTLogger.debug("showCommonControlErrorDialog:" + errorMessage);
        if (!isNeedToInsertQue(showDialogType)) {
            return;
        }
        CustomDialog errorDialog = setDialogParameter(showDialogType, apiOKCallback, apiCancelCallback, dismissCallback, null);
        errorDialog.setContent(errorMessage);
        //ダイアログをキューにためる処理
        offerDialog(errorDialog);
    }

    /**
     * 機能 ダイアログ制御チェック.
     * @param showDialogType ダイアログタイプ
     */
    private boolean isNeedToInsertQue(final CustomDialog.ShowDialogType showDialogType) {
        if (mShowDialog != null && mShowDialog.isShowing()) {
            //設定ファイルエラー/バージョンアップダイアログ以外の場合は蓄積せずに帰る
            if (showDialogType != CustomDialog.ShowDialogType.FORCED_VERSION_UP
                    && showDialogType != CustomDialog.ShowDialogType.OPTIONAL_VERSION_UP) {
                return false;
            }
            //VersionUpダイアログの場合、もし表示中であれば蓄積しない
            if (mShowDialog.getErrorDialogType() == showDialogType) {
                return false;
            }
        }
        return true;
    }

    /**
     * 機能 エラー制御ダイアログ.
     * @param showDialogType showDialogType
     * @param apiOKCallback apiOKCallback
     * @param apiCancelCallback apiCancelCallback
     * @param dismissCallback dismissCallback
     * @param apiListItemClickCallback apiListItemClickCallback
     */
    private CustomDialog setDialogParameter(final CustomDialog.ShowDialogType showDialogType, final CustomDialog.ApiOKCallback apiOKCallback,
                                            final CustomDialog.ApiCancelCallback apiCancelCallback, final CustomDialog.DismissCallback dismissCallback,
                                            final CustomDialog.ApiListItemClickCallback apiListItemClickCallback) {
        CustomDialog customDialog;
        if (showDialogType != null) {
            switch (showDialogType) {
                case DTV_EPISODE_LIST_ITEM_DIALOG:
                    customDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.LIST);
                    break;
                case OPTIONAL_VERSION_UP:
                case LAUNCH_STB_START_DIALOG:
                case REMOTE_CONTROL_START_PAIRING_DIALOG:
                    customDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.CONFIRM);
                    break;
                default:
                    customDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
                    break;
            }
        } else {
            customDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        }
        if (showDialogType == CustomDialog.ShowDialogType.LAUNCH_STB_START_DIALOG || showDialogType == CustomDialog.ShowDialogType.REMOTE_CONTROL_START_PAIRING_DIALOG) {
            customDialog.setConfirmText(R.string.contents_detail_pairing_button);
            customDialog.setCancelText(R.string.common_text_close);
        }
        //閉じたときに次のダイアログを呼ぶ処理
        customDialog.setOkCallBack((apiOKCallback == null) ? this : apiOKCallback);
        customDialog.setApiCancelCallback((apiCancelCallback == null) ? this : apiCancelCallback);
        customDialog.setDialogDismissCallback((dismissCallback == null) ? this : dismissCallback);
        customDialog.setApiListItemClickCallback((apiListItemClickCallback == null) ? this : apiListItemClickCallback);
        //ボタン以外タップ不可
        customDialog.setOnTouchOutside(false);
        customDialog.setErrorDialogType((showDialogType == null) ? CustomDialog.ShowDialogType.COMMON_DIALOG : showDialogType);
        return customDialog;
    }

    /**
     * 機能 ダイアログ制御チェック.
     * @param title タイトル
     * @param itemList リスト
     * @param showDialogType ダイアログタイプ
     * @param dismissCallback dismissCallback
     * @param apiListItemClickCallback apiListItemClickCallback
     */
    protected synchronized void showCommonControlListDialog(final String title, final String[] itemList, final CustomDialog.ShowDialogType showDialogType,
                                                            final CustomDialog.DismissCallback dismissCallback,
                                                            final CustomDialog.ApiListItemClickCallback apiListItemClickCallback) {
        DTVTLogger.debug("showCommonControlListDialog:" + title);
        CustomDialog customDialog = setDialogParameter(showDialogType, null, null, dismissCallback, apiListItemClickCallback);
        customDialog.setTitle(title);
        customDialog.setListSelectItems(itemList);
        //ダイアログをキューにためる処理
        offerDialog(customDialog);
    }

    /**
     * dアカ変更前の再起動ダイアログ表示前にテーブルの再構築を実行する.
     */
    private void reBuildAllTable() {
        DTVTLogger.start();
        RebuildDatabaseTableManager tableManager = new RebuildDatabaseTableManager(this);
        tableManager.allTableRebuild();
        restartMessageDialog();
        DTVTLogger.end();
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
        String printMessage = getString(R.string.d_account_change_message);

        //引数がある場合はその先頭を使用する
        if (message != null && message.length > 0) {
            DTVTLogger.debug("restartMessageDialog:" + message[0]);
            printMessage = message[0];
        }

        //ダイアログを、OKボタンのコールバックありに設定する
        CustomDialog restartDialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        //枠外を押した時の操作を無視するように設定する
        restartDialog.setOnTouchOutside(false);
        restartDialog.setContent(printMessage);
        //startAppDialog.setTitle(getString(R.string.dTV_content_service_start_dialog));
        showCommonControlErrorDialog(printMessage, CustomDialog.ShowDialogType.D_ACCOUNT_CHANGED, null,
                null, null);
    }

    /**
     * onRestartApplication.
     */
    private void onRestartApplication() {
        SharedPreferencesUtils.setSharedPreferencesRestartFlag(getApplicationContext(), false);

        // dアカウントが変更しましたため、新しいお知らせの未読状態に関するFlagをリセットする
        SharedPreferencesUtils.removeNewlyNoticeFlag(mContext);

        //OKが押されたので、ホーム画面の表示
        reStartApplication();
    }

    /**
     * onVersionUpDialogShow.
     * @param which which
     * @param dialogTapType dialogTapType
     */
    private void onVersionUpDialogShow(final int which, final CustomDialog.DialogTapType dialogTapType) {
        final CustomDialog.ShowDialogType showDialogType = mShowDialog.getErrorDialogType();
        if (getLinkListSize() > 0) {
            pollDialog();
            if (mShowDialog != null) {
                setVersionUpDialogCallBack(which, mShowDialog, showDialogType, dialogTapType);
            }
        } else {
            switch (showDialogType) {
                case D_ACCOUNT_CHANGED:
                    onRestartApplication();
                    break;
                case D_ACCOUNT_REGISTRATION_HELP:
                    Intent intent = new Intent(getApplicationContext(), DaccountSettingHelpActivity.class);
                    startActivity(intent);
                    break;
                case SECURE_PLAYER_ERROR:
                    contentsDetailCloseKey(null);
                    break;
                case CONTENT_DETAIL_GET_ERROR:
                    finish();
                    break;
                case LAUNCH_STB_START_DIALOG:
                    if (CustomDialog.DialogTapType.OK == dialogTapType) {
                        intent = new Intent(getApplicationContext(), LaunchStbActivity.class);
                        intent.putExtra(ContentUtils.LAUNCH_STB_FROM, ContentUtils.LAUNCH_STB_CONTENT_DETAIL);
                        startActivity(intent);
                    }
                    break;
                case REMOTE_CONTROL_START_PAIRING_DIALOG:
                    if (mRemoteControllerView != null && mRemoteControllerView.isTopRemoteControllerUI()) {
                        mRemoteControllerView.closeRemoteControllerUI();
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (CustomDialog.DialogTapType.OK == dialogTapType) {
                                Intent intent = new Intent(getApplicationContext(), LaunchStbActivity.class);
                                intent.putExtra(ContentUtils.LAUNCH_STB_FROM, ContentUtils.LAUNCH_STB_CONTENT_DETAIL);
                                startActivity(intent);
                            }
                        }
                    }, DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
                    break;
                case DTV_EPISODE_LIST_ITEM_DIALOG:
                    startListDialogDismissTask(which);
                    break;
                case COMMON_DIALOG:
                default:
                    startDialogDismissTask(new Pair<>(dialogTapType, CustomDialog.DialogTapType.NOT_TAP));
                    break;
            }
        }
    }

    /**
     * versionUpダイアログ表示.
     * @param which which
     * @param showDialog showDialog
     * @param showDialogType showDialogType
     * @param dialogTapType 　最初のダイアログタップイベント
     */
    private void setVersionUpDialogCallBack(final int which, final CustomDialog showDialog,
                                            final CustomDialog.ShowDialogType showDialogType,
                                            final CustomDialog.DialogTapType dialogTapType) {
        showDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                toGooglePlay(UrlConstants.WebUrl.DTVT_GOOGLEPLAY_DOWNLOAD_URL);
            }
        });
        showDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                switch (showDialogType) {
                    case D_ACCOUNT_CHANGED:
                        onRestartApplication();
                        break;
                    case D_ACCOUNT_REGISTRATION_HELP:
                        Intent intent = new Intent(getApplicationContext(), DaccountSettingHelpActivity.class);
                        startActivity(intent);
                        break;
                    case CONTENT_DETAIL_GET_ERROR:
                        finish();
                        break;
                    case SECURE_PLAYER_ERROR:
                        contentsDetailCloseKey(null);
                        break;
                    case LAUNCH_STB_START_DIALOG:
                        if (CustomDialog.DialogTapType.OK == dialogTapType) {
                            intent = new Intent(getApplicationContext(), LaunchStbActivity.class);
                            intent.putExtra(ContentUtils.LAUNCH_STB_FROM, ContentUtils.LAUNCH_STB_CONTENT_DETAIL);
                            startActivity(intent);
                        }
                        break;
                    case REMOTE_CONTROL_START_PAIRING_DIALOG:
                        if (mRemoteControllerView != null && mRemoteControllerView.isTopRemoteControllerUI()) {
                            mRemoteControllerView.closeRemoteControllerUI();
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (CustomDialog.DialogTapType.OK == dialogTapType) {
                                    Intent intent = new Intent(getApplicationContext(), LaunchStbActivity.class);
                                    intent.putExtra(ContentUtils.LAUNCH_STB_FROM, ContentUtils.LAUNCH_STB_CONTENT_DETAIL);
                                    startActivity(intent);
                                }
                            }
                        }, DtvtConstants.REMOTE_CONTROLLER_ANIMATION_TIME);
                        break;
                    case DTV_EPISODE_LIST_ITEM_DIALOG:
                        startListDialogDismissTask(which);
                        break;
                    case COMMON_DIALOG:
                    default:
                        startDialogDismissTask(new Pair<>(dialogTapType, CustomDialog.DialogTapType.CANCEL));
                        break;
                }
            }
        });
        showDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                clearDialogContentText();
            }

            @Override
            public void otherDismissCallback() {
                if (mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.FORCED_VERSION_UP) {
                    stopAllActivity();
                    return;
                }
                switch (showDialogType) {
                    case D_ACCOUNT_CHANGED:
                        onRestartApplication();
                        break;
                    case D_ACCOUNT_REGISTRATION_HELP:
                        Intent intent = new Intent(getApplicationContext(), DaccountSettingHelpActivity.class);
                        startActivity(intent);
                        break;
                    case CONTENT_DETAIL_GET_ERROR:
                        finish();
                        break;
                    case SECURE_PLAYER_ERROR:
                        contentsDetailCloseKey(null);
                        break;
                    case LAUNCH_STB_START_DIALOG:
                        if (CustomDialog.DialogTapType.OK == dialogTapType) {
                            intent = new Intent(getApplicationContext(), LaunchStbActivity.class);
                            intent.putExtra(ContentUtils.LAUNCH_STB_FROM, ContentUtils.LAUNCH_STB_CONTENT_DETAIL);
                            startActivity(intent);
                        }
                        break;
                    case REMOTE_CONTROL_START_PAIRING_DIALOG:
                        mRemoteControllerView.closeRemoteControllerUI();
                        break;
                    case DTV_EPISODE_LIST_ITEM_DIALOG:
                        startListDialogDismissTask(which);
                        break;
                    case COMMON_DIALOG:
                    default:
                        startDialogDismissTask(new Pair<>(dialogTapType, CustomDialog.DialogTapType.BACK_KEY));
                        break;
                }
            }
        });
    }

    /**
     * Activity内に独自の処理があればオーバライドする.
     * @param which which
     * errorDialogTapPair.first 最初のダイアログタップイベント
     * errorDialogTapPair.second 最後のダイアログタップイベント
     */
    protected void startListDialogDismissTask(final int which) {
        //NOP
    }

    /**
     * Activity内に独自の処理があればオーバライドする.
     * @param errorDialogTapPair errorDialogTapPair
     * errorDialogTapPair.first 最初のダイアログタップイベント
     * errorDialogTapPair.second 最後のダイアログタップイベント
     */
    protected void startDialogDismissTask(final Pair<CustomDialog.DialogTapType, CustomDialog.DialogTapType> errorDialogTapPair) {
        //NOP
    }

    /**
     * エラー表示ダイアログ・OKを押した後の処理指定付き.
     *
     * @param errorMessage 表示するエラーメッセージ
     * @param okCallback OKボタンを押した時の処理
     */
    protected void showErrorDialogOfferAfterProcess(final String errorMessage
            , final CustomDialog.ApiOKCallback okCallback) {
        DTVTLogger.debug("showErrorDialogOfferAfterProcess:" + errorMessage);
        CustomDialog errorDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        errorDialog.setContent(errorMessage);

        //OKを押した時の動作を指定
        errorDialog.setOkCallBack(okCallback);

        //エラーダイアログ枠外タップ不可
        errorDialog.setOnTouchOutside(false);

        //ダイアログをキューにためる処理
        offerDialog(errorDialog);
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
        if (mDisplayMetrics == null) {
            mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        }
        return mDisplayMetrics;
    }

    /**
     * 機能
     * ディスプレイインスタンスを初期化.
     */
    protected void initDisplayMetrics() {
        mDisplayMetrics = null;
    }

    /**
     * 機能
     * カレントユーザ名を戻す.
     *
     * @return カレントユーザ名
     */
    public String getUserName() {
        //TODO :2nd開発では未対応.仮の値を返却
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
            menu.setActivityAndListener(this, this, mDrawerLayout, mGlobalMenuListView);
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
        if (this instanceof SearchTopActivity
                || this instanceof TvProgramListActivity
                || this instanceof RecordedListActivity
                || this instanceof RankingTopActivity
                || this instanceof VideoTopActivity
                || this instanceof RecordReservationListActivity
                || this instanceof SettingActivity) {
            startHomeActivity();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        if (checkIsNotNeedFinish()) {
            return;
        }
        super.finish();
    }

    /**
     * バックキーとコンテンツ同時にタップ防止.
     *
     * @return true:バックキーとコンテンツ同時に押下
     */
    private boolean checkIsNotNeedFinish() {
        if (mClassName != null) {
            try {
                Class mClass = Class.forName(mClassName);
                //詳細画面へ遷移する場合は遷移元画面に戻れるため、finish制御
                if (mClass == ContentDetailActivity.class) {
                    mClassName = null;
                    return true;
                }
            } catch (ClassNotFoundException e) {
                DTVTLogger.debug(e);
            }
        }
        return false;
    }

    /**
     * webViewGoBackEvent.
     * @param webView webView
     */
    protected void webViewGoBackEvent(final WebView webView) {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            if (this instanceof NoticeActivity) {
                startHomeActivity();
            } else {
                finish();
            }
        }
    }

    /**
     * ホームActivity起動.
     */
    protected void startHomeActivity() {
        Intent intent = mActivity.getIntent();
        intent.setClass(mActivity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    /**
     * リモコン表示時の鍵交換処理フラグの設定.
     * ※コンテンツ詳細画面からのサービスアプリ起動要求時のリモコン画面表示時（false）
     */
    protected void setKeyExchangeFlag(final boolean isKeyExchangeRequired) {
        DTVTLogger.debug(String.format("isKeyExchangeRequired:%s", isKeyExchangeRequired));
        mKeyExchangeFlag = isKeyExchangeRequired;
    }

    @Override
    protected void onPause() {

        super.onPause();

        //ワンタイムトークン取得のキャンセル
        cancelOttConnection();
        DaccountReceiver.setDaccountChangedCallBack(null);
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
     * @param episodeId episodeId
     * @param isFromEpisode isFromEpisode
     */
    protected void requestStartApplication(final RemoteControlRelayClient.STB_APPLICATION_TYPES type, final String contentsId, final String episodeId, final boolean isFromEpisode) {
        mRemoteControllerView.sendStartApplicationRequest(type, contentsId, episodeId, isFromEpisode);
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
     * @param crid      crid
     */
    protected void requestStartApplicationHikariTvCategoryDtvVod(final String episodeId, final String crid) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvVodRequest(episodeId, crid);
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
     * ひかりTV内dTVチャンネルのVOD（見逃し／関連番組）
     *
     * @param crid コンテンツID
     */
    protected void requestStartApplicationHikariTvCategoryDtvchannelMissed(final String crid) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryDtvchannelMissedRequest(crid);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTVの番組 (地デジ)
     *
     * @param serviceId サービスId
     */
    protected void requestStartApplicationHikariTvCategoryTerrestrialDigital(final String serviceId) {
        mRemoteControllerView.sendStartApplicationHikariTvCategoryTerrestrialDigitalRequest(serviceId);
    }

    /**
     * STBのサービスアプリ起動リクエスト処理を実行.
     * ひかりTVの番組 （BS）
     *
     * @param serviceId サービスId
     */
    protected void requestStartApplicationHikariTvCategorySatelliteBs(final String serviceId) {
        mRemoteControllerView.sendStartApplicationHikariTvCategorySatelliteBsRequest(serviceId);
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
                        if (findViewById(R.id.base_progress_rl).getVisibility() == View.VISIBLE) {
                            DTVTLogger.debug("Return RemoteControl");
                            return;
                        }
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
                            // 鍵交換処理が必要
                            setKeyExchangeFlag(true);
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
        //TODO 2nd開発では横画面未対応のため縦画面の処理のみ実装
        if (visibility == View.VISIBLE && this instanceof ContentDetailActivity) {
            //リモコン表示時にScrollViewにマージンを設ける
            Resources resources = getResources();
            int remoteButtonMargin = resources.getDimensionPixelSize(R.dimen.remote_control_display_button_height);
            mBaseLinearLayout.setPadding(0, 0, 0, remoteButtonMargin);
            mBaseLinearLayout.setBackgroundResource(R.color.contents_detail_channel_line_color);
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

        if (data != null && clipButton != null && !isClipRunTime()) {
            DTVTLogger.debug(String.format("clip request: crid [%s] clip status [%s]", data.getCrid(), data.isClipStatus()));

            //クリップ多重実行に対応していないため実行中フラグで管理
            mClipRunTime = true;

            mClipButton = clipButton;
            mClipRequestData = data;

            boolean isParamCheck;

            //DREM-1882 期限切れコンテンツのクリップ対応により、期限切れコンテンツをクリップ登録しようとした場合にはエラーダイアログを表示する
            if (!data.isClipStatus() && data.isIsAfterLimitContents()) {
                String message = getString(R.string.str_clip_execution_after_limit_contents);
                showCommonControlErrorDialog(message, null, null, null, null);
                mClipRunTime = false;
                return;
            }

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

            if (!isParamCheck) {
                //パラメータチェックではじかれたら失敗表示とクリップ登録／解除実行中状態を解除
                showClipToast(R.string.clip_regist_error_message);
            } else {
                //クリップ登録／解除リクエストの結果応答時にコンテンツリストに反映するにクリップ登録／削除ステータスを設定.
                data.setClipStatus(!data.isClipStatus());
            }
        }
    }

    /**
     * 各一覧画面のクリップ登録／解除リクエストの結果応答時にコンテンツリストに登録／削除ステータスを反映する.
     * @param contentsList 各一覧画面のコンテンツリスト
     */
    protected void setContentsListClipStatus(List<ContentsData> contentsList) {
        ClipRequestData clipRequestData = getClipRequestData(); //クリップ登録/削除リクエスト時のクリップ情報
        if (null != contentsList && null != clipRequestData) {
            for (ContentsData info : contentsList) {
                if (clipRequestData.equals(info.getRequestData())) {
                    //コンテンツリストに登録／削除ステータスを反映する.
                    info.setClipStatus(clipRequestData.isClipStatus());
                    DTVTLogger.debug(String.format("set clip result: crid [%s] clip status [%s]", info.getCrid(), info.isClipStatus()));
                    break;
                }
            }
        }
    }

    /**
     * クリップ処理でのトースト表示.
     *
     * @param msgId 各ステータスのメッセージID
     */
    public void showClipToast(final int msgId) {
        //指定された文字リソースを表示する
        if (mToast != null) {
            mToast.cancel();
        }
        if (!this.isFinishing()) {
            mToast = Toast.makeText(this, msgId, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = null;
        }

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

        //dアカウント関連処理中
        if (mDAccountControl != null && !mDAccountControl.getIsOTTCheckFinished()) {
            return;
        }

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

    @Override
    public void daccountControlCallBack(final boolean result) {
        DTVTLogger.start();

        //エラー表示を行うかどうかのスイッチ
        boolean firstDaccountError = false;

        //初回dアカウント取得かどうかの判定
//        if (SharedPreferencesUtils.isFirstDaccountGetProcess(getApplicationContext()) && !result) {
//            //初回のdアカウント取得かつ問題が発生していた場合はtrueにする
//            firstDaccountError = true;
//        } else {
//            //初回以外はダイアログを出さないので、処理中フラグはリセット
//            mDAccountControl.setDAccountBusy(false);
//        }

        //初回dアカウント取得が行われていた場合は終わらせる
//        SharedPreferencesUtils.setFirstExecEnd(getApplicationContext());

        onDaccountOttGetComplete(result);
        //dアカウントの登録結果を受け取るコールバック
        if (result) {
            //処理に成功したので、帰る
            DTVTLogger.end("normal end");
            return;
        }

        if (mDAccountControl != null
                && mDAccountControl.getResult() == IDimDefines.RESULT_USER_CANCEL) {
            //dアカウント認証画面がキャンセルされたので、ログアウトダイアログを出すために、フラグを立てる
            OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.getInstance();
            ottGetAuthSwitch.setSkipPermission(true);

            ottGetAuthSwitch.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLogoutDialog();
                }
            });
        }

        //dアカウント処理クラスがヌルか、初回起動時にdアカウント取得で問題が発生したかどうかを確認
//        if (mDAccountControl == null) {
        //処理には失敗したが、動作の続行ができないので、ここで終わらせる。ただ、このコールバックを受けている以上、ヌルになることありえないはず
        //今はUIスレッドではないので、ダイアログの処理を移譲する
        mFirstDaccountErrorHandler.post(new Runnable() {
            @Override
            public void run() {
                //エラーダイアログを表示する.基本的に再認証オプション付きでOTT取得しており、別途ログアウトダイアログを表示するため
                //認証無効に関しては特にここではダイアログ表示はしない. 必要のある画面は showDAccountErrorDialog をオーバライドする事.
                showDAccountErrorDialog();

                //ダイアログを出す場合はここで処理中フラグをリセット
                mDAccountControl.setDAccountBusy(false);
            }
        });

//            DTVTLogger.end("null end or first daccount get error");
//            return;
//        }

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

    /**
     * dアカウントダイアログ表示を行う.
     *
     * (オーバーライドの為にprotected指定で分離)
     */
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    protected void showDAccountErrorDialog() {

        CustomDialog errorDialog = new CustomDialog(
                BaseActivity.this, CustomDialog.DialogType.ERROR);
        int errorCode = 0;

        //初期値を入れている
        DaccountControl.CheckLastClassEnum checkLastClassEnum =
                DaccountControl.CheckLastClassEnum.CHECK_SERVICE;
        //失敗原因コードを取得
        if (mDAccountControl != null) {
            errorCode = mDAccountControl.getResult();
            checkLastClassEnum = mDAccountControl.getmResultClass();
        }
        boolean isNeedDialog = true;
        if (DaccountControl.CheckLastClassEnum.REGIST_SERVICE.equals(checkLastClassEnum)
                || DaccountControl.CheckLastClassEnum.CHECK_SERVICE.equals(checkLastClassEnum)
                || DaccountControl.CheckLastClassEnum.ONE_TIME_PASS_WORD.equals(checkLastClassEnum)
                || DaccountControl.CheckLastClassEnum.CONTROL.equals(checkLastClassEnum)) {
            DTVTLogger.debug("showDAccountErrorDialog errCode:" + errorCode);
            switch (errorCode) {
                case IDimDefines.RESULT_USER_INVALID_STATE:
                    errorDialog.setContent(getString(R.string.d_account_user_abnormality_error));
                    break;
                case IDimDefines.RESULT_NETWORK_ERROR:
                    //ｄアカアプリ側でダイアログが表示されるため、ドコテレとしては通常の画面では出さない.
                    //（STB選択画面のSTB選択時のOTT取得では表示する）
                    isNeedDialog = false;
//                    errorDialog.setContent(getString(R.string.d_account_network_error));
                    break;
                case IDimDefines.RESULT_USER_TIMEOUT:
                    errorDialog.setContent(getString(R.string.d_account_user_timeout_error));
                    break;
                case IDimDefines.RESULT_USER_CANCEL:
                    // OTT取得を再認証オプションで呼び出しており
                    // 認証キャンセルはログアウトダイアログを別途表示するのでここではダイアログ表示しない.
                    // ダイアログが必要な特別な画面は、別途オーバライドする事.
                    isNeedDialog = false;
//                    errorDialog.setContent(getString(R.string.d_account_user_interruption_error));
                    break;
                case IDimDefines.RESULT_SERVER_ERROR:
                    errorDialog.setContent(getString(R.string.d_account_server_error));
                    break;
                case IDimDefines.RESULT_INVALID_ID:
                    // OTT取得を再認証オプションで呼び出しているため基本的にID無効でエラーダイアログは出さない.
                    isNeedDialog = false;
//                    errorDialog.setContent(getString(R.string.d_account_authentication_invalid_error));
                    break;
                case IDimDefines.RESULT_INTERNAL_ERROR:
                    errorDialog.setContent(getString(R.string.d_account_internal_error));
                    break;
                case IDimDefines.RESULT_NOT_REGISTERED_SERVICE:
                    errorDialog.setContent(getString(R.string.d_account_service_unregistered_error));
                    break;
                case IDimDefines.RESULT_REMOTE_EXCEPTION:
                    errorDialog.setContent(getString((R.string.d_account_remote_exception_error)));
                    break;
                case IDimDefines.RESULT_NO_AVAILABLE_ID:
                    //有効IDなし. dアカアプリを起動してID設定せずに戻った場合に返却される.
                    //毎回警告を出すことになるので出さない.
                    isNeedDialog = false;
                    break;
                case IDimDefines.RESULT_COMPLETE:
                    //正常終了
                    isNeedDialog = false;
                    break;
                case DaccountUtils.D_ACCOUNT_APP_NOT_FOUND_ERROR_CODE:
                    //事前チェックでdアカウント設定アプリが未インストールである事が分かった場合のエラー
                    //errorDialog.setContent(getString(R.string.d_account_regist_error));
                    //dアカウントアプリ未インストールの場合に出すメッセージは、ログイン中であった場合の
                    //dアカウント変更後のホーム画面復帰だけと判明した。ホーム画面復帰は別途処理が行われるので、
                    //ここではメッセージは表示しない事になった。
                    isNeedDialog = false;
                    break;
                case IDimDefines.RESULT_SERVICE_AVAILABLE:
                    //エラーダイアログ表示でこのコードが来るのは、dアカウントアプリ未インストールかつ
                    //未ログイン又はログオフ済みの場合となる。これまではdefault:で処理されていたが、
                    //この場合もメッセージは表示しない事になった。
                    isNeedDialog = false;
                    break;
                default:
                    errorDialog.setContent(getString(R.string.d_account_regist_error));
                    break;
            }
        }

        if (isNeedDialog) {
            //次のダイアログを呼ぶ為の処理
            errorDialog.setDialogDismissCallback(BaseActivity.this);
            errorDialog.setOnTouchOutside(false);

            //showDialogの代わり・重複ダイアログ実現用
            offerDialog(errorDialog);
        }
    }

    /**
     * dアカウント設定アプリ未インストールエラーダイアログ.
     */
    public void showDAccountApliNotFoundDialog() {
        DTVTLogger.debug("showDAccountApliNotFoundDialog");
        final CustomDialog notFoundDialog = new CustomDialog(
                BaseActivity.this, CustomDialog.DialogType.ERROR);
        notFoundDialog.setContent(this.getResources().getString(
                R.string.d_account_deleted_message));

        //バックボタンをキャンセルボタンとして扱うように指示
        notFoundDialog.setBackKeyAsCancel(true);

        //ダイアログ以外の部分を押した時に反応するのは禁止
        notFoundDialog.setOnTouchOutside(false);

        //メッセージを出さないキャッシュクリア
        DaccountControl.cacheClear(BaseActivity.this, null, false);

        //OKボタンが押されたら、ログアウト処理を行い、終了する
        notFoundDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                DTVTLogger.debug("logout & restart");
                SharedPreferencesUtils.setSharedPreferencesRestartFlag(getApplicationContext(),
                        false);
                reStartApplication();
            }
        });

        offerDialog(notFoundDialog);
    }

    /**
     * ログアウトダイアログ.
     *
     * 各アクティビティで使われるようになったので、publicに変更
     */
    public void showLogoutDialog() {
        //特別なコールバックは使用しないのでヌル
        showLogoutDialog(null);
    }

    /**
     * ログアウトダイアログ.
     *
     * @param dissmissCallBack キャンセル後の処理
     */
    public void showLogoutDialog(CustomDialog.DismissCallback dissmissCallBack) {

        DTVTLogger.debug("showLogoutDialog");
        final CustomDialog logoutDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.CONFIRM);
        logoutDialog.setContent(this.getResources().getString(R.string.logout_message));

        //バックボタンをキャンセルボタンとして扱うように指示
        logoutDialog.setBackKeyAsCancel(true);

        //ダイアログ以外の部分を押した時に反応するのは禁止
        logoutDialog.setOnTouchOutside(false);

        logoutDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @SuppressWarnings("OverlyLongMethod")
            @Override
            public void onOKCallback(final boolean isOK) {
                //ログアウトのダイアログは閉じられたので、認証画面を再表示できるようにする
                OttGetAuthSwitch.INSTANCE.setNowAuth(true);

                DaccountControl.cacheClear(BaseActivity.this);
                reStartApplication();
            }

        });

        logoutDialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                //ログアウトのダイアログは閉じられたので、認証画面を再表示する
                OttGetAuthSwitch.INSTANCE.setNowAuth(true);
                chkDaccountRegist();
            }
        });

        //コールバック指定の有無で処理を分ける
        if (dissmissCallBack == null) {
            //コールバックの指定が無かったので、通常の処理を行う
            logoutDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
                @Override
                public void allDismissCallback() {
                    //ログアウトのダイアログは閉じられたので、認証画面を再表示できるようにする
                    OttGetAuthSwitch.INSTANCE.setNowAuth(true);

                    clearDialogContentText();

                    //キャンセルボタンが押されたかどうかのチェック
                    if (!logoutDialog.isButtonTap()) {
                        logoutDialog.setButtonTap(true);
                        //ダイアログが閉じた理由が電源ボタンやホームボタンなので、帰る
                        return;
                    } else {
                        OttGetAuthSwitch.INSTANCE.setNowAuth(true);
                        mDAccountControl = new DaccountControl();
                        mDAccountControl.registService(getApplicationContext(), BaseActivity.this);
                    }

                    pollDialog();
                }

                @Override
                public void otherDismissCallback() {
                    DTVTLogger.debug("otherDismissCallback");
                }
            });
        } else {
            //ログアウトのダイアログは閉じられたので、認証画面を再表示できるようにする
            OttGetAuthSwitch.INSTANCE.setNowAuth(true);

            //指定があったので、コールバックを設定する
            logoutDialog.setDialogDismissCallback(dissmissCallBack);
        }

        offerDialog(logoutDialog);
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

//TODO 1/19 1/5時点での実装後に仕様の再検討が発生したためコメントアウト
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
        //ヌルチェックを追加
        View view = findViewById(R.id.base_progress_rl);
        if (view != null) {
            view.setVisibility(visible);
            // コンテンツ詳細からのサービスアプリ連携時のクルクル表示中にクルクルのタップを有効にしてリモコンのボタンタップを禁止する
            view.setClickable(true);
        }
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
        intent.putExtra(ChildContentListActivity.INTENT_KEY_IS_RENTAL, contentsData.isRental());
        startActivity(intent);
    }

    /**
     * dアカウントの削除や変更後に、自アプリがフォアグラウンドならば、アプリの再起動を行う.
     */
    protected void reStartApplication() {
        DTVTLogger.start();
        //ホーム画面に戻る
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        DTVTLogger.end();
    }

    /**
     * アプリを終了させる.
     */
    public void stopAllActivity() {
        DTVTLogger.start();

        //Androidバージョンで終了処理を使い分ける
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //finishAndRemoveTaskAPIは、タスク一覧から消すが、現在のアクティビティしか終了しない。そこでdアカウント更新時と同じように、
            //タスク情報を消すオプションを付けてホーム画面に飛ぶ。これで画面がホーム一つに限定されるので、その後にfinishAndRemoveTaskを呼ぶ処理を行う
            forceFinishAndTaskRemove(getApplicationContext());
        } else {
            //4.4ではfinishAndRemoveTaskが使えないので、この場でアプリを終了させる
            finishAffinity();
        }

        DTVTLogger.end();
    }

    /**
     * タスク一覧にアプリ情報を残さずに終了する為の準備.
     *
     * @param context コンテキスト
     */
    public static void forceFinishAndTaskRemove(Context context) {
        DTVTLogger.start();

        //タスク情報を削除しつつホーム画面に飛ぶ設定・これで他のアクティビティは消えてホーム画面だけになる
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        //ホーム画面起動後にfinishAndRemoveTaskを行わせるフラグのセット
        intent.putExtra(FORCE_FINISH, true);

        context.startActivity(intent);

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
            if (intent.getComponent() != null) {
                mClassName = intent.getComponent().getClassName();
            }
            if (isFinishing() && checkIsNotNeedFinish()) {
                return;
            }
            super.startActivity(intent);

            //STB再起動パラメータの取得
            boolean stbRestart = intent.getBooleanExtra(STB_SELECT_ACTIVITY_RESTART, false);
            //STB再起動パラメータがfalseの場合、dアカウント処理を行う
            if (!stbRestart) {
                //dアカウントアプリのバインドを解除する
                final DaccountGetOtt getOtt = new DaccountGetOtt(getApplicationContext());
                if (getOtt != null) {
                    DTVTLogger.debug("startActivity before unbind");
                    //他の画面に遷移する前に、dアカウントアプリとの連携を終わらせる
                    getOtt.daccountServiceEnd();
                }
            }

            //スプラッシュ画面からフェードアウト
            if (this instanceof LaunchActivity) {
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return;
            }
            //飛び先画面として指定されていた名前を取得する
            String callName = "";
            if (intent != null && intent.getComponent() != null) {
                callName = intent.getComponent().toShortString();
            }
            //飛び先がSTB選択の関連画面ならば、アニメは付加せず帰る
            if (callName.contains(StbSelectActivity.class.getSimpleName())
                    || callName.contains(StbSelectErrorActivity.class.getSimpleName())
                    || callName.contains(StbConnectActivity.class.getSimpleName())) {
                //ただし、チュートリアル画面と設定画面から呼ばれた場合はアニメーションは行うので帰らない
                if (!(this instanceof SettingActivity) && !(this instanceof TutorialActivity) && !(this instanceof LaunchTermsOfServiceActivity)) {
                    overridePendingTransition(0, 0);
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
                    onStopAutoTransition();
                    SharedPreferencesUtils.setSharedPreferencesIsDisplayedPermissionDialogTwice(mContext);
                    offerDialog(createPermissionDetailDialog());
                } else {
                    // ・初めてパーミッション要求ダイアログが表示された場合
                    // ・パーミッション要求ダイアログが拒否された場合（「今後は確認しない」チェックあり）
                    if (!SharedPreferencesUtils.getSharedPreferencesIsDisplayedPermissionDialogTwice(mContext)) {
                        onStopAutoTransition();
                        // 「今後は確認しない」チェックなし
                        offerDialog(createPermissionDetailDialog());
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
     * リモート視聴設定期限表示.
     */
    private void localRegisterExpireDateCheck() {
        String msg = (DlnaUtils.getLocalRegisterExpireDateCheckMessage(this));
        if (!TextUtils.isEmpty(msg)) {
            showCommonControlErrorDialog(msg, null, null, null, null);
        }
    }

    /**
     * ダウンロード状態チェック.
     */
    protected void downloadStatusCheck() {
        DownloadDataProvider downloadDataProvider = DownloadDataProvider.getInstance(mActivity);
        if (downloadDataProvider.getDownloadService() == null && downloadDataProvider.deleteDownloadContentNotCompleted() > 0) {
            showCommonControlErrorDialog(getResources().getString(R.string.record_download_not_completed_msg), null, null, null, null);
        }
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
        dialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(final boolean isOK) {
                onReStartCommunication();
            }
        });
        dialog.setApiCancelCallback(new CustomDialog.ApiCancelCallback() {
            @Override
            public void onCancelCallback() {
                //アプリ終了.
                //Androidバージョンで終了処理を使い分ける
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //finishAndRemoveTaskAPIは、タスク一覧から消すが、現在のアクティビティしか終了しない。そこでdアカウント更新時と同じように、
                    //タスク情報を消すオプションを付けてホーム画面に飛ぶ。これで画面がホーム一つに限定されるので、その後にfinishAndRemoveTaskを呼ぶ処理を行う
                    BaseActivity.forceFinishAndTaskRemove(mActivity.getApplicationContext());
                } else {
                    //4.4ではfinishAndRemoveTaskが使えないので、この場でアプリを終了させる
                    mActivity.finishAffinity();
                }
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
     *
     * @return 表示するCustomDialog
     */
    private CustomDialog createPermissionDetailDialog() {
        DTVTLogger.start();
        CustomDialog dialog = new CustomDialog(this, CustomDialog.DialogType.ERROR);
        dialog.setDialogDismissCallback(this);
        dialog.setCancelable(false);
        dialog.setOnTouchOutside(false);
        dialog.setOnTouchBackkey(false);
        dialog.setConfirmText(R.string.permission_detail_dialog_confirm);
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
                onStopAutoTransition();
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
     * 自動画面遷移の停止.
     * 共通のダイアログを表示するタイミングでコールする.
     */
    protected void onStopAutoTransition() {
        //自動的に画面遷移させるような画面はオーバーライドして処理を実装する事.
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
     *  各ActivityはこのメソッドをOverrideしてResume処理を行う.
     *  ※必ずBaseActivityのメソッドを呼び出してから各ActivityのResume処理を行う
     */
    protected void onStartCommunication() {
        DTVTLogger.start();
        registerDevListDlna();
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (null == dlnaDmsItem) {
            return;
        }

        DTVTLogger.debug("RestartFlag check "
                + SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        // 再起動フラグがtrueならば、再起動メッセージを表示する
        if (SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext())) {
            reBuildAllTable();
        }

        //再起動フラグをOFFにする
        SharedPreferencesUtils.setSharedPreferencesRestartFlag(getApplicationContext(), false);

        DTVTLogger.debug("RestartFlag false "
                + SharedPreferencesUtils.getSharedPreferencesRestartFlag(getApplicationContext()));

        //ワンタイムトークンに通信可能を通知する
        setOttDisconnectionFlag(false);

        //再ローカルレジストレーション判定を呼び出す
        execAutoLocalRegistration();

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
     * 有効なdアカウントが登録されているかどうかを見る.
     */
    private void chkDaccountRegist() {
        //dアカウント制御クラスのインスタンスの取得
        final DaccountControl daccountControl = getDAccountControl();

        //制御クラスが取得できて、かつビジーではないかを調べる
        if (daccountControl != null && (!daccountControl.isDAccountBusy())) {
            //dアカウント取得クラスを未取得ならば取得する
            if (mGetOtt == null) {
                mGetOtt = new DaccountGetOtt();
            }

            //認証画面の表示状況のインスタンスの取得。認証画面の重複表示を防止用
            final OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.INSTANCE;

            //ワンタイムトークンの取得を行う事で、dアカウントのチェックとする。dアカウントが無効ならば認証画面に遷移する
            mGetOtt.execDaccountGetOTT(getApplicationContext(),
                    ottGetAuthSwitch.isNowAuth(), new DaccountGetOtt.DaccountGetOttCallBack() {
                        @Override
                        public void getOttCallBack(int result, String id, String oneTimePassword) {
                            DTVTLogger.debug("ott" + oneTimePassword);
                            //dアカウントの制御のワンタイムトークン処理を呼び出す。
                            daccountControl.setResult(result);
                            daccountControlCallBack(false);
                        }
                    });
        }
    }

    /**
     * ユーザー情報が変化したかどうかを調べる処理.
     */
    private void checkUserInfoChange() {
        DTVTLogger.start();

        //自分のインスタンスのチェック
        if (this instanceof StbSelectActivity
                || this instanceof HomeActivity
                || this instanceof LaunchActivity) {
            //STB選択とホームの各画面は専用の処理があり、スプラッシュ画面ではそもそもチェックを行わないので、ここでは帰る
            DTVTLogger.end("STB SELECT or HOME or Launch");
            return;
        }

        //ユーザー情報の変更検知
        UserInfoDataProvider dataProvider = new UserInfoDataProvider(getApplicationContext(),
                new UserInfoDataProvider.UserDataProviderCallback() {
                    @Override
                    public void userInfoListCallback(final boolean isDataChange,
                                                     final List<UserInfoList> list, final boolean isContractChange) {
                        startTvProgramIntentService();
                        //契約変化を見るコールバック
                        checkUserInfoChangeResult(isContractChange);
                        String contractType = ContentUtils.getContractType(BaseActivity.this);
                        if (!TextUtils.isEmpty(contractType)) {
                            SparseArray<String> customDimensions = new SparseArray<>();
                            customDimensions.put(ContentUtils.CUSTOMDIMENSION_CONTRACT, contractType);
                            sendEvent(getString(R.string.google_analytics_category_contract),
                                    getString(R.string.google_analytics_category_action_remote_contract_get_success),
                                    null, customDimensions);
                        }
                    }
                });
        dataProvider.getUserInfo();

        DTVTLogger.end();
    }

    /**
     * 契約情報が変化した場合は、ホーム画面に戻る処理.
     *
     * @param isContractChange ユーザー情報のデータプロバイダーが返した契約情報変更フラグ
     */
    private void checkUserInfoChangeResult(final boolean isContractChange) {
        DTVTLogger.start();
        if (isContractChange) {
            //データ変更があった場合は、ダイアログを表示して、その後にデータクリアとホームデータ更新を行う
            showErrorDialogOfferAfterProcess(getString(R.string.h4d_agreement_change),
                    new CustomDialog.ApiOKCallback() {
                        @Override
                        public void onOKCallback(final boolean isOK) {
                            //OKが押された場合は、データのクリアを行う
                            DateUtils.clearDataSave(getApplicationContext());
                            //ホーム画面の再表示を行う
                            reStartApplication();
                        }
                    });
        }
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
        mCheckSetting.setOnShowSettingDialogListener(this);

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
        // STBSelectActivity以前のActivityとホーム画面の通信不能時が対象
        mNecessaryDAccountRegistService = false;
    }

    /**
     * dアカウント処理が必要なクラスはこのメソッドを実行する.
     *
     * デフォルトでdアカウントの処理は行う事になっているので、setUnnecessaryDaccountRegistServiceで一旦不要と
     * 宣言した後で、再び必要になった場合に使用します。
     */
    protected void setNecessaryDaccountRegistService() {
        //ホーム画面の通信不能時の後で、通信が再開した場合が対象
        mNecessaryDAccountRegistService = true;
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
        if (mToast != null) {
            mToast.cancel();
        }

        if (!this.isFinishing()) {
            mToast = Toast.makeText(this, R.string.common_get_data_failed_message, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = null;
        }
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
            if (mToast != null) {
                mToast.cancel();
            }

            if (!this.isFinishing()) {
                //指定されたメッセージを表示する
                mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                mToast.show();
            } else {
                mToast = null;
            }
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
     * アプリ終了ダイアログ.
     *
     * (ベースアクティビティ以外でも使用する為、パブリックに変更)
     * @param errorMessage エラーメッセージ
     */
    public void showApFinishDialog(final String errorMessage) {
        CustomDialog apFinishDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        apFinishDialog.setContent(errorMessage);
        apFinishDialog.showDialog();
        apFinishDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
            @Override
            public void allDismissCallback() {
                stopAllActivity();
            }

            @Override
            public void otherDismissCallback() {
                //NOP
            }
        });
    }

    /**
     * 前画面遷移ダイアログ.
     *
     * (ベースアクティビティ以外でも使用する為、パブリックに変更)
     * @param errorMessage エラーメッセージ
     */
    public void showFinishDialog(final String errorMessage) {
        CustomDialog finishDialog = new CustomDialog(BaseActivity.this, CustomDialog.DialogType.ERROR);
        finishDialog.setContent(errorMessage);
        finishDialog.showDialog();
        finishDialog.setDialogDismissCallback(new CustomDialog.DismissCallback() {
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
        if (!TextUtils.isEmpty(contentText) && mShowDialog != null
                && mShowDialog.getContent() != null && mShowDialog.getContent().equals(contentText)) {
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
    protected void pollDialog() {
        DTVTLogger.start();
        DTVTLogger.debug("mLinkedList.size()=" + mLinkedList.size());
        if ((mShowDialog == null || !mShowDialog.isShowing()) && mLinkedList.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mShowDialog = mLinkedList.poll();
                    mShowDialog.showDialog();
                }
            });
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
        DTVTLogger.start();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        DTVTLogger.debug(browserIntent.getDataString());
        startActivity(browserIntent);
        DTVTLogger.end();
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
    private void setRecyclerViewData(final RecyclerView recyclerView, final List<ContentsData> contentsDataList, final int index) {
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
            if (null != baseFragment) {
                baseFragment.clearData();
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
     * ダイアログ情報のゲッター.
     *
     * @return ダイアログ情報
     */
    public CustomDialog getmShowDialog() {
        return mShowDialog;
    }

    /**
     * Dアカウントコントロール処理のゲッター.
     *
     * @return Dアカウント処理のクラスのインスタンス
     */
    public DaccountControl getDAccountControl() {
        return mDAccountControl;
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

    /**
     * クリップ操作時のクリップボタンのViewを取得する.
     * @return クリップボタンのビュー
     */
    protected ImageView getClipButton() {
        return mClipButton;
    }

    /**
     * クリップ要求時のリクエストデータを返却する.
     * @return クリップ登録/解除要求時のリクエストデータ
     */
    protected ClipRequestData getClipRequestData() {
        return mClipRequestData;
    }

    // region callback
    /**
     * 機能：onClick event for menu.
     *
     * @param view メニューアイコンのビュー
     */
    @Override
    public void onClick(final View view) {
        if (view != null && view.equals(mMenuImageViewForBase)) {
            if (HEADER_ICON_CLOSE.equals(mMenuImageViewForBase.getTag())) {
                if (this instanceof EpisodeAllReadActivity) {
                    finish();
                } else {
                    contentsDetailCloseKey(view);
                }
            } else {
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    displayGlobalMenu();
                }
            }
        } else if (view != null && view.equals(mHeaderBackIcon)) {
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
        if (isKeyExchangeRequired()) {
            setKeyExchangeFlag(false);
            CipherUtil.exchangeKey();
        }
    }

    @Override
    public void onEndRemoteControl() {
        DTVTLogger.debug("base_end_control");
        View base = findViewById(R.id.base_motion_detection_rl);
        base.setOnClickListener(null);
        base.setVisibility(View.GONE);
    }

    @Override
    public void onErrorRemoteControl(final ContentDetailUtils.RemoteControllerType remoteControllerType) {
        // NOP
        // コンテンツ詳細でのみ使用する
    }

    @Override
    public void onClipRegistResult() {
        //DB登録開始
        ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
        clipKeyListDataProvider.clipResultInsert(mClipRequestData);

        mClipButton.setImageResource(R.drawable.common_clip_active_selector);
        mClipButton.setTag(CLIP_ACTIVE_STATUS);
        showClipToast(R.string.clip_regist_result_message);
        sendEvent(getString(R.string.google_analytics_category_service_name_h4d),
                getString(R.string.google_analytics_category_action_clip_regist),
                mClipRequestData == null ? null : mClipRequestData.getTitle(), null);
    }

    @Override
    public void onClipRegistFailure() {
        showClipToast(R.string.clip_regist_error_message);
    }

    @Override
    public void onClipDeleteResult() {
        //DB削除開始
        ClipKeyListDataProvider clipKeyListDataProvider = new ClipKeyListDataProvider(this);
        clipKeyListDataProvider.clipResultDelete(mClipRequestData);

        mClipButton.setImageResource(R.drawable.common_clip_normal_selector);
        showClipToast(R.string.clip_delete_result_message);
        mClipButton.setTag(CLIP_OPACITY_STATUS);
        sendEvent(getString(R.string.google_analytics_category_service_name_h4d),
                getString(R.string.google_analytics_category_action_clip_delete),
                mClipRequestData == null ? null : mClipRequestData.getTitle(), null);
    }

    @Override
    public void onClipDeleteFailure() {
        showClipToast(R.string.clip_delete_error_message);
    }

    @Override
    public void allDismissCallback() {
        clearDialogContentText();
    }


    @Override
    public void onOKCallback(final boolean isOK) {
        if (mShowDialog != null) {
            if (mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.OPTIONAL_VERSION_UP
                    || mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.FORCED_VERSION_UP) {
                toGooglePlay(UrlConstants.WebUrl.DTVT_GOOGLEPLAY_DOWNLOAD_URL);
                return;
            } else if ((mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.SETTING_FILE_ERROR_DIALOG)) {
                stopAllActivity();
                return;
            }
            onVersionUpDialogShow(ContentUtils.ILLEGAL_VALUE, CustomDialog.DialogTapType.OK);
        }
    }


    @Override
    public void onCancelCallback() {
        if (mShowDialog != null) {
            if (mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.OPTIONAL_VERSION_UP) {
                return;
            }
            onVersionUpDialogShow(ContentUtils.ILLEGAL_VALUE, CustomDialog.DialogTapType.CANCEL);
        }
    }

    @Override
    public void onApiListItemClickCallback(final int which) {
        if (mShowDialog != null) {
            onVersionUpDialogShow(which, CustomDialog.DialogTapType.LIST);
        }
    }

    /**
     * 次のダイアログの判定の為に、今のダイアログの文言をクリアする.
     */
    protected void clearDialogContentText() {
        if (mShowDialog != null) {
            mShowDialog.clearContentText();
        }
    }

    @Override
    public void otherDismissCallback() {
        if (mShowDialog != null) {
            if (mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.FORCED_VERSION_UP
                    || mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.SETTING_FILE_ERROR_DIALOG) {
                stopAllActivity();
                return;
            } else if (mShowDialog.getErrorDialogType() == CustomDialog.ShowDialogType.OPTIONAL_VERSION_UP) {
                return;
            }
            onVersionUpDialogShow(-1, CustomDialog.DialogTapType.BACK_KEY);
        }
    }

    @Override
    public void onItemClickCallBack(final ContentsData contentsData, final String recommendFlg) {

    }

    @Override
    public void onConnectionChangeCallback(final boolean isStbConnectedHomeNetwork) {
        DTVTLogger.warning("isStbConnectedHomeNetwork = " + isStbConnectedHomeNetwork);
        setStbStatus(isStbConnectedHomeNetwork);
        if (isStbConnectedHomeNetwork) {
            checkDAccountOnRestart();
        }
        noticeConnectionChange();
    }

    /**
     * ペアリング変更通知
     *
     * BaseActivityでペアリング状態の変更を検知した際に本メソッドをcallする。
     * ペアリング状態変更を検知したい子クラスは本メソッドをOverrideすること。
     */
    protected void noticeConnectionChange() {
        // NOP
    }

    /**
     * スクリーン・ビューを送る.
     *
     * @param screenName スクリーン名
     * @param customDimensions カスタム ディメンション
     */
    protected void sendScreenView(final String screenName, final SparseArray<String> customDimensions) {
        DTVTLogger.start();
        GoogleAnalyticsUtils.sendScreenViewInfo(screenName, customDimensions);
        DTVTLogger.end();
    }

    /**
     * イベントを送る.
     *
     * @param category イベントのカテゴリ
     * @param action イベントのアクション
     * @param customDimensions カスタム ディメンション
     * @param label ラベル
     */
    protected void sendEvent(final String category, final String action, final String label, final SparseArray<String> customDimensions) {
        DTVTLogger.start();
        GoogleAnalyticsUtils.sendEventInfo(category, action, label, customDimensions);
        DTVTLogger.end();
    }
    // endregion callback

    /**
     * 自動ローカルレジストレーションの処理.
     */
    private void execAutoLocalRegistration() {
        //前回のレジストレーションから24時間経過しているかどうかのチェック
        if (SharedPreferencesUtils.isLocalRegistAfter24Hour(getApplicationContext())) {
            //24時間経過していたので、再度ローカルレジストレーションを行う
            DlnaManager.shared().mLocalRegisterListener = new DlnaManager.LocalRegisterListener() {
                @Override
                public void onRegisterCallBack(final boolean result, final DlnaUtils.ExecuteLocalRegistrationErrorType errorType, final String errorCode) {
                    if (result) {
                        //ローカルレジストレーションが成功したので日時を蓄積する
                        SharedPreferencesUtils.setRegistTime(getApplicationContext());
                    }

                    DTVTLogger.start("auto local registration result = " + result + "/ ErrorType = " + errorType);
                    //自動ローカルレジストレーションでは、結果とは無関係にメッセージなどは表示しない
                    DTVTLogger.end();
                }
            };

            //宅内判定を行う
            if (StbConnectionManager.shared().getConnectionStatus()
                    == StbConnectionManager.ConnectionStatus.HOME_IN) {

                // RestartDiragの実行に時間がかかるのでスレッド化
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //DLNAの下準備
                        DlnaManager.shared().StartDtcp();
                        DlnaManager.shared().RestartDirag();

                        //Udnを取得し、再ローカルレジストレーションを行う
                        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils
                                .getSharedPreferencesStbInfo(mContext.getApplicationContext());
                        DlnaManager.shared().RequestLocalRegistration(dlnaDmsItem.mUdn,
                                getApplicationContext());
                        DTVTLogger.debug("auto local registration start.");
                    }
                }).start();

            } else {
                //宅外なので、再ローカルレジストレーションは行わない
                DTVTLogger.debug("local registration is not done.(not HOME_IN)");
            }
        } else {
            //ローカルレジストレーション見送りのログ出力
            DTVTLogger.debug("less than 24 hours local registration is not done.(Off hours)");
        }
    }

    @Override
    public void onChanged() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reBuildAllTable();
            }
        });
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final String[] serviceIdUniq) {
        //Nop 仕様上実装が必要なため空メソッドとして定義
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        //Nop 仕様上実装が必要なため空メソッドとして定義
    }

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
    }

    /**
     * ドロワーメニューをクローズする.
     * @return true or false
     */
    public boolean closeDrawerMenu() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.END)) {
            mDrawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    /**
     * WiFi状態取得.
     * @return true or false
     */
    protected boolean isWifiOn() {
        boolean result = false;
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        if (wm.isWifiEnabled()) {
            if (wifiInfo.getNetworkId() != -1) {
                result = true;
            }
        }
        return result;
    }

    /**
     * getLinkListSize.
     * @return LinkListSize
     */
    private int getLinkListSize() {
        return mLinkedList.size();
    }

    @Override
    public void onShowSettingFileDialog(final String errorMessage, final CustomDialog.ShowDialogType showDialogType,
                                        final CustomDialog.ApiOKCallback okCallback, final CustomDialog.ApiCancelCallback cancelCallback,
                                        final CustomDialog.DismissCallback dismissCallback) {
        showCommonControlErrorDialog(errorMessage, showDialogType, okCallback, cancelCallback, dismissCallback);
    }

    @Override
    public void onSettingFileGetErrorCallback() {
        //NOP
    }
}
