package com.nttdocomo.android.tvterminalapp.activity.common;


import com.nttdocomo.android.tvterminalapp.common.UserState;

/**
 * 機能
 *      外部インターフェース
 */
public interface MenuDisplayEventListener {
     void onPreUserStateChange(UserState oldUserState, UserState newUserState);
     void onUserStateChanged(UserState oldUserState, UserState newUserState);
     void onMenuItemSelected(MenuItem menuItem);
}
