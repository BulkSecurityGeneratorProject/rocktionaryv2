package com.rocktionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rocktionary.domain.ComentarBanda;

import com.rocktionary.domain.User;
import com.rocktionary.repository.ComentarBandaRepository;
import com.rocktionary.repository.UserRepository;
import com.rocktionary.security.SecurityUtils;
import com.rocktionary.web.rest.errors.BadRequestAlertException;
import com.rocktionary.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ComentarBanda.
 */
@RestController
@RequestMapping("/api")
public class ComentarBandaResource {

    private final Logger log = LoggerFactory.getLogger(ComentarBandaResource.class);

    private static final String ENTITY_NAME = "comentarBanda";

    private final ComentarBandaRepository comentarBandaRepository;
    private final UserRepository userRepository;

    public ComentarBandaResource(ComentarBandaRepository comentarBandaRepository, UserRepository userRepository) {
        this.comentarBandaRepository = comentarBandaRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /comentar-bandas : Create a new comentarBanda.
     *
     * @param comentarBanda the comentarBanda to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comentarBanda, or with status 400 (Bad Request) if the comentarBanda has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comentar-bandas")
    @Timed
    public ResponseEntity<ComentarBanda> createComentarBanda(@Valid @RequestBody ComentarBanda comentarBanda) throws URISyntaxException {
        log.debug("REST request to save ComentarBanda : {}", comentarBanda);
        if (comentarBanda.getId() != null) {
            throw new BadRequestAlertException("A new comentarBanda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComentarBanda result = comentarBandaRepository.save(comentarBanda);
        return ResponseEntity.created(new URI("/api/comentar-bandas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/comentar-banda")
    public ComentarBanda comentarBanda (@RequestBody ComentarBandaDTO comentarBandaDTO) {

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
        ComentarBanda comentarBanda = new ComentarBanda(user, comentarBandaDTO.getComentario(), comentarBandaDTO.getBandaName(), ZonedDateTime.now());

        return comentarBandaRepository.save(comentarBanda);
    }

    @GetMapping("/get-banda-comments/{bandaName}")
    public List<ComentarBanda> getBandaComments (@PathVariable String bandaName) {
        return comentarBandaRepository.findByBandaName(bandaName);
    }


    @DeleteMapping("/delete-banda-comment/{id}")
    public void deleteComment (@PathVariable Long id) {
        comentarBandaRepository.delete(id);
    }

    /**
     * PUT  /comentar-bandas : Updates an existing comentarBanda.
     *
     * @param comentarBanda the comentarBanda to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comentarBanda,
     * or with status 400 (Bad Request) if the comentarBanda is not valid,
     * or with status 500 (Internal Server Error) if the comentarBanda couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comentar-bandas")
    @Timed
    public ResponseEntity<ComentarBanda> updateComentarBanda(@Valid @RequestBody ComentarBanda comentarBanda) throws URISyntaxException {
        log.debug("REST request to update ComentarBanda : {}", comentarBanda);
        if (comentarBanda.getId() == null) {
            return createComentarBanda(comentarBanda);
        }
        ComentarBanda result = comentarBandaRepository.save(comentarBanda);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, comentarBanda.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comentar-bandas : get all the comentarBandas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comentarBandas in body
     */
    @GetMapping("/comentar-bandas")
    @Timed
    public List<ComentarBanda> getAllComentarBandas() {
        log.debug("REST request to get all ComentarBandas");
        return comentarBandaRepository.findAll();
        }

    /**
     * GET  /comentar-bandas/:id : get the "id" comentarBanda.
     *
     * @param id the id of the comentarBanda to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comentarBanda, or with status 404 (Not Found)
     */
    @GetMapping("/comentar-bandas/{id}")
    @Timed
    public ResponseEntity<ComentarBanda> getComentarBanda(@PathVariable Long id) {
        log.debug("REST request to get ComentarBanda : {}", id);
        ComentarBanda comentarBanda = comentarBandaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(comentarBanda));
    }


    /**
     * DELETE  /comentar-bandas/:id : delete the "id" comentarBanda.
     *
     * @param id the id of the comentarBanda to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comentar-bandas/{id}")
    @Timed
    public ResponseEntity<Void> deleteComentarBanda(@PathVariable Long id) {
        log.debug("REST request to delete ComentarBanda : {}", id);
        comentarBandaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
