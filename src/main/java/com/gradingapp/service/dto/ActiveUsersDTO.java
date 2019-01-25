package com.gradingapp.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ActiveUsers entity.
 */
public class ActiveUsersDTO implements Serializable {

    private Long id;

    private String username;

    private Instant login_time;

    private Instant logout_time;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Instant login_time) {
        this.login_time = login_time;
    }

    public Instant getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(Instant logout_time) {
        this.logout_time = logout_time;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActiveUsersDTO activeUsersDTO = (ActiveUsersDTO) o;
        if (activeUsersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activeUsersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActiveUsersDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", login_time='" + getLogin_time() + "'" +
            ", logout_time='" + getLogout_time() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
