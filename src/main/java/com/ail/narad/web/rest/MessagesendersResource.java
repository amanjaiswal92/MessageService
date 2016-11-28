package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.Messagesenders;
import com.ail.narad.repository.MessagesendersRepository;
import com.ail.narad.repository.search.MessagesendersSearchRepository;
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
 * REST controller for managing Messagesenders.
 */
@RestController
@RequestMapping("/api")
public class MessagesendersResource {

    private final Logger log = LoggerFactory.getLogger(MessagesendersResource.class);

    @Inject
    private MessagesendersRepository messagesendersRepository;

    @Inject
    private MessagesendersSearchRepository messagesendersSearchRepository;

    /**
     * POST  /messagesenderss -> Create a new messagesenders.
     */
    @RequestMapping(value = "/messagesenderss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messagesenders> createMessagesenders(@Valid @RequestBody Messagesenders messagesenders) throws URISyntaxException {
        log.debug("REST request to save Messagesenders : {}", messagesenders);
        if (messagesenders.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new messagesenders cannot already have an ID").body(null);
        }
        Messagesenders result = messagesendersRepository.save(messagesenders);
        messagesendersSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/messagesenderss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messagesenders", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messagesenderss -> Updates an existing messagesenders.
     */
    @RequestMapping(value = "/messagesenderss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messagesenders> updateMessagesenders(@Valid @RequestBody Messagesenders messagesenders) throws URISyntaxException {
        log.debug("REST request to update Messagesenders : {}", messagesenders);
        if (messagesenders.getId() == null) {
            return createMessagesenders(messagesenders);
        }
        Messagesenders result = messagesendersRepository.save(messagesenders);
        messagesendersSearchRepository.save(messagesenders);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("messagesenders", messagesenders.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messagesenderss -> get all the messagesenderss.
     */
    @RequestMapping(value = "/messagesenderss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Messagesenders>> getAllMessagesenderss(Pageable pageable)
        throws URISyntaxException {
        Page<Messagesenders> page = messagesendersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/messagesenderss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /messagesenderss/:id -> get the "id" messagesenders.
     */
    @RequestMapping(value = "/messagesenderss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messagesenders> getMessagesenders(@PathVariable Long id) {
        log.debug("REST request to get Messagesenders : {}", id);
        return Optional.ofNullable(messagesendersRepository.findOne(id))
            .map(messagesenders -> new ResponseEntity<>(
                messagesenders,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messagesenderss/:id -> delete the "id" messagesenders.
     */
    @RequestMapping(value = "/messagesenderss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMessagesenders(@PathVariable Long id) {
        log.debug("REST request to delete Messagesenders : {}", id);
        messagesendersRepository.delete(id);
        messagesendersSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("messagesenders", id.toString())).build();
    }

    /**
     * SEARCH  /_search/messagesenderss/:query -> search for the messagesenders corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/messagesenderss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Messagesenders> searchMessagesenderss(@PathVariable String query) {
        return StreamSupport
            .stream(messagesendersSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
