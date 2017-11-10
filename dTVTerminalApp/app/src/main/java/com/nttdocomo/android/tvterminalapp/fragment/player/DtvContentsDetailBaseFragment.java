/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class DtvContentsDetailBaseFragment extends Fragment {

    public Context mActivity;
    public List mContentsDetailData = null;

    private View mContentsDetailFragmentView = null;
    private OtherContentsDetailData mOtherContentsDetailData;
    private TextView mTxtTitleShortDetail;
    private TextView mTxtTitleAllDetail;
    private TextView mTxtMoreText;
    private String mContentsDetailInfo;
    private boolean mIsAllText = false;

    public DtvContentsDetailBaseFragment() {
        if (mContentsDetailData == null) {
            mContentsDetailData = new ArrayList();
        }
    }

    @Override
    public Context getContext() {
        this.mActivity = getActivity();
        return mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //コンテンツ詳細表示に必要なデータを取得する
        mOtherContentsDetailData =
                getArguments().getParcelable(DtvContentsDetailActivity.DTV_INFO_BUNDLE_KEY);
        return initView();
    }

    /**
     * 各タブ画面は別々に実現して表示されること
     */
    private View initView() {
        DTVTLogger.start();
        if (mContentsDetailData == null) {
            mContentsDetailData = new ArrayList();
        }
        if (null == mContentsDetailFragmentView) {
            mContentsDetailFragmentView = View.inflate(getActivity()
                    , R.layout.dtv_contents_detail_fragment, null);

        }

        //画面表示
        StringUtil util = new StringUtil(getActivity());
        TextView txtTitleText = mContentsDetailFragmentView.findViewById(R.id.contents_title);
        TextView txtServiceName = mContentsDetailFragmentView.findViewById(R.id.service_provider);
        mTxtTitleShortDetail = mContentsDetailFragmentView.findViewById(R.id.contents_detail_info);
        mTxtTitleAllDetail = mContentsDetailFragmentView.findViewById(R.id.contents_detail_all_info);
        mTxtMoreText = mContentsDetailFragmentView.findViewById(R.id.more_button);

        mTxtMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //moreボタン押下で、全文表示に切り替える
                mIsAllText = true;
                mTxtTitleShortDetail.setVisibility(View.GONE);
                mTxtTitleAllDetail.setVisibility(View.VISIBLE);
                mTxtMoreText.setVisibility(View.GONE);
            }
        });

        txtTitleText.setText(mOtherContentsDetailData.getTitle());
        String strServiceName = util.getContentsServiceName(mOtherContentsDetailData.getServiceId());
        txtServiceName.setText(strServiceName);
        mContentsDetailInfo = mOtherContentsDetailData.getDetail();
        mTxtTitleShortDetail.setText(mContentsDetailInfo);
        mTxtTitleAllDetail.setText(mContentsDetailInfo);
        return mContentsDetailFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTxtTitleAllDetail.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                final int DETAIL_INFO_TEXT_MAX_LINE = 4;
                Layout layout = mTxtTitleAllDetail.getLayout();

                if (layout != null) {
                    //コンテンツ情報の文字数が省略されているときは、省略文とmoreを表示する
                    int intTextViewCount = layout.getLineCount();
                    if (intTextViewCount > DETAIL_INFO_TEXT_MAX_LINE && !mIsAllText) {
                        mTxtTitleShortDetail.setVisibility(View.VISIBLE);
                        mTxtTitleAllDetail.setVisibility(View.GONE);
                        mTxtMoreText.setVisibility(View.VISIBLE);
                    } else {
                        //コンテンツ情報の文字数が省略されていないときは、全文のみを表示する
                        mTxtTitleShortDetail.setVisibility(View.GONE);
                        mTxtTitleAllDetail.setVisibility(View.VISIBLE);
                        mTxtMoreText.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
    }

    /**
     * ページの判定処理
     */
    public void initContentsDetailView() {
        //TODO：複数ページがあるときはページごとの初期化処理を行う
    }

    /**
     * Adapterをリフレッシュする
     */
    public void noticeRefresh() {
        //TODO：データ取得後の処理を記載する
    }
}