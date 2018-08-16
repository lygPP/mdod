package com.ztesoft.mdod.util.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密解密工具类
 * 
 * @author Administrator
 *
 */
public class DesEncryptUtil {

	private static final String publicKey = "60*Qj#^l"; // 公钥

	/**
	 * 利用字符串生成新的密钥
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static SecretKey getKey(String key) throws Exception {
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey sk = keyFactory.generateSecret(desKeySpec);
		return sk;
	}

	/**
	 * 字符串加密
	 * 
	 * @param key
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static String encryptString(Key key, String plainText) throws Exception {
		byte[] byteMing = plainText.getBytes("UTF-8");
		byte[] byteMi = doFinalEncrypt(key, byteMing, "DES");
		String strMi = Base64Util.encode(byteMi);
		return strMi;
	}

	/**
	 * 字符串解密
	 * 
	 * @param key
	 * @param cipherText
	 * @return
	 * @throws Exception
	 */
	public static String decryptString(Key key, String cipherText) throws Exception {
		byte[] byteMi = Base64Util.decodeByte(cipherText);
		byte[] byteMing = doFinalDecrypt(key, byteMi, "DES");
		String strMing = new String(byteMing, "UTF-8");
		return strMing;
	}

	/**
	 * 公共的加密方法,支持DES|RSA|ECB|PKCS1Padding
	 * 
	 * @param key
	 * @param data
	 * @param type
	 * @return
	 * @throws Exception
	 */
	protected static byte[] doFinalEncrypt(Key key, byte[] data, String type) throws Exception {
		Cipher cipher = Cipher.getInstance(type);// type="DES|RSA|ECB|PKCS1Padding";
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * 公共的解密方法,支持DES|RSA|ECB|PKCS1Padding
	 * 
	 * @param key
	 * @param data
	 * @param type
	 * @return
	 * @throws Exception
	 */
	protected static byte[] doFinalDecrypt(Key key, byte[] data, String type) throws Exception {
		Cipher cipher = Cipher.getInstance(type);// type="DES|RSA|ECB|PKCS1Padding";
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static void main(String[] args) {
		Key key;
		try {
			key = getKey(publicKey);
			String username = "111aaa";
			String encryptedUsername = encryptString(key, username);
			System.out.println("加密后用户名：" + encryptedUsername);
			String decryptedUsername = decryptString(key, encryptedUsername);
			System.out.println("解密后用户名：" + decryptedUsername);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
