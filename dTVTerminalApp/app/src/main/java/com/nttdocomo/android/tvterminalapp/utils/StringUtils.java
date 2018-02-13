/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.util.Base64;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * 文字列加工に関する処理を記載する.
 */
public class StringUtils {

    private static String type;
    private final Context mContext;

    //データがないときのDefault値(PG12制限)
    public static final int DEFAULT_USER_AGE_REQ = 8;
    //データがないときのDefault値(無制限)
    public static final int DEFAULT_R_VALUE = 0;
    //年齢制限値=PG12
    public static final int USER_AGE_REQ_PG12 = 9;
    //年齢制限値=R15
    public static final int USER_AGE_REQ_R15 = 12;
    //年齢制限値=R18
    public static final int USER_AGE_REQ_R18 = 15;
    //年齢制限値=R20
    public static final int USER_AGE_REQ_R20 = 17;
    //契約情報
    public static final String CONTRACT_INFO_NONE = "none";

    //カンマ
    private static final String COMMA_SEPARATOR = ",";

    //暗号化方法
    private static String CIPHER_TYPE = "AES";
    private static String CIPHER_DATA = "AES/ECB/PKCS5Padding";

    //暗号化キーの長さ
    private static int CIPHER_KEY_LENGTH = 16;

    public StringUtils(final Context context) {
        mContext = context;
    }

    /**
     * サービスID(dTV関連)に応じたサービス名を返す.
     *
     * @param id サービスID
     * @return サービス名
     */
    public int getContentsServiceName(final int id) {
        switch (id) {
            case ContentDetailActivity.DTV_CONTENTS_SERVICE_ID:
                return R.mipmap.label_service_dtv;
            case ContentDetailActivity.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                return R.mipmap.label_service_dch;
            case ContentDetailActivity.D_ANIMATION_CONTENTS_SERVICE_ID:
                return R.mipmap.label_service_danime;
        }
        return R.mipmap.label_service_hikari;
    }

    /**
     * 文字列を連結する.
     *
     * @param strings 連結したい文字列配列
     * @return 連結後の文字列
     */
    public static String getConnectString(final String[] strings) {
        StringBuilder builder = new StringBuilder();
        String conString;
        for (String string : strings) {
            builder.append(string);
        }
        conString = builder.toString();
        return conString;
    }

    /**
     * 文字列を連結する・可変長引数版（言語仕様の都合で、オーバーロードの同じ名前にはできない）.
     *
     * @param strings 連結したい文字列を必要な数だけ指定する。
     * @return 連結後の文字列
     */
    public static String getConnectStrings(final String... strings) {
        //同じ名前にはできないが、処理は委譲する。
        return getConnectString(strings);
    }

    /**
     * JSonArrayを文字列配列に変換する.
     *
     * @param jsonArray JsonArray
     * @return 文字列配列
     */
    public static String[] JSonArray2StringArray(final JSONArray jsonArray) {
        //出力文字列
        String[] stringArr;

        //JsonArrayがヌルの場合やJSONArrayではない場合は、長さゼロの配列を返す
        if (jsonArray == null) {
            return new String[0];
        }

        //jsonArrayの要素数で配列を宣言
        stringArr = new String[jsonArray.length()];

        //jsonArrayの要素数だけ回る
        for (int i = 0; i < stringArr.length; i++) {
            if (jsonArray.optString(i) == null) {
                //無いはずだが、ヌルならば空文字にする
                stringArr[i] = "";
            } else {
                //JsonArrayの中身を移す
                stringArr[i] = jsonArray.optString(i);
            }
        }

        return stringArr;
    }

    /**
     * 与えられたオブジェクトをチェックし、長整数に変換する.
     *
     * @param data オブジェクト
     * @return 長整数変換後の値。変換できなければゼロ
     */
    public static long changeString2Long(final Object data) {
        //既に数値かどうかを判定
        if (data instanceof Long) {
            //長整数なのでそのまま返す
            return (long) data;
        }

        if (data instanceof Integer) {
            //整数なので長整数に変換
            return ((Integer) data).longValue();
        }

        //数字の文字列かどうかの判定
        if (data != null && data instanceof String && DBUtils.isNumber((String) data)) {
            //数字文字列だったので、変換して返す
            return Long.parseLong((String) data);
        }

        //変換できなかったのでゼロ
        return 0;
    }

