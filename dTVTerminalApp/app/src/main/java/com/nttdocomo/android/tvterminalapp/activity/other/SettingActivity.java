/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.launch.STBSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.search.SearchTopActivity;
import com.nttdocomo.android.tvterminalapp.adapter.MainSettingListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private String[] itemName;
    private ListView mListView;
    private final List<MainSettingUtils> mSettingList = new ArrayList<>();
    private MainSettingListAdapter mAdapter;
    private Resources res;

    // カテゴリであることを示す
    private static final boolean CATEGORY = true;
    // カテゴリではない項目名であることを示す
    private static final boolean ITEM = false;
    // 空白文字
    private static final String BLANK = "";
    // Dアカウントアプリpackage名
    private String D_ACCOUNT_PACKAGE_ID = "com.nttdocomo.android.idmanager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();

        //header部分の設定
        setTitleText(getString(R.string.nav_menu_item_setting));
        setContentView(R.layout.setting_main_layout);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        menuImageView.setOnClickListener(this);

        //設定画面に表示するデータを設定する
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mListView = findViewById(R.id.main_setting_list);
        mAdapter = new MainSettingListAdapter(this, R.layout.setting_main_list_layout, mSettingList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        checkIsPairng();
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
            //Dアカウント設定
            startDAccountSetting();
        } else if (tappedItemName.equals(itemName[3])) {
            //TODO ペアリング設定
            startActivity(STBSelectActivity.class, null);
        } else if (tappedItemName.equals(itemName[5])) {
            //TODO マイ番組表連携
            startActivity(SearchTopActivity.class, null);
            Toast.makeText(this, "テストとして検索画面を開いている", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[7])) {
            //TODO ダウンロード先設定
            //外部ストレージがない場合は非表示、ただしスロットが確認できる場合は表示後刺さっていません表示
            Toast.makeText(this, "ダウンロード先変更", Toast.LENGTH_SHORT).show();
        } else if (tappedItemName.equals(itemName[9])) {
            //TODO 外出先視聴時の画質設定 (ダイアログの作成)
            startActivity(SettingDownloadLocation.class, null);
            /* テストコード */
//            SharedPreferencesUtils.setSharedPreferencesImageQuality(this, "高");
//            mSettingList.set(9, new MainSettingUtils(itemName[9], "高", true, ITEM));
//            mAdapter.notifyDataSetChanged();
            /* テストコードここまで */
        }
    }

    /**
     * 設定画面に表示する情報をリストに入れる
     */
    private void initData() {
        res = getResources();
        itemName = res.getStringArray(R.array.main_setting_items);

        // ペアリング状態の確認
        String isParing = res.getString(R.string.main_setting_paring);
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaDmsItem.mControlUrl.isEmpty()) {
            // 未ペアリング時
            isParing = res.getString(R.string.main_setting_not_paring);
        }

        //保存先ストレージの確認
        String storage = res.getString(R.string.main_setting_device_storage);
        Boolean storagePath = SharedPreferencesUtils.getSharedPreferencesStoragePath(this);
        if (!storagePath) {
            //TODO 初期値の確認
            storage = res.getString(R.string.main_setting_outside_storage);
        }

        //画質設定の設定値を確認
        String imageQuality = SharedPreferencesUtils.getSharedPreferencesImageQuality(this);
        if (imageQuality.isEmpty()) {
            //値が保存されていない場合は初期値を設定
            //TODO 初期値の確認
            imageQuality = res.getString(R.string.main_setting_image_quality_low);
        }

        //項目名、設定値、>の画像、カテゴリ情報
        mSettingList.add(new MainSettingUtils(itemName[0], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[1], BLANK, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[2], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[3], isParing, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[4], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[5], BLANK, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[6], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[7], storage, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[8], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[9], imageQuality, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[10], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[11], BLANK, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[12], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[13], "2.1.0(仮)", false, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[14], BLANK, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[15], BLANK, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[16], BLANK, true, ITEM));
    }

    /**
     * Dアカウント設定を連携起動する
     */
    private void startDAccountSetting() {
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(D_ACCOUNT_PACKAGE_ID);
        if (intent != null) {
            startActivity(intent);
        } else {
            //　アプリが無ければインストール画面に誘導
            new AlertDialog.Builder(this)
                .setMessage(getString(R.string.main_setting_d_account_message))
                .setPositiveButton(getString(R.string.positive_response), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("market://details?id=" + D_ACCOUNT_PACKAGE_ID);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.negative_response), null)
                .show();
        }
    }

    /**
     * ストレージ先変更画面からの復帰時に値を確認し、変更されていた場合は更新を行う
     */
    private void checkIsPairng() {
        String isParing = res.getString(R.string.main_setting_paring);
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaDmsItem.mControlUrl.isEmpty()) {
            // 未ペアリング時
            isParing = res.getString(R.string.main_setting_not_paring);
        }
        mSettingList.set(3, new MainSettingUtils(itemName[3], isParing, true, ITEM));
        mAdapter.notifyDataSetChanged();
    }
}