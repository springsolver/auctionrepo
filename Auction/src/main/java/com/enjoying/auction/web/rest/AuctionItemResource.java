package com.enjoying.auction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoying.auction.domain.AuctionItem;

import com.enjoying.auction.repository.AuctionItemRepository;
import com.enjoying.auction.web.rest.util.HeaderUtil;
import com.enjoying.auction.web.rest.util.PaginationUtil;
import com.enjoying.auction.service.dto.AuctionItemDTO;
import com.enjoying.auction.service.mapper.AuctionItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing AuctionItem.
 */
@RestController
@RequestMapping("/api")
public class AuctionItemResource {

    private final Logger log = LoggerFactory.getLogger(AuctionItemResource.class);
        
    @Inject
    private AuctionItemRepository auctionItemRepository;

    @Inject
    private AuctionItemMapper auctionItemMapper;

    /**
     * POST  /auction-items : Create a new auctionItem.
     *
     * @param auctionItemDTO the auctionItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auctionItemDTO, or with status 400 (Bad Request) if the auctionItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/auction-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuctionItemDTO> createAuctionItem(@Valid @RequestBody AuctionItemDTO auctionItemDTO) throws URISyntaxException {
        log.debug("REST request to save AuctionItem : {}", auctionItemDTO);
        if (auctionItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auctionItem", "idexists", "A new auctionItem cannot already have an ID")).body(null);
        }
        AuctionItem auctionItem = auctionItemMapper.auctionItemDTOToAuctionItem(auctionItemDTO);
        auctionItem = auctionItemRepository.save(auctionItem);
        AuctionItemDTO result = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);
        return ResponseEntity.created(new URI("/api/auction-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auctionItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auction-items : Updates an existing auctionItem.
     *
     * @param auctionItemDTO the auctionItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auctionItemDTO,
     * or with status 400 (Bad Request) if the auctionItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the auctionItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/auction-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuctionItemDTO> updateAuctionItem(@Valid @RequestBody AuctionItemDTO auctionItemDTO) throws URISyntaxException {
        log.debug("REST request to update AuctionItem : {}", auctionItemDTO);
        if (auctionItemDTO.getId() == null) {
            return createAuctionItem(auctionItemDTO);
        }
        AuctionItem auctionItem = auctionItemMapper.auctionItemDTOToAuctionItem(auctionItemDTO);
        auctionItem = auctionItemRepository.save(auctionItem);
        AuctionItemDTO result = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auctionItem", auctionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auction-items : get all the auctionItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of auctionItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/auction-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuctionItemDTO>> getAllAuctionItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AuctionItems");
        Page<AuctionItem> page = auctionItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auction-items");
        return new ResponseEntity<>(auctionItemMapper.auctionItemsToAuctionItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /auction-items/:id : get the "id" auctionItem.
     *
     * @param id the id of the auctionItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auctionItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/auction-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuctionItemDTO> getAuctionItem(@PathVariable Long id) {
        log.debug("REST request to get AuctionItem : {}", id);
        AuctionItem auctionItem = auctionItemRepository.findOne(id);
        AuctionItemDTO auctionItemDTO = auctionItemMapper.auctionItemToAuctionItemDTO(auctionItem);
        return Optional.ofNullable(auctionItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /auction-items/:id : delete the "id" auctionItem.
     *
     * @param id the id of the auctionItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/auction-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuctionItem(@PathVariable Long id) {
        log.debug("REST request to delete AuctionItem : {}", id);
        auctionItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auctionItem", id.toString())).build();
    }

}
