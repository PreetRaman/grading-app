package com.gradingapp.repository;

import com.gradingapp.domain.FdaiNummer;
import com.gradingapp.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the FdaiNummer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FdaiNummerRepository extends JpaRepository<FdaiNummer, Long> {

    @Query("select fdai_nummer from FdaiNummer fdai_nummer where fdai_nummer.user.login = ?#{principal.username}")
    List<FdaiNummer> findByUserIsCurrentUser();

    List<FdaiNummer> findAllByUser(User user);

}
