/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class DlnaHikariChListInfo {

    private ArrayList<DlnaHikariChListItem> mLists = new ArrayList<>();

    private void addItem(DlnaHikariChListItem item){
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

    static DlnaHikariChListInfo fromArrayList(ArrayList<Object> content){
        if(null==content){
            return null;
        }
        DlnaHikariChListInfo info=new DlnaHikariChListInfo();
        for(Object o: content){
            info.addItem((DlnaHikariChListItem)o);
        }

        return info;
    }

    ArrayList<DlnaHikariChListItem> getHikariChLists() {
        return mLists;
    }
}
