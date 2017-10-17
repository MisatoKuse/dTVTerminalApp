package com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search;


public enum SearchFunctionType {
    none(1);

    private final int value;

    private SearchFunctionType(int value) {
        this.value = value;
    }

    public SearchFunctionType valueOf(int value) {
        switch (value) {
            case 1:
                return SearchFunctionType.none;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
