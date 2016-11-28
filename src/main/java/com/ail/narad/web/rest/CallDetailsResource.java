package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.CallDetails;
import com.ail.narad.repository.CallDetailsRepository;
import com.ail.narad.repository.search.CallDetailsSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CallDetails.
 */
@RestController
@RequestMapping("/api")
public class CallDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CallDetailsResource.class);
        
    @Inject
    private CallDetailsRepository callDetailsRepository;
    
    @Inject
    private CallDetailsSearchRepository callDetailsSearchRepository;
    
    /**
     * POST  /callDetailss -> Create a new callDetails.
     */
    @RequestMapping(value = "/callDetailss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CallDetails> createCallDetails(@RequestBody CallDetails callDetails) throws URISyntaxException {
        log.debug("REST request to save CallDetails : {}", callDetails);
        if (callDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("callDetails", "idexists", "A new callDetails cannot already have an ID")).body(null);
        }
        CallDetails result = callDetailsRepository.save(callDetails);
        callDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/callDetailss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("callDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /callDetailss -> Updates an existing callDetails.
     */
    @RequestMapping(value = "/callDetailss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CallDetails> updateCallDetails(@RequestBody CallDetails callDetails) throws URISyntaxException {
        log.debug("REST request to update CallDetails : {}", callDetails);
        if (callDetails.getId() == null) {
            return createCallDetails(callDetails);
        }
        CallDetails result = callDetailsRepository.save(callDetails);
        callDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("callDetails", callDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /callDetailss -> get all the callDetailss.
     */
    @RequestMapping(value = "/callDetailss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CallDetails>> getAllCallDetailss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CallDetailss");
        Page<CallDetails> page = callDetailsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/callDetailss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /callDetailss/:id -> get the "id" callDetails.
     */
    @RequestMapping(value = "/callDetailss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CallDetails> getCallDetails(@PathVariable Long id) {
        log.debug("REST request to get CallDetails : {}", id);
        CallDetails callDetails = callDetailsRepository.findOne(id);
        return Optional.ofNullable(callDetails)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /callDetailss/:id -> delete the "id" callDetails.
     */
    @RequestMapping(value = "/callDetailss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCallDetails(@PathVariable Long id) {
        log.debug("REST request to delete CallDetails : {}", id);
        callDetailsRepository.delete(id);
        callDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("callDetails", id.toString())).build();
    }

    /**
     * SEARCH  /_search/callDetailss/:query -> search for the callDetails corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/callDetailss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CallDetails> searchCallDetailss(@PathVariable String query) {
        log.debug("REST request to search CallDetailss for query {}", query);
        return StreamSupport
            .stream(callDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
