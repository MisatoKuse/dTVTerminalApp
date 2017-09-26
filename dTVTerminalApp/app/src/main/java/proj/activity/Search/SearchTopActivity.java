package proj.activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SearchTopActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_toResult;
    private Button bt_toHistory;
    private Button bt_backHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_top_main_layout);
        bt_toResult = (Button) findViewById(R.id.bt_keyword);
        bt_toHistory = (Button) findViewById(R.id.bt_seeMore);
        bt_backHome = (Button) findViewById(R.id.bt_backHome);
        bt_toResult.setOnClickListener(this);
        bt_toHistory.setOnClickListener(this);
        bt_backHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_keyword:
                startActivity(SearchResultActivity.class,null);
                break;
            case R.id.bt_seeMore:
                startActivity(SearchHistoryListActivity.class,null);
                break;
            case R.id.bt_backHome:
                finish();
                break;
            default:
                break;
        }
    }
}


