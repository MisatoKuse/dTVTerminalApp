/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DlnaRecVideoInfo {


    private ArrayList<DlnaRecVideoItem> mRecordVideoLists = new ArrayList();

    public void addItem(DlnaRecVideoItem item){
        mRecordVideoLists.add(item);
    }

    public void clearAll(){
        mRecordVideoLists.clear();
    }

    public int size(){
        if(null!=mRecordVideoLists){
            return mRecordVideoLists.size();
        }

        return 0;
    }

    public DlnaRecVideoItem get(int index){
        if(index<0){
            index=0;
        }
        if(null==mRecordVideoLists || 0==mRecordVideoLists.size()){
            return null;
        }

        return mRecordVideoLists.get(index);
    }

    public void sortByTimeDesc(){
        Collections.sort(mRecordVideoLists, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                DlnaRecVideoItem r1=(DlnaRecVideoItem)o1;
                DlnaRecVideoItem r2=(DlnaRecVideoItem)o2;
                return r2.mDate.compareTo(r1.mDate);
            }
        });
    }

    public static DlnaRecVideoInfo fromArrayList(ArrayList<Object> content){
        if(null==content){
            return null;
        }
        DlnaRecVideoInfo info=new DlnaRecVideoInfo();
        for(Object o: content){
            info.addItem((DlnaRecVideoItem)o);
        }

        return info;
    }

    public ArrayList<DlnaRecVideoItem> getRecordVideoLists() {
        return mRecordVideoLists;
    }
}
