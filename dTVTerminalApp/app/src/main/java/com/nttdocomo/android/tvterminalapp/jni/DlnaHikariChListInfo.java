/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DlnaHikariChListInfo {

    private ArrayList<DlnaHikariChListItem> mLists = new ArrayList();

    public void addItem(DlnaHikariChListItem item){
        mLists.add(item);
    }

    public void clearAll(){
        mLists.clear();
    }

    public int size(){
        if(null!=mLists){
            return mLists.size();
        }

        return 0;
    }

    public DlnaHikariChListItem get(int index){
        if(index<0){
            index=0;
        }
        if(null==mLists || 0==mLists.size()){
            return null;
        }

        return mLists.get(index);
    }

    public void sortByTimeDesc(){
        Collections.sort(mLists, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                DlnaHikariChListItem r1=(DlnaHikariChListItem)o1;
                DlnaHikariChListItem r2=(DlnaHikariChListItem)o2;
                return r2.mDate.compareTo(r1.mDate);
            }
        });
    }

    public static DlnaHikariChListInfo fromArrayList(ArrayList<Object> content){
        if(null==content){
            return null;
        }
        DlnaHikariChListInfo info=new DlnaHikariChListInfo();
        for(Object o: content){
            info.addItem((DlnaHikariChListItem)o);
        }

        return info;
    }

    public ArrayList<DlnaHikariChListItem> getRecordVideoLists() {
        return mLists;
    }
}
