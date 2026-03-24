package com.inspire.common.binding.validation;

import com.inspire.common.binding.Update;
import com.inspire.common.binding.validation.constraints.NotAbsent;
import jakarta.validation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;


public class ValidatorTest {

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    static class UpdateTestDTO {
        @NotAbsent
        private Update<@Min(1) Integer> t1 = Update.absent();
        @NotAbsent
        private Update<@NotBlank String> t2 = Update.absent();
        private Update<String> t3 = Update.absent();
    }

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void dto1Test() {
        // when
        UpdateTestDTO dto1 = new UpdateTestDTO();
        dto1.setT1(Update.absent());

        printInfo(dto1);
    }

    @Test
    void dto2Test() {
        UpdateTestDTO dto2 = new UpdateTestDTO();
        dto2.setT1(Update.present(0));

        printInfo(dto2);
    }

    @Test
    void dto3Test() {
        UpdateTestDTO dto3 = new UpdateTestDTO();
        dto3.setT2(Update.present(""));

        printInfo(dto3);
    }

    private void printInfo(UpdateTestDTO dto) {
        Set<ConstraintViolation<UpdateTestDTO>> constraintViolations = validator.validate(dto);
        for (ConstraintViolation<UpdateTestDTO> v : constraintViolations) {
            System.out.println("==== Violation ====");
            System.out.println("Property path: " + v.getPropertyPath());
            System.out.println("Invalid value: " + v.getInvalidValue());
            System.out.println("Message: " + v.getMessage());
            System.out.println("Constraint annotation: " + v.getConstraintDescriptor().getAnnotation());
        }
    }
}
