package proj.activity.Search;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SearchHistoryListActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_backTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history_list_main_layout);
        bt_backTop = (Button) findViewById(R.id.bt_backTop);
        bt_backTop.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_backTop:
                finish();
                break;
            default:
                break;
        }
    }
}
