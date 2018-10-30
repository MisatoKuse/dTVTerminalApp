/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.PremiumVideoActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecommendActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordReservationListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RentalListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.WatchingVideoListActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.NoticeActivity;
import com.nttdocomo.android.tvterminalapp.activity.setting.SettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.RankingTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.adapter.MenuListAdapter;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MenuDisplay.
 */
public class MenuDisplay implements AdapterView.OnItemClickListener {
    /**Singleton.*/
    private static final MenuDisplay sMenuDisplay = new MenuDisplay();
    /**ユーザーステータス.*/
    private UserState mUserState = UserState.LOGIN_NG;
    /**BaseActivity.*/
    private BaseActivity mActivity = null;
    /**コンテキスト.*/
    private Context mContext = null;
    /**アカウント名.*/
    private View mAccountName = null;
    /**ポップアップウィンドウ.*/
    private PopupWindow mPopupWindow = null;
    /**メニューリスト.*/
    private ListView mGlobalMenuListView = null;
    /**メニューアイテムタイトル.*/
    private List mMenuItemTitles = null;
    /**メニューアイテムカウント.*/
    private List mMenuItemCount = null;

    /** メニュー表示種別 .*/
    public static final int INT_NONE_COUNT_STATUS = -1;
    /**ドロワーレイアウト.*/
    private CustomDrawerLayout mDrawerLayout;
    /** 項目名.*/
    private String mMenuName = null;

    /**
     * 機能.
     * Singletonのため、privateにする
     */
    private MenuDisplay() {
    }

    /**
     * インスタンス.
     * @return インスタンス.
     */
    public static MenuDisplay getInstance() {
        return sMenuDisplay;
    }

    /**
     * baseActivityに設定する.
     * @param activity BaseActivity
     * @param context コンテキスト
     * @param customDrawerLayout ドロワーレイアウト
     * @param menuListview メニューリスト
     */
    public void setActivityAndListener(final BaseActivity activity, final Context context,
                                       final CustomDrawerLayout customDrawerLayout, final ListView menuListview) {
        synchronized (MenuDisplay.class) {
            mContext = context;
            mActivity = activity;
            mDrawerLayout = customDrawerLayout;
            mGlobalMenuListView = menuListview;
        }
    }

    /**ユーザー状態チェンジ.
     * @param userState ユーザー状態
     * */
    public void changeUserState(final UserState userState) {
        mUserState = userState;
    }

    /**
     * メニュー表示.
     */
    public void display() {
        refreshMenu();
    }

