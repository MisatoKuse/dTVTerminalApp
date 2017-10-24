/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import java.util.ArrayList;

public class VodClipContentInfo {

    public class VodClipContentInfoItem {
        public boolean mClipFlag;
        public String mContentPictureUrl;
        public String mTitle;
        public String mRating;
        //public String mCrid;

        /* doing */
        public VodClipContentInfoItem(boolean clipFlag, String contentPictureUrl, String title, String rating){
            mClipFlag = clipFlag;
            mContentPictureUrl = contentPictureUrl;
            mTitle = title;
            mRating = rating;
        }
    }

    private ArrayList<VodClipContentInfoItem> mClipContentInfos= new ArrayList<VodClipContentInfoItem>();

    public void add(VodClipContentInfoItem item){
        mClipContentInfos.add(item);
    }

    public VodClipContentInfoItem get(int index) {
        index = (0 > index? 0: index);
        return mClipContentInfos.get(index);
    }

    public boolean isContentEqual(VodClipContentInfoItem item1, VodClipContentInfoItem item2){
        if(null==item1 || null==item2){
            return false;
        }
        return //item1.mClipFlag == item2.mClipFlag
                item1.mContentPictureUrl.equals(item2.mContentPictureUrl)
                        && item1.mRating.equals(item2.mRating)
                        && item1.mTitle.equals(item2.mTitle);
    }

    public int size(){
        if(null==mClipContentInfos){
            return 0;
        }
        return mClipContentInfos.size();
    }

}
