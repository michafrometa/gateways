package com.mice.gateways.web.rest;

import com.mice.gateways.service.GatewayService;
import com.mice.gateways.service.dto.GatewayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mice.gateways.domain.Gateway}.
 */
@RestController
@RequestMapping("/api/gateways")
public class GatewayResource {

    private static final String ENTITY_NAME = "gateway";

    private final Logger log = LoggerFactory.getLogger(GatewayResource.class);

    private final GatewayService gatewayService;

    /**
     * Instantiates a new Gateway resource.
     *
     * @param gatewayService the gateway service
     */
    public GatewayResource(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    /**
     * Create gateway response entity.
     *
     * @param gatewayDTO the gateway dto
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new GatewayDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<GatewayDTO> createGateway(@Valid @RequestBody GatewayDTO gatewayDTO) throws URISyntaxException {
        log.debug("REST request to save " + ENTITY_NAME + " : {}", gatewayDTO);
        if (gatewayDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new gateway cannot already have an ID");
        }
        GatewayDTO result = gatewayService.save(gatewayDTO);
        return ResponseEntity.created(new URI("/api/gateways/" + result.getId()))
                .body(result);
    }

    /**
     * Update gateway response entity.
     *
     * @param gatewayDTO the gateway dto
     * @return the response entity
     */
    @PutMapping
    public ResponseEntity<GatewayDTO> updateGateway(@Valid @RequestBody GatewayDTO gatewayDTO) {
        log.debug("REST request to update " + ENTITY_NAME + " : {}", gatewayDTO);
        if (gatewayDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        GatewayDTO result = gatewayService.save(gatewayDTO);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * Gets all gateways.
     *
     * @param pageable the pageable
     * @return the all gateways
     */
    @GetMapping
    public ResponseEntity<List<GatewayDTO>> getAllGateways(Pageable pageable) {
        log.debug("REST request to get a page of " + ENTITY_NAME);
        Page<GatewayDTO> page = gatewayService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /:id} : : get the "id" gateway.
     *
     * @param id the id of the GatewayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the GatewayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GatewayDTO> getGateway(@PathVariable Long id) {
        log.debug("REST request to get a " + ENTITY_NAME + " : {}", id);
        Optional<GatewayDTO> gatewayDTO = gatewayService.findOne(id);
        return gatewayDTO.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete gateway response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGateway(@PathVariable Long id) {
        log.debug("REST request to delete a " + ENTITY_NAME + " : {}", id);
        gatewayService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
