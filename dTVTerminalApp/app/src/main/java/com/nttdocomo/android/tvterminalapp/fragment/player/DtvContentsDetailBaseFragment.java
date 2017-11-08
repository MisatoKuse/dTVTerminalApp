/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;
import java.util.List;


public class DtvContentsDetailBaseFragment extends Fragment {

    public Context mActivity;
    public List mContentsDetailData = null;

    private View mContentsDetailFragmentView = null;

    public DtvContentsDetailBaseFragment() {
        if (mContentsDetailData == null) {
            mContentsDetailData = new ArrayList();
        }
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 各タブ画面は別々に実現して表示されること
     */
    private View initView() {
        DTVTLogger.start();
        if (mContentsDetailData == null) {
            mContentsDetailData = new ArrayList();
        }
        if (null == mContentsDetailFragmentView) {
            mContentsDetailFragmentView = View.inflate(getActivity()
                    , R.layout.dtv_contents_detail_fragment, null);

        }
        return mContentsDetailFragmentView;
    }

    /**
     * ページの判定処理
     */
    public void initContentsDetailView() {
        //TODO：複数ページがあるときはページごとの初期化処理を行う
    }

    /**
     * Adapterをリフレッシュする
     */
    public void noticeRefresh() {
        //TODO：データ取得後の処理を記載する
    }
}