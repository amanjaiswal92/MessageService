package com.ail.narad.models;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.repository.custom.CustomTemplatesRepository;
import com.ail.narad.repository.custom.CustomVariablesRepository;

@Service
public class TemplateModel {
	
	@Inject
	private CustomTemplatesRepository templatesRepository;
	
	@Inject
	private CustomVariablesRepository variablesRepository;
	
	public Templates getByTemplateId(String templateId, TemplateType templateType) {
		return templatesRepository.findByTemplateId(templateId, templateType);
	}
	
}
