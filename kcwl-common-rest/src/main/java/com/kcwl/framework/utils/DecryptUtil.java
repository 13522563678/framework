package com.kcwl.framework.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;


/**
 * @author ckwl
 */
public class DecryptUtil {

	/**
	 * 初次秘钥
	 */
	public static final String oncekey="123456kcwl654321";
	/**
	 * 秘钥
	 */
	public static final String key="123456kcwl654321";
	/**
	 * 参数解密
	 * @return
	 */
	public static Map<String,Object> decryptParam(String param, String pwd){
		Map<String,Object> result = null;
		try {
			String newData = decryptStr(param, pwd);
			result = JSONUtil.toBean(newData, new TypeReference<Map<String, Object> >(){}, true);
//			result = JsonUtil.toMap(newData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 参数解密
	 * @return
	 */
	public static String encryptParam(Map<String,Object> param, String pwd){
		String result = null;
		try {
			String newData = JsonUtil.toJson(param);
			result = encryptAES(newData, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解密字符串
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decryptStr(String str, String pwd) throws Exception{
		return decryptAES(str, pwd);
	}

	static final public byte[] KEY_VI = { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8 };

	public static final String bm = "UTF-8";

	/**
	 * AES加密
	 * @param cleartext
	 * @param dataPassword
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES( String cleartext,String dataPassword) throws Exception {

		IvParameterSpec zeroIv = new IvParameterSpec(KEY_VI);
		SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(bm), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));
		return new String (parseByte2HexStr(encryptedData));
	}

	/**
	 * AES解密
	 * @param encrypted
	 * @param dataPassword
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES( String encrypted,String dataPassword) throws Exception {
		// 对密钥进行处理-S

		byte[] byteMi = parseHexStr2Byte(encrypted);
		IvParameterSpec zeroIv = new IvParameterSpec(KEY_VI);
		SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(bm), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte[] decryptedData = cipher.doFinal(byteMi);

		return new String(decryptedData,bm);
	}

	/**
	 * 将16进制转换为二进制
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 将二进制转换成16进制
	 *
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
}
