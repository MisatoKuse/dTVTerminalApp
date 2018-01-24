/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;

public class RoleListDataProvider {

    private Context mContext = null;

    public RoleListDataProvider(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * ロールリスト取得.
     */
    public void requestRoleListData() {
        //仮実装　API取得～DB保存まで
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            dateUtils.addLastProgramDate(DateUtils.ROLELIST_LAST_UPDATE);
            RoleListWebClient roleListWebClient = new RoleListWebClient(mContext);
//            roleListWebClient.getRoleListApi((RoleListWebClient.RoleListJsonParserCallback) mContext);
        }
    }

}
