package dev.ergo.practical.validator;

import dev.ergo.practical.common.constants.GenderConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderValidator implements ConstraintValidator<Gender, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(GenderConstants.values()).map(Enum::name).toList().contains(value);
    }
}
