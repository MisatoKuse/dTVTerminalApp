/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherApi;
import com.nttdocomo.android.tvterminalapp.relayclient.security.CipherUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * Viewに長押しでクリックを繰り返す処理を追加する.
 */
public class RemoteControllerSendKeyAction {
    /**ハンドラー.*/
    private Handler mHandler = new Handler();
    /**コンテキスト.*/
    private Context mContext = null;
    /**RemoteControlRelayClientインタフェース.*/
    private RemoteControlRelayClient mRemoteControlRelayClient = null;
    /**連続送信タイマークラスインタフェース.*/
    private RepeatStateManagement mRepeatStateManagement = null;
    /** 暗号化処理の鍵交換を同期処理で実行する. */
    private CountDownLatch mLatch = null;
    /** 暗号化処理の鍵交換の同期カウンター. */
    private static int LATCH_COUNT_MAX = 1;
    /**RemoteControllerChannelViewHolder.*/
    private RemoteControllerChannelViewHolder mChannelViewHolder = null;
    /**RemoteControllerPlayerViewHolder.*/
    private RemoteControllerPlayerViewHolder mPlayerViewHolder = null;
    /**View.*/
    private View mView;
    /**SEND_KEYCODE_PARAM_ACTION_UP.*/
    private static final int SEND_KEYCODE_PARAM_ACTION_UP = 1;
    /**SEND_KEYCODE_PARAM_ACTION_DOWN.*/
    private static final int SEND_KEYCODE_PARAM_ACTION_DOWN = 0;

    /**
     * タスクステータス.
     */
    private enum RepeatTaskStatus {
        /**初期状態.*/
        REPEAT_STATUS_DEFAULT, // 初期状態
        /**実行待ち.*/
        REPEAT_STATUS_STAND_BY, // 実行待ち
        /**起動中（初回起動待ち）.*/
        REPEAT_STATUS_DURING_STARTUP, // 起動中（初回起動待ち）
        /**リピート処理実行.*/
        REPEAT_STATUS_EXECUTION, // リピート処理実行
    }

    /**
     * コンストラクタ.
     * @param context コンテキスト.
     */
    public RemoteControllerSendKeyAction(final Context context) {
        mContext = context;
        mRemoteControlRelayClient = RemoteControlRelayClient.getInstance();
    }

    /**
     * コンストラクタ.
     * @param view  view
     */
    public void initRemoteControllerPlayerView(final View view) {
        mView = view;
        mPlayerViewHolder = setPlayerViewHolder(new RemoteControllerPlayerViewHolder(), view);
    }

    /**
     * initRemoteControllerChannelView.
     * @param view view
     */
    public void initRemoteControllerChannelView(final View view) {
        mChannelViewHolder = setChannelViewHolder(new RemoteControllerChannelViewHolder(), view);
    }

    /**
     * チャンネルホルダー設定.
     * @param cViewHolder RemoteControllerChannelViewHolder
     * @param view  View
     * @return cViewHolder
     */
    private RemoteControllerChannelViewHolder setChannelViewHolder(final RemoteControllerChannelViewHolder cViewHolder, final View view) {
        DTVTLogger.start();
        cViewHolder.remote_controller_iv_power = view.findViewById(R.id.remote_controller_iv_power);
        cViewHolder.remote_controller_bt_degital = view.findViewById(R.id.remote_controller_bt_degital);
        cViewHolder.remote_controller_bt_bs = view.findViewById(R.id.remote_controller_bt_bs);
        cViewHolder.remote_controller_bt_specialtychannel = view.findViewById(R.id.remote_controller_bt_specialtychannel);
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
        DTVTLogger.end();
        return setRemoteControllerChannelViewHolderListener(cViewHolder);
    }

    /**
     * プレイヤービューホルダー設定.
     * @param pViewHolder RemoteControllerPlayerViewHolder
     * @param view View
     * @return pViewHolder
     */
    private RemoteControllerPlayerViewHolder setPlayerViewHolder(final RemoteControllerPlayerViewHolder pViewHolder, final View view) {
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
        DTVTLogger.end();
        return setRemoteControllerPlayerViewHolderListener(pViewHolder);
    }

    /**
     * ビュー管理クラス.
     */
    // プレイヤー操作UI
    private static class RemoteControllerPlayerViewHolder {
        /**録画予約リスト.*/
        ImageView remote_controller_bt_record_list = null;
        /**番組表.*/
        ImageView remote_controller_bt_tvprogram = null;
        /**決定.*/
        ImageView remote_controller_bt_decide = null;
        /**up.*/
        ImageView remote_controller_bt_up = null;
        /**left.*/
        ImageView remote_controller_bt_left = null;
        /**down .*/
        ImageView remote_controller_bt_down = null;
        /**right .*/
        ImageView remote_controller_bt_right = null;
        /**back.*/
        ImageView remote_controller_bt_back = null;
        /**ホーム.*/
        ImageView remote_controller_bt_toHome = null;
        /**プレイーまたはストップ.*/
        Button remote_controller_iv_playOrStop = null;
        /**青.*/
        Button remote_controller_iv_blue = null;
        /**赤.*/
        Button remote_controller_iv_red = null;
        /**緑.*/
        Button remote_controller_iv_green = null;
        /**黄.*/
        Button remote_controller_iv_yellow = null;
    }

