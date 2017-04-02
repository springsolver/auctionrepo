package com.enjoying.auction.service.mapper;

import com.enjoying.auction.domain.*;
import com.enjoying.auction.service.dto.BidderDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bidder and its DTO BidderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BidderMapper {

    BidderDTO bidderToBidderDTO(Bidder bidder);

    List<BidderDTO> biddersToBidderDTOs(List<Bidder> bidders);

    Bidder bidderDTOToBidder(BidderDTO bidderDTO);

    List<Bidder> bidderDTOsToBidders(List<BidderDTO> bidderDTOs);
}
