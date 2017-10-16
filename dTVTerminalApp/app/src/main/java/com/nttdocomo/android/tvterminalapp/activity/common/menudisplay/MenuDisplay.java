package com.nttdocomo.android.tvterminalapp.activity.common.menudisplay;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.Home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.Home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.Home.RecommendActivity;
import com.nttdocomo.android.tvterminalapp.activity.Home.RecordReservationListActivity;
import com.nttdocomo.android.tvterminalapp.activity.Home.RecordedListActivity;
import com.nttdocomo.android.tvterminalapp.activity.Other.NewsActivity;
import com.nttdocomo.android.tvterminalapp.activity.Other.SettingActivity;
import com.nttdocomo.android.tvterminalapp.activity.Ranking.RankingTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.Search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.TvProgram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.activity.TvProgram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.Video.VideoTopActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.UserState;

import java.util.ArrayList;
import java.util.List;


public class MenuDisplay implements AdapterView.OnItemClickListener {

    private static MenuDisplay sMenuDisplay = new MenuDisplay();
    private MenuItemParam mMenuItemParam = new MenuItemParam();
    private MenuDisplayEventListener mMenuDisplayEventListener = null;
    private BaseActivity mActivity = null;
    private View mAccountName = null;

    private PopupWindow mPopupWindow = null;
    private MenuListAdapter mMenuListAdapter = null;
    private ListView mGlobalMenuListView = null;
    private List mMenuItemTitles = null;
    private List mMenuItemCount = null;

    /**
     * 機能
     *      Singletonのため、privateにする
     */
    private MenuDisplay() {

    }

    public static MenuDisplay getInstance() {
        return sMenuDisplay;
    }

    public void setActivityAndListener(BaseActivity activity, MenuDisplayEventListener lis) throws Exception{
        if(null == activity){
            throw new Exception("MenuDisplay::setActivityAndListener() --> Param activity can not be null");
        }

        synchronized (MenuDisplay.class) {
            mActivity = activity;
            mMenuDisplayEventListener = lis;
        }
    }

    public void changeUserState(MenuItemParam menuItemParam) {
        if(null != mMenuDisplayEventListener){
            mMenuDisplayEventListener.onPreUserStateChange(mMenuItemParam.getUserState(), menuItemParam.getUserState());
        }

        mMenuItemParam = menuItemParam;
    }

    public void display() {
        refreshMenu();

        if(null != mMenuDisplayEventListener){
            mMenuDisplayEventListener.onUserStateChanged(mMenuItemParam.getUserState(), mMenuItemParam.getUserState());
        }
    }

