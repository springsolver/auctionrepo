package com.enjoying.auction.web.rest;

import com.enjoying.auction.AuctionApp;

import com.enjoying.auction.domain.AuctionItem;
import com.enjoying.auction.repository.AuctionItemRepository;
import com.enjoying.auction.service.dto.AuctionItemDTO;
import com.enjoying.auction.service.mapper.AuctionItemMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuctionItemResource REST controller.
 *
 * @see AuctionItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuctionApp.class)
public class AuctionItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_CLOSE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CLOSE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CLOSE_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CLOSE_DATE);

    private static final Double DEFAULT_START_PRICE = 1D;
    private static final Double UPDATED_START_PRICE = 2D;

    private static final Double DEFAULT_ACTUAL_PRICE = 1D;
    private static final Double UPDATED_ACTUAL_PRICE = 2D;

    private static final Double DEFAULT_SOLD_PRICE = 1D;
    private static final Double UPDATED_SOLD_PRICE = 2D;

    @Inject
    private AuctionItemRepository auctionItemRepository;

    @Inject
    private AuctionItemMapper auctionItemMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAuctionItemMockMvc;

    private AuctionItem auctionItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuctionItemResource auctionItemResource = new AuctionItemResource();
        ReflectionTestUtils.setField(auctionItemResource, "auctionItemRepository", auctionItemRepository);
        ReflectionTestUtils.setField(auctionItemResource, "auctionItemMapper", auctionItemMapper);
        this.restAuctionItemMockMvc = MockMvcBuilders.standaloneSetup(auctionItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuctionItem createEntity(EntityManager em) {
        AuctionItem auctionItem = new AuctionItem()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .startDate(DEFAULT_START_DATE)
                .closeDate(DEFAULT_CLOSE_DATE)
                .startPrice(DEFAULT_START_PRICE)
                .actualPrice(DEFAULT_ACTUAL_PRICE)
                .soldPrice(DEFAULT_SOLD_PRICE);
        return auctionItem;
    }

    @Before
    public void initTest() {
        auctionItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuctionItem() throws Exception {
        int databaseSizeBeforeCreate = auctionItemRepository.findAll().size();

        // Create the AuctionItem
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);

        restAuctionItemMockMvc.perform(post("/api/auction-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemDTO)))
                .andExpect(status().isCreated());

        // Validate the AuctionItem in the database
        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeCreate + 1);
        AuctionItem testAuctionItem = auctionItems.get(auctionItems.size() - 1);
        assertThat(testAuctionItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuctionItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAuctionItem.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAuctionItem.getCloseDate()).isEqualTo(DEFAULT_CLOSE_DATE);
        assertThat(testAuctionItem.getStartPrice()).isEqualTo(DEFAULT_START_PRICE);
        assertThat(testAuctionItem.getActualPrice()).isEqualTo(DEFAULT_ACTUAL_PRICE);
        assertThat(testAuctionItem.getSoldPrice()).isEqualTo(DEFAULT_SOLD_PRICE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionItemRepository.findAll().size();
        // set the field null
        auctionItem.setName(null);

        // Create the AuctionItem, which fails.
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);

        restAuctionItemMockMvc.perform(post("/api/auction-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemDTO)))
                .andExpect(status().isBadRequest());

        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionItemRepository.findAll().size();
        // set the field null
        auctionItem.setStartDate(null);

        // Create the AuctionItem, which fails.
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);

        restAuctionItemMockMvc.perform(post("/api/auction-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemDTO)))
                .andExpect(status().isBadRequest());

        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCloseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionItemRepository.findAll().size();
        // set the field null
        auctionItem.setCloseDate(null);

        // Create the AuctionItem, which fails.
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);

        restAuctionItemMockMvc.perform(post("/api/auction-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemDTO)))
                .andExpect(status().isBadRequest());

        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionItemRepository.findAll().size();
        // set the field null
        auctionItem.setStartPrice(null);

        // Create the AuctionItem, which fails.
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);

        restAuctionItemMockMvc.perform(post("/api/auction-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemDTO)))
                .andExpect(status().isBadRequest());

        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuctionItems() throws Exception {
        // Initialize the database
        auctionItemRepository.saveAndFlush(auctionItem);

        // Get all the auctionItems
        restAuctionItemMockMvc.perform(get("/api/auction-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auctionItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].closeDate").value(hasItem(DEFAULT_CLOSE_DATE_STR)))
                .andExpect(jsonPath("$.[*].startPrice").value(hasItem(DEFAULT_START_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].actualPrice").value(hasItem(DEFAULT_ACTUAL_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].soldPrice").value(hasItem(DEFAULT_SOLD_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getAuctionItem() throws Exception {
        // Initialize the database
        auctionItemRepository.saveAndFlush(auctionItem);

        // Get the auctionItem
        restAuctionItemMockMvc.perform(get("/api/auction-items/{id}", auctionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auctionItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.closeDate").value(DEFAULT_CLOSE_DATE_STR))
            .andExpect(jsonPath("$.startPrice").value(DEFAULT_START_PRICE.doubleValue()))
            .andExpect(jsonPath("$.actualPrice").value(DEFAULT_ACTUAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.soldPrice").value(DEFAULT_SOLD_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAuctionItem() throws Exception {
        // Get the auctionItem
        restAuctionItemMockMvc.perform(get("/api/auction-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuctionItem() throws Exception {
        // Initialize the database
        auctionItemRepository.saveAndFlush(auctionItem);
        int databaseSizeBeforeUpdate = auctionItemRepository.findAll().size();

        // Update the auctionItem
        AuctionItem updatedAuctionItem = auctionItemRepository.findOne(auctionItem.getId());
        updatedAuctionItem
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .startDate(UPDATED_START_DATE)
                .closeDate(UPDATED_CLOSE_DATE)
                .startPrice(UPDATED_START_PRICE)
                .actualPrice(UPDATED_ACTUAL_PRICE)
                .soldPrice(UPDATED_SOLD_PRICE);
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(updatedAuctionItem);

        restAuctionItemMockMvc.perform(put("/api/auction-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auctionItemDTO)))
                .andExpect(status().isOk());

        // Validate the AuctionItem in the database
        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeUpdate);
        AuctionItem testAuctionItem = auctionItems.get(auctionItems.size() - 1);
        assertThat(testAuctionItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuctionItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAuctionItem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAuctionItem.getCloseDate()).isEqualTo(UPDATED_CLOSE_DATE);
        assertThat(testAuctionItem.getStartPrice()).isEqualTo(UPDATED_START_PRICE);
        assertThat(testAuctionItem.getActualPrice()).isEqualTo(UPDATED_ACTUAL_PRICE);
        assertThat(testAuctionItem.getSoldPrice()).isEqualTo(UPDATED_SOLD_PRICE);
    }

    @Test
    @Transactional
    public void deleteAuctionItem() throws Exception {
        // Initialize the database
        auctionItemRepository.saveAndFlush(auctionItem);
        int databaseSizeBeforeDelete = auctionItemRepository.findAll().size();

        // Get the auctionItem
        restAuctionItemMockMvc.perform(delete("/api/auction-items/{id}", auctionItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        assertThat(auctionItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
