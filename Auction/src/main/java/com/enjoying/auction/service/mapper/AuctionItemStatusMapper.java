package com.enjoying.auction.service.mapper;

import com.enjoying.auction.domain.*;
import com.enjoying.auction.service.dto.AuctionItemStatusDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AuctionItemStatus and its DTO AuctionItemStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuctionItemStatusMapper {

    AuctionItemStatusDTO auctionItemStatusToAuctionItemStatusDTO(AuctionItemStatus auctionItemStatus);

    List<AuctionItemStatusDTO> auctionItemStatusesToAuctionItemStatusDTOs(List<AuctionItemStatus> auctionItemStatuses);

    AuctionItemStatus auctionItemStatusDTOToAuctionItemStatus(AuctionItemStatusDTO auctionItemStatusDTO);

    List<AuctionItemStatus> auctionItemStatusDTOsToAuctionItemStatuses(List<AuctionItemStatusDTO> auctionItemStatusDTOs);
}
