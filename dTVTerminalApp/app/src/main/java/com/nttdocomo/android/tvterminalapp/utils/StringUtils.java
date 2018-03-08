/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.SerializablePreferencesData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * 文字列加工に関する処理を記載する.
 */
public class StringUtils {

    /**
     * タイプ.
     */
    private static String type;

    /**
     * データがないときのDefault値(PG12制限).
     */
    public static final int DEFAULT_USER_AGE_REQ = 8;
    /**
     * データがないときのDefault値(無制限).
     */
    public static final int DEFAULT_R_VALUE = 0;
    /**
     * 年齢制限値=PG12.
     */
    public static final int USER_AGE_REQ_PG12 = 9;
    /**
     * 年齢制限値=R15.
     */
    public static final int USER_AGE_REQ_R15 = 12;
    /**
     * 年齢制限値=R18.
     */
    public static final int USER_AGE_REQ_R18 = 15;
    /**
     * 年齢制限値=R20.
     */
    public static final int USER_AGE_REQ_R20 = 17;
    /**
     * カンマ.
     */
    private static final String COMMA_SEPARATOR = ",";

    /**
     * 暗号化方法(AES).
     */
    private static String CIPHER_TYPE = "AES";
    /**
     * 暗号化方法(AES/ECB/PKCS5Padding).
     */
    private static String CIPHER_DATA = "AES/ECB/PKCS5Padding";
    /**
     * 暗号化キーの長さ.
     */
    private static int CIPHER_KEY_LENGTH = 16;
    /**
     * ハッシュアルゴリズム指定.
     */
    private static final String HASH_ALGORITHME = "SHA-256";

    /**
     * コンストラクタ.
     */
    public StringUtils() {
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
     * フィルタリング文字を返却する.
     *
     * @param context コンテキストファイル
     * @return 伏字
     */
    public static String returnAsterisk(final Context context) {
        return context.getString(R.string.message_three_asterisk);
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
            DTVTLogger.debug(e);
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
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
            DTVTLogger.debug(e);
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
        }
        return null;
    }

    /**
     * 暗号化した文字列を復号する.
     *
     * @param context コンテキスト
     * @param source  暗号文字列
     * @return 元の文字列
     */
    public static String getClearString(final Context context, final String source) {
        if (TextUtils.isEmpty(source)) {
            //元データが空ならば何もできないので、空文字を返す
            return "";
        }

        try {
            // 元文字列をバイト配列へ変換
            byte[] byteSource = source.getBytes(StandardCharsets.UTF_8.name());

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

            return new String(byteResult, StandardCharsets.UTF_8);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException
                | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            DTVTLogger.debug(e);
        }

        //何らかの原因で失敗したので、元の物をそのまま返す
        return source;
    }

    /**
     * 平文のままでは抵抗がある文字列を暗号化する.
     * （ただし、暗号化強度を高める設定は何もしていないので、本当に重要な情報には使用不可）
     *
     * @param context コンテキスト
     * @param source  元の文字列
     * @return 暗号化後文字列
     */
    public static String getCipherString(final Context context, final String source) {
        if (TextUtils.isEmpty(source)) {
            //元データが空ならば何もできないので、空文字を返す
            return "";
        }

        try {
            // 元文字列をバイト配列へ変換
            byte[] byteSource = source.getBytes(StandardCharsets.UTF_8.name());

            //暗号化キーの取得
            String cipherKey = (String) context.getText(R.string.save_token);

            // 暗号化キーをバイト配列へ変換
            byte[] byteKey = cipherKey.getBytes(StandardCharsets.UTF_8.name());

            // 暗号化キー生成
            SecretKeySpec key = new SecretKeySpec(byteKey, 0, CIPHER_KEY_LENGTH, CIPHER_TYPE);

            // 暗号化クラスの初期化（敢えてECB利用しているためSuppress）
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_DATA);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // 暗号化の結果格納
            byte[] byteResult = cipher.doFinal(byteSource);

            // Base64エンコード
            return Base64.encodeToString(byteResult, Base64.DEFAULT);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException
                | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            DTVTLogger.debug(e);
        }

        //何らかの原因で失敗したので、元の物をそのまま返す
        return source;
    }

    /**
     * 評価値の不正を吸収する.
     *
     * @param ratStar 評価値
     * @return 加工済み評価値
     */
    public static String toRatString(final String ratStar) {
        final String MAX_RAT_VALUE = "5.0";
        final String RAT_EXCEPTION_VALUE = "0";
        String strRatStar;
        if (DBUtils.isFloat(ratStar)) {
            float rating = Float.parseFloat(ratStar);
            if (rating >= 5) {
                strRatStar = MAX_RAT_VALUE;
            } else if (rating <= 0) {
                strRatStar = RAT_EXCEPTION_VALUE;
            } else {
                //小数点第2位で四捨五入
                BigDecimal bd = new BigDecimal(ratStar);
                BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                strRatStar = String.valueOf(bd2);
            }
        } else {
            strRatStar = RAT_EXCEPTION_VALUE;
        }
        return strRatStar;
    }

    /**
     * 取得したチャンネル詳細情報の日付データを返却する.
     *
     * @param str データ取得を行った日付文字列.
     * @return 日付データ.
     */
    public static String getChDateInfo(final String str) {
        String chInfoDate;
        if (str.equals(WebApiBasePlala.DATE_NOW)) {
            //取得日時が"now"であれば今日の日付を取得.
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat todaySdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD, Locale.JAPAN);
            chInfoDate = todaySdf.format(calendar.getTime());
            chInfoDate = chInfoDate.replace("/", "");
        } else {
            chInfoDate = str;
        }
        return chInfoDate;
    }

    /**
     * 文字列から ハッシュ値を取得する.
     *
     * @param value 文字列
     * @return ハッシュ値
     */
    public static String toHashValue(final String value) {
        byte[] hashValue;
        StringBuilder sb = new StringBuilder();

        hashValue = toHashBytes(value);
        if (hashValue == null) {
            DTVTLogger.debug("hash value is null");
            return null;
        }
        for (byte hb : hashValue) {
            String hex = String.format("%02x", hb);
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * ハッシュ化したソルトを取得.
     * ※ハッシュアルゴリズム：SHA-256
     *
     * @param salt ソルト
     * @return ハッシュ化したバイト配列
     */
    private static byte[] toHashBytes(final String salt) {
        MessageDigest digest;

        if (salt == null) {
            return (byte[]) null;
        }
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHME);
        } catch (NoSuchAlgorithmException e) {
            DTVTLogger.debug(e);
            return (byte[]) null;
        }
        digest.update(salt.getBytes(StandardCharsets.UTF_8));
        return digest.digest();
    }

    /**
     * 構造体をSharedPreferences保存するためにエンコードする.
     *
     * @param serializablePreferencesData ビデオジャンル一覧
     * @return エンコードデータ
     */
    public static String toPreferencesDataBase64(final SerializablePreferencesData serializablePreferencesData) {

        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializablePreferencesData);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            byte[] base64 = Base64.encode(bytes, Base64.NO_WRAP);

            return new String(base64);
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
        }

        return null;
    }

    /**
     * SharedPreferencesにエンコード保存した構造体をデコードする.
     *
     * @param base64 エンコードデータ
     * @return デコードデータ
     */
    public static SerializablePreferencesData toPreferencesData(final String base64) {

        if (base64 == null) {
            return null;
        }

        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;

        byte[] bytes = Base64.decode(base64.getBytes(), Base64.NO_WRAP);

        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (SerializablePreferencesData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            DTVTLogger.debug(e);
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
        }
        return null;
    }
}
