package com.beserra.teste.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.beserra.teste.domain.Continente;
import com.beserra.teste.repository.ContinenteRepository;
import com.beserra.teste.web.rest.errors.BadRequestAlertException;
import com.beserra.teste.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Continente.
 */
@RestController
@RequestMapping("/api")
public class ContinenteResource {

    private final Logger log = LoggerFactory.getLogger(ContinenteResource.class);

    private static final String ENTITY_NAME = "continente";

    private final ContinenteRepository continenteRepository;

    public ContinenteResource(ContinenteRepository continenteRepository) {
        this.continenteRepository = continenteRepository;
    }

    /**
     * POST  /continentes : Create a new continente.
     *
     * @param continente the continente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new continente, or with status 400 (Bad Request) if the continente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/continentes")
    @Timed
    public ResponseEntity<Continente> createContinente(@Valid @RequestBody Continente continente) throws URISyntaxException {
        log.debug("REST request to save Continente : {}", continente);
        if (continente.getId() != null) {
            throw new BadRequestAlertException("A new continente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Continente result = continenteRepository.save(continente);
        return ResponseEntity.created(new URI("/api/continentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /continentes : Updates an existing continente.
     *
     * @param continente the continente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated continente,
     * or with status 400 (Bad Request) if the continente is not valid,
     * or with status 500 (Internal Server Error) if the continente couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/continentes")
    @Timed
    public ResponseEntity<Continente> updateContinente(@Valid @RequestBody Continente continente) throws URISyntaxException {
        log.debug("REST request to update Continente : {}", continente);
        if (continente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Continente result = continenteRepository.save(continente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, continente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /continentes : get all the continentes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of continentes in body
     */
    @GetMapping("/continentes")
    @Timed
    public List<Continente> getAllContinentes() {
        log.debug("REST request to get all Continentes");
        return continenteRepository.findAll();
    }

    /**
     * GET  /continentes/:id : get the "id" continente.
     *
     * @param id the id of the continente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the continente, or with status 404 (Not Found)
     */
    @GetMapping("/continentes/{id}")
    @Timed
    public ResponseEntity<Continente> getContinente(@PathVariable Long id) {
        log.debug("REST request to get Continente : {}", id);
        Optional<Continente> continente = continenteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(continente);
    }

    /**
     * DELETE  /continentes/:id : delete the "id" continente.
     *
     * @param id the id of the continente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/continentes/{id}")
    @Timed
    public ResponseEntity<Void> deleteContinente(@PathVariable Long id) {
        log.debug("REST request to delete Continente : {}", id);

        continenteRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
