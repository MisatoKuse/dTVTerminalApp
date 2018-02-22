/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.UserInfoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.AccountList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ユーザ情報InsertDataManager.
 */
public class UserInfoInsertDataManager extends AsyncTask<List<UserInfoList>, Void, Void> {

    /**
     * H4Dの契約につける接頭語.
     */
    private final static String H4D_HEADER = "h4d";

    /**
     * 複数データが来た場合の分割用.
     */
    private final static String SPLITTER = ",";
    /**
     * DB読み込み用項目名一覧.
     */
    private final static String[] DATA_COLUMNS = {
            UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS,
            UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ,
            UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ,
            UserInfoJsonParser.USER_INFO_LIST_UPDATE_TIME,
    };

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * ユーザーデータ.
     */
    private List<UserInfoList> mUserData;

    /**
     * statusバッファ.
     */
    private ArrayList<String> mStatus = new ArrayList<>();
    /**
     * DchAgeバッファ.
     */
    private ArrayList<String> mDchAge = new ArrayList<>();
    /**
     * H4dAgeバッファ.
     */
    private ArrayList<String> mH4dAge = new ArrayList<>();
    /**
     * H4dStatusバッファ.
     */
    private ArrayList<String> mH4dStatus = new ArrayList<>();
    /**
     * H4dDchAgeバッファ.
     */
    private ArrayList<String> mH4dDchAge = new ArrayList<>();
    /**
     * H4dH4dAgeバッファ.
     */
    private ArrayList<String> mH4dH4dAge = new ArrayList<>();

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public UserInfoInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * UserInfoAPIの解析結果をDBに格納する.
     *
     * @param userInfoList ユーザー情報リスト
     */
    private void insertUserInfoInsertList(final List<UserInfoList> userInfoList) {
        DTVTLogger.start();

        //各種オブジェクト作成
        DBHelper userInfoListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = userInfoListDBHelper.getWritableDatabase();
        UserInfoListDao userInfoListDao = new UserInfoListDao(db);

        //読み出し用にコピー
        mUserData = userInfoList;

        //DB保存前に前回取得したデータは全消去する
        userInfoListDao.delete();

        for (UserInfoList userInfo : userInfoList) {

            //リクエストユーザデータの蓄積
            List<AccountList> list1 = userInfo.getLoggedinAccount();

            makeRecord(userInfoListDao, list1, "");

            //H4D契約ユーザデータの蓄積（データは横並びで記録するが、ステータスに接頭語を付けて区別を行う）
            List<AccountList> list2 = userInfo.getH4dContractedAccount();

            makeRecord(userInfoListDao, list2, H4D_HEADER);
        }
        db.close();
        DTVTLogger.end();
    }

    /**
     * 蓄積用データを作成して、DBに書き込む処理.
     *
     * @param userInfoListDao DBアクセス用
     * @param userInfoList    元データ
     * @param header          分離時識別用データ
     */
    private void makeRecord(final UserInfoListDao userInfoListDao,
                            final List<AccountList> userInfoList,
                            final String header) {
        //蓄積バッファ
        StringBuilder statusBuffer = new StringBuilder();
        StringBuilder dchAgeBuffer = new StringBuilder();
        StringBuilder h4dAgeBuffer = new StringBuilder();

        int counter = userInfoList.size();
        //後で分離できるように蓄積する
        for (AccountList loggedInAccount : userInfoList) {
            statusBuffer.append(loggedInAccount.getContractStatus());
            dchAgeBuffer.append(loggedInAccount.getDchAgeReq());
            h4dAgeBuffer.append(loggedInAccount.getH4dAgeReq());

            //最後以外ならば分離用文字列を挿入する
            counter--;
            if (counter > 0) {
                statusBuffer.append(SPLITTER);
                dchAgeBuffer.append(SPLITTER);
                h4dAgeBuffer.append(SPLITTER);
            }
        }

        ContentValues values = new ContentValues();
        values.put(DBUtils.fourKFlgConversion(
                UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS),
                StringUtils.getConnectStrings(header, statusBuffer.toString()));
        values.put(DBUtils.fourKFlgConversion(
                UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ),
                StringUtils.getConnectStrings(header, dchAgeBuffer.toString()));
        values.put(DBUtils.fourKFlgConversion(
                UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ),
                StringUtils.getConnectStrings(header, h4dAgeBuffer.toString()));
        userInfoListDao.insert(values);
    }

    /**
     * ユーザデータゲッター.
     *
     * @return mUserData
     */
    public List<UserInfoList> getmUserData() {
        return mUserData;
    }

    /**
     * データを読み込んでくる.
     */
    public synchronized void readUserInfoInsertList() {
        DTVTLogger.start();

        //各種オブジェクト作成
        DBHelper userInfoListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = userInfoListDBHelper.getWritableDatabase();
        UserInfoListDao userInfoListDao = new UserInfoListDao(db);

        //データを読み込む
        List<Map<String, String>> readMap = userInfoListDao.findById(DATA_COLUMNS);

        db.close();
        //データを戻す
        mUserData = decodeData(readMap);

        DTVTLogger.end();
    }

