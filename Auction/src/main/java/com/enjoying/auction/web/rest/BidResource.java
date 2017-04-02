package com.enjoying.auction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoying.auction.domain.AuctionItem;
import com.enjoying.auction.domain.Bid;
import com.enjoying.auction.repository.AuctionItemRepository;
import com.enjoying.auction.repository.BidRepository;
import com.enjoying.auction.web.rest.util.HeaderUtil;
import com.enjoying.auction.web.rest.util.PaginationUtil;
import com.enjoying.auction.service.dto.BidDTO;
import com.enjoying.auction.service.mapper.BidMapper;

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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Bid.
 */
@RestController
@RequestMapping("/api")
public class BidResource {

    private final Logger log = LoggerFactory.getLogger(BidResource.class);
        
    @Inject
    private BidRepository bidRepository;

    @Inject
    private BidMapper bidMapper;

    @Inject
    private AuctionItemRepository auctionItemRepository;
    
    /**
     * POST  /bids : Create a new bid.
     *
     * @param bidDTO the bidDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bidDTO, or with status 400 (Bad Request) if the bid has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bids",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BidDTO> createBid(@Valid @RequestBody BidDTO bidDTO) throws URISyntaxException {
        log.debug("REST request to save Bid : {}", bidDTO);
        if (bidDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bid", "Bid ID exists", "A new bid cannot already have an ID")).body(null);
        }
        AuctionItem auctionItem = auctionItemRepository.findOne(bidDTO.getAuctionItemId());
        if (auctionItem.getSoldPrice() != null) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bid", "Item alreade sold", "Auction iem was already sold")).body(null);
        }
        if (System.currentTimeMillis() - Date.from(auctionItem.getCloseDate().toInstant()).getTime() > 0) {
        	auctionItem.setSoldPrice(auctionItem.getActualPrice());    	
        	auctionItemRepository.save(auctionItem);
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bid", "Auction for this item passed", "Auction for this item passed")).body(null);
        }      	
        Bid bid = bidMapper.bidDTOToBid(bidDTO);
        if (bid.getPrice() <= auctionItem.getStartPrice() || (auctionItem.getActualPrice() != null && bid.getPrice() <= auctionItem.getActualPrice())) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bid", "Bid price is small", "A new bid price is not enough")).body(null);
        }
        auctionItem.setActualPrice(bid.getPrice());
        auctionItem.setBidder(bid.getBidder());
        bid = bidRepository.save(bid);
        auctionItemRepository.save(auctionItem);
        BidDTO result = bidMapper.bidToBidDTO(bid);
        return ResponseEntity.created(new URI("/api/bids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bid", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bids : Updates an existing bid.
     *
     * @param bidDTO the bidDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bidDTO,
     * or with status 400 (Bad Request) if the bidDTO is not valid,
     * or with status 500 (Internal Server Error) if the bidDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bids",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BidDTO> updateBid(@Valid @RequestBody BidDTO bidDTO) throws URISyntaxException {
        log.debug("REST request to update Bid : {}", bidDTO);
        if (bidDTO.getId() == null) {
            return createBid(bidDTO);
        }
        Bid bid = bidMapper.bidDTOToBid(bidDTO);
        bid = bidRepository.save(bid);
        BidDTO result = bidMapper.bidToBidDTO(bid);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bid", bidDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bids : get all the bids.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bids in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bids",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BidDTO>> getAllBids(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bids");
        Page<Bid> page = bidRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bids");
        return new ResponseEntity<>(bidMapper.bidsToBidDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /bids/:id : get the "id" bid.
     *
     * @param id the id of the bidDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bidDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bids/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BidDTO> getBid(@PathVariable Long id) {
        log.debug("REST request to get Bid : {}", id);
        Bid bid = bidRepository.findOne(id);
        BidDTO bidDTO = bidMapper.bidToBidDTO(bid);
        return Optional.ofNullable(bidDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bids/:id : delete the "id" bid.
     *
     * @param id the id of the bidDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bids/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        log.debug("REST request to delete Bid : {}", id);
        bidRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bid", id.toString())).build();
    }

}
