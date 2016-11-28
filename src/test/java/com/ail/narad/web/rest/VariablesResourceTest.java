package com.ail.narad.web.rest;

import com.ail.narad.Application;
import com.ail.narad.domain.Variables;
import com.ail.narad.repository.VariablesRepository;
import com.ail.narad.repository.search.VariablesSearchRepository;

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


/**
 * Test class for the VariablesResource REST controller.
 *
 * @see VariablesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VariablesResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private VariablesRepository variablesRepository;

    @Inject
    private VariablesSearchRepository variablesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVariablesMockMvc;

    private Variables variables;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VariablesResource variablesResource = new VariablesResource();
        ReflectionTestUtils.setField(variablesResource, "variablesRepository", variablesRepository);
        ReflectionTestUtils.setField(variablesResource, "variablesSearchRepository", variablesSearchRepository);
        this.restVariablesMockMvc = MockMvcBuilders.standaloneSetup(variablesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        variables = new Variables();
        variables.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createVariables() throws Exception {
        int databaseSizeBeforeCreate = variablesRepository.findAll().size();

        // Create the Variables

        restVariablesMockMvc.perform(post("/api/variabless")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
                .andExpect(status().isCreated());

        // Validate the Variables in the database
        List<Variables> variabless = variablesRepository.findAll();
        assertThat(variabless).hasSize(databaseSizeBeforeCreate + 1);
        Variables testVariables = variabless.get(variabless.size() - 1);
        assertThat(testVariables.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = variablesRepository.findAll().size();
        // set the field null
        variables.setName(null);

        // Create the Variables, which fails.

        restVariablesMockMvc.perform(post("/api/variabless")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
                .andExpect(status().isBadRequest());

        List<Variables> variabless = variablesRepository.findAll();
        assertThat(variabless).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVariabless() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        // Get all the variabless
        restVariablesMockMvc.perform(get("/api/variabless"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(variables.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        // Get the variables
        restVariablesMockMvc.perform(get("/api/variabless/{id}", variables.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(variables.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVariables() throws Exception {
        // Get the variables
        restVariablesMockMvc.perform(get("/api/variabless/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

		int databaseSizeBeforeUpdate = variablesRepository.findAll().size();

        // Update the variables
        variables.setName(UPDATED_NAME);

        restVariablesMockMvc.perform(put("/api/variabless")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
                .andExpect(status().isOk());

        // Validate the Variables in the database
        List<Variables> variabless = variablesRepository.findAll();
        assertThat(variabless).hasSize(databaseSizeBeforeUpdate);
        Variables testVariables = variabless.get(variabless.size() - 1);
        assertThat(testVariables.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

		int databaseSizeBeforeDelete = variablesRepository.findAll().size();

        // Get the variables
        restVariablesMockMvc.perform(delete("/api/variabless/{id}", variables.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Variables> variabless = variablesRepository.findAll();
        assertThat(variabless).hasSize(databaseSizeBeforeDelete - 1);
    }
}
