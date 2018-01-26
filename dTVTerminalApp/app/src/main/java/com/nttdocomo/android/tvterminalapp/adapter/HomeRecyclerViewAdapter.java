/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

import java.util.List;

/**
 * Home画面に表示するコンテンツリストのアダプタ.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    /**
     * Inflater.
     */
    private LayoutInflater mInflater;
    /**
     * ホーム画面に表示するコンテンツデータのリスト.
     */
    private List<ContentsData> mContentList;
    /**
     * コンテキスト.
     */
    private Activity mContext;
    /**
     * サムネイル取得プロバイダー.
     */
    private ThumbnailProvider thumbnailProvider;
    /**
     * もっと見るフッター.
     */
    private View mFooterView;
    /**
     * 最大表示件数.
     */
    private static final int MAX_COUNT = 10;
    /**
     * ヘッダー.
     */
    private static final int TYPE_HEADER = 0;
    /**
     * フッター.
     */
    private static final int TYPE_FOOTER = 1;
    /**
     * コンテンツ.
     */
    private static final int TYPE_NORMAL = 2;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param contentsDataList 表示するコンテンツリスト
     */
    public HomeRecyclerViewAdapter(Activity context, List<ContentsData> contentsDataList) {
        mInflater = LayoutInflater.from(context);
        this.mContentList = contentsDataList;
        this.mContext = context;
        thumbnailProvider = new ThumbnailProvider(context);
    }

    /**
     * 「すべてを見る」のViewをセット.
     *
     * @param footerView 「すべてを見る」View
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        int count = mContentList.size();
        if (count > MAX_COUNT) {
            count = MAX_COUNT;
        }
        if (mFooterView == null) {
            return count;
        } else {
            return count + 1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewHolder(mFooterView);
        }
        View view = mInflater.inflate(R.layout.home_main_layout_recyclerview_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImage = view.findViewById(R.id.home_main_recyclerview_item_iv);
        viewHolder.mContent = view.findViewById(R.id.home_main_recyclerview_item_tv_content);
        viewHolder.mTime = view.findViewById(R.id.home_main_recyclerview_item_tv_time);
        viewHolder.mNew = view.findViewById(R.id.home_main_recyclerview_item_iv_new);
        viewHolder.mNew.setVisibility(View.GONE);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == TYPE_FOOTER) {
            return;
        }
        ContentsData contentsData = mContentList.get(i);
        String date = contentsData.getTime();
        String title = contentsData.getTitle();
        if (TextUtils.isEmpty(title)) {
            title = contentsData.getTitle();
        }
        String thumbnail = contentsData.getThumURL();
        if (TextUtils.isEmpty(thumbnail)) {
            thumbnail = contentsData.getThumURL();
        }
        if (!TextUtils.isEmpty(title)) {
            viewHolder.mContent.setVisibility(View.VISIBLE);
            viewHolder.mContent.setText(title);
        } else {
            viewHolder.mContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(date)) {
            viewHolder.mTime.setVisibility(View.VISIBLE);
            viewHolder.mTime.setText(date);
        } else {
            viewHolder.mTime.setVisibility(View.GONE);
        }
        //URLによって、サムネイル取得
        if (!TextUtils.isEmpty(thumbnail)) {
            viewHolder.mImage.setTag(thumbnail);
            Bitmap bitmap = thumbnailProvider.getThumbnailImage(viewHolder.mImage, thumbnail);
            if (bitmap != null) {
                viewHolder.mImage.setImageBitmap(bitmap);
            }
        } else {
            //URLがない場合はサムネイル取得失敗の画像を表示
            viewHolder.mImage.setImageResource(R.mipmap.error_scroll);
        }
        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContentDetailActivity.class);
                ComponentName componentName = mContext.getComponentName();
                intent.putExtra(DTVTConstants.SOURCE_SCREEN, componentName.getClassName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (viewHolder.mImage != null) {
            //サムネイルの取得が遅い時、前のViewが残っている事がある現象の対処
            viewHolder.mImage.setImageResource(android.R.color.transparent);
        }
    }

    /**
     * コンテンツビューを初期化.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 各コンテンツを表示するViewHolder.
         *
         * @param itemView コンテンツView
         */
        public ViewHolder(View itemView) {
            super(itemView);
        }
        /**
         * サムネイル.
         */
        ImageView mImage;
        /**
         * コンテンツタイトル.
         */
        TextView mContent;
        /**
         * コンテンツ日時.
         */
        TextView mTime;
        /**
         * Newアイコン.
         * TODO: TextViewからNewアイコンの画像に置き換わる予定
         */
        TextView mNew;
    }
}