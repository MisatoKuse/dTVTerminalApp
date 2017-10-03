package com.nttdocomo.android.tvterminalapp.activity.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.nttdocomo.android.tvterminalapp.activity.Home.adapter.RecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.activity.Ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.Ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.TvProgram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.activity.Video.VideoPurchListActivity;
import com.nttdocomo.android.tvterminalapp.beans.HomeBean;
import com.nttdocomo.android.tvterminalapp.beans.HomeBeanContent;
import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.menudisplay.MenuItemParam;

public class HomeActivity extends BaseActivity{

    private  String urls1[] = {
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg"};

    private  String urls2[] = {
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            };

    private  String urls3[] = {
            "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg"};

    private  String urls4[] = {
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",

            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg"};

    private  String urls5[] = {
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg"};

    private  String urls6[] = {
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            };

    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_layout);
        initView();
    }

    private void initData(){
        HomeBean mHomeBean = new HomeBean();
        String contents[] = {"NOW ON AIR","おすすめ番組","おすすめビデオ",
                "今日のテレビランキング","ビデオランキング","クリップ"};
        String contents2[] = {"チャンネルリスト >","24 >","30 >",
                "8 >","10 >", "20 >"};
        for(int i=0;i<contents.length;i++){
            List<HomeBeanContent> mList = new ArrayList<>();
            String []url = new String[]{};
            switch (i){
                case 0:
                    url = urls1;
                break;
                case 1:
                    url = urls2;
                    break;
                case 2:
                    url = urls3;
                    break;
                case 3:
                    url = urls4;
                    break;
                case 4:
                    url = urls5;
                    break;
                case 5:
                    url = urls6;
                    break;
            }

            for (int j=0;j<10;j++){
                HomeBeanContent mHomeBeanContent = new HomeBeanContent();
                mHomeBeanContent.setContentName("test"+i+j);
                mHomeBeanContent.setContentTime("");
                mHomeBeanContent.setContentSrcURL(url[j]);
                mList.add(mHomeBeanContent);
            }
            mHomeBean.setContentTypeName(contents[i]);
            mHomeBean.setContentCount(contents2[i]);
            mHomeBean.setContentList(mList);
            initRecyclerView(mHomeBean, i);
        }
    }

    private void initView(){
        mLinearLayout = findViewById(R.id.home_main_layout_linearLayout);
        TextView mTextView = findViewById(R.id.home_main_layout_title_menu);
        findViewById(R.id.home_main_layout_kytv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.hikaritv.net/video");
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        findViewById(R.id.home_main_layout_prrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.hikaritv.net/video");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSampleGlobalMenuButton_PairLoginOk();
            }
        });
        initData();
    }

    private void initRecyclerView(HomeBean mHomeBean, final int index){
        String typeContentName = mHomeBean.getContentTypeName();
        String resultCount = mHomeBean.getContentCount();
        View view = LayoutInflater.from(this).inflate(R.layout.home_main_layout_item, null, false);
        TextView typeTextView = view.findViewById(R.id.home_main_item_type_tx);
        TextView countTextView = view.findViewById(R.id.home_main_item_type_tx_count);
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (index){
                    case 0:
                        startActivity(ChannelListActivity.class,null);
                        break;
                    case 1:
                    case 2:
                        startActivity(RecommendActivity.class,null);
                        break;
                    case 3:
                        startActivity(DailyTvRankingActivity.class,null);
                        break;
                    case 4:
                        startActivity(VideoRankingActivity.class,null);
                        break;
                    case 5:
                        startActivity(ClipListActivity.class,null);
                        break;
                }

            }
        });
        mRecyclerView = view.findViewById(R.id.home_main_item_recyclerview);
        typeTextView.setText(typeContentName);
        countTextView.setText(resultCount);
        mLinearLayout.addView(view);
        setRecyclerViewData(mRecyclerView, mHomeBean.getContentList(), index);
    }

    private void setRecyclerViewData(RecyclerView mRecyclerView, List<HomeBeanContent> mList,final int index){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter mHorizontalViewAdapter = new RecyclerViewAdapter(this, mList, index);
        mRecyclerView.setAdapter(mHorizontalViewAdapter);
        View footer = LayoutInflater.from(this).inflate(R.layout.home_main_layout_recyclerview_footer, mRecyclerView, false);
        TextView mTextView = footer.findViewById(R.id.home_main_layout_recyclerview_footer);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (index){
                    case 0:
                        startActivity(ChannelListActivity.class,null);
                        break;
                    case 1:
                    case 2:
                        startActivity(RecommendActivity.class,null);
                        break;
                    case 3:
                        startActivity(DailyTvRankingActivity.class,null);
                        break;
                    case 4:
                        startActivity(VideoRankingActivity.class,null);
                        break;
                    case 5:
                        startActivity(ClipListActivity.class,null);
                        break;
                }
            }
        });
        mHorizontalViewAdapter.setFooterView(footer);
    }

    //契約・ペアリング済み用
    private void onSampleGlobalMenuButton_PairLoginOk() {
        MenuItemParam param = new MenuItemParam();
        param.setParamForContractOkPairingOk(3, 1, 2, 6, 8);
        setUserState(param);
        displayMenu();
    }

}
