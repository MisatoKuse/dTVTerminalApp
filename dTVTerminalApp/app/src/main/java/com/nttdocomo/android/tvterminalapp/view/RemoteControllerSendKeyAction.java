/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;

import java.util.HashMap;
import java.util.Map;
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
    private View mView;

    private static final int SEND_KEYCODE_PARAM_ACTION_UP = 1;
    private static final int SEND_KEYCODE_PARAM_ACTION_DOWN = 0;

    private enum RepeatTaskStatus {
        REPEAT_STATUS_DEFAULT,// 初期状態
        REPEAT_STATUS_STAND_BY, // 実行待ち
        REPEAT_STATUS_DURING_STARTUP, // 起動中（初回起動待ち）
        REPEAT_STATUS_EXECUTION, // リピート処理実行
    }

    public RemoteControllerSendKeyAction(Context context) {
        mContext = context;
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();
    }

    public void initRemoteControllerPlayerView(View view) {
        mView = view;
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
        ImageView remote_controller_bt_record_list = null;
        ImageView remote_controller_bt_tvprogram = null;
        ImageView remote_controller_bt_decide = null;
        ImageView remote_controller_bt_up = null;
        ImageView remote_controller_bt_left = null;
        ImageView remote_controller_bt_down = null;
        ImageView remote_controller_bt_right = null;
        ImageView remote_controller_bt_back = null;
        ImageView remote_controller_bt_toHome = null;
        Button remote_controller_iv_playOrStop = null;
        Button remote_controller_iv_blue = null;
        Button remote_controller_iv_red = null;
        Button remote_controller_iv_green = null;
        Button remote_controller_iv_yellow = null;
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
                    setTouchSelector(v.getId(), false);
                    DTVTLogger.debug("MotionEvemt ACTION_UP");
                    if (mRepeatStateManagement.mStatus == RepeatTaskStatus.REPEAT_STATUS_EXECUTION) {
                        // リピート実行中の場合
                        sendKeyCode(v.getId(),SEND_KEYCODE_PARAM_ACTION_UP,true, mContext);
                        mRepeatStateManagement.repeatCancel();
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    } else if (mRepeatStateManagement.mStatus == RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP) {
                        // リピート処理を1度も行っていない場合
                        sendKeyCode(v.getId(),SEND_KEYCODE_PARAM_ACTION_UP,false, mContext);
                        mRepeatStateManagement.repeatCancel();
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                        DTVTLogger.debug("sendKeyCode");
                    } else {
                        // nop.
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    setTouchSelector(v.getId(), true);
                    sendKeyCode(v.getId(),SEND_KEYCODE_PARAM_ACTION_DOWN,false, mContext);
                    DTVTLogger.debug("MotionEvemt ACTION_DOWN");
                    if (mRepeatStateManagement == null) {
                        mRepeatStateManagement = new RepeatStateManagement(mHandler, v.getId());
                        mRepeatStateManagement.executeTimerTask();
                    } else {
                        mRepeatStateManagement.repeatCancel();
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP);
                        mRepeatStateManagement.executeTimerTask();
                        mRepeatStateManagement.setRepeatButtonId(v.getId());
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setTouchSelector(v.getId(), false);
                    sendKeyCode(v.getId(),SEND_KEYCODE_PARAM_ACTION_UP,true, mContext);
                    DTVTLogger.debug("MotionEvemt ACTION_CANCEL");
                    if (null != mRepeatStateManagement) {
                        mRepeatStateManagement.repeatCancel();
                        mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    }
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    DTVTLogger.debug("MotionEvemt ACTION_OUTSIDE");
                    mRepeatStateManagement.repeatCancel();
                    mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    DTVTLogger.debug("MotionEvemt ACTION_MOVE");
                    break;
                default:
                    DTVTLogger.debug("MotionEvent Another");
                    mRepeatStateManagement.repeatCancel();
                    mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                    break;
            }
            DTVTLogger.end();
            return true;
        }
    };

    // selector画像名に対応する STBキーコード
    private static final Map<Integer, int[]> keyDownUpSelector = new HashMap<Integer, int[]>() {
        {
            put(R.id.remote_controller_bt_up,
                    new int[]{R.mipmap.remote_player_main_btn_arrow_top, R.mipmap.remote_tap_player_main_btn_arrow_top}); // カーソル (上下左右)
            put(R.id.remote_controller_bt_down,
                    new int[]{R.mipmap.remote_player_main_btn_arrow_bottom, R.mipmap.remote_tap_player_main_btn_arrow_bottom});
            put(R.id.remote_controller_bt_left,
                    new int[]{R.mipmap.remote_player_main_btn_arrow_left, R.mipmap.remote_tap_player_main_btn_arrow_left});
            put(R.id.remote_controller_bt_right,
                    new int[]{R.mipmap.remote_player_main_btn_arrow_right, R.mipmap.remote_tap_player_main_btn_arrow_right});
            put(R.id.remote_controller_bt_toHome,
                    new int[]{R.mipmap.remote_player_sub_btn_home, R.mipmap.remote_tap_player_sub_btn_home}); // ホーム
            // チャンネル (1～12) ※ チャンネル (10)は KEYCODE_0となる
            put(R.id.remote_controller_bt_one,
                    new int[]{R.mipmap.remote_ch_btn_ch_01, R.mipmap.remote_tap_ch_btn_ch_01});
            put(R.id.remote_controller_bt_two,
                    new int[]{R.mipmap.remote_ch_btn_ch_02, R.mipmap.remote_tap_ch_btn_ch_02});
            put(R.id.remote_controller_bt_three,
                    new int[]{R.mipmap.remote_ch_btn_ch_03, R.mipmap.remote_tap_ch_btn_ch_03});
            put(R.id.remote_controller_bt_four,
                    new int[]{R.mipmap.remote_ch_btn_ch_04, R.mipmap.remote_tap_ch_btn_ch_04});
            put(R.id.remote_controller_bt_five,
                    new int[]{R.mipmap.remote_ch_btn_ch_05, R.mipmap.remote_tap_ch_btn_ch_05});
            put(R.id.remote_controller_bt_six,
                    new int[]{R.mipmap.remote_ch_btn_ch_06, R.mipmap.remote_tap_ch_btn_ch_06});
            put(R.id.remote_controller_bt_seven,
                    new int[]{R.mipmap.remote_ch_btn_ch_07, R.mipmap.remote_tap_ch_btn_ch_07});
            put(R.id.remote_controller_bt_eight,
                    new int[]{R.mipmap.remote_ch_btn_ch_08, R.mipmap.remote_tap_ch_btn_ch_08});
            put(R.id.remote_controller_bt_nine,
                    new int[]{R.mipmap.remote_ch_btn_ch_09, R.mipmap.remote_tap_ch_btn_ch_09});
            put(R.id.remote_controller_bt_ten,
                    new int[]{R.mipmap.remote_ch_btn_ch_10, R.mipmap.remote_tap_ch_btn_ch_10}); // ※ チャンネル (10)は (0)
            put(R.id.remote_controller_bt_eleven,
                    new int[]{R.mipmap.remote_ch_btn_ch_11, R.mipmap.remote_tap_ch_btn_ch_11});
            put(R.id.remote_controller_bt_twelve,
                    new int[]{R.mipmap.remote_ch_btn_ch_12, R.mipmap.remote_tap_ch_btn_ch_12});
            put(R.id.remote_controller_bt_degital,
                    new int[]{R.mipmap.remote_ch_btn_terrestrialdigital, R.mipmap.remote_tap_ch_btn_terrestrialdigital}); // 地デジ
            put(R.id.remote_controller_bt_bs,
                    new int[]{R.mipmap.remote_ch_btn_bs, R.mipmap.remote_tap_ch_btn_bs}); // BS
            put(R.id.remote_controller_bt_iptv,
                    new int[]{R.mipmap.remote_ch_btn_iptv, R.mipmap.remote_tap_ch_btn_iptv}); // IPTV
            put(R.id.remote_controller_bt_tv_program,
                    new int[]{R.mipmap.remote_player_sub_btn_tv_schedule, R.mipmap.remote_tap_player_sub_btn_tv_schedule}); // 番組表
            put(R.id.remote_controller_bt_decide,
                    new int[]{R.mipmap.remote_player_main_btn_decision, R.mipmap.remote_tap_player_main_btn_decision});  // 決定
            put(R.id.remote_controller_bt_back,
                    new int[]{R.mipmap.remote_player_sub_btn_back, R.mipmap.remote_tap_player_sub_btn_back});  // 戻る
            put(R.id.remote_controller_iv_playOrStop,
                    new int[]{R.mipmap.remote_player_sub_btn_play_stop, R.mipmap.remote_tap_player_sub_btn_play_stop});  // 再生/停止
            put(R.id.remote_controller_iv_blue,
                    new int[]{R.mipmap.remote_player_color_btn_blue_btn, R.mipmap.remote_tap_player_color_btn_blue_btn}); // カラー (青)/10秒戻し
            put(R.id.remote_controller_iv_red,
                    new int[]{R.mipmap.remote_player_color_btn_red_btn, R.mipmap.remote_tap_player_color_btn_red_btn}); // カラー (赤)/巻き戻し
            put(R.id.remote_controller_iv_green,
                    new int[]{R.mipmap.remote_player_color_btn_green_btn, R.mipmap.remote_tap_player_color_btn_green_btn});// カラー (緑)/早送り
            put(R.id.remote_controller_iv_yellow,
                    new int[]{R.mipmap.remote_player_color_btn_yellow_btn, R.mipmap.remote_tap_player_color_btn_yellow_btn}); // カラー (黄)/30秒送り
            put(R.id.remote_controller_bt_channel_plus,
                    new int[]{R.mipmap.remote_ch_btn_ch_vertical, R.mipmap.remote_tap_ch_btn_ch_vertical_top}); // チャンネル (上下)
            put(R.id.remote_controller_bt_channel_minus,
                    new int[]{R.mipmap.remote_ch_btn_ch_vertical, R.mipmap.remote_tap_ch_btn_ch_vertical_bottom});
            put(R.id.remote_controller_bt_notice,
                    new int[]{R.mipmap.remote_ch_btn_info, R.mipmap.remote_tap_ch_btn_info});  // お知らせ
            put(R.id.remote_controller_bt_ddata,
                    new int[]{R.mipmap.remote_ch_btn_d_data, R.mipmap.remote_tap_ch_btn_d_data}); // dデータ
            put(R.id.remote_controller_bt_record_list,
                    new int[]{R.mipmap.remote_player_sub_btn_recordinglist, R.mipmap.remote_tap_player_sub_btn_recordinglist}); // 録画リスト
        }
    };

    private void setTouchSelector(int viewId, boolean isDown){
        int selectorPics[] = null;
        if (keyDownUpSelector.containsKey(viewId)) {
            selectorPics = keyDownUpSelector.get(viewId);
        }
        if(selectorPics != null && selectorPics.length > 1){
            if(mView.findViewById(viewId) instanceof Button){
                Button button = mView.findViewById(viewId);
                if(isDown){
                    if(R.id.remote_controller_bt_channel_plus == viewId
                            || R.id.remote_controller_bt_channel_minus == viewId){
                        ImageView imageView = mView.findViewById(R.id.remote_controller_tv_channel);
                        imageView.setImageResource(selectorPics[1]);
                    }else{
                        button.setBackgroundResource(selectorPics[1]);
                    }
                } else {
                    if(R.id.remote_controller_bt_channel_plus == viewId
                            || R.id.remote_controller_bt_channel_minus == viewId){
                        ImageView imageView = mView.findViewById(R.id.remote_controller_tv_channel);
                        imageView.setImageResource(selectorPics[0]);
                    }else{
                        button.setBackgroundResource(selectorPics[0]);
                    }
                }
            } else if(mView.findViewById(viewId) instanceof ImageView){
                ImageView imageView = mView.findViewById(viewId);
                if(isDown){
                    imageView.setImageResource(selectorPics[1]);
                } else {
                    imageView.setImageResource(selectorPics[0]);
                }
            }
        }
    }

    /**
     * キーコードを送信する
     * @param viewId
     */
    private void sendKeyCode(int viewId,int action,boolean isCancelFlg, Context context) {
        mRemoteControlRelayClient.sendKeycode(viewId, action, isCancelFlg, context);
    }


    /**
     * 連続送信タイマークラス
     */
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
        private long EXECUTION_INTERVAL = 50;
        private long DELAY_TIME = 500;


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
//                    DTVTLogger.debug("status" + mStatus.toString());
                    // 連打フラグをみて処理を続けるか判断する
                    if (mStatus != RepeatTaskStatus.REPEAT_STATUS_EXECUTION) {
                        DTVTLogger.debug("CHANGE_STATUS EXECUTION");
                        mStatus = RepeatTaskStatus.REPEAT_STATUS_EXECUTION;
                    }
                    // クリック処理を実行する
                    sendKeyCode(mRepeatButtonId,SEND_KEYCODE_PARAM_ACTION_DOWN,false, mContext);
                    DTVTLogger.debug("sendKeyCode");

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
         * リピート処理の終了
         */
        public void repeatCancel() {
            if(mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
        }
    }

    /**
     * UI画面が閉じられた際タイマーをキャンセルする
     */
    public void cancelTimer() {
        if(mRepeatStateManagement != null) {
            mRepeatStateManagement.repeatCancel();
            mRepeatStateManagement = null;
        }
    }

    /**
     * RelayClientを取得する
     * @return RemoteControlRelayClient
     */
    public RemoteControlRelayClient getRelayClient() {
        return mRemoteControlRelayClient;
    }
}