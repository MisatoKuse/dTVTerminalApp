/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.activation;

import android.content.Context;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.digion.dixim.android.util.FileSystemUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class NewEnvironmentUtil extends EnvironmentUtil {

    private static String getDataApkCmWorkName(final EnvironmentUtil.ACTIVATE_DATA_HOME dataHome) {
        String cmWorkName;
        switch (dataHome) {
            case DMP:
                cmWorkName = "cm_work_dmp";
                break;
            case DMS:
                cmWorkName = "cm_work_dms";
                break;
            case PLAYER:
                cmWorkName = "cm_work_player";
                break;
            case NONE:
            default:
                throw new IllegalArgumentException("invalid ACTIVATE_DATA_HOME.");
        }

        return cmWorkName;
    }

    private static String getPrivateDataHome(final String destDataHomeDir, final EnvironmentUtil.ACTIVATE_DATA_HOME dataHomeEnum) {
        File f = new File(destDataHomeDir, getDataApkCmWorkName(dataHomeEnum));
        if (!f.exists()) {
            boolean r = f.mkdirs();
            if (!r) {
                return null;
            }
        }
        return f.getAbsolutePath();
    }

    /**
     * copy Device Key From Other CM Work.
     * @param context context
     * @param destDataHomeDir destDataHomeDir
     * @param dataHomeEnum dataHomeEnum
     * @return 3: 成功   1: 既にある  0: 失敗
     */
    public static int copyDeviceKeyFromOtherCMWork(final Context context, final String destDataHomeDir, final EnvironmentUtil.ACTIVATE_DATA_HOME dataHomeEnum) {
        File currentCMWorkDir;
        File currentDeviceKeyFile;

        String pdhHome = getPrivateDataHome(destDataHomeDir, dataHomeEnum);
        if (null == pdhHome) {
            return 0;
        }
        currentCMWorkDir = new File(pdhHome);
        currentDeviceKeyFile = new File(currentCMWorkDir, "db_post");
        if (currentDeviceKeyFile.exists()) {
            return 1;
        }

        List allDirs = getPrivateDataHomeAllDir(context);
        Iterator var5 = allDirs.iterator();

        String absPath;
        String absPath2;

        File foundDeviceKey;
        do {
            File otherCMWorkDir;
            do {
                do {
                    if (!var5.hasNext()) {
                        return 2;
                    }

                    String dir = (String) var5.next();
                    otherCMWorkDir = new File(dir);

                    absPath = otherCMWorkDir.getAbsolutePath();
                    absPath2 = currentCMWorkDir.getAbsolutePath();
                } while (absPath.equals(absPath2));

                if (otherCMWorkDir.exists()) {
                    break;
                }

                boolean r = otherCMWorkDir.mkdirs();
                if (!r) {
                    DTVTLogger.debug("mkdir failed");
                }
            } while (!otherCMWorkDir.exists());

            foundDeviceKey = new File(otherCMWorkDir, "db_post");
        } while (!foundDeviceKey.exists());

        FileSystemUtil.copyFile(foundDeviceKey, currentDeviceKeyFile);
        if (currentDeviceKeyFile.exists()) {
            return 3;
        } else {
            return 0;
        }
    }
}
