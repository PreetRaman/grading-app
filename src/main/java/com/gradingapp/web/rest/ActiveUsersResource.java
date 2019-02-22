package com.gradingapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gradingapp.domain.FdaiNummer;
import com.gradingapp.domain.User;
import com.gradingapp.repository.ActiveUsersRepository;
import com.gradingapp.repository.FdaiNummerRepository;
import com.gradingapp.security.SecurityUtils;
import com.gradingapp.service.ActiveUsersService;
import com.gradingapp.service.UserService;
import com.gradingapp.service.dto.UserDTO;
import com.gradingapp.web.rest.errors.BadRequestAlertException;
import com.gradingapp.web.rest.util.HeaderUtil;
import com.gradingapp.web.rest.util.PaginationUtil;
import com.gradingapp.service.dto.ActiveUsersDTO;
import com.gradingapp.service.dto.ActiveUsersCriteria;
import com.gradingapp.service.ActiveUsersQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ActiveUsers.
 */
@RestController
@RequestMapping("/api")
public class ActiveUsersResource {

    private final Logger log = LoggerFactory.getLogger(ActiveUsersResource.class);

    private static final String ENTITY_NAME = "activeUsers";

    private final ActiveUsersService activeUsersService;

    private final ActiveUsersQueryService activeUsersQueryService;

    private final UserService userService;

    private FdaiNummerRepository fdaiNummerRepository;

    private ActiveUsersRepository activeUsersRepository;

    public ActiveUsersResource(ActiveUsersService activeUsersService, ActiveUsersQueryService activeUsersQueryService, UserService userService, FdaiNummerRepository fdaiNummerRepository, ActiveUsersRepository activeUsersRepository) {
        this.activeUsersService = activeUsersService;
        this.activeUsersQueryService = activeUsersQueryService;
        this.userService = userService;
        this.fdaiNummerRepository = fdaiNummerRepository;
        this.activeUsersRepository = activeUsersRepository;
    }

