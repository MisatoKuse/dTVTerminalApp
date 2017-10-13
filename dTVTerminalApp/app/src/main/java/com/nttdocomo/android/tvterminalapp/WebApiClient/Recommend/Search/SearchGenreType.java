package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;




public class SearchGenreType extends SearchFilterTypeMappable {
    public final String active_001="映画";  //TODO: ジャンルタイプリスト仕様が決まり次第、追加する

    private String mType="";

    public SearchGenreType(String name) {
        if("SearchGenreTypeActive_001".equals(name)){
            mType=active_001;
            return;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if (active_001.equals(mType)) {
            return SearchFilterType.genreMovie;
        }
        return null;
    }
}