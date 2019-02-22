package com.gradingapp.repository;

import com.gradingapp.domain.ActiveUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ActiveUsers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiveUsersRepository extends JpaRepository<ActiveUsers, Long>, JpaSpecificationExecutor<ActiveUsers> {

    public Optional<ActiveUsers> findOneByUsernameAndActiveIsTrue(String username);

    public Optional<ActiveUsers> findOneByUsername(String username);

    List<ActiveUsers> findAllByUsername(String username);

    @Query("select activeUsers from ActiveUsers activeUsers where activeUsers.is_ip_address = :is_ip_address")
    List<ActiveUsers> findAllByIs_ip_address(@Param("is_ip_address") String is_ip_address);
}
