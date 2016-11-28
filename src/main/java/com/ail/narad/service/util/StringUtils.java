package com.ail.narad.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtils {

	public static List<String> getListOfStringsInsideBraces(String str) {
		String exp = "\\{(.*?)\\}";

		Pattern pattern = Pattern.compile(exp);
		Matcher matcher = pattern.matcher(str);

		List<String> matches = new ArrayList<String>();
		while (matcher.find()) {
			String group = matcher.group();
			matches.add(group.substring(1, group.length() - 1));
		}
		return matches;
	}

	public static List<String> getListOfStringsInsideBracesWithBraces(String str) {
		String exp = "\\{(.*?)\\}";

		Pattern pattern = Pattern.compile(exp);
		Matcher matcher = pattern.matcher(str);

		List<String> matches = new ArrayList<String>();
		while (matcher.find()) {
			String group = matcher.group();
			matches.add(group);
		}
		return matches;
	}

	public static List<String> getListFromJsonArray(JSONArray jsonArray) {
		List<String> listdata = new ArrayList<String>();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					listdata.add(jsonArray.getString(i));
				} catch (JSONException e) {
					e.printStackTrace();
					return new ArrayList<String>();
				}
			}
		}
		return listdata;
	}

	public static boolean IsvalidJsonData(String data) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(data);
			return true;
		} catch (IOException e) {
			return false;
		}

	}

}
