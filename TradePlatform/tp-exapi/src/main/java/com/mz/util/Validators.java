package com.mz.util;

import java.util.regex.Pattern;

public class  Validators {
	public static boolean isEmail(String email){
		Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		if(!p.matcher(email).matches()){
			return false;
		}
		return true;
	}
	
	public static boolean isPassword(String Password){
		Pattern p = Pattern.compile("^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[a-zA-Z0-9]+$");
		if(!p.matcher(Password).matches()){
			return false;
		}
		return true;
		
	}
}
