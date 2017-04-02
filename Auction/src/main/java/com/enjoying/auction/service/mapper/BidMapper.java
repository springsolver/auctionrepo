package com.enjoying.auction.service.mapper;

import com.enjoying.auction.domain.*;
import com.enjoying.auction.service.dto.BidDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bid and its DTO BidDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BidMapper {

    @Mapping(source = "bidder.id", target = "bidderId")
    @Mapping(source = "auctionItem.id", target = "auctionItemId")
    BidDTO bidToBidDTO(Bid bid);

    List<BidDTO> bidsToBidDTOs(List<Bid> bids);

    @Mapping(source = "bidderId", target = "bidder")
    @Mapping(source = "auctionItemId", target = "auctionItem")
    Bid bidDTOToBid(BidDTO bidDTO);

    List<Bid> bidDTOsToBids(List<BidDTO> bidDTOs);

    default Bidder bidderFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bidder bidder = new Bidder();
        bidder.setId(id);
        return bidder;
    }

    default AuctionItem auctionItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setId(id);
        return auctionItem;
    }
}
