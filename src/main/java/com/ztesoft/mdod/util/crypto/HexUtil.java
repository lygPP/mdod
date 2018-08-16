package com.ztesoft.mdod.util.crypto;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class HexUtil {

	private static final char[] hexChar = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/***
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b) {
		return String.valueOf(hexChar[(b >> 4) & 0xf]).concat(
				String.valueOf(hexChar[(b) & 0xf]));
	}

	/**
	 * @param data
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String convertStringToHexString(String data, String encode)
			throws UnsupportedEncodingException {
		if (encode == null) {
			return conventBytesToHexString(data.getBytes());
		} else {
			return conventBytesToHexString(data.getBytes(encode));
		}
	}

	/**
	 * @param data
	 * @return
	 */
	public static String conventBytesToHexString(byte[] data) {
		return convertBytesToHexString(data, 0, data.length);
	}

	/**
	 * @param data
	 * @param offset
	 * @param length
	 * @return
	 */
	private static String convertBytesToHexString(byte[] data, int offset,
			int length) {
		StringBuffer sBuf = new StringBuffer();
		for (int i = offset; i < length; i++) {
			sBuf.append(hexChar[(data[i] >> 4) & 0xf]);
			sBuf.append(hexChar[data[i] & 0xf]);
		}
		return sBuf.toString();
	}

	/**
	 * @param hexString
	 * @return
	 */
	public static byte[] convertHexStringToBytes(String hexString) {
		return convertHexStringToBytes(hexString, 0, hexString.length());
	}

	/**
	 * @param hexString
	 * @param offset
	 * @param endIndex
	 * @return
	 */
	private static byte[] convertHexStringToBytes(String hexString, int offset,
			int endIndex) {
		byte[] data;
		String realHexString = hexString.substring(offset, endIndex)
				.toLowerCase();
		if ((realHexString.length() % 2) == 0)
			data = new byte[realHexString.length() / 2];
		else
			data = new byte[(int) Math.ceil(realHexString.length() / 2d)];

		int j = 0;
		char[] tmp;
		for (int i = 0; i < realHexString.length(); i += 2) {
			try {
				tmp = realHexString.substring(i, i + 2).toCharArray();
			} catch (StringIndexOutOfBoundsException siob) {
				tmp = (realHexString.substring(i) + "0").toCharArray();
			}
			data[j] = (byte) ((Arrays.binarySearch(hexChar, tmp[0]) & 0xf) << 4);
			data[j++] |= (byte) (Arrays.binarySearch(hexChar, tmp[1]) & 0xf);
		}

		for (int i = realHexString.length(); i > 0; i -= 2) {

		}
		return data;
	}
}
