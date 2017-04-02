package com.enjoying.auction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoying.auction.domain.Bidder;

import com.enjoying.auction.repository.BidderRepository;
import com.enjoying.auction.web.rest.util.HeaderUtil;
import com.enjoying.auction.web.rest.util.PaginationUtil;
import com.enjoying.auction.service.dto.BidderDTO;
import com.enjoying.auction.service.mapper.BidderMapper;
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
 * REST controller for managing Bidder.
 */
@RestController
@RequestMapping("/api")
public class BidderResource {

    private final Logger log = LoggerFactory.getLogger(BidderResource.class);
        
    @Inject
    private BidderRepository bidderRepository;

    @Inject
    private BidderMapper bidderMapper;

    /**
     * POST  /bidders : Create a new bidder.
     *
     * @param bidderDTO the bidderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bidderDTO, or with status 400 (Bad Request) if the bidder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bidders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BidderDTO> createBidder(@Valid @RequestBody BidderDTO bidderDTO) throws URISyntaxException {
        log.debug("REST request to save Bidder : {}", bidderDTO);
        if (bidderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bidder", "idexists", "A new bidder cannot already have an ID")).body(null);
        }
        Bidder bidder = bidderMapper.bidderDTOToBidder(bidderDTO);
        bidder = bidderRepository.save(bidder);
        BidderDTO result = bidderMapper.bidderToBidderDTO(bidder);
        return ResponseEntity.created(new URI("/api/bidders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bidder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bidders : Updates an existing bidder.
     *
     * @param bidderDTO the bidderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bidderDTO,
     * or with status 400 (Bad Request) if the bidderDTO is not valid,
     * or with status 500 (Internal Server Error) if the bidderDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bidders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BidderDTO> updateBidder(@Valid @RequestBody BidderDTO bidderDTO) throws URISyntaxException {
        log.debug("REST request to update Bidder : {}", bidderDTO);
        if (bidderDTO.getId() == null) {
            return createBidder(bidderDTO);
        }
        Bidder bidder = bidderMapper.bidderDTOToBidder(bidderDTO);
        bidder = bidderRepository.save(bidder);
        BidderDTO result = bidderMapper.bidderToBidderDTO(bidder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bidder", bidderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bidders : get all the bidders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bidders in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bidders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BidderDTO>> getAllBidders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bidders");
        Page<Bidder> page = bidderRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bidders");
        return new ResponseEntity<>(bidderMapper.biddersToBidderDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /bidders/:id : get the "id" bidder.
     *
     * @param id the id of the bidderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bidderDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bidders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BidderDTO> getBidder(@PathVariable Long id) {
        log.debug("REST request to get Bidder : {}", id);
        Bidder bidder = bidderRepository.findOne(id);
        BidderDTO bidderDTO = bidderMapper.bidderToBidderDTO(bidder);
        return Optional.ofNullable(bidderDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bidders/:id : delete the "id" bidder.
     *
     * @param id the id of the bidderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bidders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBidder(@PathVariable Long id) {
        log.debug("REST request to delete Bidder : {}", id);
        bidderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bidder", id.toString())).build();
    }

}
