package com.javaweb.utils;

public final class StringUtil {

	public static boolean checkData(String data) {
		if(data != null && !data.equals("")) {
			return true;
		}
		else return false;
	}
}