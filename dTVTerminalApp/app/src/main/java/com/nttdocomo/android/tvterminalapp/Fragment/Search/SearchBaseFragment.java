package com.nttdocomo.android.tvterminalapp.Fragment.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.Search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.adapter.SearchResultBaseAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchBaseFragment extends Fragment implements AbsListView.OnScrollListener {

    public Context mActivity;
    public List mData;
    private TextView mCountText=null;
    private SearchBaseFragmentScrollListener mSearchBaseFragmentScrollListener=null;
    private View mLoadMoreView;
    private View mLoadCompleteView;

    public void setSearchBaseFragmentScrollListener(SearchBaseFragmentScrollListener lis){
        mSearchBaseFragmentScrollListener=lis;
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        initData();
        return initView();
    }

    //モックデータ
    private void initData() {
        mData = new ArrayList();
    }

    private View mTeleviFragmentView;
    private ListView mTeveviListview;

    public View initView(){
        if(null==mTeleviFragmentView) {
            mTeleviFragmentView = View.inflate(getActivity()
                    , R.layout.fragment_televi_content, null);
            mTeveviListview = mTeleviFragmentView.findViewById(R.id.lv_searched_result);

            mTeveviListview.setOnScrollListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_more, null);
            mLoadCompleteView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_complete, null);

            //mTeveviListview.addFooterView(mLoadMoreView);
            //mTeveviListview.addFooterView(mLoadCompleteView);
            mLoadMoreView.setVisibility(View.GONE);
            mLoadCompleteView.setVisibility(View.GONE);
        }

        SearchResultBaseAdapter searchResultBaseAdapter
                = new SearchResultBaseAdapter(getContext(), mData, R.layout.item_search_result_televi);
        mTeveviListview.setAdapter(searchResultBaseAdapter);

        if(null==mCountText) {
            mCountText = mTeleviFragmentView.findViewById(R.id.tv_searched_result);
        }

        return mTeleviFragmentView;
    }


    public void refresh(String count){
        initView();
        mCountText.setText(count);
    }

    public void clear(){
        mData.clear();
        refresh(SearchTopActivity.sSearchCountDefault);
    }

    public void pagingFinish(){
        if(null != mTeveviListview){
            mTeveviListview.setVisibility(View.GONE);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        switch(scrollState){
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                mTeveviListview.setVisibility(View.VISIBLE);
                break;
            default:
        }

        if(null!=mSearchBaseFragmentScrollListener){
            mSearchBaseFragmentScrollListener.onScrollStateChanged(this, absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(null!=mSearchBaseFragmentScrollListener){
            mSearchBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

}
