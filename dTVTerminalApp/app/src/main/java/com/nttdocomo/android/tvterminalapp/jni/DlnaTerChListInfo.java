/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DlnaTerChListInfo {

    private ArrayList<DlnaTerChListItem> mLists = new ArrayList();

    public void addItem(DlnaTerChListItem item){
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

    public DlnaTerChListItem get(int index){
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
                DlnaTerChListItem r1=(DlnaTerChListItem)o1;
                DlnaTerChListItem r2=(DlnaTerChListItem)o2;
                return r2.mDate.compareTo(r1.mDate);
            }
        });
    }

    public static DlnaTerChListInfo fromArrayList(ArrayList<Object> content){
        if(null==content){
            return null;
        }
        DlnaTerChListInfo info=new DlnaTerChListInfo();
        for(Object o: content){
            info.addItem((DlnaTerChListItem)o);
        }

        return info;
    }

    public static final ArrayList<Object> toArrayList(DlnaTerChListInfo info){
        ArrayList<Object> ret=new ArrayList();
        if(null==info || 0==info.size()){
            return ret;
        }
        for(int i=0;i<info.size();++i){
            ret.add(info.get(i));
        }
        return ret;
    }

    public ArrayList<DlnaTerChListItem> getTerChLists() {
        return mLists;
    }
}
