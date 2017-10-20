/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_TITLE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser.DAILYRANK_LIST_TITLE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE_ID;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_ADULT;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAIL_END_DATE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAIL_START_DATE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAIL_STATUS;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAII_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DEMONG;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DISPLAY_END_DATE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DISP_TYPE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DUR;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_EPISODE_ID;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_EPITITLE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_GENRE_ID_ARRAY;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_HDRFLG;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_MS;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_NEWA_END_DATE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_NEWA_START_DATE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_NG_FUNC;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_PUBLISH_END_DATE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_EVENT_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_LINEAR_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_LINEAR_START_END;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_MISSED_VOD;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_SERVICE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_TITLE;
//import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_TITLE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_VOD_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser.WEEKLYRANK_LIST_VOD_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_AGREEMENT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_GROUPID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_PAGEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED3;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED5;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_VIEWABLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_AGREEMENT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CHANNELID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_ENDVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_GROUPID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_PAGEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RECOMMENDMETHODID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RECOMMENDORDER;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED3;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED4;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED5;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_VIEWABLE;

public class DBConstants {

    /**
     * DB名
     */
    public static final String DATABASE_NAME = "db_data";

    /**
     * DBVersion
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * Jsonのキー名が数字から始まってる時の対策用定数
     */
    public static final String FOUR_K_FLG = "4kflg";
    public static final String UNDER_BAR_FOUR_K_FLG = "_4kflg";

    //Homeキャッシュデータ格納用テーブル
    public static final String CHANNEL_LIST_TABLE_NAME = "channel_list";
    public static final String ID_COLUMN = "row_id";

