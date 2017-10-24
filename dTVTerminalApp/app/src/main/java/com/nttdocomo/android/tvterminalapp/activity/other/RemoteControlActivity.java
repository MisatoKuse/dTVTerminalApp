/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nttdocomo.android.tvterminalapp.R;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.RemoteControlDataProvider;

public class RemoteControlActivity extends BaseActivity {
    private RemoteControlDataProvider mRemoteControlDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_control_main_layout);

        final Button button;
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_up));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_down));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_left));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_right));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_home));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_back));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_1));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_2));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_3));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_dpad_center));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_channel_up));
        setButtonOnClickListener((Button)findViewById(R.id.keycode_channel_down));

        mRemoteControlDataProvider = new RemoteControlDataProvider(this);

    }

    private void setButtonOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                mRemoteControlDataProvider.sendKeycode(v.getId());
            }
        });
    }

}
