/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.app.Activity;
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
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.List;

/**
 * カスタムダイアログ.
 */
public class CustomDialog implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener,
        DialogInterface.OnDismissListener {

    /** コンテキスト. */
    private Context mContext = null;
    /** ダイアログ. */
    private AlertDialog mDialog = null;
    /** ダイアログ種別. */
    private DialogType mDialogType = null;
    /** タイトル. */
    private String mTitle = null;
    /** 本文. */
    private String mContent = null;
    /** リスト表示時のリスト. */
    private List<String> mList = null;
    /** OK押下時のコールバック. */
    private ApiOKCallback mApiOKCallback = null;
    /** Cancel押下時のコールバック. */
    private ApiCancelCallback mApiCancelCallback = null;
    /** リスト押下時のコールバック. */
    private ApiSelectCallback mApiSelectCallback = null;
    /** リストアイテム押下時のコールバック. */
    private ApiItemSelectCallback mApiItemSelectCallback = null;
    /** ボタンタップ以外でダイアログが閉じた時のコールバック. */
    private DismissCallback mDialogDismissCallback = null;
    /** OKボタンに表示する文字列. */
    private String mConfirmText = null;
    /** Cancelボタンに表示する文字列. */
    private String mCancelText = null;
    /** ダイアログのcancelable設定値. */
    private boolean mCancelable = true;
    /** ダイアログのOKボタン. */
    private TextView mOkButton = null;
    /** ダイアログのキャンセルボタン. */
    private TextView mCancelButton = null;
    /** OKボタンの表示/非表示判定. */
    private int mConfirmVisibility = View.VISIBLE;
    /** Cancelボタンの表示/非表示判定. */
    private int mCancelVisibility = View.VISIBLE;
    /** ボタンタップフラグ. **/
    private boolean mIsButtonTap = false;

    /** 画面外タップによるキャンセル判定値. */
    private boolean mCancelableOutside = true;
    /** バックキーをキャンセルボタンとして扱うスイッチ. */
    private boolean mBackKeyAsCancel = false;

    /**
     * 最後に扱ったキーコード.
     *
     * sKeyListenerがスタティック型なので、やむを得ずスタティック型とする
     */
    private volatile static int sLastKeyCode = 0;

    /**
     * OKボタン押下を返却するためのコールバック.
     */
    public interface ApiOKCallback {
        /**
         * OKボタン押下のコールバック.
         *
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
         *
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
         *
         * @param dialog   ダイアログ
         * @param position 押下されたリストアイテム
         */
        void onItemSelectCallback(AlertDialog dialog, int position);
    }

    /**
     * ボタンタップ以外でダイアログが閉じた時のコールバック.
     */
    public interface DismissCallback {
        /** ダイアログが閉じた場合のコールバック. */
        void allDismissCallback();
        /** ボタンタップ以外でダイアログが閉じた場合時のコールバック. */
        void otherDismissCallback();
    }

    /**
     * OKボタン押下のコールバック.
     *
     * @param apiOKCallback OKボタン押下のコールバック
     */
    public void setOkCallBack(final ApiOKCallback apiOKCallback) {
        this.mApiOKCallback = apiOKCallback;
    }

    /**
     * Cancelボタン押下を返却するためのコールバック.
     *
     * @param apiCancelCallback Cancelボタン押下を返却するためのコールバック
     */
    public void setApiCancelCallback(final ApiCancelCallback apiCancelCallback) {
        this.mApiCancelCallback = apiCancelCallback;
    }

    /**
     * リスト選択時のコールバック..
     *
     * @param apiSelectCallback リスト選択時のコールバック
     */
    public void setSelectCallBack(final ApiSelectCallback apiSelectCallback) {
        this.mApiSelectCallback = apiSelectCallback;
    }

    /**
     * リストアイテムが選択時のコールバック.
     *
     * @param callback リストアイテムが選択時のコールバック
     */
    public void setItemSelectCallback(final ApiItemSelectCallback callback) {
        this.mApiItemSelectCallback = callback;
    }

    /**
     * ボタンタップ以外でダイアログが閉じた時のコールバック.
     *
     * @param callback ボタンタップ以外でダイアログが閉じた時のコールバック
     */
    public void setDialogDismissCallback(final DismissCallback callback) {
        this.mDialogDismissCallback = callback;
    }

    /**
     * コンストラクタ.
     *
     * @param context    コンテキスト
     * @param dialogType ダイアログタイプ
     */
    public CustomDialog(final Context context, final DialogType dialogType) {
        this.mContext = context;
        this.mDialogType = dialogType;
    }

    /**
     * ダイアログのタイトルを設定.
     *
     * @param title タイトル
     */
    public void setTitle(final String title) {
        this.mTitle = title;
    }

    /**
     * ダイアログに表示する本文を設定.
     *
     * @param content 本文
     */
    public void setContent(final String content) {
        this.mContent = content;
    }

    /**
     * ダイアログの本文を返す(同じダイアログが続けて表示される事を防止する為に使用).
     *
     * @return ダイアログの本文
     */
    public String getContent() {
        return mContent;
    }

    /**
     * ダイアログに表示するリストを設定.
     *
     * @param list リスト
     */
    public void setSelectData(final List<String> list) {
        this.mList = list;
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
        dialogBuilder.setOnDismissListener(this);
        initView(dialogBuilder);
    }

    /**
     * ダイアログビューの初期化.
     *
     * @param dialogBuilder スクリーン
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void initView(final AlertDialog.Builder dialogBuilder) {
        if (TextUtils.isEmpty(mConfirmText)) {
            mConfirmText = mContext.getString(R.string.custom_dialog_ok);
        }
        if (TextUtils.isEmpty(mCancelText)) {
            mCancelText = mContext.getString(R.string.custom_dialog_cancel);
        }
        switch (mDialogType) {
            case ERROR:
                dialogBuilder.setPositiveButton(mConfirmText, this);
                break;
            case SELECT:
                if (mConfirmVisibility == View.VISIBLE) {
                    dialogBuilder.setPositiveButton(mConfirmText, this);
                }
                if (mCancelVisibility == View.VISIBLE) {
                    dialogBuilder.setNegativeButton(mCancelText, this);
                }
                if (mList != null) {
                    ListView listView = new ListView(mContext);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_list_item_1, mList);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(this);
                    dialogBuilder.setView(listView);
                }
                break;
            case CONFIRM:
                dialogBuilder.setPositiveButton(mConfirmText, this);
                dialogBuilder.setNegativeButton(mCancelText, this);
                break;
            default:
                break;
        }
        mDialog = dialogBuilder.create();
        mDialog.setCancelable(mCancelable);
        mDialog.setCanceledOnTouchOutside(mCancelableOutside);
        mDialog.setOnKeyListener(sKeyListener);

        if (TextUtils.isEmpty(mTitle)) {
            Window window = mDialog.getWindow();
            if (window != null) {
                window.requestFeature(Window.FEATURE_NO_TITLE);
            }
        } else {
            mDialog.setTitle(mTitle);
        }
        if (TextUtils.isEmpty(mContent)) {
            mDialog.setMessage("");
        } else {
            mDialog.setMessage(mContent);
        }

        //コンテキストがベースアクティビティから継承された物かどうかの判定
        if (mContext instanceof BaseActivity) {
            //ベースアクティビティだった
            BaseActivity baseActivity = (BaseActivity) mContext;

            if (baseActivity.isFinishing()) {
                //該当アクティビティが既に終わっているならば、ダイアログ表示をスキップする
                DTVTLogger.debug("initView: already activity("+ mContext.getClass() +") finish or pause:" + mContent);
                return;
            }

            DTVTLogger.debug("initView: show dialog(" + mContext.getClass() + "):"+mContent);

            //ダイアログを表示する
            mDialog.show();
        } else {
            //ベースアクティビティ以外の場合のフェールセーフ処理
            //本アプリのアクティビティは全てBaseアクティビティを継承しているのここは実行されない筈
            DTVTLogger.debug("initView: not BaseActivity (" + mContext.getClass() + "):"+mContent);

            //アクティビティかつ終了していないならばダイアログを表示する
            if (mContext instanceof BaseActivity && !((Activity)mContext).isFinishing()) {
                //ダイアログを表示する
                mDialog.show();
            }
        }
    }

    /**
     * ダイアログを閉じる.
     */
    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * ダイアログの表示状態.
     * @return isShowing:true else false
     */
    public boolean isShowing() {
        boolean isShow = false;
        if (mDialog != null) {
            isShow = mDialog.isShowing();
        }
        return isShow;
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                dismissDialog();
                if (mApiOKCallback != null) {
                    mIsButtonTap = true;
                    mApiOKCallback.onOKCallback(true);
                }
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                dismissDialog();
                if (mApiCancelCallback != null) {
                    mIsButtonTap = true;
                    mApiCancelCallback.onCancelCallback();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (mApiSelectCallback != null) {
            mApiSelectCallback.onSelectCallback(position);
        }
        if (mApiItemSelectCallback != null) {
            mApiItemSelectCallback.onItemSelectCallback(mDialog, position);
        }
    }

    /**
     * バックキー押下時の動作.
     */
    private static final OnKeyListener sKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
            //キーコードを退避する
            sLastKeyCode = keyCode;

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
    public void setConfirmText(final int resId) {
        Resources resources = mContext.getResources();
        mConfirmText = resources.getString(resId);
    }

    /**
     * ダイアログの本文をクリアする.
     */
    public void clearContentText() {
        mContent = "";
    }

    /**
     * DialogType.CONFIRMのcancelテキストを変更.
     *
     * @param resId リソースID
     */
    public void setCancelText(final int resId) {
        Resources resources = mContext.getResources();
        mCancelText = resources.getString(resId);
    }

    /**
     * Dialogのcancelableを変更.
     *
     * @param cancelable キャンセル可否設定
     */
    public void setCancelable(final boolean cancelable) {
        this.mCancelable = cancelable;
    }

    /**
     * confirmの表示/非表示を設定.
     *
     * @param visibility 表示/非表示設定
     */
    public void setConfirmVisibility(final int visibility) {
        mConfirmVisibility = visibility;
    }

    /**
     * cancelの表示/非表示を設定.
     *
     * @param visibility 表示/非表示設定
     */
    public void setCancelVisibility(final int visibility) {
        mCancelVisibility = visibility;
    }

    /**
     * OKボタンの表示文言を変更する.
     *
     * @param text 表示文言
     */
    public void setOkButtonText(final String text) {
        if (mConfirmText != null) {
            mConfirmText = text;
        }
    }

    /**
     * CANCELボタンの表示文言を変更する.
     *
     * @param text 表示文言
     */
    public void setCancelButtonText(final String text) {
        if (mCancelText != null) {
            mCancelText = text;
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialogInterface) {
        if (mDialogDismissCallback != null) {
            //バックキーをキャンセルとして扱うスイッチが有効で、最後に押されたキーがバックボタンならば、
            //キーではなくキャンセルボタンとして扱う
            if(mBackKeyAsCancel && sLastKeyCode == KeyEvent.KEYCODE_BACK) {
                mIsButtonTap = true;
            }

            //ダイアログが閉じたときのコールバック
            mDialogDismissCallback.allDismissCallback();
            //ボタンタップ時は動作させない
            if (!mIsButtonTap) {
                //ボタンタップ以外でダイアログが閉じた場合
                mDialogDismissCallback.otherDismissCallback();
            }
        }
        mIsButtonTap = false;
        sLastKeyCode = 0;
    }

    /**
     * 画面外タップのキャンセル処理の可/不可を設定.
     *
     * @param cancelable キャンセル可能ならばtrue
     */
    public void setOnTouchOutside(final boolean cancelable) {
        mCancelableOutside = cancelable;
    }

    /**
     * ダイアログのボタンが押されているかどうかを見る.
     *
     * @return ダイアログのボタンが押されていた場合はtrue
     */
    public boolean isButtonTap() {
        return mIsButtonTap;
    }

    /**
     * バックキーでのダイアログキャンセルを、ダイアログのキャンセルボタンとして扱うか否かを指定する
     *
     * @param setSwitch trueを指定すると、バックキーでのキャンセルがダイアログのキャンセルボタンと同じ扱いになる
     */
    public void setBackKeyAsCancel(boolean setSwitch) {
        mBackKeyAsCancel = setSwitch;
    }
}