    public static final String CREATE_TABLE_CHANNEL_SQL = "" +
            "create table " + CHANNEL_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            CHANNEL_LIST_CRID + " text, " +
            CHANNEL_LIST_CID + " text, " +
            CHANNEL_LIST_TITLE_ID + " text, " +
            CHANNEL_LIST_EPISODE_ID + " text, " +
            CHANNEL_LIST_TITLE + " text, " +
            CHANNEL_LIST_EPITITLE + " text, " +
            CHANNEL_LIST_DISP_TYPE + " text, " +
            CHANNEL_LIST_DISPLAY_START_DATE + " text, " +
            CHANNEL_LIST_DISPLAY_END_DATE + " text, " +
            CHANNEL_LIST_AVAIL_START_DATE + " text, " +
            CHANNEL_LIST_AVAIL_END_DATE + " text, " +
            CHANNEL_LIST_PUBLISH_START_DATE + " text, " +
            CHANNEL_LIST_PUBLISH_END_DATE + " text, " +
            CHANNEL_LIST_NEWA_START_DATE + " text, " +
            CHANNEL_LIST_NEWA_END_DATE + " text, " +
            CHANNEL_LIST_COPYRIGHT + " text, " +
            CHANNEL_LIST_THUMB + " text, " +
            CHANNEL_LIST_DUR + " text, " +
            CHANNEL_LIST_DEMONG + " text, " +
            CHANNEL_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            CHANNEL_LIST_HDRFLG + " text, " +
            CHANNEL_LIST_AVAIL_STATUS + " text, " +
            CHANNEL_LIST_DELIVERY + " text, " +
            CHANNEL_LIST_R_VALUE + " text, " +
            CHANNEL_LIST_ADULT + " text, " +
            CHANNEL_LIST_MS + " text, " +
            CHANNEL_LIST_NG_FUNC + " text, " +
            CHANNEL_LIST_GENRE_ID_ARRAY + " text, " +
            CHANNEL_LIST_DTV + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String DAILYRANK_LIST_TABLE_NAME = "daily_rank_list";
    public static final String CREATE_TABLE_DAILY_RANK_SQL = "" +
            "create table " + DAILYRANK_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            DAILYRANK_LIST_CRID + " text, " +
            DAILYRANK_LIST_CID + " text, " +
            DAILYRANK_LIST_TITLE_ID + " text, " +
            DAILYRANK_LIST_EPISODE_ID + " text, " +
            DAILYRANK_LIST_TITLE + " text, " +
            DAILYRANK_LIST_EPITITLE + " text, " +
            DAILYRANK_LIST_DISP_TYPE + " text, " +
            DAILYRANK_LIST_DISPLAY_START_DATE + " text, " +
            DAILYRANK_LIST_DISPLAY_END_DATE + " text, " +
            DAILYRANK_LIST_AVAIL_START_DATE + " text, " +
            DAILYRANK_LIST_AVAIL_END_DATE + " text, " +
            DAILYRANK_LIST_PUBLISH_START_DATE + " text, " +
            DAILYRANK_LIST_PUBLISH_END_DATE + " text, " +
            DAILYRANK_LIST_NEWA_START_DATE + " text, " +
            DAILYRANK_LIST_NEWA_END_DATE + " text, " +
            DAILYRANK_LIST_COPYRIGHT + " text, " +
            DAILYRANK_LIST_THUMB + " text, " +
            DAILYRANK_LIST_DUR + " text, " +
            DAILYRANK_LIST_DEMONG + " text, " +
            DAILYRANK_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            DAILYRANK_LIST_HDRFLG + " text, " +
            DAILYRANK_LIST_AVAIL_STATUS + " text, " +
            DAILYRANK_LIST_DELIVERY + " text, " +
            DAILYRANK_LIST_R_VALUE + " text, " +
            DAILYRANK_LIST_ADULT + " text, " +
            DAILYRANK_LIST_MS + " text, " +
            DAILYRANK_LIST_NG_FUNC + " text, " +
            DAILYRANK_LIST_GENRE_ID_ARRAY + " text, " +
            DAILYRANK_LIST_DTV + " text" +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String TV_SCHEDULE_LIST_TABLE_NAME = "tv_schedule_list";
    public static final String CREATE_TABLE_TV_SCHEDULE_SQL = "" +
            "create table " + TV_SCHEDULE_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            TV_SCHEDULE_LIST_CRID + " text, " +
            TV_SCHEDULE_LIST_CID + " text, " +
            TV_SCHEDULE_LIST_TITLE_ID + " text, " +
            TV_SCHEDULE_LIST_EPISODE_ID + " text, " +
            TV_SCHEDULE_LIST_TITLE + " text, " +
            TV_SCHEDULE_LIST_EPITITLE + " text, " +
            TV_SCHEDULE_LIST_DISP_TYPE + " text, " +
            TV_SCHEDULE_LIST_DISPLAY_START_DATE + " text, " +
            TV_SCHEDULE_LIST_DISPLAY_END_DATE + " text, " +
            TV_SCHEDULE_LIST_AVAIL_START_DATE + " text, " +
            TV_SCHEDULE_LIST_AVAIL_END_DATE + " text, " +
            TV_SCHEDULE_LIST_PUBLISH_START_DATE + " text, " +
            TV_SCHEDULE_LIST_PUBLISH_END_DATE + " text, " +
            TV_SCHEDULE_LIST_NEWA_START_DATE + " text, " +
            TV_SCHEDULE_LIST_NEWA_END_DATE + " text, " +
            TV_SCHEDULE_LIST_COPYRIGHT + " text, " +
            TV_SCHEDULE_LIST_THUMB + " text, " +
            TV_SCHEDULE_LIST_DUR + " text, " +
            TV_SCHEDULE_LIST_DEMONG + " text, " +
            TV_SCHEDULE_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            TV_SCHEDULE_LIST_HDRFLG + " text, " +
            TV_SCHEDULE_LIST_AVAIL_STATUS + " text, " +
            TV_SCHEDULE_LIST_DELIVERY + " text, " +
            TV_SCHEDULE_LIST_R_VALUE + " text, " +
            TV_SCHEDULE_LIST_ADULT + " text, " +
            TV_SCHEDULE_LIST_MS + " text, " +
            TV_SCHEDULE_LIST_NG_FUNC + " text, " +
            TV_SCHEDULE_LIST_GENRE_ID_ARRAY + " text, " +
            TV_SCHEDULE_LIST_DTV + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String USER_INFO_LIST_TABLE_NAME = "user_info_list";
    public static final String CREATE_TABLE_USER_INFO_SQL = "" +
            "create table " + USER_INFO_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            USER_INFO_LIST_LOGGEDIN_ACCOUNT + " text, " +
            USER_INFO_LIST_CONTRACT_STATUS + " text, " +
            USER_INFO_LIST_DCH_AGE_REQ +" text," +
            USER_INFO_LIST_H4D_AGE_REQ + " text" +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String VODCLIP_LIST_TABLE_NAME = "vod_clip_list";
    public static final String CREATE_TABLE_VODCLIP_LIST_SQL = "" +
            "create table " + VODCLIP_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            VODCLIP_LIST_CRID + " text, " +
            VODCLIP_LIST_CID + " text, " +
            VODCLIP_LIST_TITLE_ID + " text, " +
            VODCLIP_LIST_EPISODE_ID + " text, " +
            VODCLIP_LIST_TITLE + " text, " +
            VODCLIP_LIST_EPITITLE + " text, " +
            VODCLIP_LIST_DISP_TYPE + " text, " +
            VODCLIP_LIST_DISPLAY_START_DATE + " text, " +
            VODCLIP_LIST_DISPLAY_END_DATE + " text, " +
            VODCLIP_LIST_AVAIL_START_DATE + " text, " +
            VODCLIP_LIST_AVAIL_END_DATE + " text, " +
            VODCLIP_LIST_PUBLISH_START_DATE + " text, " +
            VODCLIP_LIST_PUBLISH_END_DATE + " text, " +
            VODCLIP_LIST_NEWA_START_DATE + " text, " +
            VODCLIP_LIST_NEWA_END_DATE + " text, " +
            VODCLIP_LIST_COPYRIGHT + " text, " +
            VODCLIP_LIST_THUMB + " text, " +
            VODCLIP_LIST_DUR + " text, " +
            VODCLIP_LIST_DEMONG + " text, " +
            VODCLIP_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            VODCLIP_LIST_HDRFLG + " text, " +
            VODCLIP_LIST_AVAIL_STATUS + " text, " +
            VODCLIP_LIST_DELIVERY + " text, " +
            VODCLIP_LIST_R_VALUE + " text, " +
            VODCLIP_LIST_ADULT + " text, " +
            VODCLIP_LIST_MS + " text, " +
            VODCLIP_LIST_NG_FUNC + " text, " +
            VODCLIP_LIST_GENRE_ID_ARRAY + " text, " +
            VODCLIP_LIST_DTV + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String WEEKLYRANK_LIST_TABLE_NAME = "weekly_rank_list";
    public static final String CREATE_TABLE_WEEKLYRANK_SQL = "" +
            "create table " + WEEKLYRANK_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            WEEKLYRANK_LIST_CRID + " text, " +
            WEEKLYRANK_LIST_CID + " text, " +
            WEEKLYRANK_LIST_TITLE + " text, " +
            WEEKLYRANK_LIST_CID + " text, " +
            WEEKLYRANK_LIST_SERVICE_ID + " text, " +
            WEEKLYRANK_LIST_EVENT_ID + " text, " +
            WEEKLYRANK_LIST_CHNO + " text, " +
            WEEKLYRANK_LIST_DISP_TYPE + " text, " +
            WEEKLYRANK_LIST_MISSED_VOD + " text, " +
            WEEKLYRANK_LIST_LINEAR_START_DATE + " text, " +
            WEEKLYRANK_LIST_LINEAR_START_END + " text, " +
            WEEKLYRANK_LIST_VOD_START_DATE + " text, " +
            WEEKLYRANK_LIST_VOD_END_DATE + " text, " +
            WEEKLYRANK_LIST_THUMB + " text, " +
            WEEKLYRANK_LIST_COPYRIGHT + " text, " +
            WEEKLYRANK_LIST_DUR + " text, " +
            WEEKLYRANK_LIST_DEMONG + " text, " +
            WEEKLYRANK_LIST_AVAII_STATUS + " text, " +
            WEEKLYRANK_LIST_DELIVERY + " text, " +
            WEEKLYRANK_LIST_R_VALUE + " text, " +

