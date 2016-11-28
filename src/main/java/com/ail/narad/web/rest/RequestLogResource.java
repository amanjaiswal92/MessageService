package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.RequestLog;
import com.ail.narad.repository.RequestLogRepository;
import com.ail.narad.repository.search.RequestLogSearchRepository;
import com.ail.narad.web.rest.util.HeaderUtil;
import com.ail.narad.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RequestLog.
 */
@RestController
@RequestMapping("/api")
public class RequestLogResource {

    private final Logger log = LoggerFactory.getLogger(RequestLogResource.class);

    @Inject
    private RequestLogRepository requestLogRepository;

    @Inject
    private RequestLogSearchRepository requestLogSearchRepository;

    /**
     * POST  /requestLogs -> Create a new requestLog.
     */
    @RequestMapping(value = "/requestLogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RequestLog> createRequestLog(@Valid @RequestBody RequestLog requestLog) throws URISyntaxException {
        log.debug("REST request to save RequestLog : {}", requestLog);
        if (requestLog.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new requestLog cannot already have an ID").body(null);
        }
        RequestLog result = requestLogRepository.save(requestLog);
        requestLogSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/requestLogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("requestLog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requestLogs -> Updates an existing requestLog.
     */
    @RequestMapping(value = "/requestLogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RequestLog> updateRequestLog(@Valid @RequestBody RequestLog requestLog) throws URISyntaxException {
        log.debug("REST request to update RequestLog : {}", requestLog);
        if (requestLog.getId() == null) {
            return createRequestLog(requestLog);
        }
        RequestLog result = requestLogRepository.save(requestLog);
        requestLogSearchRepository.save(requestLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("requestLog", requestLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requestLogs -> get all the requestLogs.
     */
    @RequestMapping(value = "/requestLogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RequestLog>> getAllRequestLogs(Pageable pageable)
        throws URISyntaxException {
        Page<RequestLog> page = requestLogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requestLogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requestLogs/:id -> get the "id" requestLog.
     */
    @RequestMapping(value = "/requestLogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RequestLog> getRequestLog(@PathVariable Long id) {
        log.debug("REST request to get RequestLog : {}", id);
        return Optional.ofNullable(requestLogRepository.findOne(id))
            .map(requestLog -> new ResponseEntity<>(
                requestLog,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /requestLogs/:id -> delete the "id" requestLog.
     */
    @RequestMapping(value = "/requestLogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRequestLog(@PathVariable Long id) {
        log.debug("REST request to delete RequestLog : {}", id);
        requestLogRepository.delete(id);
        requestLogSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("requestLog", id.toString())).build();
    }

    /**
     * SEARCH  /_search/requestLogs/:query -> search for the requestLog corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/requestLogs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RequestLog> searchRequestLogs(@PathVariable String query) {
        return StreamSupport
            .stream(requestLogSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
