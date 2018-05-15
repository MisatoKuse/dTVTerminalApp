/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient.security;

/**
 * 暗号化データ.
 */
public class CipherData {

    /** Module. */
    private byte[] mModuleData = null;
    /** Exponent. */
    private byte[] mExponentData = null;

    /**
     * コンストラクタ.
     *
     * @param module Module
     * @param exponent Exponent
     */
    public CipherData(final byte[] module, final byte[] exponent) {
        mModuleData = module;
        mExponentData = exponent;
    }
    /**
     * Moduleを返す.
     *
     * @return Module
     */
    public byte[] getModuleData() {
        return mModuleData;
    }

    /**
     * Moduleを返す.
     *
     * @return Module
     */
    public byte[] getExponentData() {
        return mExponentData;
    }

}
