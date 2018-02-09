/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.activation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.nttdocomo.android.tvterminalapp.R;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.digion.dixim.android.util.FileSystemUtil;

import android.content.Context;
import android.util.Log;

public class ActivationEnvironment {
	/*------- seed関連データ  --------*/
	private static final String[] SEED = {"cEksLk32", "OG3LaLiW", "okTyxETR", "riFc3DUX", "68YIyKRX", "6p3MG86q", "3j4do3t9", "OpwJ6LAq", "y5b2pIlE", "odt5PW6b"};
	private static final int CA_SEED_INDEX = 2;
	private static final int CERT_SEED_INDEX = 9;
	private static final int PVK_SEED_INDEX = 8;
	/*--------------------------*/

	public static class JniLibrary {
		public static String name = "dixim_activation_helper_jni";
		public static String CA_PEM = "ca.pem";
		public static String CERT_PEM = "cert.pem";
		public static String PVK_PEM = "pvk.pem";
		public static String CA_DAT = "ca.dat";
		public static String CERT_DAT = "cert.dat";
		public static String PVK_DAT = "pvk.dat";
	}

	public static boolean prepareActivation(Context context,
			String deviceKeyPath) {
		if (!EnvironmentUtil.makeNoDir(deviceKeyPath))
			return false;
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
			Log.e("ActivationEnvironment", "Can't prepare pems.");
			return false;
		}
		return true;
	}

	private static void writeOut(Context context, int resId, String pathname)
			throws IOException {
		File pemFile = new File(pathname);
		if (pemFile.exists())
			return;

		InputStream is = context.getResources().openRawResource(resId);
		FileOutputStream out = null;
		byte buf[] = new byte[65536];
		int size = 0;

		try {
			out = new FileOutputStream(pathname);
			while ((size = is.read(buf)) != -1) {
				out.write(buf, 0, size);
			}
			out.flush();
			out.getFD().sync();
		} finally {
			if (out != null)
				out.close();
			if (!pemFile.exists()) {
				Log.e("ActivationEnvironment", "can't writeOut : " + pathname);
				throw new IOException();
			}
		}
	}

	private static void convert(String srcPath, String destPath, String seed)
			throws IOException {
		File destFile = new File(destPath);
		if (destFile.exists()) {
			if (!destFile.delete()) {
				Log.e(ActivationEnvironment.class.getSimpleName(),
						"Can't remove:" + destPath);
				return;
			}
		}

		byte seedBytes[] = seed.getBytes();
		FileInputStream inFile = null;
		FileOutputStream outFile = null;
		try {
			inFile = new FileInputStream(srcPath);
			outFile = new FileOutputStream(destPath);
			byte b[] = new byte[seedBytes.length];
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
				if (inFile != null)
					inFile.close();
				if (outFile != null)
					outFile.close();
			} catch (IOException e) {
			}
			if (!destFile.exists()) {
				throw new IOException();
			}
		}
	}
}
