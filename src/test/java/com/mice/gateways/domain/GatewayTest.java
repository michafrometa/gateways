package com.mice.gateways.domain;

import com.mice.gateways.base.GatewayTestBase;
import org.junit.jupiter.api.*;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

class GatewayTest {

    private static Validator validator;

    private Gateway gateway;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @BeforeEach
    public void initTest() {
        gateway = GatewayTestBase.createEntity();
    }

    @Test
    @DisplayName("Invalid Address Should Fail Validation")
    void invalidAddressShouldFailValidation() {

        List<String> expectation = new ArrayList<>();
        expectation.add(GatewayTestBase.ADDRESS_INVALID_SEPARATORS);
        expectation.add(GatewayTestBase.ADDRESS_INVALID_LENGTH);
        expectation.add(GatewayTestBase.ADDRESS_INVALID_CONTAINS_LETTERS);
        expectation.add(GatewayTestBase.ADDRESS_INVALID_OVER_255_RANGE);

        expectation.forEach(ex -> {
            gateway.setAddress(ex);
            Assertions.assertEquals(1, validator.validate(gateway).size());
        });
    }
}
