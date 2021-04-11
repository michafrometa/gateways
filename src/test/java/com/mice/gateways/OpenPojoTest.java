package com.mice.gateways;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.filters.FilterChain;
import com.openpojo.reflection.filters.FilterNonConcrete;
import com.openpojo.reflection.filters.FilterSyntheticClasses;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.*;
import com.openpojo.validation.test.impl.BusinessIdentityTester;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * POJO Testing & Identity Management Made Trivial
 * https://github.com/OpenPojo/openpojo
 */
class OpenPojoTest {

    private static final String entitiesPackageName = "com.mice.gateways.domain";
    private static final String dtosPackageName = "com.mice.gateways.service.dto";

    private static Validator validator;

    private static final FilterChain filterChain =
            new FilterChain(new FilterSyntheticClasses(), new FilterNonConcrete(), new FilterTestClasses());

    @BeforeAll
    public static void setup() {

        validator = ValidatorBuilder.create()
                /*
                 * Create Rules to validate structure of the classes
                 */
                // Make sure we have a getter and setter
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())

                // We don't want any primitives in our Pojos.
                .with(new NoPrimitivesRule())

                // Pojo's should stay simple, don't allow nested classes, anonymous or declared.
                .with(new NoNestedClassRule())

                // Static fields must be final
                .with(new NoStaticExceptFinalRule())

                // Serializable must have serialVersionUID
                .with(new SerializableMustHaveSerialVersionUIDRule())

                // Don't shadow parent's field names.
                .with(new NoFieldShadowingRule())

                // What about public fields, use one of the following rules
                // allow them only if they are static and final.
                .with(new NoPublicFieldsExceptStaticFinalRule())

                // classes must have at least one business key
                .with(new BusinessKeyMustExistRule())

                // Or you can be more restrictive and not allow ANY public fields in a Pojo.
                // pojoValidator.addRule(new NoPublicFieldsRule());

                // Finally, what if you are testing your Testing code?
                // Make sure your tests are properly named
                .with(new TestClassMustBeProperlyNamedRule())

                /*
                 * Create Testers to validate the behavior of the classes at runtime.
                 */
                // Make sure our setters and getters are behaving as expected.
                .with(new SetterTester())
                .with(new GetterTester())

                // We don't want any default values to any fields - unless they are declared final or are primitive.
                .with(new DefaultValuesNullTester())

                // Equals, Hashcode and To String are behaving as expected.
                .with(new BusinessIdentityTester())

                /*
                 * finalize Validator building.
                 */
                .build();
    }

    @Test
    @DisplayName("Test All Pojos Setters, Getters, Equals, Hashcodes and so on")
    void testPojoStructureAndBehavior() {
        validator.validate(entitiesPackageName, filterChain);
        validator.validate(dtosPackageName, filterChain);
    }


    /**
     * Testing Only Production classes
     * https://github.com/OpenPojo/openpojo/wiki/Tutorial-8.-Testing-Only-Production-classes"
     */
    private static class FilterTestClasses implements PojoClassFilter {
        public boolean include(PojoClass pojoClass) {
            return !pojoClass.getSourcePath().contains("/test-classes/");
        }
    }
}
