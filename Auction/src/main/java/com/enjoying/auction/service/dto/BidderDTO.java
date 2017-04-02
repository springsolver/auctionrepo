package com.enjoying.auction.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Bidder entity.
 */
public class BidderDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String jmbg;


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
    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BidderDTO bidderDTO = (BidderDTO) o;

        if ( ! Objects.equals(id, bidderDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BidderDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", jmbg='" + jmbg + "'" +
            '}';
    }
}
