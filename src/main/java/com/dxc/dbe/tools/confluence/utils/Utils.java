package com.dxc.dbe.tools.confluence.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class Utils {
	public static String authorization;
	public static boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static boolean isExceptionUrl(String url, String link_text) {
		boolean result = false;
		if (url.startsWith("/wiki/display/~") || url.startsWith("/wiki/label/")
				|| url.startsWith("/wiki/download/attachments/") || Utils.isValidEmailAddress(url)) {
			result = true;
		} else if (link_text.equals("Expand all") || link_text.equals("Collapse all")) {
			result = true;
		}
		return result;
	}

	public static void usage() {
		System.out.println("Usage: java -jar dbe-confluence-tool.jar <services> [args]");
		System.out.println("where services include:");
		System.out.println("\thelp          -List the usage.");
		System.out.println("\tchecklink     -Validate all href links in confluence and generate a CSV report.");
		System.out.println("\t  -args:");
		System.out.println("\t  username\t\t-Your Confluence username");
		System.out.println("\t  password\t\t-Your Confluence password");
		System.out.println("\tchecklabel    -Find out all the pages without labels and generate a CSV report.");
		System.out.println("\t  -args:");
		System.out.println("\t  username\t\t-Your Confluence username");
		System.out.println("\t  password\t\t-Your Confluence password");
		System.out.println("See https://atcswa-cr-atlassian.ecs-core.ssn.hp.com/confluence/display/EASE/DBE+Confluence+Tool for more details.");
	}

	public static void getCredential(String username, String password) {
		String src = username + ":" + password;
		Utils.authorization = "Basic "+Base64.getEncoder().encodeToString(src.getBytes());
	}

}
