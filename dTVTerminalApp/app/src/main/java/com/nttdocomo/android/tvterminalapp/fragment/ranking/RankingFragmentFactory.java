
package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragmentScrollListener;

import java.util.HashMap;
import java.util.Map;

public class RankingFragmentFactory {

    private Map<Integer, RankingBaseFragment> mFragments = new HashMap<Integer, RankingBaseFragment>();

    public RankingBaseFragment createFragment(int position, RankingFragmentScrollListener lis) {
        RankingBaseFragment fragment = null;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new RankingBaseFragment();
            fragment.setClipListBaseFragmentScrollListener(lis);
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}
