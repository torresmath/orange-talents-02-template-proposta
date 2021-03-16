package com.zup.proposal.financialproposal.common.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = EnumValueValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    Class<? extends Enum<?>> targetEnum();

    String message() default "Valor nao permitido. Consulte documentacao da API para valores mapeados";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
