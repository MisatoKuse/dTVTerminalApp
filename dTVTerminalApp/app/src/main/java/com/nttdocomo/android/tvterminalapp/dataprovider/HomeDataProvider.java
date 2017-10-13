package com.nttdocomo.android.tvterminalapp.DataProvider;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class HomeDataProvider {

    public void getHomeData() {
        //Activityからのデータ取得要求受付
    }

    public boolean checkHomeData() {
        //Home用構造体を確認する
        return false;
    }

    public boolean checkLastDate(){
        //前回DB更新日時から一定時間が経過していたらfalseを返却
        return false;
    }

    public void sendHomeData(){
        //HomeActivityにHomeBeenを返却する
    }

    public void setStructData(){
        //引数に渡された各オブジェクトに応じたDMに構造体を渡す(ポリモーフィズム)
    }
}
