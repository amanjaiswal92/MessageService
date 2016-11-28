package com.ail.narad.repository.search;

import com.ail.narad.domain.CallDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CallDetails entity.
 */
public interface CallDetailsSearchRepository extends ElasticsearchRepository<CallDetails, Long> {
}
