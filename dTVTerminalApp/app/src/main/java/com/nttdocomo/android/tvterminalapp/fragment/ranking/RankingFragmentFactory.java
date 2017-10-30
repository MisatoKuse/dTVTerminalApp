
package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import java.util.HashMap;
import java.util.Map;

public class RankingFragmentFactory {

    private Map<Integer, RankingBaseFragment> mFragments = new HashMap<Integer, RankingBaseFragment>();

    /**
     * フラグメントクラスの生成、取得
     * @param position
     * @param lis
     * @return
     */
    public RankingBaseFragment createFragment(int position, RankingFragmentScrollListener lis) {
        RankingBaseFragment fragment = null;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new RankingBaseFragment();
            fragment.setRankingBaseFragmentScrollListener(lis);
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}
