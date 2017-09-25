package proj.activity.Launch;

import android.os.Bundle;

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

/**
 * Created by ryuhan on 2017/09/22.
 */

public class BaseAccountSettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daccount_setting_main_layout);
    }
}