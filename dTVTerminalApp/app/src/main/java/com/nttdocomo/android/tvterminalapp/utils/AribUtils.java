/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.util.SparseArray;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * Arib外字変換のUtils クラス.
 */
public class AribUtils {
    /**
     * ARIB外字変換キー配列.
     */
    private static final int[] CONVERT_KEY = {
            0x2014, 0x301C, 0x2016, 0x2212, 0x4EFD, 0x4EFF, 0x509C, 0x511E, 0x51BC, 0x5361, 0x536C,
            0x544D, 0x5496, 0x550E, 0x554A, 0x5672, 0x56E4, 0x5733, 0x5734, 0x5880, 0x59E4, 0x5A23,
            0x5A55, 0x5EAC, 0x6017, 0x66C8, 0x6911, 0x693B, 0x6A45, 0x6A91, 0x6BF1, 0x6CE0, 0x6D2E,
            0x6DBF, 0x6DCA, 0x6F5E, 0x6FF9, 0x7064, 0x7200, 0x739F, 0x73A8, 0x741B, 0x7421, 0x742C,
            0x7439, 0x744B, 0x7575, 0x7581, 0x7772, 0x78C8, 0x78E0, 0x79DA, 0x7A1E, 0x7B7F, 0x7C31,
            0x7D8B, 0x8118, 0x813A, 0x82AE, 0x85CE, 0x87EC, 0x880B, 0x8DCE, 0x8FF6, 0x90DD, 0x91B2,
            0x9233, 0x96DE, 0x9940, 0x9DD7, 0x9EB4, 0x9EB5, 0xE0C9, 0xE0CA, 0xE0CB, 0xE0CC, 0xE0CD,
            0xE0CE, 0xE0D0, 0xE0D1, 0xE0D2, 0xE0D3, 0xE0D8, 0xE0D9, 0xE0DC, 0xE0DD, 0xE0DE, 0xE0DF,
            0xE0E0, 0xE0E1, 0xE0E2, 0xE0E3, 0xE0E4, 0xE0E5, 0xE0E6, 0xE0E7, 0xE0E8, 0xE0E9, 0xE0EA,
            0xE0EB, 0xE0EC, 0xE0ED, 0xE0EE, 0xE0EF, 0xE0F0, 0xE0F5, 0xE0F6, 0xE0F7, 0xE0F8, 0xE0F9,
            0xE0FA, 0xE0FB, 0xE0FC, 0xE0FD, 0xE0FE, 0xE0FF, 0xE180, 0xE181, 0xE182, 0xE183, 0xE184,
            0xE185, 0xE186, 0xE187, 0xE188, 0xE189, 0xE18A, 0xE18B, 0xE18C, 0xE18D, 0xE18E, 0xE18F,
            0xE190, 0xE191, 0xE192, 0xE193, 0xE194, 0xE195, 0xE196, 0xE197, 0xE198, 0xE199, 0xE19A,
            0xE19B, 0xE19C, 0xE1A7, 0xE1A8, 0xE1A9, 0xE1AA, 0xE1AB, 0xE1AC, 0xE1AD, 0xE1AE, 0xE1AF,
            0xE1B0, 0xE1B1, 0xE1B2, 0xE1B3, 0xE1B4, 0xE1B5, 0xE1B6, 0xE1B7, 0xE1B8, 0xE1B9, 0xE1BA,
            0xE1BB, 0xE1BC, 0xE1BD, 0xE1BE, 0xE1BF, 0xE1C0, 0xE1C1, 0xE1C2, 0xE1C3, 0xE1C4, 0xE1C5,
            0xE1C6, 0xE1C7, 0xE1C8, 0xE1C9, 0xE1CA, 0xE1CB, 0xE1CC, 0xE1CD, 0xE1CE, 0xE1CF, 0xE1D0,
            0xE1D1, 0xE1D2, 0xE1D3, 0xE1D4, 0xE1D5, 0xE1D6, 0xE1D7, 0xE285, 0xE286, 0xE287, 0xE288,
            0xE289, 0xE28A, 0xE28B, 0xE28C, 0xE28D, 0xE28E, 0x33A1, 0x33A5, 0x339D, 0x33A0, 0x33A4,
            0xE28F, 0x2488, 0x2489, 0x248A, 0x248B, 0x248C, 0x248D, 0x248E, 0x248F, 0x2490, 0xE290,
            0xE291, 0xE292, 0xE293, 0xE294, 0xE295, 0xE296, 0xE297, 0xE298, 0xE299, 0xE29A, 0xE29B,
            0xE29C, 0xE29D, 0xE29E, 0xE29F, 0x3233, 0x3236, 0x3232, 0x3231, 0x3239, 0xE2A0, 0x25B6,
            0x25C0, 0x3016, 0x3017, 0xE2A1, 0xE2A2, 0xE2A3, 0xE2A4, 0xE2A5, 0xE2A6, 0xE2A7, 0xE2A8,
            0xE2A9, 0xE2AA, 0xE2AB, 0xE2AC, 0xE2AD, 0xE2AE, 0xE2AF, 0xE2B0, 0xE2B1, 0xE2B2, 0xE2B3,
            0xE2B4, 0xE2B5, 0xE2B6, 0xE2B7, 0xE2B8, 0xE2B9, 0xE2BA, 0xE2BB, 0xE2BC, 0xE2BD, 0xE2BE,
            0xE2BF, 0xE2C0, 0xE2C1, 0xE2C2, 0x00AE, 0x00A9, 0xE2C3, 0xE2C4, 0xE2C5, 0xE2C6, 0x322A,
            0x322B, 0x322C, 0x322D, 0x322E, 0x322F, 0x3230, 0x3237, 0x337E, 0x337D, 0x337C, 0x337B,
            0xE2CA, 0xE2CB, 0x3036, 0xE2CC, 0xE2CD, 0xE2CE, 0xE2CF, 0xE2D0, 0xE2D1, 0xE2D2, 0xE2D3,
            0xE2D4, 0xE2D5, 0xE2D6, 0xE2D7, 0xE2D8, 0xE2D9, 0xE2DA, 0xE2DB, 0xE2DC, 0xE2DD, 0xE2DE,
            0xE2DF, 0xE2E0, 0xE2E1, 0xE2E2, 0x2113, 0x338F, 0x3390, 0x33CA, 0x339E, 0x33A2, 0x3371,
            0x00BD, 0xE2E5, 0x2153, 0x2154, 0x00BC, 0x00BE, 0x2155, 0x2156, 0x2157, 0x2158, 0x2159,
            0x215A, 0xE2E6, 0x215B, 0xE2E7, 0xE2E8, 0x2600, 0x2601, 0x2602, 0xE2E9, 0xE2EA, 0xE2EB,
            0xE2EC, 0xE2ED, 0x2666, 0x2665, 0x2663, 0x2660, 0xE2EE, 0xE2EF, 0x203C, 0xE2F0, 0xE2F1,
            0xE2F2, 0xE2F3, 0xE2F4, 0xE2F5, 0xE2F6, 0xE2F7, 0xE2F9, 0xE2FA, 0x266C, 0xE2FB, 0x2160,
            0x2161, 0x2162, 0x2163, 0x2164, 0x2165, 0x2166, 0x2167, 0x2168, 0x2169, 0x216A, 0x216B,
            0x2470, 0x2471, 0x2472, 0x2473, 0x2474, 0x2475, 0x2476, 0x2477, 0x2478, 0x2479, 0x247A,
            0x247B, 0x247C, 0x247D, 0x247E, 0x247F, 0xE383, 0xE384, 0xE385, 0xE386, 0xE387, 0xE388,
            0xE389, 0xE38A, 0xE38B, 0xE38C, 0xE38D, 0xE38E, 0xE38F, 0xE390, 0xE391, 0xE392, 0xE393,
            0xE394, 0xE395, 0xE396, 0xE397, 0xE398, 0xE399, 0xE39A, 0xE39B, 0xE39C, 0x2460, 0x2461,
            0x2462, 0x2463, 0x2464, 0x2465, 0x2466, 0x2467, 0x2468, 0x2469, 0x246A, 0x246B, 0x246C,
            0x246D, 0x246E, 0x246F, 0x2776, 0x2777, 0x2778, 0x2779, 0x277A, 0x277B, 0x277C, 0x277D,
            0x277E, 0x277F,
    };