    /** メニューリスト更新.*/
    private void refreshMenu() {
        initPopupWindow();
    }
    /** ポップアップメニュー初期化.*/
    private void initPopupWindow() {
        CustomDrawerLayout.DrawerListener drawerListener = new CustomDrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
            }
            @Override
            public void onDrawerOpened(final View drawerView) {
                drawerView.setClickable(true);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
            @Override
            public void onDrawerClosed(final View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (mMenuName != null) {
                    startMenuItem(mMenuName);
                }
            }
            @Override
            public void onDrawerStateChanged(final int newState) {

            }
        };
        mDrawerLayout.addDrawerListener(drawerListener);
        mDrawerLayout.openDrawer(Gravity.END);
        ImageView menuClose = mDrawerLayout.findViewById(R.id.account_status_icon_close);
        menuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                }
            }
        });
        TextView accountId = mDrawerLayout.findViewById(R.id.account_status_text);
        if (UserState.LOGIN_NG.equals(mUserState)) {
            accountId.setText(mActivity.getString(R.string.nav_menu_status_login_ng));
        } else {
            accountId.setText(StringUtils.modifyAccountId(SharedPreferencesUtils.getSharedPreferencesDaccountId(mActivity)));
        }
        accountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DaccountUtils.startDAccountApplication(mActivity);
            }
        });
        loadMenuList();
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {

        TextView title = view.findViewById(R.id.tv_title);
        if (null != title) {
             mMenuName = (String) title.getText();

            //「ひかりTV for docomo」、「テレビアプリを起動する」のClickは無効
            if (mMenuName.equals(mActivity.getString(R.string.nav_menu_item_hikari_tv_none_action))
                || mMenuName.equals(mActivity.getString(R.string.nav_menu_item_premium_tv_app_start_common))) {
                return;
            }

            // 閉じるアニメーションを見せてから遷移させる為、先に閉じる
            mDrawerLayout.closeDrawers();
        }
    }

    /**
     * GlobalMenuから起動.
     * @param menuName　項目名
     */
    private void startMenuItem(final String menuName) {

        //GlobalMenuから開いたページはRootActivityとなるため、後ろのActivityは存在しない状態にする
        Intent intent = mActivity.getIntent();

        if (menuName.equals(mActivity.getString(R.string.nav_menu_item_home))) {
            intent.setClass(mActivity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_program_list))) {
            intent.setClass(mActivity, TvProgramListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_channel_list))) {
            intent.setClass(mActivity, ChannelListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_recorder_program))) {
            intent.setClass(mActivity, RecordedListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_recommend_program_video))) {
            intent.setClass(mActivity, RecommendActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_staff_recommend))) {
            //4月時は非対応
            DTVTLogger.debug("4月時は非対応");
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_ranking))) {
            intent.setClass(mActivity, RankingTopActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_clip))) {
            intent.setClass(mActivity, ClipListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_purchased_video))) {
            //4月時は非対応
            DTVTLogger.debug("4月時は非対応");
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_watch_listen_video))) {
            intent.setClass(mActivity, WatchingVideoListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_record_reserve))) {
            intent.setClass(mActivity, RecordReservationListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_video))) {
            intent.setClass(mActivity, VideoTopActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_keyword_search))) {
            intent.setClass(mActivity, SearchTopActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_premium_video))) {
            intent.setClass(mActivity, PremiumVideoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.rental_title))) {
            intent.setClass(mActivity, RentalListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_notice))) {
            intent.setClass(mActivity, NoticeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_setting))) {
            intent.setClass(mActivity, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, true);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_d_anime_copyright))) {
            //dアニメストアのコピーライト表記サイトを表示する為、外部ブラウザを呼び出す
            Uri uri = Uri.parse(UrlConstants.WebUrl.D_ANIME_COPYRIGHT_SITE_URL);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            mActivity.startActivity(intent);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_hikari_tv))) {
            mActivity.setRemoteProgressVisible(View.VISIBLE);
            // TVアプリ起動導線(ひかりTV)
            mActivity.setRelayClientHandler();
            RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.HIKARITV, mContext);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_dtv_channel))) {
            mActivity.setRemoteProgressVisible(View.VISIBLE);
            // TVアプリ起動導線(dTVチャンネル)
            mActivity.setRelayClientHandler();
            RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL, mContext);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_dtv))) {
            mActivity.setRemoteProgressVisible(View.VISIBLE);
            // TVアプリ起動導線(dTV)
            mActivity.setRelayClientHandler();
            RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mContext);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_d_animation))) {
            mActivity.setRemoteProgressVisible(View.VISIBLE);
            // TVアプリ起動導線(dアニメ)
            mActivity.setRelayClientHandler();
            RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mContext);
        } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_dazn))) {
            mActivity.setRemoteProgressVisible(View.VISIBLE);
            // TVアプリ起動導線(DAZN)
            mActivity.setRelayClientHandler();
            RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DAZN, mContext);
        }
        mMenuName = null;
    }

    /**
     *バックグラウンド透明度.
     * @param bgAlpha 透明度
     */
    private void backgroundAlpha(final float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * メニューリストロード.
     */
    private void loadMenuList() {
//        addFooterView(mGlobalMenuListView); //アカウント名アイテム追加
        initMenuListData();

        MenuListAdapter mMenuListAdapter = new MenuListAdapter(mActivity, mMenuItemTitles, mMenuItemCount);
        mGlobalMenuListView.setAdapter(mMenuListAdapter);

        mGlobalMenuListView.setOnItemClickListener(this);
    }

    /**
     * メニューリストデータ初期化.
     */
    private void initMenuListData() {
        mMenuItemTitles = new ArrayList();
        mMenuItemCount = new ArrayList();
        switch (mUserState) {
            //メニュー(未加入)
            case LOGIN_NG:
                setMenuItemLogoff();
                //未加入だけ場合は表示されない
                removeFooterView();
                break;
            //メニュー(未契約ログイン)
            case LOGIN_OK_CONTRACT_NG:
                setMenuItemUnsignedLogin();
                break;
            //メニュー(契約・ペアリング未)
            case CONTRACT_OK_PAIRING_NG:
                setMenuItemSignedUnpaired();
                break;
            //メニュー(契約・ペアリング済み)
            case CONTRACT_OK_PARING_OK:
                setMenuItemSignedPairing();
                break;
            default:
                break;
        }
    }
    /**
     * addFooterView.
     * @param mGlobalMenuListView グロバルメニューリストビュー
     */
    private void addFooterView(final ListView mGlobalMenuListView) {
        if (mAccountName == null) {
            mAccountName = View.inflate(mActivity, R.layout.menu_login_foot, null);
        }
        TextView accountName = mAccountName.findViewById(R.id.tv_menu_account_name);
        accountName.setText(mActivity.getUserName());
        mGlobalMenuListView.addFooterView(mAccountName);
    }

    /**
     *removeFooterView.
     */
    private void removeFooterView() {
        if (mAccountName != null) {
            mGlobalMenuListView.removeFooterView(mAccountName);
        }
    }

    /** ペアリングかつ契約状態のメニューリストアイテム.*/
    private void setMenuItemSignedPairing() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //録画番組～録画予約
        setSighedMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();
        if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN) {
            //テレビアプリを起動する
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_premium_tv_app_start_common));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //ひかりTV
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_hikari_tv));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //dTVチャンネル
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_dtv_channel));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //dTV
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_dtv));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //dアニメ
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_d_animation));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //DAZN
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_dazn));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);
        }
    }
    /** 契約未ペアリングメニューリストアイテム. */
    private void setMenuItemSignedUnpaired() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //録画番組～録画予約
        setSighedMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();
    }

    /** 未契約ログインメニューリストアイテム. */
    private void setMenuItemUnsignedLogin() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //ランキング～ビデオ
        setUnsignedAndLogoffMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();

        //ペアリング済みで宅内
        if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_IN) {
            //テレビアプリを起動する
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_premium_tv_app_start_common));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //ひかりTV
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_hikari_tv));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //dTVチャンネル
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_dtv_channel));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //dTV
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_dtv));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //dアニメ
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_d_animation));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);

            //DAZN
            mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_dazn));
            mMenuItemCount.add(INT_NONE_COUNT_STATUS);
        }
    }

    /**
     *未加入場合のメニューアイテム.
     */
    private void setMenuItemLogoff() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //ランキング～ビデオ
        setUnsignedAndLogoffMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();
    }

    /**
     * ホームからチャンネルリストまでは共通のためここにまとめる.
     */
    private void setHeaderMenuItem() {
        //ホーム
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_home));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //おすすめ番組・ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recommend_program_video));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //キーワードで探す
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_keyword_search));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //ひかりTV
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_hikari_tv_none_action));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //番組表
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_program_list));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //チャンネルリスト
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_channel_list));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);
    }

    /**
     * 契約済みの録画番組～録画予約までは共通のためここにまとめる.
     */
    private void setSighedMiddleMenuItem() {
        //録画番組
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recorder_program));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //ランキング
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_ranking));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //視聴中ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_watch_listen_video));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //クリップ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_clip));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_video));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //プレミアムビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_premium_video));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //レンタル
        mMenuItemTitles.add(mActivity.getString(R.string.rental_title));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //録画予約
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_record_reserve));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);
    }

    /**
     * 未契約ログイン、未ログインの共通部分を統一.
     */
    private void setUnsignedAndLogoffMiddleMenuItem() {
        //ランキング
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_ranking));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_video));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);
    }

    /**
     * お知らせ、設定、dアニメストアのコピーライトは共通のため、このメソッドに統一.
     */
    private void setFooterMenuItem() {
        //お知らせ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_notice));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //設定
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_setting));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //dアニメストアコピーライト表記
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_d_anime_copyright));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);
    }
}
