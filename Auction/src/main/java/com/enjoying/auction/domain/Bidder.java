package com.enjoying.auction.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Bidder.
 */
@Entity
@Table(name = "bidder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bidder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jmbg", nullable = false)
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

    public Bidder name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJmbg() {
        return jmbg;
    }

    public Bidder jmbg(String jmbg) {
        this.jmbg = jmbg;
        return this;
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
        Bidder bidder = (Bidder) o;
        if(bidder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bidder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bidder{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", jmbg='" + jmbg + "'" +
            '}';
    }
}
