/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.temp.DAccountAppliActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDevList;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;


public class STBSelectActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DlnaDevListListener{

    public static final String StateModeRepair="Repair";

    private static boolean mIsNextTimeHide=false;

    CheckBox mCheckBoxSTBSelectActivity=null;
    Button mUseWithoutPairingSTBParingInvitationActivity=null;
    Button mButton1STBSelectActivity=null;
    Button mButton2STBSelectActivity=null;
    Button mButton3STBSelectActivity=null;
    Button mDAccountLoginYesSTBSelectActivity=null;
    Button mDAccountLoginNoSTBSelectActivity=null;
    Button mDAccountAppliYesSTBSelectActivity=null;
    Button mDAccountAppliNoSTBSelectActivity=null;
    Button mDAccountSameYesSTBSelectActivity=null;
    Button mDAccountSameNoSTBSelectActivity=null;
    private TextView mBackIcon;
    private ImageView mParingImageView;
    private ListView mDeviceListView;
    private List<DlnaDmsItem> mDeviceList;
    List<ContentsData> mContentsList;
    private ContentsAdapter mContentsAdapter;
    private View mLoadMoreView = null;
    private DlnaProvDevList mDlnaProvDevList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_select_main_layout);
        mContentsList = new ArrayList();
        mBackIcon = findViewById(R.id.header_layout_back);
        mBackIcon.setVisibility(View.GONE);
        mParingImageView = findViewById(R.id.header_layout_menu);
        mParingImageView.setImageResource(R.mipmap.ic_personal_video_white_24dp);
        mParingImageView.setVisibility(View.VISIBLE);
        setTitleText(getString(R.string.str_app_title));
        setContents();
        initView();
        setDevListener();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    private void initView(){
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }

        mDeviceListView = findViewById(R.id.stb_device_name_list);
        mContentsAdapter = new ContentsAdapter(this,mContentsList,ContentsAdapter.ActivityTypeItem.TYPE_STB_SELECT_LIST );
        mDeviceListView.setAdapter(mContentsAdapter);
        mDeviceListView.setOnItemClickListener(this);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    private void setDevListener() {
        // TODO DLNA側と連携次第コメントアウトを外す
        mDlnaProvDevList = new DlnaProvDevList();
//        mDlnaProvDevList.start(this);
    }

    private void setContents() {


//        TextView title= (TextView)findViewById(R.id.titleStbSelectActivity);
//        title.setText(getScreenTitle());

        mUseWithoutPairingSTBParingInvitationActivity=(Button)findViewById(R.id.useWithoutPairingSTBParingInvitationActivity);
        mUseWithoutPairingSTBParingInvitationActivity.setOnClickListener(this);

        mCheckBoxSTBSelectActivity=(CheckBox)findViewById(R.id.checkBoxSTBSelectActivity);
        mCheckBoxSTBSelectActivity.setOnClickListener(this);

        mButton1STBSelectActivity=(Button)findViewById(R.id.button1STBSelectActivity);
        mButton1STBSelectActivity.setOnClickListener(this);

        mButton2STBSelectActivity=(Button)findViewById(R.id.button2STBSelectActivity);
        mButton2STBSelectActivity.setOnClickListener(this);

        mButton3STBSelectActivity=(Button)findViewById(R.id.button3STBSelectActivity);
        mButton3STBSelectActivity.setOnClickListener(this);

        mDAccountLoginYesSTBSelectActivity=(Button)findViewById(R.id.dAccountLoginYesSTBSelectActivity);
        mDAccountLoginYesSTBSelectActivity.setOnClickListener(this);

        mDAccountLoginNoSTBSelectActivity=(Button)findViewById(R.id.dAccountLoginNoSTBSelectActivity);
        mDAccountLoginNoSTBSelectActivity.setOnClickListener(this);

        mDAccountAppliYesSTBSelectActivity=(Button)findViewById(R.id.dAccountAppliYesSTBSelectActivity);
        mDAccountAppliYesSTBSelectActivity.setOnClickListener(this);

        mDAccountAppliNoSTBSelectActivity=(Button)findViewById(R.id.dAccountAppliNoSTBSelectActivity);
        mDAccountAppliNoSTBSelectActivity.setOnClickListener(this);

        mDAccountSameYesSTBSelectActivity=(Button)findViewById(R.id.dAccountSameYesSTBSelectActivity);
        mDAccountSameYesSTBSelectActivity.setOnClickListener(this);

        mDAccountSameNoSTBSelectActivity=(Button)findViewById(R.id.dAccountSameNoSTBSelectActivity);
        mDAccountSameNoSTBSelectActivity.setOnClickListener(this);

        setDAccountButtonVisibility(View.GONE);

        repair();
    }

    private void repair() {
        Bundle b= getIntent().getExtras();
        String state="";
        try {
            state = b.getString("state");
        } catch (Exception e) {

        }

        if(state.equals(StateModeRepair)){
            onStbSelected();
        }

    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_stb_select_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mUseWithoutPairingSTBParingInvitationActivity)){
            onUseWithoutPairingButton();
        } else if(v.equals(mCheckBoxSTBSelectActivity)) {
            mIsNextTimeHide=mCheckBoxSTBSelectActivity.isChecked();
        } else if(v.equals(mButton1STBSelectActivity) || v.equals(mButton2STBSelectActivity) || v.equals(mButton3STBSelectActivity) ){
            onStbSelected();
        } else if(v.equals(mDAccountLoginYesSTBSelectActivity) ) {
            onDAccountLoginYesButton();
        } else if(v.equals(mDAccountLoginNoSTBSelectActivity) ) {
            onDAccountLoginNoButton();
        } else if(v.equals(mDAccountAppliYesSTBSelectActivity) ) {
            onDAccountAppliYesButton();
        } else if(v.equals(mDAccountAppliNoSTBSelectActivity) ) {
            onDAccountAppliNoButton();
        } else if(v.equals(mDAccountSameYesSTBSelectActivity) ) {
            onDAccountSameYesButton();
        } else if(v.equals(mDAccountSameNoSTBSelectActivity) ) {
            onDAccountSameNoButton();
        }
    }

    /**
     * STBに同じdアカウントが登録されていない
     */
    private void onDAccountSameNoButton() {
        startActivity(DAccountReSettingActivity.class, null);
    }


    /**
     * STBに同じdアカウントが登録されている
     */
    private void onDAccountSameYesButton() {
        startActivity(STBConnectActivity.class, null);
    }

    /**
     * 端末内にdアカウントアプリがあるか --> ない
     * dアカウントアプリ誘導画面へ
     */
    private void onDAccountAppliNoButton() {
        startActivity(DAccountSettingActivity.class, null);
    }

    /**
     * 端末内にdアカウントアプリがあるか --> ある
     */
    private void onDAccountAppliYesButton() {

        startActivity(DAccountAppliActivity.class, null);
    }

    /**
     * dアカウント登録状態チェック --> 未ログイン
     */
    private void onDAccountLoginNoButton() {
        setDAccountAppliButtonsVisibility(View.VISIBLE);
        setDAccountLoginButtonsVisibility(View.GONE);
    }

    /**
     * dアカウント登録状態チェック --> ログイン済
     */
    private void onDAccountLoginYesButton() {
        setDAccountLoginButtonsVisibility(View.GONE);
        setDAccountSameButtonsVisibility(View.VISIBLE);
    }


    private void onStbSelected() {
        //dAccountState();
        //setDAccountLoginButtonsVisibility(View.VISIBLE);
        // TODO DLNA側と連携次第コメントアウトを外す
//        mDlnaProvDevList.stopListen();
        startActivity(DAccountRegConfirmationActivity.class, null);
    }

    private void onUseWithoutPairingButton() {
        // TODO DLNA側と連携次第コメントアウトを外す
//        mDlnaProvDevList.stopListen();
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(
                this, SharedPreferencesUtils.STATE_TO_HOME_PAIRING_NG);
        if(mIsNextTimeHide){
            startActivity(HomeActivity.class, null);
        } else {
            startActivity(STBParingInvitationActivity.class, null);
        }
    }

