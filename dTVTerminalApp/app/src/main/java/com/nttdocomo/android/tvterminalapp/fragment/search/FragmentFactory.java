/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.search;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {

    private Map<Integer, SearchBaseFragment> mFragments = new HashMap<Integer, SearchBaseFragment>();

    public FragmentFactory(){

    }

    public synchronized SearchBaseFragment createFragment(int position, SearchBaseFragmentScrollListener lis) {
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

    public synchronized int getFragmentCount(){
        if(null!=mFragments){
            return mFragments.size();
        }

        return 0;
    }
}
