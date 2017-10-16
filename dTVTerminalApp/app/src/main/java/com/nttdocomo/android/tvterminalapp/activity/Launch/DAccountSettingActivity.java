package com.nttdocomo.android.tvterminalapp.activity.Launch;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.Home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.Temp.GoogleStoreActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.BaseActivity;

/**
 * Created by ryuhan on 2017/09/22.
 */

public class DAccountSettingActivity extends BaseActivity implements View.OnClickListener {

    private Button mDAccountDownloadDAccountSettingActivity=null;
    private Button mDLoginNoUseDAccountSettingActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_setting_main_layout);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setContents() {
        TextView title=(TextView)findViewById(R.id.titleDAccountSettingActivity);
        title.setText(getScreenTitle());

        mDAccountDownloadDAccountSettingActivity=(Button)findViewById(R.id.dAccountDownloadDAccountSettingActivity);
        mDAccountDownloadDAccountSettingActivity.setOnClickListener(this);

        mDLoginNoUseDAccountSettingActivity=(Button)findViewById(R.id.dLoginNoUseDAccountSettingActivity);
        mDLoginNoUseDAccountSettingActivity.setOnClickListener(this);
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_d_account_setting_title);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mDLoginNoUseDAccountSettingActivity)) {
            mDLoginNoUseButton();
        } else if(v.equals(mDAccountDownloadDAccountSettingActivity)) {
            onDAccountDownloadButton();
        }

    }

    private void onDAccountDownloadButton() {
        startActivity(GoogleStoreActivity.class, null);
    }

    private void mDLoginNoUseButton() {
        Bundle b=new Bundle();
        b.putString("state", LaunchActivity.mStateToHomePairingNg);
        startActivity(HomeActivity.class, b);
    }
}