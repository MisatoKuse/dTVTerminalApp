package com.nttdocomo.activity.android.tvterminalapp.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created on 2017/09/20.
 *
 * クラス機能：
 *      プロジェクトにて、すべての「Activity」のベースクラスである
 *      「Activity」全体にとって、共通の機能があれば、追加すること
 */

public class BaseActivity extends Activity{

    /**
     * Created on 2017/09/21.
     * 関数機能：
     *      「Activity」の「画面ID」を戻す。
     *      各ActivityにてOverrideする関数である。
     *
     * @return 「Activity」の「画面ID」を戻す。
     */
    public String getScreenID(){
        return "";
    }

    /**
     *  Created on 2017/09/21.
     *  関数機能：
     *      「Activity」の「画面タイトル」を戻す。
     *      各ActivityにてOverrideする関数である。
     * @return 「Activity」の「画面タイトル」を戻す。
     */
    public String getScreenTitle(){
        return "";
    }

    /**
     * 関数機能：
     *      Activityを起動する
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            //intent.putExtra("bundle", bundle);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
