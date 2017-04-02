package com.enjoying.auction.web.rest;

import com.enjoying.auction.AuctionApp;

import com.enjoying.auction.domain.AuctionItemStatus;
import com.enjoying.auction.repository.AuctionItemStatusRepository;
import com.enjoying.auction.service.dto.AuctionItemStatusDTO;
import com.enjoying.auction.service.mapper.AuctionItemStatusMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuctionItemStatusResource REST controller.
 *
 * @see AuctionItemStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuctionApp.class)
public class AuctionItemStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AuctionItemStatusRepository auctionItemStatusRepository;

    @Inject
    private AuctionItemStatusMapper auctionItemStatusMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAuctionItemStatusMockMvc;

    private AuctionItemStatus auctionItemStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuctionItemStatusResource auctionItemStatusResource = new AuctionItemStatusResource();
        ReflectionTestUtils.setField(auctionItemStatusResource, "auctionItemStatusRepository", auctionItemStatusRepository);
        ReflectionTestUtils.setField(auctionItemStatusResource, "auctionItemStatusMapper", auctionItemStatusMapper);
        this.restAuctionItemStatusMockMvc = MockMvcBuilders.standaloneSetup(auctionItemStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuctionItemStatus createEntity(EntityManager em) {
        AuctionItemStatus auctionItemStatus = new AuctionItemStatus()
                .name(DEFAULT_NAME);
        return auctionItemStatus;
    }

    @Before
    public void initTest() {
        auctionItemStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuctionItemStatus() throws Exception {
        int databaseSizeBeforeCreate = auctionItemStatusRepository.findAll().size();

        // Create the AuctionItemStatus
        AuctionItemStatusDTO auctionItemStatusDTO = auctionItemStatusMapper.auctionItemStatusToAuctionItemStatusDTO(auctionItemStatus);

        restAuctionItemStatusMockMvc.perform(post("/api/auction-item-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemStatusDTO)))
                .andExpect(status().isCreated());

        // Validate the AuctionItemStatus in the database
        List<AuctionItemStatus> auctionItemStatuses = auctionItemStatusRepository.findAll();
        assertThat(auctionItemStatuses).hasSize(databaseSizeBeforeCreate + 1);
        AuctionItemStatus testAuctionItemStatus = auctionItemStatuses.get(auctionItemStatuses.size() - 1);
        assertThat(testAuctionItemStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionItemStatusRepository.findAll().size();
        // set the field null
        auctionItemStatus.setName(null);

        // Create the AuctionItemStatus, which fails.
        AuctionItemStatusDTO auctionItemStatusDTO = auctionItemStatusMapper.auctionItemStatusToAuctionItemStatusDTO(auctionItemStatus);

        restAuctionItemStatusMockMvc.perform(post("/api/auction-item-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemStatusDTO)))
                .andExpect(status().isBadRequest());

        List<AuctionItemStatus> auctionItemStatuses = auctionItemStatusRepository.findAll();
        assertThat(auctionItemStatuses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuctionItemStatuses() throws Exception {
        // Initialize the database
        auctionItemStatusRepository.saveAndFlush(auctionItemStatus);

        // Get all the auctionItemStatuses
        restAuctionItemStatusMockMvc.perform(get("/api/auction-item-statuses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auctionItemStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAuctionItemStatus() throws Exception {
        // Initialize the database
        auctionItemStatusRepository.saveAndFlush(auctionItemStatus);

        // Get the auctionItemStatus
        restAuctionItemStatusMockMvc.perform(get("/api/auction-item-statuses/{id}", auctionItemStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auctionItemStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuctionItemStatus() throws Exception {
        // Get the auctionItemStatus
        restAuctionItemStatusMockMvc.perform(get("/api/auction-item-statuses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuctionItemStatus() throws Exception {
        // Initialize the database
        auctionItemStatusRepository.saveAndFlush(auctionItemStatus);
        int databaseSizeBeforeUpdate = auctionItemStatusRepository.findAll().size();

        // Update the auctionItemStatus
        AuctionItemStatus updatedAuctionItemStatus = auctionItemStatusRepository.findOne(auctionItemStatus.getId());
        updatedAuctionItemStatus
                .name(UPDATED_NAME);
        AuctionItemStatusDTO auctionItemStatusDTO = auctionItemStatusMapper.auctionItemStatusToAuctionItemStatusDTO(updatedAuctionItemStatus);

        restAuctionItemStatusMockMvc.perform(put("/api/auction-item-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemStatusDTO)))
                .andExpect(status().isOk());

        // Validate the AuctionItemStatus in the database
        List<AuctionItemStatus> auctionItemStatuses = auctionItemStatusRepository.findAll();
        assertThat(auctionItemStatuses).hasSize(databaseSizeBeforeUpdate);
        AuctionItemStatus testAuctionItemStatus = auctionItemStatuses.get(auctionItemStatuses.size() - 1);
        assertThat(testAuctionItemStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteAuctionItemStatus() throws Exception {
        // Initialize the database
        auctionItemStatusRepository.saveAndFlush(auctionItemStatus);
        int databaseSizeBeforeDelete = auctionItemStatusRepository.findAll().size();

        // Get the auctionItemStatus
        restAuctionItemStatusMockMvc.perform(delete("/api/auction-item-statuses/{id}", auctionItemStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AuctionItemStatus> auctionItemStatuses = auctionItemStatusRepository.findAll();
        assertThat(auctionItemStatuses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
