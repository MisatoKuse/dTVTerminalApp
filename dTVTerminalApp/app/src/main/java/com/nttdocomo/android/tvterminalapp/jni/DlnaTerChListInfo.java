/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DlnaTerChListInfo {

    private ArrayList<DlnaTerChListItem> mLists = new ArrayList<>();

    private void addItem(DlnaTerChListItem item){
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

    static DlnaTerChListInfo fromArrayList(ArrayList<Object> content){
        if(null==content){
            return null;
        }
        DlnaTerChListInfo info=new DlnaTerChListInfo();
        for(Object o: content){
            info.addItem((DlnaTerChListItem)o);
        }

        return info;
    }

    public static ArrayList<Object> toArrayList(DlnaTerChListInfo info){
        ArrayList<Object> ret=new ArrayList<>();
        if(null==info || 0==info.size()){
            return ret;
        }
        for(int i=0;i<info.size();++i){
            ret.add(info.get(i));
        }
        return ret;
    }

    ArrayList<DlnaTerChListItem> getTerChLists() {
        return mLists;
    }
}
