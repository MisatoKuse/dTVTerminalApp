package com.nttdocomo.android.tvterminalapp.Fragment.Search;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {

    private static Map<Integer, SearchBaseFragment> mFragments = new HashMap<Integer, SearchBaseFragment>();

    public static synchronized SearchBaseFragment createFragment(int position, SearchBaseFragmentScrollListener lis) {
        SearchBaseFragment fragment = null;
        fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new SearchBaseFragment();
            if (fragment != null) {
                mFragments.put(position, fragment);
                fragment.setSearchBaseFragmentScrollListener(lis);
            }
        }

        return fragment;
    }

    public static synchronized int getFragmentCount(){
        if(null!=mFragments){
            return mFragments.size();
        }

        return 0;
    }
}
