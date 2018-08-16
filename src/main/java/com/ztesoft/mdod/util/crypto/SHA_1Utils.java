package com.ztesoft.mdod.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha_1加密工具类
 * 
 * @Description: TODO
 * @author zhao.jingang
 * @date 2016年6月2日 下午4:52:14
 * @version V1.0
 */
public class SHA_1Utils {

	/**
	 * 加密方法
	 * 
	 * @param src
	 * @return
	 */
	public static String sha1_hex(String str) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 测试
	public static void main(String[] args) {
		String s = "abcdefg123";
		System.out.println(sha1_hex(s)); // f61a56082c62717815e7024bd7694bf3ac7f49a1

	}

}
