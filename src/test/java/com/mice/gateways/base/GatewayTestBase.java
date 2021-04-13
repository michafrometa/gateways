package com.mice.gateways.base;

import com.mice.gateways.domain.Gateway;

public class GatewayTestBase {

    public static final String DEFAULT_SERIAL_NUMBER = "123123";
    public static final String UPDATED_SERIAL_NUMBER = "456456";

    public static final String DEFAULT_NAME = "AAAAAAAAAA";
    public static final String UPDATED_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_ADDRESS = "12.12.12.12";
    public static final String UPDATED_ADDRESS = "13.13.13.13";

    public static final String ADDRESS_INVALID_CONTAINS_LETTERS = "ERROR.8.8.9";
    public static final String ADDRESS_INVALID_OVER_255_RANGE = "256.256.256.256";
    public static final String ADDRESS_INVALID_LENGTH = "256.256.256";
    public static final String ADDRESS_INVALID_SEPARATORS = "25,25,25;60";

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gateway createEntity() {
        return new Gateway()
                .name(DEFAULT_NAME)
                //.serialNumber(DEFAULT_SERIAL_NUMBER)
                //.serialNumber(UUID.randomUUID().toString())
                .address(DEFAULT_ADDRESS);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gateway createUpdatedEntity() {
        return new Gateway()
                .name(UPDATED_NAME)
                //.serialNumber(UPDATED_SERIAL_NUMBER)
                .address(UPDATED_ADDRESS);
    }
}