    /**
     * POST  /active-users : Create a new activeUsers.
     *
     * @param activeUsersDTO the activeUsersDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activeUsersDTO, or with status 400 (Bad Request) if the activeUsers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/active-users")
    @Timed
    public ResponseEntity<ActiveUsersDTO> createActiveUsers(@RequestBody ActiveUsersDTO activeUsersDTO) throws URISyntaxException {
        log.debug("REST request to save ActiveUsers : {}", activeUsersDTO);
        if (activeUsersDTO.getId() != null) {
            throw new BadRequestAlertException("A new activeUsers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActiveUsersDTO result = activeUsersService.save(activeUsersDTO);
        return ResponseEntity.created(new URI("/api/active-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /active-users : Updates an existing activeUsers.
     *
     * @param activeUsersDTO the activeUsersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activeUsersDTO,
     * or with status 400 (Bad Request) if the activeUsersDTO is not valid,
     * or with status 500 (Internal Server Error) if the activeUsersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/active-users")
    @Timed
    public ResponseEntity<ActiveUsersDTO> updateActiveUsers(@RequestBody ActiveUsersDTO activeUsersDTO) throws URISyntaxException {
        log.debug("REST request to update ActiveUsers : {}", activeUsersDTO);
        if (activeUsersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActiveUsersDTO result = activeUsersService.save(activeUsersDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activeUsersDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /active-users : get all the activeUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of activeUsers in body
     */
    @GetMapping("/active-users")
    @Timed
    public ResponseEntity<List<ActiveUsersDTO>> getAllActiveUsers(ActiveUsersCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ActiveUsers by criteria: {}", criteria);
        Page<ActiveUsersDTO> page = activeUsersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/active-users");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /active-users/count : count all the activeUsers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/active-users/count")
    @Timed
    public ResponseEntity<Long> countActiveUsers(ActiveUsersCriteria criteria) {
        log.debug("REST request to count ActiveUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(activeUsersQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /active-users/:id : get the "id" activeUsers.
     *
     * @param id the id of the activeUsersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activeUsersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/active-users/{id}")
    @Timed
    public ResponseEntity<ActiveUsersDTO> getActiveUsers(@PathVariable Long id) {
        log.debug("REST request to get ActiveUsers : {}", id);
        Optional<ActiveUsersDTO> activeUsersDTO = activeUsersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activeUsersDTO);
    }


    /**
     * GET  /active-users/:id : get the "id" activeUsers.
     *
     *  the name of the activeUsersDTO to retrieve
     *
     */
    @GetMapping("/active-users/logout")
    @Timed
    public void logoutActiveUsers() {
        log.debug("REST request to get ActiveUsers : {}");
        if (SecurityUtils.getCurrentUserLogin().isPresent())
        {
            String user = SecurityUtils.getCurrentUserLogin().get();
            Optional<ActiveUsersDTO> activeUsersDTO = activeUsersService.findActiveUserFromName(user);
            if (activeUsersDTO.isPresent()) {
                ActiveUsersDTO activeUsersDTO1 = activeUsersDTO.get();
                activeUsersDTO1.setLogout_time(Instant.now());
                activeUsersDTO1.setActive(false);
                activeUsersService.save(activeUsersDTO1);
            }
        }
    }

    /**
     * DELETE  /active-users/:id : delete the "id" activeUsers.
     *
     * @param id the id of the activeUsersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/active-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteActiveUsers(@PathVariable Long id) {
        log.debug("REST request to delete ActiveUsers : {}", id);
        activeUsersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETEALL  /active-users/:id : delete the "id" activeUsers.
     *
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/active-users")
    @Timed
    public ResponseEntity<Void> deleteAllActiveUsers() {
        log.debug("REST request to delete All ActiveUsers : {}");
        activeUsersRepository.deleteAll();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "all")).build();
    }

    /**
     * GET  /active-users/:id/ladmin get id of the students belonging to LADMIN
     *
     *  the name of the activeUsersDTO to retrieve
     *
     */
    @GetMapping("/active-users/ladmin")
    @Timed
    public List<ActiveUsersDTO> getLadminUsers(ActiveUsersCriteria criteria) {
        log.debug("REST request to get ActiveUsers by ladmin: {}", criteria);
        String user = SecurityUtils.getCurrentUserLogin().get();
        Optional<User> user1 = userService.getUserWithAuthoritiesByLogin(user);
        //current user id

        //get fdai_nummer list of all users with ladmin id
        List<FdaiNummer> fdaiNummers = fdaiNummerRepository.findAllByUser(user1.get());

        List<ActiveUsersDTO> list = new ArrayList<>();
        fdaiNummers.forEach(fdaiNummer ->  {
                List<ActiveUsersDTO> fdaiNummerlist = activeUsersService.findAllFromName(fdaiNummer.getFdainumber());
            fdaiNummerlist.forEach(fdaiNummer3 -> {
                    list.add(fdaiNummer3);
                });
        });

        return list;
    }


    @GetMapping("/active-users/notassigned/ladmin")
    @Timed
    public List<ActiveUsersDTO> getNotAssignedLadminUsers(ActiveUsersCriteria criteria) {
        log.debug("REST request to get ActiveUsers by ladmin: {}", criteria);

        List<ActiveUsersDTO> listNotAssigned = activeUsersService.getNotAssignedActiveUsersOfLadmin();

        return listNotAssigned;
    }

    @GetMapping("/active-users/ip/{ip}")
    @Timed
    public ResponseEntity<List<ActiveUsersDTO>> saveIPActiveUser(@PathVariable String ip) {
        log.debug("REST request to get ActiveUsers by criteria: {}");
        // get current user
        String user = SecurityUtils.getCurrentUserLogin().get();
        activeUsersService.saveIPaddressForUsername(user, ip);
        return ResponseEntity.ok().build();
    }
}
