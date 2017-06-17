package com.datawings.app.common;

import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtilz {

	public static String replaceMoney(String text) {
		String tmp = StringUtils.replace(text, ",", "");
		String rs = StringUtils.replace(tmp, ".", "");
		return rs;
	}
	

	public static void replaceTagInText(StringBuffer text, String tagName, String value) {
		if ((text == null) || (tagName == null)) {
			return;
		}

		if (value == null) {
			value = " ";
		}

		if (value.length() == 0) {
			value = " ";
		}

		if ((text.length() > 0) && (tagName.length() > 0) && (value.length() > 0)) {
			while (text.indexOf(tagName) > 0) {
				text.replace(text.indexOf(tagName), text.indexOf(tagName) + tagName.length(), value);
			}
		}
	}
	
	public static String encodeString(String input, String algorithme)
			throws NoSuchAlgorithmException {
		byte[] hash = MessageDigest.getInstance("MD5").digest(input.getBytes());
		StringBuffer hashString = new StringBuffer();

		for (int i = 0; i < hash.length; ++i) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			}

			else {
				hashString.append(hex.substring(hex.length() - 2));
			}
		}

		return hashString.toString();

	}
	
	public static boolean isSignedNumeric(String value) {
		String valueToTest = null;
		if (value == null || value.equals(""))
			return false;
		if (value.charAt(0) == '-') {
			valueToTest = value.substring(1);
		} else {
			valueToTest = value;
		}
		return StringUtils.isNumeric(valueToTest);
	}

	public static boolean isNumbericString(String str) {
		// /Check input string is only numberic string
		String regex1 = "\\d+\\.\\d+";
		String regex2 = "\\d+";
		if ((str.matches(regex1) || str.matches(regex2))) {
			return true;
		}
		return false;
	}

	public static boolean isSignedDecimal(String value, int nbDecimal) {
		String pattern = "^-?[0-9]+";
		if (nbDecimal < 0) {
			throw new InvalidParameterException();
		} else if (nbDecimal == 0) {
			pattern += "$";
		} else {
			pattern += "\\.[0-9]{" + nbDecimal + "}?";
		}
		if (!Pattern.matches(pattern, value)) {
			return false;
		}
		return true;
	}

	public static String unAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
	}
	
	public static String referrer(String s) {
		String rs = StringUtils.substringAfter(s, "/secure");
		return "/secure" + rs;
	}
}
