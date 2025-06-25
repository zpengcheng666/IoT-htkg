package com.hss.core.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @Description: 加密工具
 * @author: jeecg-boot
 */
public class Md5Util {

    private static final String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++){
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEXDIGITS[d1] + HEXDIGITS[d2];
	}

	public static String md5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	public static String calculateFileMD5(String filePath) {
		try {
			// 创建一个MessageDigest实例，指定算法为MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// 打开文件输入流
			InputStream fis = new FileInputStream(filePath);

			// 创建一个缓冲区用于读取文件
			byte[] buffer = new byte[1024];
			int numRead;

			// 逐块读取文件内容并更新摘要
			while ((numRead = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, numRead);
			}

			// 关闭输入流
			fis.close();

			// 转换摘要为16进制字符串
			return toHexString(digest.digest());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 将字节数组转换为16进制字符串
	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
