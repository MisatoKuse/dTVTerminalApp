package com.nttdocomo.android.tvterminalapp.activity.common;


import com.nttdocomo.android.tvterminalapp.common.UserState;

public class MenuItemParam{

    private UserState mUserState=UserState.LOGIN_NG;
    private int mRecordProgram = 0;     //録画番組数
    private int mClipCount = 0;          //クリップ数
    private int mBoughtVideo = 0;       //購入済みビデオ数
    private int mInformationCount=0;   //お知らせ数
    private int mRecordReserveCount = 0;//録画予約数

    public MenuItemParam() {

    }

    public UserState getUserState(){
        return mUserState;
    }

    /**
     * 機能
     *      契約・ペアリング済み用
     *
     * RECORD_PROGRAM
     * @param recordProgram     録画番組数
     * @param clipCount          クリップ数
     * @param boughtVideo        購入済みビデオ数
     * @param recordReserveCount 録画予約数
     * @param informationCount  お知らせ数
     */
    public void setParamForContractOkPairingOk(int recordProgram, int clipCount, int boughtVideo, int recordReserveCount,int informationCount){

        mUserState = UserState.CONTRACT_OK_PARING_OK;
        mRecordProgram = recordProgram;
        mClipCount = clipCount;
        mBoughtVideo = boughtVideo;
        mRecordReserveCount=recordReserveCount;
        mInformationCount=informationCount;
    }

    /**
     * 未ログイン
     */
    public void setParamForLoginNg(int informationCount){

        mUserState = UserState.LOGIN_NG;
        mInformationCount=informationCount;
    }

    /**
     * 未契約ログイン
     *
     * @param informationCount
     */
    public void setParamForLoginOkContractNg(int informationCount){

        mUserState = UserState.LOGIN_OK_CONTRACT_NG;
        mInformationCount=informationCount;
    }

    /**
     * 契約・ペアリング未
     *
     */
    public void setParamForContractOkPairingNg(int clipCount, int informationCount){

        mUserState = UserState.CONTRACT_OK_PAIRING_NG;
        mClipCount = clipCount;
        mInformationCount=informationCount;
    }

    //録画番組数を戻す
    public int getRecordProgramCount(){
        return mRecordProgram;
    }

    //クリップ数を戻す
    public int getClipCount(){
        return mClipCount;
    }

    //購入済みビデオ数を戻す
    public int getPurchasedVideoCount(){
        return mBoughtVideo;
    }

    //お知らせ数を戻す
    public int getInformationCount(){
        return mInformationCount;
    }

    //録画予約数
    public int getRecordReserveCount() {
        return mRecordReserveCount;
    }

}
