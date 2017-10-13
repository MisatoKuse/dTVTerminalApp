package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.DataProvider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchContentInfo;

import java.util.List;

public class SearchResultBaseAdapter extends BaseAdapter {

    private Context mContext = null;
    private List mData = null;
    //private int layoutid;
    private ThumbnailProvider mThumbnailProvider=null;

    public SearchResultBaseAdapter(Context context, List data, int id) {
        this.mContext = context;
        this.mData = data;
        //this.layoutid = id;
        mThumbnailProvider = new ThumbnailProvider(mContext);
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int i) {
        return mData.get(i);
    }

    public long getItemId(int i) {
        return i;
    }



//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = null;
//        view = View.inflate(mContext, layoutid, null);
//        return view;
//    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = setListItemContent(convertView, (SearchContentInfo)mData.get(position));
        } else {
            setListItemContent(convertView, (SearchContentInfo)mData.get(position));
        }
        return convertView;
    }

    private View setListItemContent(View view, SearchContentInfo searchContentInfo){
        if(null==view){
            view = View.inflate(mContext, R.layout.item_search_result_televi, null);
        }

        ViewHolder holder = new ViewHolder();
        holder.iv_thumbnail = view.findViewById(R.id.iv_thumbnail);
        holder.tv_title = view.findViewById(R.id.tv_title);
        holder.tv_des = view.findViewById(R.id.tv_des);
        holder.bt_clip = view.findViewById(R.id.bt_clip);

        float mWidth = mContext.getResources().getDisplayMetrics().widthPixels / 3;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)mWidth,(int)mWidth/2);
        holder.iv_thumbnail.setLayoutParams(layoutParams);

        if(null != holder.tv_title){
            holder.tv_title.setText(searchContentInfo.title);
        }

        if(null != holder.tv_des){
            holder.tv_des.setText("");
        }

        if(searchContentInfo.clipFlag){

        }

        if(null!=holder.iv_thumbnail){
            //holder.iv_thumbnail.setImageBitmap(null);

            Bitmap bp= mThumbnailProvider.getThumbnailImage(holder.iv_thumbnail, searchContentInfo.contentPictureUrl);
            if(null!=bp){
                holder.iv_thumbnail.setImageBitmap(bp);
            }
        }

        view.setTag(holder);

        return view;
    }

    class ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_title;
        TextView tv_des;
        Button bt_clip;
    }
}

