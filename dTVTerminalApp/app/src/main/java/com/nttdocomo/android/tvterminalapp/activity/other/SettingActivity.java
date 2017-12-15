/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.MainSettingListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private String[] itemName;
    private ListView mListView;
    private final List<MainSettingUtils> mSettingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();

        setTitleText(getString(R.string.nav_menu_item_setting));
        setContentView(R.layout.setting_main_layout);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        menuImageView.setOnClickListener(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mListView = findViewById(R.id.main_setting_list);
        MainSettingListAdapter adapter = new MainSettingListAdapter(
                this, R.layout.setting_main_list_layout, mSettingList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_layout_menu:
                //ダブルクリックを防ぐ
                if (isFastClick()) {
                    onSampleGlobalMenuButton_PairLoginOk();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String tappedItemName = mSettingList.get(i).getText();

        if (tappedItemName.equals(itemName[1])) {
            //TODO dアカウント設定
            Toast.makeText(this, "dアカウントアプリを起動する処理", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[3])) {
            //TODO ペアリング設定
            Toast.makeText(this, "ペアリング再設定", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[5])) {
            //TODO マイ番組表連携
            Toast.makeText(this, "マイ番組表を開く処理", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[7])) {
            //TODO ダウンロード先設定
            Toast.makeText(this, "ダウンロード先変更", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[9])) {
            //TODO 外出先視聴時の画質設定
            Toast.makeText(this, "画質設定処理", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[14])) {
            //TODO ライセンス情報表示
            Toast.makeText(this, "ライセンス情報", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 設定画面に表示する情報をリストに入れる
     */
    private void initData() {
        //項目名、設定値、>の画像、カテゴリ情報
        Resources res = getResources();
        itemName = res.getStringArray(R.array.main_setting_items);
        //TODO ペアリング状態の確認
        String isParing = res.getString(R.string.main_setting_paring);
        //TODO 保存先ストレージの確認
        String storage = res.getString(R.string.main_setting_device_storage);
        //TODO 画質設定の設定値を確認
        String imageQuality = res.getString(R.string.main_setting_image_quality_low);

        mSettingList.add(new MainSettingUtils(itemName[0], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[1], "", true, false));
        mSettingList.add(new MainSettingUtils(itemName[2], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[3], isParing, true, false));
        mSettingList.add(new MainSettingUtils(itemName[4], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[5], "", true, false));
        mSettingList.add(new MainSettingUtils(itemName[6], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[7], storage, true, false));
        mSettingList.add(new MainSettingUtils(itemName[8], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[9], imageQuality, true, false));
        mSettingList.add(new MainSettingUtils(itemName[10], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[11], "", true, false));
        mSettingList.add(new MainSettingUtils(itemName[12], "", false, true));
        mSettingList.add(new MainSettingUtils(itemName[13], "2.1.0(仮)", false, false));
        mSettingList.add(new MainSettingUtils(itemName[14], "", true, false));
        mSettingList.add(new MainSettingUtils(itemName[15], "", true, false));
        mSettingList.add(new MainSettingUtils(itemName[16], "", true, false));
    }
}