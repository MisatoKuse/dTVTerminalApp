/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;

import java.util.ArrayList;

import static com.nttdocomo.android.tvterminalapp.common.CustomDialog.DialogType.CONFIRM;

public class EditMyChannelListAdapter extends BaseAdapter implements View.OnClickListener {

    private static final int GO_FOR_CHANNEL_LIST = 1;
    public static final String COMMA = "," ;
    public static final String SERVICE_ID_MY_CHANNEL_LIST = "service_id";
    private static final String POSITION_MY_CHANNEL_LIST = "position";
    private static final String INDEX_MY_CHANNEL_LIST = "index";
    private static final String TITLE_MY_CHANNEL_LIST = "title";
    private final MyChannelEditActivity mContext;
    private final ArrayList<MyChannelMetaData> mData;
    private final DataTransferImpl mDataTransferImpl;
    private EditChannelListItemImpl mEditItemImpl;

    public EditMyChannelListAdapter(MyChannelEditActivity context, ArrayList<MyChannelMetaData> list) {
        this.mContext = context;
        this.mData = list;
        this.mEditItemImpl = context;
        this.mDataTransferImpl = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.edit_my_channel_list_item, null);
        MyChannelMetaData channel = mData.get(i);
        TextView noTv = inflate.findViewById(R.id.edit_my_channel_list_item_no_tv);
        Button editBt = inflate.findViewById(R.id.edit_my_channel_list_item_edit_bt);
        TextView titleTv = inflate.findViewById(R.id.edit_my_channel_list_item_channel_name_tv);
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_MY_CHANNEL_LIST, i);
        bundle.putString(SERVICE_ID_MY_CHANNEL_LIST,channel.getServiceId());
        bundle.putString(INDEX_MY_CHANNEL_LIST,channel.getIndex());
        bundle.putString(TITLE_MY_CHANNEL_LIST, channel.getTitle());
        editBt.setTag(bundle);
        editBt.setOnClickListener(this);
        titleTv.setText(channel.getTitle());
        noTv.setText(channel.getIndex());
        if(!TextUtils.isEmpty(channel.getServiceId())){//登録
            noTv.setTextColor(mContext.getResources().getColor(R.color.item_num_black));
            noTv.setBackgroundResource(R.color.item_num_register_bg_black);
            editBt.setBackgroundResource(R.mipmap.icon_circle_normal_minus);
        }else {//解除
            noTv.setTextColor(mContext.getResources().getColor(R.color.white_text));
            noTv.setBackgroundResource(R.color.item_num_unregister_bg_black);
            editBt.setBackgroundResource(R.drawable.my_ch_btn_selector);
        }
        return inflate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_my_channel_list_item_edit_bt:
                Bundle bundle = (Bundle) view.getTag();
                if (TextUtils.isEmpty(bundle.getString(SERVICE_ID_MY_CHANNEL_LIST))) {//登録
                    sendEditItemInfoToActivity(view);
                    setEditListInfo();
                } else {//解除
                    showDialogToConfirmUnRegistration(bundle);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 解除確認ダイアログ表示
     *
     * @param bundle
     */
    private void showDialogToConfirmUnRegistration(final Bundle bundle) {
        CustomDialog customDialog = new CustomDialog(mContext, CONFIRM);
        customDialog.setTitle(mContext.getResources().getString(R.string.my_channel_list_setting_dialog_title_unregister));
        customDialog.setContent(mContext.getResources().getString(R.string.my_channel_list_setting_dialog_content_unregister));
        customDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
            @Override
            public void onOKCallback(boolean isOK) {
                if (isOK) {
                    if (mDataTransferImpl != null) {
                        mDataTransferImpl.sendDataToRefreshUi(bundle, mData);//Ui用データを送る
                    }
                }
            }
        });
        customDialog.showDialog();
    }

    /**
     * EditList情報を設定する
     */
    private void setEditListInfo() {
        StringBuffer sb = new StringBuffer();
        for (MyChannelMetaData myChannelData :
                mData) {
            if(myChannelData.getServiceId() != null){
                sb.append(myChannelData.getServiceId());
                sb.append(COMMA);
            }
        }
        mContext.setEditListInfo(sb);
    }

    /**
     * 選択中アイテム情報をactivityに送る
     * @param view
     */
    private void sendEditItemInfoToActivity(View view) {
        Bundle bundle = (Bundle) view.getTag();
        int position = bundle.getInt(POSITION_MY_CHANNEL_LIST);
        String serviceId = bundle.getString(SERVICE_ID_MY_CHANNEL_LIST);
        String index = bundle.getString(INDEX_MY_CHANNEL_LIST);
        String title = bundle.getString(TITLE_MY_CHANNEL_LIST);
        MyChannelMetaData channel = new MyChannelMetaData();
        channel.setIndex(index);
        channel.setServiceId(serviceId);
        channel.setTitle(title);
        if (mEditItemImpl != null) {
            mEditItemImpl.onTapEditListItem(position, channel);
        }
        requestChannelListDataFromServer();
        mContext.mViewPager.setCurrentItem(GO_FOR_CHANNEL_LIST);
    }

    /**
     * サーバからチャンネルリストデータをリクエストする
     */
    private void requestChannelListDataFromServer() {
        HikariTvChDataProvider hikariTvChDataProvider = new HikariTvChDataProvider(mContext);
        hikariTvChDataProvider.getChannelList(1, 1, "");
    }

    /**
     * "MY編集画面"からactivityにアイテム情報を送るインターフェース
     */
    public interface EditChannelListItemImpl {
        void onTapEditListItem(int position, MyChannelMetaData channel);
    }

    /**
     * 画面更新用のデータを送る
     */
    public interface DataTransferImpl {
        void sendDataToRefreshUi(Bundle bundle, ArrayList<MyChannelMetaData> data);
    }
}