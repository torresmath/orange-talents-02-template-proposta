package com.zup.proposal.financialproposal.common.annotations;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@CPF
@CNPJ
public @interface CPForCNPJ {

    String message() default "Precisa ser um CPF ou CNPJ válido";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
