package com.gradingapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A ActiveUsers.
 */
@Entity
@Table(name = "active_users")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActiveUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "login_time")
    private Instant login_time;

    @Column(name = "logout_time")
    private Instant logout_time;

    @Column(name = "active")
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public ActiveUsers username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getLogin_time() {
        return login_time;
    }

    public ActiveUsers login_time(Instant login_time) {
        this.login_time = login_time;
        return this;
    }

    public void setLogin_time(Instant login_time) {
        this.login_time = login_time;
    }

    public Instant getLogout_time() {
        return logout_time;
    }

    public ActiveUsers logout_time(Instant logout_time) {
        this.logout_time = logout_time;
        return this;
    }

    public void setLogout_time(Instant logout_time) {
        this.logout_time = logout_time;
    }

    public Boolean isActive() {
        return active;
    }

    public ActiveUsers active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        ActiveUsers activeUsers = (ActiveUsers) o;
        if (activeUsers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activeUsers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActiveUsers{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", login_time='" + getLogin_time() + "'" +
            ", logout_time='" + getLogout_time() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
