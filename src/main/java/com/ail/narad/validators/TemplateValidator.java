package com.ail.narad.validators;

import java.util.List;

import com.ail.narad.domain.Templates;
import com.ail.narad.service.util.StringUtils;
import com.ail.narad.service.util.TemplateUtils;

public class TemplateValidator {

	public static Boolean isValidTemplate(Templates template) {
		if(template == null)  {
			return false;
		}
		String message = template.getMessage();
		List<String> variables = StringUtils.getListOfStringsInsideBraces(message);
		for(int i = 0; i < variables.size(); i++) {
			if(!TemplateUtils.isValidVariableName(variables.get(i))) {
				return false;
			}
		}
		return true;
		
	}
	
}
