package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.Templates;
import com.ail.narad.repository.TemplatesRepository;
import com.ail.narad.repository.search.TemplatesSearchRepository;

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

import com.ail.narad.domain.enumeration.TemplateStatus;
import com.ail.narad.domain.enumeration.TemplateType;

/**
 * Test class for the TemplatesResource REST controller.
 *
 * @see TemplatesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TemplatesResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TEMPLATE_ID = "AA";
    private static final String UPDATED_TEMPLATE_ID = "BB";
    private static final String DEFAULT_MESSAGE = "AAA";
    private static final String UPDATED_MESSAGE = "BBB";


private static final TemplateStatus DEFAULT_STATUS = TemplateStatus.NEW;
    private static final TemplateStatus UPDATED_STATUS = TemplateStatus.PENDING_APPROVAL;

    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_TIME);

    private static final ZonedDateTime DEFAULT_APPROVAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_APPROVAL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_APPROVAL_TIME_STR = dateTimeFormatter.format(DEFAULT_APPROVAL_TIME);

    private static final ZonedDateTime DEFAULT_DISABLED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DISABLED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DISABLED_TIME_STR = dateTimeFormatter.format(DEFAULT_DISABLED_TIME);


private static final TemplateType DEFAULT_TYPE = TemplateType.TRANSACTIONAL_EMAIL;
    private static final TemplateType UPDATED_TYPE = TemplateType.TRANSACTIONAL_SMS;
    private static final String DEFAULT_MODULE = "AA";
    private static final String UPDATED_MODULE = "BB";

    @Inject
    private TemplatesRepository templatesRepository;

    @Inject
    private TemplatesSearchRepository templatesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTemplatesMockMvc;

    private Templates templates;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TemplatesResource templatesResource = new TemplatesResource();
        ReflectionTestUtils.setField(templatesResource, "templatesRepository", templatesRepository);
        ReflectionTestUtils.setField(templatesResource, "templatesSearchRepository", templatesSearchRepository);
        this.restTemplatesMockMvc = MockMvcBuilders.standaloneSetup(templatesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        templates = new Templates();
        templates.setTemplate_id(DEFAULT_TEMPLATE_ID);
        templates.setMessage(DEFAULT_MESSAGE);
        templates.setStatus(DEFAULT_STATUS);
        templates.setCreation_time(DEFAULT_CREATION_TIME);
        templates.setApproval_time(DEFAULT_APPROVAL_TIME);
        templates.setDisabled_time(DEFAULT_DISABLED_TIME);
        templates.setType(DEFAULT_TYPE);
        templates.setModule(DEFAULT_MODULE);
    }

    @Test
    @Transactional
    public void createTemplates() throws Exception {
        int databaseSizeBeforeCreate = templatesRepository.findAll().size();

        // Create the Templates

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isCreated());

        // Validate the Templates in the database
        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeCreate + 1);
        Templates testTemplates = templatess.get(templatess.size() - 1);
        assertThat(testTemplates.getTemplate_id()).isEqualTo(DEFAULT_TEMPLATE_ID);
        assertThat(testTemplates.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testTemplates.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTemplates.getCreation_time()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testTemplates.getApproval_time()).isEqualTo(DEFAULT_APPROVAL_TIME);
        assertThat(testTemplates.getDisabled_time()).isEqualTo(DEFAULT_DISABLED_TIME);
        assertThat(testTemplates.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTemplates.getModule()).isEqualTo(DEFAULT_MODULE);
    }

    @Test
    @Transactional
    public void checkTemplate_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = templatesRepository.findAll().size();
        // set the field null
        templates.setTemplate_id(null);

        // Create the Templates, which fails.

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isBadRequest());

        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = templatesRepository.findAll().size();
        // set the field null
        templates.setMessage(null);

        // Create the Templates, which fails.

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isBadRequest());

        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = templatesRepository.findAll().size();
        // set the field null
        templates.setStatus(null);

        // Create the Templates, which fails.

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isBadRequest());

        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreation_timeIsRequired() throws Exception {
        int databaseSizeBeforeTest = templatesRepository.findAll().size();
        // set the field null
        templates.setCreation_time(null);

        // Create the Templates, which fails.

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isBadRequest());

        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = templatesRepository.findAll().size();
        // set the field null
        templates.setType(null);

        // Create the Templates, which fails.

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isBadRequest());

        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModuleIsRequired() throws Exception {
        int databaseSizeBeforeTest = templatesRepository.findAll().size();
        // set the field null
        templates.setModule(null);

        // Create the Templates, which fails.

        restTemplatesMockMvc.perform(post("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isBadRequest());

        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTemplatess() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatess
        restTemplatesMockMvc.perform(get("/api/templatess"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(templates.getId().intValue())))
                .andExpect(jsonPath("$.[*].template_id").value(hasItem(DEFAULT_TEMPLATE_ID.toString())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].creation_time").value(hasItem(DEFAULT_CREATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].approval_time").value(hasItem(DEFAULT_APPROVAL_TIME_STR)))
                .andExpect(jsonPath("$.[*].disabled_time").value(hasItem(DEFAULT_DISABLED_TIME_STR)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE.toString())));
    }

    @Test
    @Transactional
    public void getTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get the templates
        restTemplatesMockMvc.perform(get("/api/templatess/{id}", templates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(templates.getId().intValue()))
            .andExpect(jsonPath("$.template_id").value(DEFAULT_TEMPLATE_ID.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creation_time").value(DEFAULT_CREATION_TIME_STR))
            .andExpect(jsonPath("$.approval_time").value(DEFAULT_APPROVAL_TIME_STR))
            .andExpect(jsonPath("$.disabled_time").value(DEFAULT_DISABLED_TIME_STR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.module").value(DEFAULT_MODULE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTemplates() throws Exception {
        // Get the templates
        restTemplatesMockMvc.perform(get("/api/templatess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

		int databaseSizeBeforeUpdate = templatesRepository.findAll().size();

        // Update the templates
        templates.setTemplate_id(UPDATED_TEMPLATE_ID);
        templates.setMessage(UPDATED_MESSAGE);
        templates.setStatus(UPDATED_STATUS);
        templates.setCreation_time(UPDATED_CREATION_TIME);
        templates.setApproval_time(UPDATED_APPROVAL_TIME);
        templates.setDisabled_time(UPDATED_DISABLED_TIME);
        templates.setType(UPDATED_TYPE);
        templates.setModule(UPDATED_MODULE);

        restTemplatesMockMvc.perform(put("/api/templatess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templates)))
                .andExpect(status().isOk());

        // Validate the Templates in the database
        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeUpdate);
        Templates testTemplates = templatess.get(templatess.size() - 1);
        assertThat(testTemplates.getTemplate_id()).isEqualTo(UPDATED_TEMPLATE_ID);
        assertThat(testTemplates.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTemplates.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTemplates.getCreation_time()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testTemplates.getApproval_time()).isEqualTo(UPDATED_APPROVAL_TIME);
        assertThat(testTemplates.getDisabled_time()).isEqualTo(UPDATED_DISABLED_TIME);
        assertThat(testTemplates.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplates.getModule()).isEqualTo(UPDATED_MODULE);
    }

    @Test
    @Transactional
    public void deleteTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

		int databaseSizeBeforeDelete = templatesRepository.findAll().size();

        // Get the templates
        restTemplatesMockMvc.perform(delete("/api/templatess/{id}", templates.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Templates> templatess = templatesRepository.findAll();
        assertThat(templatess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
