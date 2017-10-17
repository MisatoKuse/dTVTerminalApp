/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Home;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.activity.Player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class RecordReservationListActivity extends BaseActivity {

    private RelativeLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_reservation_list_main_layout);

        initView();
    }

    private void initView(){
        mLinearLayout = findViewById(R.id.record_reservation_list_main_layout_ll1);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TvPlayerActivity.class,null);
            }
        });
    }
}