    /**
     * ARIB外字変換結果配列.
     */
    private static final String[] CONVERT_RESULT = {
            "―", "～", "∥", "－", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□",
            "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□",
            "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□",
            "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□",
            "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□", "□",
            "□", "□", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "10", "11", "12", "(HV)", "(SD)", "(P)", "(W)", "(MV)",
            "(手)", "(字)", "(双)", "(デ)", "(S)", "(二)", "(多)", "(解)", "(SS)", "(B)",
            "(N)", "■", "●", "(天)", "(交)", "(映)", "(無)", "(料)", "(鍵)", "(前)", "(後)",
            "(再)", "(新)", "(初)", "(終)", "(生)", "(販)", "(声)", "(吹)", "(PPV)", "(秘)",
            "他", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[記号]", "[記号]", "→", "←", "↑", "↓", "[記号]", "[記号]", "年", "月", "日",
            "円", "m2", "m3", "cm", "cm2", "cm3", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "氏", "副", "元", "故", "前", "新", "0,", "1,", "2,", "3,", "4,", "5,", "6,",
            "7,", "8,", "9,", "(社)", "(財)", "(有)", "(株)", "(代)", "(問)", "[記号]",
            "[記号]", "【", "】", "[記号]", "2", "3", "(CD)", "(vn)", "(ob)", "(cb)", "(ce",
            "mb)", "(hp)", "(br)", "(p)", "(s)", "(ms)", "(t)", "(bs)", "(b)", "(tb)",
            "(tp)", "(ds)", "(ag)", "(eg)", "(vo)", "(fl)", "(ke", "y)", "(sa", "x)", "(sy",
            "n)", "(or", "g)", "(pe", "r)", "(R)", "(C)", "(箏)", "DJ", "[演]", "FAX",
            "(月)", "(火)", "(水)", "(木)", "(金)", "(土)", "(日)", "(祝)", "明治", "大正",
            "昭和", "平成", "No.", "TEL", "〒", "[記号]", "[本]", "[三]", "[二]", "[安]",
            "[点]", "[打]", "[盗]", "[勝]", "[敗]", "[S]", "【投】", "【捕】", "【一】",
            "【二】", "【三】", "【遊】", "【左】", "【中】", "【右】", "【指】", "【走】",
            "【打】", "ｌ", "kg", "Hz", "ha", "km", "km2", "hPa", "1/2", "0/3", "1/3", "2/3",
            "1/4", "3/4", "1/5", "2/5", "3/5", "4/5", "1/6", "5/6", "1/7", "1/8", "1/9",
            "1/10", "[晴]", "[曇]", "[雨]", "[雪]", "[記号]", "[記号]", "[記号]", "[記号]",
            "[ダイヤ]", "[ハート]", "[クラブ]", "[スペード]", "[記号]", "[霧]", "!!", "!?",
            "[曇／晴]", "[にわか雨]", "[雨]", "[雪]", "[大雪]", "[雷]", "[雷雨]", "[記号]",
            "[記号]", "[音符]", "[電話]", "I", "II", "III", "IV", "V", "VI", "VII", "VIII",
            "IX", "X", "XI", "XII", "[17]", "[18]", "[19]", "[20]", "(1)", "(2)", "(3)",
            "(4)", "(5)", "(6)", "(7)", "(8)", "(9)", "(10)", "(11)", "(12)", "(A)", "(B)",
            "(C)", "(D)", "(E)", "(F)", "(G)", "(H)", "(I)", "(J)", "(K)", "(L)", "(M)",
            "(N)", "(O)", "(P)", "(Q)", "(R)", "(S)", "(T)", "(U)", "(V)", "(W)", "(X)",
            "(Y)", "(Z)", "[1]", "[2]", "[3]", "[4]", "[5]", "[6]", "[7]", "[8]", "[9]",
            "[10]", "[11]", "[12]", "[13]", "[14]", "[15]", "[16]", "【1】", "【2】",
            "【3】", "【4】", "【5】", "【6】", "【7】", "【8】", "【9】", "【10】",
    };

