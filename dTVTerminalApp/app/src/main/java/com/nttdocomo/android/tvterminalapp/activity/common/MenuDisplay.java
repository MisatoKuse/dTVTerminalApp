/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecommendActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordReservationListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RentalListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.WatchingVideoListActivity;
import com.nttdocomo.android.tvterminalapp.activity.other.NewsActivity;
import com.nttdocomo.android.tvterminalapp.activity.other.SettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.RankingTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;

import java.util.ArrayList;
import java.util.List;

public class MenuDisplay implements AdapterView.OnItemClickListener {

    private static MenuDisplay sMenuDisplay = new MenuDisplay();
    private MenuItemParam mMenuItemParam = new MenuItemParam();
    private MenuDisplayEventListener mMenuDisplayEventListener = null;
    // TODO BaseActivityを持つことは実装的に望ましくない
    private BaseActivity mActivity = null;
    private Context mContext = null;
    private View mAccountName = null;

    private PopupWindow mPopupWindow = null;
    private MenuListAdapter mMenuListAdapter = null;
    private ListView mGlobalMenuListView = null;
    private List mMenuItemTitles = null;
    private List mMenuItemCount = null;

    // TODO:メニュー表示種別
    public static final int INT_NONE_COUNT_STATUS = -1;

    /**
     * 機能
     * Singletonのため、privateにする
     */
    private MenuDisplay() {
    }

    public static MenuDisplay getInstance() {
        return sMenuDisplay;
    }

    public void setActivityAndListener(BaseActivity activity, Context context) throws Exception {
        if (null == activity) {
            throw new Exception("MenuDisplay::setActivityAndListener() --> Param activity can not be null");
        }

        synchronized (MenuDisplay.class) {
            mContext = context;
            mActivity = activity;
            mMenuDisplayEventListener = (MenuDisplayEventListener) context;
        }
    }

    public void changeUserState(MenuItemParam menuItemParam) {
        if (null != mMenuDisplayEventListener) {
            mMenuDisplayEventListener.onPreUserStateChange(mMenuItemParam.getUserState(), menuItemParam.getUserState());
        }
        mMenuItemParam = menuItemParam;
    }

    public void display() {
        refreshMenu();
        if (null != mMenuDisplayEventListener) {
            mMenuDisplayEventListener.onUserStateChanged(mMenuItemParam.getUserState(), mMenuItemParam.getUserState());
        }
    }

    private void refreshMenu() {
        initPopupWindow();
    }

