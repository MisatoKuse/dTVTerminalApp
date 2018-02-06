package com.nttdocomo.android.tvterminalapp.dataprovider.callback;

public interface TransoceanicCommunicationAlertDialogCallback {
    /**
     * 海外通信警告ダイアログが表示された場合の通知
     */
    void showTransoceanicCommunicationDialogCallback();

    /**
     * 海外通信警告ダイアログのOKが押下された場合の通知
     */
    void selectOkTransoceanicCommunicationDialogCallback();

    /**
     * 海外通信警告ダイアログのNGが押下された場合の通知
     */
    void selectNgTransoceanicCommunicationDialogCallback();
}