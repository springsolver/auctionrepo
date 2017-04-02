package com.enjoying.auction.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the AuctionItemStatus entity.
 */
public class AuctionItemStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuctionItemStatusDTO auctionItemStatusDTO = (AuctionItemStatusDTO) o;

        if ( ! Objects.equals(id, auctionItemStatusDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuctionItemStatusDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
