/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ContentsData> mContentList;
    private Context context;
    //サムネイル取得プロバイダー
    private ThumbnailProvider thumbnailProvider;
    //もっと見るフッター
    private View mFooterView;
    //最大表示件数
    private static final int MAX_COUNT = 10;
    //ヘッダー
    private static final int TYPE_HEADER = 0;
    //フッター
    private static final int TYPE_FOOTER = 1;
    //普通
    private static final int TYPE_NORMAL = 2;

    public HomeRecyclerViewAdapter(Context context, List<ContentsData> contentsDataList) {
        mInflater = LayoutInflater.from(context);
        this.mContentList = contentsDataList;
        this.context = context;
        thumbnailProvider = new ThumbnailProvider(context);
    }

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
        //コンテンツキャッシュを幅さ、長さ初期化
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels / 3 * 2;
        float heightPixels = widthPixels / 1.8f;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                (int) widthPixels,
                (int) heightPixels);
        view.setLayoutParams(lp);
        viewHolder.mImage.setMaxWidth((int) widthPixels);
        viewHolder.mImage.setMaxHeight((int) heightPixels);
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
        String date = mContentList.get(i).getTime();
        String title = mContentList.get(i).getTitle();
        if (TextUtils.isEmpty(title)) {
            title = mContentList.get(i).getTitle();
        }
        String thumbnail = mContentList.get(i).getThumURL();
        if (TextUtils.isEmpty(thumbnail)) {
            thumbnail = mContentList.get(i).getThumURL();
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
        viewHolder.mImage.setImageResource(R.drawable.test_image);
        //URLによって、サムネイル取得
        if (!TextUtils.isEmpty(thumbnail)) {
            viewHolder.mImage.setTag(thumbnail);
            Bitmap bitmap = thumbnailProvider.getThumbnailImage(viewHolder.mImage, thumbnail);
            if (bitmap != null) {
                viewHolder.mImage.setImageBitmap(bitmap);
            }
        }
        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)context).startActivity(DtvContentsDetailActivity.class, null);
            }
        });
    }

    /**
     * コンテンツビューを初期化
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView mImage;
        TextView mContent;
        TextView mTime;
        TextView mNew;
    }
}