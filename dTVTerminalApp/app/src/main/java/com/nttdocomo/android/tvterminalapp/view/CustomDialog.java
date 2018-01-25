/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;

import java.util.List;

/**
 * カスタムダイアログ.
 */
public class CustomDialog implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener {

    /**
     * コンテキスト.
     */
    private Context mContext;
    /**
     * ダイアログ.
     */
    private AlertDialog dialog;
    /**
     * ダイアログ種別.
     */
    private DialogType dialogType;
    /**
     * タイトル.
     */
    private String title;
    /**
     * 本文.
     */
    private String content;
    /**
     * リスト表示時のリスト.
     */
    private List<String> list = null;
    /**
     * OK押下時のコールバック.
     */
    private ApiOKCallback apiOKCallback;
    /**
     * Cancel押下時のコールバック.
     */
    private ApiCancelCallback mApiCancelCallback = null;
    /**
     * リスト押下時のコールバック.
     */
    private ApiSelectCallback apiSelectCallback;
    /**
     * リストアイテム押下時のコールバック.
     */
    private ApiItemSelectCallback apiItemSelectCallback;
    /**
     * OKボタンに表示する文字列.
     */
    private String confirmText = null;
    /**
     * Cancelボタンに表示する文字列.
     */
    private String cancelText = null;
    /**
     * ダイアログのcancelable設定値.
     */
    private boolean cancelable = true;
    /**
     * ダイアログのOKボタン.
     */
    private TextView mOkButton = null;
    /**
     * ダイアログのキャンセルボタン.
     */
    private TextView mCancelButton = null;
    /**
     * OKボタンの表示/非表示判定.
     */
    private int confirmVisibility = View.VISIBLE;
    /**
     * Cancelボタンの表示/非表示判定.
     */
    private int cancelVisibility = View.VISIBLE;

    /**
     * OKボタン押下を返却するためのコールバック.
     */
    public interface ApiOKCallback {
        /**
         * OKボタン押下のコールバック.
         * @param isOK true
         */
        void onOKCallback(boolean isOK);
    }

    /**
     * Cancelボタン押下を返却するためのコールバック.
     */
    public interface ApiCancelCallback {
        /**
         * Cancelボタン押下時のコールバック.
         */
        void onCancelCallback();
    }

    /**
     * SELECTを返却するためのコールバック.
     */
    public interface ApiSelectCallback {
        /**
         * リスト選択時のコールバック.
         * @param position 押下されたリストアイテム
         */
        void onSelectCallback(int position);
    }

    /**
     * SELECTを返却するためのコールバック.
     */
    public interface ApiItemSelectCallback {
        /**
         * リストアイテムが選択時のコールバック.
         * @param dialog ダイアログ
         * @param position 押下されたリストアイテム
         */
        void onItemSelectCallback(AlertDialog dialog, int position);
    }

    /**
     * OKボタン押下のコールバック.
     *
     * @param apiOKCallback OKボタン押下のコールバック
     */
    public void setOkCallBack(ApiOKCallback apiOKCallback) {
        this.apiOKCallback = apiOKCallback;
    }

    /**
     * Cancelボタン押下を返却するためのコールバック.
     *
     * @param apiCancelCallback Cancelボタン押下を返却するためのコールバック
     */
    public void setApiCancelCallback(ApiCancelCallback apiCancelCallback) {
        this.mApiCancelCallback = apiCancelCallback;
    }

    /**
     * リスト選択時のコールバック..
     *
     * @param apiSelectCallback リスト選択時のコールバック
     */
    public void setSelectCallBack(ApiSelectCallback apiSelectCallback) {
        this.apiSelectCallback = apiSelectCallback;
    }

    /**
     * リストアイテムが選択時のコールバック.
     *
     * @param callback リストアイテムが選択時のコールバック
     */
    public void setItemSelectCallback(ApiItemSelectCallback callback) {
        this.apiItemSelectCallback = callback;
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param dialogType ダイアログタイプ
     */
    public CustomDialog(Context context, DialogType dialogType) {
        this.mContext = context;
        this.dialogType = dialogType;
    }

    /**
     * ダイアログのタイトルを設定.
     *
     * @param title タイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * ダイアログに表示する本文を設定.
     *
     * @param content 本文
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * ダイアログに表示するリストを設定.
     *
     * @param list リスト
     */
    public void setSelectData(List<String> list) {
        this.list = list;
    }

    /**
     * ダイアログタイプ.
     */
    public enum DialogType {
        /**
         * ダイアログタイプ：エラー(単一ボタン).
         */
        ERROR,          //エラーダイアログ
        /**
         * ダイアログタイプ：確認(OK/Cancelボタン).
         */
        CONFIRM,        //確認ダイアログ
        /**
         * ダイアログタイプ：リスト(リスト表示、OK/Cancelは任意).
         */
        SELECT          //選択ダイアログ
    }

    /**
     * ダイアログの表示.
     */
    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        initView(dialogBuilder);
    }

