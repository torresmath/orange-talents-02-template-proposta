package com.zup.proposal.financialproposal.common.annotations;

import com.zup.proposal.financialproposal.common.Base64Utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsBase64Validator implements ConstraintValidator<IsBase64, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Base64Utils.isBase64(s);
    }
}
