package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.Messagesenders;
import com.ail.narad.repository.MessagesendersRepository;
import com.ail.narad.repository.search.MessagesendersSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.domain.enumeration.Message_sender_status;

/**
 * Test class for the MessagesendersResource REST controller.
 *
 * @see MessagesendersResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessagesendersResourceTest {

    private static final String DEFAULT_SENDER_ID = "AA";
    private static final String UPDATED_SENDER_ID = "BB";
    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";


private static final TemplateType DEFAULT_TYPE = TemplateType.TRANSACTIONAL_EMAIL;
    private static final TemplateType UPDATED_TYPE = TemplateType.TRANSACTIONAL_SMS;


private static final Message_sender_status DEFAULT_STATUS = Message_sender_status.APPROVED;
    private static final Message_sender_status UPDATED_STATUS = Message_sender_status.DISABLED;

    @Inject
    private MessagesendersRepository messagesendersRepository;

    @Inject
    private MessagesendersSearchRepository messagesendersSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMessagesendersMockMvc;

    private Messagesenders messagesenders;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessagesendersResource messagesendersResource = new MessagesendersResource();
        ReflectionTestUtils.setField(messagesendersResource, "messagesendersRepository", messagesendersRepository);
        ReflectionTestUtils.setField(messagesendersResource, "messagesendersSearchRepository", messagesendersSearchRepository);
        this.restMessagesendersMockMvc = MockMvcBuilders.standaloneSetup(messagesendersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        messagesenders = new Messagesenders();
        messagesenders.setSender_id(DEFAULT_SENDER_ID);
        messagesenders.setName(DEFAULT_NAME);
        messagesenders.setType(DEFAULT_TYPE);
        messagesenders.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createMessagesenders() throws Exception {
        int databaseSizeBeforeCreate = messagesendersRepository.findAll().size();

        // Create the Messagesenders

        restMessagesendersMockMvc.perform(post("/api/messagesenderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagesenders)))
                .andExpect(status().isCreated());

        // Validate the Messagesenders in the database
        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeCreate + 1);
        Messagesenders testMessagesenders = messagesenderss.get(messagesenderss.size() - 1);
        assertThat(testMessagesenders.getSender_id()).isEqualTo(DEFAULT_SENDER_ID);
        assertThat(testMessagesenders.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMessagesenders.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMessagesenders.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkSender_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesendersRepository.findAll().size();
        // set the field null
        messagesenders.setSender_id(null);

        // Create the Messagesenders, which fails.

        restMessagesendersMockMvc.perform(post("/api/messagesenderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagesenders)))
                .andExpect(status().isBadRequest());

        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesendersRepository.findAll().size();
        // set the field null
        messagesenders.setName(null);

        // Create the Messagesenders, which fails.

        restMessagesendersMockMvc.perform(post("/api/messagesenderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagesenders)))
                .andExpect(status().isBadRequest());

        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesendersRepository.findAll().size();
        // set the field null
        messagesenders.setType(null);

        // Create the Messagesenders, which fails.

        restMessagesendersMockMvc.perform(post("/api/messagesenderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagesenders)))
                .andExpect(status().isBadRequest());

        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesendersRepository.findAll().size();
        // set the field null
        messagesenders.setStatus(null);

        // Create the Messagesenders, which fails.

        restMessagesendersMockMvc.perform(post("/api/messagesenderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagesenders)))
                .andExpect(status().isBadRequest());

        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessagesenderss() throws Exception {
        // Initialize the database
        messagesendersRepository.saveAndFlush(messagesenders);

        // Get all the messagesenderss
        restMessagesendersMockMvc.perform(get("/api/messagesenderss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(messagesenders.getId().intValue())))
                .andExpect(jsonPath("$.[*].sender_id").value(hasItem(DEFAULT_SENDER_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getMessagesenders() throws Exception {
        // Initialize the database
        messagesendersRepository.saveAndFlush(messagesenders);

        // Get the messagesenders
        restMessagesendersMockMvc.perform(get("/api/messagesenderss/{id}", messagesenders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(messagesenders.getId().intValue()))
            .andExpect(jsonPath("$.sender_id").value(DEFAULT_SENDER_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMessagesenders() throws Exception {
        // Get the messagesenders
        restMessagesendersMockMvc.perform(get("/api/messagesenderss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessagesenders() throws Exception {
        // Initialize the database
        messagesendersRepository.saveAndFlush(messagesenders);

		int databaseSizeBeforeUpdate = messagesendersRepository.findAll().size();

        // Update the messagesenders
        messagesenders.setSender_id(UPDATED_SENDER_ID);
        messagesenders.setName(UPDATED_NAME);
        messagesenders.setType(UPDATED_TYPE);
        messagesenders.setStatus(UPDATED_STATUS);

        restMessagesendersMockMvc.perform(put("/api/messagesenderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagesenders)))
                .andExpect(status().isOk());

        // Validate the Messagesenders in the database
        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeUpdate);
        Messagesenders testMessagesenders = messagesenderss.get(messagesenderss.size() - 1);
        assertThat(testMessagesenders.getSender_id()).isEqualTo(UPDATED_SENDER_ID);
        assertThat(testMessagesenders.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMessagesenders.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMessagesenders.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteMessagesenders() throws Exception {
        // Initialize the database
        messagesendersRepository.saveAndFlush(messagesenders);

		int databaseSizeBeforeDelete = messagesendersRepository.findAll().size();

        // Get the messagesenders
        restMessagesendersMockMvc.perform(delete("/api/messagesenderss/{id}", messagesenders.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Messagesenders> messagesenderss = messagesendersRepository.findAll();
        assertThat(messagesenderss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
