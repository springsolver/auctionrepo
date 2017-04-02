package com.enjoying.auction.repository;

import com.enjoying.auction.domain.Bid;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bid entity.
 */
@SuppressWarnings("unused")
public interface BidRepository extends JpaRepository<Bid,Long> {

}
