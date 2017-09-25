package proj.activity.Home;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.os.Bundle;

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_layout);
    }
}
