package com.nttdocomo.android.tvterminalapp.webapiclient_temp.recommend.search;


import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.common.DCommon;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

public class XMLParser extends AsyncTask<String, Integer, String> {

    private TotalSearchResponseData searchResponse = null;
    private TotalSearchErrorData searchError = null;
    private XMLParserFinishListener mXMLParserFinishListener=null;

    private static final String kStatus = "status";
    private static final String kStatusOk = "ok";
    private static final String kStatusNg = "ng";

    private static final String kId = "id";
    private static final String kParam = "param";

    private static final String kTotalCount = "totalCount";
    private static final String kResultCount = "resultCount";

    private static final String kServiceCount = "serviceCount";
    private static final String kContent = "content";

    private static final String kContentCount = "contentCount";

    private static final String kRank = "rank";
    private static final String kCtId = "ctId";
    private static final String kCtPicURL = "ctPicURL";
    private static final String kTitle = "title";
    private static final String kPerson = "person";
    private static final String kTitleKind = "titleKind";

    private static final String kUserId = "userId";
    private static final String kFunction = "function";
    private static final String kResponseType = "responseType";
    private static final String kQuery = "query";
    private static final String kStartIndex = "startIndex";
    private static final String kMaxResult = "maxResult";
    private static final String kSortKind = "sortKind";
    private static final String kServiceId = "serviceId";
    private static final String kCondition = "condition";

    public interface XMLParserFinishListener {
        public void onXMLParserFinish(TotalSearchResponseData responseData);
        public void onXMLParserError(TotalSearchErrorData errorData);
    }


    public XMLParser(XMLParserFinishListener xMLParserFinishListener){
        mXMLParserFinishListener=  xMLParserFinishListener;
    }

    @Override
    protected String doInBackground(String... strings) {

        searchResponse = null;
        searchError = null;

        if(null==strings || 1>strings.length){
            setOtherError();    //searchError = new TotalSearchErrorData(DCommon.SEARCH_ERROR_ID_1, DCommon.SEARCH_ERROR_PARAM_NULL_REQ);
            return null;
        }
        parse(strings[0]);

        return null;
    }

    private void setOtherError(){
        searchError = new TotalSearchErrorData(DCommon.SEARCH_ERROR_ID_1, DCommon.SEARCH_ERROR_PARAM_NULL_REQ);
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        Log.d(DCommon.LOG_DEF_TAG, "MyAsyncTask.onPostExecute aVoid=" + aVoid);

        if(null!=mXMLParserFinishListener){
            if(null == searchError){
                mXMLParserFinishListener.onXMLParserFinish(searchResponse);
            } else {
                mXMLParserFinishListener.onXMLParserError(searchError);
            }
        }
    }