    private void refreshMenu(){
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
                .inflate(R.layout.home_main_layout, null), Gravity.RIGHT, 0, 0);
        mPopupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        loadMenuList(popupWindowView);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        if(null!=title) {
            String menuName = (String) title.getText();

            if (menuName.equals(mActivity.getString(R.string.nav_menu_item_home))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.HOME);
                    if( !(mActivity instanceof HomeActivity) ) {
                        mActivity.startActivity(HomeActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_program_list))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.PROGRAM_LIST);
                    if( !(mActivity instanceof TvProgramListActivity) ) {
                        mActivity.startActivity(TvProgramListActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_channel_list))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.CHANNEL_LIST);
                    if( !(mActivity instanceof ChannelListActivity) ) {
                        mActivity.startActivity(ChannelListActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_recorder_program))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RECORD_PROGRAM);
                    if( !(mActivity instanceof RecordedListActivity) ) {
                        mActivity.startActivity(RecordedListActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_recommend_program_video))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RECOMMEND_PRO_VIDEO);
                    if( !(mActivity instanceof RecommendActivity) ) {
                        mActivity.startActivity(RecommendActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_staff_recommend))) {
                if (null != mMenuDisplayEventListener) {
                    //4月時は非対応
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_ranking))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RANKING);
                    if( !(mActivity instanceof RankingTopActivity) ) {
                        mActivity.startActivity(RankingTopActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_clip))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.CLIP);
                    if( !(mActivity instanceof ClipListActivity) ) {
                        mActivity.startActivity(ClipListActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_purchased_video))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.VIDEO);
                    //4月時は非対応
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_record_reserve))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.RECORD_RESERVE);
                    if( !(mActivity instanceof RecordReservationListActivity) ) {
                        mActivity.startActivity(RecordReservationListActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_video))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.VIDEO);
                    if( !(mActivity instanceof VideoTopActivity) ) {
                        mActivity.startActivity(VideoTopActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_keyword_search))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.KEY_WORD_SEARCH);
                    if( !(mActivity instanceof SearchTopActivity) ) {
                        mActivity.startActivity(SearchTopActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_notice))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.NOTICE);
                    if( !(mActivity instanceof NewsActivity) ) {
                        mActivity.startActivity(NewsActivity.class, null);
                    }
                }
            } else if (menuName.equals(mActivity.getString(R.string.nav_menu_item_setting))) {
                if (null != mMenuDisplayEventListener) {
                    mMenuDisplayEventListener.onMenuItemSelected(MenuItem.SETTING);
                    if( !(mActivity instanceof SettingActivity) ) {
                        mActivity.startActivity(SettingActivity.class, null);
                    }
                }
            }

            mPopupWindow.dismiss();
        }
    }

    class popupDismissListener implements PopupWindow.OnDismissListener {

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
        addFooterView(mGlobalMenuListView); //アカウント名アイテム追加
        initMenuListData();

        mMenuListAdapter = new MenuListAdapter(mActivity, mMenuItemTitles, mMenuItemCount);
        mGlobalMenuListView.setAdapter((ListAdapter) mMenuListAdapter);

        mGlobalMenuListView.setOnItemClickListener(this);
    }

    private void initMenuListData() {
        mMenuItemTitles = new ArrayList();
        mMenuItemCount = new ArrayList();

        if(UserState.LOGIN_NG == mMenuItemParam.getUserState()) {     //メニュー(未加入)
            setMenuItemLogoff();
            removeFooterView(); //未加入だけ場合は表示されない
        } else if(UserState.LOGIN_OK_CONTRACT_NG==mMenuItemParam.getUserState()) {  //メニュー(未契約ログイン)
            setMenuItemUnsignedLogin();
        } else if(UserState.CONTRACT_OK_PAIRING_NG==mMenuItemParam.getUserState()) { //メニュー(契約・ペアリング未)
            setMenuItemSignedUnpaired();
        } else if(UserState.CONTRACT_OK_PARING_OK==mMenuItemParam.getUserState()) { //メニュー(契約・ペアリング済み)
            setMenuItemSignedPairing();
        }

    }

    private void addFooterView(ListView mGlobalMenuListView) {
        if (mAccountName == null) {
            mAccountName = View.inflate(mActivity, R.layout.menu_login_foot, null);
        }
        TextView accoutName = mAccountName.findViewById(R.id.tv_menu_account_name);
        accoutName.setText(mActivity.getUserName());
        mGlobalMenuListView.addFooterView(mAccountName);
    }

    private void removeFooterView() {
        if(mAccountName != null){
            mGlobalMenuListView.removeFooterView(mAccountName);
        }
    }

    private void setMenuItemSignedPairing() {
        //ホーム
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_home));
        mMenuItemCount.add(-1);

        //番組表
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_program_list));
        mMenuItemCount.add(-1);

        //チャンネルリスト
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_channel_list));
        mMenuItemCount.add(-1);

        //録画番組
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recorder_program));
        mMenuItemCount.add(mMenuItemParam.getRecordProgramCount());

        //おすすめ番組・ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recommend_program_video));
        mMenuItemCount.add(-1);

        //ランキング
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_ranking));
        mMenuItemCount.add(-1);

        //クリップ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_clip));
        mMenuItemCount.add(mMenuItemParam.getClipCount());

        //録画予約
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_record_reserve));
        mMenuItemCount.add(mMenuItemParam.getRecordReserveCount());

        //ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_video));
        mMenuItemCount.add(-1);

        //キーワードで探す
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_keyword_search));
        mMenuItemCount.add(-1);

        //お知らせ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_notice));
        mMenuItemCount.add(mMenuItemParam.getInformationCount());

        //設定
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_setting));
        mMenuItemCount.add(-1);

    }

    private void setMenuItemSignedUnpaired() {
        //ホーム
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_home));
        mMenuItemCount.add(-1);

        //番組表
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_program_list));
        mMenuItemCount.add(-1);

        //チャンネルリスト
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_channel_list));
        mMenuItemCount.add(-1);

        //おすすめ番組・ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recommend_program_video));
        mMenuItemCount.add(-1);

        //ランキング
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_ranking));
        mMenuItemCount.add(-1);

        //クリップ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_clip));
        mMenuItemCount.add(mMenuItemParam.getClipCount());

        //ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_video));
        mMenuItemCount.add(-1);

        //キーワードで探す
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_keyword_search));
        mMenuItemCount.add(-1);

        //お知らせ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_notice));
        mMenuItemCount.add(mMenuItemParam.getInformationCount());

        //設定
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_setting));
        mMenuItemCount.add(-1);

    }

    private void setMenuItemUnsignedLogin() {
        //ホーム
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_home));
        mMenuItemCount.add(-1);

        //番組表
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_program_list));
        mMenuItemCount.add(-1);

        //チャンネルリスト
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_channel_list));
        mMenuItemCount.add(-1);

        //おすすめ番組・ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recommend_program_video));
        mMenuItemCount.add(-1);

        //ランキング
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_ranking));
        mMenuItemCount.add(-1);

        //ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_video));
        mMenuItemCount.add(-1);

        //キーワードで探す
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_keyword_search));
        mMenuItemCount.add(-1);

        //お知らせ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_notice));
        mMenuItemCount.add(mMenuItemParam.getInformationCount());

        //設定
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_setting));
        mMenuItemCount.add(-1);

    }

    private void setMenuItemLogoff() {
        //ホーム
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_home));
        mMenuItemCount.add(-1);

        //番組表
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_program_list));
        mMenuItemCount.add(-1);

        //チャンネルリスト
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_channel_list));
        mMenuItemCount.add(-1);

        //おすすめ番組・ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_recommend_program_video));
        mMenuItemCount.add(-1);

        //ランキング
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_ranking));
        mMenuItemCount.add(-1);

        //ビデオ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_video));
        mMenuItemCount.add(-1);

        //キーワードで探す
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_keyword_search));
        mMenuItemCount.add(-1);

        //お知らせ
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_notice));
        mMenuItemCount.add(mMenuItemParam.getInformationCount());

        //設定
        mMenuItemTitles.add(mActivity.getString(R.string.nav_menu_item_setting));
        mMenuItemCount.add(-1);
    }

}
