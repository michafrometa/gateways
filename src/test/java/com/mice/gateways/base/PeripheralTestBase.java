package com.mice.gateways.base;

import com.mice.gateways.domain.Peripheral;
import com.mice.gateways.domain.enumeration.Status;

public class PeripheralTestBase {

    public static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    public static final String UPDATED_VENDOR = "BBBBBBBBBB";

    public static final Status DEFAULT_STATUS = Status.ONLINE;
    public static final Status UPDATED_STATUS = Status.OFFLINE;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peripheral createEntity() {
        return new Peripheral()
                .vendor(DEFAULT_VENDOR)
                .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peripheral createUpdatedEntity() {
        return new Peripheral()
                .vendor(UPDATED_VENDOR)
                .status(UPDATED_STATUS);
    }
}
