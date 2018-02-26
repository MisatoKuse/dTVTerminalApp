/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;


public class RoleListMetaData implements Serializable {

    private static final long serialVersionUID = -4466388996087422463L;
    private String mId;
    private String mName;
    public String getId() {
        return mId;
    }

    public void setId(final String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }
    public void setName(final String mName) {
        this.mName = mName;
    }
}



