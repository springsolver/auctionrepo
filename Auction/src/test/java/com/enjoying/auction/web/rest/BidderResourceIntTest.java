package com.enjoying.auction.web.rest;

import com.enjoying.auction.AuctionApp;

import com.enjoying.auction.domain.Bidder;
import com.enjoying.auction.repository.BidderRepository;
import com.enjoying.auction.service.dto.BidderDTO;
import com.enjoying.auction.service.mapper.BidderMapper;

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
 * Test class for the BidderResource REST controller.
 *
 * @see BidderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuctionApp.class)
public class BidderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_JMBG = "AAAAA";
    private static final String UPDATED_JMBG = "BBBBB";

    @Inject
    private BidderRepository bidderRepository;

    @Inject
    private BidderMapper bidderMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBidderMockMvc;

    private Bidder bidder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BidderResource bidderResource = new BidderResource();
        ReflectionTestUtils.setField(bidderResource, "bidderRepository", bidderRepository);
        ReflectionTestUtils.setField(bidderResource, "bidderMapper", bidderMapper);
        this.restBidderMockMvc = MockMvcBuilders.standaloneSetup(bidderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bidder createEntity(EntityManager em) {
        Bidder bidder = new Bidder()
                .name(DEFAULT_NAME)
                .jmbg(DEFAULT_JMBG);
        return bidder;
    }

    @Before
    public void initTest() {
        bidder = createEntity(em);
    }

    @Test
    @Transactional
    public void createBidder() throws Exception {
        int databaseSizeBeforeCreate = bidderRepository.findAll().size();

        // Create the Bidder
        BidderDTO bidderDTO = bidderMapper.bidderToBidderDTO(bidder);

        restBidderMockMvc.perform(post("/api/bidders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidderDTO)))
                .andExpect(status().isCreated());

        // Validate the Bidder in the database
        List<Bidder> bidders = bidderRepository.findAll();
        assertThat(bidders).hasSize(databaseSizeBeforeCreate + 1);
        Bidder testBidder = bidders.get(bidders.size() - 1);
        assertThat(testBidder.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBidder.getJmbg()).isEqualTo(DEFAULT_JMBG);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidderRepository.findAll().size();
        // set the field null
        bidder.setName(null);

        // Create the Bidder, which fails.
        BidderDTO bidderDTO = bidderMapper.bidderToBidderDTO(bidder);

        restBidderMockMvc.perform(post("/api/bidders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidderDTO)))
                .andExpect(status().isBadRequest());

        List<Bidder> bidders = bidderRepository.findAll();
        assertThat(bidders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJmbgIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidderRepository.findAll().size();
        // set the field null
        bidder.setJmbg(null);

        // Create the Bidder, which fails.
        BidderDTO bidderDTO = bidderMapper.bidderToBidderDTO(bidder);

        restBidderMockMvc.perform(post("/api/bidders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidderDTO)))
                .andExpect(status().isBadRequest());

        List<Bidder> bidders = bidderRepository.findAll();
        assertThat(bidders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBidders() throws Exception {
        // Initialize the database
        bidderRepository.saveAndFlush(bidder);

        // Get all the bidders
        restBidderMockMvc.perform(get("/api/bidders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bidder.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].jmbg").value(hasItem(DEFAULT_JMBG.toString())));
    }

    @Test
    @Transactional
    public void getBidder() throws Exception {
        // Initialize the database
        bidderRepository.saveAndFlush(bidder);

        // Get the bidder
        restBidderMockMvc.perform(get("/api/bidders/{id}", bidder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bidder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.jmbg").value(DEFAULT_JMBG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBidder() throws Exception {
        // Get the bidder
        restBidderMockMvc.perform(get("/api/bidders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBidder() throws Exception {
        // Initialize the database
        bidderRepository.saveAndFlush(bidder);
        int databaseSizeBeforeUpdate = bidderRepository.findAll().size();

        // Update the bidder
        Bidder updatedBidder = bidderRepository.findOne(bidder.getId());
        updatedBidder
                .name(UPDATED_NAME)
                .jmbg(UPDATED_JMBG);
        BidderDTO bidderDTO = bidderMapper.bidderToBidderDTO(updatedBidder);

        restBidderMockMvc.perform(put("/api/bidders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidderDTO)))
                .andExpect(status().isOk());

        // Validate the Bidder in the database
        List<Bidder> bidders = bidderRepository.findAll();
        assertThat(bidders).hasSize(databaseSizeBeforeUpdate);
        Bidder testBidder = bidders.get(bidders.size() - 1);
        assertThat(testBidder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBidder.getJmbg()).isEqualTo(UPDATED_JMBG);
    }

    @Test
    @Transactional
    public void deleteBidder() throws Exception {
        // Initialize the database
        bidderRepository.saveAndFlush(bidder);
        int databaseSizeBeforeDelete = bidderRepository.findAll().size();

        // Get the bidder
        restBidderMockMvc.perform(delete("/api/bidders/{id}", bidder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Bidder> bidders = bidderRepository.findAll();
        assertThat(bidders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
