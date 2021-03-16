package com.zup.proposal.financialproposal.common.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    private List<String> validValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        validValues = Arrays.stream(constraintAnnotation.targetEnum().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return validValues.contains(s);
    }
}
