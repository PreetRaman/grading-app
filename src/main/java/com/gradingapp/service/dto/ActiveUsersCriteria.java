package com.gradingapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the ActiveUsers entity. This class is used in ActiveUsersResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /active-users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActiveUsersCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private InstantFilter login_time;

    private InstantFilter logout_time;

    private BooleanFilter active;

    private StringFilter is_ip_address;

    private StringFilter should_ip_address;

    public ActiveUsersCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUsername() {
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public InstantFilter getLogin_time() {
        return login_time;
    }

    public void setLogin_time(InstantFilter login_time) {
        this.login_time = login_time;
    }

    public InstantFilter getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(InstantFilter logout_time) {
        this.logout_time = logout_time;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public StringFilter getIs_ip_address() {
        return is_ip_address;
    }

    public void setIs_ip_address(StringFilter is_ip_address) {
        this.is_ip_address = is_ip_address;
    }

    public StringFilter getShould_ip_address() {
        return should_ip_address;
    }

    public void setShould_ip_address(StringFilter should_ip_address) {
        this.should_ip_address = should_ip_address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActiveUsersCriteria that = (ActiveUsersCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(login_time, that.login_time) &&
            Objects.equals(logout_time, that.logout_time) &&
            Objects.equals(active, that.active) &&
            Objects.equals(is_ip_address, that.is_ip_address) &&
            Objects.equals(should_ip_address, that.should_ip_address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        username,
        login_time,
        logout_time,
        active,
        is_ip_address,
        should_ip_address
        );
    }

    @Override
    public String toString() {
        return "ActiveUsersCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (login_time != null ? "login_time=" + login_time + ", " : "") +
                (logout_time != null ? "logout_time=" + logout_time + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (is_ip_address != null ? "is_ip_address=" + is_ip_address + ", " : "") +
                (should_ip_address != null ? "should_ip_address=" + should_ip_address + ", " : "") +
            "}";
    }

}
