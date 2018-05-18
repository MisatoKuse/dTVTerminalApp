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
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.jni.bs.DlnaBsChListItem;
import com.nttdocomo.android.tvterminalapp.jni.ter.DlnaTerChListItem;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * チャンネルリストのアダプタ.
 */
public class ChannelListAdapter extends BaseAdapter {

    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * 表示するデータのリスト.
     */
    private final List mData;
    /**
     * 表示するレイアウトのリソースID.
     */
    private final int mLayoutId;
    /**
     * サムネイル取得用プロパイダー.
     */
    private final ThumbnailProvider mThumbnailProvider;
    /**
     * データタイプ.
     */
    private ChannelListActivity.ChannelListDataType mChannelListDataType = ChannelListActivity
            .ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI;

    /**
     * テキストの高さ.
     */
    private static final int TEXT_HEIGHT = 46;
    /**
     * テキストのSTARTマージン.
     */
    private static final int TEXT_MARGIN_START = 10;
    /**
     * テキストのENDマージン.
     */
    private static final int TEXT_MARGIN_END = 16;
    /**
     * チャンネル名取得用定数.
     */
    private static final int POSITION_ZERO = 0;
    /**
     * サムネイル取得用定数.
     */
    private static final int POSITION_ONE = 1;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param data コンテンツデータ
     * @param id リソースID
     */
    public ChannelListAdapter(final Context context, final List data, final int id) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = id;
        this.mThumbnailProvider = new ThumbnailProvider(context, ThumbnailDownloadTask.ImageSizeType.CHANNEL);
    }

    /**
     * データタイプを設定.
     *
     * @param type データタイプ
     */
    public void setChannelListDataType(final ChannelListActivity.ChannelListDataType type) {
        mChannelListDataType = type;
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
    public View getView(final int position, final View convertView, final  ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, mLayoutId, null);
            holder.mThumbnail = view.findViewById(R.id.channel_list_item_thumb_iv);
            holder.mChannelName = view.findViewById(R.id.channel_list_item_title_tv);
            switch (mChannelListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                case CH_LIST_DATA_TYPE_TDB:
                    holder.mThumbnail.setVisibility(View.GONE);
                    DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                    float density = displayMetrics.density;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, TEXT_HEIGHT * (int) density);
                    layoutParams.setMarginStart(TEXT_MARGIN_START * (int) density);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    layoutParams.setMarginEnd(TEXT_MARGIN_END * (int) density);
                    view.findViewById(R.id.channel_list_item_title_tv).setLayoutParams(layoutParams);
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                case CH_LIST_DATA_TYPE_DCH:
                    holder.mThumbnail.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        ArrayList<String> nameThumbnail = new ArrayList<>();
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
            }
        } else if (null != holder.mThumbnail && thumbnail == null) {
            holder.mThumbnail.setImageResource(R.mipmap.error_ch_mini);
        }

        return view;
    }

    /**
     * 表示用データを取得.
     *
     * @param nameThumbnailOut 取得したデータを詰めるリスト
     * @param position ポジション
     */
    private void getDatas(final ArrayList<String> nameThumbnailOut, final int position) {
        String chName = null;
        String thumbnail = null;
        if (null != mData && mData.size() > 0) {
            switch (mChannelListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                    if (mData.get(position) instanceof DlnaBsChListItem) {
                        DlnaBsChListItem bsItem = (DlnaBsChListItem) mData.get(position);
                        if (null != bsItem) {
                            chName = bsItem.mChannelName;
                            thumbnail = null;
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_TDB:
                    if (mData.get(position) instanceof DlnaTerChListItem) {
                        DlnaTerChListItem terItem = (DlnaTerChListItem) mData.get(position);
                        if (null != terItem) {
                            chName = terItem.mChannelName;
                            thumbnail = null;
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_HIKARI:
                    if (mData.get(position) instanceof ChannelInfo) {
                        ChannelInfo ch = (ChannelInfo) mData.get(position);
                        if (null != ch) {
                            chName = ch.getTitle();
                            thumbnail = ch.getThumbnail();
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_DCH:
                    if (mData.get(position) instanceof ChannelInfo) {
                        ChannelInfo ch2 = (ChannelInfo) mData.get(position);
                        if (null != ch2) {
                            chName = ch2.getTitle();
                            thumbnail = ch2.getThumbnail();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        nameThumbnailOut.add(chName);
        nameThumbnailOut.add(thumbnail);
    }

    /**
     * ViewHolder.
     */
    static class ViewHolder {
        /**
         * サムネイル.
         */
        ImageView mThumbnail;
        /**
         * チャンネル名.
         */
        TextView mChannelName;
    }
}

