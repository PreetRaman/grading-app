package com.gradingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A FdaiNummer.
 */
@Entity
@Table(name = "fdai_nummer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FdaiNummer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fdainumber")
    private String fdainumber;

    @Column(name = "ip")
    private String ip;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFdainumber() {
        return fdainumber;
    }

    public FdaiNummer fdainumber(String fdainumber) {
        this.fdainumber = fdainumber;
        return this;
    }

    public void setFdainumber(String fdainumber) {
        this.fdainumber = fdainumber;
    }

    public String getIp() {
        return ip;
    }

    public FdaiNummer ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getUser() {
        return user;
    }

    public FdaiNummer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FdaiNummer fdaiNummer = (FdaiNummer) o;
        if (fdaiNummer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fdaiNummer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FdaiNummer{" +
            "id=" + getId() +
            ", fdainumber='" + getFdainumber() + "'" +
            ", ip='" + getIp() + "'" +
            "}";
    }
}
