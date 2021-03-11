package com.zup.proposal.financialproposal.common.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsBase64Validator.class)
public @interface IsBase64 {

    String message() default "Campo precisa ser no formato base64";
    Class<? extends Payload>[] payload() default {};
    Class[] groups() default {};
}
