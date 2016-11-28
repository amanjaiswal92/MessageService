package com.ail.narad.repository.search;

import com.ail.narad.domain.CallRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CallRequest entity.
 */
public interface CallRequestSearchRepository extends ElasticsearchRepository<CallRequest, Long> {
}