    /**
     * DBから読んだデータを構造体に入れる.
     *
     * @param dataList DBから読んだデータ
     * @return ユーザー情報構造体
     */
    private List<UserInfoList> decodeData(final List<Map<String, String>> dataList) {
        List<UserInfoList> dataBuffer = new ArrayList<>();

        boolean h4dFlag;

        for (Map<String, String> map : dataList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //値にh4d用ヘッダーがあれば、それを取り除いてフラグを立てる
                String valueBuffer = entry.getValue();

                //ヌルならば空文字に変更
                if (valueBuffer == null) {
                    valueBuffer = "";
                }

                //h4d用ヘッダーの有無でフラグを設定する
                h4dFlag = valueBuffer.contains(H4D_HEADER);

                //データを各項目に振り分ける
                makeUserInfo(entry.getKey(), valueBuffer, h4dFlag);

            }
        }


        //個々の情報を一つにする
        List<AccountList> lineBuffer;
        lineBuffer = makeLine(mStatus, mDchAge, mH4dAge);
        List<AccountList> lineBufferH4d;
        lineBufferH4d = makeLine(mH4dStatus, mH4dDchAge, mH4dH4dAge);

        //統合して蓄積する
        UserInfoList userInfo = new UserInfoList();
        userInfo.setLoggedinAccount(lineBuffer);
        userInfo.setH4dContractedAccount(lineBufferH4d);
        dataBuffer.add(userInfo);

        return dataBuffer;
    }

    /**
     * キーに応じた値の蓄積を行う.
     *
     * @param keyBuffer キー名
     * @param value     値
     * @param h4dFlag   H4D側のリストならばTRUE
     */
    private void makeUserInfo(final String keyBuffer, final String value, final boolean h4dFlag) {
        //値がヌルならば帰る
        if (value == null) {
            return;
        }

        //分割してリストに変換する
        String[] buffer = value.split(SPLITTER);

        //h4dFlagが立っていた場合は、値からヘッダーを削除する
        if (h4dFlag) {
            for (int i = 0; i < buffer.length; i++) {
                //値からヘッダーを削除
                buffer[i] = buffer[i].replace(H4D_HEADER, "");
            }
        }

        ArrayList<String> bufferArray = new ArrayList<>(Arrays.asList(buffer));

        //キーに応じて、蓄積先を変える
        switch (keyBuffer) {
            case UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS:
                if (h4dFlag) {
                    mH4dStatus = bufferArray;
                } else {
                    mStatus = bufferArray;
                }
                break;

            case UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ:
                if (h4dFlag) {
                    mH4dDchAge = bufferArray;
                } else {
                    mDchAge = bufferArray;
                }
                break;

            case UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ:
                if (h4dFlag) {
                    mH4dH4dAge = bufferArray;
                } else {
                    mH4dAge = bufferArray;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 配列の各情報を一つにまとめる.
     *
     * @param statusList 契約状態のリスト
     * @param dchAgeList dTVチャンネル視聴年齢のリスト
     * @param h4dAgeList ひかりTV for docomo視聴年齢のリスト
     * @return 配列をまとめた情報
     */
    private ArrayList<AccountList> makeLine(final List<String> statusList, final List<String> dchAgeList,
                                                         final List<String> h4dAgeList) {
        //リストを配列にする
        String[] status = statusList.toArray(new String[0]);
        String[] dchAge = dchAgeList.toArray(new String[0]);
        String[] h4dAge = h4dAgeList.toArray(new String[0]);

        //結果用バッファ
        ArrayList<AccountList> lineBuffer = new ArrayList<>();

        //最大値を求める
        int maxLine = maxSize(status.length, dchAge.length, h4dAge.length);
        AccountList account = new AccountList();

        //最大値の数だけ回る
        for (int i = 0; i < maxLine; i++) {
            //存在している情報は追加する
            if (status.length >= i) {
                account.setContractStatus(status[i]);
            }
            if (dchAge.length >= i) {
                account.setDchAgeReq(dchAge[i]);
            }
            if (h4dAge.length >= i) {
                account.setH4dAgeReq(h4dAge[i]);
            }

            lineBuffer.add(account);

        }

        return lineBuffer;
    }

    /**
     * 指定された値の中から最大の物を求める.
     *
     * @param value 複数指定数字
     * @return 指定された数字の中で最大
     */
    private int maxSize(final int... value) {
        int max = 0;

        //総当たりで数字を比べる
        for (int size : value) {
            if (max < size) {
                max = size;
            }
        }
        return max;
    }

    @Override
    protected Void doInBackground(final List<UserInfoList>[] lists) {
        DTVTLogger.start();

        //書き込みと読み込みの切り替えを行う
        if (lists != null) {
            DTVTLogger.debug("lists" + Arrays.toString(lists));
            //データの書き込みを行う
            insertUserInfoInsertList(lists[0]);
        } else {
            //データの読み込みを行う
            readUserInfoInsertList();
        }

        DTVTLogger.end();
        return null;
    }
}