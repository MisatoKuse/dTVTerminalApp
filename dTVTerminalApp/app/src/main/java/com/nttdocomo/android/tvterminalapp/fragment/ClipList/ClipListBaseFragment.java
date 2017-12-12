/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ClipList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ClipMainAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipContentInfo;

import java.util.ArrayList;
import java.util.List;


public class ClipListBaseFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnTouchListener {

    private Context mActivity;
    public List mData = null;

    private View mTeleviFragmentView;
    private ListView mTeveviListview;

    private ClipMainAdapter mClipMainAdapter;

    //スクロール位置の記録
    private int mFirstVisibleItem = 0;

    //最後のスクロール方向が上ならばtrue
    private boolean mLastScrollUp = false;

    //指を置いたY座標
    private float mStartY = 0;

    public ClipListBaseFragment() {
        if (mData == null) {
            mData = new ArrayList();
        }
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initData();//一時使うデータ
        return initView();
    }

    private ClipListBaseFragmentScrollListener mClipListBaseFragmentScrollListener = null;

    public void setClipListBaseFragmentScrollListener(ClipListBaseFragmentScrollListener lis) {
        mClipListBaseFragmentScrollListener = lis;
    }

    /**
     * 各タブ画面は別々に実現して表示されること
     */
    private View initView() {
        if (mData == null) {
            mData = new ArrayList();
        }
        mTeleviFragmentView = View.inflate(getActivity()
                , R.layout.fragment_clip_list_item_content, null);

        mLoadMoreView = LayoutInflater.from(getActivity()).inflate(
                R.layout.search_load_more, null);

        /*
        if (mData.size() != 0) {
            initContentListView();
        } else {
            initLoadingContentView();
        }
        */
        initContentListView();
        return mTeleviFragmentView;
    }

    public ClipMainAdapter getClipMainAdapter() {
        return mClipMainAdapter;
    }

    private View initLoadingContentView() {
        View defaultView = View.inflate(getActivity(),
                R.layout.clip_list_default_loading_view, null);
        return defaultView;
    }

    /*テレビタブコンテンツ初期化*/
    private void initContentListView() {
        mTeveviListview = mTeleviFragmentView
                .findViewById(R.id.clip_list_lv_searched_result);

        mTeveviListview.setOnScrollListener(this);
        mTeveviListview.setOnItemClickListener(this);

        //スクロールの上下方向検知用のリスナーを設定
        mTeveviListview.setOnTouchListener(this);

        ThumbnailProvider thumbnailProvider = new ThumbnailProvider(getActivity());
        mClipMainAdapter
                = new ClipMainAdapter(getContext()
                , mData, R.layout.item_clip_list_dvideo, thumbnailProvider);
        mTeveviListview.setAdapter(mClipMainAdapter);
    }

    public void noticeRefresh() {
        if (null != mClipMainAdapter) {
            mClipMainAdapter.notifyDataSetChanged();
        }
    }

    private View mLoadMoreView;

    public void displayMoreData(boolean b) {
        if (null != mTeveviListview) {
            if (b) {
                mTeveviListview.addFooterView(mLoadMoreView);

                //スクロール位置を最下段にすることで、追加した更新フッターを画面内に入れる
                mTeveviListview.setSelection(mTeveviListview.getMaxScrollAmount());
            } else {
                mTeveviListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    public void setMode(ClipMainAdapter.Mode mode) {
        if (null != mClipMainAdapter) {
            mClipMainAdapter.setMode(mode);
            mData.clear();
            /*
            ClipMainAdapter.Mode old= mClipMainAdapter.getMode();
            if(old!=mode){
                mData.clear();
            }
            */
        }
    }

    /*
    public void clearAllDatas(){
        if(null!=mData){
            mData.clear();
        }
    }
    */

    public void setDataVod(VodClipContentInfo.VodClipContentInfoItem clipContentInfoItem) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mData.add(clipContentInfoItem);
    }

    public void setDataTv(TvClipContentInfo.TvClipContentInfoItem clipContentInfoItem) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mData.add(clipContentInfoItem);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (null != mClipListBaseFragmentScrollListener) {
            //スクロール位置がリストの先頭で上スクロールだった場合は、更新をせずに帰る
            if (mFirstVisibleItem == 0 && mLastScrollUp) {
                return;
            }

            mClipListBaseFragmentScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (null != mClipListBaseFragmentScrollListener) {
            //現在のスクロール位置の記録
            mFirstVisibleItem = firstVisibleItem;

            mClipListBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!(view instanceof ListView)) {
            //今回はリストビューの事しか考えないので、他のビューならば帰る
            return false;
        }

        //指を動かした方向を検知する
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //指を降ろしたので、位置を記録
                mStartY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                //指を離したので、位置を記録
                float mEndY = motionEvent.getY();

                mLastScrollUp = false;

                //スクロール方向の判定
                if (mStartY < mEndY) {
                    //終了時のY座標の方が大きいので、上スクロール
                    mLastScrollUp = true;
                }

                break;

            default:
                //現状処理は無い・警告対応
        }

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mLoadMoreView == view) {
            return;
        }

        if(mActivity != null) {
            ((BaseActivity) mActivity).startActivity(DtvContentsDetailActivity.class, null);
        }
    }
}
