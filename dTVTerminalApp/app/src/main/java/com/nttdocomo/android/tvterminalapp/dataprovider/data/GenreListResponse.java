/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;


public class GenreListResponse implements Serializable {
    private static final long serialVersionUID = -1211331811012005279L;
    private String mUpdateDate; //UpdateDate
    private ArrayList<GenreListMetaData> mPLALA;//PLALA
    private ArrayList<GenreListMetaData> mNOD;//NOD
    private ArrayList<GenreListMetaData> mARIB;//ARIB

    public ArrayList<GenreListMetaData> getmPLALA() {
        return mPLALA;
    }

    public void setmPLALA(ArrayList<GenreListMetaData> mPLALA) {
        this.mPLALA = mPLALA;
    }

    public ArrayList<GenreListMetaData> getmNOD() {
        return mNOD;
    }

    public void setmNOD(ArrayList<GenreListMetaData> mNOD) {
        this.mNOD = mNOD;
    }

    public ArrayList<GenreListMetaData> getmARIB() {
        return mARIB;
    }

    public void setmARIB(ArrayList<GenreListMetaData> mARIB) {
        this.mARIB = mARIB;
    }


    public static final String GENRE_LIST_RESPONSE_UPDATE_DATE = "UpdateDate";
    public static final String GENRE_LIST_RESPONSE_PLALA_LIST = "PLALA";
    public static final String GENRE_LIST_RESPONSE_NOD_LIST = "NOD";
    public static final String GENRE_LIST_RESPONSE_ARIB_LIST = "ARIB";

    public String getUpdateDate() {
        return mUpdateDate;
    }

    public void setUpdateDate(String UpdateDate) {
        mUpdateDate = UpdateDate;
    }


    public GenreListResponse() {
        mUpdateDate = GENRE_LIST_RESPONSE_UPDATE_DATE;     //UpdateDate
    }
}
