/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;


import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class STBSelectErrorActivity extends BaseActivity {
    private CheckBox mErrorCheckBox;
    private TextView mParingAgain;
    private TextView  mParingHelp;
    private TextView mWithoutParing;
    private TextView mErrorCheckboxText;
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
        setTitleText(getString(R.string.str_app_title));
        mParingAgain = findViewById(R.id.stb_search_failed_paring_again);
        mParingHelp = findViewById(R.id.stb_paring_failed_help);
        mErrorCheckBox = findViewById(R.id.stb_select_error_checkBox);
        mErrorCheckboxText = findViewById(R.id.launch_select_error_check_box_text);
        findViewById(R.id.header_layout_back).setVisibility(View.GONE);
        mWithoutParing = findViewById(R.id.use_without_paring);
        mParingHelp.setOnClickListener(this);
        mParingAgain.setOnClickListener(this);
        mWithoutParing.setOnClickListener(this);
        mErrorCheckBox.setOnClickListener(this);
        mErrorCheckboxText.setOnClickListener(this);

        mErrorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mErrorCheckBox.isChecked()){
                    mErrorCheckBox.setBackgroundResource(R.drawable.
                            ic_check_box_white_24dp);
                }else {
                    mErrorCheckBox.setBackgroundResource(R.drawable.
                            ic_check_box_outline_blank_white_24dp);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mParingAgain)) {
            finish();
        } else if (v.equals(mParingHelp)) {
            startActivity(PairingHelpActivity.class, null);
        } else if (v.equals(mErrorCheckBox)) {
            mIsNextTimeHide = mErrorCheckBox.isChecked();
        } else if (v.equals(mWithoutParing)) {
            SharedPreferencesUtils.setSharedPreferencesStbSelect(this, mIsNextTimeHide);
            startActivity(HomeActivity.class, null);
        }else if(v.equals(mErrorCheckboxText)){
            if(mErrorCheckBox.isChecked()){
                mErrorCheckBox.setChecked(false);
            }else if(!mErrorCheckBox.isChecked()){
                mErrorCheckBox.setChecked(true);
            }
        }
    }
}