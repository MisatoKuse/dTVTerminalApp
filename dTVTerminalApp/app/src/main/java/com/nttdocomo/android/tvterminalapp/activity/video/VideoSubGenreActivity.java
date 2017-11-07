/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;

public class VideoSubGenreActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_sub_genre_main_layout);

        Intent intent = getIntent();
        String genreId = intent.getStringExtra("genre_id");

        init();
    }

    private void init(){

    }

    @Override
    public void onClick(View view) {
//        Intent intent = new Intent(this,VideoContentListActivity.class);
//        intent.putExtra("genre_id", genre_id);
//        startActivity(intent);
        switch (view.getId()){
            case R.id.video_sub_genre_main_layout_rl1:
            case R.id.video_sub_genre_main_layout_rl2:
            case R.id.video_sub_genre_main_layout_rl3:
            case R.id.video_sub_genre_main_layout_rl4:
                startActivity(VideoContentListActivity.class,null);
                break;
            default:
                break;
        }
    }
}
