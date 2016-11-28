package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.Audit_logs;
import com.ail.narad.repository.Audit_logsRepository;
import com.ail.narad.repository.search.Audit_logsSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ail.narad.domain.enumeration.AuditEvent;

/**
 * Test class for the Audit_logsResource REST controller.
 *
 * @see Audit_logsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Audit_logsResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_REQUEST_ID = "AAAAA";
    private static final String UPDATED_REQUEST_ID = "BBBBB";


private static final AuditEvent DEFAULT_EVENT = AuditEvent.NEW;
    private static final AuditEvent UPDATED_EVENT = AuditEvent.ADDED_TO_QUEUE;
    private static final String DEFAULT_MESSAGE = "AAAAA";
    private static final String UPDATED_MESSAGE = "BBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_TIME_STR = dateTimeFormatter.format(DEFAULT_UPDATED_TIME);

    @Inject
    private Audit_logsRepository audit_logsRepository;

    @Inject
    private Audit_logsSearchRepository audit_logsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAudit_logsMockMvc;

    private Audit_logs audit_logs;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Audit_logsResource audit_logsResource = new Audit_logsResource();
        ReflectionTestUtils.setField(audit_logsResource, "audit_logsRepository", audit_logsRepository);
        ReflectionTestUtils.setField(audit_logsResource, "audit_logsSearchRepository", audit_logsSearchRepository);
        this.restAudit_logsMockMvc = MockMvcBuilders.standaloneSetup(audit_logsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        audit_logs = new Audit_logs();
        audit_logs.setRequestId(DEFAULT_REQUEST_ID);
        audit_logs.setEvent(DEFAULT_EVENT);
        audit_logs.setMessage(DEFAULT_MESSAGE);
        audit_logs.setUpdated_time(DEFAULT_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void createAudit_logs() throws Exception {
        int databaseSizeBeforeCreate = audit_logsRepository.findAll().size();

        // Create the Audit_logs

        restAudit_logsMockMvc.perform(post("/api/audit_logss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audit_logs)))
                .andExpect(status().isCreated());

        // Validate the Audit_logs in the database
        List<Audit_logs> audit_logss = audit_logsRepository.findAll();
        assertThat(audit_logss).hasSize(databaseSizeBeforeCreate + 1);
        Audit_logs testAudit_logs = audit_logss.get(audit_logss.size() - 1);
        assertThat(testAudit_logs.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testAudit_logs.getEvent()).isEqualTo(DEFAULT_EVENT);
        assertThat(testAudit_logs.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testAudit_logs.getUpdated_time()).isEqualTo(DEFAULT_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = audit_logsRepository.findAll().size();
        // set the field null
        audit_logs.setRequestId(null);

        // Create the Audit_logs, which fails.

        restAudit_logsMockMvc.perform(post("/api/audit_logss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audit_logs)))
                .andExpect(status().isBadRequest());

        List<Audit_logs> audit_logss = audit_logsRepository.findAll();
        assertThat(audit_logss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventIsRequired() throws Exception {
        int databaseSizeBeforeTest = audit_logsRepository.findAll().size();
        // set the field null
        audit_logs.setEvent(null);

        // Create the Audit_logs, which fails.

        restAudit_logsMockMvc.perform(post("/api/audit_logss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audit_logs)))
                .andExpect(status().isBadRequest());

        List<Audit_logs> audit_logss = audit_logsRepository.findAll();
        assertThat(audit_logss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdated_timeIsRequired() throws Exception {
        int databaseSizeBeforeTest = audit_logsRepository.findAll().size();
        // set the field null
        audit_logs.setUpdated_time(null);

        // Create the Audit_logs, which fails.

        restAudit_logsMockMvc.perform(post("/api/audit_logss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audit_logs)))
                .andExpect(status().isBadRequest());

        List<Audit_logs> audit_logss = audit_logsRepository.findAll();
        assertThat(audit_logss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAudit_logss() throws Exception {
        // Initialize the database
        audit_logsRepository.saveAndFlush(audit_logs);

        // Get all the audit_logss
        restAudit_logsMockMvc.perform(get("/api/audit_logss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(audit_logs.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
                .andExpect(jsonPath("$.[*].event").value(hasItem(DEFAULT_EVENT.toString())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].updated_time").value(hasItem(DEFAULT_UPDATED_TIME_STR)));
    }

    @Test
    @Transactional
    public void getAudit_logs() throws Exception {
        // Initialize the database
        audit_logsRepository.saveAndFlush(audit_logs);

        // Get the audit_logs
        restAudit_logsMockMvc.perform(get("/api/audit_logss/{id}", audit_logs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(audit_logs.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.event").value(DEFAULT_EVENT.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.updated_time").value(DEFAULT_UPDATED_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAudit_logs() throws Exception {
        // Get the audit_logs
        restAudit_logsMockMvc.perform(get("/api/audit_logss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudit_logs() throws Exception {
        // Initialize the database
        audit_logsRepository.saveAndFlush(audit_logs);

		int databaseSizeBeforeUpdate = audit_logsRepository.findAll().size();

        // Update the audit_logs
        audit_logs.setRequestId(UPDATED_REQUEST_ID);
        audit_logs.setEvent(UPDATED_EVENT);
        audit_logs.setMessage(UPDATED_MESSAGE);
        audit_logs.setUpdated_time(UPDATED_UPDATED_TIME);

        restAudit_logsMockMvc.perform(put("/api/audit_logss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audit_logs)))
                .andExpect(status().isOk());

        // Validate the Audit_logs in the database
        List<Audit_logs> audit_logss = audit_logsRepository.findAll();
        assertThat(audit_logss).hasSize(databaseSizeBeforeUpdate);
        Audit_logs testAudit_logs = audit_logss.get(audit_logss.size() - 1);
        assertThat(testAudit_logs.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAudit_logs.getEvent()).isEqualTo(UPDATED_EVENT);
        assertThat(testAudit_logs.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testAudit_logs.getUpdated_time()).isEqualTo(UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteAudit_logs() throws Exception {
        // Initialize the database
        audit_logsRepository.saveAndFlush(audit_logs);

		int databaseSizeBeforeDelete = audit_logsRepository.findAll().size();

        // Get the audit_logs
        restAudit_logsMockMvc.perform(delete("/api/audit_logss/{id}", audit_logs.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Audit_logs> audit_logss = audit_logsRepository.findAll();
        assertThat(audit_logss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
