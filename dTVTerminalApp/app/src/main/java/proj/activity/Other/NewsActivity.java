package proj.activity.Other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;



import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class NewsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_toNewsDetail1;
    private TextView tv_toNewsDetail2;
    private Button bt_back3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_layout);
        tv_toNewsDetail1 = (TextView) findViewById(R.id.tv_news1);
        tv_toNewsDetail2 = (TextView) findViewById(R.id.tv_news2);
        bt_back3=(Button)findViewById(R.id.Back3);
        tv_toNewsDetail1.setClickable(true);
        tv_toNewsDetail2.setClickable(true);
        tv_toNewsDetail1.setOnClickListener(this);
        tv_toNewsDetail2.setOnClickListener(this);
        bt_back3.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
     switch(view.getId()){
         case R.id.Back3:
             finish();
             break;
         case R.id.tv_news1:
             startActivity(NewsDetailActivity.class,null);
             break;
         case R.id.tv_news2:
             startActivity(NewsDetailActivity.class,null);
             break;
         default:
             break;
     }

        }
    }