    /**
     * 与えられたオブジェクトをチェックし、整数に変換する.
     *
     * @param data オブジェクト
     * @return 整数変換後の値。変換できなければゼロ
     */
    public static int changeString2Int(final Object data) {
        //既に数値かどうかを判定
        if (data instanceof Integer) {
            //整数なのでそのまま返す
            return (Integer) data;
        }

        if (data instanceof Long) {
            //長整数なので整数に変換
            return ((Long) data).intValue();
        }

        //数字の文字列かどうかの判定
        if (data != null && data instanceof String && DBUtils.isNumber((String) data)) {
            //数字文字列だったので、変換して返す
            return Integer.parseInt((String) data);
        }

        //変換できなかったのでゼロ
        return 0;
    }

    /**
     * 与えられたオブジェクトをチェックし、文字列に変換する.
     *
     * @param data オブジェクト
     * @return 変換後の文字列。変換できなければ空文字
     */
    public static String changeObject2String(final Object data) {
        //Stringかどうかを見る
        if (data instanceof String) {
            //そのまま返す
            return (String) data;
        }

        //intかどうかを判定
        if (data instanceof Integer) {
            //intを文字に変換して返す
            return String.valueOf((int) data);
        }

        //longかどうかを判定
        if (data instanceof Long) {
            //longを文字に変換して返す
            return String.valueOf((long) data);
        }

        //doubleかどうかを判定
        if (data instanceof Double) {
            //doubleを文字に変換して返す
            return String.valueOf((double) data);
        }

        //変換できなかったので空文字
        return "";
    }

    /**
     * ひかりコンテンツ判定(ひかり内DTVのみ).
     *
     * @param type コンテンツタイプ
     * @return ひかり内DTVフラグ
     */
    public static boolean isHikariInDtvContents(final String type) {
        boolean isHikariIn = false;
        if (type != null && (type.equals(WebApiBasePlala.CLIP_TYPE_DTV_VOD))) {
            isHikariIn = true;
        }
        return isHikariIn;
    }

    /**
     * ひかりコンテンツ判定.
     *
     * @param contentsType コンテンツタイプ
     * @return ひかりコンテンツ：true ,ひかり以外：false
     */
    public static boolean isHikariContents(final String contentsType) {
        boolean isHikari = false;
        if (contentsType != null && (contentsType.equals(WebApiBasePlala.CLIP_TYPE_H4D_IPTV)
                || contentsType.equals(WebApiBasePlala.CLIP_TYPE_H4D_VOD)
                || contentsType.equals(WebApiBasePlala.CLIP_TYPE_DCH))) {
            isHikari = true;
        }
        return isHikari;
    }

    /**
     * 年齢パレンタル値(R_VALUE)を数値に変換.
     *
     * @param context  コンテクストファイル
     * @param ageValue 年齢パレンタル値(R_VALUE)
     * @return 年齢情報
     */
    public static int convertRValueToAgeReq(final Context context, final String ageValue) {

        int ageReq = DEFAULT_R_VALUE;
        if (ageValue != null) {
            if (ageValue.equals(context.getString(R.string.parental_pg_12))) {
                ageReq = USER_AGE_REQ_PG12;
            } else if (ageValue.equals(context.getString(R.string.parental_r_15))) {
                ageReq = USER_AGE_REQ_R15;
            } else if (ageValue.equals(context.getString(R.string.parental_r_18))) {
                ageReq = USER_AGE_REQ_R18;
            } else if (ageValue.equals(context.getString(R.string.parental_r_20))) {
                ageReq = USER_AGE_REQ_R20;
            }
        }
        return ageReq;
    }

    /**
     * ユーザ情報から年齢情報を取得する.
     *
     * @param userInfoList ユーザ情報
     * @return 年齢パレンタル情報
     */
    public static int getUserAgeInfo(final List<Map<String, String>> userInfoList) {
        final int INT_LIST_HEAD = 0;
        String age = null;

        //ユーザ情報がないときはPG12制限値を返却
        if (userInfoList == null || userInfoList.size() < 1) {
            return DEFAULT_USER_AGE_REQ;
        }

        Map<String, String> infoMap = userInfoList.get(INT_LIST_HEAD);

        //ユーザ情報がないときはPG12制限値を返却
        if (infoMap == null) {
            return DEFAULT_USER_AGE_REQ;
        }

        String contractStatus = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS);

