package com.nttdocomo.android.tvterminalapp.activity.Launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.Temp.DAccountAppliActivity;
import com.nttdocomo.android.tvterminalapp.activity.common.BaseActivity;

/**
 * Created by ryuhan on 2017/09/22.
 */

public class DAccountSettingHelpActivity extends BaseActivity implements View.OnClickListener {

    private Button mActivationCodeInputDAccounHelpActivity=null;
    private Button mRepairDAccounHelpActivity=null;
    private Button mReturnDAccounHelpActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_help_main_layout);

        setContents();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_d_account_setting_help_title);
    }

    private void setContents() {

        TextView title=(TextView)findViewById(R.id.titleDAccounHelpActivity);
        title.setText(getScreenTitle());

        mActivationCodeInputDAccounHelpActivity=(Button)findViewById(R.id.activationCodeInputDAccounHelpActivity);
        mActivationCodeInputDAccounHelpActivity.setOnClickListener(this);

        mRepairDAccounHelpActivity=(Button)findViewById(R.id.repairDAccounHelpActivity);
        mRepairDAccounHelpActivity.setOnClickListener(this);

        mReturnDAccounHelpActivity=(Button)findViewById(R.id.returnDAccounHelpActivity);
        mReturnDAccounHelpActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mActivationCodeInputDAccounHelpActivity)){
            onActivationButton();
        } else if(v.equals(mRepairDAccounHelpActivity)){
            onRepairButton();
        } else if(v.equals(mReturnDAccounHelpActivity)){
            onReturnButton();
        }
    }

    private void onReturnButton() {
        startActivity(DAccountReSettingActivity.class, null);
    }

    /**
     * アクティベーションコードを入力する
     */
    private void onActivationButton() {
        startActivity(DAccountAppliActivity.class, null);
    }

    /**
     * dアカウント登録状態チェック
     */
    private void onRepairButton() {
        //
        Bundle b=new Bundle();
        b.putString("state", STBSelectActivity.StateModeRepair);
        startActivity(STBSelectActivity.class, b);
    }


}