    /**
     * 変換表.
     */
    private SparseArray<String> mAribConvert = null;

    /**
     * コンストラクタ
     */
    public AribUtils() {
        //変換のキーと値の配列を検索可能にして蓄積する
        mAribConvert = new SparseArray<>(CONVERT_KEY.length);

        for (int counter = 0; counter < CONVERT_KEY.length; counter++) {
            mAribConvert.put(CONVERT_KEY[counter], CONVERT_RESULT[counter]);
        }
    }

    /**
     * 文字列にARIB外字が格納されていた場合、変換表に従って文字を置き換える.
     *
     * @param source 変換元の文字列
     * @return 変換後の文字列
     */
    public String convertAribGaiji(String source) {
        DTVTLogger.start();

        //変換元がヌルならば、ヌルを返す
        if (source == null) {
            return null;
        }

        //変換結果用
        StringBuilder convertedString = new StringBuilder();

        //文字単位の配列に分解する
        char[] stringArray = source.toCharArray();

        //ユニコード・コードポイント
        int codePoint;

        //文字数だけ回る（増分はコードポイントを見て決定）
        for (int counter = 0; counter < source.length();
             counter += Character.charCount(codePoint)) {

            //ユニコードのコードポイントを取得
            codePoint = Character.codePointAt(stringArray, counter);

            //該当文字の変換
            String converted = mAribConvert.get(codePoint);

            //変換成功チェック
            if (converted != null) {
                //変換対象が存在したので変換後の値を蓄積
                convertedString.append(converted);
            } else {
                //値がヌル＝変換対象が存在しないので、変換前の値をそのまま蓄積
                convertedString.append(String.valueOf(Character.toChars(codePoint)));
            }
        }

        DTVTLogger.end();
        //処理後の文字列を返却
        return convertedString.toString();
    }
}
