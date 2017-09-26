package proj.activity.Other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_toSettingDetail;
    private Button bt_back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_layout);
        tv_toSettingDetail = (TextView) findViewById(R.id.tv_setting);
        bt_back1 = (Button) findViewById(R.id.Back1);
        tv_toSettingDetail.setClickable(true);
        bt_back1.setOnClickListener(this);
        tv_toSettingDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Back1:
                finish();
                break;
            case R.id.tv_setting:
                startActivity(SettingDetailActivity.class,null);
            default:
                break;
        }
    }
}
