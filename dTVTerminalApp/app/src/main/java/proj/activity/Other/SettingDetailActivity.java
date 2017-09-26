package proj.activity.Other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

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