    /**
     * チャンネル操作UI.
      */
    private static class RemoteControllerChannelViewHolder {
        /**電源.*/
        ImageView remote_controller_iv_power = null;
        /**地デジ.*/
        Button remote_controller_bt_degital = null;
        /**BS.*/
        Button remote_controller_bt_bs = null;
        /**専門Ch.*/
        Button remote_controller_bt_specialtychannel = null;
        /**2.*/
        Button remote_controller_bt_two = null;
        /**1.*/
        Button remote_controller_bt_one = null;
        /**3.*/
        Button remote_controller_bt_three = null;
        /**4.*/
        Button remote_controller_bt_four = null;
        /**5.*/
        Button remote_controller_bt_five = null;
        /**6.*/
        Button remote_controller_bt_six = null;
        /**7.*/
        Button remote_controller_bt_seven = null;
        /**8.*/
        Button remote_controller_bt_eight = null;
        /**9.*/
        Button remote_controller_bt_nine = null;
        /**10.*/
        Button remote_controller_bt_ten = null;
        /**11.*/
        Button remote_controller_bt_eleven = null;
        /**12.*/
        Button remote_controller_bt_twelve = null;
        /**channel_plus.*/
        Button remote_controller_bt_channel_plus = null;
        /**channel_minus.*/
        Button remote_controller_bt_channel_minus = null;
        /**お知らせ.*/
        Button remote_controller_bt_notice = null;
        /**dデータ.*/
        Button remote_controller_bt_ddata = null;
    }

