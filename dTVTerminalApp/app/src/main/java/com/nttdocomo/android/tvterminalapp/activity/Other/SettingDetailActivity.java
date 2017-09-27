package com.nttdocomo.android.tvterminalapp.activity.Other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import com.nttdocomo.activity.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class SettingDetailActivity extends BaseActivity {
    private Button bt_back2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_detail_main_layout);
        bt_back2= (Button) findViewById(R.id.back2);
        bt_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingDetailActivity.this.finish();
            }
        });
    }
}
