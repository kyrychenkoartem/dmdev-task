package com.artem.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE_USE, FIELD, LOCAL_VARIABLE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumPropertyValidator.class})
public @interface EnumProperty {

    Class<? extends Enum<?>> clazz();

    String message() default "Invalid {fieldName} value. Supported values: {acceptedValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
