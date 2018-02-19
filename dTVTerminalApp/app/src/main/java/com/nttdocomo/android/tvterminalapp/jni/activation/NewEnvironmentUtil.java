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

public class NewEnvironmentUtil extends EnvironmentUtil{

    private static String getDataApkCmWorkName(EnvironmentUtil.ACTIVATE_DATA_HOME dataHome) {
        String cmWorkName = null;
        switch(dataHome) {
            case DMP:
                cmWorkName = "cm_work_dmp";
                break;
            case DMS:
                cmWorkName = "cm_work_dms";
                break;
            case PLAYER:
                cmWorkName = "cm_work_player";
                break;
            default:
                throw new IllegalArgumentException("invalid ACTIVATE_DATA_HOME.");
        }

        return cmWorkName;
    }

    public static String getPrivateDataHome(String destDataHomeDir, EnvironmentUtil.ACTIVATE_DATA_HOME dataHomeEnum) {
        try {
            File e = new File(destDataHomeDir, getDataApkCmWorkName(dataHomeEnum));
            if(!e.exists()) {
                e.mkdirs();
            }

            return e.getAbsolutePath();
        } catch (Exception e) {
            DTVTLogger.debug(e);
            return null;
        }
    }

    /**
     * copy Device Key From Other CM Work
     * @param context
     * @param destDataHomeDir
     * @param dataHomeEnum
     * @return 3: 成功   1: 既にある  0: 失敗
     */
    public static int copyDeviceKeyFromOtherCMWork(Context context, String destDataHomeDir, EnvironmentUtil.ACTIVATE_DATA_HOME dataHomeEnum) {
        File currentCMWorkDir = null;

        File currentDeviceKeyFile;
        try {
            currentCMWorkDir = new File(getPrivateDataHome(destDataHomeDir, dataHomeEnum));
            currentDeviceKeyFile = new File(currentCMWorkDir, "db_post");
            if(currentDeviceKeyFile.exists()) {
                return 1;
            }
        } catch (Exception var9) {
            return 0;
        }

        List allDirs = getPrivateDataHomeAllDir(context);
        Iterator var5 = allDirs.iterator();

        File foundDeviceKey;
        do {
            File otherCMWorkDir;
            do {
                do {
                    if(!var5.hasNext()) {
                        return 2;
                    }

                    String dir = (String)var5.next();
                    otherCMWorkDir = new File(dir);
                } while(otherCMWorkDir.getAbsolutePath().equals(currentCMWorkDir.getAbsolutePath()));

                if(otherCMWorkDir.exists()) {
                    break;
                }

                otherCMWorkDir.mkdirs();
            } while(!otherCMWorkDir.exists());

            foundDeviceKey = new File(otherCMWorkDir, "db_post");
        } while(!foundDeviceKey.exists());

        FileSystemUtil.copyFile(foundDeviceKey, currentDeviceKeyFile);
        if(currentDeviceKeyFile.exists()) {
            return 3;
        } else {
            return 0;
        }
    }
}
