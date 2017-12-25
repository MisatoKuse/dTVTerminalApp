/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.other;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.launch.STBSelectActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.MyChannelEditActivity;
import com.nttdocomo.android.tvterminalapp.adapter.MainSettingListAdapter;
import com.nttdocomo.android.tvterminalapp.common.CustomDialog;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.MainSettingUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private Boolean isSDCard = false;
    private String[] itemName;
    private Resources res;
    private MainSettingListAdapter mAdapter;
    private final List<MainSettingUtils> mSettingList = new ArrayList<>();

    //カテゴリであることを示す
    private static final boolean CATEGORY = true;
    //カテゴリではない項目名であることを示す
    private static final boolean ITEM = false;
    //空白文字
    private static final String BLANK = "";
    //Dアカウントアプリ Package名
    private static final String D_ACCOUNT_APP_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    //Dアカウントアプリ Activity名
    private static final String D_ACCOUNT_APP_ACTIVITY_NAME=".activity.DocomoIdTopActivity";
    //DアカウントアプリURI
    private static final String D_ACCOUNT_APP_URI = "market://details?id=com.nttdocomo.android.idmanager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.setting_main_layout);

        //header部分の設定
        setTitleText(getString(R.string.nav_menu_item_setting));
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        menuImageView.setOnClickListener(this);

        //設定画面に表示するデータを設定する
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView mListView = findViewById(R.id.main_setting_list);
        mAdapter = new MainSettingListAdapter(this, R.layout.setting_main_list_layout, mSettingList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        checkDownloadPath();
        checkIsPairing();
        checkImageQuality();
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
            //ペアリング設定
            Intent intent = new Intent(getApplicationContext(),STBSelectActivity.class);
            intent.putExtra(STBSelectActivity.FROM_WHERE, STBSelectActivity.STBSelectFromMode.STBSelectFromMode_Setting.ordinal());
            startActivity(intent);
        } else if (tappedItemName.equals(itemName[5])) {
            //マイ番組表連携
            startActivity(MyChannelEditActivity.class, null);
        } else if (tappedItemName.equals(itemName[7])) {
            //ダウンロードコンテンツ保存先設定画面への遷移
            Intent intent = new Intent(this, SettingDownloadPathActivity.class);
            intent.putExtra(getString(R.string.main_setting_storage_status), mSettingList.get(i).getStateText());
            startActivity(intent);
        } else if (tappedItemName.equals(itemName[9]) && !mSettingList.get(i).isCategory()) {
            //外出先視聴時の画質設定画面への遷移
            Intent intent = new Intent(this, SettingImageQualityActivity.class);
            intent.putExtra(getString(R.string.main_setting_quality_status), mSettingList.get(i).getStateText());
            startActivity(intent);
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

        //外部ストレージスロットの存在判定
        isExternalStorageSlot();
        String storage = res.getString(R.string.main_setting_device_storage);
        if (isSDCard) {
            //保存先ストレージの確認
            Boolean storagePath = SharedPreferencesUtils.getSharedPreferencesStoragePath(this);
            if (!storagePath) {
                storage = res.getString(R.string.main_setting_outside_storage);
            }
        }

        //画質設定の設定値を確認
        String imageQuality = SharedPreferencesUtils.getSharedPreferencesImageQuality(this);
        if (imageQuality.isEmpty()) {
            //値が保存されていない場合は初期値を設定
            imageQuality = res.getString(R.string.main_setting_image_quality_high);
            /* test code begin */
            SharedPreferencesUtils.setSharedPreferencesImageQuality(this, imageQuality);
            /* test code end */
        }

        //項目名、設定値、>の画像、カテゴリ情報
        mSettingList.add(new MainSettingUtils(itemName[0], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[1], BLANK, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[2], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[3], isParing, true, ITEM));
        mSettingList.add(new MainSettingUtils(itemName[4], BLANK, false, CATEGORY));
        mSettingList.add(new MainSettingUtils(itemName[5], BLANK, true, ITEM));
        if (isSDCard) {
            //SDCardが存在しない場合、ストレージ保存先の項目は非表示
            mSettingList.add(new MainSettingUtils(itemName[6], BLANK, false, CATEGORY));
            mSettingList.add(new MainSettingUtils(itemName[7], storage, true, ITEM));
        }
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
        Intent intent = new Intent();
        intent.setClassName(D_ACCOUNT_APP_PACKAGE_NAME,
                D_ACCOUNT_APP_PACKAGE_NAME + D_ACCOUNT_APP_ACTIVITY_NAME);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            //　アプリが無ければインストール画面に誘導
            CustomDialog dAccountUninstallDialog = new CustomDialog(this, CustomDialog.DialogType.CONFIRM);
            dAccountUninstallDialog.setContent(getResources().getString(R.string.main_setting_d_account_message));
            dAccountUninstallDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(boolean isOK) {
                    Uri uri = Uri.parse(D_ACCOUNT_APP_URI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            dAccountUninstallDialog.showDialog();
        }
    }

    /**
     * ダウンロード先設定画面からの復帰時に値を確認し、変更されていた場合は変更を行う
     */
    private void checkDownloadPath() {
        if (isSDCard) {
            Boolean storagePath = SharedPreferencesUtils.getSharedPreferencesStoragePath(this);
            String storage = storagePath ? res.getString(R.string.main_setting_device_storage) :
            res.getString(R.string.main_setting_outside_storage);
            for (int i=0; i < mSettingList.size(); i++) {
                if (mSettingList.get(i).getText().equals(itemName[7]) && !mSettingList.get(i).isCategory()) {
                    mSettingList.set(i, new MainSettingUtils(itemName[7], storage, true, ITEM));
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 再ペアリング画面からの復帰時に値を確認し、変更されていた場合は更新を行う
     */
    private void checkIsPairing() {
        String isParing = res.getString(R.string.main_setting_paring);
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaDmsItem.mControlUrl.isEmpty()) {
            // 未ペアリング時
            isParing = res.getString(R.string.main_setting_not_paring);
        }
        for (int i=0; i < mSettingList.size(); i++) {
            if (mSettingList.get(i).getText().equals(itemName[3]) && !mSettingList.get(i).isCategory()) {
                mSettingList.set(i, new MainSettingUtils(itemName[3], isParing, true, ITEM));
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 外出先視聴時の画質設定からの復帰時に値を確認し、変更されていた場合変更を行う
     */
    private void checkImageQuality() {
        String imageQuality = SharedPreferencesUtils.getSharedPreferencesImageQuality(this);
        for (int i=0; i < mSettingList.size(); i++) {
            if (mSettingList.get(i).getText().equals(itemName[9]) && !mSettingList.get(i).isCategory()) {
                mSettingList.set(i, new MainSettingUtils(itemName[9], imageQuality, true, ITEM));
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * SDカードスロットの存在判定を行う。
     */
    private void isExternalStorageSlot() {
        //TODO 本来はSDカードスロットの存在判定の結果を返すメソッドだが、
        //getExternalStorageState()がSDカードスロット以外のPathも返却する可能性がある。
        //現時点では端末にSDカードスロットがあるものとしてtrue返却する。
        //SDカードスロットの無い端末が手元に届いてから再度検証を行う。
        isSDCard = true;

        /*
        List<String> sdCardDirPathList = new ArrayList<>();
        List<String> deviceStorageDirPath = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Android5.0以上の時の処理
            File[] dirArr = getExternalFilesDirs(null);
            for (File dir : dirArr) {
                if (dir != null) {
                    String path = dir.getAbsolutePath();
                    // 取り外し可能か（SDカードか）を判定
                    if (Environment.isExternalStorageRemovable(dir)) {
                        // 取り外し可能であればSDカード
                        if (!sdCardDirPathList.contains(path)) {
                            sdCardDirPathList.add(path);
                        }
                    } else {
                        // 取り外し不可能であれば内部ストレージ。
                        deviceStorageDirPath.add(path);
                    }
                }
            }
            isSDCard = sdCardDirPathList.size() > 0;
        } else {
            //Android4.4の時の処理
            StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
            try {
                Method getVolumeListMethod = sm.getClass().getDeclaredMethod("getVolumeList");
                Object[] volumeList = (Object[]) getVolumeListMethod.invoke(sm);
                for (Object volume : volumeList) {
                    Method getPathFileMethod = volume.getClass().getDeclaredMethod("getPathFile");
                    File file = (File) getPathFileMethod.invoke(volume);
                    String storageBasePath = file.getAbsolutePath();

                    Method isRemovableMethod = volume.getClass().getDeclaredMethod("isRemovable");
                    boolean isRemovable = (boolean) isRemovableMethod.invoke(volume);
                    // ストレージが取り外し可能か（SDカードか）を判定。
                    if (isRemovable) {
                        // ベースパスがマウントされているかどうか
                        if (isMountedBasePath(storageBasePath)) {
                            // StorageVolumeの中で、取り外し可能かつマウント済みのパスはSDカード
                            if (!sdCardDirPathList.contains(storageBasePath)) {
                                String sdCardFilesDirPath = storageBasePath + "/Android/data/com.nttdocomo.android.tvterminalapp/files";
                                sdCardDirPathList.add(sdCardFilesDirPath);
                            }
                        }
                    } else {
                        // StorageVolumeの中で、取り外し不可能なパスは内部ストレージ
                        deviceStorageDirPath.add(storageBasePath);
                    }
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            isSDCard = sdCardDirPathList.size() > 0;
        }
        */
    }

    /**
     * 指定したベースパスがマウントされているかどうか
     *
     * @param basePath ベースパス
     * @return ベースパスがマウントされていればtrue、マウントされていなければfalse
     */
    private static boolean isMountedBasePath(String basePath) {
        boolean isMounted = false;
        BufferedReader br = null;
        File mounts = new File("/proc/mounts");

        // /proc/mountsが存在しなければ処理を終了する
        if (!mounts.exists()) {
            return false;
        }

        try {
            // マウントポイントを取得する
            br = new BufferedReader(new FileReader(mounts));
            String line;
            // マウントポイントに該当するパスがあるかチェックする
            while ((line = br.readLine()) != null) {
                if (line.contains(basePath)) {
                    // 該当するパスがあればマウントされている
                    isMounted = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isMounted;
    }
}