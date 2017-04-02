package com.enjoying.auction.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Bid.
 */
@Entity
@Table(name = "bid")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "successful")
    private Boolean successful;

    @NotNull
    @ManyToOne
    private Bidder bidder;

    @NotNull
    @ManyToOne
    private AuctionItem auctionItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public Bid price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isSuccessful() {
        return successful;
    }

    public Bid successful(Boolean successful) {
        this.successful = successful;
        return this;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public Bid bidder(Bidder bidder) {
        this.bidder = bidder;
        return this;
    }

    public void setBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

    public Bid auctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
        return this;
    }

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bid bid = (Bid) o;
        if(bid.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bid.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bid{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", successful='" + successful + "'" +
            '}';
    }
}
