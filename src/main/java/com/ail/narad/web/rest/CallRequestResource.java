package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.CallRequest;
import com.ail.narad.repository.CallRequestRepository;
import com.ail.narad.repository.search.CallRequestSearchRepository;
import com.ail.narad.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing CallRequest.
 */
@RestController
@RequestMapping("/api")
public class CallRequestResource {

    private final Logger log = LoggerFactory.getLogger(CallRequestResource.class);
        
    @Inject
    private CallRequestRepository callRequestRepository;
    
    @Inject
    private CallRequestSearchRepository callRequestSearchRepository;
    
    /**
     * POST  /callRequests -> Create a new callRequest.
     */
    @RequestMapping(value = "/callRequests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CallRequest> createCallRequest(@RequestBody CallRequest callRequest) throws URISyntaxException {
        log.debug("REST request to save CallRequest : {}", callRequest);
        if (callRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("callRequest", "idexists", "A new callRequest cannot already have an ID")).body(null);
        }
        CallRequest result = callRequestRepository.save(callRequest);
        callRequestSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/callRequests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("callRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /callRequests -> Updates an existing callRequest.
     */
    @RequestMapping(value = "/callRequests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CallRequest> updateCallRequest(@RequestBody CallRequest callRequest) throws URISyntaxException {
        log.debug("REST request to update CallRequest : {}", callRequest);
        if (callRequest.getId() == null) {
            return createCallRequest(callRequest);
        }
        CallRequest result = callRequestRepository.save(callRequest);
        callRequestSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("callRequest", callRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /callRequests -> get all the callRequests.
     */
    @RequestMapping(value = "/callRequests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CallRequest> getAllCallRequests() {
        log.debug("REST request to get all CallRequests");
        return callRequestRepository.findAll();
            }

    /**
     * GET  /callRequests/:id -> get the "id" callRequest.
     */
    @RequestMapping(value = "/callRequests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CallRequest> getCallRequest(@PathVariable Long id) {
        log.debug("REST request to get CallRequest : {}", id);
        CallRequest callRequest = callRequestRepository.findOne(id);
        return Optional.ofNullable(callRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /callRequests/:id -> delete the "id" callRequest.
     */
    @RequestMapping(value = "/callRequests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCallRequest(@PathVariable Long id) {
        log.debug("REST request to delete CallRequest : {}", id);
        callRequestRepository.delete(id);
        callRequestSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("callRequest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/callRequests/:query -> search for the callRequest corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/callRequests/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CallRequest> searchCallRequests(@PathVariable String query) {
        log.debug("REST request to search CallRequests for query {}", query);
        return StreamSupport
            .stream(callRequestSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
