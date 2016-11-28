package com.ail.narad.repository.search;

import com.ail.narad.domain.RequestLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RequestLog entity.
 */
public interface RequestLogSearchRepository extends ElasticsearchRepository<RequestLog, Long> {
}
