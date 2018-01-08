/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;


import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class STBSelectErrorActivity extends BaseActivity {
    private CheckBox mCheckBox;
    private TextView mParingAgain, mParingHelp, mWithoutParing;
    private boolean mIsNextTimeHide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stb_select_error_layout);
        setContents();
    }

    /**
     * 画面上に表示するコンテンツを設定する
     */
    private void setContents() {
        setTitleText(getString(R.string.str_stb_select_error));
        mParingAgain = findViewById(R.id.stb_search_failed_paring_again);
        mParingHelp = findViewById(R.id.stb_paring_failed_help);
        findViewById(R.id.header_layout_back).setVisibility(View.GONE);
        mWithoutParing = findViewById(R.id.use_without_paring);
        mParingHelp.setOnClickListener(this);
        mParingAgain.setOnClickListener(this);
        mWithoutParing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mParingAgain)) {
            finish();
        } else if (v.equals(mParingHelp)) {
            startActivity(ParingHelpActivity.class, null);
        } else if (v.equals(mCheckBox)) {
            mIsNextTimeHide = mCheckBox.isChecked();
        } else if (v.equals(mWithoutParing)) {
            SharedPreferencesUtils.setSharedPreferencesStbSelect(this, mIsNextTimeHide);
            startActivity(HomeActivity.class, null);
        }
    }
}
