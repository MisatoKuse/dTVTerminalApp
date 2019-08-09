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

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * カスタムダイアログ.
 */
public class CustomDialog implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    /** コンテキスト. */
    private Context mContext;
    /** ダイアログ. */
    private AlertDialog mDialog = null;
    /** ダイアログ種別. */
    private DialogType mDialogType;
    /** タイトル. */
    private String mTitle = null;
    /** 本文. */
    private String mContent = null;
    /** OK押下時のコールバック. */
    private ApiOKCallback mApiOKCallback = null;
    /** Cancel押下時のコールバック. */
    private ApiCancelCallback mApiCancelCallback = null;
    /** Neutral押下時のコールバック. */
    private ApiNeutralCallback mApiNeutralCallback = null;
    /**リストタップのコールバック. */
    private ApiListItemClickCallback mApiListItemClickCallback = null;
    /** ボタンタップ以外でダイアログが閉じた時のコールバック. */
    private DismissCallback mDialogDismissCallback = null;
    /** OKボタンに表示する文字列. */
    private String mConfirmText = null;
    /** Cancelボタンに表示する文字列. */
    private String mCancelText = null;
    /** leftボタンに表示する文字列. */
    private String mLeftText = null;
    /** ダイアログのcancelable設定値. */
    private boolean mCancelable = true;
    /** ボタンタップフラグ. **/
    private boolean mIsButtonTap = false;

    /** 画面外タップによるキャンセル判定値. */
    private boolean mCancelableOutside = true;
    /** バックキーをキャンセルボタンとして扱うスイッチ. */
    private boolean mBackKeyAsCancel = false;
    /** バックキータップの可能／不可. */
    private boolean mEnableBackkey = true;
    /** バックキーをアプリ終了として扱うスイッチ. */
    private boolean mBackKeyAsFinishActivity = false;
    /**独自レイアウトView.*/
    private View mCustomView;
    /** リストダイアログデータソース. */
    private String[] mItemList = null;
    /** ShowDialogType.*/
    private ShowDialogType mShowDialogType = ShowDialogType.COMMON_DIALOG;

    /**
     * 最後に扱ったキーコード.
     *
     */
    private int mLastKeyCode = 0;

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
     * Neutralボタン押下を返却するためのコールバック.
     */
    public interface ApiNeutralCallback {
        /**
         * Neutralボタン押下時のコールバック.
         */
        void onNeutralCallback();
    }

    /**
     * 排他的リスト選択のコールバック.
     */
    public interface ApiListItemClickCallback {
        /**
         * 排他的選択リストダイアログ項目押下時のコールバック.
         * @param which ポジション
         */
        void onApiListItemClickCallback(int which);
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
     * getErrorDialogType.
     * @return ShowDialogType
     */
    public ShowDialogType getErrorDialogType() {
        return mShowDialogType;
    }

    /**
     * setErrorDialogType.
     * @param showDialogType showDialogType
     */
    public void setErrorDialogType(final ShowDialogType showDialogType) {
        this.mShowDialogType = showDialogType;
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
     * リストタップコールバック設定.
     * @param apiListItemClickCallback リストタップコールバック
     */
    public void setApiListItemClickCallback(final ApiListItemClickCallback apiListItemClickCallback) {
        this.mApiListItemClickCallback = apiListItemClickCallback;
    }

    /**
     * Neutralボタン押下を返却するためのコールバック.
     *
     * @param apiNeutralCallback Neutralボタン押下を返却するためのコールバック
     */
    public void setApiNeutralCallback(final ApiNeutralCallback apiNeutralCallback) {
        this.mApiNeutralCallback = apiNeutralCallback;
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
     * 選択リストを設定.
     * @param list リストデータ
     */
    public void setListSelectItems(final String[] list) {
        this.mItemList = list;
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
         * ダイアログタイプ：リスト(独自レイアウト).
         */
        SELECT,          //選択ダイアログ
        /**
         * ダイアログタイプ：リスト(システムレイアウト).
         */
        LIST          //LISTダイアログ
    }

    /**
     * ShowDialogType.
     */
    public enum ShowDialogType {
        /**. dアカウントヘルプ画面へ遷移*/
        D_ACCOUNT_REGISTRATION_HELP,
        /**. dアカウント変わったため、ホーム画面に遷移*/
        D_ACCOUNT_CHANGED,
        /** プレイヤーエラー.*/
        SECURE_PLAYER_ERROR,
        /** コンテンツ詳細エラー.*/
        CONTENT_DETAIL_GET_ERROR,
        /** 強制バージョンアップ.*/
        FORCED_VERSION_UP,
        /** 任意バージョンアップ.*/
        OPTIONAL_VERSION_UP,
        /** 設定ファイルエラー.*/
        SETTING_FILE_ERROR_DIALOG,
        /** dtv(起動).*/
        DTV_EPISODE_LIST_ITEM_DIALOG,
        /** 確認ダイアログ.*/
        LAUNCH_STB_START_DIALOG,
        /**. 画面遷移なし.*/
        COMMON_DIALOG
    }

    /**
     * DialogTapType.
     */
    public enum  DialogTapType {
        /** ok.*/
        OK,
        /** cancel.*/
        CANCEL,
        /** list.*/
        LIST,
        /** back key.*/
        BACK_KEY,
        /** not tap.*/
        NOT_TAP
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
     * 独自レイアウトViewを受け取る.
     * @param view view
     */
    public void setContentView(final View view) {
        this.mCustomView = view;
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
                break;
            case LIST:
                dialogBuilder.setTitle(mTitle).setItems(mItemList, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        dismissDialog();
                        if (mApiListItemClickCallback != null) {
                            mIsButtonTap = true;
                            mApiListItemClickCallback.onApiListItemClickCallback(which);
                        }
                    }
                });
                break;
            case CONFIRM:
                dialogBuilder.setPositiveButton(mConfirmText, this);
                dialogBuilder.setNegativeButton(mCancelText, this);
                break;
            default:
                break;
        }
        if (mCustomView != null) {
            dialogBuilder.setView(mCustomView);
        }
        mDialog = dialogBuilder.create();
        mDialog.setCancelable(mCancelable);
        mDialog.setCanceledOnTouchOutside(mCancelableOutside);
        mDialog.setOnKeyListener(sKeyListener);
        if (mDialogType != DialogType.LIST) {
            if (mCustomView == null) {
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
            }
        }

        //コンテキストがベースアクティビティから継承された物かどうかの判定
        if (mContext instanceof BaseActivity) {
            //ベースアクティビティだった
            BaseActivity baseActivity = (BaseActivity) mContext;

            if (baseActivity.isFinishing()) {
                //該当アクティビティが既に終わっているならば、ダイアログ表示をスキップする
                DTVTLogger.debug("initView: already activity(" + mContext.getClass() + ") finish or pause:" + mContent);
                return;
            }

            DTVTLogger.debug("initView: show dialog(" + mContext.getClass() + "):" + mContent);

            //ダイアログを表示する
            mDialog.show();
            setButtonClickListener();
        } else {
            //ベースアクティビティ以外の場合のフェールセーフ処理
            //本アプリのアクティビティは全てBaseアクティビティを継承しているのここは実行されない筈
            DTVTLogger.debug("initView: not BaseActivity (" + mContext.getClass() + "):" + mContent);

            //アクティビティかつ終了していないならばダイアログを表示する
            if (mContext instanceof BaseActivity && !((Activity) mContext).isFinishing()) {
                //ダイアログを表示する
                mDialog.show();
                setButtonClickListener();
            }
        }
    }

    /**
     * setButtonClickListener.
     */
    private void setButtonClickListener() {
        if (getErrorDialogType() == ShowDialogType.OPTIONAL_VERSION_UP) {
            mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (mApiOKCallback != null) {
                        mIsButtonTap = true;
                        mApiOKCallback.onOKCallback(true);
                    }
                }
            });
        }
    }

    /**
     * アプリ終了.
     */
    private void finishActivity() {
        if (mContext != null && mContext instanceof BaseActivity) {
            //コンテキストがベースアクティビティから継承された物かどうかの判定
            ((BaseActivity) mContext).stopAllActivity();
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
            case AlertDialog.BUTTON_NEUTRAL:
                dismissDialog();
                if (mApiNeutralCallback != null) {
                    mIsButtonTap = true;
                    mApiNeutralCallback.onNeutralCallback();
                }
                break;
            default:
                break;
        }
    }

    /**
     * バックキー押下時の動作.
     */
    private final OnKeyListener sKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
            //キーコードを退避する
            mLastKeyCode = keyCode;

            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                    && event.getAction() != KeyEvent.ACTION_UP) {
                if (mEnableBackkey) {
                    dialog.dismiss();
                    if (mBackKeyAsFinishActivity) {
                        finishActivity();
                    }
                    return true;
                }
            }
            return false;
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
     * DialogType.CONFIRMのNeutralテキストを変更.
     * @param resId リソースID
     */
    public void setLeftText(final int resId) {
        Resources resources = mContext.getResources();
        mLeftText = resources.getString(resId);
    }

    /**
     * Dialogのcancelableを変更.
     *
     * @param cancelable キャンセル可否設定
     */
    public void setCancelable(final boolean cancelable) {
        this.mCancelable = cancelable;
    }

    @Override
    public void onDismiss(final DialogInterface dialogInterface) {
        if (mDialogDismissCallback != null) {
            //バックキーをキャンセルとして扱うスイッチが有効で、最後に押されたキーがバックボタンならば、
            //キーではなくキャンセルボタンとして扱う
            if (mBackKeyAsCancel && mLastKeyCode == KeyEvent.KEYCODE_BACK) {
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
        mLastKeyCode = 0;
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
     * バックキータップの可能／不可を設定.
     *
     * @param  enable バックキーのタップが不可ならばfalse
     */
    public void setOnTouchBackkey(final boolean enable) {
        mEnableBackkey = enable;
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
     * ボタンが押されたかどうかの情報をセット.
     * @param isButton ボタンで押されていた場合はtrue
     */
    public void setButtonTap(final boolean isButton) {
        mIsButtonTap = isButton;
    }

    /**
     * バックキーでのダイアログキャンセルを、ダイアログのキャンセルボタンとして扱うか否かを指定する.
     *
     * @param setSwitch trueを指定すると、バックキーでのキャンセルがダイアログのキャンセルボタンと同じ扱いになる
     */
    public void setBackKeyAsCancel(final boolean setSwitch) {
        mBackKeyAsCancel = setSwitch;
    }

    /**
     * バックキーでダイアログのアプリ終了ボタンとして扱うか否かを指定する.
     *
     * @param setSwitch trueを指定すると、バックキーでのアプリが終了になる
     */
    public void setBackKeyAsFinishActivity(final boolean setSwitch) {
        mBackKeyAsFinishActivity = setSwitch;
    }
}
