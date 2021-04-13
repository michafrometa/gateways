package com.mice.gateways.web.rest;

import com.mice.gateways.service.PeripheralService;
import com.mice.gateways.service.dto.peripheral.PeripheralDTO;
import com.mice.gateways.service.dto.peripheral.PeripheralOnlyGatewayDTO;
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
import java.util.UUID;

/**
 * REST controller for managing {@link com.mice.gateways.domain.Peripheral}.
 */
@RestController
@RequestMapping("/api/peripherals")
public class PeripheralResource {

    private static final String ENTITY_NAME = "peripheral";

    private final Logger log = LoggerFactory.getLogger(PeripheralResource.class);

    private final PeripheralService peripheralService;

    /**
     * Instantiates a new Peripheral resource.
     *
     * @param peripheralService the peripheral service
     */
    public PeripheralResource(PeripheralService peripheralService) {
        this.peripheralService = peripheralService;
    }

    /**
     * Create peripheral response entity.
     *
     * @param peripheralDTO the peripheral dto
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new PeripheralDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<PeripheralDTO> createPeripheral(@Valid @RequestBody PeripheralDTO peripheralDTO) throws URISyntaxException {
        log.debug("REST request to save " + ENTITY_NAME + " : {}", peripheralDTO);
        if (peripheralDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new peripheral cannot already have an ID");
        }
        PeripheralDTO result = peripheralService.save(peripheralDTO);
        return ResponseEntity.created(new URI("/api/peripherals/" + result.getId()))
                .body(result);
    }

    /**
     * Update peripheral response entity.
     *
     * @param peripheralDTO the peripheral dto
     * @return the response entity
     */
    @PutMapping
    public ResponseEntity<PeripheralDTO> updatePeripheral(@Valid @RequestBody PeripheralDTO peripheralDTO) {
        log.debug("REST request to update " + ENTITY_NAME + " : {}", peripheralDTO);
        if (peripheralDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        PeripheralDTO result = peripheralService.save(peripheralDTO);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * Gets all peripherals.
     *
     * @param pageable the pageable
     * @return the all peripherals
     */
    @GetMapping
    public ResponseEntity<List<PeripheralDTO>> getAllPeripherals(Pageable pageable) {
        log.debug("REST request to get a page of " + ENTITY_NAME);
        Page<PeripheralDTO> page = peripheralService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * Gets all peripherals.
     *
     * @param pageable the pageable
     * @return the all peripherals
     */
    @GetMapping(params = "gateway")
    public ResponseEntity<List<PeripheralDTO>> getPeripheralsByGateway(@RequestParam Long gateway, Pageable pageable) {
        log.debug("REST request to get a page of " + ENTITY_NAME);
        Page<PeripheralDTO> page = peripheralService.findByGateway(gateway, pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /:id} : : get the "id" peripheral.
     *
     * @param id the id of the PeripheralDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PeripheralDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PeripheralDTO> getPeripheral(@PathVariable UUID id) {
        log.debug("REST request to get a " + ENTITY_NAME + " : {}", id);
        Optional<PeripheralDTO> peripheralDTO = peripheralService.findOne(id);
        return peripheralDTO.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete peripheral response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeripheral(@PathVariable UUID id) {
        log.debug("REST request to delete a " + ENTITY_NAME + " : {}", id);
        peripheralService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete peripheral response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PatchMapping("/{id}/setGateway")
    public ResponseEntity<Object> setGateway(
            @Valid @RequestBody PeripheralOnlyGatewayDTO peripheralOnlyGatewayDTO,
            @PathVariable("id") UUID id) {
        return peripheralService.findOne(id)
                .map(peripheralDTO -> {
                    peripheralDTO.setGatewayId(peripheralOnlyGatewayDTO.getGatewayId());
                    return peripheralService.save(peripheralDTO);
                })
                .map(response -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
