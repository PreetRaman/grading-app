package com.gradingapp.repository;

import com.gradingapp.domain.Submissions;
import com.gradingapp.domain.enumeration.Course;
import com.gradingapp.domain.enumeration.Exercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Submissions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Long> {

    @Query("select submissions from Submissions submissions where submissions.user.login = ?#{principal.username}")
    List<Submissions> findByUserIsCurrentUser();

    List<Submissions> findByFdaiNumberAndCourseAndExercises(String fdaiNumber, Course course, Exercise exercise);

    List<Submissions> findByFdaiNumber(String fdaiNumber);
}
