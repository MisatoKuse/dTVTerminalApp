/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;

import java.util.ArrayList;

/**
 * 文字列加工に関する処理を記載する
 */
public class StringUtil {

    private Context mContext;

    public StringUtil(Context context) {
        mContext = context;
    }

    /**
     * サービスID(dTV関連)に応じたサービス名を返す
     *
     * @param id サービスID
     * @return サービス名
     */
    public String getContentsServiceName(int id) {
        switch (id) {
            case DtvContentsDetailActivity.DTV_CONTENTS_SERVICE_ID:
                return mContext.getString(R.string.dtv_contents_service_name);
            case DtvContentsDetailActivity.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                return mContext.getString(R.string.dtv_channel_contents_service_name);
            case DtvContentsDetailActivity.D_ANIMATION_CONTENTS_SERVICE_ID:
                return mContext.getString(R.string.d_animation_contents_service_name);
        }
        return mContext.getString(R.string.hikari_tv_contents_service_name);
    }

    /**
     * 文字列を連結する
     *
     * @param strings 連結したい文字列配列
     * @return 連結後の文字列
     */
    public String getConnectString(String[] strings){
        StringBuilder builder = new StringBuilder();
        String conString = null;
        for (String string : strings) {
            builder.append(string);
        }
        conString = builder.toString();
        return conString;
    }
}
