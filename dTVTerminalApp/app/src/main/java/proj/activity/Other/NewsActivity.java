package proj.activity.Other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class NewsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_layout);
        TextView tv_toNewsDetail1 = (TextView) findViewById(R.id.tv_news1);
        tv_toNewsDetail1.setClickable(true);
        tv_toNewsDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_toNewsDetail = new Intent(NewsActivity.this, NewsDetailActivity.class);
                startActivity(it_toNewsDetail);
            }
        });
        TextView tv_toNewsDetail2 = (TextView) findViewById(R.id.tv_news2);
        tv_toNewsDetail2.setClickable(true);
        tv_toNewsDetail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_toDetail = new Intent(NewsActivity.this, NewsDetailActivity.class);
                startActivity(it_toDetail);
            }
        });

    }
}
