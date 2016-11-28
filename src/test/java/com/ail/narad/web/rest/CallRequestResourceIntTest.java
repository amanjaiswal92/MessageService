package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.CallRequest;
import com.ail.narad.repository.CallRequestRepository;
import com.ail.narad.repository.search.CallRequestSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CallRequestResource REST controller.
 *
 * @see CallRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CallRequestResourceIntTest {

    private static final String DEFAULT_REQUEST_ID = "AAAAA";
    private static final String UPDATED_REQUEST_ID = "BBBBB";
    private static final String DEFAULT_PHONENO = "AAAAA";
    private static final String UPDATED_PHONENO = "BBBBB";
    private static final String DEFAULT_CONSIGNMENTID = "AAAAA";
    private static final String UPDATED_CONSIGNMENTID = "BBBBB";
    
    private static final String DEFAULT_RESPONSE = "AAAAA";
    private static final String UPDATED_RESPONSE = "BBBBB";
    private static final String DEFAULT_BODY = "AAAAA";
    private static final String UPDATED_BODY = "BBBBB";

    private static final Boolean DEFAULT_CANCELLED = false;
    private static final Boolean UPDATED_CANCELLED = true;

    @Inject
    private CallRequestRepository callRequestRepository;

    @Inject
    private CallRequestSearchRepository callRequestSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCallRequestMockMvc;

    private CallRequest callRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CallRequestResource callRequestResource = new CallRequestResource();
        ReflectionTestUtils.setField(callRequestResource, "callRequestSearchRepository", callRequestSearchRepository);
        ReflectionTestUtils.setField(callRequestResource, "callRequestRepository", callRequestRepository);
        this.restCallRequestMockMvc = MockMvcBuilders.standaloneSetup(callRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        callRequest = new CallRequest();
        callRequest.setRequestId(DEFAULT_REQUEST_ID);
        callRequest.setPhoneno(DEFAULT_PHONENO);
        callRequest.setConsignmentid(DEFAULT_CONSIGNMENTID);
        callRequest.setResponse(DEFAULT_RESPONSE);
        callRequest.setBody(DEFAULT_BODY);
        callRequest.setCancelled(DEFAULT_CANCELLED);
    }

    @Test
    @Transactional
    public void createCallRequest() throws Exception {
        int databaseSizeBeforeCreate = callRequestRepository.findAll().size();

        // Create the CallRequest

        restCallRequestMockMvc.perform(post("/api/callRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(callRequest)))
                .andExpect(status().isCreated());

        // Validate the CallRequest in the database
        List<CallRequest> callRequests = callRequestRepository.findAll();
        assertThat(callRequests).hasSize(databaseSizeBeforeCreate + 1);
        CallRequest testCallRequest = callRequests.get(callRequests.size() - 1);
        assertThat(testCallRequest.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testCallRequest.getPhoneno()).isEqualTo(DEFAULT_PHONENO);
        assertThat(testCallRequest.getConsignmentid()).isEqualTo(DEFAULT_CONSIGNMENTID);
        assertThat(testCallRequest.getResponse()).isEqualTo(DEFAULT_RESPONSE);
        assertThat(testCallRequest.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testCallRequest.getCancelled()).isEqualTo(DEFAULT_CANCELLED);
    }

    @Test
    @Transactional
    public void getAllCallRequests() throws Exception {
        // Initialize the database
        callRequestRepository.saveAndFlush(callRequest);

        // Get all the callRequests
        restCallRequestMockMvc.perform(get("/api/callRequests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(callRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
                .andExpect(jsonPath("$.[*].phoneno").value(hasItem(DEFAULT_PHONENO.toString())))
                .andExpect(jsonPath("$.[*].consignmentid").value(hasItem(DEFAULT_CONSIGNMENTID.toString())))
                .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE.toString())))
                .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
                .andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getCallRequest() throws Exception {
        // Initialize the database
        callRequestRepository.saveAndFlush(callRequest);

        // Get the callRequest
        restCallRequestMockMvc.perform(get("/api/callRequests/{id}", callRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(callRequest.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.phoneno").value(DEFAULT_PHONENO.toString()))
            .andExpect(jsonPath("$.consignmentid").value(DEFAULT_CONSIGNMENTID.toString()))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.cancelled").value(DEFAULT_CANCELLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCallRequest() throws Exception {
        // Get the callRequest
        restCallRequestMockMvc.perform(get("/api/callRequests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCallRequest() throws Exception {
        // Initialize the database
        callRequestRepository.saveAndFlush(callRequest);

		int databaseSizeBeforeUpdate = callRequestRepository.findAll().size();

        // Update the callRequest
        callRequest.setRequestId(UPDATED_REQUEST_ID);
        callRequest.setPhoneno(UPDATED_PHONENO);
        callRequest.setConsignmentid(UPDATED_CONSIGNMENTID);
        callRequest.setResponse(UPDATED_RESPONSE);
        callRequest.setBody(UPDATED_BODY);
        callRequest.setCancelled(UPDATED_CANCELLED);

        restCallRequestMockMvc.perform(put("/api/callRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(callRequest)))
                .andExpect(status().isOk());

        // Validate the CallRequest in the database
        List<CallRequest> callRequests = callRequestRepository.findAll();
        assertThat(callRequests).hasSize(databaseSizeBeforeUpdate);
        CallRequest testCallRequest = callRequests.get(callRequests.size() - 1);
        assertThat(testCallRequest.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testCallRequest.getPhoneno()).isEqualTo(UPDATED_PHONENO);
        assertThat(testCallRequest.getConsignmentid()).isEqualTo(UPDATED_CONSIGNMENTID);
        assertThat(testCallRequest.getResponse()).isEqualTo(UPDATED_RESPONSE);
        assertThat(testCallRequest.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testCallRequest.getCancelled()).isEqualTo(UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    public void deleteCallRequest() throws Exception {
        // Initialize the database
        callRequestRepository.saveAndFlush(callRequest);

		int databaseSizeBeforeDelete = callRequestRepository.findAll().size();

        // Get the callRequest
        restCallRequestMockMvc.perform(delete("/api/callRequests/{id}", callRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CallRequest> callRequests = callRequestRepository.findAll();
        assertThat(callRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
