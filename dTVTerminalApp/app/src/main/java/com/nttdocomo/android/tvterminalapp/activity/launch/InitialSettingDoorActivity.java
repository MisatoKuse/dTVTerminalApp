/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;


public class InitialSettingDoorActivity extends BaseActivity {

    private TextView mSetLaterTextView;
    private TextView mParingSetTextView;

    private boolean mIsShowBackIcon = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText(getString(R.string.str_stb_paring_setting_title));

        Intent intent = getIntent();


        if (intent != null) {
            mIsShowBackIcon = intent.getBooleanExtra("LAUNCH_STB_BACK_KEY", false);
        }

        if (mIsShowBackIcon) {

            enableHeaderBackIcon(true);
            findViewById(R.id.header_layout_back).setOnClickListener(this);

        } else {
            enableHeaderBackIcon(false);
        }
        setContentView(R.layout.launch_initial_setting_door_layout);

        setContents();
    }

    private void setContents() {
        mSetLaterTextView = findViewById(R.id.launch_to_set_later_text);
        mParingSetTextView = findViewById(R.id.launch_initial_setting_paring_setting);
        mSetLaterTextView.setPaintFlags(mSetLaterTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mSetLaterTextView.setOnClickListener(this);
        mParingSetTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
        if (mSetLaterTextView.equals(view)) {
            if (mIsShowBackIcon) {
                finish();
            } else {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (mParingSetTextView.equals(view)) {
            Intent intent = new Intent(this, StbSelectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(StbSelectActivity.FROM_WHERE, 0);
            startActivity(intent);
        } else if (view.getId() == R.id.header_layout_back) {
            super.onClick(view);
        }
    }
}
