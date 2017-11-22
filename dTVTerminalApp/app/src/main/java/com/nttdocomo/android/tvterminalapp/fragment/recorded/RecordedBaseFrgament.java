/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;

import java.util.ArrayList;
import java.util.List;

public class RecordedBaseFrgament extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public Context mActivity;
    public List<ContentsData> mContentsData;
    private View mLoadMoreView;
    private ContentsAdapter mContentsAdapter = null;
    private RecordedBaseFragmentScrollListener mRecordedBaseFragmentScrollListener = null;

    // TODO DPからのコールバックを受ける

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

    /**
     * モックデータ
     */
    private void initData() {
        mContentsData = new ArrayList();
    }

    private View mRecordedFragmentView;
    private ListView mRecordedListview;

    /**
     * viewの生成
     * @return
     */
    public View initView() {
        if (null == mRecordedFragmentView) {
            mRecordedFragmentView = View.inflate(getActivity()
                    , R.layout.record_contents_list_layout, null);
            mRecordedListview = mRecordedFragmentView.findViewById(R.id.recorded_contents_result);

            mRecordedListview.setOnScrollListener(this);
            mRecordedListview.setOnItemClickListener(this);

            getContext();
            mLoadMoreView = LayoutInflater.from(mActivity).inflate(R.layout.search_load_more, null);
        }

        setDeta();

        mContentsAdapter = new ContentsAdapter(getContext(),
                mContentsData, ContentsAdapter.ActivityTypeItem.TYPE_RECORDED_LIST);
        mRecordedListview.setAdapter(mContentsAdapter);

        return mRecordedFragmentView;
    }

    // TODO 後で消す. テスト用メソッド
    public void setDeta() {
        mContentsData = new ArrayList<>();
        ContentsData contentsData;

        for (int i = 0; i < 100; i++) {
            contentsData = new ContentsData();
            contentsData.setRank(String.valueOf(i + 1));
            contentsData.setTitle("大晦日だよ! ドラえもんのび太の宇宙漂流記　シーズン" + i);
            contentsData.setRecordedChannelName("FOX HD");
            contentsData.setTime("8/31 (木) 21：00");
            mContentsData.add(contentsData);
        }
    }

    /**
     * リスト全体を更新する
     * @param count
     */
    public void notifyDataSetChanged(String count) {
        if (null != mContentsAdapter) {
            mContentsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * カーソルを指定の位置する
     * @param itemNo
     */
    public void setSelection(int itemNo) {
        if (null != mRecordedListview) {
            mRecordedListview.setSelection(itemNo);
        }
    }

    /**
     * リストの最後に更新中の行を追加、または追加した行の削除
     * @param bool
     */
    public void displayLoadMore(boolean bool) {
        if (null != mRecordedListview && null != mLoadMoreView) {
            if (bool) {
                mRecordedListview.addFooterView(mLoadMoreView);
            } else {
                mRecordedListview.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * mContentsDataのすべての要素を削除する
     */
    public void clear() {
        mContentsData.clear();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mRecordedBaseFragmentScrollListener) {
            mRecordedBaseFragmentScrollListener.onScroll(this, absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO コンテンツタップで再生画面(縦)へ
    }
}