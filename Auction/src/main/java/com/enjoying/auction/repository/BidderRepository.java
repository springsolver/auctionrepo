package com.enjoying.auction.repository;

import com.enjoying.auction.domain.Bidder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bidder entity.
 */
@SuppressWarnings("unused")
public interface BidderRepository extends JpaRepository<Bidder,Long> {

}
