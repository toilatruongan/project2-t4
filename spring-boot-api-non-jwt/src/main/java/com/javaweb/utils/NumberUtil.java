package com.javaweb.utils;

public class NumberUtil {

	public static boolean isNumber(String value) {
		if(value == null) return false;
		try {
			Integer number = Integer.parseInt(value);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
}