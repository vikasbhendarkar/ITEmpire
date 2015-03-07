package com.jobPortal.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Validation {

	// validating email id
	public static boolean isValidEmail(String email, boolean isStudent) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			if (isStudent) {
				if (email.contains("raisoni.net") || email.contains("gmail.com")) {
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	// validating password with retype password
	public static boolean isValidPassword(String pass, String rPass) {
		if (!pass.isEmpty() && !rPass.isEmpty() && pass.equals(rPass)) {
			return true;
		}
		return false;
	}

	// validating student percentage
	public static boolean isValidStudentPercentage(String pass) {
		int per = Integer.parseInt(pass);
		if (per >= 0 && per <= 100) {
			return true;
		}
		return false;
	}

	// validating mobile number
	public static boolean isValidContactNumber(String number) {
		Pattern pattern = Pattern.compile("[0-9]{10}");
		Matcher matcher = pattern.matcher(number);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	// validating student activation key
	public static boolean isValidActivationKey(String email, String key) {
		try {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(email, "",
					"isKeyAvailable");
			if (json.getString("success") != null) {
				String res = json.getString("success");
				if (Integer.parseInt(res) == 1) {
					JSONObject json_user = json.getJSONObject("user");
					if (json_user.getString("autoGenKey").equals(key)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
