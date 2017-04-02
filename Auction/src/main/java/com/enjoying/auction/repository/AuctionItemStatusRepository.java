package com.enjoying.auction.repository;

import com.enjoying.auction.domain.AuctionItemStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AuctionItemStatus entity.
 */
@SuppressWarnings("unused")
public interface AuctionItemStatusRepository extends JpaRepository<AuctionItemStatus,Long> {

}
