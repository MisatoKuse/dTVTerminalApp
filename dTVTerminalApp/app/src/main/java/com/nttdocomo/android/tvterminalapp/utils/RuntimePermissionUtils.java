/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * RuntimePermission関連のUtils クラス.
 */
public class RuntimePermissionUtils {

    /**
     * コンストラクタ.
     */
    private RuntimePermissionUtils() {
    }

    /**
     * Permissionが与えられているか確認を行うクラス.
     *
     * @param context コンテキスト
     * @param permissions 確認するPermission
     * @return Permissionが与えられていればtrue、そうでなければfalse
     */
    public static boolean hasSelfPermissions(final Context context, final String... permissions) {
        //Android6.0未満ではtrueを返す
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Permission要求後、Permissionが付与されたかどうかを確認する.
     *
     * @param grantResults Permission要求後の返り値
     * @return Permissionが与えられていればtrue、そうでなければfalse
     */
    public static boolean checkGrantResults(final int... grantResults) {
        //引数が0個の場合はfalseを返す
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 「今後は確認しない」にチェックが入っているかどうかを確認する.
     *
     * @param activity アクティビティ
     * @param permission 確認対象のPermission
     * @return falseであれば「今後は確認しない」にチェックが入っている
     */
    public static boolean shouldShowRequestPermissionRationale(final Activity activity, final String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return true;
    }
}
