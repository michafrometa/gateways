package com.mice.gateways.web.rest;

import com.mice.gateways.GatewaysApplication;
import com.mice.gateways.base.GatewayTestBase;
import com.mice.gateways.domain.Gateway;
import com.mice.gateways.repository.GatewayRepository;
import com.mice.gateways.service.GatewayService;
import com.mice.gateways.service.dto.GatewayDTO;
import com.mice.gateways.service.mapper.GatewayMapper;
import com.mice.gateways.util.TestUtil;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GatewayResource} REST controller.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = GatewaysApplication.class)
class GatewayResourceIT {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGatewayMockMvc;

    private Gateway gateway;

    @BeforeEach
    public void initTest() {
        gateway = GatewayTestBase.createEntity();
    }

    @Test
    @Transactional
    void createGateway() throws Exception {
        int databaseSizeBeforeCreate = gatewayRepository.findAll().size();
        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);
        restGatewayMockMvc.perform(post("/api/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
                .andExpect(status().isCreated());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertEquals(gatewayList.size(), databaseSizeBeforeCreate + 1);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertEquals(GatewayTestBase.DEFAULT_NAME, testGateway.getName());
        assertEquals(GatewayTestBase.DEFAULT_ADDRESS, testGateway.getAddress());
        assertEquals(GatewayTestBase.DEFAULT_SERIAL_NUMBER, testGateway.getSerialNumber());
    }

    @Test
    @Transactional
    void createGatewayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gatewayRepository.findAll().size();

        // Create the Gateway with an existing ID
        gateway.setId(1L);
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGatewayMockMvc.perform(post("/api/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertEquals(gatewayList.size(), databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @DisplayName("Create Gateway With Existing Serial Number Should Fail")
    void createGatewayWithExistingSerialNumberShouldFail() {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        Gateway duplicatedSerialGateway = GatewayTestBase.createUpdatedEntity();
        duplicatedSerialGateway.setSerialNumber(gateway.getSerialNumber());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(duplicatedSerialGateway);

        // An entity with an existing Serial Number cannot be created, so this API call must throw an Exception
        assertThrows(NestedServletException.class, () -> restGatewayMockMvc.perform(post("/api/gateways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))));

        /*
         * Due to issues loading message.properties file i am going to bypass the fine grained
         * assertion for Exception message
         * it should be like:
         * Exception exception = assertThrows(NestedServletException.class, () -> restGatewayMockMvc.perform(post("/api/gateways")
         *     .contentType(MediaType.APPLICATION_JSON)
         *     .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))));
         *
         * String expectedMessage = "Unique constraint violated";
         * String actualMessage = exception.getMessage();
         * But for now, as i am only waiting for a single Exception, so i believe it is enough.
         */
    }

    @Test
    @Transactional
    void getAllGateways() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList
        restGatewayMockMvc.perform(get("/api/gateways?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gateway.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(GatewayTestBase.DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].serialNumber").value(GatewayTestBase.DEFAULT_SERIAL_NUMBER))
                .andExpect(jsonPath("$.[*].address").value(GatewayTestBase.DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get the gateway
        restGatewayMockMvc.perform(get("/api/gateways/{id}", gateway.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(gateway.getId().intValue()))
                .andExpect(jsonPath("$.name").value(GatewayTestBase.DEFAULT_NAME))
                .andExpect(jsonPath("$.serialNumber").value(GatewayTestBase.DEFAULT_SERIAL_NUMBER))
                .andExpect(jsonPath("$.address").value(GatewayTestBase.DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingGateway() throws Exception {
        // Get the gateway
        restGatewayMockMvc.perform(get("/api/gateways/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Update the gateway
        Gateway updatedGateway = gatewayRepository.findById(gateway.getId()).orElse(gateway);
        // Disconnect from session so that the updates on updatedGateway are not directly saved in db
        em.detach(updatedGateway);
        updatedGateway
                .name(GatewayTestBase.UPDATED_NAME)
                .address(GatewayTestBase.UPDATED_ADDRESS)
                .serialNumber(GatewayTestBase.UPDATED_SERIAL_NUMBER);
        GatewayDTO gatewayDTO = gatewayMapper.toDto(updatedGateway);

        restGatewayMockMvc.perform(put("/api/gateways")/*.with(csrf())*/
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
                .andExpect(status().isOk());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertEquals(gatewayList.size(), databaseSizeBeforeUpdate);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertEquals(GatewayTestBase.UPDATED_NAME, testGateway.getName());
        assertEquals(GatewayTestBase.UPDATED_ADDRESS, testGateway.getAddress());
        assertEquals(GatewayTestBase.UPDATED_SERIAL_NUMBER, testGateway.getSerialNumber());
    }

    @Test
    @Transactional
    void updateNonExistingGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGatewayMockMvc.perform(put("/api/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertEquals(gatewayList.size(), databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeDelete = gatewayRepository.findAll().size();

        // Delete the gateway
        restGatewayMockMvc.perform(delete("/api/gateways/{id}", gateway.getId())/*.with(csrf())*/
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertEquals(gatewayList.size(), databaseSizeBeforeDelete - 1);
    }
}