    //2.parse
    private void parse(String responseData) {
        Log.d(DCommon.LOG_DEF_TAG, "parse");

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(responseData));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            setOtherError();
            return;
        }
        int event = 0;
        try {
            event = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            setOtherError();
            return;
        }

        boolean loop=true;
        String text="";
        try{
            while (loop) {

                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        parserDidStartDocument();
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        loop=false;
                        parserDidEndDocument();
                        break;

                    case XmlPullParser.START_TAG:

                        String name2 = parser.getName();
                        String xmlValue = null;

                        try {
                            xmlValue = parser.nextText();
                        } catch (XmlPullParserException e) {
                            //e.printStackTrace();
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }

                        if(name2 != null && 0<name2.length()){
                            parseProc(parser, name2, xmlValue);
                        }

                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        String name = parser.getName();
                       // String xmlValue = null;

                        //xmlValue = parser.nextText();
                        if(name != null && 0<name.length()){
                            parseProc(parser, name, text);
                        }

                        break;
                    default:
                }

                event = parser.next();

            }   //while end
        } catch (Exception e) {
            e.printStackTrace();
            if(null==searchError){
                setOtherError();
            }
        }

    }

    public void parserDidStartDocument(/*XMLParser parser*/) {
        Log.d(DCommon.LOG_DEF_TAG, "パース開始");

        searchResponse = new TotalSearchResponseData();
        //searchError = new TotalSearchErrorData();
    }

    private void ifNullCreate(){
        if(null==searchError) {
            searchError = new TotalSearchErrorData();
        }
    }

    public void parserDidEndDocument(/*XMLParser parser*/) {
        Log.d(DCommon.LOG_DEF_TAG, "パース終了");
    }

    public void parseProc(XmlPullParser parser, String tagName, String value) throws Exception {

        if(kStatus.equals(tagName)){
            if(kStatusOk.equals(value)){
                Log.d(DCommon.LOG_DEF_TAG, "parseProc, " + kStatusOk);
                searchResponse.status = kStatusOk;
            }else if(kStatusNg.equals(value)) {
                Log.d(DCommon.LOG_DEF_TAG, "parseProc, " + kStatusNg);
                ifNullCreate();
                searchError.status=value;
            }
        } else if(kTotalCount.equals(tagName)){
            searchResponse.totalCount = Integer.parseInt(value);
        } else if(kQuery.equals(tagName)){
            searchResponse.query = value;
        } else if(kStartIndex.equals(tagName)){
            searchResponse.startIndex = Integer.parseInt(value);
        } else if(kResultCount.equals(tagName)){
            searchResponse.resultCount = Integer.parseInt(value);
        } else if(kServiceCount.equals(tagName)) {
            if(null==value) {
                searchResponse.appendServiceCount();
            }
        } else if(kContent.equals(tagName)) {

            if(null==value){
                searchResponse.appendContent();
            }
        }  else if(kServiceId.equals(tagName)) {     //serviceCountList
            if(searchResponse.contentList.size() > 0) { //parse content serviceId
                int currentContentListIndex = searchResponse.contentList.size() - 1;
                TotalSearchResponseData.Content content = searchResponse.contentList.get(currentContentListIndex);
                content.serviceId = Integer.parseInt(value);
                searchResponse.contentList.set(currentContentListIndex, content);
            } else {
                int currentServiceCountListIndex = searchResponse.serviceCountList.size() - 1;
                TotalSearchResponseData.ServiceCount serviceCount= searchResponse.serviceCountList.get(currentServiceCountListIndex);
                serviceCount.serviceId = Integer.parseInt(value);
                searchResponse.serviceCountList.set(currentServiceCountListIndex, serviceCount);
            }
        } else if(kContentCount.equals(tagName)){
            int currentServiceCountListIndex = searchResponse.serviceCountList.size() - 1;
            TotalSearchResponseData.ServiceCount serviceCount = searchResponse.serviceCountList.get(currentServiceCountListIndex);
            serviceCount.contentCount = Integer.parseInt(value);
            searchResponse.serviceCountList.set(currentServiceCountListIndex, serviceCount);

            //contentList
        } else if(kRank.equals(tagName)) {
            int currentContentListIndex = searchResponse.contentList.size() - 1;
            TotalSearchResponseData.Content content = searchResponse.contentList.get(currentContentListIndex);
            content.rank = Integer.parseInt(value);
            searchResponse.contentList.set(currentContentListIndex, content);
        } else if(kCtId.equals(tagName)) {
            int currentContentListIndex = searchResponse.contentList.size() - 1;
            TotalSearchResponseData.Content content = searchResponse.contentList.get(currentContentListIndex);
            content.ctId = value;
            searchResponse.contentList.set(currentContentListIndex, content);
        } else if(kCtPicURL.equals(tagName)) {
            int currentContentListIndex = searchResponse.contentList.size() - 1;
            TotalSearchResponseData.Content content = searchResponse.contentList.get(currentContentListIndex);
            content.ctPicURL = value;
            searchResponse.contentList.set(currentContentListIndex, content);
        } else if(kTitle.equals(tagName)) {
            int currentContentListIndex = searchResponse.contentList.size() - 1;
            TotalSearchResponseData.Content content = searchResponse.contentList.get(currentContentListIndex);
            content.title = value;
            searchResponse.contentList.set(currentContentListIndex, content);
        } else if(kId.equals(tagName)){
            ifNullCreate();
            searchError.error.id = value;
        } else if(kParam.equals(tagName)){
            ifNullCreate();
            searchError.error.param = value;
        }

            /* stub
             case kPerson:
             let currentContentListIndex = searchResponse.contentList.count - 1
             var content = searchResponse.contentList[currentContentListIndex]
             content.person = string
             searchResponse.contentList[currentContentListIndex] = content

             case kTitleKind:
             let currentContentListIndex = searchResponse.contentList.count - 1
             var content = searchResponse.contentList[currentContentListIndex]
             content.titleKind = Int(string)
             searchResponse.contentList[currentContentListIndex] = content
             */
    }
}
