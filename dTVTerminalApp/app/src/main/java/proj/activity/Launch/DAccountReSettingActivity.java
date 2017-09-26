package proj.activity.Launch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import proj.activity.Temp.DAccountAppliActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

/**
 * Created by ryuhan on 2017/09/22.
 */

public class DAccountReSettingActivity extends BaseActivity implements View.OnClickListener {

    private Button mOtherDAccountDAccounReSettingActivity=null;
    private Button mDAccountRegDAccountReSettingActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_resetting_main_layout);

        setContents();

    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.str_d_account_resetting_title);
    }

    private void setContents() {
        TextView title= (TextView)findViewById(R.id.titleDAccountReSettingActivity);
        title.setText(getScreenTitle());

        mOtherDAccountDAccounReSettingActivity=(Button)findViewById(R.id.otherDAccountDAccounReSettingActivity);
        mOtherDAccountDAccounReSettingActivity.setOnClickListener(this);

        mDAccountRegDAccountReSettingActivity=(Button)findViewById(R.id.dAccountRegDAccountReSettingActivity);
        mDAccountRegDAccountReSettingActivity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.equals(mOtherDAccountDAccounReSettingActivity)){
            onOtherDAccountButton();
        } else if(v.equals(mDAccountRegDAccountReSettingActivity)){
            onDAccountRegButton();
        }
    }

    /**
     * 別のdアカウントでログイン
     */
    private void onOtherDAccountButton() {
        startActivity(DAccountAppliActivity.class, null);
    }

    /**
     * dアカウント設定ヘルプ
     */
    private void onDAccountRegButton() {
        startActivity(DAccountSettingHelpActivity.class, null);
    }

}
