/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.search;


public class SearchServiceType {

    private String mType=ServiceId.DTV_CONTENTS;

    public SearchServiceType(String type){
        mType=type;
    }

    public  static class ServiceId {
        public static  final String DTV_CONTENTS = "15";
        public static  final String D_ANIMATION_CONTENTS = "17";
        public static  final String DTV_CHANNEL_CONTENTS = "43";
        public static  final String HIKARI_TV_FOR_DOCOMO = "44";
    }

    public String serverRequestServiceIdString(){
        if(mType.equals(ServiceId.DTV_CONTENTS)) {
            return ServiceId.DTV_CONTENTS;
        } else if(mType.equals(ServiceId.D_ANIMATION_CONTENTS)) {
            return ServiceId.D_ANIMATION_CONTENTS;
        }  else if(mType.equals(ServiceId.DTV_CHANNEL_CONTENTS)) {
            return ServiceId.DTV_CHANNEL_CONTENTS;
        }

        return ServiceId.HIKARI_TV_FOR_DOCOMO;
    }
}
