/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.digion.dixim.android.activation.helper;

import android.content.Context;

import com.digion.dixim.android.definitions.DeveloperOptionDefinitions;
import com.digion.dixim.android.util.DeveloperOption;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.digion.dixim.android.util.EnvironmentUtil.ACTIVATE_DATA_HOME;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.activation.ActivationEnvironment;

/**
 *アクティベーションヘルパークラス.
 */
public class ActivationHelper {
	/**OK.*/
	public static final int ACTC_OK = 0x00000000;

//  client TODO :現在未使用ですが、将来つかうかもしれません
//	public static final int  ACTC_ERROR_INVALID_PARAMETER = 0x00011001;
//	public static final int  ACTC_ERROR_MEMORY_ALLOCATION = 0x00011002;
//	public static final int  ACTC_ERROR_HWID_GET = 0x00011003;
//	public static final int  ACTC_ERROR_HTTP_CONNECT = 0x00011004;
//	public static final int  ACTC_ERROR_FILE_IO = 0x00011005;
//	public static final int  ACTC_ERROR_INVALID_SERVER_RES = 0x00011006;
//	public static final int  ACTC_ERROR_GET_PROXY = 0x00011007;
//	public static final int  ACTC_ERROR_CANT_GET_ACTIVE_USERS_PROXY_SETTING = 0x00011008;
//	public static final int  ACTC_ERROR = 0x00011FFF;
//	public static final int  ACTC_ERROR_EXCEEDED_ACTIVATION_TIMES = 0x00022002;
	/**コンテキスト.*/
	private Context context;

	static {
		System.loadLibrary(ActivationEnvironment.JniLibrary.name);
		setAndroidLogHandler();
	}

	// DiXiM SDK内の soファイル内に実装されているため警告が出るが、supressする

	/**
	 * アクティベーション.
	 * @param private_data_home private_data_home
	 * @return  アクティベーション回数
	 */
	@SuppressWarnings("JniMissingFunction")
	public native final int activation(String private_data_home);

	/**
	 * activationState.
	 * @param private_data_home private_data_home
	 * @return true or false
	 */
	@SuppressWarnings("JniMissingFunction")
	public native final boolean activationState(String private_data_home);

	/**
	 * setAndroidLogHandler.
	 * @return true or false
	 */
	@SuppressWarnings("JniMissingFunction")
	public native static boolean setAndroidLogHandler();

	/**
	 * setLogLevel.
	 * @param level level
	 */
	@SuppressWarnings("JniMissingFunction")
	public native static final void setLogLevel(int level);

	/**
	 * activationInit.
	 * @return true or false
	 */
	@SuppressWarnings("JniMissingFunction")
	private native final boolean activationInit();

	/**
	 * activationTerm.
	 */
	@SuppressWarnings("JniMissingFunction")
	private native final void activationTerm();

	/**
	 * コンストラクタ.
	 * @param context context
	 * @param deviceKeyPath deviceKeyPath
	 */
	public ActivationHelper(final Context context, final String deviceKeyPath) {
		this.context = context;
		if (DeveloperOption.permission(DeveloperOptionDefinitions.Option.NATIVE)) {
			setLogLevel(DeveloperOptionDefinitions.DU_LOG_LEVEL_DEBUG);
		}
		if (!ActivationEnvironment.prepareActivation(context, deviceKeyPath)) {
			throw new RuntimeException("prepareActivation failed");
		}
		if (!activationInit()) {
			throw new RuntimeException("activationInit failed");
		}
	}

	/**
	 *term.
	 */
	public void term() {
		activationTerm();
	}

	/**
	 * getMacAddress.
	 * @return MacAddress
	 */
	public String getMacAddress() {
		String id = EnvironmentUtil.getCalculatedUniqueId(context,
				ACTIVATE_DATA_HOME.DMP);
		DTVTLogger.debug("getCalculatedUniqueId = " + id);
		return id;
	}
}