    /**
     * リモコンUI（プレイヤー操作）の各ボタンにリスナーを設定.
     *
     * @param pViewHolder RemoteControllerPlayerViewHolder
     * @return pViewHolder
     */
    private RemoteControllerPlayerViewHolder setRemoteControllerPlayerViewHolderListener(final RemoteControllerPlayerViewHolder pViewHolder) {
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
     * リモコンUI（チャンネル操作）の各ボタンにリスナーを設定.
     *
     * @param cViewHolder RemoteControllerChannelViewHolder
     * @return cViewHolder
     */
    private RemoteControllerChannelViewHolder setRemoteControllerChannelViewHolderListener(final RemoteControllerChannelViewHolder cViewHolder) {
        DTVTLogger.start();
        cViewHolder.remote_controller_iv_power.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_degital.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_bs.setOnTouchListener(mListener);
        cViewHolder.remote_controller_bt_specialtychannel.setOnTouchListener(mListener);
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

    /**
     *  OnTouchListener.
     */
    private View.OnTouchListener mListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            DTVTLogger.start();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    setTouchSelector(v.getId(), false);
                    DTVTLogger.debug("MotionEvemt ACTION_UP");
                    switch (mRepeatStateManagement.mStatus) {
                        case REPEAT_STATUS_EXECUTION:
                            // リピート実行中の場合
                            sendKeyCode(v.getId(), SEND_KEYCODE_PARAM_ACTION_UP, true, mContext);
                            mRepeatStateManagement.repeatCancel();
                            mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                            break;
                        case REPEAT_STATUS_DURING_STARTUP:
                            // リピート処理を1度も行っていない場合
                            sendKeyCode(v.getId(), SEND_KEYCODE_PARAM_ACTION_UP, false, mContext);
                            mRepeatStateManagement.repeatCancel();
                            mRepeatStateManagement.setRepeatTaskStatus(RepeatTaskStatus.REPEAT_STATUS_STAND_BY);
                            DTVTLogger.debug("sendKeyCode");
                            break;
                        default:
                            DTVTLogger.debug("nop");
                            break;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    setTouchSelector(v.getId(), true);
                    sendKeyCode(v.getId(), SEND_KEYCODE_PARAM_ACTION_DOWN, false, mContext);
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
                    sendKeyCode(v.getId(), SEND_KEYCODE_PARAM_ACTION_UP, true, mContext);
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

    /**
     * selector画像名に対応する STBキーコード.
     */
    private static final Map<Integer, int[]> sKeyDownUpSelector = new HashMap<Integer, int[]>() {
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
            put(R.id.remote_controller_bt_specialtychannel,
                    new int[]{R.mipmap.remote_ch_btn_specialtychannel, R.mipmap.remote_tap_ch_btn_specialtychannel}); // 専門ch
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
                    new int[]{R.mipmap.remote_player_color_btn_green_btn, R.mipmap.remote_tap_player_color_btn_green_btn}); // カラー (緑)/早送り
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

    /**
     * タッチリスナー設定.
     * @param viewId  viewId
     * @param isDown タッチされたか
     */
    private void setTouchSelector(final int viewId, final boolean isDown) {
        int[] selectorPics = null;
        if (sKeyDownUpSelector.containsKey(viewId)) {
            selectorPics = sKeyDownUpSelector.get(viewId);
        }
        if (selectorPics != null && selectorPics.length > 1) {
            if (mView.findViewById(viewId) instanceof Button) {
                Button button = mView.findViewById(viewId);
                if (isDown) {
                    switch (viewId) {
                        case R.id.remote_controller_bt_channel_plus:
                        case R.id.remote_controller_bt_channel_minus:
                            ImageView imageView = mView.findViewById(R.id.remote_controller_tv_channel);
                            imageView.setImageResource(selectorPics[1]);
                            break;
                        default:
                            button.setBackgroundResource(selectorPics[1]);
                            break;
                    }
                } else {
                    switch (viewId) {
                        case R.id.remote_controller_bt_channel_plus:
                        case R.id.remote_controller_bt_channel_minus:
                            ImageView imageView = mView.findViewById(R.id.remote_controller_tv_channel);
                            imageView.setImageResource(selectorPics[0]);
                            break;
                        default:
                            button.setBackgroundResource(selectorPics[0]);
                            break;
                    }
                }
            } else if (mView.findViewById(viewId) instanceof ImageView) {
                ImageView imageView = mView.findViewById(viewId);
                if (isDown) {
                    imageView.setImageResource(selectorPics[1]);
                } else {
                    imageView.setImageResource(selectorPics[0]);
                }
            }
        }
    }

    /**
     * キーコードを送信する.
     * @param viewId viewId
     * @param action アクション
     * @param isCancelFlg キャンセルフラグ
     * @param context コンテキスト
     */
    private void sendKeyCode(final int viewId, final int action, final boolean isCancelFlg, final Context context) {

        if (!CipherUtil.hasShareKey()) {
            CipherApi api = new CipherApi(new CipherApi.CipherApiCallback() {
                @Override
                public void apiCallback(final boolean result, final String data) {
                    // 鍵交換処理同期ラッチカウンターを解除する
                    mLatch.countDown();
                }
            });
            DTVTLogger.debug("sending public key");
            api.requestSendPublicKey();
            // 鍵交換処理が終わるまでキーコード送信を待機をさせる.
            // ※この同期を行わないと連続したリモコンキーのタップにより初めの鍵交換が終了するまでに
            // 　次の鍵交換の送信が開始されキーコード送信が失敗する
            mLatch = new CountDownLatch(LATCH_COUNT_MAX);
            try {
                DTVTLogger.debug("sync to completion of public key transmission");
                mLatch.await();
                DTVTLogger.debug("completion of public key transmission");
            } catch (InterruptedException e) {
                DTVTLogger.debug(e);
                return;
            }
        }
        if (CipherUtil.hasShareKey()) {
            mRemoteControlRelayClient.sendKeycode(viewId, action, isCancelFlg, context);
        }
    }

    /**
     * 連続送信タイマークラス.
     */
    private class RepeatStateManagement extends Timer {
        /**
         * ハンドラー.
         */
        private Handler mHandler = null;
        /**
         * 状態管理変数.
         */
        private RepeatTaskStatus mStatus = RepeatTaskStatus.REPEAT_STATUS_DEFAULT;
        /**
         * リピート中のButtonID.
         */
        private int mRepeatButtonId;
        /**
         * タイマー.
         */
        private TimerTask mTimerTask = null;
        /**
         * 実行間隔.
         */
        private long EXECUTION_INTERVAL = 50;
        /**
         * 遅延時間.
         */
        private long DELAY_TIME = 500;

        /**
         * コンストラクタ.
         * @param handler Handler
         * @param buttonId リピート中のButtonID
         */
        RepeatStateManagement(final Handler handler, final int buttonId) {
            mHandler = handler;
            mRepeatButtonId = buttonId;
        }

        /**
         * タイマータスクの状態を設定.
         *
         * @param status ステータス
         */
        private void setRepeatTaskStatus(final RepeatTaskStatus status) {
            mStatus = status;
        }

        /**
         * TimerTask実行予約処理.
         */
        private void executeTimerTask() {
            DTVTLogger.start();
            setTimerTask();
            mStatus = RepeatTaskStatus.REPEAT_STATUS_DURING_STARTUP;
            schedule(mTimerTask, DELAY_TIME, EXECUTION_INTERVAL);
            DTVTLogger.end();
        }

        /**
         * TimerTask処理の設定.
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
                    sendKeyCode(mRepeatButtonId, SEND_KEYCODE_PARAM_ACTION_DOWN, false, mContext);
                    DTVTLogger.debug("sendKeyCode");

                    DTVTLogger.end();
                }
            };
        }

        /**
         * ボタンのViewIdを設定.
         * @param viewId viewId
         */
        private void setRepeatButtonId(final int viewId) {
            mRepeatButtonId = viewId;
        }

        /**
         * リピート処理の終了.
         */
        public void repeatCancel() {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
        }
    }

    /**
     * UI画面が閉じられた際タイマーをキャンセルする.
     */
    public void cancelTimer() {
        if (mRepeatStateManagement != null) {
            mRepeatStateManagement.repeatCancel();
            mRepeatStateManagement = null;
        }
    }

    /**
     * RelayClientを取得する.
     * @return RemoteControlRelayClient
     */
    public RemoteControlRelayClient getRelayClient() {
        return mRemoteControlRelayClient;
    }
}