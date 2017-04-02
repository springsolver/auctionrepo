package com.enjoying.auction.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AuctionItem entity.
 */
public class AuctionItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private ZonedDateTime startDate;

    @NotNull
    private ZonedDateTime closeDate;

    @NotNull
    private Double startPrice;

    private Double actualPrice;

    private Double soldPrice;


    private Long bidderId;
    
    private Long auctionItemStatusId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }
    public ZonedDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(ZonedDateTime closeDate) {
        this.closeDate = closeDate;
    }
    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }
    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }
    public Double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(Double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public Long getAuctionItemStatusId() {
        return auctionItemStatusId;
    }

    public void setAuctionItemStatusId(Long auctionItemStatusId) {
        this.auctionItemStatusId = auctionItemStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuctionItemDTO auctionItemDTO = (AuctionItemDTO) o;

        if ( ! Objects.equals(id, auctionItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuctionItemDTO{" +
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
