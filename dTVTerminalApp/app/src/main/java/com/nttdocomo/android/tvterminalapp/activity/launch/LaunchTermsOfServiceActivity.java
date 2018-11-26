/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.launch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * 初回起動時の利用規約アクティビティ.
 */
public class LaunchTermsOfServiceActivity extends BaseActivity {
    /**外部送信する情報TextView.*/
    private TextView mExternallyTransmitInfo;
    /**利用目的TextView.*/
    private TextView mPurposeOfUseText;
    /**第三者提供等の有無TextView.*/
    private TextView mThirdPartyOfferingText;
    /**STARTポジション.*/
    private final static int SPAN_START_POSITION = 28;
    /**ENDポジション.*/
    private final static int SPAN_END_POSITION = 31;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_application_terms_of_service);
        setContent();
        // dアカウント処理は不要
        setUnnecessaryDaccountRegistService();
    }

    /**
     * 画面上に表示するコンテンツを設定する.
     */
    private void setContent() {
        DTVTLogger.start();
        setTitleVisibility(false);
        Button agreeToStart =  findViewById(R.id.agree_to_terms_start_use_button);
        agreeToStart.setOnClickListener(this);
        //外部送信する情報
        LinearLayout externallyTransmit = findViewById(R.id.externally_transmit_layout);
        externallyTransmit.setOnClickListener(this);
        mExternallyTransmitInfo = findViewById(R.id.externally_transmit_text);

        //利用目的
        LinearLayout purposeOfUse = findViewById(R.id.purpose_of_use_layout);
        mPurposeOfUseText = findViewById(R.id.purpose_of_use_text);
        purposeOfUse.setOnClickListener(this);

        //第三者提供等の有無 　
        LinearLayout thirdPartyOfferings = findViewById(R.id.third_party_offerings_layout);
        thirdPartyOfferings.setOnClickListener(this);
        mThirdPartyOfferingText = findViewById(R.id.third_party_offerings_text);

        //詳細を確認する
        TextView detailLink = findViewById(R.id.application_privacy_all_text);
        SpannableString spannableString = new SpannableString(getString(R.string.str_launch_application_privacy_all_text));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(final View widget) {
                Uri detailUri = Uri.parse(UrlConstants.WebUrl.SETTING_MENU_APP_URL);
                Intent detailIntent = new Intent(Intent.ACTION_VIEW, detailUri);
                startActivity(detailIntent);
            }

            @Override
            public void updateDrawState(final TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        spannableString.setSpan(clickableSpan, SPAN_START_POSITION, SPAN_END_POSITION, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        detailLink.setText(spannableString);
        detailLink.setMovementMethod(LinkMovementMethod.getInstance());

        //利用規約を確認する
        TextView confirmLink = findViewById(R.id.confirm_application_terms_of_service_link);
        SpannableString confirmLinkSpannable = new SpannableString(getString(R.string.str_launch_confirm_application_terms_of_service_link));
        ClickableSpan confirmClickableSpan  = new ClickableSpan() {
            @Override
            public void onClick(final View widget) {
                Uri confirmUri = Uri.parse(UrlConstants.WebUrl.SETTING_MENU_AGREEMENT_HTML);
                Intent confirmIntent = new Intent(Intent.ACTION_VIEW, confirmUri);
                startActivity(confirmIntent);
            }
            @Override
            public void updateDrawState(final TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        confirmLinkSpannable.setSpan(confirmClickableSpan, 0, confirmLinkSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        confirmLink.setText(confirmLinkSpannable);
        confirmLink.setMovementMethod(LinkMovementMethod.getInstance());

        DTVTLogger.end();
    }

    @Override
    public void onClick(final View view) {
        DTVTLogger.start();
        switch (view.getId()) {
            case R.id.agree_to_terms_start_use_button:
                Intent intent = new Intent(this, StbSelectActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(StbSelectActivity.FROM_WHERE, StbSelectActivity.StbSelectFromMode.StbSelectFromMode_Launch.ordinal());
                SharedPreferencesUtils.setSharedPreferencesIsDisplayedTutorial(this, true);
                startActivity(intent);
                break;
            case R.id.externally_transmit_layout:
                ImageView externallyImageView = findViewById(R.id.externally_transmit_open_image);
                if (mExternallyTransmitInfo.getVisibility() == View.VISIBLE) {
                    mExternallyTransmitInfo.setVisibility(View.GONE);
                    externallyImageView.setImageResource(R.mipmap.ic_bar_open);
                } else {
                    mExternallyTransmitInfo.setVisibility(View.VISIBLE);
                    externallyImageView.setImageResource(R.mipmap.ic_bar_close);
                }
                break;
            case R.id.purpose_of_use_layout:
                ImageView purposeOfUseImageView = findViewById(R.id.purpose_of_use_open_image);
                if (mPurposeOfUseText.getVisibility() == View.VISIBLE) {
                    mPurposeOfUseText.setVisibility(View.GONE);
                    purposeOfUseImageView.setImageResource(R.mipmap.ic_bar_open);
                } else {
                    mPurposeOfUseText.setVisibility(View.VISIBLE);
                    purposeOfUseImageView.setImageResource(R.mipmap.ic_bar_close);
                }
                break;
            case R.id.third_party_offerings_layout:
                ImageView thirdPartyImageView = findViewById(R.id.third_party_offerings_image);
                if (mThirdPartyOfferingText.getVisibility() == View.VISIBLE) {
                    mThirdPartyOfferingText.setVisibility(View.GONE);
                    thirdPartyImageView.setImageResource(R.mipmap.ic_bar_open);
                } else {
                    mThirdPartyOfferingText.setVisibility(View.VISIBLE);
                    thirdPartyImageView.setImageResource(R.mipmap.ic_bar_close);
                }
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    @Override
    protected void restartMessageDialog(final String... message) {
        //この画面でdアカウント変わった旨のホーム画面遷移する促すダイアログを出さない
    }
}
