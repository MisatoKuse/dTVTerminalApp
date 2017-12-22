/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.EditMyChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.EditChannelListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.EditChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.EditMyChannelListFragment;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;
import java.util.List;

import static com.nttdocomo.android.tvterminalapp.adapter.EditMyChannelListAdapter.SERVICE_ID_MY_CHANNEL_LIST;
import static com.nttdocomo.android.tvterminalapp.common.CustomDialog.DialogType.ERROR;
import static com.nttdocomo.android.tvterminalapp.common.JsonContents.META_RESPONSE_STATUS_OK;
import static com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala.MY_CHANNEL_MAX_INDEX;

public class MyChannelEditActivity extends BaseActivity implements View.OnClickListener
        , MyChannelDataProvider.ApiDataProviderCallback
        , ScaledDownProgramListDataProvider.ApiDataProviderCallback
        , EditMyChannelListAdapter.EditChannelListItemImpl
        , EditChannelListFragment.ChannelListItemImpl
        ,EditMyChannelListAdapter.DataTransferImpl {

    private static final int CHANNEL_LIST_PAGE = 1;
    private static final int MY_EDIT_CHANNEL_LIST_PAGE = 0;
    public ViewPager mViewPager;
    private EditMyChannelListFragment mEditMyChannelListFragment;
    private EditChannelListFragment mEditChannelListFragment;
    private ArrayList<EditChannelListBaseFragment> mFragmentList = new ArrayList<>();
    private static final int EDIT_CHANNEL_LIST_COUNT = MY_CHANNEL_MAX_INDEX;
    private String[] mServiceIds;
    private int mEditPosition;
    public MyChannelDataProvider mMyChannelDataProvider;
    private Bundle mUnregisterBundle;
    private ArrayList<MyChannelMetaData> mUnregisterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_channel_edit_main_layout);
        setTitleText(getResources().getString(R.string.my_channel_list_setting));
        loadData();
        initView();
    }

    /**
     * view 初期化
     */
    private void initView() {
        mViewPager = findViewById(R.id.my_channel_edit_main_layout_edit_vp);
        mEditMyChannelListFragment = new EditMyChannelListFragment();
        mEditChannelListFragment = new EditChannelListFragment();
        mFragmentList.add(mEditMyChannelListFragment);
        mFragmentList.add(mEditChannelListFragment);
        mViewPager.setAdapter(new EditChannelListPageAdapter(getSupportFragmentManager(), mFragmentList));
    }

    /**
     * データ初期化
     */
    private void loadData() {
        mMyChannelDataProvider = new MyChannelDataProvider(this);
        mMyChannelDataProvider.getMyChannelList(R.layout.my_channel_edit_main_layout);
    }

    /**
     * サーバからデータ取得
     * @param myChannelMetaData 画面に渡すチャンネル番組情報
     */
    @Override
    public void onMyChannelListCallback(ArrayList<MyChannelMetaData> myChannelMetaData) {
        DTVTLogger.start();
        if (myChannelMetaData != null) {
            ArrayList<MyChannelMetaData> editList = mEditMyChannelListFragment.editDatas;
            if(editList.size() == 0){
                for (int i = 1; i <= EDIT_CHANNEL_LIST_COUNT; i++) {
                    MyChannelMetaData myChannelItemData = null;
                    for (int j = 0; j < myChannelMetaData.size(); j++) {
                        MyChannelMetaData myChData = myChannelMetaData.get(j);
                        int index = Integer.parseInt(myChData.getIndex());
                        if (i == index) {
                            myChannelItemData = myChData;
                            break;
                        }
                    }
                    if (myChannelItemData == null) {
                        myChannelItemData = new MyChannelMetaData();
                        myChannelItemData.setIndex(String.valueOf(i));
                    }
                    editList.add(myChannelItemData);
                    mEditMyChannelListFragment.notifyDataChanged();
                }
            } else {
                ArrayList<MyChannelMetaData> rmList = new ArrayList<>();
                for(int i=0;i<myChannelMetaData.size();i++){
                    for(int j=0;j<editList.size();j++){
                        String myChannelItemServiceId = myChannelMetaData.get(i).getServiceId();
                        if(myChannelItemServiceId != null){
                            if(myChannelItemServiceId.equals(editList.get(j).getServiceId())){
                                rmList.add(myChannelMetaData.get(i));
                                break;
                            }
                        }
                    }
                }
                myChannelMetaData.removeAll(rmList);
                editList.addAll(myChannelMetaData);
                mEditMyChannelListFragment.notifyDataChanged();
            }
        }
        DTVTLogger.end();
    }

    /**
     * 登録結果返し
     * @param result 登録状態
     */
    @Override
    public void onMyChannelRegisterCallback(String result) {
        switch (result){
          case META_RESPONSE_STATUS_OK:
              //そのまま通信してデータ取得
              loadData();
            break;
          default:
              showFailedDialog(getResources().getString(R.string
                      .my_channel_list_setting_failed_dialog_content_register));
              break;
        }
    }

    /**
     * 動作失敗ダイアログ
     * @param content
     */
    private void showFailedDialog(String content) {
        CustomDialog customDialog = new CustomDialog(this, ERROR);
        customDialog.setTitle(getResources().getString(R.string.my_channel_list_setting_failed_dialog_title));
        customDialog.setContent(content);
        customDialog.showDialog();
    }

    /**
     * 解除結果返し
     *
     * @param result 解除状態
     */
    @Override
    public void onMyChannelDeleteCallback(String result) {
        switch (result) {
            case META_RESPONSE_STATUS_OK:
                for (int i = 0; i < mUnregisterData.size(); i++) {//解除UI更新
                    if (mUnregisterBundle.getString(SERVICE_ID_MY_CHANNEL_LIST)
                            .equals(mUnregisterData.get(i).getServiceId())) {
                        MyChannelMetaData channel = new MyChannelMetaData();
                        channel.setIndex(mUnregisterData.get(i).getIndex());
                        mUnregisterData.set(i, channel);
                        mEditMyChannelListFragment.notifyDataChanged();
                        break;
                    }
                }
                break;
            default:
                showFailedDialog(getResources().getString(R.string
                        .my_channel_list_setting_failed_dialog_content_unregister));
                break;
        }
    }

    @Override
    public void channelInfoCallback(ChannelsInfo channelsInfo) {

    }

    /**
     * チャンネルリストデータ取得
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(ArrayList<Channel> channels) {
        if (channels != null) {
            mEditChannelListFragment.mData.clear();
            ArrayList<Channel> rmChannels = new ArrayList<>();
            for (int i = 0; i < mServiceIds.length; i++) {
                for (int j = 0; j < channels.size(); j++) {
                    if (mServiceIds[i] != null) {
                        if (mServiceIds[i].equals(channels.get(j).getServiceId())) {
                            rmChannels.add(channels.get(j));
                        }
                    }
                }
            }
            channels.removeAll(rmChannels);//重複登録不可とする
            mEditChannelListFragment.mData.addAll(channels);
            mEditChannelListFragment.notifyDataChanged();
        }
    }

    /**
     * My編集番組表Itemをタップする時
     *
     * @param position
     * @param channel
     */
    @Override
    public void onTapEditListItem(int position, MyChannelMetaData channel) {
        this.mEditPosition = position;
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * チャンネルリストItemをタップする時
     *
     * @param position
     * @param channel
     */
    @Override
    public void onTapChannelListItem(int position, Channel channel) {
        DTVTLogger.start();
        //MYチャンネル登録実行
        executeMyChannelListRegister(position,channel);
        DTVTLogger.end();
    }

    /**
     * マイ番組表設定登録
     * @param position
     * @param channel
     */
    private void executeMyChannelListRegister(int position, Channel channel) {
        MyChannelDataProvider myChDataProvider = new MyChannelDataProvider(this);
        myChDataProvider.getMyChannelRegisterStatus(channel.getServiceId()
                ,channel.getTitle(), WebApiBasePlala.MY_CHANNEL_R_VALUE_G
                ,WebApiBasePlala.MY_CHANNEL_ADULT_TYPE_ADULT,position+1);
    }

    /**
     * 画面更新用データ届く
     * @param bundle
     * @param data
     */
    @Override
    public void sendDataToRefreshUi(Bundle bundle, ArrayList<MyChannelMetaData> data) {
        if(bundle != null && data != null){
            this.mUnregisterBundle = bundle;
            this.mUnregisterData = data;
            //解除通信
            mMyChannelDataProvider.getMyChannelDeleteStatus(bundle.getString(SERVICE_ID_MY_CHANNEL_LIST));
        }
    }

    /**
     * マイチャンネル編集 アダプター
     */
    private class EditChannelListPageAdapter extends FragmentStatePagerAdapter {
        private List fragmentList;

        public EditChannelListPageAdapter(FragmentManager fm, List fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    /**
     * backKey 処理
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mViewPager.getCurrentItem() == CHANNEL_LIST_PAGE
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mViewPager.setCurrentItem(MY_EDIT_CHANNEL_LIST_PAGE);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * EditList情報を設定する
     *
     * @param editListInfo
     */
    public void setEditListInfo(StringBuffer editListInfo) {
        mServiceIds = null;
        mServiceIds = editListInfo.toString().split(EditMyChannelListAdapter.COMMA);
    }

}
