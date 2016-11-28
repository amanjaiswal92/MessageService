package com.ail.narad.service.util;

import org.apache.commons.lang3.StringUtils;

public class TemplateUtils {

	public static Boolean isValidVariableName(String variableName) {
		if(variableName == null || variableName.isEmpty()) {
			return false;
		}
		if(variableName.contains(" ")) {
			return false;
		}
		if(variableName.contains("_")) {
			variableName = variableName.replace("_", "");
		}
		if(StringUtils.isAlphanumeric(variableName)) {
			return true;
		}
		return false;
	}
}
