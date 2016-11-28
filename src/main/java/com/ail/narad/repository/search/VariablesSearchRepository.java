package com.ail.narad.repository.search;

import com.ail.narad.domain.Variables;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Variables entity.
 */
public interface VariablesSearchRepository extends ElasticsearchRepository<Variables, Long> {
}
