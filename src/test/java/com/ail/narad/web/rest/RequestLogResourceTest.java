package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.RequestLog;
import com.ail.narad.repository.RequestLogRepository;
import com.ail.narad.repository.search.RequestLogSearchRepository;

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


/**
 * Test class for the RequestLogResource REST controller.
 *
 * @see RequestLogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RequestLogResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_REQUEST_ID = "AAAAA";
    private static final String UPDATED_REQUEST_ID = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_REQUEST = "AAAAA";
    private static final String UPDATED_REQUEST = "BBBBB";

    private static final ZonedDateTime DEFAULT_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_TIME_STR = dateTimeFormatter.format(DEFAULT_UPDATE_TIME);

    @Inject
    private RequestLogRepository requestLogRepository;

    @Inject
    private RequestLogSearchRepository requestLogSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRequestLogMockMvc;

    private RequestLog requestLog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestLogResource requestLogResource = new RequestLogResource();
        ReflectionTestUtils.setField(requestLogResource, "requestLogRepository", requestLogRepository);
        ReflectionTestUtils.setField(requestLogResource, "requestLogSearchRepository", requestLogSearchRepository);
        this.restRequestLogMockMvc = MockMvcBuilders.standaloneSetup(requestLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        requestLog = new RequestLog();
        requestLog.setRequestId(DEFAULT_REQUEST_ID);
        requestLog.setType(DEFAULT_TYPE);
        requestLog.setRequest(DEFAULT_REQUEST);
        requestLog.setUpdate_time(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createRequestLog() throws Exception {
        int databaseSizeBeforeCreate = requestLogRepository.findAll().size();

        // Create the RequestLog

        restRequestLogMockMvc.perform(post("/api/requestLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestLog)))
                .andExpect(status().isCreated());

        // Validate the RequestLog in the database
        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeCreate + 1);
        RequestLog testRequestLog = requestLogs.get(requestLogs.size() - 1);
        assertThat(testRequestLog.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRequestLog.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequestLog.getRequest()).isEqualTo(DEFAULT_REQUEST);
        assertThat(testRequestLog.getUpdate_time()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestLogRepository.findAll().size();
        // set the field null
        requestLog.setRequestId(null);

        // Create the RequestLog, which fails.

        restRequestLogMockMvc.perform(post("/api/requestLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestLog)))
                .andExpect(status().isBadRequest());

        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestLogRepository.findAll().size();
        // set the field null
        requestLog.setType(null);

        // Create the RequestLog, which fails.

        restRequestLogMockMvc.perform(post("/api/requestLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestLog)))
                .andExpect(status().isBadRequest());

        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestLogRepository.findAll().size();
        // set the field null
        requestLog.setRequest(null);

        // Create the RequestLog, which fails.

        restRequestLogMockMvc.perform(post("/api/requestLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestLog)))
                .andExpect(status().isBadRequest());

        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdate_timeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestLogRepository.findAll().size();
        // set the field null
        requestLog.setUpdate_time(null);

        // Create the RequestLog, which fails.

        restRequestLogMockMvc.perform(post("/api/requestLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestLog)))
                .andExpect(status().isBadRequest());

        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequestLogs() throws Exception {
        // Initialize the database
        requestLogRepository.saveAndFlush(requestLog);

        // Get all the requestLogs
        restRequestLogMockMvc.perform(get("/api/requestLogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(requestLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].request").value(hasItem(DEFAULT_REQUEST.toString())))
                .andExpect(jsonPath("$.[*].update_time").value(hasItem(DEFAULT_UPDATE_TIME_STR)));
    }

    @Test
    @Transactional
    public void getRequestLog() throws Exception {
        // Initialize the database
        requestLogRepository.saveAndFlush(requestLog);

        // Get the requestLog
        restRequestLogMockMvc.perform(get("/api/requestLogs/{id}", requestLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(requestLog.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.request").value(DEFAULT_REQUEST.toString()))
            .andExpect(jsonPath("$.update_time").value(DEFAULT_UPDATE_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingRequestLog() throws Exception {
        // Get the requestLog
        restRequestLogMockMvc.perform(get("/api/requestLogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestLog() throws Exception {
        // Initialize the database
        requestLogRepository.saveAndFlush(requestLog);

		int databaseSizeBeforeUpdate = requestLogRepository.findAll().size();

        // Update the requestLog
        requestLog.setRequestId(UPDATED_REQUEST_ID);
        requestLog.setType(UPDATED_TYPE);
        requestLog.setRequest(UPDATED_REQUEST);
        requestLog.setUpdate_time(UPDATED_UPDATE_TIME);

        restRequestLogMockMvc.perform(put("/api/requestLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requestLog)))
                .andExpect(status().isOk());

        // Validate the RequestLog in the database
        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeUpdate);
        RequestLog testRequestLog = requestLogs.get(requestLogs.size() - 1);
        assertThat(testRequestLog.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRequestLog.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequestLog.getRequest()).isEqualTo(UPDATED_REQUEST);
        assertThat(testRequestLog.getUpdate_time()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void deleteRequestLog() throws Exception {
        // Initialize the database
        requestLogRepository.saveAndFlush(requestLog);

		int databaseSizeBeforeDelete = requestLogRepository.findAll().size();

        // Get the requestLog
        restRequestLogMockMvc.perform(delete("/api/requestLogs/{id}", requestLog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RequestLog> requestLogs = requestLogRepository.findAll();
        assertThat(requestLogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