        //contractStatusがないときはPG12制限値を設定
        int intAge = DEFAULT_USER_AGE_REQ;
        if (contractStatus != null) {
            if (contractStatus.equals(UserInfoDataProvider.CONTRACT_STATUS_DTV)) {
                //H4Dの制限情報がないときはDCH側を使用
                age = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ);
                if (age == null || age.length() < 1) {
                    age = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ);
                }
            } else if (contractStatus.equals(UserInfoDataProvider.CONTRACT_STATUS_H4D)) {
                //DCHの制限情報がないときはH4D DCH側を使用
                age = infoMap.get(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ);
            }
        }
        //年齢情報が数字ならINTに変換
        if (DBUtils.isNumber(age)) {
            intAge = Integer.parseInt(age);
        }
        return intAge;
    }

    /**
     * フィルタリング文字を返却する.
     *
     * @param context コンテキストファイル
     * @return 伏字
     */
    public static String returnAsterisk(final Context context) {
        return context.getString(R.string.message_three_asterisk);
    }

    /**
     * 契約情報を取得する.
     *
     * @param userInfoList ユーザーデータ
     * @return 契約しているかどうか
     */
    public static String getUserContractInfo(final List<UserInfoList> userInfoList) {
        final int INT_LIST_HEAD = 0;
        final String CONTRACT_DTV = "001";
        final String CONTRACT_H4D = "002";
        String contractStatus;

        //ユーザ情報がないときは契約情報は無し
        if (userInfoList == null || userInfoList.size() < 1) {
            return CONTRACT_INFO_NONE;
        }

        UserInfoList infoList = userInfoList.get(INT_LIST_HEAD);

        //ユーザ情報がないときは契約情報は無し
        if (infoList == null) {
            return CONTRACT_INFO_NONE;
        }

        List<UserInfoList.AccountList> mLoggedInAccountList = infoList.getLoggedinAccount();

        //ログインユーザ情報がないときは契約情報は無し
        if (mLoggedInAccountList == null || userInfoList.size() < 1) {
            return CONTRACT_INFO_NONE;
        }

        UserInfoList.AccountList mLoggedInAccount = mLoggedInAccountList.get(INT_LIST_HEAD);

        //ユーザ情報がないときは契約情報は無し
        if (mLoggedInAccount == null) {
            return CONTRACT_INFO_NONE;
        }

        // 契約中であれば契約情報を返す
        contractStatus = mLoggedInAccount.getContractStatus();
        if (contractStatus.equals(CONTRACT_DTV) || contractStatus.equals(CONTRACT_H4D)) {
            return contractStatus;
        }

        return CONTRACT_INFO_NONE;
    }

    /**
     * UserInfoListのリストから年齢情報を取得する.
     *
     * @param userInfoLists ユーザー情報のリスト
     * @return 年齢情報
     */
    public static int getUserAgeInfoWrapper(final List<UserInfoList> userInfoLists) {
        //変換後のユーザー情報
        List<Map<String, String>> newUserInfoLists = new ArrayList<>();

        if (userInfoLists != null) {
            //ユーザー情報の数だけ回る
            for (UserInfoList userInfo : userInfoLists) {
                Map<String, String> dataBuffer1 = getAccountList(userInfo.getLoggedinAccount());
                Map<String, String> dataBuffer2 = getAccountList(userInfo.getH4dContractedAccount());

                newUserInfoLists.add(dataBuffer1);
                newUserInfoLists.add(dataBuffer2);
            }
        }
        //年齢情報の取得
        return getUserAgeInfo(newUserInfoLists);
    }

    /**
     * 契約情報をマップで蓄積.
     *
     * @param accountBuffers 契約情報リスト
     * @return マップ化契約情報
     */
    private static Map<String, String> getAccountList(
            final List<UserInfoList.AccountList> accountBuffers) {
        Map<String, String> buffer = new HashMap<>();

        //契約情報の数だけ回ってマップに変換する
        for (UserInfoList.AccountList accountBuffer : accountBuffers) {
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ,
                    accountBuffer.getDchAgeReq());
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ,
                    accountBuffer.getH4dAgeReq());
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT, null);
            buffer.put(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS,
                    accountBuffer.getContractStatus());
        }

        return buffer;
    }

    /**
     * 文字列リストをカンマ区切りで返す.
     *
     * @param strings 変換対象
     * @return 変換文字列
     */
    public static String setCommaSeparator(final ArrayList<String> strings) {
        String result;
        StringBuilder builder = new StringBuilder();
        if (strings != null && strings.size() > 0) {
            for (int i = 0; i < strings.size(); i++) {
                if (i == strings.size() - 1) {
                    builder.append(strings.get(i));
                    break;
                } else {
                    builder.append(strings.get(i));
                    builder.append(COMMA_SEPARATOR);
                }
            }
        }
        result = builder.toString();
        return result;
    }

    /**
     * ビデオジャンルレスポンスをSharedPreferences保存するためにエンコードする.
     *
     * @param genreListResponse ビデオジャンル一覧
     * @return エンコードデータ
     */
    public static String toGenreListResponseBase64(final GenreListResponse genreListResponse) {

        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(genreListResponse);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            byte[] base64 = Base64.encode(bytes, Base64.NO_WRAP);

            return new String(base64);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * SharedPreferencesにエンコード保存したビデオジャンルレスポンスをデコードする.
     *
     * @param base64 エンコードデータ
     * @return デコードデータ
     */
    public static GenreListResponse toGenreListResponse(final String base64) {

        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;

        byte[] bytes = Base64.decode(base64.getBytes(), Base64.NO_WRAP);

        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (GenreListResponse) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 暗号化した文字列を復号する.
     *
     * @param context コンテキスト
     * @param source 暗号文字列
     * @return 元の文字列
     */
    public static String getClearString(Context context, String source) {
        try {
            // 元文字列をバイト配列へ変換
            byte[] byteSource = new byte[0];
            byteSource = source.getBytes(StandardCharsets.UTF_8.name());

            //base64デコード
            byte[] decodeSource = Base64.decode(byteSource, Base64.DEFAULT);

            //暗号化キーをバイト配列へ変換
            String cipherKey = (String) context.getText(R.string.save_token);
            byte[] byteKey = cipherKey.getBytes(StandardCharsets.UTF_8.name());

            // 暗号化情報生成
            SecretKeySpec key = new SecretKeySpec(byteKey, 0, CIPHER_KEY_LENGTH, CIPHER_TYPE);

            // 暗号化クラスの初期化
            Cipher cipher = Cipher.getInstance(CIPHER_DATA);
            cipher.init(Cipher.DECRYPT_MODE, key);

            // 暗号化の結果格納
            byte[] byteResult = cipher.doFinal(decodeSource);

            //文字列に変換する
            String stringResult = new String(byteResult,StandardCharsets.UTF_8);

            return stringResult;

        } catch (UnsupportedEncodingException e) {
            DTVTLogger.debug(e);
        } catch (NoSuchAlgorithmException e) {
            DTVTLogger.debug(e);
        } catch (InvalidKeyException e) {
            DTVTLogger.debug(e);
        } catch (NoSuchPaddingException e) {
            DTVTLogger.debug(e);
        } catch (BadPaddingException e) {
            DTVTLogger.debug(e);
        } catch (IllegalBlockSizeException e) {
            DTVTLogger.debug(e);
        }

        //何らかの原因で失敗したので、元の物をそのまま返す
        return source;
    }

    /**
     * 平文のままでは抵抗がある文字列を暗号化する.
     * （ただし、暗号化強度を高める設定は何もしていないので、本当に重要な情報には使用不可）
     *
     * @param source 元の文字列
     * @return 暗号化後文字列
     */
    public static String getCipherString(Context context, String source) {
        try {
            // 元文字列をバイト配列へ変換
            byte[] byteSource = new byte[0];
            byteSource = source.getBytes(StandardCharsets.UTF_8.name());

            //暗号化キーの取得
            String cipherKey = (String) context.getText(R.string.save_token);

            // 暗号化キーをバイト配列へ変換
            byte[] byteKey = cipherKey.getBytes(StandardCharsets.UTF_8.name());

            // 暗号化キー生成
            SecretKeySpec key = new SecretKeySpec(byteKey, 0, CIPHER_KEY_LENGTH, CIPHER_TYPE);

            // 暗号化クラスの初期化
            Cipher cipher = Cipher.getInstance(CIPHER_DATA);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // 暗号化の結果格納
            byte[] byteResult = cipher.doFinal(byteSource);

            // Base64エンコード
            String stringResult = Base64.encodeToString(byteResult,Base64.DEFAULT);

            return stringResult;

        } catch (UnsupportedEncodingException e) {
            DTVTLogger.debug(e);
        } catch (NoSuchAlgorithmException e) {
            DTVTLogger.debug(e);
        } catch (InvalidKeyException e) {
            DTVTLogger.debug(e);
        } catch (NoSuchPaddingException e) {
            DTVTLogger.debug(e);
        } catch (BadPaddingException e) {
            DTVTLogger.debug(e);
        } catch (IllegalBlockSizeException e) {
            DTVTLogger.debug(e);
        }

        //何らかの原因で失敗したので、元の物をそのまま返す
        return source;
    }
}
