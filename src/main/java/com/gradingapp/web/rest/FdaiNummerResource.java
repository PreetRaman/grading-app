package com.gradingapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gradingapp.domain.FdaiNummer;
import com.gradingapp.repository.FdaiNummerRepository;
import com.gradingapp.web.rest.errors.BadRequestAlertException;
import com.gradingapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

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

    public FdaiNummerResource(FdaiNummerRepository fdaiNummerRepository) {
        this.fdaiNummerRepository = fdaiNummerRepository;
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
}
