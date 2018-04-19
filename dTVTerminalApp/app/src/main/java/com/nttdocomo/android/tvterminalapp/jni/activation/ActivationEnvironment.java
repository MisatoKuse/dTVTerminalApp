/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.activation;

import android.content.Context;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.digion.dixim.android.util.FileSystemUtil;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * DTCP-IPアクティベーション処理クラス.
 */
public class ActivationEnvironment {
	/**
	 * seed情報クラス.
	 */
	private static final String[] SEED = {"cEksLk32", "OG3LaLiW", "okTyxETR", "riFc3DUX", "68YIyKRX",
			"6p3MG86q", "3j4do3t9", "OpwJ6LAq", "y5b2pIlE", "odt5PW6b"};
	/**ca_seed_index.*/
	private static final int CA_SEED_INDEX = 2;
	/**cert_seed_index.*/
	private static final int CERT_SEED_INDEX = 9;
	/**pvk_seed_index.*/
	private static final int PVK_SEED_INDEX = 8;

	/**
	 * ファイル情報.
	 */
	public static class JniLibrary {
		/**ライブラリ名.*/
		public static final String name = "dixim_activation_helper_jni";
		/**ca_pemキー.*/
		public static final  String CA_PEM = "ca.pem";
		/**cert_pemキー.*/
		public static final String CERT_PEM = "cert.pem";
		/**pvk_pemキー.*/
		public static final String PVK_PEM = "pvk.pem";
		/**ca_datキー.*/
		public static final String CA_DAT = "ca.dat";
		/**cert_datキー.*/
		public static final String CERT_DAT = "cert.dat";
		/**pvk_datキー.*/
		public static final String PVK_DAT = "pvk.dat";
	}

	/**
	 * デバイスキー生成.
	 * @param context コンテキスト
	 * @param deviceKeyPath デバイスキー保存パス
	 * @return 成否
	 */
	public static boolean prepareActivation(final Context context,
											final String deviceKeyPath) {
		if (!EnvironmentUtil.makeNoDir(deviceKeyPath)) {
			return false;
		}
		try {
			String datFileName = deviceKeyPath + File.separator
					+ JniLibrary.CA_DAT;
			String pemFileName = deviceKeyPath + File.separator
					+ JniLibrary.CA_PEM;
			writeOut(context, R.raw.ca, datFileName);
			convert(datFileName, pemFileName, SEED[CA_SEED_INDEX]);
			FileSystemUtil.deleteFile(new File(datFileName));

			datFileName = deviceKeyPath + File.separator
					+ JniLibrary.CERT_DAT;
			pemFileName = deviceKeyPath + File.separator + JniLibrary.CERT_PEM;
			writeOut(context, R.raw.cert, datFileName);
			convert(datFileName, pemFileName, SEED[CERT_SEED_INDEX]);
			FileSystemUtil.deleteFile(new File(datFileName));

			datFileName = deviceKeyPath + File.separator
					+ JniLibrary.PVK_DAT;
			pemFileName = deviceKeyPath + File.separator + JniLibrary.PVK_PEM;
			writeOut(context, R.raw.pvk, datFileName);
			convert(datFileName, pemFileName, SEED[PVK_SEED_INDEX]);
			FileSystemUtil.deleteFile(new File(datFileName));
		} catch (IOException e) {
			DTVTLogger.error("Can't prepare pems.");
			return false;
		}
		return true;
	}

	/**
	 * ファイル書き出し.
	 * @param context コンテキスト
	 * @param resId リソースID
	 * @param pathname ファイルパス
	 * @throws IOException IOException
	 */
	private static void writeOut(final Context context, final int resId, final String pathname)
			throws IOException {
		File pemFile = new File(pathname);
		if (pemFile.exists()) {
			return;
		}

		InputStream is = null;
		FileOutputStream out = null;
		byte[] buf = new byte[65536];
		int size;

		try {
			is = context.getResources().openRawResource(resId);
			out = new FileOutputStream(pathname);
			while ((size = is.read(buf)) != -1) {
				out.write(buf, 0, size);
			}
			out.flush();
			out.getFD().sync();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				DTVTLogger.debug("InputStream close Error", e);
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				DTVTLogger.debug("FileOutputStream close Error", e);
			}
			if (!pemFile.exists()) {
				DTVTLogger.error("can't writeOut : " + pathname);
				throw new IOException();
			}
		}
	}

	/**
	 * キー情報変換.
	 * @param srcPath 変換元ファイルパス
	 * @param destPath 変換先ファイルパス
	 * @param seed seed情報
	 * @throws IOException IOException
	 */
	private static void convert(final String srcPath, final String destPath, final String seed)
			throws IOException {
		File destFile = new File(destPath);
		if (destFile.exists()) {
			if (!destFile.delete()) {
				DTVTLogger.error("Can't remove:" + destPath);
				return;
			}
		}

		byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8.name());
		FileInputStream inFile = null;
		FileOutputStream outFile = null;
		try {
			inFile = new FileInputStream(srcPath);
			outFile = new FileOutputStream(destPath);
			byte[] b = new byte[seedBytes.length];
			int c;
			while ((c = inFile.read(b)) > 0) {
				for (int i = 0; i < seedBytes.length && i < c; i++) {
					outFile.write(b[i] ^ seedBytes[i]);
				}
			}
			outFile.flush();
			outFile.getFD().sync();
		} finally {
			try {
				if (inFile != null) {
					inFile.close();
				}
			} catch (IOException e) {
				DTVTLogger.debug("FileInputStream close Error", e);
			}
			try {
				if (outFile != null) {
					outFile.close();
				}
			} catch (IOException e) {
				DTVTLogger.debug("FileOutputStream close Error", e);
			}
			if (!destFile.exists()) {
				throw new IOException();
			}
		}
	}
}
