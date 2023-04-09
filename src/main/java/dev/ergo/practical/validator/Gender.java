package dev.ergo.practical.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = GenderValidator.class)
public @interface Gender {

    // Error message
    String message() default "The gender value is not supported";
    // Represents group of constraints
    Class<?>[] groups() default {};
    // Represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
