package com.pretest.payment.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public final class AES256Util {
	private String iv;
	private Key keySpec;

	final static String key = "1234567890123456";

	public AES256Util()  {
		this.iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		
		
		try {
			byte[] b = key.getBytes("UTF-8");
			int len = b.length;
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			System.arraycopy(b, 0, keyBytes, 0, len);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		this.keySpec = keySpec;
	}

	public String encrypt(String str) {
		Cipher c;
		byte[] encrypted;
		String encrStr = "";
		try {
			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			encrypted = c.doFinal(str.getBytes("UTF-8"));
			encrStr = new String(Base64.encodeBase64(encrypted));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return encrStr;
	}

	public String decrypt(String str) {
		Cipher c;
		byte[] byteStr;
		String decrStr = "";
		try {
			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byteStr = Base64.decodeBase64(str.getBytes());
			decrStr = new String(c.doFinal(byteStr), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decrStr;
	}
}