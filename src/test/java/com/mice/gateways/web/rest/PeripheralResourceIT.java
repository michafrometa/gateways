package com.mice.gateways.web.rest;

import com.mice.gateways.GatewaysApplication;
import com.mice.gateways.base.GatewayTestBase;
import com.mice.gateways.base.PeripheralTestBase;
import com.mice.gateways.domain.Gateway;
import com.mice.gateways.domain.Peripheral;
import com.mice.gateways.domain.enumeration.Status;
import com.mice.gateways.repository.GatewayRepository;
import com.mice.gateways.repository.PeripheralRepository;
import com.mice.gateways.service.PeripheralService;
import com.mice.gateways.service.dto.peripheral.PeripheralDTO;
import com.mice.gateways.service.dto.peripheral.PeripheralOnlyGatewayDTO;
import com.mice.gateways.service.mapper.GatewayMapper;
import com.mice.gateways.service.mapper.peripheral.PeripheralMapper;
import com.mice.gateways.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PeripheralResource} REST controller.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
// @WithMockUser
@SpringBootTest(classes = GatewaysApplication.class)
class PeripheralResourceIT {

    @Autowired
    private PeripheralRepository peripheralRepository;

    @Autowired
    private PeripheralService peripheralService;

    @Autowired
    private PeripheralMapper peripheralMapper;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeripheralMockMvc;

    private Peripheral peripheral;

    @BeforeEach
    void initTest() {
        peripheral = PeripheralTestBase.createEntity();
    }

    @Test
    @Transactional
    void createPeripheral() throws Exception {
        int databaseSizeBeforeCreate = peripheralRepository.findAll().size();
        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);
        restPeripheralMockMvc.perform(post("/api/peripherals")/*.with(csrf())*/
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
                .andExpect(status().isCreated());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertEquals(peripheralList.size(), databaseSizeBeforeCreate + 1);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertEquals(PeripheralTestBase.DEFAULT_VENDOR, testPeripheral.getVendor());
        assertEquals(PeripheralTestBase.DEFAULT_STATUS, testPeripheral.getStatus());

        // Validate Created Date Automatically created
        assertNotNull(testPeripheral.getCreatedDate());
    }

    @Test
    @Transactional
    void createPeripheralWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peripheralRepository.findAll().size();

        // Create the Peripheral with an existing ID
        peripheral.setId(UUID.randomUUID());
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeripheralMockMvc.perform(post("/api/peripherals")/*.with(csrf())*/
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertEquals(peripheralList.size(), databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeripherals() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList
        restPeripheralMockMvc.perform(get("/api/peripherals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(peripheral.getId().toString()/*.intValue()*/)))
                .andExpect(jsonPath("$.[*].vendor").value(hasItem(PeripheralTestBase.DEFAULT_VENDOR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(PeripheralTestBase.DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get the peripheral
        restPeripheralMockMvc.perform(get("/api/peripherals/{id}", peripheral.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(peripheral.getId().toString()))
                .andExpect(jsonPath("$.vendor").value(PeripheralTestBase.DEFAULT_VENDOR))
                .andExpect(jsonPath("$.status").value(PeripheralTestBase.DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPeripheral() throws Exception {
        // Get the peripheral
        restPeripheralMockMvc.perform(get("/api/peripherals/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updatePeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Update the peripheral
        Peripheral updatedPeripheral = peripheralRepository.findById(peripheral.getId()).orElse(peripheral);
        // Disconnect from session so that the updates on updatedPeripheral are not directly saved in db
        em.detach(updatedPeripheral);
        updatedPeripheral
                .vendor(PeripheralTestBase.UPDATED_VENDOR)
                .status(PeripheralTestBase.UPDATED_STATUS);
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(updatedPeripheral);

        restPeripheralMockMvc.perform(put("/api/peripherals")/*.with(csrf())*/
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
                .andExpect(status().isOk());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertEquals(peripheralList.size(), databaseSizeBeforeUpdate);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertEquals(PeripheralTestBase.UPDATED_STATUS, testPeripheral.getStatus());
        assertEquals(PeripheralTestBase.UPDATED_VENDOR, testPeripheral.getVendor());
    }

    @Test
    @Transactional
    void updateNonExistingPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeripheralMockMvc.perform(put("/api/peripherals")/*.with(csrf())*/
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertEquals(peripheralList.size(), databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeDelete = peripheralRepository.findAll().size();

        // Delete the peripheral
        restPeripheralMockMvc.perform(delete("/api/peripherals/{id}", peripheral.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertEquals(peripheralList.size(), databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void shouldPersistCategoryEnumConvertedValue() {

        peripheral.setStatus(Status.ONLINE);

        peripheralRepository.saveAndFlush(peripheral);

        Peripheral persistedPeripheral = peripheralRepository.findById(peripheral.getId()).orElseThrow(EntityNotFoundException::new);

        Assertions.assertEquals(Status.ONLINE, persistedPeripheral.getStatus());
    }

    @Test
    @DisplayName("Test Detach from Gateway")
    void testDetachFromGateway() throws Exception {

        // Create the Gateway
        Gateway gateway = GatewayTestBase.createEntity();

        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);
        gatewayRepository.saveAndFlush(gateway);

        final PageRequest pageable = PageRequest.of(0, 10);

        long databaseSizeBeforeCreate = peripheralRepository.findByGatewayId(gateway.getId(), pageable).getTotalElements();

        peripheral.setGateway(gateway);

        PeripheralOnlyGatewayDTO peripheralDTO = peripheralMapper.toPeripheralOnlyGatewayDTO(peripheral);

        restPeripheralMockMvc.perform(patch("/api/peripherals/{id}/setGateway", peripheralDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
                .andExpect(status().isNoContent());

        // Validate the Gateway in the database
        Page<Peripheral> gatewayPeripherals = peripheralRepository.findByGatewayId(gateway.getId(), pageable);
        assertEquals(gatewayPeripherals.getTotalElements(), databaseSizeBeforeCreate + 1);
    }
}
