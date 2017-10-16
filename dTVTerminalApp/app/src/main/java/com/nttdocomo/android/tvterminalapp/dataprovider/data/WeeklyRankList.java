package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class WeeklyRankList implements Serializable{
    private int rowId;

    private String value;

    HashMap<String, String> map = new HashMap<>();
    List<HashMap<String, String>> hashMaps = new ArrayList<HashMap<String, String>>();

    /**
     * テスト用getter
     *
     * @return
     */
    public HashMap<String, String> getMap() {
        return map;
    }

    /**
     * テスト用getter
     *
     * @return
     */
    public List<HashMap<String, String>> getHashMaps() {
        return hashMaps;
    }

    /**
     * テスト用setter
     *
     * @param hashMap
     */
    public void setHashMap(HashMap<String, String> hashMap) {
//        map = hashMap;
        map.put("status", "OK");
        map.put("pager_upper_limit", "1");
        map.put("pager_lower_limit", "1");
        map.put("pager_offset", "0");
        map.put("pager_count", "5");
    }

    /**
     * テスト用setter
     *
     * @param hashMapList
     */
    public void setHashMapList(List<HashMap<String, String>> hashMapList) {
        hashMaps = hashMapList;
        HashMap<String, String> hash = new HashMap<>();

        map.put("crid","crid001");
        map.put("cid","cid001");
        map.put("title_id","title_id001");
        map.put("episode_id","episode_id001");
        map.put("title","ＭＬＢ　アメリカン・リーグ　地区シリーズ　第１戦「レッドソックス×アストロズ」");
        map.put("epititle","「ボストン・レッドソックス」対「ヒューストン・アストロズ」（試合開始　日本時間　５：０８）【解説】今中慎二，【アナウンサー】早瀬雄一");
        map.put("disp_type","tv_channel");
        map.put("display_start_date","2017-10-06T06:49:00+09:00");
        map.put("display_end_date","2017-10-06T06:50:00+09:00");
        map.put("avail_start_date","2017-10-06T06:49:00+09:00");
        map.put("avail_end_date","2017-10-06T06:50:00+09:00");
        map.put("publish_start_date","2017-10-06T06:49:00+09:00");
        map.put("publish_end_date","2017-10-06T06:50:00+09:00");
        map.put("newa_start_date","2017-10-06T06:49:00+09:00");
        map.put("newa_end_date","2017-10-06T06:50:00+09:00");
        map.put("copyright","copyright");
        map.put("thumb","//www.nhk.or.jp/prog/img/639/639.jpg");
        map.put("dur","3600");
        map.put("demong","");
        map.put("bvflg","");
        map.put("4kflg","");
        map.put("hdrflg","");
        map.put("avail_status","avail_status");
        map.put("delivery","1.2");
        map.put("r_value","r_value");
        map.put("adult","");
        map.put("ms","");
        map.put("ng_func","");
        map.put("genre_id_array","0101,0100,0104");
        map.put("dtv","");

        for (int i = 0; i < 2; i++) {
            hashMaps.add(hash);
        }
    }
}
