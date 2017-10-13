package com.nttdocomo.android.tvterminalapp.Model.Search;


public class SearchServiceType {

    private String mType=ServiceId.dTV;

    public SearchServiceType(String type){
        mType=type;
    }

    public  class ServiceId {
        public static  final String dTV = "15";
        public static  final String dAnime = "17";
        public static  final String dTVChannel = "43";
        public static  final String hikariTVForDocomo = "44";
    }

    public String serverRequestServiceIdString(){
        if(mType.equals(ServiceId.dTV)) {
            return ServiceId.dTV;
        } else if(mType.equals(ServiceId.dAnime)) {
            return ServiceId.dAnime;
        }  else if(mType.equals(ServiceId.dTVChannel)) {
            return ServiceId.dTVChannel;
        }

        return ServiceId.hikariTVForDocomo;
    }
}
