package com.nttdocomo.android.tvterminalapp.activity.Video;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by hitue-fsi on 2017/09/22.
 */

import com.nttdocomo.android.tvterminalapp.activity.Player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class VideoContentListActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_content_list_main_layout);
        initView();
    }

    private void initView(){
        mRelativeLayout = findViewById(R.id.video_content_main_layout_ll1);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TvPlayerActivity.class,null);
            }
        });
    }
}
