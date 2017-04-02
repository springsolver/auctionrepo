package com.enjoying.auction.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Bid entity.
 */
public class BidDTO implements Serializable {

    private Long id;

    @NotNull
    private Double price;

    private Boolean successful;


    private Long bidderId;
    
    private Long auctionItemId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public Long getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(Long auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BidDTO bidDTO = (BidDTO) o;

        if ( ! Objects.equals(id, bidDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BidDTO{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", successful='" + successful + "'" +
            '}';
    }
}
