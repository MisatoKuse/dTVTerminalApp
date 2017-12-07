/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.remotecontroller;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.home.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;

import java.util.Timer;
import java.util.TimerTask;

// Viewに長押しでクリックを繰り返す処理を追加する
public class RemoteControllerSendKeyAction {

    private Handler mHandler = new Handler();
    private Context mContext = null;
    private RemoteControlRelayClient mRemoteControlRelayClient = null;
    private RepeatStateManagement mRepeatStateManagement = null;
    private RemoteControllerChannelViewHolder mChannelViewHolder = null;
    private RemoteControllerPlayerViewHolder mPlayerViewHolder = null;

    private enum RepeatTaskStatus {
        REPEAT_STATUS_DEFAULT,// 初期状態
        REPEAT_STATUS_STAND_BY, // 実行待ち
        REPEAT_STATUS_DURING_STARTUP, // 起動中（初回起動待ち）
        REPEAT_STATUS_EXECUTION, // リピート処理実行
        REPEAT_STATUS_CANCEL, // キャンセル
    }

    public RemoteControllerSendKeyAction(Context context) {
        mContext = context;
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();
    }

    public void initRemoteControllerPlayerView(View view) {
        mPlayerViewHolder = setPlayerViewHolder(new RemoteControllerPlayerViewHolder(), view);
    }

    public void initRemoteControllerChannelView(View view) {
        mChannelViewHolder = setChannelViewHolder(new RemoteControllerChannelViewHolder(), view);
    }


    private RemoteControllerChannelViewHolder setChannelViewHolder(RemoteControllerChannelViewHolder cViewHolder, View view) {
        DTVTLogger.start();
        cViewHolder.remote_controller_iv_power = view.findViewById(R.id.remote_controller_iv_power);
        cViewHolder.remote_controller_bt_degital = view.findViewById(R.id.remote_controller_bt_degital);
        cViewHolder.remote_controller_bt_bs = view.findViewById(R.id.remote_controller_bt_bs);
        cViewHolder.remote_controller_bt_iptv = view.findViewById(R.id.remote_controller_bt_iptv);
        cViewHolder.remote_controller_bt_two = view.findViewById(R.id.remote_controller_bt_two);
        cViewHolder.remote_controller_bt_one = view.findViewById(R.id.remote_controller_bt_one);
        cViewHolder.remote_controller_bt_three = view.findViewById(R.id.remote_controller_bt_three);
        cViewHolder.remote_controller_bt_four = view.findViewById(R.id.remote_controller_bt_four);
        cViewHolder.remote_controller_bt_five = view.findViewById(R.id.remote_controller_bt_five);
        cViewHolder.remote_controller_bt_six = view.findViewById(R.id.remote_controller_bt_six);
        cViewHolder.remote_controller_bt_seven = view.findViewById(R.id.remote_controller_bt_seven);
        cViewHolder.remote_controller_bt_eight = view.findViewById(R.id.remote_controller_bt_eight);
        cViewHolder.remote_controller_bt_nine = view.findViewById(R.id.remote_controller_bt_nine);
        cViewHolder.remote_controller_bt_ten = view.findViewById(R.id.remote_controller_bt_ten);
        cViewHolder.remote_controller_bt_eleven = view.findViewById(R.id.remote_controller_bt_eleven);
        cViewHolder.remote_controller_bt_twelve = view.findViewById(R.id.remote_controller_bt_twelve);
        cViewHolder.remote_controller_bt_channel_plus = view.findViewById(R.id.remote_controller_bt_channel_plus);
        cViewHolder.remote_controller_bt_channel_minus = view.findViewById(R.id.remote_controller_bt_channel_minus);
        cViewHolder.remote_controller_bt_notice = view.findViewById(R.id.remote_controller_bt_notice);
        cViewHolder.remote_controller_bt_ddata = view.findViewById(R.id.remote_controller_bt_ddata);

        cViewHolder = setRemoteControllerChannelViewHolderListener(cViewHolder);

        DTVTLogger.end();
        return cViewHolder;
    }

