package com.enjoying.auction.repository;

import com.enjoying.auction.domain.AuctionItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AuctionItem entity.
 */
@SuppressWarnings("unused")
public interface AuctionItemRepository extends JpaRepository<AuctionItem,Long> {

}
