package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.Variables;
import com.ail.narad.repository.VariablesRepository;
import com.ail.narad.repository.search.VariablesSearchRepository;
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
 * REST controller for managing Variables.
 */
@RestController
@RequestMapping("/api")
public class VariablesResource {

    private final Logger log = LoggerFactory.getLogger(VariablesResource.class);

    @Inject
    private VariablesRepository variablesRepository;

    @Inject
    private VariablesSearchRepository variablesSearchRepository;

    /**
     * POST  /variabless -> Create a new variables.
     */
    @RequestMapping(value = "/variabless",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Variables> createVariables(@Valid @RequestBody Variables variables) throws URISyntaxException {
        log.debug("REST request to save Variables : {}", variables);
        if (variables.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new variables cannot already have an ID").body(null);
        }
        Variables result = variablesRepository.save(variables);
        variablesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/variabless/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("variables", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /variabless -> Updates an existing variables.
     */
    @RequestMapping(value = "/variabless",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Variables> updateVariables(@Valid @RequestBody Variables variables) throws URISyntaxException {
        log.debug("REST request to update Variables : {}", variables);
        if (variables.getId() == null) {
            return createVariables(variables);
        }
        Variables result = variablesRepository.save(variables);
        variablesSearchRepository.save(variables);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("variables", variables.getId().toString()))
            .body(result);
    }

    /**
     * GET  /variabless -> get all the variabless.
     */
    @RequestMapping(value = "/variabless",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Variables>> getAllVariabless(Pageable pageable)
        throws URISyntaxException {
        Page<Variables> page = variablesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/variabless");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /variabless/:id -> get the "id" variables.
     */
    @RequestMapping(value = "/variabless/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Variables> getVariables(@PathVariable Long id) {
        log.debug("REST request to get Variables : {}", id);
        return Optional.ofNullable(variablesRepository.findOne(id))
            .map(variables -> new ResponseEntity<>(
                variables,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /variabless/:id -> delete the "id" variables.
     */
    @RequestMapping(value = "/variabless/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVariables(@PathVariable Long id) {
        log.debug("REST request to delete Variables : {}", id);
        variablesRepository.delete(id);
        variablesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("variables", id.toString())).build();
    }

    /**
     * SEARCH  /_search/variabless/:query -> search for the variables corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/variabless/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Variables> searchVariabless(@PathVariable String query) {
        return StreamSupport
            .stream(variablesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