    private RemoteControllerPlayerViewHolder setPlayerViewHolder(RemoteControllerPlayerViewHolder pViewHolder, View view) {
        DTVTLogger.start("" + view.getId());
        pViewHolder.remote_controller_bt_record_list = view.findViewById(R.id.remote_controller_bt_record_list);
        pViewHolder.remote_controller_bt_tvprogram = view.findViewById(R.id.remote_controller_bt_tv_program);
        pViewHolder.remote_controller_bt_decide = view.findViewById(R.id.remote_controller_bt_decide);
        pViewHolder.remote_controller_bt_up = view.findViewById(R.id.remote_controller_bt_up);
        pViewHolder.remote_controller_bt_left = view.findViewById(R.id.remote_controller_bt_left);
        pViewHolder.remote_controller_bt_down = view.findViewById(R.id.remote_controller_bt_down);
        pViewHolder.remote_controller_bt_right = view.findViewById(R.id.remote_controller_bt_right);
        pViewHolder.remote_controller_bt_back = view.findViewById(R.id.remote_controller_bt_back);
        pViewHolder.remote_controller_bt_toHome = view.findViewById(R.id.remote_controller_bt_toHome);
        pViewHolder.remote_controller_iv_playOrStop = view.findViewById(R.id.remote_controller_iv_playOrStop);
        pViewHolder.remote_controller_iv_blue = view.findViewById(R.id.remote_controller_iv_blue);
        pViewHolder.remote_controller_iv_red = view.findViewById(R.id.remote_controller_iv_red);
        pViewHolder.remote_controller_iv_green = view.findViewById(R.id.remote_controller_iv_green);
        pViewHolder.remote_controller_iv_yellow = view.findViewById(R.id.remote_controller_iv_yellow);

        pViewHolder = setRemoteControllerPlayerViewHolderListener(pViewHolder);

        DTVTLogger.end();
        return pViewHolder;
    }

    /**
     * ビュー管理クラス
     */
    // プレイヤー操作UI
    private static class RemoteControllerPlayerViewHolder {
        Button remote_controller_bt_record_list = null;
        Button remote_controller_bt_tvprogram = null;
        Button remote_controller_bt_decide = null;
        Button remote_controller_bt_up = null;
        Button remote_controller_bt_left = null;
        Button remote_controller_bt_down = null;
        Button remote_controller_bt_right = null;
        Button remote_controller_bt_back = null;
        Button remote_controller_bt_toHome = null;
        ImageView remote_controller_iv_playOrStop = null;
        ImageView remote_controller_iv_blue = null;
        ImageView remote_controller_iv_red = null;
        ImageView remote_controller_iv_green = null;
        ImageView remote_controller_iv_yellow = null;
    }

    // チャンネル操作UI
    private static class RemoteControllerChannelViewHolder {
        ImageView remote_controller_iv_power = null;
        Button remote_controller_bt_degital = null;
        Button remote_controller_bt_bs = null;
        Button remote_controller_bt_iptv = null;
        Button remote_controller_bt_two = null;
        Button remote_controller_bt_one = null;
        Button remote_controller_bt_three = null;
        Button remote_controller_bt_four = null;
        Button remote_controller_bt_five = null;
        Button remote_controller_bt_six = null;
        Button remote_controller_bt_seven = null;
        Button remote_controller_bt_eight = null;
        Button remote_controller_bt_nine = null;
        Button remote_controller_bt_ten = null;
        Button remote_controller_bt_eleven = null;
        Button remote_controller_bt_twelve = null;
        Button remote_controller_bt_channel_plus = null;
        Button remote_controller_bt_channel_minus = null;
        Button remote_controller_bt_notice = null;
        Button remote_controller_bt_ddata = null;
    }

