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

public class DAccountSettingHelpActivity extends BaseActivity implements View.OnClickListener {

    private Button mActivationCodeInputDAccounHelpActivity=null;
    private Button mRepairDAccounHelpActivity=null;  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_help_main_layout);

        setContents();
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
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mActivationCodeInputDAccounHelpActivity)){
            onActivationButton();
        } else if(v.equals(mRepairDAccounHelpActivity)){
            onRepairButton();
        }
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