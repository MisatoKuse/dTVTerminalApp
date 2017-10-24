/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import java.util.ArrayList;

public class WatchListenVideoContentInfo {

    public class WatchListenVideoContentInfoItem {
        public String mContentPictureUrl;
        public String mTitle;
        public String mRatingValue;

        /* サムネイル
		   タイトル
		   評価値 */
        public WatchListenVideoContentInfoItem(String contentPictureUrl, String title, String ratingValue){
            mContentPictureUrl = contentPictureUrl;
            mTitle = title;
            mRatingValue = ratingValue;
        }
    }

    private ArrayList<WatchListenVideoContentInfoItem> mClipContentInfos= new ArrayList<WatchListenVideoContentInfoItem>();

    public void add(WatchListenVideoContentInfoItem item){
        mClipContentInfos.add(item);
    }

    public WatchListenVideoContentInfoItem get(int index) {
        index = (0 > index? 0: index);
        return mClipContentInfos.get(index);
    }

    public boolean isContentEqual(WatchListenVideoContentInfoItem item1, WatchListenVideoContentInfoItem item2){
        if(null==item1 || null==item2){
            return false;
        }
        return item1.mContentPictureUrl.equals(item2.mContentPictureUrl)
                && item1.mRatingValue.equals(item2.mRatingValue)
                && item1.mTitle.equals(item2.mTitle);
    }

    public int size(){
        if(null==mClipContentInfos){
            return 0;
        }
        return mClipContentInfos.size();
    }

}
