/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;

public class ClassNameUtils {

    public Class<?> getContentsService(int serviceId){
        Class<?> aClass;
        switch (serviceId) {
            case OtherContentsDetailData.DTV_CONTENTS_SERVICE_ID:
            case OtherContentsDetailData.D_ANIMATION_CONTENTS_SERVICE_ID:
            case OtherContentsDetailData.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                aClass = DtvContentsDetailActivity.class;
                break;
            default:
                aClass = TvPlayerActivity.class;
                break;
        }
        return aClass;
    }
}