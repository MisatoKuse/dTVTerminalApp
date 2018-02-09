/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.digion.dixim.android.activation.helper;

import android.content.Context;
import android.util.Log;

import com.nttdocomo.android.tvterminalapp.BuildConfig;
import com.digion.dixim.android.definitions.DeveloperOptionDefinitions;
import com.digion.dixim.android.util.DeveloperOption;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.digion.dixim.android.util.EnvironmentUtil.ACTIVATE_DATA_HOME;
import com.nttdocomo.android.tvterminalapp.jni.activation.ActivationEnvironment;

public class ActivationHelper {
	public static final int ACTC_OK = 0x00000000;

	// client
	public static final int  ACTC_ERROR_INVALID_PARAMETER = 0x00011001;
	public static final int  ACTC_ERROR_MEMORY_ALLOCATION = 0x00011002;
	public static final int  ACTC_ERROR_HWID_GET = 0x00011003;
	public static final int  ACTC_ERROR_HTTP_CONNECT = 0x00011004;
	public static final int  ACTC_ERROR_FILE_IO = 0x00011005;
	public static final int  ACTC_ERROR_INVALID_SERVER_RES = 0x00011006;
	public static final int  ACTC_ERROR_GET_PROXY = 0x00011007;
	public static final int  ACTC_ERROR_CANT_GET_ACTIVE_USERS_PROXY_SETTING = 0x00011008;
	public static final int  ACTC_ERROR = 0x00011FFF;
	public static final int  ACTC_ERROR_EXCEEDED_ACTIVATION_TIMES = 0x00022002;

	private Context context;
	private static boolean initialized = false;

	static {
		System.loadLibrary(ActivationEnvironment.JniLibrary.name);
		setAndroidLogHandler();
	}

	public native final int activation(String private_data_home);
	public native final boolean activationState(String private_data_home);
	public native static boolean setAndroidLogHandler();
	public native static final void setLogLevel(int level);
	private native final boolean activationInit();
	private native final void activationTerm();

	public ActivationHelper(Context context, String deviceKeyPath) {
		this.context = context;
		if (DeveloperOption.permission(DeveloperOptionDefinitions.Option.NATIVE)){
			setLogLevel(DeveloperOptionDefinitions.DU_LOG_LEVEL_DEBUG);
		}
		if (!ActivationEnvironment.prepareActivation(context, deviceKeyPath)) {
			throw new RuntimeException("prepareActivation failed");
		}
		if (!activationInit()) {
			throw new RuntimeException("activationInit failed");
		}
	}

	public void term() {
		activationTerm();
	}

	public String getMacAddress() {
		String id = EnvironmentUtil.getCalculatedUniqueId(context,
				ACTIVATE_DATA_HOME.DMP);
		if (BuildConfig.DEBUG)
			Log.i("ActivationHelper","getCalculatedUniqueId = "+id);
		return id;
	}
}