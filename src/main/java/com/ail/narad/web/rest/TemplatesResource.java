package com.ail.narad.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ail.narad.domain.Templates;
import com.ail.narad.repository.TemplatesRepository;
import com.ail.narad.repository.search.TemplatesSearchRepository;
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
 * REST controller for managing Templates.
 */
@RestController
@RequestMapping("/api")
public class TemplatesResource {

    private final Logger log = LoggerFactory.getLogger(TemplatesResource.class);

    @Inject
    private TemplatesRepository templatesRepository;

    @Inject
    private TemplatesSearchRepository templatesSearchRepository;

    /**
     * POST  /templatess -> Create a new templates.
     */
    @RequestMapping(value = "/templatess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Templates> createTemplates(@Valid @RequestBody Templates templates) throws URISyntaxException {
        log.debug("REST request to save Templates : {}", templates);
        if (templates.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new templates cannot already have an ID").body(null);
        }
        Templates result = templatesRepository.save(templates);
        templatesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/templatess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("templates", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /templatess -> Updates an existing templates.
     */
    @RequestMapping(value = "/templatess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Templates> updateTemplates(@Valid @RequestBody Templates templates) throws URISyntaxException {
        log.debug("REST request to update Templates : {}", templates);
        if (templates.getId() == null) {
            return createTemplates(templates);
        }
        Templates result = templatesRepository.save(templates);
        templatesSearchRepository.save(templates);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("templates", templates.getId().toString()))
            .body(result);
    }

    /**
     * GET  /templatess -> get all the templatess.
     */
    @RequestMapping(value = "/templatess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Templates>> getAllTemplatess(Pageable pageable)
        throws URISyntaxException {
        Page<Templates> page = templatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/templatess");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /templatess/:id -> get the "id" templates.
     */
    @RequestMapping(value = "/templatess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Templates> getTemplates(@PathVariable Long id) {
        log.debug("REST request to get Templates : {}", id);
        return Optional.ofNullable(templatesRepository.findOne(id))
            .map(templates -> new ResponseEntity<>(
                templates,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /templatess/:id -> delete the "id" templates.
     */
    @RequestMapping(value = "/templatess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTemplates(@PathVariable Long id) {
        log.debug("REST request to delete Templates : {}", id);
        templatesRepository.delete(id);
        templatesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("templates", id.toString())).build();
    }

    /**
     * SEARCH  /_search/templatess/:query -> search for the templates corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/templatess/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Templates> searchTemplatess(@PathVariable String query) {
        return StreamSupport
            .stream(templatesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
