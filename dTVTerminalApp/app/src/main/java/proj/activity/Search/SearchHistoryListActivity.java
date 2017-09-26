package proj.activity.Search;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class SearchHistoryListActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_backTop;
    private TextView tv_toResult1;
    private TextView tv_toResult2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history_list_main_layout);
        bt_backTop = (Button) findViewById(R.id.bt_backTop);
        tv_toResult1=(TextView)findViewById(R.id.tv_toResult1);
        tv_toResult2=(TextView)findViewById(R.id.tv_toResult2);
        bt_backTop.setOnClickListener(this);
        tv_toResult1.setOnClickListener(this);
        tv_toResult2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_backTop:
                finish();
                break;
            case R.id.tv_toResult1:
                startActivity(SearchResultActivity.class,null);
                break;
            case R.id.tv_toResult2:
                startActivity(SearchResultActivity.class,null);
                break;
            default:
                break;
        }
    }
}
