package cn.chamatou.biz.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5������
 * 
 * @author ��½ ���к�����ְ�ռ���
 * @date 2013-10-8
 * @version 1.0
 */
public class MD5 {

	public static String md5(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] e = md.digest(value.getBytes());
			return toHex(e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return value;
		}
	}

	public static String md5(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] e = md.digest(bytes);
			return toHex(e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String toHex(byte bytes[]) {
		StringBuilder hs = new StringBuilder();
		String stmp = "";
		for (int n = 0; n < bytes.length; n++) {
			stmp = Integer.toHexString(bytes[n] & 0xff);
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString();
	}
}