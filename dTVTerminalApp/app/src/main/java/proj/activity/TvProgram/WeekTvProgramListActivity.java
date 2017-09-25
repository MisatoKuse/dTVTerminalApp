package proj.activity.TvProgram;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.os.Bundle;

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class WeekTvProgramListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_tv_program_list_main_layout);
    }
}