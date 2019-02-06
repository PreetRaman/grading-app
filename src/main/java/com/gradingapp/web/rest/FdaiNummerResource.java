package com.gradingapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gradingapp.domain.FdaiNummer;
import com.gradingapp.domain.User;
import com.gradingapp.repository.FdaiNummerRepository;
import com.gradingapp.service.ActiveUsersService;
import com.gradingapp.service.UserService;
import com.gradingapp.service.dto.ActiveUsersDTO;
import com.gradingapp.web.rest.errors.BadRequestAlertException;
import com.gradingapp.web.rest.util.HeaderUtil;
import com.gradingapp.web.rest.util.ReadCSV;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FdaiNummer.
 */
@RestController
@RequestMapping("/api")
public class FdaiNummerResource {

    private final Logger log = LoggerFactory.getLogger(FdaiNummerResource.class);

    private static final String ENTITY_NAME = "fdaiNummer";

    private final FdaiNummerRepository fdaiNummerRepository;

    private final UserService userService;

    private final ActiveUsersService activeUsersService;

    public FdaiNummerResource(FdaiNummerRepository fdaiNummerRepository, UserService userService, ActiveUsersService activeUsersService) {
        this.fdaiNummerRepository = fdaiNummerRepository;
        this.userService = userService;
        this.activeUsersService = activeUsersService;
    }

    /**
     * POST  /fdai-nummers : Create a new fdaiNummer.
     *
     * @param fdaiNummer the fdaiNummer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fdaiNummer, or with status 400 (Bad Request) if the fdaiNummer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fdai-nummers")
    @Timed
    public ResponseEntity<FdaiNummer> createFdaiNummer(@RequestBody FdaiNummer fdaiNummer) throws URISyntaxException {
        log.debug("REST request to save FdaiNummer : {}", fdaiNummer);
        if (fdaiNummer.getId() != null) {
            throw new BadRequestAlertException("A new fdaiNummer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FdaiNummer result = fdaiNummerRepository.save(fdaiNummer);
        ActiveUsersDTO activeUsersDTO = new ActiveUsersDTO();
        activeUsersDTO.setUsername(fdaiNummer.getFdainumber());
        activeUsersDTO.setShould_ip_address(fdaiNummer.getIp());
        activeUsersService.save(activeUsersDTO);
        return ResponseEntity.created(new URI("/api/fdai-nummers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fdai-nummers : Updates an existing fdaiNummer.
     *
     * @param fdaiNummer the fdaiNummer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fdaiNummer,
     * or with status 400 (Bad Request) if the fdaiNummer is not valid,
     * or with status 500 (Internal Server Error) if the fdaiNummer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fdai-nummers")
    @Timed
    public ResponseEntity<FdaiNummer> updateFdaiNummer(@RequestBody FdaiNummer fdaiNummer) throws URISyntaxException {
        log.debug("REST request to update FdaiNummer : {}", fdaiNummer);
        if (fdaiNummer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FdaiNummer result = fdaiNummerRepository.save(fdaiNummer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fdaiNummer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fdai-nummers : get all the fdaiNummers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fdaiNummers in body
     */
    @GetMapping("/fdai-nummers")
    @Timed
    public List<FdaiNummer> getAllFdaiNummers() {
        log.debug("REST request to get all FdaiNummers");
        return fdaiNummerRepository.findAll();
    }

    /**
     * GET  /fdai-nummers/:id : get the "id" fdaiNummer.
     *
     * @param id the id of the fdaiNummer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fdaiNummer, or with status 404 (Not Found)
     */
    @GetMapping("/fdai-nummers/{id}")
    @Timed
    public ResponseEntity<FdaiNummer> getFdaiNummer(@PathVariable Long id) {
        log.debug("REST request to get FdaiNummer : {}", id);
        Optional<FdaiNummer> fdaiNummer = fdaiNummerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fdaiNummer);
    }

    /**
     * DELETE  /fdai-nummers/:id : delete the "id" fdaiNummer.
     *
     * @param id the id of the fdaiNummer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fdai-nummers/{id}")
    @Timed
    public ResponseEntity<Void> deleteFdaiNummer(@PathVariable Long id) {
        log.debug("REST request to delete FdaiNummer : {}", id);

        fdaiNummerRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /fdai-nummers : Create a new list of fdaiNummer.
     *
     * @param file the fdaiNummer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fdaiNummer, or with status 400 (Bad Request) if the fdaiNummer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Transactional
    @PostMapping("/fdai-nummers/import/{login}")
    @Timed
    public ResponseEntity<List<FdaiNummer>> createFdaiNummerList(@RequestBody MultipartFile file, @PathVariable String login) throws URISyntaxException, IOException {
        log.debug("REST request to save FdaiNummer : {}", file);
        if (file.isEmpty() || login.equals(null) || login.isEmpty()) {
            throw new BadRequestAlertException("A new fdaiNummer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<FdaiNummer> fdaiNummers = new ArrayList<>();

        // convert multipart file to file
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        // Read data from CSV file
        ReadCSV readCSV =  new ReadCSV();
        fdaiNummers = readCSV.loadObjectList(FdaiNummer.class, convFile);

        List<FdaiNummer> result = new ArrayList<>();

        // get user by login
        Optional<User> user = userService.getUserByLogin(login);
        if(user.isPresent()) {
            fdaiNummers.forEach(fdaiNummer -> {
                fdaiNummer.setUser(user.get());
                result.add(fdaiNummerRepository.save(fdaiNummer)) ;
            });
        }
        return ResponseEntity.created(new URI("/api/fdai-nummers/import" + result.size()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.size())))
            .body(result);
    }
}
