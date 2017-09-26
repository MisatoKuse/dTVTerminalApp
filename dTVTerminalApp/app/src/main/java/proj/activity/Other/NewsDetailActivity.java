package proj.activity.Other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_back4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details_main_layout);
        bt_back4 = (Button) findViewById(R.id.Back4);
        bt_back4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Back4:
                finish();
                break;
            default:
                break;
        }
    }
}
