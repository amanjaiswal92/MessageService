package com.ail.narad.repository.search;

import com.ail.narad.domain.Messagesenders;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Messagesenders entity.
 */
public interface MessagesendersSearchRepository extends ElasticsearchRepository<Messagesenders, Long> {
}
