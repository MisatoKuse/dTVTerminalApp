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

/**
 * チャンネルリストのアダプタ.
 */
public class ChannelListAdapter extends BaseAdapter {

    /**
     * チャンネルリストのタイプ.
     */
    public enum ChListDataType {
        /**
         * タイプ:未定.
         */
        CH_LIST_DATA_TYPE_INVALID,
        /**
         * タイプ:BS.
         */
        CH_LIST_DATA_TYPE_BS,
        /**
         * タイプ:地上波.
         */
        CH_LIST_DATA_TYPE_TER,
        /**
         * タイプ:ひかりTV.
         */
        CH_LIST_DATA_TYPE_HIKARI,
        /**
         * タイプ:dTVチャンネル.
         */
        CH_LIST_DATA_TYPE_DTV,
    }

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
    private ChListDataType mChListDataType = ChListDataType.CH_LIST_DATA_TYPE_INVALID;

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
        this.mThumbnailProvider = new ThumbnailProvider(context);
    }

    /**
     * データタイプを設定.
     *
     * @param type データタイプ
     */
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
    public View getView(final int position, View convertView, final  ViewGroup parent) {
        View view;
        ViewHolder holder;
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
            } else {
                //TODO URLがない場合はサムネイル取得失敗の画像を表示
                // チャンネルエラーlogoを差し替え
                holder.mThumbnail.setImageResource(R.drawable.error_list);
            }
        }

        return convertView;
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
        if (null != mData) {
            switch (mChListDataType) {
                case CH_LIST_DATA_TYPE_BS:
                    if (mData.get(position) instanceof  DlnaBsChListItem) {
                        DlnaBsChListItem bsItem = (DlnaBsChListItem) mData.get(position);
                        if (null != bsItem) {
                            chName = bsItem.mTitle;
                            thumbnail = null;
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_TER:
                    if (mData.get(position) instanceof DlnaTerChListItem) {
                        DlnaTerChListItem terItem = (DlnaTerChListItem) mData.get(position);
                        if (null != terItem) {
                            chName = terItem.mTitle;
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
                case CH_LIST_DATA_TYPE_DTV:
                    if (mData.get(position) instanceof ChannelInfo) {
                        ChannelInfo ch2 = (ChannelInfo) mData.get(position);
                        if (null != ch2) {
                            chName = ch2.getTitle();
                            thumbnail = ch2.getThumbnail();
                        }
                    }
                    break;
                case CH_LIST_DATA_TYPE_INVALID:
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

