package com.ail.narad.repository.search;

import com.ail.narad.domain.Audit_logs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Audit_logs entity.
 */
public interface Audit_logsSearchRepository extends ElasticsearchRepository<Audit_logs, Long> {
}
