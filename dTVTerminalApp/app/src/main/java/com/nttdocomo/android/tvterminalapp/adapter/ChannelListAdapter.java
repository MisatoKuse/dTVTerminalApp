/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListItem;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

public class ChannelListAdapter extends BaseAdapter {

    public enum ChListDataType {
        CH_LIST_DATA_TYPE_INVALID,
        CH_LIST_DATA_TYPE_BS,
        CH_LIST_DATA_TYPE_TER,
        CH_LIST_DATA_TYPE_HIKARI,
        CH_LIST_DATA_TYPE_DTV,
    }

    private Context mContext;
    private List mData;
    private int mLayoutId;
    private ThumbnailProvider mThumbnailProvider;
    private ChListDataType mChListDataType;

    private static final int TEXT_HEIGHT = 46;
    private static final int TEXT_MARGIN_START = 10;
    private static final int TEXT_MARGIN_END = 16;
    private static final int POSITION_ZERO = 0;
    private static final int POSITION_ONE = 1;

    public ChannelListAdapter(final Context context, final List data, final int id) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = id;
        this.mThumbnailProvider = new ThumbnailProvider(context);
    }

    public void setChListDataType(final ChListDataType type) {
        mChListDataType = type;
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public Object getItem(final int i) {
        if (null == mData) {
            return null;
        }
        return mData.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public View getView(final int position,View convertView, final  ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, mLayoutId, null);
            holder.mThumbnail = view.findViewById(R.id.channel_list_item_thumb_iv);
            holder.mChannelName = view.findViewById(R.id.channel_list_item_title_tv);
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                case CH_LIST_DATA_TYPE_TER:
                    holder.mThumbnail.setVisibility(View.GONE);
                    DisplayMetrics DisplayMetrics = mContext.getResources().getDisplayMetrics();
                    float density = DisplayMetrics.density;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, TEXT_HEIGHT * (int) density);
                    layoutParams.setMarginStart(TEXT_MARGIN_START * (int) density);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    layoutParams.setMarginEnd(TEXT_MARGIN_END * (int) density);
                    view.findViewById(R.id.channel_list_item_title_tv).setLayoutParams(layoutParams);
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                case CH_LIST_DATA_TYPE_DTV:
                    holder.mThumbnail.setVisibility(View.VISIBLE);
                    break;
                case CH_LIST_DATA_TYPE_INVALID:
                    break;
                default:
                    break;
            }
            convertView = view;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<String> nameThumbnail = new ArrayList();
        getDatas(nameThumbnail, position);
        String chName = nameThumbnail.get(POSITION_ZERO);
        String thumbnail = nameThumbnail.get(POSITION_ONE);

        if (null != holder.mChannelName && null != chName) {
            holder.mChannelName.setText(chName);
        }

        if (null != holder.mThumbnail && null != thumbnail) {
            holder.mThumbnail.setTag(thumbnail);
            Bitmap bp = mThumbnailProvider.getThumbnailImage(holder.mThumbnail, thumbnail);
            if (null != bp) {
                holder.mThumbnail.setImageBitmap(bp);
            } else {
                //TODO URLがない場合はサムネイル取得失敗の画像を表示
                // チャンネルエラーlogoを差し替え
                holder.mThumbnail.setImageResource(R.drawable.error_list);
            }
        }

        return convertView;
    }

    private void getDatas(final ArrayList<String> nameThumbnailOut, final int position) {
        String chName = null;
        String thumbnail = null;
        if (null != mData) {
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                    DlnaBsChListItem bsItem = (DlnaBsChListItem) mData.get(position);
                    if (null != bsItem) {
                        chName = bsItem.mTitle;
                        thumbnail = null;
                    }
                    break;
                case CH_LIST_DATA_TYPE_TER:
                    DlnaTerChListItem terItem = (DlnaTerChListItem) mData.get(position);
                    if (null != terItem) {
                        chName = terItem.mTitle;
                        thumbnail = null;
                    }
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                    ChannelInfo ch = (ChannelInfo) mData.get(position);
                    if (null != ch) {
                        chName = ch.getTitle();
                        thumbnail = ch.getThumbnail();
                    }
                    break;
                case CH_LIST_DATA_TYPE_DTV:
                    ChannelInfo ch2 = (ChannelInfo) mData.get(position);
                    if (null != ch2) {
                        chName = ch2.getTitle();
                        thumbnail = ch2.getThumbnail();
                    }
                    break;
                case CH_LIST_DATA_TYPE_INVALID:
                    break;
            }
        }
        nameThumbnailOut.add(chName);
        nameThumbnailOut.add(thumbnail);
    }

    class ViewHolder {
        ImageView mThumbnail;
        TextView mChannelName;
    }
}

