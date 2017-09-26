package proj.activity.Home;

/**
 * Created by ryuhan on 2017/09/22.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import proj.activity.Other.NewsActivity;
import proj.activity.Other.SettingActivity;
import proj.activity.Player.ChannelDetailPlayerActivity;
import proj.activity.Ranking.DailyTvRankingActivity;
import proj.activity.Ranking.RankingTopActivity;
import proj.activity.Ranking.VideoRankingActivity;
import proj.activity.Search.SearchTopActivity;
import proj.activity.TvProgram.ChannelListActivity;
import proj.activity.TvProgram.TvProgramListActivity;
import proj.activity.Video.VideoPurchListActivity;
import proj.activity.Video.VideoTopActivity;
import proj.common.BaseActivity;
import proj.dtvterminalapp.R;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView mChanellistRecyclerView;
    private TextView channelTextView;
    private TextView recommendTextView;
    private TextView recommendVideoTextView;
    private TextView rankTextView;
    private TextView videoRankTextView;
    private TextView clipTextView;
    private List<Integer> mList;

    private ActionBarDrawerToggle toggle=null;
    private DrawerLayout drawerlayout=null;
    private NavigationView navigationview=null;
    private Toolbar toolbar=null;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_layout);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_main_chanellist:
                startActivity(ChannelListActivity.class,null);
                break;
            case R.id.home_main_recommendlist:

            case R.id.home_main_recommendvideolist:
                startActivity(RecommendActivity.class,null);
                break;
            case R.id.home_main_ranklist:
                startActivity(DailyTvRankingActivity.class,null);
                break;
            case R.id.home_main_videoranklist:
                startActivity(VideoRankingActivity.class,null);
                break;
            case R.id.home_main_cliplist:
                startActivity(ClipListActivity.class,null);
                break;
        }

    }

    protected void initPopupWindow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop, null);
        popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //if(Location.LEFT.ordinal() == from){
        //    popupWindow.setAnimationStyle(R.style.AnimationLeftFade);
        //}else if(Location.RIGHT.ordinal() == from){
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        //}else if(Location.BOTTOM.ordinal() == from){
        //    popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);

        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.home_main_layout, null), Gravity.RIGHT, 0, 0);
        popupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindowView.findViewById(R.id.pop_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ChannelDetailPlayerActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity();
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_banngumi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TvProgramListActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_chanellist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ChannelListActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_recordbanngumi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RecordedListActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_susenbanngumi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RecommendActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_rank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RankingTopActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_clip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ClipListActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_purchased).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(VideoPurchListActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_yoyaku).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RecordReservationListActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(VideoTopActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_keyword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SearchTopActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NewsActivity.class,null);
                popupWindow.dismiss();
            }
        });
        popupWindowView.findViewById(R.id.pop_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.class,null);
                popupWindow.dismiss();
            }
        });



    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    private void initView(){
        channelTextView = findViewById(R.id.home_main_chanellist);
        recommendTextView = findViewById(R.id.home_main_recommendlist);
        recommendVideoTextView = findViewById(R.id.home_main_recommendvideolist);
        rankTextView = findViewById(R.id.home_main_ranklist);
        videoRankTextView = findViewById(R.id.home_main_videoranklist);
        clipTextView = findViewById(R.id.home_main_cliplist);
        channelTextView.setOnClickListener(this);
        recommendTextView.setOnClickListener(this);
        recommendVideoTextView.setOnClickListener(this);
        rankTextView.setOnClickListener(this);
        videoRankTextView.setOnClickListener(this);
        clipTextView.setOnClickListener(this);
        mChanellistRecyclerView = findViewById(R.id.home_main_chanellist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mChanellistRecyclerView.setLayoutManager(linearLayoutManager);
        initData();
        HorizontalViewAdapter mHorizontalViewAdapter = new HorizontalViewAdapter(this,mList);
        mChanellistRecyclerView.setAdapter(mHorizontalViewAdapter);

        TextView rTextView =  findViewById(R.id.home_main_layout_r);
        rTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow();
            }
        });
        findViewById(R.id.home_main_layout_prrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.google.co.jp/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        /*drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationview = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.rgb(255, 0, 0));
        navigationview.getChildAt(0).setVerticalScrollBarEnabled(false);
        setDrawerToggle();
        setListener();
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData(){
        mList = new ArrayList<>(Arrays.asList(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
    }

    /*private void setDrawerToggle() {
        // left --> right
        //toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, 0, 0);

        *//* right --> left *//*
        toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, 0, 0){

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                if (id == R.id.navigation) {
                    if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
                        drawerlayout.closeDrawer(GravityCompat.START);
                    }
                    drawerlayout.openDrawer(GravityCompat.END);
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }
        };


        drawerlayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    private void setListener() {
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.single_11:
                        System.out.print("test");
                        startActivity(TvProgramListActivity.class,null);
                        break;
                    case R.id.single_21:
                        break;
                    case R.id.single_31:
                        finish();
                        break;
                }
                drawerlayout.closeDrawer(GravityCompat.START);
                //drawerlayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }*/

    public class HorizontalViewAdapter extends RecyclerView.Adapter<HorizontalViewAdapter.ViewHolder>
    {
        private LayoutInflater mInflater;
        private List<Integer> mDatas;

        public HorizontalViewAdapter(Context context, List<Integer> datats)
        {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View view = mInflater.inflate(R.layout.home_main_layout_item_demo,viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.mImg = view.findViewById(R.id.home_main_layout_item_iv);
            viewHolder.mTxt = view.findViewById(R.id.home_main_layout_item_tv);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i)
        {
            viewHolder.mImg.setImageResource(mDatas.get(i));
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(VideoRankingActivity.class,null);
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View arg0)
            {
                super(arg0);
            }

            ImageView mImg;
            TextView mTxt;
        }
    }
}