    /**
     * リモコンUI（プレイヤー操作）の各ボタンにリスナーを設定
     *
     * @param pViewHolder
     * @return
     */
    private RemoteControllerPlayerViewHolder setRemoteControllerPlayerViewHolderListener(RemoteControllerPlayerViewHolder pViewHolder) {
        DTVTLogger.start();
        pViewHolder.remote_controller_bt_record_list.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_tvprogram.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_decide.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_up.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_left.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_down.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_right.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_back.setOnTouchListener(mListener);
        pViewHolder.remote_controller_bt_toHome.setOnTouchListener(mListener);
        pViewHolder.remote_controller_iv_playOrStop.setOnTouchListener(mListener);
        pViewHolder.remote_controller_iv_blue.setOnTouchListener(mListener);
        pViewHolder.remote_controller_iv_red.setOnTouchListener(mListener);
        pViewHolder.remote_controller_iv_green.setOnTouchListener(mListener);
        pViewHolder.remote_controller_iv_yellow.setOnTouchListener(mListener);

        DTVTLogger.end();
        return pViewHolder;
    }

    /**
     * リモコンUI（チャンネル操作）の各ボタンにリスナーを設定
     *
     * @param cViewHolder
     * @return
     */
    private RemoteControllerChannelViewHolder setRemoteControllerChannelViewHolderListener(RemoteControllerChannelViewHolder cViewHolder) {
        DTVTLogger.start();
        cViewHolder.remote_controller_iv_power.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_degital.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_bs.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_iptv.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_two.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_one.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_three.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_four.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_five.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_six.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_seven.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_eight.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_nine.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_ten.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_eleven.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_twelve.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_channel_plus.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_channel_minus.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_notice.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_ddata.setOnTouchListener(mListener);

        DTVTLogger.end();
        return cViewHolder;
    }

    // OnTouchListener
    private View.OnTouchListener mListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            DTVTLogger.start();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    DTVTLogger.debug("MotionEvemt ACTION_UP");
                    if (mRepeatStateManagement.mStatus == RepeatTaskStatus.REPEAT_STATUS_EXECUTION) {
                        // リピート実行中の場合
                        mRepeatStateManagement.resetCounter();
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    } else if (mRepeatStateManagement.mStatus == RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP) {
                        // リピート処理を1度も行っていない場合
                        sendKeyCode(v.getId());
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                        DTVTLogger.debug("sendKeyCode");
                    } else {
                        // nop.
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    DTVTLogger.debug("MotionEvemt ACTION_DOWN");
                    if (mRepeatStateManagement == null) {
                        mRepeatStateManagement = new RepeatStateManagement(mHandler, v.getId());
                        mRepeatStateManagement.executeTimerTask();
                    } else {
                        mRepeatStateManagement.resetCounter();
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP);
                        mRepeatStateManagement.setRepeatButtonId(v.getId());
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    DTVTLogger.debug("MotionEvemt ACTION_CANCEL");
                    mRepeatStateManagement.resetCounter();
                    mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    return true;
//                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    DTVTLogger.debug("MotionEvemt ACTION_OUTSIDE");
                    mRepeatStateManagement.resetCounter();
                    mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    DTVTLogger.debug("MotionEvemt ACTION_MOVE");
                    // TODO デフォルトの端末の判定に依存するため、頻出する場合がある
                    // TODO x軸y軸の閾値を設定する必要あり？
//                    mRepeatStateManagement.resetCounter();
//                    mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    break;
                default:
                    DTVTLogger.debug("MotionEvent Another");
                    mRepeatStateManagement.resetCounter();
                    mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    break;
            }
            DTVTLogger.end();
            return false;
        }
    };

    private void sendKeyCode(int viewId) {
        mRemoteControlRelayClient.sendKeycode(viewId);
    }

    private class RepeatStateManagement extends Timer {
        private Handler mHandler = null;
        // 状態管理変数
        private RepeatTaskStatus mStatus = RepeatTaskStatus.REPEAT_STATUS_DEFAULT;
        /**
         * リピート中のButtonID
         */
        private int mRepeatButtonId;
        private TimerTask mTimerTask = null;
        // 実行間隔
        private long EXECUTION_INTERVAL = 500;
        private long DELAY_TIME = 500;
        // 実行回数カウンター
        private int execution_counter = 0;


        RepeatStateManagement(Handler handler, int buttonId) {
            mHandler = handler;
            mRepeatButtonId = buttonId;
        }

        /**
         * タイマータスクの状態を設定
         *
         * @param status
         */
        private void setRepeatTaskStatus(RepeatTaskStatus status) {
            mStatus = status;
        }

        /**
         * TimerTask実行予約処理
         */
        private void executeTimerTask() {
            DTVTLogger.start();
            setTimerTask();
            mStatus = RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP;
            schedule(mTimerTask, DELAY_TIME, EXECUTION_INTERVAL);
            DTVTLogger.end();
        }


        /**
         * TimerTask処理の設定
         */
        private void setTimerTask() {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    DTVTLogger.start();
                    DTVTLogger.debug("status" + mStatus.toString());
                    // 連打フラグをみて処理を続けるか判断する
                    if (execution_counter >= 10 && mStatus != RepeatTaskStatus.REPEAT_STATUS_EXECUTION) {
                        DTVTLogger.debug("CHANGE_STATUS EXECUTION");
                        mStatus = RepeatTaskStatus.REPEAT_STATUS_EXECUTION;
                    } else if (mStatus == RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP
                            && execution_counter < 10) {
                        execution_counter++;
                        DTVTLogger.debug("STARTUP");
                        return;
                    }
                    if (mStatus == RepeatTaskStatus.REPEAT_STATUS_EXECUTION) {
                        // クリック処理を実行する
                        sendKeyCode(mRepeatButtonId);
                        DTVTLogger.debug("sendKeyCode");
                    }
                    DTVTLogger.end();
                }
            };
        }

        /**
         * ボタンのViewIdを設定
         */
        private void setRepeatButtonId(int viewId) {
            mRepeatButtonId = viewId;
        }

        /**
         * execution_counterのリセット
         */
        private void resetCounter() {
            execution_counter = 0;
        }

        /**
         * リピート処理の終了
         */
        public void repeatCancel() {
            mTimerTask.cancel();
        }
    }

    /**
     * プレイヤー操作UIのリスナーを解除
     * @param pViewHolder
     * @return
     */
