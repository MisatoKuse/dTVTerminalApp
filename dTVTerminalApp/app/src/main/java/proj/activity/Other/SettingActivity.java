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

public class SettingActivity extends BaseActivity {
   private  TextView tv_toSettingDetail;
    private Button bt_back1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_layout);
        tv_toSettingDetail = (TextView) findViewById(R.id.tv_setting);
        bt_back1= (Button) findViewById(R.id.Back1);
        bt_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        tv_toSettingDetail.setClickable(true);
        tv_toSettingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_toSettingDetail = new Intent(SettingActivity.this,SettingDetailActivity.class);
                startActivity(it_toSettingDetail);
            }
        });

    }
}
