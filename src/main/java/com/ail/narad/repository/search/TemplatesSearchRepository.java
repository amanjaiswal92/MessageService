package com.ail.narad.repository.search;

import com.ail.narad.domain.Templates;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Templates entity.
 */
public interface TemplatesSearchRepository extends ElasticsearchRepository<Templates, Long> {
}
