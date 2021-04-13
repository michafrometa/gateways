package com.mice.gateways.service.mapper;

import com.mice.gateways.service.mapper.peripheral.PeripheralMapper;
import com.mice.gateways.service.mapper.peripheral.PeripheralMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class PeripheralMapperTest {

    private PeripheralMapper peripheralMapper;

    @BeforeEach
    public void setUp() {
        peripheralMapper = new PeripheralMapperImpl();
    }

    @Test
    @DisplayName("Mapped entity's Id should be Equal to given Id")
    void mappedEntityIdShouldBeEqualToGivenId() {
        UUID uuid = UUID.randomUUID();
        Assertions.assertEquals(peripheralMapper.fromId(uuid).getId(), uuid);
        Assertions.assertNull(peripheralMapper.fromId(null));
    }
}
