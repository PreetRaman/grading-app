package com.gradingapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gradingapp.domain.FdaiNummer;
import com.gradingapp.domain.Submissions;
import com.gradingapp.domain.User;
import com.gradingapp.repository.FdaiNummerRepository;
import com.gradingapp.repository.SubmissionsRepository;
import com.gradingapp.repository.UserRepository;
import com.gradingapp.security.AuthoritiesConstants;
import com.gradingapp.security.SecurityUtils;
import com.gradingapp.service.UserService;
import com.gradingapp.service.util.WriteCsvToResponse;
import com.gradingapp.web.rest.errors.BadRequestAlertException;
import com.gradingapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Submissions.
 */
@RestController
@RequestMapping("/api")
public class SubmissionsResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionsResource.class);

    private static final String ENTITY_NAME = "submissions";

    private final SubmissionsRepository submissionsRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final FdaiNummerRepository fdaiNummerRepository;

    public SubmissionsResource(SubmissionsRepository submissionsRepository, UserRepository userRepository, UserService userService, FdaiNummerRepository fdaiNummerRepository) {
        this.submissionsRepository = submissionsRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.fdaiNummerRepository = fdaiNummerRepository;
    }

    /**
     * POST  /submissions : Create a new submissions.
     *
     * @param submissions the submissions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new submissions, or with status 400 (Bad Request) if the submissions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/submissions")
    @Timed
    public ResponseEntity<Submissions> createSubmissions(@RequestBody Submissions submissions) throws URISyntaxException {
        log.debug("REST request to save Submissions : {}", submissions);
        if (submissions.getId() != null) {
            throw new BadRequestAlertException("A new submissions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            String user = SecurityUtils.getCurrentUserLogin().get();
            if (userRepository.findOneByLogin(user).isPresent()) {
                User u1 = userRepository.findOneByLogin(user).get();
                submissions.setUser(u1);
            }
        }

        if (submissionsRepository.findByFdaiNumberAndCourseAndExercises(submissions.getFdaiNumber(), submissions.getCourse(),
            submissions.getExercises()).size() > 0) {
            throw new BadRequestAlertException("Already Submitted this exercise", ENTITY_NAME, "exerciseexists");
        }

        Submissions result = submissionsRepository.save(submissions);
        return ResponseEntity.created(new URI("/api/submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /submissions : Updates an existing submissions.
     *
     * @param submissions the submissions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated submissions,
     * or with status 400 (Bad Request) if the submissions is not valid,
     * or with status 500 (Internal Server Error) if the submissions couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/submissions")
    @Timed
    public ResponseEntity<Submissions> updateSubmissions(@RequestBody Submissions submissions) throws URISyntaxException {
        log.debug("REST request to update Submissions : {}", submissions);
        if (submissions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Submissions result = submissionsRepository.save(submissions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, submissions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /submissions : get all the submissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of submissions in body
     */
    @GetMapping("/submissions")
    @Timed
    public List<Submissions> getAllSubmissions() {
        log.debug("REST request to get all Submissions");
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.LADMIN)) {
            return submissionsRepository.findAll();
        } else
            return submissionsRepository.findByUserIsCurrentUser();
    }

    /**
     * GET  /submissions/:id : get the "id" submissions.
     *
     * @param id the id of the submissions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the submissions, or with status 404 (Not Found)
     */
    @GetMapping("/submissions/{id}")
    @Timed
    public ResponseEntity<Submissions> getSubmissions(@PathVariable Long id) {
        log.debug("REST request to get Submissions : {}", id);
        Optional<Submissions> submissions = submissionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(submissions);
    }

    /**
     * DELETE  /submissions/:id : delete the "id" submissions.
     *
     * @param id the id of the submissions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/submissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubmissions(@PathVariable Long id) {
        log.debug("REST request to delete Submissions : {}", id);

        submissionsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/submissions")
    @Timed
    public ResponseEntity<Void> deleteAllSubmissions() {
        log.debug("REST request to delete Submissions : {}");

        submissionsRepository.deleteAll();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "all")).build();
    }

    /**
     * Handle request to download a CSV document
     */
    @RequestMapping(value = "/submissions/download", method = RequestMethod.GET, produces = "text/csv")
    public void download(HttpServletResponse response) throws IOException {
        List<Submissions> submissionsList = submissionsRepository.findAll();

        response.setContentType("text/plain; charset=utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "download.csv" + "\"");

       WriteCsvToResponse.writeSubmissionList(response.getWriter(), submissionsList);
    }

    /*Handle Get request for submissions of particular LADMINS */

    @GetMapping("/submissions/ladmin")
    @Timed
    public List<Submissions> getAllSubmissionsforLadmin() {
        log.debug("REST request to get all Submissions for LADMINS");
        // get current user id
        String currentuser = SecurityUtils.getCurrentUserLogin().get();
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(currentuser);

        // get list of all fdainummber list with the ladmin id
        List<FdaiNummer> fdaiNummers = fdaiNummerRepository.findAllByUser(user.get());

        // create a new array list of submissions for that id
        List<Submissions> list = new ArrayList<>();
        fdaiNummers.forEach(fdaiNummer -> {
            List<Submissions> submissions1 = submissionsRepository.findAllByFdaiNumber(fdaiNummer.getFdainumber());
            submissions1.forEach(subm -> {
                list.add(subm);
            });
        });
        return list;
    }
}
