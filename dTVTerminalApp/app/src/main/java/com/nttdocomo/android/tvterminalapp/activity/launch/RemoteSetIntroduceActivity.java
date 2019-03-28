/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * リモート視聴設定確認.
 */
public class RemoteSetIntroduceActivity extends BaseActivity implements View.OnClickListener, DlnaManager.LocalRegisterListener {

    /** 画像の素材.*/
    private final int[] mImagResource = {R.mipmap.remote_setting_01, R.mipmap.remote_setting_02, R.mipmap.remote_setting_03};
    /** レイアウトのid.*/
    private final int[] mImagId = {R.id.remote_introduce_main_layout_img1, R.id.remote_introduce_main_layout_img2, R.id.remote_introduce_main_layout_img3};
    /** タブレット幅（スクリーンの67％）.*/
    private static final int IMAGE_WIDTH_PERCENT_100 = 100;
    /** タブレット幅（スクリーンの67％）.*/
    private static final int IMAGE_WIDTH_PERCENT_67 = 67;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_introduce_main_layout);
        setTitleText(getString(R.string.remote_introduce_header));
        enableHeaderBackIcon(false);
        initView();
    }

    /**
     * ビュー初期化.
     */
    private void initView() {
        LinearLayout.LayoutParams layoutParams;
        TextView setBtn = findViewById(R.id.remote_introduce_main_layout_set_btn);
        TextView linkBtn = findViewById(R.id.remote_introduce_main_layout_tv_link);
        linkBtn.setPaintFlags(linkBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setBtn.setOnClickListener(this);
        linkBtn.setOnClickListener(this);
        for (int i = 0; i < mImagResource.length; i++) {
            Bitmap srcBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), mImagResource[i]);
            float margin = getResources().getDimension(R.dimen.stb_launch_padding_start_end);
            float maxWidth;
            if (ContentUtils.isTablet(getApplicationContext())) {
                maxWidth = getWidthDensity() / IMAGE_WIDTH_PERCENT_100 * IMAGE_WIDTH_PERCENT_67;
            } else {
                maxWidth = getWidthDensity() - margin * 2;
            }
            float ratio =  maxWidth / srcBitmap.getWidth();
            if (ratio > 0) {
                Matrix matrix = new Matrix();
                matrix.postScale(ratio, ratio);
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
            }
            ImageView imageView = findViewById(mImagId[i]);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, srcBitmap.getHeight());
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(srcBitmap);
        }
    }

    /**
     * ローカルレジストレーション実施.
     */
    private void executeLocalRegistration() {
        setRemoteProgressVisible(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DlnaUtils.ExcuteLocalRegistrationErrorType errorType =
                        DlnaUtils.excuteLocalRegistration(getApplicationContext(), RemoteSetIntroduceActivity.this);
                boolean result = DlnaUtils.ExcuteLocalRegistrationErrorType.NONE.equals(errorType);
                if (!result) {
                    showRegistrationResultDialog(false, errorType);
                }
            }
        }).start();
    }

    /**
     * ローカルレジストレーションの処理結果.
     *
     * @param isSuccess true 成功 false 失敗
     * @param errorType エラータイプ
     */
    private void showRegistrationResultDialog(final boolean isSuccess, final DlnaUtils.ExcuteLocalRegistrationErrorType errorType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog resultDialog = DlnaUtils.getRegistResultDialog(RemoteSetIntroduceActivity.this, isSuccess, errorType);
                resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                setRemoteProgressVisible(View.GONE);
                resultDialog.showDialog();
            }
        });
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.remote_introduce_main_layout_set_btn:
                executeLocalRegistration();
                break;
            case R.id.remote_introduce_main_layout_tv_link:
                CustomDialog resultDialog = new CustomDialog(RemoteSetIntroduceActivity.this, CustomDialog.DialogType.ERROR);
                resultDialog.setOnTouchOutside(false);
                resultDialog.setCancelable(false);
                resultDialog.setOnTouchBackkey(false);
                resultDialog.setContent(getString(R.string.remote_introduce_dialog_txt));
                resultDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                    @Override
                    public void onOKCallback(final boolean isOK) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                resultDialog.showDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DlnaManager.shared().StartDmp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DlnaManager.shared().StopDmp();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRegisterCallBack(final boolean result, final DlnaUtils.ExcuteLocalRegistrationErrorType errorType) {
        if (result) {
            //ローカルレジストレーションが成功したので日時を蓄積する
            SharedPreferencesUtils.setRegistTime(getApplicationContext());
        }
        showRegistrationResultDialog(result, errorType);
    }
}