    private void initPopupWindow() {
        View popupWindowView = mActivity.getLayoutInflater().inflate(R.layout.nav_pop, null);
        mPopupWindow = new PopupWindow(popupWindowView
                , LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.AnimationRightFade);
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.showAtLocation(mActivity.getLayoutInflater()
                .inflate(R.layout.home_main_layout, null), Gravity.END, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        loadMenuList(popupWindowView);
    }

    // TODO ボタン押下時の画面遷移処理はBaseActivityへリスナーを設定して送る
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView title = view.findViewById(R.id.tv_title);
        if (null != title) {
            String menuName = (String) title.getText();

            //GlobalMenuから開いたページはRootActivityとなるため、後ろのActivityは存在しない状態にする
            Intent intent = mActivity.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            if (menuName.equals(mActivity.getString(R.string.nav_menu_item_home))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.HOME);
                    if (!(mActivity instanceof HomeActivity)) {
                        intent.setClass(mActivity, HomeActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_program_list))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.PROGRAM_LIST);
                    if (!(mActivity instanceof TvProgramListActivity)) {
                        intent.setClass(mActivity, TvProgramListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_channel_list))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.CHANNEL_LIST);
                    if (!(mActivity instanceof ChannelListActivity)) {
                        intent.setClass(mActivity, ChannelListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_recorder_program))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RECORD_PROGRAM);
                    if (!(mActivity instanceof RecordedListActivity)) {
                        intent.setClass(mActivity, RecordedListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_recommend_program_video))) {
                if (null != mMenuDisplayEventListener) {
                    intent.setClass(mActivity, RecommendActivity.class);
                    intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                    mActivity.startActivity(intent);
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_staff_recommend))) {
                if (null != mMenuDisplayEventListener) {
                    //4月時は非対応
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_ranking))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RANKING);
                    if (!(mActivity instanceof RankingTopActivity)) {
                        intent.setClass(mActivity, RankingTopActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_clip))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.CLIP);
                    if (!(mActivity instanceof ClipListActivity)) {
                        intent.setClass(mActivity, ClipListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_purchased_video))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.VIDEO);
                    //4月時は非対応
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_watch_listen_video))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.WATCH_LISTEN_VIDEO);
                    if (!(mActivity instanceof WatchingVideoListActivity)) {
                        intent.setClass(mActivity, WatchingVideoListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_record_reserve))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RECORD_RESERVE);
                    if (!(mActivity instanceof RecordReservationListActivity)) {
                        intent.setClass(mActivity, RecordReservationListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_video))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.VIDEO);
                    if (!(mActivity instanceof VideoTopActivity)) {
                        intent.setClass(mActivity, VideoTopActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_keyword_search))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.KEY_WORD_SEARCH);
                    if (!(mActivity instanceof SearchTopActivity)) {
                        intent.setClass(mActivity, SearchTopActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.rental_title))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.KEY_WORD_SEARCH);
                    if (!(mActivity instanceof RentalListActivity)) {
                        intent.setClass(mActivity, RentalListActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_notice))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.NOTICE);
                    if (!(mActivity instanceof NewsActivity)) {
                        intent.setClass(mActivity, NewsActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_setting))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.SETTING);
                    if (!(mActivity instanceof SettingActivity)) {
                        intent.setClass(mActivity, SettingActivity.class);
                        intent.putExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, true);
                        mActivity.startActivity(intent);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_hikari_tv))) {
                if (null != mMenuDisplayEventListener) {
                    // TVアプリ起動導線(ひかりTV)
                    mActivity.setRelayClientHandler();
                    RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.HIKARITV, mContext);
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_dtv_channel))) {
                if (null != mMenuDisplayEventListener) {
                    // TVアプリ起動導線(dTVチャンネル)
                    // TODO BaseActivityで実行するのが望ましい
                    mActivity.setRelayClientHandler();
                    RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL, mContext);
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_dtv))) {
                if (null != mMenuDisplayEventListener) {
                    // TVアプリ起動導線(dTV)
                    // TODO BaseActivityで実行するのが望ましい
                    mActivity.setRelayClientHandler();
                    RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV, mContext);
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_d_animation))) {
                if (null != mMenuDisplayEventListener) {
                    // TVアプリ起動導線(dアニメ)
                    // TODO BaseActivityで実行するのが望ましい
                    mActivity.setRelayClientHandler();
                    RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE, mContext);
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_dazn))) {
                if (null != mMenuDisplayEventListener) {
                    // TVアプリ起動導線(DAZN)
                    // TODO BaseActivityで実行するのが望ましい
                    mActivity.setRelayClientHandler();
                    RemoteControlRelayClient.getInstance().startApplicationRequest(RemoteControlRelayClient.STB_APPLICATION_TYPES.DAZN, mContext);
                }
            }
            mPopupWindow.dismiss();
        }
    }

    class PopupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    private void loadMenuList(View popupWindowView) {

        mGlobalMenuListView = popupWindowView.findViewById(R.id.menu_list);
//        addFooterView(mGlobalMenuListView); //アカウント名アイテム追加
        initMenuListData();

        mMenuListAdapter = new MenuListAdapter(mActivity, mMenuItemTitles, mMenuItemCount);
        mGlobalMenuListView.setAdapter(mMenuListAdapter);

        mGlobalMenuListView.setOnItemClickListener(this);
    }

    private void initMenuListData() {
        mMenuItemTitles = new ArrayList();
        mMenuItemCount = new ArrayList();
        if (UserState.LOGIN_NG == mMenuItemParam.getUserState()) {     //メニュー(未加入)
            setMenuItemLogoff();
            removeFooterView(); //未加入だけ場合は表示されない
        } else if (UserState.LOGIN_OK_CONTRACT_NG == mMenuItemParam.getUserState()) {  //メニュー(未契約ログイン)
            setMenuItemUnsignedLogin();
        } else if (UserState.CONTRACT_OK_PAIRING_NG == mMenuItemParam.getUserState()) { //メニュー(契約・ペアリング未)
            setMenuItemSignedUnpaired();
        } else if (UserState.CONTRACT_OK_PARING_OK == mMenuItemParam.getUserState()) { //メニュー(契約・ペアリング済み)
            setMenuItemSignedPairing();
        }
    }

    private void addFooterView(ListView mGlobalMenuListView) {
        if (mAccountName == null) {
            mAccountName = View.inflate(mActivity, R.layout.menu_login_foot, null);
        }
        TextView accountName = mAccountName.findViewById(R.id.tv_menu_account_name);
        accountName.setText(mActivity.getUserName());
        mGlobalMenuListView.addFooterView(mAccountName);
    }

    private void removeFooterView() {
        if (mAccountName != null) {
            mGlobalMenuListView.removeFooterView(mAccountName);
        }
    }

    private void setMenuItemSignedPairing() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //録画番組～録画予約
        setSighedMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();

        if (mActivity.getStbStatus()) {
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

    private void setMenuItemSignedUnpaired() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //録画番組～録画予約
        setSighedMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();
    }

    private void setMenuItemUnsignedLogin() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //ランキング～ビデオ
        setUnsignedAndLogoffMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();
    }

    private void setMenuItemLogoff() {
        //ホーム～チャンネルリスト
        setHeaderMenuItem();

        //ランキング～ビデオ
        setUnsignedAndLogoffMiddleMenuItem();

        //お知らせ、設定
        setFooterMenuItem();
    }

    /**
     * ホームからチャンネルリストまでは共通のためここにまとめる
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
     * 契約済みの録画番組～録画予約までは共通のためここにまとめる
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
     * 未契約ログイン、未ログインの共通部分を統一
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
     * お知らせ、設定は共通のため、このメソッドに統一
     */
    private void setFooterMenuItem() {
        //お知らせ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_notice));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);

        //設定
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_setting));
        mMenuItemCount.add(INT_NONE_COUNT_STATUS);
    }
}