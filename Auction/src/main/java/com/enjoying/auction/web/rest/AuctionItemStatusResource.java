package com.enjoying.auction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoying.auction.domain.AuctionItemStatus;

import com.enjoying.auction.repository.AuctionItemStatusRepository;
import com.enjoying.auction.web.rest.util.HeaderUtil;
import com.enjoying.auction.web.rest.util.PaginationUtil;
import com.enjoying.auction.service.dto.AuctionItemStatusDTO;
import com.enjoying.auction.service.mapper.AuctionItemStatusMapper;
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
 * REST controller for managing AuctionItemStatus.
 */
@RestController
@RequestMapping("/api")
public class AuctionItemStatusResource {

    private final Logger log = LoggerFactory.getLogger(AuctionItemStatusResource.class);
        
    @Inject
    private AuctionItemStatusRepository auctionItemStatusRepository;

    @Inject
    private AuctionItemStatusMapper auctionItemStatusMapper;

    /**
     * POST  /auction-item-statuses : Create a new auctionItemStatus.
     *
     * @param auctionItemStatusDTO the auctionItemStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auctionItemStatusDTO, or with status 400 (Bad Request) if the auctionItemStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/auction-item-statuses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuctionItemStatusDTO> createAuctionItemStatus(@Valid @RequestBody AuctionItemStatusDTO auctionItemStatusDTO) throws URISyntaxException {
        log.debug("REST request to save AuctionItemStatus : {}", auctionItemStatusDTO);
        if (auctionItemStatusDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auctionItemStatus", "idexists", "A new auctionItemStatus cannot already have an ID")).body(null);
        }
        AuctionItemStatus auctionItemStatus = auctionItemStatusMapper.auctionItemStatusDTOToAuctionItemStatus(auctionItemStatusDTO);
        auctionItemStatus = auctionItemStatusRepository.save(auctionItemStatus);
        AuctionItemStatusDTO result = auctionItemStatusMapper.auctionItemStatusToAuctionItemStatusDTO(auctionItemStatus);
        return ResponseEntity.created(new URI("/api/auction-item-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auctionItemStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auction-item-statuses : Updates an existing auctionItemStatus.
     *
     * @param auctionItemStatusDTO the auctionItemStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auctionItemStatusDTO,
     * or with status 400 (Bad Request) if the auctionItemStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the auctionItemStatusDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/auction-item-statuses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuctionItemStatusDTO> updateAuctionItemStatus(@Valid @RequestBody AuctionItemStatusDTO auctionItemStatusDTO) throws URISyntaxException {
        log.debug("REST request to update AuctionItemStatus : {}", auctionItemStatusDTO);
        if (auctionItemStatusDTO.getId() == null) {
            return createAuctionItemStatus(auctionItemStatusDTO);
        }
        AuctionItemStatus auctionItemStatus = auctionItemStatusMapper.auctionItemStatusDTOToAuctionItemStatus(auctionItemStatusDTO);
        auctionItemStatus = auctionItemStatusRepository.save(auctionItemStatus);
        AuctionItemStatusDTO result = auctionItemStatusMapper.auctionItemStatusToAuctionItemStatusDTO(auctionItemStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auctionItemStatus", auctionItemStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auction-item-statuses : get all the auctionItemStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of auctionItemStatuses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/auction-item-statuses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuctionItemStatusDTO>> getAllAuctionItemStatuses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AuctionItemStatuses");
        Page<AuctionItemStatus> page = auctionItemStatusRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auction-item-statuses");
        return new ResponseEntity<>(auctionItemStatusMapper.auctionItemStatusesToAuctionItemStatusDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /auction-item-statuses/:id : get the "id" auctionItemStatus.
     *
     * @param id the id of the auctionItemStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auctionItemStatusDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/auction-item-statuses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuctionItemStatusDTO> getAuctionItemStatus(@PathVariable Long id) {
        log.debug("REST request to get AuctionItemStatus : {}", id);
        AuctionItemStatus auctionItemStatus = auctionItemStatusRepository.findOne(id);
        AuctionItemStatusDTO auctionItemStatusDTO = auctionItemStatusMapper.auctionItemStatusToAuctionItemStatusDTO(auctionItemStatus);
        return Optional.ofNullable(auctionItemStatusDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /auction-item-statuses/:id : delete the "id" auctionItemStatus.
     *
     * @param id the id of the auctionItemStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/auction-item-statuses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuctionItemStatus(@PathVariable Long id) {
        log.debug("REST request to delete AuctionItemStatus : {}", id);
        auctionItemStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auctionItemStatus", id.toString())).build();
    }

}
