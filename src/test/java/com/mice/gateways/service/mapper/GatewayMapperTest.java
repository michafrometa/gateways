package com.mice.gateways.service.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GatewayMapperTest {

    private GatewayMapper gatewayMapper;

    @BeforeEach
    public void setUp() {
        gatewayMapper = new GatewayMapperImpl();
    }

    @Test
    @DisplayName("Mapped entity's Id should be Equal to given Id")
    void mappedEntityIdShouldBeEqualToGivenId() {
        Long id = 1L;
        Assertions.assertEquals(gatewayMapper.fromId(id).getId(), id);
        Assertions.assertNull(gatewayMapper.fromId(null));
    }
}
