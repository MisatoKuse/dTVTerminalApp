/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ChildContentDataProvider;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.List;

/**
 * Created by moon_jaesung on 2018/04/05.
 */

public class ChildContentListActivity extends BaseActivity implements ChildContentDataProvider.DataCallback {

    // region variable

    // view

    //data
    private ChildContentDataProvider mChildContentDataProvider;

    public static final String INTENT_KEY_CRID = "crid",
            INTENT_KEY_TITLE = "title",
            INTENT_KEY_DISP_TYPE = "dispType";


    private String mCrid;
    private String mTitle;
    private String mDispType;
    private int mOffset = 1;
    // endregion variable

    // region Activity LifeCycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentsView(R.layout.rental_list_main_layout);

        Intent intent = getIntent();

        mCrid = intent.getStringExtra(INTENT_KEY_CRID);
        mTitle = intent.getStringExtra(INTENT_KEY_TITLE);
        mDispType = intent.getStringExtra(INTENT_KEY_DISP_TYPE);

        DTVTLogger.debug("mCrid = " + mCrid + ", mTitle = " + mTitle);
        setTitleText(mTitle);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
    }
    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
    }
    // endregion Activity LifeCycle

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        if (mChildContentDataProvider != null) {
            mChildContentDataProvider.enableConnect();
        }

        //コンテンツ情報が無ければ取得を行う
        mChildContentDataProvider = new ChildContentDataProvider(this);
        mChildContentDataProvider.getChildContentList(mCrid, mOffset, mDispType);
    }

    @Override
    public void childContentListCallback(@Nullable List<ContentsData> list) {
        DTVTLogger.warning("list.size() = " + list.size());
    }

}
