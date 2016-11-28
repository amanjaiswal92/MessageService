package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.CallDetails;
import com.ail.narad.repository.CallDetailsRepository;
import com.ail.narad.repository.search.CallDetailsSearchRepository;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the CallDetailsResource REST controller.
 *
 * @see CallDetailsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CallDetailsResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_REQUEST_ID = "AAAAA";
    private static final String UPDATED_REQUEST_ID = "BBBBB";
    private static final String DEFAULT_CALLER_ID = "AAAAA";
    private static final String UPDATED_CALLER_ID = "BBBBB";
    private static final String DEFAULT_DIALED_NUMBER = "AAAAA";
    private static final String UPDATED_DIALED_NUMBER = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);
    private static final String DEFAULT_TIME_TO_ANSWER = "AAAAA";
    private static final String UPDATED_TIME_TO_ANSWER = "BBBBB";
    private static final String DEFAULT_DURATION = "AAAAA";
    private static final String UPDATED_DURATION = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";
    private static final String DEFAULT_AGENT_UNIQUE_ID = "AAAAA";
    private static final String UPDATED_AGENT_UNIQUE_ID = "BBBBB";
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";
    
    private static final String DEFAULT_RESPONSE = "AAAAA";
    private static final String UPDATED_RESPONSE = "BBBBB";

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;
    private static final String DEFAULT_ORDERID = "AAAAA";
    private static final String UPDATED_ORDERID = "BBBBB";

    @Inject
    private CallDetailsRepository callDetailsRepository;

    @Inject
    private CallDetailsSearchRepository callDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCallDetailsMockMvc;

    private CallDetails callDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CallDetailsResource callDetailsResource = new CallDetailsResource();
        ReflectionTestUtils.setField(callDetailsResource, "callDetailsSearchRepository", callDetailsSearchRepository);
        ReflectionTestUtils.setField(callDetailsResource, "callDetailsRepository", callDetailsRepository);
        this.restCallDetailsMockMvc = MockMvcBuilders.standaloneSetup(callDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        callDetails = new CallDetails();
        callDetails.setRequestId(DEFAULT_REQUEST_ID);
        callDetails.setCallerId(DEFAULT_CALLER_ID);
        callDetails.setDialedNumber(DEFAULT_DIALED_NUMBER);
        callDetails.setStartTime(DEFAULT_START_TIME);
        callDetails.setEndTime(DEFAULT_END_TIME);
        callDetails.setTimeToAnswer(DEFAULT_TIME_TO_ANSWER);
        callDetails.setDuration(DEFAULT_DURATION);
        callDetails.setLocation(DEFAULT_LOCATION);
        callDetails.setAgentUniqueID(DEFAULT_AGENT_UNIQUE_ID);
        callDetails.setStatus(DEFAULT_STATUS);
        callDetails.setResponse(DEFAULT_RESPONSE);
        callDetails.setCount(DEFAULT_COUNT);
        callDetails.setOrderid(DEFAULT_ORDERID);
    }

    @Test
    @Transactional
    public void createCallDetails() throws Exception {
        int databaseSizeBeforeCreate = callDetailsRepository.findAll().size();

        // Create the CallDetails

        restCallDetailsMockMvc.perform(post("/api/callDetailss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(callDetails)))
                .andExpect(status().isCreated());

        // Validate the CallDetails in the database
        List<CallDetails> callDetailss = callDetailsRepository.findAll();
        assertThat(callDetailss).hasSize(databaseSizeBeforeCreate + 1);
        CallDetails testCallDetails = callDetailss.get(callDetailss.size() - 1);
        assertThat(testCallDetails.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testCallDetails.getCallerId()).isEqualTo(DEFAULT_CALLER_ID);
        assertThat(testCallDetails.getDialedNumber()).isEqualTo(DEFAULT_DIALED_NUMBER);
        assertThat(testCallDetails.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testCallDetails.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testCallDetails.getTimeToAnswer()).isEqualTo(DEFAULT_TIME_TO_ANSWER);
        assertThat(testCallDetails.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testCallDetails.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCallDetails.getAgentUniqueID()).isEqualTo(DEFAULT_AGENT_UNIQUE_ID);
        assertThat(testCallDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCallDetails.getResponse()).isEqualTo(DEFAULT_RESPONSE);
        assertThat(testCallDetails.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testCallDetails.getOrderid()).isEqualTo(DEFAULT_ORDERID);
    }

    @Test
    @Transactional
    public void getAllCallDetailss() throws Exception {
        // Initialize the database
        callDetailsRepository.saveAndFlush(callDetails);

        // Get all the callDetailss
        restCallDetailsMockMvc.perform(get("/api/callDetailss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(callDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
                .andExpect(jsonPath("$.[*].callerId").value(hasItem(DEFAULT_CALLER_ID.toString())))
                .andExpect(jsonPath("$.[*].dialedNumber").value(hasItem(DEFAULT_DIALED_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].timeToAnswer").value(hasItem(DEFAULT_TIME_TO_ANSWER.toString())))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].agentUniqueID").value(hasItem(DEFAULT_AGENT_UNIQUE_ID.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE.toString())))
                .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())))
                .andExpect(jsonPath("$.[*].orderid").value(hasItem(DEFAULT_ORDERID.toString())));
    }

    @Test
    @Transactional
    public void getCallDetails() throws Exception {
        // Initialize the database
        callDetailsRepository.saveAndFlush(callDetails);

        // Get the callDetails
        restCallDetailsMockMvc.perform(get("/api/callDetailss/{id}", callDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(callDetails.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.callerId").value(DEFAULT_CALLER_ID.toString()))
            .andExpect(jsonPath("$.dialedNumber").value(DEFAULT_DIALED_NUMBER.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR))
            .andExpect(jsonPath("$.timeToAnswer").value(DEFAULT_TIME_TO_ANSWER.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.agentUniqueID").value(DEFAULT_AGENT_UNIQUE_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()))
            .andExpect(jsonPath("$.orderid").value(DEFAULT_ORDERID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCallDetails() throws Exception {
        // Get the callDetails
        restCallDetailsMockMvc.perform(get("/api/callDetailss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCallDetails() throws Exception {
        // Initialize the database
        callDetailsRepository.saveAndFlush(callDetails);

		int databaseSizeBeforeUpdate = callDetailsRepository.findAll().size();

        // Update the callDetails
        callDetails.setRequestId(UPDATED_REQUEST_ID);
        callDetails.setCallerId(UPDATED_CALLER_ID);
        callDetails.setDialedNumber(UPDATED_DIALED_NUMBER);
        callDetails.setStartTime(UPDATED_START_TIME);
        callDetails.setEndTime(UPDATED_END_TIME);
        callDetails.setTimeToAnswer(UPDATED_TIME_TO_ANSWER);
        callDetails.setDuration(UPDATED_DURATION);
        callDetails.setLocation(UPDATED_LOCATION);
        callDetails.setAgentUniqueID(UPDATED_AGENT_UNIQUE_ID);
        callDetails.setStatus(UPDATED_STATUS);
        callDetails.setResponse(UPDATED_RESPONSE);
        callDetails.setCount(UPDATED_COUNT);
        callDetails.setOrderid(UPDATED_ORDERID);

        restCallDetailsMockMvc.perform(put("/api/callDetailss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(callDetails)))
                .andExpect(status().isOk());

        // Validate the CallDetails in the database
        List<CallDetails> callDetailss = callDetailsRepository.findAll();
        assertThat(callDetailss).hasSize(databaseSizeBeforeUpdate);
        CallDetails testCallDetails = callDetailss.get(callDetailss.size() - 1);
        assertThat(testCallDetails.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testCallDetails.getCallerId()).isEqualTo(UPDATED_CALLER_ID);
        assertThat(testCallDetails.getDialedNumber()).isEqualTo(UPDATED_DIALED_NUMBER);
        assertThat(testCallDetails.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testCallDetails.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testCallDetails.getTimeToAnswer()).isEqualTo(UPDATED_TIME_TO_ANSWER);
        assertThat(testCallDetails.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testCallDetails.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCallDetails.getAgentUniqueID()).isEqualTo(UPDATED_AGENT_UNIQUE_ID);
        assertThat(testCallDetails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCallDetails.getResponse()).isEqualTo(UPDATED_RESPONSE);
        assertThat(testCallDetails.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testCallDetails.getOrderid()).isEqualTo(UPDATED_ORDERID);
    }

    @Test
    @Transactional
    public void deleteCallDetails() throws Exception {
        // Initialize the database
        callDetailsRepository.saveAndFlush(callDetails);

		int databaseSizeBeforeDelete = callDetailsRepository.findAll().size();

        // Get the callDetails
        restCallDetailsMockMvc.perform(delete("/api/callDetailss/{id}", callDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CallDetails> callDetailss = callDetailsRepository.findAll();
        assertThat(callDetailss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
