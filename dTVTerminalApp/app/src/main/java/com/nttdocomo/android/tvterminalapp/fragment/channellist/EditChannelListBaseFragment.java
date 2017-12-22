/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;

public abstract class EditChannelListBaseFragment extends Fragment {

    //Fragmentのviewが作成完了
    private boolean isViewCreated;

    //Fragmentが見られる
    private boolean isUIVisible;
    public MyChannelEditActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = (MyChannelEditActivity) getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        if (isViewCreated && isUIVisible) {
            loadData();
            //falseに戻す多数loadしないよう
            /*isViewCreated = false;
            isUIVisible = false;*/
        }
    }

    protected abstract void loadData();
}
