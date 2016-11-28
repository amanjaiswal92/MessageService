package com.ail.narad.repository.custom;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.repository.TemplatesRepository;

/**
 * Spring Data JPA repository for the Templates entity.
 */
public interface CustomTemplatesRepository extends TemplatesRepository {

	@Query("select t from Templates t where t.template_id = :templateId and t.type = :type")
	Templates findByTemplateId(@Param("templateId") String templateId, @Param("type") TemplateType type);
	
}
