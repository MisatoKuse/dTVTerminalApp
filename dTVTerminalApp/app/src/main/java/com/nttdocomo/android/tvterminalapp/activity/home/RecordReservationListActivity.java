/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
// TODO スクロールリスナー設定, DPコールバック設定
public class RecordReservationListActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout mLinearLayout;
    private ImageView mMenuImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_reservation_list_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        setTitleText(getString(R.string.rental_title));

        initView();
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    private void initView(){
        // TODO ContentsListAdapter設定
    }

    // TODO スクロール処理(ページング)
//    public void onScroll() {
//
//    }
//
//    public void onScrollStateChanged() {
//
//    }
}