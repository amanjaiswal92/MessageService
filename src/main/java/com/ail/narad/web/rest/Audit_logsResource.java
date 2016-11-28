package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.Audit_logs;
import com.ail.narad.repository.Audit_logsRepository;
import com.ail.narad.repository.search.Audit_logsSearchRepository;
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
 * REST controller for managing Audit_logs.
 */
@RestController
@RequestMapping("/api")
public class Audit_logsResource {

    private final Logger log = LoggerFactory.getLogger(Audit_logsResource.class);

    @Inject
    private Audit_logsRepository audit_logsRepository;

    @Inject
    private Audit_logsSearchRepository audit_logsSearchRepository;

    /**
     * POST  /audit_logss -> Create a new audit_logs.
     */
    @RequestMapping(value = "/audit_logss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Audit_logs> createAudit_logs(@Valid @RequestBody Audit_logs audit_logs) throws URISyntaxException {
        log.debug("REST request to save Audit_logs : {}", audit_logs);
        if (audit_logs.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new audit_logs cannot already have an ID").body(null);
        }
        Audit_logs result = audit_logsRepository.save(audit_logs);
        audit_logsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/audit_logss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("audit_logs", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audit_logss -> Updates an existing audit_logs.
     */
    @RequestMapping(value = "/audit_logss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Audit_logs> updateAudit_logs(@Valid @RequestBody Audit_logs audit_logs) throws URISyntaxException {
        log.debug("REST request to update Audit_logs : {}", audit_logs);
        if (audit_logs.getId() == null) {
            return createAudit_logs(audit_logs);
        }
        Audit_logs result = audit_logsRepository.save(audit_logs);
        audit_logsSearchRepository.save(audit_logs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("audit_logs", audit_logs.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audit_logss -> get all the audit_logss.
     */
    @RequestMapping(value = "/audit_logss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Audit_logs>> getAllAudit_logss(Pageable pageable)
        throws URISyntaxException {
        Page<Audit_logs> page = audit_logsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/audit_logss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /audit_logss/:id -> get the "id" audit_logs.
     */
    @RequestMapping(value = "/audit_logss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Audit_logs> getAudit_logs(@PathVariable Long id) {
        log.debug("REST request to get Audit_logs : {}", id);
        return Optional.ofNullable(audit_logsRepository.findOne(id))
            .map(audit_logs -> new ResponseEntity<>(
                audit_logs,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audit_logss/:id -> delete the "id" audit_logs.
     */
    @RequestMapping(value = "/audit_logss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAudit_logs(@PathVariable Long id) {
        log.debug("REST request to delete Audit_logs : {}", id);
        audit_logsRepository.delete(id);
        audit_logsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("audit_logs", id.toString())).build();
    }

    /**
     * SEARCH  /_search/audit_logss/:query -> search for the audit_logs corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/audit_logss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Audit_logs> searchAudit_logss(@PathVariable String query) {
        return StreamSupport
            .stream(audit_logsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
