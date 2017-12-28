/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import java.util.List;

public class CustomDialog {
    private Context context;
    private AlertDialog dialog;
    private DialogType dialogType;
    private String title;
    private String content;
    private List<String> list;
    private ApiOKCallback apiOKCallback;
    private ApiSelectCallback apiSelectCallback;
    private ApiItemSelectCallback apiItemSelectCallback;
    private String confirmText = null;
    private String cancelText = null;
    private boolean cancelable = true;

    private int cancelVisibility = View.VISIBLE;
    private int confirmVisibility = View.VISIBLE;
    private boolean isCancelVisibilityChanged = false;
    private boolean isConfirmVisibilityChanged = false;

    /**
     * OKを返却するためのコールバック
     */
    public interface ApiOKCallback {
        void onOKCallback(boolean isOK);
    }

    /**
     * SELECTを返却するためのコールバック
     */
    public interface ApiSelectCallback {
        void onSelectCallback(int position);
    }

    /**
     * SELECTを返却するためのコールバック
     */
    public interface ApiItemSelectCallback {
        void onItemSelectCallback(AlertDialog dialog, int position);
    }

    public void setOkCallBack(ApiOKCallback apiOKCallback) {
        this.apiOKCallback = apiOKCallback;
    }

    public void setSelectCallBack(ApiSelectCallback apiSelectCallback) {
        this.apiSelectCallback = apiSelectCallback;
    }

    public void setItemSelectCallback(ApiItemSelectCallback callback) {
        this.apiItemSelectCallback = callback;
    }

    public CustomDialog(Context context, DialogType dialogType) {
        this.context = context;
        this.dialogType = dialogType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSelectData(List<String> list) {
        this.list = list;
    }

    public enum DialogType {
        ERROR,          //エラーダイアログ
        CONFIRM,        //確認ダイアログ
        SELECT          //選択ダイアログ
    }

    /**
     * ダイアログの表示
     */
    public void showDialog() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnKeyListener(keyListener);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.custom_dialog);
            initView(window);
        } else {
            dismissDialog();
        }
    }

    /**
     * ダイアログビューの初期化
     * @param window スクリーン
     */
    private void initView(Window window) {
        TextView titleTextView = window.findViewById(R.id.custom_dialog_title_tv);
        TextView contentTextView = window.findViewById(R.id.custom_dialog_content);
        if (TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setText(title);
        }
        if (TextUtils.isEmpty(content)) {
            contentTextView.setVisibility(View.GONE);
        } else {
            contentTextView.setText(content);
        }
        TextView tv_confirm = window.findViewById(R.id.custom_dialog_confirm);
        TextView tv_cancel = window.findViewById(R.id.custom_dialog_cancel);
        switch (dialogType) {
            case ERROR:
                tv_cancel.setVisibility(View.GONE);
                if(confirmText != null) {
                    tv_confirm.setText(confirmText);
                }
                break;
            case SELECT:
                if(isConfirmVisibilityChanged) {
                    tv_confirm.setVisibility(confirmVisibility);
                } else {
                    tv_confirm.setVisibility(View.GONE);
                }
                if(isCancelVisibilityChanged) {
                    tv_cancel.setVisibility(cancelVisibility);
                }
                window.findViewById(R.id.custom_dialog_line_separete).setVisibility(View.VISIBLE);
                window.findViewById(R.id.custom_dialog_sl).setVisibility(View.VISIBLE);
                if (list != null) {
                    LinearLayout linearLayout = window.findViewById(R.id.custom_dialog_ll);
                    for (int i = 0; i < list.size(); i++) {
                        TextView tabTextView = new TextView(context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        tabTextView.setLayoutParams(params);
                        tabTextView.setPadding(0, (int) context.getResources().getDimension(R.dimen.contents_detail_5dp),
                                0, (int) context.getResources().getDimension(R.dimen.contents_detail_5dp));
                        tabTextView.setText(list.get(i));
                        tabTextView.setBackgroundResource(R.drawable.rectangele_white);
                        tabTextView.setGravity(Gravity.CENTER);
                        tabTextView.setTextColor(Color.BLUE);
                        tabTextView.setTag(i);
                        tabTextView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = (int) view.getTag();
                                if (apiSelectCallback != null) {
                                    apiSelectCallback.onSelectCallback(position);
                                }
                                if(apiItemSelectCallback != null) {
                                    apiItemSelectCallback.onItemSelectCallback(dialog ,position);
                                }
                            }
                        });
                        linearLayout.addView(tabTextView);
                    }
                }
                break;
            case CONFIRM:
                if(confirmText != null) {
                    tv_confirm.setText(confirmText);
                }
                if(cancelText != null) {
                    tv_cancel.setText(cancelText);
                }
                break;
            default:
                break;

        }
        if(tv_confirm.getVisibility() == View.VISIBLE){
            tv_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    dismissDialog();
                    if (apiOKCallback != null) {
                        apiOKCallback.onOKCallback(true);
                    }
                }
            });
        }
        if (tv_cancel.getVisibility() == View.VISIBLE){
            tv_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.setCancelable(cancelable);

        /*WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = 800;
        window.setAttributes(lp);*/
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    /**
     * ダイアログを閉じる
     */
    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

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
     * DialogType.CONFIRMのconfirmテキストを変更
     * @param resId リソースID
     */
    public void setConfirmText(int resId) {
        confirmText = context.getResources().getString(resId);
    }

    /**
     * DialogType.CONFIRMのcancelテキストを変更
     * @param resId リソースID
     */
    public void setCancelText(int resId) {
        cancelText = context.getResources().getString(resId);
    }

    /**
     * Dialogのcancelableを変更
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * confirmの表示/非表示を設定
     */
    public void setConfirmVisibility(int visibility) {
        isConfirmVisibilityChanged = true;
        confirmVisibility = visibility;
    }

    /**
     * cancelの表示/非表示を設定
     */
    public void setCancelVisibility(int visibility) {
        isCancelVisibilityChanged = true;
        cancelVisibility = visibility;
    }
}
