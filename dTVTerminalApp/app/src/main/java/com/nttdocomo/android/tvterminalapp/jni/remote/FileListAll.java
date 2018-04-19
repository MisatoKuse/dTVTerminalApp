/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.remote;

import java.io.File;
import java.util.HashMap;

/**
 * todo 開発中ツールとして使う
 * リリース時、削除.
 */
public class FileListAll {
    /**
     * getList.
     * @param file file
     * @return fileList
     */
    public HashMap<String, String> getList(final File file) {

        HashMap<String, String> fileList = new HashMap<>();
        getFileList(file, fileList);
        return fileList;
    }

    /**
     * getFileList.
     * @param path path
     * @param fileList fileList
     */
    private void getFileList(final File path, final HashMap<String, String> fileList) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (null == files) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                getFileList(files[i], fileList);
            }
        } else {
            String filePath = path.getAbsolutePath();

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            File file = new File(fileName);
            fileList.put(fileName, filePath + " size=" + DlnaInterfaceRI.getFileSize(filePath));
        }
    }

}