    /**
     * ダイアログビューの初期化.
     * @param dialogBuilder スクリーン
     */
    private void initView(AlertDialog.Builder dialogBuilder) {
        if (TextUtils.isEmpty(confirmText)) {
            confirmText = mContext.getString(R.string.custom_dialog_ok);
        }
        if (TextUtils.isEmpty(cancelText)) {
            cancelText = mContext.getString(R.string.custom_dialog_cancel);
        }
        switch (dialogType) {
            case ERROR:
                dialogBuilder.setPositiveButton(confirmText, this);
                break;
            case SELECT:
                if (confirmVisibility == View.VISIBLE) {
                    dialogBuilder.setPositiveButton(confirmText, this);
                }
                if (cancelVisibility == View.VISIBLE) {
                    dialogBuilder.setNegativeButton(cancelText, this);
                }
                if (list != null) {
                    ListView listView = new ListView(mContext);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(this);
                    dialogBuilder.setView(listView);
                }
                break;
            case CONFIRM:
                dialogBuilder.setPositiveButton(confirmText, this);
                dialogBuilder.setNegativeButton(cancelText, this);
                break;
            default:
                break;
        }
        dialog = dialogBuilder.create();
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnKeyListener(keyListener);

        if (TextUtils.isEmpty(title)) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.requestFeature(Window.FEATURE_NO_TITLE);
            }
        } else {
            dialog.setTitle(title);
        }
        if (TextUtils.isEmpty(content)) {
            dialog.setMessage("");
        } else {
            dialog.setMessage(content);
        }

        dialog.show();
    }

    /**
     * ダイアログを閉じる.
     */
    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                dismissDialog();
                if (apiOKCallback != null) {
                    apiOKCallback.onOKCallback(true);
                }
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                dismissDialog();
                if (mApiCancelCallback != null) {
                    mApiCancelCallback.onCancelCallback();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (apiSelectCallback != null) {
            apiSelectCallback.onSelectCallback(position);
        }
        if (apiItemSelectCallback != null) {
            apiItemSelectCallback.onItemSelectCallback(dialog, position);
        }
    }

    /**
     * バックキー押下時の動作.
     */
    private static OnKeyListener keyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                    && event.getAction() != KeyEvent.ACTION_UP) {
                dialog.dismiss();
                return true;
            } else {
                return false;
            }
        }
    };

    /**
     * DialogType.CONFIRMのconfirmテキストを変更.
     *
     * @param resId リソースID
     */
    public void setConfirmText(int resId) {
        Resources resources = mContext.getResources();
        confirmText = resources.getString(resId);
    }

    /**
     * DialogType.CONFIRMのcancelテキストを変更.
     * @param resId リソースID
     */
    public void setCancelText(int resId) {
        Resources resources = mContext.getResources();
        cancelText = resources.getString(resId);
    }

    /**
     * Dialogのcancelableを変更.
     *
     * @param cancelable キャンセル可否設定
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * confirmの表示/非表示を設定.
     *
     * @param visibility 表示/非表示設定
     */
    public void setConfirmVisibility(int visibility) {
        confirmVisibility = visibility;
    }

    /**
     * cancelの表示/非表示を設定.
     *
     * @param visibility 表示/非表示設定
     */
    public void setCancelVisibility(int visibility) {
        cancelVisibility = visibility;
    }

    /**
     * OKボタンの表示文言を変更する.
     *
     * @param text 表示文言
     */
    public void setOkButtonText(final String text) {
        if (confirmText != null) {
            confirmText = text;
        }
    }

    /**
     * CANCELボタンの表示文言を変更する.
     *
     * @param text 表示文言
     */
    public void setCancelButtonText(final String text) {
        if (cancelText != null) {
            cancelText = text;
        }
    }
}
