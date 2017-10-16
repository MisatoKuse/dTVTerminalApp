/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.Other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_back4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details_main_layout);
        bt_back4 = (Button) findViewById(R.id.Back4);
        bt_back4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Back4:
                finish();
                break;
            default:
                break;
        }
    }
}
