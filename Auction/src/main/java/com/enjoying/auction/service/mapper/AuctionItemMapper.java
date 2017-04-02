package com.enjoying.auction.service.mapper;

import com.enjoying.auction.domain.*;
import com.enjoying.auction.service.dto.AuctionItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AuctionItem and its DTO AuctionItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuctionItemMapper {

    @Mapping(source = "bidder.id", target = "bidderId")
    @Mapping(source = "auctionItemStatus.id", target = "auctionItemStatusId")
    AuctionItemDTO auctionItemToAuctionItemDTO(AuctionItem auctionItem);

    List<AuctionItemDTO> auctionItemsToAuctionItemDTOs(List<AuctionItem> auctionItems);

    @Mapping(source = "bidderId", target = "bidder")
    @Mapping(source = "auctionItemStatusId", target = "auctionItemStatus")
    AuctionItem auctionItemDTOToAuctionItem(AuctionItemDTO auctionItemDTO);

    List<AuctionItem> auctionItemDTOsToAuctionItems(List<AuctionItemDTO> auctionItemDTOs);

    default Bidder bidderFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bidder bidder = new Bidder();
        bidder.setId(id);
        return bidder;
    }

    default AuctionItemStatus auctionItemStatusFromId(Long id) {
        if (id == null) {
            return null;
        }
        AuctionItemStatus auctionItemStatus = new AuctionItemStatus();
        auctionItemStatus.setId(id);
        return auctionItemStatus;
    }
}