//    private void dAccountState(){
//        mUseWithoutPairingSTBParingInvitationActivity.setVisibility(View.GONE);
//        mCheckBoxSTBSelectActivity.setVisibility(View.GONE);
//        mButton1STBSelectActivity.setVisibility(View.GONE);
//        mButton2STBSelectActivity.setVisibility(View.GONE);
//        mButton3STBSelectActivity.setVisibility(View.GONE);
//    }

    private void setDAccountButtonVisibility(int visibility){
        mDAccountLoginYesSTBSelectActivity.setVisibility(visibility);
        mDAccountLoginNoSTBSelectActivity.setVisibility(visibility);

        mDAccountAppliYesSTBSelectActivity.setVisibility(visibility);
        mDAccountAppliNoSTBSelectActivity.setVisibility(visibility);

        mDAccountSameYesSTBSelectActivity.setVisibility(visibility);
        mDAccountSameNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountLoginButtonsVisibility(int visibility){
        mDAccountLoginYesSTBSelectActivity.setVisibility(visibility);
        mDAccountLoginNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountAppliButtonsVisibility(int visibility){
        mDAccountAppliYesSTBSelectActivity.setVisibility(visibility);
        mDAccountAppliNoSTBSelectActivity.setVisibility(visibility);
    }

    private void setDAccountSameButtonsVisibility(int visibility){
        mDAccountSameYesSTBSelectActivity.setVisibility(visibility);
        mDAccountSameNoSTBSelectActivity.setVisibility(visibility);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(STBConnectActivity.class, null);
    }



    @Override
    public void onDeviceJoin(DlnaDMSInfo curInfo, DlnaDmsItem newItem) {
        DTVTLogger.start();

        if(newItem != null) {
            if(mDeviceList == null) {
                mDeviceList = new ArrayList<DlnaDmsItem>();
                mDeviceList.add(newItem);
            } else {
                mDeviceList.add(newItem);
            }
            updateDeviceList();
        } else {
            DTVTLogger.debug("DeviceInfo is NULL");
        }

        DTVTLogger.end();
    }

    @Override
    public void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn) {
        DTVTLogger.start();
        if(leaveDmsUdn != null) {
            if(mDeviceList != null) {
                int i;
                for(i = 0; i < mDeviceList.size(); i++) {
                    if(mDeviceList.get(i).mUdn.equals(leaveDmsUdn)) {
                        mDeviceList.remove(i);
                        break;
                    }
                }
                if(i >= mDeviceList.size()) {
                    updateDeviceList();
                }
            } else {
                // nop.
            }
        } else {
            DTVTLogger.debug("DeviceInfo is NULL");
        }
        DTVTLogger.end();
    }

    @Override
    public void onError(String msg) {
        DTVTLogger.error("DevListListener error msg" + msg);
    }

    private void updateDeviceList() {

        if(mDeviceList != null && mDeviceList.size() != 0) {
            displayMoreData(false);
            mContentsList.clear();
            for (int i=0; i < mDeviceList.size(); i++) {
                ContentsData data = new ContentsData();
                data.setDeviceName(mDeviceList.get(i).mFriendlyName);
                mContentsList.add(data);
            }
        } else {
            mContentsList.clear();
            displayMoreData(true);
        }

        synchronized (this) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 再読み込み時のダイアログ表示処理
     *
     * @param b
     */
    private void displayMoreData(boolean b) {
        if (null != mDeviceListView && null != mLoadMoreView) {
            if (b) {
                mDeviceListView.addFooterView(mLoadMoreView);
            } else {
                mDeviceListView.removeFooterView(mLoadMoreView);
            }
        }
    }
}