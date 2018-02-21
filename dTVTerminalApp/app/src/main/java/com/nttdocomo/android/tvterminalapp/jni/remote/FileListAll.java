/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.remote;

import java.io.File;
import java.util.HashMap;

/**
 * todo 開発中ツールとして使う
 *       リリース時、削除
 */
public  class FileListAll {

    public HashMap<String, String> getList(File file) {

        HashMap<String, String> fileList = new HashMap<>();
        getFileList(file, fileList);
        return fileList;
    }

    /**
     * @param path path
     * @param fileList fileList
     */
    private void getFileList(File path, HashMap<String, String> fileList){
        if(path.isDirectory()){
            File[] files = path.listFiles();
            if(null == files) {
                return;
            }
            for(File file: files){
                getFileList(file, fileList);
            }
        }else{
            String filePath = path.getAbsolutePath();
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            fileList.put(fileName, filePath + " size=" + DlnaInterfaceRI.getFileSize(filePath));
        }
    }

}