            // 旧パラメータをコメントアウト
//            WEEKLYRANK_LIST_CRID + " text, " +
//            WEEKLYRANK_LIST_CID + " text, " +
//            WEEKLYRANK_LIST_TITLE_ID + " text, " +
//            WEEKLYRANK_LIST_EPISODE_ID + " text, " +
//            WEEKLYRANK_LIST_TITLE + " text, " +
//            WEEKLYRANK_LIST_EPITITLE + " text, " +
//            WEEKLYRANK_LIST_DISP_TYPE + " text, " +
//            WEEKLYRANK_LIST_DISPLAY_START_DATE + " text, " +
//            WEEKLYRANK_LIST_DISPLAY_END_DATE + " text, " +
//            WEEKLYRANK_LIST_AVAIL_START_DATE + " text, " +
//            WEEKLYRANK_LIST_AVAIL_END_DATE + " text, " +
//            WEEKLYRANK_LIST_PUBLISH_START_DATE + " text, " +
//            WEEKLYRANK_LIST_PUBLISH_END_DATE + " text, " +
//            WEEKLYRANK_LIST_NEWA_START_DATE + " text, " +
//            WEEKLYRANK_LIST_NEWA_END_DATE + " text, " +
//            WEEKLYRANK_LIST_COPYRIGHT + " text, " +
//            WEEKLYRANK_LIST_THUMB + " text, " +
//            WEEKLYRANK_LIST_DUR + " text, " +
//            WEEKLYRANK_LIST_DEMONG + " text, " +
//            WEEKLYRANK_LIST_BVFLG + " text, " +
//            UNDER_BAR_FOUR_K_FLG + " text, " +
//            WEEKLYRANK_LIST_HDRFLG + " text, " +
//            WEEKLYRANK_LIST_AVAIL_STATUS + " text, " +
//            WEEKLYRANK_LIST_DELIVERY + " text, " +
//            WEEKLYRANK_LIST_R_VALUE + " text, " +
//            WEEKLYRANK_LIST_ADULT + " text, " +
//            WEEKLYRANK_LIST_MS + " text, " +
//            WEEKLYRANK_LIST_NG_FUNC + " text, " +
//            WEEKLYRANK_LIST_GENRE_ID_ARRAY + " text, " +
//            WEEKLYRANK_LIST_DTV + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String RECOMMEND_CHANNEL_LIST_TABLE_NAME = "recommend_channel_list";
    public static final String CREATE_TABLE_RECOMMEND_CHANNEL_SQL = "" +
            "create table " + RECOMMEND_CHANNEL_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDCHANNEL_LIST_SERVICEID + " text, " +
            RECOMMENDCHANNEL_LIST_CATEGORYID + " text, " +
            RECOMMENDCHANNEL_LIST_CHANNELID + " text, " +
            RECOMMENDCHANNEL_LIST_CONTENTSID + " text, " +
            RECOMMENDCHANNEL_LIST_TITLE + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, " +
            RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED1 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED2 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED3 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED4 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED5 + " text, " +
            RECOMMENDCHANNEL_LIST_AGREEMENT + " text, " +
            RECOMMENDCHANNEL_LIST_VIEWABLE + " text, " +
            RECOMMENDCHANNEL_LIST_PAGEID + " text " +
            RECOMMENDCHANNEL_LIST_GROUPID + " text " +
            RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String RECOMMEND_VIDEO_LIST_TABLE_NAME = "recommend_video_list";
    public static final String CREATE_TABLE_RECOMMEND_VIDEO_SQL = "" +
            "create table " + RECOMMEND_VIDEO_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDVIDEO_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDVIDEO_LIST_SERVICEID + " text, " +
            RECOMMENDVIDEO_LIST_CATEGORYID + " text, " +
            RECOMMENDVIDEO_LIST_CHANNELID + " text, " +
            RECOMMENDVIDEO_LIST_CONTENTSID + " text, " +
            RECOMMENDVIDEO_LIST_TITLE + " text, " +
            RECOMMENDVIDEO_LIST_CTPICURL1 + " text, " +
            RECOMMENDVIDEO_LIST_CTPICURL2 + " text, " +
            RECOMMENDVIDEO_LIST_STARTVIEWING + " text, " +
            RECOMMENDVIDEO_LIST_ENDVIEWING + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED1 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED2 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED3 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED4 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED5 + " text, " +
            RECOMMENDVIDEO_LIST_AGREEMENT + " text, " +
            RECOMMENDVIDEO_LIST_VIEWABLE + " text, " +
            RECOMMENDVIDEO_LIST_PAGEID + " text, " +
            RECOMMENDVIDEO_LIST_GROUPID + " text, " +
            RECOMMENDVIDEO_LIST_RECOMMENDMETHODID + " text " +
            ")";

}
