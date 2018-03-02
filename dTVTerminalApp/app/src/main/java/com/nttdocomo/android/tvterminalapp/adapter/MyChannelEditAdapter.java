/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;

import java.util.ArrayList;

/**
 * マイ番組表設定アクティビティ.
 */
public class MyChannelEditAdapter extends BaseAdapter implements View.OnClickListener {

    /**
     * コンマ.
     */
    public static final String COMMA = ",";
    /**
     * サービスIDキー.
     */
    public static final String SERVICE_ID_MY_CHANNEL_LIST = "service_id";
    /**
     *ポジション.
     */
    private final String POSITION_MY_CHANNEL_LIST = "position";
    /**
     * インデックス.
     */
    private static final String INDEX_MY_CHANNEL_LIST = "index";
    /**
     * タイトル.
     */
    private static final String TITLE_MY_CHANNEL_LIST = "title";
    /**
     * マイ番組表設定アクティビティ.
     */
    private final MyChannelEditActivity mContext;
    /**
     * マイ番組表設定Itemのコレクション.
     */
    private final ArrayList<MyChannelMetaData> mData;
    /**
     * 編集ボタンをタップすると"MY編集画面"に情報を反映されるインターフェース.
     */
    private final EditMyChannelItemImpl mEditMyChannelItemImpl;

    /**
     * マイ番組表編集アダプター.
     * @param context Activity
     * @param list チャンネルリスト
     */

    public MyChannelEditAdapter(final MyChannelEditActivity context, final ArrayList<MyChannelMetaData> list) {
        this.mContext = context;
        this.mData = list;
        this.mEditMyChannelItemImpl = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View view, final ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.edit_my_channel_list_item, null);
        MyChannelMetaData channel = mData.get(i);
        TextView noTv = inflate.findViewById(R.id.edit_my_channel_list_item_no_tv);
        Button editBt = inflate.findViewById(R.id.edit_my_channel_list_item_edit_bt);
        TextView titleTv = inflate.findViewById(R.id.edit_my_channel_list_item_channel_name_tv);
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_MY_CHANNEL_LIST, i);
        bundle.putString(SERVICE_ID_MY_CHANNEL_LIST, channel.getServiceId());
        bundle.putString(INDEX_MY_CHANNEL_LIST, channel.getIndex());
        bundle.putString(TITLE_MY_CHANNEL_LIST, channel.getTitle());
        editBt.setTag(bundle);
        editBt.setOnClickListener(this);
        titleTv.setText(channel.getTitle());
        noTv.setText(channel.getIndex());
        if (!TextUtils.isEmpty(channel.getServiceId())) { //登録
            noTv.setTextColor(ContextCompat.getColor(mContext, R.color.item_num_black));
            noTv.setBackgroundResource(R.mipmap.channel_num_active);
            editBt.setBackgroundResource(R.mipmap.icon_circle_normal_minus);
        } else { //解除
            noTv.setTextColor(ContextCompat.getColor(mContext, R.color.white_text));
            noTv.setBackgroundResource(R.mipmap.channel_num_normal);
            editBt.setBackgroundResource(R.drawable.selector_my_ch_btn);
        }
        return inflate;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.edit_my_channel_list_item_edit_bt:
                Bundle bundle = (Bundle) view.getTag();
                //登録
                if (TextUtils.isEmpty(bundle.getString(SERVICE_ID_MY_CHANNEL_LIST))) {
                    mEditMyChannelItemImpl.onAddChannelItem(bundle.getInt(POSITION_MY_CHANNEL_LIST));
                    //解除
                } else {
                    mEditMyChannelItemImpl.onRemoveChannelItem(bundle, bundle.getInt(POSITION_MY_CHANNEL_LIST));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 編集ボタンをタップすると"MY編集画面"に情報を反映されるインターフェース.
     */
    public interface EditMyChannelItemImpl {
        /**
         *登録際、画面遷移用.
         *
         * @param position 追加ポジション
         */
        void onAddChannelItem(final int position);
        /**
         * 解除際、ダイアログを呼び出す.
         *
         * @param bundle Bundle
         * @param position 削除ポジション
         */
        void onRemoveChannelItem(Bundle bundle, final int position);
    }
}