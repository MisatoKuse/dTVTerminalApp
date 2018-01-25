/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import java.util.ArrayList;

/**
 * チャンネルクラス.
 * 　　機能： チャンネルクラスである
 */
public class ChannelInfo {

    //Scheduleの配列を保存
    private ArrayList<ScheduleInfo> schedules = null;
    //チャンネルのタイトル
    private String title;
    //チャンネルのチャンネルID
    private int chNo;
    //チャンネルのサービスID
    private String serviceId;
    //チャンネルの開始時間
    private String startDate;
    //チャンネルの終了時間
    private String endDate;
    //チャンネルのアイコン
    private String thumbnail;
    /**
     * チャンネルのタイプ.
     */
    private String chType;
    /**
     * パーチャスID.
     */
    private String puId;
    /**
     * サブパーチャスID.
     */
    private String subPuId;
    /**
     * チャンネルパックのパーチャスID.
     */
    private String chPackPuId;
    /**
     * チャンネルパックのサブパーチャスID.
     */
    private String chPackSubPuId;

    /**
     * Channelタイトルを取得.
     * @return title タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * Channelタイトルを設定.
     * @param title タイトル
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * chNoを取得.
     * @return chNo
     */
    public int getChNo() {
        return chNo;
    }

    /**
     * ｍChnを設定.
     * @param chNo チャンネルID
     */
    public void setChNo(final int chNo) {
        this.chNo = chNo;
    }

    /**
     * serviceIdを取得.
     * @return serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * serviceIdを設定.
     * @param serviceId チャンネルID
     */
    public void setServiceId(final String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * startDateを取得.
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * startDateを設定.
     * @param startDate チャンネルID
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * endDateを取得.
     * @return endDate
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * endDateを設定.
     * @param endDate チャンネルID
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }
    /**
     * thumbnailを取得.
     * @return thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }
    /**
     * thumbnail.
     * @param thumbnail チャンネルID
     */
    public void setThumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
    }
    /**
     * chTypeを取得.
     * @return chType
     */
    public String getChType() {
        return chType;
    }
    /**
     * chTypeを設定.
     * @param chType チャンネルタイプ
     */
    public void setChType(final String chType) {
        this.chType = chType;
    }
    /**
     * puidを取得.
     * @return パーチャスID
     */
    public String getPuId() {
        return puId;
    }
    /**
     * puidを設定.
     * @param puId パーチャスID
     */
    public void setPuId(final String puId) {
        this.puId = puId;
    }
    /**
     * sub_puidを取得.
     * @return サブパーチャスID
     */
    public String getSubPuId() {
        return subPuId;
    }
    /**
     * sub_puidを設定.
     * @param subPuId サブパーチャスID
     */
    public void setSubPuId(final String subPuId) {
        this.subPuId = subPuId;
    }
    /**
     * CHPACKのpuidを取得.
     * @return チャンネルパックのパーチャスID
     */
    public String getChPackPuId() {
        return chPackPuId;
    }
    /**
     * CHPACKのpuidを設定.
     * @param chPackPuId チャンネルパックのパーチャスID
     */
    public void setChPackPuId(final String chPackPuId) {
        this.chPackPuId = chPackPuId;
    }
    /**
     * CHPACKのsub_puidを取得.
     * @return チャンネルパックのサブパーチャスID
     */
    public String getChPackSubPuId() {
        return chPackSubPuId;
    }
    /**
     * CHPACKのsub_puidを設定.
     * @param chPackSubPuId チャンネルパックのサブパーチャスID
     */
    public void setChPackSubPuId(final String chPackSubPuId) {
        this.chPackSubPuId = chPackSubPuId;
    }

    /**
     * チャンネル番組を取得.
     * @return schedules　チャンネル番組
     */
    public ArrayList<ScheduleInfo> getSchedules() {
        return schedules;
    }
    /**
     * チャンネル番組を設定.
     * @param schedules チャンネル
     */
    public void setSchedules(final ArrayList<ScheduleInfo> schedules) {
        this.schedules = schedules;
    }

    /**
     * チャンネルを比較.
     * @param ch2 比較対象チャンネル
     * @return true:同じ false:違う
     */
    public boolean equalTo(final ChannelInfo ch2) {
        if (null == ch2 || null == this.title || null == ch2.title) {
            return false;
        }
        return this.chNo == ch2.chNo && this.title.equals(ch2.title);
    }
}
