package com.enjoying.auction.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AuctionItem.
 */
@Entity
@Table(name = "auction_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuctionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "close_date", nullable = false)
    private ZonedDateTime closeDate;

    @NotNull
    @Column(name = "start_price", nullable = false)
    private Double startPrice;

    @Column(name = "actual_price")
    private Double actualPrice;

    @Column(name = "sold_price")
    private Double soldPrice;

    @ManyToOne
    private Bidder bidder;

    @ManyToOne
    private AuctionItemStatus auctionItemStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AuctionItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public AuctionItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public AuctionItem startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getCloseDate() {
        return closeDate;
    }

    public AuctionItem closeDate(ZonedDateTime closeDate) {
        this.closeDate = closeDate;
        return this;
    }

    public void setCloseDate(ZonedDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public AuctionItem startPrice(Double startPrice) {
        this.startPrice = startPrice;
        return this;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public AuctionItem actualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
        return this;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Double getSoldPrice() {
        return soldPrice;
    }

    public AuctionItem soldPrice(Double soldPrice) {
        this.soldPrice = soldPrice;
        return this;
    }

    public void setSoldPrice(Double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public AuctionItem bidder(Bidder bidder) {
        this.bidder = bidder;
        return this;
    }

    public void setBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    public AuctionItemStatus getAuctionItemStatus() {
        return auctionItemStatus;
    }

    public AuctionItem auctionItemStatus(AuctionItemStatus auctionItemStatus) {
        this.auctionItemStatus = auctionItemStatus;
        return this;
    }

    public void setAuctionItemStatus(AuctionItemStatus auctionItemStatus) {
        this.auctionItemStatus = auctionItemStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuctionItem auctionItem = (AuctionItem) o;
        if(auctionItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, auctionItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuctionItem{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", startDate='" + startDate + "'" +
            ", closeDate='" + closeDate + "'" +
            ", startPrice='" + startPrice + "'" +
            ", actualPrice='" + actualPrice + "'" +
            ", soldPrice='" + soldPrice + "'" +
            '}';
    }
}
