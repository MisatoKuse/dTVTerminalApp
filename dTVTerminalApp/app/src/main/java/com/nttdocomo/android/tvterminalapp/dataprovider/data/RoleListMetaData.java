/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;

/**
 * ロールリストメタデータ.
 */
public class RoleListMetaData implements Serializable {

    private static final long serialVersionUID = -4466388996087422463L;
    /**ロールID.*/
    private String mId;
    /**ロール名前.*/
    private String mName;
    /**
     * ロールID取得.
     * @return  ロールID
     */
    public String getId() {
        return mId;
    }
    /**
     * ロールID設定.
     * @param mId ロールID
     */
    public void setId(final String mId) {
        this.mId = mId;
    }
    /**
     * ロール名前取得.
     * @return ロール名前
     */
    public String getName() {
        return mName;
    }
    /**
     * ロール名前設定.
     * @param mName ロール名前
     */
    public void setName(final String mName) {
        this.mName = mName;
    }
}



