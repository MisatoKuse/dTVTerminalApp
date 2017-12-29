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

    public static class ServiceCategorySet {
        public static final String HIKARI_TV_FOR_DOCOMO_IP_TV = "44:03";
        public static final String HIKARI_TV_FOR_DOCOMO_DTV_CHANNEL_BROADCAST = "44:04";
        public static final String HIKARI_TV_FOR_DOCOMO_WATCH_AGAIN = "44:05";
        public static final String HIKARI_TV_FOR_DOCOMO_DTV_CHANNEL_RELATION = "44:06";
        public static final String HIKARI_TV_FOR_DOCOMO_HIKARI_TV_VOD = "44:08";
        public static final String HIKARI_TV_FOR_DOCOMO_DTV_SVOD = "44:10";
    }

    //TODO:仕様未確認のため保留
//    public  static class CategoryId {
//        //地デジ(TODO:4月時点では提供なし)
//        public static  final String H4D_CATEGORY_TERRESTRIAL_DIGITAL = "01";
//        //BS(TODO:4月時点では提供なし)
//        public static  final String H4D_CATEGORY_SATELLITE_BROADCASTING = "02";
//        //IPTV
//        public static  final String H4D_CATEGORY_IP_TV = "03";
//        //dTVチャンネル　放送
//        public static  final String H4D_CATEGORY_DTV_CHANNEL_BROADCAST = "04";
//        //dTVチャンネル　VOD(見逃し)
//        public static  final String H4D_CATEGORY_DTV_CHANNEL_WATCH_AGAIN = "05";
//        //dTVチャンネル　VOD(関連番組)
//        public static  final String H4D_CATEGORY_DTV_CHANNEL_RELATION = "06";
//        //録画(TODO:4月時点では提供なし)
//        public static  final String H4D_CATEGORY_RECORDED_CONTENTS = "07";
//        //ひかりTV VOD
//        public static  final String H4D_CATEGORY_HIKARI_TV_VOD = "08";
//        //dTV SVOD
//        public static  final String H4D_CATEGORY_DTV_SVOD = "10";
//    }

    /**
     * サービスIDを返す
     *
     * @return サービスID
     */
    public String serverRequestServiceIdString() {
        if (mType.equals(ServiceId.DTV_CONTENTS)) {
            return ServiceId.DTV_CONTENTS;
        } else if (mType.equals(ServiceId.D_ANIMATION_CONTENTS)) {
            return ServiceId.D_ANIMATION_CONTENTS;
        } else if (mType.equals(ServiceId.DTV_CHANNEL_CONTENTS)) {
            return ServiceId.DTV_CHANNEL_CONTENTS;
        }

        return ServiceId.HIKARI_TV_FOR_DOCOMO;
    }

    /**
     * サービスIDとカテゴリIDのセットを返す
     *
     * @return サービスIDとカテゴリIDのセット
     */
    public String serverRequestServiceCategory() {
        if (mType.equals(ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_IP_TV)) {
            return ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_IP_TV;
        } else if (mType.equals(ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_DTV_CHANNEL_BROADCAST)) {
            return ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_DTV_CHANNEL_BROADCAST;
        } else if (mType.equals(ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_DTV_SVOD)) {
            return ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_DTV_SVOD;
        } else if (mType.equals(ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_DTV_CHANNEL_RELATION)) {
            return ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_DTV_CHANNEL_RELATION;
        } else if (mType.equals(ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_HIKARI_TV_VOD)) {
            return ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_HIKARI_TV_VOD;
        }
        return ServiceCategorySet.HIKARI_TV_FOR_DOCOMO_WATCH_AGAIN;
    }
}