//    private RemoteControllerPlayerViewHolder removeRemoteControllerPlayerViewHolderListener(RemoteControllerPlayerViewHolder pViewHolder) {
//        DTVTLogger.start();
//        pViewHolder.remote_controller_bt_record_list.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_channel_minus.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_decide.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_up.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_left.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_down.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_right.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_back.setOnTouchListener(null);
//        pViewHolder.remote_controller_bt_toHome.setOnTouchListener(null);
//        pViewHolder.remote_controller_iv_playOrStop.setOnTouchListener(null);
//        pViewHolder.remote_controller_iv_blue.setOnTouchListener(null);
//        pViewHolder.remote_controller_iv_red.setOnTouchListener(null);
//        pViewHolder.remote_controller_iv_green.setOnTouchListener(null);
//        pViewHolder.remote_controller_iv_yellow.setOnTouchListener(null);
//
//        DTVTLogger.end();
//        return pViewHolder;
//    }

    /**
     * チャンネル操作UIのリスナーを解除
     * @param cViewHolder
     * @return
     */
//    private RemoteControllerChannelViewHolder removeRemoteControllerChannelViewHolderListener(RemoteControllerChannelViewHolder cViewHolder) {
//        DTVTLogger.start();
//        cViewHolder.remote_controller_iv_power.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_degital.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_bs.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_iptv.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_two.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_one.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_three.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_four.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_five.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_six.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_seven.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_eight.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_nine.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_ten.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_eleven.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_twelve.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_channel_plus.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_channel_minus.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_notice.setOnTouchListener(null);
//        cViewHolder.remote_controller_bt_ddata.setOnTouchListener(null);
//
//        DTVTLogger.end();
//        return cViewHolder;
//    }
}