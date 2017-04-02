package com.enjoying.auction.web.rest;

import com.enjoying.auction.AuctionApp;

import com.enjoying.auction.domain.Bid;
import com.enjoying.auction.repository.BidRepository;
import com.enjoying.auction.service.dto.BidDTO;
import com.enjoying.auction.service.mapper.BidMapper;

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
 * Test class for the BidResource REST controller.
 *
 * @see BidResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuctionApp.class)
public class BidResourceIntTest {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Boolean DEFAULT_SUCCESSFUL = false;
    private static final Boolean UPDATED_SUCCESSFUL = true;

    @Inject
    private BidRepository bidRepository;

    @Inject
    private BidMapper bidMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBidMockMvc;

    private Bid bid;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BidResource bidResource = new BidResource();
        ReflectionTestUtils.setField(bidResource, "bidRepository", bidRepository);
        ReflectionTestUtils.setField(bidResource, "bidMapper", bidMapper);
        this.restBidMockMvc = MockMvcBuilders.standaloneSetup(bidResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bid createEntity(EntityManager em) {
        Bid bid = new Bid()
                .price(DEFAULT_PRICE)
                .successful(DEFAULT_SUCCESSFUL);
        return bid;
    }

    @Before
    public void initTest() {
        bid = createEntity(em);
    }

    @Test
    @Transactional
    public void createBid() throws Exception {
        int databaseSizeBeforeCreate = bidRepository.findAll().size();

        // Create the Bid
        BidDTO bidDTO = bidMapper.bidToBidDTO(bid);

        restBidMockMvc.perform(post("/api/bids")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidDTO)))
                .andExpect(status().isCreated());

        // Validate the Bid in the database
        List<Bid> bids = bidRepository.findAll();
        assertThat(bids).hasSize(databaseSizeBeforeCreate + 1);
        Bid testBid = bids.get(bids.size() - 1);
        assertThat(testBid.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBid.isSuccessful()).isEqualTo(DEFAULT_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setPrice(null);

        // Create the Bid, which fails.
        BidDTO bidDTO = bidMapper.bidToBidDTO(bid);

        restBidMockMvc.perform(post("/api/bids")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidDTO)))
                .andExpect(status().isBadRequest());

        List<Bid> bids = bidRepository.findAll();
        assertThat(bids).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBids() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);

        // Get all the bids
        restBidMockMvc.perform(get("/api/bids?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bid.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].successful").value(hasItem(DEFAULT_SUCCESSFUL.booleanValue())));
    }

    @Test
    @Transactional
    public void getBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);

        // Get the bid
        restBidMockMvc.perform(get("/api/bids/{id}", bid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bid.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.successful").value(DEFAULT_SUCCESSFUL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBid() throws Exception {
        // Get the bid
        restBidMockMvc.perform(get("/api/bids/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);
        int databaseSizeBeforeUpdate = bidRepository.findAll().size();

        // Update the bid
        Bid updatedBid = bidRepository.findOne(bid.getId());
        updatedBid
                .price(UPDATED_PRICE)
                .successful(UPDATED_SUCCESSFUL);
        BidDTO bidDTO = bidMapper.bidToBidDTO(updatedBid);

        restBidMockMvc.perform(put("/api/bids")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bidDTO)))
                .andExpect(status().isOk());

        // Validate the Bid in the database
        List<Bid> bids = bidRepository.findAll();
        assertThat(bids).hasSize(databaseSizeBeforeUpdate);
        Bid testBid = bids.get(bids.size() - 1);
        assertThat(testBid.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBid.isSuccessful()).isEqualTo(UPDATED_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void deleteBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);
        int databaseSizeBeforeDelete = bidRepository.findAll().size();

        // Get the bid
        restBidMockMvc.perform(delete("/api/bids/{id}", bid.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Bid> bids = bidRepository.findAll();
        assertThat(bids).hasSize(databaseSizeBeforeDelete - 1);
    }
}
